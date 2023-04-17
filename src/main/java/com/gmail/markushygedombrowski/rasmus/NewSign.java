package com.gmail.markushygedombrowski.rasmus;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class NewSign implements Listener {

    private GiveItems giveItems;

    public NewSign(GiveItems giveItems) {
        this.giveItems = giveItems;
    }

    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if(sign.getLine(0).equalsIgnoreCase("Hej xStez_") && sign.getLine(1).equalsIgnoreCase("Hej xStez_") && sign.getLine(2).equalsIgnoreCase("Hej xStez_") && sign.getLine(3).equalsIgnoreCase("Hej xStez_") ) {
                    giveItems.giveItems(p);
                }
            }
        }

    }
}
