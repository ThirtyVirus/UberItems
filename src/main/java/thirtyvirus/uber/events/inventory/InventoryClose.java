package thirtyvirus.uber.events.inventory;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.helpers.Utilities;

import java.util.Arrays;
import java.util.List;

public class InventoryClose implements Listener {

    @EventHandler
    private void onCloseInventory(InventoryCloseEvent event) {

        // save ShootyBox inventory on close
        if (event.getView().getTitle().contains("Shooty Box")){
            Player player = (Player) event.getPlayer();
            ItemStack shootyBox = player.getInventory().getItemInMainHand();

            Utilities.saveCompactInventory(shootyBox, event.getInventory().getContents());
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
        }

        // add items back to a player's inventory if closing the UberItem crafting menu
        if (event.getView().getTitle().contains("UberItems - Craft Item") && event.getView().getTopInventory().getLocation() == null) {
            Inventory i = event.getInventory();
            List<ItemStack> items = Arrays.asList(
                    i.getItem(10), i.getItem(11), i.getItem(12),
                    i.getItem(19), i.getItem(20), i.getItem(21),
                    i.getItem(28), i.getItem(29), i.getItem(30));
            for (ItemStack it : items) if (it != null) Utilities.givePlayerItemSafely((Player)event.getPlayer(), it);

        }

    }

}
