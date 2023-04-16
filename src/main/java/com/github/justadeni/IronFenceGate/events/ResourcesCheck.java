package com.github.justadeni.IronFenceGate.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import static org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.*;

public class ResourcesCheck implements Listener {

    @EventHandler
    public static void onResourcesCheck(PlayerResourcePackStatusEvent e){
        if (e.getStatus().equals(DECLINED)){

        }
    }
}
