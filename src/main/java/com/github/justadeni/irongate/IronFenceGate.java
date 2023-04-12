package com.github.justadeni.irongate;

import com.github.justadeni.irongate.events.BlockPlace;
import com.github.justadeni.irongate.events.EntityLeftClick;
import com.github.justadeni.irongate.events.EntityRightClick;
import com.github.justadeni.irongate.events.PlayerInteract;
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
        Bukkit.addRecipe(Recipe.getUpperRecipe());
        Bukkit.addRecipe(Recipe.getLowerRecipe());
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EntityLeftClick(), this);
        getServer().getPluginManager().registerEvents(new EntityRightClick(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(Recipe.upperKey);
        Bukkit.removeRecipe(Recipe.lowerKey);
    }
}
