package thirtyvirus.uber.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thirtyvirus.uber.UberItem;
import thirtyvirus.uber.UberItems;
import thirtyvirus.uber.helpers.MenuUtils;
import thirtyvirus.uber.helpers.Utilities;

import java.util.Arrays;

public class uber implements CommandExecutor{

    private UberItems main = null;
    public uber(UberItems main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // verify that the user has proper permissions
        if (!sender.hasPermission("uber.user")) {
            Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-permissions-message")));
            return true;
        }

        try {

            switch (args[0].toLowerCase()) {
                // standard plugin commands
                case "help":
                    help(sender);
                    break;
                case "info":
                    info(sender);
                    break;
                case "tutorial":
                    if (sender instanceof Player) MenuUtils.tutorialMenu((Player) sender);
                    else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-console-message")));
                    break;

                // plugin specific user commands
                case "identify":
                    identify(sender, args);
                    break;
                case "list":
                    list(sender);
                    break;

                // staff commands
                case "give":
                    if (sender.hasPermission("uber.admin")) give(sender, args);
                    else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-permissions-message")));
                    break;
                case "reload":
                    if (sender.hasPermission("uber.admin")) reload(sender);
                    else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-permissions-message")));
                    break;

                default:
                    Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("not-a-command-message")));
                    help(sender);
                    break;
            }

        } catch(Exception e) {
            Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("formatting-error-message")));
        }

        return true;
    }

    // give command
    private void give(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Sorry, you must execute this command as a player!");
            return;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("uber.give")) {
            player.sendMessage(UberItems.prefix + "Sorry! Not enough permissions!");
        }

        String name = args[1];

        UberItem item;
        if (Utilities.isInteger(name)) {
            item = UberItems.items.get(UberItems.itemIDs.get(Integer.parseInt(name)));
        }
        else {
            item = UberItems.items.get(name);
        }
        if (item == null) {
            player.sendMessage(UberItems.prefix + "Sorry! Not an Uber Item!");
            return;
        }

        player.sendMessage(UberItems.prefix + "Given " + item.getName());

        ItemStack testItem = item.getItem().clone();
        if (args.length > 2 && item.isStackable()){
            testItem.setAmount(Integer.parseInt(args[2]));
        }
        player.getInventory().addItem(testItem);
    }

    // identify Command
    public void identify(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Sorry, you must execute this command as a player!");
            return;
        }
        Player player = (Player) sender;
        Bukkit.getLogger().info("did ti work? " + args.length);
        if (args.length > 2){
            player.sendMessage(UberItems.prefix + "Sorry! Format invalid");
            return;
        }
        else if (args.length == 1) {
            UberItem mainHand = Utilities.getUber(player.getInventory().getItemInMainHand());
            UberItem offHand = Utilities.getUber(player.getInventory().getItemInOffHand());

            if (mainHand != null && offHand != null) {
                player.sendMessage(UberItems.prefix + "Main Hand - " + mainHand.getID() + ": " + mainHand.getName() + ": " + mainHand.getDescription());
                player.sendMessage(UberItems.prefix + "Off  Hand - " + offHand.getID() + ": " + offHand.getName() + ": " + offHand.getDescription());
                return;
            }
            else if (mainHand != null) {
                player.sendMessage(UberItems.prefix + mainHand.getID() + ": " + mainHand.getName() + ": " + mainHand.getDescription());
                return;
            }
            else if (offHand != null) {
                player.sendMessage(UberItems.prefix + offHand.getID() + ": " + offHand.getName() + ": " + offHand.getDescription());
                return;
            }
            else {
                player.sendMessage(UberItems.prefix + "Sorry! Not an Uber Item!");
                return;
            }
        }

        String name = args[1];

        UberItem item;
        if (Utilities.isInteger(name)){
            item = UberItems.items.get(UberItems.itemIDs.get(Integer.parseInt(name)));
        }
        else{
            item = UberItems.items.get(name);
        }
        if (item == null) {
            player.sendMessage(UberItems.prefix + "Sorry! Not an Uber Item!");
            return;
        }

        player.sendMessage(UberItems.prefix + item.getID() + ": " + item.getName() + ": " + item.getDescription());
    }

    // list Command
    public void list(CommandSender sender){
        sender.sendMessage(UberItems.prefix + "Listing Uber Items:");
        for (String id : UberItems.itemIDs.values()) {
            UberItem item = UberItems.items.get(id);
            sender.sendMessage(ChatColor.GOLD + "" + item.getID() + ": " + item.getName() + ": " + item.getDescription());
        }

    }

    // info command
    private void info(CommandSender sender) {
        sender.sendMessage(UberItems.prefix + ChatColor.GRAY + "Plugin Info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GREEN + "Version " + main.getVersion() + " - By ThirtyVirus");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GREEN + "~The best plugin lbp ever!");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.RED + "" + ChatColor.BOLD + "You" + ChatColor.WHITE + ChatColor.BOLD + "Tube" + ChatColor.GREEN + " - https://youtube.com/thirtyvirus");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Twitter" + ChatColor.GREEN + " - https://twitter.com/Thirtyvirus");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.GOLD + "" + ChatColor.BOLD + "SpigotMC" + ChatColor.GREEN + " - https://www.spigotmc.org/members/thirtyvirus.179587/");
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    private void help(CommandSender sender) {
        sender.sendMessage(UberItems.prefix + ChatColor.GRAY + "Commands");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber help");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber tutorial");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber cancel");
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    private void reload(CommandSender sender) {
        main.reloadConfig();
        main.loadConfiguration();

        main.loadLangFile();

        Utilities.informPlayer(sender, Arrays.asList("configuration, values, and language settings reloaded"));
    }

}
