package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.hitbox.NonCollision;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e){
        NonCollision.get().add(e.getPlayer());
        e.getPlayer().setScoreboard(NonCollision.get().getBoard());
    }
}
