package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.logic.GateBreak;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityLeftClick implements Listener {

    @EventHandler
    public static void onEntityLeftClick(EntityDamageByEntityEvent e){

        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {

            Location location = e.getEntity().getLocation();//.add(0.5,0,0.5);

            StandManager standManager = new StandManager(location);
            if (standManager.isOurs()) {
                if (e.getDamager().getType() == EntityType.PLAYER) {
                    new GateBreak(location, ((Player) e.getDamager()).getGameMode() != GameMode.CREATIVE);
                    return;
                }

                //This ensures armorstands only break when it's player damaging them
                e.setCancelled(true);
            }
        }

    }

}
