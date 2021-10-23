package thirtyvirus.uber.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import thirtyvirus.uber.helpers.Utilities;

public class BlockEvents implements Listener {

    // prevent accidentally placing UberItems as Vanilla Blocks
    @EventHandler
    private void onBlockPlace (BlockPlaceEvent event) {
        if (Utilities.isUber(event.getItemInHand())) event.setCancelled(true);
        if (Utilities.isUberMaterial(event.getItemInHand())) event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (Utilities.temporaryBlocks.contains(event.getBlock())) event.setCancelled(true);
    }
}
