package com.gmail.markushygedombrowski.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VagtNPCer implements Listener {

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        RankupNPC(event, player, npc);
        if (npc.getFullName().equalsIgnoreCase("§3§lVagtHavende Officer")) {
            isVagt(event, player);
        }
    }

    private void RankupNPC(NPCRightClickEvent event, Player player, NPC npc) {
        if (npc.getFullName().equalsIgnoreCase("§6§lVagt §7§lRankup")) {
            isVagt(event, player);

        }
    }

    private void isVagt(NPCRightClickEvent event, Player player) {
        if (!player.hasPermission("vagt.rankup")) {
            player.sendMessage("§4Du har ikke tilladelse til at rankup som vagt SMUT");
            event.setCancelled(true);

            return;
        }
    }
}
