package thirtyvirus.uber.events.inventory;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.helpers.ItemStorageUtilities;
import thirtyvirus.uber.helpers.Utilities;

public class InventoryClose implements Listener {

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){

        //save Shooty-Box inventory on Close
        if (event.getView().getTitle().contains("Shooty Box")){
            Player player = (Player) event.getPlayer();
            ItemStack shootyBox = player.getInventory().getItemInMainHand();

            ItemStorageUtilities.saveItemsInLore(shootyBox, event.getInventory().getContents(), 3);
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
        }

    }

}
