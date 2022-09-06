package com.gmail.markushygedombrowski.mikkel;

import com.gmail.markushygedombrowski.HLvagt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Gui implements Listener {


    private HLvagt plugin;

    public Gui(HLvagt plugin) {
        this.plugin = plugin;
    }
    private final int DIAMOND_INDEX = 4;

    public void create(Player p){
        Inventory inventory = Bukkit.createInventory(null,9,"Gui");
        ItemStack diamond = new ItemStack(Material.DIAMOND);

        ItemMeta metaDia = diamond.getItemMeta();

        metaDia.setDisplayName("§bMin diamond");

        diamond.setItemMeta(metaDia);

        inventory.setItem(DIAMOND_INDEX, diamond);

        p.openInventory(inventory);

    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if(item == null) {
            return;
        }

        if (inventory.getTitle().equalsIgnoreCase("Gui")) {
            if (clickedSlot == DIAMOND_INDEX) {
                p.sendMessage("§6Hej med dig §7" + p.getName() + " §6går det godt");
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }









}
