package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.Utilities;

public class escape_rope extends UberItem{

	//Constructor
	public escape_rope(int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
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
		
		if(player.getWorld().getHighestBlockYAt(player.getLocation()) != player.getLocation().getY()){
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			player.teleport(Utilities.fromLocString(item.getItemMeta().getLore().get(1).substring(2)));
			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
			
			if (player.getGameMode() != GameMode.CREATIVE) destroy(item, 1);
		}

	}

	@Override
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		shiftRightClickAirAction(player, item);
	}

	@Override
	public void middleClickAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeEffect(Player player, ItemStack item) {
		//check if player is exposed to sky
		if(player.getWorld().getHighestBlockYAt(player.getLocation()) == player.getLocation().getY()){
			Utilities.loreItem(item, Arrays.asList("Last saw sky at:", ChatColor.GOLD + Utilities.toLocString(player.getLocation()), "Shift-Right-Click to teleport!"));
		}
		
	}
}
