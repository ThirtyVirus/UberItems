package thirtyvirus.uber.events.inventory;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.Utilities;

import static org.bukkit.Material.TROPICAL_FISH;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClickItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        //Lunch Box Code
        if (event.getView().getTitle().contains("Insert Food into Lunch Box!")) {
            event.setCancelled(true);
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "[UBER] " + ChatColor.GRAY + "Lunch Box")){

                if (item.getType().isEdible() || item.getType() == Material.MELON){

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);

                    double saturation = Float.parseFloat(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(0).substring(14));
                    saturation = addSaturation(saturation, item, item.getAmount());
                    saturation = (double)Math.round(saturation * 100d) / 100d;

                    List<String> lore = player.getInventory().getItemInMainHand().getItemMeta().getLore();
                    lore.set(0, ChatColor.GOLD + "Saturation: " + saturation);
                    Utilities.loreItem(player.getInventory().getItemInMainHand(), lore);
                    player.getInventory().remove(event.getCurrentItem());
                }
                else{
                    event.setCancelled(true);
                }
            }

            else if (event.getRawSlot() < 9 && event.getRawSlot() != 4) { event.setCancelled(true); return; }
        }

        //Infini-Gulp Code
        if (event.getView().getTitle().contains("Add Potion Effects!")) {
            event.setCancelled(true);

            //ItemStack bucket = player.getInventory().getItemInMainHand();

            //ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();



            //UberItems.loreItem(item, Arrays.asList(""));
        }

    }

    //Values based on "effective quality" from Minecraft wiki
    public double addSaturation(double saturation2, ItemStack i, int amount){
        double saturation = saturation2;
        switch (i.getType()){

            case APPLE: saturation += 6.4 * amount; break;
            case BAKED_POTATO: saturation += 11 * amount; break;
            case BEETROOT: saturation += 2.2 * amount; break;
            case BEETROOT_SOUP: saturation += 13.2 * amount; break;
            case BREAD: saturation += 11 * amount; break;
            case CAKE: saturation += 16.8 * amount; break;
            case CARROT: saturation += 6.6 * amount; break;
            case CHORUS_FRUIT: saturation += 6.4 * amount; break;
            case TROPICAL_FISH: saturation += 1.2 * amount; break;
            case COOKED_CHICKEN: saturation += 13.2 * amount; break;
            case COOKED_COD:  saturation += 11 * amount; break;
            case COOKED_SALMON: saturation += 15.6 * amount; break;
            case COOKED_MUTTON: saturation += 15.6 * amount; break;
            case COOKED_PORKCHOP: saturation += 20.8 * amount; break;
            case COOKED_RABBIT: saturation += 11 * amount; break;
            case COOKIE: saturation += 2.4 * amount; break;
            case DRIED_KELP: saturation += 1.6 * amount; break;
            case GOLDEN_APPLE: saturation += 13.6 * amount; break;
            case GOLDEN_CARROT: saturation += 20.4 * amount; break;
            case MELON_SLICE: saturation += 3.2 * amount; break;
            case MELON: saturation += 28.8 * amount; break;
            case MUSHROOM_STEW: saturation += 13.2 * amount; break;
            case POTATO: saturation += 1.6 * amount; break;
            case POISONOUS_POTATO: saturation += 3.2 * amount; break;
            case PUFFERFISH: saturation += 1.2 * amount; break;
            case PUMPKIN_PIE: saturation += 12.8 * amount; break;
            case RABBIT_STEW: saturation += 22 * amount; break;
            case BEEF: saturation += 4.8 * amount; break;
            case CHICKEN: saturation += 3.2 * amount; break;
            case COD: saturation += 2.4 * amount; break;
            case SALMON: saturation += 2.4 * amount; break;
            case MUTTON: saturation += 3.2 * amount; break;
            case PORKCHOP: saturation += 4.8 * amount; break;
            case RABBIT: saturation += 4.8 * amount; break;
            case ROTTEN_FLESH: saturation += 4.8 * amount; break;
            case SPIDER_EYE: saturation += 5.2 * amount; break;
            case COOKED_BEEF: saturation += 20.8 * amount; break;
            default:
                break;
        }

        //balancing (to make it not too OP)
        saturation /= 2;

        return saturation;
    }
}
