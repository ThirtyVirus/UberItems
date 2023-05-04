package thirtyvirus.uber.items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.*;

public class crystal_ball extends UberItem {

    public crystal_ball(ItemStack itemStack, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(itemStack, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
        int storedExp = Utilities.getIntFromItem(item, "storedexp");
        double level = Math.round(getLevelFromExp(storedExp) * 100.0) / 100.0;
        lore.add(ChatColor.GRAY + "Stored Experience: " + ChatColor.GREEN + level + " levels.");
    }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) {
        if (player.getLevel() == 0 && player.getExp() == 0) return false;

        int storedExp = Utilities.getIntFromItem(item, "storedexp");
        int goal = 0;
        if (player.getLevel() > 0) goal = getExpToLevelUp(player.getLevel() - 1);
        else goal = getPlayerExp(player);

        setPlayerTotalExp(player, getPlayerExp(player) - goal);
        Utilities.storeIntInItem(item, storedExp + goal, "storedexp");

        updateLore(item);
        return false;
    }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return leftClickAirAction(player, item); }

    public boolean rightClickAirAction(Player player, ItemStack item) {

        int storedExp = Utilities.getIntFromItem(item, "storedexp");
        int goal = getExpToLevelUp(player.getLevel());
        if (storedExp >= goal) {
            setPlayerExp(player, goal);
            Utilities.storeIntInItem(item, storedExp - goal, "storedexp");
        }
        else {
            setPlayerExp(player, storedExp);
            Utilities.storeIntInItem(item, 0, "storedexp");
        }

        updateLore(item);
        return false;
    }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return rightClickAirAction(player, item); }

    public boolean shiftLeftClickAirAction(Player player, ItemStack item) {
        int storedExp = Utilities.getIntFromItem(item, "storedexp");
        storedExp += getPlayerExp(player);

        player.setLevel(0); player.setExp(0);
        Utilities.storeIntInItem(item, storedExp, "storedexp");
        updateLore(item);
        return false;
    }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return shiftLeftClickAirAction(player, item); }

    public boolean shiftRightClickAirAction(Player player, ItemStack item) {
        int storedExp = Utilities.getIntFromItem(item, "storedexp");
        storedExp += getPlayerExp(player);

        setPlayerTotalExp(player, storedExp);
        Utilities.storeIntInItem(item, 0, "storedexp");
        updateLore(item);
        return false;
    }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return shiftRightClickAirAction(player, item); }

    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }
    public boolean activeEffect(Player player, ItemStack item) { return false; }

    // EXPERIENCE HELPER FUNCTIONS

    // Calculate total experience based on level
    private static int getExpFromLevel(int level) {
        if (level <= 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level <= 31) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360.0);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220.0);
        }
    }

    // Calculate level based on total experience
    private static double getLevelFromExp(int experience) {
        if (experience > 1395) {
            return (Math.sqrt(72 * experience - 54215) + 325) / 18;
        } else if (experience > 315) {
            return Math.sqrt(40 * experience - 7839) / 10 + 8.1;
        } else if (experience > 0) {
            return Math.sqrt(experience + 9) - 3;
        }
        return 0;
    }

    // Calculate amount of EXP needed to level up
    private static int getExpToLevelUp(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }

    // Calculate player's current EXP amount
    // TODO fix rounding error at high levels?
    private static int getPlayerExp(Player player) {
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpFromLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

    // Give or take EXP in the current level
    private static int setPlayerExp(Player player, int exp) {
        // Get player's current exp
        int currentExp = getPlayerExp(player);

        // Reset player's current exp to 0
        player.setExp(0);
        player.setLevel(0);

        // Give the player their exp back, with the difference
        int newExp = currentExp + exp;
        player.giveExp(newExp);

        // Return the player's new exp amount
        return newExp;
    }

    private static void setPlayerTotalExp(Player player, int exp) {
        // Reset player's current exp to 0
        player.setExp(0);
        player.setLevel(0);

        player.giveExp(exp);
    }

}
