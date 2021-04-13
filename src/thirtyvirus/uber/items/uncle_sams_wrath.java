package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class uncle_sams_wrath extends UberItem{

	public static ArrayList<Color> colors = new ArrayList<>();
	public static ArrayList<FireworkEffect.Type> types = new ArrayList<>();

	public uncle_sams_wrath(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	
		colors.addAll(Arrays.asList(Color.WHITE, Color.PURPLE, Color.RED, Color.GREEN, Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.LIME, Color.MAROON, Color.YELLOW, Color.SILVER, Color.TEAL, Color.ORANGE, Color.OLIVE, Color.NAVY, Color.BLACK));
		types.addAll(Arrays.asList(FireworkEffect.Type.BURST, FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.CREEPER, FireworkEffect.Type.STAR));
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// throw random firework
	public void rightClickAirAction(Player player, ItemStack item) {
	    Firework thrown = (Firework) player.getWorld().spawn(player.getEyeLocation(), Firework.class);
	    FireworkMeta meta = thrown.getFireworkMeta();
	    //use meta to customize the firework or add parameters to the method
	    
	    meta.setPower(1);
	    meta.addEffects(FireworkEffect.builder().with(getRandomType()).withColor(getRandomColor()).build());
	    thrown.setFireworkMeta(meta);
	    
	    thrown.setVelocity(player.getLocation().getDirection().multiply(1.0));

		Utilities.scheduleTask(new Runnable() { public void run() { thrown.detonate(); } }, 6);
		
		thrown.setCustomName("UberFirework");

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftRightClickAirAction(Player player, ItemStack item) { }
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void middleClickAction(Player player, ItemStack item) { }
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { }
	public void activeEffect(Player player, ItemStack item) { }
	
	// return random firework effect
	public FireworkEffect.Type getRandomType(){
		int size = types.size();
		Random ran = new Random();
		FireworkEffect.Type theType = types.get(ran.nextInt(size));
		 
		return theType;
	}
	
	// return random color
	public Color getRandomColor(){
		int size = colors.size();
		Random ran = new Random();
		Color color = colors.get(ran.nextInt(size));
		 
		return color;
	}
}
