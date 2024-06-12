package de.aethosprojekt.aethoseffects;

import de.aethosprojekt.aethoseffects.api.EffectEntity;
import de.aethosprojekt.aethoseffects.api.EffectType;
import de.aethosprojekt.aethoseffects.command.Command;
import de.aethosprojekt.aethoseffects.display.Display;
import de.aethosprojekt.aethoseffects.entity.EffectEntityImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AethosEffects extends JavaPlugin {
    private final @NotNull EffectRegistry registry = new EffectRegistry();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(EffectEntityImpl.EFFECT_LISTENER, this);
        getServer().getCommandMap().register("aethoseffect", new Command());

        EffectType type = new EffectType() {

            @Override
            public void tick(@NotNull EffectEntity<?> entity) {
                entity.entity().sendMessage("Tick");
            }

            @Override
            public @NotNull NamespacedKey getKey() {
                return new NamespacedKey(AethosEffects.getPlugin(AethosEffects.class), "TEST");
            }

            @Override
            public @NotNull String getName() {
                return "test";
            }

            @Override
            public char getIcon() {
                return 'A';
            }

            @Override
            public int getTicksBetween() {
                return 20;
            }

            @NotNull
            @Override
            public Display getDisplay() {
                return entity -> entity.sendMessage("TEST");
            }


        };
        registry.register(type);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public @NotNull EffectRegistry getRegistry() {
        return registry;
    }
}
