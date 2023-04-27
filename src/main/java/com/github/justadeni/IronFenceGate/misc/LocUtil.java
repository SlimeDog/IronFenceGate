package com.github.justadeni.IronFenceGate.misc;

import org.bukkit.Location;

public class LocUtil {

    public static Location alter(Location location, double x, double y, double z){
        return new Location(location.getWorld(), location.getX()+x, location.getY()+y, location.getZ()+z);
    }

    public static Location center(Location location){
        return new Location(location.getWorld(), Math.floor(location.getX())+0.5, Math.floor(location.getY()), Math.floor(location.getZ())+0.5);
    }

}
