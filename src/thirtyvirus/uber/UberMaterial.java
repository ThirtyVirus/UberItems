package thirtyvirus.uber;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

import java.util.ArrayList;
import java.util.List;

public class UberMaterial {

    private Material material;
    private String name, description;
    private UberRarity rarity;
    private boolean stackable;
    private int UUID;
    private boolean enchantGlint;
    private boolean isVanillaCraftable;

    private UberCraftingRecipe craftingRecipe;

    // define a new UberMaterial type
    public UberMaterial(Material material, String name, UberRarity rarity, boolean enchantGlint, boolean stackable, boolean isVanillaCraftable, String description, UberCraftingRecipe craftingRecipe) {
        this.material = material;
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.enchantGlint = enchantGlint;
        this.stackable = stackable;
        this.isVanillaCraftable = isVanillaCraftable;
        this.craftingRecipe = craftingRecipe;

        UUID = Utilities.stringToSeed(material.name() + name + rarity.toString());
    }

    // test if two ingredients are the same
    public boolean compare(ItemStack other) {
        int otherUUID = Utilities.getIntFromItem(other, "MaterialUUID");
        return (otherUUID == UUID);
    }

    // make an instance of this UberMaterial in ItemStack form
    public ItemStack makeItem(int amount) {
        ItemStack item = Utilities.nameItem(material, rarity.getColor() + name);
        Utilities.storeIntInItem(item, UUID, "MaterialUUID");
        Utilities.loreItem(item, getLore());
        item.setAmount(amount);
        if (enchantGlint) Utilities.addEnchantGlint(item);
        if (!stackable) Utilities.storeStringInItem(item,  java.util.UUID.randomUUID().toString(), "UUID");

        return item;
    }

    // properly format the lore for Uber Items
    private List<String> getLore() {
        List<String> lore = new ArrayList<>();

        if (!description.equals("")) {
            lore.addAll(Utilities.stringToLore(description, 35, ChatColor.GRAY));
            lore.add("");
        }

        // show the rarity of the item
        lore.add("" + rarity.getColor() + ChatColor.BOLD + rarity.toString() + " MATERIAL");

        return lore;
    }

    // getters
    public Material getMaterial() { return material; }
    public String getName() { return name; }
    public UberRarity getRarity() { return rarity; }
    public boolean isStackable() { return stackable; }
    public int getUUID() { return UUID; }
    public boolean isVanillaCraftable() { return isVanillaCraftable; }
    public boolean hasCraftingRecipe() { return (craftingRecipe != null); }
    public UberCraftingRecipe getCraftingRecipe() { return craftingRecipe; }
}
