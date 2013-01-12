package ch.worldofminecraft.bukkit.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class womTeleport {
	
	private boolean active;
	private Location destination = null;

	/************************************************************
	 * Constructor
	 ************************************************************/
	public womTeleport(Location destination) {
		this.destination = destination;
		this.active = false;
	}
	
	/************************************************************
	 * getDestination()
	 ************************************************************/
	public Location getDestination() {
		return this.destination;
	}
	
	/************************************************************
	 * setDestination()
	 ************************************************************/
	public boolean setDestination(Location destination) {
		if(destination == null)
			return false;
		
		this.destination = destination;
		return true;
	}
	
	/************************************************************
	 * isActive()
	 ************************************************************/
	public boolean isActive() {
		return this.active;
	}
	
	/************************************************************
	 * activate()
	 ************************************************************/
	public void activate() {
		this.active = true;
	}
	
	/************************************************************
	 * deactivate()
	 ************************************************************/
	public void deactivate() {
		this.active = false;
	}
	
	/************************************************************
	 * teleport()
	 ************************************************************/
	public boolean teleport(Player player) {
		if(this.isActive() == false) 
			return false;
		if(player.isOnline() == false) 
			return false;
		
		return player.teleport(this.destination);
	}
}
