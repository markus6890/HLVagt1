package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.buff.AktivBuffCmd;
import com.gmail.markushygedombrowski.buff.BuffGui;
import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.commands.*;
import com.gmail.markushygedombrowski.deliveredItems.ItemProfileLoader;
import com.gmail.markushygedombrowski.npc.VagtNPCer;
import com.gmail.markushygedombrowski.npc.vagthavende.DeliverGearGUI;
import com.gmail.markushygedombrowski.npc.vagthavende.VagthavendeOfficer;
import com.gmail.markushygedombrowski.playerProfiles.PlayerProfiles;
import com.gmail.markushygedombrowski.rankup.RankupLoader;

import com.gmail.markushygedombrowski.listners.*;

import com.gmail.markushygedombrowski.settings.ConfigManager;
import com.gmail.markushygedombrowski.settings.Settings;
import com.gmail.markushygedombrowski.settings.VagtFangePvpConfigManager;
import com.gmail.markushygedombrowski.sign.*;

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
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HLvagt extends JavaPlugin {
    public Economy econ = null;
    private VagtSpawnManager vagtSpawnManager;
    private Lon lon;
    private Logger logger;
    private CombatList combatList;
    private ConfigManager configM;
    private PlayerProfiles playerProfiles;
    private Settings settings;
    private VagtFangePvpConfigManager vagtFangePvpConfigManager;
    private ItemProfileLoader itemProfileLoader;
    private VagtProfiler vagtProfiler;
    private static HLvagt instance;
    private LuckPerms luckPerms;


    public void onEnable() {
        instance = this;
        vagtProfiler = VagtProfiler.getInstance();


        combatList = CombatMain.getInstance().getCombatList();
        playerProfiles = vagtProfiler.getPlayerProfiles();
        settings = vagtProfiler.getSettings();
        configM = vagtProfiler.getConfigManager();
        itemProfileLoader = vagtProfiler.getItemProfileLoader();
        vagtFangePvpConfigManager = vagtProfiler.getVagtFangePvpConfigManager();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        loadConfigManager();

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms.class).getProvider();
        }


        VagtUtils vagtUtils = new VagtUtils(this, playerProfiles, settings);
        System.out.println("HL Vagt enabled!!");
        initWarps();

        VagtLevelAdminCommands vagtLevelAdminCommands = new VagtLevelAdminCommands(playerProfiles);
        getCommand("vresetall").setExecutor(vagtLevelAdminCommands);
        initVagt();

        initListener();

        DeliverGearGUI deliverGearGUI = new DeliverGearGUI(itemProfileLoader, playerProfiles);
        Bukkit.getPluginManager().registerEvents(deliverGearGUI, this);

        GuiTestCommand guiTestCommand = new GuiTestCommand(deliverGearGUI);
        getCommand("guitest").setExecutor(guiTestCommand);


        VagthavendeOfficer vagthavendeOfficer = new VagthavendeOfficer(deliverGearGUI, itemProfileLoader, playerProfiles);
        Bukkit.getPluginManager().registerEvents(vagthavendeOfficer, this);

        VagtNPCer vagtNPCer = new VagtNPCer(vagthavendeOfficer);
        Bukkit.getPluginManager().registerEvents(vagtNPCer, this);


        VagtCooldown vagtCooldown = new VagtCooldown(lon);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                vagtCooldown.handleCooldowns();
            }
        }, 1L, 1L);


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



    public void loadConfigManager() {
        logger = new Logger(this);
        logger.setup();
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



        TopVagterGUI topVagterGUI = new TopVagterGUI(playerProfiles);
        Bukkit.getPluginManager().registerEvents(topVagterGUI, this);

        StatsGUI statsGUI = new StatsGUI(playerProfiles);
        Bukkit.getPluginManager().registerEvents(statsGUI, this);
        RankupLoader rankupLoader = new RankupLoader();
        rankupLoader.load(configM.getRankupCfg());

        RankupGUI rankupGUI = new RankupGUI(playerProfiles, econ, rankupLoader);
        Bukkit.getPluginManager().registerEvents(rankupGUI, this);

        VagtLevelGUI vagtLevelGUI = new VagtLevelGUI(playerProfiles);
        Bukkit.getPluginManager().registerEvents(vagtLevelGUI, this);

        MainMenu mainMenu = new MainMenu(this, pvgui, topVagterGUI, playerProfiles, statsGUI, rankupGUI, vagtLevelGUI);
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

        DamageListener damageListener = new DamageListener(settings, vagtSpawnManager, playerProfiles, this, combatList, vagtFangePvpConfigManager, logger, luckPerms);
        Bukkit.getPluginManager().registerEvents(damageListener, this);


        DropItemListener dropItemListener = new DropItemListener();
        Bukkit.getPluginManager().registerEvents(dropItemListener, this);

        Dropcommand dropcommand = new Dropcommand(dropItemListener);
        getCommand("drop").setExecutor(dropcommand);

        RegionEnterlistener regionEnterlistener = new RegionEnterlistener(logger);
        Bukkit.getPluginManager().registerEvents(regionEnterlistener, this);
    }


    public static HLvagt getInstance() {
        return instance;
    }


    public void onDisable() {

    }
}
