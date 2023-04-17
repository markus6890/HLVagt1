package com.gmail.markushygedombrowski.buff;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.VagtUtils;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class BuffGui implements Listener {
    private int BUFF_INDEX = 4;
    private final int OFFBUFF_INDEX = 6;
    private Settings settings;
    private HLvagt plugin;

    public BuffGui(Settings settings, HLvagt plugin) {
        this.settings = settings;
        this.plugin = plugin;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§c§lBuff");
        int pay = 5000;
        ItemStack buff = new ItemStack(Material.POTION,1,(short) 1);
        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §b" + pay);
        ItemMeta meta = buff.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName("§CBuff");
        buff.setItemMeta(meta);
        if (p.hasPermission("extraBuff")) {
            pay = settings.getExtraBuffPay();
            lore.set(0, "§7Koster: §b" + pay);
            ItemStack extraBuff = new ItemStack(Material.POTION,1,(short) 3);
            ItemMeta extraBuffItemMeta = extraBuff.getItemMeta();
            extraBuffItemMeta.setLore(lore);
            extraBuffItemMeta.setDisplayName("§bExtra §cBuff");
            extraBuff.setItemMeta(extraBuffItemMeta);
            BUFF_INDEX = 2;
            inventory.setItem(OFFBUFF_INDEX, extraBuff);
        } else {
            BUFF_INDEX = 4;
        }
        inventory.setItem(BUFF_INDEX, buff);
        p.openInventory(inventory);


    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getClickedInventory();
        ItemStack clickeditem = e.getCurrentItem();
        int clickedSlot = e.getRawSlot();

        if (clickeditem == null) {
            return;
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lBuff")) {
            int pay = settings.getBuffPay();
            if (clickedSlot == BUFF_INDEX) {
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke penge nok!");
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    return;
                }
                p.removePotionEffect(PotionEffectType.ABSORPTION);
                p.removePotionEffect(PotionEffectType.SPEED);
                p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, settings.getBufflength(), settings.getAbsorption()));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, settings.getBufflength(), settings.getSpeed()));
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, settings.getBufflength(), settings.getStrengh()));
                plugin.econ.withdrawPlayer(p, pay);
                p.sendMessage("§aDu har taget §cBuff");
                if (VagtUtils.procent(1)) {
                    p.sendMessage("pika pika");
                }



            }else if (clickedSlot == OFFBUFF_INDEX) {
                if (!p.hasPermission("extraBuff")) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    return;
                }
                pay = settings.getExtraBuffPay();
                if (!plugin.econ.has(p, pay)) {
                    p.sendMessage("§cDu har ikke penge nok!");
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    return;
                }
                if(VagtCooldown.isCooling(p.getName(), "extraBuff")){
                    p.sendMessage("§cDu kan først bruge denne buff igen om §b" + VagtCooldown.getRemaining(p.getName(), "extraBuff") + "§c minutter");
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    return;
                }
                p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
                plugin.econ.withdrawPlayer(p, pay);
                p.sendMessage("§aDu har taget Extra §cBuff");
                VagtCooldown.add(p.getName(), "extraBuff", settings.getExtraBuffLength(),System.currentTimeMillis());


            }
            p.sendMessage("§aDu har betalt §b" + pay + "§a for §cBuff");
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }

    }
}
