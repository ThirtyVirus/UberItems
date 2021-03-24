package thirtyvirus.uber.helpers;

import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;

import java.util.Comparator;
import java.util.Random;

public class ItemComparator implements Comparator<ItemStack> {

    public int compare(ItemStack a, ItemStack b) {

        int result = 0;
        switch (UberItems.sortingMode) {

            // alphabetical
            case 1:
                result = alphabetical(a, b);
                break;
            // smart
            case 2:
                result = smart(a, b);
                break;
            // scramble aka random
            case 3:
                Random rand = new Random();
                result = rand.nextInt(2) - 1;
                break;
        }

        if (UberItems.reverseSort) result *= -1;

        return result;
    }

    private int alphabetical(ItemStack a, ItemStack b) {
        return a.getType().name().compareTo(b.getType().name());
    }
    private int smart(ItemStack a, ItemStack b) {

        // building blocks
        // decoration
        // redstone
        // transportation
        // misc
        // food
        // tools
        // combat
        // brewing

        // put food first (test)
        if (a.getType().isEdible() && !b.getType().isEdible()) {
            return -1;
        }
        if (!a.getType().isEdible() && b.getType().isEdible()) {
            return 1;
        }

        // sort items by type (block, food, etc...)

        // sort equipment by max durability
        if (a.getType().getMaxDurability() > b.getType().getMaxDurability()) {
            return -1;
        }
        if (a.getType().getMaxDurability() < b.getType().getMaxDurability()) {
            return 1;
        }

        // sort equipment by durability
        if (a.isSimilar(b)) {
            if (a.getDurability() < b.getDurability()) {
                return -1;
            }
            if (a.getDurability() < b.getDurability()) {
                return 1;
            }
        }

        // final comparison, if no other factors to sort by
        return a.getType().name().compareTo(b.getType().name());
    }
}