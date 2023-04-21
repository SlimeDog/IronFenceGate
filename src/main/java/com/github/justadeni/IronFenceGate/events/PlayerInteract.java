package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.animation.logic.Task;
import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){

        if (e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        Location location = e.getClickedBlock().getLocation().add(0.5,0,0.5);
        ItemStack itemStack = e.getItem();
        StandManager manager;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        else
            manager = new StandManager(location);

        if (e.getClickedBlock().getType() != Material.BARRIER)
            return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){

            if (manager.getStand() != null){
                if (itemStack != null && StandManager.isValidBlock(itemStack.getType()))
                    e.setCancelled(true);
                else
                    if (e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin"))
                        manager.flipState(e.getPlayer().getLocation());
            } else {
                Location downLoc = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
                StandManager downmanager = new StandManager(downLoc);

                if (downmanager.getStand() != null){
                    if (itemStack != null && (StandManager.isValidBlock(itemStack.getType())))
                        e.setCancelled(true);
                    else
                        if (e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin"))
                            downmanager.flipState(e.getPlayer().getLocation());

                }
            }
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK){

            if (manager.getStand() != null){
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    Gate.delete(location, false, manager);
                } else {
                    if (Task.tracker.contains(location))
                        return;

                    Task.tracker.add(location);
                    Task.track(location, e.getPlayer(), manager);
                }

            }
        }
    }

}
