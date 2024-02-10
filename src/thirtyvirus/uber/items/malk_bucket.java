package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class malk_bucket extends UberItem implements Listener {

	public malk_bucket(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
		super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
	}
	public void onItemStackCreate(ItemStack item) {
		Utilities.storeStringInItem(item, "none", "potion-name");
	}
	public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
		lore.add(ChatColor.GREEN + "Spiked with: " + ChatColor.GRAY + Utilities.getStringFromItem(item, "potion-name"));
	}

	public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

	public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean rightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
	public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
	public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

	public boolean middleClickAction(Player player, ItemStack item) { return false; }
	public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
	public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }

	// apply potion effect to malk bucket
	public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) {

		// verify that the item is compatible with the malk bucket
		if (!(addition.hasItemMeta() && addition.getItemMeta() instanceof PotionMeta)) return false;

		// store the potion in the malk bucket
		ItemStack[] itemArray = new ItemStack[1]; itemArray[0] = addition; // store the potion as a 1 item inventory
		Utilities.saveCompactInventory(item, itemArray);

		// update lore and play confirmation sound
		player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1);
		PotionMeta meta = (PotionMeta) addition.getItemMeta();
		if (meta.getDisplayName().equals(""))
			Utilities.storeStringInItem(item, meta.getBasePotionData().getType().name().replace('_', ' '), "potion-name");
		else
			Utilities.storeStringInItem(item, meta.getDisplayName(), "potion-name");
		updateLore(item);

		// delete the item being clicked onto the Uber Item
		event.getWhoClicked().setItemOnCursor(null);
		event.setCancelled(true);

		return true;
	}

	public boolean activeEffect(Player player, ItemStack item) { return false; }

	// process malk bucket
	@EventHandler
	private void consumeEvent(PlayerItemConsumeEvent event) {

		// the player is drinking from a malk bucket
		if (UberItems.getItem("malk_bucket").compare(event.getItem())) {
			// remove all potion effects
			ArrayList<PotionEffect> effects = new ArrayList<>();
			for (PotionEffect e : event.getPlayer().getActivePotionEffects()) { effects.add(e); }
			for (PotionEffect e : effects) event.getPlayer().removePotionEffect(e.getType());

			// retrieve potion from malk bucket
			UberItem uber = Utilities.getUber(event.getItem());

			// enforce permissions
			if (Utilities.enforcePermissions(event.getPlayer(), uber, true)) return;

			ItemStack[] itemArray = Utilities.getCompactInventory(event.getItem());
			if (itemArray.length == 0) return; // ensure that the malk bucket has a spiked potion effect
			ItemStack potion = itemArray[0]; PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

			// give player potion effects
			// default vanilla potion effects to 5 minute duration because of the lack of potion duration in the API
			int amplifier = 0; if (potionMeta.getBasePotionData().isUpgraded()) amplifier = 1; //vanilla potion effect
			event.getPlayer().addPotionEffect(potionMeta.getBasePotionData().getType().getEffectType().createEffect(5 * 60 * 20, amplifier));

			if (potionMeta != null) // custom potion effects
				for (PotionEffect effect : potionMeta.getCustomEffects()) event.getPlayer().addPotionEffect(effect);

			event.setCancelled(true);
		}

	}
}