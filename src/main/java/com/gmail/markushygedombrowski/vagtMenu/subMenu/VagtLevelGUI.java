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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VagtLevelGUI implements Listener {
    private PlayerProfiles playerProfiles;
    public VagtLevelGUI(PlayerProfiles playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    public void openVagtLevelGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§2Vagt Level");
        PlayerProfile playerProfile = playerProfiles.getPlayerProfile(player.getUniqueId());
        ItemStack red = new ItemStack(new Wool(DyeColor.RED).toItemStack(1));
        ItemStack green = new ItemStack(new Wool(DyeColor.GREEN).toItemStack(1));
        ItemStack yellow = new ItemStack(new Wool(DyeColor.YELLOW).toItemStack(1));

        for(int i = 9; i < 45; i = i + 1) {
            ItemStack item;
            int lvl = i - 8;
            if(playerProfile.getLvl() >= lvl) {
                if(playerProfile.getLvl() == lvl) {
                    item = yellow;
                } else {
                    item = green;
                }
            } else {
                item = red;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§2§lVagt Level §6" + lvl);
            List<String> lore = new ArrayList<>();
            lore.add("§7§a+ §b1000 §7løn");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(i, item);
        }
        Random random = new Random();
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random.nextInt(15));
        for (ItemStack item : inventory.getContents()) {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null || !event.getClickedInventory().getTitle().equalsIgnoreCase("§2Vagt Level")) {
            return;
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        if(clickeditem.getType() == Material.AIR) {
            return;
        }
        if(clickeditem.getType() == Material.WOOL) {
            if(clickeditem.getData().getData() == DyeColor.RED.getData()) {
                p.sendMessage("§cDu har ikke nået dette level endnu!");
            } else if(clickeditem.getData().getData() == DyeColor.YELLOW.getData()) {
                p.sendMessage("§eDu har ikke nået dette level endnu!");
            } else if(clickeditem.getData().getData() == DyeColor.GREEN.getData()) {
                p.sendMessage("§aDu har nået dette level!");
            }
        }
    }
}
