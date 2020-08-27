package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
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

public class big_bucket extends UberItem {

	public big_bucket(UberItems main, int id, UberRarity rarity, String name, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	// swap the bucket mode
	public void leftClickAirAction(Player player, ItemStack item) {
		// swap mode of bucket
		switch (Utilities.getIntFromItem(getMain(), item, "mode")) {
			// empty bucket (take liquids)
			case 6:
				item.setType(Material.WATER_BUCKET);
				Utilities.storeIntInItem(getMain(), item, 1, "mode");
				break;
			// water bucket
			case 7:
				item.setType(Material.LAVA_BUCKET);
				Utilities.storeIntInItem(getMain(), item, 2, "mode");
				break;
			// lava bucket
			case 8:
				item.setType(Material.BUCKET);
				Utilities.storeIntInItem(getMain(), item, 0, "mode");
				break;
		}

	}

	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		event.setCancelled(true);
		leftClickAirAction(player, item);
	}

	public void rightClickAirAction(Player player, ItemStack item) { }

	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		Utilities.informPlayer(event.getPlayer(), Arrays.asList("mode: " + Utilities.getIntFromItem(getMain(), item, "mode"), "water: " + Utilities.getIntFromItem(getMain(), item, "water-count"), "lava: " + Utilities.getIntFromItem(getMain(), item, "lava-count"), ""));
	}

	public void shiftLeftClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	public void shiftRightClickAirAction(Player player, ItemStack item) {
		

	}
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	public void middleClickAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }

	public void activeEffect(Player player, ItemStack item) {
		// enforce item material (fights the game being weird with buckets)
		switch (Utilities.getIntFromItem(getMain(), item, "mode")) {
			// empty bucket (take liquids)
			case 0:
				changeMaterial(item, Material.BUCKET);
				break;
			// water bucket
			case 1:
				changeMaterial(item, Material.WATER_BUCKET);
				break;
			// lava bucket
			case 2:
				changeMaterial(item, Material.LAVA_BUCKET);
				break;
		}
	}

}
