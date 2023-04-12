package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.logic.GateBreak;
import com.github.justadeni.irongate.logic.Helper;
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

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            Helper helper = new Helper(e.getClickedBlock().getLocation());

            if (!helper.isOurs())
                return;

            helper.flipState();

        } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){

            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            Helper helper = new Helper(e.getClickedBlock().getLocation());

            if (helper.getStand() == null)
                return;

            if (!helper.isOurs())
                return;

            new GateBreak(e.getClickedBlock().getLocation());

        } /*else if (e.getAction().equals(Action.RIGHT_CLICK_AIR)){

            Helper helper = new Helper(e.getClickedBlock().getLocation());

            if (helper.stand == null)
                return;

            if (!helper.isOurs())
                return;

            helper.flipState();
        }*/
    }

}
