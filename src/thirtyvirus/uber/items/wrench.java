package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;

public class wrench extends UberItem{

	public static List<Material> supportedBlocks = Arrays.asList(Material.PISTON, Material.STICKY_PISTON, Material.DISPENSER, Material.DROPPER, Material.OBSERVER, Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG,
			Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG,
			Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_ACACIA_WOOD,
			Material.STRIPPED_BIRCH_WOOD, Material.STRIPPED_DARK_OAK_WOOD, Material.STRIPPED_JUNGLE_WOOD, Material.STRIPPED_OAK_WOOD, Material.STRIPPED_SPRUCE_WOOD, Material.BLACK_GLAZED_TERRACOTTA, Material.ACACIA_WOOD,
			Material.BIRCH_WOOD, Material.DARK_OAK_WOOD, Material.JUNGLE_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.STRIPPED_ACACIA_WOOD, Material.CARVED_PUMPKIN, Material.JACK_O_LANTERN, Material.QUARTZ_PILLAR,
			Material.BLUE_GLAZED_TERRACOTTA, Material.BROWN_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, 
			Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, 
			Material.WHITE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA, Material.REPEATER, Material.COMPARATOR, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
			Material.JUNGLE_FENCE_GATE, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.FURNACE, Material.ENDER_CHEST, Material.ANVIL, Material.HAY_BLOCK, Material.CHEST, Material.TRAPPED_CHEST, Material.RAIL,
			Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL);
	//Material.ACACIA_SLAB, Material.ACACIA_STAIRS, Material.BLACK_SHULKER_BOX
	
	//Constructor
	public wrench(UberItems main, int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(main, id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
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
		if (supportedBlocks.contains(block.getType()) || block.getType().name().contains("SLAB") || block.getType().name().contains("STAIRS") || block.getType().name().contains("SHULKER_BOX")){
			BlockBreakEvent e = new BlockBreakEvent(block, player);
			Bukkit.getServer().getPluginManager().callEvent(e);
			if (!e.isCancelled()) {
				String data = block.getBlockData().toString();
				//Bukkit.broadcastMessage(data);
				
				if (block.getType().name().contains("CHEST") && !data.contains("type=single")) return;
				
				if (data.contains("axis=x")) { data = data.replace("axis=x", "axis=y"); }
				else if (data.contains("axis=y")) { data = data.replace("axis=y", "axis=z"); }
				else if (data.contains("axis=z")) { data = data.replace("axis=z", "axis=x"); }
				else if (data.contains("facing=north")) { data = data.replace("facing=north", "facing=east"); }
				else if (data.contains("facing=east")) { data = data.replace("facing=east", "facing=south"); }
				else if (data.contains("facing=south")) { data = data.replace("facing=south", "facing=west"); }
				
				else if (data.contains("facing=west")) {
					
					if (!block.getType().name().contains("TERRACOTTA") && !block.getType().name().contains("PUMPKIN") && !block.getType().name().contains("JACK_O_LANTERN") && !block.getType().name().contains("REPEATER") && !block.getType().name().contains("COMPARATOR") && !block.getType().name().contains("STAIRS") && !block.getType().name().contains("GATE") && !block.getType().name().contains("CHEST") && !block.getType().name().contains("FURNACE") && !block.getType().name().contains("ANVIL")){
						data = data.replace("facing=west", "facing=up");
					}
					else {
						data = data.replace("facing=west", "facing=north");
					}
				}
				
				else if (data.contains("facing=up")) { data = data.replace("facing=up", "facing=down"); }
				else if (data.contains("facing=down")) { data = data.replace("facing=down", "facing=north"); }
				
				else if (data.contains("type=top")) { data = data.replace("type=top", "type=bottom"); }
				else if (data.contains("type=bottom")) { data = data.replace("type=bottom", "type=top"); }
				
				else if (data.contains("shape=north_south")) { data = data.replace("shape=north_south", "shape=east_west"); }
				
				else if (data.contains("shape=east_west")) {
					
					if (!block.getType().name().contains("ACTIVATOR") && !block.getType().name().contains("DETECTOR") && !block.getType().name().contains("POWERED")){
						data = data.replace("shape=east_west", "shape=south_east");
					}
					else {
						data = data.replace("shape=east_west", "shape=north_south");
					}
					
				}
				
				else if (data.contains("shape=south_east")) { data = data.replace("shape=south_east", "shape=south_west"); }
				else if (data.contains("shape=south_west")) { data = data.replace("shape=south_west", "shape=north_west"); }
				else if (data.contains("shape=north_west")) { data = data.replace("shape=north_west", "shape=north_east"); }
				else if (data.contains("shape=north_east")) { data = data.replace("shape=north_east", "shape=north_south"); }
				
				data = data.replace("CraftBlockData{", "");
				data = data.replace("}", "");
				//Bukkit.broadcastMessage(data);
				//Bukkit.broadcastMessage("-");
				block.setBlockData(Bukkit.createBlockData(data));
				
				player.playSound(block.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
			}
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		// TODO Auto-generated method stub
		
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
