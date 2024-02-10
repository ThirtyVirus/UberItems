package thirtyvirus.uber;

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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import thirtyvirus.uber.commands.UberCommand;
import thirtyvirus.uber.events.MiscEvents;
import thirtyvirus.uber.events.TabComplete;
import thirtyvirus.uber.events.InventoryClick;
import thirtyvirus.uber.events.PlayerUseUberItem;
import thirtyvirus.uber.helpers.*;
import thirtyvirus.uber.items.null_item;
import thirtyvirus.uber.items.uber_workbench;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class UberItems extends JavaPlugin {

    // TODO
    //  fix duplicate item / material entries being null with UberDrops
    //  process active effects for uber items not in hand without big performance hit?

    // data for UberItems, UberMaterials
    private static final Map<String, UberItem> items = new HashMap<>();
    private static final Map<Integer, UberItem> itemIDs = new HashMap<>();
    private static final Map<String, UberMaterial> materials = new HashMap<>();
    private static final Map<Integer, UberMaterial> materialIDs = new HashMap<>();

    public enum ArmorType {LEATHER, CHAINMAIL, GOLD, IRON, DIAMOND}
    public static final List<Integer> default_items = new ArrayList<>();
    public static final List<Integer> default_materials = new ArrayList<>();

    // chat messages
    private static final Map<String, String> phrases = new HashMap<>();

    // global plugin settings
    public static String prefix = "&8&l[&5&lUberItems&8&l] &8&l";
    public static String consolePrefix = "[UberItems] ";
    public static boolean defaultUberItems = true;
    public static boolean defaultUberMaterials = true;
    public static List<Integer> scheduledTasks = new ArrayList<>();
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
    private static final Set<Listener> registeredListeners = new HashSet<>();
    private static UberItems instance;

    // actions to be taken on plugin enable
    public void onEnable() {
        // set static instance of the main class, for use in other parts of the plugin & the API
        instance = this;

        // load config.yml and language.yml (generate them if not there)
        loadConfiguration();
        loadLangFile();

        // register commands, events, items and materials
        registerCommands();
        registerEvents();
        registerItemsAndMaterials();

        // schedule repeating task for processing UberItem active effects
        scheduledTasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, UberItems::uberActiveEffects, activeEffectsDelay, activeEffectsDelay));

        // schedule checking of recent added containers (Document of Order)
        scheduledTasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> SortingUtilities.checkCancelMultisort(multisorts, multiSortTimeout), 20 * multiSortTimeout, 20 * multiSortTimeout));

        // schedule checking if a player is in the crafting menu (to update the recipe)
        scheduledTasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTitle().contains("UberItems - Craft Item")) {
                    MenuUtils.checkCraft(player.getOpenInventory().getTopInventory());
                }
            }
        }, 10, 10));

        // manage player mana (regeneration and updating display)
        scheduledTasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Utilities.dontUpdateMana.containsKey(player)) continue;

                // add new player to mana map
                if (!Utilities.mana.containsKey(player) || !Utilities.maxMana.containsKey(player)) {
                    Utilities.mana.put(player, Utilities.DEFAULT_MAX_MANA);
                    Utilities.maxMana.put(player, Utilities.DEFAULT_MAX_MANA);
                }
                // regenerate mana
                else {
                    double newMana = Utilities.mana.get(player) + Utilities.maxMana.get(player) / 100;
                    Utilities.mana.put(player, Math.min(newMana, Utilities.maxMana.get(player)));
                }

                // only show mana if holding item that uses it
                boolean usesMana = false;
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                ItemStack offHand = player.getInventory().getItemInOffHand();
                if (Utilities.isUber(mainHand)) {
                    UberItem uber = Utilities.getUber(mainHand);
                    if (uber != null) { for (UberAbility ability : uber.getAbilities()) { if (ability.getManaCost() > 0) { usesMana = true; break; } } }
                }
                if (Utilities.isUber(offHand)) {
                    UberItem uber = Utilities.getUber(offHand);
                    if (uber != null) { for (UberAbility ability : uber.getAbilities()) { if (ability.getManaCost() > 0) { usesMana = true; break; } } }
                }
                if (usesMana) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA + String.valueOf(Math.round(Utilities.mana.get(player))) + "/" + Math.round(Utilities.maxMana.get(player)) + "✎ Mana"));
            }
        }, 10, 10));

        // post confirmation in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been enabled");
    }

    // actions to be taken on plugin disable
    public void onDisable() {
        // un-register Listeners
        for (Listener listener : registeredListeners) HandlerList.unregisterAll(listener);
        registeredListeners.clear();

        // cancel scheduled tasks
        for (int id : scheduledTasks) Bukkit.getScheduler().cancelTask(id);
        scheduledTasks.clear();

        // posts exit message in chat
        getLogger().info(getDescription().getName() + " V: " + getDescription().getVersion() + " has been disabled");
    }

    // load config.yml (generate one if not there)
    private void loadConfiguration() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) Utilities.loadResource(this, "config.yml");
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
        String w = config.getString("inventory-whitelist"); whiteList.clear();
        if (w != null) whiteList.addAll(Arrays.asList(w.split(",")));

        useBlackList = config.getBoolean("use-inventory-blacklist");
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
        if (!langFile.exists()) { Utilities.loadResource(this, "language.yml"); }

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
        registerListener(new PlayerUseUberItem());
        registerListener(new MiscEvents());
        registerListener(new InventoryClick());
    }
    private static void registerListener(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
        registeredListeners.add(listener);
    }

    private static void registerItemsAndMaterials() {

        // unload old versions of default items and materials
        for (UberItem item : getItems()) if (default_items.contains(item.getUUID())) { removeItem(item); break; }
        for (UberMaterial material: getMaterials()) if (default_materials.contains(material.getUUID())) { removeMaterial(material); break; }
        default_items.clear(); default_materials.clear();

        // register (Uber Workbench, null Item, null Material) separately from the rest of the items, they are essential
        putItem("uber_workbench", new uber_workbench(Material.CRAFTING_TABLE, "Uber WorkBench", UberRarity.UNCOMMON, false, false, false,
                Collections.singletonList(new UberAbility("A new chapter", AbilityType.RIGHT_CLICK, "Opens the UberItems Crafting Menu")), null));
        putItem("null", new null_item(Material.BARRIER, "null", UberRarity.UNFINISHED, false, false, false, Collections.emptyList(), null));
        putMaterial("null", new UberMaterial(Material.BARRIER, "null", UberRarity.UNFINISHED, false, false, false, "ERROR: UberMaterial not found", null));

        // register default UberMaterials, then UberItems
        if (defaultUberMaterials) RegisterItems.registerUberMaterials();
        if (defaultUberItems) RegisterItems.registerUberItems();
    }

    // process active effects for uber items that are in use
    private static void uberActiveEffects() {

        // loop through all online players to check for UberItems with active effects
        for (Player player : Bukkit.getOnlinePlayers()) {

            // process active effects for UberItems in main and off hands, armor slots
            List<ItemStack> items = Arrays.asList(
                    player.getInventory().getItemInMainHand(),
                    player.getInventory().getItemInOffHand(),
                    player.getInventory().getHelmet(),
                    player.getInventory().getChestplate(),
                    player.getInventory().getLeggings(),
                    player.getInventory().getBoots());

            for (ItemStack item : items) {
                UberItem uber = Utilities.getUber(item);
                if (uber != null && uber.hasActiveEffect()) uber.activeEffect(player, item);
            }

        }
    }

    // place UberItems and UberMaterials into the proper HashMaps
    public static void putItem(String name, UberItem item) {

        // skip blacklist / whitelist for essential items
        if (!name.equals("null") && !name.equals("uber_workbench")) {
            // don't add item if in blacklist, or not included in whitelist
            if (itemBlacklist.contains(name)) return;
            if (itemWhitelist.size() > 0 && !itemWhitelist.contains(name)) return;
        }

        // if the event implements listener, register events (MAKE SURE NOT TO DO THIS IN ADDON PLUGINS)
        if (item instanceof Listener && !registeredListeners.contains(item)) registerListener((Listener) item);

        // add the item
        items.put(name, item);
        itemIDs.put(item.getUUID(), item);
    }
    public static void putMaterial(String name, UberMaterial material) {

        // skip blacklist / whitelist for essential material
        if (!name.equals("null")) {
            if (materialBlacklist.contains(name)) return;
            if (materialWhitelist.size() > 0 && !materialWhitelist.contains(name)) return;
        }

        // add the material
        materials.put(name, material);
        materialIDs.put(material.getUUID(), material);
    }
    public static void removeItem(UberItem item) {
        // unregister Listeners
        if (item instanceof Listener) {
            HandlerList.unregisterAll((Listener) item);
            registeredListeners.remove(item);
        }

        itemIDs.remove(item.getUUID());
        for (String key : items.keySet()) if (items.get(key).getUUID() == item.getUUID()) {
            items.remove(key);
            return;
        }

    }
    public static void removeMaterial(UberMaterial material) {
        materialIDs.remove(material.getUUID());
        for (String key : materials.keySet()) if (materials.get(key).getUUID() == material.getUUID()) {
            materials.remove(key);
            return;
        }
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
        instance.reloadConfig();
        instance.loadConfiguration();
        instance.loadLangFile();

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