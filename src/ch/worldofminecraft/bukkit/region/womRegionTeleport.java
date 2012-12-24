package ch.worldofminecraft.bukkit.region;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class womRegionTeleport extends womRegion {
	
	private boolean active;
	private Location arriveLoc = null;
	private womRegionTeleport dstTeleport = null;
	private HashMap<String, womRegionTeleport> dstTeleportPlayer = new HashMap<String, womRegionTeleport>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegionTeleport(String name) {
		super(name);
		this.active = false;
	}
	
	/************************************************************
	 * setDestinationTeleport()
	 ************************************************************/
	public void setDestinationTeleport(womRegionTeleport dst) {
		this.dstTeleport = dst;
	}
	
	/************************************************************
	 * isDestinationTeleportSet()
	 ************************************************************/
	public boolean isDestinationTeleportSet() {
		if(this.dstTeleport != null) {
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * getDestinationTeleport()
	 ************************************************************/
	public womRegionTeleport getDestinationTeleport() {
		return this.dstTeleport;
	}
	
	/************************************************************
	 * setDestinationTeleportPlayer()
	 ************************************************************/
	public boolean setDestinationTeleportPlayer(String playerName, womRegionTeleport teleport) {
		if(this.isDestinationTeleportPlayerSet(playerName) == true) 
			return false;
		if(teleport == null)
			return false;
		
		this.dstTeleportPlayer.put(playerName, teleport);
		
		return true;
	}
	
	/************************************************************
	 * isDestinationTeleportPlayerSet()
	 ************************************************************/
	public boolean isDestinationTeleportPlayerSet(String playerName) {
		return this.dstTeleportPlayer.containsKey(playerName);
	}
	
	/************************************************************
	 * getDestinationTeleportPlayer()
	 ************************************************************/
	public womRegionTeleport getDestinationTeleportPlayer(String playerName) {
		if(this.isDestinationTeleportPlayerSet(playerName) == true) {
			return this.dstTeleportPlayer.get(playerName);
		}
		return null;
	}

	/************************************************************
	 * removeDestinationTeleport()
	 ************************************************************/
	public void removeDestinationTeleport(womRegionTeleport teleport) {
		womRegionTeleport teleportTmp;
		for( String name: this.dstTeleportPlayer.keySet() ) {
			teleportTmp = this.dstTeleportPlayer.get(name);
			if(teleportTmp.equals(teleport) == true) {
				this.dstTeleportPlayer.remove(name);
			}
		}
		
		if(this.dstTeleport.equals(teleport) == true) {
			this.dstTeleport = null;
			this.setInactive();
		}
	}
	
	/************************************************************
	 * setArrivePosition()
	 ************************************************************/
	public boolean setArrivePosition(Location dst) {
		if(this.isRegionSet() == false)
			return false;
		if(this.isInsideRegion(dst) == true)
			return false;

		this.arriveLoc = dst;
		
		return true;
	}
	
	/************************************************************
	 * getArrivePosition()
	 ************************************************************/
	public Location getArrivePosition() {
		return this.arriveLoc;
	}
	
	/************************************************************
	 * isArrivePositionSet()
	 ************************************************************/
	public boolean isArrivePositionSet() {
		if(this.arriveLoc != null) {
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * setActive
	 ************************************************************/
	public boolean setActive() {
		if(this.isRegionSet() == false) 
			return false;
		if(this.isDestinationTeleportSet() == false)
			return false;
		if(this.dstTeleport.isArrivePositionSet() == false)
			return false;
			
		this.active = true;
		return true;
	}
	
	/************************************************************
	 * setInactive
	 ************************************************************/
	public void setInactive() {
		this.active = false;
	}
	
	/************************************************************
	 * isActive
	 ************************************************************/
	public boolean isActive() {
		return this.active;
	}
	
	/************************************************************
	 * teleportPlayer()
	 ************************************************************/
	public boolean teleportPlayer(Player player) {
		if(this.isActive() == false) 
			return false;
		if(player.isOnline() == false) 
			return false;
		
		String playerName = player.getName();
		womRegionTeleport teleport;
		
		// player specific teleport
		if(this.isDestinationTeleportPlayerSet(playerName) == true) {
			teleport = this.dstTeleportPlayer.get(playerName);
		
			if(teleport.isArrivePositionSet() == true) {
				return player.teleport(teleport.getArrivePosition());
			}
		} 
		
		// default teleport
		if(this.isDestinationTeleportSet() == true) {
			teleport = this.getDestinationTeleport();
			
			if(teleport.isArrivePositionSet() == true) {
				return player.teleport(teleport.getArrivePosition());
			}
		}
		
		return false;
	}
}