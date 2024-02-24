package com.gmail.markushygedombrowski.settings;

import org.bukkit.configuration.file.FileConfiguration;

public class SqlSettings {
    private String database;
    private String host;
    private int port;
    private String user;
    private String password;

    public void load(FileConfiguration config) {
        this.database = config.getString("sql.database");
        this.host = config.getString("sql.host");
        this.port = config.getInt("sql.port");
        this.user = config.getString("sql.user");
        this.password = config.getString("sql.password");
    }
    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
