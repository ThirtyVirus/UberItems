package thirtyvirus.uber.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

import java.util.Random;

public class MiscEvents implements Listener {

    private Random rand = new Random();

    // 10% chance to drop 1-3 calamari from squid kills
    @EventHandler
    private void onKillSquid(EntityDeathEvent event) {
        if (event.getEntity() instanceof Squid && !UberItems.getItem("calamari").equals(UberItems.getItem("null"))) {
            int r = rand.nextInt(100) + 1;
            int d = rand.nextInt(3) + 1;
            if (r < 20) event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), UberItems.getItem("calamari").makeItem(d));
        }
    }

    // handle soul anchor returning to the player once they die and respawn
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        ItemStack item = Utilities.searchFor(player.getInventory(), UberItems.getItem("soul_anchor"));
        if (item != null) {
            event.getDrops().remove(item);
            Utilities.storeStringInItem(item, Utilities.toLocString(event.getEntity().getLocation()), "deathloc");
            if (!event.getKeepInventory()) returnSoulAnchor(player, item);
        }
    }

    // loop until the player respawns to return the death anchor
    private void returnSoulAnchor(Player player, ItemStack item) {
        if (player.isDead()) Utilities.scheduleTask(()->returnSoulAnchor(player, item), 10);
        else player.getInventory().addItem(item);
    }

    // prevent hackerman from being used as a totem of undying
    @EventHandler
    private static void onTotemUse(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();

        if (UberItems.getItem("hackerman").compare(player.getInventory().getItemInMainHand()) ||
                UberItems.getItem("hackerman").compare(player.getInventory().getItemInOffHand())) {
            event.setCancelled(true);
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
