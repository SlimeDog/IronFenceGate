package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.enums.State;
import com.github.justadeni.irongate.logic.GateBreak;
import com.github.justadeni.irongate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteract implements Listener {

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){

        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        Location location;
        StandManager standManager;

        switch (e.getAction()){
            case RIGHT_CLICK_BLOCK, LEFT_CLICK_BLOCK -> {
                if (e.getClickedBlock().getType() != Material.BARRIER) {

                    location = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
                    StandManager manager = new StandManager(location);
                    if (manager.getStand() != null)
                        standManager = manager;
                    else
                        return;
                } else {
                    location = e.getClickedBlock().getLocation().add(0.5, 0, 0.5);
                    StandManager manager = new StandManager(location);

                    if (manager.getStand() != null) {
                        standManager = manager;
                    } else {
                        location.add(0, -1, 0);
                        manager = new StandManager(location);
                        if (manager.getStand() != null)
                            standManager = manager;
                        else
                            return;
                    }
                }
            }
            default -> {return;}
        }

        if (!standManager.isOurs())
            return;

        switch (e.getAction()){
            case RIGHT_CLICK_BLOCK -> {
                if (e.getMaterial().equals(Material.AIR))
                    standManager.flipState(e.getPlayer().getLocation());
            }
            case LEFT_CLICK_BLOCK -> {
                new GateBreak(location);
            }
        }
    }

}
