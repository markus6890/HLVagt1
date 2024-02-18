package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.PVGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Rankup;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainMenu implements Listener {
    private HLvagt plugin;
    private PVGUI pvgui;
    private Rankup rankupgui;
    private TopVagterGUI topVagterGUI;
    private final int SPILTID_INDEX = 1;
    private final int TOPVAGT_INDEX = 4;
    private final int STATS_INDEX = 7;
    private final int ACHIVEMENT_INDEX = 22;
    private final int PV_INDEX = 19;
    private final int RANKUP_INDEX = 25;
    private final int VAGTLVL_INDEX = 30;
    private final int L0N_INDEX = 32;
    private ItemStack stats = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack topvagt = new ItemStack(Material.BUCKET);
    private ItemStack spilletid = new ItemStack(Material.WATCH);
    private ItemStack pv = new ItemStack(Material.CHEST);
    private ItemStack achivement = new ItemStack(Material.EXP_BOTTLE);
    private ItemStack rankup = new ItemStack(Material.GOLD_SWORD);
    private ItemStack vagtlvl = new ItemStack(Material.BEACON);
    private ItemStack lon = new ItemStack(Material.GOLD_INGOT);
    private PlayerProfiles playerProfiles;


    public MainMenu(HLvagt plugin, PVGUI pvgui, TopVagterGUI topVagterGUI, PlayerProfiles playerProfiles, Rankup rankupgui) {
        this.plugin = plugin;
        this.pvgui = pvgui;
        this.topVagterGUI = topVagterGUI;
        this.playerProfiles = playerProfiles;
        this.rankupgui = rankupgui;

    }

    public void create(Player p, PlayerProfile profile) {
        Inventory inventory = Bukkit.createInventory(null, 36, "§cVagt Menu §8" + p.getName());



        meta(p, profile);

        inventory.setItem(STATS_INDEX, stats);
        inventory.setItem(TOPVAGT_INDEX, topvagt);
        inventory.setItem(SPILTID_INDEX, spilletid);
        inventory.setItem(PV_INDEX, pv);
        inventory.setItem(ACHIVEMENT_INDEX, achivement);
        inventory.setItem(RANKUP_INDEX, rankup);
        inventory.setItem(VAGTLVL_INDEX, vagtlvl);
        inventory.setItem(L0N_INDEX, lon);


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
        if (inventory.getTitle().equalsIgnoreCase("§cVagt Menu §8" + p.getName())) {

            switch (clickedSlot) {
                case PV_INDEX:
                    pvgui.create(p);
                    break;
                case RANKUP_INDEX:
                    if (p.hasPermission("a-vagt") || p.hasPermission("officer") || p.hasPermission("viceinspektør") || p.hasPermission("inspektør") || p.hasPermission("direktør")) {
                        p.sendMessage("§aDu kan ikke ranke up mere!");
                        break;
                    } else if (p.hasPermission("p-vagt")) {
                        p.sendMessage("§cDu kan ikke rankup som P-vagt!!");
                        break;
                    }
                    rankupgui.create(p);
                    break;
                case L0N_INDEX:
                    if (VagtCooldown.isCooling(p.getName(), "lon")) {
                        VagtCooldown.coolDurMessage(p, "lon");
                    }
                    break;
                case TOPVAGT_INDEX:
                    topVagterGUI.create(p);
                    break;
            }

            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }


    }

    public void meta(Player p, PlayerProfile profile) {
        String pattern = "###,###.##";
        DecimalFormat df = new DecimalFormat(pattern);
        int seconds = p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
        int minutes = (seconds / 60);
        int hours = (minutes / 60);
        int actualMinutes = minutes - (hours * 60);


        // Getting Meta
        ItemMeta metastats = stats.getItemMeta();
        ItemMeta metatopv = topvagt.getItemMeta();
        ItemMeta metatid = spilletid.getItemMeta();
        ItemMeta metapv = pv.getItemMeta();
        ItemMeta metaachive = achivement.getItemMeta();
        ItemMeta metarankup = rankup.getItemMeta();
        ItemMeta metavagtlvl = vagtlvl.getItemMeta();
        ItemMeta metalon = lon.getItemMeta();

        // DisplayName
        metastats.setDisplayName("§7[§a§lStats§7]");
        metatopv.setDisplayName("§7[§cTopVagter§7]");
        metatid.setDisplayName("§7[§aSpilleTid§7]");
        metapv.setDisplayName("§7[§cPV§7]");
        metaachive.setDisplayName("§7[§aAchivements§7]");
        metarankup.setDisplayName("§7[§2Rank up§7]");
        metavagtlvl.setDisplayName("§7[§cVagt-Levels§7]");
        metalon.setDisplayName("§7[§aLøn§7]");

        // Lores

        List<String> topVagterLore = new ArrayList<>();
        topVagterLore.add("§7Se §aTopVagterne");

        List<String> statsLore = new ArrayList<>();
        statsLore.add("§6§lSe dine Stats§7");
        statsLore.add("§a§lLevel: §f" + profile.getLvl());
        statsLore.add("§b§lXP: §f" + profile.getXp() + "/" + profile.getXpToNextLvl());
        statsLore.add("§c§lDød: §f" + profile.getDeaths());
        statsLore.add("§a§lDræbt: §f" + profile.getKills());
        statsLore.add("§a§lPenge: §f$" + df.format(plugin.econ.getBalance(p)));


        List<String> spilletidlore = new ArrayList<>();
        spilletidlore.add("§9" + hours + " §6Hours");
        spilletidlore.add("§9"+actualMinutes + " §6Minutes");



        List<String> lonLore = new ArrayList<>();
        lonLore.add("§7din §2løn: §a" + profile.getLon());


        //Setting Lore on Item
        metastats.setLore(statsLore);
        metatid.setLore(spilletidlore);
        metarankup.setLore(rankupgui.meta(p,metarankup));
        metatopv.setLore(topVagterLore);
        metalon.setLore(lonLore);

        // Setting Meta on Item
        stats.setItemMeta(metastats);
        topvagt.setItemMeta(metatopv);
        spilletid.setItemMeta(metatid);
        pv.setItemMeta(metapv);
        achivement.setItemMeta(metaachive);
        rankup.setItemMeta(metarankup);
        vagtlvl.setItemMeta(metavagtlvl);
        lon.setItemMeta(metalon);

    }


}
