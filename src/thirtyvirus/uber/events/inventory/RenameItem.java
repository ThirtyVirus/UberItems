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
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.Utilities;

public class RenameItem implements Listener {

    UberItems main;
    public RenameItem(UberItems main) { this.main = main; }

    @EventHandler
    // prevent breaking UberItem using an Anvil
    public void playerRenameItem(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getType() == InventoryType.ANVIL) {
            if (event.getRawSlot() == 2) {
                if (event.getView().getItem(0).getType() != Material.AIR && event.getView().getItem(2).getType() != Material.AIR) {
                    if (Utilities.isUber(main, event.getView().getItem(0)) || Utilities.isUber(main, event.getView().getItem(1)) || Utilities.isUber(main, event.getView().getItem(2))) {
                        event.setCancelled(true);
                        Utilities.playSound(ActionSound.ERROR, player);
                    }
                }
            }
        }
    }

    @EventHandler
    // prevent breaking UberItem using a crafting table
    public void playerCraftEvent(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack[] item = event.getInventory().getMatrix();
        if (Utilities.isUber(main, item[0]) || Utilities.isUber(main, item[1]) || Utilities.isUber(main, item[2]) || Utilities.isUber(main, item[3]) || Utilities.isUber(main, item[4]) || Utilities.isUber(main, item[5]) || Utilities.isUber(main, item[6]) || Utilities.isUber(main, item[7]) || Utilities.isUber(main, item[8])){
            event.setCancelled(true);
            Utilities.playSound(ActionSound.ERROR, player);
        }
    }

}
