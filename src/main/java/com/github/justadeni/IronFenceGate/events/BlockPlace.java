package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.logic.Connect;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {

        Location location = e.getBlockPlaced().getLocation().add(0.5, 0, 0.5);

        Connect.around(location);
        Connect.one(location);
    }
}
