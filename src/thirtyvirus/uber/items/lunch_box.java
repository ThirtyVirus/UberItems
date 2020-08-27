package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Bukkit;
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
	//  tweak FoodLevelChange.java to properly adjust saturation

	public lunch_box(UberItems main, int id, UberRarity rarity, String name, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
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
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) {

		// verify that the item is compatible with the lunchbox
		ItemStack item = event.getWhoClicked().getItemOnCursor();
		ItemStack uber = event.getCurrentItem();
		if (!(item.getType().isEdible() || item.getType() == Material.MELON)) return;

		// get the current saturation from the lunch box
		int saturation = Utilities.getIntFromItem(getMain(), uber, "saturation");
		Bukkit.getLogger().info("" + saturation);

		// add the appropriate amount of saturation to the total
		saturation = addSaturation(saturation, item, item.getAmount());

		// save the new saturation amount in the item, update lore
		player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1, 1);
		Utilities.storeIntInItem(getMain(), uber, saturation, "saturation");
		updateLore(uber);
		Bukkit.getLogger().info("" + saturation);

		// delete the item being clicked onto the Uber Item
		event.getWhoClicked().setItemOnCursor(null);
		event.setCancelled(true);
	}

	public void activeEffect(Player player, ItemStack item) { }

	// values based on "effective quality" from Minecraft wiki
	public int addSaturation(double saturation2, ItemStack i, int amount){
		double saturation = saturation2;
		switch (i.getType()) {

			case APPLE: saturation += 6.4 * amount; break;
			case BAKED_POTATO: saturation += 11 * amount; break;
			case BEETROOT: saturation += 2.2 * amount; break;
			case BEETROOT_SOUP: saturation += 13.2 * amount; break;
			case BREAD: saturation += 11 * amount; break;
			case CAKE: saturation += 16.8 * amount; break;
			case CARROT: saturation += 6.6 * amount; break;
			case CHORUS_FRUIT: saturation += 6.4 * amount; break;
			case TROPICAL_FISH: saturation += 1.2 * amount; break;
			case COOKED_CHICKEN: saturation += 13.2 * amount; break;
			case COOKED_COD:  saturation += 11 * amount; break;
			case COOKED_SALMON: saturation += 15.6 * amount; break;
			case COOKED_MUTTON: saturation += 15.6 * amount; break;
			case COOKED_PORKCHOP: saturation += 20.8 * amount; break;
			case COOKED_RABBIT: saturation += 11 * amount; break;
			case COOKIE: saturation += 2.4 * amount; break;
			case DRIED_KELP: saturation += 1.6 * amount; break;
			case GOLDEN_APPLE: saturation += 13.6 * amount; break;
			case GOLDEN_CARROT: saturation += 20.4 * amount; break;
			case MELON_SLICE: saturation += 3.2 * amount; break;
			case MELON: saturation += 28.8 * amount; break;
			case MUSHROOM_STEW: saturation += 13.2 * amount; break;
			case POTATO: saturation += 1.6 * amount; break;
			case POISONOUS_POTATO: saturation += 3.2 * amount; break;
			case PUFFERFISH: saturation += 1.2 * amount; break;
			case PUMPKIN_PIE: saturation += 12.8 * amount; break;
			case RABBIT_STEW: saturation += 22 * amount; break;
			case BEEF: saturation += 4.8 * amount; break;
			case CHICKEN: saturation += 3.2 * amount; break;
			case COD: saturation += 2.4 * amount; break;
			case SALMON: saturation += 2.4 * amount; break;
			case MUTTON: saturation += 3.2 * amount; break;
			case PORKCHOP: saturation += 4.8 * amount; break;
			case RABBIT: saturation += 4.8 * amount; break;
			case ROTTEN_FLESH: saturation += 4.8 * amount; break;
			case SPIDER_EYE: saturation += 5.2 * amount; break;
			case COOKED_BEEF: saturation += 20.8 * amount; break;
			default:
				break;
		}

		return (int) saturation;
	}

}
