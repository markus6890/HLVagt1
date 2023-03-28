package com.gmail.markushygedombrowski.model;

import com.gmail.markushygedombrowski.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfiles {
    private Map<UUID, PlayerProfile> profileMap = new HashMap<>();
    private Settings settings;
    private ConfigManager configManager;

    public PlayerProfiles(Settings settings, ConfigManager configManager) {

        this.settings = settings;
        this.configManager = configManager;
    }

    public void load() {
        FileConfiguration config = configManager.getPlayercfg();
        profileMap.clear();
        config.getConfigurationSection("vagter").getKeys(false).forEach(key -> {
            String uuid = config.getString("vagter." + key + ".uuid");
            String name = config.getString("vagter." + key + ".name");
            int pv = config.getInt("vagter." + key + ".pv");
            int lvl = config.getInt("vagter." + key + ".lvl");
            int lon = config.getInt("vagter." + key + ".løn");
            int deaths = config.getInt("vagter." + key + ".deaths");
            int kills = config.getInt("vagter." + key + ".kills");
            PlayerProfile profile = new PlayerProfile(UUID.fromString(uuid), name, pv, lvl, lon, deaths, kills);
            profileMap.put(profile.getUuid(), profile);
        });

    }

    public void save(PlayerProfile profile) {
        FileConfiguration config = configManager.getPlayercfg();
        String key = profile.getName();
        UUID uuid = profile.getUuid();
        String stuuid = uuid.toString();
        config.set("vagter." + key + ".uuid", stuuid);
        config.set("vagter." + key + ".name", profile.getName());
        config.set("vagter." + key + ".pv", profile.getPv());
        config.set("vagter." + key + ".lvl", profile.getLvl());
        config.set("vagter." + key + ".løn", profile.getLon());
        config.set("vagter." + key + ".deaths", profile.getDeaths());
        config.set("vagter." + key + ".kills", profile.getKills());
        configManager.savePlayer();
        profileMap.put(profile.getUuid(), profile);
    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return profileMap.get(uuid);
    }

    public void createVagt(Player p, PlayerProfile profile) {
        if (profile != null) return;

        int lon = settings.getLonp();
        if (p.hasPermission("a-vagt")) {
            lon = settings.getLona();
        } else if (p.hasPermission("b-vagt")) {
            lon = settings.getLonb();
        } else if (p.hasPermission("c-vagt")) {
            lon = settings.getLonc();
        }
        profile = new PlayerProfile(p.getUniqueId(), p.getName(), 1, 1, lon, 0, 0);
        System.out.println("name" + profile.getName());
        System.out.println("UUID" + profile.getUuid());
        System.out.println("løn" + profile.getLon());
        System.out.println("deaths" + profile.getDeaths());
        System.out.println("kills" + profile.getKills());
        save(profile);
    }

    public void removeVagt(PlayerProfile profile) {
        FileConfiguration config = configManager.getPlayercfg();
        String key = profile.getName();
        config.set("vagter." + key, null);
        configManager.savePlayer();
        profileMap.remove(profile.getUuid());
    }

}
