package com.gmail.markushygedombrowski.vagtShop;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.utils.VagtWorldGuardUtils;
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
    private int pay;

    public VagtShopEnchant(HLvagt plugin) {
        this.plugin = plugin;
    }

    public void enchantItem(Player p, ItemStack item) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§9Enchant §bShop");
        invItem = new ItemStack(item);
        maxInvItem = new ItemStack(item.getType());

        int pay;
        checkEnchant(invItem, p);

        lvl = invItem.getEnchantmentLevel(enchant);

        if (lvl < enchant.getMaxLevel()) lvl++;

        pay = 500 * lvl;
        invItem.removeEnchantment(enchant);
        invItem.addUnsafeEnchantment(enchant, lvl);

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
        if(VagtWorldGuardUtils.isLocInRegion(p.getLocation(),"a")) {
            maxLvl = 5;
        }
        maxInvItem.addUnsafeEnchantment(enchant, maxLvl);
        inventory.setItem(MAXENCHANT_INDEX, maxInvItem);

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
        ItemStack item = p.getItemInHand();
        if (inventory.getTitle().equalsIgnoreCase("§9Enchant §bShop")) {
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
                        update(inventory,p,item);
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
                        if(VagtWorldGuardUtils.isLocInRegion(p.getLocation(),"a")) {
                            lvl = 5;
                        } else {
                            lvl = 4;
                        }

                        update(inventory,p,item);
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
        int max = 4;
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
        if(VagtWorldGuardUtils.isLocInRegion(p.getLocation(),"a")) {
            max = 5;
        }
        return lvl < max;

    }

    public void update(Inventory inventory, Player p, ItemStack item) {
        plugin.econ.withdrawPlayer(p, pay);
        item.addUnsafeEnchantment(enchant, lvl);
        p.sendMessage("§7du har købt: §9" + message + " §7for: §a" + pay);
        lvl = invItem.getEnchantmentLevel(enchant);
        if (lvl >= 4) {
            lvl = 4;
        } else {
            lvl++;
        }

        int pay = 500 * lvl;
        invItem.removeEnchantment(enchant);
        invItem.addUnsafeEnchantment(enchant, lvl);

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §a" + pay);
        ItemMeta meta = invItem.getItemMeta();
        meta.setLore(lore);
        invItem.setItemMeta(meta);
        inventory.setItem(ENCHANT_INDEX, invItem);
        p.updateInventory();
    }
}
