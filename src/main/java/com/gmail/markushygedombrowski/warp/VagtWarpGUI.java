package com.gmail.markushygedombrowski.warp;


import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
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

    public VagtWarpGUI(VagtSpawnManager vagtSpawnManager) {
        this.vagtSpawnManager = vagtSpawnManager;
    }

    public void create(Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Warp");
        Wool woolRed = new Wool(DyeColor.RED);
        Wool woolBlue = new Wool(DyeColor.BLUE);
        Wool woolGreen = new Wool(DyeColor.GREEN);
        ItemStack warpC = woolRed.toItemStack(1);
        ItemStack warpB = woolBlue.toItemStack(1);
        ItemStack warpA = woolGreen.toItemStack(1);



        ItemMeta metaC = warpC.getItemMeta();
        ItemMeta metaB = warpB.getItemMeta();
        ItemMeta metaA = warpA.getItemMeta();

        metaC.setDisplayName("§c§lWarp C");
        metaB.setDisplayName("§b§lWarp B");
        metaA.setDisplayName("§a§lWarp A");

        warpC.setItemMeta(metaC);
        warpB.setItemMeta(metaB);
        warpA.setItemMeta(metaA);

        inventory.setItem(WARP_C_INDEX, warpC);
        inventory.setItem(WARP_B_INDEX, warpB);
        inventory.setItem(WARP_A_INDEX, warpA);
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

        if (inventory.getTitle().equalsIgnoreCase("Warp")) {
            if (clickedSlot == WARP_C_INDEX) {
                p.sendMessage("§7Du er warpet til §cC");
                p.teleport(vagtSpawnManager.getWarpInfo("vagtc").getLocation());

            }
            if (clickedSlot == WARP_B_INDEX) {
                if(!p.hasPermission("vagtwarpb")) {
                    p.sendMessage("§cDu har ikke højt nok rank");
                } else {
                    p.sendMessage("§7Du er warpet til §bB");
                    p.teleport(vagtSpawnManager.getWarpInfo("vagtb").getLocation());
                }


            }
            if (clickedSlot == WARP_A_INDEX) {
                if(!p.hasPermission("vagtwarpa")) {
                    p.sendMessage("§cDu har ikke højt nok rank");

                } else {
                    p.sendMessage("§7Du er warpet til §aA");
                    p.teleport(vagtSpawnManager.getWarpInfo("vagta").getLocation());
                    p.sendTitle("§4§lHusk! §c§log skifte gear!", " ");
                }


            }
            event.setCancelled(true);
            event.isCancelled();
            event.setResult(Event.Result.DENY);


        }
    }
}
