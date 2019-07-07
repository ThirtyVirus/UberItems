package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberItem;

class ItemComparator implements Comparator<ItemStack> {
    @SuppressWarnings("deprecation")
	public int compare(ItemStack a, ItemStack b) {
        //return a.getType().compareTo(b.getType());
        
        if (a.getType().getId() < b.getType().getId()) {
        	return -1;
        }
        else if (a.getType().getId() > b.getType().getId()){
        	return 1;
        }
        else{
        	//Compare Block Data
        	if (a.getData().getData() < b.getData().getData()){
        		return -1;
        	}
        	else if (a.getData().getData() > b.getData().getData()){
        		return 1;
        	}
        	else{
        		return 0;
        	}
        	
        }
        
    }
}

public class document_of_order extends UberItem  {

	public document_of_order(int id, String name, List<String> lore, String description, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect) {
		super(id, name, lore, description, material, canBreakBlocks, stackable, hasActiveEffect);
	}
	public void leftClickAirAction(Player player, ItemStack item) {
		ArrayList<Material> supportedBlocks = new ArrayList<Material>(); supportedBlocks.add(Material.CHEST); supportedBlocks.add(Material.DROPPER); supportedBlocks.add(Material.DISPENSER); supportedBlocks.add(Material.ENDER_CHEST); supportedBlocks.add(Material.TRAPPED_CHEST);
		supportedBlocks.add(Material.BLACK_SHULKER_BOX); supportedBlocks.add(Material.BLUE_SHULKER_BOX); supportedBlocks.add(Material.BROWN_SHULKER_BOX); supportedBlocks.add(Material.CYAN_SHULKER_BOX); supportedBlocks.add(Material.GRAY_SHULKER_BOX);
		supportedBlocks.add(Material.GREEN_SHULKER_BOX); supportedBlocks.add(Material.LIGHT_BLUE_SHULKER_BOX); supportedBlocks.add(Material.LIME_SHULKER_BOX); supportedBlocks.add(Material.MAGENTA_SHULKER_BOX); supportedBlocks.add(Material.ORANGE_SHULKER_BOX);
		supportedBlocks.add(Material.PINK_SHULKER_BOX); supportedBlocks.add(Material.PURPLE_SHULKER_BOX); supportedBlocks.add(Material.RED_SHULKER_BOX); supportedBlocks.add(Material.GRAY_SHULKER_BOX); supportedBlocks.add(Material.WHITE_SHULKER_BOX); supportedBlocks.add(Material.YELLOW_SHULKER_BOX);
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		//TEST TEST TEST TEST
		ArrayList<Location> locations = new ArrayList<Location>(); //Change to actually get stored locations from item metadata
		locations.add(new Location(player.getWorld(), -567, 91, 61));
		locations.add(new Location(player.getWorld(), -566, 91, 61));
		locations.add(new Location(player.getWorld(), -565, 91, 61));
		locations.add(new Location(player.getWorld(), -564, 91, 61));
		
		//Store inventory references for later
		ArrayList<Inventory> inventories = new ArrayList<Inventory>();
		
		//Add all items from containers to item ArrayList
		for (Location location : locations){
			Block block = player.getWorld().getBlockAt(location);
			Inventory inv = null;
			if (!supportedBlocks.contains(block.getType())) continue;
			
			else if (block.getType() == Material.CHEST) { Chest chest = (Chest) block.getState(); inv = chest.getInventory(); }
			//else if (block.getType() == Material.TRAPPED_CHEST) { Chest chest = (Chest) block.getState(); inv = chest.getInventory(); }
			else if (block.getType() == Material.ENDER_CHEST) { inv = player.getEnderChest(); }
			else if (block.getType() == Material.DROPPER) { Dropper dropper = (Dropper) block.getState(); inv = dropper.getInventory(); }
			else if (block.getType() == Material.DISPENSER){ Dispenser dispenser = (Dispenser) block.getState(); inv = dispenser.getInventory(); }
			else{ ShulkerBox box = (ShulkerBox) block.getState(); inv = box.getInventory(); } //Shulker Boxes
			
			items = addItems(inv, items);
			inventories.add(inv);
		}
		
		//Sort all items and naturalize stacks
		items.sort(new ItemComparator());
		items = naturalizeItemArrayList(items);

		player.sendMessage(UberItems.prefix + "inventories: " + inventories.size());
		player.sendMessage(UberItems.prefix + "items: " + items.size());
		
		//Distribute items between inventories
		for (Inventory inventory : inventories){
			inventory.clear();
			int inventorySize = inventory.getSize();
			if (items.size() <= inventorySize) inventorySize = items.size();
			
			List<ItemStack> currentInv = items.subList(0, inventorySize);
			ItemStack currentItems[] = new ItemStack[inventorySize];
			
			int counter = 0;
			for (ItemStack i : currentInv){
				currentItems[counter] = i;
				counter++;
			}
			
			inventory.setContents(currentItems);

			counter = 0; 
			while (counter < currentItems.length) { items.remove(0); counter++; }
			
		}		
		player.sendMessage(UberItems.prefix + "Inventories: " + inventories.size());
	}
	public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		leftClickAirAction(player, item);
	}
	public void rightClickAirAction(Player player, ItemStack item) {

	}

	public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		ArrayList<Material> supportedBlocks = new ArrayList<Material>(); supportedBlocks.add(Material.CHEST); supportedBlocks.add(Material.DROPPER); supportedBlocks.add(Material.DISPENSER); supportedBlocks.add(Material.ENDER_CHEST); //supportedBlocks.add(Material.TRAPPED_CHEST);
		supportedBlocks.add(Material.BLACK_SHULKER_BOX); supportedBlocks.add(Material.BLUE_SHULKER_BOX); supportedBlocks.add(Material.BROWN_SHULKER_BOX); supportedBlocks.add(Material.CYAN_SHULKER_BOX); supportedBlocks.add(Material.GRAY_SHULKER_BOX);
		supportedBlocks.add(Material.GREEN_SHULKER_BOX); supportedBlocks.add(Material.LIGHT_BLUE_SHULKER_BOX); supportedBlocks.add(Material.LIME_SHULKER_BOX); supportedBlocks.add(Material.MAGENTA_SHULKER_BOX); supportedBlocks.add(Material.ORANGE_SHULKER_BOX);
		supportedBlocks.add(Material.PINK_SHULKER_BOX); supportedBlocks.add(Material.PURPLE_SHULKER_BOX); supportedBlocks.add(Material.RED_SHULKER_BOX); supportedBlocks.add(Material.GRAY_SHULKER_BOX); supportedBlocks.add(Material.WHITE_SHULKER_BOX); supportedBlocks.add(Material.YELLOW_SHULKER_BOX);
		
		Inventory inv = null;
		if (!supportedBlocks.contains(block.getType())) return;
		
		else if (event.getClickedBlock().getType() == Material.CHEST) { Chest chest = (Chest) block.getState(); inv = chest.getInventory(); }
		//else if (event.getClickedBlock().getType() == Material.TRAPPED_CHEST) { Chest chest = (Chest) block.getState(); inv = chest.getInventory(); }
		else if (event.getClickedBlock().getType() == Material.ENDER_CHEST) { inv = player.getEnderChest(); }
		else if (event.getClickedBlock().getType() == Material.DROPPER) { Dropper dropper = (Dropper) block.getState(); inv = dropper.getInventory(); }
		else if (event.getClickedBlock().getType() == Material.DISPENSER){ Dispenser dispenser = (Dispenser) block.getState(); inv = dispenser.getInventory(); }
		else{ ShulkerBox box = (ShulkerBox) block.getState(); inv = box.getInventory(); } //Shulker Boxes
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items = addItems(inv, items);
		
		items.sort(new ItemComparator());
		items = naturalizeItemArrayList(items);

		ItemStack sortedItems[] = new ItemStack[items.size()];
		
		int counter = 0;
		for (ItemStack i : items){
			sortedItems[counter] = i;
			counter++;
		}
		
		inv.setContents(sortedItems);
		
		
	}
	public void shiftLeftClickAirAction(Player player, ItemStack item) {

	}
	public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {

	}
	public void shiftRightClickAirAction(Player player, ItemStack item) {

	}
	public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
		
	}
	public void middleClickAction(Player player, ItemStack item) {

	}
	public void activeEffect(Player player, ItemStack item) {

	}
	
	public int containsMaterial(ArrayList<ItemStack> items, ItemStack i){

		int index = 0;
		for (ItemStack item : items){
			if (item.isSimilar(i)) return index;
			index++;
		}
		return -1;
	}
	
	//Add items to arraylist from given inventory
	public ArrayList<ItemStack> addItems(Inventory inv, ArrayList<ItemStack> items) {
		for (ItemStack i : inv){
			if (i == null) continue;
			int index = containsMaterial(items, i);
			if (index == -1 || i.getMaxStackSize() == 1) {
				items.add(i);
			}
			else {
				items.get(index).setAmount(items.get(index).getAmount() + i.getAmount());
			}
		}
		return items;
	}
	
	//Spread out ItemStack amounts to 64 max
	public ArrayList<ItemStack> naturalizeItemArrayList(ArrayList<ItemStack> items){
		ArrayList<ItemStack> newItems = new ArrayList<ItemStack>();
		
		for (ItemStack i : items){
			
			int amount = i.getAmount();
			while (amount > 64){
				ItemStack newItem = i.clone();
				i.setAmount(64);
				newItems.add(newItem);
				amount -= 64;
				i.setAmount(amount);
			}
			if (i.getAmount() > 0) newItems.add(i);
			
		}
		
		return newItems;
	}
	
	
}
