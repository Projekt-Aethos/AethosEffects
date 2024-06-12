package de.aethosprojekt.aethoseffects.entity;


import de.aethos.lib.option.Option;
import de.aethos.lib.option.Some;
import de.aethosprojekt.aethoseffects.AethosEffects;
import de.aethosprojekt.aethoseffects.api.EffectEntity;
import de.aethosprojekt.aethoseffects.api.EffectType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public record EffectEntityImpl<E extends LivingEntity>(@NotNull E entity) implements EffectEntity<E> {
    private static final @NotNull NamespacedKey EFFECT_SET_KEY = new NamespacedKey(AethosEffects.getProvidingPlugin(AethosEffects.class), "EFFECT_SET");
    private static final @NotNull NamespacedKey IS_TICKING_KEY = new NamespacedKey(AethosEffects.getProvidingPlugin(AethosEffects.class), "IS_TICKING");
    public static final @NotNull Listener EFFECT_LISTENER = new Listener() {
        @EventHandler
        public void onDeath(@NotNull EntityDeathEvent event) {
            final EffectEntityImpl<LivingEntity> entity = new EffectEntityImpl<>(event.getEntity());
            entity.clearEffects();
        }

        @EventHandler
        public void onJoin(@NotNull PlayerJoinEvent event) {
            final EffectEntityImpl<Player> entity = new EffectEntityImpl<>(event.getPlayer());

            if (entity.isTicking()) {
                final EntityTicker ticker = new EntityTicker(entity);
                event.getPlayer().getScheduler().runAtFixedRate(AethosEffects.getPlugin(AethosEffects.class), ticker, ticker::retired, 1, 1);
            }
        }
    };

    public long getRemainingTicks(EffectType type) {
        for (ActiveEffect effect : getEffects()) {
            if (effect.getType().equals(type)) {
                return effect.getRemainingTicks();
            }
        }
        return 0L;
    }

    public boolean isTicking() {
        Boolean bool = entity.getPersistentDataContainer().get(IS_TICKING_KEY, PersistentDataType.BOOLEAN);
        return bool != null && bool;
    }

    public void setTicking(boolean bool) {
        entity.getPersistentDataContainer().set(IS_TICKING_KEY, PersistentDataType.BOOLEAN, bool);
    }

    public void clearEffects() {
        setTicking(false);
        entity.getPersistentDataContainer().set(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new EffectType.DataType()), new ArrayList<>());
    }


    public @NotNull List<ActiveEffect> getEffects() {
        List<ActiveEffect> list = entity.getPersistentDataContainer().get(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()));
        if (list == null) {
            list = List.of();
        }
        return list;
    }


    @Override
    public void removeEffect(EffectType type) {
        List<ActiveEffect> list = getEffects();
        Stream<Option<ActiveEffect>> stream = list.stream().map(active -> active.getType().equals(type) ? Option.none() : Option.some(active));
        list = stream.flatMap(Option::stream).toList();
        if (list.isEmpty()) {
            this.setTicking(false);
        }
        entity.getPersistentDataContainer().set(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()), list);

    }

    @Override
    public void addEffect(@NotNull EffectType effect, long ticks) {
        List<ActiveEffect> list = entity.getPersistentDataContainer().get(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()));
        if (list == null) {
            list = List.of();
        }
        for (ActiveEffect active : list) {
            if (active.getType().equals(effect)) {
                list = list.stream().map(act -> act.getType().equals(effect) && act.getRemainingTicks() < ticks ? act.changeRemainingTicks(ticks) : act).toList();
                entity.getPersistentDataContainer().set(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()), list);
                return;
            }
        }
        list = new ArrayList<>(list);
        list.add(new ActiveEffect(effect, ticks));
        entity.getPersistentDataContainer().set(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()), list);
        EntityTicker ticker = new EntityTicker(this);
        entity.getScheduler().runAtFixedRate(AethosEffects.getProvidingPlugin(AethosEffects.class), ticker, ticker::retired, 1, 1);
    }

    public @NotNull List<EffectType> getEffectTypes() {
        List<EffectType> list = entity.getPersistentDataContainer().get(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new EffectType.DataType()));
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public boolean tick() {
        List<ActiveEffect> effects = entity.getPersistentDataContainer().get(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()));
        if (effects == null) {
            return true;
        }
        effects = effects.stream().map(effect -> effect.tick(this)).flatMap(Option::stream).toList();
        entity.getPersistentDataContainer().set(EFFECT_SET_KEY, PersistentDataType.LIST.listTypeFrom(new ActiveEffect.DataType()), effects);
        return effects.isEmpty();
    }

    public void display() {
        List<ActiveEffect> effects = getEffects();
        for (ActiveEffect effect : effects) {
            effect.getType().getDisplay().display(entity);
        }

        final Option<BossBar> option = getBossBar();
        if (option instanceof Some<BossBar> some) {
            BossBar bar = some.value();
            String title = BossBarSerializer.serialize(effects);
            bar.setTitle(title);
            bar.addPlayer((Player) entity);
            bar.setVisible(true);
        }


    }

    public Option<BossBar> getBossBar() {
        if (entity instanceof Player) {
            @NotNull NamespacedKey key = new NamespacedKey(AethosEffects.getPlugin(AethosEffects.class), entity.getName());
            KeyedBossBar bar = Bukkit.getBossBar(key);
            return Option.some(bar != null ? bar : Bukkit.createBossBar(key, "TEST", BarColor.BLUE, BarStyle.SOLID));
        }
        return Option.none();
    }
}
