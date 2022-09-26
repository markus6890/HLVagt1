package com.gmail.markushygedombrowski.listners;
import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.cooldown.VagtCooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnJoin implements Listener {
    private Map<UUID,Integer> playerMap = new HashMap<>();
    private PlayerProfiles playerProfiles;
    private Settings settings;
    private Integer time = 0;

    public OnJoin(PlayerProfiles playerProfiles, Settings settings) {
        this.playerProfiles = playerProfiles;
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


        if (!VagtCooldown.isCooling(p.getName(), "lon")) {
            if(time == 0) {
                time = settings.getLonTime();
            }
            if(playerMap.containsKey(p.getUniqueId())) {
                playerMap.replace(p.getUniqueId(),time);
            } else {
                playerMap.put(p.getUniqueId(),time);
            }

            VagtCooldown.add(p.getName(), "lon",playerMap.get(p.getUniqueId()), System.currentTimeMillis());
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
        profile = new PlayerProfile(p.getUniqueId(), p.getName(), 1, 1, lon, 0,0);
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

        if (VagtCooldown.isCooling(p.getName(), "lon")) {
            time = (int) (VagtCooldown.getRemaining(p.getName(),"lon") * 60);
            playerMap.replace(p.getUniqueId(),time);
            VagtCooldown.removeCooldown(p.getName(), "lon");
        }


    }


}
