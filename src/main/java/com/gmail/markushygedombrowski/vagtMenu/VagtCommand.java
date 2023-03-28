package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtCommand implements CommandExecutor {
    private final MainMenu mainMenu;
    public VagtCommand(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] strings) {

        if(VagtUtils.isSenderNotPlayer(sender)) return true;
        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p,"vagt")) return true;

        mainMenu.create(p);

        return true;
    }
}
