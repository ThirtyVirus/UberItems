package thirtyvirus.uber.events.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class BlockPlace implements Listener {

    UberItems main;
    public BlockPlace(UberItems main) { this.main = main; }

    // prevent accidentally placing UberItems as Vanilla Blocks
    @EventHandler(priority= EventPriority.HIGH)
    public void onBlockPlace (BlockPlaceEvent event) {
        if (Utilities.isUber(event.getItemInHand())) event.setCancelled(true);
        if (Utilities.isUberMaterial(event.getItemInHand())) event.setCancelled(true);
    }
}
