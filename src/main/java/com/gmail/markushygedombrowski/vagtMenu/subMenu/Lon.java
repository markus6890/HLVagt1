package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.cooldown.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Lon {
    private HLvagt plugin;
    private PlayerProfiles playerProfiles;
    private Settings settings;

    public Lon(HLvagt plugin, PlayerProfiles playerProfiles, Settings settings) {
        this.plugin = plugin;
        this.playerProfiles = playerProfiles;
        this.settings = settings;

    }


    public void giveLon(Player p) {
        if (p.hasPermission("vagt")) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());

                plugin.econ.depositPlayer(p, profile.getLon());
                p.sendMessage(ChatColor.GRAY + "Du har fået" + ChatColor.AQUA + " Løn!");

            Cooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());


        }
    }
}



