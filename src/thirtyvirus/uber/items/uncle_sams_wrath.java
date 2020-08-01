package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;

public class uncle_sams_wrath extends UberItem{

	public UberItems main = null;
	
	public static ArrayList<Color> colors = new ArrayList<Color>();
	public static ArrayList<FireworkEffect.Type> types = new ArrayList<FireworkEffect.Type>();
	
	//Constructor
	public uncle_sams_wrath(UberItems main, int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(main, id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
	
		colors.addAll(Arrays.asList(Color.WHITE, Color.PURPLE, Color.RED, Color.GREEN, Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.LIME, Color.MAROON, Color.YELLOW, Color.SILVER, Color.TEAL, Color.ORANGE, Color.OLIVE, Color.NAVY, Color.BLACK));
		types.addAll(Arrays.asList(FireworkEffect.Type.BURST, FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.CREEPER, FireworkEffect.Type.STAR));
	
		this.main = main;
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

	    Firework thrown = (Firework) player.getWorld().spawn(player.getEyeLocation(), Firework.class);
	    FireworkMeta meta = thrown.getFireworkMeta();
	    //use meta to customize the firework or add parameters to the method
	    
	    meta.setPower(1);
	    meta.addEffects(FireworkEffect.builder().with(getRandomType()).withColor(getRandomColor()).build());
	    thrown.setFireworkMeta(meta);
	    
	    thrown.setVelocity(player.getLocation().getDirection().multiply(1.0));
	    
	    Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() { public void run() { thrown.detonate(); } }, 5);
		
		thrown.setCustomName("UberFirework");
		
		if (player.getGameMode() != GameMode.CREATIVE) destroy(item, 1);
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
	
	//Return random firework effect
	public FireworkEffect.Type getRandomType(){
		int size = types.size();
		Random ran = new Random();
		FireworkEffect.Type theType = types.get(ran.nextInt(size));
		 
		return theType;
	}
	
	//Return random color
	public Color getRandomColor(){
		int size = colors.size();
		Random ran = new Random();
		Color color = colors.get(ran.nextInt(size));
		 
		return color;
	}
}
