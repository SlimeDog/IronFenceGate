package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.logic.Connect;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        Location location = LocUtil.center(e.getBlockPlaced().getLocation());

        new Connect(location).around();
    }
}
