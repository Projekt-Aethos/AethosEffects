package de.aethosprojekt.aethoseffects;


import de.aethosprojekt.aethoseffects.api.EffectType;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EffectRegistry {

    private final @NotNull Map<NamespacedKey, EffectType> map = new ConcurrentHashMap<>();

    protected EffectRegistry() {

    }

    public void register(@NotNull EffectType type) {
        map.put(type.getKey(), type);
    }

    public @Nullable EffectType fromKey(@NotNull NamespacedKey key) {
        return map.get(key);
    }

    public @NotNull Set<NamespacedKey> keys() {
        return map.keySet();
    }
}
