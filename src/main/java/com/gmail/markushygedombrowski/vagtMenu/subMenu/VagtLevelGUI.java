package com.gmail.markushygedombrowski.vagtMenu.subMenu;

import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import net.citizensnpcs.trait.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.text.DecimalFormat;
import java.util.*;

public class VagtLevelGUI implements Listener {
    private PlayerProfiles playerProfiles;
    private HashMap<UUID, Integer> playerPageNumbers = new HashMap<>();

    public VagtLevelGUI(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    private final int NEXT_PAGE = 53;
    private final int PREV_PAGE = 45;
    public void openVagtLevelGUI(Player p, int currentPage) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§2Vagt Level side " + currentPage);
        playerPageNumbers.put(p.getUniqueId(), currentPage);
        inventory.setItem(NEXT_PAGE, createItem(p, Material.ARROW, "§aNæste side", null));

        for (int i = 9; i < 45; i++) {
            int lvl = i - 8;
            inventory.setItem(i, createItem(p, Material.WOOL, "§2§lVagt Level §6" + lvl, lvl));
        }
        fillEmptySlots(inventory);
        p.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        int clickedSlot = event.getRawSlot();
        if (event.getCurrentItem() == null || !event.getClickedInventory().getTitle().contains("§2Vagt Level")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem.getType() == Material.AIR || clickedItem.getType() == Material.STAINED_GLASS_PANE){
            return;
        }
        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§aNæste side") || clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§cForrige side")) {
            int currentPage = playerPageNumbers.getOrDefault(p.getUniqueId(), 1);
            int newPage = clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§aNæste side") ? currentPage + 1 : currentPage - 1;
            playerPageNumbers.put(p.getUniqueId(), newPage);
            nextSite(p, newPage);
        }
    }

    public void nextSite(Player p, int currentPage) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§2Vagt Level side " + currentPage);
        inventory.setItem(NEXT_PAGE, createItem(p, Material.ARROW, "§aNæste side", null));
        if (currentPage > 1) {
            inventory.setItem(PREV_PAGE, createItem(p, Material.ARROW, "§cForrige side", null));
        }
        for (int i = 9; i < 45; i++) {
            int lvl = i - 8 + (currentPage - 1) * 36;
            inventory.setItem(i, createItem(p, Material.WOOL, "§2§lVagt Level §6" + lvl, lvl));
        }
        fillEmptySlots(inventory);
        p.openInventory(inventory);
    }

    private ItemStack createItem(Player p, Material material, String displayName, Integer lvl) {
        ItemStack item;
        if (lvl != null) {
            PlayerProfile playerProfile = playerProfiles.getPlayerProfile(p.getUniqueId());
            int level = (int) playerProfile.getProperty("level");
            DyeColor color = level >= lvl ? (level == lvl ? DyeColor.YELLOW : DyeColor.GREEN) : DyeColor.RED;
            item = new ItemStack(new Wool(color).toItemStack(1));
            List<String> lore = new ArrayList<>();
            String pattern = "###,###.##";
            DecimalFormat df = new DecimalFormat(pattern);
            lore.add("§3Exp: §b" + playerProfile.getProperty("exp") + "§7/§b" + df.format(playerProfile.getExpSpecificLevel(lvl)));
            lore.add("§7§a+ §b1000 §7løn");
            if (lvl == 30) {
                lore.add("§cSpassermine Pickaxe: §5Unbreaking 3,");
            } else if (lvl == 25) {
                lore.add("§aSpassermine Pickaxe: §5Efficiency 1");
                lore.add("§aDobbelt drop ved §5Shards");
            } else if (lvl == 23) {
                lore.add("§5Random §6Glass");
            } else if (lvl == 20) {
                lore.add("§cSpassermine Pickaxe: §5Unbreaking 2");
                lore.add("§5Random§7: §6Blomst");
                lore.add("§aAdgang til §cA Vagt Celler ");
            } else if (lvl == 18) {
                lore.add("§aAdgang til §bB Vagt Celler ");
            }
            if (lvl == 10) {
                lore.add("§cSpassermine Pickaxe: §5Unbreaking 1");
                lore.add("§aAdgang til §cC Vagt Celler ");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void fillEmptySlots(Inventory inventory) {
        Random random = new Random();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random.nextInt(15));
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        }
    }
}
