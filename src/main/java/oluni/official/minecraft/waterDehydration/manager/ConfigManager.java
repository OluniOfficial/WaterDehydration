package oluni.official.minecraft.waterDehydration.manager;

import oluni.official.minecraft.waterDehydration.WaterDehydration;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static FileConfiguration cfg;
    public static void setupConfig(WaterDehydration plugin) {
        ConfigManager.cfg = plugin.getConfig();
        plugin.saveConfig();

    }
    public static void reloadConfig(WaterDehydration plugin) {
        plugin.reloadConfig();
        ConfigManager.cfg = plugin.getConfig();
        plugin.reloadRecipe();
    }
    public static FileConfiguration gCfg() {
        return cfg;
    }

    public static int getMW() {
        return cfg.getInt("max-water");
    }
}

