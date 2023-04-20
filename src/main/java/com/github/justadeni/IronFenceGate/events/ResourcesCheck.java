package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.files.MessageConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;

import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.*;

public class ResourcesCheck implements Listener {

    private static ArrayList<Integer> unloadedPlayers = new ArrayList<>();

    public static boolean isLoaded(String playername){
        return !unloadedPlayers.contains(playername.hashCode());
    }
    /*
    public static void removeLoaded(String playername){
        unloadedPlayers.remove(Integer.valueOf(playername.hashCode()));
    }
    */
    @EventHandler
    public static void onResourcesCheck(PlayerResourcePackStatusEvent e){
        MessageConfig mc = MessageConfig.get();
        Player p = e.getPlayer();

        if (e.getStatus().equals(DECLINED)){
            mc.sendMessage(p, "in-game.packdeclined");
            unloadedPlayers.add(p.getName().hashCode());
            return;
        }

        if (e.getStatus().equals(FAILED_DOWNLOAD)){
            mc.sendMessage(p, "in-game.packfailedload");
            unloadedPlayers.add(p.getName().hashCode());
            return;
        }

        unloadedPlayers.remove(e.getPlayer().getName().hashCode());
    }
}
