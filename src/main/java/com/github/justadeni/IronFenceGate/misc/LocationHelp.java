package com.github.justadeni.IronFenceGate.misc;

import org.bukkit.Location;

import java.util.ArrayList;

public class LocationHelp {

    public static ArrayList<Location> getLocsAround(Location location){
        ArrayList<Location> locations = new ArrayList<>(4);
        locations.add(new Location(location.getWorld(), location.getX()-1, location.getY(), location.getZ()));
        locations.add(new Location(location.getWorld(), location.getX()+1, location.getY(), location.getZ()));
        locations.add(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()-1));
        locations.add(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()+1));
        return locations;
    }

}
