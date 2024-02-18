package com.gmail.markushygedombrowski.model;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProfile {

    private final double x = 0.1;
    private final double y = 2;
    private final UUID uuid;
    private final String name;
    private int pv;
    private int lvl;
    private int lon;
    private int kills;
    private int deaths;
    private int xp;
    private int achievements;



    private int vagtposter;


    public PlayerProfile(UUID uuid, String name, int pv, int lvl, int lon, int deaths, int kills, int xp, int vagtposter, int achievements) {
        this.uuid = uuid;
        this.name = name;
        this.pv = pv;
        this.lvl = lvl;
        this.lon = lon;
        this.deaths = deaths;
        this.kills = kills;
        this.xp = xp;
        this.vagtposter = vagtposter;
        this.achievements = achievements;
    }
    public int getAchievements() {
        return achievements;
    }

    public void setAchievements(int achievements) {
        this.achievements = achievements;
    }
    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public double getXpToNextLvl() {
        return (lvl/x) *y;
    }

    public int getVagtposter() {
        return vagtposter;
    }

    public void setVagtposter(int vagtposter) {
        this.vagtposter = vagtposter;
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
