package thirtyvirus.uber.helpers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import thirtyvirus.multiversion.API;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuUtils {

    private UberItems main = null;
    public MenuUtils(UberItems main) { this.main = main; }

    public static final ItemStack EMPTY_SLOT_ITEM = Utilities.nameItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    public static final ItemStack EMPTY_ERROR_SLOT_ITEM = Utilities.nameItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Item has no crafting recipe");
    public static final ItemStack CRAFTING_SLOT_ITEM = Utilities.loreItem(Utilities.nameItem(Material.BARRIER, ChatColor.RED + "Recipe Required"), Arrays.asList(ChatColor.GRAY + "Add the items for a valid", ChatColor.GRAY + "recipe in the crafting grid", ChatColor.GRAY + "to the left!"));

    public static final ItemStack UPGRADE_MENU_BUTTON = Utilities.loreItem(Utilities.nameItem(Material.ANVIL, ChatColor.GREEN + "Upgrade UberItem"), Arrays.asList(ChatColor.GRAY + "Open the Upgrade Menu"));
    public static final ItemStack RECIPE_MENU_ITEM = Utilities.loreItem(Utilities.nameItem(Material.KNOWLEDGE_BOOK, ChatColor.GREEN + "Recipe Guide"), Arrays.asList(ChatColor.GRAY + "View all UberItems Recipes"));

    public static final ItemStack BACK_BUTTON = Utilities.nameItem(Material.ARROW, ChatColor.GREEN + "Back");
    public static final ItemStack NEXT_BUTTON = Utilities.nameItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Next");
    public static final ItemStack PREVIOUS_BUTTON = Utilities.nameItem(Material.RED_CONCRETE, ChatColor.RED + "Previous");

    public static final List<ItemStack> customItems = Arrays.asList(EMPTY_SLOT_ITEM, EMPTY_ERROR_SLOT_ITEM, CRAFTING_SLOT_ITEM, RECIPE_MENU_ITEM, UPGRADE_MENU_BUTTON, BACK_BUTTON, PREVIOUS_BUTTON, NEXT_BUTTON);

    public static final List<Integer> CUSTOM_CRAFTING_MENU_EXCEPTIONS = Arrays.asList(10,11,12,19,20,21,28,29,30,23);
    public static final List<Integer> CRAFTING_GUIDE_MENU_EXCEPTIONS = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,51,52,53);
    public static final List<Integer> CRAFTING_GUIDE_ITEM_SLOTS = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);
    public static final int ITEMS_PER_GUIDE_PAGE = 28;

    public static Inventory createCustomCraftingMenu() {
        Inventory i = Bukkit.createInventory(null, 45, "UberItems - Craft Item");

        for (int counter = 0; counter < 45; counter++) {
            if (!CUSTOM_CRAFTING_MENU_EXCEPTIONS.contains(counter)) i.setItem(counter, EMPTY_SLOT_ITEM);
        }
        i.setItem(23, CRAFTING_SLOT_ITEM);

        i.setItem(16, UPGRADE_MENU_BUTTON);
        i.setItem(25, RECIPE_MENU_ITEM);
        i.setItem(34, BACK_BUTTON);

        return i;
    }

    public static Inventory createCustomCraftingTutorialMenu(int page) {

        // prep inventory elements
        Inventory i = Bukkit.createInventory(null, 54, "UberItems Guide - " + (page + 1));
        for (int index : CRAFTING_GUIDE_MENU_EXCEPTIONS) i.setItem(index, EMPTY_SLOT_ITEM);
        i.setItem(48, PREVIOUS_BUTTON);
        i.setItem(49, BACK_BUTTON);
        i.setItem(50, NEXT_BUTTON);

        // populate guide menu with UberItems and UberMaterials
        int counter = 0;
        int slot = 0;
        for (String name : UberItems.itemIDs.values()) {
            if (slot >= CRAFTING_GUIDE_ITEM_SLOTS.size()) break;

            if (counter < page * ITEMS_PER_GUIDE_PAGE || counter >= ITEMS_PER_GUIDE_PAGE * (page + 1)) {
                counter++; continue;
            }

            UberItem item = UberItems.items.get(name);
            i.setItem(CRAFTING_GUIDE_ITEM_SLOTS.get(slot), UberItem.fromString("" + item.getID() + "", 1));
            slot++;
        }
        for (UberMaterial material : UberItems.materials.values()) {
            if (slot >= CRAFTING_GUIDE_ITEM_SLOTS.size()) break;

            if (counter < page * ITEMS_PER_GUIDE_PAGE || counter >= ITEMS_PER_GUIDE_PAGE * (page + 1)) {
                counter++; continue;
            }

            i.setItem(CRAFTING_GUIDE_ITEM_SLOTS.get(slot), material.makeItem(1));
            slot++;
        }

        return i;
    }

    public static Inventory createShootyBoxAmmoGuide() {
        // prep inventory elements
        Inventory i = Bukkit.createInventory(null, 54, "UberItems ShootyBox Ammo Guide");
        for (int index : CRAFTING_GUIDE_MENU_EXCEPTIONS) i.setItem(index, EMPTY_SLOT_ITEM);
        i.setItem(48, EMPTY_SLOT_ITEM);
        i.setItem(49, BACK_BUTTON);
        i.setItem(50, EMPTY_SLOT_ITEM);

        List<ItemStack> guideItems = Arrays.asList(
                mgi(Material.ARROW, "Shoots the arrow at high speeds, without the need to pull a bowstring back!"),
                mgi(Material.FLINT, "Shoots a precise projectile long distances, can break blocks! 5x flint yield when used on gravel."),
                mgi(Material.SAND, "Throw sand in front of you, damaging enemies."),
                mgi(Material.GRAVEL, "Throw gravel in front of you, hurts enemies pretty badly."),
                mgi(Material.GLASS, "Launches shards of sharp glass at your enemies, yikes that's gotta hurt."),
                mgi(Material.GUNPOWDER, "Doesn't actually shoot much other than a lot of hot air, could be useful to knock enemies back."),
                mgi(Material.FIREWORK_ROCKET, "Shoots nothing, but JEEZ that recoil is insane."),
                mgi(Material.FIRE_CHARGE, "Shoots a small fireball, light blocks and enemies on fire!"),
                mgi(Material.TNT, "Throws a Primed TNT. Watch out, the blast is dangerous and there is some crazy recoil."),
                mgi(Material.ENDER_PEARL, "Imagine if a major league baseball player could throw a pearl... ya."),
                mgi(Material.EGG, "Throws an egg at high speed. What did you think was gonna happen?"),
                mgi(Material.SPLASH_POTION, "Throw potions a half mile away."),
                mgi(Material.WATER_BUCKET, "chucks water a mile away, because why not."),
                mgi(Material.LAVA_BUCKET, "a griefer's best friend."),
                mgi(Material.CREEPER_SPAWN_EGG, "Wait, this thing can shoot MOBS???"),
                mgi(Material.DIRT, "So ya, you can shoot blocks of any type. Neat!")
        );

        for (int counter = 0; counter < CRAFTING_GUIDE_ITEM_SLOTS.size(); counter++) {
            if (counter >= guideItems.size()) break;
            i.setItem(CRAFTING_GUIDE_ITEM_SLOTS.get(counter), guideItems.get(counter));
        }

        return i;
    }

    // make guide item (MGI)
    private static ItemStack mgi(Material material, String description) {
        ItemStack item = new ItemStack(material);
        Utilities.loreItem(item, Utilities.stringToLore(description, 25, ChatColor.GRAY));
        return item;
    }

    // creates a tutorial menu that is not bound to the normal rules
    public static Inventory createUnboundCraftingTutorialMenu(ItemStack example, UberCraftingRecipe recipe, int amount) {
        Inventory i1 = createCustomCraftingMenu();
        Inventory i2 = Bukkit.createInventory(null, 45, "UberItems Guide - " + example.getItemMeta().getDisplayName());
        i2.setContents(i1.getContents());

        ItemStack example2 = example.clone();
        example2.setAmount(amount);
        i2.setItem(23, example2);

        // handle the specific crafting recipe
        List<Integer> exceptions = Arrays.asList(10,11,12,19,20,21,28,29,30);

        if (recipe == null) {
            for (int counter = 0; counter < exceptions.size(); counter++) {
                i2.setItem(exceptions.get(counter), EMPTY_ERROR_SLOT_ITEM);
            }
        }
        else {
            for (int counter = 0; counter < exceptions.size(); counter++) {
                i2.setItem(exceptions.get(counter), recipe.get(counter));
            }
        }

        return i2;

    }

    public static void tutorialMenu(Player player) {
        ItemStack book = new ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial());
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setAuthor("ThirtyVirus");
        meta.setTitle("Welcome to TemplatePlugin!");

        List<String> pages = new ArrayList<String>();

        // exmaple main menu
        pages.add(ChatColor.translateAlternateColorCodes('&',
                "      &7&lWelcome to:" + "\n" +
                        "     &c&lUberItems&r" + "\n" +
                        "This guide book will show you everything you need to know about UberItems! Happy reading!" + "\n" +
                        "" + "\n" +
                        " - ThirtyVirus" + "\n" +
                        "" + "\n\n\n" +
                        "&7&lNext: Getting Items!"));

        // example secondary page
        pages.add(ChatColor.translateAlternateColorCodes('&',
                "&c&lGetting Items&r" + "\n" +
                        "" + "\n" +
                        "Grab an uber item with /uber give ____. Then the available items will be available with tab completion." + "\n" +
                        "" + "\n" +
                        "Items have a tutorial built - in with their ability descriptions." + "\n" +
                        "" + "\n"));

        meta.setPages(pages);
        book.setItemMeta(meta);

        Utilities.playSound(ActionSound.CLICK, player);
        API.openBook(book, player);
    }

    // make the craftable UberItem appear in the crafted slot if
    public static void checkCraft(Inventory i) {

        // put all crafting grid items into a list
        List<ItemStack> items = Arrays.asList(
                i.getItem(10), i.getItem(11), i.getItem(12),
                i.getItem(19), i.getItem(20), i.getItem(21),
                i.getItem(28), i.getItem(29), i.getItem(30));

        // check if any UberItem has a matching recipe to what's in the crafting grid
        for (UberItem item : UberItems.items.values()) {
            if (!item.hasCraftingRecipe()) continue;

            if (item.getCraftingRecipe().isEqual(items)) {
                i.setItem(23, UberItem.fromString("" + item.getID(), 1));
                return;
            }
            else i.setItem(23, MenuUtils.CRAFTING_SLOT_ITEM);
        }

        // check if any UberMaterial has a matching recipe to what's in the crafting grid
        for (UberMaterial item : UberItems.materials.values()) {
            if (!item.hasCraftingRecipe()) continue;

            if (item.getCraftingRecipe().isEqual(items)) {
                i.setItem(23, item.makeItem(item.getCraftAmount()));
                return;
            }
            else i.setItem(23, MenuUtils.CRAFTING_SLOT_ITEM);
        }
    }

}
