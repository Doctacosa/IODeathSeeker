package com.interordi.iodeathseeker.listeners;

import com.interordi.iodeathseeker.IODeathSeeker;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	private IODeathSeeker plugin;
	
	public PlayerDeathListener(IODeathSeeker plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String message = event.getDeathMessage();

		//Remove the player's name
		message = message.replace(event.getEntity().getDisplayName(), "").trim();

		//Replace the killer's name
		//TODO: Better handling, some names have spaces
		message = message.substring(0, message.lastIndexOf(" ")).trim();

		if (message.length() > 0)
			this.plugin.players.logDeath(event.getEntity().getUniqueId(), message, 1);
	}
	
}
