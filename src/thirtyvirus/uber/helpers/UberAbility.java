package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// an object that stores a name and string to describe the ability of an Uber Item. For display purposes only!
public class UberAbility {

    private String name;
    private AbilityType type;
    private String description = "This item wasn't given a description!";
    private int cooldown = 0;

    public UberAbility(String name, AbilityType type, String description) {
        this.name = name; this.type = type; this.description = description;
    }

    public UberAbility(String name, AbilityType type, String description, int cooldown) {
        this.name = name; this.type = type; this.description = description; this.cooldown = cooldown;
    }

    // convert the ability into Item Lore
    public List<String> toLore() {
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GOLD + "Item Ability: " + name + " " + ChatColor.YELLOW + ChatColor.BOLD + type.getText());
        lore.addAll(Utilities.stringToLore(description, 40, ChatColor.GRAY));
        if (cooldown > 0) lore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.GREEN + cooldown + "s.");

        return lore;
    }

}
