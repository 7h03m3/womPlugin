package ch.worldofminecraft.bukkit.region;

import org.bukkit.Location;
import org.bukkit.World;

public class womRegion {
	
	private String name;
	private boolean regionSet;
	private World world;
	private int x1;
	private int x2;
	private int y1; 
	private int y2;
	private int z1; 
	private int z2;
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegion(String name) {
		this.name = name;
		this.regionSet = false;
	}
	
	/************************************************************
	 * setRegion()
	 ************************************************************/
	public void setRegion(Location edge1, Location edge2) {		
		world = edge2.getWorld();
		
		x1 = Math.min(edge1.getBlockX(), edge2.getBlockX());
		x2 = Math.max(edge1.getBlockX(), edge2.getBlockX());
		y1 = Math.min(edge1.getBlockY(), edge2.getBlockY());
		y2 = Math.max(edge1.getBlockY(), edge2.getBlockY());
		z1 = Math.min(edge1.getBlockZ(), edge2.getBlockZ());
		z2 = Math.max(edge1.getBlockZ(), edge2.getBlockZ());
		
		this.regionSet = true;
	}
	
	/************************************************************
	 * getName()
	 ************************************************************/
	public String getName() {
		return this.name;
	}
	
	/************************************************************
	 * getEdge1()
	 ************************************************************/
	public Location getEdge1() {
		return new Location(this.world, this.x1, this.y1, this.z1);
	}
	
	/************************************************************
	 * getEdge2()
	 ************************************************************/
	public Location getEdge2() {
		return new Location(this.world, this.x2, this.y2, this.z2);
	}
	
	/************************************************************
	 * isRegionSet()
	 ************************************************************/
	public boolean isRegionSet() {		
		return this.regionSet;
	}
	
	/************************************************************
	 * isInsideRegion()
	 ************************************************************/
	public boolean isInsideRegion(Location loc) {		
		if(this.isRegionSet() == true) {
			if(this.world.equals(loc.getWorld()) == true) {
				if((loc.getBlockX() >= this.x1) && (loc.getBlockX() <= this.x2) &&
				   (loc.getBlockY() >= this.y1) && (loc.getBlockY() <= this.y2) &&
			       (loc.getBlockZ() >= this.z1) && (loc.getBlockZ() <= this.z2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/************************************************************
	 * isEntering()
	 ************************************************************/
	public boolean isEntering(Location locTo, Location locFrom) {
		final boolean to = isInsideRegion(locTo);
		final boolean from = isInsideRegion(locFrom);
	
		if((to == true) && (from == false)) {
			return true;
		}
		
		return false;
	}
	
	/************************************************************
	 * isLeaving()
	 ************************************************************/
	public boolean isLeaving(Location locTo, Location locFrom) {
		final boolean to = isInsideRegion(locTo);
		final boolean from = isInsideRegion(locFrom);
	
		if((to == false) && (from == true)) {
			return true;
		}
		
		return false;
	}

}
