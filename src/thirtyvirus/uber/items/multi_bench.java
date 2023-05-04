package thirtyvirus.uber.items;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.*;

import static javax.swing.UIManager.put;

public class multi_bench extends UberItem {

    public multi_bench(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { Utilities.addEnchantGlint(item); }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) {

        String upgrade = "none";
        switch (item.getType()) {
            case CRAFTING_TABLE: upgrade = "Crafting"; break;
            case FURNACE: upgrade = "Smelting"; break;
            case BREWING_STAND: upgrade = "Brewing"; break;
            case ANVIL: upgrade = "Smithing"; break;
            case ENCHANTING_TABLE: upgrade = "Enchanting"; break;
        }
        if (!upgrade.equals("none")) {
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        }

        switch (getNext(item, Arrays.asList(Utilities.getUpgrades(item)), upgrade)) {
            case "Crafting": item.setType(Material.CRAFTING_TABLE); break;
            case "Smelting": item.setType(Material.FURNACE); break;
            case "Brewing": item.setType(Material.BREWING_STAND); break;
            case "Smithing": item.setType(Material.ANVIL); break;
            case "Enchanting": item.setType(Material.ENCHANTING_TABLE); break;
        }

        return false;
    }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return leftClickAirAction(player, item); }

    public boolean rightClickAirAction(Player player, ItemStack item) {
        switch (item.getType()) {
            case CRAFTING_TABLE: player.openInventory(Bukkit.createInventory(player, InventoryType.WORKBENCH)); break;
            case FURNACE: player.openInventory(Bukkit.createInventory(player, InventoryType.FURNACE)); break;
            case BREWING_STAND: player.openInventory(Bukkit.createInventory(player, InventoryType.BREWING)); break;
            case ANVIL: player.openInventory(Bukkit.createInventory(player, InventoryType.ANVIL)); break;
            case ENCHANTING_TABLE: player.openInventory(Bukkit.createInventory(player, InventoryType.ENCHANTING)); break;
        }

        return false;
    }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return rightClickAirAction(player, item); }

    public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
    public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {

        if (UberItems.getMaterial("enchanted_crafting_table").compare(addition)) Utilities.applyUpgrade(player, event, item, "Crafting", "Transmute into a Crafting Table");
        else if (UberItems.getMaterial("enchanted_furnace").compare(addition)) Utilities.applyUpgrade(player, event, item, "Smelting", "Transmute into a Furnace");
        else if (UberItems.getMaterial("enchanted_brewing_stand").compare(addition)) Utilities.applyUpgrade(player, event, item, "Brewing", "Transmute into a Brewing Stand");
        else if (UberItems.getMaterial("enchanted_anvil").compare(addition)) Utilities.applyUpgrade(player, event, item, "Smithing", "Transmute into an Anvil");
        else if (UberItems.getMaterial("enchanted_enchanting_table").compare(addition)) Utilities.applyUpgrade(player, event, item, "Enchanting", "Transmute into an Enchanting Table");

        return false;
    }
    public boolean activeEffect(Player player, ItemStack item) { return false; }

    public String getNext(ItemStack item, List<String> upgrades, String upgrade) {
        String next = "";

        switch (upgrade) {
            case "none":
            case "Enchanting": next = "Crafting"; break;
            case "Crafting": next = "Smelting"; break;
            case "Smelting": next = "Brewing"; break;
            case "Brewing": next = "Smithing"; break;
            case "Smithing": next = "Enchanting"; break;
        }
        if (upgrades.contains(next)) return next;
        else return getNext(item, upgrades, next);
    }
}
