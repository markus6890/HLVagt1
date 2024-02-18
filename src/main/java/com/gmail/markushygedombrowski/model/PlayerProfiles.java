package com.gmail.markushygedombrowski.model;

import com.gmail.markushygedombrowski.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfiles {
    private Map<UUID, PlayerProfile> profileMap = new HashMap<>();
    private Settings settings;
    private ConfigManager configManager;
    private Sql sql;

    public PlayerProfiles(Settings settings, ConfigManager configManager, Sql sql) {

        this.settings = settings;
        this.configManager = configManager;
        this.sql = sql;
    }

    public void load() throws SQLException {
        Connection connection = sql.getConnection();
        profileMap.clear();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vagtprofile");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            UUID uuid = UUID.fromString(resultSet.getString("UUID"));
            String name = resultSet.getString("name");
            int deaths = resultSet.getInt("deaths");
            int kills = resultSet.getInt("kills");
            int pvs = resultSet.getInt("pvs");
            int level = resultSet.getInt("level");
            int exp = resultSet.getInt("exp");
            int vagtposter = resultSet.getInt("vagtposter");
            int salary = resultSet.getInt("salary");
            int achevments = resultSet.getInt("achevments");
            PlayerProfile profile = new PlayerProfile(uuid, name, pvs, level, salary, deaths, kills, exp, vagtposter, achevments);
            profileMap.put(uuid, profile);
        }



    }

    public void saveAll() {
        profileMap.values().forEach(profile -> {
            try {
                save(profile);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


    }


    public void save(PlayerProfile profile) throws SQLException {
        Connection connection = sql.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO vagtprofile (UUID, name, deaths, kills, pvs, level, nextlevelexp, exp, vagtposter, salary, achevments) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = ?, deaths = ?, kills = ?, pvs = ?, level = ?, nextlevelexp = ?, exp = ?, vagtposter = ?, salary = ?, achevments = ?");

        statement.setString(1, profile.getUuid().toString());
        statement.setString(2, profile.getName());
        statement.setInt(3, profile.getDeaths());
        statement.setInt(4, profile.getKills());
        statement.setInt(5, profile.getPv());
        statement.setInt(6, profile.getLvl());
        statement.setDouble(7, profile.getXpToNextLvl());
        statement.setInt(8, profile.getXp());
        statement.setInt(9, profile.getVagtposter());
        statement.setInt(10, profile.getLon());
        statement.setInt(11, profile.getAchievements());


        statement.setString(12, profile.getName());
        statement.setInt(13, profile.getDeaths());
        statement.setInt(14, profile.getKills());
        statement.setInt(15, profile.getPv());
        statement.setInt(16, profile.getLvl());
        statement.setDouble(17, profile.getXpToNextLvl());
        statement.setInt(18, profile.getXp());
        statement.setInt(19, profile.getVagtposter());
        statement.setInt(20, profile.getLon());
        statement.setInt(21, profile.getAchievements());

        statement.executeUpdate();
        sql.closeAllSQL(connection, statement, null);
        profileMap.put(profile.getUuid(), profile);

    }

    public PlayerProfile getPlayerProfile(UUID uuid) {
        return profileMap.get(uuid);
    }

    public void createVagt(Player p, PlayerProfile profile) throws SQLException {
        if (profile != null) return;

        int lon = settings.getLonp();
        if (p.hasPermission("officer")) {
            lon = settings.getLonoffi();
        } else if (p.hasPermission("a-vagt")) {
            lon = settings.getLona();
        } else if (p.hasPermission("b-vagt")) {
            lon = settings.getLonb();
        } else if (p.hasPermission("c-vagt")) {
            lon = settings.getLonc();
        }
        profile = new PlayerProfile(p.getUniqueId(), p.getName(), 1, 1, lon, 0, 0, 0, 0,0);
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
