package com.github.justadeni.IronFenceGate.events;

import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.LocUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockUpdate implements Listener {

    /**
     * Storing and comparing hashes is much easier
     * and less memory intensive, especially with
     * such frequent event and redstone
     */
    private static final ArrayList<Integer> redstoned = new ArrayList<>();
    private static final ArrayList<Integer> locked = new ArrayList<>();

    @EventHandler
    public static void onBlockUpdate(BlockPhysicsEvent e){
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
