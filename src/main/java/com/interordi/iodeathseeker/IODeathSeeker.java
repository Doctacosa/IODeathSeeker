package com.interordi.iodeathseeker;

import com.interordi.iodeathseeker.listeners.LoginListener;
import com.interordi.iodeathseeker.utilities.Scores;

import org.bukkit.plugin.java.JavaPlugin;


public class IODeathSeeker extends JavaPlugin {

	public LoginListener thisLoginListener;
	public Players players;
	public Stats stats;
	private boolean ignoreMobs = true;
	private Scores thisScores;
	
	
	public void onEnable() {
		thisScores = new Scores("Score");

		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();

		if (this.getConfig().contains("ignore-mobs"))
			ignoreMobs = this.getConfig().getBoolean("ignore-mobs");
		
		thisLoginListener = new LoginListener(this);
		stats = new Stats(this);
		players = new Players(this);
		
		//Init players data
		PlayerTracking.initScore(this);

		//Save the config every minute
		getServer().getScheduler().scheduleSyncRepeatingTask(this, stats, 60*20L, 60*20L);
		
		getLogger().info("IODeathSeeker enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IODeathSeeker disabled");
	}
	

	public Scores getScores() {
		return thisScores;
	}

}
