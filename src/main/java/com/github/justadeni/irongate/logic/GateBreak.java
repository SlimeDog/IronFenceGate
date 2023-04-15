package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.misc.ConfigManager;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;

public class GateBreak {

    public GateBreak(Location location, boolean drop){

        StandManager standManager = new StandManager(location);
        ArmorStand stand = standManager.getStand();
        stand.remove();
        standManager.removeBarriers();
        ConfigManager cm = ConfigManager.get();
        location.getWorld().playSound(location, Sound.valueOf(cm.getString("sound.break.name")), cm.getFloat("sound.break.volume"), cm.getFloat("sound.break.pitch"));
        if (drop)
            location.getWorld().dropItemNaturally(new Location(location.getWorld(), location.getX(), location.getY()+0.5, location.getZ()), Recipe.recipes.get(0).getResult());
    }
}
