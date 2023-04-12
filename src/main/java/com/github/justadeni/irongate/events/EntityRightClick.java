package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.helper.Helper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityRightClick implements Listener {

    @EventHandler
    public static void onEntityRightClick(PlayerInteractEntityEvent e){
        if (e.getRightClicked().getType() != EntityType.ARMOR_STAND)
            return;

        Helper helper = new Helper(e.getRightClicked().getLocation());

        if (!helper.isOurs())
            return;

        helper.flipState();
    }

}
