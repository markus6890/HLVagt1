package com.gmail.markushygedombrowski.model;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private int bufflength;
    private int speed;
    private int strengh;
    private int absorption;


    private int buffPay;
    private int lonc;
    private int lonb;
    private int lona;
    private int lonp;
    private int lonoffi;
    private int lonviceins;
    private int lonins;
    private int londire;
    private int lonTime;



    private int vagtheaddrop;

    private String vagthead;

    private List<ItemStack> items = new ArrayList<>();


    public void load(FileConfiguration config) {
        this.bufflength = config.getInt("buff.buffLength");
        this.speed = config.getInt("buff.buffamplifer.speed");
        this.strengh = config.getInt("buff.buffamplifer.strengh");
        this.absorption = config.getInt("buff.buffamplifer.absorption");
        this.lonc = config.getInt("vagt.lonc");
        this.lonb = config.getInt("vagt.lonb");
        this.lona = config.getInt("vagt.lona");
        this.lonp = config.getInt("vagt.lonp");
        this.lonoffi = config.getInt("vagt.lonoffi");
        this.lonviceins = config.getInt("vagt.lonvicins");
        this.lonins = config.getInt("vagt.lonins");
        this.londire = config.getInt("vagt.londire");
        this.lonTime = config.getInt("vagt.lontime");
        this.buffPay = config.getInt("buff.buffamplifer.pay");
        this.vagthead = config.getString("vagt.vagthead");
        this.vagtheaddrop = config.getInt("vagt.vagtheaddrop");


    }
    public int getBufflength () {
        return bufflength;
    }
    public int getSpeed() {
        return speed;
    }
    public int getStrengh() {
        return strengh;
    }
    public int getAbsorption() {
        return absorption;
    }
    public int getLonc() {
        return lonc;
    }
    public int getLonb() {
        return lonb;
    }
    public int getLona() {
        return lona;
    }
    public int getLonp() {
        return lonp;
    }
    public int getLonoffi() {
        return lonoffi;
    }
    public int getLonTime() {
        int timesend = (lonTime * 60);
        return timesend;
    }
    public int getBuffPay() {
        return buffPay;
    }
    public String getVagthead() {
        return vagthead;
    }
    public void setVagthead(String vagthead) {
        this.vagthead = vagthead;
    }
    public void setLonTime(int lonTime) {
        this.lonTime = lonTime;
    }
    public int getVagtheaddrop() {
        return vagtheaddrop;
    }
    public void setVagtheaddrop(int vagtheaddrop) {
        this.vagtheaddrop = vagtheaddrop;
    }

}

