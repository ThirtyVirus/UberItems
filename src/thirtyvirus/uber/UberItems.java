package thirtyvirus.uber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
import thirtyvirus.uber.events.player.PlayerInteract;
import thirtyvirus.uber.events.player.PlayerUseUberItem;
import thirtyvirus.uber.helpers.*;
import thirtyvirus.uber.items.*;

import java.io.File;
import java.util.*;

public class UberItems extends JavaPlugin {

    // data for all UberItems
    public static Map<String, UberItem> items = new HashMap<>();
    public static Map<Integer, String> itemIDs = new HashMap<>();

    // chat messages
    private Map<String, String> phrases = new HashMap<>();

    // global plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String itemPrefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.AQUA + "UBER" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static String consolePrefix = "[UberItems] ";

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 5; //in ticks

    // sorting settings
    public static int sortingMode = 1;
    public static boolean reverseSort = false;
    public static boolean externalSort = true;
    public static boolean multiSort = true;
    private static int multiSortTimeout = 60;
    public static boolean automaticSort = false;
    public static boolean ignoreBuildPerms = false;

    public static boolean useWhiteList = true;
    public static boolean useBlackList = false;

    public static List<String> whiteList = new ArrayList<>();
    public static List<String> blackList = new ArrayList<>();

    public static Map<Player, List<Block>> multisorts = new HashMap<>();

    // other variables
    public static boolean premium = true;

    // actions to be taken on plugin enable
    public void onEnable() {
        loadConfiguration(); // load config.yml (generate one if not there)
        loadLangFile(); // load language.yml (generate one if not there)

        // register commands, events, and UberItems
        registerCommands();
        registerEvents();
        registerUberItems();

        // schedule repeating task for processing Uber Item active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() { Utilities.uberActiveEffects(UberItems.this); } }, activeEffectsDelay, activeEffectsDelay);

        // schedule checking of recent added containers
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                SortingUtilities.checkCancelMultisort(multisorts, multiSortTimeout);
            }
        }, 20 * multiSortTimeout, 20 * multiSortTimeout);

        // post confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");
    }

    // actions to be taken on plugin disable
    public void onDisable() {
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

        // sorting settings
        sortingMode = config.getInt("sorting-mode");
        reverseSort = config.getBoolean("reverse-sort");
        externalSort = config.getBoolean("external-sort");
        multiSort = config.getBoolean("multi-sort");
        multiSortTimeout = config.getInt("multi-sort-timeout");
        automaticSort = config.getBoolean("automatic-sort");
        ignoreBuildPerms = config.getBoolean("ignore-area-build-permissions");

        useWhiteList = config.getBoolean("use-whitelist");
        useBlackList = config.getBoolean("use-blacklist");

        whiteList.clear();
        whiteList.addAll(Arrays.asList(config.getString("whitelist").split(",")));

        blackList.clear();
        blackList.addAll(Arrays.asList(config.getString("blacklist").split(",")));

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
        getServer().getPluginManager().registerEvents(new PlayerUseUberItem(this), this);
        getServer().getPluginManager().registerEvents(new RenameItem(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
        getServer().getPluginManager().registerEvents(new Bucket(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
    }

    // register UberItems
    public void registerUberItems() {
        //putItem("NAME", new UberItemTemplate(this, 0, UberRarity.UNFINISHED, "NAME", Material.STONE, false, false, false, false, Arrays.asList(new UberAbility("Ability name!", AbilityType.RIGHT_CLICK, "Ability description"))));
        putItem("builders_wand", new builders_wand(this, 1, UberRarity.LEGENDARY, "Builder's Wand", XMaterial.STICK.parseMaterial(), false, false, false, true, Arrays.asList(new UberAbility("Contruction!", AbilityType.RIGHT_CLICK, "Right click the face of any block to\nextend all connected block faces.\n" + ChatColor.DARK_GRAY + "(consumes blocks from your inventory)"))));
        putItem("lunch_box", new lunch_box(this,2, UberRarity.RARE, "Lunch Box", Material.CONDUIT, false, false, false, false, Arrays.asList(new UberAbility("Gluttony", AbilityType.NONE, "Automatically feeds you when hungry\n" + ChatColor.DARK_GRAY + "(drag and click food onto the box to fill)"))));
        putItem("document_of_order", new document_of_order(this,3, UberRarity.EPIC, "Document of Order", Material.PAPER, false, false, false, false, Arrays.asList(new UberAbility("Bureaucracy", AbilityType.RIGHT_CLICK, "Use on a container block while crouched\nto sort that container's contents.\nCrouch use on air to sort own inventory."), new UberAbility("Multisort", AbilityType.LEFT_CLICK, "Select many containers at once, then\nleft click any non-container block to confirm,\ncr crouch left click any block to cancel.\n"+ ChatColor.DARK_GRAY + "(sorts everything as if one large inventory)"))));
        putItem("escape_rope", new escape_rope(this,5, UberRarity.UNCOMMON,  "Escape Rope", XMaterial.LEAD.parseMaterial(), false, true, true, true, Arrays.asList(new UberAbility("Round Trip!", AbilityType.RIGHT_CLICK, "Instantly teleport back to the last\nlocation with the sky visible"))));
        putItem("fireball", new fireball(this,6, UberRarity.UNCOMMON,  "FireBall", XMaterial.FIRE_CHARGE.parseMaterial(), false, true, true, false, Arrays.asList(new UberAbility("Throw em!", AbilityType.RIGHT_CLICK, "Throw a fireball which\nexplodes on impact"))));
        putItem("wrench", new wrench(this,7, UberRarity.COMMON,   "Wrench", Material.IRON_HOE, true, false, false, false, Arrays.asList(new UberAbility("Tinker", AbilityType.RIGHT_CLICK, "Change the direction of certain blocks"))));
        putItem("malk_bucket", new malk_bucket(this,8, UberRarity.RARE,  "Malk Bucket", Material.MILK_BUCKET, false, false, false, false, Arrays.asList(new UberAbility("Void Udder", AbilityType.NONE, "It's an infinite milk bucket!"), new UberAbility("Spiked Milk", AbilityType.NONE, "Place a potion onto this\nitem to spike the milk!\nSpiked milk is still infinite,\nbut also gives the potion effect"))));
        putItem("uncle_sams_wrath", new uncle_sams_wrath(this,9, UberRarity.RARE, ChatColor.RED + "Uncle " + ChatColor.WHITE + "Sam's " + ChatColor.AQUA + "Wrath", Material.FIREWORK_ROCKET, false, false, false, false, Arrays.asList(new UberAbility("July 4th", AbilityType.RIGHT_CLICK, "Shoot lethal fireworks at\nyour enemies. MURCA"))));
        putItem("electromagnet", new electromagnet(this,10, UberRarity.UNCOMMON,   "ElectroMagnet", Material.IRON_INGOT, false, false, false, true, Arrays.asList(new UberAbility("Magnetic Pull", AbilityType.NONE, "Attract dropped items from\na radius of " + ChatColor.GREEN + "32" + ChatColor.GRAY + " blocks away."), new UberAbility("Force Field", AbilityType.NONE, "When held in the hand,\nrepel hostile mobs\nand projectiles\n\n" + ChatColor.DARK_GRAY + "(toggle with crouch + right click)"))));
        putItem("pocket_portal", new pocket_portal(this,11, UberRarity.RARE,  "Pocket Portal", Material.COMPASS, false, false, false, false, Arrays.asList(new UberAbility("Beam me up Scotty!", AbilityType.RIGHT_CLICK, "Teleport to and from the nether"))));
        putItem("shooty_box", new shooty_box(this,12, UberRarity.MYTHIC,   "Shooty Box", Material.DISPENSER, false, false, false, false, Arrays.asList(new UberAbility("Blunderbuss!", AbilityType.RIGHT_CLICK, "Shoot the contents of the box at\nhigh speed, like a handheld dispenser\n" + ChatColor.DARK_GRAY + "(open with crouch + right click"))));
        putItem("chisel", new chisel(this,13, UberRarity.UNFINISHED,   "Chisel", Material.SHEARS, true, false, false, false, Arrays.asList(new UberAbility("Transmutation", AbilityType.RIGHT_CLICK, "Use on a block to transmute\nit into a similar one"), new UberAbility("Machine Chisel", AbilityType.LEFT_CLICK, "Transmute many blocks at a time\nin your inventory"))));
        putItem("smart_pack", new smart_pack(this,14, UberRarity.UNFINISHED,  "Smart Pack", Material.LIME_SHULKER_BOX, false, false, false, true, Arrays.asList(new UberAbility("Black Box", AbilityType.NONE, "Any items with special properties\ninside will retain their active effects"), new UberAbility("Automation", AbilityType.RIGHT_CLICK, "Activating the pack will activate items\ninside with a Right-Click ability"))));
        putItem("boom_stick", new boom_stick(this,15, UberRarity.LEGENDARY, "BOOM Stick", Material.STICK, false, false, false, false, Arrays.asList(new UberAbility("BOOM", AbilityType.RIGHT_CLICK, "Blow up all nearby enemies"), new UberAbility("Banish", AbilityType.LEFT_CLICK, "Punch your enemies\ninto the shadow realm"))));
        putItem("aspect_of_the_end", new aspect_of_the_end(this, 16, UberRarity.RARE, "Aspect Of The End", Material.DIAMOND_SWORD, false, false, false, false, Arrays.asList(new UberAbility("Instant Transmission", AbilityType.RIGHT_CLICK, "Teleport " + ChatColor.GREEN + "8 blocks" + ChatColor.GRAY + " ahead of\nyou and gain " + ChatColor.GREEN + "+50 " + ChatColor.WHITE + "✦ Speed" + ChatColor.GRAY + "\nfor " + ChatColor.GREEN + "3 seconds"))));
        putItem("plumbers_sponge", new plumbers_sponge(this, 17, UberRarity.UNCOMMON, "Plumber's Sponge", Material.SPONGE, false, true, true, true, Arrays.asList(new UberAbility("Drain", AbilityType.RIGHT_CLICK, "Instructions:\n1. Place on water.\n2. Drains other water.\n3. Double-bill client.\n\n" + ChatColor.DARK_GRAY + "Thanks Plumber Joe!"))));
        putItem("grappling_hook", new grappling_hook(this, 18, UberRarity.UNCOMMON, "Grappling Hook", Material.FISHING_ROD, true, false, false, false, Arrays.asList()));
        putItem("ember_rod", new ember_rod(this, 19, UberRarity.EPIC, "Ember Rod", Material.BLAZE_ROD, false, false, false, false, Arrays.asList(new UberAbility("Fire Blast!", AbilityType.RIGHT_CLICK, "Shoot 3 FireBalls in rapid\nsuccession in front of you!"))));
    }

    // put the item into the 2 hashmaps
    public void putItem(String name, UberItem item) {
        items.put(name, item);
        itemIDs.put(item.getID(), name);
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }

}