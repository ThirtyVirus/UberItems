package thirtyvirus.uber.helpers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.*;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;

import java.lang.reflect.Field;
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

    public static List<Block> temporaryBlocks = new ArrayList<>();

    public static Map<Player, Boolean> dontUpdateMana = new HashMap<>();

    public static Map<Player, Double> mana = new HashMap<>();
    public static Map<Player, Double> maxMana = new HashMap<>();
    public static final Double DEFAULT_MAX_MANA = 100.0;

    public static final ItemStack emptySlot = Utilities.nameItem(Material.BLACK_STAINED_GLASS_PANE, " ");

    // GENERAL PLUGIN FUNCTIONS
    // _____________________________________________________________________________ \\

    /**
     * Warn a player or console with a list of messages, plays an error sound and uses red text
     *
     * @param sender the player or console being warned
     * @param messages the messages
     */
    public static void warnPlayer(CommandSender sender, List<String> messages) {
        if (sender instanceof Player) { Player player = (Player) sender; playSound(ActionSound.ERROR, player); }
        for (String message : messages) sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.RED + message);
    }

    /**
     * Warn a player or console with a single message, plays an error sound and uses red text
     *
     * @param sender the player or console being warned
     * @param message the message
     */
    public static void warnPlayer(CommandSender sender, String message) {
        warnPlayer(sender, Collections.singletonList(message));
    }

    /**
     * Inform a player or console with a list of messages
     *
     * @param sender the player or console being informed
     * @param messages the list of messages
     */
    public static void informPlayer(CommandSender sender, List<String> messages) {
        for (String message : messages) sender.sendMessage(UberItems.prefix + ChatColor.RESET + ChatColor.GRAY + message);
    }

    /**
     * Inform a player or console with a single message
     * @param sender the player or console being informed
     * @param message the message
     */
    public static void informPlayer(CommandSender sender, String message) {
        informPlayer(sender, Collections.singletonList(message));
    }

    /**
     * Return whether or not the give player has the proper permissions to use a given UberItem
     * Can be used to restrict access to code depending on whether or the player has proper permissions
     * USAGE: if (enforcePermissions) return;
     *
     * @param player the player being tested for permissions
     * @param item the UberItem
     * @return whether or not to enforce permissions (true = restrict code, false = allow the code to run)
     */
    public static boolean enforcePermissions(Player player, UberItem item) {

        // test for premium and over Rare rarity
        if (!UberItems.premium && item.getRarity().isRarerThan(UberRarity.EPIC)) {
            warnPlayer(player, UberItems.getPhrase("not-premium-message"));
            return true;
        }

        // test for player's item specific permissions
        if (!player.hasPermission("uber.item." + item.getName())) {
            warnPlayer(player, UberItems.getPhrase("no-permissions-message"));
            return true;
        }

        // test for player's rarity permissions
        switch (item.getRarity()) {
            case COMMON:
                if (player.hasPermission("uber.rarity.common")) return false;
                break;
            case UNCOMMON:
                if (player.hasPermission("uber.rarity.uncommon")) return false;
                break;
            case RARE:
                if (player.hasPermission("uber.rarity.rare")) return false;
                break;
            case EPIC:
                if (player.hasPermission("uber.rarity.epic")) return false;
                break;
            case LEGENDARY:
                if (player.hasPermission("uber.rarity.legendary")) return false;
                break;
            case MYTHIC:
                if (player.hasPermission("uber.rarity.mythic")) return false;
                break;
            case SPECIAL:
                if (player.hasPermission("uber.rarity.special")) return false;
                break;
            case VERY_SPECIAL:
                if (player.hasPermission("uber.rarity.very_special")) return false;
                break;
            case UNFINISHED:
                if (player.hasPermission("uber.rarity.unfinished")) return false;
                break;
        }

        warnPlayer(player, UberItems.getPhrase("no-permissions-message"));
        return true;
    }

    /**
     * @param player the player whose line of sight is being tested
     * @return the block the player is looking at, ignoring transparent blocks
     */
    public static Block getBlockLookingAt(Player player) {
        return getBlockLookingAt(player, 120);
    }
    public static Block getBlockLookingAt(Player player, int range) {
        return player.getTargetBlock(TRANSPARENT, range);
    }

    /**
     * as long as the entity is alive, aim it towards the closest living entity
     *
     * @param entity the entity which is to be launched
     * @param exception an entity who is to be ignored
     */
    public static void directEntity(Entity entity, Entity exception, int widthX, int heightY, int lengthZ) {

        if (!entity.isDead()) {
            Entity other = getClosestLivingEntity(entity.getLocation(), exception, widthX, heightY, lengthZ);
            if (other != null) {
                Location loc = other.getLocation().subtract(entity.getLocation()).add(0,1,0);
                entity.setVelocity(loc.toVector().normalize());
            }
            Utilities.scheduleTask(()->directEntity(entity, exception, widthX, heightY, lengthZ), 1);
        }
    }

    /**
     * get the closest living entity to a location
     *
     * @param location the location to start the search
     * @param exception an entity who is to be ignored
     */
    public static LivingEntity getClosestLivingEntity(Location location, Entity exception, int widthX, int heightY, int lengthZ) {
        double closest = 100;
        LivingEntity closestEntity = null;
        if (location.getWorld() == null) return null;

        for (Entity e : location.getWorld().getNearbyEntities(location, widthX, heightY, lengthZ)) {
            if (e instanceof LivingEntity) {
                // skip exception (usually the player that shot the homing arrow)
                if (e instanceof Player && e.equals(exception)) continue;

                double testClose = location.distance(e.getLocation());

                if (testClose < closest && Utilities.isPathObstructed(location.add(0,-1,0), e.getLocation()) != -1) {
                    closestEntity = (LivingEntity) e;
                    closest = testClose;
                }
            }
        }

        return closestEntity;
    }

    /**
     * safely teleport player to a specific location, if path or end location obstructed teleport to the next best spot
     *
     * @param entity the entity who is to be teleported
     * @param start the starting location of the teleport (eye loc for players typically)
     * @param end the end location
     */
    public static void safeTeleport(Entity entity, Location start, Location end) {
        if (start.getWorld() == null || end.getWorld() == null || !start.getWorld().equals(end.getWorld())) return;

        int obstruction = Utilities.isPathObstructed(start, end);

        if (obstruction == -1) entity.teleport(end);
        else {
            Location safeEnd = start.clone().add(entity.getLocation().add(0, entity.getHeight() - 1, 0).getDirection().multiply(obstruction));
            if (start.getWorld().getBlockAt(safeEnd).getType().isSolid()) {
                entity.teleport(safeEnd.add(0,1,0));
            }
            else entity.teleport(safeEnd);
        }
    }

    /**
     * @param location the location to be translated
     * @return a formatted string (world,x,y,z)
     */
    public static String toLocString(Location location) {
        if (location == null || location.getWorld() == null) return "";
        return location.getWorld().getName() + "," + (int) location.getX() + "," + (int) location.getY() + "," + (int) location.getZ();
    }

    /**
     * @param locString the locString to be translated
     * @return a location based on the string (world,x,y,z)
     */
    public static Location fromLocString(String locString) {
        if (locString.equals("")) return null;
        String[] data = locString.split(",");
        return new Location(Bukkit.getWorld(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
    }

    /**
     * @param sound the sound to be played
     * @param player the player the sound is to be played around
     */
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

    public static void openBackpack(Player player, ItemStack itemStack) {
        // verify that the player isn't null
        if (player == null) return;

        // set the inventory size
        int inventorySize = 0;
        if (UberItems.getItem("small_backpack").compare(itemStack))
            inventorySize = 27;
        else if (UberItems.getItem("big_backpack").compare(itemStack))
            inventorySize = 54;
        else return;

        // create the inventory
        Inventory inv = Bukkit.createInventory(null, inventorySize, "UberItems - Backpack");
        for (int counter = 0; counter < 9; counter++) inv.setItem(counter, emptySlot);
        ItemStack backpack = itemStack.clone();
        inv.setItem(4, backpack);

        // populate the inventory
        ItemStack[] items = Utilities.getCompactInventory(backpack);
        if (items.length == 0) items = new ItemStack[inventorySize];

        // store a value to verify that an item comes from a backpack, will be used later for anti-dupe
        for (ItemStack item : items) Utilities.storeIntInItem(item, 1, "frombackpack");

        for (int counter = 9; counter < inventorySize; counter++) {
            inv.setItem(counter, items[counter - 9]);
        }

        // open the inventory
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
    }

    /**
     * shorthand for using the Bukkit scheduler to run a runnable after i ticks
     *
     * @param run the runnable to be executed later
     * @param i the number of ticks delay
     */
    public static void scheduleTask(Runnable run, int i) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(UberItems.getInstance(), run, i);
    }

    /**
     * @param input  the source String
     * @return if given number string is integer
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate a unique integer from a string, can be used to seed random number generators or as a UUID
     *
     * @param s the source string
     * @return a unique integer made using the string
     */
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

    /**
     * Wrap the text from a string onto multiple lines, can be used to easily write item lore.
     * "/newline" can be used to force a new line
     *
     * @param string the source string
     * @param characterLimit the maximum number of characters per line
     * @param prefixColor the color that the text is to be
     * @return the wrapped text, in multiple lines
     */
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

    /**
     * 'Tag' an entity with a string value, can be checked later
     *
     * @param host the entity to be tagged
     * @param string the string to be tagged to the entity
     * @param key the key to be used to look up the tag
     */
    public static void tagEntity(Entity host, String string, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the entity isn't null, meta isn't null
        if (host == null) return;
        host.getPersistentDataContainer().set(k, new StoredString(), string);
    }

    /**
     * retrieve a tag from an entity if there is one
     *
     * @param host the entity to tested for the tag
     * @param key the key to be used to look up the tag
     * @return the value of the tag value associated with 'key', or null
     */
    public static String getEntityTag(Entity host, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the entity isn't null
        if (host == null) return null;

        String string = host.getPersistentDataContainer().get(k, new StoredString());
        if (string == null) string = "";
        return string;
    }

    /**
     * @param start starting location
     * @param end ending location
     * @return -1 is no obstruction in the path, the number of blocks that are un-obstructed if there is one
     */
    public static int isPathObstructed(Location start, Location end) {
        if (start.getWorld() == null || end.getWorld() == null || !start.getWorld().equals(end.getWorld())) return 0;
        Vector vector = end.toVector().subtract(start.toVector());
        double distance = Math.floor(vector.length());
        vector.multiply(1 / vector.length()); // convert v to a unit vector
        for (int progress = 0; progress <= distance; progress++) {
            vector = end.toVector().subtract(start.toVector());
            vector.multiply(1 / vector.length());
            Block block = start.getWorld().getBlockAt((start.toVector().add(vector.multiply(progress))).toLocation(start.getWorld()));
            if (block.getType().isSolid()) {
                return progress;
            }
        }
        return -1;
    }

    /**
     * Heal an entity and avoid errors
     *
     * @param entity the entity to be healed
     * @param amount the amount to heal the entity
     */
    public static void safeHeal(LivingEntity entity, int amount) {
        double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        if (entity.getHealth() + amount >= maxHealth) {
            entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
        else entity.setHealth(entity.getHealth() + amount);
    }

    /**
     * Change a block type into a specified material so long as it is within range of the player
     *
     * @param player the player to test the radius from
     * @param block to block to be effected
     * @param oldState the state of the block before changing material
     * @param material the material the temporarily change the block to
     * @param tickDelay the delay between checks to change the material type
     * @param radius the radius from the player to test for to keep the block effected
     */
    public static void maintainBlockReplacement(Player player, Block block, BlockState oldState, Material material, int tickDelay, int radius) {
        maintainBlockReplacement(player, block, oldState, material, tickDelay, radius, radius, radius);
    }
    public static void maintainBlockReplacement(Player player, Block block, BlockState oldState, Material material, int tickDelay, int widthX, int heightY, int lengthZ) {
        List<Block> blocks = getBlocksInCube(player.getLocation(), widthX, heightY, lengthZ);
        if (block.getState() instanceof Container) return;
        if (blocks.contains(block) && player.isOnline()) {
            block.setType(material);
            if (!temporaryBlocks.contains(block)) temporaryBlocks.add(block);
            Utilities.scheduleTask(()->maintainBlockReplacement(player, block, oldState, material, tickDelay, widthX, heightY, lengthZ), 5);
        }
        else {
            block.setType(oldState.getType());
            block.setBlockData(oldState.getBlockData());
            temporaryBlocks.remove(block);
        }
    }

    /**
     * get all blocks in a radius
     *
     * @param location the center of the cube
     * @param radius the direction to check in all axis
     * @return a list of the blocks contained in the radius
     */
    public static List<Block> getBlocksInRadius(Location location, int radius) {
        return getBlocksInCube(location, radius, radius, radius);
    }
    /**
     * get all blocks in a cube
     *
     * @param location the center of the cube
     * @param widthX the size of the cube in the x axis
     * @param heightY the size of the cube in the y axis
     * @param lengthZ the size of the cube in the z axis
     * @return a list of the blocks contained in the cube
     */
    public static List<Block> getBlocksInCube(Location location, int widthX, int heightY, int lengthZ) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - widthX; x <= location.getBlockX() + widthX; x++) {
            for(int y = location.getBlockY() - heightY; y <= location.getBlockY() + heightY; y++) {
                for(int z = location.getBlockZ() - lengthZ; z <= location.getBlockZ() + lengthZ; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    // ITEM FUNCTIONS
    // _____________________________________________________________________________ \\

    /**
     * @param item the ItemStack to be renamed
     * @param name the new name
     * @return the renamed ItemStack
     */
    public static ItemStack nameItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * creates item that is renamed given material and name
     *
     * @param item the material of the new ItemStack
     * @param name the name of the new ItemStack
     * @return the renamed new ItemStack
     */
    public static ItemStack nameItem(Material item, String name) {
        return nameItem(new ItemStack(item), name);
    }

    /**
     * Set the lore of an item
     *
     * @param item the ItemStack to be effected
     * @param lore a list of strings that represent the new lore
     * @return the updated ItemStack
     */
    public static ItemStack loreItem(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Add an enchantment glint to a given ItemStack, with no enchantment text in the lore
     *
     * @param item the ItemStack to be "enchanted"
     */
    public static void addEnchantGlint(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
    }

    /**
     * Remove all enchantments from an item
     *
     * @param item the ItemStack to be un-enchanted
     */
    public static void removeEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        item.setItemMeta(meta);
    }

    /**
     * Set the given ItemStack's durability to maximum
     *
     * @param item the ItemStack to be repaired
     */
    public static void repairItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            ((Damageable)meta).setDamage(0);
            item.setItemMeta(meta);
        }
    }

    /**
     * @param host the ItemStack to contain the String
     * @param string the String to be stored
     * @param key the key for the String
     */
    public static void storeStringInItem(ItemStack host, String string, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredString(), string);
        host.setItemMeta(itemMeta);
    }

    /**
     * get a string value from the meta of an item, completely invisible to player
     *
     * @param host the ItemStack containing the String
     * @param key the key for the String
     * @return the String stored under the key
     */
    public static String getStringFromItem(ItemStack host, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return null;
        if (!host.hasItemMeta()) return null;

        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(k, new StoredString())) return container.get(k, new StoredString());

        // if no entry, return null
        return null;
    }

    /**
     * store an int value in the meta of an item, completely invisible to player
     *
     * @param host the ItemStack to contain the integer
     * @param i the Integer to be stored in the ItemStack
     * @param key the key for the integer
     */
    public static void storeIntInItem(ItemStack host, Integer i, String key) {
        NamespacedKey k = new NamespacedKey(UberItems.getInstance(), key);

        // make sure that the item isn't null, meta isn't null
        if (host == null) return;
        if (!host.hasItemMeta()) return;

        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(k, new StoredInt(), i);
        host.setItemMeta(itemMeta);
    }

    /**
     * retrieve an int value from the meta of an item, completely invisible to player
     *
     * @param host the ItemStack containing the integer
     * @param key the key for the integer
     * @return the integer stored under the key
     */
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

    /**
     * store a list of items in the meta of an item, completely invisible to the player
     *
     * @param host the ItemStack to contain the compact inventory
     * @param items an array of ItemStack
     */
    public static void saveCompactInventory(ItemStack host, ItemStack[] items) {
        NamespacedKey key = new NamespacedKey(UberItems.getInstance(), "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, new CompactInventory(), items);
        host.setItemMeta(itemMeta);
    }

    /**
     * retrieve a list of items from the meta of an item, completely invisible to the player
     *
     * @param host the ItemStack containing the compact inventory
     * @return an array of ItemStack
     */
    public static ItemStack[] getCompactInventory(ItemStack host) {
        NamespacedKey key = new NamespacedKey(UberItems.getInstance(), "compact-inventory");
        ItemMeta itemMeta = host.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if(container.has(key, new CompactInventory()))
            return container.get(key, new CompactInventory());

        return new ItemStack[0];
    }

    /**
     * return a player head item with the skin from the url
     *
     * @param url the full minecraft.net URL for the player head texture (minecraft-heads.com is good for this)
     * @return an array of ItemStack
     */
    public static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);

        if (url == null || url.isEmpty())
            return head;

        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;

        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        profileField.setAccessible(true);

        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    /**
     * Dye a leather armor piece
     *
     * @param item the armor to be dyed
     * @param color the color to dye the armor piece
     * @return the armor piece but dyed
     */
    public static ItemStack dyeArmor(ItemStack item, Color color) {
        if (!(item.getItemMeta() instanceof LeatherArmorMeta)) return item;
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static void givePlayerItemSafely(Player player, ItemStack item) {
        final Map<Integer, ItemStack> items = player.getInventory().addItem(item);
        for (final ItemStack i : items.values()) {
            if (i == null || i.getType() == Material.AIR) continue;
            Entity e = player.getWorld().dropItemNaturally(player.getLocation(), i);
            e.setVelocity(player.getLocation().getDirection().multiply(0.1f));
        }
    }

    // UBERITEM FUNCTIONS
    // _____________________________________________________________________________ \\

    /**
     * @param item the ItemStack being tested
     * @return whether or not the item is an UberItem
     */
    public static boolean isUber(ItemStack item) { return getIntFromItem(item, "UberUUID") != 0; }

    /**
     * @param item the ItemStack being tested
     * @return whether or not the item is an UberMaterial
     */
    public static boolean isUberMaterial(ItemStack item) { return getIntFromItem(item, "MaterialUUID") != 0; }

    /**
     * @param item the ItemStack being tested
     * @return the UberItem that represents the item (null if no match found)
     */
    public static UberItem getUber(ItemStack item) {
        int UUID = getIntFromItem(item, "UberUUID");
        if (UUID == 0) return null;
        else return UberItems.getItemFromID(UUID);
    }

    /**
     * @param item the ItemStack being tested
     * @return the UberMaterial that represents the item (null if no match found)
     */
    public static UberMaterial getUberMaterial(ItemStack item) {
        int UUID = getIntFromItem(item, "MaterialUUID");
        if (UUID == 0) return null;
        else return UberItems.getMaterialFromID(UUID);
    }

    /**
     * Search through an inventory to find the first instance of an item belonging to uber
     *
     * @param inv the inventory being searched through
     * @param uber the requested UberItem type
     * @return the first found instance of uber in the inventory (or null if none found)
     */
    public static ItemStack searchFor(Inventory inv, UberItem uber) {
        for (ItemStack item : inv) if (uber.compare(item)) return item;
        return null;
    }

    /**
     * @param player the player whose armor is to be checked
     * @param fullSetBonus the full set bonus to be checked for
     * @return whether or not the player is wearing the full set
     */
    public static boolean hasFullSetBonus(Player player, String fullSetBonus) {

        // verify that the player has a full set
        if (player.getInventory().getHelmet() == null) return false;
        if (player.getInventory().getChestplate() == null) return false;
        if (player.getInventory().getLeggings() == null) return false;
        if (player.getInventory().getBoots() == null) return false;

        // verify that every armor slot is tagged with the ability
        if (Utilities.getIntFromItem(player.getInventory().getHelmet(), fullSetBonus) == 0) return false;
        if (Utilities.getIntFromItem(player.getInventory().getChestplate(), fullSetBonus) == 0) return false;
        if (Utilities.getIntFromItem(player.getInventory().getLeggings(), fullSetBonus) == 0) return false;
        if (Utilities.getIntFromItem(player.getInventory().getBoots(), fullSetBonus) == 0) return false;

        return true;
    }

    // enforce a given mana cost, if available use the mana and return false
    public static boolean enforceManaCost(Player player, double cost) {
        if (!mana.containsKey(player)) mana.put(player, 0.0);
        if (mana.get(player) >= cost) {
            mana.put(player, mana.get(player) - cost);
            return false;
        }
        else {
            Utilities.playSound(ActionSound.ERROR, player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "NOT ENOUGH MANA"));
            dontUpdateMana.put(player, true);
            Utilities.scheduleTask(()->dontUpdateMana.remove(player), 20);
            return true;
        }
    }

    /**
     * Return whether or not 'seconds' seconds have passed since the previous time 'player' used 'key' action with 'item'.
     * Can be used to restrict access to code depending on whether or not the cooldown is over.
     * USAGE: if (enforceCooldown) return;
     *
     * @param player the player holding the item
     * @param key a string specifying which action is being tested (can be any string that you want)
     * @param seconds the minimum time since the action to allow another action use
     * @param item the UberItem being tested
     * @param throwError whether or not to warn the player of the failure to execute the action, and the time remaining
     * @return whether or not to enforce the cooldown (true = restrict code, false = allow the code to run)
     */
    public static boolean enforceCooldown(Player player, String key, double seconds, ItemStack item, boolean throwError) {
        double time = (double)System.currentTimeMillis() / 1000;

        // get time last used from item
        int lastTime = getIntFromItem(item, key);

        // add "time last used" key if not already there
        if (lastTime == 0) {
            storeIntInItem(item, (int)time, key);
            return false;
        }

        // was the item  last used longer than "seconds" seconds ago?
        else {
            if (time - seconds > lastTime) {
                storeIntInItem(item, (int)time, key);
                return false; // yes, allow the action (plus update the last time used)
            }
            else {
                int timeLeft = (int)time - lastTime;
                timeLeft = (int)seconds - timeLeft;
                if (throwError) warnPlayer(player, "This ability is on cooldown for " + timeLeft + "s.");
                return true; // no, disallow the action
            }
        }
    }

    /**
     * Apply an upgrade to an UberItem, which is traditionally done through the clickedInInventoryAction method.
     * Checks if the requested upgrade is already on the item, if not adds it, cancels the event, and consumes the upgrade item.
     * NOTE: Upgrade name must be [a-z0-9A-Z/._-]
     *
     * @param player the player holding the item
     * @param event an InventoryClickEvent, adding an upgrade to an item is meant to be done by dropping an item on the UberItem
     * @param item the UberItem in ItemStack form
     * @param upgradeName the name of the upgrade
     * @param upgradeDescription the description of the upgrade
     */
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

    /**
     * Remove an upgrade from an UberItem, which is traditionally done through the clickedInInventoryAction method
     * Checks if the requested upgrade is already on the item, if so remove the upgrade, cancel the event, and consumes the upgrade item
     * NOTE: Upgrade name must be [a-z0-9A-Z/._-]
     *
     * @param player the player holding the item
     * @param event an InventoryClickEvent, removing an upgrade from an item is meant to be done by dropping an item on the UberItem
     * @param item the UberItem in ItemStack form
     * @param upgradeName the name of the upgrade
     */
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

    /**
     * apply upgrade to UberItem, and update lore. Can be used to lock item functionality behind an upgrade
     *
     * @param item the MC itemstack to be updated
     * @param upgradeName the name of the upgrade
     * @param upgradeDescription the description of the upgrade
     */
    private static void addUpgrade(ItemStack item, String upgradeName, String upgradeDescription) {
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

    /**
     * remove upgrade from UberItem, and update lore.
     *
     * @param item the MC itemstack to be updated
     * @param upgradeName the name of the upgrade
     */
    private static void removeUpgrade(ItemStack item, String upgradeName) {
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

    /**
     * @param item the UberItem in ItemStack form
     * @param upgradeName the name of the upgrade
     * @return whether or not the UberItem has the upgrade
     */
    public static boolean hasUpgrade(ItemStack item, String upgradeName) {
        String upgrade = getStringFromItem(item, "UberUpgrade-" + upgradeName);
        if (upgrade == null) return false;
        return !upgrade.equals("");
    }

    /**
     * @param item the UberItem in ItemStack form
     * @param upgradeName the name of the upgrade
     * @return the description of the upgrade (or " " if there is no such upgrade)
     */
    public static String getUpgradeDescription(ItemStack item, String upgradeName) {
        return getStringFromItem(item, "UberUpgrade-" + upgradeName);
    }

    /**
     * @param item the UberItem in ItemStack form
     * @return an array of String, elements consisting of the upgrades on this particular item
     */
    public static String[] getUpgrades(ItemStack item) {
        String rawList = getStringFromItem(item, "UberUpgrades");
        if (rawList != null) return rawList.split(",");
        else return null;
    }

}