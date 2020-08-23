package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;

public class pocket_portal extends UberItem{

	//Constructor
	public pocket_portal(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}

	@Override
	public void onItemStackCreate(ItemStack item) {
		// TODO Auto-generated method stub
	}

	@Override
	public void leftClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rightClickAirAction(Player player, ItemStack item) {
		
		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 5, 1);
		player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 1));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(super.getMain(), new Runnable() { public void run() {
			
			player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 5, 1);
			
			if (player.getLocation().getWorld().getName().contains("_nether")){
				String newWorld = player.getLocation().getWorld().getName().replace("_nether", "");
				player.teleport(new Location(Bukkit.getWorld(newWorld), player.getLocation().getBlockX() * 8, player.getLocation().getBlockY(), player.getLocation().getBlockZ() * 8));
			}
			else {
				String newWorld = player.getLocation().getWorld().getName().replace("_the_end", "");
				player.teleport(new Location(Bukkit.getWorld(newWorld + "_nether"), player.getLocation().getBlockX() / 8, player.getLocation().getBlockY(), player.getLocation().getBlockZ() / 8));
			}
			
			player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 5, 1);
			
		} }, 40);
		

	}

	@Override
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	@Override
	public void shiftLeftClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftRightClickAirAction(Player player, ItemStack item) {
		rightClickAirAction(player, item);
	}

	@Override
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	@Override
	public void middleClickAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeEffect(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	
}
