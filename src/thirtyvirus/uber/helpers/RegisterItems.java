package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.items.*;

import java.util.Arrays;
import java.util.Collections;

public class RegisterItems {

    // NEW UBER ITEM CHECKLIST

    // - make a new class file, named with all lowercase lettering and underscores for spaces
    // - copy the UberItemTemplate class contents into the new class, extend UberItem
    // - make a putItem entry, follow the format of previous items and make sure to give a unique id
    // - write the unique item ability code in the appropriate method

    // - add the following line of code just after executing the item's ability:
    //      onItemUse(player, item); // confirm that the item's ability has been successfully used

    // - if the ability needs a cooldown, prefix it's code with a variation of the following line of code:
    //      if (!Utilities.enforceCooldown(getMain(), player, "name", 1, item, true)) return;

    // - if the item needs work done on create (like adding enchantments, adding other data) refer to onItemStackCreate
    // - if the item needs a prefix or suffix in its description,
    //   refer to the getSpecificLorePrefix and getSpecificLoreSuffix functions, then add the following:
    //      lore.add(ChatColor.RESET + "text goes here");

    // - if you need to store & retrieve ints and strings from items, you can use the following functions:
    //      Utilities.storeIntInItem(getMain(), item, 1, "number tag");
    //      if (Utilities.getIntFromItem(getMain(), item, "number tag") == 1) // { blah blah blah }
    //      (the same case for strings, just storeStringInItem and getStringFromItem)

    // register UberItems
    public static void registerUberItems() {
        // UberItems.putItem("NAME", new UberItemTemplate(0, UberRarity.UNFINISHED, "NAME", Material.STONE, false, false, false, Arrays.asList(new UberAbility("Ability name!", AbilityType.RIGHT_CLICK, "Ability description")), null));

        UberItems.putItem("builders_wand", new builders_wand(Material.BLAZE_ROD, "Builder's Wand", UberRarity.LEGENDARY,
                  false, false, true,
                Collections.singletonList(new UberAbility("Contruction!", AbilityType.RIGHT_CLICK, "Right click the face of any block to extend all connected block faces. /newline " + ChatColor.DARK_GRAY + "(consumes blocks from your inventory)")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.GOLD_BLOCK, 8),
                        new ItemStack(Material.CHEST),
                        new ItemStack(Material.GOLD_BLOCK, 8),
                        new ItemStack(Material.GOLD_BLOCK, 8),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.GOLD_BLOCK, 8),
                        new ItemStack(Material.GOLD_BLOCK, 8),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.GOLD_BLOCK, 8)), false, 1)));

        UberItems.putItem("lunch_box", new lunch_box(Material.CONDUIT, "Lunch Box", UberRarity.RARE,
                  false, false, false,
                Collections.singletonList(new UberAbility("Gluttony", AbilityType.NONE, "Automatically feeds you when hungry " + ChatColor.DARK_GRAY + "(drag & click food onto box to fill)")),
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.APPLE, 32),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.COOKED_COD, 2),
                        new ItemStack(Material.CHEST, 64),
                        new ItemStack(Material.COOKED_BEEF, 2),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.APPLE, 32),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1)), false, 1)));

        UberItems.putItem("document_of_order", new document_of_order(Material.PAPER, "Document of Order", UberRarity.EPIC,
                 false, false, false,
                Arrays.asList(new UberAbility("Bureaucracy", AbilityType.RIGHT_CLICK, "Use on a container block while crouched to sort that container's contents. Crouch use on air to sort own inventory."), new UberAbility("Multisort", AbilityType.LEFT_CLICK, "Select many containers at once, then left click any non-container block to confirm, cr crouch left click any block to cancel. /newline "+ ChatColor.DARK_GRAY + "(sorts everything as if 1 large chest)")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.COMPARATOR, 8),
                        new ItemStack(Material.REDSTONE, 32),
                        new ItemStack(Material.COMPARATOR, 8),
                        new ItemStack(Material.REDSTONE_TORCH, 8),
                        new ItemStack(Material.PAPER, 1),
                        new ItemStack(Material.REDSTONE_TORCH, 8),
                        new ItemStack(Material.COMPARATOR, 8),
                        new ItemStack(Material.REDSTONE, 32),
                        new ItemStack(Material.COMPARATOR, 8)), false, 1)));

        UberItems.putItem("cheat_code", new cheat_code(Material.STONE, "Cheat Code", UberRarity.EPIC,
                 false, false, false,
                Arrays.asList(new UberAbility("↑↑↓↓←→←→ⒷⒶ[start]", AbilityType.LEFT_CLICK, "Toggle creative mode"), new UberAbility("Game End", AbilityType.RIGHT_CLICK, "Instantly win the game")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64)), false, 1)));

        UberItems.putItem("escape_rope", new escape_rope(Material.LEAD, "Escape Rope", UberRarity.UNCOMMON,
                true, true, true,
                Collections.singletonList(new UberAbility("Round Trip!", AbilityType.RIGHT_CLICK, "Instantly teleport back to the last location with the sky visible")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.STRING),
                        new ItemStack(Material.SLIME_BALL),
                        new ItemStack(Material.STRING),
                        new ItemStack(Material.STRING),
                        new ItemStack(Material.ENDER_PEARL),
                        new ItemStack(Material.STRING),
                        new ItemStack(Material.STRING),
                        new ItemStack(Material.SLIME_BALL),
                        new ItemStack(Material.STRING)), false, 1)));

        UberItems.putItem("fireball", new fireball(Material.FIRE_CHARGE, "FireBall", UberRarity.UNCOMMON,
                  true, true, false,
                Collections.singletonList(new UberAbility("Throw em!", AbilityType.RIGHT_CLICK, "Throw a fireball which explodes on impact")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.TNT),
                        UberItems.getMaterial("flammable_substance").makeItem(1),
                        new ItemStack(Material.TNT),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.AIR)), false, 8)));

        UberItems.putItem("wrench", new wrench(Material.IRON_HOE, "Wrench", UberRarity.COMMON,
                  false, false, false,
                Collections.singletonList(new UberAbility("Tinker", AbilityType.RIGHT_CLICK, "Change the direction of certain blocks")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.PISTON),
                        new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("malk_bucket", new malk_bucket(Material.MILK_BUCKET, "Malk Bucket", UberRarity.RARE,
                  false, false, false,
                Arrays.asList(new UberAbility("Void Udder", AbilityType.NONE, "It's an infinite milk bucket!"), new UberAbility("Spiked Milk", AbilityType.NONE, "Place a potion onto this item to spike the milk! Spiked milk is still infinite, and also gives the potion effect!")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.GLOWSTONE_DUST),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.MILK_BUCKET),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("uncle_sams_wrath", new uncle_sams_wrath(Material.FIREWORK_ROCKET, ChatColor.RED + "Uncle " + ChatColor.WHITE + "Sam's " + ChatColor.AQUA + "Wrath", UberRarity.RARE,
                  false, false, false,
                Collections.singletonList(new UberAbility("July 4th", AbilityType.RIGHT_CLICK, "Shoot lethal fireworks at your enemies. MURCA")),
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.PAPER),
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.PAPER),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.PAPER),
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.PAPER),
                        UberItems.getMaterial("spark_dust").makeItem(1)), false, 1)));

        UberItems.putItem("electromagnet", new electromagnet(Material.IRON_INGOT, "ElectroMagnet", UberRarity.UNCOMMON,
                  false, false, true,
                Arrays.asList(new UberAbility("Magnetic Pull", AbilityType.NONE, "Attract dropped items from a radius of " + ChatColor.GREEN + "32" + ChatColor.GRAY + " blocks away."), new UberAbility("Force Field", AbilityType.NONE, "When held in the hand, repel hostile mobs and projectiles /newline " + ChatColor.DARK_GRAY + "(toggle with crouch + right click)")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.REDSTONE, 64),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.REDSTONE, 64),
                        new ItemStack(Material.IRON_BLOCK),
                        new ItemStack(Material.REDSTONE, 64),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.REDSTONE, 64),
                        new ItemStack(Material.PHANTOM_MEMBRANE)), false, 1)));

        UberItems.putItem("pocket_portal", new pocket_portal(Material.COMPASS, "Pocket Portal", UberRarity.RARE,
                  false, false, false,
                Collections.singletonList(new UberAbility("Beam me up Scotty!", AbilityType.RIGHT_CLICK, "Teleport to and from the nether", 120)),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(1),
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN),
                        new ItemStack(Material.OBSIDIAN)), false, 1)));

        UberItems.putItem("shooty_box", new shooty_box(Material.DISPENSER, "Shooty Box", UberRarity.MYTHIC,
                  false, false, false,
                Collections.singletonList(new UberAbility("Blunderbuss!", AbilityType.RIGHT_CLICK, "Shoot the contents of the box at high speed, like a handheld dispenser " + ChatColor.DARK_GRAY + "(open with crouch + right click)")),
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32)), false, 1)));

        UberItems.putItem("chisel", new chisel(Material.SHEARS, "Chisel", UberRarity.UNFINISHED,
                  false, false, false,
                Arrays.asList(new UberAbility("Transmutation", AbilityType.RIGHT_CLICK, "Use on a block to transmute it into a similar one"), new UberAbility("Machine Chisel", AbilityType.LEFT_CLICK, "Transmute many blocks at a time in your inventory")),
                null));

        UberItems.putItem("smart_pack", new smart_pack(Material.LIME_SHULKER_BOX, "Smart Pack", UberRarity.UNFINISHED,
                false, false, true,
                Arrays.asList(new UberAbility("Black Box", AbilityType.NONE, "Any items with special properties inside will retain their active effects"), new UberAbility("Automation", AbilityType.RIGHT_CLICK, "Activating the pack will activate items inside with a Right-Click ability")),
                null));

        UberItems.putItem("boom_stick", new boom_stick(Material.STICK, "BOOM Stick", UberRarity.LEGENDARY,
                false, false, false,
                Arrays.asList(new UberAbility("BOOM", AbilityType.RIGHT_CLICK, "Blow up all nearby enemies", 5), new UberAbility("Banish", AbilityType.LEFT_CLICK, "Punch your enemies into the shadow realm")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE),
                        new ItemStack(Material.PHANTOM_MEMBRANE)), false, 1)));

        UberItems.putItem("aspect_of_the_end", new aspect_of_the_end(Material.DIAMOND_SWORD, "Aspect Of The End", UberRarity.RARE,
                false, false, false,
                Collections.singletonList(new UberAbility("Instant Transmission", AbilityType.RIGHT_CLICK, "Teleport " + ChatColor.GREEN + "8 blocks" + ChatColor.GRAY + " ahead of you and gain " + ChatColor.GREEN + "+50 " + ChatColor.WHITE + "✦ Speed" + ChatColor.GRAY + " for " + ChatColor.GREEN + "3 seconds")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_ender_pearl").makeItem(16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.OBSIDIAN, 64),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("plumbers_sponge", new plumbers_sponge(Material.SPONGE, "Plumber's Sponge", UberRarity.UNCOMMON,
                true, true, true,
                Collections.singletonList(new UberAbility("Drain", AbilityType.RIGHT_CLICK, "Instructions: 1. Place on water. 2. Drains other water. 3. Double-bill client. " + ChatColor.DARK_GRAY + "Thanks Plumber Joe!")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CHORUS_FRUIT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CHORUS_FRUIT),
                        new ItemStack(Material.SPONGE, 8),
                        new ItemStack(Material.CHORUS_FRUIT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CHORUS_FRUIT),
                        new ItemStack(Material.AIR)), false, 4)));

        UberItems.putItem("grappling_hook", new grappling_hook(Material.FISHING_ROD, "Grappling Hook", UberRarity.UNCOMMON,
                false, false, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        UberItems.getMaterial("enchanted_string").makeItem(1),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_string").makeItem(1)), false, 1)));

        UberItems.putItem("ember_rod", new ember_rod(Material.BLAZE_ROD, "Ember Rod", UberRarity.EPIC,
                 false, false, false,
                Collections.singletonList(new UberAbility("Fire Blast!", AbilityType.RIGHT_CLICK, "Shoot 3 FireBalls in rapid succession in front of you!", 30)),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("flammable_substance").makeItem(32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BLAZE_ROD),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BLAZE_ROD),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("treecapitator", new treecapitator(Material.GOLDEN_AXE, "Treecapitator", UberRarity.EPIC,
                false, false, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.GOLD_BLOCK, 64),
                        new ItemStack(Material.GOLD_BLOCK, 64),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.GOLD_BLOCK, 64),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("world_eater", new world_eater(Material.DIAMOND_PICKAXE, "World Eater", UberRarity.LEGENDARY,
                false, false, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.getMaterial("enchanted_diamond").makeItem(1),
                        UberItems.getMaterial("enchanted_diamond").makeItem(1),
                        UberItems.getMaterial("enchanted_diamond").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BLAZE_ROD),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BLAZE_ROD),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("lightning_rod", new lightning_rod(Material.END_ROD, "Lightning Rod", UberRarity.EPIC,
                false, false, false,
                Collections.singletonList(new UberAbility("Smite", AbilityType.RIGHT_CLICK, "Strike lightning up to 150 blocks away")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_diamond").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("aspect_of_the_virus", new aspect_of_the_virus(Material.NETHERITE_SWORD, "Aspect of the Virus", UberRarity.MYTHIC,
                false, false, false,
                Collections.singletonList(new UberAbility("Shadow Death", AbilityType.RIGHT_CLICK, "While crouched, teleport behind the closest mob")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_diamond").makeItem(4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_diamond").makeItem(4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.NETHERITE_SWORD),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putItem("hackerman", new hackerman(Material.TOTEM_OF_UNDYING, "Hackerman", UberRarity.RARE,
                false, false, true,
                Collections.singletonList(new UberAbility("Scaffold!", AbilityType.RIGHT_CLICK, "banned")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.ENDER_EYE),
                        UberItems.getMaterial("enchanted_stone").makeItem(1),
                        new ItemStack(Material.ENDER_EYE),
                        new ItemStack(Material.LEVER),
                        new ItemStack(Material.TOTEM_OF_UNDYING),
                        new ItemStack(Material.STONE_BUTTON),
                        new ItemStack(Material.ENDER_EYE),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        new ItemStack(Material.ENDER_EYE)), false, 1)));
    }

    // register UberMaterials
    public static void registerUberMaterials() {
        UberItems.putMaterial("enchanted_cobblestone", new UberMaterial(Material.COBBLESTONE,
                "Enchanted Cobblestone", UberRarity.UNCOMMON, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR)), false, 1)));

        UberItems.putMaterial("enchanted_diamond", new UberMaterial(Material.DIAMOND,
                "Enchanted Diamond", UberRarity.UNCOMMON, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.DIAMOND, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.DIAMOND, 32),
                        new ItemStack(Material.DIAMOND, 32),
                        new ItemStack(Material.DIAMOND, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.DIAMOND, 32),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putMaterial("enchanted_stone", new UberMaterial(Material.STONE,
                "Enchanted Stone", UberRarity.RARE, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putMaterial("enchanted_ender_pearl", new UberMaterial(Material.ENDER_PEARL,
                "Enchanted Ender Pearl", UberRarity.UNCOMMON, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putMaterial("enchanted_string", new UberMaterial(Material.STRING,
                "Enchanted String", UberRarity.UNCOMMON, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16),
                        new ItemStack(Material.STRING, 16)), false, 1)));

        UberItems.putMaterial("spark_dust", new UberMaterial(Material.GUNPOWDER,
                "Spark Dust", UberRarity.COMMON, false, true, true,
                "This dangerous pile of explosives can be used in place of gunpowder when crafting",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.CHARCOAL),
                        new ItemStack(Material.FLINT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.SAND),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 10)));

        UberItems.putMaterial("flammable_substance", new UberMaterial(Material.FIRE_CHARGE,
                "Flammable Substance", UberRarity.UNCOMMON, false, true, true,
                "This cocktail can light ablaze at any moment",
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.getMaterial("spark_dust").makeItem(1),
                        new ItemStack(Material.COAL),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        UberItems.putMaterial("paper_fletching", new UberMaterial(Material.FEATHER,
        "Paper Fletching", UberRarity.COMMON, false, true, true,
                "Feathers are so 2010",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.PAPER),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.PAPER),
                        new ItemStack(Material.AIR)), false, 8)));

        UberItems.putMaterial("fools_gold", new UberMaterial(Material.GOLD_INGOT,
                "Fools Gold", UberRarity.RARE, false, true, true,
                "A speedrunner's best friend",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.REDSTONE),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));
    }
}
