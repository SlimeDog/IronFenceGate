package com.github.justadeni.IronFenceGate.nms.entity;

import com.github.justadeni.IronFenceGate.animation.Task;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import com.github.justadeni.IronFenceGate.misc.NonCollision;
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

    public static org.bukkit.entity.Pig find(Location location){
        for (org.bukkit.entity.Entity e : location.getChunk().getEntities()){
            if (e.getType() == org.bukkit.entity.EntityType.PIG){
                if (e.getLocation().distanceSquared(location) <= 0.22){
                    org.bukkit.entity.Pig pig = (org.bukkit.entity.Pig) e;
                    if (!pig.hasAI() && !pig.hasGravity())
                        return pig;
                }
            }
        }
        return null;
    }

    public static void spawn(Location location){
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        CustomPig pig = new CustomPig(location);
        NonCollision.get().add(pig.getBukkitEntity());
        world.addFreshEntity(pig);
    }

    /*
    public static void respawn(Location location){
        org.bukkit.entity.Pig pig = find(location);
        if (pig != null) {
            NonCollision.get().remove(pig);
            pig.remove();
        }
        spawn(location);
    }
    */
    public static void remove(Location location){
        org.bukkit.entity.Pig pig = find(location);
        if (pig == null)
            return;

        NonCollision.get().remove(pig);
        pig.remove();
    }

    public CustomPig(EntityType<?> entityType, Level world){
        super(EntityType.PIG, world);
    }

    public CustomPig(Location location) {
        this(EntityType.PIG, ((CraftWorld) location.getWorld()).getHandle());
        //super(EntityType.PIG, ((CraftWorld) location.getWorld()).getHandle());
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setSilent(true);
        this.setDiscardFriction(true);
        this.setHealth(200);
        //this.setInvisible(true);
        this.setPersistenceRequired(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
        this.getBukkitEntity().setPersistent(true);
        ((org.bukkit.entity.Pig) this.getBukkitEntity()).setRemoveWhenFarAway(false);
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
        StandManager standManager = new StandManager(location);
        if (!standManager.hasStand())
            return false;

        if (player.getGameMode().equals(GameMode.CREATIVE))
            Gate.delete(location, false, standManager);
        else {

            if (Task.tracker.contains(location))
                return false;

            Task.tracker.add(location);
            Task.track(location, player, standManager);
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:custom_pig"); // <= this line of code
    }
}
