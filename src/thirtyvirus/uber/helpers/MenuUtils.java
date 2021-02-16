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

    public static final ItemStack NEXT_BUTTON = Utilities.nameItem(Material.LIME_CONCRETE, ChatColor.GREEN + "Next Item");
    public static final ItemStack PREVIOUS_BUTTON = Utilities.nameItem(Material.RED_CONCRETE, ChatColor.RED + "Previous Item");

    public static final List<ItemStack> customItems = Arrays.asList(EMPTY_SLOT_ITEM, EMPTY_ERROR_SLOT_ITEM, CRAFTING_SLOT_ITEM, RECIPE_MENU_ITEM, UPGRADE_MENU_BUTTON, BACK_BUTTON, NEXT_BUTTON, PREVIOUS_BUTTON);

    public static Inventory createCustomCraftingMenu() {
        Inventory i = Bukkit.createInventory(null, 45, "UberItems - Craft Item");
        List<Integer> exceptions = Arrays.asList(10,11,12,19,20,21,28,29,30,23);
        for (int counter = 0; counter < 45; counter++) {
            if (!exceptions.contains(counter)) i.setItem(counter, EMPTY_SLOT_ITEM);
        }
        i.setItem(23, CRAFTING_SLOT_ITEM);

        i.setItem(16, UPGRADE_MENU_BUTTON);
        i.setItem(25, RECIPE_MENU_ITEM);
        i.setItem(34, BACK_BUTTON);

        return i;
    }

    public static Inventory createCustomCraftingTutorialMenu(int id) {
        ItemStack item = UberItem.fromString(UberItems.getInstance(), "" + id + "", 1);
        UberItem uber = Utilities.getUber(id);

        Inventory i1 = createCustomCraftingMenu();
        Inventory i2 = Bukkit.createInventory(null, 45, "Guide - " + item.getItemMeta().getDisplayName());
        i2.setContents(i1.getContents());

        i2.setItem(16, NEXT_BUTTON);
        i2.setItem(25, PREVIOUS_BUTTON);

        i2.setItem(23, item);

        // handle the specific crafting recipe
        List<Integer> exceptions = Arrays.asList(10,11,12,19,20,21,28,29,30);
        if (!uber.hasCraftingRecipe()) {
            for (int counter = 0; counter < exceptions.size(); counter++) {
                i2.setItem(exceptions.get(counter), EMPTY_ERROR_SLOT_ITEM);
            }
        }
        else {
            for (int counter = 0; counter < exceptions.size(); counter++) {
                i2.setItem(exceptions.get(counter), uber.getCraftingRecipe().get(counter));
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

}
