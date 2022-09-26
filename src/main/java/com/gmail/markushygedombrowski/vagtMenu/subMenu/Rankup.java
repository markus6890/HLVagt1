package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.cooldown.TimeUnit;
import com.gmail.markushygedombrowski.utils.cooldown.UtilTime;
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

    public Rankup(HLvagt plugin, PlayerProfiles profiles,Settings settings) {
        this.plugin = plugin;
        this.profiles = profiles;
        this.settings = settings;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Rankup");
        meta(p);

        inventory.setItem(RANKUP_INDEX, rankup);


        p.openInventory(inventory);
    }

    @EventHandler
    public void onClickEvent (InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        PlayerProfile profile = profiles.getPlayerProfile(p.getUniqueId());

        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("Rankup")) {
            int pay;
            int moneyneeded;
            if(clickedSlot == RANKUP_INDEX) {
                if (p.hasPermission("a-vagt")) {
                    p.sendMessage("§aDu kan ikke ranke up mere!");
                }else if (p.hasPermission("c-vagt")) {
                    moneyneeded = 70000;
                    pay = 50000;
                    boolean kills = p.getStatistic(Statistic.PLAYER_KILLS) >= 10;
                    if (plugin.econ.getBalance(p) >= moneyneeded && kills) {
                        plugin.econ.withdrawPlayer(p, pay);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove c-vagt prison");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add b-vagt prison");
                        profile.setLon(settings.getLonb());
                        p.closeInventory();
                        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
                        Bukkit.broadcastMessage("§c§lVagten §6" + p.getName());
                        Bukkit.broadcastMessage("§7Har lige Ranket up til §l§9B-Vagt");
                        Bukkit.broadcastMessage("             §a§lTILLYKKE!!!");
                        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");

                    } else {
                        p.sendMessage("§aDu har ikke nok til rankup!!");

                    }



                } else if (p.hasPermission("b-vagt")) {
                    p.sendMessage("kommer snart");
                } else {
                    p.sendMessage("hmmmmmmmmmmmmmmmmmmmmm");
                }
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }


        }

    public void meta(Player p) {
        String pattern = "###,###.##";
        DecimalFormat df = new DecimalFormat(pattern);
        ItemMeta metaRankup = rankup.getItemMeta();
        int bal = (int)plugin.econ.getBalance(p);
        List<String> rankuplore = new ArrayList<>();
        if (p.hasPermission("a-vagt")) {
            rankuplore.add("Du kan ikke ranke up mere!");
        }else if (p.hasPermission("c-vagt")) {
            metaRankup.setDisplayName("§cRank up til §b§lB-Vagt");
            rankuplore.add("§bB-vagt koster:");
            rankuplore.add("§7$50.000 §8[§f" + df.format(bal) + "/50.000§8]" );
            rankuplore.add("§7Du skal have $70.000§8[§f" + df.format(bal) + "/70.000§8]");
            rankuplore.add("§7Dræb 10 fanger §8[§f" + p.getStatistic(Statistic.PLAYER_KILLS) + "/10§8]");
        } else if (p.hasPermission("b-vagt")) {
            rankuplore.add("§aA-vagt koster:");
            rankuplore.add("§71.000.000");
            rankuplore.add("§730 timer spillet på serveren");
        } else {
            rankuplore.add("Hmmm der er vist en fejl kontakt Staff");
        }
        metaRankup.setLore(rankuplore);
        rankup.setItemMeta(metaRankup);

    }




}
