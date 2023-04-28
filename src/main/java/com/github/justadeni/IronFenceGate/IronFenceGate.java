package com.github.justadeni.IronFenceGate;

import com.github.justadeni.IronFenceGate.command.IFGCommand;
import com.github.justadeni.IronFenceGate.command.TabComplete;
import com.github.justadeni.IronFenceGate.events.*;
import com.github.justadeni.IronFenceGate.files.MainConfig;
import com.github.justadeni.IronFenceGate.files.MessageConfig;
import com.github.justadeni.IronFenceGate.files.Resourcepack;
import com.github.justadeni.IronFenceGate.misc.NonCollision;
import com.github.justadeni.IronFenceGate.misc.Metrics;
import com.github.justadeni.IronFenceGate.misc.Recipe;
import com.github.justadeni.IronFenceGate.misc.LogFilter;
import com.github.justadeni.IronFenceGate.nms.entity.CustomEntities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class IronFenceGate extends JavaPlugin {

    private static IronFenceGate instance;

    public static IronFenceGate get(){
        return instance;
    }

    @Override
    public void onEnable() {
        NonCollision.setup();
        saveDefaultConfig();
        Resourcepack.setup();
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
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new EntityRightClick(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new BlockUpdate(), this);
        getServer().getPluginManager().registerEvents(new ResourcesCheck(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

    }

    @Override
    public void onLoad() {
        if (instance == null)
            instance = this;

        new LogFilter();

        CustomEntities.register();
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
