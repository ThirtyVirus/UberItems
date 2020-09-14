package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class shooty_box extends UberItem {

	public shooty_box(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, oneTimeUse, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// shoot item from box
	public void rightClickAirAction(Player player, ItemStack item) {
		
		// get all items inside shooty box
		ItemStack[] rawItems = Utilities.getCompactInventory(super.getMain(), item);
		ArrayList<ItemStack> items = new ArrayList<>();
		for (ItemStack i : rawItems) if (i != null) items.add(i);
		
		// play "empty" sound when no items in dispenser
		if (items.isEmpty()) { player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 1); return; }
		
		// pick random Item
		Random ran = new Random();
		ItemStack actionItem = items.get(ran.nextInt(items.size()));
		
		// perform dispenser action
		shootItem(player, actionItem);

		// delete ActionItem stack if last item used
		if (actionItem.getAmount() == 0) items.remove(actionItem);
		
		// save inventory update to item lore
		ItemStack[] finalItems = new ItemStack[items.size()];
		for (int counter = 0; counter < items.size(); counter++) finalItems[counter] = items.get(counter);
		Utilities.saveCompactInventory(super.getMain(), item, finalItems);

		// confirm that the item's ability has been successfully used
		onItemUse(player, item);
	}
	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		rightClickAirAction(player, item);
	}

	public void shiftLeftClickAirAction(Player player, ItemStack item) { }
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	// open the box's inventory
	public void shiftRightClickAirAction(Player player, ItemStack item) {
		Inventory inventory = Bukkit.createInventory(player, InventoryType.DISPENSER, UberItems.itemPrefix + ChatColor.DARK_GRAY + "Shooty Box");
		
		ItemStack[] items = Utilities.getCompactInventory(super.getMain(), item);
		if (items != null) { for (ItemStack i : items) { if (i != null) { inventory.addItem(i); } } }
		player.openInventory(inventory);
		
		player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 1);
	}
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		shiftRightClickAirAction(player, item);
	}

	public void middleClickAction(Player player, ItemStack item) { }
	public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
	public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
	public void activeEffect(Player player, ItemStack item) { }

	// perform dispenser action
	private void shootItem(Player player, ItemStack item) {

		switch (item.getType()) {
			case ARROW:
				player.launchProjectile(Arrow.class);
				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				break;
			case TIPPED_ARROW:
				Arrow arrow = player.launchProjectile(Arrow.class);
				PotionMeta meta = (PotionMeta) item.getItemMeta();
				arrow.setBasePotionData(meta.getBasePotionData());
				break;
			case SPECTRAL_ARROW:
				SpectralArrow spec = player.launchProjectile(SpectralArrow.class);
				break;
			case TNT:
				Entity tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
				((TNTPrimed)tnt).setFuseTicks(30);
				((TNTPrimed)tnt).setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				break;
			case EGG:
				Egg thrown = player.launchProjectile(Egg.class);
				thrown.setVelocity(player.getEyeLocation().getDirection().multiply(1.5));
				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				break;
			case ENDER_PEARL:
				EnderPearl pearl = player.launchProjectile(EnderPearl.class);
				pearl.setVelocity(player.getEyeLocation().getDirection().multiply(3.0));
				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				break;
			case SPLASH_POTION:
				ThrownPotion potion = player.launchProjectile(ThrownPotion.class);
				potion.setVelocity(player.getEyeLocation().getDirection().multiply(2.0));
				potion.setItem(item);

				player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
				break;
			case FIRE_CHARGE:
				Fireball fireball = player.launchProjectile(SmallFireball.class);
				fireball.setVelocity(player.getEyeLocation().getDirection().multiply(2.0));
				player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
				break;
			// TODO add particles to projectile
			case WATER_BUCKET:
				launchFallingBlock(player, Material.WATER, 2.0F, Effect.BOW_FIRE);
				break;
			// TODO add particles to projectile
			case LAVA_BUCKET:
				launchFallingBlock(player, Material.LAVA, 2.0F, Effect.BOW_FIRE);
				break;
			case SALMON_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SALMON, 3.0F, Effect.BLAZE_SHOOT); break;
			case SHEEP_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SHEEP, 3.0F, Effect.BLAZE_SHOOT); break;
			case SHULKER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SHULKER, 3.0F, Effect.BLAZE_SHOOT); break;
			case SILVERFISH_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SILVERFISH, 3.0F, Effect.BLAZE_SHOOT); break;
			case SKELETON_HORSE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SKELETON_HORSE, 3.0F, Effect.BLAZE_SHOOT); break;
			case SKELETON_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SKELETON, 3.0F, Effect.BLAZE_SHOOT); break;
			case SLIME_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SLIME, 3.0F, Effect.BLAZE_SHOOT); break;
			case SPIDER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SPIDER, 3.0F, Effect.BLAZE_SHOOT); break;
			case SQUID_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.SQUID, 3.0F, Effect.BLAZE_SHOOT); break;
			case STRAY_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.STRAY, 3.0F, Effect.BLAZE_SHOOT); break;
			case BAT_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.BAT, 3.0F, Effect.BLAZE_SHOOT); break;
			case BLAZE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.BLAZE, 3.0F, Effect.BLAZE_SHOOT); break;
			case CAVE_SPIDER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.CAVE_SPIDER, 3.0F, Effect.BLAZE_SHOOT); break;
			case CHICKEN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.CHICKEN, 3.0F, Effect.BLAZE_SHOOT); break;
			case COD_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.COD, 3.0F, Effect.BLAZE_SHOOT); break;
			case COW_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.COW, 3.0F, Effect.BLAZE_SHOOT); break;
			case CREEPER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.CREEPER, 3.0F, Effect.BLAZE_SHOOT); break;
			case DOLPHIN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.DOLPHIN, 3.0F, Effect.BLAZE_SHOOT); break;
			case DONKEY_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.DONKEY, 3.0F, Effect.BLAZE_SHOOT); break;
			case DROWNED_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.DROWNED, 3.0F, Effect.BLAZE_SHOOT); break;
			case ELDER_GUARDIAN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ELDER_GUARDIAN, 3.0F, Effect.BLAZE_SHOOT); break;
			case ENDERMAN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ENDERMAN, 3.0F, Effect.BLAZE_SHOOT); break;
			case ENDERMITE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ENDERMITE, 3.0F, Effect.BLAZE_SHOOT); break;
			case EVOKER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.EVOKER, 3.0F, Effect.BLAZE_SHOOT); break;
			case GHAST_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.GHAST, 3.0F, Effect.BLAZE_SHOOT); break;
			case GUARDIAN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.GUARDIAN, 3.0F, Effect.BLAZE_SHOOT); break;
			case HORSE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.HORSE, 3.0F, Effect.BLAZE_SHOOT); break;
			case HUSK_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.HUSK, 3.0F, Effect.BLAZE_SHOOT); break;
			case LLAMA_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.LLAMA, 3.0F, Effect.BLAZE_SHOOT); break;
			case MAGMA_CUBE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.MAGMA_CUBE, 3.0F, Effect.BLAZE_SHOOT); break;
			case MOOSHROOM_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.MUSHROOM_COW, 3.0F, Effect.BLAZE_SHOOT); break;
			case MULE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.MULE, 3.0F, Effect.BLAZE_SHOOT); break;
			case OCELOT_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.OCELOT, 3.0F, Effect.BLAZE_SHOOT); break;
			case PARROT_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PARROT, 3.0F, Effect.BLAZE_SHOOT); break;
			case PHANTOM_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PHANTOM, 3.0F, Effect.BLAZE_SHOOT); break;
			case PIG_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PIG, 3.0F, Effect.BLAZE_SHOOT); break;
			case POLAR_BEAR_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.POLAR_BEAR, 3.0F, Effect.BLAZE_SHOOT); break;
			case PUFFERFISH_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PUFFERFISH, 3.0F, Effect.BLAZE_SHOOT); break;
			case RABBIT_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.RABBIT, 3.0F, Effect.BLAZE_SHOOT); break;
			case TROPICAL_FISH_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.TROPICAL_FISH, 3.0F, Effect.BLAZE_SHOOT); break;
			case TURTLE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.TURTLE, 3.0F, Effect.BLAZE_SHOOT); break;
			case VEX_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.VEX, 3.0F, Effect.BLAZE_SHOOT); break;
			case VILLAGER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.VILLAGER, 3.0F, Effect.BLAZE_SHOOT); break;
			case VINDICATOR_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.VINDICATOR, 3.0F, Effect.BLAZE_SHOOT); break;
			case WITCH_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.WITCH, 3.0F, Effect.BLAZE_SHOOT); break;
			case WITHER_SKELETON_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.WITHER_SKELETON, 3.0F, Effect.BLAZE_SHOOT); break;
			case WOLF_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.WOLF, 3.0F, Effect.BLAZE_SHOOT); break;
			case ZOMBIE_HORSE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ZOMBIE_HORSE, 3.0F, Effect.BLAZE_SHOOT); break;
			case ZOMBIE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ZOMBIE, 3.0F, Effect.BLAZE_SHOOT); break;
			case ZOMBIE_VILLAGER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ZOMBIE_VILLAGER, 3.0F, Effect.BLAZE_SHOOT); break;
			case STRIDER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.STRIDER, 3.0F, Effect.BLAZE_SHOOT); break;
			case BEE_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.BEE, 3.0F, Effect.BLAZE_SHOOT); break;
			case CAT_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.CAT, 3.0F, Effect.BLAZE_SHOOT); break;
			case FOX_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.FOX, 3.0F, Effect.BLAZE_SHOOT); break;
			case HOGLIN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.HOGLIN, 3.0F, Effect.BLAZE_SHOOT); break;
			case PANDA_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PANDA, 3.0F, Effect.BLAZE_SHOOT); break;
			case PIGLIN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PIGLIN, 3.0F, Effect.BLAZE_SHOOT); break;
			case PILLAGER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.PILLAGER, 3.0F, Effect.BLAZE_SHOOT); break;
			case RAVAGER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.RAVAGER, 3.0F, Effect.BLAZE_SHOOT); break;
			case TRADER_LLAMA_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.TRADER_LLAMA, 3.0F, Effect.BLAZE_SHOOT); break;
			case WANDERING_TRADER_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.WANDERING_TRADER, 3.0F, Effect.BLAZE_SHOOT); break;
			case ZOGLIN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ZOGLIN, 3.0F, Effect.BLAZE_SHOOT); break;
			case ZOMBIFIED_PIGLIN_SPAWN_EGG: launchMobFromSpawnEgg(player, EntityType.ZOMBIFIED_PIGLIN, 3.0F, Effect.BLAZE_SHOOT); break;

			default:
				// shoot blocks as falling sand
				if (item.getType().isBlock()) launchFallingBlock(player, item.getType(), 2.0F, Effect.BOW_FIRE);
				else {
					// shoot item forward as default action
					ItemStack dropItem = item.clone(); dropItem.setAmount(1);
					Entity drop = (Entity) player.getWorld().dropItemNaturally(player.getEyeLocation(), dropItem);
					drop.setVelocity(player.getLocation().getDirection().multiply(1.5));
					player.getWorld().playEffect(player.getLocation(), Effect.CLICK2, 1);
				}
				break;

			// IDEAS
			// particles for water and lava as it flew
			// glass, dyed glass ice, packed ice, blue ice shattering on impact and hurting a radius
			//

			// SYSTEMS
			// on block impact function
			// during block flight function (particles)
			// scattershot shift left click ability
		}

		// update inventory
		if (player.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);
	}

	// launch a falling block (duh lol)
	private FallingBlock launchFallingBlock(Player player, Material material, float multiplier, Effect sound) {
		FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), material, (byte) 0);
		block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(multiplier));
		player.getWorld().playEffect(player.getLocation(), sound, 1);
		return block;
	}

	// launch a mob
	private Entity launchMobFromSpawnEgg(Player player, EntityType type, float multiplier, Effect sound) {
		Entity mob = (Entity) player.getWorld().spawnEntity(player.getEyeLocation(), type);
		mob.setVelocity(player.getLocation().getDirection().multiply(multiplier));
		player.getWorld().playEffect(player.getLocation(), sound, 1);
		return mob;
	}


}
