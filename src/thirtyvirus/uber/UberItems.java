package thirtyvirus.uber;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.uber.commands.UberCommand;
import thirtyvirus.uber.events.MiscEvents;
import thirtyvirus.uber.events.BlockEvents;
import thirtyvirus.uber.events.chat.TabComplete;
import thirtyvirus.uber.events.inventory.InventoryClick;
import thirtyvirus.uber.events.inventory.InventoryClose;
import thirtyvirus.uber.events.inventory.RenameItem;
import thirtyvirus.uber.events.player.Bucket;
import thirtyvirus.uber.events.player.FoodLevelChange;
import thirtyvirus.uber.events.player.PlayerUseUberItem;
import thirtyvirus.uber.helpers.*;
import thirtyvirus.uber.items.null_item;
import thirtyvirus.uber.items.uber_workbench;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class UberItems extends JavaPlugin {

    // data for UberItems, UberMaterials
    private static Map<String, UberItem> items = new HashMap<>();
    private static Map<Integer, UberItem> itemIDs = new HashMap<>();
    private static Map<String, UberMaterial> materials = new HashMap<>();
    private static Map<Integer, UberMaterial> materialIDs = new HashMap<>();

    public enum ArmorType {LEATHER, CHAINMAIL, GOLD, IRON, DIAMOND}
    public static final List<String> default_items = Arrays.asList("lunch_box", "document_of_order", "cheat_code", "escape_rope",
            "fireball", "wrench", "malk_bucket", "uncle_sams_wrath", "electromagnet", "pocket_portal", "shooty_box", "chisel",
            "smart_pack", "boom_stick", "world_eater", "lightning_rod", "aspect_of_the_virus", "hackerman");

    public static final List<String> default_materials = Arrays.asList("creative_core", "enchanted_cobblestone", "enchanted_diaond",
            "enchanted_stone", "enchanted_ender_pearl", "enchanted_string", "spark_dust", "flammable_substance",
            "paper_fletching", "fools_gold");

    // chat messages
    private static Map<String, String> phrases = new HashMap<>();

    // global plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l";
    public static String consolePrefix = "[UberItems] ";
    public static boolean defaultUberItems = true;
    public static boolean defaultUberMaterials = true;
    public static int activeEffectsCheckID = 0;
    public static int activeEffectsDelay = 2; // in ticks

    public static List<String> itemBlacklist = new ArrayList<>();
    public static List<String> itemWhitelist = new ArrayList<>();
    public static List<String> materialBlacklist = new ArrayList<>();
    public static List<String> materialWhitelist = new ArrayList<>();

    // sorting settings
    public static int sortingMode = 1;
    public static boolean reverseSort = false;
    public static boolean externalSort = true;
    public static boolean multiSort = true;
    public static int multiSortTimeout = 60;
    public static boolean automaticSort = false;
    public static boolean ignoreBuildPerms = false;
    public static boolean useWhiteList = true;
    public static boolean useBlackList = false;

    // sorting essentials
    public static List<String> whiteList = new ArrayList<>();
    public static List<String> blackList = new ArrayList<>();
    public static Map<Player, List<Block>> multisorts = new HashMap<>();

    // other variables
    public static final boolean premium = true;
    private static boolean haveCountedDefaultItems = false;
    private static int defaultItemCount = 0;
    private static UberItems instance;

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
        registerItemsAndMaterials();

        // schedule repeating task for processing UberItem active effects
        activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, UberItems::uberActiveEffects, activeEffectsDelay, activeEffectsDelay);

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

        // manage player mana (regeneration and updating display)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Utilities.dontUpdateMana.containsKey(player)) continue;
                // only show mana if holding item that uses it
                boolean usesMana = false;
                ItemStack item = player.getInventory().getItemInMainHand();
                if (Utilities.isUber(item)) {
                    UberItem uber = Utilities.getUber(item);
                    if (uber == null) continue;
                    for (UberAbility ability : uber.getAbilities()) {
                        if (ability.getManaCost() > 0) {
                            usesMana = true;
                            break;
                        }
                    }
                }
                if (!usesMana) return;

                // add new player to mana map
                if (!Utilities.mana.containsKey(player) || !Utilities.maxMana.containsKey(player)) {
                    Utilities.mana.put(player, Utilities.DEFAULT_MAX_MANA);
                    Utilities.maxMana.put(player, Utilities.DEFAULT_MAX_MANA);
                }
                else {
                    double newMana = Utilities.mana.get(player) + Utilities.maxMana.get(player) / 100;
                    Utilities.mana.put(player, Math.min(newMana, Utilities.maxMana.get(player)));
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + "" + Math.round(Utilities.mana.get(player)) + "/" + Math.round(Utilities.maxMana.get(player)) + "✎ Mana"));
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
            loadResource(this, "config.yml");
        }
        FileConfiguration config = this.getConfig();

        // general settings
        String p = config.getString("plugin-prefix");
        if (p != null) prefix = ChatColor.translateAlternateColorCodes('&', p);
        defaultUberItems = config.getBoolean("default-uber-items");
        defaultUberMaterials = config.getBoolean("default-uber-materials");

        String ibl = config.getString("item-blacklist"); itemBlacklist.clear();
        if (ibl != null) itemBlacklist.addAll(Arrays.asList(ibl.trim().split(",")));

        String mbl = config.getString("material-blacklist"); materialBlacklist.clear();
        if (mbl != null) materialBlacklist.addAll(Arrays.asList(mbl.trim().split(",")));

        String iwl = config.getString("item-whitelist"); itemWhitelist.clear();
        if (iwl != null) itemWhitelist.addAll(Arrays.asList(iwl.trim().split(",")));

        String mwl = config.getString("material-whitelist"); materialWhitelist.clear();
        if (mwl != null) {
            materialWhitelist.addAll(Arrays.asList(mwl.trim().split(",")));
            materialWhitelist.add("null");
        }

        // sorting settings
        sortingMode = config.getInt("sorting-mode");
        reverseSort = config.getBoolean("reverse-sort");
        externalSort = config.getBoolean("external-sort");
        multiSort = config.getBoolean("multi-sort");
        multiSortTimeout = config.getInt("multi-sort-timeout");
        automaticSort = config.getBoolean("automatic-sort");
        ignoreBuildPerms = config.getBoolean("ignore-area-build-permissions");

        useWhiteList = config.getBoolean("use-inventory-whitelist");
        useBlackList = config.getBoolean("use-inventory-blacklist");

        String w = config.getString("inventory-whitelist"); whiteList.clear();
        if (w != null) whiteList.addAll(Arrays.asList(w.split(",")));

        String b = config.getString("inventory-blacklist"); blackList.clear();
        if (b !=null) blackList.addAll(Arrays.asList(b.split(",")));

        // post confirmation in chat
        Bukkit.getLogger().info(consolePrefix + "Settings reloaded from config");
    }

    // load language.yml (generate one if not there)
    private void loadLangFile() {
        // console and IO
        File langFile = new File(getDataFolder(), "language.yml");
        FileConfiguration langFileConfig = new YamlConfiguration();
        if (!langFile.exists()) { loadResource(this, "language.yml"); }

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
        getServer().getPluginManager().registerEvents(new MiscEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(),this);
        getServer().getPluginManager().registerEvents(new InventoryClose(), this);
        getServer().getPluginManager().registerEvents(new RenameItem(), this);
        getServer().getPluginManager().registerEvents(new BlockEvents(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new Bucket(), this);
    }

    private static void registerItemsAndMaterials() {
        haveCountedDefaultItems = false;

        // make sure that items and materials are updated ONLY for the base plugin
        List<String> str = new ArrayList<>(items.keySet());
        for (String item : str) {
            if (default_items.contains(item)) {
                itemIDs.remove(items.get(item).getUUID());
                items.remove(item);
            }
        }
        str = new ArrayList<>(materials.keySet());
        for (String material : str) {
            if (default_materials.contains(material)) {
                materialIDs.remove(materials.get(material).getUUID());
                materials.remove(material);
            }
        }

        // register the Uber Workbench separately from the rest of the items, it's essential
        putItem("uber_workbench", new uber_workbench(Material.CRAFTING_TABLE, "Uber WorkBench", UberRarity.UNCOMMON, false, false, false,
                Collections.singletonList(new UberAbility("A new chapter", AbilityType.RIGHT_CLICK, "Opens the UberItems Crafting Menu")), null));

        // register the error UberItem and UberMaterial separately from the rest of the items, it's essential
        putItem("null", new null_item(Material.BARRIER, "null", UberRarity.UNFINISHED, false, false, false, Collections.emptyList(), null));
        putMaterial("null", new UberMaterial(Material.BARRIER, "null", UberRarity.UNFINISHED, false, false, false, "ERROR: UberMaterial not found", null));

        // register UberMaterials, UberItems. Then count the number of default items
        if (defaultUberMaterials) RegisterItems.registerUberMaterials();
        if (defaultUberItems) {
            RegisterItems.registerUberItems();
            defaultItemCount = items.keySet().size();
        }
        haveCountedDefaultItems = true;
    }

    // process active effets for uber items that are in use
    // TODO: Do active effects for uber items not in hand?
    private static void uberActiveEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            // main hand
            if (Utilities.isUber(player.getInventory().getItemInMainHand())) {
                UberItem uber = Utilities.getUber(player.getInventory().getItemInMainHand());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInMainHand());
            }

            // off hand
            if (Utilities.isUber(player.getInventory().getItemInOffHand())) {
                UberItem uber = Utilities.getUber(player.getInventory().getItemInOffHand());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());

            }

            // helmet slot
            if (Utilities.isUber(player.getInventory().getHelmet())) {
                UberItem uber = Utilities.getUber(player.getInventory().getHelmet());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());
            }

            // chestplate slot
            if (Utilities.isUber(player.getInventory().getChestplate())) {
                UberItem uber = Utilities.getUber(player.getInventory().getChestplate());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());
            }

            // leggings slot
            if (Utilities.isUber(player.getInventory().getLeggings())) {
                UberItem uber = Utilities.getUber(player.getInventory().getLeggings());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());
            }

            // boots slot
            if (Utilities.isUber(player.getInventory().getBoots())) {
                UberItem uber = Utilities.getUber(player.getInventory().getBoots());
                if (uber == null) continue;

                // enforce premium vs lite
                if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) return;

                if (uber.hasActiveEffect()) uber.activeEffect(player, player.getInventory().getItemInOffHand());
            }

        }
    }

    // load file from JAR with comments
    private static File loadResource(Plugin plugin, String resource) {
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

    // place UberItems and UberMaterials into the proper HashMaps
    public static void putItem(String name, UberItem item) {

        if (name.equals("null") || name.equals("uber_workbench")) {
            items.put(name, item);
            itemIDs.put(item.getUUID(), item);
            return;
        }

        if (itemBlacklist.contains(name)) return;
        if (itemWhitelist.size() > 0 && !itemWhitelist.contains(name)) return;

        if (items.keySet().size() < defaultItemCount + 10 || !haveCountedDefaultItems || premium) {
            items.put(name, item);
            itemIDs.put(item.getUUID(), item);
        }
        else Bukkit.getLogger().severe("You're trying to load more than 5 custom items! Purchase UberItems Premium to load unlimited custom items: https://www.spigotmc.org/resources/83851/");
    }
    public static void putMaterial(String name, UberMaterial material) {

        if (!name.equals("null")) {
            if (materialBlacklist.contains(name)) return;
            if (materialWhitelist.size() > 0 && !materialWhitelist.contains(name)) return;
        }

        materials.put(name, material);
        materialIDs.put(material.getUUID(), material);
    }

    // put entire armor set into the UberItems HashMap
    public static void putUberArmorSet(Class<? extends UberItem> uber, String name, UberRarity rarity, ArmorType type, Color dyeColor, List<UberAbility> abilities, ItemStack customHelmet, ItemStack customChestplate, ItemStack customLeggings, ItemStack customBoots, UberCraftingRecipe helmetRecipe, UberCraftingRecipe chestplateRecipe, UberCraftingRecipe leggingsRecipe, UberCraftingRecipe bootsRecipe) {

        String helmet_name, helmet_code, chestplate_name, chestplate_code, leggings_name, leggings_code, boots_name, boots_code;
        helmet_code = name.toLowerCase() + "_helmet"; helmet_name = name + " Helmet";
        chestplate_code = name.toLowerCase() + "_chestplate"; chestplate_name = name + " Chestplate";
        leggings_code = name.toLowerCase() + "_leggings"; leggings_name = name + " Leggings";
        boots_code = name.toLowerCase() + "_boots"; boots_name = name + " Boots";

        if (dyeColor == null) dyeColor = Color.GRAY;

        // set the armor pieces to specific materials and dye colors if applicable
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET); if (customHelmet != null) helmet = customHelmet.clone();
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE); if (customChestplate != null) chestplate = customChestplate.clone();
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS); if (customLeggings != null) leggings = customLeggings.clone();
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS); if (customBoots != null) boots = customBoots.clone();
        switch (type) {
            case LEATHER:
                if (customHelmet == null) { helmet = new ItemStack(Material.LEATHER_HELMET); Utilities.dyeArmor(helmet, dyeColor); }
                if (customChestplate == null) { chestplate = new ItemStack(Material.LEATHER_CHESTPLATE); Utilities.dyeArmor(chestplate, dyeColor); }
                if (customLeggings == null) { leggings = new ItemStack(Material.LEATHER_LEGGINGS); Utilities.dyeArmor(leggings, dyeColor); }
                if (customBoots == null) { boots = new ItemStack(Material.LEATHER_BOOTS); Utilities.dyeArmor(boots, dyeColor); }
                break;
            case CHAINMAIL:
                if (customHelmet == null) helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                if (customChestplate == null) chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                if (customLeggings == null) leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                break;
            case GOLD:
                if (customHelmet == null) helmet = new ItemStack(Material.GOLDEN_HELMET);
                if (customChestplate == null) chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
                if (customLeggings == null) leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
                boots = new ItemStack(Material.GOLDEN_BOOTS);
                break;
            case IRON:
                if (customHelmet == null) helmet = new ItemStack(Material.IRON_HELMET);
                if (customChestplate == null) chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                if (customLeggings == null) leggings = new ItemStack(Material.IRON_LEGGINGS);
                boots = new ItemStack(Material.IRON_BOOTS);
                break;
            case DIAMOND:
                if (customHelmet == null) helmet = new ItemStack(Material.DIAMOND_HELMET);
                if (customChestplate == null) chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                if (customLeggings == null) leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                if (customBoots == null) boots = new ItemStack(Material.DIAMOND_BOOTS);
                break;
        }

        try {
            putItem(helmet_code, uber.getConstructor(ItemStack.class, String.class, UberRarity.class, boolean.class, boolean.class, boolean.class, List.class, UberCraftingRecipe.class, String.class).newInstance(helmet, helmet_name, rarity, false, false, true, abilities, helmetRecipe, "HELMET"));
            putItem(chestplate_code, uber.getConstructor(ItemStack.class, String.class, UberRarity.class, boolean.class, boolean.class, boolean.class, List.class, UberCraftingRecipe.class, String.class).newInstance(chestplate, chestplate_name, rarity, false, false, false, abilities, chestplateRecipe, "CHESTPLATE"));
            putItem(leggings_code, uber.getConstructor(ItemStack.class, String.class, UberRarity.class, boolean.class, boolean.class, boolean.class, List.class, UberCraftingRecipe.class, String.class).newInstance(leggings, leggings_name, rarity, false, false, false, abilities, leggingsRecipe, "LEGGINGS"));
            putItem(boots_code, uber.getConstructor(ItemStack.class, String.class, UberRarity.class, boolean.class, boolean.class, boolean.class, List.class, UberCraftingRecipe.class, String.class).newInstance(boots, boots_name, rarity, false, false, false, abilities, bootsRecipe, "BOOTS"));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        // tag all armor pieces with full set bonus, helmet with helmet tag
        for (UberAbility ability : abilities) {
            if (ability.getType() == AbilityType.FULL_SET_BONUS) {
                String fullSetBonusTag = ability.getName().toLowerCase().replaceAll("[^a-z0-9/._-]", "");
                getItem(helmet_code).addStartingProperty(fullSetBonusTag, 1);
                getItem(chestplate_code).addStartingProperty(fullSetBonusTag, 1);
                getItem(leggings_code).addStartingProperty(fullSetBonusTag, 1);
                getItem(boots_code).addStartingProperty(fullSetBonusTag, 1);
            }
        }
        getItem(helmet_code).addStartingProperty("uberhelmet", 1);

    }

    // reload all plugin assets
    public static void reload() {
        getInstance().reloadConfig();
        getInstance().loadConfiguration();
        getInstance().loadLangFile();


        registerItemsAndMaterials();

        Bukkit.getLogger().info("configuration, items, and language settings reloaded");
    }

    // getters for UberItems and UberMaterials
    public static UberItem getItem(String key) {
        UberItem item = items.get(key);
        if (item == null) return items.get("null");
        else return item;
    }
    public static UberItem getItemFromID(int id) {
        UberItem item = itemIDs.get(id);
        if (item == null) return items.get("null");
        else return item;
    }
    public static Collection<UberItem> getItems() { return items.values(); }
    public static Collection<String> getItemNames() { return items.keySet(); }

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

    // getters for language phrases, version, instance
    public static String getPhrase(String key) {
        if (phrases.containsKey(key)) return phrases.get(key);
        else {
            Bukkit.getLogger().warning("no UberItems language phrase found for '" + key + "', is your language.yml up to date?");
            return "ERROR";
        }
    }
    public String getVersion() {
        return getDescription().getVersion();
    }
    public static UberItems getInstance() { return instance; }
}