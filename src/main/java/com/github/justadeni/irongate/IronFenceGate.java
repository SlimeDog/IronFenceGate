package com.github.justadeni.irongate;

import com.github.justadeni.irongate.events.*;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IronFenceGate extends JavaPlugin {

    private static IronFenceGate instance;

    public static IronFenceGate getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Recipe.makeRecipes();
        Bukkit.addRecipe(Recipe.recipes.get(0));
        Bukkit.addRecipe(Recipe.recipes.get(1));
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EntityLeftClick(), this);
        getServer().getPluginManager().registerEvents(new EntityRightClick(), this);
        getServer().getPluginManager().registerEvents(new BlockUpdate(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(Recipe.keylist.get(0));
        Bukkit.removeRecipe(Recipe.keylist.get(1));
    }
}
