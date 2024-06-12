package de.aethosprojekt.aethoseffects.api;

import de.aethosprojekt.aethoseffects.entity.EffectEntityImpl;
import de.aethosprojekt.aethoseffects.events.EffectDamageEvent;
import de.aethosprojekt.aethoseffects.events.EffectHealEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

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
        EffectDamageEvent event = new EffectDamageEvent(this, type, amount);
        if (event.callEvent()) {
            entity().damage(event.getFinalDamage());
        }
    }

    default void heal(EffectType type, double amount) {
        if (new EffectHealEvent(this, type, amount).callEvent()) {
            entity().setHealth(Math.max(entity().getHealth() + amount, entity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        }
    }

}
