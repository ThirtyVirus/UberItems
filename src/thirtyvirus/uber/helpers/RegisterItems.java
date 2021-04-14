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

        UberItems.putItem("builders_wand", new builders_wand(1, UberRarity.LEGENDARY, "Builder's Wand",
                Material.BLAZE_ROD,  false, false, true,
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
                        new ItemStack(Material.GOLD_BLOCK, 8)), false)));

        UberItems.putItem("lunch_box", new lunch_box(2, UberRarity.RARE, "Lunch Box",
                Material.CONDUIT,  false, false, false,
                Collections.singletonList(new UberAbility("Gluttony", AbilityType.NONE, "Automatically feeds you when hungry " + ChatColor.DARK_GRAY + "(drag & click food onto box to fill)")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.APPLE, 32),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.COOKED_COD, 2),
                        new ItemStack(Material.CHEST, 64),
                        new ItemStack(Material.COOKED_BEEF, 2),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.APPLE, 32),
                        new ItemStack(Material.ENDER_PEARL, 16)), false)));

        UberItems.putItem("document_of_order", new document_of_order(3, UberRarity.EPIC, "Document of Order",
                Material.PAPER, false, false, false,
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
                        new ItemStack(Material.COMPARATOR, 8)), false)));

        UberItems.putItem("cheat_code", new cheat_code(4, UberRarity.EPIC, "Cheat Code", Material.STONE, false, false, false, Arrays.asList(new UberAbility("↑↑↓↓←→←→ⒷⒶ[start]", AbilityType.LEFT_CLICK, "Toggle creative mode"), new UberAbility("Game End", AbilityType.RIGHT_CLICK, "Instantly win the game")), null));


        UberItems.putItem("escape_rope", new escape_rope(5, UberRarity.UNCOMMON,  "Escape Rope", Material.LEAD,true, true, true, Arrays.asList(new UberAbility("Round Trip!", AbilityType.RIGHT_CLICK, "Instantly teleport back to the last location with the sky visible")), null));
        UberItems.putItem("fireball", new fireball(6, UberRarity.UNCOMMON,  "FireBall", Material.FIRE_CHARGE,  true, true, false, Arrays.asList(new UberAbility("Throw em!", AbilityType.RIGHT_CLICK, "Throw a fireball which explodes on impact")), null));
        UberItems.putItem("wrench", new wrench(7, UberRarity.COMMON,   "Wrench", Material.IRON_HOE,  false, false, false, Arrays.asList(new UberAbility("Tinker", AbilityType.RIGHT_CLICK, "Change the direction of certain blocks")), null));
        UberItems.putItem("malk_bucket", new malk_bucket(8, UberRarity.RARE,  "Malk Bucket", Material.MILK_BUCKET,  false, false, false, Arrays.asList(new UberAbility("Void Udder", AbilityType.NONE, "It's an infinite milk bucket!"), new UberAbility("Spiked Milk", AbilityType.NONE, "Place a potion onto this item to spike the milk! Spiked milk is still infinite, but also gives the potion effect")), null));
        UberItems.putItem("uncle_sams_wrath", new uncle_sams_wrath(9, UberRarity.RARE, ChatColor.RED + "Uncle " + ChatColor.WHITE + "Sam's " + ChatColor.AQUA + "Wrath", Material.FIREWORK_ROCKET,  false, false, false, Arrays.asList(new UberAbility("July 4th", AbilityType.RIGHT_CLICK, "Shoot lethal fireworks at your enemies. MURCA")), null));
        UberItems.putItem("electromagnet", new electromagnet(10, UberRarity.UNCOMMON,   "ElectroMagnet", Material.IRON_INGOT,  false, false, true, Arrays.asList(new UberAbility("Magnetic Pull", AbilityType.NONE, "Attract dropped items from a radius of " + ChatColor.GREEN + "32" + ChatColor.GRAY + " blocks away."), new UberAbility("Force Field", AbilityType.NONE, "When held in the hand, repel hostile mobs and projectiles /newline " + ChatColor.DARK_GRAY + "(toggle with crouch + right click)")), null));
        UberItems.putItem("pocket_portal", new pocket_portal(11, UberRarity.RARE,  "Pocket Portal", Material.COMPASS,  false, false, false, Arrays.asList(new UberAbility("Beam me up Scotty!", AbilityType.RIGHT_CLICK, "Teleport to and from the nether", 120)), null));
        UberItems.putItem("shooty_box", new shooty_box(12, UberRarity.MYTHIC,   "Shooty Box", Material.DISPENSER,  false, false, false, Arrays.asList(new UberAbility("Blunderbuss!", AbilityType.RIGHT_CLICK, "Shoot the contents of the box at high speed, like a handheld dispenser " + ChatColor.DARK_GRAY + "(open with crouch + right click)")),
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32)), false)));

        UberItems.putItem("chisel", new chisel(13, UberRarity.UNFINISHED,   "Chisel", Material.SHEARS,  false, false, false, Arrays.asList(new UberAbility("Transmutation", AbilityType.RIGHT_CLICK, "Use on a block to transmute it into a similar one"), new UberAbility("Machine Chisel", AbilityType.LEFT_CLICK, "Transmute many blocks at a time in your inventory")), null));
        UberItems.putItem("smart_pack", new smart_pack(14, UberRarity.UNFINISHED,  "Smart Pack", Material.LIME_SHULKER_BOX,  false, false, true, Arrays.asList(new UberAbility("Black Box", AbilityType.NONE, "Any items with special properties inside will retain their active effects"), new UberAbility("Automation", AbilityType.RIGHT_CLICK, "Activating the pack will activate items inside with a Right-Click ability")), null));
        UberItems.putItem("boom_stick", new boom_stick(15, UberRarity.LEGENDARY, "BOOM Stick", Material.STICK,  false, false, false, Arrays.asList(new UberAbility("BOOM", AbilityType.RIGHT_CLICK, "Blow up all nearby enemies", 5), new UberAbility("Banish", AbilityType.LEFT_CLICK, "Punch your enemies into the shadow realm")), null));
        UberItems.putItem("aspect_of_the_end", new aspect_of_the_end(16, UberRarity.RARE, "Aspect Of The End", Material.DIAMOND_SWORD, false, false, false, Arrays.asList(new UberAbility("Instant Transmission", AbilityType.RIGHT_CLICK, "Teleport " + ChatColor.GREEN + "8 blocks" + ChatColor.GRAY + " ahead of you and gain " + ChatColor.GREEN + "+50 " + ChatColor.WHITE + "✦ Speed" + ChatColor.GRAY + " for " + ChatColor.GREEN + "3 seconds")), null));
        UberItems.putItem("plumbers_sponge", new plumbers_sponge(17, UberRarity.UNCOMMON, "Plumber's Sponge", Material.SPONGE,  true, true, true, Arrays.asList(new UberAbility("Drain", AbilityType.RIGHT_CLICK, "Instructions: 1. Place on water. 2. Drains other water. 3. Double-bill client. " + ChatColor.DARK_GRAY + "Thanks Plumber Joe!")), null));
        UberItems.putItem("grappling_hook", new grappling_hook(18, UberRarity.UNCOMMON, "Grappling Hook", Material.FISHING_ROD,  false, false, false, Arrays.asList(), null));
        UberItems.putItem("ember_rod", new ember_rod(19, UberRarity.EPIC, "Ember Rod", Material.BLAZE_ROD,  false, false, false, Arrays.asList(new UberAbility("Fire Blast!", AbilityType.RIGHT_CLICK, "Shoot 3 FireBalls in rapid succession in front of you!", 30)), null));
        UberItems.putItem("treecapitator", new treecapitator(20, UberRarity.EPIC, "Treecapitator", Material.GOLDEN_AXE,  false, false, false, Arrays.asList(), null));
        UberItems.putItem("world_eater", new world_eater(21, UberRarity.LEGENDARY, "World Eater", Material.DIAMOND_PICKAXE, false, false, false, Arrays.asList(), null));
        UberItems.items.get("world_eater").setCraftingRecipe(new UberCraftingRecipe(Arrays.asList(new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.AIR), new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.AIR)), false));
        UberItems.putItem("lightning_rod", new lightning_rod(22, UberRarity.EPIC, "Lightning Rod", Material.END_ROD, false, false, false, Arrays.asList(new UberAbility("Smite", AbilityType.RIGHT_CLICK, "Strike lightning up to 150 blocks away")), null));
        UberItems.putItem("aspect_of_the_virus", new aspect_of_the_virus(23, UberRarity.MYTHIC, "Aspect of the Virus", Material.NETHERITE_SWORD, false, false, false, Arrays.asList(new UberAbility("Shadow Death", AbilityType.RIGHT_CLICK, "While crouched, teleport behind the closest mob")), null));
        UberItems.putItem("hackerman", new hackerman(24, UberRarity.RARE, "Hackerman", Material.TOTEM_OF_UNDYING, false, false, true, Arrays.asList(new UberAbility("Scaffold!", AbilityType.RIGHT_CLICK, "banned")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.ENDER_EYE),
                        UberItems.materials.get("enchanted_stone").makeItem(1),
                        new ItemStack(Material.ENDER_EYE),
                        new ItemStack(Material.LEVER),
                        new ItemStack(Material.TOTEM_OF_UNDYING),
                        new ItemStack(Material.STONE_BUTTON),
                        new ItemStack(Material.ENDER_EYE),
                        new ItemStack(Material.REDSTONE_BLOCK),
                        new ItemStack(Material.ENDER_EYE)), false)));

    }

    // register UberMaterials
    public static void registerUberMaterials() {
        UberItems.putMaterial("enchanted_cobblestone", new UberMaterial(Material.COBBLESTONE,
                "Enchanted Cobblestone", UberRarity.UNCOMMON, true, true,1, false,
                new UberCraftingRecipe(Arrays.asList(
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR),
                    new ItemStack(Material.COBBLESTONE, 32),
                    new ItemStack(Material.AIR)), false)));
        UberItems.putMaterial("enchanted_stone", new UberMaterial(Material.STONE,
                "Enchanted Stone", UberRarity.RARE, true, true,1, false,
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR),
                        UberItems.materials.get("enchanted_cobblestone").makeItem(32),
                        new ItemStack(Material.AIR)), false)));
        UberItems.putMaterial("spark_dust", new UberMaterial(Material.GUNPOWDER,
                "Spark Dust", UberRarity.COMMON, false, true,10, true,
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.CHARCOAL),
                        new ItemStack(Material.FLINT),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.SAND),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)
                ), false)));
        UberItems.putMaterial("flammable_substance", new UberMaterial(Material.FIRE_CHARGE,
                "Flammable Substance", UberRarity.UNCOMMON, false, true, 1, true,
                new UberCraftingRecipe(Arrays.asList(
                        UberItems.materials.get("spark_dust").makeItem(1),
                        new ItemStack(Material.COAL),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)
                ), false)));
        UberItems.putMaterial("paper_fletching", new UberMaterial(Material.FEATHER,
        "Paper Fletching", UberRarity.COMMON, false, true, 8, true,
        new UberCraftingRecipe(Arrays.asList(
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.PAPER),
                new ItemStack(Material.AIR),
                new ItemStack(Material.AIR),
                new ItemStack(Material.PAPER),
                new ItemStack(Material.AIR)
        ), false)));
        UberItems.putMaterial("fools_gold", new UberMaterial(Material.GOLD_INGOT,
                "Fools Gold", UberRarity.RARE, false, true, 1, true,
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.REDSTONE),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)
                ), false)));
    }
}
