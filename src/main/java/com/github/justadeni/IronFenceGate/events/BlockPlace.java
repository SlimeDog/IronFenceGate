package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.logic.Connect;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocationHelp;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BlockPlace implements Listener {

    /*
    private static void placeNormally(BlockPlaceEvent e){
        Location location = e.getBlockPlaced().getLocation().add(0.5,0,0.5);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

        e.getBlock().setType(Material.BARRIER);

        Gate.create(location);
        StandManager standManager = new StandManager(location);
        standManager.addBarriers();

        standManager.setYaw((int) Direction.getYaw(opposite));
    }

    private static void placeInsideAgainst(BlockPlaceEvent e){
        Location againstLoc = e.getBlockAgainst().getLocation().add(0.5, 0, 0.5);
        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));
        if (!StandManager.hasStand(againstLoc)) {
            if (StandManager.hasStand(againstLoc.add(0, -1, 0))) {
                againstLoc.add(0, 1, 0);

                e.setCancelled(true);
                Gate.create(againstLoc);
                StandManager standManager = new StandManager(againstLoc);
                standManager.addBarriers();

                standManager.setYaw((int) Direction.getYaw(opposite));
                e.getItemInHand().setAmount(e.getItemInHand().getAmount() - 1);
                return;
            }
            placeNormally(e);
            return;
        }
        placeNormally(e);
        return;
    }
    */
    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {

        Location location = e.getBlockPlaced().getLocation().add(0.5, 0, 0.5);

        Connect connect = new Connect();
        for (Location loc : LocationHelp.getLocsAround(location)) {
            connect.reconnect(loc);
        }
        connect.reconnect(location);

        Player player = e.getPlayer();
        ItemStack itemStack = e.getItemInHand();

        Direction opposite = Direction.getOpposite(Direction.getDirection(e.getPlayer().getLocation()));

        //In case player clicked inside open gate
        if (StandManager.hasStand(location)){
            e.setCancelled(true);
            return;
        }

        //In case player clicked on closed gate
        Location againstLoc = e.getBlockAgainst().getLocation().add(0.5,0,0.5);
        if (/*e.getBlockAgainst().getType() == Material.BARRIER &&*/ StandManager.hasStand(againstLoc)){

            //Player holds another iron gate
            if (itemStack.isSimilar(Recipe.recipes.get(0).getResult())){
                e.setCancelled(true);
                Gate.create(location);
                itemSubtract(e);
            }
            return;
        }

        //In case player clicked on barrier above
        Location belowLoc = e.getBlockAgainst().getLocation().add(0.5,-1,0.5);
        if (e.getBlockAgainst().getType() == Material.BARRIER && StandManager.hasStand(belowLoc)){

            if (itemStack.getType() == Material.SHULKER_BOX)
                return;

            if (itemStack.isSimilar(Recipe.recipes.get(0).getResult())){
                e.setCancelled(true);
                Gate.create(againstLoc);
                itemSubtract(e);
                StandManager standManager = new StandManager(againstLoc);
                standManager.addBarriers();
                standManager.setYaw((int) Direction.getYaw(opposite));
            } else {
                e.setCancelled(true);
                againstLoc.getBlock().setType(itemStack.getType());
                itemSubtract(e);
            }
            return;
        }

        //In case player places the iron fence gate
        if (itemStack.isSimilar(Recipe.recipes.get(0).getResult())){
            e.setCancelled(true);
            Gate.create(location);
            itemSubtract(e);
            StandManager standManager = new StandManager(location);
            standManager.addBarriers();
            standManager.setYaw((int) Direction.getYaw(opposite));
        }
    }

    private static void itemSubtract(BlockPlaceEvent e){
        Player player = e.getPlayer();
        EquipmentSlot hand = e.getHand();

        if (hand == EquipmentSlot.HAND)
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
        if (hand == EquipmentSlot.OFF_HAND)
            player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
    }
}
