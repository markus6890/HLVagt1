package com.gmail.markushygedombrowski.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProfile {

    private UUID uuid;
    private String name;
    private int pv;
    private int lvl;
    private int lon;


    private int kills;


    private int deaths;


    public PlayerProfile(UUID uuid, String name, int pv, int lvl, int lon, int deaths, int kills) {
        this.uuid = uuid;
        this.name = name;
        this.pv = pv;
        this.lvl = lvl;
        this.lon = lon;
        this.deaths = deaths;
        this.kills = kills;
    }

    // get players info
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getPv() {
        return pv;
    }

    public int getLvl() {
        return lvl;
    }

    public int getLon() {
        return lon;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }


    // set some values
    public void setLon(int lon) {
        this.lon = lon;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }


}
