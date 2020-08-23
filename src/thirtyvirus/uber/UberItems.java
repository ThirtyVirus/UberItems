package thirtyvirus.uber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.uber.commands.uber;
import thirtyvirus.uber.events.block.BlockBreak;
import thirtyvirus.uber.events.chat.TabComplete;
import thirtyvirus.uber.events.inventory.InventoryClick;
import thirtyvirus.uber.events.inventory.InventoryClose;
import thirtyvirus.uber.events.inventory.RenameItem;
import thirtyvirus.uber.events.player.Bucket;
import thirtyvirus.uber.events.player.FoodLevelChange;
import thirtyvirus.uber.events.player.PlayerUse;
import thirtyvirus.uber.helpers.AbilityType;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;
import thirtyvirus.uber.items.*;

import java.io.File;
import java.util.*;

public class UberItems extends JavaPlugin {

    // data for all UberItems
    public static Map<String, UberItem> items = new HashMap<>();
    public static Map<Integer, String> itemIDs = new HashMap<>();

    // chat messages
    private Map<String, String> phrases = new HashMap<>();

    // plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String itemPrefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.AQUA + "UBER" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static String consolePrefix = "[UberItems] ";

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 5; //in ticks

    // actions to be taken on plugin enable
    public void onEnable(){
        loadConfiguration(); // load config.yml (generate one if not there)
        loadLangFile(); // load language.yml (generate one if not there)

        // register commands, events, and UberItems
        registerCommands();
        registerEvents();
        registerUberItems();

        // schedule repeating task for processing Uber Item active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() { Utilities.uberActiveEffects(UberItems.this); } }, activeEffectsDelay, activeEffectsDelay);

        // post confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");
    }

    // actions to be taken on plugin disable
    public void onDisable(){
        // cancel scheduled tasks
        Bukkit.getScheduler().cancelTask(activeEffectsCheckID);

        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    // load config.yml (generate one if not there)
    public void loadConfiguration() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Utilities.loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));

        // post confirmation in chat
        Bukkit.getLogger().info(consolePrefix + "Settings reloaded from config");
    }

    // load language.yml (generate one if not there)
    public void loadLangFile() {
        // console and IO
        File langFile = new File(getDataFolder(), "language.yml");
        FileConfiguration langFileConfig = new YamlConfiguration();
        if (!langFile.exists()){ Utilities.loadResource(this, "language.yml"); }

        try { langFileConfig.load(langFile); }
        catch (Exception e3) { e3.printStackTrace(); }

        for(String priceString : langFileConfig.getKeys(false)) {
            phrases.put(priceString, langFileConfig.getString(priceString));
        }
    }

    // register commands
    private void registerCommands() {
        getCommand("uber").setExecutor(new uber(this));
        getCommand("uber").setTabCompleter(new TabComplete(this)); // set up tab completion
    }

    // register event handlers
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this),this);
        getServer().getPluginManager().registerEvents(new PlayerUse(this), this);
        getServer().getPluginManager().registerEvents(new RenameItem(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
        getServer().getPluginManager().registerEvents(new Bucket(this), this);
    }

    // register UberItems
    public void registerUberItems() {
        //UberItems.items.put("NAME", new UberItemTemplate(this, 0, UberRarity.UNFINISHED, "NAME", "DESCRIPTION", Material.STONE, false, false, false, Arrays.asList(new UberAbility("Ability name!", AbilityType.RIGHT_CLICK, "Ability description")))); itemIDs.put(0, "NAME");
        items.put("builders_wand", new builders_wand(this, 1, UberRarity.UNFINISHED, "Builder's Wand", XMaterial.STICK.parseMaterial(), false, false, true, Arrays.asList(new UberAbility("Contruction!", AbilityType.RIGHT_CLICK, "Right click the face of any block to\nextend all connected block faces.\n" + ChatColor.DARK_GRAY + "(consumes blocks from your inventory)")))); itemIDs.put(1, "builders_wand");
        //items.put("lunch_box", new lunch_box(this,2, UberRarity.RARE, "Lunch Box", Arrays.asList(ChatColor.GOLD + "Saturation: 0", "Feed yourself on the go!", "Shift-Right-Click to add food"), "Automatic Feeding!", Material.SKELETON_SKULL, false, false, false)); itemIDs.put(2, "lunch_box");
        items.put("document_of_order", new document_of_order(this,3, UberRarity.UNFINISHED, "Document of Order", Material.PAPER, false, false, false, Arrays.asList(new UberAbility("Bureaucracy", AbilityType.RIGHT_CLICK, "Use on a container block to sort its contents"), new UberAbility("Multisort", AbilityType.LEFT_CLICK, "Select many containers at once, then\nleft click any non-container block to confirm,\ncr crouch left click any block to cancel.\n"+ ChatColor.DARK_GRAY + "(sorts everything as if one large inventory)")))); itemIDs.put(3, "document_of_order");
        //items.put("big_bucket", new big_bucket(this,4, UberRarity.EPIC,  "Big Bucket", Arrays.asList("Pick up and Place Infinite Liquids!", ChatColor.GOLD + "Mode: Collect", "Left click to cycle modes", "Collect Aura Consumes Eyes of Ender"), "Infinite liquid storage", Material.BUCKET, true, false, true)); itemIDs.put(4, "big_bucket");
        items.put("escape_rope", new escape_rope(this,5, UberRarity.UNCOMMON,  "Escape Rope", XMaterial.LEAD.parseMaterial(), false, true, true, Arrays.asList(new UberAbility("Round Trip!", AbilityType.RIGHT_CLICK, "Instantly teleport back to the last\nlocation with the sky visible\n" + ChatColor.DARK_GRAY + "(one time use)")))); itemIDs.put(5, "escape_rope");
        items.put("fireball", new fireball(this,6, UberRarity.EPIC,  "FireBall", XMaterial.FIRE_CHARGE.parseMaterial(), false, true, false, Arrays.asList(new UberAbility("Throw em!", AbilityType.RIGHT_CLICK, "Throw a fireball which\nexplodes on impact\n" + ChatColor.DARK_GRAY + "(one time use)")))); itemIDs.put(6, "fireball");
        items.put("wrench", new wrench(this,7, UberRarity.COMMON,   "Wrench", Material.IRON_HOE, true, false, false, Arrays.asList(new UberAbility("Tinker", AbilityType.RIGHT_CLICK, "Change the direction of certain blocks"))));; itemIDs.put(7, "wrench");
        //items.put("infini_gulp", new infini_gulp(this,8, UberRarity.RARE,  "Infini-Gulp", Arrays.asList("Endless Milk Bucket"), "Infinite Milk Bucket which can be spiked with potions", Material.MILK_BUCKET, false, false, false)); itemIDs.put(8, "infini_gulp");
        items.put("uncle_sams_wrath", new uncle_sams_wrath(this,9, UberRarity.RARE, ChatColor.RED + "Uncle " + ChatColor.WHITE + "Sam's " + ChatColor.AQUA + "Wrath", Material.FIREWORK_ROCKET, false, false, false, Arrays.asList(new UberAbility("July 4th", AbilityType.RIGHT_CLICK, "Shoot lethal fireworks at\nyour enemies. MURCA")))); itemIDs.put(9, "uncle_sams_wrath");
        items.put("electromagnet", new electromagnet(this,10, UberRarity.UNCOMMON,   "ElectroMagnet", Material.IRON_INGOT, false, false, true, Arrays.asList(new UberAbility("Magnetic Pull", AbilityType.NONE, "Attract dropped items from\na radius of " + ChatColor.GREEN + "32" + ChatColor.GRAY + " blocks away."), new UberAbility("Force Field", AbilityType.NONE, "When held in the hand,\nrepel hostile mobs\nand projectiles\n\n" + ChatColor.DARK_GRAY + "(toggle with crouch + right click)")))); itemIDs.put(10, "electromagnet");
        items.put("pocket_portal", new pocket_portal(this,11, UberRarity.RARE,  "Pocket Portal", Material.COMPASS, false, false, false, Arrays.asList(new UberAbility("Beam me up Scotty!", AbilityType.RIGHT_CLICK, "Teleport to and from the nether")))); itemIDs.put(11, "pocket_portal");
        items.put("shooty_box", new shooty_box(this,12, UberRarity.MYTHIC,   "Shooty Box", Material.DISPENSER, false, false, false, Arrays.asList(new UberAbility("Blunderbuss!", AbilityType.RIGHT_CLICK, "Shoot the contents of the box at\nhigh speed, like a handheld dispenser\n" + ChatColor.DARK_GRAY + "(open with crouch + right click")))); itemIDs.put(12, "shooty_box");
        //items.put("chisel", new chisel(this,13, UberRarity.COMMON,   "Chisel", Arrays.asList(ChatColor.GOLD + "Transmute Similar Blocks", "Punch to cycle block types", "Leave block type chisel to lock type", ChatColor.AQUA + "Stored: none"), "Transmute Similar Blocks", Material.SHEARS, true, false, false)); itemIDs.put(13, "chisel");
        //items.put("smart_pack", new smart_pack(this,14, UberRarity.EPIC,  "Smart Pack", Arrays.asList("Smart Pack!", "Right-Click to open!"), "The smartest (and only) backpack in minecraft!", Material.LIME_SHULKER_BOX, false, false, true)); itemIDs.put(14, "smart_pack");
        //items.put("boom_stick", new boom_stick(this,15, UberRarity.SPECIAL,   "BOOM Stick", Arrays.asList("You can't touch this"), "Make your enemies go BOOM", Material.STICK, false, false, false)); itemIDs.put(15, "boom_stick");
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }

}