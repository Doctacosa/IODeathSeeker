package com.interordi.iodeathseeker;

import com.interordi.iodeathseeker.listeners.LoginListener;
import com.interordi.iodeathseeker.listeners.PlayerDeathListener;

import org.bukkit.plugin.java.JavaPlugin;


public class IODeathSeeker extends JavaPlugin {

	public LoginListener thisLoginListener;
	public PlayerDeathListener thisPlayerDeathListener;
	public Players players;
	public Stats stats;
	
	
	public void onEnable() {
		thisLoginListener = new LoginListener(this);
		thisPlayerDeathListener = new PlayerDeathListener(this);
		stats = new Stats(this);
		players = new Players(this);
		
		//Read the current stats
		this.stats.loadStats();

		//Save the config every minute
		getServer().getScheduler().scheduleSyncRepeatingTask(this, stats, 60*20L, 60*20L);
		
		getLogger().info("IODeathSeeker enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IODeathSeeker disabled");
	}
	
}
