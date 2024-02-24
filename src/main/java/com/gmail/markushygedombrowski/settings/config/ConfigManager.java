package com.gmail.markushygedombrowski.settings.config;

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
    public FileConfiguration deliveredItemsCfg;
    public File deliveredItemsFile;




    public void setup() {
        List<File> configList = new ArrayList<>();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();

        }
        vagtFangePvpFile = new File(plugin.getDataFolder(), "vagtFangePvp.yml");
        deliveredItemsFile = new File(plugin.getDataFolder(), "deliveredItems.yml");

        configList.add(vagtFangePvpFile);
        configList.add(deliveredItemsFile);

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
        deliveredItemsCfg = YamlConfiguration.loadConfiguration(deliveredItemsFile);

    }
    public FileConfiguration getVagtFangePvpcfg() {
        return vagtFangePvpcfg;
    }
    public FileConfiguration getDeliveredItemsCfg() {
        return deliveredItemsCfg;
    }

    public void saveDeliveredItems() {
        try {
            deliveredItemsCfg.save(deliveredItemsFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not save deliveredItems.yml File");
        }
    }
    public void reloadDeliveredItems() {
        deliveredItemsCfg = YamlConfiguration.loadConfiguration(deliveredItemsFile);
    }
    public void saveVagtFangePvp() {
        try {
            vagtFangePvpcfg.save(vagtFangePvpFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "could not save vagtFangePvp.yml File");
        }
    }

}
