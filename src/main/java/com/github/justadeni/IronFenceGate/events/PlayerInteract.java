package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.logic.GateBreak;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
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

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        if (e.getClickedBlock().getType() == Material.BARRIER) {
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
        } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (e.getClickedBlock().getType() != Material.BARRIER) {

                location = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
                StandManager manager = new StandManager(location);
                if (manager.getStand() != null)
                    standManager = manager;
                else
                    return;
            } else
                return;
        } else
            return;

        if (!standManager.isOurs())
            return;

        switch (e.getAction()){
            case RIGHT_CLICK_BLOCK -> {
                if (e.getMaterial().equals(Material.AIR))
                    if (e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin"))
                        standManager.flipState(e.getPlayer().getLocation());
                    else
                        MessageConfig.get().sendMessage(e.getPlayer(), "ingame.nopermission");
            }
            case LEFT_CLICK_BLOCK -> {
                new GateBreak(location, e.getPlayer().getGameMode() != GameMode.CREATIVE);
            }
        }
    }

}
