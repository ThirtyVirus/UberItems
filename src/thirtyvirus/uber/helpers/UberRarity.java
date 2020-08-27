package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;

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

}
