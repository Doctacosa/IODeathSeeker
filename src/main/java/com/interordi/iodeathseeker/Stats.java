package com.interordi.iodeathseeker;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Stats implements Runnable {
	
	IODeathSeeker plugin;
	private boolean saving = false;
	private String pluginPath = "plugins/IODeathSeeker/";
	private String statsPath = "plugins/IODeathSeeker/stats.yml";
	
	
	Stats(IODeathSeeker plugin) {
		this.plugin = plugin;
	}

	
	@Override
	public void run() {
		saveStats();
	}
	
	
	public void loadStats() {
		File statsFile = new File(this.statsPath);

		try {
			if (!statsFile.exists()) {
				File pluginDir = new File(this.pluginPath);
				pluginDir.mkdirs();
				statsFile.createNewFile();
			}
		} catch (IOException e) {
			System.err.println("Failed to load the stats file");
			e.printStackTrace();
			return;
		}

		FileConfiguration statsAccess = YamlConfiguration.loadConfiguration(statsFile);
		
		ConfigurationSection deathsData = statsAccess.getConfigurationSection("deaths");
		if (deathsData == null)
			return;	//Nothing yet, exit
		Set< String > cs = deathsData.getKeys(false);
		if (cs == null)
			return;	//No players found, exit
		
		
		//Loop on each player
		for (String temp : cs) {
			UUID uuid = UUID.fromString(temp);
			ConfigurationSection playerData = deathsData.getConfigurationSection(uuid.toString());
			
			Set< String > rs = playerData.getKeys(false);
			
			//Loop on each death for this player
			for (String deathMessage : rs) {
				Integer nbDeaths = playerData.getInt(deathMessage);
				this.plugin.players.logDeath(uuid, deathMessage, nbDeaths);
			}
		}
	}
	
	
	public void loadPlayer(UUID uuid) {
		//Do in a separate thread, player loading with large stats files is costly
		Bukkit.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
			@Override
			public void run() {
				File statsFile = new File(statsPath);
				FileConfiguration statsAccess = YamlConfiguration.loadConfiguration(statsFile);
				
				ConfigurationSection playerData = statsAccess.getConfigurationSection("deaths." + uuid);
				if (playerData == null)
					return;	//No player found, exit
				
				Set< String > rs = playerData.getKeys(false);
				
				//Loop on each death for this player
				for (String deathMessage : rs) {
					Integer nbDeaths = playerData.getInt(deathMessage);
					plugin.players.logDeath(uuid, deathMessage, nbDeaths);
				}
				
			}
		});
	}
	
	
	public void saveStats() {
		//No need to save if we're already saving
		if (saving)
			return;

		saving = true;

		Map< UUID, PlayerTracking > players = this.plugin.players.getPlayers();
		Map< UUID, PlayerTracking > playersCopy = new HashMap< UUID, PlayerTracking >();
		playersCopy.putAll(players);
		
		//Run on its own thread to avoid holding up the server
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			File statsFile = new File(this.statsPath);
			FileConfiguration statsAccess = YamlConfiguration.loadConfiguration(statsFile);

			for (Map.Entry< UUID , PlayerTracking > entry : playersCopy.entrySet()) {
				UUID uuid = entry.getKey();
				PlayerTracking tracking = entry.getValue();
				
				Map< String, Integer > deaths = new HashMap< String, Integer>();
				deaths.putAll(tracking.getDeaths());

				for (Map.Entry< String , Integer > deathEntry : deaths.entrySet()) {
					String deathMessage = deathEntry.getKey();
					Integer nbDeaths = deathEntry.getValue();
					
					statsAccess.set("deaths." + uuid + "." + deathMessage, nbDeaths);
				}
			}
			
			try {
				statsAccess.save(statsFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			saving = false;
		});
	}
}
