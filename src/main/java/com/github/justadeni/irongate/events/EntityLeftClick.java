package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.GateBreak;
import com.github.justadeni.irongate.logic.StandManager;
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
        if (e.getDamager().getType() != EntityType.PLAYER)
            return;

        if (e.getEntity().getType() != EntityType.ARMOR_STAND)
            return;

        Location location = e.getEntity().getLocation();//.add(0.5,0,0.5);

        StandManager standManager = new StandManager(location);
        if (!standManager.isOurs())
            return;

        new GateBreak(location, ((Player) e.getDamager()).getGameMode() != GameMode.CREATIVE);
    }

}
