package thirtyvirus.uber;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class UberItem {

    private ItemStack item;
    private String name;
    private UberRarity rarity;
    private boolean stackable;
    private int UUID;

    private boolean oneTimeUse;
    private boolean hasActive;
    private List<UberAbility> abilities;

    private UberCraftingRecipe craftingRecipe;

    /**
     * Define a new UberItem
     */
    public UberItem(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        this.item = new ItemStack(material);
        this.name = name;
        this.rarity = rarity;

        this.stackable = stackable;
        this.oneTimeUse = oneTimeUse;
        this.hasActive = hasActiveEffect;
        this.abilities = abilities;

        this.craftingRecipe = craftingRecipe;
        UUID = Utilities.stringToSeed(material.name() + name + rarity.toString());
    }
    public UberItem(ItemStack item, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        this.item = item;
        this.name = name;
        this.rarity = rarity;

        this.stackable = stackable;
        this.oneTimeUse = oneTimeUse;
        this.hasActive = hasActiveEffect;
        this.abilities = abilities;

        this.craftingRecipe = craftingRecipe;
        UUID = Utilities.stringToSeed(item.getType().name() + name + rarity.toString());
    }

    /**
     * @param other the item to test
     * @return whether or not the given ItemStack is this UberItem
     */
    public boolean compare(ItemStack other) {
        int otherUUID = Utilities.getIntFromItem(other, "UberUUID");
        return (otherUUID == UUID);
    }

    /**
     * @param amount the amount of the item to make
     * @return an instance of this UberItem in ItemStack form
     */
    public ItemStack makeItem(int amount) {
        ItemStack newItem = item.clone();

        Utilities.nameItem(newItem, rarity.getColor() + name);
        Utilities.storeIntInItem(newItem, UUID, "UberUUID");

        onItemStackCreate(newItem);
        Utilities.loreItem(newItem, getLore(newItem));

        newItem.setAmount(amount);
        if (!stackable) Utilities.storeStringInItem(newItem,  java.util.UUID.randomUUID().toString(), "UUID");

        return newItem;
    }

    // properly format the lore for Uber Items
    private List<String> getLore(ItemStack item) {
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

        // show the item's upgrades
        String[] rawUpgrades = Utilities.getUpgrades(item);
        List<String> upgrades = new ArrayList<>();
        if (rawUpgrades != null && rawUpgrades.length > 0) {
            for (String upgrade : rawUpgrades) if (!upgrade.equals("")) upgrades.add(upgrade);
        }

        if (upgrades.size() > 0) {
            lore.add(ChatColor.RED + "Upgrades:");
            for (String upgrade : upgrades) {
                lore.addAll(Utilities.stringToLore(upgrade + ": " + Utilities.getUpgradeDescription(item, upgrade), 43, ChatColor.YELLOW));
            }
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

    /**
     * Update the lore of an UberItem
     *
     * @param item the item to be updated
     */
    public void updateLore(ItemStack item) {
        // verify that the UberItem isn't null
        if (item == null) return;
        Utilities.loreItem(item, getLore(item));
    }

    /**
     * Perform the actions to be taken when an item is successfully used
     *
     * @param player the player holding the item
     * @param item the item being used
     */
    public void onItemUse(Player player, ItemStack item) {
        // process one time use items
        if (oneTimeUse && player.getGameMode() != GameMode.CREATIVE) destroy(item, 1);
    }

    /**
     * Destroy UberItem (mostly used for single - use items)
     *
     * @param item the item to destroy
     * @param quantity the amount of the item to remove
     */
    public static void destroy(ItemStack item, int quantity) {
        if (item.getAmount() <= quantity) item.setAmount(0);
        else item.setAmount(item.getAmount() - quantity);
    }

    public abstract void onItemStackCreate(ItemStack item);
    public abstract void getSpecificLorePrefix(List<String> lore, ItemStack item);
    public abstract void getSpecificLoreSuffix(List<String> lore, ItemStack item);

    public abstract boolean leftClickAirAction(Player player, ItemStack item);
    public abstract boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract boolean rightClickAirAction(Player player, ItemStack item);
    public abstract boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract boolean shiftLeftClickAirAction(Player player, ItemStack item);
    public abstract boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract boolean shiftRightClickAirAction(Player player, ItemStack item);
    public abstract boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item);

    public abstract boolean middleClickAction(Player player, ItemStack item);
    public abstract boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item);
    public abstract boolean breakBlockAction(Player player, BlockBreakEvent event, Block target, ItemStack item);
    public abstract boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition);

    public abstract boolean activeEffect(Player player, ItemStack item);

    // getters
    public ItemStack getRawItem() { return item; }
    public Material getMaterial() { return item.getType(); }
    public String getName() { return name; }
    public UberRarity getRarity() { return rarity; }
    public boolean isStackable() { return stackable; }
    public int getUUID() { return UUID; }
    public boolean hasActiveEffect() { return hasActive; }
    public boolean hasCraftingRecipe() { return (craftingRecipe != null); }
    public UberCraftingRecipe getCraftingRecipe() { return craftingRecipe; }
    public void setCraftingRecipe(UberCraftingRecipe recipe) { this.craftingRecipe = recipe; }

}