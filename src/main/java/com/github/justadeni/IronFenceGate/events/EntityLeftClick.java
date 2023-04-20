package com.github.justadeni.IronFenceGate.events;

import com.comphenix.protocol.PacketType;
import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.GateBreak;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.nms.BlockBreaking;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityLeftClick implements Listener {

    @EventHandler
    public static void onEntityLeftClick(EntityDamageByEntityEvent e){

        if (e.getEntity().getType() == EntityType.ARMOR_STAND) {

            Location location = e.getEntity().getLocation();//.add(0.5,0,0.5);

            StandManager standManager = new StandManager(location);
            if (standManager.isOurs()) {
                if (e.getDamager().getType() == EntityType.PLAYER) {
                    Player p = ((Player) e.getDamager());
                    if (p.getGameMode().equals(GameMode.CREATIVE))
                        new GateBreak(location, false);
                    else {
                        location.getBlock().setType(Material.BARRIER);

                        /*
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (standManager.getStand() == null || !standManager.isOurs())
                                    cancel();

                                if (standManager.getState() != State.OPEN)
                                    return;

                                if (!BlockBreaking.isBreaking(location))
                                    location.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(IronFenceGate.getInstance(), 10);
                        */
                    }
                    return;
                }

                //This ensures armorstands only break when it's player damaging them
                e.setCancelled(true);
            }
        }

    }

}
