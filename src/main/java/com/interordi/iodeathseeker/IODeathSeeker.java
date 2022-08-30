package com.interordi.iodeathseeker;

import com.interordi.iodeathseeker.listeners.LoginListener;
import com.interordi.iodeathseeker.listeners.PlayerDeathListener;

import org.bukkit.plugin.java.JavaPlugin;


public class IODeathSeeker extends JavaPlugin {

	public LoginListener thisLoginListener;
	public PlayerDeathListener thisPlayerDeathListener;
	public Players players;
	public Stats stats;
	private boolean ignoreMobs = true;
	
	
	public void onEnable() {
		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();

		if (this.getConfig().contains("ignore-mobs"))
			ignoreMobs = this.getConfig().getBoolean("ignore-mobs");
		
		thisLoginListener = new LoginListener(this);
		thisPlayerDeathListener = new PlayerDeathListener(this, ignoreMobs);
		stats = new Stats(this);
		players = new Players(this);
		
		//Init players data
		PlayerTracking.initScore();

		//Save the config every minute
		getServer().getScheduler().scheduleSyncRepeatingTask(this, stats, 60*20L, 60*20L);
		
		getLogger().info("IODeathSeeker enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IODeathSeeker disabled");
	}
	
}
