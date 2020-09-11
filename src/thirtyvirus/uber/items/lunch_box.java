package thirtyvirus.uber.items;

import java.util.List;

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

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class lunch_box extends UberItem {

	// TODO fix to give hunger and saturation for faster healing
	//  fix the values for each food type in addSaturation()

	public lunch_box(UberItems main, int id, UberRarity rarity, String name, Material material, boolean canBreakBlocks, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, oneTimeUse, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
		lore.add(ChatColor.GREEN + "Food: " + ChatColor.GRAY + Utilities.getIntFromItem(getMain(), item, "food"));
		lore.add(ChatColor.GREEN + "Saturation: " + ChatColor.GRAY + Utilities.getIntFromItem(getMain(), item, "saturation"));
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

	// click food items onto the lunch box in your inventory to insert food
	// TODO make actually work lol
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) {

		// verify that the item is compatible with the lunchbox
		ItemStack item = event.getWhoClicked().getItemOnCursor();
		ItemStack uber = event.getCurrentItem();
		if (!(item.getType().isEdible() || item.getType() == Material.MELON)) return;

		// get the current saturation from the lunch box
		int saturation = Utilities.getIntFromItem(getMain(), uber, "saturation");
		int food = Utilities.getIntFromItem(getMain(), uber, "food");

		// add the appropriate amount of saturation and food to the total
		//if (item.getItemMeta() instanceof FoodMetaData) {
		//	FoodMetaData meta = (FoodMetaData) item.getItemMeta();
		//	food += meta.getFoodLevel() * item.getAmount();
		//	saturation += meta.getSaturationLevel() * item.getAmount();
		//}
		saturation += 10;
		food += 10;

		// save the new saturation and food amounts in the item, update lore
		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);
		Utilities.storeIntInItem(getMain(), uber, saturation, "saturation");
		Utilities.storeIntInItem(getMain(), uber, food, "food");
		updateLore(uber);

		// delete the item being clicked onto the Uber Item
		event.getWhoClicked().setItemOnCursor(null);
		event.setCancelled(true);
	}

	public void activeEffect(Player player, ItemStack item) { }
}
