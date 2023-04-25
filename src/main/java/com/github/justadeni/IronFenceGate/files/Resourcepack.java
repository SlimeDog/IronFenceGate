package com.github.justadeni.IronFenceGate.files;

import com.github.justadeni.IronFenceGate.IronFenceGate;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class Resourcepack {

    private static File file;

    public static void setup() {
        File datafolder = new File(Bukkit.getServer().getPluginManager().getPlugin("IronFenceGate").getDataFolder() + "/resourcepack/");
        file = new File(datafolder, "IronFenceGate-v1.zip");
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(IronFenceGate.class.getResource("/IronFenceGate-v1.zip"), new File(datafolder + "/IronFenceGate-v1.zip"));
            } catch (IOException | NullPointerException ignored) {}
        }
    }

}
