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

    // prevent renaming UberItems or UberMaterials with an anvil
    @EventHandler
    private void playerRenameItem(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getType() == InventoryType.ANVIL) {
            if (event.getRawSlot() == 2) {
                if (event.getView().getItem(0).getType() != Material.AIR && event.getView().getItem(2).getType() != Material.AIR) {
                    if (Utilities.isUber(event.getView().getItem(0)) || Utilities.isUber(event.getView().getItem(1)) || Utilities.isUber(event.getView().getItem(2))) {
                        event.setCancelled(true);
                        Utilities.playSound(ActionSound.ERROR, player);
                    }
                    if (Utilities.isUberMaterial(event.getView().getItem(0)) || Utilities.isUberMaterial(event.getView().getItem(1)) || Utilities.isUberMaterial(event.getView().getItem(2))) {
                        event.setCancelled(true);
                        Utilities.playSound(ActionSound.ERROR, player);
                    }
                }
            }
        }
    }

    // prevent crafting using UberItems or (unwanted) UberMaterials into vanilla recipes
    @EventHandler
    private void playerCraftEvent(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack[] item = event.getInventory().getMatrix();

        for (int counter = 0; counter < 9; counter++) {
            if (Utilities.isUber(item[counter]) ||(Utilities.isUberMaterial(item[counter]) && !Utilities.getUberMaterial(item[counter]).isVanillaCraftable())) {
                event.setCancelled(true);
                Utilities.playSound(ActionSound.ERROR, player);
            }
        }
    }

}
