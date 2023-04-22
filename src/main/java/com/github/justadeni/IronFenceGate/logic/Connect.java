package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.enums.State;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class Connect {

    /**
     * Figures out orientation of the fence gate and connection to blocks to the right and left
     * @param location location of gate
     */
    public void reconnect(Location location){

        new BukkitRunnable() {
            @Override
            public void run() {

                StandManager standManager = new StandManager(location);
                if (standManager.hasStand()) {
                    return;
                }

                Direction direction = Direction.getDirection(standManager.getYaw());

                int id = 0;
                if (standManager.getState() == State.OPEN)
                    id = 4;

                boolean leftSolid = isSolid(location, direction, false);
                boolean rightSolid = isSolid(location, direction, true);

                if (leftSolid && rightSolid)
                    id += 4;
                else if (rightSolid)
                    id += 2;
                else if (leftSolid)
                    id += 3;
                else
                    id += 1;

                standManager.setId(id+standManager.getDecaId());
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
    private static boolean isSolid(Location location, Direction direction, boolean right){
        int i = 1;
        if (right)
            i = -1;

        Block block = switch (direction){
            case SOUTH -> location.getBlock().getRelative(i,0,0);
            case WEST -> location.getBlock().getRelative(0,0,i);
            case NORTH -> location.getBlock().getRelative(-i,0,0);
            case EAST -> location.getBlock().getRelative(0,0,-i);
        };

        String materialName = block.getType().name();
        for (String potential : whitelisted)
            if (materialName.endsWith(potential))
                return true;

        return false;
    }

    private static final ArrayList<String> whitelisted = new ArrayList<>(Arrays.asList("_FENCE", "_WALL", "IRON_BARS"));
}
