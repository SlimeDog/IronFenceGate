package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
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

        ItemStack itemStack = e.getItem();
        Location location;
        StandManager standManager;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        if (e.getClickedBlock().getType() == Material.BARRIER) {
            location = e.getClickedBlock().getLocation().add(0.5, 0, 0.5);
            StandManager manager = new StandManager(location);

            if (manager.getStand() != null) {
                standManager = manager;

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
                    if (itemStack != null && (itemStack.getType().isOccluding() || itemStack.getType().name().endsWith("GLASS"))) {
                        e.setCancelled(true);
                        return;
                    }

            } else {
                location.add(0, -1, 0);
                manager = new StandManager(location);
                if (manager.getStand() != null)
                    standManager = manager;
                else
                    return;
            }
        } else
            return;

        switch (e.getAction()){
            case RIGHT_CLICK_BLOCK -> {
                if (e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin"))
                    if (itemStack == null || (!itemStack.getType().isOccluding() && !itemStack.getType().name().endsWith("GLASS")))
                        standManager.flipState(e.getPlayer().getLocation());
                else
                    MessageConfig.get().sendMessage(e.getPlayer(), "in-game.nopermission");
            }
            case LEFT_CLICK_BLOCK -> {
                if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                    Gate.delete(location, false, standManager);
            }
        }
    }

}
