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

// a template class that can be copy - pasted and renamed when making new Uber Items
public class aspect_of_the_virus extends UberItem {

    public aspect_of_the_virus(int id, UberRarity rarity, String name, Material material, boolean stackable, boolean oneTimeUse, boolean hasActiveEffect, List<UberAbility> abilities, UberCraftingRecipe craftingRecipe) {
        super(id, rarity, name, material, stackable, oneTimeUse, hasActiveEffect, abilities, craftingRecipe);
    }
    public void onItemStackCreate(ItemStack item) { }
    public void getSpecificLorePrefix(List<String> lore, ItemStack item) { }
    public void getSpecificLoreSuffix(List<String> lore, ItemStack item) { }

    public void leftClickAirAction(Player player, ItemStack item) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.2f, 2.5f);
    }
    public void leftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        leftClickAirAction(player, item);
    }

    public void rightClickAirAction(Player player, ItemStack item) { }
    public void rightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

    public void shiftLeftClickAirAction(Player player, ItemStack item) { }
    public void shiftLeftClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) { }

    public void shiftRightClickAirAction(Player player, ItemStack item) {
        List<Entity> nearAll = player.getNearbyEntities(40,40,40);
        List<Entity> nearLiving = new ArrayList<Entity>();
        for (Entity a : nearAll) if (a instanceof LivingEntity && !(a instanceof Bat)) nearLiving.add(a);

        if (nearLiving.size() == 0) return; // prevent crash
        Entity nearestEntity = nearLiving.get(0);
        for (Entity a : nearLiving) {
            if (player.getLocation().distance(a.getLocation()) < player.getLocation().distance(nearestEntity.getLocation())) {
                nearestEntity = a;
            }
        }

        player.teleport(nearestEntity.getLocation().add(nearestEntity.getLocation().getDirection().multiply(-1)));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
    }
    public void shiftRightClickBlockAction(Player player, PlayerInteractEvent event, Block block, ItemStack item) {
        rightClickAirAction(player, item);
    }

    public void middleClickAction(Player player, ItemStack item) { }
    public void hitEntityAction(Player player, EntityDamageByEntityEvent event, Entity target, ItemStack item) {
        leftClickAirAction(player, item);
        if (!(target instanceof LivingEntity)) return;
        LivingEntity li = (LivingEntity) target;
        li.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 120, 2));
        li.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 120, 2));

        for (int counter = 0; counter < 100; counter++) {
            Utilities.scheduleTask(new Runnable() { public void run() {
                if (!target.isDead()) player.getWorld().playEffect(target.getLocation().add(0,1,0), Effect.SMOKE, 100);
            } }, counter);
        }

    }
    public void breakBlockAction(Player player, BlockBreakEvent event, Block block, ItemStack item) { }
    public void clickedInInventoryAction(Player player, InventoryClickEvent event, ItemStack item, ItemStack addition) { }
    public void activeEffect(Player player, ItemStack item) { }
}
