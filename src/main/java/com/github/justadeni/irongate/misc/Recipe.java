package com.github.justadeni.irongate.misc;

import com.github.justadeni.irongate.IronGate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipe {

    public static ShapedRecipe getRecipe() {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&rIron Gate"));
        itemMeta.setCustomModelData(1);
        itemMeta.getLore().add("Connects to Iron Fences");
        itemStack.setItemMeta(itemMeta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(IronGate.getInstance(), "irongate"), itemStack);
        recipe.shape("BIB", "BIB", "   ");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

}
