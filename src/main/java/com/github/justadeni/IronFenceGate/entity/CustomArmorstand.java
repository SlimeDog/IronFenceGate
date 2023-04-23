package com.github.justadeni.IronFenceGate.entity;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

public class CustomArmorstand extends ArmorStand {

    public static org.bukkit.entity.ArmorStand findStand(Location location){
        for (Entity e : location.getChunk().getEntities()){
            if (e.getType() == EntityType.ARMOR_STAND){
                if (e.getLocation().distanceSquared(location) <= 0.22){
                    try {
                        org.bukkit.entity.ArmorStand armorStand = (org.bukkit.entity.ArmorStand) e;
                        if (!armorStand.hasBasePlate() && !armorStand.hasGravity())
                            if (armorStand.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.WARPED_FENCE_GATE)
                                return armorStand;
                    } catch (NullPointerException ignored){}
                }
            }
        }
        return null;
    }

    public CustomArmorstand(Location location) {
        super(((CraftWorld) location.getWorld()).getHandle(), location.getX(),location.getY(), location.getZ());
        org.bukkit.entity.ArmorStand stand = (org.bukkit.entity.ArmorStand) this.getBukkitEntity();

        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setRemoveWhenFarAway(false);
        stand.setPersistent(true);
        stand.setInvisible(true);
        stand.setSmall(true);
        stand.setArms(false);
        stand.setInvulnerable(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.setYHeadRot(0);
        this.setYBodyRot(0);
        this.setXRot(0);
        this.setRot(0,0);
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        if (damagesource.is(DamageTypeTags.IS_FIRE))
            this.clearFire();
        return false;
    }
}
