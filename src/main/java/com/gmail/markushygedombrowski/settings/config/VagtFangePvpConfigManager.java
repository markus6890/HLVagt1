package com.gmail.markushygedombrowski.settings.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class VagtFangePvpConfigManager {
    private List<String> vFpvp = new ArrayList<>();



    public void load(FileConfiguration config) {
        vFpvp.clear();
        vFpvp = config.getStringList("regions.name");

    }


    public List<String> getvFpvp() {
        return vFpvp;
    }
}
