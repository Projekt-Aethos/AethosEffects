package de.aethosprojekt.aethoseffects.command;

import de.aethosprojekt.aethoseffects.AethosEffects;
import de.aethosprojekt.aethoseffects.EffectRegistry;
import de.aethosprojekt.aethoseffects.api.EffectEntity;
import de.aethosprojekt.aethoseffects.api.EffectType;
import de.aethosprojekt.aethoseffects.entity.EffectEntityImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Command extends org.bukkit.command.Command {

    public Command() {
        super("effect");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @Nullable [] args) {
        assert args != null;
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                switch (args[0]) {
                    case "give" -> {
                        if (args.length < 2) {
                            player.sendMessage("Nicht genügend Argumente");
                            return false;
                        }
                        EffectEntityImpl<Player> entity = new EffectEntityImpl<>(player);
                        EffectRegistry registry = AethosEffects.getPlugin(AethosEffects.class).getRegistry();
                        NamespacedKey key = NamespacedKey.fromString(args[1]);
                        if (key == null) {
                            player.sendMessage("Key ungültig");
                            return false;
                        }
                        EffectType type = registry.fromKey(key);
                        if (type == null) {
                            player.sendMessage("EffectType ungültig");
                            return false;
                        }
                        if (args.length < 3) {
                            player.sendMessage("Angabe von Ticks fehlt");
                            return false;
                        }
                        long ticks = Long.parseLong(args[2]);
                        entity.addEffect(type, ticks);
                        return true;

                    }
                    case "remove" -> {
                        EffectEntity<Player> entity = EffectEntity.of(player);
                        EffectRegistry registry = AethosEffects.getPlugin(AethosEffects.class).getRegistry();
                        NamespacedKey key = NamespacedKey.fromString(args[1]);
                        if (key == null) {
                            player.sendMessage("Key ungültig");
                            return false;
                        }
                        EffectType type = registry.fromKey(key);
                        if (type == null) {
                            player.sendMessage("EffectType ungültig");
                            return false;
                        }
                        entity.removeEffect(type);
                        return true;
                    }
                    case "clear" -> {
                        EffectEntityImpl<Player> entity = new EffectEntityImpl<>(player);
                        entity.clearEffects();
                        return true;
                    }
                    case "info" -> {
                        EffectEntityImpl<Player> entity = new EffectEntityImpl<>(player);
                        player.sendMessage("Ticking: " + entity.isTicking());
                        for (EffectType type : entity.getEffectTypes()) {
                            player.sendMessage(type.getKey().toString());
                            player.sendMessage("Ticks: " + entity.getRemainingTicks(type));
                        }
                    }
                    default -> {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("give", "remove", "info", "clear");
        }
        if ("give".equals(args[0]) || "remove".equals(args[0])) {
            if (args.length == 2) {
                return AethosEffects.getPlugin(AethosEffects.class).getRegistry().keys().stream().map(Objects::toString).toList();
            }
        }
        return List.of();
    }
}
