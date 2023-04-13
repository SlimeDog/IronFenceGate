package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.Connect;
import com.github.justadeni.irongate.misc.Helpers;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e){

        Connect connect = new Connect();

        Location location = e.getBlock().getLocation().add(0.5,0,0.5);

        for (Location loc : Helpers.getLocsAround(location)) {
            connect.reconnect(loc);
        }
    }

}
