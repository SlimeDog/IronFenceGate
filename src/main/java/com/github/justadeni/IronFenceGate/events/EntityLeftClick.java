package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.animation.logic.Task;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityLeftClick implements Listener {

    @EventHandler
    public static void onEntityLeftClick(EntityDamageByEntityEvent e){

        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {

            Location location = e.getEntity().getLocation();//.add(0.5,0,0.5);
            //Location eloc = e.getEntity().getLocation();
            //Location location = new Location(e.getEntity().getWorld(), Math.floor(eloc.getX())+0.5, Math.floor(eloc.getY()), Math.floor(eloc.getZ())+0.5);

            Bukkit.broadcastMessage("Entity 1");

            StandManager standManager = new StandManager(location);
            if (standManager.getStand() != null) {

                Bukkit.broadcastMessage("Entity 2");

                if (e.getDamager().getType() == EntityType.PLAYER) {
                    Player player = ((Player) e.getDamager());
                    if (player.getGameMode().equals(GameMode.CREATIVE))
                        Gate.delete(location, false, standManager);
                    else {

                        /*
                        location.getBlock().setType(Material.BARRIER);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!Task.isBreaking(location)) {
                                    location.getBlock().setType(Material.AIR);
                                }
                            }
                        }.runTaskLater(IronFenceGate.getInstance(), 10);
                        */
                        //Started breaking
                        Bukkit.broadcastMessage("Entity 3");

                        if (Task.tracker.contains(location))
                            return;

                        Bukkit.broadcastMessage("Entity 4");

                        Task.tracker.add(location);
                        Task.track(location, player, standManager);

                    }
                    return;
                }

                //This ensures armorstands only break when it's player damaging them
                e.setCancelled(true);
            }
        }

    }

}
