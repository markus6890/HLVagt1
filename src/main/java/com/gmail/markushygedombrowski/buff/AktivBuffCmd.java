package com.gmail.markushygedombrowski.buff;


import com.gmail.markushygedombrowski.model.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AktivBuffCmd implements CommandExecutor {
    private Settings settings;

    public AktivBuffCmd(Settings settings) {
        this.settings = settings;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            System.out.println("kun spillere kan bruge denne command");
            return true;
        }
        Player p = (Player) commandSender;
        if(!p.hasPermission("buff.admin")) {
            p.sendMessage("§cDu har ikke permission til at starte et event");
            return true;
        }
        if(settings.isAktivbuff()) {
            settings.setAktivbuff(false);
            p.sendMessage("§cDu har stoppet for at vagter kan tag buff");
            return true;
        }
        p.sendMessage("§aDu har startet for at vagter kan tag buff");
        settings.setAktivbuff(true);

        return true;
    }
}
