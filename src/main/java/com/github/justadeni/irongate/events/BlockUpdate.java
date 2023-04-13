package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.StandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockUpdate implements Listener {

    @EventHandler
    public static void onBlockUpdate(BlockPhysicsEvent e){
        StandManager standManager = new StandManager(e.getBlock().getLocation());
        if (standManager.stand == null)
            return;

        if (e.getBlock().isBlockPowered()){

        }
    }

}
