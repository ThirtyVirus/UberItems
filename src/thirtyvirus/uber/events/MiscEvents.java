package thirtyvirus.uber.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class MiscEvents implements Listener {

    // prevent hackerman from being used as a totem of undying
    @EventHandler
    private static void onTotemUse(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();

        if (UberItems.getItem("hackerman").compare(player.getInventory().getItemInMainHand()) ||
                UberItems.getItem("hackerman").compare(player.getInventory().getItemInOffHand())) {
            event.setCancelled(true);
        }

    }

    // prevent uberitems from being smelted
    @EventHandler
    private static void onUberSmelt(FurnaceSmeltEvent event) {
        if (Utilities.isUber(event.getSource()) && Utilities.getIntFromItem(event.getSource(), "smeltable") == 0)
            event.setCancelled(true);
    }

}
