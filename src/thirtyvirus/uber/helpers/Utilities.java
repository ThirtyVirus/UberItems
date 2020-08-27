package thirtyvirus.uber.helpers;

import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
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

    private static Map<Player, Long> mostRecentSelect = new HashMap<>();

    // load file from JAR with comments
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

    // store a string value in the meta of an item, completely invisible to player
    public static void storeStringInItem(UberItems main, ItemStack host, String string, String key) {
        NamespacedKey k = new NamespacedKey(main, key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredString(), string);
        host.setItemMeta(itemMeta);
    }

    // retrieve a string value from the meta of an item, completely invisible to player
    public static String getStringFromItem(UberItems main, ItemStack host, String key) {
        NamespacedKey k = new NamespacedKey(main, key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return null;
        if (!host.hasItemMeta()) return null;

        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(k, new CompactInventory())) return container.get(k, new StoredString());

        // if no entry, return null
        return null;
    }

    // store an int value in the meta of an item, completely invisible to player
    public static void storeIntInItem(UberItems main, ItemStack host, Integer i, String key) {
        NamespacedKey k = new NamespacedKey(main, key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredInt(), i);
        host.setItemMeta(itemMeta);
    }

    // retrieve an int value from the meta of an item, completely invisible to player
    public static Integer getIntFromItem(UberItems main, ItemStack host, String key) {
        NamespacedKey k = new NamespacedKey(main, key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return 0;
        if (!host.hasItemMeta()) return 0;

        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(k, new CompactInventory())) return container.get(k, new StoredInt());

        // if no entry, return 0
        return 0;
    }

    // store a list of items in the meta of an item, completely invisible to the player
    public static void saveCompactInventory(UberItems main, ItemStack host, ItemStack[] items) {
        NamespacedKey key = new NamespacedKey(main, "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, new CompactInventory(), items);
        host.setItemMeta(itemMeta);
    }

    // retrieve a list of items from the meta of an item, completely invisible to the player
    public static ItemStack[] getCompactInventory(UberItems main, ItemStack host) {
        NamespacedKey key = new NamespacedKey(main, "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(key, new CompactInventory())) {
            ItemStack[] items = container.get(key, new CompactInventory());

            return items;
        }

        return new ItemStack[0];
    }

    // UBER ITEM CENTRIC FUNCTIONS
    // _____________________________________________________________________________ \\

    // test if given item is an UberItem
    public static boolean isUber(UberItems main, ItemStack item) {
        return getStringFromItem(main, item, "is-uber") != null;
    }

    // test if given item is a specific UberItem
    public static boolean isUber(UberItems main, ItemStack item, int id) {
        if (!isUber(main, item)) return false;
        return (getIntFromItem(main, item, "uber-id") == id);
    }

    // get the type of UberItem (null if not an UberItem)
    public static UberItem getUber(UberItems main, ItemStack item) {
        if (!isUber(main, item)) return null;
        return UberItems.items.get(getStringFromItem(main, item, "uber-name"));
    }

    // return UberItem with given name
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
    public static void uberActiveEffects(UberItems main) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isUber(main, player.getInventory().getItemInMainHand())) {
                if (getUber(main, player.getInventory().getItemInMainHand()).hasActiveEffect()) {
                    getUber(main, player.getInventory().getItemInMainHand()).activeEffect(player, player.getInventory().getItemInMainHand());
                }
            }
            if (isUber(main, player.getInventory().getItemInOffHand())) {
                if (getUber(main, player.getInventory().getItemInOffHand()).hasActiveEffect()) {
                    getUber(main, player.getInventory().getItemInOffHand()).activeEffect(player, player.getInventory().getItemInOffHand());
                }
            }
        }
    }

    // find uber item of given ID in inventory (null if nothing)
    public static ItemStack searchFor(UberItems main, Inventory inv, int id) {
        for (ItemStack item : inv) {
            if (isUber(main, item, id)) return item;
        }

        return null;
    }

}