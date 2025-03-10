package oluni.official.minecraft.waterDehydration;

import net.kyori.adventure.text.minimessage.MiniMessage;
import oluni.official.minecraft.waterDehydration.commands.WaterCommand;
import oluni.official.minecraft.waterDehydration.listener.WaterListener;
import oluni.official.minecraft.waterDehydration.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

public final class WaterDehydration extends JavaPlugin {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static String color(String m) {
        m = m.replaceAll("&#([A-Fa-f0-9]{6})", "<color:#$1>");
        return m;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new WaterListener(this), this);
        getCommand("wd").setExecutor(new WaterCommand(this));
        getCommand("wd").setTabCompleter(new WaterCommand(this));
        ConfigManager.setupConfig(this);
        addRecipe();
    }

    public void addRecipe() {
        ItemStack r = new ItemStack(Material.POTION);
        ItemMeta rm = r.getItemMeta();
        if (rm instanceof PotionMeta pm) {
            pm.setBasePotionType(PotionType.WATER);
        }
        if (rm.displayName() == null) {
            rm.displayName(mm.deserialize(color(ConfigManager.gCfg().getString("disinfected-water-name"))));
        } else {
            rm.displayName(rm.displayName().append(mm.deserialize(color(ConfigManager.gCfg().getString("disinfected-water-name")))));
        }
        rm.setCustomModelData(ConfigManager.gCfg().getInt("disinfected-custom-model-data"));
        r.setItemMeta(rm);
        ItemStack i = new ItemStack(Material.POTION);
        PotionMeta im = (PotionMeta) i.getItemMeta();
        im.setBasePotionType(PotionType.WATER);
        i.setItemMeta(im);
        CampfireRecipe recipe = new CampfireRecipe(
                new NamespacedKey(this, "disinfected-water"),
                r,
                new RecipeChoice.ExactChoice(i),
                0.35f,
                ConfigManager.gCfg().getInt("boiling-water.campfire-cooking-time") * 20
        );
        getServer().addRecipe(recipe);
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(
                new NamespacedKey(this, "disinfected-water-furnace"),
                r,
                new RecipeChoice.ExactChoice(i),
                0.35f,
                ConfigManager.gCfg().getInt("boiling-water.furnace-cooking-time") * 20
        );
        getServer().addRecipe(furnaceRecipe);
    }

    public void reloadRecipe() {
        getServer().resetRecipes();
        addRecipe();
    }
}
