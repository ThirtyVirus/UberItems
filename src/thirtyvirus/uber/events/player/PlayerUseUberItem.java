package thirtyvirus.uber.events.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.ActionSound;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

import java.util.Arrays;

public class PlayerUseUberItem implements Listener {

    UberItems main;
    public PlayerUseUberItem(UberItems main) { this.main = main; }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {

        // test if items in main and off hand are UberItems
        if (Utilities.isUber(event.getPlayer().getInventory().getItemInMainHand())) {
            useUberItem(event, event.getPlayer().getInventory().getItemInMainHand());
        }
        if (Utilities.isUber(event.getPlayer().getInventory().getItemInOffHand())) {
            useUberItem(event, event.getPlayer().getInventory().getItemInOffHand());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {

        // enforce that only players can do damage (TODO support projectile hits from uber items in the future)
        if (event.getDamager().getType() != EntityType.PLAYER) return;

        // process player hitting entity with an uber item
        Player player = (Player)event.getDamager();

        ItemStack mainhand = player.getInventory().getItemInMainHand();
        ItemStack offhand = player.getInventory().getItemInOffHand();
        if (Utilities.isUber(mainhand)) {
            Utilities.getUber(mainhand).hitEntityAction(player, event, event.getEntity(), mainhand);
        }
        if (Utilities.isUber(offhand)) {
            Utilities.getUber(offhand).hitEntityAction(player, event, event.getEntity(), offhand);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // determine whether or not a player is trying to make an Uber Workbench
        // breaking a workbench with a lever to make the table, and delete the components
        if (item.getType() == Material.LEVER && event.getBlock().getType() == Material.CRAFTING_TABLE) {
            if (player.getGameMode() != GameMode.CREATIVE)
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            event.setDropItems(false);
            player.getInventory().addItem(UberItem.fromString("0", 1));
            Utilities.playSound(ActionSound.POP, player);
        }

        // test if item in main hand is an UberItem
        if (Utilities.isUber(item)) {
            Utilities.getUber(item).breakBlockAction(player, event, event.getBlock(), item);
        }

    }

    // handle interaction events (air and blocks)
    private void useUberItem(PlayerInteractEvent event, ItemStack item) {
        Player player = event.getPlayer();
        UberItem uber = Utilities.getUber(item);

        // enforce premium vs lite
        if (!UberItems.premium && uber.getRarity().isRarerThan(UberRarity.RARE)) { Utilities.warnPlayer(player, Arrays.asList(main.getPhrase("not-premium-message"))); return; }

        // don't cancel event for malk bucket
        //if (uber.getID() != 8 && uber.getID() != 18) event.setCancelled(true);

        // air and block interaction
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (!player.isSneaking()) { uber.leftClickAirAction(player, item); }
            else { uber.shiftLeftClickAirAction(player, item); }
        }
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (!player.isSneaking()) { uber.leftClickBlockAction(player, event, event.getClickedBlock(), item); }
            else { uber.shiftLeftClickBlockAction(player, event, event.getClickedBlock(), item); }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!player.isSneaking()) { uber.rightClickAirAction(player, item); }
            else { uber.shiftRightClickAirAction(player, item); }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!player.isSneaking()) { uber.rightClickBlockAction(player, event, event.getClickedBlock(), item); }
            else { uber.shiftRightClickBlockAction(player, event, event.getClickedBlock(), item); }
        }
    }

}
