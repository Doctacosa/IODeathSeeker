package com.interordi.iodeathseeker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTracking {
	
	private UUID uuid;
	private Map< String, Integer > deaths;
	
	
	PlayerTracking(UUID uuid) {
		this.uuid = uuid;
		this.deaths = new HashMap< String, Integer>();
	}
	
	
	public UUID getUuid() {
		return this.uuid;
	}
	
	
	public Map< String, Integer > getDeaths() {
		return this.deaths;
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
	}

}
