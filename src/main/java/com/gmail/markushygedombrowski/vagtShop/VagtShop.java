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

public class VagtShop implements Listener {
    private final int SWORD_INDEX = 2;
    private final int HELMET_INDEX = 4;
    private final int BOW_INDEX = 6;
    private final int STICK_INDEX = 11;
    private final int CHEST_INDEX = 13;
    private final int ARROW_INDEX = 15;
    private final int LEGS_INDEX = 22;
    private final int FOOD_INDEX = 30;
    private final int BOOTS_INDEX = 31;
    private HLvagt plugin;
    private String region;
    private ItemStack sword;
    private ItemStack chest;
    private ItemStack legs;
    private ItemStack boots;
    private ItemStack bow = new ItemStack(Material.BOW);
    private ItemStack food = new ItemStack(Material.GRILLED_PORK);
    private ItemStack stick = new ItemStack(Material.STICK);
    private ItemStack arrow = new ItemStack(Material.ARROW, 64);

    public VagtShop(HLvagt plugin) {
        this.plugin = plugin;
    }


    public void vagtShop(Player p, String region) {
        this.region = region;
        Inventory inventory = Bukkit.createInventory(null, 36, region + "-Vagt §bShop §8" + p.getName());
        if (region.equalsIgnoreCase("§bB") || region.equalsIgnoreCase("§aA")) {
            bAndAItems();
        } else {
            cItems();
        }
        ItemStack helmet;
        if (p.hasPermission("diaHelmet")) {
            helmet = new ItemStack(Material.DIAMOND_HELMET);
        } else if (p.hasPermission("officer")) {
            helmet = new ItemStack(Material.CHAINMAIL_HELMET);
        } else {
            helmet = new ItemStack(Material.GOLD_HELMET);
        }
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK,2);

        ItemMeta metasword = sword.getItemMeta();
        ItemMeta metahelmet = helmet.getItemMeta();
        ItemMeta metachest = chest.getItemMeta();
        ItemMeta metalegs = legs.getItemMeta();
        ItemMeta metaboots = boots.getItemMeta();
        ItemMeta metabow = bow.getItemMeta();
        ItemMeta metafood = food.getItemMeta();
        ItemMeta metastick = stick.getItemMeta();
        ItemMeta metaarrow = arrow.getItemMeta();


        metasword.setDisplayName(region + "-Sværd");
        metahelmet.setDisplayName(region + "-Hjelm");
        metachest.setDisplayName(region + "-ChestPlate");
        metalegs.setDisplayName(region + "-Bukser");
        metaboots.setDisplayName(region + "-Sko");
        metabow.setDisplayName(region + "-Bow");
        metafood.setDisplayName(region + "-Mad");
        metastick.setDisplayName(region + "-Stav");
        metaarrow.setDisplayName("§cVagt-pile");

        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Koster: §a" + 400);
        metahelmet.setLore(lore);
        metachest.setLore(lore);
        metalegs.setLore(lore);
        metaboots.setLore(lore);
        metasword.setLore(lore);
        lore.set(0, "§7Koster: §a" + 300);
        metabow.setLore(lore);
        lore.set(0, "§7Koster: §a" + 200);
        metaarrow.setLore(lore);
        lore.set(0, "§7Koster: §a" + 100);
        metafood.setLore(lore);
        metastick.setLore(lore);


        sword.setItemMeta(metasword);
        helmet.setItemMeta(metahelmet);
        chest.setItemMeta(metachest);
        legs.setItemMeta(metalegs);
        boots.setItemMeta(metaboots);
        food.setItemMeta(metafood);
        bow.setItemMeta(metabow);
        stick.setItemMeta(metastick);
        arrow.setItemMeta(metaarrow);

        inventory.setItem(SWORD_INDEX, sword);
        inventory.setItem(HELMET_INDEX, helmet);
        inventory.setItem(CHEST_INDEX, chest);
        inventory.setItem(LEGS_INDEX, legs);
        inventory.setItem(BOOTS_INDEX, boots);
        inventory.setItem(FOOD_INDEX, food);
        inventory.setItem(BOW_INDEX, bow);
        inventory.setItem(FOOD_INDEX, food);
        inventory.setItem(STICK_INDEX, stick);
        inventory.setItem(ARROW_INDEX, arrow);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        double pay;
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase(region + "-Vagt §bShop §8" + p.getName())) {
            ItemStack helmet;
            pay = 400;
            if (region.equalsIgnoreCase("§bB") || region.equalsIgnoreCase("§aA")) {
                bAndAItems();
            } else {
                cItems();
            }
            switch (clickedSlot) {
                case SWORD_INDEX:
                    itembuy(sword, region + "-Sværd", p, pay);
                    break;
                case HELMET_INDEX:
                    if (p.hasPermission("diaHelmet")) {
                        helmet = new ItemStack(Material.DIAMOND_HELMET);
                    } else if (p.hasPermission("officer")) {
                        helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                    } else helmet = new ItemStack(Material.GOLD_HELMET);

                    itembuy(helmet, region + "-Hjelm", p, pay );
                    break;
                case CHEST_INDEX:
                    itembuy(chest, region + "-ChestPlate", p, pay );
                    break;
                case LEGS_INDEX:
                    itembuy(legs, region + "-Bukser", p, pay );
                    break;
                case BOOTS_INDEX:
                    itembuy(boots, region + "-Sko", p, pay );
                    break;
                case BOW_INDEX:
                    pay = 300;
                    itembuy(bow,region + "-Bue",p,pay);
                    break;
                case ARROW_INDEX:
                    pay = 200;
                    itembuy(arrow, "§cVagt§6-§7Pile",p,pay);
                    break;
                case FOOD_INDEX:
                    pay = 100;
                    itembuy(food, "§cVagt§6-§7Mad",p,pay);
                    break;
                case STICK_INDEX:
                    pay = 100;
                    itembuy(stick, "§cVagt Stav",p,pay);
                    break;

            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);


        }
    }

    public void itembuy(ItemStack item, String name, Player p, double pay) {
        if (!plugin.econ.has(p, pay)) {
            p.sendMessage("§cDu har ikke nok penge!");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> itemlore = new ArrayList<>();
        itemlore.add("§cMå kun bruges i " + region + "!");

        itemlore.add(" ");
        itemlore.add("§7Købt af: §c" + p.getName());
        meta.setLore(itemlore);
        p.sendMessage("§7Du har købt " + name);
        item.setItemMeta(meta);
        p.getInventory().addItem(item);
        plugin.econ.withdrawPlayer(p, pay);


    }

    public void cItems() {
        sword = new ItemStack(Material.IRON_SWORD);
        chest = new ItemStack(Material.IRON_CHESTPLATE);
        legs = new ItemStack(Material.IRON_LEGGINGS);
        boots = new ItemStack(Material.IRON_BOOTS);

    }

    public void bAndAItems() {
        sword = new ItemStack(Material.DIAMOND_SWORD);
        chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        legs = new ItemStack(Material.DIAMOND_LEGGINGS);
        boots = new ItemStack(Material.DIAMOND_BOOTS);

    }


}
