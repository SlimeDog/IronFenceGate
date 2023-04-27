package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.animation.Task;
import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocUtil;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
        Location location = LocUtil.center(e.getClickedBlock().getLocation());
        StandManager manager = new StandManager(location);
        Location againstLoc = LocUtil.center(e.getClickedBlock().getRelative(e.getBlockFace()).getLocation());

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){

            //Empty hands
            if (itemStack == null){
                if (e.getClickedBlock().getType() != Material.BARRIER)
                    return;

                if (manager.hasStand()){
                    if(hasPermission(e))
                        manager.open(e.getPlayer());

                    return;
                } else {
                    Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
                    StandManager belowManager = new StandManager(belowLoc);
                    if (belowManager.hasStand()){
                        if(hasPermission(e))
                            belowManager.open(e.getPlayer());
                        
                        return;
                    }
                }
            }

            //Placing something but not our fence gate
            if (itemStack != null && !itemStack.isSimilar(Recipe.result())){
                if (!StandManager.isValidBlock(itemStack)) {
                    if (manager.hasStand()){
                        e.setCancelled(true);
                        if(hasPermission(e))
                            manager.open(e.getPlayer());
                        
                        return;
                    }

                    StandManager insideManager = new StandManager(againstLoc);
                    if (insideManager.hasStand()) {
                        e.setCancelled(true);
                        if(hasPermission(e))
                            insideManager.open(e.getPlayer());
                        
                        return;
                    }

                    Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                    StandManager belowManager = new StandManager(belowLoc);
                    if (belowManager.hasStand()) {
                        if (location.getBlock().getType() == Material.BARRIER) {
                            e.setCancelled(true);
                            if(hasPermission(e))
                                belowManager.open(e.getPlayer());
                            
                            return;
                        }
                    }
                }

                //Placing a real block
                if (StandManager.isValidBlock(itemStack)) {

                    if (!manager.hasStand()) {
                        Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                        StandManager belowManager = new StandManager(belowLoc);

                        if (!belowManager.hasStand())
                            return;

                        if (location.getBlock().getType() != Material.BARRIER)
                            return;

                        //No space to put block
                        e.setCancelled(true);
                        if (!isValidPlaceable(againstLoc)) {

                            if (hasPermission(e))
                                belowManager.open(e.getPlayer());

                            return;
                        }

                        itemSubtract(e);
                        location.getBlock().setType(itemStack.getType());

                        return;

                        //No space to put block
                    } else if (!isValidPlaceable(againstLoc)) {
                        e.setCancelled(true);
                        if (hasPermission(e))
                            manager.open(e.getPlayer());

                        return;
                    }
                }
            }

            //Placing our iron fence gate
            if (itemStack != null && itemStack.isSimilar(Recipe.result())){
                if (manager.hasStand()){
                    if (!isValidPlaceable(againstLoc)) {
                        e.setCancelled(true);
                        if (hasPermission(e))
                            manager.open(e.getPlayer());

                        return;
                    }

                    placeGate(againstLoc, e);
                    
                    return;
                }

                StandManager insideManager = new StandManager(againstLoc);
                if (insideManager.hasStand()) {
                    e.setCancelled(true);
                    if(hasPermission(e))
                        insideManager.open(e.getPlayer());
                    
                    return;
                }

                Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
                StandManager belowManager = new StandManager(belowLoc);
                if (belowManager.hasStand()) {
                    if (belowLoc.getBlock().getType() == Material.BARRIER) {
                        placeGate(location, e);
                    } else {
                        placeGate(againstLoc, e);
                    }
                    
                    return;
                }

                placeGate(againstLoc, e);

                return;
            }
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK){

            //First we check if player clicked gate block directly
            if (manager.hasStand()){
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    Gate.delete(location, false, manager);
                } else {
                    if (Task.tracker.contains(location))
                        return;


                    Task.tracker.add(location);
                    Task.track(location, e.getPlayer(), manager);
                }
                return;
            }

            //Then we check if player clicked barrier that's above
            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY()-1, location.getZ());
            StandManager belowManager = new StandManager(belowLoc);
            if (!belowManager.hasStand())
                return;

            if (Task.tracker.contains(belowLoc))
                return;

            Task.tracker.add(belowLoc);
            Task.track(belowLoc, e.getPlayer(), belowManager);
        }

        
    }

    private static boolean hasPermission(PlayerInteractEvent e){
        return e.getPlayer().hasPermission("ironfencegate.use") || e.getPlayer().hasPermission("ironfencegate.admin");
    }

    private static boolean isAir(EquipmentSlot hand, PlayerInteractEvent e){
        return hand == HAND ? e.getPlayer().getInventory().getItemInMainHand().getType().isAir() : e.getPlayer().getInventory().getItemInOffHand().getType().isAir();
    }

    private static void itemSubtract(PlayerInteractEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        if (e.getHand() == HAND)
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
        else
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
    }

    private static void placeGate(Location location, PlayerInteractEvent e){

        if (!ResourcesCheck.isLoaded(e.getPlayer()))
            return;

        e.setCancelled(true);
        Gate.create(location);
        itemSubtract(e);
        StandManager standManager = new StandManager(location);
        standManager.setYaw((int) Direction.getYaw(Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()))));

        standManager.addBarriers(2);

        Connect.around(location);
        Connect.one(location);
    }

    private static boolean isValidPlaceable(Location location){

        for (Entity entity : location.getChunk().getEntities()){
            if (entity.getLocation().distanceSquared(location) <= 0.22)
                return false;
        }

        return true;
    }

}
