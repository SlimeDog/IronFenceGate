package com.github.justadeni.ironfencegate.files;

import com.github.justadeni.ironfencegate.IronFenceGate;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class Resourcepack {

    private static volatile Resourcepack resourcepack;
    private File file;

    private Resourcepack(){
        File datafolder = new File(Bukkit.getServer().getPluginManager().getPlugin("IronFenceGate").getDataFolder() + "/resourcepack/");
        file = new File(datafolder, "IronFenceGate-v1.zip");
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(IronFenceGate.class.getResource("/IronFenceGate-v1.zip"), new File(datafolder + "/IronFenceGate-v1.zip"));
            } catch (IOException | NullPointerException ignored) {}
        }
    }

    public static Resourcepack getInstance() {
        Resourcepack cached = resourcepack;
        if (cached == null)
            cached = resourcepack = new Resourcepack();
        return cached;
    }

}
