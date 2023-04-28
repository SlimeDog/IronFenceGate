package com.github.justadeni.IronFenceGate.files;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfig extends Config{

    private static volatile MainConfig mainConfig;

    private MainConfig(){
        mainConfig = this;
    }

    public static MainConfig getInstance(){
        MainConfig cached = mainConfig;
        if (cached == null)
            cached = mainConfig = new MainConfig();
        return cached;
    }

    @Override
    public FileConfiguration fileConfiguration() {
        return IronFenceGate.get().getConfig();
    }

    @Override
    public void reload() {
        IronFenceGate.get().reloadConfig();
    }
}
