package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.Utilities;

public class big_bucket extends UberItem {

	public big_bucket(int id, String name, List<String> lore, String description, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
	}

	public void leftClickAirAction(Player player, ItemStack item) {
		//swap mode of bucket
		if (item.getItemMeta().getLore().get(1).contains("Collect")){
			Utilities.loreItem(item, Arrays.asList(item.getItemMeta().getLore().get(0), ChatColor.GOLD + "Mode: Water", item.getItemMeta().getLore().get(2), item.getItemMeta().getLore().get(3)));
			player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
			return;
		}
		if (item.getItemMeta().getLore().get(1).contains("Water")){
			Utilities.loreItem(item, Arrays.asList(item.getItemMeta().getLore().get(0), ChatColor.GOLD + "Mode: Lava", item.getItemMeta().getLore().get(2), item.getItemMeta().getLore().get(3)));
			player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
			return;
		}
		if (item.getItemMeta().getLore().get(1).contains("Lava")){
			Utilities.loreItem(item, Arrays.asList(item.getItemMeta().getLore().get(0), ChatColor.GOLD + "Mode: Collect-Aura", item.getItemMeta().getLore().get(2), item.getItemMeta().getLore().get(3)));
			player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
			return;
		}
		if (item.getItemMeta().getLore().get(1).contains("Collect-Aura")){
			Utilities.loreItem(item, Arrays.asList(item.getItemMeta().getLore().get(0), ChatColor.GOLD + "Mode: Collect", item.getItemMeta().getLore().get(2), item.getItemMeta().getLore().get(3)));
			player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
			return;
		}
	}

	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		event.setCancelled(true);
		leftClickAirAction(player, item);
	}

	public void rightClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		if (block.getType() == Material.WATER){
			block.setType(Material.AIR);
		}
		if (block.getType() == Material.LAVA){
			block.setType(Material.AIR);
		}
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

	public void activeEffect(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	
	

}
