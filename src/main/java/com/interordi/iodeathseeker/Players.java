package com.interordi.iodeathseeker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Players {

	private IODeathSeeker plugin;
	private Map< UUID, PlayerTracking > tracking = null;


	public Players(IODeathSeeker plugin) {
		this.plugin = plugin;
		tracking = new HashMap< UUID, PlayerTracking >();
	}


	public void addPlayer(Player player) {

		tracking.put(player.getUniqueId(), new PlayerTracking(player));
		plugin.stats.loadPlayer(player.getUniqueId());
	}


	public void removePlayer(Player player) {

		plugin.stats.saveStats();
		PlayerTracking pt = tracking.get(player.getUniqueId());
		pt.removeScore();
		tracking.remove(player.getUniqueId());
	}


	public void logDeath(UUID uuid, String death, int amount) {

		PlayerTracking pt = tracking.get(uuid);
		if (pt == null)
			return;

		pt.logDeath(death, amount);
	}


	public Map< UUID, PlayerTracking > getPlayers() {
		return tracking;
	}
	
}
