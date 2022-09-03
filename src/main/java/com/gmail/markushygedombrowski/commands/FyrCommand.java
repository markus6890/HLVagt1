package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.model.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FyrCommand implements CommandExecutor {


    private HLvagt plugin;

    public FyrCommand(HLvagt plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!(sender instanceof Player)) {
            System.out.println("denne command er kun for spillere");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("direktør")) {
            p.sendMessage("§cDet har du ikke Permission til!!");
            return true;
        }
        if(args.length == 0) {
            p.sendMessage("/fyr <Player>");

            return true;
        }
        Player fyr = Bukkit.getPlayer(args[0]);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + fyr.getName() + " parent remove c-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + fyr.getName() + " parent remove b-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + fyr.getName() + " parent remove a-vagt prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + fyr.getName() + " parent remove officer prison");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + fyr.getName() + " parent add c-fange prison");

        fyr.getInventory().clear();
        fyr.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
        fyr.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
        fyr.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
        fyr.getInventory().setBoots(new ItemStack(Material.AIR, 1));
        fyr.setHealth(0);
        double money = plugin.econ.getBalance(fyr);
        plugin.econ.withdrawPlayer(fyr,money);
        plugin.econ.depositPlayer(fyr, 400);
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
        Bukkit.broadcastMessage("§c§lVagten §6" + fyr.getName());
        Bukkit.broadcastMessage("§7Er lige blevet fyret");
        Bukkit.broadcastMessage("§7Af §4" + p.getName());
        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");




        return true;
    }
}
