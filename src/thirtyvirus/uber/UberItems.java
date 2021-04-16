package thirtyvirus.uber;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.uber.commands.UberCommand;
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
import thirtyvirus.uber.items.uber_workbench;

import java.io.File;
import java.util.*;

public class UberItems extends JavaPlugin {

    // data for UberItems
    public static Map<String, UberItem> items = new HashMap<>();
    public static Map<Integer, String> itemIDs = new HashMap<>();

    // data for UberMaterials
    private static Map<String, UberMaterial> materials = new HashMap<>();
    private static Map<Integer, UberMaterial> materialIDs = new HashMap<>();

    // chat messages
    private static Map<String, String> phrases = new HashMap<>();

    // global plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l";
    public static String consolePrefix = "[UberItems] ";

    public static boolean defaultUberItems = true;
    public static boolean defaultUberMaterials = true;

    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 2; // in ticks

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
    public static final boolean premium = true;
    private static boolean haveCountedDefaultItems = false;
    private static int defaultItemCount = 0;

    // actions to be taken on plugin enable
    public void onEnable() {
        // set static instance of the main class, for use in other parts of the plugin & the API
        instance = this;

        // load config.yml and language.yml (generate them if not there)
        loadConfiguration();
        loadLangFile();

        // register commands, events
        registerCommands();
        registerEvents();

        // register the Uber Workbench separately from the rest of the items, it's essential
        putItem("uber_workbench", new uber_workbench(0, UberRarity.UNCOMMON, "Uber WorkBench",
                Material.CRAFTING_TABLE, false, false, false,
                Collections.singletonList(new UberAbility("A new chapter", AbilityType.RIGHT_CLICK, "Opens the UberItems Crafting Menu")), null));

        // register the error UberMaterial separately from the rest of the items, it's essential
        UberItems.putMaterial("null", new UberMaterial(Material.BARRIER,
                "null", UberRarity.UNFINISHED, false, false, false, "ERROR: UberMaterial not found", null));

        // register UberMaterials, UberItems. Then count the number of default items
        if (defaultUberMaterials) RegisterItems.registerUberMaterials();
        if (defaultUberItems) {
            RegisterItems.registerUberItems();
            defaultItemCount = items.keySet().size();
        }
        haveCountedDefaultItems = true;

        // schedule repeating task for processing UberItem active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, Utilities::uberActiveEffects, activeEffectsDelay, activeEffectsDelay);

        // schedule checking of recent added containers (Document of Order)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> SortingUtilities.checkCancelMultisort(multisorts, multiSortTimeout), 20 * multiSortTimeout, 20 * multiSortTimeout);

        // schedule checking if a player is in the crafting menu (to update the recipe)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTitle().contains("UberItems - Craft Item")) {
                    MenuUtils.checkCraft(player.getOpenInventory().getTopInventory());
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
    private void loadConfiguration() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()){
            Utilities.loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-prefix"));
        defaultUberItems = config.getBoolean("default-uber-items");
        defaultUberMaterials = config.getBoolean("default-uber-materials");

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
    private void loadLangFile() {
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
        getCommand("uber").setExecutor(new UberCommand());
        getCommand("uber").setTabCompleter(new TabComplete()); // set up tab completion
    }

    // register event handlers
    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerUseUberItem(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        getServer().getPluginManager().registerEvents(new InventoryClose(), this);
        getServer().getPluginManager().registerEvents(new RenameItem(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);

        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new Bucket(), this);
    }

    // put an item into the 2 hashmaps
    public static void putItem(String name, UberItem item) {
        if (items.keySet().size() < defaultItemCount + 5 || !haveCountedDefaultItems || premium) {
            items.put(name, item);
            itemIDs.put(item.getID(), name);
        }
        else Bukkit.getLogger().severe("You're trying to load more than 5 custom items! Purchase UberItems Premium to load unlimited custom items: https://www.spigotmc.org/resources/83851/");
    }
    // put a material into the 2 hashmaps
    public static void putMaterial(String name, UberMaterial material) {
        materials.put(name, material);
        materialIDs.put(material.getUUID(), material);
    }
    // reload all plugin assets
    public static void reload() {
        getInstance().reloadConfig();
        getInstance().loadConfiguration();
        getInstance().loadLangFile();
        Bukkit.getLogger().info("configuration, values, and language settings reloaded");
    }

    // getters
    public static UberMaterial getMaterial(String key) {
        UberMaterial material = materials.get(key);
        if (material == null) return materials.get("null");
        else return material;
    }
    public static UberMaterial getMaterialFromID(int id) {
        UberMaterial material = materialIDs.get(id);
        if (material == null) return materials.get("null");
        else return material;
    }
    public static Collection<UberMaterial> getMaterials() { return materials.values(); }
    public static Collection<String> getMaterialNames() { return materials.keySet(); }

    public static String getPhrase(String key) {
        return phrases.get(key);
    }
    public String getVersion() {
        return getDescription().getVersion();
    }
    public static UberItems getInstance() { return instance; }
}