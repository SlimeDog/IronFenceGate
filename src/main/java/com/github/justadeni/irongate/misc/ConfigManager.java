package com.github.justadeni.irongate.misc;

import com.github.justadeni.irongate.IronFenceGate;
import org.bukkit.ChatColor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ConfigManager {
    /**
     * For performance reasons,
     * ConfigManager is instantiated
     * once and then only accessed through
     * this static instance
     */
    private static ConfigManager configManager;

    public static void setup(){
        configManager = new ConfigManager();
    }

    public static ConfigManager get(){
        return configManager;
    }

    private static IronFenceGate main(){
        return IronFenceGate.getInstance();
    }

    public void save(){
        main().saveConfig();
    }

    public void reload(){
        main().reloadConfig();
    }

    public boolean getBoolean(String path){
        return main().getConfig().getBoolean(path);
    }

    public String getString(String path){
        return main().getConfig().getString(path);
    }

    public String getStringColors(String path){
        try {
            return ChatColor.translateAlternateColorCodes('&', main().getConfig().getString(path));
        } catch (NullPointerException e) {
            return "";
        }
    }

    public Double getDouble(String path){
        return main().getConfig().getDouble(path);
    }

    public Float getFloat(String path){
        return getDouble(path).floatValue();
    }

    public Integer getInt(String path){
        return main().getConfig().getInt(path);
    }

    public ArrayList<String> getList(String path){
        return new ArrayList<>(main().getConfig().getStringList(path));
    }
}
