package com.github.justadeni.irongate.logic;

import org.bukkit.Location;

public class Connect {

    public void reconnect(Location location){
        Helper helper = new Helper(location);
        if (helper.getStand() == null)
            return;

        if (!helper.isOurs())
            return;


    }

}
