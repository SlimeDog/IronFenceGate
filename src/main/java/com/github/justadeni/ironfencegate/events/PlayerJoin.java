package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.logic.NonCollision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e){
        NonCollision nonCollision = NonCollision.getInstance();
        nonCollision.add(e.getPlayer());
        e.getPlayer().setScoreboard(nonCollision.getBoard());
    }
}
