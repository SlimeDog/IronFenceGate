package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.logic.Connect;
import com.github.justadeni.irongate.logic.GateMaker;
import com.github.justadeni.irongate.logic.StandManager;
import com.github.justadeni.irongate.misc.LocationHelp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BlockPlace implements Listener {

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e){

        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);

        Connect connect = new Connect();
        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }
        connect.reconnect(location);

        if (e.getItemInHand().getType() != Material.STONE)
            return;

        if (e.getItemInHand().getItemMeta() == null)
            return;

        if (!e.getItemInHand().getItemMeta().hasCustomModelData())
            return;

        e.getBlock().setType(Material.BARRIER);

        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

        new GateMaker(location);
        StandManager standManager = new StandManager(location);
        standManager.addBarriers();

        standManager.setYaw((int) Direction.getYaw(opposite));
    }
}
