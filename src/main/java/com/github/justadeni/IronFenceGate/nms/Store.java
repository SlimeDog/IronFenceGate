package com.github.justadeni.IronFenceGate.nms;

import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Store {

    public Player player;
    public StandManager manager;
    public Long start;

    public Store(Player player, StandManager manager, Long start){
        this.player = player;
        this.manager = manager;
        this.start = start;
    }
}
