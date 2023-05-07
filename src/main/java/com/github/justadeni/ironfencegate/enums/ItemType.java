package com.github.justadeni.ironfencegate.enums;

import com.github.justadeni.ironfencegate.misc.Recipe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ItemType {
    NONE,
    IRON_FENCE_GATE,
    VALID_BLOCK,
    INVALID_BLOCK;

    private static final List<String> WHITELISTED = Collections.unmodifiableList(Arrays.asList("_FENCE","_WALL", "IRON_BARS","GLASS"));

    /*public static ItemType get(ItemStack itemStack){
        return get(itemStack.getType());
    }*/

    /*public static ItemType get(Block block){
        return get(block.getType());
    }*/

    public static ItemType get(Material material){
        if (material == null || material.isAir())
            return NONE;

        if (material == Recipe.getInstance().getResult().getType())
            return IRON_FENCE_GATE;

        if (material.isOccluding())
            return VALID_BLOCK;
        for (String whitelisted : WHITELISTED)
            if (material.toString().contains(whitelisted))
                return VALID_BLOCK;


        return INVALID_BLOCK;
    }
}
