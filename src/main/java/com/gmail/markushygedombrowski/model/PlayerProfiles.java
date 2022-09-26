package com.gmail.markushygedombrowski.model;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfiles {
    private Map<UUID, PlayerProfile> profileMap = new HashMap<>();
    private final JavaPlugin plugin;

    public PlayerProfiles(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        profileMap.clear();
        for(String key : config.getConfigurationSection("vagter").getKeys(false)) {
            String strUuid = config.getString("vagter." + key + ".uuid");
            String playerName = config.getString("vagter." + key + ".name");
            int pv = config.getInt("vagter." + key + ".pv");
            int lvl = config.getInt("vagter." + key + ".lvl");
            int lon = config.getInt("vagter." + key + ".løn");
            int deaths= config.getInt("vagter." + key + ".deaths");
            int kills = config.getInt("vagter. " + key + ".kills");
            UUID uuid = UUID.fromString(strUuid);
            PlayerProfile playerProfile = new PlayerProfile(uuid,playerName,pv,lvl,lon,deaths,kills);
            profileMap.put(uuid, playerProfile);
        }
    }

    public void save(PlayerProfile profile) {
        FileConfiguration config = plugin.getConfig();
        String key = "" + (profileMap.size() + 1);
        UUID uuid = profile.getUuid();
        String stuuid = uuid.toString();
        config.set("vagter." + key + ".uuid", stuuid);
        config.set("vagter." + key + ".name", profile.getName());
        config.set("vagter." + key + ".pv", profile.getPv());
        config.set("vagter." + key + ".lvl", profile.getLvl());
        config.set("vagter." + key + ".løn", profile.getLon());
        config.set("vagter." + key + ".deaths",profile.getDeaths());

        plugin.saveConfig();
        profileMap.put(profile.getUuid(),profile);
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return profileMap.get(uuid);
    }

}
