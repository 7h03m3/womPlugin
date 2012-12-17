package ch.worldofminecraft.bukkit.region;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class womRegionTeleport extends womRegion {
	
	private Location arriveLoc = null;
	private womRegionTeleport dstTeleport = null;
	private boolean active;

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
	public boolean isDestinationTeleport() {
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
		if(this.isDestinationTeleport() == false)
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
		
		return player.teleport(this.dstTeleport.arriveLoc);
	}
}
