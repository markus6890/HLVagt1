package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VagtCommand implements CommandExecutor {
    private final MainMenu mainMenu;
    private final PlayerProfiles playerProfiles;
    public VagtCommand(MainMenu mainMenu, PlayerProfiles playerProfiles) {
        this.mainMenu = mainMenu;
        this.playerProfiles = playerProfiles;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if(VagtUtils.isSenderNotPlayer(sender)) return true;
        Player p = (Player) sender;
        if (VagtUtils.notHasPermission(p,"vagt")) return true;
        PlayerProfile profile;
        if(args.length == 0){
            profile = playerProfiles.getPlayerProfile(p.getUniqueId());

        } else {
            Player player = Bukkit.getPlayer(args[0]);
            profile = playerProfiles.getPlayerProfile(player.getUniqueId());
        }

        mainMenu.create(p, profile);

        return true;
    }
}
