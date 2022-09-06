package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.warp.WarpManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class DamageListener implements Listener {


    private final Settings settings;
    private final WarpManager warpManager;
    private final PlayerProfiles profiles;

    public DamageListener(Settings settings, WarpManager warpManager, PlayerProfiles profiles) {
        this.settings = settings;
        this.warpManager = warpManager;
        this.profiles = profiles;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacker;
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                attacker = (Player) projectile.getShooter();
            } else {
                attacker = (Player) event.getDamager();
            }
            if (!(event.getEntity() instanceof Player)) {
                return;
            }
            Player defender = (Player) event.getEntity();
            if (attacker.hasPermission("vagt.slag") && defender.hasPermission("vagt.slag")) {
                attacker.sendMessage("§aDu kan ikke slå andre vagter!");
                event.setCancelled(true);
                return;
            }
            if (attacker.hasPermission("vagt.slag")) {
                attacker.sendMessage("§2Du slog §4" + defender.getName());
            }
            if (defender.hasPermission("vagt.slag")) {
                defender.sendMessage("§a" + attacker.getName() + " §aSlog dig!");
            }
        }
    }


    @EventHandler
    public void vagtFangePvp(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player attacker;
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                attacker = (Player) projectile.getShooter();
            } else {
                attacker = (Player) event.getDamager();
            }
            Player defender = (Player) event.getEntity();
            if (defender.hasPermission("vagt.slag") || attacker.hasPermission("vagt.slag")) return;
            if (!Utils.isLocInRegion(attacker.getLocation(), "vagtfangepvp") && !Utils.isLocInRegion(attacker.getLocation(), "vagtfangepvp2"))
                return;

            attacker.sendMessage("§aDu kan ikke slå fanger her!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void deathMessage(PlayerDeathEvent event) {
        Player defender = event.getEntity();
        Player attacker = defender.getKiller();
        PlayerProfile profile = profiles.getPlayerProfile(defender.getUniqueId());
        if (attacker == null) {
            return;
        }
        if (attacker.hasPermission("vagt.slag")) {
            attacker.sendMessage("§2Du §4Dræbte §8" + defender.getName());
        }
        if (defender.hasPermission("vagt.slag")) {
            String rank;
            String block;
            if (defender.hasPermission("direktør")) {
                rank = "§4§lDirektøren ";
            } else if (defender.hasPermission("inspektør")) {
                rank = "§2§lInspektøren ";
            } else if (defender.hasPermission("viceinspektør")) {
                rank = "§2§lVice-Inspektøren ";
            } else if (defender.hasPermission("officer")) {
                rank = "§6§lOfficeren ";
            } else {
                rank = "§6§lVagten ";
            }
            if (Utils.isLocInRegion(defender.getLocation(), "c")) {
                block = "§cC";
            } else if (Utils.isLocInRegion(defender.getLocation(), "b")) {
                block = "§bB";
            } else if (Utils.isLocInRegion(defender.getLocation(), "a")) {
                block = "§aA";
            } else {
                block = "bobi";
            }
            Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
            Bukkit.broadcastMessage(rank + "§c" + defender.getName());
            Bukkit.broadcastMessage("§7blev dræbt af fangen §a§l" + attacker.getName());
            Bukkit.broadcastMessage("§e§lBlock§c§l: " + block);
            Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemStack item = api.getItemHead(settings.getVagthead());
            profile.setDeaths(profile.getDeaths() + 1);
            if (Utils.procent(0.5)) {
                attacker.getInventory().addItem(item);
                settings.setVagtheaddrop(settings.getVagtheaddrop() + 1);
                Bukkit.broadcastMessage("§7Et Vagt head er lige §5droppet");
            }

        }
    }

    @EventHandler
    public void onVagtRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (!p.hasPermission("vagt")) return;
        Location loc = warpManager.getWarpInfo("spassermine").getLocation();
        p.teleport(loc);

    }

    @EventHandler
    public void OnArmorDamage(PlayerItemDamageEvent event) {
        int dura = 1;
        event.setDamage(dura);
    }


}





