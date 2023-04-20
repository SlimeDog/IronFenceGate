package com.github.justadeni.IronFenceGate.animation.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Task {

    public static boolean isBreaking(Location location){
        return tracker.contains(location)/* && !toRemove.contains(location)*/;
    }

    public static List<Location> tracker = new ArrayList<>();

    public static void track(Location location, Player player, StandManager manager){
        new BukkitRunnable() {
            //from 1 to 9 stages of breaking
            double progress=0.0;
            //first index marks start time, second gets updated every function run
            long start=System.currentTimeMillis();
            long current = System.currentTimeMillis();
            @Override
            public void run() {
                try {
                    //Timeout if block isn't broken after 6 seconds
                    if (start + 3*1000 < current){
                        end(location);
                        cancel();
                        return;
                    }

                    //Could have been removed or is due to be removed
                    if (!tracker.contains(location)) {
                        end(location);
                        cancel();
                        return;
                    }

                    //Stand could've been broken or deleted
                    if (player == null || location.getBlock().isEmpty()) {
                        end(location);
                        cancel();
                        return;
                    }

                    if (progress >= 9.0) {
                        //This is the only thing that has to run sync
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Gate.delete(location, true, manager);
                            }
                        }.runTask(IronFenceGate.getInstance());
                        end(location);
                        cancel();
                        return;
                    }

                    /*
                    //should be 0.1 second but supports auto-throttle should that be implemented
                    double delta = (System.currentTimeMillis() - start[1]) / 1000f;
                    start[1] = System.currentTimeMillis();
                    */
                    Material material = player.getInventory().getItemInMainHand().getType();

                    progress += 0.1 * switch (material) {
                        case IRON_PICKAXE -> 7; //takes about 1.6 sec to break
                        case DIAMOND_PICKAXE -> 9; //takes about 1.3 sec to break
                        case NETHERITE_PICKAXE -> 11; //takes about 1.1 sec to break
                        default -> 0;
                    };

                    //Total of 9 stages, how fast you reach them depends on pickaxe
                    int decaProgress = (int) Math.floor(progress) * 10;
                    if (decaProgress > manager.getDecaId()) {
                        //Bukkit.broadcastMessage("stage:"+(store.manager.getId() + decaProgress));
                        manager.setId(manager.getId() + decaProgress);
                    }
                } catch (NullPointerException e){
                    end(location);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(IronFenceGate.getInstance(), 0, 2);
    }

    private static void end(Location location){

        //Bukkit.broadcastMessage("end animation");

        tracker.remove(location);
        StandManager manager = new StandManager(location);
        if (manager.getStand() == null)
            return;

        manager.setId(manager.getId());

        if (location.getBlock().getType().equals(Material.BARRIER)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.getBlock().setType(Material.AIR);
                }
            }.runTask(IronFenceGate.getInstance());
        }
    }
}
