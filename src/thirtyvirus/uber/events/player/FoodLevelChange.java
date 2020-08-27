package thirtyvirus.uber.events.player;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

public class FoodLevelChange implements Listener {

    UberItems main;
    public FoodLevelChange(UberItems main) { this.main = main; }

    @EventHandler
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        // check if the player has a lunch box
        ItemStack lunchBox = Utilities.searchFor(main, player.getInventory(), 2);
        if (lunchBox == null) return;
        UberItem uber = Utilities.getUber(main, lunchBox);

        // get saturation, verify that the amount is urgent
        final int max = 20; int now = event.getFoodLevel();
        double amountNeeded = max - now; if (amountNeeded < 5) return;
        double saturation = Utilities.getIntFromItem(main, lunchBox, "saturation");

        // feed player
        if (saturation < amountNeeded) {
            if (saturation == 0) return;
            event.setFoodLevel((int) (now + saturation));
            saturation = 0;
            player.setSaturation((float) (player.getSaturation() + amountNeeded / 2));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
        }
        else {
            saturation -= amountNeeded;
            event.setFoodLevel(max);
            player.setSaturation((float) (player.getSaturation() + amountNeeded / 2));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
        }

        // save the new saturation amount in the item, update lore
        saturation = (double)Math.round(saturation * 100d) / 100d;
        Utilities.storeIntInItem(main, lunchBox, (int)saturation, "saturation");
        uber.updateLore(lunchBox);
        Bukkit.getLogger().info("" + saturation);
    }
}
