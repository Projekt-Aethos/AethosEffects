package de.aethosprojekt.aethoseffects;

import de.aethosprojekt.aethoseffects.command.Command;
import de.aethosprojekt.aethoseffects.entity.EffectEntityImpl;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AethosEffects extends JavaPlugin {
    private final @NotNull EffectRegistry registry = new EffectRegistry();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(EffectEntityImpl.EFFECT_LISTENER, this);
        getServer().getCommandMap().register("aethoseffect", new Command());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public @NotNull EffectRegistry getRegistry() {
        return registry;
    }
}
