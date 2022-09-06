package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnJoin implements Listener {

    private PlayerProfiles playerProfiles;
    private HLvagt plugin;
    private Settings settings;

    public OnJoin(PlayerProfiles playerProfiles, HLvagt plugin, Settings settings) {
        this.playerProfiles = playerProfiles;
        this.plugin = plugin;
        this.settings = settings;

    }

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        Player p = event.getPlayer();

        if (!p.hasPermission("Vagt")) {
            return;
        }

        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        createVagt(p, profile);


        if (!Cooldown.isCooling(p.getName(), "lon")) {
            Cooldown.add(p.getName(), "lon", settings.getLonTime(), System.currentTimeMillis());
        }


    }


    public void createVagt(Player p, PlayerProfile profile) {

        if (profile != null) {
            return;
        }
        int lon;

        if (p.hasPermission("direktør")) {
            lon = settings.getLondire();
        } else if (p.hasPermission("inspektør")) {
            lon = settings.getLonins();
        } else if (p.hasPermission("viceinspektør")) {
            lon = settings.getLonviceins();
        } else if (p.hasPermission("officer")) {
            lon = settings.getLonoffi();
        } else if (p.hasPermission("a-vagt")) {
            lon = settings.getLona();
        } else if (p.hasPermission("b-vagt")) {
            lon = settings.getLonb();
        } else if (p.hasPermission("c-vagt")) {
            lon = settings.getLonc();
        } else {
            lon = 1000;
        }
        profile = new PlayerProfile(p.getUniqueId(), p.getName(), 1, 1, lon, 0);
        playerProfiles.save(profile);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        if (!p.hasPermission("vagt")) {
            return;
        }
        if (profile == null) {
            return;
        }

        if (Cooldown.isCooling(p.getName(), "lon")) {
            Cooldown.removeCooldown(p.getName(), "lon");
        }


    }


}
