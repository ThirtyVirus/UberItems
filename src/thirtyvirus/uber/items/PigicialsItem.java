package thirtyvirus.uber.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;

import java.util.List;

public class PigicialsItem extends UberItem {

    public PigicialsItem(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
        super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
        lore.add(ChatColor.GREEN + "this is a prefix blurb");
    }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public void leftClickAirAction(Player player, ItemStack item) { }
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void rightClickAirAction(Player player, ItemStack item) {
        player.playSound(player.getLocation(), Sound.ENTITY_PIG_DEATH, 1F, 1F);
    }
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { rightClickAirAction(player, item); }
    public void shiftLeftClickAirAction(Player player, ItemStack item) { }
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void shiftRightClickAirAction(Player player, ItemStack item) { }
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void middleClickAction(Player player, ItemStack item) { }
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
    public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
    public void activeEffect(Player player, ItemStack item) { }
}
