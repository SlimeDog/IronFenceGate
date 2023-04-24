package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreak implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e){

        Location location = e.getBlock().getLocation().add(0.5,0,0.5);

        Connect.around(location);

        location.add(0,-1,0);

        StandManager manager = new StandManager(location);
        if (!manager.hasStand())
            return;

        if (manager.getState() == State.OPEN)
            return;

        new BukkitRunnable(){
            @Override
            public void run() {
                location.add(0,1,0).getBlock().setType(Material.BARRIER);
            }
        }.runTaskLater(IronFenceGate.get(), 2);
    }

}
