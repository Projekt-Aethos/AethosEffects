package de.aethosprojekt.aethoseffects.events;

import de.aethosprojekt.aethoseffects.api.EffectEntity;
import de.aethosprojekt.aethoseffects.api.EffectType;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EffectDamageEvent extends EntityDamageEvent implements Cancellable {

    private static final HandlerList list = new HandlerList();

    private final EffectType type;

    private final EffectEntity<? extends LivingEntity> entity;

    private boolean cancelled = false;

    public EffectDamageEvent(EffectEntity<? extends LivingEntity> entity, EffectType type, double damage) {
        super(entity.entity(), EntityDamageEvent.DamageCause.CUSTOM, DamageSource.builder(DamageType.MAGIC).build(), damage);
        this.entity = entity;
        this.type = type;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return list;
    }


    public EffectType getEffectType() {
        return type;
    }

    @NotNull
    public EffectEntity<? extends LivingEntity> getEffectEntity() {
        return entity;
    }
}
