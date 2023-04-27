package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreak implements Listener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e){

        Location location = LocUtil.center(e.getBlock().getLocation());

        Connect.around(location);

        if (e.getBlock().getType() == Material.BARRIER)
            return;

        StandManager manager = new StandManager(LocUtil.alter(location, 0,-1,0));
        if (!manager.hasStand())
            return;

        if (manager.getState() == State.OPEN)
            return;

        new BukkitRunnable(){
            @Override
            public void run() {
                location.getBlock().setType(Material.BARRIER);
            }
        }.runTaskLater(IronFenceGate.get(), 2);
    }

}
