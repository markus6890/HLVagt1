package com.gmail.markushygedombrowski.npc.vagthavende;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VagthavendeOfficer implements Listener {

    private final int SEEDS_INDEX = 10;
    private final int GEAR_INDEX = 13;
    private final int BREAD_INDEX = 15;
    private DeliverGearGUI deliverGearGUI;

    public VagthavendeOfficer(DeliverGearGUI deliverGearGUI) {
        this.deliverGearGUI = deliverGearGUI;
    }

    public void create(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Vagthavende Officer");
        ItemStack seeds = new ItemStack(Material.SEEDS, 1);
        ItemStack gear = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 1);
        ItemMeta seedsMeta = seeds.getItemMeta();
        ItemMeta gearMeta = gear.getItemMeta();
        ItemMeta breadMeta = bread.getItemMeta();
        seedsMeta.setDisplayName("§9§lSeeds");
        gearMeta.setDisplayName("§6§lGear");
        breadMeta.setDisplayName("§e§lBread");
        List<String> lore = new ArrayList<>();
        lore.add(0,"§7Aflever §9§lSeeds §7her!");
        lore.add(1,"§7Du for §b5 §3exp");
        seedsMeta.setLore(lore);
        lore.set(0,"§7Aflever §6§lGear §7her!");
        lore.remove(1);
        gearMeta.setLore(lore);
        lore.set(0,"§7Aflever §e§lBread §7her!");
        lore.add(1,"§7Du for §b30 §3exp");
        breadMeta.setLore(lore);
        seeds.setItemMeta(seedsMeta);
        gear.setItemMeta(gearMeta);
        bread.setItemMeta(breadMeta);
        inventory.setItem(SEEDS_INDEX, seeds);
        inventory.setItem(GEAR_INDEX, gear);
        inventory.setItem(BREAD_INDEX, bread);
        player.openInventory(inventory);

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
        if(inventory.getTitle().equalsIgnoreCase("Vagthavende Officer")) {
            if (clickedSlot == SEEDS_INDEX) {
                p.sendMessage("§7Du har afleveret §9§lSeeds");
                p.closeInventory();
            }
            if (clickedSlot == GEAR_INDEX) {
                deliverGearGUI.create(p);
            }
            if (clickedSlot == BREAD_INDEX) {
                p.sendMessage("§7Du har afleveret §e§lBread");
                p.closeInventory();
            }
        }


    }
}
