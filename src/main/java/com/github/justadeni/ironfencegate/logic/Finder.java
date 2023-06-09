package com.github.justadeni.ironfencegate.logic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.EquipmentSlot;

public final class Finder {

    private Finder(){}

    public static ArmorStand armorStand(Location location){
        for (Entity entity : location.getChunk().getEntities()){
            if (entity instanceof ArmorStand stand) {
                if (stand.getLocation().distanceSquared(location) > 0.22)
                    continue;

                if (stand.hasBasePlate() || stand.hasGravity())
                    continue;

                if (stand.getEquipment().getHelmet() == null)
                    continue;

                if (stand.getEquipment().getItem(EquipmentSlot.HEAD).getType() != Material.WARPED_FENCE_GATE)
                    continue;

                return stand;
            }
        }
        return null;
    }

    public static Pig pig(Location location){
        for (Entity entity : location.getChunk().getEntities()){
            if (entity.getType() != EntityType.PIG)
                continue;

            if (entity.getLocation().distanceSquared(location) > 0.22)
                continue;

            return (Pig) entity;
        }
        return null;
    }

}
