package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.IronFenceGate;
import com.github.justadeni.ironfencegate.enums.State;
import com.github.justadeni.ironfencegate.logic.Connect;
import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        Location location = LocUtil.center(e.getBlock().getLocation());

        new Connect(location).around();

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
        }.runTaskLater(IronFenceGate.getInstance(), 2);
    }

}
