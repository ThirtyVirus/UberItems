package thirtyvirus.uber.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.helpers.UberAbility;
import thirtyvirus.uber.helpers.UberCraftingRecipe;
import thirtyvirus.uber.helpers.UberRarity;
import thirtyvirus.uber.helpers.Utilities;

public class aspect_of_the_virus extends UberItem {

    public aspect_of_the_virus(Material material, String name, UberRarity rarity, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe, String raritySuffix) {
        super(material, name, rarity, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe, raritySuffix);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public boolean leftClickAirAction(Player player, ItemStack item) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.2f, 2.5f);
        return false;
    }
    public boolean leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        return leftClickAirAction(player, item);
    }

    public boolean rightClickAirAction(Player player, ItemStack item) { return false; }
    public boolean rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean shiftLeftClickAirAction(Player player, ItemStack item) { return false; }
    public boolean shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { return false; }

    public boolean shiftRightClickAirAction(Player player, ItemStack item) {
        List<Entity> nearAll = player.getNearbyEntities(40,40,40);
        List<Entity> nearLiving = new ArrayList<Entity>();
        for (Entity a : nearAll) if (a instanceof LivingEntity && !(a instanceof Bat)) nearLiving.add(a);

        if (nearLiving.size() == 0) return false; // prevent crash
        Entity nearestEntity = nearLiving.get(0);
        for (Entity a : nearLiving) {
            if (player.getLocation().distance(a.getLocation()) < player.getLocation().distance(nearestEntity.getLocation())) {
                nearestEntity = a;
            }
        }

        player.teleport(nearestEntity.getLocation().add(nearestEntity.getLocation().getDirection().multiply(-1)));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
        return true;
    }
    public boolean shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        return rightClickAirAction(player, item);
    }

    public boolean middleClickAction(Player player, ItemStack item) { return false; }
    public boolean hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) {
        leftClickAirAction(player, item);
        if (!(target instanceof LivingEntity)) return false;
        LivingEntity li = (LivingEntity) target;
        li.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 2));
        li.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 120, 2));

        for (int counter = 0; counter < 100; counter++) {
            Utilities.scheduleTask(new Runnable() { public void run() {
                if (!target.isDead()) player.getWorld().playEffect(target.getLocation().add(0,1,0), Effect.SMOKE, 100);
            } }, counter);
        }
        return true;
    }
    public boolean breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { return false; }
    public boolean clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { return false; }
    public boolean activeEffect(Player player, ItemStack item) { return false; }
}
