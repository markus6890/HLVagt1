package com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter;

import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.vagtMenu.MainMenu;
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


import java.util.List;


public class TopVagterGUI implements Listener {
    private final int TOP10KILLS_index = 1;
    private final int TOP10DEATH_INDEX = 3;
    private final int TOP10WALKED_INDEX = 5;
    private final int TOP10MONEY_INDEX = 7;
    private ItemStack kills = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack deaths = new ItemStack(Material.SKULL_ITEM);
    private ItemStack walked = new ItemStack(Material.DIAMOND_BOOTS);
    private ItemStack money = new ItemStack(Material.GOLD_INGOT);

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

        ItemMeta metakills = kills.getItemMeta();
        metakills.setDisplayName("§7Top 10 §cKills");
        metakills.setLore(Utils.top10("vagt", Statistic.PLAYER_KILLS));
        kills.setItemMeta(metakills);

        ItemMeta metaDeaths = deaths.getItemMeta();
        metaDeaths.setDisplayName("§7Top 10 §4Deaths");
        metaDeaths.setLore(Utils.top10("vagt",Statistic.DEATHS));
        deaths.setItemMeta(metaDeaths);

        ItemMeta metaWalked = walked.getItemMeta();
        metaWalked.setDisplayName("§7Top 10 §eGået");
        metaWalked.setLore(Utils.top10("vagt",Statistic.WALK_ONE_CM));
        walked.setItemMeta(metaWalked);

        ItemMeta metaMoney = money.getItemMeta();
        metaMoney.setDisplayName("§7Top 10 §aPenge");
        metaMoney.setLore(Utils.top10Money("vagt"));
        money.setItemMeta(metaMoney);
    }


}
