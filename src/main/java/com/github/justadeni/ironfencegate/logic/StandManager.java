package com.github.justadeni.ironfencegate.logic;

import com.github.justadeni.ironfencegate.IronFenceGate;
import com.github.justadeni.ironfencegate.enums.Direction;
import com.github.justadeni.ironfencegate.enums.State;
import com.github.justadeni.ironfencegate.files.MainConfig;
import com.github.justadeni.ironfencegate.files.MessageConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class StandManager {

    public static final Integer IDFIRST = 5000;

    private Location location;
    private ArmorStand stand;

    public StandManager(Location location){
        this.location = location;
        stand = new Finder(location).armorStand();
        if (stand != null)
            this.location = stand.getLocation();
    }

    public boolean hasStand(){
        return getStand() != null;
    }

    private ArmorStand getStand(){
        return stand;
    }

    public void removeStand(){
        new BukkitRunnable() {
            @Override
            public void run() {

                if (hasStand())
                    stand.remove();
            }
        }.runTask(IronFenceGate.getInstance());
    }

    public static boolean isValidBlock(ItemStack itemStack){
        return  itemStack.getType().isOccluding() || itemStack.getType().name().endsWith("GLASS");
    }

    public int getDecaId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        int total = itemMeta.getCustomModelData()-IDFIRST;
        return Math.floorDiv(total,10)*10;
    }

    public int getId(){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.getCustomModelData()-IDFIRST-getDecaId();
    }

    public void setId(int id){
        ItemStack itemStack = stand.getEquipment().getItem(EquipmentSlot.HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id+IDFIRST);
        itemStack.setItemMeta(itemMeta);
        stand.getEquipment().setHelmet(itemStack);
    }

    public State getState(){
        if (getId() > 4)
            return State.OPEN;
        else
            return State.CLOSED;
    }

    public void open(Player player){
        if (getState() == State.OPEN)
            return;

        if (player != null) {
            if (!player.hasPermission("ironfencegate.use") && !player.hasPermission("ironfencegate.admin")) {
                MessageConfig.getInstance().sendMessage(player, "in-game.nopermission");
                return;
            }
            adjustDirection(player);
        }

        removeBarriers(1);
        setId(getId() + 4 + getDecaId());
        MainConfig mc = MainConfig.getInstance();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.open.name")), mc.getFloat("sound.open.volume"), mc.getFloat("sound.open.pitch"));
    }

    public void close(Player player){
        if (getState() == State.CLOSED)
            return;

        if (player != null) {
            if (!player.hasPermission("ironfencegate.use") && !player.hasPermission("ironfencegate.admin")) {
                MessageConfig.getInstance().sendMessage(player, "in-game.nopermission");
                return;
            }
            adjustDirection(player);
        }

        addBarriers(1);
        setId(getId() - 4 + getDecaId());
        MainConfig mc = MainConfig.getInstance();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.close.name")), mc.getFloat("sound.close.volume"), mc.getFloat("sound.close.pitch"));
    }

    private void adjustDirection(Player player){
        Direction standDirection = Direction.getDirection(getYaw());
        Direction playerDirection = Direction.getDirection(player.getLocation());

        if (playerDirection == standDirection) {
            setYaw((int) Direction.getYaw(Direction.getOpposite(playerDirection)));

            setId(switch (getId()){
                case 2,6 -> getId()+1;
                case 3,7 -> getId()-1;
                default -> getId();
            }+ getDecaId());
        }
    }

    public void setYaw(int yaw){
        EulerAngle a = new EulerAngle(0,Math.toRadians(yaw),0);
        stand.setHeadPose(a);
    }

    public int getYaw(){
        return (int) Math.toDegrees(stand.getHeadPose().getY());
    }

    public void addBarriers(int delay){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (location.getBlock().getType() == Material.AIR)
                    location.getBlock().setType(Material.BARRIER);

                Location aboveLoc = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());

                if (aboveLoc.getBlock().getType() == Material.AIR && !new StandManager(aboveLoc).hasStand())
                    aboveLoc.getBlock().setType(Material.BARRIER);
            }
        }.runTaskLater(IronFenceGate.getInstance(), delay);
    }

    public void removeBarriers(int delay){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (location.getBlock().getType() == Material.BARRIER)
                    location.getBlock().setType(Material.AIR);

                Location aboveLoc = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());

                if (aboveLoc.getBlock().getType() == Material.BARRIER && !new StandManager(aboveLoc).hasStand())
                    aboveLoc.getBlock().setType(Material.AIR);
            }
        }.runTaskLater(IronFenceGate.getInstance(), delay);
    }


}
