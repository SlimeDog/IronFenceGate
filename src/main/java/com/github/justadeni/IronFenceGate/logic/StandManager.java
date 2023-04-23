package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.Direction;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.files.MessageConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
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
                if (e.getLocation().distanceSquared(location) <= 0.22){
                    ArmorStand armorStand = (ArmorStand) e;
                    if (isOurs(armorStand))
                        return armorStand;
                }
            }
        }
        return null;
    }

    //Convenience method
    public static boolean hasStand(Location loc){
        StandManager manager = new StandManager(loc);
        return manager.getStand() != null;
    }

    public boolean hasStand(){
        return getStand() != null;
    }

    private ArmorStand getStand(){
        return stand;
    }

    public void removeStand(){
        if (hasStand())
            stand.remove();
    }

    private boolean isOurs(ArmorStand armorStand){
        try {
            if (armorStand.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.WARPED_FENCE_GATE)
                return true;
        } catch (NullPointerException e){
            return false;
        }

        return false;
    }

    public static boolean isValidBlock(Material material){
        return  material.isOccluding() || material.name().endsWith("GLASS");
    }

    public static boolean isValidBlock(ItemStack itemStack){
        return  itemStack.getType().isOccluding() || itemStack.getType().name().endsWith("GLASS");
    }

    //Hardcoded the first id of item in resource pack
    public static int getIdFirst(){
        return 5000;
    }

    public int getDecaId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        int total = itemMeta.getCustomModelData()-getIdFirst();
        return Math.floorDiv(total,10)*10;
    }

    public int getId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getCustomModelData()-getIdFirst()-getDecaId();
    }

    public void setId(int id){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id+getIdFirst());
        itemStack.setItemMeta(itemMeta);
        stand.getEquipment().setHelmet(itemStack);
    }

    public State getState(){
        if (getId() > 4)
            return State.OPEN;
        else
            return State.CLOSED;
    }

    public void flipState(Player player){

        if (!player.hasPermission("ironfencegate.use") && !player.hasPermission("ironfencegate.admin")) {
            MessageConfig.get().sendMessage(player, "in-game.nopermission");
            return;
        }

        Direction standDirection = Direction.getDirection(getYaw());
        Direction playerDirection = Direction.getDirection(player.getLocation());

        if (playerDirection.equals(standDirection)) {
            setYaw((int) Direction.getYaw(Direction.getOpposite(playerDirection)));

            setId(switch (getId() + getDecaId()){
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
            setId(getId() + 4 + getDecaId());
            removeBarriers(1);
            MainConfig mc = MainConfig.get();
            location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.open.name")), mc.getFloat("sound.open.volume"), mc.getFloat("sound.open.pitch"));
        }
    }

    public void close(){
        if (getState() == State.OPEN){
            setId(getId() - 4 + getDecaId());
            addBarriers(1);
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

    public void addBarriers(int delay){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (location.getBlock().getType() == Material.AIR)
                    location.getBlock().setType(Material.BARRIER);

                Location newloc = location.add(0, 1, 0);

                if (newloc.getBlock().getType() == Material.AIR)
                    newloc.getBlock().setType(Material.BARRIER);
            }
        }.runTaskLater(IronFenceGate.getInstance(), delay);
    }

    public void removeBarriers(int delay){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (location.getBlock().getType() == Material.BARRIER)
                    location.getBlock().setType(Material.AIR);

                Location newloc = location.add(0,1,0);

                if (newloc.getBlock().getType() == Material.BARRIER)
                    newloc.getBlock().setType(Material.AIR);
            }
        }.runTaskLater(IronFenceGate.getInstance(), delay);
    }


}
