package com.github.justadeni.IronFenceGate.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
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

    public CustomPig(Location location) {
        super(EntityType.PIG, ((CraftWorld) location.getWorld()).getHandle());
        this.setNoAi(true);
        this.setNoGravity(true);
        this.setSilent(true);
        this.setDiscardFriction(true);
        this.setHealth(1023);
        this.setXRot(0f);
        this.setPos(location.getX(), location.getY(), location.getZ());
    }

    //Overrides of Slime class

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

    //Overrides for Mob class

    @Override
    public void checkDespawn(){}

    //Overrides for Entity class

    @Override
    public boolean isColliding(BlockPos blockposition, BlockState iblockdata) {return false;}

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
    protected void markHurt() {
        this.hurtMarked = false;
    }

    @Override
    public boolean hurt(DamageSource damagesource, float f) {return false;}

    @Override
    public boolean isPushable() {
        return false;
    }
}
