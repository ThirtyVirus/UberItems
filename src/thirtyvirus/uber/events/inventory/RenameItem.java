package thirtyvirus.uber.events.inventory;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;

public class RenameItem implements Listener {

    @EventHandler// (priority = EventPriority.LOWEST)
    public void playerRenameItem(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        //Prevent breaking Uber Item using Anvil
        if (event.getView().getType() == InventoryType.ANVIL) {
            if (event.getRawSlot() == 2) {
                if (event.getView().getItem(0).getType() != Material.AIR && event.getView().getItem(2).getType() != Material.AIR) {

                    if (UberItems.isUber(event.getView().getItem(0)) || UberItems.isUber(event.getView().getItem(1)) || UberItems.isUber(event.getView().getItem(2))) {
                        event.setCancelled(true);
                        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_SHULKER, 1, 1);
                    }

                }
            }
        }


    }

    @EventHandler
    public void playerCraftEvent(CraftItemEvent event){
        Player player = (Player) event.getWhoClicked();
        //Prevent breaking Uber Item using Crafting Table

        ItemStack[] item = event.getInventory().getMatrix();

        if (UberItems.isUber(item[0]) || UberItems.isUber(item[1]) || UberItems.isUber(item[2]) || UberItems.isUber(item[3]) || UberItems.isUber(item[4]) || UberItems.isUber(item[5]) || UberItems.isUber(item[6]) || UberItems.isUber(item[7]) || UberItems.isUber(item[8])){
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_SHULKER, 1, 1);
        }

    }

}
