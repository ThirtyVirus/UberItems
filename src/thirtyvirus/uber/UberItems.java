//UberItems for CraftBukkit and Spigot
//Author: Brandon (ThirtyVirus) Calabrese

package thirtyvirus.uber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.uber.commands.uber_command;
import thirtyvirus.uber.events.block.BlockBreak;
import thirtyvirus.uber.events.inventory.InventoryClick;
import thirtyvirus.uber.events.inventory.InventoryClose;
import thirtyvirus.uber.events.inventory.RenameItem;
import thirtyvirus.uber.events.player.Bucket;
import thirtyvirus.uber.events.player.FoodLevelChange;
import thirtyvirus.uber.events.player.PlayerUse;
import thirtyvirus.uber.items.big_bucket;
import thirtyvirus.uber.items.boom_stick;
import thirtyvirus.uber.items.builders_wand;
import thirtyvirus.uber.items.chisel;
import thirtyvirus.uber.items.document_of_order;
import thirtyvirus.uber.items.electromagnet;
import thirtyvirus.uber.items.escape_rope;
import thirtyvirus.uber.items.fireball;
import thirtyvirus.uber.items.infini_gulp;
import thirtyvirus.uber.items.lunch_box;
import thirtyvirus.uber.items.pocket_portal;
import thirtyvirus.uber.items.shooty_box;
import thirtyvirus.uber.items.smart_pack;
import thirtyvirus.uber.items.uncle_sams_wrath;
import thirtyvirus.uber.items.wrench;

public class UberItems extends JavaPlugin {

    PluginDescriptionFile descFile = getDescription();
    PluginManager pm = getServer().getPluginManager();
    Logger logger = getLogger();

    //static String prefix = "&6&l[&3&lS&d&lC&6&l]&r&B ";
    public static String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + ChatColor.BOLD + "U" + ChatColor.GOLD + ChatColor.BOLD + "]" + ChatColor.RESET + ChatColor.GRAY + " ";
    public static String itemPrefix = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[UBER] " + ChatColor.GRAY;

    //Permissions
    public Permission give = new Permission("uber.give");

    //Data for all Uber Items
    public static Map<String, UberItem> items = new HashMap<String, UberItem>();
    public static Map<Integer, String> itemIDs = new HashMap<Integer, String>();

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 5; //in ticks

    //Processes to be carried out at server start
    public void onEnable(){
        registerCommands();
        registerEvents();
        registerPermissions();
        registerUberItems();

        //posts in chat
        logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been enabled");

        //schedule repeating task for processing Uber Item active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() { uberActiveEffects(); } }, activeEffectsDelay, activeEffectsDelay);

    }

    //Processes to be carried out at server stop
    public void onDisable() {
        //Cancel scheduled task
        Bukkit.getScheduler().cancelTask(activeEffectsCheckID);

        //posts exit message in chat
        logger.info(descFile.getName() + " V: " + descFile.getVersion() + " has been disabled");
    }

    //Register Commands
    public void registerCommands() {
        getCommand("uber").setExecutor(new uber_command(this));;
    }

    //Register Events
    public void registerEvents() {
        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new FoodLevelChange(), this);
        pm.registerEvents(new InventoryClick(),this);
        pm.registerEvents(new PlayerUse(), this);
        pm.registerEvents(new RenameItem(), this);
        pm.registerEvents(new InventoryClose(), this);
        pm.registerEvents(new Bucket(), this);
    }

    //Register Permissions
    public void registerPermissions() {
        pm.addPermission(give);
    }

    //Register Uber Items
    public void registerUberItems() {
        //UberItems.items.put("NAME", new UberItemTemplate(0, itemPrefix + "NAME", Arrays.asList("LORE"), "DESCRIPTION", Material.AIR, false, false, false)); itemIDs.put(0, "NAME");
        UberItems.items.put("builders_wand", new builders_wand(1, itemPrefix + "Builder's Wand", Arrays.asList("Right Click to duplicate", "connected block faces"), "Build all the things!", Material.DIAMOND_SHOVEL, false, false, true)); itemIDs.put(1, "builders_wand");
        UberItems.items.put("lunch_box", new lunch_box(2, itemPrefix + "Lunch Box", Arrays.asList(ChatColor.GOLD + "Saturation: 0", "Feed yourself on the go!", "Shift-Right-Click to add food"), "Automatic Feeding!", Material.SKELETON_SKULL, false, false, false)); itemIDs.put(2, "lunch_box");
        UberItems.items.put("document_of_order", new document_of_order(3, itemPrefix + "Document of Order", Arrays.asList(ChatColor.GOLD + "Chests Selected: 0", "Sort Containers with Right-Click", "Shift-Right-Click for multiple, Left-Click to confirm"), "Automatic Chest Sorting", Material.PAPER, false, false, false)); itemIDs.put(3, "document_of_order");
        UberItems.items.put("big_bucket", new big_bucket(4, itemPrefix + "Big Bucket", Arrays.asList("Pick up and Place Infinite Liquids!", ChatColor.GOLD + "Mode: Collect", "Left click to cycle modes", "Collect Aura Consumes Eyes of Ender"), "Infinite liquid storage", Material.BUCKET, true, false, true)); itemIDs.put(4, "big_bucket");
        UberItems.items.put("escape_rope", new escape_rope(5, itemPrefix +"Escape Rope", Arrays.asList("Last saw sky at:", ChatColor.GOLD + "0, 0, 0", "Shift-Right-Click to teleport!"), "Teleport to the most recent spot exposed to the sky!", Material.LEAD, false, true, true)); itemIDs.put(5, "escape_rope");
        UberItems.items.put("fireball", new fireball(6, itemPrefix + "FireBall", Arrays.asList(ChatColor.GOLD + "Right Click to Throw!"), "Throw explosives!", Material.FIRE_CHARGE, false, true, false)); itemIDs.put(6, "fireball");
        UberItems.items.put("wrench", new wrench(7, itemPrefix + "Wrench", Arrays.asList("Rotate Blocks w/ Right Click!"), "Change orientation of blocks", Material.IRON_HOE, true, false, false)); itemIDs.put(7, "wrench");
        UberItems.items.put("infini_gulp", new infini_gulp(8, itemPrefix + "Infini-Gulp", Arrays.asList("Endless Milk Bucket"), "Infinite Milk Bucket which can be spiked with potions", Material.MILK_BUCKET, false, false, false)); itemIDs.put(8, "infini_gulp");
        UberItems.items.put("uncle_sams_wrath", new uncle_sams_wrath(9, itemPrefix + "Uncle Sam's Wrath", Arrays.asList(ChatColor.RED + "Show " + ChatColor.WHITE + "your " + ChatColor.AQUA + "patriotism!", ChatColor.GOLD + "Right-Click to shoot fireworks!"), "Shoot fireworks at your enemies!", Material.FIREWORK_ROCKET, false, false, false, this)); itemIDs.put(9, "uncle_sams_wrath");
        UberItems.items.put("electromagnet", new electromagnet(10, itemPrefix + "ElectroMagnet", Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Off"), "Suck in items and repel mobs!", Material.IRON_INGOT, false, false, true)); itemIDs.put(10, "electromagnet");
        UberItems.items.put("pocket_portal", new pocket_portal(11, itemPrefix + "Pocket Portal", Arrays.asList(ChatColor.GOLD + "Portable nether portal!", "Right Click to teleport!"), "Teleport to and from the nether instantly!", Material.COMPASS, false, false, false, this)); itemIDs.put(11, "pocket_portal");
        UberItems.items.put("shooty_box", new shooty_box(12, itemPrefix + "Shooty Box", Arrays.asList(ChatColor.GOLD + "A hand held dispenser!", "Right Click to Shoot", "Shift-Right-Click to open!", ""), "A hand held dispenser!", Material.DISPENSER, false, false, false)); itemIDs.put(12, "shooty_box");
        UberItems.items.put("chisel", new chisel(13, itemPrefix + "Chisel", Arrays.asList(ChatColor.GOLD + "Transmute Similar Blocks", "Punch to cycle block types", "Leave block type chisel to lock type", ChatColor.AQUA + "Stored: none"), "Transmute Similar Blocks", Material.SHEARS, true, false, false)); itemIDs.put(13, "chisel");
        UberItems.items.put("smart_pack", new smart_pack(14, itemPrefix + "Smart Pack", Arrays.asList("Smart Pack!", "Right-Click to open!"), "The smartest (and only) backpack in minecraft!", Material.LIME_SHULKER_BOX, false, false, true)); itemIDs.put(14, "smart_pack");
        UberItems.items.put("boom_stick", new boom_stick(15, itemPrefix + "BOOM Stick", Arrays.asList("You can't touch this"), "Make your enemies go BOOM", Material.STICK, false, false, false)); itemIDs.put(15, "boom_stick");
    }

    //Test if given item is an UberItem
    @SuppressWarnings("deprecation")
    public static boolean isUber(ItemStack item) {
        if (item == null) { return false; }

        ItemStack testItem = item.clone();
        testItem.setDurability((short)0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        if (meta == null) { return false; }
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for (String key : UberItems.items.keySet()) {
            ItemStack loopItem = UberItems.items.get(key).getItem();
            loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(loopItem)) return true;
        }

        return false;
    }

    //Test if given item is a specific Uber item
    @SuppressWarnings("deprecation")
    public static boolean isUber(ItemStack item, int id) {
        if (item == null) { return false; }

        ItemStack testItem = item.clone();
        testItem.setDurability((short)0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        if (meta == null) { return false; }
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for (String key : UberItems.items.keySet()) {
            ItemStack loopItem = UberItems.items.get(key).getItem();
            loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(loopItem) && UberItems.items.get(key).getID() == id) return true;
        }

        return false;
    }

    //Get the type of uber item (null if not uber)
    @SuppressWarnings("deprecation")
    public static UberItem getUber(ItemStack item) {
        ItemStack testItem = item.clone();
        testItem.setDurability((short)0);
        testItem.setAmount(1);

        ItemMeta meta = testItem.getItemMeta();
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        testItem.setItemMeta(meta);

        for ( String key : UberItems.items.keySet() ) {
            UberItem uberr = UberItems.items.get(key);
            loreItem(testItem, UberItems.items.get(key).getDefaultLore());
            if (testItem.equals(uberr.getItem())) return uberr;
        }

        return null;
    }

    //Return uber item with given name
    public static UberItem getUber(String name) {
        for ( String key : UberItems.items.keySet() ) {
            UberItem uberr = UberItems.items.get(key);
            if (uberr.getName().equals(name)) return uberr;
        }
        return null;
    }

    //Process active effets for uber items that are in use
    //TODO: Find more efficient way to do this?
    //TODO: Do active effects for uber items not in hand?
    public static void uberActiveEffects(){

        for (Player player : Bukkit.getOnlinePlayers()){
            if (isUber(player.getInventory().getItemInMainHand())){
                if (getUber(player.getInventory().getItemInMainHand()).hasActiveEffect()){
                    getUber(player.getInventory().getItemInMainHand()).activeEffect(player, player.getInventory().getItemInMainHand());
                }
            }
            if (isUber(player.getInventory().getItemInOffHand())){
                if (getUber(player.getInventory().getItemInOffHand()).hasActiveEffect()){
                    getUber(player.getInventory().getItemInOffHand()).activeEffect(player, player.getInventory().getItemInOffHand());
                }
            }
        }
    }

    //Renames Given Item
    public static ItemStack nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        //lots of options for changing item meta data
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    //Find uber item of given ID in inventory (null if nothing)
    public static ItemStack searchFor(Inventory inv, int id) {
        for (ItemStack item : inv){
            if (isUber(item, id)) return item;
        }

        return null;
    }

    //Changes Item Lore
    public static ItemStack loreItem(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        //lots of options for changing item meta data
        if (meta == null) return null;
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    //Creates item that is renamed given material and name
    public static ItemStack nameItem(Material item, String name) {
        return nameItem(new ItemStack(item), name);
    }

    //test if number is integer
    public static boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    //Convert Location to locString
    public static String toLocString(Location location) {
        if (location.equals(null)) return "";
        return location.getWorld().getName() + "," + (int)location.getX() + "," + (int)location.getY() + "," + (int)location.getZ();
    }
    //Convert locString to Location
    public static Location fromLocString(String locString) {
        if (locString.equals("")) return null;
        String[] data = locString.split(",");
        return new Location(Bukkit.getWorld(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
    }

    //Save list of items into the lore of a given item, starting at startLoreIndex
    public static void saveItemsInLore(ItemStack item, ItemStack[] items, int startLoreIndex) {
        String dataString = UberItems.itemsToString(items);

        ArrayList<String> loreChunks = new ArrayList<String>();

        for (int index = 0; index < startLoreIndex; index++) {
            loreChunks.add(item.getItemMeta().getLore().get(index));
        }

        while (dataString.length() > 0) {
            String chunk = "";

            if (dataString.length() >= 510) {
                chunk = dataString.substring(0, 510);
                dataString = dataString.substring(510);
            }
            else{
                chunk = dataString;
                dataString = "";
            }

            loreChunks.add(UberItems.convertToInvisibleString(chunk));

        }
        UberItems.loreItem(item, loreChunks);
    }

    //Load list of items from the lore of a given item, starting at startLoreIndex
    public static ItemStack[] getItemsFromLore(ItemStack item, int startLoreIndex) {

        String itemString = "";
        while (startLoreIndex < item.getItemMeta().getLore().size()) {
            itemString = itemString + item.getItemMeta().getLore().get(startLoreIndex);
            startLoreIndex++;
        }

        if (!itemString.equals("")){
            return UberItems.stringToItems(UberItems.convertToVisibleString(itemString));
        }
        else {
            return null;
        }
    }

    //Convert list of items into string
    public static String itemsToString(ItemStack[] items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStack(items));
            oos.flush();
            return DatatypeConverter.printBase64Binary(bos.toByteArray());
        }
        catch (Exception e) {
            //Logger.exception(e);
        }
        return "";
    }

    //Convert string to list of items
    @SuppressWarnings("unchecked")
    public static ItemStack[] stringToItems(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    DatatypeConverter.parseBase64Binary(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return deserializeItemStack(
                    (Map<String, Object>[]) ois.readObject());
        }
        catch (Exception e) {
            //Logger.exception(e);
        }
        return new ItemStack[] {
                new ItemStack(Material.AIR) };
    }

    //Serialize list of items
    @SuppressWarnings("unchecked")
    private static Map<String, Object>[] serializeItemStack(ItemStack[] items) {

        Map<String, Object>[] result = new Map[items.length];

        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is == null) {
                result[i] = new HashMap<>();
            }
            else {
                result[i] = is.serialize();
                if (is.hasItemMeta()) {
                    result[i].put("meta", is.getItemMeta().serialize());
                }
            }
        }

        return result;
    }

    //Deserialize list of items
    @SuppressWarnings("unchecked")
    private static ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];

        for (int i = 0; i < items.length; i++) {
            Map<String, Object> s = map[i];
            if (s.size() == 0) {
                items[i] = null;
            }
            else {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap<>(
                                (Map<String, Object>) s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta) ConfigurationSerialization
                                .deserializeObject(im));
                        items[i] = is;
                    }
                    else {
                        items[i] = ItemStack.deserialize(s);
                    }
                }
                catch (Exception e) {
                    //Logger.exception(e);
                    items[i] = null;
                }
            }

        }

        return items;
    }

    //Makes string invisible to player
    public static String convertToInvisibleString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        return hidden;
    }

    //Makes invisible string visible to player
    public static String convertToVisibleString(String s){
        String c = "";
        c = c + ChatColor.COLOR_CHAR;
        return s.replaceAll(c, "");
    }


}
