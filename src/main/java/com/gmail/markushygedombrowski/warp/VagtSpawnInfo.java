package com.gmail.markushygedombrowski.warp;

import org.bukkit.Location;


public class VagtSpawnInfo {
    private String name;
    private Location location;



    public VagtSpawnInfo(String name, Location location) {
        this.name = name;
        this.location = location;

    }

    public String getName() {
        return name;
    }
    public Location getLocation () {
        return location;
    }



}
