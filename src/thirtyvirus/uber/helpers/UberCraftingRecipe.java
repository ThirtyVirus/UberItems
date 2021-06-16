package thirtyvirus.uber.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;

import java.util.List;

public class UberCraftingRecipe {

    private List<ItemStack> components;
    private boolean shapeless;
    private int craftAmount;

    // make an UberCraftingRecipe from a list of strings, assumes 9 items
    public UberCraftingRecipe(List<ItemStack> components, boolean shapeless, int craftAmount) {
        this.components = components;
        this.shapeless = shapeless;
        this.craftAmount = craftAmount;
    }

    // compare a list of ItemStacks to this recipe, assumes 9 items
    public boolean isEqual(List<ItemStack> items) {
        // verify that the list was no null entries
        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) items.set(counter, new ItemStack(Material.AIR));
        }

        // shaped crafting recipe
        if (!shapeless) {
            // test if the incoming list of items is "equal" to the recipe
            for (int counter = 0; counter < components.size(); counter++) {
                if (!isSame(components.get(counter), items.get(counter))) return false;
            }
            return true;
        }

        // TODO shapeless crafting recipe
        else {

        }
        return false;
    }

    // check if two ItemStacks are "the same", i2 can have a higher quantity than i1
    // distinguish between normal item and uber material / uber item
    private boolean isSame(ItemStack i1, ItemStack i2) {
        int UUID = Utilities.getIntFromItem(i1, "MaterialUUID");

        // item is an Uber Material
        if (UUID != 0) return (UberItems.getMaterialFromID(UUID).compare(i2) && i1.getAmount() <= i2.getAmount());

        // item is not an Uber Material
        // TODO compare more than material and amount? Do I even need to?
        else if (i1.getType() == Material.AIR && i2.getType() == Material.AIR) return true;
        else return (i1.getType() == i2.getType() && i1.getAmount() <= i2.getAmount());
    }

    // getters
    public ItemStack get(int index) { return components.get(index); }
    public int getCraftAmount() { return craftAmount; }
}