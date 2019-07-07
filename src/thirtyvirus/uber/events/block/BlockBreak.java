package thirtyvirus.uber.events.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;

public class BlockBreak implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void OnBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (UberItems.isUber(mainHand)){
            if (!UberItems.getUber(mainHand).getCanBreakBlocks()) event.setCancelled(true);
        }
        if (UberItems.isUber(offHand)){
            if (!UberItems.getUber(offHand).getCanBreakBlocks()) event.setCancelled(true);
        }
    }
}
