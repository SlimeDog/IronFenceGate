package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.logic.GateMaker;
import com.github.justadeni.irongate.logic.Helper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getType() != Material.STONE)
            return;

        if (e.getItemInHand().getItemMeta() == null)
            return;

        if (!e.getItemInHand().getItemMeta().hasCustomModelData())
            return;

        e.getBlock().setType(Material.BARRIER);

        Location location = e.getBlock().getLocation();
        Direction opposite = Direction.getOpposite(Direction.getDirection(location.getYaw()));

        new GateMaker(e.getBlock().getLocation());
        new Helper(e.getBlock().getLocation()).addBarriers();
    }
}
