package oluni.official.minecraft.waterDehydration.commands;

import oluni.official.minecraft.waterDehydration.WaterDehydration;
import oluni.official.minecraft.waterDehydration.manager.ConfigManager;
import oluni.official.minecraft.waterDehydration.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WaterCommand implements TabCompleter, CommandExecutor {
    private final WaterDehydration wd;
    public WaterCommand(WaterDehydration wd) {
        this.wd = wd;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player p) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("wd.reload")) {
                        ConfigManager.reloadConfig(wd);
                        MessageUtils.sendMessage(p, ConfigManager.gCfg().getString("message.reload"), null);
                    } else {
                        MessageUtils.sendMessage(p, ConfigManager.gCfg().getString("message.no-permission"), null);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of("reload");
    }
}
