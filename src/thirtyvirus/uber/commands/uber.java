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

        // verify that the command is executed by a player
        if (!(sender instanceof Player)) { Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-console-message"))); return; }
        Player player = (Player) sender;

        // get the item from either ID or name
        String name = args[1]; UberItem item;
        if (Utilities.isInteger(name)) item = UberItems.items.get(UberItems.itemIDs.get(Integer.parseInt(name)));
        else item = UberItems.items.get(name);

        // verify that the item is in fact an UberItem
        if (item == null) { Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("not-uberitem"))); return; }

        // apply UberItem properties to item
        ItemStack newItemStack = new ItemStack(item.getMaterial());
        Utilities.nameItem(newItemStack, item.getRarity().getColor() + item.getName());
        Utilities.loreItem(newItemStack, item.getLore());
        Utilities.storeStringInItem(main, newItemStack, "true", "is-uber");
        Utilities.storeStringInItem(main, newItemStack, name, "uber-name");
        Utilities.storeIntInItem(main, newItemStack, item.getID(), "uber-id");

        item.enforceStackability(newItemStack);
        item.onItemStackCreate(newItemStack);

        if (args.length > 2 && item.isStackable()) {
            newItemStack.setAmount(Integer.parseInt(args[2]));
        }

        // give the item to the player
        player.getInventory().addItem(newItemStack);
        player.sendMessage(UberItems.prefix + "Given " + item.getName());
    }

    // identify Command
    public void identify(CommandSender sender, String[] args) {

        // verify that the command is executed by a player
        if (!(sender instanceof Player)) { Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-console-message"))); return; }
        Player player = (Player) sender;

        // identify UberItem(s) held by the player (main or offhand)
        if (args.length == 1) {
            UberItem mainHand = Utilities.getUber(main, player.getInventory().getItemInMainHand());
            UberItem offHand = Utilities.getUber(main, player.getInventory().getItemInOffHand());

            if (mainHand != null && offHand != null) {
                player.sendMessage(UberItems.prefix + ChatColor.YELLOW + "Main Hand - " + mainHand.getID() + ": " + mainHand.getName());
                player.sendMessage(UberItems.prefix + ChatColor.YELLOW + "Off  Hand - " + offHand.getID() + ": " + offHand.getName());
                return;
            }
            else if (mainHand != null) {
                player.sendMessage(UberItems.prefix + ChatColor.YELLOW + mainHand.getID() + ": " + mainHand.getName());
                return;
            }
            else if (offHand != null) {
                player.sendMessage(UberItems.prefix + ChatColor.YELLOW + offHand.getID() + ": " + offHand.getName());
                return;
            }
            else {
                Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("not-uberitem")));
                return;
            }
        }

        // identify an UberItem by name

        // get the item from either ID or name
        String name = args[1]; UberItem item;
        if (Utilities.isInteger(name)) item = UberItems.items.get(UberItems.itemIDs.get(Integer.parseInt(name)));
        else item = UberItems.items.get(name);

        // verify that the item is in fact an UberItem
        if (item == null) { Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("not-uberitem"))); return; }

        // tell player what
        player.sendMessage(UberItems.prefix + ChatColor.YELLOW + item.getID() + ": " + item.getName());
    }

    // list Command
    public void list(CommandSender sender){
        sender.sendMessage(UberItems.prefix + "Listing UberItems:");
        for (String id : UberItems.itemIDs.values()) {
            UberItem item = UberItems.items.get(id);
            sender.sendMessage(ChatColor.GOLD + "" + item.getID() + ": " + item.getName());
        }
    }

    // info command
    private void info(CommandSender sender) {
        sender.sendMessage(UberItems.prefix + ChatColor.GRAY + "UberItems Plugin Info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GREEN + "Version " + main.getVersion() + " - By ThirtyVirus");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GREEN + "~Add powerful and fun items to your Minecraft Server!");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.RED + "" + ChatColor.BOLD + "You" + ChatColor.WHITE + ChatColor.BOLD + "Tube" + ChatColor.GREEN + " - https://youtube.com/thirtyvirus");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Twitter" + ChatColor.GREEN + " - https://twitter.com/ThirtyVirus");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.RESET + ChatColor.GOLD + "" + ChatColor.BOLD + "SpigotMC" + ChatColor.GREEN + " - https://www.spigotmc.org/members/thirtyvirus.179587/");
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    // help command
    private void help(CommandSender sender) {
        sender.sendMessage(UberItems.prefix + ChatColor.GRAY + "Commands");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber help");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber tutorial");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber identify");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber list");
        if (sender.hasPermission("uber.admin")) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber give");
            sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/uber reload");
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    // reload the config and language files in real time
    private void reload(CommandSender sender) {
        main.reloadConfig();
        main.loadConfiguration();

        main.loadLangFile();

        Utilities.informPlayer(sender, Arrays.asList("configuration, values, and language settings reloaded"));
    }

}
