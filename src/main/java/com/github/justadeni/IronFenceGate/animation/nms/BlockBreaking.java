package com.github.justadeni.IronFenceGate.animation.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.animation.logic.Task;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBreaking {

    public static PacketAdapter adapter;

    public BlockBreaking(){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(adapter=new PacketAdapter(IronFenceGate.getInstance(), PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                Player player = e.getPlayer();
                PacketContainer packet = e.getPacket();
                int action = packet.getPlayerDigTypes().readSafely(0).ordinal();

                if (action > 1)
                    return;

                if (player.getGameMode()==GameMode.CREATIVE)
                    return;

                BlockPosition bp = packet.getBlockPositionModifier().read(0);
                Location location = bp.toLocation(player.getWorld()).add(0.5,0,0.5);
                //Location location = new Location(player.getWorld(),bp.getX()+0.5, bp.getY(), bp.getZ()+0.5);

                if (!location.getBlock().getType().equals(Material.BARRIER))
                    return;

                if (!StandManager.hasStand(location))
                    return;

                StandManager manager = new StandManager(location);

                /*
                //Stopped breaking
                if (action == 1){

                    Task.tracker.remove(location);

                    if (location.getBlock().getType().equals(Material.BARRIER)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (manager.getStand() == null || manager.getState() == State.OPEN)
                                    location.getBlock().setType(Material.AIR);
                            }
                        }.runTaskLater(IronFenceGate.getInstance(), 5);
                    }

                    return;
                }*/

                //Started breaking
                if (Task.tracker.contains(location))
                    return;

                Task.tracker.add(location);
                Task.track(location, player, manager);
            }
        });
    }
}
