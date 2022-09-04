package com.gmail.markushygedombrowski.vagtShop;

import com.gmail.markushygedombrowski.HLvagt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class VagtShopEnchant implements Listener {
    private final int ENCHANT_INDEX = 12;
    private final int MAXENCHANT_INDEX = 14;
    private ItemStack invItem;
    private HLvagt plugin;
    private Enchantment enchant;
    private Enchantment maxEnchant;
    private ItemStack maxInvItem;
    private int lvl;
    private String message;

    public VagtShopEnchant(HLvagt plugin) {
        this.plugin = plugin;
    }

    public void enchantItem(Player p, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§9Enchant §bShop" + p.getName());
        invItem = new ItemStack(item);
        maxInvItem = new ItemStack(item.getType());


        int pay;
        checkEnchant(invItem, p);

        lvl = invItem.getEnchantmentLevel(enchant);

        if (lvl < enchant.getMaxLevel()) lvl++;

        pay = 500 * lvl;
        invItem.removeEnchantment(enchant);
        invItem.addEnchantment(enchant, lvl);

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §a" + pay);
        ItemMeta meta = invItem.getItemMeta();
        meta.setLore(lore);
        invItem.setItemMeta(meta);
        inventory.setItem(ENCHANT_INDEX, invItem);

        int maxLvl;
        pay = 6000;
        lore.set(0, "§7Koster: §a" + pay);
        meta.setLore(lore);
        maxInvItem.setItemMeta(meta);
        maxLvl = 4;
        maxInvItem.addEnchantment(enchant, maxLvl);
        inventory.setItem(MAXENCHANT_INDEX, maxInvItem);


        p.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        int pay;
        if (clickeditem == null) {
            return;
        }
        ItemStack item = p.getItemInHand();
        if (inventory.getTitle().equalsIgnoreCase("§9Enchant §bShop" + p.getName())) {
            if (!(item.getType() == invItem.getType())) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
                return;
            }
            if (clickedSlot == ENCHANT_INDEX) {
                lvl = item.getEnchantmentLevel(enchant);
                if (checkEnchant(item, p)) {


                    lvl = lvl + 1;
                    pay = 500 * lvl;
                    if (plugin.econ.has(p, pay)) {

                        plugin.econ.withdrawPlayer(p, pay);
                        item.addEnchantment(enchant, lvl);
                        p.sendMessage("§7du har købt: §9" + message + " §7for: §a" + pay);
                        update(inventory,p);
                    } else {
                        p.sendMessage("§cDu har ikke nok penge!");
                        event.setCancelled(true);
                        event.setResult(Event.Result.DENY);
                        return;
                    }

                } else {
                    p.sendMessage("§7Du kan ikke §9enchante §7det item mere");
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
            if (clickedSlot == MAXENCHANT_INDEX) {
                lvl = item.getEnchantmentLevel(enchant);
                if (checkEnchant(item, p)) {
                    pay = 6000;
                    if (plugin.econ.has(p, pay)) {
                        lvl = 4;
                        plugin.econ.withdrawPlayer(p, pay);
                        item.addEnchantment(enchant, lvl);
                        p.sendMessage("§7du har købt: §9" + message + " §7for: §a" + pay);
                        update(inventory,p);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cDu har ikke nok penge!");
                        event.setCancelled(true);
                        event.setResult(Event.Result.DENY);
                        return;
                    }

                } else {
                    p.sendMessage("§7Du kan ikke §9enchante §7det item mere");
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
                return;

            }

            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }
    }

    public boolean checkEnchant(ItemStack item, Player p) {
        if (item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD) {
            enchant = Enchantment.DAMAGE_ALL;
            message = "Sharpness";
        } else if (item.getType() == Material.BOW) {
            enchant = Enchantment.ARROW_DAMAGE;
            message = "Power";
        } else if (invItem.getType() == Material.IRON_PICKAXE || invItem.getType() == Material.IRON_AXE || invItem.getType() == Material.IRON_SPADE) {
            enchant = Enchantment.DIG_SPEED;
            message = "Effectivity";
        } else {
            enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
            message = "Protection";

        }
        return lvl < 4;

    }

    public void update(Inventory inventory, Player p) {
        lvl = invItem.getEnchantmentLevel(enchant);
        if (lvl >= 4) {
            lvl = 4;
        } else {
            lvl++;
        }

        int pay = 500 * lvl;
        invItem.removeEnchantment(enchant);
        invItem.addEnchantment(enchant, lvl);

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §a" + pay);
        ItemMeta meta = invItem.getItemMeta();
        meta.setLore(lore);
        invItem.setItemMeta(meta);
        inventory.setItem(ENCHANT_INDEX, invItem);
        p.updateInventory();
    }
}
