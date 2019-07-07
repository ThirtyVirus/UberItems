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

public class uber_command implements CommandExecutor {

    public UberItems main = null;
    public uber_command(UberItems main){
        this.main = main;
    }

    //Uber Command
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        //try {

        Player player = (Player) sender;

        if (args.length > 0) {

            switch (args[0].toLowerCase()){
                case "give":
                    give(player, args);
                    break;
                case "info":
                    info(player, args);
                    break;
                case "list":
                    list(player);
                    break;
                case "reload":
                    Bukkit.getScheduler().cancelTask(UberItems.activeEffectsCheckID);
                    UberItems.activeEffectsCheckID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() { public void run() { UberItems.uberActiveEffects(); } }, UberItems.activeEffectsDelay, UberItems.activeEffectsDelay);
                    main.registerUberItems();
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "-----------------------------");
                    sender.sendMessage(UberItems.prefix + ChatColor.GOLD + "Welcome to UberItems! Commands:");
                    sender.sendMessage(UberItems.prefix + "/uber info: Gives info for uber item");
                    //player.sendMessage(ChatColor.GOLD + "[held in hand OR num ID OR name]");
                    sender.sendMessage(UberItems.prefix + "/uber list: Lists all uber items");
                    sender.sendMessage(UberItems.prefix + "/uber give ID: Gives uber item " + ChatColor.RED + "ADMIN ONLY");
                    sender.sendMessage(ChatColor.RED + "-----------------------------");
                    break;
            }

        }

        //}
        //catch(Exception e) {
        //	sender.sendMessage(UberItems.prefix + ChatColor.RED + "Incorrect Format: Try again or try /uber help");
        //}

        return true;
    }

    //Give Command
    public void give(Player player, String[] args){
        if (!player.hasPermission("uber.give")){
            player.sendMessage(UberItems.prefix + "Sorry! Not enough permissions!");
        }

        String name = args[1];

        UberItem item;
        if (UberItems.isInteger(name)) {
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

    //Info Command
    public void info(Player player, String[] args){
        if (args.length > 2){
            player.sendMessage(UberItems.prefix + "Sorry! Format invalid");
            return;
        }
        else if (args.length == 1){
            UberItem mainHand = UberItems.getUber(player.getInventory().getItemInMainHand());
            UberItem offHand = UberItems.getUber(player.getInventory().getItemInOffHand());

            if (mainHand != null && offHand != null){
                player.sendMessage(UberItems.prefix + "Main Hand - " + mainHand.getID() + ": " + mainHand.getName() + ": " + mainHand.getDescription());
                player.sendMessage(UberItems.prefix + "Off  Hand - " + offHand.getID() + ": " + offHand.getName() + ": " + offHand.getDescription());
                return;
            }
            else if (mainHand != null){
                player.sendMessage(UberItems.prefix + mainHand.getID() + ": " + mainHand.getName() + ": " + mainHand.getDescription());
                return;
            }
            else if (offHand != null){
                player.sendMessage(UberItems.prefix + offHand.getID() + ": " + offHand.getName() + ": " + offHand.getDescription());
                return;
            }
            else{
                player.sendMessage(UberItems.prefix + "Sorry! Not an Uber Item!");
                return;
            }
        }

        String name = args[1];

        UberItem item;
        if (UberItems.isInteger(name)){
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

    //List Command
    public void list(CommandSender sender){
        sender.sendMessage(UberItems.prefix + "Listing Uber Items:");
        for (String id : UberItems.itemIDs.values()) {
            UberItem item = UberItems.items.get(id);
            sender.sendMessage(ChatColor.GOLD + "" + item.getID() + ": " + item.getName() + ": " + item.getDescription());
        }

    }


}