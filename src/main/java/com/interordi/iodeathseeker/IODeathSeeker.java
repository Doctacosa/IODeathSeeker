package com.interordi.iodeathseeker;

import com.interordi.iodeathseeker.listeners.LoginListener;
import com.interordi.iodeathseeker.listeners.PlayerDeathListener;
import com.interordi.iodeathseeker.utilities.Scores;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;


public class IODeathSeeker extends JavaPlugin {

	public LoginListener thisLoginListener;
	public PlayerDeathListener thisPlayerDeathListener;
	public Players players;
	public Stats stats;
	private boolean ignoreMobs = true;
	private Scores thisScores;
	
	
	public void onEnable() {
		//Always ensure we've got a copy of the config in place (does not overwrite existing)
		this.saveDefaultConfig();

		if (this.getConfig().contains("ignore-mobs"))
			ignoreMobs = this.getConfig().getBoolean("ignore-mobs");

		thisScores = new Scores("Unique Deaths");
		Map< UUID, Integer > dummyScores = new HashMap< UUID, Integer>();
		thisScores.loadScores(dummyScores);
			
		thisLoginListener = new LoginListener(this);
		thisPlayerDeathListener = new PlayerDeathListener(this, ignoreMobs);
		stats = new Stats(this);
		players = new Players(this);
		
		//Save the config every minute
		getServer().getScheduler().scheduleSyncRepeatingTask(this, stats, 60*20L, 60*20L);
		
		//Enable metrics
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this, 27721);

		getLogger().info("IODeathSeeker enabled");
	}
	
	
	public void onDisable() {
		getLogger().info("IODeathSeeker disabled");
	}
	

	public Scores getScores() {
		return thisScores;
	}
}
