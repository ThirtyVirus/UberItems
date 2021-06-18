package thirtyvirus.uber.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;

import java.util.ArrayList;
import java.util.List;

public class UberCraftingRecipe {

    private List<ItemStack> recipe = new ArrayList<ItemStack>();
    private List<ItemStack> components = new ArrayList<ItemStack>();
    private List<int[]> relativeCoords = new ArrayList<int[]>();
    private boolean shapeless;
    private int craftAmount;

    // make an UberCraftingRecipe from a list of strings, assumes 9 items
    public UberCraftingRecipe(List<ItemStack> recipe, boolean shapeless, int craftAmount) {
        this.recipe = recipe;
        this.shapeless = shapeless;
        this.craftAmount = craftAmount;

        // populate components and relativeCoords
        int[] prevCoord = new int[]{0,0};
        for (int counter = 0; counter < recipe.size(); counter++) {
            ItemStack item = recipe.get(counter);
            if (item.getType() == Material.AIR) continue;
            components.add(item);
            if (components.size() == 1) {
                prevCoord = indexToCoord(counter, 3);
                relativeCoords.add(prevCoord.clone());
            }
            else {
                int[] coord = indexToCoord(counter, 3);
                relativeCoords.add(new int[] { coord[0] - prevCoord[0], coord[1] - prevCoord[1] });
                int x = coord[0] - prevCoord[0];
                int y = coord[1] - prevCoord[1];
                prevCoord = indexToCoord(counter, 3);
            }
        }
    }

    // compare a list of ItemStacks to this recipe, assumes 9 items
    public boolean isEqual(List<ItemStack> items) {
        // verify that the list was no null entries
        for (int counter = 0; counter < items.size(); counter++) {
            if (items.get(counter) == null) items.set(counter, new ItemStack(Material.AIR));
        }

        // shaped crafting recipe
        if (!shapeless) {

            // get first element of the recipe, test if present
            ItemStack firstMaterial = null;
            int[] previousCoord = new int[] {0,0};
            for (int counter = 0; counter < items.size(); counter++) {
                if (isSame(components.get(0), items.get(counter))) {
                    firstMaterial = items.get(counter);
                    previousCoord = indexToCoord(counter, 3);
                    break;
                }
            }
            if (firstMaterial == null) return false;

            // test if the rest of the components are present
            for (int counter = 1; counter < components.size(); counter++) {
                int x = previousCoord[0] + relativeCoords.get(counter)[0];
                int y = previousCoord[1] + relativeCoords.get(counter)[1];
                int newSpot = coordToIndex(new int[] {x,y}, 3);

                if (!isSame(components.get(counter), items.get(newSpot))) return false;
                previousCoord = new int[]{x,y};
            }

            return true;
        }

        // shapeless crafting recipe
        else {
            ArrayList<ItemStack> mats = new ArrayList<ItemStack>(components);
            mats.removeIf(mat -> mat.getType() == Material.AIR);
            for (ItemStack item : items) {
                if (item.getType() == Material.AIR) continue;
                for (ItemStack mat : mats) {
                    if (isSame(mat, item)) {
                        mats.remove(mat); break;
                    }
                }
            }
            return mats.size() == 0;
        }
    }

    // consume the materials in a given crafting grid
    public void consumeMaterials(List<ItemStack> items) {

        // shaped crafting recipe
        if (!shapeless) {

            // get first element of the recipe, test if present
            int[] previousCoord = new int[] {0,0};
            for (int counter = 0; counter < items.size(); counter++) {
                if (isSame(components.get(0), items.get(counter))) {
                    items.get(counter).setAmount(items.get(counter).getAmount() - components.get(0).getAmount());
                    previousCoord = indexToCoord(counter, 3);
                    break;
                }
            }

            // test if the rest of the components are present
            for (int counter = 1; counter < components.size(); counter++) {
                int x = previousCoord[0] + relativeCoords.get(counter)[0];
                int y = previousCoord[1] + relativeCoords.get(counter)[1];
                int newSpot = coordToIndex(new int[] {x,y}, 3);

                items.get(newSpot).setAmount(items.get(newSpot).getAmount() - components.get(counter).getAmount());
                previousCoord = new int[]{x,y};
            }

        }

        // shapeless crafting recipe
        else {
            ArrayList<ItemStack> mats = new ArrayList<ItemStack>(components);
            mats.removeIf(mat -> mat.getType() == Material.AIR);
            for (ItemStack item : items) {
                if (item.getType() == Material.AIR) continue;
                for (ItemStack mat : mats) {
                    if (isSame(mat, item)) {
                        item.setAmount(item.getAmount() - mat.getAmount());
                        mats.remove(mat); break;
                    }
                }
            }
        }

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
    public ItemStack get(int index) { return recipe.get(index); }
    public int getCraftAmount() { return craftAmount; }

    // convert between a list index and a location in a 2 dimensional array
    private int[] indexToCoord(int index, int craftingGridWidth) {
        int x = 0, y = 0;
        for (int counter = 0; counter < index; counter++) {
            x++;
            if (x == craftingGridWidth) { y++; x = 0; }
        }
        return new int[] {x,y};
    }
    private int coordToIndex(int[] coord, int craftingGridWidth) {
        return craftingGridWidth * coord[1] + coord[0];
    }
}