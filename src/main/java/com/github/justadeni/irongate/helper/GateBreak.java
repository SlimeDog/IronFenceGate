package com.github.justadeni.irongate.helper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

public class GateBreak {

    public GateBreak(Location location){

        Helper helper = new Helper(location);
        ArmorStand stand = helper.getStand();
        stand.remove();
        helper.removeBarriers();
    }
}
