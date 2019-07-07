package thirtyvirus.uber;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class UberItem {

    private int id;
    private String name;
    private String description;
    private ItemStack item;

    private List<String> defaultLore;
    private boolean canBreakBlocks;
    private boolean stackable;
    private boolean hasActive;

    //New Builder's Wand Item
    public UberItem(int id, String name, List<String> lore, String description, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect){
        this.id = id;
        this.name = name;
        this.description = description;

        this.defaultLore = lore;
        item = UberItems.nameItem(material, name);
        item = UberItems.loreItem(item, lore);

        this.canBreakBlocks = canBreakBlocks;
        this.stackable = stackable;
        this.hasActive = hasActiveEffect;
    }

    public abstract void leftClickAirAction(Player player, ItemStack item);
    public abstract void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void rightClickAirAction(Player player, ItemStack item);
    public abstract void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void shiftLeftClickAirAction(Player player, ItemStack item);
    public abstract void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void shiftRightClickAirAction(Player player, ItemStack item);
    public abstract void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void middleClickAction(Player player, ItemStack item);

    public abstract void activeEffect(Player player, ItemStack item);

    public int getID() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ItemStack getItem() { return item; }
    public List<String> getDefaultLore() { return defaultLore; }
    public boolean getCanBreakBlocks() { return canBreakBlocks; }
    public boolean isStackable() { return stackable; }
    public boolean hasActiveEffect() { return hasActive; }

    //Destroy Uber Item (mostly used for Single-Use items)
    public static void destroy(ItemStack item, int quantity) {
        if (item.getAmount() <= quantity) item.setAmount(0);
        else item.setAmount(item.getAmount() - quantity);
    }
}
