package com.github.justadeni.ironfencegate.logic;

import com.github.justadeni.ironfencegate.IronFenceGate;
import com.github.justadeni.ironfencegate.enums.Direction;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import com.github.justadeni.ironfencegate.nms.entity.CustomArmorstand;
import com.github.justadeni.ironfencegate.enums.State;
import com.github.justadeni.ironfencegate.files.MainConfig;
import com.github.justadeni.ironfencegate.nms.entity.CustomPig;
import com.github.justadeni.ironfencegate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class Gate {

    private Gate(){}

    public static void delete(Location location, boolean drop, StandManager standManager){
        standManager.removeStand();
        standManager.removeBarriers(1);
        MainConfig mc = MainConfig.getInstance();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.break.name")), mc.getFloat("sound.break.volume"), mc.getFloat("sound.break.pitch"));
        if (drop)
            location.getWorld().dropItemNaturally(new Location(location.getWorld(), location.getX(), location.getY()+0.5, location.getZ()), Recipe.getInstance().getResult());

        CustomPig.remove(location);

        StandManager lowerStand = new StandManager(LocUtil.alter(location, 0,-1,0));
        if (lowerStand.hasStand() && lowerStand.getState() == State.CLOSED) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.getBlock().setType(Material.BARRIER);
                }
            }.runTaskLater(IronFenceGate.getInstance(), 2);
        }
    }

    public static void create(Location location, Player player){

        CustomArmorstand.spawn(location);
        CustomPig.spawn(location);

        MainConfig mc = MainConfig.getInstance();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.place.name")), mc.getFloat("sound.place.volume"), mc.getFloat("sound.place.pitch"));

        StandManager standManager = new StandManager(location);
        standManager.setYaw((int) Direction.getYaw(Direction.getOpposite(Direction.getDirection(player.getLocation()))));

        standManager.addBarriers(2);

        new Connect(location).around();
    }
}
