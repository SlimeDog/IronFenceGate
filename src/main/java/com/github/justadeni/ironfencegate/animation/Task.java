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

    public void track(Location location, Player player, StandManager manager){
        tracker.add(location);

        new BukkitRunnable() {

            final long start = System.currentTimeMillis();
            final Material material = player.getInventory().getItemInMainHand().getType();
            final double hardness = switch(material){
                case IRON_PICKAXE -> 0.7; //takes about 1.6 sec to break
                case DIAMOND_PICKAXE -> 0.9; //takes about 1.3 sec to break
                case NETHERITE_PICKAXE -> 0.11; //takes about 1.1 sec to break
                default -> 0.3;
            };

            long current = System.currentTimeMillis();
            double progress = 0.0;

            @Override
            public void run() {
                try {
                    //If was removed and timeout checks
                    if (manager == null || !manager.hasStand() || !tracker.contains(location) || start + 2000 < current) {
                        end(location, hardness);
                        cancel();
                        return;
                    }

                    if (progress >= 9.0) {
                        //This is the only thing that has to run sync
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                end(location, hardness);
                            }
                        }.runTask(IronFenceGate.getInstance());
                        cancel();
                        return;
                    }

                    current = System.currentTimeMillis();

                    progress += hardness;

                    //Total of 9 stages, how fast you reach them depends on pickaxe
                    int decaProgress = (int) Math.floor(progress)*10;
                    if (decaProgress > manager.getDecaId()) {
                        manager.setId(manager.getId() + decaProgress);
                        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.breaking.name")), mc.getFloat("sound.breaking.volume"), mc.getFloat("sound.breaking.pitch"));
                    }

                } catch (NullPointerException e){
                    end(location, hardness);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(IronFenceGate.getInstance(), 0, 2);
    }

    private void end(Location location, double hardness){
        tracker.remove(location);
        StandManager manager = new StandManager(location);
        if (!manager.hasStand())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Gate.delete(location, hardness > 0.5, manager);
            }
        }.runTask(IronFenceGate.getInstance());
    }
}