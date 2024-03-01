package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
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

public class StatsGUI implements Listener {

    private PlayerProfiles playerProfiles;
    private final int SEEDS_INDEX = 10;
    private final int IRON_GEAR_INDEX = 12;
    private final int DIA_GEAR_INDEX = 14;
    private final int BREAD_INDEX = 16;

    public StatsGUI(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    public void create(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§a§lStats");
        ItemStack seeds = new ItemStack(Material.SEEDS, 1);
        ItemStack ironGear = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack diaGear = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 1);

        ItemMeta seedsMeta = seeds.getItemMeta();
        ItemMeta ironGearMeta = ironGear.getItemMeta();
        ItemMeta diaGearMeta = diaGear.getItemMeta();
        ItemMeta breadMeta = bread.getItemMeta();

        seedsMeta.setDisplayName("§9§lSeeds");
        ironGearMeta.setDisplayName("§6§lIron Gear");
        diaGearMeta.setDisplayName("§b§lDiamond Gear");
        breadMeta.setDisplayName("§e§lBread");
        PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Afleveret §9§lSeeds: §b" + profile.getDeliveredItems().getSeed());
        seedsMeta.setLore(lore);

        lore.set(0, "§7Afleveret §e§lBread: §b" + profile.getDeliveredItems().getBread());
        breadMeta.setLore(lore);

        lore.set(0, "§7Afleveret §6§lIron Helmets: §b" + profile.getDeliveredItems().getIronHelmet());
        lore.add(1, "§7Afleveret §6§lIron Chestplates: §b" + profile.getDeliveredItems().getIronChestplate());
        lore.add(2, "§7Afleveret §6§lIron Leggings: §b" + profile.getDeliveredItems().getIronLeggings());
        lore.add(3, "§7Afleveret §6§lIron Boots: §b" + profile.getDeliveredItems().getIronBoots());
        lore.add(4, "§7Afleveret §6§lIron Swords: §b" + profile.getDeliveredItems().getIronSword());
        ironGearMeta.setLore(lore);

        lore.set(0, "§7Afleveret §b§lDiamond Helmets: §b" + profile.getDeliveredItems().getDiamondHelmet());
        lore.set(1, "§7Afleveret §b§lDiamond Chestplates: §b" + profile.getDeliveredItems().getDiamondChestplate());
        lore.set(2, "§7Afleveret §b§lDiamond Leggings: §b" + profile.getDeliveredItems().getDiamondLeggings());
        lore.set(3, "§7Afleveret §b§lDiamond Boots: §b" + profile.getDeliveredItems().getDiamondBoots());
        lore.set(4, "§7Afleveret §b§lDiamond Swords: §b" + profile.getDeliveredItems().getDiamondSword());
        diaGearMeta.setLore(lore);

        seeds.setItemMeta(seedsMeta);
        ironGear.setItemMeta(ironGearMeta);
        diaGear.setItemMeta(diaGearMeta);
        bread.setItemMeta(breadMeta);

        inv.setItem(SEEDS_INDEX, seeds);
        inv.setItem(IRON_GEAR_INDEX, ironGear);
        inv.setItem(DIA_GEAR_INDEX, diaGear);
        inv.setItem(BREAD_INDEX, bread);
        p.openInventory(inv);

    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (event.getClickedInventory().getTitle().equals("§a§lStats")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
        }
    }
}
