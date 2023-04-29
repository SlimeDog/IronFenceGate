package com.github.justadeni.ironfencegate.files;

import com.github.justadeni.ironfencegate.IronFenceGate;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageConfig extends Config{

    private static volatile MessageConfig messageConfig;

    private File file;
    private FileConfiguration messageConfiguration;

    private MessageConfig(){
        File datafolder = Bukkit.getServer().getPluginManager().getPlugin("IronFenceGate").getDataFolder();
        file = new File(datafolder, "messages.yml");
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(IronFenceGate.class.getResource("/messages.yml"), new File(datafolder + "/messages.yml"));
                file = new File(datafolder, "messages.yml");
            } catch (IOException | NullPointerException ignored) {}
        }

        messageConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static MessageConfig getInstance(){
        MessageConfig cached = messageConfig;
        if (cached == null)
            cached = messageConfig = new MessageConfig();
        return cached;
    }

    @Override
    public FileConfiguration fileConfiguration(){
        return messageConfiguration;
    }

    @Override
    public void reload(){
        messageConfig = new MessageConfig();
    }

}
