package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
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

public class hackerman extends UberItem implements Listener {

    public hackerman(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
    public boolean rightClickAirAction(Player player, ItemStack item) {
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
        return true;
    }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        return rightClickAirAction(player, item);
    }
    public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
    public boolean shiftRightClickAirAction(Player player, ItemStack item) { return rightClickAirAction(player, item); }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return rightClickAirAction(player, item); }
    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {
        if (UberItems.getMaterial("enchanted_stone").compare(addition))
            Utilities.applyUpgrade(player, event, item, "Compacted", "Your Hackerman is now... heavier?");

        if (addition.getType() == Material.DIAMOND)
            Utilities.applyUpgrade(player, event, item, "Expensive", "Your Hackerman is now... more expensive???");

        if (UberItems.getMaterial("enchanted_cobblestone").compare(addition))
            Utilities.unapplyUpgrade(player, event, item, "Compacted");

        return false;
    }
    public boolean activeEffect(Player player, ItemStack item) {
        if (Utilities.getIntFromItem(item, "status") == 1 && !player.isSneaking()) {

            Block block = player.getLocation().add(0,-1,0).getBlock(); Material material = Material.STONE;
            if (Utilities.hasUpgrade(item, "Compacted") && Utilities.hasUpgrade(item, "Expensive")) material = Material.GOLD_BLOCK;
            else if (Utilities.hasUpgrade(item, "Compacted")) material = Material.IRON_BLOCK;

            // maintain block under player until they move
            if (!block.getType().isSolid()) {
                Utilities.maintainBlockReplacement(player, block, block.getState(), material, 2, 1);
            }
            return true;
        }
        return false;
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
}
