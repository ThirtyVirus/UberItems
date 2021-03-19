package thirtyvirus.uber.events.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.MenuUtils;
import thirtyvirus.uber.helpers.Utilities;

public class InventoryClick implements Listener {

    UberItems main;
    public InventoryClick(UberItems main) { this.main = main; }

    public static List<InventoryAction> validCraftActions = Arrays.asList(InventoryAction.PICKUP_ALL, InventoryAction.MOVE_TO_OTHER_INVENTORY);

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
        Player player = (Player) event.getWhoClicked();

        Bukkit.getLogger().info(event.getAction().toString() + " " + event.getClick().toString() + " " + event.getRawSlot());

        // cancel any clicks on GUI buttons
        if (MenuUtils.customItems.contains(event.getCurrentItem())) event.setCancelled(true);

        // make crafting grid *more* responsive
        MenuUtils.checkCraft(event.getInventory());

        // verify that an item was clicked
        if (event.getCurrentItem() == null) return;
        // open crafting guide button functionality
        if (event.getCurrentItem().equals(MenuUtils.RECIPE_MENU_ITEM)) {
            player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(0));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // close inventory button functionality
        else if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON)) {
            player.closeInventory(); Utilities.playSound(ActionSound.CLICK, player);
        }

        // check for clicking a crafted item out of the grid
        // test if the item is NOT the barrier item, the action is trying to pickup, and
        //   re-verify that the item's components are indeed in the crafting grid
        if (event.getRawSlot() == 23) {
            if (event.getCurrentItem() == MenuUtils.CRAFTING_SLOT_ITEM) event.setCancelled(true);
            else if (event.getClick() == ClickType.DOUBLE_CLICK) event.setCancelled(true);
            else if (!validCraftActions.contains(event.getAction())) event.setCancelled(true);
            else pullItem(event, event.getInventory(), event.getCurrentItem());
        }

    }

    // process click events in the UberItems crafting guide menu
    @EventHandler
    public void interactInCraftingGuideMenu(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("UberItems Guide")) return;
        Player player = (Player) event.getWhoClicked();

        // cancel all clicks in this menu
        event.setCancelled(true);

        // prevent console errors
        if (event.getCurrentItem() == null) return;

        // open crafting guide button functionality
        if (event.getCurrentItem().equals(MenuUtils.RECIPE_MENU_ITEM)) {
            player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(0));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // close inventory button functionality
        else if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON)) {
            ItemStack test = event.getView().getTopInventory().getItem(24);
            if (test != null && test.equals(MenuUtils.EMPTY_SLOT_ITEM))
                player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(0));
            else player.openInventory(MenuUtils.createCustomCraftingMenu());
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // previous button
        else if (event.getCurrentItem().equals(MenuUtils.PREVIOUS_BUTTON)) {
            int page = Integer.parseInt(event.getView().getTitle().split(" - ")[1]) - 1;
            if (page > 0) player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(page - 1));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // next button
        else if (event.getCurrentItem().equals(MenuUtils.NEXT_BUTTON)) {
            int page = Integer.parseInt(event.getView().getTitle().split(" - ")[1]) - 1;
            if (UberItems.itemIDs.values().size() + UberItems.materialIDs.values().size() > (page + 1) * MenuUtils.ITEMS_PER_GUIDE_PAGE)
                player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(page + 1));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // clicking on an UberItem functionality
        else if (Utilities.isUber(event.getCurrentItem())) {
            UberItem item = Utilities.getUber(event.getCurrentItem());

            // allow Creative Mode players to take UberItems from the menu directly
            if (player.getGameMode() == GameMode.CREATIVE && event.getClick() == ClickType.SHIFT_LEFT) {
                ItemStack i = event.getCurrentItem().clone();
                if (!item.isStackable()) Utilities.storeStringInItem(i,  java.util.UUID.randomUUID().toString(), "UUID");
                player.getInventory().addItem(i);
                return;
            }

            player.openInventory(MenuUtils.createUnboundCraftingTutorialMenu(event.getCurrentItem(), item.getCraftingRecipe(), 1));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // clicking on an UberMaterial functionality
        else if (Utilities.isUberMaterial(event.getCurrentItem())) {
            UberMaterial item = Utilities.getUberMaterial(event.getCurrentItem());

            // allow Creative Mode players to take UberItems from the menu directly
            if (player.getGameMode() == GameMode.CREATIVE && event.getClick() == ClickType.SHIFT_LEFT) {
                ItemStack i = event.getCurrentItem().clone(); i.setAmount(event.getCurrentItem().getType().getMaxStackSize());
                if (!item.isStackable()) Utilities.storeStringInItem(i,  java.util.UUID.randomUUID().toString(), "UUID");
                player.getInventory().addItem(i);
                return;
            }

            player.openInventory(MenuUtils.createUnboundCraftingTutorialMenu(event.getCurrentItem(), item.getCraftingRecipe(), item.getCraftAmount()));
            Utilities.playSound(ActionSound.CLICK, player);
        }

    }
    @EventHandler
    public void shootyBoxAmmoGuide(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("Ammo Guide")) return;

        // cancel all clicks in this menu
        event.setCancelled(true);

        // close inventory button functionality
        if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON))
            event.getWhoClicked().closeInventory();
    }

    // pull crafted item from the slot
    // assumes that the item was pulled from a craft
    public void pullItem(InventoryClickEvent event, Inventory i, ItemStack item) {
        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));

        if (Utilities.isUber(item)) pullUberItem(event, items, item);
        if (Utilities.isUberMaterial(item)) pullUberMaterial(event, items, item);

        // update inventory on a 1 tick delay as to prevent visual bugs clientside
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), new Runnable() {
            public void run() { ((Player) event.getWhoClicked()).updateInventory(); } }, 1);
    }
    public void pullUberItem(InventoryClickEvent event, List<ItemStack> items, ItemStack item) {
        UberItem uber = Utilities.getUber(item);

        // prevent null, double check if the item should be crafted
        if (uber == null || !uber.getCraftingRecipe().isEqual(items)) {
            event.setCancelled(true);
            return;
        }

        // the player has the go-ahead to pull the item, delete the components
        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) continue;
            items.get(counter).setAmount(items.get(counter).getAmount() - uber.getCraftingRecipe().get(counter).getAmount());
        }

    }
    public void pullUberMaterial(InventoryClickEvent event, List<ItemStack> items, ItemStack item) {
        UberMaterial mat = Utilities.getUberMaterial(item);

        // prevent null, double check if the item should be crafted
        if (mat == null || !mat.getCraftingRecipe().isEqual(items)) {
            event.setCancelled(true);
            return;
        }

        // attempt to stack items together
        //if (event.getAction() == InventoryAction.PLACE_ALL) {
        //    ItemStack inHand = event.getWhoClicked().getItemOnCursor();
        //    if (Utilities.getUberMaterial(inHand) == Utilities.getUberMaterial(event.getCurrentItem())
        //            && inHand.getAmount() + event.getCurrentItem().getAmount() <= inHand.getMaxStackSize()) {
        //        inHand.setAmount(inHand.getAmount() + event.getCurrentItem().getAmount());
        //        event.setCancelled(true);
        //        ((Player) event.getWhoClicked()).updateInventory();
        //    }
        //    else {
        //        event.setCancelled(true);
        //        return;
        //    }
        //    event.setCancelled(true);
        //}

        // the player has the go-ahead to pull the item, delete the components
        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) continue;
            items.get(counter).setAmount(items.get(counter).getAmount() - mat.getCraftingRecipe().get(counter).getAmount());
        }

    }

}
