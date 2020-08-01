package thirtyvirus.uber.events.inventory;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class InventoryClose implements Listener {

    UberItems main;
    public InventoryClose(UberItems main) { this.main = main; }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){

        // save ShootyBox inventory on close
        if (event.getView().getTitle().contains("Shooty Box")){
            Player player = (Player) event.getPlayer();
            ItemStack shootyBox = player.getInventory().getItemInMainHand();

            Utilities.saveCompactInventory(main, shootyBox, event.getInventory().getContents());
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
        }

    }

}
