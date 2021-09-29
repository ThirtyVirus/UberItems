package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class boom_stick extends UberItem{

	public boom_stick(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

	// use the explosive ability
	public boolean rightClickAirAction(Player player, ItemStack item) {

		// enforce the 5 second cooldown of the boom stick's BOOM ability
		if (Utilities.enforceCooldown(player, "boom", 5, item, true)) return false;

		for(Entity e : player.getNearbyEntities(15,15,15)) {
			if (e instanceof LivingEntity && e != player) {
				player.getLocation().getWorld().createExplosion(e.getLocation().add(0,0,0), 1);
			}
		}

		return true;
	}

	public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean middleClickAction(Player player, ItemStack item) { return false; }

	// send mobs to the shadow dimension
	public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) {

		// enforce that the entity is a mob
		if (!(target instanceof LivingEntity)) { return false; }

		LivingEntity mob = (LivingEntity) target;

		player.playSound(mob.getLocation(), Sound.ENTITY_SHULKER_BULLET_HIT, 5, 1);
		mob.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 1));

		// perform the teleport ability
		Utilities.scheduleTask(() -> {

			player.playSound(mob.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 5, 1);
			mob.getWorld().playEffect(mob.getLocation().add(0,1,0), Effect.SMOKE, 0);
			mob.remove();
		}, 40);

		return true;
	}
	public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }

	public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }
	public boolean activeEffect(Player player, ItemStack item) { return false; }
}
