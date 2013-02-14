package ch.worldofminecraft.bukkit.region;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ch.worldofminecraft.bukkit.teleport.womTeleport;

public class womRegionTeleport extends womRegion {
	
	private boolean active;
	private womTeleport arriveTeleport = null;
	private womRegionTeleport destinationRegion = null;
	private HashMap<String, womRegionTeleport> destinationRegionPlayer = new HashMap<String, womRegionTeleport>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegionTeleport(String name) {
		super(name);
		this.active = false;
	}
	
	/************************************************************
	 * setDestinationRegion()
	 ************************************************************/
	public void setDestinationRegion(womRegionTeleport dst) {
		this.destinationRegion = dst;
	}
	
	/************************************************************
	 * isDestinationRegionSet()
	 ************************************************************/
	public boolean isDestinationRegionSet() {
		if(this.destinationRegion != null) {
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * getDestinationRegion()
	 ************************************************************/
	public womRegionTeleport getDestinationRegion() {
		return this.destinationRegion;
	}

	/************************************************************
	 * removeDestinationRegion()
	 ************************************************************/
	public void removeDestinationRegion() {	
		this.destinationRegion = null;
		this.deactivate();
	}
	
	/************************************************************
	 * setArriveLocation()
	 ************************************************************/
	public boolean setArriveLocation(Location dst) {
		if(this.isRegionSet() == false)
			return false;
		if(this.isInsideRegion(dst) == true)
			return false;

		this.arriveTeleport = new womTeleport(this.getName(),dst);
		this.arriveTeleport.activate();
		
		return true;
	}
	
	/************************************************************
	 * getArriveLocation()
	 ************************************************************/
	public Location getArriveLocation() {
		if(this.arriveTeleport != null) {
			return this.arriveTeleport.getDestination();
		}
		return null;
	}
	
	/************************************************************
	 * getArriveTeleport()
	 ************************************************************/
	public womTeleport getArriveTeleport() {
		return this.arriveTeleport;
	}
	
	/************************************************************
	 * isArriveTeleportSet()
	 ************************************************************/
	public boolean isArriveTeleportSet() {
		if(this.arriveTeleport != null) {
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * isDestinationRegionPlayerSet()
	 ************************************************************/
	public boolean isDestinationRegionPlayerSet(String playerName) {
		return this.destinationRegionPlayer.containsKey(playerName);
	}
	
	/************************************************************
	 * isDestinationRegionPlayerEmpty()
	 ************************************************************/
	public boolean isDestinationRegionPlayerEmpty() {
		return this.destinationRegionPlayer.isEmpty();
	}
	
	/************************************************************
	 * setDestinationRegionPlayer()
	 ************************************************************/
	public boolean setDestinationRegionPlayer(String playerName, womRegionTeleport teleport) {
		if(this.isDestinationRegionPlayerSet(playerName) == true) 
			return false;
		if(teleport == null)
			return false;
		
		this.destinationRegionPlayer.put(playerName, teleport);
		
		return true;
	}

	/************************************************************
	 * getDestinationRegionPlayer()
	 ************************************************************/
	public womRegionTeleport getDestinationRegionPlayer(String playerName) {
		if(this.isDestinationRegionPlayerSet(playerName) == true) {
			return this.destinationRegionPlayer.get(playerName);
		}
		return null;
	}
	
	/************************************************************
	 * getDestinationRegionPlayerHashMap
	 ************************************************************/
	public HashMap<String, womRegionTeleport> getDestinationRegionPlayerHashMap() {
		return this.destinationRegionPlayer;
	}
	
	/************************************************************
	 * removeDestinationRegionPlayer()
	 ************************************************************/
	public void removeDestinationRegionPlayer(String playerName) {
		this.destinationRegionPlayer.remove(playerName);
	}
	
	/************************************************************
	 * cleanupDestinationRegion()
	 ************************************************************/
	public void cleanupDestinationRegion(String name) {
		for( womRegionTeleport teleportTmp: this.destinationRegionPlayer.values() ) {
			if(teleportTmp.getName().equals(name) == true) {
				this.removeDestinationRegionPlayer(name);
			}
		}
		
		if(this.destinationRegion.getName().equals(name) == true) {
			this.removeDestinationRegion();
		}
	}
	
	/************************************************************
	 * isActive
	 ************************************************************/
	public boolean isActive() {
		return this.active;
	}
	
	/************************************************************
	 * activate
	 ************************************************************/
	public boolean activate() {
		if(this.isRegionSet() == false) 
			return false;
		if(this.isDestinationRegionSet() == false)
			return false;
		if(this.destinationRegion.isArriveTeleportSet() == false)
			return false;
			
		this.active = true;
		return true;
	}
	
	/************************************************************
	 * deactivate
	 ************************************************************/
	public void deactivate() {
		this.active = false;
	}
	
	/************************************************************
	 * teleportPlayer()
	 ************************************************************/
	public boolean teleportPlayer(Player player) {
		if(this.isActive() == false)
			return false;
		
		String playerName = player.getName();
		womRegionTeleport region;
		
		// player specific teleport
		if(this.isDestinationRegionPlayerSet(playerName) == true) {
			region = this.destinationRegionPlayer.get(playerName);
		
			if(region.isArriveTeleportSet() == true) {
				return region.arriveTeleport.teleport(player);
			}
		} 
		
		// default teleport
		if(this.isDestinationRegionSet() == true) {
			region = this.getDestinationRegion();
			
			if(region.isArriveTeleportSet() == true) {
				return region.arriveTeleport.teleport(player);
			}
		}
		
		return false;
	}
}