package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.buff.BuffGui;
import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.combat.HotBarMessage;
import com.gmail.markushygedombrowski.commands.Dropcommand;
import com.gmail.markushygedombrowski.commands.FyrCommand;
import com.gmail.markushygedombrowski.commands.Rankupcommand;
import com.gmail.markushygedombrowski.commands.VagtChat;
import com.gmail.markushygedombrowski.config.ConfigManager;
import com.gmail.markushygedombrowski.config.VagtFangePvpConfigManager;
import com.gmail.markushygedombrowski.listners.*;
import com.gmail.markushygedombrowski.model.PlayerProfiles;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.sign.*;
import com.gmail.markushygedombrowski.config.Reconfigurations;
import com.gmail.markushygedombrowski.utils.Logger;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.vagtMenu.MainMenu;
import com.gmail.markushygedombrowski.vagtMenu.VagtCommand;
import com.gmail.markushygedombrowski.vagtShop.VagtShop;
import com.gmail.markushygedombrowski.vagtShop.VagtShopEnchant;
import com.gmail.markushygedombrowski.warp.VagtWarpGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.PVGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Rankup;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.Lon;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
import com.gmail.markushygedombrowski.warp.VagtSpawnCommand;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import com.gmail.markushygedombrowski.warp.Warpsign;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HLvagt extends JavaPlugin {
    public Economy econ = null;
    private Settings settings;
    private VagtSpawnManager vagtSpawnManager;
    private PlayerProfiles playerProfiles;
    private Lon lon;
    private ConfigManager configM;
    private Logger logger;
    private CombatList combatList;
    private VagtFangePvpConfigManager vagtFangePvpConfigManager;
    public void onEnable() {
        HotBarMessage hotBarMessage = new HotBarMessage();
        combatList = new CombatList(this,hotBarMessage);
        saveDefaultConfig();
        loadConfigManager();
        FileConfiguration config = getConfig();
        vagtFangePvpConfigManager = new VagtFangePvpConfigManager();
        vagtFangePvpConfigManager.load(configM.vagtFangePvpcfg);
        settings = new Settings();
        settings.load(config);
        playerProfiles = new PlayerProfiles(settings, configM);
        playerProfiles.load();
        VagtUtils vagtUtils = new VagtUtils(this,playerProfiles,settings);

        System.out.println("HL Plugin enabled!!");
        initWarps();

        initVagt();

        initListener();


        VagtCooldown vagtCooldown = new VagtCooldown(lon);
        Reconfigurations reconfigurations = new Reconfigurations(this, settings);
        getCommand("hlvagtreload").setExecutor(reconfigurations);


        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                vagtCooldown.handleCooldowns();
            }
        }, 1L, 1L);



    }


    private void initWarps() {
        vagtSpawnManager = new VagtSpawnManager(this);
        vagtSpawnManager.load();
        VagtSpawnCommand vagtSpawnCommand = new VagtSpawnCommand(vagtSpawnManager, this);
        getCommand("vagtspawn").setExecutor(vagtSpawnCommand);

        VagtWarpGUI vagtWarpGUI = new VagtWarpGUI(vagtSpawnManager);
        Warpsign warpsign = new Warpsign(vagtWarpGUI);
        Bukkit.getPluginManager().registerEvents(vagtWarpGUI, this);
        Bukkit.getPluginManager().registerEvents(warpsign, this);


    }


    public void reload() {
        reloadConfig();
        FileConfiguration config = getConfig();
        loadConfigManager();
        playerProfiles.load();
        vagtFangePvpConfigManager.load(configM.getVagtFangePvpcfg());
        settings.load(config);

    }
    public void loadConfigManager() {
        logger = new Logger(this);
        logger.setup();
        configM = new ConfigManager();
        configM.setup();
        configM.reloadVagtFangePvp();
        configM.reloadPlayer();
        configM.saveVagtFangePvp();
        configM.savePlayer();


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

        PVGUI pvgui = new PVGUI(this, playerProfiles);
        Bukkit.getPluginManager().registerEvents(pvgui, this);

        Rankup rankup = new Rankup(this, playerProfiles, settings, vagtSpawnManager);
        Bukkit.getPluginManager().registerEvents(rankup, this);

        TopVagterGUI topVagterGUI = new TopVagterGUI();
        Bukkit.getPluginManager().registerEvents(topVagterGUI, this);

        MainMenu mainMenu = new MainMenu(this, pvgui, topVagterGUI, playerProfiles, rankup);
        Bukkit.getPluginManager().registerEvents(mainMenu, this);

        VagtCommand vagtCommand = new VagtCommand(mainMenu);
        getCommand("vagt").setExecutor(vagtCommand);

        Rankupcommand rankupcommand = new Rankupcommand(settings, playerProfiles,vagtSpawnManager);
        getCommand("ansat").setExecutor(rankupcommand);

        VagtChat vagtChat = new VagtChat();
        getCommand("vc").setExecutor(vagtChat);

        FyrCommand fyrCommand = new FyrCommand(this, playerProfiles);
        getCommand("fyr").setExecutor(fyrCommand);

        RepairGUI repairGUI = new RepairGUI(this, logger);
        Bukkit.getPluginManager().registerEvents(repairGUI, this);



        VagtStav stav = new VagtStav();
        Bukkit.getPluginManager().registerEvents(stav, this);

        lon = new Lon(this, playerProfiles, settings);

        VagtShop vagtShop = new VagtShop(this);
        Bukkit.getPluginManager().registerEvents(vagtShop,this);

        VagtShopEnchant vagtShopEnchant = new VagtShopEnchant(this);
        Bukkit.getPluginManager().registerEvents(vagtShopEnchant,this);

        BuffGui buffGui = new BuffGui(settings,this);
        Bukkit.getPluginManager().registerEvents(buffGui,this);
        VagtSigns vagtSigns = new VagtSigns(vagtSpawnManager, settings, this, repairGUI,vagtShop,vagtShopEnchant,buffGui, logger);
        Bukkit.getPluginManager().registerEvents(vagtSigns, this);
    }

    public void initListener() {
        OnJoin onJoin = new OnJoin(playerProfiles, settings);
        Bukkit.getPluginManager().registerEvents(onJoin, this);

        DamageListener damageListener = new DamageListener(settings, vagtSpawnManager, playerProfiles,this, combatList, vagtFangePvpConfigManager);
        Bukkit.getPluginManager().registerEvents(damageListener, this);


        DropItemListener dropItemListener = new DropItemListener();
        Bukkit.getPluginManager().registerEvents(dropItemListener, this);

        Dropcommand dropcommand = new Dropcommand(dropItemListener);
        getCommand("drop").setExecutor(dropcommand);
    }




    public void onDisable() {

    }
}
