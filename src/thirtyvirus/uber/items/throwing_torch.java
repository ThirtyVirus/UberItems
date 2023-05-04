package thirtyvirus.uber.items;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.*;

public class throwing_torch extends UberItem {

    private static final Set<Material> TRANSPARENT = EnumSet.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR);

    public throwing_torch(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) {
        lore.add(ChatColor.GRAY + "a throw-able torch!");
    }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean rightClickAirAction(Player player, ItemStack item) {

        FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation().add(0,1,0), Bukkit.createBlockData(Material.TORCH));
        block.setDropItem(false);
        block.setVelocity(player.getEyeLocation().add(0, 1, 0).getDirection().multiply(1.5));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, 1, 1);
        Utilities.tagEntity(block, "asd", "throwntorch");
        processFallingTorch(block);
        return true;

    }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return rightClickAirAction(player, item); }

    public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }
    public boolean shiftRightClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { return false; }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }
    public boolean activeEffect(Player player, ItemStack item) { return false; }

    private void processFallingTorch(FallingBlock block) {
        Utilities.scheduleTask(()->{
            Block landingBlock = block.getWorld().getBlockAt(block.getLocation());

            if (block.isDead()) {
                if (TRANSPARENT.contains(landingBlock.getType())) {
                    landingBlock.setType(Material.TORCH);
                }
            }
            else if (landingBlock.getRelative(BlockFace.NORTH).getType().isSolid()) {
                landingBlock.setType(Material.WALL_TORCH);
                Directional data = (Directional) landingBlock.getBlockData();
                data.setFacing(BlockFace.SOUTH);
                landingBlock.setBlockData(data);
                block.remove();
            }
            else if (landingBlock.getRelative(BlockFace.SOUTH).getType().isSolid()) {
                landingBlock.setType(Material.WALL_TORCH);
                Directional data = (Directional) landingBlock.getBlockData();
                data.setFacing(BlockFace.NORTH);
                landingBlock.setBlockData(data);
                block.remove();
            }
            else if (landingBlock.getRelative(BlockFace.EAST).getType().isSolid()) {
                landingBlock.setType(Material.WALL_TORCH);
                Directional data = (Directional) landingBlock.getBlockData();
                data.setFacing(BlockFace.WEST);
                landingBlock.setBlockData(data);
                block.remove();
            }
            else if (landingBlock.getRelative(BlockFace.WEST).getType().isSolid()) {
                landingBlock.setType(Material.WALL_TORCH);
                Directional data = (Directional) landingBlock.getBlockData();
                data.setFacing(BlockFace.EAST);
                landingBlock.setBlockData(data);
                block.remove();
            }
            else processFallingTorch(block);
        }, 2);
    }

}
