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

            Location location = e.getEntity().getLocation();
            StandManager standManager = new StandManager(location);
            if (standManager.getStand() != null) {

                if (e.getDamager().getType() == EntityType.PLAYER) {
                    Player player = ((Player) e.getDamager());
                    if (player.getGameMode().equals(GameMode.CREATIVE))
                        Gate.delete(location, false, standManager);
                    else {

                        if (Task.tracker.contains(location))
                            return;

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
