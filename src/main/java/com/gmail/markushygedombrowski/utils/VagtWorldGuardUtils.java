package com.gmail.markushygedombrowski.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class VagtWorldGuardUtils {

    public static boolean isLocInRegion(Location loc, String regionName) {
        if (regionName == null) {
            return true;
        }
        ApplicableRegionSet set = getWGSet(loc);
        if (set == null) {
            return false;
        }
        for (ProtectedRegion r : set) {
            if (r.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }

    private static ApplicableRegionSet getWGSet(Location loc) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) {
            return null;
        }
        RegionManager rm = wg.getRegionManager(loc.getWorld());
        if (rm == null) {
            return null;
        }
        return rm.getApplicableRegions(com.sk89q.worldguard.bukkit.BukkitUtil.toVector(loc));
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
}
