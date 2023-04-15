package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.misc.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;

public class GateBreak {

    public GateBreak(Location location){

        StandManager standManager = new StandManager(location);
        ArmorStand stand = standManager.getStand();
        stand.remove();
        standManager.removeBarriers();
        ConfigManager cm = ConfigManager.get();
        location.getWorld().playSound(location, Sound.valueOf(cm.getString("sound.break.name")), cm.getFloat("sound.break.volume"), cm.getFloat("sound.break.pitch"));
    }
}
