package thirtyvirus.uber.helpers;

import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;

import java.io.*;
import java.util.*;

public final class Utilities {

    // list of transparent blocks to be ignored when a player looks at a block
    private static final Set<Material> TRANSPARENT = EnumSet.of(Material.AIR, Material.CAVE_AIR,
            Material.BLACK_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.CYAN_CARPET,
            Material.GRAY_CARPET, Material.GREEN_CARPET, Material.LIGHT_BLUE_CARPET, Material.LIME_CARPET,
            Material.MAGENTA_CARPET, Material.ORANGE_CARPET, Material.PINK_CARPET, Material.PURPLE_CARPET,
            Material.RED_CARPET, Material.WHITE_CARPET, Material.YELLOW_CARPET);

    // store the most recent attempts at sorting by each player on the server
    private static Map<Player, Long> mostRecentSelect = new HashMap<>();

    // GENERAL PLUGIN FUNCTIONS
    // _____________________________________________________________________________ \\

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

    // warns player of something in plugin and plays error noise
    public static void warnPlayer(CommandSender sender, List<String> messages) {
        if (sender instanceof Player) { Player player = (Player) sender; playSound(ActionSound.ERROR, player); }
        for (String message : messages) sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.RED + message);
    }
    public static void warnPlayer(CommandSender sender, String message) {
        warnPlayer(sender, Collections.singletonList(message));
    }

    // informs player of something in plugin
    public static void informPlayer(CommandSender sender, List<String> messages) {
        for (String message : messages) sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.GRAY + message);
    }
    public static void informPlayer(CommandSender sender, String message) {
        informPlayer(sender, Collections.singletonList(message));
    }

    // return the block the player is looking at, ignoring transparent blocks
    public static Block getBlockLookingAt(Player player) {
        return player.getTargetBlock(TRANSPARENT, 120);
    }

    // convert a location to formatted string (world,x,y,z)
    public static String toLocString(Location location) {
        if (location == null || location.getWorld() == null) return "";
        return location.getWorld().getName() + "," + (int) location.getX() + "," + (int) location.getY() + "," + (int) location.getZ();
    }

    // convert a formatted location string to a Location
    public static Location fromLocString(String locString) {
        if (locString.equals("")) return null;
        String[] data = locString.split(",");
        return new Location(Bukkit.getWorld(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
    }

    // play sound at player
    public static void playSound(ActionSound sound, Player player) {

        switch (sound) {
            case OPEN:
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN,1,1);
                break;
            case MODIFY:
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1,1);
                break;
            case SELECT:
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
                break;
            case CLICK:
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,1,1);
                break;
            case POP:
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG,1,1);
                break;
            case BREAK:
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND,1,1);
                break;
            case ERROR:
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,1,0.5f);
                break;
        }

    }

    // shorthand for using the bukkit scheduler to run a runnable after i ticks
    public static void scheduleTask(Runnable run, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), run, i);
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

    // make a string into a key that can be used with the Random class
    public static int stringToSeed(String s) {
        if (s == null) {
            return 0;
        }
        int hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31*hash + c;
        }
        return hash;
    }

    // seperate a single string into mulitple lines that can be used as Item Lore
    // "/newline" can be used to force a new line
    public static List<String> stringToLore(String string, int characterLimit, ChatColor prefixColor) {
        String[] words = string.split(" ");
        List<String> lines = new ArrayList<>();

        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {

            // add word to line
            if (!word.equals("/newline")) {
                if (currentLine.toString().equals("")) currentLine = new StringBuilder(word);
                else currentLine.append(" ").append(word);
            }

            // test if adding the word would make the line too long, start new line if so
            if (word.equals("/newline") || currentLine.length() + word.length() >= characterLimit) {
                String newLine = currentLine.toString();
                lines.add("" + prefixColor + newLine);
                currentLine = new StringBuilder();
            }
        }
        if (currentLine.length() > 0) lines.add("" + prefixColor + currentLine);

        return lines;
    }

    // ITEM FUNCTIONS
    // _____________________________________________________________________________ \\

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

    // add enchantment glint to item
    public static void addEnchantGlint(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    // repair a (repairable) item
    public static void repairItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            ((Damageable)meta).setDamage(0);
            item.setItemMeta(meta);
        }
    }

    // store a string value in the meta of an item, completely invisible to player
    public static void storeStringInItem(ItemStack host, String string, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredString(), string);
        host.setItemMeta(itemMeta);
    }

    // retrieve a string value from the meta of an item, completely invisible to player
    public static String getStringFromItem(ItemStack host, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

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
    public static void storeIntInItem(ItemStack host, Integer i, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredInt(), i);
        host.setItemMeta(itemMeta);
    }

    // retrieve an int value from the meta of an item, completely invisible to player
    public static Integer getIntFromItem(ItemStack host, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

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
    public static void saveCompactInventory(ItemStack host, ItemStack[] items) {
        NamespacedKey key = new NamespacedKey(UberItems.getInstance(), "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, new CompactInventory(), items);
        host.setItemMeta(itemMeta);
    }

    // retrieve a list of items from the meta of an item, completely invisible to the player
    public static ItemStack[] getCompactInventory(ItemStack host) {
        NamespacedKey key = new NamespacedKey(UberItems.getInstance(), "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(key, new CompactInventory()))
            return container.get(key, new CompactInventory());

        return new ItemStack[0];
    }

    // UBERITEM FUNCTIONS
    // _____________________________________________________________________________ \\

    // test if given item is an UberItem
    public static boolean isUber(ItemStack item) {
        return getStringFromItem(item, "is-uber") != null;
    }
    public static boolean isUber(ItemStack item, int id) {
        if (!isUber(item)) return false;
        return (getIntFromItem(item, "uber-id") == id);
    }
    public static boolean isUberMaterial(ItemStack item) { return getIntFromItem(item, "MaterialUUID") != 0;}

    // get the type of UberItem (null if not an UberItem)
    public static UberItem getUber(ItemStack item) {
        if (!isUber(item)) return null;
        return UberItems.items.get(getStringFromItem(item, "uber-name"));
    }
    public static UberMaterial getUberMaterial(ItemStack item) {
        int UUID = getIntFromItem(item, "MaterialUUID");
        if (UUID == 0) return null;
        else return UberItems.materialIDs.get(UUID);
    }

    // return UberItem with given name or ID
    public static UberItem getUber(String name) {
        for (String key : UberItems.items.keySet()) {
            UberItem uberr = UberItems.items.get(key);
            if (uberr.getName().equals(name)) return uberr;
        }
        return null;
    }
    public static UberItem getUber(int id) {
        for (String key : UberItems.items.keySet()) {
            UberItem uberr = UberItems.items.get(key);
            if (uberr.getID() == id) return uberr;
        }
        return null;
    }

    // process active effets for uber items that are in use
    //TODO: Do active effects for uber items not in hand?
    public static void uberActiveEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            // main hand
            if (isUber(player.getInventory().getItemInMainHand())) {
                UberItem uber = getUber(player.getInventory().getItemInMainHand());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInMainHand());
            }

            // off hand
            if (isUber(player.getInventory().getItemInOffHand())) {
                UberItem uber = getUber(player.getInventory().getItemInOffHand());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());

            }

        }
    }

    // find the first uber item of given ID in inventory (null if nothing)
    public static ItemStack searchFor(Inventory inv, int id) {
        for (ItemStack item : inv) {
            if (isUber(item, id)) return item;
        }

        return null;
    }

    // delays code based on stored int in UberItem,
    public static boolean enforceCooldown(Player player, String key, double seconds, ItemStack item, boolean throwError) {
        double time = (double)System.currentTimeMillis() / 1000;

        // get time last used from item
        int lastTime = getIntFromItem(item, key);

        // add "time last used" key if not already there
        if (lastTime == 0) {
            storeIntInItem(item, (int)time, key);
            return true;
        }

        // was the item  last used longer than "seconds" seconds ago?
        else {
            if (time - seconds > lastTime) {
                storeIntInItem(item, (int)time, key);
                return true; // yes, allow the action (plus update the last time used)
            }
            else {
                int timeLeft = (int)time - lastTime;
                timeLeft = (int)seconds - timeLeft;
                if (throwError) warnPlayer(player, "This ability is on cooldown for " + timeLeft + "s.");
                return false; // no, disallow the action
            }
        }
    }

    // UPGRADE RULES
    // Upgrade name must be [a-z0-9A-Z/._-]

    // checks if the requested upgrade is already on the item, if not adds upgrade, cancels event and consumes item
    public static void applyUpgrade(Player player, InventoryClickEvent event, ItemStack item, String upgradeName, String upgradeDescription) {
        // verify that the upgrade isn't already on the item
        if (hasUpgrade(item, upgradeName)) return;

        // actually apply the upgrade to the item
        addUpgrade(item, upgradeName, upgradeDescription);

        // confirm application of the upgrade, play sound and consume addition item
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        ItemStack addition = event.getWhoClicked().getItemOnCursor();
        if (addition.getAmount() > 1) addition.setAmount(addition.getAmount() - 1);
        else event.getWhoClicked().setItemOnCursor(null);

        event.setCancelled(true);
    }

    // checks if the requested upgrade is already on the item, if so remove upgrade, cancels event and consumes item
    public static void unapplyUpgrade(Player player, InventoryClickEvent event, ItemStack item, String upgradeName) {
        // verify that the upgrade is on the item
        if (!hasUpgrade(item, upgradeName)) return;

        // actually remove the upgrade from the item
        removeUpgrade(item, upgradeName);

        // confirm removal of the upgrade, play sound and consume addition item
        player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);

        ItemStack addition = event.getWhoClicked().getItemOnCursor();
        if (addition.getAmount() > 1) addition.setAmount(addition.getAmount() - 1);
        else event.getWhoClicked().setItemOnCursor(null);

        event.setCancelled(true);
    }

    // apply upgrade to UberItem, and update lore
    public static void addUpgrade(ItemStack item, String upgradeName, String upgradeDescription) {
        UberItem uber = getUber(item); if (uber == null) return;

        // generate an updated upgrade list
        List<String> upgrades = new ArrayList<>(); String[] rawUpgrades = getUpgrades(item); StringBuilder updatedUpgradeList = new StringBuilder();
        if (rawUpgrades != null) for (String upgrade : rawUpgrades) { if (!upgrades.contains(upgrade)) upgrades.add(upgrade); }
        if (!upgrades.contains(upgradeName)) upgrades.add(upgradeName);
        for (String upgrade : upgrades) updatedUpgradeList.append(upgrade).append(",");
        if (updatedUpgradeList.length() > 0) updatedUpgradeList = new StringBuilder(updatedUpgradeList.substring(0, updatedUpgradeList.length() - 1));

        storeStringInItem(item, updatedUpgradeList.toString(), "UberUpgrades");
        storeStringInItem(item, upgradeDescription, "UberUpgrade-" + upgradeName);

        // update the item lore to mirror the change
        uber.updateLore(item);
    }
    // remove upgrade from UberItem, and update lore
    public static void removeUpgrade(ItemStack item, String upgradeName) {
        UberItem uber = getUber(item); if (uber == null) return;

        // generate an updated upgrade list
        List<String> upgrades = new ArrayList<>(); String[] rawUpgrades = getUpgrades(item); StringBuilder updatedUpgradeList = new StringBuilder();
        if (rawUpgrades != null) for (String upgrade : rawUpgrades) { if (!upgrades.contains(upgrade)) upgrades.add(upgrade); }
        upgrades.remove(upgradeName);
        for (String upgrade : upgrades) updatedUpgradeList.append(upgrade).append(",");
        if (updatedUpgradeList.length() > 0) updatedUpgradeList = new StringBuilder(updatedUpgradeList.substring(0, updatedUpgradeList.length() - 1));

        storeStringInItem(item, updatedUpgradeList.toString(), "UberUpgrades");
        storeStringInItem(item, "", "UberUpgrade-" + upgradeName);

        // update the item lore to mirror the change
        uber.updateLore(item);
    }
    // test whether or not an item has an upgrade
    public static boolean hasUpgrade(ItemStack item, String upgradeName) {
        String upgrade = getStringFromItem(item, "UberUpgrade-" + upgradeName);
        if (upgrade == null) return false;
        return !upgrade.equals("");
    }
    // get the description of an Uber Upgrade
    public static String getUpgradeDescription(ItemStack item, String upgradeName) {
        return getStringFromItem(item, "UberUpgrade-" + upgradeName);
    }
    // list all of the current upgrades on this item
    public static String[] getUpgrades(ItemStack item) {
        String rawList = getStringFromItem(item, "UberUpgrades");
        if (rawList != null) return rawList.split(",");
        else return null;
    }

}