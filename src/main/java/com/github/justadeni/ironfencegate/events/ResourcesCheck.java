package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.files.MessageConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.*;

public class ResourcesCheck implements Listener {

    private static List<String> unloadedPlayers = new ArrayList<>();

    public static boolean isLoaded(Player player){
        return !unloadedPlayers.contains(player.getName());
    }

    @EventHandler
    public static void onResourcesCheck(PlayerResourcePackStatusEvent e){
        MessageConfig mc = MessageConfig.getInstance();
        Player p = e.getPlayer();

        if (e.getStatus() == DECLINED){
            mc.sendMessage(p, "in-game.packdeclined");
            unloadedPlayers.add(p.getName());
            return;
        }

        if (e.getStatus() == FAILED_DOWNLOAD){
            mc.sendMessage(p, "in-game.packfailedload");
            unloadedPlayers.add(p.getName());
            return;
        }

        unloadedPlayers.remove(p.getName());
    }
}
