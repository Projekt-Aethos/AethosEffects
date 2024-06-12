package de.aethosprojekt.aethoseffects.events;

import de.aethosprojekt.aethoseffects.api.EffectEntity;
import de.aethosprojekt.aethoseffects.api.EffectType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class EffectHealEvent extends EntityEvent implements Cancellable {
    private static final HandlerList list = new HandlerList();
    private final EffectType type;
    private final EffectEntity<? extends LivingEntity> entity;
    private double amount;
    private boolean cancelled = false;

    public EffectHealEvent(EffectEntity<? extends LivingEntity> entity, EffectType type, double amount) {
        super(entity.entity());
        this.type = type;
        this.entity = entity;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @NotNull

    public EffectEntity<? extends LivingEntity> getEffectEntity() {
        return entity;
    }

    public EffectType getEffectType() {
        return type;
    }
}
