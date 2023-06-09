package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.enums.State;
import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.logic.NonCollision;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EntityRightClick implements Listener {

    @EventHandler
    public void onEntityRightClick(PlayerInteractAtEntityEvent e){
        if (e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        if (e.getRightClicked().getType() != EntityType.PIG)
            return;

        if (!NonCollision.getInstance().has(e.getRightClicked()))
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
