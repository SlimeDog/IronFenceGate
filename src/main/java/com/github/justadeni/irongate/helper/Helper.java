package com.github.justadeni.irongate.helper;

import com.github.justadeni.irongate.IronGate;
import com.github.justadeni.irongate.enums.Adjacent;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Helper {

    //public ArmorStand stand;

    public ArmorStand findStand(Location location){
        for (Entity e : location.getChunk().getEntities()){
            if (e.getType() == EntityType.ARMOR_STAND){
                if (e.getLocation().distance(location) <= 0.75){
                    //stand = (ArmorStand) e;
                    return (ArmorStand) e;
                }
            }
        }
        return null;
    }

    private int getId(ArmorStand stand){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getCustomModelData();
    }

    public State getState(ArmorStand stand){
        if (getId(stand) > 4)
            return State.OPEN;
        else
            return State.CLOSED;
    }

    public Adjacent getAdjacent(ArmorStand stand){
        int id = getId(stand);

        if (id > 4)
            id -= 4;

        switch (id){
            case 1: return Adjacent.NEITHER;
            case 2: return Adjacent.LEFT;
            case 3: return Adjacent.RIGHT;
            default: return Adjacent.BOTH;
        }
    }

}
