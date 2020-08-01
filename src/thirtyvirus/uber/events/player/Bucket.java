package thirtyvirus.uber.events.player;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class Bucket implements Listener {

    UberItems main;
    public Bucket(UberItems main) { this.main = main; }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {

        //Big Bucket
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInMainHand(), 4)){

            event.getBlockClicked().setType(Material.AIR);
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {

        //Big Bucket
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInMainHand(), 4)){

            event.getBlockClicked().setType(Material.AIR);
            event.setCancelled(true);

        }
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
