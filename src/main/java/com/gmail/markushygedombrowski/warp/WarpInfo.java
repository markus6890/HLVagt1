package com.gmail.markushygedombrowski.warp;

import org.bukkit.Location;


public class WarpInfo {
    private String name;
    private Location location;



    public WarpInfo(String name, Location location) {
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
