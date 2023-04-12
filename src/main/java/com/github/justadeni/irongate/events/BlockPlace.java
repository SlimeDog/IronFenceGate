package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.helper.GateMaker;
import com.github.justadeni.irongate.helper.Helper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BlockPlace implements Listener {

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getType() != Material.STONE)
            return;

        if (!e.getItemInHand().hasItemMeta())
            return;

        if (!e.getItemInHand().getItemMeta().hasLore())
            return;

        if (!e.getItemInHand().getItemMeta().getLore().get(0).equals("Connects to Iron Fences"))
            return;

        //e.setCancelled(true);

        new GateMaker(e.getBlock().getLocation());
        new Helper(e.getBlock().getLocation()).addBarriers();
    }
}
