package thirtyvirus.uber;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

import java.util.ArrayList;
import java.util.List;

public class UberMaterial {

    private ItemStack item;
    private String name, description;
    private UberRarity rarity;
    private String raritySuffix = "";
    private boolean stackable;
    private int UUID;
    private boolean enchantGlint;
    private boolean isVanillaCraftable;

    private UberCraftingRecipe craftingRecipe;

    /**
     * define a new UberMaterial type
     */
    public UberMaterial(Material material, String name, UberRarity rarity, boolean enchantGlint, boolean stackable, boolean isVanillaCraftable, String description, UberCraftingRecipe craftingRecipe) {
        this.item = new ItemStack(material);
        this.name = name;
        this.description = description;
        this.rarity = rarity;

        this.enchantGlint = enchantGlint;
        this.stackable = stackable;
        this.isVanillaCraftable = isVanillaCraftable;

        this.craftingRecipe = craftingRecipe;
        UUID = Utilities.stringToSeed(material.name() + name + rarity.toString());
    }
    public UberMaterial(Material material, String name, UberRarity rarity, boolean enchantGlint, boolean stackable, boolean isVanillaCraftable, String description, UberCraftingRecipe craftingRecipe, String raritySuffix) {
        this(material, name, rarity, enchantGlint, stackable, isVanillaCraftable, description, craftingRecipe);
        this.raritySuffix = " " + raritySuffix;
    }
    public UberMaterial(ItemStack item, String name, UberRarity rarity, boolean enchantGlint, boolean stackable, boolean isVanillaCraftable, String description, UberCraftingRecipe craftingRecipe) {
        this.item = item;
        this.name = name;
        this.description = description;
        this.rarity = rarity;

        this.enchantGlint = enchantGlint;
        this.stackable = stackable;
        this.isVanillaCraftable = isVanillaCraftable;

        this.craftingRecipe = craftingRecipe;
        UUID = Utilities.stringToSeed(item.getType().name() + name + rarity.toString());
    }
    public UberMaterial(ItemStack item, String name, UberRarity rarity, boolean enchantGlint, boolean stackable, boolean isVanillaCraftable, String description, UberCraftingRecipe craftingRecipe, String raritySuffix) {
        this(item, name, rarity, enchantGlint, stackable, isVanillaCraftable, description, craftingRecipe);
        this.raritySuffix = " " + raritySuffix;
    }

    /**
     * @param other the ItemStack being compared
     * @return if two UberMaterials belong to the same type (ignores meta)
     */
    public boolean compare(ItemStack other) {
        int otherUUID = Utilities.getIntFromItem(other, "MaterialUUID");
        return (otherUUID == UUID);
    }

    /**
     * @param amount the maount of this item to make
     * @return an instance of this UberMaterial in ItemStack form
     */
    public ItemStack makeItem(int amount) {
        ItemStack newItem = item.clone();

        // remove vanilla attributes
        ItemMeta itemMeta = newItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        newItem.setItemMeta(itemMeta);

        Utilities.nameItem(newItem, rarity.getColor() + name);
        Utilities.storeIntInItem(newItem, UUID, "MaterialUUID");

        Utilities.loreItem(newItem, getLore());
        newItem.setAmount(amount);
        if (enchantGlint) Utilities.addEnchantGlint(newItem);
        if (!stackable) Utilities.storeStringInItem(newItem,  java.util.UUID.randomUUID().toString(), "UUID");

        return newItem;
    }

    // properly format the lore for Uber Items
    private List<String> getLore() {
        List<String> lore = new ArrayList<>();

        if (!description.equals("")) {
            lore.addAll(Utilities.stringToLore(description, 35, ChatColor.GRAY));
            lore.add("");
        }

        // show the option to right-click the item to view recipes (only if it is used in any)
        List<UberMaterial> materials = new ArrayList<>();
        for (UberMaterial material : UberItems.getMaterials()) {
            if (!material.hasCraftingRecipe()) continue;
            for (ItemStack component : material.getCraftingRecipe().getComponents()) {
                if (compare(component)) {
                    materials.add(material);
                    break;
                }
            }
        }
        List<UberItem> items = new ArrayList<>();
        for (UberItem item : UberItems.getItems()) {
            if (!item.hasCraftingRecipe()) continue;
            for (ItemStack component : item.getCraftingRecipe().getComponents()) {
                if (compare(component)) {
                    items.add(item);
                    break;
                }
            }
        }
        if (items.size() > 0 || materials.size() > 0) lore.add(ChatColor.YELLOW + "Right-click to view recipes!");

        // show the rarity of the item
        lore.add("" + rarity.getColor() + ChatColor.BOLD + rarity.toString().replace('_', ' ') + raritySuffix.toUpperCase());

        return lore;
    }

    // getters
    public ItemStack getRawItem() { return item; }
    public Material getMaterial() { return item.getType(); }
    public String getName() { return name; }
    public UberRarity getRarity() { return rarity; }
    public boolean isStackable() { return stackable; }
    public int getUUID() { return UUID; }
    public boolean isVanillaCraftable() { return isVanillaCraftable; }
    public boolean hasCraftingRecipe() { return (craftingRecipe != null); }
    public UberCraftingRecipe getCraftingRecipe() { return craftingRecipe; }
}
