package com.gmail.markushygedombrowski.sign;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.Logger;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RepairGUI implements Listener {
    private final int REPAIRALL_INDEX = 11;
    private final int REPAIRONE_INDEX = 15;
    private ItemStack repairOne = new ItemStack(Material.DIAMOND_SWORD);
    private ItemStack repairAll = new ItemStack(Material.DIAMOND_CHESTPLATE);
    private HLvagt plugin;
    private int fullKost;
    private int kost = 2000;
    private Logger logger;
    public RepairGUI(HLvagt plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }


    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§eRepair");


        ItemMeta metaRepairOne = repairOne.getItemMeta();
        ItemMeta metaRepairAll = repairAll.getItemMeta();

        metaRepairOne.setDisplayName("§eRepair §7hånd");
        metaRepairAll.setDisplayName("§eRepair §7all");

        List<String> repairOnelore = new ArrayList<>();
        repairOnelore.add("§7Koster: §a" + kost);
        metaRepairOne.setLore(repairOnelore);
        repairOne.setItemMeta(metaRepairOne);

        ItemStack[] items = p.getInventory().getContents();
        int amount = 0;
        for (ItemStack item : items) {
            if(!(item == null)) {
                amount = amount + 1;
            }

        }
        fullKost = kost * amount;
        List<String> repairAllLore = new ArrayList<>();
        repairAllLore.add("§7Koster: §a" + fullKost);
        repairAllLore.add("§7Antal: §b" + amount);
        metaRepairAll.setLore(repairAllLore);
        repairAll.setItemMeta(metaRepairAll);


        inventory.setItem(REPAIRONE_INDEX, repairOne);
        inventory.setItem(REPAIRALL_INDEX, repairAll);
        p.openInventory(inventory);
    }


    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("§eRepair")) {
            switch (clickedSlot) {
                case (REPAIRONE_INDEX):
                    ItemStack item = p.getItemInHand();
                    if (item.getType() == Material.AIR || item.getType() == Material.SKULL) {
                        p.sendMessage("§cDu har ikke noget i hånden!");

                    }else if (!plugin.econ.has(p, kost)) {
                        p.sendMessage("§cDu har ikke nok penge!");

                    } else {
                        short dura = (short) (item.getType().getMaxDurability() - item.getType().getMaxDurability());
                        item.setDurability(dura);
                        plugin.econ.withdrawPlayer(p, kost);
                        logger.formatMessage("REPAIRSIGN: ", p.getName() + " has repaired item in hand: " + item);
                        p.sendMessage("§7Hånd §e repaired");
                    }
                    break;
                case(REPAIRALL_INDEX):
                    ItemStack[] items = p.getInventory().getContents();
                    List<String> logItems = new ArrayList<>();
                    int amount = 0;
                    for (ItemStack itemi : items) {
                        if(!(itemi == null)) {
                            amount = amount + 1;
                            logItems.add(itemi.toString());
                        }

                    }
                    fullKost = kost * amount;
                    if(p.getInventory().getContents() == null) {
                        p.sendMessage("Du har ikke noget i dit inventory");

                    } else if (!plugin.econ.has(p, fullKost)) {
                        p.sendMessage("§cDu har ikke nok penge!");

                    }else {
                        VagtUtils.repairItems(p);

                        logger.formatMessage(p.getName() + " has repaired all items in inventory", "REPAIRSIGN: ");
                        logger.formatMessage(p.getName() + " items: " + logItems,"REPAIRSIGN: ");
                        logItems.clear();
                        p.sendMessage("§7Alle items i dit inventory §e repaired");
                        plugin.econ.withdrawPlayer(p,fullKost);

                    }
                    break;
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }

    }

}
