package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

// an object that stores a name and string to describe the ability of an Uber Item. For display purposes only!
public class UberAbility {

    private String name, description;
    private AbilityType type;
    private int cooldown = 0;
    private int manaCost = 0;

    public UberAbility(String name, AbilityType type, String description) {
        this.name = name; this.type = type; this.description = description;
    }

    public UberAbility(String name, AbilityType type, String description, int cooldown) {
        this.name = name; this.type = type; this.description = description; this.cooldown = cooldown;
    }

    public UberAbility(String name, AbilityType type, String description, int cooldown, int manaCost) {
        this.name = name; this.type = type; this.description = description; this.cooldown = cooldown;
        this.manaCost = manaCost;
    }

    // convert the ability into Item Lore
    public List<String> toLore() {
        List<String> lore = new ArrayList<>();

        if (type != AbilityType.FULL_SET_BONUS) {
            lore.add(ChatColor.GOLD + "Item Ability: " + name + " " + ChatColor.YELLOW + ChatColor.BOLD + type.getText());
            lore.addAll(Utilities.stringToLore(description, 40, ChatColor.GRAY));

            if (manaCost > 0) lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.AQUA + manaCost);
            if (cooldown > 0) lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + "s.");
        }
        else {
            lore.add(ChatColor.GOLD + "Full Set Bonus: " + name + " " + ChatColor.YELLOW);
            lore.addAll(Utilities.stringToLore(description, 40, ChatColor.GRAY));
            if (cooldown > 0) lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + "s.");
        }

        return lore;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public AbilityType getType() {
        return type;
    }
    public int getCooldown() { return cooldown; }
    public int getManaCost() { return manaCost; }

}
