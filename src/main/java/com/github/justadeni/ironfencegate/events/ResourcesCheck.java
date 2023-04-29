package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.files.MainConfig;
import com.github.justadeni.ironfencegate.files.MessageConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.DECLINED;
import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD;

public class ResourcesCheck implements Listener {

    private static ResourcesCheck resourcesCheck;
    private final List<String> unloadedPlayers = new ArrayList<>();

    private ResourcesCheck(){}

    public static ResourcesCheck getInstance(){
        ResourcesCheck cached = resourcesCheck;
        if (cached == null)
            cached = resourcesCheck = new ResourcesCheck();
        return cached;
    }

    public boolean isLoaded(Player player){
        return !unloadedPlayers.contains(player.getName());
    }

    @EventHandler
    public void onResourcesCheck(PlayerResourcePackStatusEvent e){
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
