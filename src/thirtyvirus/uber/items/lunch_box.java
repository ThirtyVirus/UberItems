package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class lunch_box extends UberItem implements Listener {

	public lunch_box(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
		lore.add(ChatColor.GREEN + "Food: " + ChatColor.GRAY + Utilities.getIntFromItem(item, "food"));
		lore.add(ChatColor.GREEN + "Saturation: " + ChatColor.GRAY + Utilities.getIntFromItem(item, "saturation"));
	}
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean rightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean middleClickAction(Player player, ItemStack item) { return false; }
	public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
	public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }

	// click food items onto the lunch box in your inventory to insert food
	public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {

		// verify that the item is compatible with the lunchbox
		if (!(addition.getType().isEdible() || addition.getType() == Material.MELON)) return false;

		// get the current saturation from the lunch box
		int saturation = Utilities.getIntFromItem(item, "saturation");
		int food = Utilities.getIntFromItem(item, "food");

		// add the appropriate amount of saturation and food to the total
		food += getFood(addition.getType(), false) * addition.getAmount();
		saturation += getFood(addition.getType(), true) * addition.getAmount();

		// save the new saturation and food amounts in the item, update lore
		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);
		Utilities.storeIntInItem(item, saturation, "saturation");
		Utilities.storeIntInItem(item, food, "food");
		updateLore(item);

		// delete the item being clicked onto the Uber Item
		event.getWhoClicked().setItemOnCursor(null);
		event.setCancelled(true);

		return false;
	}

	// get the food and saturation value of a food item
	public int getFood(Material material, boolean saturation) {
		switch (material) {
			case APPLE:
			case CHORUS_FRUIT: if (saturation) return 2; else return 4;
			case BAKED_POTATO:
			case BREAD:
			case COOKED_COD:
			case COOKED_RABBIT: if (saturation) return 6; else return 5;
			case BEETROOT:
			case DRIED_KELP:
			case POTATO:
			case TROPICAL_FISH: return 1;
			case BEETROOT_SOUP:
			case COOKED_CHICKEN:
			case MUSHROOM_STEW:
			case SUSPICIOUS_STEW: if (saturation) return 7; else return 6;
			case CAKE: if (saturation) return 3; else return 14;
			case CARROT: if (saturation) return 4; else return 3;
			case COOKED_MUTTON:
			case COOKED_SALMON: if (saturation) return 10; else return 6;
			case COOKED_PORKCHOP:
			case COOKED_BEEF: if (saturation) return 13; else return 8;
			case COOKIE:
			case MELON_SLICE:
			case POISONOUS_POTATO:
			case MUTTON:
			case COD:
			case CHICKEN:
			case SALMON:
			case SWEET_BERRIES: if (saturation) return 1; else return 2;
			case ENCHANTED_GOLDEN_APPLE:
			case GOLDEN_APPLE: if (saturation) return 10; else return 4;
			case GOLDEN_CARROT: if (saturation) return 15; else return 6;
			case PUFFERFISH: if (saturation) return 0; else return 1;
			case PUMPKIN_PIE: if (saturation) return 5; else return 8;
			case RABBIT_STEW: if (saturation) return 12; else return 10;
			case BEEF:
			case PORKCHOP:
			case RABBIT: if (saturation) return 2; else return 3;
			case ROTTEN_FLESH: if (saturation) return 1; else return 4;
			case SPIDER_EYE: if (saturation) return 3; else return 2;
			default: return 0;
		}
	}

	public boolean activeEffect(Player player, ItemStack item) { return false; }

	// process lunch box ability
	@EventHandler
	private void onPlayerHungerChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();

		// check if the player has a lunch box
		ItemStack lunchBox = Utilities.searchFor(player.getInventory(), UberItems.getItem("lunch_box"));
		if (lunchBox == null) return;
		UberItem uber = Utilities.getUber(lunchBox);
		if (uber == null) return;

		// enforce permissions
		if (Utilities.enforcePermissions(player, uber, true)) return;

		// get food and saturation levels
		final int max = 20; // maximum food and saturation
		int availableFood = Utilities.getIntFromItem(lunchBox, "food");
		int availableSaturation = Utilities.getIntFromItem(lunchBox, "saturation");
		int playerFood = player.getFoodLevel();
		int playerSaturation = (int) player.getSaturation();

		// verify that the amount is urgent
		int foodNeeded = max - playerFood;
		int saturationNeeded = max - playerSaturation;
		if (foodNeeded < 5) return;

		// limit the given food and saturation to the amount available
		if (availableFood < foodNeeded) foodNeeded = availableFood;
		if (availableSaturation < saturationNeeded) saturationNeeded = availableSaturation;

		// subtract food and saturation from lunch box
		availableFood -= foodNeeded;
		availableSaturation -= saturationNeeded;

		// feed player
		player.setFoodLevel(playerFood + foodNeeded);
		player.setSaturation(playerSaturation + saturationNeeded);
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
		event.setCancelled(true); // prevents the food and saturation levels from being reset by the event

		// save the new saturation and food amounts in the item, update lore
		Utilities.storeIntInItem(lunchBox, availableFood, "food");
		Utilities.storeIntInItem(lunchBox, availableSaturation, "saturation");
		uber.updateLore(lunchBox);
	}
}
