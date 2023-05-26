package com.github.justadeni.ironfencegate.nms.entity;

import com.github.justadeni.ironfencegate.animation.Task;
import com.github.justadeni.ironfencegate.logic.Finder;
import com.github.justadeni.ironfencegate.logic.Gate;
import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.misc.LocUtil;
import com.github.justadeni.ironfencegate.misc.Recipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.Pig;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomArmorstand extends ArmorStand {

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

        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);

        ItemStack itemStack = Recipe.getInstance().getResult();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(StandManager.IDFIRST+1);
        itemStack.setItemMeta(itemMeta);

        org.bukkit.entity.ArmorStand livingStand = (org.bukkit.entity.ArmorStand) this.getBukkitEntity();
        livingStand.getEquipment().setHelmet(itemStack);
        livingStand.addEquipmentLock(EquipmentSlot.HEAD, org.bukkit.entity.ArmorStand.LockType.REMOVING_OR_CHANGING);
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {

        if (damagesource.is(DamageTypes.ON_FIRE) || damagesource.is(DamageTypes.IN_FIRE)) {
            this.clearFire();
            return false;
        }

        Location location = this.getBukkitEntity().getLocation();
        StandManager manager = new StandManager(location);

        if (!damagesource.is(DamageTypes.PLAYER_ATTACK)){
            Gate.delete(location, false, manager);
            return true;
        }

        org.bukkit.entity.Player player = (org.bukkit.entity.Player) damagesource.getEntity().getBukkitEntity();
        if (!manager.hasStand())
            return false;

        if (player.getGameMode() == GameMode.CREATIVE || damagesource.isIndirect()) {
            Gate.delete(location, false, manager);
            return true;
        }

        Task task = new Task();

        task.new Track(location, player, manager);
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:custom_armor_stand");
    }

    @Override
    public void remove(Entity.RemovalReason entity_removalreason) {
        if (entity_removalreason == RemovalReason.KILLED) {
            Location location = this.getBukkitEntity().getLocation();

            if (location.getBlock().getType() == Material.BARRIER)
                location.getBlock().setType(Material.AIR);

            if (LocUtil.alter(location, 0, 1, 0).getBlock().getType() == Material.BARRIER)
                LocUtil.alter(location, 0, 1, 0).getBlock().setType(Material.AIR);

            Pig pig = Finder.pig(location);
            if (pig != null)
                pig.remove();
        }
        super.remove(entity_removalreason);
    }

    @Override
    public void push(net.minecraft.world.entity.Entity entity){}

    @Override
    public void playerTouch(Player entityhuman) {}

    @Override
    protected float getSoundVolume() { return 0.0F; }

    @Override
    public void checkDespawn(){}

    @Override
    public boolean isColliding(BlockPos blockposition, BlockState iblockdata) {return false;}

    @Override
    public boolean canCollideWithBukkit(Entity entity) {return false;}

    @Override
    public boolean canBeCollidedWith(){return false;}

    @Override
    protected void doWaterSplashEffect() {}

    @Override
    public boolean isInWater() {return false;}

    @Override
    public boolean isInLava() {return false;}

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {return false;}

    @Override
    public void push(double d0, double d1, double d2) {}

    @Override
    public boolean isPushable() {
        return false;
    }
}
