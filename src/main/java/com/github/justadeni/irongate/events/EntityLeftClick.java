package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.helper.GateBreak;
import com.github.justadeni.irongate.helper.Helper;
import org.bukkit.entity.EntityType;
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

        Helper helper = new Helper(e.getEntity().getLocation());
        if (!helper.isOurs())
            return;

        new GateBreak(e.getEntity().getLocation());
    }

}
