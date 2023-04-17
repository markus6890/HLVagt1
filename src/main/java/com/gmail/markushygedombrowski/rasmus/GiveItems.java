package com.gmail.markushygedombrowski.rasmus;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;



public class GiveItems {
    private HLvagt plugin;

    public GiveItems(HLvagt plugin) {
        this.plugin = plugin;
    }

    public void giveItems(Player p) {

        if (VagtUtils.procent(40)) {
            p.getInventory().addItem(new ItemStack(Material.BREAD, 16));

        }
        if(VagtUtils.procent(40)) {
            plugin.econ.depositPlayer(p,10000);
        }
        if (VagtUtils.procent(10)) {
            p.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 1));
            return;
        }
        if (VagtUtils.procent(5)) {
            p.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, 4));
            return;
        }



    }

}
