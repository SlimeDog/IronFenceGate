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
            if (sender.hasPermission("ironfencegate.admin")) {
                completions.add("reload");
                completions.add("get");
                completions.add("help");
            }
        }
        return completions;
    }
}
