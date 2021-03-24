package thirtyvirus.uber.helpers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import thirtyvirus.uber.UberItems;

import java.util.*;

import static thirtyvirus.uber.helpers.ActionSound.*;

public class SortingUtilities {

    // list of all supported inventory blocks in the plugin
    public static final List<Material> INVENTORY_BLOCKS = Arrays.asList(
            Material.CHEST,Material.TRAPPED_CHEST, Material.ENDER_CHEST, Material.SHULKER_BOX,
            Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX,
            Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX,
            Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX);

    private static Map<Player, Long> mostRecentSelect = new HashMap<>();

    // check if an inventory has the required parameters for a name
    public static boolean hasValidName(InventoryClickEvent e) {

        if (UberItems.useBlackList) {
            for (String str : UberItems.blackList) {
                if (e.getView().getTitle().contains(str)) {
                    return false;
                }
            }
        }

        if (UberItems.useWhiteList) {
            for (String str : UberItems.whiteList) {
                if (e.getView().getTitle().contains(str)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    // check if an inventory has the required parameters for a name
    public static boolean hasValidName(InventoryOpenEvent e) {

        if (UberItems.useBlackList) {
            for (String str : UberItems.blackList) {
                if (e.getView().getTitle().contains(str)) {
                    return false;
                }
            }
            return true;
        }

        if (UberItems.useWhiteList) {
            for (String str : UberItems.whiteList) {
                if (e.getView().getTitle().contains(str)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    // check to cancel a multisort if the allowed time between inventory selections expires
    public static void checkCancelMultisort(Map<Player, List<Block>> multisorts, int timedif) {
        Date date = new Date();
        for (Player player : multisorts.keySet()) {
            if (date.getTime() > mostRecentSelect.get(player) + timedif * 1000) {
                mostRecentSelect.remove(player);
                multisorts.remove(player);
                Utilities.playSound(ERROR, player);
            }

        }
    }

    // _______________________________________________________________ \\
    // main sorting functions

    // sort the inventory of a single container block
    // assumes the block given is a supported inventory block
    public static void sortBlock(Block block, Player player) {

        // enforce area build permissions
        //if (!UberItems.ignoreBuildPerms) {
        //    BlockBreakEvent e = new BlockBreakEvent(block, player);
        //    Bukkit.getServer().getPluginManager().callEvent(e);
        //    if (e.isCancelled()) {
        //        Utilities.warnPlayer(player, Arrays.asList(main.getPhrase("no-build-permissions-message")));
        //        e.setCancelled(true);
        //        return;
        //    }
        //}

        // chest
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest)block.getState();
            Inventory inv = chest.getInventory();

            // 1.14.4 support?
            if (inv.getHolder() instanceof DoubleChest) {
                sortDoubleChest((DoubleChest)inv.getHolder());
            } else {
                sortInventory(inv);
            }

        }

        // double chest
        if (block.getState() instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest)block.getState();
            sortInventory(chest.getInventory());
        }

        // ender chest
        if (block.getState() instanceof EnderChest) {
            sortInventory(player.getEnderChest());
        }

        // shulker box
        if (block.getState() instanceof ShulkerBox) {
            ShulkerBox box = (ShulkerBox)block.getState();
            sortInventory(box.getInventory());
        }

        Utilities.playSound(CLICK, player);
    }

    // sort a single inventory
    public static void sortInventory(Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();

        items = addItems(inventory, items);

        items.sort(new ItemComparator());
        items = naturalizeItemList(items);

        // sort items
        ItemStack sortedItems[] = new ItemStack[items.size()];
        int counter = 0;
        for (ItemStack i : items) {
            sortedItems[counter] = i;
            counter++;
        }

        // add sorted items back to inventory
        inventory.setContents(sortedItems);
    }

    // sort player inventory (exclude hotbar)
    public static void sortPlayerInventory(PlayerInventory inventory) {

        // get unsorted list of items
        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> armorAndOffhand = new ArrayList<>();
        for(ItemStack i : inventory.getArmorContents()) armorAndOffhand.add(i);

        int counter = 0;
        for (ItemStack i : inventory.getContents()){
            if (counter < 9) { counter++; continue; } // skip hotbar
            if (armorAndOffhand.contains(i)) { counter++; continue; } // skip armor
            if (inventory.getItemInOffHand().equals(i)) { counter++; continue; } // skip offhand

            counter++;
            if (i == null) continue;
            int index = containsMaterial(items, i);
            if (index == -1 || i.getMaxStackSize() == 1) {
                items.add(i.clone());
                i.setAmount(0);
            }
            else {
                items.get(index).setAmount(items.get(index).getAmount() + i.getAmount());
                i.setAmount(0);
            }
        }

        // sort and naturalize item list (proper stacking)
        items.sort(new ItemComparator());
        items = naturalizeItemList(items);
        ItemStack sortedItems[] = new ItemStack[items.size()];
        counter = 0;
        for (ItemStack i : items) {
            sortedItems[counter] = i;
            counter++;
        }

        // add sorted items back to inventory
        counter = 9;
        while (counter < inventory.getSize()) { counter++; }
        counter = 9;
        for (ItemStack item : sortedItems) {
            inventory.setItem(counter, item);
            counter++;
        }

    }

    // double chest inventory (need to process left and right side separately when placing items back in)
    public static void sortDoubleChest(DoubleChest doubleChest) {

        // convert contents into list of items
        List<ItemStack> items = new ArrayList<>();
        items = addItems(doubleChest.getInventory(), items);

        // sort item list
        items.sort(new ItemComparator());
        items = naturalizeItemList(items);

        // add sorted items back to inventory
        distributeItems(Arrays.asList(doubleChest.getLeftSide().getInventory(), doubleChest.getRightSide().getInventory()), items);
    }

    // _______________________________________________________________ \\
    // multi sorting functions

    // add a container block to a multi-sort
    public static void addSelectedBlock(Player player, Block block) {

        // make sure selected block is a container block
        if (!INVENTORY_BLOCKS.contains(block.getType())) return;

        // enforce area build permissions
        //if (!UberItems.ignoreBuildPerms) {
        //    BlockBreakEvent e = new BlockBreakEvent(block, player);
        //    Bukkit.getServer().getPluginManager().callEvent(e);
        //    if (e.isCancelled()) {
        //        Utilities.playSound(ERROR, player);
        //        e.setCancelled(true);
        //        return;
        //    }
        //}

        // add new empty list to player in map if none is found
        UberItems.multisorts.putIfAbsent(player, new ArrayList<>());
        // put block in the list
        UberItems.multisorts.get(player).add(block);
        Utilities.playSound(SELECT, player);
        Date date = new Date();
        mostRecentSelect.put(player, date.getTime());
    }

    // sort between multiple inventories at once (confirm process)
    public static void multiSort(Player player) {
        // verify that the player has pending inventories to sort
        if (UberItems.multisorts.get(player) == null) return;

        List<Inventory> inventories = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();

        // add all inventories to list
        for (Block block : UberItems.multisorts.get(player)) {

            // remove blocks that are not containers
            if (!INVENTORY_BLOCKS.contains(block.getType())) continue;

            // enforce area build permissions
            //if (!UberItems.ignoreBuildPerms) {
            //    BlockBreakEvent e = new BlockBreakEvent(block, player);
            //    Bukkit.getServer().getPluginManager().callEvent(e);
            //    if (e.isCancelled()) {
            //        e.setCancelled(true);
            //        continue;
            //    }
            //}

            // add container block inventories to list
            // double chest
            if (block.getState() instanceof DoubleChest) {
                DoubleChest chest = (DoubleChest)block.getState();
                safeAddInventory(inventories, chest.getInventory());
                continue;
            }
            // chest
            if (block.getState() instanceof Chest) {
                Chest chest = (Chest)block.getState();
                safeAddInventory(inventories, chest.getInventory());
                continue;
            }
            // ender chest
            if (block.getState() instanceof EnderChest) {
                safeAddInventory(inventories, player.getEnderChest());
                continue;
            }
            // shulker boxes
            if (block.getState() instanceof ShulkerBox) {
                ShulkerBox box = (ShulkerBox) ((ShulkerBox) block.getState()).getInventory().getHolder();
                safeAddInventory(inventories, box.getInventory());
            }

        }

        // add all items from inventories to items list
        for (Inventory inventory : inventories) {
            items = addItems(inventory, items);
        }

        // perform sort
        items.sort(new ItemComparator());
        items = naturalizeItemList(items);

        // add sorted items back to inventories
        distributeItems(inventories, items);

        UberItems.multisorts.remove(player);
        Utilities.playSound(MODIFY, player);
        mostRecentSelect.remove(player);
    }

    // cancel multi sort function
    public static void cancelMultisort(Player player) {
        if (!UberItems.multisorts.containsKey(player)) return;

        UberItems.multisorts.remove(player);
        Utilities.playSound(ERROR, player);
        mostRecentSelect.remove(player);
    }

    public static Location getInventoryLocation(Inventory inventory) {
        // double chest
        if (inventory.getHolder() instanceof DoubleChest) {
            DoubleChest chest = (DoubleChest) inventory.getHolder();

            BlockState state = (BlockState) chest.getLeftSide();
            return state.getBlock().getLocation();
        }
        // chest
        if (inventory.getHolder() instanceof Chest) {
            Chest chest = (Chest) inventory.getHolder();
            return chest.getBlock().getLocation();
        }
        // shulker boxes
        if (inventory.getHolder() instanceof ShulkerBox) {
            return inventory.getLocation();
        }
        return null;
    }

    // _______________________________________________________________ \\
    // helper sorting functions

    // add items from inventory's contents to list
    private static List<ItemStack> addItems(Inventory inv, List<ItemStack> items) {
        for (ItemStack i : inv.getContents()){
            if (i == null) continue;
            int index = containsMaterial(items, i);
            if (index == -1 || i.getMaxStackSize() == 1) {
                items.add(i);
            }
            else {
                items.get(index).setAmount(items.get(index).getAmount() + i.getAmount());
            }
        }
        return items;
    }
    // spread out ItemStack amounts to 64 max
    private static List<ItemStack> naturalizeItemList(List<ItemStack> items) {
        ArrayList<ItemStack> newItems = new ArrayList<>();

        for (ItemStack i : items){

            int amount = i.getAmount();
            int stack = i.getMaxStackSize();
            while (amount > stack){

                // make new item that is max stack size quantity
                ItemStack newItem = i.clone();
                newItem.setAmount(stack);
                newItems.add(newItem);

                // subtract max stack size from main item stack
                amount -= stack;
                i.setAmount(amount);
            }
            if (i.getAmount() > 0) newItems.add(i);

        }

        return newItems;
    }
    // test if list of items contains exact copy of item
    private static int containsMaterial(List<ItemStack> items, ItemStack i) {

        int index = 0;
        for (ItemStack item : items){
            if (item.isSimilar(i)) return index;
            index++;
        }
        return -1; // this means there are no stacks of that item in the list
    }
    // test if an inventory has a free slot
    private static boolean hasFreeSlot(Inventory inventory) {
        for (ItemStack item : inventory) {
            if (item == null) return true;
        }

        return false;
    }
    // safely add inventory to list (prevent duplicates and double chest confusion)
    private static void safeAddInventory(List<Inventory> inventories, Inventory inventory) {

        if (inventories.contains(inventory)) return;
        for (Inventory inv : inventories) {
            if (inv.getHolder() == inventory.getHolder()) return;

            // Double Chests
            if (inventory.getHolder() instanceof DoubleChest) {
                if (Arrays.equals(inv.getContents(), inventory.getContents()) && inv.getLocation().equals(inventory.getLocation())) return;
            }

        }

        inventories.add(inventory);
    }
    // spread out ItemStacks between multiple inventories
    private static void distributeItems(List<Inventory> inventories, List<ItemStack> items) {

        for (Inventory inventory : inventories) { inventory.clear(); }

        int inventoryNum = 0;
        for (ItemStack item : items) {
            if (item == null) continue;

            if (hasFreeSlot(inventories.get(inventoryNum))) { inventories.get(inventoryNum).addItem(item); }
            else { inventoryNum++; inventories.get(inventoryNum).addItem(item); }
        }
    }
}
