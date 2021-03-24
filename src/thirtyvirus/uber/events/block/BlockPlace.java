package thirtyvirus.uber.events.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import thirtyvirus.uber.helpers.Utilities;

public class BlockPlace implements Listener {

    // prevent accidentally placing UberItems as Vanilla Blocks
    @EventHandler(priority= EventPriority.HIGH)
    private void onBlockPlace (BlockPlaceEvent event) {
        if (Utilities.isUber(event.getItemInHand())) event.setCancelled(true);
        if (Utilities.isUberMaterial(event.getItemInHand())) event.setCancelled(true);
    }
}
