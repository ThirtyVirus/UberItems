package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;

public class builders_wand extends UberItem {
	// TODO /wandoops command to undo wand action
	// TODO simplify code and fix the wand taking too many blocks in survival

	public builders_wand(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void rightClickAirAction(Player player, ItemStack item) { }

	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		BlockFace face = event.getBlockFace();
		fillConnectedFaces(player, block, face, item);
	}

	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftRightClickAirAction(Player player, ItemStack item) { }
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void middleClickAction(Player player, ItemStack item) { }

	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
	public void activeEffect(Player player, ItemStack item) { }
	
	// main logic for builder's wand
	public void fillConnectedFaces(Player player, Block origin, BlockFace face, ItemStack item){
		
		ArrayList<Block> blocks = new ArrayList<>(); blocks.add(origin);
		Material fillMaterial = origin.getType();
		int blockLimit = 2048; int blocksPlaced = 0; boolean needBlocks = true;

		int blocksInInventory = countBlocks(player.getInventory(), origin.getType());
		if (player.getGameMode() == GameMode.CREATIVE) { needBlocks = false; }
		if (blocksInInventory < blockLimit && needBlocks) { blockLimit = blocksInInventory; }
		
		switch (face) {
		case NORTH: //Z-
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		case SOUTH: //Z+
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		case EAST: //X+
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		case WEST: //X-
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		case UP: //Y+
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() - 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() + 1, blocks.get(0).getZ() + 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() + 1, blocks.get(0).getZ()));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					Bukkit.broadcastMessage("can't place here!");
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		case DOWN: //Y-
			
			while(blocks.size() > 0 && blockLimit > 0){
				Block block1 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b1test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block1.getType() == fillMaterial && b1test.getType() == Material.AIR){ blocks.add(block1); }
				
				Block block2 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b2test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				if (block2.getType() == fillMaterial && b2test.getType() == Material.AIR){ blocks.add(block2); }
				
				Block block3 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b3test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() - 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block3.getType() == fillMaterial && b3test.getType() == Material.AIR){ blocks.add(block3); }
				
				Block block4 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b4test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block4.getType() == fillMaterial && b4test.getType() == Material.AIR){ blocks.add(block4); }
				
				Block block5 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b5test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block5.getType() == fillMaterial && b5test.getType() == Material.AIR){ blocks.add(block5); }
				
				Block block6 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() - 1));
				Block b6test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() - 1));
				if (block6.getType() == fillMaterial && b6test.getType() == Material.AIR){ blocks.add(block6); }
				
				Block block7 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ()));
				Block b7test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				if (block7.getType() == fillMaterial && b7test.getType() == Material.AIR){ blocks.add(block7); }
				
				Block block8 = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY(), blocks.get(0).getZ() + 1));
				Block b8test = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX() + 1, blocks.get(0).getY() - 1, blocks.get(0).getZ() + 1));
				if (block8.getType() == fillMaterial && b8test.getType() == Material.AIR){ blocks.add(block8); }
				
				Block fillBlock = player.getWorld().getBlockAt(new Location(player.getWorld(), blocks.get(0).getX(), blocks.get(0).getY() - 1, blocks.get(0).getZ()));
				
				if (canPlaceBlock(player, fillBlock.getLocation())){
					fillBlock.setType(fillMaterial); blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1; blocksPlaced++;
					if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), 1); }
					if (needBlocks && blocksPlaced == blockLimit) { break; }
				}
				else {
					blocks.removeIf(blocks.get(0)::equals);; blockLimit -= 1;
				}
			}
			break;
			
		}
		
		if (item.getDurability() > 1532){
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_HURT, 1, 1);
			return;
		}
		
		if (blocksPlaced != 0){
			if (needBlocks) { removeBlocks(player.getInventory(), origin.getType(), blocksPlaced); }
			
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_BULLET_HIT, 1, 1);
			player.getWorld().playEffect(player.getEyeLocation(), Effect.SMOKE, 0);
		}

	}
	
	// counts amount of blocks of type m in inventory inv
	public int countBlocks(Inventory inv, Material m){
		int blockAmount = 0;
		
		for (ItemStack item : inv){
			if (item != null){
				if (item.getType() == m){
					blockAmount += item.getAmount();
				}
			}
		}
		return blockAmount;
	}
	
	// remove blockAmount blocks of type m from inv
	public void removeBlocks(Inventory inv, Material m, int blockAmount){
		inv.removeItem(new ItemStack (m, blockAmount));
	}
	
	public boolean canPlaceBlock(Player player, Location l) {

		return true;
		
		//BlockBreakEvent e = new BlockBreakEvent(l.getWorld().getBlockAt(l), player);
		//Bukkit.getServer().getPluginManager().callEvent(e);
		////return !e.isCancelled();
		
	}
}

