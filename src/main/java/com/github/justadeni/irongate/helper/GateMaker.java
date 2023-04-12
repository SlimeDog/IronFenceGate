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

        Location impromptu = location.add(0,100,0);
        stand = (ArmorStand) impromptu.getWorld().spawnEntity(impromptu, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setBasePlate(false);
        stand.setRemoveWhenFarAway(false);
        stand.setPersistent(true);
        stand.setInvulnerable(true);
        stand.setInvisible(true);
        stand.setSmall(true);

        stand.getEquipment().setHelmet(Recipe.getRecipe().getResult());
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.teleport(this.location);


    }

}
