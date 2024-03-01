package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.buff.AktivBuffCmd;
import com.gmail.markushygedombrowski.buff.BuffGui;
import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.commands.*;
import com.gmail.markushygedombrowski.npc.VagtNPCer;
import com.gmail.markushygedombrowski.npc.vagthavende.DeliverGearGUI;
import com.gmail.markushygedombrowski.npc.vagthavende.VagthavendeOfficer;
import com.gmail.markushygedombrowski.rankup.RankupLoader;
import com.gmail.markushygedombrowski.settings.config.ConfigManager;
import com.gmail.markushygedombrowski.settings.config.VagtFangePvpConfigManager;
import com.gmail.markushygedombrowski.listners.*;
import com.gmail.markushygedombrowski.settings.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.settings.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.settings.Settings;
import com.gmail.markushygedombrowski.settings.Sql;
import com.gmail.markushygedombrowski.settings.SqlSettings;
import com.gmail.markushygedombrowski.settings.deliveredItems.DeliveredItemsLoader;
import com.gmail.markushygedombrowski.sign.*;
import com.gmail.markushygedombrowski.settings.config.Reconfigurations;
import com.gmail.markushygedombrowski.utils.Logger;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.vagtMenu.MainMenu;
import com.gmail.markushygedombrowski.vagtMenu.VagtCommand;

import com.gmail.markushygedombrowski.vagtMenu.subMenu.*;
import com.gmail.markushygedombrowski.warp.VagtWarpGUI;
import com.gmail.markushygedombrowski.vagtMenu.subMenu.topVagter.TopVagterGUI;
import com.gmail.markushygedombrowski.warp.VagtSpawnCommand;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import com.gmail.markushygedombrowski.warp.Warpsign;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

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
    private Sql sql;
    private DeliveredItemsLoader deliveredItemsLoader;

    public void onEnable() {

        combatList = CombatMain.getInstance().getCombatList();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        loadSQL(config);
        loadConfigManager();

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings(config);
        vagtFangePvpConfigManager = new VagtFangePvpConfigManager();
        vagtFangePvpConfigManager.load(configM.getVagtFangePvpcfg());
        VagtUtils vagtUtils = new VagtUtils(this, playerProfiles, settings);
        System.out.println("HL Vagt enabled!!");
        initWarps();

        VagtLevelAdminCommands vagtLevelAdminCommands = new VagtLevelAdminCommands(playerProfiles);
        getCommand("vresetall").setExecutor(vagtLevelAdminCommands);
        initVagt();

        initListener();

        ItemProfileLoader itemProfileLoader = new ItemProfileLoader();
        itemProfileLoader.load(configM.getDeliveredItemsCfg());

        DeliverGearGUI deliverGearGUI = new DeliverGearGUI(itemProfileLoader, playerProfiles);
        Bukkit.getPluginManager().registerEvents(deliverGearGUI, this);

        GuiTestCommand guiTestCommand = new GuiTestCommand(deliverGearGUI);
        getCommand("guitest").setExecutor(guiTestCommand);


        VagthavendeOfficer vagthavendeOfficer = new VagthavendeOfficer(deliverGearGUI, itemProfileLoader, playerProfiles);
        Bukkit.getPluginManager().registerEvents(vagthavendeOfficer, this);

        VagtNPCer vagtNPCer = new VagtNPCer(vagthavendeOfficer);
        Bukkit.getPluginManager().registerEvents(vagtNPCer, this);
        Bukkit.getPluginManager().registerEvents(deliverGearGUI, this);

        VagtCooldown vagtCooldown = new VagtCooldown(lon);
        Reconfigurations reconfigurations = new Reconfigurations(this, settings);
        getCommand("hlvagtreload").setExecutor(reconfigurations);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                vagtCooldown.handleCooldowns();
            }
        }, 1L, 1L);


    }

    private void settings(FileConfiguration config) {
        settings = new Settings();
        settings.load(config);
        deliveredItemsLoader = new DeliveredItemsLoader(sql);

        playerProfiles = new PlayerProfiles(settings, sql, deliveredItemsLoader);

        try {
            playerProfiles.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSQL(FileConfiguration config) {
        SqlSettings sqlSettings = new SqlSettings();
        sqlSettings.load(config);
        sql = new Sql(sqlSettings);
    }


    public void initWarps() {
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
        playerProfiles = new PlayerProfiles(settings, sql, deliveredItemsLoader);
        try {
            playerProfiles.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        vagtFangePvpConfigManager.load(configM.getVagtFangePvpcfg());
        settings.load(config);


    }

    public void loadConfigManager() {
        logger = new Logger(this);
        logger.setup();
        configM = new ConfigManager();
        configM.setup();
        configM.saveVagtFangePvp();
        configM.saveDeliveredItems();
        configM.saveRankup();


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

        TopVagterGUI topVagterGUI = new TopVagterGUI(playerProfiles);
        Bukkit.getPluginManager().registerEvents(topVagterGUI, this);

        StatsGUI statsGUI = new StatsGUI(playerProfiles);
        Bukkit.getPluginManager().registerEvents(statsGUI, this);
        RankupLoader rankupLoader = new RankupLoader();
        rankupLoader.load(configM.getRankupCfg());

        RankupGUI rankupGUI = new RankupGUI(playerProfiles, econ, rankupLoader);
        Bukkit.getPluginManager().registerEvents(rankupGUI, this);

        MainMenu mainMenu = new MainMenu(this, pvgui, topVagterGUI, playerProfiles, rankup, statsGUI, rankupGUI);
        Bukkit.getPluginManager().registerEvents(mainMenu, this);

        VagtCommand vagtCommand = new VagtCommand(mainMenu, playerProfiles);
        getCommand("vagt").setExecutor(vagtCommand);

        Rankupcommand rankupcommand = new Rankupcommand(settings, playerProfiles);
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


        BuffGui buffGui = new BuffGui(settings, this);
        Bukkit.getPluginManager().registerEvents(buffGui, this);
        VagtSigns vagtSigns = new VagtSigns(vagtSpawnManager, settings, this, repairGUI, buffGui, logger);
        Bukkit.getPluginManager().registerEvents(vagtSigns, this);

        AktivBuffCmd aktivBuffCmd = new AktivBuffCmd(settings);
        getCommand("aktivbuff").setExecutor(aktivBuffCmd);
    }

    public void initListener() {
        OnJoin onJoin = new OnJoin(playerProfiles, settings);
        Bukkit.getPluginManager().registerEvents(onJoin, this);

        DamageListener damageListener = new DamageListener(settings, vagtSpawnManager, playerProfiles, this, combatList, vagtFangePvpConfigManager, logger);
        Bukkit.getPluginManager().registerEvents(damageListener, this);


        DropItemListener dropItemListener = new DropItemListener();
        Bukkit.getPluginManager().registerEvents(dropItemListener, this);

        Dropcommand dropcommand = new Dropcommand(dropItemListener);
        getCommand("drop").setExecutor(dropcommand);
    }


    public void onDisable() {
        playerProfiles.saveAll();
    }
}
