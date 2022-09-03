package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.commands.Dropcommand;
import com.gmail.markushygedombrowski.commands.FyrCommand;
import com.gmail.markushygedombrowski.commands.Rankupcommand;
import com.gmail.markushygedombrowski.commands.VagtChat;
import com.gmail.markushygedombrowski.listners.*;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.sign.*;
import com.gmail.markushygedombrowski.utils.Configreloadcommand;
import com.gmail.markushygedombrowski.utils.cooldown.Cooldown;
import com.gmail.markushygedombrowski.utils.Utils;
import com.gmail.markushygedombrowski.vagtMenu.MainMenu;
import com.gmail.markushygedombrowski.vagtMenu.VagtCommand;
import com.gmail.markushygedombrowski.vagtShop.VagtShop;
import com.gmail.markushygedombrowski.vagtShop.VagtShopEnchant;
import com.gmail.markushygedombrowski.warp.VagtWarpGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.PVGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Rankup;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Lon;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
import com.gmail.markushygedombrowski.warp.WarpCommand;
import com.gmail.markushygedombrowski.warp.WarpManager;
import com.gmail.markushygedombrowski.warp.Warpsign;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HLvagt extends JavaPlugin {
    public Economy econ = null;
    private Settings settings;
    private WarpManager warpManager;
    private PlayerProfiles playerProfiles;
    private Utils utils;
    private Lon lon;

    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        settings = new Settings();
        settings.load(config);
        utils = new Utils(this);
        System.out.println("HL Plugin enabled!!");

        initWarps();

        initVagt();

        initListener();


        Cooldown cooldown = new Cooldown(lon);
        Configreloadcommand configreloadcommand = new Configreloadcommand(this, settings);
        getCommand("hlreload").setExecutor(configreloadcommand);


        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                cooldown.handleCooldowns();
            }
        }, 1L, 1L);


    }


    private void initWarps() {
        warpManager = new WarpManager(this);
        warpManager.load();
        WarpCommand warpCommand = new WarpCommand(warpManager, this);
        getCommand("hlwarp").setExecutor(warpCommand);

        VagtWarpGUI vagtWarpGUI = new VagtWarpGUI(warpManager);
        Warpsign warpsign = new Warpsign(vagtWarpGUI);
        Bukkit.getPluginManager().registerEvents(vagtWarpGUI, this);
        Bukkit.getPluginManager().registerEvents(warpsign, this);


    }


    public void reload() {
        reloadConfig();
        FileConfiguration config = getConfig();
        settings.load(config);
        playerProfiles.load();

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void initVagt() {
        playerProfiles = new PlayerProfiles(this);
        playerProfiles.load();
        PVGUI pvgui = new PVGUI(this, playerProfiles);
        Bukkit.getPluginManager().registerEvents(pvgui, this);

        Rankup rankup = new Rankup(this, playerProfiles, settings);
        Bukkit.getPluginManager().registerEvents(rankup, this);

        TopVagterGUI topVagterGUI = new TopVagterGUI();
        Bukkit.getPluginManager().registerEvents(topVagterGUI, this);


        MainMenu mainMenu = new MainMenu(this, pvgui, topVagterGUI, playerProfiles, rankup);
        Bukkit.getPluginManager().registerEvents(mainMenu, this);

        VagtCommand vagtCommand = new VagtCommand(mainMenu);
        getCommand("vagt").setExecutor(vagtCommand);

        Rankupcommand rankupcommand = new Rankupcommand(settings, playerProfiles);
        getCommand("ansat").setExecutor(rankupcommand);

        VagtChat vagtChat = new VagtChat();
        getCommand("vc").setExecutor(vagtChat);

        FyrCommand fyrCommand = new FyrCommand(this);
        getCommand("fyr").setExecutor(fyrCommand);

        RepairGUI repairGUI = new RepairGUI(this);
        Bukkit.getPluginManager().registerEvents(repairGUI, this);



        VagtStav stav = new VagtStav();
        Bukkit.getPluginManager().registerEvents(stav, this);

        lon = new Lon(this, playerProfiles, settings);

        VagtShop vagtShop = new VagtShop(this);
        Bukkit.getPluginManager().registerEvents(vagtShop,this);

        VagtShopEnchant vagtShopEnchant = new VagtShopEnchant(this);
        Bukkit.getPluginManager().registerEvents(vagtShopEnchant,this);
        VagtSigns vagtSigns = new VagtSigns(warpManager, settings, this, repairGUI,vagtShop,vagtShopEnchant);
        Bukkit.getPluginManager().registerEvents(vagtSigns, this);
    }

    public void initListener() {
        OnJoin onJoin = new OnJoin(playerProfiles, this, settings);
        Bukkit.getPluginManager().registerEvents(onJoin, this);

        DamageListener damageListener = new DamageListener(settings, warpManager, playerProfiles);
        Bukkit.getPluginManager().registerEvents(damageListener, this);

        BreakBlockListener breakBlockListener = new BreakBlockListener();
        Bukkit.getPluginManager().registerEvents(breakBlockListener, this);

        DropItemListener dropItemListener = new DropItemListener();
        Bukkit.getPluginManager().registerEvents(dropItemListener, this);

        Dropcommand dropcommand = new Dropcommand(dropItemListener);
        getCommand("drop").setExecutor(dropcommand);
    }


    public void onDisable() {

    }
}
