package thirtyvirus.uber.helpers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MenuUtils {

    public static final ItemStack EMPTY_SLOT_ITEM = Utilities.nameItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    public static final ItemStack EMPTY_ERROR_SLOT_ITEM = Utilities.nameItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Item has no crafting recipe");
    public static final ItemStack CRAFTING_SLOT_ITEM = Utilities.loreItem(Utilities.nameItem(Material.BARRIER, ChatColor.RED + "Recipe Required"), Arrays.asList(ChatColor.GRAY + "Add the items for a valid", ChatColor.GRAY + "recipe in the crafting grid", ChatColor.GRAY + "to the left!"));
    public static final ItemStack RECIPE_MENU_ITEM = Utilities.loreItem(Utilities.nameItem(Material.KNOWLEDGE_BOOK, ChatColor.GREEN + "Recipe Guide"), Collections.singletonList(ChatColor.GRAY + "View all UberItems Recipes"));
    private static final ItemStack PLUGIN_INFO = Utilities.loreItem(Utilities.nameItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "About UberItems"), Arrays.asList(
            ChatColor.GRAY + "Plugin made by " + ChatColor.RED + "ThirtyVirus", "", ChatColor.GRAY + "UberItems is a versatile custom item API, ", ChatColor.GRAY + "allowing for quick and easy advanced item", ChatColor.GRAY + "functionality on Minecraft Spigot servers!", "",
            ChatColor.RED + "" + ChatColor.BOLD + "You" + ChatColor.WHITE + ChatColor.BOLD + "Tube" + ChatColor.GREEN + " - YouTube.com/ThirtyVirus",
            ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Twitter" + ChatColor.GREEN + " - Twitter.com/ThirtyVirus"));
    public static final ItemStack BACK_BUTTON = Utilities.nameItem(Material.ARROW, ChatColor.GREEN + "Back");
    public static final ItemStack NEXT_BUTTON = Utilities.nameItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Next");
    public static final ItemStack PREVIOUS_BUTTON = Utilities.nameItem(Material.RED_CONCRETE, ChatColor.RED + "Previous");

    public static final List<ItemStack> customItems = Arrays.asList(EMPTY_SLOT_ITEM, EMPTY_ERROR_SLOT_ITEM, CRAFTING_SLOT_ITEM, RECIPE_MENU_ITEM, PLUGIN_INFO, BACK_BUTTON, NEXT_BUTTON, PREVIOUS_BUTTON);

    public static final List<Integer> CUSTOM_CRAFTING_MENU_EXCEPTIONS = Arrays.asList(10,11,12,19,20,21,28,29,30,23);
    public static final List<Integer> CRAFTING_GUIDE_MENU_EXCEPTIONS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,51,52,53);
    public static final List<Integer> CRAFTING_GUIDE_ITEM_SLOTS = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
    public static final int ITEMS_PER_GUIDE_PAGE = 28;

    public static final List<InventoryAction> validCraftActions = Arrays.asList(InventoryAction.PICKUP_ALL, InventoryAction.MOVE_TO_OTHER_INVENTORY);

    // create the "main" custom crafting menu
    public static Inventory createCustomCraftingMenu() {
        Inventory i = Bukkit.createInventory(null, 45, "UberItems - Craft Item");

        for (int counter = 0; counter < 45; counter++) {
            if (!CUSTOM_CRAFTING_MENU_EXCEPTIONS.contains(counter)) i.setItem(counter, EMPTY_SLOT_ITEM);
        }
        i.setItem(23, CRAFTING_SLOT_ITEM);

        i.setItem(16, RECIPE_MENU_ITEM);
        i.setItem(25, PLUGIN_INFO);
        i.setItem(34, BACK_BUTTON);

        return i;
    }

    // create the UberItems "main" Tutorial Menu
    public static Inventory createCustomCraftingTutorialMenu(int page) {

        // prep inventory elements
        Inventory i = Bukkit.createInventory(null, 54, "UberItems Guide - " + (page + 1));
        for (int index : CRAFTING_GUIDE_MENU_EXCEPTIONS) i.setItem(index, EMPTY_SLOT_ITEM);
        i.setItem(48, PREVIOUS_BUTTON);
        i.setItem(49, BACK_BUTTON);
        i.setItem(50, NEXT_BUTTON);

        // populate guide menu with UberItems and UberMaterials
        int counter = 0;
        int slot = 0;
        for (String name : UberItems.getItemNames()) {
            if (slot >= CRAFTING_GUIDE_ITEM_SLOTS.size()) break;
            if (name.equals("uber_workbench")) continue;
            if (name.equals("null")) continue;

            if (counter < page * ITEMS_PER_GUIDE_PAGE || counter >= ITEMS_PER_GUIDE_PAGE * (page + 1)) {
                counter++; continue;
            }

            UberItem item = UberItems.getItem(name);
            i.setItem(CRAFTING_GUIDE_ITEM_SLOTS.get(slot), item.makeItem(1));
            slot++;
        }
        for (UberMaterial material : UberItems.getMaterials()) {
            if (slot >= CRAFTING_GUIDE_ITEM_SLOTS.size()) break;
            if (material.getName().equals("null")) continue;

            if (counter < page * ITEMS_PER_GUIDE_PAGE || counter >= ITEMS_PER_GUIDE_PAGE * (page + 1)) {
                counter++; continue;
            }

            i.setItem(CRAFTING_GUIDE_ITEM_SLOTS.get(slot), material.makeItem(1));
            slot++;
        }

        return i;
    }

    // creates a specific UberItem / UberMaterial's Guide Menu
    public static Inventory createUnboundCraftingTutorialMenu(ItemStack example, UberCraftingRecipe recipe) {
        Inventory i1 = createCustomCraftingMenu();
        Inventory i2 = Bukkit.createInventory(null, 45, "Guide - " + example.getItemMeta().getDisplayName());
        i2.setContents(i1.getContents());

        int amount = 1; if (recipe != null) amount = recipe.getCraftAmount();
        ItemStack example2 = example.clone();
        example2.setAmount(amount);
        i2.setItem(23, example2);

        // handle the specific crafting recipe
        List<Integer> exceptions = Arrays.asList(10,11,12,19,20,21,28,29,30);

        if (recipe == null) for (Integer exception : exceptions) i2.setItem(exception, EMPTY_ERROR_SLOT_ITEM);
        else for (int counter = 0; counter < exceptions.size(); counter++) i2.setItem(exceptions.get(counter), recipe.get(counter));

        return i2;

    }

    // MENU UTILITY FUNCTIONS

    // make the craftable UberItem appear in the crafted slot if the appropriate materials are there
    public static void checkCraft(Inventory i) {

        // prevent renamed chest dupe
        if (i.getLocation() != null) return;

        // put all crafting grid items into a list
        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));

        // check if any UberItem has a matching recipe to what's in the crafting grid
        for (UberItem item : UberItems.getItems()) {
            if (!item.hasCraftingRecipe()) continue;

            if (item.getCraftingRecipe().isEqual(items)) {
                ItemStack newItem = item.makeItem(item.getCraftingRecipe().getCraftAmount());
                Utilities.storeStringInItem(newItem, "" + UUID.randomUUID(), "crafting_display_item");
                i.setItem(23, newItem);
                return;
            }
            else i.setItem(23, MenuUtils.CRAFTING_SLOT_ITEM);
        }

        // check if any UberMaterial has a matching recipe to what's in the crafting grid
        for (UberMaterial item : UberItems.getMaterials()) {
            if (!item.hasCraftingRecipe()) continue;

            if (item.getCraftingRecipe().isEqual(items)) {
                ItemStack newItem = item.makeItem(item.getCraftingRecipe().getCraftAmount());
                Utilities.storeStringInItem(newItem, "" + UUID.randomUUID(), "crafting_display_item");
                i.setItem(23, newItem);
                return;
            }
            else i.setItem(23, MenuUtils.CRAFTING_SLOT_ITEM);
        }
    }

    // attempt to pull crafted item from the crafting grid
    public static void pullItem(InventoryClickEvent event) {
        // cancel vanilla event, because it allows me to process the itemstack properly
        event.setCancelled(true);

        // enforce clicking on an actual item in the crafting grid
        if (event.getCurrentItem() == CRAFTING_SLOT_ITEM) {
            event.setCancelled(true); return;
        }

        // make a list from the items in the crafting grid slots
        Inventory i = event.getInventory();
        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));

        if (Utilities.isUber(i.getItem(23))) pullUberItem(event, items, i.getItem(23), event.getCursor());
        if (Utilities.isUberMaterial(i.getItem(23))) pullUberMaterial(event, items, i.getItem(23), event.getCursor());

        // update inventory on a 1 tick delay as to prevent visual bugs clientside
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> ((Player) event.getWhoClicked()).updateInventory(), 1);
    }

    private static void pullUberItem(InventoryClickEvent event, List<ItemStack> items, ItemStack product, ItemStack cursor) {
        UberItem uber = Utilities.getUber(product);

        // enforce premium vs lite, item rarity perms, item specific perms
        if (Utilities.enforcePermissions((Player)event.getWhoClicked(), Utilities.getUber(event.getCurrentItem()))) {
            event.setCancelled(true); return;
        }

        // prevent null, double check if the item should be crafted
        if (uber == null || !uber.getCraftingRecipe().isEqual(items)) return;

        // enforce stack-ability and clicking onto pre-existing stacks
        if (cursor.getType() != Material.AIR && ((cursor.getAmount() == 1 && !uber.isStackable()) ||
                cursor.getAmount() == uber.getMaterial().getMaxStackSize())) return;

        // enforce that the held item (if any) is the same type as the product (prevent clicking with a different item)
        if (cursor.getType() != Material.AIR && !uber.compare(cursor)) return;

        // handle giving the items to the player

        // process shift clicking separately
        if (event.getClick() == ClickType.SHIFT_LEFT) {

            // loop the crafting process until out of materials
            int stack = 0; while (stack < uber.getMaterial().getMaxStackSize()) {
                if (!uber.getCraftingRecipe().isEqual(items)) break; // cancel loop if can no longer craft items
                // remove components, add 1 to number of successful crafts
                uber.getCraftingRecipe().consumeMaterials(items); stack += product.getAmount();
            } final int s = stack;

            Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { Utilities.givePlayerItemSafely((Player) event.getWhoClicked(), uber.makeItem(s)); }, 1);
            return;
        }

        // the player has the go-ahead to pull the item, delete the components
        uber.getCraftingRecipe().consumeMaterials(items);

        // cursor is empty
        if (cursor.getType() == Material.AIR) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { event.getWhoClicked().setItemOnCursor(uber.makeItem(product.getAmount()));}, 1);
        }
        // cursor has similar item to product, but only process if combined quantity <= max stack size
        else {
            if (cursor.getAmount() + product.getAmount() <= uber.getMaterial().getMaxStackSize()) {
                int newAmount = product.getAmount() + cursor.getAmount();
                Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { event.getWhoClicked().setItemOnCursor(uber.makeItem(newAmount));}, 1);
            }
        }

    }
    private static void pullUberMaterial(InventoryClickEvent event, List<ItemStack> items, ItemStack product, ItemStack cursor) {
        UberMaterial uber = Utilities.getUberMaterial(product);
        // prevent null, double check if the item should be crafted
        if (uber == null || !uber.getCraftingRecipe().isEqual(items)) return;

        // enforce stack-ability and clicking onto pre-existing stacks
        if (cursor.getType() != Material.AIR && ((cursor.getAmount() == 1 && !uber.isStackable()) ||
                cursor.getAmount() == uber.getMaterial().getMaxStackSize())) return;

        // enforce that the held item (if any) is the same type as the product (prevent clicking with a different item)
        if (cursor.getType() != Material.AIR && !uber.compare(cursor)) return;

        // handle giving the items to the player

        // process shift clicking separately
        if (event.getClick() == ClickType.SHIFT_LEFT) {

            // loop the crafting process until out of materials
            int stack = 0; while (stack < uber.getMaterial().getMaxStackSize()) {
                if (!uber.getCraftingRecipe().isEqual(items)) break; // cancel loop if can no longer craft items
                // remove components, add 1 to number of successful crafts
                uber.getCraftingRecipe().consumeMaterials(items); stack += product.getAmount();
            } final int s = stack;

            Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { Utilities.givePlayerItemSafely((Player)event.getWhoClicked(), uber.makeItem(s)); }, 1);
            return;
        }

        // the player has the go-ahead to pull the item, delete the components
        uber.getCraftingRecipe().consumeMaterials(items);

        // cursor is empty
        if (cursor.getType() == Material.AIR) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { event.getWhoClicked().setItemOnCursor(uber.makeItem(product.getAmount()));}, 1);
        }
        // cursor has similar item to product, but only process if combined quantity <= max stack size
        else {
            if (cursor.getAmount() + product.getAmount() <= uber.getMaterial().getMaxStackSize()) {
                int newAmount = product.getAmount() + cursor.getAmount();
                Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), () -> { event.getWhoClicked().setItemOnCursor(uber.makeItem(newAmount));}, 1);
            }
        }

    }

}
