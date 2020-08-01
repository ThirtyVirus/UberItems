package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.Utilities;

public class electromagnet extends UberItem{

	public static List<EntityType> repelTargets = Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.CREEPER,
			EntityType.DRAGON_FIREBALL, EntityType.ARROW, EntityType.FIREBALL, EntityType.FIREWORK, EntityType.DROWNED, EntityType.EGG, EntityType.ELDER_GUARDIAN, EntityType.BLAZE,
			EntityType.ENDERMAN, EntityType.EVOKER, EntityType.FISHING_HOOK, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HUSK, EntityType.LIGHTNING,
			EntityType.MAGMA_CUBE, EntityType.LLAMA_SPIT, EntityType.PHANTOM, EntityType.PRIMED_TNT, EntityType.ZOMBIFIED_PIGLIN, EntityType.POLAR_BEAR, EntityType.PUFFERFISH,
			EntityType.SHULKER, EntityType.SHULKER_BULLET, EntityType.SNOWBALL, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER,
			EntityType.WITHER_SKELETON, EntityType.WITHER_SKULL, EntityType.ZOMBIE_VILLAGER);
	
	//Constructor
	public electromagnet(UberItems main, int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(main, id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
	}

	@Override
	public void leftClickAirAction(Player player, ItemStack item) {
		//TEST TEST TEST, will make a mode that consumes fuel and does this as active effect
		for (Entity e : player.getNearbyEntities(8, 8, 8)) {
			if (repelTargets.contains(e.getType())) {
				repelEntity(player, e);
			}
		}
	}

	@Override
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		leftClickAirAction(player, item);
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
		
		if (item.getItemMeta().getLore().get(1).contains("Item Magnet")) {
			ItemMeta meta = item.getItemMeta();
			for (Enchantment e : meta.getEnchants().keySet()) {
				meta.removeEnchant(e);
			}
			item.setItemMeta(meta);
			Utilities.loreItem(item, Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Off"));
		}
		else {
			item.addUnsafeEnchantment(Enchantment.LURE, 10);
			Utilities.loreItem(item, Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Item Magnet"));
		}
		
		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
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
		
		if (item.getItemMeta().getLore().get(1).contains("Item Magnet")) {
			for (Entity e : player.getNearbyEntities(16, 16, 16)) {
				if (e.getType() == EntityType.DROPPED_ITEM && e.hasGravity()) {
					e.teleport(player.getEyeLocation());
				}
			}
		}
		
		if (item.getItemMeta().getLore().get(1).contains("Electro-Aura")) {
			for (Entity e : player.getNearbyEntities(8, 8, 8)) {
				if (repelTargets.contains(e.getType())) {
					repelEntity(player, e);
				}
			}
		}
		
	}
	
	//Shoots entity away
	public void repelEntity(Player player, Entity e) {
		
		Vector v = e.getLocation().toVector().subtract(player.getLocation().toVector());
		double x = v.getX() / Math.abs(v.getX());
		double y = v.getY();
		double z = v.getZ() / Math.abs(v.getZ());
		v = new Vector(x, y, z);
		
		v.multiply(0.5);
		e.setVelocity(v);
	}
}
