package com.interordi.iodeathseeker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerTracking {
	
	private static IODeathSeeker plugin;

	private Player player;
	private UUID uuid;
	private Map< String, Integer > deaths;
	
	static Map< UUID, Integer > scores = new HashMap< UUID, Integer>();

	
	PlayerTracking(Player player) {
		this.player = player;
		this.deaths = new HashMap< String, Integer>();

		updateScore();
	}
	
	
	public UUID getUuid() {
		return this.uuid;
	}
	
	
	public Map< String, Integer > getDeaths() {
		return this.deaths;
	}
	
	
	public int getNbUniqueDeaths() {
		return deaths.size();
	}
	
	
	public void logDeath(String death) {
		logDeath(death, 1);
	}


	public void logDeath(String death, Integer count) {
		death = death.toLowerCase();
		if (this.deaths.containsKey(death)) {
			Integer deathCount = this.deaths.get(death);
			this.deaths.put(death, deathCount + count);
		} else {
			this.deaths.put(death, count);
		}

		updateScore();
	}


	//Initialize the scoreboard
	public static void initScore(IODeathSeeker plugin) {
		PlayerTracking.plugin = plugin;
		scores = plugin.stats.loadStats();
		plugin.getScores().loadScores(scores);
	}
	
	
	//Update a player's score on the global display
	public void updateScore() {
		plugin.getScores().updateScore(player, getNbUniqueDeaths());
	}
	
	
	//Remove a player's score from the display
	public void removeScore() {
		plugin.getScores().refreshDisplay();
	}
}
