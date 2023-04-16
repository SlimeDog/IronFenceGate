package com.github.justadeni.IronFenceGate.files;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public abstract class Config {

    public void reload(){
        IronFenceGate.getInstance().reloadConfig();
    }

    public FileConfiguration fileConfiguration(){
        return IronFenceGate.getInstance().getConfig();
    }

    public boolean getBoolean(String path){
        return fileConfiguration().getBoolean(path);
    }

    public String getString(String path){
        return fileConfiguration().getString(path);
    }

    public String getStringColors(String path){
        try {
            return ChatColor.translateAlternateColorCodes('&', fileConfiguration().getString(path));
        } catch (NullPointerException e) {
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

    public ArrayList<String> getList(String path){
        return new ArrayList<>(fileConfiguration().getStringList(path));
    }

    public void sendMessage(CommandSender sender, String path){
        String message = this.getStringColors(path);
        if (!message.isBlank())
            sender.sendMessage(getStringColors("prefix") + message);
    }

}
