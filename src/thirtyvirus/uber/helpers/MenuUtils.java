package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import thirtyvirus.multiversion.API;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.uber.UberItems;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    private UberItems main = null;
    public MenuUtils(UberItems main) { this.main = main; }

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
