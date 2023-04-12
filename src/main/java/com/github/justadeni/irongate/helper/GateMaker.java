package com.github.justadeni.irongate.helper;

import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

public class GateMaker {

    private Location location;
    private ArmorStand stand;

    public GateMaker(Location location){
        this.location = location.add(0.5,0,0.5);

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
