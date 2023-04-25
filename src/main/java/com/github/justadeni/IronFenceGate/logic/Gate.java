package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.nms.entity.CustomArmorstand;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.nms.entity.CustomPig;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class Gate {

    public static void delete(Location location, boolean drop, StandManager standManager){
        standManager.removeStand();
        standManager.removeBarriers(1);
        MainConfig mc = MainConfig.get();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.break.name")), mc.getFloat("sound.break.volume"), mc.getFloat("sound.break.pitch"));
        if (drop)
            location.getWorld().dropItemNaturally(new Location(location.getWorld(), location.getX(), location.getY()+0.5, location.getZ()), Recipe.result());

        CustomPig.remove(location);

        location.add(0,-1,0);
        StandManager lowerStand = new StandManager(location);
        if (lowerStand.hasStand() && lowerStand.getState() == State.CLOSED) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.add(0,1,0).getBlock().setType(Material.BARRIER);
                }
            }.runTaskLater(IronFenceGate.get(), 2);
        }
    }

    public static void create(Location location){

        CustomArmorstand.spawn(location);
        CustomPig.spawn(location);

        MainConfig mc = MainConfig.get();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.place.name")), mc.getFloat("sound.place.volume"), mc.getFloat("sound.place.pitch"));
    }
}
