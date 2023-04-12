package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.helper.GateBreak;
import com.github.justadeni.irongate.helper.GateMaker;
import com.github.justadeni.irongate.helper.Helper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            Helper helper = new Helper(e.getClickedBlock().getLocation());
            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            helper.flipState();

        } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){

            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            if (new Helper(e.getClickedBlock().getLocation()).getStand() == null)
                return;

            new GateMaker(e.getClickedBlock().getLocation());
        }
    }

}
