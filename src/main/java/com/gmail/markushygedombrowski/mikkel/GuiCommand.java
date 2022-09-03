package com.gmail.markushygedombrowski.mikkel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiCommand implements CommandExecutor {

    private Gui gui;

    public GuiCommand(Gui gui) {
        this.gui = gui;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {

        if(!(sender instanceof Player)) {
            System.out.println("Kun players kan bruge den her command");
            return true;
        }
        Player p = (Player) sender;
        gui.create(p);
        return true;
    }
}
