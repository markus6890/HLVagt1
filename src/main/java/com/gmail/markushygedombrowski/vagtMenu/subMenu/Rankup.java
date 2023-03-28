package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Rankup implements Listener {
    private final int RANKUP_INDEX = 4;
    private Wool woolGreen = new Wool(DyeColor.GREEN);
    private ItemStack rankup = woolGreen.toItemStack(1);
    private HLvagt plugin;
    private PlayerProfiles profiles;
    private Settings settings;
    private VagtSpawnManager vagtSpawnM;

    public Rankup(HLvagt plugin, PlayerProfiles profiles, Settings settings, VagtSpawnManager vagtSpawnM) {
        this.plugin = plugin;
        this.profiles = profiles;
        this.settings = settings;
        this.vagtSpawnM = vagtSpawnM;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Rankup");
        ItemMeta metaRankup = rankup.getItemMeta();
         List<String> lore = meta(p,metaRankup);
         metaRankup.setLore(lore);
        rankup.setItemMeta(metaRankup);
        inventory.setItem(RANKUP_INDEX, rankup);


        p.openInventory(inventory);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();

        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("Rankup")) {
            if (clickedSlot == RANKUP_INDEX) {
                if (p.hasPermission("a-vagt")) {
                    p.sendMessage("§aDu kan ikke ranke up mere!");
                } else if (p.hasPermission("c-vagt")) {
                    rankupRequarid(50000, 75000, p, "§l§bB-Vagt", "b-vagt", 10);

                } else if (p.hasPermission("b-vagt")) {
                    rankupRequarid(1000000, 1500000, p, "§l§aA-Vagt", "a-vagt", 30);

                } else {
                    p.sendMessage("kontakt admin+");
                }
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }
    }

    public void rankupRequarid(int money, int moneyneed, Player p, String rank, String perm, int hours) {
        PlayerProfile profile = profiles.getPlayerProfile(p.getUniqueId());
        long timeplayed = (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
        if (isProfileNull(p, profile)) return;
        if (!hasMoney(moneyneed, p)) return;

        if (!(timeplayed >= hours)) {
            p.sendMessage("§aDu har ikke nok til rankup!!");
            return;
        }
        plugin.econ.withdrawPlayer(p, money);

        setLon(perm, profile);


        setVagtPerms(p, perm);
        profiles.save(profile);
        p.closeInventory();
        vagtRankupMessage(p, rank);


    }

    private void setLon(String perm, PlayerProfile profile) {
        int lon = settings.getLonb();
        if (perm.equalsIgnoreCase("a-vagt")) {
            lon = settings.getLona();
        }
        profile.setLon(lon);
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

    private boolean isProfileNull(Player p, PlayerProfile profile) {
        if (profile == null) {
            p.sendMessage("Du har ikke en loaded profile skriv til en admin+");
            return true;
        }
        return false;
    }

    private boolean hasMoney(int moneyneed, Player p) {
        if (!(plugin.econ.getBalance(p) >= moneyneed)) {
            p.sendMessage("§aDu har ikke nok penge!!");
            return false;
        }
        return true;
    }

    public List<String> meta(Player p,ItemMeta metaRankup) {
        String pattern = "###,###.##";
        DecimalFormat df = new DecimalFormat(pattern);
        long timeplayed = (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
        int bal = (int) plugin.econ.getBalance(p);
        List<String> rankuplore = new ArrayList<>();
        if (p.hasPermission("a-vagt")) {
            rankuplore.add("Du kan ikke ranke up mere!");
        } else if (p.hasPermission("c-vagt")) {
            metaRankup.setDisplayName("§cRank up til §b§lB-Vagt");
            rankuplore.add("§bB-vagt koster:");
            rankuplore.add("§7$50.000 §8[§f" + df.format(bal) + "/50.000§8]");
            rankuplore.add("§7Du skal have $75.000§8[§f" + df.format(bal) + "/75.000§8]");
            rankuplore.add("§7Du skal havet været på vagt i 10 timer §8[§f" + timeplayed + "/10§8]");
        } else if (p.hasPermission("b-vagt")) {
            rankuplore.add("§aA-vagt koster:");
            rankuplore.add("§71.000.000§8[§f" + df.format(bal) + "/1.000.000§8]");
            rankuplore.add("§7Du skal have $1.500.000§8[§f" + df.format(bal) + "/1.500.000§8]");
            rankuplore.add("§7Du skal havet været på vagt i 30 timer §8[§f" + timeplayed + "/30§8]");
        } else {
            rankuplore.add("Hmmm der er vist en fejl kontakt Staff");
        }
        return rankuplore;
    }


}
