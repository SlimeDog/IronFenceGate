package com.github.justadeni.ironfencegate.animation;

import com.github.justadeni.ironfencegate.IronFenceGate;
import com.github.justadeni.ironfencegate.files.MainConfig;
import com.github.justadeni.ironfencegate.logic.Gate;
import com.github.justadeni.ironfencegate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private static final List<Location> tracker = new ArrayList<>();

    public boolean contains(Location location){
        return tracker.contains(location);
    }

    private final MainConfig mc;

    public Task(){
        mc = MainConfig.getInstance();
    }

    public class Track{
        public Track(Location location, Player player, StandManager manager) {
            tracker.add(location);

            new BukkitRunnable() {

                final long start = System.currentTimeMillis();
                final Material material = player.getInventory().getItemInMainHand().getType();
                final double hardness = switch (material) {
                    default -> 0.035;                   // ~25 seconds
                    case WOODEN_PICKAXE -> 0.24;        //3.75 seconds
                    case STONE_PICKAXE -> 0.473;        //1.9 seconds
                    case IRON_PICKAXE -> 0.72;          //1.25 seconds
                    case DIAMOND_PICKAXE -> 0.947;      //0.95 seconds
                    case NETHERITE_PICKAXE -> 1.058;    //0.85 seconds
                    case GOLDEN_PICKAXE -> 1.384;       //0.65 seconds
                };

                long current = System.currentTimeMillis();
                double progress = 0.0;

                @Override
                public void run() {
                    //If was removed and timeout checks
                    if (manager == null || !manager.hasStand() || !tracker.contains(location) || start + 30000 < current) {
                        end(location, hardness > 0.035);
                        cancel();
                        return;
                    }

                    if (progress >= 9.0) {
                        end(location, hardness > 0.035);
                        cancel();
                        return;
                    }

                    current = System.currentTimeMillis();

                    progress += hardness;

                    //Total of 9 stages, how fast you reach them depends on pickaxe
                    int decaProgress = (int) Math.floor(progress) * 10;
                    if (decaProgress > manager.getDecaId()) {
                        manager.setId(manager.getId() + decaProgress);
                        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.breaking.name")), mc.getFloat("sound.breaking.volume"), mc.getFloat("sound.breaking.pitch"));
                    }
                }
            }.runTaskTimer(IronFenceGate.getInstance(), 0, 2);
        }
    }

    private void end(Location location, boolean drop){
        tracker.remove(location);
        StandManager manager = new StandManager(location);
        if (!manager.hasStand())
            return;


        Gate.delete(location, drop, manager);
    }
}