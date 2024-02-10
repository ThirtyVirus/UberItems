package thirtyvirus.uber.helpers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItems;

public class UberDrop implements Comparable<UberDrop> {

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
        if (Utilities.isNullUberItem(item) || Utilities.isNullUberMaterial(item)) {
            Bukkit.getLogger().warning("UberDrop Cancelled because item is null!");
            return false;
        }

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
        if (Utilities.isNullUberItem(item) || Utilities.isNullUberMaterial(item)) {
            Bukkit.getLogger().warning("UberDrop Cancelled because item is null!");
            return;
        }

        if (UberItems.getItem("null").compare(item)) { Bukkit.getLogger().warning("UberDrop Cancelled because item is null!"); return; }
        location.getWorld().dropItem(location, item);
        sendDropMessage(player);
    }
    public void sendDropMessage(Player player) {
        String message;

        float[] pitches = new float[] {0.3f, 0.6f, 0.9f, 1.2f };
        int[] delays = {1, 5, 9, 13}; // Adjust delays as needed

        if (chance <= 0.001) {
            pitches = new float[] {1.0f, 1.2f, 1.4f, 1.6f };
            message = ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "RNGESUS INCARNATE! ";
        } else if (chance <= 0.01) {
            pitches = new float[] {0.8f, 1.0f, 1.2f, 1.4f };
            message = ChatColor.LIGHT_PURPLE + String.valueOf(ChatColor.BOLD) + "RNGESUS DROP! ";
        } else if (chance <= 0.1) {
            pitches = new float[] {0.5f, 0.7f, 0.9f, 1.1f };
            message = ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "EXCEPTIONALLY RARE DROP! ";
        } else if (chance <= 1) {
            pitches = new float[] {0.5f, 0.6f, 0.7f, 0.8f };
            message = ChatColor.DARK_PURPLE + String.valueOf(ChatColor.BOLD) + "CRAZY RARE DROP! ";
        } else if (chance <= 2) {
            message = ChatColor.BLUE + String.valueOf(ChatColor.BOLD) + "VERY RARE DROP! ";
        } else if (chance <= 5) {
            message = ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "RARE DROP! ";
        } else if (chance <= 20) {
            message = ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "DROP! ";
        } else {
            message = ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "DROP! ";
        }

        String displayName = item.getItemMeta().getDisplayName();
        if (displayName.equals("")) displayName = item.getType().name();

        String amountString = "";
        if (item.getAmount() > 1) amountString = " x" + item.getAmount();

        if (chance != 100) player.sendMessage(message + ChatColor.GRAY + "(" + displayName + amountString + ChatColor.GRAY + ") " + ChatColor.AQUA + "(" + chance + "% Chance)");
        else player.sendMessage(message + ChatColor.GRAY + "(" + displayName + amountString + ChatColor.GRAY + ")");

        // Play the sound sequence
        playSoundSequence(player, Sound.BLOCK_NOTE_BLOCK_HARP, 2, pitches, delays);
    }

    public void playSoundSequence(Player player, Sound sound, float volume, float[] pitches, int[] delays) {
        if (pitches.length != delays.length) {
            throw new IllegalArgumentException("The lengths of pitches and delays arrays must be the same.");
        }

        for (int i = 0; i < pitches.length; i++) {
            int delay = delays[i];
            float pitch = pitches[i];
            Utilities.scheduleTask(() -> player.playSound(player.getLocation(), sound, volume, pitch), delay);
        }
    }

    // override the compareTo method to compare UberDrop instances based on chance
    @Override
    public int compareTo(UberDrop other) {
        return Float.compare(this.chance, other.chance);
    }
}
