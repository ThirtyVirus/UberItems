package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class pocket_portal extends UberItem {

	public pocket_portal(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// teleport player cross-dimension
	public void rightClickAirAction(Player player, ItemStack item) {

		// enforce the 2 minute cooldown of the teleport ability
		if (!Utilities.enforceCooldown(player, "teleport", 120, item, true)) return;

		// execute the teleport
		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 5, 1);
		player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 1));

		Utilities.scheduleTask(new Runnable() { public void run() {
			
			player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 5, 1);
			
			if (player.getLocation().getWorld().getName().contains("_nether")) {
				String newWorld = player.getLocation().getWorld().getName().replace("_nether", "");
				player.teleport(new Location(Bukkit.getWorld(newWorld), player.getLocation().getBlockX() * 8, player.getLocation().getBlockY(), player.getLocation().getBlockZ() * 8));
			}
			else {
				String newWorld = player.getLocation().getWorld().getName().replace("_the_end", "");
				player.teleport(new Location(Bukkit.getWorld(newWorld + "_nether"), player.getLocation().getBlockX() / 8, player.getLocation().getBlockY(), player.getLocation().getBlockZ() / 8));
			}
			
			player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 5, 1);
			
		} }, 40);

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	public void shiftRightClickAirAction(Player player, ItemStack item) {
		rightClickAirAction(player, item);
	}
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	public void middleClickAction(Player player, ItemStack item) { }
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
	public void activeEffect(Player player, ItemStack item) { }
}