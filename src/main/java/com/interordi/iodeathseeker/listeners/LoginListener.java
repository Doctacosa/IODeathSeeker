package com.interordi.iodeathseeker.listeners;

import org.bukkit.event.player.*;

import com.interordi.iodeathseeker.IODeathSeeker;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class LoginListener implements Listener {
	
	private IODeathSeeker plugin;
	
	public LoginListener(IODeathSeeker plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		this.plugin.players.addPlayer(event.getPlayer());
	}
	
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event) {
		this.plugin.players.removePlayer(event.getPlayer());
	}
}
