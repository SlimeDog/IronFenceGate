package com.github.justadeni.IronFenceGate.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("ironfencegate.admin") || sender.hasPermission("ironfencegate.get"))
                completions.add("get");
            if (sender.hasPermission("ironfencegate.admin") || sender.hasPermission("ironfencegate.help"))
                completions.add("help");
            if (sender.hasPermission("ironfencegate.admin"))
                completions.add("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")){
            if (sender.hasPermission("ironfencegate.admin") || sender.hasPermission("ironfencegate.get")) {
                completions.add("number");
            }
        }
        return completions;
    }
}
