package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class Connect {

    public void reconnect(Location location){
        Helper helper = new Helper(location);
        if (helper.getStand() == null)
            return;

        if (!helper.isOurs())
            return;

        //Location standLocation = helper.stand.getLocation();
        Direction direction = Direction.getDirection(helper.getYaw());

        int id = 0;
        if (helper.getState() == State.OPEN)
            id = 4;

        boolean leftSolid = isLeftSolid(location, direction);
        boolean rightSolid = isRightSolid(location, direction);

        if (leftSolid && rightSolid)
            id +=4;
        else if (rightSolid)
            id += 3;
        else if (leftSolid)
            id +=2;
        else
            id +=1;

        helper.setId(id);
    }

    private boolean isLeftSolid(Location location, Direction direction){
        return (new Location(location.getWorld(), location.getX()+xDelta(direction), location.getY(), location.getZ()+zDelta(direction)).getBlock().getType().isSolid());
    }

    private boolean isRightSolid(Location location, Direction direction){
        return (new Location(location.getWorld(), location.getX()-xDelta(direction), location.getY(), location.getZ()-zDelta(direction)).getBlock().getType().isSolid());
    }

    private int xDelta(Direction direction){
        return switch (direction){
            case NORTH -> -1;
            case SOUTH -> 1;
            default -> 0;
        };
    }
    private int zDelta(Direction direction){
        return switch (direction){
            case EAST -> -1;
            case WEST -> 1;
            default -> 0;
        };
    }
}
