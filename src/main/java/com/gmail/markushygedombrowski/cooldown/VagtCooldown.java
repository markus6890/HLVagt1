package com.gmail.markushygedombrowski.cooldown;

import com.gmail.markushygedombrowski.vagtMenu.subMenu.Lon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VagtCooldown {
    public VagtCooldown(Lon lon) {
        this.lon = lon;
    }

    private Lon lon;

    public static HashMap<String, VagtAbilityCooldown> cooldownPlayers = new HashMap<String, VagtAbilityCooldown>();


    public static void add(String player, String ability, long seconds, long systime) {
        if (!cooldownPlayers.containsKey(player)) cooldownPlayers.put(player, new VagtAbilityCooldown(player));
        if (isCooling(player, ability)) return;
        cooldownPlayers.get(player).cooldownMap.put(ability, new VagtAbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }

    public static boolean isCooling(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return false;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return false;
        return true;
    }

    public static double getRemaining(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) return 0.0;
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) return 0.0;
        return UtilTime.convert((cooldownPlayers.get(player).cooldownMap.get(ability).seconds + cooldownPlayers.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.MINUTES, 1);
    }

    public static void coolDurMessage(Player player, String ability) {
        if (player == null) {
            return;
        }
        if (!isCooling(player.getName(), ability)) {
            return;
        }
        if(ability.equalsIgnoreCase("lon")) {
            player.sendMessage(ChatColor.GRAY + " du får løn om " + ChatColor.AQUA + getRemaining(player.getName(), ability) + " Minuter");
        }

    }

    public static void removeCooldown(String player, String ability) {
        if (!cooldownPlayers.containsKey(player)) {
            return;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);
        Player cPlayer = Bukkit.getPlayer(player);

    }


    public void removeCooldownLon(String player, String ability) {

        if (!cooldownPlayers.containsKey(player)) {
            return;
        }
        if (!cooldownPlayers.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownPlayers.get(player).cooldownMap.remove(ability);
        Player cPlayer = Bukkit.getPlayer(player);
        if (cPlayer != null) {
            if(ability.equalsIgnoreCase("lon")) {
                lon.giveLon(cPlayer);
            }


        }


    }


    public void handleCooldowns() {
        if (cooldownPlayers.isEmpty()) {
            return;
        }
        cooldownPlayers.forEach((player, abilityCooldown) -> {
            if (abilityCooldown.cooldownMap.isEmpty()) {
                return;
            }
            abilityCooldown.cooldownMap.forEach((ability, cooldown) -> {
                if (getRemaining(player, ability) <= 0.0) {
                    removeCooldownLon(player, ability);
                }

            });
        });

    }
}
