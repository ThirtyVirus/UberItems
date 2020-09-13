package thirtyvirus.uber.events.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import thirtyvirus.uber.UberItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TabComplete implements TabCompleter {

    private UberItems main = null;
    public TabComplete(UberItems main) { this.main = main; }

    @EventHandler
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        // verify sender is a player
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        ArrayList<String> arguments = new ArrayList<>();

        // tab completion for /exchange command
        if (command.getName().equals("uber")) {
            // no arguments
            if (args.length == 1) {
                if (player.hasPermission("uber.user")) { arguments.addAll(Arrays.asList("help", "info", "tutorial", "identify", "list")); }
                if (player.hasPermission("uber.admin")) { arguments.addAll(Arrays.asList("give", "reload")); }

                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[0].toLowerCase())) iter.remove(); }
            }

            // give command
            else if (args.length == 2 && args[0].equals("give") && player.hasPermission("uber.admin")) {
                arguments.addAll(UberItems.items.keySet());
                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }

            // identify command
            else if (args.length == 2 && args[0].equals("identify") && player.hasPermission("uber.admin")) {
                arguments.addAll(UberItems.items.keySet());
                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }
        }

        return arguments;
    }

}