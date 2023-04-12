package com.github.justadeni.irongate.events;

import com.github.justadeni.irongate.helper.GateMaker;
import com.github.justadeni.irongate.helper.Helper;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getType() != Material.STONE)
            return;

        if (e.getItemInHand().getItemMeta() == null)
            return;

        if (!e.getItemInHand().getItemMeta().hasCustomModelData())
            return;

        e.getBlock().setType(Material.BARRIER);

        new GateMaker(e.getBlock().getLocation());
        new Helper(e.getBlock().getLocation()).addBarriers();
    }
}
