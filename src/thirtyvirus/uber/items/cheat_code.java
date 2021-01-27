package thirtyvirus.uber.items;

import java.util.List;

import net.minecraft.server.v1_16_R1.PacketPlayOutGameStateChange;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberRarity;

// a template class that can be copy - pasted and renamed when making new Uber Items
public class cheat_code extends UberItem {

    public cheat_code(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities) {
        super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public void leftClickAirAction(Player player, ItemStack item) {
        if (player.getGameMode() == GameMode.CREATIVE) player.setGameMode(GameMode.SURVIVAL);
        else player.setGameMode(GameMode.CREATIVE);
        onItemUse(player, item); // confirm that the item's ability has been successfully used
    }
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { leftClickAirAction(player, item); }

    public void rightClickAirAction(Player player, ItemStack item) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(new PacketPlayOutGameStateChange.a(4), 1);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        onItemUse(player, item); // confirm that the item's ability has been successfully used
    }
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { rightClickAirAction(player, item); }

    public void shiftLeftClickAirAction(Player player, ItemStack item) { }
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void shiftRightClickAirAction(Player player, ItemStack item) { }
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }
    public void middleClickAction(Player player, ItemStack item) { }
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) { }
    public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
    public void clickedInInventoryAction(Player player, InventoryClickEvent event) { }
    public void activeEffect(Player player, ItemStack item) { }
}