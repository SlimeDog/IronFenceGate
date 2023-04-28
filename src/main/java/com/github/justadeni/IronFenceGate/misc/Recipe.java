package com.github.justadeni.IronFenceGate.misc;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.logic.StandManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Recipe {

    private static volatile Recipe recipe;

    private ArrayList<NamespacedKey> keylist = new ArrayList<>();
    private ArrayList<ShapedRecipe> recipes = new ArrayList<>();

    private Recipe(){
        MainConfig mc = MainConfig.getInstance();

        ItemStack itemStack = new ItemStack(Material.WARPED_FENCE_GATE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(mc.getStringColors("item.name"));
        itemMeta.setCustomModelData(StandManager.IDFIRST+1);

        ArrayList<String> colored = new ArrayList<>();
        for (String line : mc.getList("item.lore")){
            colored.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        itemMeta.setLore(colored);
        itemStack.setItemMeta(itemMeta);

        keylist.add(new NamespacedKey(IronFenceGate.get(), "UpperIronFenceGate"));
        ShapedRecipe upperRecipe = new ShapedRecipe(keylist.get(0), itemStack);
        upperRecipe.shape("BIB", "BIB", "   ");
        upperRecipe.setIngredient('B', Material.IRON_BARS);
        upperRecipe.setIngredient('I', Material.IRON_INGOT);
        recipes.add(upperRecipe);

        keylist.add(new NamespacedKey(IronFenceGate.get(), "LowerIronFenceGate"));
        ShapedRecipe lowerRecipe = new ShapedRecipe(keylist.get(1), itemStack);
        lowerRecipe.shape("   ", "BIB", "BIB");
        lowerRecipe.setIngredient('B', Material.IRON_BARS);
        lowerRecipe.setIngredient('I', Material.IRON_INGOT);
        recipes.add(lowerRecipe);
    }

    public ItemStack getResult(){
        return recipes.get(0).getResult();
    }

    public ShapedRecipe getRecipe(int index){
        return recipes.get(index);
    }

    public NamespacedKey getNamespacedKey(int index){
        return keylist.get(index);
    }

    public static Recipe getInstance(){
        Recipe cached = recipe;
        if (cached == null)
            cached = recipe = new Recipe();
        return cached;
    }

}
