package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.IronFenceGate;
import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Connect {

    private static ArrayList<Location> list = new ArrayList<>();

    public void reconnect(Location location){

        if (list.contains(location))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {

                StandManager standManager = new StandManager(location);
                if (standManager.getStand() == null) {
                    list.remove(location);
                    return;
                }
                if (!standManager.isOurs()) {
                    list.remove(location);
                    return;
                }

                Direction direction = Direction.getDirection(standManager.getYaw());

                int id = 0;
                if (standManager.getState() == State.OPEN)
                    id = 4;

                boolean leftSolid = isLeftSolid(location, direction);
                boolean rightSolid = isRightSolid(location, direction);

                if (leftSolid && rightSolid)
                    id += 4;
                else if (rightSolid)
                    id += 2;
                else if (leftSolid)
                    id += 3;
                else
                    id += 1;

                standManager.setId(id);
                list.remove(location);
            }


        }.runTaskLater(IronFenceGate.getInstance(), 10);
    }

    public static boolean isLeftSolid(Location location, Direction direction){
        Block block = switch (direction){
            case SOUTH -> location.getBlock().getRelative(1,0,0);
            case WEST -> location.getBlock().getRelative(0,0,1);
            case NORTH -> location.getBlock().getRelative(-1,0,0);
            case EAST -> location.getBlock().getRelative(0,0,-1);

        };
        return block.getType().isSolid();
    }

    public static boolean isRightSolid(Location location, Direction direction){
        Block block = switch (direction){
            case SOUTH -> location.getBlock().getRelative(-1,0,0);
            case WEST -> location.getBlock().getRelative(0,0,-1);
            case NORTH -> location.getBlock().getRelative(1,0,0);
            case EAST -> location.getBlock().getRelative(0,0,1);

        };
        return block.getType().isSolid();
    }
}
