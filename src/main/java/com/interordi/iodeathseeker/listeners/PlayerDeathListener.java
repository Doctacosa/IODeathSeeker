package com.interordi.iodeathseeker.listeners;

import com.interordi.iodeathseeker.IODeathSeeker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	private IODeathSeeker plugin;
	
	public PlayerDeathListener(IODeathSeeker plugin) {
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String message = event.getDeathMessage().toLowerCase();

		//Remove the player's name
		message = message.replace(event.getEntity().getDisplayName().toLowerCase(), "").trim();

		//Replace the killer's name
		String killerName = "";
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();
		if (lastDamage != null && lastDamage instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent)lastDamage;
			
			if (nEvent.getDamager() != null) {
				//If the damage came from a projectile, find said projectile's owner
				if (nEvent.getDamager() instanceof Projectile) {
					final Projectile projectile = (Projectile)nEvent.getDamager();
					Entity temp = (Entity)projectile.getShooter();
					killerName = temp.getType().toString().toLowerCase();
				}
				//Just a regular mob kill
				else {
					killerName = nEvent.getDamager().getType().toString().toLowerCase();
				}
			}
		}

		message = message.replace(killerName, "").trim();

		if (message.length() > 0)
			this.plugin.players.logDeath(event.getEntity().getUniqueId(), message, 1);
	}
	
}
