package de.aethosprojekt.aethoseffects.api;

import de.aethosprojekt.aethoseffects.AethosEffects;
import de.aethosprojekt.aethoseffects.display.Display;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public interface EffectType {

    void tick(@NotNull EffectEntity<?> entity);

    @NotNull NamespacedKey getKey();

    @NotNull String getName();

    char getIcon();

    int getTicksBetween();


    @NotNull Display getDisplay();

    class DataType implements PersistentDataType<PersistentDataContainer, EffectType> {
        public static final @NotNull NamespacedKey EFFECT_TYPE = new NamespacedKey(AethosEffects.getProvidingPlugin(AethosEffects.class), "EFFECT_TYPE");

        @Override
        public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
            return PersistentDataContainer.class;
        }

        @Override
        public @NotNull Class<EffectType> getComplexType() {
            return EffectType.class;
        }

        @Override
        public @NotNull PersistentDataContainer toPrimitive(@NotNull EffectType complex, @NotNull PersistentDataAdapterContext context) {
            PersistentDataContainer container = context.newPersistentDataContainer();
            container.set(EFFECT_TYPE, STRING, complex.getKey().toString());
            return container;
        }

        @Override
        public @NotNull EffectType fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
            NamespacedKey key = NamespacedKey.fromString(Objects.requireNonNull(primitive.get(EFFECT_TYPE, STRING)));
            assert key != null;
            return Objects.requireNonNull(AethosEffects.getPlugin(AethosEffects.class).getRegistry().fromKey(key));
        }
    }
}
