package thirtyvirus.uber.events.player;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class PlayerUseUberItem implements Listener {

    UberItems main;
    public PlayerUseUberItem(UberItems main) { this.main = main; }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {

        // test if items in main and off hand are UberItems
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInMainHand())) {
            useUberItem(event, event.getPlayer().getInventory().getItemInMainHand());
        }
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInOffHand())) {
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
        if (Utilities.isUber(main, mainhand)) {
            Utilities.getUber(main, mainhand).hitEntityAction(player, event, event.getEntity(), mainhand);
        }
        if (Utilities.isUber(main, offhand)) {
            Utilities.getUber(main, offhand).hitEntityAction(player, event, event.getEntity(), offhand);
        }
    }

    // handle interaction events (air and blocks)
    private void useUberItem(PlayerInteractEvent event, ItemStack item) {
        Player player = event.getPlayer();
        UberItem uber = Utilities.getUber(main, item);

        event.setCancelled(true);

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
