package com.gmail.markushygedombrowski.config;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private HLvagt plugin = HLvagt.getPlugin(HLvagt.class);



    public FileConfiguration vagtFangePvpcfg;
    public File vagtFangePvpFile;

    public void setup() {
        List<File> configList = new ArrayList<>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();

        }
        vagtFangePvpFile = new File(plugin.getDataFolder(), "vagtFangePvp.yml");
        configList.add(vagtFangePvpFile);
        configList.forEach(file -> {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not create " + file.getName() + "File");
                }
            }
        });
        vagtFangePvpcfg = YamlConfiguration.loadConfiguration(vagtFangePvpFile);
    }
    public FileConfiguration getVagtFangePvpcfg() {
        return vagtFangePvpcfg;
    }
    public void saveVagtFangePvp() {
        try {
            vagtFangePvpcfg.save(vagtFangePvpFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not save vagtFangePvp.yml File");
        }
    }

    public void reloadVagtFangePvp() {
        vagtFangePvpcfg = YamlConfiguration.loadConfiguration(vagtFangePvpFile);
    }
}
