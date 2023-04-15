package com.github.justadeni.irongate.command;

import com.github.justadeni.irongate.misc.ConfigManager;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IFGCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!s.equalsIgnoreCase("ifg") && !s.equalsIgnoreCase("ironfencegate"))
            return true;

        if (sender instanceof Player)
            if (!sender.hasPermission("ironfencegate.admin"))
                return true;

        if (args.length == 0) {
            sendMessage(sender, "command.tooshort");
            return true;
        }
        if (args.length > 1) {
            sendMessage(sender, "command.toolong");
            return true;
        }

        if (args[0].equalsIgnoreCase("get")){
            if (sender instanceof Player p){

                if (p.getInventory().getItemInMainHand().getType().isAir()){

                    p.getInventory().setItemInMainHand(Recipe.recipes.get(0).getResult());
                    sendMessage(sender, "command.itemrecieved");

                } else if (p.getInventory().getItemInOffHand().getType().isAir()){
                    p.getInventory().setItemInOffHand(Recipe.recipes.get(0).getResult());
                    sendMessage(sender, "command.itemrecieved");
                } else {
                    sendMessage(sender, "command.handsfull");
                }
            } else {
                sendMessage(sender, "command.consolecant");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            ConfigManager.get().reload();
            sendMessage(sender, "command.configreloaded");
            return true;
        }

        return true;
    }

    private void sendMessage(CommandSender sender, String path){
        ConfigManager cm = ConfigManager.get();
        String message = cm.getStringColors(path);
        if (!message.equalsIgnoreCase("blank"))
            sender.sendMessage(message);
    }
}
