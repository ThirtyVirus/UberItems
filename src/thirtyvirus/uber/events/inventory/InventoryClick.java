package thirtyvirus.uber.events.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

import static org.bukkit.Material.TROPICAL_FISH;

public class InventoryClick implements Listener {

    UberItems main;
    public InventoryClick(UberItems main) { this.main = main; }

    // process what happens when you hold an item and click on an UberItem in the inventory
    // TODO make work for creative mode
    @EventHandler
    public void clickItemOntoUber(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && Utilities.isUber(event.getCurrentItem())) {
            Utilities.getUber(event.getCurrentItem()).clickedInInventoryAction((Player)event.getWhoClicked(), event);
        }
    }

}
