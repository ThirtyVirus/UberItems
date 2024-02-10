package thirtyvirus.uber.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import thirtyvirus.uber.UberItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TabComplete implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        // verify sender is a player
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        ArrayList<String> arguments = new ArrayList<>();

        // tab completion for /uber command
        if (command.getName().equals("uber")) {
            // no arguments
            if (args.length == 1) {
                if (player.hasPermission("uber.user")) { arguments.addAll(Arrays.asList("help", "info", "identify", "list")); }
                if (player.hasPermission("uber.admin")) { arguments.addAll(Arrays.asList("give", "giveMaterial", "setMana", "setMaxMana", "updateLore", "reload")); }

                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[0].toLowerCase())) iter.remove(); }
            }

            // give command
            else if (args.length == 2 && args[0].equalsIgnoreCase("give") && player.hasPermission("uber.admin")) {
                arguments.addAll(UberItems.getItemNames()); arguments.remove("null");
                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }

            // give material command
            else if (args.length == 2 && args[0].equalsIgnoreCase("givematerial") && player.hasPermission("uber.admin")) {
                arguments.addAll(UberItems.getMaterialNames()); arguments.remove("null");
                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }

            // identify command
            else if (args.length == 2 && args[0].equalsIgnoreCase("identify") && player.hasPermission("uber.admin")) {
                arguments.addAll(UberItems.getItemNames());
                Iterator<String> iter = arguments.iterator(); while (iter.hasNext()) { String str = iter.next().toLowerCase(); if (!str.contains(args[1].toLowerCase())) iter.remove(); }
            }
        }

        return arguments;
    }

}
