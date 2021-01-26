package thirtyvirus.uber.items;

import java.util.List;
import java.util.Vector;

import net.minecraft.server.v1_16_R1.Explosion;
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
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class boom_stick extends UberItem{

	public boom_stick(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) {
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// use the explosive ability
	public void rightClickAirAction(Player player, ItemStack item) {

		// enforce the 5 second cooldown of the boom stick's BOOM ability
		if (!Utilities.enforceCooldown(player, "boom", 5, item, true)) return;

		for(Entity e : player.getNearbyEntities(15,15,15)) {
			if (e instanceof LivingEntity && e != player) {
				player.getLocation().getWorld().createExplosion(e.getLocation().add(0,0,0), 1);
			}
		}

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}

	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void shiftRightClickAirAction(Player player, ItemStack item) { }
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
	public void middleClickAction(Player player, ItemStack item) { }

	// send mobs to the shadow dimension
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) {

		// enforce that the entity is a mob
		if (!(target instanceof LivingEntity)) { return; }

		LivingEntity mob = (LivingEntity) target;

		player.playSound(mob.getLocation(), Sound.ENTITY_SHULKER_BULLET_HIT, 5, 1);
		mob.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 1));

		// perform the teleport ability
		Utilities.scheduleTask(new Runnable() { public void run() {

			player.playSound(mob.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 5, 1);
			mob.getWorld().playEffect(mob.getLocation().add(0,1,0), Effect.SMOKE, 0);
			mob.remove();
		} }, 40);

		onItemUse(player, item); // confirm that the item's ability has been successfully used
	}
	public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }

	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
	public void activeEffect(Player player, ItemStack item) { }
}
