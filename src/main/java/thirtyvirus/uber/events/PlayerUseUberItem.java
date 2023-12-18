package thirtyvirus.uber.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.MainHand;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.UberMaterial;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.MenuUtils;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static thirtyvirus.uber.helpers.MenuUtils.createBoundCraftingTutorialMenu;

public class PlayerUseUberItem implements Listener {

    // handle equipping an UberItem helmet
    @EventHandler
    private void onRightClickHelmet(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || (event.getAction() != Action.RIGHT_CLICK_AIR ) && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem(); ItemStack helmet = event.getPlayer().getInventory().getHelmet();
        if (Utilities.getIntFromItem(item, "uberhelmet") == 1 && (helmet == null || helmet.getType() == Material.AIR)) {

            event.getPlayer().getInventory().setHelmet(item);
            Utilities.scheduleTask(()->event.getPlayer().getInventory().remove(event.getPlayer().getInventory().getItemInMainHand()), 1);

            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1, 1);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onPlayerUse(PlayerInteractEvent event) {

        // test if items in main and off hand are UberItems
        if (event.getHand() == EquipmentSlot.HAND && Utilities.isUber(event.getPlayer().getInventory().getItemInMainHand())) {
            useUberItem(event, event.getPlayer().getInventory().getItemInMainHand());
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND && Utilities.isUber(event.getPlayer().getInventory().getItemInOffHand())) {
            useUberItem(event, event.getPlayer().getInventory().getItemInOffHand());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onPlayerHit(EntityDamageByEntityEvent event) {

        // enforce that only players can do damage (TODO support projectile hits from uber items in the future)
        if (event.getDamager().getType() != EntityType.PLAYER) return;

        // process player hitting entity with an uber item
        Player player = (Player)event.getDamager();

        ItemStack mainhand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (Utilities.isUber(mainhand)) {
            UberItem uber = Utilities.getUber(mainhand);
            if (uber != null) {
                // enforce premium vs lite, item rarity perms, item specific perms
                if (Utilities.enforcePermissions(player, uber)) return;

                if (uber.hitEntityAction(player, event, event.getEntity(), mainhand)) uber.onItemUse(player, mainhand);
            }
        }
        if (Utilities.isUber(offhand)) {
            UberItem uber = Utilities.getUber(offhand);
            if (uber != null) {
                // enforce premium vs lite, item rarity perms, item specific perms
                if (Utilities.enforcePermissions(player, uber)) return;

                if (uber.hitEntityAction(player, event, event.getEntity(), offhand)) uber.onItemUse(player, offhand);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // test if item in main hand is an UberItem
        if (Utilities.isUber(item)) {
            UberItem uber = Utilities.getUber(item);
            if (uber != null) {
                // enforce premium vs lite, item rarity perms, item specific perms
                if (Utilities.enforcePermissions(player, uber)) return;

                if (uber.breakBlockAction(player, event, event.getBlock(), item)) uber.onItemUse(player, item);
            }
        }
        else {
            // determine whether or not a player is trying to make an Uber Workbench
            // breaking a workbench with a lever to make the table, and delete the components
            if (item.getType() == Material.LEVER && event.getBlock().getType() == Material.CRAFTING_TABLE) {
                if (player.getGameMode() != GameMode.CREATIVE)
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                event.setDropItems(false);
                Utilities.givePlayerItemSafely(player, UberItems.getItem("uber_workbench").makeItem(1));
                Utilities.playSound(ActionSound.POP, player);
            }
        }

    }

    // handle interaction events (air and blocks)
    private void useUberItem(PlayerInteractEvent event, ItemStack item) {
        Player player = event.getPlayer();
        UberItem uber = Utilities.getUber(item);
        if (uber == null) return;

        // enforce premium vs lite, item rarity perms, item specific perms
        if (Utilities.enforcePermissions(player, uber)) return;

        // air and block interaction
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (!player.isSneaking()) { if (uber.leftClickAirAction(player, item)) uber.onItemUse(player, item); }
            else { if (uber.shiftLeftClickAirAction(player, item)) uber.onItemUse(player, item); }
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!player.isSneaking()) { if (uber.leftClickBlockAction(player, event, event.getClickedBlock(), item)) uber.onItemUse(player, item); }
            else { if (uber.shiftLeftClickBlockAction(player, event, event.getClickedBlock(), item)) uber.onItemUse(player, item); }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!player.isSneaking()) { if (uber.rightClickAirAction(player, item)) uber.onItemUse(player, item); }
            else { if (uber.shiftRightClickAirAction(player, item)) uber.onItemUse(player, item); }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!player.isSneaking()) { if (uber.rightClickBlockAction(player, event, event.getClickedBlock(), item)) uber.onItemUse(player, item); }
            else { if (uber.shiftRightClickBlockAction(player, event, event.getClickedBlock(), item)) uber.onItemUse(player, item); }
        }
    }

    @EventHandler
    private void checkUberMaterialRecipes(PlayerInteractEvent event) {
        if (!Utilities.isUberMaterial(event.getItem())) return;
        UberMaterial tested = Utilities.getUberMaterial(event.getItem());

        MenuUtils.checkMaterialRecipeUsage(event.getPlayer(), tested);
    }

}