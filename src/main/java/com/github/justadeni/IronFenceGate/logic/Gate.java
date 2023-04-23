package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.hitbox.CustomPig;
import com.github.justadeni.IronFenceGate.hitbox.NonCollision;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Gate {

    public static void delete(Location location, boolean drop, StandManager standManager){
        standManager.removeStand();
        standManager.removeBarriers(1);
        MainConfig mc = MainConfig.get();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.break.name")), mc.getFloat("sound.break.volume"), mc.getFloat("sound.break.pitch"));
        if (drop)
            location.getWorld().dropItemNaturally(new Location(location.getWorld(), location.getX(), location.getY()+0.5, location.getZ()), Recipe.recipes.get(0).getResult());

        Pig pig = CustomPig.find(location);
        if (pig != null) {
            NonCollision.get().remove(pig);
            pig.remove();
        }

        location.add(0,-1,0);
        StandManager lowerStand = new StandManager(location);
        if (lowerStand.hasStand() && lowerStand.getState() == State.CLOSED) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.add(0,1,0).getBlock().setType(Material.BARRIER);
                }
            }.runTaskLater(IronFenceGate.getInstance(), 2);
        }
    }

    public static void create(Location location){

        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setRemoveWhenFarAway(false);
        stand.setPersistent(true);
        stand.setInvisible(true);
        stand.setSmall(true);
        stand.setArms(false);
        stand.setInvulnerable(true);

        CustomPig pig = new CustomPig(location);
        NonCollision.get().add(pig.getBukkitEntity());
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        world.addFreshEntity(pig);

        ItemStack itemStack = Recipe.recipes.get(0).getResult();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(StandManager.getIdFirst()+1);
        itemStack.setItemMeta(itemMeta);

        stand.getEquipment().setHelmet(itemStack);
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        MainConfig mc = MainConfig.get();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.place.name")), mc.getFloat("sound.place.volume"), mc.getFloat("sound.place.pitch"));
    }
}
