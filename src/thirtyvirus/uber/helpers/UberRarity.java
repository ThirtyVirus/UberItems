package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import thirtyvirus.multiversion.Version;

public enum UberRarity {
    COMMON(ChatColor.WHITE),
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.BLUE),
    EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.GOLD),
    MYTHIC(ChatColor.LIGHT_PURPLE),
    SPECIAL(ChatColor.RED),
    VERY_SPECIAL(ChatColor.RED),
    UNFINISHED(ChatColor.DARK_RED);

    private ChatColor color;

    private UberRarity(ChatColor color) { this.color = color; }
    public ChatColor getColor() { return color; }

    // determine if the item is "rarer than" a certain Rarity
    public boolean isRarerThan(UberRarity rarity) {
        int current = getIndex();
        int param = rarity.getIndex();

        return current > param;
    }
    public int getIndex() {
        int index = 0;
        for(UberRarity rarity : values()) {
            if(this.equals(rarity)) return index;
            else index++;
        }

        return -1;
    }
}
