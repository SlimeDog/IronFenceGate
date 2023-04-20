package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocationHelp;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlace implements Listener {

    private static void placeNormally(BlockPlaceEvent e){
        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

        e.getBlock().setType(Material.BARRIER);

        Gate.create(location);
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
                Gate.create(againstLoc);
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

        ItemStack itemStack = e.getItemInHand();

        if (itemStack.getType().equals(Material.SHULKER_BOX))
            return;

        StandManager manager = new StandManager(location);
        if (manager.getStand() != null)
            if (manager.getState() == State.OPEN){
                e.setCancelled(true);
                return;
            }

        Location againstLoc = e.getBlockAgainst().getLocation().add(0.5, 0, 0.5);
        if (StandManager.hasStand(againstLoc))
            return;

        if (itemStack.getType() == Material.WARPED_FENCE_GATE && itemStack.getItemMeta() != null && itemStack.getItemMeta().hasCustomModelData()) {

            if (!ResourcesCheck.isLoaded(e.getPlayer().getName()))
                return;

            if (e.getBlockAgainst().getType() == Material.BARRIER) {
                placeInsideAgainst(e);
            } else {
                placeNormally(e);
            }
            return;
        }

        if (e.getBlockAgainst().getType() == Material.BARRIER){
            if (StandManager.hasStand(againstLoc.add(0, -1, 0))) {
                if(itemStack.getType().isOccluding() || itemStack.getType().name().endsWith("GLASS")) {
                    againstLoc.add(0, 1, 0);
                    e.setCancelled(true);
                    againstLoc.getBlock().setType(e.getItemInHand().getType());
                    e.getItemInHand().setAmount(e.getItemInHand().getAmount() - 1);
                }
            }
        }
    }
}
