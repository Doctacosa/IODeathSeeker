package com.interordi.iodeathseeker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerTracking {
	
	private IODeathSeeker plugin;
	private Player player;
	private UUID uuid;
	private Map< String, Integer > deaths;
	
	static Map< String, Integer > scores = new HashMap< String, Integer>();

	
	PlayerTracking(Player player, IODeathSeeker plugin) {
		this.player = player;
		this.plugin = plugin;
		this.deaths = new HashMap< String, Integer>();

		//Init at 0, loading happens separately
		plugin.getScores().updateScore(player, 0);
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
			count += this.deaths.get(death);
			this.deaths.put(death, count);
		} else {
			this.deaths.put(death, count);
		}

		plugin.getScores().updateScore(player, this.deaths.size());
	}
}
