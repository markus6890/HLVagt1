package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnJoin implements Listener {
    private Map<UUID, Integer> playerCooldownTime = new HashMap<>();
    private PlayerProfiles playerProfiles;
    private Settings settings;
    private Integer time = 0;

    public OnJoin(PlayerProfiles playerProfiles, Settings settings) {
        this.playerProfiles = playerProfiles;
        this.settings = settings;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (!p.hasPermission("Vagt")) {
            return;
        }
        p.sendTitle("§4Husk og sid i Vagt Call!",null);

        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        try {
            playerProfiles.createVagt(p, profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addPlayerToCooldown(p);
    }

    private void addPlayerToCooldown(Player p) {
        if (VagtCooldown.isCooling(p.getName(), "lon")) {
            return;
        }
        setCooldownTime(p);

        VagtCooldown.add(p.getName(), "lon", playerCooldownTime.get(p.getUniqueId()), System.currentTimeMillis());
    }

    private void setCooldownTime(Player p) {
        if (time == 0) {
            time = settings.getLonTime();
        }
        putPlayerInPlayerMap(p);
    }

    private void putPlayerInPlayerMap(Player p) {
        if (!playerCooldownTime.containsKey(p.getUniqueId())) {
            playerCooldownTime.put(p.getUniqueId(), time);
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPermission("vagt")) return;
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());

        if (profile == null) return;

        if (!VagtCooldown.isCooling(p.getName(), "lon")) {
            return;
        }

        time = (int) (VagtCooldown.getRemaining(p.getName(), "lon") * 60);
        VagtCooldown.removeCooldown(p.getName(), "lon");
        playerCooldownTime.replace(p.getUniqueId(), time);
        try {
            playerProfiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
