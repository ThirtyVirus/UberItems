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
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class escape_rope extends UberItem{

	public escape_rope(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// teleport player to the last known location exposed to the sky
	public void rightClickAirAction(Player player, ItemStack item) {

		// if the player isn't outside
		if (player.getWorld().getHighestBlockYAt(player.getLocation()) != player.getLocation().getY()) {
			Location destination = Utilities.fromLocString(Utilities.getStringFromItem(item, "destination"));
			if (destination != null) {
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
				player.teleport(destination);
				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			}
		}

		onItemUse(player, item); // confirm that the item's ability has been successfully used
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
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }

	// check if player is exposed to sky
	public void activeEffect(Player player, ItemStack item) {
		if (player.getWorld().getHighestBlockYAt(player.getLocation()) + 1 == player.getLocation().getY()) {
			Utilities.storeStringInItem(item, Utilities.toLocString(player.getLocation()), "destination");
		}
	}
}