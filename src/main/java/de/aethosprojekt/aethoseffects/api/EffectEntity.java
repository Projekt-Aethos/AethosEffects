package de.aethosprojekt.aethoseffects.api;

import de.aethosprojekt.aethoseffects.entity.EffectEntityImpl;
import de.aethosprojekt.aethoseffects.events.EffectDamageEvent;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EffectEntity<E extends LivingEntity> {
    static <E extends LivingEntity> EffectEntity<E> of(E entity) {
        return new EffectEntityImpl<>(entity);
    }

    void removeEffect(EffectType type);

    void addEffect(EffectType type, long ticks);

    List<EffectType> getEffectTypes();

    void clearEffects();

    E entity();

    default void damage(EffectType type, double amount) {
        if (new EffectDamageEvent(this, type, amount).callEvent()) {
            entity().damage(amount);
        }
    }


    default void damage(EffectType type, double amount, @Nullable Entity source) {
        if (new EffectDamageEvent(this, type, amount).callEvent()) {
            entity().damage(amount, source);
        }
    }

    @ApiStatus.Experimental
    default void damage(EffectType type, double amount, @NotNull DamageSource damageSource) {
        if (new EffectDamageEvent(this, type, amount).callEvent()) {
            entity().damage(amount, damageSource);
        }
    }
}
