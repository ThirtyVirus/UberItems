package thirtyvirus.uber.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import thirtyvirus.uber.UberItems;

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

}
