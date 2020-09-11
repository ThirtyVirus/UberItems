package thirtyvirus.uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    private boolean canBreakBlocks = false;
    private boolean stackable = false;
    private boolean oneTimeUse = false;
    private boolean hasActive = false;

    private List<UberAbility> abilities = new ArrayList<>();

    private UberItems main;

    // new UberItem
    public UberItem(UberItems main, int id, UberRarity rarity, String name, Material material, boolean canBreakBlocks, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities){
        this.main = main;

        this.id = id;
        this.rarity = rarity;
        this.name = name;
        this.material = material;

        this.canBreakBlocks = canBreakBlocks;
        this.stackable = stackable;
        this.oneTimeUse = oneTimeUse;
        this.hasActive = hasActiveEffect;

        this.abilities = abilities;
    }

    // properly format the lore for Uber Items
    public List<String> getLore(ItemStack item) {
        List<String> lore = new ArrayList<>();

        // warn players that an item is unfinished
        if (rarity == UberRarity.UNFINISHED) {
            lore.add(ChatColor.RED + "This item is UNFINISHED");
            lore.add(ChatColor.RED + "It may not perform as expected");
        }

        // put in item specific prefix
        getSpecificLorePrefix(lore, item);
        lore.add("");

        // show the item's abilities
        for (UberAbility ability : abilities) {
            lore.addAll(ability.toLore());
            lore.add("");
        }

        // put in item specific suffix
        getSpecificLoreSuffix(lore, item);

        // show the "consumed on use" msg if the item is a one time use
        if (oneTimeUse) lore.add(ChatColor.DARK_GRAY + "(consumed on use)");

        // show the rarity of the item
        lore.add("" + rarity.getColor() + ChatColor.BOLD + rarity.toString());

        return lore;
    }

    // convenience functions (facilitates understanding of the code by giving an official name for these actions)
    public void updateLore(ItemStack uber) {
        // verify that the UberItem isn't null
        if (uber == null) return;

        Utilities.loreItem(uber, getLore(uber));
    }
    public void changeMaterial(ItemStack uber, Material material) {
        // verify that the UberItem isn't null
        if (uber == null) return;

        uber.setType(material);
    }
    public void enforceStackability(ItemStack uber) {
        // verify that the UberItem isn't null
        if (uber == null) return;

        if (!stackable) {
            Utilities.storeStringInItem(getMain(), uber,  UUID.randomUUID().toString(), "UUID");
        }
    }
    public void onItemUse(Player player, ItemStack item) {
        // process one time use items
        if (oneTimeUse && player.getGameMode() != GameMode.CREATIVE) destroy(item, 1);
    }

    public abstract void onItemStackCreate(ItemStack item);
    public abstract void getSpecificLorePrefix(List<String> lore, ItemStack item);
    public abstract void getSpecificLoreSuffix(List<String> lore, ItemStack item);

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
    public abstract void clickedInInventoryAction(Player player, InventoryClickEvent event);

    public abstract void activeEffect(Player player, ItemStack item);

    public int getID() { return id; }
    public UberRarity getRarity() { return rarity; }
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public boolean getCanBreakBlocks() { return canBreakBlocks; }
    public boolean isStackable() { return stackable; }
    public boolean hasActiveEffect() { return hasActive; }

    // generate an UberItem ItemStack from a given string
    public static ItemStack fromString(UberItems main, String name, int stackSize) {

        String effectiveName = name;
        UberItem item;
        if (Utilities.isInteger(name)) {
            item = UberItems.items.get(UberItems.itemIDs.get(Integer.parseInt(name)));
            effectiveName = UberItems.itemIDs.get(Integer.parseInt(name));
            Bukkit.getLogger().info(item.getName());
        }
        else item = UberItems.items.get(name);

        // verify that the item is in fact an UberItem
        if (item == null) { return null; }

        // apply UberItem properties to item
        ItemStack newItemStack = new ItemStack(item.getMaterial());
        Utilities.nameItem(newItemStack, item.getRarity().getColor() + item.getName());
        Utilities.storeStringInItem(main, newItemStack, "true", "is-uber");
        Utilities.storeStringInItem(main, newItemStack, effectiveName, "uber-name");
        Utilities.storeIntInItem(main, newItemStack, item.getID(), "uber-id");

        item.enforceStackability(newItemStack);
        item.onItemStackCreate(newItemStack);
        Utilities.loreItem(newItemStack, item.getLore(newItemStack));

        if (item.isStackable()) newItemStack.setAmount(stackSize);

        return newItemStack;
    }

    // destroy UberItem (mostly used for single - use items)
    public static void destroy(ItemStack item, int quantity) {
        if (item.getAmount() <= quantity) item.setAmount(0);
        else item.setAmount(item.getAmount() - quantity);
    }

    // get an instance of the main class
    public UberItems getMain() { return main; }
}