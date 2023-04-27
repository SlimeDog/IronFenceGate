package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {

        Location location = LocUtil.center(e.getBlockPlaced().getLocation());

        Connect.around(location);
        Connect.one(location);
    }
}
