package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.misc.LocationHelp;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {

        Location location = e.getBlockPlaced().getLocation().add(0.5, 0, 0.5);

        Connect connect = new Connect();
        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }
        connect.reconnect(location);
    }
}
