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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class Bucket implements Listener {

    UberItems main;
    public Bucket(UberItems main) { this.main = main; }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {

        // the player is using a big bucket
        if (Utilities.isUber(main, event.getPlayer().getInventory().getItemInMainHand(), 4)) {
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

        // the player is drinking from a malk bucket
        if (Utilities.isUber(main, event.getItem(), 8)) {
            // remove all potion effects
            ArrayList<PotionEffect> effects = new ArrayList<>();
            for (PotionEffect e : event.getPlayer().getActivePotionEffects()) { effects.add(e); }
            for (PotionEffect e : effects) event.getPlayer().removePotionEffect(e.getType());

            // retrieve potion from malk bucket
            UberItem uber = Utilities.getUber(main, event.getItem());
            ItemStack[] itemArray = Utilities.getCompactInventory(uber.getMain(), event.getItem());
            if (itemArray.length == 0) return; // ensure that the malk bucket has a spiked potion effect
            ItemStack potion = itemArray[0]; PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

            // give player potion effects
            // default vanilla potion effects to 5 minute duration because of the lack of potion duration in the API
            int amplifier = 0; if (potionMeta.getBasePotionData().isUpgraded()) amplifier = 1; //vanilla potion effect
            event.getPlayer().addPotionEffect(potionMeta.getBasePotionData().getType().getEffectType().createEffect(5 * 60 * 20, amplifier));

            if (potionMeta != null) // custom potion effects
                for (PotionEffect effect : potionMeta.getCustomEffects()) event.getPlayer().addPotionEffect(effect);

            event.setCancelled(true);
        }

    }

}
