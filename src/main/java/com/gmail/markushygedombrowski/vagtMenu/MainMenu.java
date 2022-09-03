package com.gmail.markushygedombrowski.vagtMenu;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.cooldown.Cooldown;
import com.gmail.markushygedombrowski.utils.cooldown.TimeUnit;
import com.gmail.markushygedombrowski.utils.cooldown.UtilTime;
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

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 36, "§cVagt Menu §8" + p.getName());

        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());

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
                    } else {
                        rankupgui.create(p);
                    }
                    break;
                case L0N_INDEX:
                    if (Cooldown.isCooling(p.getName(), "lon")) {
                        Cooldown.coolDurMessage(p, "lon");
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
        long timeplayed = (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
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
        metastats.setDisplayName("§f> §aStats§f <");
        metatopv.setDisplayName("§f> §cTopVagter§f <");
        metatid.setDisplayName("§f> §aSpilleTid§f <");
        metapv.setDisplayName("§f> §cPV§f <");
        metaachive.setDisplayName("§f> §aAchivements§f <");
        metarankup.setDisplayName("§f> §2Rank up§f <");
        metavagtlvl.setDisplayName("§f> §cVagt-Levels§f <");
        metalon.setDisplayName("§f> §aLøn§f <");

        // Lores

        List<String> topVagterLore = new ArrayList<>();
        topVagterLore.add("§cKommer Snart");

        List<String> statsLore = new ArrayList<>();
        statsLore.add("§4Død: §f" + p.getStatistic(Statistic.DEATHS));
        statsLore.add("§aDræbt: §f" + p.getStatistic(Statistic.PLAYER_KILLS));
        statsLore.add("§aPenge: §f$" + df.format(plugin.econ.getBalance(p)));

        List<String> spilletidlore = new ArrayList<>();
        spilletidlore.add("§6Spillet §7Tid: " + timeplayed + " Hours");

        List<String> rankuplore = new ArrayList<>();
        if (p.hasPermission("a-vagt")) {
            rankuplore.add("§7Du kan ikke ranke up mere!");
        } else if (p.hasPermission("c-vagt")) {
            rankuplore.add("§7Til §bB-vagt §7koster:");
            rankuplore.add("§7$50.000");
            rankuplore.add("§7Du skal have $70.000");
        } else if (p.hasPermission("b-vagt")) {
            rankuplore.add("§7Til §aA-vagt §7koster:");
            rankuplore.add("§7$1.000.000");
            rankuplore.add("§730 timer spillet på serveren");
        } else {
            rankuplore.add("Hmmm der er vist en fejl kontakt Staff");
        }

        List<String> lonLore = new ArrayList<>();
        lonLore.add("§7din §2løn: §a" + profile.getLon());


        //Setting Lore on Item
        metastats.setLore(statsLore);
        metatid.setLore(spilletidlore);
        metarankup.setLore(rankuplore);
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
