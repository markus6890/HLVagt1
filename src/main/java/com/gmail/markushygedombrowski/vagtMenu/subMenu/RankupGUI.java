package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.rankup.RankupKrav;
import com.gmail.markushygedombrowski.rankup.RankupLoader;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RankupGUI implements Listener {
    private PlayerProfiles playerProfiles;
    private Economy econ;
    private final int RANKUP_INDEX = 13;
    private RankupLoader rankupLoader;

    public RankupGUI(PlayerProfiles playerProfiles, Economy econ, RankupLoader rankupLoader) {
        this.playerProfiles = playerProfiles;
        this.econ = econ;
        this.rankupLoader = rankupLoader;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§a§lRankup");
        long timeplayed = (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
        ItemStack rankup = new ItemStack(Material.DIAMOND, 1);
        ItemMeta rankupMeta = rankup.getItemMeta();
        List<String> lore = new ArrayList<>();
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        RankupKrav rankupKrav;
        rankupMeta.setDisplayName("§a§lRankup");
        if (p.hasPermission("a-vagt")) {
            lore.add(0, "§c§lDu kan §4§lIkke §c§lRankup");
        } else if (p.hasPermission("b-vagt")) {
            rankupKrav = rankupLoader.getRankupKrav("a-vagt");
            lore.add("§6§lKrav:");
            lore.add("§a§lSpilletid: §b" + timeplayed + "§7/§b" + rankupKrav.getOnTime() + "§7§lTimer");
            lore.add("§bVagt Level: §b" + profile.getLvl() + "§7/§b" + rankupKrav.getVagtlevel());
            lore.add("§a§lPenge: §b" + econ.getBalance(p) + "§7/§b" + rankupKrav.getMoney());
            lore.add("§2VagtPoster: §b" + profile.getVagtposter() + "§7/§b" + rankupKrav.getVagtposter());
            lore.add("§9§lSeeds: §b" + profile.getDeliveredItems().getSeed() + "§7/§b" + rankupKrav.getSeeds());
            lore.add("§e§lBread: §b" + profile.getDeliveredItems().getBread() + "§7/§b" + rankupKrav.getBread());
            lore.add("§f§lIron Helmets: §b" + profile.getDeliveredItems().getIronHelmet() + "§7/§b3");
            lore.add("§f§lIron Chestplates: §b" + profile.getDeliveredItems().getIronChestplate() + "§7/§b3");
            lore.add("§f§lIron Leggings: §b" + profile.getDeliveredItems().getIronLeggings() + "§7/§b3");
            lore.add("§f§lIron Boots: §b" + profile.getDeliveredItems().getIronBoots() + "§7/§b3");
            lore.add("§f§lIron Swords: §b" + profile.getDeliveredItems().getIronSword() + "§7/§b3");

        } else if (p.hasPermission("c-vagt")) {
            rankupKrav = rankupLoader.getRankupKrav("b-vagt");
            lore.add(0, "§6§lKrav:");
            lore.add("§a§lSpilletid: §b" + timeplayed + "§7/§b" + rankupKrav.getOnTime() + "§7§lTimer");
            lore.add("§bVagt Level: §b" + profile.getLvl() + "§7/§b" + rankupKrav.getVagtlevel());
            lore.add("§a§lPenge: §b" + econ.getBalance(p) + "§7/§b" + rankupKrav.getMoney());
            lore.add("§2VagtPoster: §b" + profile.getVagtposter() + "§7/§b" + rankupKrav.getVagtposter());
            lore.add("§9§lSeeds: §b" + profile.getDeliveredItems().getSeed() + "§7/§b" + rankupKrav.getSeeds());
            lore.add("§e§lBread: §b" + profile.getDeliveredItems().getBread() + "§7/§b" + rankupKrav.getBread());
            lore.add("§f§lIron Helmets: §b" + profile.getDeliveredItems().getIronHelmet() + "§7/§b1");
            lore.add("§f§lIron Chestplates: §b" + profile.getDeliveredItems().getIronChestplate() + "§7/§b1");
            lore.add("§f§lIron Leggings: §b" + profile.getDeliveredItems().getIronLeggings() + "§7/§b1");
            lore.add("§f§lIron Boots: §b" + profile.getDeliveredItems().getIronBoots() + "§7/§b1");
            lore.add("§f§lIron Swords: §b" + profile.getDeliveredItems().getIronSword() + "§7/§b1");
        }


        rankupMeta.setLore(lore);
        rankup.setItemMeta(rankupMeta);
        inv.setItem(RANKUP_INDEX, rankup);

        p.openInventory(inv);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("§a§lRankup")) {
            if (clickedSlot == RANKUP_INDEX) {
                RankupKrav rankupKrav;
                if (p.hasPermission("a-vagt")) {
                    p.sendMessage("§aDu kan ikke ranke up mere!");
                } else if (p.hasPermission("c-vagt")) {
                    rankupKrav = rankupLoader.getRankupKrav("b-vagt");
                    rankup(p, rankupKrav, "§b§lb-vagt");

                } else if (p.hasPermission("b-vagt")) {
                    rankupKrav = rankupLoader.getRankupKrav("a-vagt");
                    rankup(p, rankupKrav, "§a§la-vagt");
                }
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

    }

    private void rankup(Player p, RankupKrav rankupKrav,String rank) {
        if (rankupKrav.canRankup(playerProfiles.getPlayerProfile(p.getUniqueId()))) {
            if (econ.has(p, rankupKrav.getMoney()) && p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60 >= rankupKrav.getOnTime()) {
                econ.withdrawPlayer(p, rankupKrav.getMoney());
                vagtRankupMessage(p, rank);
                setVagtPerms(p, rankupKrav.getRankup());
            }
        } else {
            p.sendMessage("§cDu opfylder ikke kravene til at rankup!");
        }
    }
    private void setVagtPerms(Player p, String perm) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove c-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove b-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add " + perm + " prison");
    }

    private void vagtRankupMessage(Player p, String rank) {
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + p.getName());
        Bukkit.broadcastMessage("§7Har lige Ranket up til " + rank);
        Bukkit.broadcastMessage("             §a§lTILLYKKE!!!");
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
    }
}
