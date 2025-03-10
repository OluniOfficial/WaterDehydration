
package oluni.official.minecraft.waterDehydration.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import java.util.Map;

public class MessageUtils {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static String color(String m) {
        m = m.replaceAll("&#([A-Fa-f0-9]{6})", "<color:#$1>");
        return m;
    }

    public static String rP(String m, Map<String, String> ph) {
        if (ph != null) {
            for (Map.Entry<String, String> entry : ph.entrySet()) {
                m = m.replace(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    public static void sendMessage(Player p, String m, Map<String, String> ph) {
        m = rP(color(m), ph);
        Component message = mm.deserialize(m);
        p.sendMessage(message);
    }

    public static void sendActionBar(Player p, String m, Map<String, String> ph) {
        m = rP(color(m), ph);
        Component message = mm.deserialize(m);
        p.sendActionBar(message);
    }
}