package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.ChatColor;
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

import org.bukkit.inventory.meta.PotionMeta;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class malk_bucket extends UberItem{

	public malk_bucket(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) {
		Utilities.storeStringInItem(item, "none", "potion-name");
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
		lore.add(ChatColor.GREEN + "Spiked with: " + ChatColor.GRAY + Utilities.getStringFromItem(item, "potion-name"));
	}

	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void rightClickAirAction(Player player, ItemStack item) { }
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftRightClickAirAction(Player player, ItemStack item) { }
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	public void middleClickAction(Player player, ItemStack item) { }
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }

	// apply potion effect to malk bucket
	public void clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {

		// verify that the item is compatible with the malk bucket
		if (!(addition.hasItemMeta() && addition.getItemMeta() instanceof PotionMeta)) return;

		// store the potion in the malk bucket
		ItemStack[] itemArray = new ItemStack[1]; itemArray[0] = addition; // store the potion as a 1 item inventory
		Utilities.saveCompactInventory(item, itemArray);

		// update lore and play confirmation sound
		player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		PotionMeta meta = (PotionMeta) addition.getItemMeta();
		if (meta.getDisplayName().equals(""))
			Utilities.storeStringInItem(item, meta.getBasePotionData().getType().name().replace('_', ' '), "potion-name");
		else
			Utilities.storeStringInItem(item, meta.getDisplayName(), "potion-name");
		updateLore(item);

		// delete the item being clicked onto the Uber Item
		event.getWhoClicked().setItemOnCursor(null);
		event.setCancelled(true);
	}

	public void activeEffect(Player player, ItemStack item) { }
}