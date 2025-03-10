package oluni.official.minecraft.waterDehydration.listener;

import oluni.official.minecraft.waterDehydration.WaterDehydration;
import oluni.official.minecraft.waterDehydration.manager.ConfigManager;
import oluni.official.minecraft.waterDehydration.utils.MessageUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.UUID;

public class WaterListener implements Listener {

    private final HashMap<UUID, Integer> waterlvl = new HashMap<>();
    private final NamespacedKey waterLevelKey;
    private final WaterDehydration plugin;
    private final HashMap<UUID, Long> poisonedPlayers = new HashMap<>();

    public WaterListener(WaterDehydration plugin) {
        this.plugin = plugin;
        this.waterLevelKey = new NamespacedKey(plugin, "waterlvl");
    }

    private String getWaterEmoji(int level, boolean poisoned) {
        StringBuilder waterLevel = new StringBuilder(ConfigManager.gCfg().getString("message.water-actionbar.wa1"));
        for (int i = 0; i < ConfigManager.getMW(); i++) {
            if (i < level) {
                if (poisoned) {
                    waterLevel.append(ConfigManager.gCfg().getString("message.water-actionbar.water-poisoning"));
                } else {
                    waterLevel.append(ConfigManager.gCfg().getString("message.water-actionbar.emoji-water"));
                }
            } else {
                waterLevel.append(ConfigManager.gCfg().getString("message.water-actionbar.emoji-dehydration-water"));
            }
        }
        waterLevel.append(ConfigManager.gCfg().getString("message.water-actionbar.wa2"));
        return waterLevel.toString();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PersistentDataContainer data = p.getPersistentDataContainer();
        if (p.hasPlayedBefore() && data.has(waterLevelKey, PersistentDataType.INTEGER)) {
            int level = data.get(waterLevelKey, PersistentDataType.INTEGER);
            waterlvl.put(p.getUniqueId(), level);
        } else {
            waterlvl.put(p.getUniqueId(), ConfigManager.getMW());
        }
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (p.hasPermission("wd.no-water") || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
                return;
            }
            if (waterlvl.containsKey(p.getUniqueId())) {
                int level = waterlvl.get(p.getUniqueId());
                if (level > 0) {
                    waterlvl.put(p.getUniqueId(), level - ConfigManager.gCfg().getInt("how-much-to-reduce-the-water-level"));
                }
            }
        }, 0, ConfigManager.gCfg().getInt("how-much-time-to-reduce-the-water-level") * 20L);
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (p.hasPermission("wd.no-water") || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
                return;
            }
            if (waterlvl.containsKey(p.getUniqueId())) {
                int level = waterlvl.get(p.getUniqueId());
                boolean poisoned = poisonedPlayers.containsKey(p.getUniqueId()) && poisonedPlayers.get(p.getUniqueId()) > System.currentTimeMillis();
                String waterLevelString = getWaterEmoji(level, poisoned);
                MessageUtils.sendActionBar(p, MessageUtils.color(waterLevelString), null);
                if (level == 0) {
                    MessageUtils.sendActionBar(p, ConfigManager.gCfg().getString("message.water-actionbar.dehydrate"), null);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 40, ConfigManager.gCfg().getInt("dehydrate.hunger-effect-amplifier")));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, ConfigManager.gCfg().getInt("dehydrate.poison-effect-amplifier")));
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (waterlvl.containsKey(p.getUniqueId())) {
            int level = waterlvl.get(p.getUniqueId());
            PersistentDataContainer data = p.getPersistentDataContainer();
            data.set(waterLevelKey, PersistentDataType.INTEGER, level);
        }
    }

    @EventHandler
    public void onWater(PlayerItemConsumeEvent e) {
        ItemStack i = e.getItem();
        Player p = e.getPlayer();
        UUID pi = p.getUniqueId();
        if (p.hasPermission("wd.no-water") || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (i.getType() == Material.POTION) {
            ItemMeta m = i.getItemMeta();
            if (m instanceof PotionMeta pm) {
                if (pm.getBasePotionType() == PotionType.WATER) {
                    if (m.hasCustomModelData()) {
                        if (m.getCustomModelData() == ConfigManager.gCfg().getInt("disinfected-custom-model-data")) {
                            if (waterlvl.containsKey(pi)) {
                                waterlvl.put(pi, waterlvl.get(pi) + ConfigManager.gCfg().getInt("how-much-disinfected-water-increase-water-level"));
                                return;
                            }
                        }
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, ConfigManager.gCfg().getInt("water-poisoning.poison-effect-duration") * 20, ConfigManager.gCfg().getInt("water-poisoning.poison-effect-amplifier")));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, ConfigManager.gCfg().getInt("water-poisoning.hunger-effect-duration") * 20, ConfigManager.gCfg().getInt("water-poisoning.hunger-effect-amplifier")));
                    int level = waterlvl.getOrDefault(pi, 0);
                    poisonedPlayers.put(pi, System.currentTimeMillis() + ConfigManager.gCfg().getInt("water-poisoning.water-poisoning-time") * 1000L);
                    if (level < ConfigManager.getMW()) {
                        waterlvl.put(pi, level + ConfigManager.gCfg().getInt("water-poisoning.how-much-infected-water-increase-water-level"));
                    }
                    Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                        if (poisonedPlayers.containsKey(pi) && poisonedPlayers.get(pi) > System.currentTimeMillis()) {
                            if (level > 0) {
                                waterlvl.put(pi, level - ConfigManager.gCfg().getInt("water-poisoning.water-poisoning-reduce-water-in-time"));
                            }
                        }
                    }, 0, ConfigManager.gCfg().getInt("water-poisoning.water-poisoning-reduce-water-time"));
                }
            }
        }
    }
}