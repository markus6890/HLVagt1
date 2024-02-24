package com.gmail.markushygedombrowski.settings.deliveredItems;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ItemProfileLoader {
    private HashMap<String, ItemProfile> itemProfiles = new HashMap<>();

    public void load(FileConfiguration config) {
        itemProfiles.clear();
        config.getConfigurationSection("deliveredItems").getKeys(false).forEach(key -> {
            int exp = config.getInt("deliveredItems." + key + ".exp");
            String name = config.getString("deliveredItems." + key + ".name");
            itemProfiles.put(name, new ItemProfile(exp, name));
        });
    }

    public ItemProfile getItemProfile(String name) {
        return itemProfiles.get(name);
    }
}
