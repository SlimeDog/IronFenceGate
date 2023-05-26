package com.github.justadeni.ironfencegate;

import com.github.justadeni.ironfencegate.animation.Task;
import com.github.justadeni.ironfencegate.command.IFGCommand;
import com.github.justadeni.ironfencegate.command.TabComplete;
import com.github.justadeni.ironfencegate.events.*;
import com.github.justadeni.ironfencegate.files.MainConfig;
import com.github.justadeni.ironfencegate.files.MessageConfig;
import com.github.justadeni.ironfencegate.files.Resourcepack;
import com.github.justadeni.ironfencegate.logic.NonCollision;
import com.github.justadeni.ironfencegate.misc.Metrics;
import com.github.justadeni.ironfencegate.misc.Recipe;
import com.github.justadeni.ironfencegate.misc.LogFilter;
import com.github.justadeni.ironfencegate.nms.entity.EntityRegisterer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IronFenceGate extends JavaPlugin {

    private static IronFenceGate ironFenceGate;

    public static IronFenceGate getInstance(){
        return ironFenceGate;
    }

    @Override
    public void onEnable() {
        //Initializing these singletons at enable so that they create their files/team
        NonCollision.getInstance();
        Resourcepack.getInstance();
        MessageConfig.getInstance();

        MainConfig mc = MainConfig.getInstance();
        if (mc.getBoolean("enable-metrics")) {
            Metrics metrics = new Metrics(this, 18193);
        }
        Recipe recipe = Recipe.getInstance();
        Bukkit.addRecipe(recipe.getRecipe(0));
        Bukkit.addRecipe(recipe.getRecipe(1));
        getCommand("ironfencegate").setExecutor(new IFGCommand());
        getCommand("ironfencegate").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new PlayerInteract(new Task()), this);
        getServer().getPluginManager().registerEvents(new EntityRightClick(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new BlockUpdate(), this);
        getServer().getPluginManager().registerEvents(ResourcesCheck.getInstance(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    @Override
    public void onLoad() {
        if (ironFenceGate == null)
            ironFenceGate = this;

        new LogFilter();

        EntityRegisterer.register();
    }

    @Override
    public void onDisable() {
        Recipe recipe = Recipe.getInstance();
        Bukkit.removeRecipe(recipe.getNamespacedKey(0));
        Bukkit.removeRecipe(recipe.getNamespacedKey(1));
    }

    public void log(String msg){
        getLogger().info(msg);
    }
}
