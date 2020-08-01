package thirtyvirus.uber.events.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class BlockBreak implements Listener {

    UberItems main;
    public BlockBreak(UberItems main) { this.main = main; }

    @EventHandler(priority=EventPriority.HIGH)
    public void OnBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (Utilities.isUber(main, mainHand)){
            if (!Utilities.getUber(main, mainHand).getCanBreakBlocks()) event.setCancelled(true);
        }
        if (Utilities.isUber(main, offHand)){
            if (!Utilities.getUber(main, offHand).getCanBreakBlocks()) event.setCancelled(true);
        }
    }
}
