package com.interordi.iodeathseeker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerTracking {
	
	private Player player;
	private UUID uuid;
	private Map< String, Integer > deaths;
	
	static Map< String, Integer > scores = new HashMap< String, Integer>();

	
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
		if (this.deaths.containsKey(death)) {
			Integer deathCount = this.deaths.get(death);
			this.deaths.put(death, deathCount + count);
		} else {
			this.deaths.put(death, count);
		}

		updateScore();
	}


	//Initialize the scoreboard
	public static void initScore() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective objective = board.getObjective("deaths");
		
		if (objective != null)
			objective.unregister();
		
		objective = board.registerNewObjective("deaths", "dummy", "Unique deaths");
		board.clearSlot(DisplaySlot.SIDEBAR);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	
	//Update a player's score on the global display
	public void updateScore() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective objective = board.getObjective("deaths");
		if (objective != null) {
			Score myScore = objective.getScore(player.getDisplayName());
			myScore.setScore(getNbUniqueDeaths());
			
			scores.put(player.getDisplayName(), getNbUniqueDeaths());
		}
	}
	
	
	//Remove a player's score from the display
	public void removeScore() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective objective = board.getObjective("deaths");
		if (objective != null) {
			//Remove a player then rebuild the scoreboard from the known data
			objective.unregister();
			objective = null;
			
			objective = board.registerNewObjective("deaths", "dummy", "Unique deaths");
			board.clearSlot(DisplaySlot.SIDEBAR);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			scores.remove(player.getDisplayName());
			
			for (String key : scores.keySet()) {
				Score myScore = objective.getScore(key);
				myScore.setScore(scores.get(key));
			}
		}
	}
}
