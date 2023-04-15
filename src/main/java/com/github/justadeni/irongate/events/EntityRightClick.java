package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.StandManager;
import com.github.justadeni.irongate.misc.ConfigManager;
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

        if (e.getPlayer().hasPermission("ironfencegate.use"))
            standManager.flipState(e.getPlayer().getLocation());
        else
            ConfigManager.get().sendMessage(e.getPlayer(), "ingame.nopermission");
    }

}
