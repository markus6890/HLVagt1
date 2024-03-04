package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class VagtLevelAdminCommands implements CommandExecutor {
    private PlayerProfiles playerProfiles;

    public VagtLevelAdminCommands(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("Du skal være en spiller for at bruge denne kommando");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("Vagt.Admin")) {
            p.sendMessage("§4Du har ikke tilladelse til at bruge denne kommando");
            return true;
        }
        boolean reseta = false;
        if(alias.equalsIgnoreCase("vlevel")) {
            setLevel(args, p);
            return true;
        } else if (alias.equalsIgnoreCase("vexp")) {
            setExp(args, p);
            return true;
        } else if (alias.equalsIgnoreCase("vreset")) {
            reset(args, p);
            return true;
        }
        if (alias.equalsIgnoreCase("vconfirmresetall")) {
            reseta = true;

        }
        if(!reseta) {
            p.sendMessage("§4-------RESET ALL-------");
            p.sendMessage("§4§lER DU SIKKER PÅ AT DU VIL NULSTILLE ALLE SPILLERES LEVEL OG EXP?");
            p.sendMessage("§4§lSKRIV /vconfirmresetall FOR AT BEKRÆFTE");
            p.sendMessage("§4-----------------------");
        }
        if(reseta) {
            confirmResetAll(p);
        }



        return true;
    }


    private void confirmResetAll(Player p) {
        playerProfiles.getProfileMap().values().forEach(profile -> {
            profile.setLvl(0);
            profile.setXp(0);

        });
        playerProfiles.saveAll();
        p.sendMessage("§aYou have reset all players' level and exp.");
    }
    private void reset(String[] args, Player p) {
        if(args.length == 0) {
            p.sendMessage("§4Brug /vreset <spiller>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setLvl(0);
        profile.setXp(0);
        p.sendMessage("§aDu har nulstillet " + target.getName() + "'s level og exp");
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }
    private void setExp(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vexp <spiller> <exp>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        int exp = Integer.parseInt(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setXp(exp);
        p.sendMessage("§aDu har sat " + target.getName() + " til " + exp + " exp");
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }

    private void setLevel(String[] args, Player p) {
        if(args.length == 0 || args.length == 1) {
            p.sendMessage("§4Brug /vlevel <spiller> <level>");
            return;
        }
        Player target = getPlayer(args, p);
        if (target == null) return;
        int level = Integer.parseInt(args[1]);
        PlayerProfile profile = playerProfiles.getPlayerProfile(target.getUniqueId());
        profile.setLvl(level);
        p.sendMessage("§aDu har sat " + target.getName() + " til level " + level);
        try {
            playerProfiles.save(profile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }

    @Nullable
    private Player getPlayer(String[] args, Player p) {
        Player target = p.getServer().getPlayer(args[0]);
        if(target == null) {
            target = p.getServer().getOfflinePlayer(args[0]).getPlayer();
            if (target == null) {
                p.sendMessage("§4Spilleren findes ikke");
                return null;
            }
        }
        return target;
    }
}
