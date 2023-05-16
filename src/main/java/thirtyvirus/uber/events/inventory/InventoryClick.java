package thirtyvirus.uber.events.inventory;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

import java.util.Objects;

public class InventoryClick implements Listener {

    // process clicking an ItemStack ONTO an UberItem in the inventory
    // TODO make work for creative mode
    @EventHandler
    private void clickItemOntoUber(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && Utilities.isUber(event.getCurrentItem())) {
            UberItem uber = Utilities.getUber(event.getCurrentItem());
            if (uber != null) uber.clickedInInventoryAction((Player)event.getWhoClicked(), event, event.getCurrentItem(), event.getCursor());
        }
    }

    // update the crafting result on InventoryDragEvents
    @EventHandler
    private void inventoryDragEvent(InventoryDragEvent event) {
        // verify that the Player is in the UberItems crafting menu
        if (!event.getView().getTitle().equals("UberItems - Craft Item") || event.getView().getTopInventory().getLocation() != null) return;

        // make crafting grid *more* responsive
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> MenuUtils.checkCraft(event.getInventory()), 1);
    }

    // process click events in the UberItems Crafting Menu
    @EventHandler
    private void interactInCraftingMenu(InventoryClickEvent event) {
        // verify that the Player is in the UberItems crafting menu
        if (!event.getView().getTitle().equals("UberItems - Craft Item") || event.getView().getTopInventory().getLocation() != null) return;

        Player player = (Player) event.getWhoClicked();

        // cancel any clicks on GUI buttons
        if (MenuUtils.customItems.contains(event.getCurrentItem())) event.setCancelled(true);

        // make crafting grid *more* responsive
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> MenuUtils.checkCraft(event.getInventory()), 1);

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
        if (event.getRawSlot() == 23) MenuUtils.pullItem(event);
    }

    // process click events in the UberItems Crafting Guide Menu
    @EventHandler
    private void interactInCraftingGuideMenu(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("Guide - ") || event.getView().getTopInventory().getLocation() != null) return;
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
            if (UberItems.getItems().size() + UberItems.getMaterials().size() > (page + 1) * MenuUtils.ITEMS_PER_GUIDE_PAGE)
                player.openInventory(MenuUtils.createCustomCraftingTutorialMenu(page + 1));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // clicking on an UberItem functionality
        else if (Utilities.isUber(event.getCurrentItem())) {
            UberItem item = Utilities.getUber(event.getCurrentItem());
            if (item == null || UberItems.getItem("null").compare(event.getCurrentItem())) return;

            // allow Creative Mode players to take UberItems from the menu directly
            if (player.getGameMode() == GameMode.CREATIVE && event.getClick() == ClickType.SHIFT_LEFT) {

                // enforce premium vs lite, item rarity perms, item specific perms
                if (Utilities.enforcePermissions(player, item)) return;

                ItemStack i = event.getCurrentItem().clone(); i.setAmount(1);
                if (!item.isStackable()) Utilities.storeStringInItem(i, java.util.UUID.randomUUID().toString(), "UUID");
                else i.setAmount(event.getCurrentItem().getType().getMaxStackSize());
                Utilities.givePlayerItemSafely(player, i);
                return;
            }

            player.openInventory(MenuUtils.createUnboundCraftingTutorialMenu(event.getCurrentItem(), item.getCraftingRecipe()));
            Utilities.playSound(ActionSound.CLICK, player);
        }
        // clicking on an UberMaterial functionality
        else if (Utilities.isUberMaterial(event.getCurrentItem())) {
            UberMaterial item = Utilities.getUberMaterial(event.getCurrentItem());
            if (item == null) return;

            // allow Creative Mode players to take UberMaterials from the menu directly
            if (player.getGameMode() == GameMode.CREATIVE && event.getClick() == ClickType.SHIFT_LEFT) {
                ItemStack i = event.getCurrentItem().clone(); i.setAmount(1);
                if (!item.isStackable()) Utilities.storeStringInItem(i, java.util.UUID.randomUUID().toString(), "UUID");
                else i.setAmount(event.getCurrentItem().getType().getMaxStackSize());
                Utilities.givePlayerItemSafely(player, i);
                return;
            }

            player.openInventory(MenuUtils.createUnboundCraftingTutorialMenu(event.getCurrentItem(), item.getCraftingRecipe()));
            Utilities.playSound(ActionSound.CLICK, player);
        }

    }

    // process click events in the UberItems Shooty Box Guide Menu
    @EventHandler
    private void shootyBoxAmmoGuide(InventoryClickEvent event) {
        // verify that the Player is in a UberItems crafting guide menu
        if (!event.getView().getTitle().contains("Ammo Guide") || event.getView().getTopInventory().getLocation() != null) return;

        // cancel all clicks in this menu
        event.setCancelled(true);

        // close inventory button functionality
        if (Objects.equals(event.getCurrentItem(), MenuUtils.BACK_BUTTON))
            event.getWhoClicked().closeInventory();
    }

    // allow UberItems that are helmets to be worn as such
    @EventHandler
    private void onClickhelmet(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.CRAFTING) return;

        // clicking UberItem onto head slot
        if (event.getSlot() == 39 && Utilities.isUber(event.getCursor())) {
            if (Utilities.getIntFromItem(event.getCursor(), "uberhelmet") == 0) return;

            // no item is there
            if (event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE) {
                event.getWhoClicked().getInventory().setHelmet(event.getCursor());
                event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
                event.setCancelled(true);
            }
            // an item is there
            if (event.getAction() == InventoryAction.NOTHING) {
                ItemStack head = event.getCurrentItem();
                Utilities.scheduleTask(()->{
                    event.setCurrentItem(event.getCursor());
                    event.getWhoClicked().setItemOnCursor(head);
                }, 1);
            }
        }
        // shift clicking UberItem in inventory with head slot open
        else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && Utilities.isUber(event.getCurrentItem())) {
            if (Utilities.getIntFromItem(event.getCurrentItem(), "uberhelmet") == 0) return;

            if (event.getWhoClicked().getInventory().getHelmet() == null || event.getWhoClicked().getInventory().getHelmet().getType() == Material.AIR) {
                event.getWhoClicked().getInventory().setItem(39, event.getCurrentItem());
                event.setCurrentItem(new ItemStack(Material.AIR));
                event.setCancelled(true);
            }
        }

    }

}
