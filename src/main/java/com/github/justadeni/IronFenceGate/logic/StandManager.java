package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class StandManager {

    private Location location;
    private ArmorStand stand;

    public StandManager(Location location){
        this.location = location;
        stand = findStand();
        if (stand != null)
            this.location = stand.getLocation();
    }

    private ArmorStand findStand(){
        for (Entity e : location.getChunk().getEntities()){
            if (e.getType() == EntityType.ARMOR_STAND){
                if (e.getLocation().distance(location) <= 0.49){
                    return (ArmorStand) e;
                }
            }
        }
        return null;
    }

    //Convenience method
    public static boolean hasStand(Location loc){
        StandManager manager = new StandManager(loc);
        return manager.getStand() != null && manager.isOurs();
    }

    //Hardcoded the first id of item in resource pack
    public static int getIdFirst(){
        return 5464;
    }

    private int getId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getCustomModelData()-getIdFirst();
    }

    public void setId(int id){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id+getIdFirst());
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

            setId(switch (getId()){
                case 2,6 -> getId()+1;
                case 3,7 -> getId()-1;
                default -> getId();
            });
        }

        if (getState() == State.CLOSED){
            open();
        } else {
            close();
        }
    }

    public void open(){
        if (getState() == State.CLOSED){
            setId(getAdjacentId() + 4);
            removeBarriers();
            MainConfig mc = MainConfig.get();
            location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.open.name")), mc.getFloat("sound.open.volume"), mc.getFloat("sound.open.pitch"));
        }
    }

    public void close(){
        if (getState() == State.OPEN){
            setId(getId() - 4);
            addBarriers();
            MainConfig mc = MainConfig.get();
            location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.close.name")), mc.getFloat("sound.close.volume"), mc.getFloat("sound.close.pitch"));
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
