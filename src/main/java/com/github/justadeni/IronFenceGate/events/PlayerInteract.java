package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.animation.Task;
import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.inventory.EquipmentSlot.*;

public class PlayerInteract implements Listener {

    /**
     *
     * Schematic of actions for less confusion
     *
     *                               +-----Is barrier---> Flip state
     * Empty hand +---> Click on gate|    ^
     *            +---> Click above gate--|
     *
     *                                +---->Click on gate
     * Item in Hand +-> Is valid block+---->Click above gate --> Is barrier -> Place
     *              +-> Isnt valid block
     *              |                  |                  +----> Cancel, Flip state
     *              |                  +--->Click on gate |                    ^
     *              |                  +--->Click above gate---> Is barrier----+
     *              |
     *              +-> Is gate +---------->Click on gate -----> Cancel, Flip state
     *                          +---------->Click above gate
     *                                                    -----> Is barrier -> Place
     * @param e
     */

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e){

        if (e.getHand() == HAND)
            if (isAir(HAND, e))
                if (!isAir(OFF_HAND,e))
                    return;

        if (e.getHand() == OFF_HAND) {
            if (isAir(OFF_HAND, e))
                return;

            if (!isAir(HAND, e))
                return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        ItemStack itemStack = e.getItem();
        Location location = e.getClickedBlock().getLocation().add(0.5,0,0.5);
        StandManager manager = new StandManager(location);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));
        Location againstLoc = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation().add(0.5,0,0.5);

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            //Placing our iron fence gate
            if (itemStack == null){
                if (e.getClickedBlock().getType() != Material.BARRIER)
                    return;

                if (manager.hasStand()){
                    manager.flipState(e.getPlayer().getLocation());
                    return;
                } else {
                    Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
                    StandManager belowManager = new StandManager(belowLoc);
                    if (belowManager.hasStand()){
                        belowManager.flipState(e.getPlayer().getLocation());
                        return;
                    }
                }
            }

            if (itemStack != null && !itemStack.isSimilar(Recipe.result())){
                if (!StandManager.isValidBlock(itemStack)) {
                    if (manager.hasStand()){
                        e.setCancelled(true);
                        manager.flipState(e.getPlayer().getLocation());
                        return;
                    }

                    StandManager insideManager = new StandManager(againstLoc);
                    if (insideManager.hasStand()) {
                        e.setCancelled(true);
                        insideManager.flipState(e.getPlayer().getLocation());
                        return;
                    }

                    Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                    StandManager belowManager = new StandManager(belowLoc);
                    if (belowManager.hasStand()) {
                        if (location.getBlock().getType() == Material.BARRIER) {
                            e.setCancelled(true);
                            belowManager.flipState(e.getPlayer().getLocation());
                            return;
                        }
                    }
                }

                if (StandManager.isValidBlock(itemStack)) {
                    if (!manager.hasStand()){
                        Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                        StandManager belowManager = new StandManager(belowLoc);

                        if (belowManager.hasStand()) {
                            if (location.getBlock().getType() == Material.BARRIER) {
                                e.setCancelled(true);
                                itemSubtract(e);
                                location.getBlock().setType(itemStack.getType());
                                return;
                            }
                        }
                    }
                }
            }

            if (itemStack != null && itemStack.isSimilar(Recipe.result())){
                if (manager.hasStand()){
                    e.setCancelled(true);
                    Gate.create(againstLoc);
                    itemSubtract(e);
                    StandManager standManager = new StandManager(againstLoc);
                    standManager.setYaw((int) Direction.getYaw(opposite));
                    delayBarriers(standManager);
                    return;
                }

                StandManager insideManager = new StandManager(againstLoc);
                if (insideManager.hasStand()) {
                    e.setCancelled(true);
                    insideManager.flipState(e.getPlayer().getLocation());
                    return;
                }

                Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                StandManager belowManager = new StandManager(belowLoc);
                if (belowManager.hasStand()) {
                    if (belowLoc.getBlock().getType() == Material.BARRIER) {
                        e.setCancelled(true);
                        Gate.create(location);
                        itemSubtract(e);
                        StandManager standManager = new StandManager(location);
                        standManager.setYaw((int) Direction.getYaw(opposite));
                        delayBarriers(standManager);
                        return;
                    } else {
                        e.setCancelled(true);
                        Gate.create(againstLoc);
                        itemSubtract(e);
                        StandManager standManager = new StandManager(againstLoc);
                        standManager.setYaw((int) Direction.getYaw(opposite));
                        delayBarriers(standManager);
                        return;
                    }
                }

                e.setCancelled(true);
                Gate.create(againstLoc);
                itemSubtract(e);
                StandManager standManager = new StandManager(againstLoc);
                standManager.setYaw((int) Direction.getYaw(opposite));
                delayBarriers(standManager);
                return;
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

    private static boolean isAir(EquipmentSlot hand, PlayerInteractEvent e){
        return hand == HAND ? e.getPlayer().getInventory().getItemInMainHand().getType().isAir() : e.getPlayer().getInventory().getItemInOffHand().getType().isAir();
    }

    private static void itemSubtract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        EquipmentSlot hand = e.getHand();

        if (hand == HAND)
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
        if (hand == OFF_HAND)
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
    }

    private static void delayBarriers(StandManager manager) {
        new BukkitRunnable() {
            @Override
            public void run() {
                manager.addBarriers();
            }
        }.runTaskLater(IronFenceGate.getInstance(), 2);
    }

}
