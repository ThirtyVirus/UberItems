package thirtyvirus.uber.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.UberDrop;
import thirtyvirus.uber.helpers.Utilities;

import java.util.UUID;

public class MiscEvents implements Listener {

    // prevent accidentally placing UberItems as Vanilla Blocks
    @EventHandler
    private void onBlockPlace (BlockPlaceEvent event) {
        if (Utilities.isUber(event.getItemInHand())) event.setCancelled(true);
        if (Utilities.isUberMaterial(event.getItemInHand())) event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (Utilities.temporaryBlocks.contains(event.getBlock())) event.setCancelled(true);
    }

    // ensure that players have the proper bonus attack speed when they log in
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Utilities.applyBonusAttackSpeed(player);
    }

    // drop heads on player death
    @EventHandler
    private void playerDeath(PlayerDeathEvent event) {

        // only drop player heads if "defeated players heads" are a valid UberMaterial
        if (UberItems.getMaterial("null").compare(UberItems.getMaterial("defeated_player_head").makeItem(1))) return;

        Player killer = event.getEntity().getKiller();
        Player killedPlayer = event.getEntity();
        if (killer != null) {

            // Asynchronously fetch the skin texture and apply it to the head
            Bukkit.getScheduler().runTaskAsynchronously(UberItems.getInstance(), () -> {
                UUID killedPlayerUUID = killedPlayer.getUniqueId();
                String textureUrl = Utilities.getSkullFromUUID(killedPlayerUUID);

                // Ensure you run the following part on the main thread
                Bukkit.getScheduler().runTask(UberItems.getInstance(), () -> {

                    ItemStack head = UberItems.getMaterial("defeated_player_head").makeItem(1);
                    Utilities.setSkull(head, textureUrl);
                    Utilities.nameItem(head, ChatColor.GOLD + killedPlayer.getName() + "'s Head");

                    UberDrop drop = new UberDrop(head, 100, true);
                    drop.tryDrop(killedPlayer.getLocation(), killer);
                });
            });
        }
    }

    // prevent renaming UberItems or UberMaterials with an anvil
    // TODO fix this to not prevent anvil enchant combining? (do I need to prevent renaming?)
//    @EventHandler
//    private void playerRenameItem(InventoryClickEvent event){
//        Player player = (Player) event.getWhoClicked();
//        if (event.getView().getType() == InventoryType.ANVIL) {
//            if (event.getRawSlot() == 2) {
//                if (event.getView().getItem(0).getType() != Material.AIR && event.getView().getItem(2).getType() != Material.AIR) {
//                    if (Utilities.isUber(event.getView().getItem(0)) || Utilities.isUber(event.getView().getItem(1)) || Utilities.isUber(event.getView().getItem(2))) {
//                        event.setCancelled(true);
//                        Utilities.playSound(ActionSound.ERROR, player);
//                    }
//                    if (Utilities.isUberMaterial(event.getView().getItem(0)) || Utilities.isUberMaterial(event.getView().getItem(1)) || Utilities.isUberMaterial(event.getView().getItem(2))) {
//                        event.setCancelled(true);
//                        Utilities.playSound(ActionSound.ERROR, player);
//                    }
//                }
//            }
//        }
//    }

    // prevent crafting using UberItems or (unwanted) UberMaterials into vanilla recipes
    @EventHandler
    private void playerCraftEvent(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack[] item = event.getInventory().getMatrix();

        for (int counter = 0; counter < 9; counter++) {

            // prevent crash with the small manual grid
            if (counter >= item.length) return;

            if (Utilities.isUber(item[counter])) {
                event.setCancelled(true);
                Utilities.playSound(ActionSound.ERROR, player);
                return;
            }

            if (Utilities.isUberMaterial(item[counter])) {
                UberMaterial um = Utilities.getUberMaterial(item[counter]);
                if (um == null || !um.isVanillaCraftable()) {
                    event.setCancelled(true);
                    Utilities.playSound(ActionSound.ERROR, player);
                    return;
                }
            }

        }
    }

    // cancel players consuming UberItems that are food ItemStacks
    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (Utilities.isUber(item)) event.setCancelled(true);
        if (Utilities.isUberMaterial(item)) event.setCancelled(true);
    }

    // prevent uberitems from being smelted
    @EventHandler
    private static void onUberSmelt(FurnaceSmeltEvent event) {
        if (Utilities.isUber(event.getSource()) && Utilities.getIntFromItem(event.getSource(), "smeltable") == 0)
            event.setCancelled(true);
    }

    // backpack event handlers
    @EventHandler
    private static void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("UberItems - Backpack")) return;

        if (UberItems.getItem("small_backpack").compare(event.getCurrentItem())) event.setCancelled(true);
        if (UberItems.getItem("big_backpack").compare(event.getCurrentItem())) event.setCancelled(true);
        if (Utilities.emptySlot.equals(event.getCurrentItem())) event.setCancelled(true);

        // cancel all inventory swap actions to prevent putting a backpack inside itself
        if (event.getAction().name().contains("HOTBAR")) { event.setCancelled(true); }
    }
    @EventHandler
    private static void onInventoryClose(InventoryCloseEvent event) {
        // verify that the closed inventory is a backpack
        if (!event.getView().getTitle().equals("UberItems - Backpack")) return;

        Inventory backpackInv = event.getInventory();
        Inventory playerInv = event.getPlayer().getInventory();

        // store the backpack inventory in the itemstack, place back in player inventory
        ItemStack[] items = new ItemStack[backpackInv.getSize() - 9];
        for (int counter = 9; counter < backpackInv.getSize(); counter++) {
            items[counter - 9] = backpackInv.getItem(counter);
        }
        ItemStack backpack = backpackInv.getItem(4);
        if (backpack != null) {
            Utilities.saveCompactInventory(backpack, items);

            String UUID = Utilities.getStringFromItem(backpack, "UUID");
            boolean alreadyFinished = false;
            for (int counter = 0; counter < playerInv.getSize(); counter++) {
                ItemStack item = playerInv.getItem(counter);
                if (UUID.equals(Utilities.getStringFromItem(item, "UUID"))) {
                    // prevent duping backpacks, delete duplicates
                    if (alreadyFinished)
                        playerInv.setItem(counter, new ItemStack(Material.AIR));
                    else {
                        playerInv.setItem(counter, backpack.clone());
                        alreadyFinished = true;
                    }

                }
            }

            // backpack inventory closed and the backpack is no longer in the inventory (DUPE ATTEMPTED)
            if (!alreadyFinished) {
                Bukkit.getLogger().info("hi");
                for (ItemStack item : playerInv) {
                    // DELETE ALL ITEMS FROM MISSING BACKPACK
                    int fromBackpack = 0;
                    if (item != null) fromBackpack = Utilities.getIntFromItem(item, "frombackpack");
                    if (fromBackpack == 1) playerInv.remove(item);
                }
            }
            else {
                for (ItemStack item : playerInv) Utilities.storeIntInItem(item, 0, "frombackpack");
            }

            Player player = (Player)event.getPlayer();
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }

    }

}
