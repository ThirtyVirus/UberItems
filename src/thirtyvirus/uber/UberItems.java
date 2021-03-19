package thirtyvirus.uber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.uber.commands.uber;
import thirtyvirus.uber.events.block.BlockPlace;
import thirtyvirus.uber.events.chat.TabComplete;
import thirtyvirus.uber.events.inventory.InventoryClick;
import thirtyvirus.uber.events.inventory.InventoryClose;
import thirtyvirus.uber.events.inventory.RenameItem;
import thirtyvirus.uber.events.player.Bucket;
import thirtyvirus.uber.events.player.FoodLevelChange;
import thirtyvirus.uber.events.player.PlayerInteract;
import thirtyvirus.uber.events.player.PlayerUseUberItem;
import thirtyvirus.uber.helpers.*;

import java.io.File;
import java.util.*;

public class UberItems extends JavaPlugin {

    // data for all UberItems
    public static Map<String, UberItem> items = new HashMap<>();
    public static Map<Integer, String> itemIDs = new HashMap<>();

    // data for UberMaterials
    public static Map<String, UberMaterial> materials = new HashMap<>();
    public static Map<Integer, UberMaterial> materialIDs = new HashMap<>();

    // chat messages
    private Map<String, String> phrases = new HashMap<>();

    // global plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l"; // generally unchanged unless otherwise stated in config
    public static String itemPrefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.AQUA + "UBER" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static String consolePrefix = "[UberItems] ";

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 2; //in ticks

    // getter for main class
    private static UberItems instance;

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
        // set static instance of the main class, for use in other parts of the plugin & the API
        instance = this;

        loadConfiguration(); // load config.yml (generate one if not there)
        loadLangFile(); // load language.yml (generate one if not there)

        // register commands, events, UberMaterials, and UberItems
        registerCommands();
        registerEvents();
        RegisterItems.registerUberMaterials();
        RegisterItems.registerUberItems();

        // schedule repeating task for processing Uber Item active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() { Utilities.uberActiveEffects(); } }, activeEffectsDelay, activeEffectsDelay);

        // schedule checking of recent added containers
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() { SortingUtilities.checkCancelMultisort(multisorts, multiSortTimeout); }
        }, 20 * multiSortTimeout, 20 * multiSortTimeout);

        // schedule checking if a player is in the crafting menu (to update the recipe)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getOpenInventory().getTitle().contains("UberItems - Craft Item")) {
                        MenuUtils.checkCraft(player.getOpenInventory().getTopInventory());
                    }
                }
            }
        }, 10, 10);

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
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this),this);
        getServer().getPluginManager().registerEvents(new PlayerUseUberItem(this), this);
        getServer().getPluginManager().registerEvents(new RenameItem(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClose(this), this);
        getServer().getPluginManager().registerEvents(new Bucket(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
    }

    // put the item into the 2 hashmaps
    public static void putItem(String name, UberItem item) {
        items.put(name, item);
        itemIDs.put(item.getID(), name);
    }

    // put the material into the 2 hashmaps
    public static void putMaterial(String name, UberMaterial material) {
        materials.put(name, material);
        materialIDs.put(material.getUUID(), material);
    }

    // getters
    public String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }
    public static UberItems getInstance() { return instance; }
}