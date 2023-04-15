package com.github.justadeni.irongate.logic;

import com.github.justadeni.irongate.misc.ConfigManager;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        ItemStack itemStack = Recipe.recipes.get(0).getResult();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(StandManager.getIdFirst()+1);
        itemStack.setItemMeta(itemMeta);

        stand.getEquipment().setHelmet(itemStack);
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        ConfigManager cm = ConfigManager.get();
        location.getWorld().playSound(location, Sound.valueOf(cm.getString("sound.place.name")), cm.getFloat("sound.place.volume"), cm.getFloat("sound.place.pitch"));
    }

}