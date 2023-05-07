package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.animation.Task;
import com.github.justadeni.ironfencegate.enums.ItemType;
import com.github.justadeni.ironfencegate.logic.Gate;
import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static org.bukkit.inventory.EquipmentSlot.*;

public class PlayerInteract implements Listener {

    private final Task task;

    public PlayerInteract(Task task){
        this.task = task;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        if (e.getHand() == HAND && !isItem(HAND, e) && isItem(OFF_HAND,e))
            return;
        if (e.getHand() == OFF_HAND && (isItem(HAND, e) || !isItem(OFF_HAND, e)))
            return;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        final Block clickedBlock = e.getClickedBlock();

        final Location clickedLoc = LocUtil.center(e.getClickedBlock().getLocation());
        final Location belowLoc = LocUtil.alter(e.getClickedBlock().getLocation(), 0.5,-1,0.5);
        final Location againstLoc = LocUtil.center(e.getClickedBlock().getRelative(e.getBlockFace()).getLocation());

        final StandManager clickedManager = new StandManager(clickedLoc);
        final StandManager belowManager = new StandManager(belowLoc);
        final StandManager againstManager = new StandManager(againstLoc);

        final boolean hasIFGclicked = clickedManager.hasStand();
        final boolean hasIFGbelow  = belowManager.hasStand();
        final boolean hasIFGagainst = againstManager.hasStand();

        final ItemType handMaterialType = ItemType.get(e.getMaterial());
        final boolean hasPermission = e.getPlayer().hasPermission("ironfencegate.use");

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK){

            //First we check if player clicked gate block directly
            if (hasIFGclicked){
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    Gate.delete(clickedLoc, false, clickedManager);
                    return;
                }

                task.new Track(clickedLoc, e.getPlayer(), clickedManager);
                return;
            }

            //Then we check if player clicked barrier that's above
            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            if (hasIFGbelow)
                task.new Track(belowLoc, e.getPlayer(), belowManager);

            return;
        }


        if (handMaterialType == ItemType.NONE){
            if (e.getClickedBlock().getType() != Material.BARRIER)
                return;

            if (hasIFGclicked){
                if(hasPermission)
                    clickedManager.open(e.getPlayer());

                return;
            }

            if (hasIFGbelow){
                if(hasPermission)
                    belowManager.open(e.getPlayer());
                        
                return;
            }
        }

        if (handMaterialType == ItemType.INVALID_BLOCK) {
            if (hasIFGclicked) {
                e.setCancelled(true);
                if (hasPermission)
                    clickedManager.open(e.getPlayer());

                return;
            }

            if (hasIFGagainst) {
                e.setCancelled(true);
                if (hasPermission)
                    againstManager.open(e.getPlayer());

                return;
            }

            if (hasIFGbelow) {
                if (clickedBlock != null && clickedBlock.getType() == Material.BARRIER) {
                    e.setCancelled(true);
                    if (hasPermission)
                        belowManager.open(e.getPlayer());

                    return;
                }
            }
        }

        if (handMaterialType == ItemType.VALID_BLOCK) {
            if (!hasIFGclicked) {

                if (!hasIFGbelow)
                    return;

                if (clickedBlock != null && clickedBlock.getType() != Material.BARRIER)
                    return;

                e.setCancelled(true);
                if (!isValidPlaceable(againstLoc, e.getPlayer())) {
                    if (hasPermission)
                        belowManager.open(e.getPlayer());

                    return;
                }

                itemSubtract(e.getPlayer(), e.getHand());
                clickedBlock.setType(e.getItem().getType());

                return;
            }

            if (!isValidPlaceable(againstLoc, e.getPlayer())) {

                e.setCancelled(true);

                if (hasPermission)
                    clickedManager.open(e.getPlayer());

                return;
            }
        }

        if (handMaterialType == ItemType.IRON_FENCE_GATE){
            if (hasIFGclicked){
                if (!isValidPlaceable(againstLoc, e.getPlayer())) {
                    e.setCancelled(true);
                    if (hasPermission)
                        clickedManager.open(e.getPlayer());

                    return;
                }

                if (!ResourcesCheck.getInstance().isLoaded(e.getPlayer()))
                    return;

                e.setCancelled(true);
                Gate.create(againstLoc, e.getPlayer());
                itemSubtract(e.getPlayer(), e.getHand());
                    
                return;
            }

            if (hasIFGagainst) {
                e.setCancelled(true);
                if(hasPermission)
                    againstManager.open(e.getPlayer());
                    
                return;
            }

            if (!ResourcesCheck.getInstance().isLoaded(e.getPlayer()))
                return;

            if (hasIFGbelow) {
                if (clickedBlock != null && clickedBlock.getType() == Material.BARRIER) {
                    e.setCancelled(true);
                    Gate.create(clickedLoc, e.getPlayer());
                    itemSubtract(e.getPlayer(), e.getHand());
                    return;
                }
            }

            if (!isValidPlaceable(clickedLoc, e.getPlayer()))
                return;

            e.setCancelled(true);
            Gate.create(againstLoc, e.getPlayer());
            itemSubtract(e.getPlayer(), e.getHand());

            return;
        }
    }

    private static boolean isItem(EquipmentSlot hand, PlayerInteractEvent e){
        return e.getPlayer().getInventory().getItem(hand) != null;
    }

    private static void itemSubtract(Player player, EquipmentSlot hand){
        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        player.getInventory().getItem(hand).setAmount(player.getInventory().getItem(hand).getAmount()-1);
    }

    private static boolean isValidPlaceable(Location location, Player player){

        if (!player.isSneaking())
            if (ItemType.get(location.getBlock().getType()) == ItemType.INVALID_BLOCK)
                return false;

        for (Entity entity : location.getChunk().getEntities()){
            if (entity.getLocation().distanceSquared(location) <= 0.22)
                return false;

            if (entity.getHeight() > 1)
                if (LocUtil.alter(entity.getLocation(), 0, entity.getHeight(), 0).distanceSquared(location) <= 0.22)
                    return false;
        }

        return true;
    }

}
