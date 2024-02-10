package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.items.*;

import java.util.Arrays;
import java.util.Collections;

public class RegisterItems {

    // register UberItems
    public static void registerUberItems() {
        // UberItems.putItem("NAME", new UberItemTemplate(0, UberRarity.UNFINISHED, "NAME", Material.STONE, false, false, false, Arrays.asList(new UberAbility("Ability name!", AbilityType.RIGHT_CLICK, "Ability description")), null), true);

        //UberItems.putUberArmorSet(infinity_armor.class, "Infinity", UberRarity.SPECIAL, UberItems.ArmorType.LEATHER, Color.fromRGB(252, 186, 3),
        //        Collections.singletonList(new UberAbility("Inevitable", AbilityType.FULL_SET_BONUS, "This does put a smile on my face")),
        //        Utilities.getSkull("http://textures.minecraft.net/texture/eb9e6815d741fa1f87389027c357abaae9eeada173df89bbac96dfc8b9d6a3c9"),
        //        null,null,null,null,null,null,null);

        putDefaultItem("cheat_code", new cheat_code(Material.STONE, "Cheat Code", UberRarity.ADMIN,
                false, false, false,
                Arrays.asList(new UberAbility("Konami Code", AbilityType.LEFT_CLICK, "Toggle creative mode"), new UberAbility("P2W", AbilityType.RIGHT_CLICK, "Heal and feed yourself instantly")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        UberItems.getMaterial("creative_core").makeItem(1),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64),
                        new ItemStack(Material.DIAMOND_BLOCK, 64)), false, 1)));

        putDefaultItem("monster_eraser", new monster_eraser(Material.STICK, "Monster Eraser", UberRarity.ADMIN,
                false, false, false,
                Collections.singletonList(new UberAbility("Erase", AbilityType.RIGHT_CLICK, "Point at Monster, Right-Click, Erase from Existence")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK, 1),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("creative_core").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultItem("lunch_box", new lunch_box(Material.CONDUIT, "Lunch Box", UberRarity.RARE,
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

        putDefaultItem("document_of_order", new document_of_order(Material.PAPER, "Document of Order", UberRarity.EPIC,
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

        putDefaultItem("escape_rope", new escape_rope(Material.LEAD, "Escape Rope", UberRarity.UNCOMMON,
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

        putDefaultItem("fireball", new fireball(Material.FIRE_CHARGE, "FireBall", UberRarity.UNCOMMON,
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

        putDefaultItem("wrench", new wrench(Material.IRON_HOE, "Wrench", UberRarity.COMMON,
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

        putDefaultItem("malk_bucket", new malk_bucket(Material.MILK_BUCKET, "Malk Bucket", UberRarity.RARE,
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

        putDefaultItem("uncle_sams_wrath", new uncle_sams_wrath(Material.FIREWORK_ROCKET, ChatColor.RED + "Uncle " + ChatColor.WHITE + "Sam's " + ChatColor.AQUA + "Wrath", UberRarity.RARE,
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

        putDefaultItem("electromagnet", new electromagnet(Material.IRON_INGOT, "ElectroMagnet", UberRarity.UNCOMMON,
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

        putDefaultItem("pocket_portal", new pocket_portal(Material.COMPASS, "Pocket Portal", UberRarity.RARE,
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

        putDefaultItem("shooty_box", new shooty_box(Material.DISPENSER, "Shooty Box", UberRarity.MYTHIC,
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

        putDefaultItem("chisel", new chisel(Material.SHEARS, "Chisel", UberRarity.UNFINISHED,
                  false, false, false,
                Arrays.asList(new UberAbility("Transmutation", AbilityType.RIGHT_CLICK, "Use on a block to transmute it into a similar one"), new UberAbility("Machine Chisel", AbilityType.LEFT_CLICK, "Transmute many blocks at a time in your inventory")),
                null));

        putDefaultItem("boom_stick", new boom_stick(Material.STICK, "BOOM Stick", UberRarity.LEGENDARY,
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

        putDefaultItem("world_eater", new world_eater(Material.DIAMOND_PICKAXE, "World Eater", UberRarity.LEGENDARY,
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
                        new ItemStack(Material.AIR)), false, 1), "PICKAXE"));

        putDefaultItem("lightning_rod", new lightning_rod(Material.END_ROD, "Lightning Rod", UberRarity.EPIC,
                false, false, false,
                Collections.singletonList(new UberAbility("Smite", AbilityType.RIGHT_CLICK, "Strike lightning up to 150 blocks away")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        UberItems.getMaterial("enchanted_diamond").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.AIR),
                        UberItems.getItem("boom_stick").makeItem(1),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultItem("aspect_of_the_virus", new aspect_of_the_virus(Material.DIAMOND_SWORD, "Aspect of the Virus", UberRarity.MYTHIC,
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
                        new ItemStack(Material.DIAMOND_SWORD),
                        new ItemStack(Material.AIR)), false, 1), "SWORD"));

        putDefaultItem("hackerman", new hackerman(Material.TOTEM_OF_UNDYING, "Hackerman", UberRarity.RARE,
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

        // TODO add Atlas

        putDefaultItem("pillow", new pillow(Utilities.getSkull("http://textures.minecraft.net/texture/3088895e90ea5899499e32a3176ecdacd88656cba734d1345175fd11f2844893"), "Pillow",
                UberRarity.COMMON, false, false, true,
                Collections.singletonList(new UberAbility("Well Rested", AbilityType.NONE, "When sleeping in a bed while holding this pillow, you regain health. Cure any active poison effects.", 300)),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.STRING, 8),
                        new ItemStack(Material.FEATHER, 16),
                        new ItemStack(Material.STRING, 8),
                        new ItemStack(Material.FEATHER, 16),
                        new ItemStack(Material.FEATHER, 16),
                        new ItemStack(Material.FEATHER, 16),
                        new ItemStack(Material.STRING, 8),
                        new ItemStack(Material.FEATHER, 16),
                        new ItemStack(Material.STRING, 8)), false, 1)));

        putDefaultItem("omlette", new omlette(Utilities.getSkull("http://textures.minecraft.net/texture/4e947fe09ebb7e7b3769bb8a5da5cb734646b2c7973ab6a35b6627a7dc1245d1"), "Omlette",
                UberRarity.UNCOMMON, true, true, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.EGG, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.EGG, 4),
                        UberItems.getMaterial("flammable_substance").makeItem(1),
                        new ItemStack(Material.EGG, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.EGG, 4),
                        new ItemStack(Material.AIR)), false, 4)));

        putDefaultItem("experience_bottle", new experience_bottle(Material.EXPERIENCE_BOTTLE, "Experience Bottle", UberRarity.COMMON,
                true, true, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.LAPIS_LAZULI),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.LAPIS_LAZULI),
                        new ItemStack(Material.GLASS_BOTTLE),
                        new ItemStack(Material.LAPIS_LAZULI),
                        new ItemStack(Material.LAPIS_LAZULI),
                        new ItemStack(Material.LAPIS_LAZULI),
                        new ItemStack(Material.LAPIS_LAZULI)), false, 1)));

        // TODO add way to convert enchantments into experience bottles?

        // TODO add livna (reverse anvil)

        putDefaultItem("small_backpack", new small_backpack(Utilities.getSkull("http://textures.minecraft.net/texture/2308bf5cc3e9decaf0770c3fdad1e042121cf39cc2505bbb866e18c6d23ccd0c"), "Small Backpack",
                UberRarity.RARE, false, false, false,
                Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        UberItems.getMaterial("enchanted_leather").makeItem(1),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8),
                        new ItemStack(Material.WHITE_WOOL, 8)), false, 1)));

        putDefaultItem("big_backpack", new big_backpack(Utilities.getSkull("http://textures.minecraft.net/texture/a2bb38516b29504186e11559cd5250ae218db4ddd27ae438726c847ce6b3c98"), "Big Backpack",
                UberRarity.EPIC, false, false, false,
                Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.WHITE_WOOL, 64),
                        UberItems.getMaterial("enchanted_leather").makeItem(1),
                        new ItemStack(Material.WHITE_WOOL, 64),
                        UberItems.getMaterial("enchanted_leather").makeItem(1),
                        new ItemStack(Material.CHEST),
                        UberItems.getMaterial("enchanted_leather").makeItem(1),
                        new ItemStack(Material.WHITE_WOOL, 64),
                        UberItems.getMaterial("enchanted_leather").makeItem(1),
                        new ItemStack(Material.WHITE_WOOL, 64)), false, 1)));

        putDefaultItem("kebab", new kebab(Utilities.getSkull("https://textures.minecraft.net/texture/31e9359292be8de0e6467bba69260c4c7a8c1bfc08ae09bae78b6fcd612b7ca3"), "Kebab",
                UberRarity.RARE, true, true, false,
                Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.COOKED_COD),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.COOKED_MUTTON),
                        new ItemStack(Material.COOKED_BEEF),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.COOKED_PORKCHOP),
                        new ItemStack(Material.COOKED_RABBIT),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.COOKED_CHICKEN)), false, 3)));

        putDefaultItem("calamari", new calamari(Material.COOKED_COD, "Calamari",
                UberRarity.UNCOMMON, true, true, false, Collections.emptyList(), null));

        putDefaultItem("homemade_portal_frame", new homemade_portal_frame(Material.END_PORTAL_FRAME, "Homemade Portal Frame",
                UberRarity.EPIC, true, true, false,
                Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.END_STONE, 12),
                        new ItemStack(Material.END_STONE, 12),
                        new ItemStack(Material.END_STONE, 12),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.NETHER_STAR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.END_STONE, 12),
                        new ItemStack(Material.END_STONE, 12),
                        new ItemStack(Material.END_STONE, 12)), false, 12)));

        putDefaultItem("crystal_ball", new crystal_ball(Utilities.getSkull("http://textures.minecraft.net/texture/5a5f29a76d1f91c165f63baac048670e7b1d37ce785a4d9c21d8c3a177b5"), "Crystal Ball",
                UberRarity.EPIC, false, false, false,
                Arrays.asList(new UberAbility("Mana Battery", AbilityType.LEFT_CLICK, "Deposit an experience level into the crystal ball, crouch to deposit all."),
                        new UberAbility("Mana Discharge", AbilityType.RIGHT_CLICK, "Withdraw an experience level from the crystal ball, crouch to withdraw all.")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.GLASS),
                        new ItemStack(Material.SEA_LANTERN),
                        new ItemStack(Material.GLASS),
                        new ItemStack(Material.SEA_LANTERN),
                        new ItemStack(Material.BEACON),
                        new ItemStack(Material.SEA_LANTERN),
                        new ItemStack(Material.GLASS),
                        new ItemStack(Material.SEA_LANTERN),
                        new ItemStack(Material.GLASS)), false, 1)));

        putDefaultItem("soul_anchor", new soul_anchor(Utilities.getSkull("https://textures.minecraft.net/texture/38be8abd66d09a58ce12d377544d726d25cad7e979e8c2481866be94d3b32f"), "Soul Anchor",
                UberRarity.LEGENDARY, false, true, false,
                Arrays.asList(new UberAbility("Soulbound", AbilityType.NONE, "Stores location on death, returns to the player inventory after respawn"),
                        new UberAbility("Do-Over", AbilityType.RIGHT_CLICK, "Return to your previous death point")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.CHORUS_FRUIT),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENDER_PEARL, 16),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultItem("multi_bench", new multi_bench(Material.OBSIDIAN, "Multi-Bench",
                UberRarity.UNFINISHED, false, false, false,
                Collections.singletonList(new UberAbility("Ultimate Multitool", AbilityType.LEFT_CLICK, "Swap between various utility blocks")),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.OBSIDIAN, 16),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.OBSIDIAN, 16),
                        new ItemStack(Material.REDSTONE_BLOCK, 32),
                        new ItemStack(Material.OBSIDIAN, 16),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.OBSIDIAN, 16),
                        new ItemStack(Material.STICK)), false, 1)));

        putDefaultItem("throwing_torch", new throwing_torch(Material.TORCH, "Throwing Torch", UberRarity.COMMON,
                true, true, false, Collections.emptyList(),
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.TORCH),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.STICK),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.AIR)), false, 1)));

        // TODO add jerky?

        // TODO add go-cart

        // TODO add hang glider

        // TODO add metal detector

    }

    // register UberMaterials
    public static void registerUberMaterials() {
        putDefaultMaterial("creative_core", new UberMaterial(Utilities.getSkull("http://textures.minecraft.net/texture/faff2eb498e5c6a04484f0c9f785b448479ab213df95ec91176a308a12add70"),
                "Creative Core", UberRarity.VERY_SPECIAL, false, false, false, "", null));

        putDefaultMaterial("defeated_player_head", new UberMaterial(Utilities.getSkull("http://textures.minecraft.net/texture/44eaa0ddf7603ec5581af5023c3f0057d933863d7c5a3abc8975d7b50d7bae6e"),
                "Player Head", UberRarity.LEGENDARY, false, false, false, "", null));

        putDefaultMaterial("enchanted_cobblestone", new UberMaterial(Material.COBBLESTONE,
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
                    new ItemStack(Material.AIR)), true, 1)));

        putDefaultMaterial("enchanted_diamond", new UberMaterial(Material.DIAMOND,
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
                        new ItemStack(Material.AIR)), true, 1)));

        putDefaultMaterial("enchanted_stone", new UberMaterial(Material.STONE,
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
                        new ItemStack(Material.AIR)), true, 1)));

        putDefaultMaterial("enchanted_ender_pearl", new UberMaterial(Material.ENDER_PEARL,
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
                        new ItemStack(Material.AIR)), true, 1)));

        putDefaultMaterial("enchanted_string", new UberMaterial(Material.STRING,
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
                        new ItemStack(Material.STRING, 16)), true, 1)));

        putDefaultMaterial("spark_dust", new UberMaterial(Material.GUNPOWDER,
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
                        new ItemStack(Material.AIR)), true, 10)));

        putDefaultMaterial("flammable_substance", new UberMaterial(Material.FIRE_CHARGE,
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
                        new ItemStack(Material.AIR)), true, 1)));

        putDefaultMaterial("paper_fletching", new UberMaterial(Material.FEATHER,
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

        putDefaultMaterial("fools_gold", new UberMaterial(Material.GOLD_INGOT,
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

        putDefaultMaterial("enchanted_crafting_table", new UberMaterial(Material.CRAFTING_TABLE, "Enchanted Crafting Table", UberRarity.UNCOMMON, true, false, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CRAFTING_TABLE, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CRAFTING_TABLE, 32),
                        new ItemStack(Material.CRAFTING_TABLE, 32),
                        new ItemStack(Material.CRAFTING_TABLE, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.CRAFTING_TABLE, 32),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultMaterial("enchanted_furnace", new UberMaterial(Material.FURNACE, "Enchanted Furnace", UberRarity.UNCOMMON, true, false, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.FURNACE, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.FURNACE, 32),
                        new ItemStack(Material.FURNACE, 32),
                        new ItemStack(Material.FURNACE, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.FURNACE, 32),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultMaterial("enchanted_brewing_stand", new UberMaterial(Material.BREWING_STAND, "Enchanted Brewing Stand", UberRarity.UNCOMMON, true, false, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BREWING_STAND, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BREWING_STAND, 32),
                        new ItemStack(Material.BREWING_STAND, 32),
                        new ItemStack(Material.BREWING_STAND, 32),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.BREWING_STAND, 32),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultMaterial("enchanted_anvil", new UberMaterial(Material.ANVIL, "Enchanted Anvil", UberRarity.UNCOMMON, true, false, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ANVIL, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ANVIL, 4),
                        new ItemStack(Material.ANVIL, 4),
                        new ItemStack(Material.ANVIL, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ANVIL, 4),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultMaterial("enchanted_enchanting_table", new UberMaterial(Material.ENCHANTING_TABLE, "Enchanted Enchanting Table", UberRarity.UNCOMMON, true, false, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENCHANTING_TABLE, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENCHANTING_TABLE, 4),
                        new ItemStack(Material.ENCHANTING_TABLE, 4),
                        new ItemStack(Material.ENCHANTING_TABLE, 4),
                        new ItemStack(Material.AIR),
                        new ItemStack(Material.ENCHANTING_TABLE, 4),
                        new ItemStack(Material.AIR)), false, 1)));

        putDefaultMaterial("enchanted_leather", new UberMaterial(Material.LEATHER, "Enchanted Leather", UberRarity.UNCOMMON, true, true, false, "",
                new UberCraftingRecipe(Arrays.asList(
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64),
                        new ItemStack(Material.LEATHER, 64)), false, 1)));
    }

    // put UberItem or UberMaterial, but also add to default_items / default_materials
    private static void putDefaultItem(String name, UberItem item) {
        UberItems.putItem(name, item);
        UberItems.default_items.add(item.getUUID());
    }
    private static void putDefaultMaterial(String name, UberMaterial material) {
        UberItems.putMaterial(name, material);
        UberItems.default_materials.add(material.getUUID());
    }
}