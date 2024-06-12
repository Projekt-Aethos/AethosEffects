package de.aethosprojekt.aethoseffects.entity;

import de.aethos.lib.option.Option;
import de.aethosprojekt.aethoseffects.AethosEffects;
import de.aethosprojekt.aethoseffects.api.EffectType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;


record ActiveEffect(@NotNull EffectType type, long remainingTicks) {

    public @NotNull EffectType getType() {
        return type;
    }

    public Option<ActiveEffect> tick(@NotNull EffectEntityImpl<?> entity) {
        if (remainingTicks % type.getTicksBetween() == 0) {
            type.tick(entity);
        }
        final long next = remainingTicks - 1;
        if (next == 0) {
            return Option.none();
        }
        return Option.some(new ActiveEffect(type, next));
    }

    public long getRemainingTicks() {
        return remainingTicks;
    }

    public ActiveEffect changeRemainingTicks(long ticks) {
        return new ActiveEffect(type, ticks);
    }

    @Override
    public String toString() {
        return "ActiveEffect{" +
                "type=" + type +
                ", remainingTicks=" + remainingTicks +
                ", hash=" + hashCode() +
                '}';
    }

    static class DataType implements PersistentDataType<PersistentDataContainer, ActiveEffect> {
        private static final @NotNull NamespacedKey REMAINING_TICKS_KEY = new NamespacedKey(AethosEffects.getProvidingPlugin(AethosEffects.class), "REMAINING_TICKS");

        @Override
        public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
            return PersistentDataContainer.class;
        }

        @Override
        public @NotNull Class<ActiveEffect> getComplexType() {
            return ActiveEffect.class;
        }

        @Override
        public @NotNull PersistentDataContainer toPrimitive(@NotNull ActiveEffect activeEffect, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            final PersistentDataContainer container = persistentDataAdapterContext.newPersistentDataContainer();
            container.set(REMAINING_TICKS_KEY, PersistentDataType.LONG, activeEffect.getRemainingTicks());
            container.set(EffectType.DataType.EFFECT_TYPE, new EffectType.DataType(), activeEffect.getType());
            return container;
        }

        @Override
        public @NotNull ActiveEffect fromPrimitive(@NotNull PersistentDataContainer container, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
            final long i = container.get(REMAINING_TICKS_KEY, PersistentDataType.LONG).longValue();
            final EffectType type = container.get(EffectType.DataType.EFFECT_TYPE, new EffectType.DataType());
            return new ActiveEffect(type, i);
        }
    }
}
