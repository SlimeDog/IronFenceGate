package com.github.justadeni.IronFenceGate.hitbox;

import com.github.justadeni.IronFenceGate.animation.Task;
import com.github.justadeni.IronFenceGate.logic.Gate;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.Team;

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
        this.setHealth(200);
        this.setXRot(0f);
        //this.setInvisible(true);
        this.setPos(location.getX(), location.getY(), location.getZ());
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

}
