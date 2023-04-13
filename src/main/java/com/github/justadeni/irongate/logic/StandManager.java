package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.enums.Direction;
import com.github.justadeni.irongate.enums.State;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class StandManager {

    private Location location;
    public ArmorStand stand;

    public StandManager(Location location){
        this.location = location;
        stand = findStand();
        if (stand != null)
            this.location = stand.getLocation();
    }

    private ArmorStand findStand(){
        for (Entity e : location.getChunk().getEntities()){
            if (e.getType() == EntityType.ARMOR_STAND){
                if (e.getLocation().distance(location) <= 0.75){
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

    public void setId(int id){
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
        try {
            if (stand.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.STONE)
                return true;
        } catch (NullPointerException e){
            return false;
        }

        return false;
    }

    public State getState(){
        if (getId() > 4)
            return State.OPEN;
        else
            return State.CLOSED;
    }

    public void flipState(Location playerLoc){

        Direction standDirection = Direction.getDirection(getYaw());
        Direction playerDirection = Direction.getDirection(playerLoc);

        if (playerDirection.equals(standDirection)) {
            setYaw((int) Direction.getYaw(Direction.getOpposite(playerDirection)));
        }


        if (getState() == State.CLOSED){
            setId(getAdjacentId() + 4);
            removeBarriers();
        } else {
            setId(getId() - 4);
            addBarriers();
        }
    }

    public void open(){
        if (getState() == State.CLOSED){
            setId(getAdjacentId() + 4);
            removeBarriers();
        }
    }

    public void close(){
        if (getState() == State.OPEN){
            setId(getId() - 4);
            addBarriers();
        }
    }

    public void setYaw(int yaw){
        EulerAngle a = new EulerAngle(0,Math.toRadians(yaw),0);
        stand.setHeadPose(a);
    }

    public int getYaw(){
        return (int) Math.toDegrees(stand.getHeadPose().getY());
    }

    public int getAdjacentId(){
        int id = getId();

        if (id > 4)
            id -= 4;

        return id;
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
