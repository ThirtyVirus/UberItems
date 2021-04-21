package thirtyvirus.uber.events.player;

import java.util.ArrayList;
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

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) { }
    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) { }

    // process malk bucket
    @EventHandler
    private void consumeEvent(PlayerItemConsumeEvent event) {

        // the player is drinking from a malk bucket
        if (UberItems.getItem("malk_bucket").compare(event.getItem())) {
            // remove all potion effects
            ArrayList<PotionEffect> effects = new ArrayList<>();
            for (PotionEffect e : event.getPlayer().getActivePotionEffects()) { effects.add(e); }
            for (PotionEffect e : effects) event.getPlayer().removePotionEffect(e.getType());

            // retrieve potion from malk bucket
            UberItem uber = Utilities.getUber(event.getItem());

            // enforce premium vs lite, item rarity perms, item specific perms
            if (!Utilities.enforcePermissions(event.getPlayer(), uber)) return;

            ItemStack[] itemArray = Utilities.getCompactInventory(event.getItem());
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
