package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;

public class lunch_box extends UberItem {

	//Constructor
	public lunch_box(int id, String name, List<String> lore, String description, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
	}
	//Left Click Air Action
	public void leftClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Left Click Block Action
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Right Click Air Action
	public void rightClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Right Click Block Action
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Shift Left Click Air Action
	public void shiftLeftClickAirAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Shift Left Click Block Action
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Shift Right Click Air Action
	public void shiftRightClickAirAction(Player player, ItemStack item) {
		//Open insert inventory
		Inventory inv = Bukkit.createInventory(player, 9, UberItems.prefix + ChatColor.DARK_GRAY + "Insert Food into Lunch Box!");
		ItemStack menuBlock = UberItems.nameItem(Material.HOPPER, ChatColor.RED + "Click on Food Below!");
		
		inv.setItem(0, menuBlock);
		inv.setItem(1, menuBlock);
		inv.setItem(2, menuBlock);
		inv.setItem(3, menuBlock);
		inv.setItem(4, menuBlock);
		inv.setItem(5, menuBlock);
		inv.setItem(6, menuBlock);
		inv.setItem(7, menuBlock);
		inv.setItem(8, menuBlock);
		player.openInventory(inv);
	}
	//Shift Right Click Block Action
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Middle Click Action
	public void middleClickAction(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	//Active Effect
	public void activeEffect(Player player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

}
