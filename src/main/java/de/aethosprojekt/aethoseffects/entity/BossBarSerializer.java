package de.aethosprojekt.aethoseffects.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BossBarSerializer {


    private static final String MAGIC_STRING = "ꈁꈇꈅꈄꈁ";

    public static @NotNull String serialize(ActiveEffect effect) {
        return MAGIC_STRING + effect.getType().getIcon() + String.format("%02d:%02d", effect.getRemainingTicks() / 1200, (effect.getRemainingTicks() % 1200) / 20);
    }

    public static @NotNull String serialize(List<ActiveEffect> effects) {
        return effects.stream().map(BossBarSerializer::serialize).toList()
                .toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", " ");
    }


}
