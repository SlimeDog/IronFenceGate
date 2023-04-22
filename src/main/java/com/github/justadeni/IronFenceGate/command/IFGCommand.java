package com.github.justadeni.IronFenceGate.command;

import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class IFGCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!s.equalsIgnoreCase("ifg") && !s.equalsIgnoreCase("ironfencegate"))
            return true;

        MessageConfig mc = MessageConfig.get();

        if (args.length == 0) {
            mc.sendMessage(sender, "command.tooshort");
            return true;
        }
        if (args.length > 1) {
            mc.sendMessage(sender, "command.toolong");
            return true;
        }

        if (args[0].equalsIgnoreCase("get")){
            if (!sender.hasPermission("ironfencegate.get") && !sender.hasPermission("ironfencegate.admin")) {
                mc.sendMessage(sender, "command.nopermission");
                return true;
            }

            if (sender instanceof Player p){

                if (addItem(p))
                    mc.sendMessage(sender, "command.itemreceived");
                else
                    mc.sendMessage(sender, "command.handsfull");

            } else {
                mc.sendMessage(sender, "command.consolecant");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("ironfencegate.reload") && !sender.hasPermission("ironfencegate.admin")) {
                mc.sendMessage(sender, "command.nopermission");
                return true;
            }

            MainConfig.get().reload();
            MessageConfig.get().reload();
            mc.sendMessage(sender, "command.configreloaded");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            if (!sender.hasPermission("ironfencegate.help") && !sender.hasPermission("ironfencegate.admin")) {
                mc.sendMessage(sender, "command.nopermission");
                return true;
            }

            mc.sendMessage(sender, "command.help");
            return true;
        }

        mc.sendMessage(sender, "command.invalidargs");

        return true;
    }

    private boolean addItem(Player player){
        PlayerInventory inv = player.getInventory();
        if (inv.getItemInMainHand().getType().isAir()){
            inv.setItemInMainHand(Recipe.result());
            return true;
        }
        if (inv.getItemInMainHand().isSimilar(Recipe.result())){
            inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount()+1);
            return true;
        }
        if (inv.getItemInOffHand().getType().isAir()){
            inv.setItemInOffHand(Recipe.result());
            return true;
        }
        if (inv.getItemInOffHand().isSimilar(Recipe.result())){
            inv.getItemInOffHand().setAmount(inv.getItemInOffHand().getAmount()+1);
            return true;
        }
        return false;
    }

}
