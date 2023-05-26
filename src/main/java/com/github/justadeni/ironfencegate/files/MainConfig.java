package com.github.justadeni.ironfencegate.files;

import com.github.justadeni.ironfencegate.IronFenceGate;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfig extends Config{

    private static volatile MainConfig mainConfig;

    private MainConfig(){
        IronFenceGate.getInstance().saveDefaultConfig();
    }

    public static MainConfig getInstance(){
        MainConfig cached = mainConfig;
        if (cached == null)
            cached = mainConfig = new MainConfig();
        return cached;
    }

    @Override
    public FileConfiguration fileConfiguration() {
        return IronFenceGate.getInstance().getConfig();
    }

    @Override
    public void reload() {
        IronFenceGate.getInstance().reloadConfig();
    }
}
