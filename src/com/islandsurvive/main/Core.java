package com.islandsurvive.main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.islandsurvive.commands.Island;
import com.islandsurvive.commands.Profile;
import com.islandsurvive.commands.Refer;
import com.islandsurvive.commands.Upgrade;
import com.islandsurvive.events.BlocksDestroyed;
import com.islandsurvive.events.ColoredNames;
import com.islandsurvive.events.FirstJoin;
import com.islandsurvive.events.GoldPlateTrigger;
import com.islandsurvive.events.ProfileEvents;
import com.islandsurvive.events.BlockProtection;
import com.islandsurvive.events.cooldowns.CoalOreBreakCD;
import com.islandsurvive.events.cooldowns.DiamondOreBreakCD;
import com.islandsurvive.events.cooldowns.EmeraldOreBreakCD;
import com.islandsurvive.events.cooldowns.GoldOreBreakCD;
import com.islandsurvive.events.cooldowns.IronOreBreakCD;
import com.islandsurvive.events.cooldowns.LapisOreBreakCD;
import com.islandsurvive.events.cooldowns.QuartzOreBreakCD;
import com.islandsurvive.events.cooldowns.RedstoneOreBreakCD;

import net.milkbowl.vault.economy.Economy;


public class Core extends JavaPlugin {
	
	public static Economy eco = null;
	
	public void onEnable() {
	PluginDescriptionFile pdfFile = getDescription();
	Logger logger = getLogger();

	registerCommands();
	registerConfig();
	registerEvents();
	
	if(!setupEconomy()){
	Bukkit.shutdown();
	
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();
	
	Team admin = board.registerNewTeam("Admin");
	
	admin.setPrefix(C.yellow + "*" + C.red);
	}

	logger.info(pdfFile.getName() + "Has been enabled - Version " + pdfFile.getVersion());	
}

public void onDisable() {
	PluginDescriptionFile pdfFile = getDescription();
	Logger logger = getLogger();

	logger.info(pdfFile.getName() + "Has been disabled - Version " + pdfFile.getVersion());
}

public void registerCommands() {
	getCommand("profile").setExecutor(new Profile(this));
	getCommand("refer").setExecutor(new Refer(this));
	getCommand("upgrade").setExecutor(new Upgrade(this));
	getCommand("island").setExecutor(new Island(this));
}

public void registerEvents() {
	PluginManager pm = getServer().getPluginManager();
	
	//pm.registerEvents(new BlocksDestroyed(this), this);
	pm.registerEvents(new FirstJoin(this), this);
	pm.registerEvents(new ProfileEvents(), this);
	pm.registerEvents(new CoalOreBreakCD(this), this);
	pm.registerEvents(new IronOreBreakCD(this), this);
	pm.registerEvents(new GoldOreBreakCD(this), this);
	pm.registerEvents(new DiamondOreBreakCD(this), this);
	pm.registerEvents(new EmeraldOreBreakCD(this), this);
	pm.registerEvents(new LapisOreBreakCD(this), this);
	pm.registerEvents(new RedstoneOreBreakCD(this), this);
	pm.registerEvents(new QuartzOreBreakCD(this), this);
	pm.registerEvents(new GoldPlateTrigger(this), this);
	pm.registerEvents(new ColoredNames(), this);
	pm.registerEvents(new BlockProtection(this), this);
}

public void registerConfig() {
	getConfig().options().copyDefaults(true);
	saveDefaultConfig();
	}

private boolean setupEconomy(){
    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
    if (economyProvider != null) {
        eco = economyProvider.getProvider();
    }

    return (eco != null);
    }
}

