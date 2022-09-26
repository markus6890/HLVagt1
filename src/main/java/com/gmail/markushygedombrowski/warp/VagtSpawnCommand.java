package com.gmail.markushygedombrowski.warp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VagtSpawnCommand implements CommandExecutor {
    private final VagtSpawnManager vagtSpawnManager;
    private final JavaPlugin plugin;

    public VagtSpawnCommand(VagtSpawnManager vagtSpawnManager, JavaPlugin plugin) {
        this.vagtSpawnManager = vagtSpawnManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("warpAllow")){
            sender.sendMessage("Det har du ikke permission til!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§e/vagtspawn <name>");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cthis command can only be used by players");
            return true;
        }
        Player player = (Player) sender;
        String warpName = args[0];
        VagtSpawnInfo info = vagtSpawnManager.getWarpInfo(warpName);

        if (alias.equalsIgnoreCase("setvagtspawn")) {
            setWarp(sender, player, warpName, info);
            return true;
        }

        if (info == null) {
            sender.sendMessage("§cThat warp doesn't exist");
            return true;
        }


        player.teleport(info.getLocation());
        return true;
    }

    private void setWarp(CommandSender sender, Player player, String warpName, VagtSpawnInfo info) {
        if (!sender.hasPermission("setwarp")) {
            sender.sendMessage("Det har du ikke permission til!");
            return;
        }
        if (info != null) {
            sender.sendMessage("§cThat warp already exist!");
            return;
        }
        Location location = player.getLocation();
        info = new VagtSpawnInfo(warpName, location);
        vagtSpawnManager.save(info);
        sender.sendMessage("§a§lWarp created at §e" + location.toString());
    }
}
