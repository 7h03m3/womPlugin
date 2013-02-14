package ch.worldofminecraft.bukkit.region;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import ch.worldofminecraft.bukkit.region.womRegionTeleport;

public class womRegionTeleportWorldManager {
	
	private World world;
	private HashMap<String, womRegionTeleport> teleportRegion = new HashMap<String, womRegionTeleport>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegionTeleportWorldManager(World world) {
		this.world = world;
	}
	
	/************************************************************
	 * getWorld()
	 ************************************************************/
	public World getWorld() {
		return this.world;
	}
	
	/************************************************************
	 * getWorldName()
	 ************************************************************/
	public String getWorldName() {
		return this.world.getName();
	}
	
	/************************************************************
	 * addRegion()
	 ************************************************************/
	public womRegionTeleport addRegion(String name, Location edge1, Location edge2) {
		if(this.teleportRegion.containsKey(name)) {
			return null;
		}
		
		womRegionTeleport teleportObj = new womRegionTeleport(name);
		teleportObj.setRegion(edge1, edge2);
		this.teleportRegion.put(name, teleportObj);
		
		return teleportObj;
	}
	
	/************************************************************
	 * isRegionExist()
	 ************************************************************/
	public boolean isRegionExist(String name) {
		if(this.teleportRegion.containsKey(name)) {
			return true;
		}
		
		return false;
	}
	
	/************************************************************
	 * removeRegion()
	 ************************************************************/
	public void removeRegion(String name) {
		
		for( womRegionTeleport region: this.teleportRegion.values() ) {
			region.cleanupDestinationRegion(name);
		}
		
		if(this.isRegionExist(name) == true) { 		
			this.teleportRegion.remove(name);
		}
	}
	
	/************************************************************
	 * initRegion()
	 ************************************************************/
	public womRegionTeleport initRegion(String name, Location edge1, Location edge2, Location dst) {

		womRegionTeleport teleportTmp = addRegion(name, edge1, edge2);
		
		teleportTmp.setArriveLocation(dst);
		
		return teleportTmp;
	}
	
	/************************************************************
	 * getRegion()
	 ************************************************************/
	public womRegionTeleport getRegion(String name) {
		return this.teleportRegion.get(name);
	}
	
	/************************************************************
	 * getRegionArray()
	 ************************************************************/
	public womRegionTeleport[] getRegionArray() {
		womRegionTeleport[] array = new womRegionTeleport[this.teleportRegion.size()];
		int i=0;
		
		for( String name: this.teleportRegion.keySet()) {
			array[i] = this.teleportRegion.get(name);
			i++;
		}
		return array;
	}
	
	/************************************************************
	 * isInsideRegion()
	 ************************************************************/
	public boolean isInsideRegion(Location loc) {
		
	    for( String name: this.teleportRegion.keySet() ) {
	    	if(this.getRegion(name).isActive() == true) {
	    		if(this.getRegion(name).isInsideRegion(loc) == true) {
	    			return true;
	    		}
	    	}
		}
		
		return false;
	}

	/************************************************************
	 * isEnteringRegion()
	 ************************************************************/
	public String isEnteringRegion(Location locTo, Location locFrom) {
		
		for( String name: this.teleportRegion.keySet() ) {
			if(this.getRegion(name).isActive() == true) {
				if(this.getRegion(name).isEntering(locTo, locFrom) == true) {
					return name;
				}
			}
		}
		
		return null;
	}
	
	/************************************************************
	 * isLeavingRegion()
	 ************************************************************/
	public String isLeavingRegion(Location locTo, Location locFrom) {		
		
		for( String name: this.teleportRegion.keySet() ) {
			if(this.getRegion(name).isActive() == true) {
				if(this.getRegion(name).isLeaving(locTo, locFrom) == true) {
					return name;
				}
			}
		}
		
		return null;
	}
}
