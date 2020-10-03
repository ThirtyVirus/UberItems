package thirtyvirus.uber.events.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class PlayerInteract implements Listener {

    UberItems main;
    public PlayerInteract(UberItems main) { this.main = main; }

    @EventHandler(priority= EventPriority.HIGH)
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.IN_GROUND) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            UberItem uber = Utilities.getUber(main, item);
            if (uber != null && uber.getID() == 18) {

                Location l1 = player.getLocation();
                Location l2 = event.getHook().getLocation();
                Vector v = new Vector(l2.getX() - l1.getX(), 1, l2.getZ() - l1.getZ());
                player.setVelocity(v);
            }
        }


    }

}
