package com.gmail.markushygedombrowski.utils;


import com.gmail.markushygedombrowski.HLvagt;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;



import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;


import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;



import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


import static org.bukkit.Bukkit.getServer;

public class VagtUtils {
    private static HLvagt plugin;


    public VagtUtils(HLvagt plugin) {
        VagtUtils.plugin = plugin;

    }


    public static boolean removeItems(Player p, List<ItemStack> items) {
        for (ItemStack item : items) { //dette loop checker om han har alle items inden vi fjerner dem
            if (!p.getInventory().contains(item)) {

                return false; //returnere false hvis et af vores items i vores list ikke er der i hans inventory
            }
        }
        for (ItemStack item : items) { //dette looper fjerner alle items fordi vi ved der er nok nu
            p.getInventory().removeItem(item);
        }
        return true;
    }

    public static int repairItems(Player p) {
        ItemStack[] items = p.getInventory().getContents();
        int amount = 0;
        for (ItemStack item : items) {
            if (!(item == null)) {
                item.setDurability((short) (item.getType().getMaxDurability() - item.getType().getMaxDurability()));
                amount = amount + 1;
            }

        }
        return amount;


    }

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


    public static List<Player> getPlayers(String perm) {
        List<Player> playerList = new ArrayList<>();

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission(perm)) {
                playerList.add(all.getPlayer());
            }
        }
        return playerList;
    }


    public static List<String> top10(String perm, Statistic statistic) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();
        for (Player player : players) {

            stats.put(player.getName(), player.getStatistic(statistic));
        }
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        for (int values : sortmap) {
            for (String name : stats.keySet()) {
                if (stats.get(name) == values) {
                    if (!list.contains(name + ": " + values)) {
                        list.add("§6" + name + ": §7" + values);
                    }
                }
            }
        }
        return list;
    }

    public static List<String> top10Money(String perm) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();
        for (Player player : players) {
            stats.put(player.getName(), (int) plugin.econ.getBalance(player));
        }
        String pattern = "###,###.##";
        DecimalFormat df = new DecimalFormat(pattern);
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);
        for (int values : sortmap) {
            for (String name : stats.keySet()) {
                if (stats.get(name) == values) {
                    if (!list.contains(name + ": " + values)) {
                        list.add("§6" + name + ": §7" + df.format(values));
                    }
                }
            }
        }
        return list;
    }

    public static boolean procent(double pro) {
        Random r = new Random();
        double num = r.nextInt(100);
        return num < pro;

    }


}
















