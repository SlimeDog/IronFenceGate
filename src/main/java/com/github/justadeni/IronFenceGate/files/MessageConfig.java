package com.github.justadeni.IronFenceGate.files;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageConfig extends Config{

    private static File file;
    private static FileConfiguration messageConfiguration;

    private static MessageConfig messageConfig;

    public static void setup() {
        File datafolder = Bukkit.getServer().getPluginManager().getPlugin("IronFenceGate").getDataFolder();
        file = new File(datafolder, "messages.yml");
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(IronFenceGate.class.getResource("/messages.yml"), new File(datafolder + "/messages.yml"));
                file = new File(datafolder, "messages.yml");
            } catch (IOException | NullPointerException e) {}
        }

        messageConfiguration = YamlConfiguration.loadConfiguration(file);
        messageConfig = new MessageConfig();
    }

    public static MessageConfig get(){
        return messageConfig;
    }

    @Override
    public FileConfiguration fileConfiguration(){
        return messageConfiguration;
    }

    @Override
    public void reload(){
        setup();
    }

}
