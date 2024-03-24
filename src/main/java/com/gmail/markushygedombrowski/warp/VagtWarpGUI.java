package com.gmail.markushygedombrowski.warp;


import com.gmail.markushygedombrowski.inventory.ChangeInvOnWarp;
import com.gmail.markushygedombrowski.inventory.InvHolder;
import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.utils.VagtUtils;
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

public class VagtWarpGUI implements Listener {
    private final VagtSpawnManager vagtSpawnManager;
    private final int WARP_C_INDEX = 11;
    private final int WARP_B_INDEX = 13;
    private final int WARP_A_INDEX = 15;
    private final int WARP_CVCELLESTUE_INDEX = 29;
    private final int WARP_BVCELLESTUE_INDEX = 31;
    private final int WARP_AVAGTCELLER_INDEX = 33;
    private final ChangeInvOnWarp changeInvOnWarp;

    public VagtWarpGUI(VagtSpawnManager vagtSpawnManager, ChangeInvOnWarp changeInvOnWarp) {
        this.vagtSpawnManager = vagtSpawnManager;
        this.changeInvOnWarp = changeInvOnWarp;
    }

    public void create(Player p, String block) {
        Inventory inventory = Bukkit.createInventory(null, 45, "Warp: " + block);
        Wool woolRed = new Wool(DyeColor.RED);
        Wool woolBlue = new Wool(DyeColor.BLUE);
        Wool woolGreen = new Wool(DyeColor.GREEN);
        ItemStack warpC = woolRed.toItemStack(1);
        ItemStack warpB = woolBlue.toItemStack(1);
        ItemStack warpA = woolGreen.toItemStack(1);
        ItemStack warpAvagtceller = woolGreen.toItemStack(1);
        ItemStack warpBvcellestue = woolBlue.toItemStack(1);
        ItemStack warpCvcellestue = woolRed.toItemStack(1);

        ItemMeta metaC = warpC.getItemMeta();
        ItemMeta metaB = warpB.getItemMeta();
        ItemMeta metaA = warpA.getItemMeta();
        ItemMeta metaAvagtceller = warpAvagtceller.getItemMeta();
        ItemMeta metaBvcellestue = warpBvcellestue.getItemMeta();
        ItemMeta metaCvcellestue = warpCvcellestue.getItemMeta();

        metaC.setDisplayName("§c§lWarp C");
        metaB.setDisplayName("§b§lWarp B");
        metaA.setDisplayName("§a§lWarp A");
        metaBvcellestue.setDisplayName("§b§lWarp B Vand Celler");
        metaAvagtceller.setDisplayName("§a§lWarp A Vagt Celler");
        metaCvcellestue.setDisplayName("§c§lWarp C Vagt Celler");


        warpC.setItemMeta(metaC);
        warpB.setItemMeta(metaB);
        warpA.setItemMeta(metaA);
        warpCvcellestue.setItemMeta(metaCvcellestue);
        warpBvcellestue.setItemMeta(metaBvcellestue);
        warpAvagtceller.setItemMeta(metaAvagtceller);

        inventory.setItem(WARP_C_INDEX, warpC);
        inventory.setItem(WARP_B_INDEX, warpB);
        inventory.setItem(WARP_A_INDEX, warpA);
        inventory.setItem(WARP_CVCELLESTUE_INDEX, warpCvcellestue);
        inventory.setItem(WARP_BVCELLESTUE_INDEX, warpBvcellestue);
        inventory.setItem(WARP_AVAGTCELLER_INDEX, warpAvagtceller);
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        inventory.forEach(item -> {
            if (item == null) {
                inventory.setItem(inventory.firstEmpty(), glass);
            }
        });
        p.openInventory(inventory);

    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickeditem = event.getCurrentItem();

        int clickedSlot = event.getRawSlot();
        if (clickeditem == null) {
            return;
        }

        if (inventory.getTitle().contains("Warp: ")) {
            event.setCancelled(true);
            event.isCancelled();
            event.setResult(Event.Result.DENY);
            String block = inventory.getTitle().replace("Warp: ", "");
            if (clickedSlot == WARP_C_INDEX) {
                p.sendMessage("§7Du er warpet til §cC");
                p.teleport(vagtSpawnManager.getWarpInfo("vagtc").getLocation());


            } else if (clickedSlot == WARP_CVCELLESTUE_INDEX) {
                p.sendMessage("§7Du er warpet til §cC Vagt Celler");
                p.teleport(vagtSpawnManager.getWarpInfo("C2").getLocation());

            } else if (clickedSlot == WARP_BVCELLESTUE_INDEX) {
                p.sendMessage("§7Du er warpet til §bB Celle Stue");
                p.teleport(vagtSpawnManager.getWarpInfo("bcellestue").getLocation());
            } else if (clickedSlot == WARP_B_INDEX) {
                if (!p.hasPermission("vagtwarpb")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                p.sendMessage("§7Du er warpet til §bB");
                p.teleport(vagtSpawnManager.getWarpInfo("vagtb").getLocation());

            } else if (clickedSlot == WARP_A_INDEX) {
                if (!p.hasPermission("vagtwarpa")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                p.sendMessage("§7Du er warpet til §aA");
                p.teleport(vagtSpawnManager.getWarpInfo("a").getLocation());
                p.sendTitle("§4§lHusk! §c§log skifte gear!", " ");

            } else if (clickedSlot == WARP_AVAGTCELLER_INDEX) {
                if (!p.hasPermission("vagtwarpa")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                    return;
                }
                p.sendMessage("§7Du er warpet til §aA Vagt Celler");
                p.teleport(vagtSpawnManager.getWarpInfo("avagtceller").getLocation());
            } else {
                return;
            }
            changeInvOnWarp.changeInventory(p, block);
            InvHolder invHolder = changeInvOnWarp.getInventory(p.getUniqueId(), VagtUtils.getRegion(p.getLocation()));
            if (invHolder != null) {
                p.getInventory().setContents(invHolder.getInventory());
                p.getInventory().setArmorContents(invHolder.getGear());
                p.sendMessage("Du har skiftet gear");
                return;
            }
            p.getInventory().clear();
            p.getInventory().setHelmet(new ItemStack(Material.AIR));
            p.getInventory().setChestplate(new ItemStack(Material.AIR));
            p.getInventory().setLeggings(new ItemStack(Material.AIR));
            p.getInventory().setBoots(new ItemStack(Material.AIR));
            p.sendMessage("Dit inventory for denne region blev ikke fundet.");
            p.sendMessage("Du har ikke mistet dine ting");
            p.sendMessage("Hvis du warper er dine ting der stadig");
        }
    }
}
