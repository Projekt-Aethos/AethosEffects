package de.aethosprojekt.aethoseffects.api;

import de.aethosprojekt.aethoseffects.display.Display;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEffectType implements EffectType {
    private final NamespacedKey key;
    private final String name;
    private final char icon;
    private final int ticksBetween;
    private final Display display;

    protected AbstractEffectType(NamespacedKey key, String name, char icon, int ticksBetween, Display display) {
        this.key = key;
        this.name = name;
        this.icon = icon;
        this.ticksBetween = ticksBetween;
        this.display = display;
    }


    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public char getIcon() {
        return icon;
    }

    @Override
    public int getTicksBetween() {
        return ticksBetween;
    }

    @Override
    public @NotNull Display getDisplay() {
        return display;
    }
}
