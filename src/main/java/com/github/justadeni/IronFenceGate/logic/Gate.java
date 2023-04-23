package com.github.justadeni.IronFenceGate.logic;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.entity.CustomArmorstand;
import com.github.justadeni.IronFenceGate.enums.State;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.entity.hitbox.CustomPig;
import com.github.justadeni.IronFenceGate.entity.hitbox.NonCollision;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

        //ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        CustomArmorstand stand =  new CustomArmorstand(location);
        Level world = ((CraftWorld) location.getWorld()).getHandle();
        world.addFreshEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);

        ItemStack itemStack = Recipe.result();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(StandManager.getIdFirst()+1);
        itemStack.setItemMeta(itemMeta);

        ArmorStand livingStand = (ArmorStand) stand.getBukkitEntity();
        livingStand.getEquipment().setHelmet(itemStack);
        livingStand.addEquipmentLock(EquipmentSlot.HEAD, org.bukkit.entity.ArmorStand.LockType.REMOVING_OR_CHANGING);

        CustomPig pig = new CustomPig(location);
        NonCollision.get().add(pig.getBukkitEntity());
        world.addFreshEntity(pig);

        MainConfig mc = MainConfig.get();
        location.getWorld().playSound(location, Sound.valueOf(mc.getString("sound.place.name")), mc.getFloat("sound.place.volume"), mc.getFloat("sound.place.pitch"));
    }
}
