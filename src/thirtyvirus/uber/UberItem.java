package thirtyvirus.uber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public abstract class UberItem {

    private int id;
    private UberRarity rarity;
    private String name;
    private Material material;

    private List<String> defaultLore;
    private boolean canBreakBlocks;
    private boolean stackable;
    private boolean hasActive;

    private List<UberAbility> abilities = new ArrayList<>();

    private UberItems main;

    // new UberItem
    public UberItem(UberItems main, int id, UberRarity rarity, String name, Material material, boolean canBreakBlocks, boolean stackable, boolean hasActiveEffect, List<UberAbility> abilities){
        this.main = main;

        this.id = id;
        this.rarity = rarity;
        this.name = name;
        this.material = material;

        this.canBreakBlocks = canBreakBlocks;
        this.stackable = stackable;

        this.hasActive = hasActiveEffect;

        this.abilities = abilities;
    }

    public List<String> getLore() {
        List<String> lore = new ArrayList<>();

        lore.add("");

        // show the item's abilities
        for (UberAbility ability : abilities) {
            lore.addAll(ability.toLore());
            lore.add("");
        }
        // show the rarity of the item
        lore.add("" + rarity.getColor() + ChatColor.BOLD + rarity.toString());

        return lore;
    }
    public void enforceStackability(ItemStack item) {
        if (!stackable) {
            Utilities.storeStringInItem(getMain(), item,  UUID.randomUUID().toString(), "UUID");
        }
    }

    public abstract void onItemStackCreate(ItemStack item);

    public abstract void leftClickAirAction(Player player, ItemStack item);
    public abstract void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void rightClickAirAction(Player player, ItemStack item);
    public abstract void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void shiftLeftClickAirAction(Player player, ItemStack item);
    public abstract void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void shiftRightClickAirAction(Player player, ItemStack item);
    public abstract void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract void middleClickAction(Player player, ItemStack item);
    public abstract void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item);

    public abstract void activeEffect(Player player, ItemStack item);

    public int getID() { return id; }
    public UberRarity getRarity() { return rarity; }
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public List<String> getDefaultLore() { return defaultLore; }
    public boolean getCanBreakBlocks() { return canBreakBlocks; }
    public boolean isStackable() { return stackable; }
    public boolean hasActiveEffect() { return hasActive; }

    // destroy UberItem (mostly used for single - use items)
    public static void destroy(ItemStack item, int quantity) {
        if (item.getAmount() <= quantity) item.setAmount(0);
        else item.setAmount(item.getAmount() - quantity);
    }

    // get an instance of the main class
    public UberItems getMain() { return main; }
}
