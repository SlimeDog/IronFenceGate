package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.IronFenceGate;
import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Connect {

    public void reconnect(Location location){

        new BukkitRunnable() {
            @Override
            public void run() {

                StandManager standManager = new StandManager(location);
                if (standManager.getStand() == null) {
                    return;
                }
                if (!standManager.isOurs()) {
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

                standManager.setId(id);
            }
        }.runTaskLater(IronFenceGate.getInstance(), 5);
    }

    public static boolean isSolid(Location location, Direction direction, boolean right){
        int i = 1;
        if (right)
            i = -1;

        Block block = switch (direction){
            case SOUTH -> location.getBlock().getRelative(1*i,0,0);
            case WEST -> location.getBlock().getRelative(0,0,1*i);
            case NORTH -> location.getBlock().getRelative(-1*i,0,0);
            case EAST -> location.getBlock().getRelative(0,0,-1*i);
        };
        return block.getType().isSolid() && !block.getType().equals(Material.BARRIER);
    }
}
