package thirtyvirus.uber.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import thirtyvirus.uber.UberItems_old;
import thirtyvirus.uber.UberItem;

public class infini_gulp extends UberItem{

	//Constructor
	public infini_gulp(int id, String name, List<String> lore, String description, Material material, Boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
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

		if (item.getItemMeta().getLore().get(0).contains("Mode: Potions")){
			ItemMeta meta = item.getItemMeta();
			for (Enchantment e : meta.getEnchants().keySet()) {
				meta.removeEnchant(e);
			}
			item.setItemMeta(meta);
			UberItems_old.loreItem(item, Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Off"));
		}
		else {
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			UberItems_old.loreItem(item, Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Item Magnet"));
		}
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
	//not an actual active effect, used as function for applying stored potion effects
	public void activeEffect(Player player, ItemStack item) {
		
	}
}
