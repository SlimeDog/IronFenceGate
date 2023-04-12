package com.github.justadeni.irongate;

import com.github.justadeni.irongate.events.BlockPlace;
import com.github.justadeni.irongate.events.EntityLeftClick;
import com.github.justadeni.irongate.events.PlayerInteract;
import com.github.justadeni.irongate.misc.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IronGate extends JavaPlugin {

    private static IronGate instance;

    public static IronGate getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Bukkit.addRecipe(Recipe.getRecipe());
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EntityLeftClick(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
