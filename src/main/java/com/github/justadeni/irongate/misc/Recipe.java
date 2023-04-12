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

    public static NamespacedKey upperKey;
    public static NamespacedKey lowerKey;

    public static ShapedRecipe getUpperRecipe() {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&rIron Gate"));
        itemMeta.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Connects to Iron Fences");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        upperKey = new NamespacedKey(IronFenceGate.getInstance(), "UpperIronFenceGate");
        ShapedRecipe recipe = new ShapedRecipe(upperKey, itemStack);
        recipe.shape("BIB", "BIB", "   ");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

    public static ShapedRecipe getLowerRecipe() {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&rIron Gate"));
        itemMeta.setCustomModelData(1);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Connects to Iron Fences");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        lowerKey = new NamespacedKey(IronFenceGate.getInstance(), "LowerIronFenceGate");
        ShapedRecipe recipe = new ShapedRecipe(lowerKey, itemStack);
        recipe.shape("   ", "BIB", "BIB");
        recipe.setIngredient('B', Material.IRON_BARS);
        recipe.setIngredient('I', Material.IRON_INGOT);

        return recipe;
    }

}
