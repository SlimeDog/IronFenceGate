package com.github.justadeni.irongate.helper;

import com.github.justadeni.irongate.enums.Adjacent;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Helper {

    private Location location;
    public ArmorStand stand;

    public Helper(Location location){
        this.location = location;
        stand = findStand();
    }

    private ArmorStand findStand(){
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

    private int getId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getCustomModelData();
    }

    private void setId(int id){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id);
        itemStack.setItemMeta(itemMeta);
        stand.getEquipment().setHelmet(itemStack);
    }

    public ArmorStand getStand(){
        return stand;
    }

    public boolean isOurs(){
        //if (stand.isInvisible() && stand.isSmall() && !stand.hasBasePlate()){
            try {
                if (stand.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.STONE)
                    return true;
            } catch (NullPointerException e){
                return false;
            }
        //}

        return false;
    }

    public State getState(){
        if (getId() > 4)
            return State.OPEN;
        else
            return State.CLOSED;
    }

    public void flipState(){
        if (getState() == State.CLOSED){
            setId(getAdjacent().id + 4);
            removeBarriers();
        } else {
            setId(getAdjacent().id - 4);
            addBarriers();
        }
    }

    public Adjacent getAdjacent(){
        int id = getId();

        if (id > 4)
            id -= 4;

        switch (id){
            case 1: return Adjacent.NEITHER;
            case 2: return Adjacent.LEFT;
            case 3: return Adjacent.RIGHT;
            default: return Adjacent.BOTH;
        }
    }

    public void addBarriers(){
        if (location.getBlock().getType() == Material.AIR)
            location.getBlock().setType(Material.BARRIER);
        
        Location newloc = location.add(0,1,0);
        
        if (newloc.getBlock().getType() == Material.AIR)
            newloc.getBlock().setType(Material.BARRIER);
    }

    public void removeBarriers(){
        if (location.getBlock().getType() == Material.BARRIER)
            location.getBlock().setType(Material.AIR);

        Location newloc = location.add(0,1,0);

        if (newloc.getBlock().getType() == Material.BARRIER)
            newloc.getBlock().setType(Material.AIR);
    }


}
