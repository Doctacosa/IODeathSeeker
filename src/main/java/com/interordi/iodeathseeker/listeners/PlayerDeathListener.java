package com.interordi.iodeathseeker.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.interordi.iodeathseeker.IODeathSeeker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

	private IODeathSeeker plugin;
	private Map< UUID, String > lastMobName;
	private Map< UUID, String > lastMobType;
	private boolean ignoreMobs;
	
	public PlayerDeathListener(IODeathSeeker plugin, boolean ignoreMobs) {
		this.plugin = plugin;
		this.ignoreMobs = ignoreMobs;
		
		lastMobName = new HashMap< UUID, String >();
		lastMobType = new HashMap< UUID, String >();
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}


	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		
		//Attack from a mob or another player
		if (event instanceof EntityDamageByEntityEvent) {
			onEntityDamage((EntityDamageByEntityEvent)event);
		}
	}
	
	
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		
		float damage = Math.round(event.getFinalDamage() * 10) / 10;
		
		//Ignore no damage
		if (damage == 0)
			return;
		
		boolean playerTarget = (event.getEntity() != null && event.getEntity() instanceof Player);
		
		//No human target = don't care
		if (!playerTarget) {
			return;
		}
		
		Entity attacker = event.getDamager();
		Entity target = event.getEntity();
		
		//If the damage came from a projectile, find said projectile's owner instead
		if (event.getDamager() instanceof Projectile) {
			final Projectile projectile = (Projectile)event.getDamager();
			if (projectile.getShooter() instanceof Entity)
				attacker = (Entity)projectile.getShooter();

		}

		//This isn't perfect and doesn't need to be - at worst, it will be ignored
		if (attacker != null) {
			lastMobName.put(target.getUniqueId(), attacker.getName().toString());
			lastMobType.put(target.getUniqueId(), attacker.getType().toString());
		}
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		String message = event.getDeathMessage().toLowerCase();

		//Replace the killer's name
		String killerName = "";
		String killerType = "";
		EntityDamageEvent lastDamage = event.getEntity().getLastDamageCause();
		if (lastDamage != null && lastDamage instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent)lastDamage;
			
			if (nEvent.getDamager() != null) {
				//If the damage came from a projectile, find said projectile's owner
				if (nEvent.getDamager() instanceof Projectile) {
					final Projectile projectile = (Projectile)nEvent.getDamager();
					if (projectile.getShooter() instanceof Entity) {
						Entity temp = (Entity)projectile.getShooter();
						if (temp != null) {
							killerName = cleanName(temp.getName().toString());
							killerType = cleanName(temp.getType().toString());
						}
					}
				}
				//Just a regular mob kill
				else {
					killerName = cleanName(nEvent.getDamager().getName().toString());
					killerType = cleanName(nEvent.getDamager().getType().toString());
				}
			}
		} else if (lastDamage != null && lastDamage instanceof EntityDamageByBlockEvent) {
			EntityDamageByBlockEvent nEvent = (EntityDamageByBlockEvent)lastDamage;

			//Ignored, handled through the lastMobDamage map
			if (nEvent.getDamager() != null) {
				//System.out.println("Killer name block: " + nEvent.getDamager().getType().toString());
			}

		}

		//Remove the player's name
		message = message.replace(event.getEntity().getDisplayName().toLowerCase(), "").trim();

		//Remove the killer's name
		if (ignoreMobs)
			message = message.replace(killerName, "").trim();
		else
			message = message.replace(killerName, killerType).trim();

		//Remove the last damager's name
		String checkMobName = lastMobName.get(event.getEntity().getUniqueId());
		String checkMobType = lastMobType.get(event.getEntity().getUniqueId());
		if (checkMobName != null) {
			if (ignoreMobs)
				message = message.replace(cleanName(checkMobName), "").trim();
			else
				message = message.replace(cleanName(checkMobName), cleanName(checkMobType)).trim();
		}


		if (message.length() > 0)
			this.plugin.players.logDeath(event.getEntity().getUniqueId(), message, 1);
	}


	public static String cleanName(String name) {
		return name.toLowerCase().replace("_", " ").trim();
	}
	
}
