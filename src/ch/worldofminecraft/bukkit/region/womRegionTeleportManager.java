package ch.worldofminecraft.bukkit.region;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ch.worldofminecraft.bukkit.test.womTestPlugin;
import ch.worldofminecraft.bukkit.region.womRegionTeleport;;

public class womRegionTeleportManager {
	
	private womTestPlugin plugin;
	
	private World world;
	private HashMap<String, womRegionTeleport> teleport = new HashMap<String, womRegionTeleport>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegionTeleportManager(womTestPlugin plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		
		Location loc1 = new Location(world, 1450,4,-120);
		Location loc2 = new Location(world, 1460,4,-130);
		Location loc3 = new Location(world, 1455,4,-135);
		initRegion("test1", loc1, loc2, loc3);		
		
		loc1 = new Location(world, 1420,4,-100);
		loc2 = new Location(world, 1430,4,-110);
		loc3 = new Location(world, 1425,4,-95);  
		initRegion("test2", loc1, loc2, loc3);
		
		linkTeleport("test1", "test2");
		linkTeleport("test2", "test1");
		
		womRegionTeleport teleportTmp = this.getRegion("test1");
		if(teleportTmp.setActive() == true) {
			this.plugin.info("Region "+teleportTmp.getName()+" activated");
		}
		teleportTmp = this.getRegion("test2");
		if(teleportTmp.setActive() == true) {
			this.plugin.info("Region "+teleportTmp.getName()+" activated");
		}
	}
	
	/************************************************************
	 * addRegion()
	 ************************************************************/
	public womRegionTeleport addRegion(String name) {
		if(this.teleport.containsKey(name)) {
			return null;
		}
		
		womRegionTeleport teleportObj = new womRegionTeleport(name);
		this.teleport.put(name, teleportObj);
		
		return teleportObj;
	}
	
	/************************************************************
	 * removeRegion()
	 ************************************************************/
	public boolean removeRegion(String name) {
		if(this.teleport.get(name) != null) {
			this.teleport.remove(name);	
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * initRegion()
	 ************************************************************/
	public womRegionTeleport initRegion(String name, Location edge1, Location edge2, Location dst) {
		if((this.world.equals(edge1.getWorld()) == false) || 
		   (this.world.equals(edge2.getWorld()) == false)) {
			return null;
		}
		womRegionTeleport teleportTmp = addRegion(name);
		teleportTmp.setRegion(edge1, edge2);
		teleportTmp.setArrivePosition(dst);
		
		return teleportTmp;
	}
	
	/************************************************************
	 * getRegion()
	 ************************************************************/
	public womRegionTeleport getRegion(String name) {
		return this.teleport.get(name);
	}
	
	/************************************************************
	 * getRegionArray()
	 ************************************************************/
	public womRegionTeleport[] getRegionArray() {
		womRegionTeleport[] array = new womRegionTeleport[this.teleport.size()];
		int i=0;
		
		for( String name: this.teleport.keySet()) {
			array[i] = this.teleport.get(name);
			i++;
		}
		return array;
	}
	
	/************************************************************
	 * linkTeleport()
	 ************************************************************/
	public boolean linkTeleport(String nameFrom, String nameTo)
	{
		womRegionTeleport objFrom = this.getRegion(nameFrom);
		womRegionTeleport objTo = this.getRegion(nameTo);
		
		if((objFrom != null) && (objTo != null)) { 
			this.getRegion(nameFrom).setDestinationTeleport(this.getRegion(nameTo));
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * isInsideRegion()
	 ************************************************************/
	public boolean isInsideRegion(Location loc) {
		if(this.world.equals(loc.getWorld()) == false) {
			return false;
		}
		
	    for( String name: this.teleport.keySet() ) {
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
		if((this.world.equals(locTo.getWorld()) == false) ||
		   (this.world.equals(locFrom.getWorld()) == false)) {
			return null;
		}
		
		for( String name: this.teleport.keySet() ) {
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
		if((this.world.equals(locTo.getWorld()) == false) ||
		   (this.world.equals(locFrom.getWorld()) == false)) {
			return null;
		}
		
		for( String name: this.teleport.keySet() ) {
			if(this.getRegion(name).isActive() == true) {
				if(this.getRegion(name).isLeaving(locTo, locFrom) == true) {
					return name;
				}
			}
		}
		
		return null;
	}
	
	/************************************************************
	 * handleTeleport()
	 ************************************************************/
	public boolean handleTeleport(String name, Player player) {
		
		womRegionTeleport teleportTmp = this.getRegion(name);
		if(teleportTmp != null) {
			return teleportTmp.teleportPlayer(player);
		}
		
		return false;
	}
}
