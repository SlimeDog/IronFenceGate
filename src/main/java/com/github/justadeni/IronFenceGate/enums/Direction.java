package com.github.justadeni.IronFenceGate.enums;

import org.bukkit.Location;

public enum Direction{
    SOUTH(0),
    WEST(90),
    NORTH(180),
    EAST(270);

    public static Direction getDirection(Location location) {

        float yaw = location.getYaw();

        if (yaw < 0){
            yaw = 360-Math.abs(yaw);
        }

        return getDirection(yaw);
    }

    public static Direction getDirection(float yaw) {
        if (yaw > 315 || yaw <= 45)
            return SOUTH;
        else if (yaw > 45 && yaw <= 135)
            return WEST;
        else if (yaw > 135 && yaw <= 225)
            return NORTH;
        else if (yaw > 225 && yaw <= 315)
            return EAST;

        return SOUTH;
    }

    public static Direction getOpposite(Direction direction){
        return switch (direction){
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
        };
    }

    public static float getYaw(Direction direction){
        return direction.angle;
    }

    private final int angle;

    private Direction(int id){
        this.angle = id;
    }
}
