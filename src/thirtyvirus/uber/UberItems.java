package thirtyvirus.uber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.uber.commands.uber_command;
import thirtyvirus.uber.events.block.BlockBreak;
import thirtyvirus.uber.events.chat.TabComplete;
import thirtyvirus.uber.events.inventory.InventoryClick;
import thirtyvirus.uber.events.inventory.InventoryClose;
import thirtyvirus.uber.events.inventory.RenameItem;
import thirtyvirus.uber.events.player.Bucket;
import thirtyvirus.uber.events.player.FoodLevelChange;
import thirtyvirus.uber.events.player.PlayerUse;
import thirtyvirus.uber.helpers.Utilities;
import thirtyvirus.uber.items.*;

import java.io.File;
import java.util.*;

public class UberItems extends JavaPlugin {

    // console and IO
    private File langFile;
    private FileConfiguration langFileConfig;

    // chat messages
    private Map<String, String> phrases = new HashMap<String, String>();

    // core settings
    public static String prefix = "&c&l[&5&lTemplatePlugin&c&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String itemPrefix = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[UBER] " + ChatColor.GRAY;
    public static String consolePrefix = "[TemplatePlugin] ";

    // data for all Uber Items
    public static Map<String, UberItem> items = new HashMap<String, UberItem>();
    public static Map<Integer, String> itemIDs = new HashMap<Integer, String>();

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 5; //in ticks

    // customizable settings
    public static boolean customSetting = false;

    public void onEnable(){
        // load config.yml (generate one if not there)
        loadConfiguration();

        // load language.yml (generate one if not there)
        loadLangFile();

        // register commands and events
        registerCommands();
        registerEvents();

        // register all uber items?
        registerUberItems();

        // posts confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");

        // schedule repeating task for processing Uber Item active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() { Utilities.uberActiveEffects(); } }, activeEffectsDelay, activeEffectsDelay);
    }

    public void onDisable(){
        // cancel scheduled tasks
        Bukkit.getScheduler().cancelTask(activeEffectsCheckID);

        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    private void registerCommands() {
        getCommand("uber").setExecutor(new uber_command(this));

        // set up tab completion
        getCommand("uber").setTabCompleter(new TabComplete(this));
    }
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        getServer().getPluginManager().registerEvents(new PlayerUse(), this);
        getServer().getPluginManager().registerEvents(new RenameItem(), this);
        getServer().getPluginManager().registerEvents(new InventoryClose(), this);
        getServer().getPluginManager().registerEvents(new Bucket(), this);
    }

    // load the config file and apply settings
    public void loadConfiguration() {
        // prepare config.yml (generate one if not there)
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Utilities.loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));

        customSetting = config.getBoolean("custom-setting");
        // put more settings here

        Bukkit.getLogger().info(consolePrefix + "Settings Reloaded from config");
    }

    // load the language file and apply settings
    public void loadLangFile() {

        // load language.yml (generate one if not there)
        langFile = new File(getDataFolder(), "language.yml");
        langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "language.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String priceString : langFileConfig.getKeys(false)) {
            phrases.put(priceString, langFileConfig.getString(priceString));
        }
    }

    // register Uber Items
    public void registerUberItems() {
        //UberItems.items.put("NAME", new UberItemTemplate(0, itemPrefix + "NAME", Arrays.asList("LORE"), "DESCRIPTION", Material.AIR, false, false, false)); itemIDs.put(0, "NAME");
        items.put("builders_wand", new builders_wand(1, itemPrefix + "Builder's Wand", Arrays.asList("Right Click to duplicate", "connected block faces"), "Build all the things!", XMaterial.DIAMOND_SHOVEL.parseMaterial(), false, false, true)); itemIDs.put(1, "builders_wand");
        items.put("lunch_box", new lunch_box(2, itemPrefix + "Lunch Box", Arrays.asList(ChatColor.GOLD + "Saturation: 0", "Feed yourself on the go!", "Shift-Right-Click to add food"), "Automatic Feeding!", Material.SKELETON_SKULL, false, false, false)); itemIDs.put(2, "lunch_box");
        items.put("document_of_order", new document_of_order(3, itemPrefix + "Document of Order", Arrays.asList(ChatColor.GOLD + "Chests Selected: 0", "Sort Containers with Right-Click", "Shift-Right-Click for multiple, Left-Click to confirm"), "Automatic Chest Sorting", Material.PAPER, false, false, false)); itemIDs.put(3, "document_of_order");
        items.put("big_bucket", new big_bucket(4, itemPrefix + "Big Bucket", Arrays.asList("Pick up and Place Infinite Liquids!", ChatColor.GOLD + "Mode: Collect", "Left click to cycle modes", "Collect Aura Consumes Eyes of Ender"), "Infinite liquid storage", Material.BUCKET, true, false, true)); itemIDs.put(4, "big_bucket");
        items.put("escape_rope", new escape_rope(5, itemPrefix +"Escape Rope", Arrays.asList("Last saw sky at:", ChatColor.GOLD + "0, 0, 0", "Shift-Right-Click to teleport!"), "Teleport to the most recent spot exposed to the sky!", XMaterial.LEAD.parseMaterial(), false, true, true)); itemIDs.put(5, "escape_rope");
        items.put("fireball", new fireball(6, itemPrefix + "FireBall", Arrays.asList(ChatColor.GOLD + "Right Click to Throw!"), "Throw explosives!", XMaterial.FIRE_CHARGE.parseMaterial(), false, true, false)); itemIDs.put(6, "fireball");
        items.put("wrench", new wrench(7, itemPrefix + "Wrench", Arrays.asList("Rotate Blocks w/ Right Click!"), "Change orientation of blocks", Material.IRON_HOE, true, false, false)); itemIDs.put(7, "wrench");
        items.put("infini_gulp", new infini_gulp(8, itemPrefix + "Infini-Gulp", Arrays.asList("Endless Milk Bucket"), "Infinite Milk Bucket which can be spiked with potions", Material.MILK_BUCKET, false, false, false)); itemIDs.put(8, "infini_gulp");
        items.put("uncle_sams_wrath", new uncle_sams_wrath(9, itemPrefix + "Uncle Sam's Wrath", Arrays.asList(ChatColor.RED + "Show " + ChatColor.WHITE + "your " + ChatColor.AQUA + "patriotism!", ChatColor.GOLD + "Right-Click to shoot fireworks!"), "Shoot fireworks at your enemies!", Material.FIREWORK_ROCKET, false, false, false, this)); itemIDs.put(9, "uncle_sams_wrath");
        items.put("electromagnet", new electromagnet(10, itemPrefix + "ElectroMagnet", Arrays.asList("Shift-Right-Click to change mode", ChatColor.GOLD + "Mode: Off"), "Suck in items and repel mobs!", Material.IRON_INGOT, false, false, true)); itemIDs.put(10, "electromagnet");
        items.put("pocket_portal", new pocket_portal(11, itemPrefix + "Pocket Portal", Arrays.asList(ChatColor.GOLD + "Portable nether portal!", "Right Click to teleport!"), "Teleport to and from the nether instantly!", Material.COMPASS, false, false, false, this)); itemIDs.put(11, "pocket_portal");
        items.put("shooty_box", new shooty_box(12, itemPrefix + "Shooty Box", Arrays.asList(ChatColor.GOLD + "A hand held dispenser!", "Right Click to Shoot", "Shift-Right-Click to open!", ""), "A hand held dispenser!", Material.DISPENSER, false, false, false)); itemIDs.put(12, "shooty_box");
        items.put("chisel", new chisel(13, itemPrefix + "Chisel", Arrays.asList(ChatColor.GOLD + "Transmute Similar Blocks", "Punch to cycle block types", "Leave block type chisel to lock type", ChatColor.AQUA + "Stored: none"), "Transmute Similar Blocks", Material.SHEARS, true, false, false)); itemIDs.put(13, "chisel");
        items.put("smart_pack", new smart_pack(14, itemPrefix + "Smart Pack", Arrays.asList("Smart Pack!", "Right-Click to open!"), "The smartest (and only) backpack in minecraft!", Material.LIME_SHULKER_BOX, false, false, true)); itemIDs.put(14, "smart_pack");
        items.put("boom_stick", new boom_stick(15, itemPrefix + "BOOM Stick", Arrays.asList("You can't touch this"), "Make your enemies go BOOM", Material.STICK, false, false, false)); itemIDs.put(15, "boom_stick");
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }

}