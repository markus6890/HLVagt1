package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.config.VagtFangePvpConfigManager;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.sql.SQLException;
import java.util.List;


public class DamageListener implements Listener {


    private final Settings settings;
    private final VagtSpawnManager vagtSpawnManager;
    private final PlayerProfiles profiles;
    private final HLvagt plugin;
    private final CombatList combatList;
    private final VagtFangePvpConfigManager vFPvpConfig;

    public DamageListener(Settings settings, VagtSpawnManager vagtSpawnManager, PlayerProfiles profiles, HLvagt plugin, CombatList combatList, VagtFangePvpConfigManager vFPvpConfig) {
        this.settings = settings;
        this.vagtSpawnManager = vagtSpawnManager;
        this.profiles = profiles;
        this.plugin = plugin;
        this.combatList = combatList;
        this.vFPvpConfig = vFPvpConfig;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if(entity.hasMetadata("NPC")) {
            event.setCancelled(true);
            event.isCancelled();
            return;
        }
        if (!(entity instanceof Player)) return;

        Player defender = (Player) entity;

        Player attacker = VagtUtils.getAttacker(event);

        if (isAttackerAndDefenderVagt(attacker, defender)) {
            event.setCancelled(true);
            return;
        }
        sendMessageToVagt(defender, attacker);
    }




    private void sendMessageToVagt(Player defender, Player attacker) {
        if (attacker.hasPermission("vagt.slag")) {
            attacker.sendMessage("§2Du slog §4" + defender.getName());
        } else if (defender.hasPermission("vagt.slag")) {
            defender.sendMessage("§a" + attacker.getName() + " §aSlog dig!");
        }
    }

    private boolean isAttackerAndDefenderVagt(Player attacker, Player defender) {
        if (attacker.hasPermission("vagt.slag") && defender.hasPermission("vagt.slag")) {
            attacker.sendMessage("§aDu kan ikke slå andre vagter!");
            return true;
        }
        return false;
    }


    @EventHandler
    public void vagtFangePvp(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player defender = (Player) event.getEntity();
        Player attacker = VagtUtils.getAttacker(event);
        if (defender.hasPermission("vagt.slag") || attacker.hasPermission("vagt.slag")) return;
        List<String> vFpvp = vFPvpConfig.getvFpvp();
        Location attackerLoc = attacker.getLocation();
        Location defenderLoc = defender.getLocation();
        vFpvp.forEach(s -> {
            if (Utils.isLocInRegion(attackerLoc, s) || Utils.isLocInRegion(defenderLoc, s)) {
                event.setCancelled(true);
                attacker.sendMessage("§aDu kan ikke slå fanger her!");

            }
        });
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player defender = event.getEntity();
        Player attacker = defender.getKiller();
        PlayerProfile profile;
        if (attacker == null) {
            if(combatList.getLastHit(defender) == null) return;
            attacker = combatList.getLastHit(defender);
        }
        if (attackerIsVagt(defender, attacker)) return;
        if (!defender.hasPermission("vagt.slag")) return;

        vagtDeath(defender, attacker);
    }

    private void vagtDeath(Player defender, Player attacker) {
        PlayerProfile profile;
        profile = profiles.getPlayerProfile(defender.getUniqueId());

        String rank = getRankName(defender);
        String block = getBlockDisplayName(defender);

        sendVagtDeathMessage(defender, attacker, rank, block);

        if(Utils.isLocInRegion(defender.getLocation(),"bandekrig")) {
            defender.sendMessage("§7[§cBandeKrig§7] §cDu døde til bandekrig event");
            defender.setMetadata("bandekrigDeathvagt",new FixedMetadataValue(plugin,true));
        } else {
            profile.setDeaths(profile.getDeaths() + 1);
        }

        dropVagtHeadChance(attacker);
        try {
            profiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean attackerIsVagt(Player defender, Player attacker) {
        PlayerProfile profile;
        if (attacker.hasPermission("vagt.slag")) {
            attacker.sendMessage("§2Du §4Dræbte §8" + defender.getName());
            profile = profiles.getPlayerProfile(attacker.getUniqueId());
            profile.setKills(profile.getKills() + 1);
            return true;
        }
        return false;
    }

    private void dropVagtHeadChance(Player p) {
        if (VagtUtils.procent(1)) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemStack item = api.getItemHead(settings.getVagthead());
            p.getInventory().addItem(item);
            settings.setVagtheaddrop(settings.getVagtheaddrop() + 1);
            Bukkit.broadcastMessage("§7Et Vagt head er lige §5droppet");
        }
    }

    private void sendVagtDeathMessage(Player defender, Player attacker, String rank, String block) {
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage(rank + "§c" + defender.getName());
        Bukkit.broadcastMessage("§7blev dræbt af fangen §a§l" + attacker.getName());
        Bukkit.broadcastMessage("§e§lBlock§c§l: " + block);
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
    }

    private String getBlockDisplayName(Player p) {
        String block = "bobi";
        if (Utils.isLocInRegion(p.getLocation(), "c")) {
            block = "§cC";
        } else if (Utils.isLocInRegion(p.getLocation(), "b")) {
            block = "§bB";
        } else if (Utils.isLocInRegion(p.getLocation(), "a")) {
            block = "§aA";
        } else if(Utils.isLocInRegion(p.getLocation(), "BandeKrig")) {
            block = "§c§lBandeKrig";
        }
        return block;
    }

    private String getRankName(Player p) {
        String rank = "§6§lVagten ";
        if (p.hasPermission("direktør")) {
            rank = "§4§lDirektøren ";
        } else if (p.hasPermission("inspektør")) {
            rank = "§2§lInspektøren ";
        } else if (p.hasPermission("viceinspektør")) {
            rank = "§2§lVice-Inspektøren ";
        } else if (p.hasPermission("officer")) {
            rank = "§6§lOfficeren ";
        }
        return rank;
    }


    @EventHandler
    public void onVagtRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (p.hasPermission("vagt")) {
            Location loc;

                loc = vagtSpawnManager.getWarpInfo("spassermine").getLocation();

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    p.teleport(loc);
                }
            }, 2);

        }

    }

    @EventHandler
    public void onArmorDamage(PlayerItemDamageEvent event) {
        if(event.getItem() == null) return;
        ItemStack item = event.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.getDisplayName() == null) {
            return;
        }
        if(itemMeta.getDisplayName().contains("§aA") || itemMeta.getDisplayName().contains("§bB") || itemMeta.getDisplayName().contains("§cC")) {
            int dura = 1;
            event.setDamage(dura);
        }

    }


}





