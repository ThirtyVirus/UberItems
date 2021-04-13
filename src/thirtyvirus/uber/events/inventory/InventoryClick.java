package thirtyvirus.uber.events.inventory;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.MenuUtils;
import thirtyvirus.uber.helpers.Utilities;

public class InventoryClick implements Listener {

    // process clicking an ItemStack ONTO an UberItem in the inventory
    // TODO make work for creative mode
    @EventHandler
    private void clickItemOntoUber(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && Utilities.isUber(event.getCurrentItem())) {
            Utilities.getUber(event.getCurrentItem()).clickedInInventoryAction((Player)event.getWhoClicked(), event, event.getCurrentItem(), event.getCursor());
        }
    }

    // process click events in the UberItems Crafting Menu
    @EventHandler
    private void interactInCraftingMenu(InventoryClickEvent event) {
        // verify that the Player is in the UberItems crafting menu
        if (!event.getView().getTitle().equals("UberItems - Craft Item")) return;
        Player player = (Player) event.getWhoClicked();

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

        // process pulling an item out of the crafting slot
        if (event.getRawSlot() == 23) MenuUtils.checkIfPullValid(event);
    }

    // process click events in the UberItems Crafting Guide Menu
    @EventHandler
    private void interactInCraftingGuideMenu(InventoryClickEvent event) {
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

    // process click events in the UberItems Shooty Box Guide Menu
    @EventHandler
    private void shootyBoxAmmoGuide(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("Ammo Guide")) return;

        // cancel all clicks in this menu
        event.setCancelled(true);

        // close inventory button functionality
        if (event.getCurrentItem().equals(MenuUtils.BACK_BUTTON))
            event.getWhoClicked().closeInventory();
    }

}
