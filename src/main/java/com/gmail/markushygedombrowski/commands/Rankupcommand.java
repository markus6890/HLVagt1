package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rankupcommand implements CommandExecutor {
    private Settings settings;
    private PlayerProfiles profiles;
    private VagtSpawnManager spawnManager;

    public Rankupcommand(Settings settings, PlayerProfiles profiles, VagtSpawnManager spawnManager) {
        this.settings = settings;
        this.profiles = profiles;
        this.spawnManager = spawnManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (VagtUtils.isSenderNotPlayer(sender)) return true;

        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p, "direktør")) return true;

        if (args.length <= 1) {
            p.sendMessage("/ansat <Player> <Rank>");
            p.sendMessage("Ranks: p-vagt, c-vagt, b-vagt, a-vagt, officer");
            return true;
        }
        Player ansat = Bukkit.getPlayer(args[0]);

        if (ansat == null) {
            p.sendMessage("er ikke en spiller");
            return true;
        }


        PlayerProfile profile = profiles.getPlayerProfile(ansat.getUniqueId());

        profiles.createVagt(ansat, profile);
        String rank = "bob";
        String perm = "1";
        String prePerm = "0";
        if (args[1].equalsIgnoreCase("p-vagt")) {
            ansat.setStatistic(Statistic.PLAY_ONE_TICK, 1);
            rank = "§cp-vagt";
            perm = "p-vagt";
            if (ansat.hasPermission("a-fange")) {
                prePerm = "a-fange";
            } else if (ansat.hasPermission("b-fange")) {
                prePerm = "b-fange";
            } else {
                prePerm = "c-fange";
            }
        } else if (args[1].equalsIgnoreCase("c-vagt")) {
            rank = "§cc-vagt";
            perm = "c-vagt";
            prePerm = "p-vagt";
        } else if (args[1].equalsIgnoreCase("b-vagt")) {
            rank = "§bb-vagt";
            perm = "b-vagt";
            prePerm = "c-vagt";
        } else if (args[1].equalsIgnoreCase("a-vagt")) {
            rank = "§aa-vagt";
            perm = "a-vagt";
            prePerm = "b-vagt";
        } else if (args[1].equalsIgnoreCase("officer")) {
            rank = "§6Officer";
            perm = "officer";
            prePerm = "a-vagt";
        }


        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ansat.getName() + " parent remove " + prePerm + " prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + ansat.getName() + " parent add " + perm + " prison");
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + ansat.getName());
        Bukkit.broadcastMessage("§7Har lige Ranket up til " + rank);
        Bukkit.broadcastMessage("§7Ved hjælp af §4" + p.getName());
        Bukkit.broadcastMessage("             §a§lTILLYKKE!!!");
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");


        return true;
    }
}
