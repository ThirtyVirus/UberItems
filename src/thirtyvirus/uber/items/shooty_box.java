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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
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

	// TODO add more item support, optimize code

	public shooty_box(UberItems main, int id, UberRarity rarity, String name, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities) {
		super(main, id, rarity, name, material, canBreakBlocks, stackable, hasActiveEffect, abilities);
	}
	public void onItemStackCreate(ItemStack item) { }
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public void leftClickAirAction(Player player, ItemStack item) { }
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

	public void rightClickAirAction(Player player, ItemStack item) {
		
		// get all items inside shooty box
		ItemStack[] rawItems = Utilities.getCompactInventory(super.getMain(), item);
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack i : rawItems) if (i != null) items.add(i);
		
		// play "empty" sound when no items in dispenser
		if (items.isEmpty()){
			player.getWorld().playEffect(player.getLocation(), Effect.CLICK1, 1);
			return;
		}
		
		// pick random Item
		Random ran = new Random();
		ItemStack actionItem = items.get(ran.nextInt(items.size()));
		
		// perform dispenser action + update inventory
		if (actionItem.getType().name().toLowerCase().contains("arrow")) {
			
			player.launchProjectile(Arrow.class);
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
			
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.ANVIL){

			FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.ANVIL, (byte) 0);
            block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
            player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
            
			// update inventory
            if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.TORCH){

			FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.TORCH, (byte) 0);
			block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);

			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.WATER_BUCKET){

			FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.WATER, (byte) 0);
			block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);

			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.LAVA_BUCKET){

			FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Material.LAVA, (byte) 0);
			block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);

			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.TNT){
			Entity tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
			((TNTPrimed)tnt).setFuseTicks(30);
			((TNTPrimed)tnt).setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(2.0));
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
	          
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.EGG){
			Egg thrown = player.launchProjectile(Egg.class);
			Vector v = player.getEyeLocation().getDirection().multiply(1.5);
			thrown.setVelocity(v);
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
	          
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.ENDER_PEARL) {
			EnderPearl thrown = player.launchProjectile(EnderPearl.class);
			Vector v = player.getEyeLocation().getDirection().multiply(3.0);
			thrown.setVelocity(v);
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
	          
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.SPLASH_POTION) {
			ThrownPotion thrown = player.launchProjectile(ThrownPotion.class);
			
			Collection<PotionEffect>effects = thrown.getEffects();
			PotionMeta meta = (PotionMeta) actionItem.getItemMeta();
			
			for (PotionEffect e : meta.getCustomEffects()){
				effects.add(e);
			}
			
			Vector v = player.getEyeLocation().getDirection().multiply(2.0);
			thrown.setVelocity(v);
			
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
			
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (actionItem.getType() == Material.FIRE_CHARGE && !Utilities.isUber(getMain(), actionItem, 6)){
			Fireball thrown = player.launchProjectile(SmallFireball.class);
			Vector v = player.getEyeLocation().getDirection().multiply(2.0);
			thrown.setVelocity(v);
			player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
			
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else if (Utilities.isUber(getMain(), actionItem, 6)) {
			Utilities.getUber(getMain(), actionItem).rightClickAirAction(player, actionItem);
			player.getWorld().playEffect(player.getLocation(), Effect.BOW_FIRE, 1);
			
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		else {
			// shoot item forward as default action
			ItemStack dropItem = actionItem.clone(); dropItem.setAmount(1);
			Entity drop = (Entity) player.getWorld().dropItemNaturally(player.getEyeLocation(), dropItem);
			drop.setVelocity(player.getLocation().getDirection().multiply(0.5));
			player.getWorld().playEffect(player.getLocation(), Effect.CLICK2, 1);
			
			// update inventory
			if (player.getGameMode() != GameMode.CREATIVE) actionItem.setAmount(actionItem.getAmount() - 1);
		}
		
		// delete ActionItem stack if last item used
		if (actionItem.getAmount() == 0) items.remove(actionItem);
		
		// save inventory update to item lore
		ItemStack[] finalItems = new ItemStack[items.size()];
		for (int counter = 0; counter < items.size(); counter++) finalItems[counter] = items.get(counter);
		Utilities.saveCompactInventory(super.getMain(), item, finalItems);
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
	
}
