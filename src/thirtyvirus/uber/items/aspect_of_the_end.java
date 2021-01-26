package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

// a template class that can be copy - pasted and renamed when making new Uber Items
public class aspect_of_the_end extends UberItem{

    public aspect_of_the_end(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities) {
        super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public void leftClickAirAction(Player player, ItemStack item) { }
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

    // teleport ability
    public void rightClickAirAction(Player player, ItemStack item) {
        Location l = player.getLocation().clone();
        l.add(player.getEyeLocation().getDirection().multiply(8));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f,1f);
        player.teleport(l);

        if (Utilities.getIntFromItem(item, "has_teleported") == 0) {
            // change player speed
            player.setWalkSpeed(player.getWalkSpeed() + 0.05f);
            Utilities.storeIntInItem(item, 1, "has_teleported");

            // remove player speed after 3 seconds
            Utilities.scheduleTask(new Runnable() { public void run() {
                player.setWalkSpeed(player.getWalkSpeed() - 0.05f);
                Utilities.storeIntInItem(item, 0, "has_teleported");
            } }, 60);
        }

        onItemUse(player, item); // confirm that the item's ability has been successfully used
    }
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void shiftLeftClickAirAction(Player player, ItemStack item) { }
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void shiftRightClickAirAction(Player player, ItemStack item) { }
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void middleClickAction(Player player, ItemStack item) { }
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
    public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
    public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
    public void activeEffect(Player player, ItemStack item) { }
}
