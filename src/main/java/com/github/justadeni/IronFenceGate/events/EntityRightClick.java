package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.logic.StandManager;
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

        if (e.getRightClicked().getType() != EntityType.ARMOR_STAND)
            return;

        Location location = e.getRightClicked().getLocation();

        StandManager standManager = new StandManager(location);

        if (!standManager.isOurs())
            return;

        if (e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin"))
            standManager.flipState(e.getPlayer().getLocation());
        else
            MessageConfig.get().sendMessage(e.getPlayer(), "in-game.nopermission");
    }

}
