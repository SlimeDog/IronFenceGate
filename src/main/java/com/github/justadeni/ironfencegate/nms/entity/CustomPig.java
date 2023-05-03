package com.github.justadeni.ironfencegate.nms.entity;

import com.github.justadeni.ironfencegate.animation.Task;
import com.github.justadeni.ironfencegate.logic.Finder;
import com.github.justadeni.ironfencegate.logic.Gate;
import com.github.justadeni.ironfencegate.logic.StandManager;
import com.github.justadeni.ironfencegate.logic.NonCollision;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

public class CustomPig extends Pig {

    public static void remove(Location location){
        org.bukkit.entity.Pig pig = Finder.pig(location);
        if (pig == null)
            return;

        NonCollision.getInstance().remove(pig);
        pig.remove();
    }

    public CustomPig(EntityType<?> entityType, Level world){
        super(EntityType.PIG, world);
    }

    public CustomPig(Location location) {
        this(EntityType.PIG, ((CraftWorld) location.getWorld()).getHandle());
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setSilent(true);
        this.setDiscardFriction(true);
        this.setHealth(200);
        this.setInvisible(true);
        this.setPersistenceRequired(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.getBukkitEntity().setPersistent(true);
        ((org.bukkit.entity.Pig) this.getBukkitEntity()).setRemoveWhenFarAway(false);

        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
        NonCollision.getInstance().add(this.getBukkitEntity());
    }

    @Override
    public void push(Entity entity){}

    @Override
    public void playerTouch(Player entityhuman) {}

    @Override
    public void tick() {}

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    protected void registerGoals() {}

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

    @Override
    public boolean hurt(DamageSource damagesource, float f) {
        if (!(damagesource.getEntity().getBukkitEntity() instanceof org.bukkit.entity.Player player))
            return false;

        Location location = this.getBukkitEntity().getLocation();
        StandManager manager = new StandManager(location);
        if (!manager.hasStand())
            return false;

        if (player.getGameMode() == GameMode.CREATIVE)
            Gate.delete(location, false, manager);
        else {

            Task task = new Task();

            if (task.contains(location))
                return false;

            task.new Track(location, player, manager);
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:custom_pig");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        this.setInvisible(true);
        NonCollision.getInstance().add(this.getBukkitEntity());
    }

    @Override
    public void remove(Entity.RemovalReason entity_removalreason) {
        NonCollision.getInstance().remove(this.getBukkitEntity());
        super.remove(entity_removalreason);
    }
}
