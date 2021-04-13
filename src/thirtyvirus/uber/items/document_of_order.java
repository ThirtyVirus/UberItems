package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.*;

import static thirtyvirus.uber.helpers.ActionSound.CLICK;

public class document_of_order extends UberItem  {

	// TODO make the sort respect area build permissions.
	// TODO make smart sort smarter

	public document_of_order(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.MENDING, 10);
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }

	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// enforce multi sort enabled setting
		if (!UberItems.multiSort) return;

		// multi inventory sort selection function (left click container)
		if (SortingUtilities.INVENTORY_BLOCKS.contains(event.getClickedBlock().getType())) {
			SortingUtilities.addSelectedBlock(event.getPlayer(), Utilities.getBlockLookingAt(event.getPlayer()));
		}
		// multi inventory sort confirmation function (left click non-container)
		else {
			SortingUtilities.multiSort(event.getPlayer());
		}

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}

	public void rightClickAirAction(Player player, ItemStack item) { }
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftLeftClickAirAction(Player player, ItemStack item) { }

	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// multi inventory sort cancel function (sneak + left click non-container)
		if (!SortingUtilities.INVENTORY_BLOCKS.contains(event.getClickedBlock().getType())) {
			SortingUtilities.cancelMultisort(event.getPlayer());
		}
	}
	public void shiftRightClickAirAction(Player player, ItemStack item) {
		// sort the player's inventory
		SortingUtilities.sortPlayerInventory(player.getInventory());
		Utilities.playSound(CLICK, player);

		// confirm that the item's ability has been successfully used
		onItemUse(player, item);
	}
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// enforce external sorting setting
		if (!UberItems.externalSort) return;

		// sort a single inventory
		if (SortingUtilities.INVENTORY_BLOCKS.contains(event.getClickedBlock().getType())) {
			SortingUtilities.sortBlock(event.getClickedBlock(), event.getPlayer());
		}

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}

	public void middleClickAction(Player player, ItemStack item) { }
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { }

	public void activeEffect(Player player, ItemStack item) { }
}