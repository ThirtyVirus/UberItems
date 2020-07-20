package thirtyvirus.uber.helpers;

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import thirtyvirus.multiversion.Sound;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;

import java.io.*;
import java.util.*;

public final class Utilities {

    // list of transparent blocks to be ignored when a player looks at a block
    private static final Set<Material> TRANSPARENT = EnumSet.of(XMaterial.AIR.parseMaterial(), XMaterial.BLACK_CARPET.parseMaterial(), XMaterial.BLUE_CARPET.parseMaterial(),
            XMaterial.BROWN_CARPET.parseMaterial(), XMaterial.CYAN_CARPET.parseMaterial(), XMaterial.GRAY_CARPET.parseMaterial(), XMaterial.GREEN_CARPET.parseMaterial(), XMaterial.LIGHT_BLUE_CARPET.parseMaterial(),
            XMaterial.LIME_CARPET.parseMaterial(), XMaterial.MAGENTA_CARPET.parseMaterial(), XMaterial.ORANGE_CARPET.parseMaterial(), XMaterial.PINK_CARPET.parseMaterial(), XMaterial.PURPLE_CARPET.parseMaterial(),
            XMaterial.RED_CARPET.parseMaterial(), XMaterial.WHITE_CARPET.parseMaterial(), XMaterial.YELLOW_CARPET.parseMaterial());

    // list of all supported inventory blocks in the plugin
    public static final List<Material> INVENTORY_BLOCKS = Arrays.asList(XMaterial.CHEST.parseMaterial(), XMaterial.TRAPPED_CHEST.parseMaterial(), XMaterial.ENDER_CHEST.parseMaterial(), XMaterial.SHULKER_BOX.parseMaterial(), XMaterial.BLACK_SHULKER_BOX.parseMaterial(),
            XMaterial.BLUE_SHULKER_BOX.parseMaterial(), XMaterial.BROWN_SHULKER_BOX.parseMaterial(), XMaterial.CYAN_SHULKER_BOX.parseMaterial(), XMaterial.GRAY_SHULKER_BOX.parseMaterial(),
            XMaterial.GREEN_SHULKER_BOX.parseMaterial(), XMaterial.LIGHT_BLUE_SHULKER_BOX.parseMaterial(), XMaterial.LIGHT_GRAY_SHULKER_BOX.parseMaterial(), XMaterial.LIME_SHULKER_BOX.parseMaterial(),
            XMaterial.MAGENTA_SHULKER_BOX.parseMaterial(), XMaterial.ORANGE_SHULKER_BOX.parseMaterial(), XMaterial.PINK_SHULKER_BOX.parseMaterial(), XMaterial.PURPLE_SHULKER_BOX.parseMaterial(),
            XMaterial.RED_SHULKER_BOX.parseMaterial(), XMaterial.WHITE_SHULKER_BOX.parseMaterial(), XMaterial.YELLOW_SHULKER_BOX.parseMaterial());

    private static Map<Player, Long> mostRecentSelect = new HashMap<>();

    // loads file from JAR with comments
    public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    // convert a location to formatted string (world,x,y,z)
    public static String toLocString(Location location) {
        if (location.equals(null)) return "";
        return location.getWorld().getName() + "," + (int) location.getX() + "," + (int) location.getY() + "," + (int) location.getZ();
    }

    // convert a formatted location string to a Location
    public static Location fromLocString(String locString) {
        if (locString.equals("")) return null;
        String[] data = locString.split(",");
        return new Location(Bukkit.getWorld(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
    }

    // renames item
    public static ItemStack nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    // creates item that is renamed given material and name
    public static ItemStack nameItem(Material item, String name) {
        return nameItem(new ItemStack(item), name);
    }

    // set the lore of an item
    public static ItemStack loreItem(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    // makes visible string invisible to player
    public static String convertToInvisibleString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR + "" + c;
        return hidden;
    }

    // make invisible string visible to player
    public static String convertToVisibleString(String s) {
        String c = "";
        c = c + ChatColor.COLOR_CHAR;
        return s.replaceAll(c, "");
    }

    // send player a collection of error messages and play error noise
    public static void warnPlayer(CommandSender sender, List<String> messages) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            playSound(ActionSound.ERROR, player);
        }

        for (String message : messages) {
            sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.RED + message);
        }
    }

    // send player a collection of messages
    public static void informPlayer(CommandSender sender, List<String> messages) {
        for (String message : messages) {
            sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.GRAY + message);
        }
    }

    // return the block the player is looking at, ignoring transparent blocks
    public static Block getBlockLookingAt(Player player) {
        return player.getTargetBlock(TRANSPARENT, 120);
    }

    // play sound at player (version independent)
    public static void playSound(ActionSound sound, Player player) {

        switch (sound) {
            case OPEN:
                Sound.CHEST_OPEN.playSound(player);
                break;
            case MODIFY:
                Sound.ANVIL_USE.playSound(player);
                break;
            case SELECT:
                Sound.LEVEL_UP.playSound(player);
                break;
            case CLICK:
                Sound.CLICK.playSound(player);
                break;
            case POP:
                Sound.CHICKEN_EGG_POP.playSound(player);
                break;
            case BREAK:
                Sound.ANVIL_LAND.playSound(player);
                break;
            case ERROR:
                Sound.BAT_DEATH.playSound(player);
                break;
        }

    }

    // test if given number string is integer
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // _____________________________________________________________________________ \\

    //Test if given item is an UberItem
    @SuppressWarnings("deprecation")
    public static boolean isUber(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemStack testItem = item.clone();
        testItem.setDurability((short) 0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        if (meta == null) {
            return false;
        }
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for (String key : UberItems.items.keySet()) {
            ItemStack loopItem = UberItems.items.get(key).getItem();
            Utilities.loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(loopItem)) return true;
        }

        return false;
    }

    //Test if given item is a specific Uber item
    @SuppressWarnings("deprecation")
    public static boolean isUber(ItemStack item, int id) {
        if (item == null) {
            return false;
        }

        ItemStack testItem = item.clone();
        testItem.setDurability((short) 0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        if (meta == null) {
            return false;
        }
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for (String key : UberItems.items.keySet()) {
            ItemStack loopItem = UberItems.items.get(key).getItem();
            Utilities.loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(loopItem) && UberItems.items.get(key).getID() == id) return true;
        }

        return false;
    }

    //Get the type of uber item (null if not uber)
    @SuppressWarnings("deprecation")
    public static UberItem getUber(ItemStack item) {
        ItemStack testItem = item.clone();
        testItem.setDurability((short) 0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for (String key : UberItems.items.keySet()) {
            UberItem uberr = UberItems.items.get(key);
            Utilities.loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(uberr.getItem())) return uberr;
        }

        return null;
    }

    //Return uber item with given name
    public static UberItem getUber(String name) {
        for (String key : UberItems.items.keySet()) {
            UberItem uberr = UberItems.items.get(key);
            if (uberr.getName().equals(name)) return uberr;
        }
        return null;
    }

    // process active effets for uber items that are in use
    //TODO: Find more efficient way to do this?
    //TODO: Do active effects for uber items not in hand?
    public static void uberActiveEffects() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isUber(player.getInventory().getItemInMainHand())) {
                if (getUber(player.getInventory().getItemInMainHand()).hasActiveEffect()) {
                    getUber(player.getInventory().getItemInMainHand()).activeEffect(player, player.getInventory().getItemInMainHand());
                }
            }
            if (isUber(player.getInventory().getItemInOffHand())) {
                if (getUber(player.getInventory().getItemInOffHand()).hasActiveEffect()) {
                    getUber(player.getInventory().getItemInOffHand()).activeEffect(player, player.getInventory().getItemInOffHand());
                }
            }
        }
    }

    // find uber item of given ID in inventory (null if nothing)
    public static ItemStack searchFor(Inventory inv, int id) {
        for (ItemStack item : inv) {
            if (isUber(item, id)) return item;
        }

        return null;
    }
}