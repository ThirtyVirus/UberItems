package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

// a template class that can be copy - pasted and renamed when making new Uber Items
public class hackerman extends UberItem {

    public hackerman(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public void leftClickAirAction(Player player, ItemStack item) { }
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void rightClickAirAction(Player player, ItemStack item) {
        // status = 0 means off, 1 means on
        if (Utilities.getIntFromItem(item, "status") == 0) {
            Utilities.addEnchantGlint(item);
            Utilities.storeIntInItem(item, 1, "status");
        }
        else {
            ItemMeta meta = item.getItemMeta();
            for (Enchantment e : meta.getEnchants().keySet()) {
                meta.removeEnchant(e);
            }
            item.setItemMeta(meta);
            Utilities.storeIntInItem(item, 0, "status");
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 1, 2);
    }
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        rightClickAirAction(player, item);
    }
    public void shiftLeftClickAirAction(Player player, ItemStack item) { }
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void shiftRightClickAirAction(Player player, ItemStack item) { }
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void middleClickAction(Player player, ItemStack item) { }
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
    public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
    public void clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {
        if (UberItems.materials.get("enchanted_stone").compare(addition)) {
            Utilities.addUpgrade(item, "Compacted", "Your Hackerman is now... heavier?");
        }
        if (addition.getType() == Material.DIAMOND) {
            Utilities.addUpgrade(item, "Expensive", "Your Hackerman is now... more expensive???");
        }
        if (UberItems.materials.get("enchanted_cobblestone").compare(addition)) {
            Utilities.removeUpgrade(item, "Compacted");
        }
    }
    public void activeEffect(Player player, ItemStack item) {
        if (Utilities.getIntFromItem(item, "status") == 1) {
            Block b = player.getLocation().add(0,-1,0).getBlock();
            if (b.getType() == Material.AIR || b.getType() == Material.WATER) {
                Material old = b.getType();
                if (Utilities.hasUpgrade(item, "Compacted") && Utilities.hasUpgrade(item, "Expensive")) b.setType(Material.GOLD_BLOCK);
                else if (Utilities.hasUpgrade(item, "Compacted")) b.setType(Material.IRON_BLOCK);
                else b.setType(Material.STONE);
                Utilities.scheduleTask(new Runnable() { public void run() { b.setType(old); } }, 60);
            }
        }
    }
}
