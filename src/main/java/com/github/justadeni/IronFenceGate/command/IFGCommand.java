package com.github.justadeni.IronFenceGate.command;

import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.misc.LocUtil;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

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
        if (args.length > 2) {
            mc.sendMessage(sender, "command.toolong");
            return true;
        }

        if (args[0].equalsIgnoreCase("get")){
            if (sender instanceof Player p){
                if (((Player) sender).getGameMode() != GameMode.CREATIVE && !sender.hasPermission("ironfencegate.get") && !sender.hasPermission("ironfencegate.admin")){
                    mc.sendMessage(sender, "command.nopermission");
                    return true;
                }

                int amount = args.length == 1 ? 1 : getNumeric(args[1]);
                if (amount > 0)
                    mc.sendMessage(sender, addItem(p, amount) ? "command.itemreceived" : "command.invfull");
                else
                    mc.sendMessage(sender, "command.invalidargs");

            } else {
                mc.sendMessage(sender, "command.consolecant");
            }
            return true;
        }
        if (args.length == 2) {
            mc.sendMessage(sender, "command.invalidargs");
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

    private boolean addItem(Player player, int amount){
        PlayerInventory inv = player.getInventory();
        Location location = LocUtil.alter(player.getLocation(), 0,0.3,0);
        HashMap<Integer, ItemStack> ifgslots = new HashMap<>();
        ArrayList<Integer> emptyslots = new ArrayList<>();

        for (int i = 0; i <= 35; i++){
            ItemStack itemStack = inv.getItem(i);
            if (itemStack == null || itemStack.getType().isAir()){
                emptyslots.add(i);
                continue;
            }
            if (itemStack.isSimilar(Recipe.result()) && itemStack.getAmount() < 64){
                ifgslots.put(i, itemStack);
                continue;
            }
        }
        //First we put it into slots that contain IronFenceGate item already
        for (int key : ifgslots.keySet()){
            if (amount == 0)
                continue;

            ItemStack itemStack = ifgslots.get(key);
            int amountfree = 64 - itemStack.getAmount();

            //if (amountfree == 0)
            //    continue;

            if (amountfree >= amount){
                itemStack.setAmount(itemStack.getAmount()+amount);
                amount = 0;
            } else {
                itemStack.setAmount(64);
                amount -= amountfree;
            }
            //ifgslots.put(key, itemStack);
        }
        //Then we put it into free slots
        if (amount != 0){
            int divided = amount <= emptyslots.size()*64 ? (int) Math.floor(amount/64f) : emptyslots.size()*64;
            int rest = amount < emptyslots.size()*64 ? amount-64*divided : 0;

            amount -= divided*64 + rest;

            for (int slot : emptyslots){
                if (divided == 0 && rest == 0)
                    break;

                ItemStack itemStack = Recipe.result().clone();
                if (divided > 0) {
                    itemStack.setAmount(64);
                    divided--;
                } else if (rest > 0){
                    itemStack.setAmount(rest);
                    rest = 0;
                }
                ifgslots.put(slot, itemStack);
            }
        }

        //We put the items in player's inventory
        for (int slot : ifgslots.keySet()){
            inv.setItem(slot, ifgslots.get(slot));
        }

        //Finally we drop the rest on the ground
        if (amount != 0){
            ItemStack itemStack = Recipe.result().clone();
            itemStack.setAmount(64);
            location.getWorld().dropItemNaturally(location, itemStack);

            return false;
        }
        return true;
    }

    private static int getNumeric(String str) {
        if (str == null) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
