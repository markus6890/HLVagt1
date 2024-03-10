package com.gmail.markushygedombrowski.listners;

import com.gmail.markushygedombrowski.utils.Logger;
import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionEnterlistener implements Listener {

    private Logger logger;

    public RegionEnterlistener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onRegionEnter(RegionEnterEvent event) {
        Player p = event.getPlayer();

        if(p.hasPermission("vagt.fceller")) {
           if(event.getRegion().getId().contains("a-") || event.getRegion().getId().contains("b-") || event.getRegion().getId().contains("c-")) {
               p.sendMessage("§4Du må ikke gå ind i celler som vagt!");
               p.sendMessage("§4Dette er blevet logget!");
               logger.formatMessage(p.getName() + " har forsøgt og gå ind i en celle!","VAGTCELLER","vagtcellelog");
               logger.formatMessage("CELLE: " + event.getRegion().getId(),"VAGTCELLER","vagtcellelog");
               logger.formatMessage("WORLD: " + p.getWorld().getName(),"VAGTCELLER","vagtcellelog");


           }
        }

    }
}
