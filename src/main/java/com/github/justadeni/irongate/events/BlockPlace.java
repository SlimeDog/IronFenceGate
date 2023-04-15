package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.IronFenceGate;
import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.logic.Connect;
import com.github.justadeni.irongate.logic.GateMaker;
import com.github.justadeni.irongate.logic.StandManager;
import com.github.justadeni.irongate.misc.LocationHelp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BlockPlace implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onBlockPlace(BlockPlaceEvent e){

        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);

        Connect connect = new Connect();
        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }
        connect.reconnect(location);

        if (e.getItemInHand().getType() == Material.STONE && e.getItemInHand().getItemMeta() != null && e.getItemInHand().getItemMeta().hasCustomModelData()) {


            e.getBlock().setType(Material.BARRIER);

            Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

            new GateMaker(location);
            StandManager standManager = new StandManager(location);
            standManager.addBarriers();

            standManager.setYaw((int) Direction.getYaw(opposite));

        } else if (e.getBlockAgainst().getType() == Material.BARRIER){
            Location againstLoc = e.getBlockAgainst().getLocation().add(0.5,0,0.5);
            StandManager testManager1 = new StandManager(againstLoc);
            if (testManager1.getStand() == null){
                StandManager testManager2 = new StandManager(againstLoc.add(0,-1,0));
                if (testManager2.getStand() != null){
                    againstLoc.add(0,1,0);
                    e.setCancelled(true);

                    Bukkit.getPluginManager().callEvent(new BlockPlaceEvent(againstLoc.getBlock(), e.getBlockReplacedState(),
                            againstLoc.getBlock(), e.getItemInHand(), e.getPlayer(), true, e.getHand()));

                }
            }

        }
    }
}
