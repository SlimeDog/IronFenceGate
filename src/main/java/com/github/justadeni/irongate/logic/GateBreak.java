package com.github.justadeni.irongate.logic;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class GateBreak {

    public GateBreak(Location location){

        Helper helper = new Helper(location);
        ArmorStand stand = helper.getStand();
        stand.remove();
        helper.removeBarriers();
    }
}
