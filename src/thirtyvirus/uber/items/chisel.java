package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;

public class chisel extends UberItem{

	public static List<Material> stoneGroup = Arrays.asList(Material.STONE, Material.COBBLESTONE, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.DIORITE, Material.POLISHED_DIORITE,
			Material.ANDESITE, Material.POLISHED_ANDESITE, Material.GRANITE, Material.POLISHED_GRANITE, Material.MOSSY_COBBLESTONE, Material.SMOOTH_STONE);
	public static List<Material> sandGroup = Arrays.asList(Material.SAND, Material.SANDSTONE, Material.CHISELED_SANDSTONE, Material.CUT_SANDSTONE, Material.SMOOTH_SANDSTONE, Material.SANDSTONE_SLAB, Material.SANDSTONE_STAIRS, Material.RED_SAND,
			Material.RED_SANDSTONE, Material.CHISELED_RED_SANDSTONE, Material.CUT_RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.RED_SANDSTONE_SLAB, Material.RED_SANDSTONE_STAIRS);
	public static List<Material> stoneSlabGround = Arrays.asList(Material.AIR);
	public static List<Material> stoneStairGround = Arrays.asList(Material.AIR);
	public static List<Material> sandSlabGroup = Arrays.asList(Material.AIR);
	public static List<Material> sandStairGroup = Arrays.asList(Material.AIR);
	public static List<Material> woodGroup = Arrays.asList(Material.AIR);
	public static List<Material> woolGroup = Arrays.asList(Material.AIR);
	public static List<Material> glassGroup = Arrays.asList(Material.AIR);
	public static List<Material> glassPaneGroup = Arrays.asList(Material.AIR);
	public static List<Material> concreteGroup = Arrays.asList(Material.AIR);
	public static List<Material> coralGroup = Arrays.asList(Material.AIR);
	public static List<Material> diskGroup = Arrays.asList(Material.AIR);
	
	//Constructor
	public chisel(int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
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
		//Open chisel inventory
		
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
