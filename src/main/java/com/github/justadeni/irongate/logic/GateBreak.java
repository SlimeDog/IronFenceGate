package com.github.justadeni.irongate.logic;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class GateBreak {

    public GateBreak(Location location){

        StandManager standManager = new StandManager(location);
        ArmorStand stand = standManager.getStand();
        stand.remove();
        standManager.removeBarriers();
    }
}
