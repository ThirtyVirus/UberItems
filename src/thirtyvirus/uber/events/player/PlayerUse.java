package thirtyvirus.uber.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.helpers.Utilities;

public class PlayerUse implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void OnPlayerUse(PlayerInteractEvent event){

        Player player = event.getPlayer();

        //MAIN HAND ITEM
        if(event.getHand() == EquipmentSlot.HAND){
            ItemStack item = player.getInventory().getItemInMainHand();
            if(item == null) return;

            //Test if item is an Uber Item
            if(Utilities.isUber(item)){

                if (event.getAction() == Action.LEFT_CLICK_AIR){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInMainHand()).leftClickAirAction(player, item); }
                    else{ Utilities.getUber(player.getInventory().getItemInMainHand()).shiftLeftClickAirAction(player, item); }
                    return;
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInMainHand()).leftClickBlockAction(player, event, event.getClickedBlock(), item); }
                    else{ Utilities.getUber(player.getInventory().getItemInMainHand()).shiftLeftClickBlockAction(player, event, event.getClickedBlock(), item); }
                    return;
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInMainHand()).rightClickAirAction(player, item); }
                    else{ Utilities.getUber(player.getInventory().getItemInMainHand()).shiftRightClickAirAction(player, item); }
                    return;
                }

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInMainHand()).rightClickBlockAction(player, event, event.getClickedBlock(), item); }
                    else{ Utilities.getUber(player.getInventory().getItemInMainHand()).shiftRightClickBlockAction(player, event, event.getClickedBlock(), item); }
                    event.setCancelled(true);
                    return;
                }

                //MIDDLE CLICK???
            }
        }

        //OFF HAND ITEM
        if(event.getHand() == EquipmentSlot.OFF_HAND){

            ItemStack offhandItem = player.getInventory().getItemInOffHand();
            if(offhandItem == null) return;

            //Test if item is an Uber Item
            if(Utilities.isUber(offhandItem)){

                if (event.getAction() == Action.LEFT_CLICK_AIR){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInOffHand()).leftClickAirAction(player, offhandItem); }
                    else{ Utilities.getUber(player.getInventory().getItemInOffHand()).shiftLeftClickAirAction(player, offhandItem); }
                    return;
                }

                if (event.getAction() == Action.LEFT_CLICK_BLOCK){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInOffHand()).leftClickBlockAction(player, event, event.getClickedBlock(), offhandItem); }
                    else{ Utilities.getUber(player.getInventory().getItemInOffHand()).shiftLeftClickBlockAction(player, event, event.getClickedBlock(), offhandItem); }
                    return;
                }

                if (event.getAction() == Action.RIGHT_CLICK_AIR){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInOffHand()).rightClickAirAction(player, offhandItem); }
                    else{ Utilities.getUber(player.getInventory().getItemInOffHand()).shiftRightClickAirAction(player, offhandItem); }
                    return;
                }

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    if (!player.isSneaking()){ Utilities.getUber(player.getInventory().getItemInOffHand()).rightClickBlockAction(player, event, event.getClickedBlock(), offhandItem); }
                    else{ Utilities.getUber(player.getInventory().getItemInOffHand()).shiftRightClickBlockAction(player, event, event.getClickedBlock(), offhandItem); }
                    return;
                }

                //MIDDLE CLICK???
            }
        }
    }
}
