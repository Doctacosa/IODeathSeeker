package com.interordi.iodeathseeker.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.interordi.iodeathseeker.IODeathSeeker;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {

	private IODeathSeeker plugin;
	
	public PlayerKickListener(IODeathSeeker plugin) {
		this.plugin = plugin;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}


	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKickEvent(PlayerKickEvent event) {
		
		String message = event.getReason().toLowerCase();

		String playerName = event.getPlayer().getDisplayName();

		//Ignore quits
		if (message.startsWith("you have left:"))
			return;

		//Remove the player's name
		message = message.replace(playerName.toLowerCase(), "").trim();

		//TODO: Not a death, it's a kick
		if (message.length() > 0)
			this.plugin.players.logDeath(event.getPlayer().getUniqueId(), message, 1);
	}


	public static String cleanName(String name) {
		return name.toLowerCase().replace("_", " ").trim();
	}
	
}
