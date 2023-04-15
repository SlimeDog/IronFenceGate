package com.github.justadeni.irongate.misc;

import com.github.justadeni.irongate.IronFenceGate;
import com.github.justadeni.irongate.logic.StandManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Recipe {

    public static ArrayList<NamespacedKey> keylist = new ArrayList<>();

    public static ArrayList<ShapedRecipe> recipes = new ArrayList<>();

    public static void makeRecipes() {
        ConfigManager cm = ConfigManager.get();

        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', cm.getString("item.name")));
        itemMeta.setCustomModelData(StandManager.getIdFirst());

        ArrayList<String> colored = new ArrayList<>();
        for (String line : cm.getList("item.lore")){
            colored.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        itemMeta.setLore(colored);
        itemStack.setItemMeta(itemMeta);

        keylist.add(new NamespacedKey(IronFenceGate.getInstance(), "UpperIronFenceGate"));
        ShapedRecipe upperRecipe = new ShapedRecipe(keylist.get(0), itemStack);
        upperRecipe.shape("BIB", "BIB", "   ");
        upperRecipe.setIngredient('B', Material.IRON_BARS);
        upperRecipe.setIngredient('I', Material.IRON_INGOT);
        recipes.add(upperRecipe);

        keylist.add(new NamespacedKey(IronFenceGate.getInstance(), "LowerIronFenceGate"));
        ShapedRecipe lowerRecipe = new ShapedRecipe(keylist.get(1), itemStack);
        lowerRecipe.shape("   ", "BIB", "BIB");
        lowerRecipe.setIngredient('B', Material.IRON_BARS);
        lowerRecipe.setIngredient('I', Material.IRON_INGOT);
        recipes.add(lowerRecipe);
    }

}
