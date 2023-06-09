package com.github.justadeni.ironfencegate.files;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class Config {

    public abstract FileConfiguration fileConfiguration();

    public abstract void reload();

    public boolean getBoolean(String path){
        return fileConfiguration().getBoolean(path);
    }

    public String getString(String path){
        return fileConfiguration().getString(path);
    }

    public String getStringColors(String path){
        try {
            return ChatColor.translateAlternateColorCodes('&', fileConfiguration().getString(path));
        } catch (Exception e) {
            return "";
        }
    }

    public Double getDouble(String path){
        return fileConfiguration().getDouble(path);
    }

    public Float getFloat(String path){
        return getDouble(path).floatValue();
    }

    public Integer getInt(String path){
        return fileConfiguration().getInt(path);
    }

    public List<String> getList(String path){
        return new ArrayList<>(fileConfiguration().getStringList(path));
    }

    public void sendMessage(CommandSender sender, String path){
        String message = this.getStringColors(path);
        if (!message.isBlank())
            sender.sendMessage(getStringColors("prefix") + message);
    }

}
