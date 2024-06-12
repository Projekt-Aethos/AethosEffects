package de.aethosprojekt.aethoseffects.entity;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EntityTicker implements Consumer<ScheduledTask> {
    private final @NotNull EffectEntityImpl<?> entity;


    EntityTicker(@NotNull EffectEntityImpl<?> e) {
        this.entity = e;
        if (entity.isTicking()) {
            throw new RuntimeException("Cant tick an Entity that is already ticking");
        }
        this.entity.setTicking(true);
    }

    @Override
    public void accept(ScheduledTask scheduledTask) {
        if (entity.tick()) {
            entity.setTicking(false);
            scheduledTask.cancel();
        }
        entity.display();

    }

    public void retired() {
        entity.setTicking(false);
    }
}
