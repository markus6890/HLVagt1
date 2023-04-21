package com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter;

import com.gmail.markushygedombrowski.model.PlayerProfile;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.VagtUtils;
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


public class TopVagterGUI implements Listener {
    private final int TOP10KILLS_index = 1;
    private final int TOP10DEATH_INDEX = 3;
    private final int TOP10WALKED_INDEX = 5;
    private final int TOP10MONEY_INDEX = 7;
    private ItemStack kills = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack deaths = new ItemStack(Material.SKULL_ITEM);
    private ItemStack walked = new ItemStack(Material.DIAMOND_BOOTS);
    private ItemStack money = new ItemStack(Material.GOLD_INGOT);
    private PlayerProfiles playerProfiles;

    public TopVagterGUI(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 9, "top10");
        meta(p);
        inventory.setItem(TOP10KILLS_index, kills);
        inventory.setItem(TOP10DEATH_INDEX, deaths);
        inventory.setItem(TOP10WALKED_INDEX, walked);
        inventory.setItem(TOP10MONEY_INDEX, money);
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
        if (inventory.getTitle().equalsIgnoreCase("top10")) {


            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }


    public void meta(Player p) {
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        ItemMeta metakills = kills.getItemMeta();
        metakills.setDisplayName("§7Top 10 §cKills");
        metakills.setLore(VagtUtils.top10Kills("vagt"));
        kills.setItemMeta(metakills);

        ItemMeta metaDeaths = deaths.getItemMeta();
        metaDeaths.setDisplayName("§7Top 10 §4Deaths");
        metaDeaths.setLore(VagtUtils.top10Deaths("vagt"));
        deaths.setItemMeta(metaDeaths);

        ItemMeta metaWalked = walked.getItemMeta();
        metaWalked.setDisplayName("§7Top 10 §eGået");
        metaWalked.setLore(VagtUtils.top10Walk("vagt"));
        walked.setItemMeta(metaWalked);

        ItemMeta metaMoney = money.getItemMeta();
        metaMoney.setDisplayName("§7Top 10 §aPenge");
        metaMoney.setLore(VagtUtils.top10Money("vagt"));
        money.setItemMeta(metaMoney);
    }


}
