package com.github.justadeni.irongate.enums;

import org.bukkit.Location;

public enum Direction{
    NORTH(0),
    SOUTH(180),
    EAST(90),
    WEST(270);

    private static final Direction[] directions = {SOUTH, WEST, NORTH, EAST};

    public static Direction getDirection(Location location) {
        return getDirection(location.getYaw());
    }
    public static Direction getDirection(float yaw) {
        return directions[((int)(yaw+45F)%360)/90];
    }
    public static Direction getOpposite(Direction direction){
        return switch (direction){
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            //default -> direction;
        };
    }
    public static float getYaw(Direction direction){
        return direction.id;
    }

    private final int id;

    private Direction(int id){
        this.id = id;
    }
}
