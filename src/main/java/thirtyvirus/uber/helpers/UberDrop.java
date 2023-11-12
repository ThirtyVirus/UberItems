package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UberDrop {

    public ItemStack item;
    public float chance;
    private final boolean alwaysSendMessage;

    public UberDrop(ItemStack item, float chance, boolean alwaysSendMessage) {
        this.item = item; this.chance = chance; this.alwaysSendMessage = alwaysSendMessage;
    }
    public UberDrop(ItemStack item, float chance) {
        this.item = item; this.chance = chance; alwaysSendMessage = false;
    }

    public boolean tryDrop(Location location, LivingEntity entity) {

        if (Utilities.percentChance(chance)) {
            if (Utilities.isUber(item) && !Utilities.getUber(item).isStackable()) Utilities.storeStringInItem(item,  java.util.UUID.randomUUID().toString(), "UUID");
            location.getWorld().dropItem(location, item);

            // If the item is common enough, don't give RNG message
            if (chance > 20 && !alwaysSendMessage) return true;

            // send the RNG drop chat message and play sounds
            if (entity instanceof Player) sendDropMessage((Player) entity);
        }

        return false;
    }

    public void forceDrop(Location location, Player player) {
        if (Utilities.isUber(item) && !Utilities.getUber(item).isStackable()) Utilities.storeStringInItem(item,  java.util.UUID.randomUUID().toString(), "UUID");
        location.getWorld().dropItem(location, item);
        sendDropMessage(player);
    }
    public void sendDropMessage(Player player) {
        String message;

        if (chance <= 0.001) {
            message = ChatColor.AQUA + "RNGESUS INCARNATE! ";
        } else if (chance <= 0.01) {
            message = ChatColor.LIGHT_PURPLE + "RNGESUS DROP! ";
        } else if (chance <= 0.1) {
            message = ChatColor.GOLD + "EXCEPTIONALLY RARE DROP! ";
        } else if (chance <= 1) {
            message = ChatColor.DARK_PURPLE + "CRAZY RARE DROP! ";
        } else if (chance <= 2) {
            message = ChatColor.BLUE + "VERY RARE DROP! ";
        } else if (chance <= 5) {
            message = ChatColor.GREEN + "RARE DROP! ";
        } else if (chance <= 20) {
            message = ChatColor.WHITE + "DROP! ";
        } else {
            message = ChatColor.WHITE + "DROP! ";
        }

        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.equals("")) displayName = item.getType().name();

        player.sendMessage(message + ChatColor.GRAY + "(" + displayName + ChatColor.GRAY + ") " + ChatColor.AQUA + "(" + chance + "% Chance)");
        Utilities.scheduleTask(()->player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.3f), 1);
        Utilities.scheduleTask(()->player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.6f), 5);
        Utilities.scheduleTask(()->player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.9f), 9);
        Utilities.scheduleTask(()->player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 1.2f), 13);
    }
}
