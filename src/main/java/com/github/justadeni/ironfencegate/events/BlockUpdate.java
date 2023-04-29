package com.github.justadeni.ironfencegate.events;

import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockUpdate implements Listener {

    private final List<Integer> redstoned = new ArrayList<>();
    private final List<Integer> locked = new ArrayList<>();

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent e){
        Location location = LocUtil.center(e.getBlock().getLocation());
        if (locked.contains(location.hashCode()))
            return;

        locked.add(location.hashCode());

        StandManager standManager = new StandManager(location);
        if (!standManager.hasStand()) {
            locked.remove(Integer.valueOf(location.hashCode()));
            return;
        }

        if (e.getBlock().isBlockPowered()) {
            if (!redstoned.contains(location.hashCode())) {
                redstoned.add(location.hashCode());
                standManager.open(null);
            }
        } else {
            Iterator<Integer> iterator = redstoned.iterator();
            while (iterator.hasNext()) {
                int hash = iterator.next();
                if (location.hashCode() == hash) {
                    standManager.close(null);
                    iterator.remove();
                    locked.remove(Integer.valueOf(location.hashCode()));
                    break;
                }
            }
        }
        locked.remove(Integer.valueOf(location.hashCode()));
    }

}
