package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

public class GateMaker {


    private ArmorStand stand;

    public GateMaker(Location location){

        stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setRemoveWhenFarAway(false);
        stand.setPersistent(true);
        stand.setInvulnerable(true);
        stand.setInvisible(true);
        stand.setSmall(true);
        stand.setArms(false);

        stand.getEquipment().setHelmet(Recipe.recipes.get(0).getResult());
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);

    }

}
