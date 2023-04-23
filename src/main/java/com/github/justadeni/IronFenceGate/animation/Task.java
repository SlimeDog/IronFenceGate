package com.github.justadeni.IronFenceGate.animation;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Task {

    public static List<Location> tracker = new ArrayList<>();

    public static void track(Location location, Player player, StandManager manager){
        new BukkitRunnable() {
            final MainConfig mc = MainConfig.get();
            //from 1 to 9 stages of breaking
            double progress=0.0;
            //first index marks start time, second gets updated every function run
            long start=System.currentTimeMillis();
            long current = System.currentTimeMillis();
            Material material = player.getInventory().getItemInMainHand().getType();

            final int hardness = switch(material){
                case IRON_PICKAXE -> 7; //takes about 1.6 sec to break
                case DIAMOND_PICKAXE -> 9; //takes about 1.3 sec to break
                case NETHERITE_PICKAXE -> 11; //takes about 1.1 sec to break
                default -> 5;
            };

            @Override
            public void run() {
                try {
                    //If was removed and timeout checks
                    if (manager == null || !manager.hasStand() || !tracker.contains(location) || player == null || start + 2000 < current) {
                        end(location);
                        cancel();
                        return;
                    }

                    if (progress >= 9.0) {
                        //This is the only thing that has to run sync
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Gate.delete(location, hardness > 5, manager);
                            }
                        }.runTask(IronFenceGate.getInstance());
                        end(location);
                        cancel();
                        return;
                    }

                    current = System.currentTimeMillis();

                    progress += 0.1 * hardness;

                    //Total of 9 stages, how fast you reach them depends on pickaxe
                    int decaProgress = (int) Math.floor(progress) * 10;
                    if (decaProgress > manager.getDecaId()) {
                        manager.setId(manager.getId() + decaProgress);
                        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.breaking.name")), mc.getFloat("sound.breaking.volume"), mc.getFloat("sound.breaking.pitch"));
                    }

                } catch (NullPointerException e){
                    end(location);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(IronFenceGate.getInstance(), 0, 2);
    }

    private static void end(Location location){
        tracker.remove(location);
        StandManager manager = new StandManager(location);
        if (manager.hasStand())
            return;

        manager.setId(manager.getId());
    }
}