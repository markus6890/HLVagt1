package com.gmail.markushygedombrowski.vagtMenu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class VagtCommand implements CommandExecutor {
    private final MainMenu mainMenu;
    public VagtCommand(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] strings) {
        if(!sender.hasPermission("Vagt")) {
            sender.sendMessage("Det har du ikke permission til!");
            return true;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            mainMenu.create(p);
        }


        return true;
    }
}
