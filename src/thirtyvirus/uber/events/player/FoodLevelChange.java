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
    // handle the lunch box ability
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        // check if the player has a lunch box
        ItemStack lunchBox = Utilities.searchFor(main, player.getInventory(), 2);
        if (lunchBox == null) return;
        UberItem uber = Utilities.getUber(main, lunchBox);

        // get food and saturation levels
        final int max = 20; // maximum food and saturation
        int availableFood = Utilities.getIntFromItem(main, lunchBox, "food");
        int availableSaturation = Utilities.getIntFromItem(main, lunchBox, "saturation");
        int playerFood = player.getFoodLevel();
        int playerSaturation = (int) player.getSaturation();

        // verify that the amount is urgent
        int foodNeeded = max - playerFood;
        int saturationNeeded = max - playerSaturation;
        if (foodNeeded < 5) return;

        // limit the given food and saturation to the amount available
        if (availableFood < foodNeeded) foodNeeded = availableFood;
        if (availableSaturation < saturationNeeded) saturationNeeded = availableSaturation;

        // subtract food and saturation from lunch box
        availableFood -= foodNeeded;
        availableSaturation -= saturationNeeded;

        // feed player
        player.setFoodLevel(playerFood + foodNeeded);
        player.setSaturation(playerSaturation + saturationNeeded);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
        event.setCancelled(true); // prevents the food and saturation levels from being reset by the event

        // save the new saturation and food amounts in the item, update lore
        Utilities.storeIntInItem(main, lunchBox, (int)availableFood, "food");
        Utilities.storeIntInItem(main, lunchBox, (int)availableSaturation, "saturation");
        uber.updateLore(lunchBox);
    }
}
