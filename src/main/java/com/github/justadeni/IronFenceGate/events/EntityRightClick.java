package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.NonCollision;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EntityRightClick implements Listener {

    @EventHandler
    public static void onEntityRightClick(PlayerInteractAtEntityEvent e){
        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if (e.getRightClicked().getType() != EntityType.PIG)
            return;

        if (!NonCollision.get().has(e.getRightClicked()))
            return;

        Location location = e.getRightClicked().getLocation();

        StandManager standManager = new StandManager(location);

        if (!standManager.hasStand())
            return;

        if (standManager.getState() == State.OPEN)
            standManager.close(e.getPlayer());

        e.setCancelled(true);
    }
}
