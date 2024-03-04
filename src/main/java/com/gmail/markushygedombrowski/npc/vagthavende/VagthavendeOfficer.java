package com.gmail.markushygedombrowski.npc.vagthavende;

import com.gmail.markushygedombrowski.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfile;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VagthavendeOfficer implements Listener {

    private final int SHARDS_INDEX = 11;
    private final int GEAR_INDEX = 13;
    private final int BREAD_INDEX = 15;
    private DeliverGearGUI deliverGearGUI;
    private ItemProfileLoader itemProfileLoader;
    private PlayerProfiles playerProfiles;

    public VagthavendeOfficer(DeliverGearGUI deliverGearGUI, ItemProfileLoader itemProfileLoader, PlayerProfiles playerProfiles) {
        this.deliverGearGUI = deliverGearGUI;
        this.itemProfileLoader = itemProfileLoader;
        this.playerProfiles = playerProfiles;
    }

    public void create(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Vagthavende Officer");
        ItemStack shards = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);
        ItemStack gear = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack bread = new ItemStack(Material.BREAD, 1);
        ItemMeta shardsMeta = shards.getItemMeta();
        ItemMeta gearMeta = gear.getItemMeta();
        ItemMeta breadMeta = bread.getItemMeta();
        shardsMeta.setDisplayName("§9§lShards");
        gearMeta.setDisplayName("§6§lGear");
        breadMeta.setDisplayName("§e§lBread");
        List<String> lore = new ArrayList<>();
        lore.add(0, "§7Aflever §9§lShards §7her!");
        lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(shardsMeta.getDisplayName()).getExp() + " §3exp");
        shardsMeta.setLore(lore);
        lore.set(0, "§7Aflever §6§lGear §7her!");
        lore.remove(1);
        gearMeta.setLore(lore);
        lore.set(0, "§7Aflever §e§lBread §7her!");
        lore.add(1, "§7Du for §b" + itemProfileLoader.getItemProfile(breadMeta.getDisplayName()).getExp() + " §3exp");
        breadMeta.setLore(lore);
        shards.setItemMeta(shardsMeta);
        gear.setItemMeta(gearMeta);
        bread.setItemMeta(breadMeta);
        inventory.setItem(SHARDS_INDEX, shards);
        inventory.setItem(GEAR_INDEX, gear);
        inventory.setItem(BREAD_INDEX, bread);
        player.openInventory(inventory);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();
        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }
        if (inventory.getTitle().equalsIgnoreCase("Vagthavende Officer")) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            switch (clickedSlot) {
                case SHARDS_INDEX:
                    int amount = deliverItem(p, clickeditem);
                   if(amount == 0){
                       p.sendMessage("§7Du har ikke §9§lShards §7i dit inventory");
                       break;
                   }
                   profile.getDeliveredItems().setShards(profile.getDeliveredItems().getShards() + amount);
                    break;
                case GEAR_INDEX:
                    deliverGearGUI.create(p);
                    break;
                case BREAD_INDEX:
                    amount = deliverItem(p, clickeditem);
                    if (amount == 0) {
                        p.sendMessage("§7Du har ikke §e§lBread §7i dit inventory");
                        break;
                    }
                    profile.getDeliveredItems().setBread(profile.getDeliveredItems().getBread() + amount);
                    break;
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

        }


    }

    public int deliverItem(Player p, ItemStack item)  {
        if (p.getInventory().contains(item.getType())) {
            PlayerProfile profile = playerProfiles.getPlayerProfile(p.getUniqueId());
            int amount = 0;
            int exp = 0;
            for (ItemStack itemStack : p.getInventory().getContents()) {
                if(itemStack == null) continue;
                if (itemStack.getType() == item.getType()) {
                    amount += itemStack.getAmount();
                    exp += itemProfileLoader.getItemProfile(item.getItemMeta().getDisplayName()).getExp() * itemStack.getAmount();

                    p.getInventory().remove(itemStack);
                }

            }
            p.sendMessage("§7Du har afleveret §a" + amount + " " + item.getItemMeta().getDisplayName());
            p.sendMessage("§7Du har fået §b" + exp + "§3 exp");
            profile.setXp(profile.getXp() + exp);

            if(profile.getXp() >= profile.getXpToNextLvl()){
                levelUp(p, profile);


            }


            return amount;
        }

        return 0;
    }

    private void levelUp(Player p, PlayerProfile profile) {
        profile.setLvl(profile.getLvl() + 1);
        p.sendMessage("§6§l--------§a§lLevel Up!§6§l--------");
        p.sendMessage("Tillykke du er nu level §b" + profile.getLvl());
        p.sendMessage("Du skal bruge §b" + profile.getXpToNextLvl() + " §3exp til næste level");
        p.sendMessage("§6§l--------§a§lLevel Up!§6§l--------");
        profile.setXp(0);
        try {
            playerProfiles.save(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
