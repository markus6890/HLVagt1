package com.gmail.markushygedombrowski.warp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpCommand implements CommandExecutor {
    private final WarpManager warpManager;
    private final JavaPlugin plugin;

    public WarpCommand(WarpManager warpManager, JavaPlugin plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("warpAllow")){
            sender.sendMessage("Det har du ikke permission til!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e/hlwarp <name>");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cthis command can only be used by players");
            return true;
        }
        Player player = (Player) sender;
        String warpName = args[0];
        WarpInfo info = warpManager.getWarpInfo(warpName);

        if (alias.equalsIgnoreCase("sethlWarp")) {
            setWarp(sender, player, warpName, info);
            return true;
        }

        if (info == null) {
            sender.sendMessage("§cThat warp doesn't exist");
            return true;
        }

        /*Runnable runnable = new Runnable() {
            @Override
            public void run() {
                player.teleport(info.getLocation());
            }
        };

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, 20, 20);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
            }
        }, 20 * 20); */

        player.teleport(info.getLocation());
        return true;
    }

    private void setWarp(CommandSender sender, Player player, String warpName, WarpInfo info) {
        if (!sender.hasPermission("setwarp")) {
            sender.sendMessage("Det har du ikke permission til!");
            return;
        }
        if (info != null) {
            sender.sendMessage("§cThat warp already exist!");
            return;
        }
        Location location = player.getLocation();
        info = new WarpInfo(warpName, location);
        warpManager.save(info);
        sender.sendMessage("§a§lWarp created at §e" + location.toString());
    }
}
