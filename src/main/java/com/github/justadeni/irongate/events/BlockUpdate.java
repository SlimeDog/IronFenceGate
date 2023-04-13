package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.Connect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockUpdate implements Listener {

    private static final Connect connect = new Connect();

    @EventHandler
    public static void onBlockUpdated(BlockPhysicsEvent e){
        connect.reconnect(e.getBlock().getLocation());
    }

}
