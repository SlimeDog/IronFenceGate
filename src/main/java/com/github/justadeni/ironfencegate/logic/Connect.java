package com.github.justadeni.ironfencegate.logic;

import com.github.justadeni.ironfencegate.IronFenceGate;
import com.github.justadeni.ironfencegate.enums.Direction;
import com.github.justadeni.ironfencegate.enums.State;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Connect {

    private final List<String> WHITELISTED = Collections.unmodifiableList(Arrays.asList("_FENCE", "_WALL", "IRON_BARS"));

    private final Location location;

    public Connect(Location location){
        this.location = location;
    }

    public void around(){
        new BukkitRunnable() {
            @Override
            public void run() {

                final List<Location> locations = Arrays.asList(
                        location,
                        new Location(location.getWorld(), location.getX()-1, location.getY(), location.getZ()),
                        new Location(location.getWorld(), location.getX()+1, location.getY(), location.getZ()),
                        new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()-1),
                        new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()+1));

                for (Location potentialLoc : locations) {
                    StandManager standManager = new StandManager(potentialLoc);
                    if (!standManager.hasStand()) {
                        continue;
                    }

                    Direction direction = Direction.getDirection(standManager.getYaw());

                    int id = 0;
                    if (standManager.getState() == State.OPEN)
                        id = 4;

                    boolean leftSolid = isSolid(potentialLoc, direction, false);
                    boolean rightSolid = isSolid(potentialLoc, direction, true);

                    if (leftSolid && rightSolid)
                        id += 4;
                    else if (rightSolid)
                        id += 2;
                    else if (leftSolid)
                        id += 3;
                    else
                        id += 1;

                    standManager.setId(id + standManager.getDecaId());
                }
            }
        }.runTaskLaterAsynchronously(IronFenceGate.getInstance(), 3);
    }

    /**
     * Checks whether blocks to the right and left are solid and if they're not blacklisted in config
     * @param location location of the gate
     * @param direction direction in which the gate is facing
     * @param right whether it should check to it's right, if not then left
     * @return returns true if our block can be connected to
     */
    private boolean isSolid(Location location, Direction direction, boolean right){
        int i = right ? -1 : 1;

        Block block = switch (direction){
            case SOUTH -> location.getBlock().getRelative(i,0,0);
            case WEST -> location.getBlock().getRelative(0,0,i);
            case NORTH -> location.getBlock().getRelative(-i,0,0);
            case EAST -> location.getBlock().getRelative(0,0,-i);
        };

        String materialName = block.getType().name();
        for (String potential : WHITELISTED)
            if (materialName.endsWith(potential))
                return true;

        return false;
    }
}
