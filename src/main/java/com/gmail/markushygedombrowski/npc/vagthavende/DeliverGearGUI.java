package com.gmail.markushygedombrowski.npc.vagthavende;


import com.gmail.markushygedombrowski.settings.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DeliverGearGUI implements Listener {
    private final int HELMET_INDEX = 10;
    private final int CHESTPLATE_INDEX = 12;
    private final int LEGGINGS_INDEX = 14;
    private final int BOOTS_INDEX = 16;
    private final int SWORD_INDEX = 22;
    private ItemProfileLoader itemProfileLoader;

    public DeliverGearGUI(ItemProfileLoader itemProfileLoader) {
        this.itemProfileLoader = itemProfileLoader;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "Vagthavende Officer");
        ItemStack helmet;
        ItemStack chestplate;
        ItemStack leggings;
        ItemStack boots;
        ItemStack sword;
        String material;

        if (Utils.isLocInRegion(p.getLocation(), "a")) {
            helmet = new ItemStack(Material.DIAMOND_HELMET);
            chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            boots = new ItemStack(Material.DIAMOND_BOOTS);
            sword = new ItemStack(Material.DIAMOND_SWORD);
            material = "§b§lDiamond";


        } else {
            helmet = new ItemStack(Material.IRON_HELMET);
            chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            leggings = new ItemStack(Material.IRON_LEGGINGS);
            boots = new ItemStack(Material.IRON_BOOTS);
            sword = new ItemStack(Material.IRON_SWORD);
            material = "§f§lIron";
        }

        ItemMeta helmetMeta = helmet.getItemMeta();
        ItemMeta chestplateMeta = chestplate.getItemMeta();
        ItemMeta leggingsMeta = leggings.getItemMeta();
        ItemMeta bootsMeta = boots.getItemMeta();
        ItemMeta swordMeta = sword.getItemMeta();
        helmetMeta.setDisplayName(material + " Helmet");
        chestplateMeta.setDisplayName(material + " Chestplate");
        leggingsMeta.setDisplayName(material + " Leggings");
        bootsMeta.setDisplayName(material + " Boots");
        swordMeta.setDisplayName(material + " Sword");
        List<ItemMeta> itemMetas = new ArrayList<>();
        itemMetas.add(helmetMeta);
        itemMetas.add(chestplateMeta);
        itemMetas.add(leggingsMeta);
        itemMetas.add(bootsMeta);
        itemMetas.add(swordMeta);
        itemMetas.forEach(meta -> {
            List<String> lore = new ArrayList<>();
            lore.add(0,"§7Aflever "+ meta.getDisplayName() +" §7her!");
            lore.add(1,"§7Du for §b" + itemProfileLoader.getItemProfile(meta.getDisplayName()).getExp() + " §3exp");
            meta.setLore(lore);
        });

        helmet.setItemMeta(helmetMeta);
        chestplate.setItemMeta(chestplateMeta);
        leggings.setItemMeta(leggingsMeta);
        boots.setItemMeta(bootsMeta);
        sword.setItemMeta(swordMeta);
        inv.setItem(HELMET_INDEX, helmet);
        inv.setItem(CHESTPLATE_INDEX, chestplate);
        inv.setItem(LEGGINGS_INDEX, leggings);
        inv.setItem(BOOTS_INDEX, boots);
        inv.setItem(SWORD_INDEX, sword);
        p.openInventory(inv);
    }
}
