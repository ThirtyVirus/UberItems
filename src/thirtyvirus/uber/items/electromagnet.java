package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class electromagnet extends UberItem{

	public static List<EntityType> repelTargets = Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.CREEPER,
			EntityType.DRAGON_FIREBALL, EntityType.ARROW, EntityType.FIREBALL, EntityType.FIREWORK, EntityType.DROWNED, EntityType.EGG, EntityType.ELDER_GUARDIAN, EntityType.BLAZE,
			EntityType.ENDERMAN, EntityType.EVOKER, EntityType.FISHING_HOOK, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HUSK, EntityType.LIGHTNING,
			EntityType.MAGMA_CUBE, EntityType.LLAMA_SPIT, EntityType.PHANTOM, EntityType.PRIMED_TNT, EntityType.POLAR_BEAR, EntityType.PUFFERFISH,
			EntityType.SHULKER, EntityType.SHULKER_BULLET, EntityType.SNOWBALL, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER,
			EntityType.WITHER_SKELETON, EntityType.WITHER_SKULL, EntityType.ZOMBIE_VILLAGER);

	public electromagnet(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean rightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

	// toggle the 2 abilities
	public boolean shiftRightClickAirAction(Player player, ItemStack item) {
		// status = 0 means off, 1 means on
		if (Utilities.getIntFromItem(item, "status") == 0) {
			Utilities.addEnchantGlint(item);
			Utilities.storeIntInItem(item, 1, "status");
		}
		else {
			Utilities.removeEnchantments(item);
			Utilities.storeIntInItem(item, 0, "status");
		}

		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
		return true;
	}
	public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		return shiftRightClickAirAction(player, item);
	}

	public boolean middleClickAction(Player player, ItemStack item) { return false; }
	public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
	public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
	public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }

	// actively repel entities
	public boolean activeEffect(Player player, ItemStack item) {
		if (Utilities.getIntFromItem(item, "status") == 1) {
			// teleport drops in range to the player
			for (Entity e : player.getNearbyEntities(16, 16, 16)) {
				if (e.getType() == EntityType.DROPPED_ITEM && e.hasGravity()) {
					e.teleport(player.getEyeLocation());
				}
			}
			// repel entities
			for (Entity e : player.getNearbyEntities(8, 8, 8)) {
				if (repelTargets.contains(e.getType())) {
					repelEntity(player, e);
				}
			}
			return true;
		}
		return false;
	}
	
	// shoots entity away
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
