package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class escape_rope extends UberItem{

	//Constructor
	public escape_rope(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}

	@Override
	public void onItemStackCreate(ItemStack item) {
		// TODO Auto-generated method stub
	}

	@Override
	public void leftClickAirAction(Player player, ItemStack item) {

	}

	@Override
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {

	}

	@Override
	public void rightClickAirAction(Player player, ItemStack item) {
		if (player.getWorld().getHighestBlockYAt(player.getLocation()) != player.getLocation().getY()){
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

			Location destination = Utilities.fromLocString(Utilities.getStringFromItem(getMain(), item, "destination"));
			if (destination != null) player.teleport(destination);

			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

			if (player.getGameMode() != GameMode.CREATIVE) destroy(item, 1);
		}
		
	}

	@Override
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	@Override
	public void shiftLeftClickAirAction(Player player, ItemStack item) {

	}

	@Override
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {

	}

	@Override
	public void shiftRightClickAirAction(Player player, ItemStack item) {

	}

	@Override
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {

	}

	@Override
	public void middleClickAction(Player player, ItemStack item) {

	}

	@Override
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) {

	}

	@Override
	public void activeEffect(Player player, ItemStack item) {
		// check if player is exposed to sky
		if (player.getWorld().getHighestBlockYAt(player.getLocation()) + 1 == player.getLocation().getY()) {
			Utilities.storeStringInItem(getMain(), item, Utilities.toLocString(player.getLocation()), "destination");

		}
		
	}
}
