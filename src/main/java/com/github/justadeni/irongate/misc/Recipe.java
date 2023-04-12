package com.github.justadeni.irongate.misc;

import com.github.justadeni.irongate.IronFenceGate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Recipe {

    public static NamespacedKey key;

    public static ShapedRecipe getRecipe() {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&rIron Gate"));
        itemMeta.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Connects to Iron Fences");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        key = new NamespacedKey(IronFenceGate.getInstance(), "IronFenceGate");
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        recipe.shape("BIB", "BIB", "   ");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

}
