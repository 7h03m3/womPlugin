package ch.worldofminecraft.bukkit.region;

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;

import java.util.logging.Level;

import ch.worldofminecraft.bukkit.test.womTestPlugin;
import ch.worldofminecraft.bukkit.configuration.womConfiguration;
import ch.worldofminecraft.bukkit.region.womRegionTeleportWorldManager;

public class womRegionTeleportManager {
	
	private womTestPlugin plugin;
	private womConfiguration configuration;
	
	private HashMap<String, womRegionTeleportWorldManager> worldManagers = new HashMap<String, womRegionTeleportWorldManager>();
	private HashMap<String, womRegionTeleportWorldManager> regionLinkMap = new HashMap<String, womRegionTeleportWorldManager>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womRegionTeleportManager(womTestPlugin plugin) {
		this.plugin = plugin;
		
		// Initialize configuration class
		this.configuration = new womConfiguration(this.plugin, "regionTeleport.yml");
		
		// Initialize world region managers
		List<World> worldList = Bukkit.getWorlds();
		Iterator<World> worldListIterator = worldList.iterator();
		while(worldListIterator.hasNext()) {
			World world = worldListIterator.next();
			String worldName = world.getName();
			
			this.worldManagers.put(worldName, new womRegionTeleportWorldManager(world));
		}
		
		// Load configuration file 
		if(this.loadConfig() == false)
			this.plugin.getLogger().log(Level.SEVERE, "Teleport region manager could not load configuration");
		
	}
	
	/************************************************************
	 * saveConfig()
	 ************************************************************/
	private boolean saveConfig() {
		if(this.configuration.isWriteLocked()) 
			return false;
		
		if(this.configuration.loadConfig() == false) 
			return false;
		
		YamlConfiguration config = this.configuration.getConfig();
		
		for(String name: this.regionLinkMap.keySet()) {
			womRegionTeleportWorldManager manager = this.regionLinkMap.get(name);
			womRegionTeleport region = manager.getRegion(name);
			String path = name+this.configuration.getSeparator();
			
			config.set(path+"enabled",region.isActive());
			config.set(path+"world",manager.getWorldName());
			
			if(region.isDestinationRegionSet()) {
				config.set(path+"linkedRegion",region.getDestinationRegion().getName());
			}
			
			this.configuration.setLocation(path+"edge1", region.getEdge1());
			this.configuration.setLocation(path+"edge2", region.getEdge2());
			
			if(region.isArriveTeleportSet()) {
				this.configuration.setLocation(path+"arriveLocation",region.getArriveLocation());
			}
			
			if(region.isDestinationRegionPlayerEmpty() == false) {
				for(String playerName: region.getDestinationRegionPlayerHashMap().keySet()) {
					config.set(path+"playerLinkedRegion."+playerName,region.getDestinationRegionPlayer(playerName).getName());	
				}
			}
		}			
		
		if(this.configuration.saveConfig()) 
			return false;
		
		return true;
	}
	
	/************************************************************
	 * loadConfig()
	 ************************************************************/
	private boolean loadConfig() {
		
		this.configuration.lockWrite();
		
		if(this.configuration.loadConfig() == false) 
			return false;
		
		ConfigurationSection configSection;
		YamlConfiguration config = this.configuration.getConfig();
		
		// World loop 
		Iterator<String> regionIterator = config.getKeys(false).iterator();
		while(regionIterator.hasNext()) {
			String regionName = regionIterator.next();

			// Region 
			configSection = config.getConfigurationSection(regionName);	
			World world = Bukkit.getWorld(configSection.getString("world"));
			Location edge1 = this.configuration.getLocation(regionName+".edge1", world);
			Location edge2 = this.configuration.getLocation(regionName+".edge2", world);
			
			womRegionTeleport regionObj = this.addRegion(regionName, edge1, edge2);
			
			if(configSection.contains("arriveLocation")) {
				Location arriveLocation = this.configuration.getLocation(regionName+".arriveLocation", world);
				regionObj.setArriveLocation(arriveLocation);
			}
		}
		
		// Link regions
		regionIterator = config.getKeys(false).iterator();
		while(regionIterator.hasNext()) {
			String regionName = regionIterator.next();
			configSection = config.getConfigurationSection(regionName);	
			
			// Region link
			if(configSection.contains("linkedRegion")) {
				this.linkRegion(regionName, configSection.getString("linkedRegion"));
			}
			
			// Player linked region
			if(configSection.contains("playerLinkedRegion")) {
				ConfigurationSection playerLinkedRegion = config.getConfigurationSection(regionName+".playerLinkedRegion");
				Iterator <String> playerLinkedRegionIterator = playerLinkedRegion.getKeys(false).iterator();
				
				while(playerLinkedRegionIterator.hasNext()) {
					String playerName = playerLinkedRegionIterator.next();
					this.linkRegionPlayer(regionName, playerLinkedRegion.getString(playerName), playerName);	
				}
			}
			
			// activate teleport region
			if(configSection.getBoolean("enabled")) {		
				this.regionLinkMap.get(regionName).getRegion(regionName).activate();
			}
		}
		
		this.configuration.unlockWrite();
		
		return true;
	}
	
	/************************************************************
	 * getManager()
	 ************************************************************/
	private womRegionTeleportWorldManager getManager(World world) {
		return this.worldManagers.get(world.getName());
	}
	
	/************************************************************
	 * addRegion()
	 ************************************************************/
	public womRegionTeleport addRegion(String name, Location edge1, Location edge2) {
		womRegionTeleport ret = null;
		womRegionTeleportWorldManager worldManager = this.getManager(edge1.getWorld());
		
		if(worldManager != null) {
			if(this.regionLinkMap.containsKey(name) == false) {
				this.regionLinkMap.put(name, worldManager);
				ret = worldManager.addRegion(name, edge1, edge2);
				this.saveConfig();
			}
		}
		return ret;
	}
	

	/************************************************************
	 * isRegionExist
	 ************************************************************/
	public boolean isRegionExist(String name) {
		if(this.regionLinkMap.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * removeRegion()
	 ************************************************************/
	public void removeRegion(String name) {
		for( womRegionTeleportWorldManager manager: this.worldManagers.values() ) {
			manager.removeRegion(name);
		}
		this.saveConfig();
	}
	
	/************************************************************
	 * getRegion()
	 ************************************************************/
	public womRegionTeleport getRegion(String name) {
		if(this.regionLinkMap.containsKey(name) == true) {
			return this.regionLinkMap.get(name).getRegion(name);
		}
		return null;
	}
	
	/************************************************************
	 * getRegionNames()
	 ************************************************************/
	public String[] getRegionNames() {
		String[] array = new String[this.regionLinkMap.size()];
		int i = 0;
		
		for( String name: this.regionLinkMap.keySet()) {
			array[i] = name;
			i++;
		}
		return array;
	}
	
	/************************************************************
	 * setArriveLocation()
	 ************************************************************/
	public boolean setArriveLocation(String name, Location dst) {
		boolean ret;
		if(isRegionExist(name) == false) 
			return false;
		ret = this.getRegion(name).setArriveLocation(dst);
		this.saveConfig();
		return ret;
	}
	
	/************************************************************
	 * activate()
	 ************************************************************/
	public boolean activate(String name) {
		boolean ret;
		if(isRegionExist(name) == false) 
			return false;
		
		ret = this.getRegion(name).activate();
		this.saveConfig();
		return ret;
	}
	
	/************************************************************
	 * deactivate()
	 ************************************************************/
	public boolean deactivate(String name) {
		if(isRegionExist(name) == false)
			return false;
		
		this.getRegion(name).deactivate();
		this.saveConfig();
		return true;
	}
	
	/************************************************************
	 * linkTeleport()
	 ************************************************************/
	public boolean linkRegion(String nameFrom, String nameTo)
	{
		womRegionTeleport objFrom = this.getRegion(nameFrom);
		womRegionTeleport objTo = this.getRegion(nameTo);
		
		if((objFrom != null) && (objTo != null)) { 
			objFrom.setDestinationRegion(objTo);
			this.saveConfig();
			return true;
		}
		return false;
	}
	
	/************************************************************
	 * linkTeleportPlayer()
	 ************************************************************/
	public boolean linkRegionPlayer(String nameFrom, String nameTo, String namePlayer)
	{
		womRegionTeleport objFrom = this.getRegion(nameFrom);
		womRegionTeleport objTo = this.getRegion(nameTo);
		
		if((objFrom != null) && (objTo != null)) { 
			objFrom.setDestinationRegionPlayer(namePlayer, objTo);
			this.saveConfig();
			return true;
		}
		return false;
	}
	
	
	/************************************************************
	 * isInsideRegion()
	 ************************************************************/
	public boolean isInsideRegion(Location loc) {
		return this.getManager(loc.getWorld()).isInsideRegion(loc);
	}

	/************************************************************
	 * isEnteringRegion()
	 ************************************************************/
	public String isEnteringRegion(Location locTo, Location locFrom) {
		return this.getManager(locFrom.getWorld()).isEnteringRegion(locTo, locFrom);
	}
	
	/************************************************************
	 * isLeavingRegion()
	 ************************************************************/
	public String isLeavingRegion(Location locTo, Location locFrom) {
		return this.getManager(locFrom.getWorld()).isLeavingRegion(locTo, locFrom);
	}
	
	/************************************************************
	 * handleTeleport()
	 ************************************************************/
	public boolean handleTeleport(String name, Player player) {
		womRegionTeleport region = this.getRegion(name);
		if(region != null) {
			return region.teleportPlayer(player);
		}
		return false;
	}

}
