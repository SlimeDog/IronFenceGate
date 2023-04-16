package com.github.justadeni.IronFenceGate.files;

public class MainConfig extends Config{
    /**
     * For performance reasons,
     * ConfigManager is instantiated
     * once and then only accessed through
     * this static instance
     */
    private static MainConfig mainConfig;

    public static void setup(){
        mainConfig = new MainConfig();
    }

    public static MainConfig get(){
        return mainConfig;
    }
}
