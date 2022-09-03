package com.gmail.markushygedombrowski.vagtShop;

import com.gmail.markushygedombrowski.HLvagt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import java.util.List;

public class VagtShopEnchant implements Listener {
    private final int ENCHANT_INDEX = 12;
    private final int MAXENCHANT_INDEX = 14;
    private ItemStack invItem;
    private ItemStack mexInvItem;
    private HLvagt plugin;
    private Enchantment enchant;
    private int lvl;

    public VagtShopEnchant(HLvagt plugin) {
        this.plugin = plugin;
    }

    public void enchantItem(Player p, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§cVagt-§9Enchant §bShop");
        invItem = new ItemStack(item.getType());
        mexInvItem = new ItemStack(item.getType());
        int pay = 500;
        Enchantment enchant;
        int lvl;
        if (invItem.getEnchantments() == null) {
            if (invItem.getType() == Material.IRON_SWORD || invItem.getType() == Material.DIAMOND_SWORD) {
                enchant = Enchantment.DAMAGE_ALL;
            } else if (invItem.getType() == Material.BOW) {
                enchant = Enchantment.ARROW_DAMAGE;
            } else enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
            lvl = 1;
            invItem.addEnchantment(enchant, lvl);
        } else {
            if (invItem.getType() == Material.IRON_SWORD || invItem.getType() == Material.DIAMOND_SWORD) {
                enchant = Enchantment.DAMAGE_ALL;
            } else if (invItem.getType() == Material.BOW) {
                enchant = Enchantment.ARROW_DAMAGE;
            } else {
                enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
            }
            lvl = invItem.getEnchantmentLevel(enchant);

            lvl = lvl + 1;
            pay = 500 * lvl;
            invItem.addEnchantment(enchant, lvl);

        }

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §a" + pay);
        ItemMeta meta = invItem.getItemMeta();
        meta.setLore(lore);
        invItem.setItemMeta(meta);
        inventory.setItem(ENCHANT_INDEX, invItem);


        invItem.addEnchantment(enchant, lvl);
        pay = 5000;
        lore.set(0, "§7Koster: §a" + pay);
        meta.setLore(lore);
        mexInvItem.setItemMeta(meta);
        inventory.setItem(MAXENCHANT_INDEX, mexInvItem);
        p.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        double pay;
        ItemStack item = p.getItemInHand();
        if (inventory.getTitle().equalsIgnoreCase("§cVagt-§9Enchant §bShop")) {
            if (clickedSlot == ENCHANT_INDEX) {
                checkEnchant(item, p);
                if (enchant.getMaxLevel() == lvl) {

                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
                lvl = lvl + 1;
                pay = 500 * lvl;
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke nok penge!");
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }

                item.addEnchantment(enchant, lvl);
            }
            if (clickedSlot == MAXENCHANT_INDEX) {
                checkEnchant(item, p);
                lvl = enchant.getMaxLevel();
                pay = 5000;
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke nok penge!");
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    return;
                }
                item.addEnchantment(enchant, lvl);

            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    public void checkEnchant(ItemStack item, Player p) {
        if (item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD) {
            enchant = Enchantment.DAMAGE_ALL;
        } else if (item.getType() == Material.BOW) {
            enchant = Enchantment.ARROW_DAMAGE;
        } else {
            enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        lvl = item.getEnchantmentLevel(enchant);
        if (enchant.getMaxLevel() == lvl) {
            p.sendMessage("§7Du kan ikke §9enchante §7det item mere");
            return;
        }


    }
}
