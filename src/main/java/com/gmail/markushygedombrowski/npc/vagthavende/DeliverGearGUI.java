package com.gmail.markushygedombrowski.npc.vagthavende;

import com.gmail.markushygedombrowski.settings.deliveredItems.ItemProfileLoader;

import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.utils.Utils;
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

import java.util.ArrayList;
import java.util.List;

public class DeliverGearGUI implements Listener {
    private final int HELMET_INDEX = 10;
    private final int CHESTPLATE_INDEX = 12;
    private final int LEGGINGS_INDEX = 14;
    private final int BOOTS_INDEX = 16;
    private final int SWORD_INDEX = 22;
    private ItemProfileLoader itemProfileLoader;
    private PlayerProfiles playerProfiles;

    public DeliverGearGUI(ItemProfileLoader itemProfileLoader, PlayerProfiles playerProfiles) {
        this.itemProfileLoader = itemProfileLoader;
        this.playerProfiles = playerProfiles;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "Aflever Gear");

        String material;
        ItemStack boots;
        ItemStack leggings;
        ItemStack sword;
        ItemStack chestplate;
        ItemStack helmet;
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
            lore.add(0, "§7Aflever " + meta.getDisplayName() + " §7her!");
            lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(meta.getDisplayName()).getExp() + " §3exp");
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

    @EventHandler
    public void onClickInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("Aflever Gear")) {

            if (clickedSlot == HELMET_INDEX || clickedSlot == CHESTPLATE_INDEX || clickedSlot == LEGGINGS_INDEX || clickedSlot == BOOTS_INDEX || clickedSlot == SWORD_INDEX) {
                gear(p, clickeditem);
                PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
                switch (clickedSlot) {
                    case HELMET_INDEX:
                        if (clickeditem.getType() == Material.DIAMOND_HELMET) {
                            profile.getDeliveredItems().setDiamondHelmet(profile.getDeliveredItems().getDiamondHelmet() + 1);
                        } else {
                            profile.getDeliveredItems().setIronHelmet(profile.getDeliveredItems().getIronHelmet() + 1);

                        }
                        break;
                    case CHESTPLATE_INDEX:
                        if (clickeditem.getType() == Material.DIAMOND_CHESTPLATE) {
                            profile.getDeliveredItems().setDiamondChestplate(profile.getDeliveredItems().getDiamondChestplate() + 1);
                        } else {
                            profile.getDeliveredItems().setIronChestplate(profile.getDeliveredItems().getIronChestplate() + 1);
                        }
                        break;
                    case LEGGINGS_INDEX:
                        if (clickeditem.getType() == Material.DIAMOND_LEGGINGS) {
                            profile.getDeliveredItems().setDiamondLeggings(profile.getDeliveredItems().getDiamondLeggings() + 1);
                        } else {
                            profile.getDeliveredItems().setIronLeggings(profile.getDeliveredItems().getIronLeggings() + 1);
                        }
                        break;
                    case BOOTS_INDEX:
                        if (clickeditem.getType() == Material.DIAMOND_BOOTS) {
                            profile.getDeliveredItems().setDiamondBoots(profile.getDeliveredItems().getDiamondBoots() + 1);
                        } else {
                            profile.getDeliveredItems().setIronBoots(profile.getDeliveredItems().getIronBoots() + 1);
                        }
                        break;
                    case SWORD_INDEX:
                        if (clickeditem.getType() == Material.DIAMOND_SWORD) {
                            profile.getDeliveredItems().setDiamondSword(profile.getDeliveredItems().getDiamondSword() + 1);
                        } else {
                            profile.getDeliveredItems().setIronSword(profile.getDeliveredItems().getIronSword() + 1);
                        }
                        break;
                }
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }


    }

    private void gear(Player p, ItemStack clickeditem) {
        if (deliverGear(p, clickeditem.getType())) {
            p.sendMessage("§7Du har afleveret " + clickeditem.getItemMeta().getDisplayName());
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            profile.setXp(profile.getXp() + itemProfileLoader.getItemProfile(clickeditem.getItemMeta().getDisplayName()).getExp());
        } else {
            p.sendMessage("§7Du har ikke " + clickeditem.getItemMeta().getDisplayName());
        }
    }


    public boolean deliverGear(Player p, Material gear) {
        if (p.getInventory().contains(gear)) {
            p.getInventory().remove(gear);
            return true;
        }
        return false;
    }
}
