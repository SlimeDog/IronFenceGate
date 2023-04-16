package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.GateMaker;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocationHelp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private static void placeNormally(BlockPlaceEvent e){
        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

        e.getBlock().setType(Material.BARRIER);

        new GateMaker(location);
        StandManager standManager = new StandManager(location);
        standManager.addBarriers();

        standManager.setYaw((int) Direction.getYaw(opposite));
    }

    private static void placeInsideAgainst(BlockPlaceEvent e){
        Location againstLoc = e.getBlockAgainst().getLocation().add(0.5, 0, 0.5);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));
        if (!StandManager.hasStand(againstLoc)) {
            if (StandManager.hasStand(againstLoc.add(0, -1, 0))) {
                againstLoc.add(0, 1, 0);

                e.setCancelled(true);
                new GateMaker(againstLoc);
                StandManager standManager = new StandManager(againstLoc);
                standManager.addBarriers();

                standManager.setYaw((int) Direction.getYaw(opposite));
                e.getItemInHand().setAmount(e.getItemInHand().getAmount() - 1);
                return;
            }
            placeNormally(e);
            return;
        }
        placeNormally(e);
        return;
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e){

        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);

        Connect connect = new Connect();
        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }
        connect.reconnect(location);

        if (e.getItemInHand().getType().equals(Material.SHULKER_BOX))
            return;

        if (e.getItemInHand().getType() == Material.STONE && e.getItemInHand().getItemMeta() != null && e.getItemInHand().getItemMeta().hasCustomModelData()) {

            if (e.getBlockAgainst().getType() != Material.BARRIER) {

                placeNormally(e);
                return;
            } else {

                placeInsideAgainst(e);
                return;
            }
        }

        if (e.getBlockAgainst().getType() == Material.BARRIER){
            Location againstLoc = e.getBlockAgainst().getLocation().add(0.5, 0, 0.5);
            if (!StandManager.hasStand(againstLoc)) {
                if (StandManager.hasStand(againstLoc.add(0, -1, 0))) {
                    againstLoc.add(0, 1, 0);
                    e.setCancelled(true);
                    againstLoc.getBlock().setType(e.getItemInHand().getType());
                    e.getItemInHand().setAmount(e.getItemInHand().getAmount() - 1);
                }
            }
        }
    }
}
