package thirtyvirus.uber.events.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.MenuUtils;
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

    // process click events in the UberItems crafting menu
    @EventHandler
    public void interactInCraftingMenu(InventoryClickEvent event) {
        // verify that the Player is in the UberItems crafting menu
        if (!event.getView().getTitle().equals("UberItems - Craft Item")) return;

        // check for the recipe for items, and update inventory menu accordingly
        checkCraft(event.getInventory());

        // cancel any clicks on GUI buttons
        if (MenuUtils.customItems.contains(event.getCurrentItem())) event.setCancelled(true);

        // prevent crash?
        if (event.getCurrentItem() == null) return;

        // open crafting guide button functionality
        if (event.getCurrentItem().equals(MenuUtils.RECIPE_MENU_ITEM))
            for (Entity p : event.getViewers()) {
                Player pl = (Player) p; pl.openInventory(MenuUtils.createCustomCraftingTutorialMenu(1));
                Utilities.playSound(ActionSound.CLICK, pl);
            }

        // close inventory button functionality
        if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON))
            for (Entity p : event.getViewers()) { Player pl = (Player) p; pl.closeInventory(); Utilities.playSound(ActionSound.CLICK, pl); }

        // check for clicking a crafted item out of the grid
        if (event.getSlot() == 23) {
            if (event.getCurrentItem() != MenuUtils.CRAFTING_SLOT_ITEM && event.getAction() == InventoryAction.PICKUP_ALL) {
                if (!pullItem(event.getInventory(), event.getCurrentItem())) event.setCancelled(true);
            }
            else {
                event.setCancelled(true);
            }

        }

        // check for the recipe for items
        checkCraft(event.getInventory());
    }

    // update the processing of the crafting process for every inventory event
    @EventHandler
    public void updateUberCraftingGUI(InventoryDragEvent event) {
        // verify that the Player is in the UberItems crafting menu
        if (!event.getView().getTitle().equals("UberItems - Craft Item")) return;

        // check for the recipe for items, and update inventory menu accordingly
        checkCraft(event.getInventory());
    }

    // process click events in the UberItems crafting guide menu
    @EventHandler
    public void interactInCraftingGuideMenu(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("Guide - ")) return;

        // cancel all clicks in this menu
        event.setCancelled(true);

        // next recipe button functionality
        if (event.getCurrentItem().equals(MenuUtils.NEXT_BUTTON)) {
            UberItem item = Utilities.getUber(event.getView().getItem(23));
            int id = item.getID();
            if (UberItems.itemIDs.keySet().contains(id + 1)) {
                for (Entity p : event.getViewers()) {
                    Player pl = (Player) p; pl.openInventory(MenuUtils.createCustomCraftingTutorialMenu(id + 1));
                    Utilities.playSound(ActionSound.CLICK, pl);
                }
            }
            else for (Entity p : event.getViewers()) { Player pl = (Player) p; Utilities.playSound(ActionSound.ERROR, pl); }
        }
        // previous recipe button functionality
        if (event.getCurrentItem().equals(MenuUtils.PREVIOUS_BUTTON)) {
            UberItem item = Utilities.getUber(event.getView().getItem(23));
            int id = item.getID();
            if (id > 1 && UberItems.itemIDs.keySet().contains(id - 1)) {
                for (Entity p : event.getViewers()) {
                    Player pl = (Player) p; pl.openInventory(MenuUtils.createCustomCraftingTutorialMenu(id - 1));
                    Utilities.playSound(ActionSound.CLICK, pl);
                }
            }
            else for (Entity p : event.getViewers()) { Player pl = (Player) p; Utilities.playSound(ActionSound.ERROR, pl); }
        }

        // clicking on an UberItem functionality
        if (Utilities.isUber(event.getCurrentItem())) {
            UberItem item = Utilities.getUber(event.getCurrentItem());
            for (Entity p : event.getViewers()) {
                Player pl = (Player) p; pl.openInventory(MenuUtils.createCustomCraftingTutorialMenu(item.getID()));
                Utilities.playSound(ActionSound.CLICK, pl);
            }
        }

        // close inventory button functionality
        if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON))
            for (Entity p : event.getViewers()) {
                Player pl = (Player) p;
                pl.openInventory(MenuUtils.createCustomCraftingMenu());
                Utilities.playSound(ActionSound.CLICK, pl);
            }

    }

    // make the craftable UberItem appear in the crafted slot if
    public void checkCraft(Inventory i) {

        // put all crafting grid items into a list
        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));
        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) items.set(counter, new ItemStack(Material.AIR));
        }

        // check if any UberItem has a matching recipe to what's in the crafting grid
        for (UberItem item : UberItems.items.values()) {
            if (!item.hasCraftingRecipe()) continue;

            if (listsMatch(items, item.getCraftingRecipe())) {
                i.setItem(23, UberItem.fromString(UberItems.getInstance(), "" + item.getID(), 1));
                break;
            }
            else i.setItem(23, MenuUtils.CRAFTING_SLOT_ITEM);

        }
    }

    // pull crafted item from the slot
    // assumes that the item was pulled from a craft
    public boolean pullItem(Inventory i, ItemStack item) {

        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));
        UberItem uber = Utilities.getUber(item);

        // double check if the item should be crafted
        if (!listsMatch(items, uber.getCraftingRecipe())) return false;

        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) continue;
            items.get(counter).setAmount(items.get(counter).getAmount() - uber.getCraftingRecipe().get(counter).getAmount());
        }
        return true;
    }

    // verify that the crafting grid is identical to that of a crafting recipe
    // (for now) checks for matching Material and having enough quantity
    // TODO check for more info in the case that the item is more than just a material (like uber materials)
    public boolean listsMatch(List<ItemStack> l1, List<ItemStack> l2) {
        boolean itemApplicable = true;

        for (int counter = 0; counter < l1.size(); counter++) {
            if (l1.get(counter).getType() != l2.get(counter).getType() ||
                    l1.get(counter).getAmount() < l2.get(counter).getAmount()) {
                return false;
            }
        }
        return true;
    }
}
