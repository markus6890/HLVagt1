package com.gmail.markushygedombrowski.jail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println("Kun players kan bruge den command");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("vagt")){
            p.sendMessage("§");
        }
        return true;
    }
}
