package thirtyvirus.uber.events.player;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.helpers.Utilities;

public class FoodLevelChange implements Listener {

    @EventHandler
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        ItemStack lunchBox = Utilities.searchFor(player.getInventory(), 2);

        if (lunchBox != null){
            int now = event.getFoodLevel();
            int max = 20;

            double amountNeeded = max - now;
            if (amountNeeded < 5) return;

            double saturation = Float.parseFloat(lunchBox.getItemMeta().getLore().get(0).substring(14));

            if (saturation < amountNeeded){
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

            saturation = (double)Math.round(saturation * 100d) / 100d;
            List<String> lore = lunchBox.getItemMeta().getLore();
            lore.set(0, ChatColor.GOLD + "Saturation: " + saturation);
            Utilities.loreItem(lunchBox, lore);

        }

    }


}
