package com.gmail.markushygedombrowski.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class WarpManager {
    private final Map<String, WarpInfo> warpMap = new HashMap<>();
    private final JavaPlugin plugin;

    public WarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();
        warpMap.clear();
        for (String key : config.getConfigurationSection("warps").getKeys(false)) {
            String name = config.getString("warps." + key + ".name");
            double x = config.getDouble("warps." + key + ".location.x");
            double y = config.getDouble("warps." + key + ".location.y");
            double z = config.getDouble("warps." + key + ".location.z");
            String stringWorld = config.getString("warps." + key + ".location.world");
            World world = Bukkit.getWorld(stringWorld);
            Location location = new Location(world, x, y, z);
            WarpInfo warpInfo = new WarpInfo(name, location);
            warpMap.put(name, warpInfo);
        }
    }

    public void save(WarpInfo warpInfo) {
        FileConfiguration config = plugin.getConfig();
        String key = "" + (warpMap.size() + 1);
        config.set("warps." + key + ".name", warpInfo.getName());
        config.set("warps." + key + ".location.x", warpInfo.getLocation().getX());
        config.set("warps." + key + ".location.y", warpInfo.getLocation().getY());
        config.set("warps." + key + ".location.z", warpInfo.getLocation().getZ());
        config.set("warps." + key + ".location.world", warpInfo.getLocation().getWorld().getName());
        plugin.saveConfig();
        warpMap.put(warpInfo.getName(), warpInfo);

    }
    public WarpInfo getWarpInfo(String warpName) {
        return warpMap.get(warpName);
    }

}
