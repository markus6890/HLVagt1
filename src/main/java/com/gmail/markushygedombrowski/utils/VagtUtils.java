package com.gmail.markushygedombrowski.utils;


import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;


import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;


import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class VagtUtils {
    private static HLvagt plugin;
    private static PlayerProfiles playerProfiles;
    private static Settings settings;

    public VagtUtils(HLvagt plugin, PlayerProfiles playerProfiles, Settings settings) {
        VagtUtils.plugin = plugin;
        VagtUtils.playerProfiles = playerProfiles;
        VagtUtils.settings = settings;
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
                if(item.getType() != Material.SKULL) {
                    item.setDurability((short) (item.getType().getMaxDurability() - item.getType().getMaxDurability()));
                    amount = amount + 1;
                }

            }

        }
        return amount;


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

    public static Player getAttacker(EntityDamageByEntityEvent event) {
        Player p;
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            p = (Player) projectile.getShooter();
            return p;
        }
        p = (Player) event.getDamager();
        return p;
    }


    public static List<String> top10Deaths(String perm) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();

        players.forEach(player -> {
            PlayerProfile profile = playerProfiles.getPlayerProfile(player.getUniqueId());
            stats.put(player.getName(), profile.getDeaths());
        });
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);

        sortmap.forEach(values -> stats.forEach((name, value) -> {
            if (value.equals(values)) {
                if (!list.contains(name + ": " + values)) {
                    list.add("§6" + name + ": §7" + values);
                }
            }
        }));
        return list;
    }
    public static boolean isPlayerNotOnline(Player player) {
        return player == null;
    }
    public static List<String> top10Kills(String perm) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();

        players.forEach(player -> {
            PlayerProfile profile = playerProfiles.getPlayerProfile(player.getUniqueId());
            stats.put(player.getName(), profile.getKills());
        });
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);

        sortmap.forEach(values -> stats.forEach((name, value) -> {
            if (value.equals(values)) {
                if (!list.contains(name + ": " + values)) {
                    list.add("§6" + name + ": §7" + values);
                }
            }
        }));
        return list;
    }
    public static List<String> top10Walk(String perm) {
        HashMap<String, Integer> stats = new HashMap<>();
        List<Player> players = VagtUtils.getPlayers(perm);
        List<String> list = new ArrayList<>();

        players.forEach(player -> {

            stats.put(player.getName(), player.getStatistic(Statistic.WALK_ONE_CM ) / 100);
        });
        List<Integer> sortmap = new LinkedList<>(stats.values());
        Collections.sort(sortmap);
        Collections.reverse(sortmap);

        sortmap.forEach(values -> stats.forEach((name, value) -> {
            if (value.equals(values)) {
                if (!list.contains(name + ": " + values)) {
                    list.add("§6" + name + ": §7" + values);
                }
            }
        }));
        return list;
    }




    public static boolean isSenderNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            System.out.println("denne command er kun for spillere");
            return true;
        }
        return false;
    }

    public static boolean notHasPermission(Player p, String perm) {
        if (!p.hasPermission(perm)) {
            p.sendMessage("§cDet har du ikke Permission til!");
            return true;
        }
        return false;
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
        return num <= pro;

    }


}
















