package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocationHelp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreak implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e){

        Connect connect = new Connect();

        Location location = e.getBlock().getLocation().add(0.5,0,0.5);

        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }

        location.add(0,-1,0);

        StandManager standManager = new StandManager(location);
        if (standManager.getStand() == null)
            return;

        location.add(0,1,0);

        new BukkitRunnable(){
            @Override
            public void run() {
                location.getBlock().setType(Material.BARRIER);
            }
        }.runTaskLater(IronFenceGate.getInstance(), 2);
    }

}
