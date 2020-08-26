package thirtyvirus.uber.events.player;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class Bucket implements Listener {

    UberItems main;
    public Bucket(UberItems main) { this.main = main; }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {

        // the player is using a big bucket
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInMainHand())) {
            Block block = event.getBlock();
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

            int waterCount = 0; waterCount = Utilities.getIntFromItem(main, item, "water-count");
            int lavaCount = 0; lavaCount = Utilities.getIntFromItem(main, item, "lava-count");

            if (block.getType() == Material.WATER) { Utilities.storeIntInItem(main, item, waterCount + 1, "water-count"); item.setType(Material.BUCKET); event.setCancelled(false); event.getPlayer().getInventory().removeItem(event.getItemStack()); }
            else if (block.getType() == Material.LAVA) { Utilities.storeIntInItem(main, item, lavaCount + 1, "lava-count"); item.setType(Material.BUCKET); event.setCancelled(false); event.getPlayer().getInventory().removeItem(event.getItemStack()); }
        }

    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {


    }

    @EventHandler
    public void consumeEvent(PlayerItemConsumeEvent event) {
        if (Utilities.isUber(main, event.getItem(), 8)){

            ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
            for (PotionEffect e : event.getPlayer().getActivePotionEffects()) { effects.add(e); }
            for (PotionEffect e : effects) event.getPlayer().removePotionEffect(e.getType());

            Utilities.getUber(main, event.getItem()).activeEffect(event.getPlayer(), event.getItem());

            event.setCancelled(true);
        }

    }

}
