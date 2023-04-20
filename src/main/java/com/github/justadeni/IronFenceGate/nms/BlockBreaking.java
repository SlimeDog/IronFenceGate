package com.github.justadeni.IronFenceGate.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.GateBreak;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class BlockBreaking {

    public static boolean isBreaking(Location location){
        return tracker.containsKey(location) && !toRemove.contains(location);
    }

    private static ConcurrentHashMap<Location, Store> tracker = new ConcurrentHashMap<>();

    //Needed to avoid concurrent modification exception
    private static ArrayList<Location> toRemove = new ArrayList<>();

    private static void track(Location location){
        final double[] progress = {0.0};

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!tracker.containsKey(location) || toRemove.contains(location)) {
                        toRemove.remove(location);
                        tracker.remove(location);
                        cancel();
                    }

                    Store store = tracker.get(location);

                    if (store == null || store.player == null || location.getBlock().isEmpty()) {
                        tracker.remove(location);
                        toRemove.remove(location);
                        cancel();
                    }

                    if (progress[0] >= 9.0) {
                        //This is the only thing that has to run sync
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new GateBreak(location, true);
                            }
                        }.runTask(IronFenceGate.getInstance());
                        tracker.remove(location);
                        toRemove.remove(location);
                        cancel();
                    }

                    //should be 0.1 second but supports auto-throttle
                    double delta = (System.currentTimeMillis() - store.start) / 1000f;
                    store.start = System.currentTimeMillis();

                    Material material = store.player.getInventory().getItemInMainHand().getType();

                    progress[0] += delta * switch (material) {
                        case IRON_PICKAXE -> 7;
                        case DIAMOND_PICKAXE -> 9;
                        case NETHERITE_PICKAXE -> 11;
                        default -> 0;
                    };

                    int decaProgress = (int) Math.floor(progress[0]) * 10;
                    if (decaProgress > store.manager.getDecaId()) {
                        store.manager.setId(store.manager.getId() + decaProgress);
                    }
                } catch (NullPointerException e){
                    toRemove.remove(location);
                    tracker.remove(location);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(IronFenceGate.getInstance(), 0, 2);
    }

    public BlockBreaking(){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(IronFenceGate.getInstance(), PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                Player player = e.getPlayer();
                PacketContainer packet = e.getPacket();
                int action = packet.getPlayerDigTypes().readSafely(0).ordinal();

                if (action > 1)
                    return;

                if (player.getGameMode().equals(GameMode.CREATIVE))
                    return;

                BlockPosition bp = packet.getBlockPositionModifier().read(0);
                Location location = bp.toLocation(player.getWorld()).add(0.5,0,0.5);
                Location aboveLoc = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());
                Location belowLoc = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());

                if (!location.getBlock().getType().equals(Material.BARRIER))
                    return;

                if (!StandManager.hasStand(location))
                    return;

                StandManager manager = new StandManager(location);

                //Stopped breaking
                if (action == 1){
                    if (tracker.containsKey(location)) {
                        if (StandManager.hasStand(aboveLoc) || !aboveLoc.getBlock().getType().equals(Material.BARRIER)) {
                            toRemove.add(location);
                            manager.setId(manager.getId());
                        }
                    }

                    if (location.getBlock().getType().equals(Material.BARRIER)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (manager.getState() == State.OPEN)
                                    location.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(IronFenceGate.getInstance(), 5);
                    }

                    return;
                }

                //Started breaking
                if (tracker.containsKey(location))
                    return;

                if (tracker.containsKey(belowLoc) && location.getBlock().getType().equals(Material.BARRIER))
                    return;

                tracker.put(location, new Store(player, manager, System.currentTimeMillis()));
                track(location);
            }
        });
    }
}
