package thirtyvirus.uber.events.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.Utilities;

import java.util.Arrays;

public class PlayerInteract implements Listener {

    // process grappling hook ability
    @EventHandler(priority= EventPriority.HIGH)
    private void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.IN_GROUND) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            UberItem uber = Utilities.getUber(item);
            if (uber != null && uber.getID() == 18) {

                // repair rod each time it is used
                Utilities.repairItem(item);

                // enforce 1.5s cooldown on the grappling hook
                if (!Utilities.enforceCooldown(player, "grapple", 1.5, item, false)) {
                    Utilities.warnPlayer(player, "Whow! Slow down there!");
                    return;
                }

                Location l1 = player.getLocation();
                Location l2 = event.getHook().getLocation();
                Vector v = new Vector(l2.getX() - l1.getX(), 1, l2.getZ() - l1.getZ());
                player.setVelocity(v);
            }
        }


    }

}
