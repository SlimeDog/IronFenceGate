package com.github.justadeni.IronFenceGate.nms.entity;

import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomArmorstand extends ArmorStand {

    public static org.bukkit.entity.ArmorStand find(Location location){
        for (Entity entity : location.getChunk().getEntities()){
            if (entity instanceof org.bukkit.entity.ArmorStand stand) {
                if (stand.getLocation().distanceSquared(location) > 0.22)
                    continue;

                if (stand.hasBasePlate() || stand.hasGravity())
                    continue;

                try {
                    if (stand.getEquipment().getItem(EquipmentSlot.HEAD).getType() == Material.WARPED_FENCE_GATE)
                        return stand;
                } catch (NullPointerException ignored) {
                }
            }
        }
        return null;
    }

    public static void spawn(Location location){
        CustomArmorstand stand =  new CustomArmorstand(location);
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        world.addFreshEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);

        ItemStack itemStack = Recipe.result();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(StandManager.getIdFirst()+1);
        itemStack.setItemMeta(itemMeta);

        org.bukkit.entity.ArmorStand livingStand = (org.bukkit.entity.ArmorStand) stand.getBukkitEntity();
        livingStand.getEquipment().setHelmet(itemStack);
        livingStand.addEquipmentLock(EquipmentSlot.HEAD, org.bukkit.entity.ArmorStand.LockType.REMOVING_OR_CHANGING);
    }

    /*
    public static void respawn(Entity entity){
        Location location = entity.getLocation();
        entity.remove();
        spawn(location);
    }
    */
    /*
    public static void remove(Location location){
        org.bukkit.entity.ArmorStand stand = find(location);
        if (stand == null)
            return;

        stand.remove();
    }
    */

    public CustomArmorstand (EntityType<?> entityType, Level world){
        super(EntityType.ARMOR_STAND, world);
    }


    public CustomArmorstand(Location location) {
        this(EntityType.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());
        //super(((CraftWorld) location.getWorld()).getHandle(), location.getX(),location.getY(), location.getZ());
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

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:custom_armor_stand"); // <= this line of code
    }
}
