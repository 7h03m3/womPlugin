package ch.worldofminecraft.bukkit.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ch.worldofminecraft.bukkit.configuration.womConfiguration;
import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womPlayerManager {
	private womTestPlugin plugin;
	private womConfiguration configuration;
	private HashMap<String, womPlayer> playerMap = new HashMap<String, womPlayer>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womPlayerManager(womTestPlugin plugin) {
		this.plugin = plugin;
		
		// Initialize configuration class
		this.configuration = new womConfiguration(this.plugin, "player.yml");
		
		// load configuration
		if(this.loadConfig() == false) 
			this.plugin.getLogger().log(Level.SEVERE, "PlayerManager could not load configuration");
		
		this.setPlayerLicense("stth", "dummy");
	}
	
	/************************************************************
	 * saveConfig()
	 ************************************************************/
	public boolean saveConfig() {
		if(this.configuration.isWriteLocked()) 
			return false;
		
		if(this.configuration.loadConfig() == false) 
			return false;
		
		YamlConfiguration config = this.configuration.getConfig();
		
		for(womPlayer playerObj: this.playerMap.values()) {
			config.set(playerObj.getName()+".state",playerObj.isOnline());
			config.set(playerObj.getName()+".license", playerObj.getLicenseList());
		}
		
		if(this.configuration.saveConfig()) 
			return false;
		
		return true;
	}
	
	/************************************************************
	 * loadConfig()
	 ************************************************************/
	public boolean loadConfig() {
		
		this.configuration.lockWrite();
		
		if(this.configuration.loadConfig() == false) 
			return false;
		
		ConfigurationSection configSection;
		YamlConfiguration config = this.configuration.getConfig();
		
		Iterator<String> playerIterator = config.getKeys(false).iterator();
		while(playerIterator.hasNext()) {
			String playerName = playerIterator.next();
			configSection = config.getConfigurationSection(playerName);
			
			this.addPlayer(playerName);
			
			if(configSection.getBoolean("state")) {
				this.getPlayer(playerName).setOnline();
			}
			
			Iterator<String> licenseIterator = configSection.getStringList("license").iterator();
			while(licenseIterator.hasNext()) {
				this.getPlayer(playerName).addLicense(licenseIterator.next());
			}
		}
		
		this.configuration.unlockWrite();
		return true;
	}
	
	/************************************************************
	 * isPlayerExist()
	 ************************************************************/
	public boolean isPlayerExist(String name) {
		return this.playerMap.containsKey(name); 
	}
	
	/************************************************************
	 * getPlayer()
	 ************************************************************/
	public womPlayer getPlayer(String name) {
		if(this.isPlayerExist(name) == true) {
			return this.playerMap.get(name);
		}
		return null;
	}
	
	/************************************************************
	 * addPlayer()
	 ************************************************************/
	public boolean addPlayer(String name) {
		if(this.isPlayerExist(name) == true) {
			return false;
		}
		
		this.playerMap.put(name, new womPlayer(name));
		this.saveConfig();
		return true;
	}
	
	/************************************************************
	 * removePlayer()
	 ************************************************************/
	public boolean removePlayer(String name) {
		if(this.isPlayerExist(name) == true) {
			return false;
		}
		
		this.playerMap.remove(name);
		this.saveConfig();
		return true;
	}
	
	/************************************************************
	 * isOnline()
	 ************************************************************/
	public boolean isOnline(Player player) {
		String playerName = player.getName();
		
		if(this.isPlayerExist(playerName) == false) {
			return false;
		}
		
		return this.getPlayer(playerName).isOnline();
	}
	
	/************************************************************
	 * setOnline()
	 ************************************************************/
	public void setOnline(Player player) {
		String playerName = player.getName();
		
		if(this.isPlayerExist(playerName) == false) {
			this.addPlayer(playerName);
		}
		
		this.getPlayer(playerName).setOnline();
		this.saveConfig();
		
		// attach player to permission class & load plugin specific permissions
		if(this.plugin.permissionManager.attachPlayer(player) == false) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not attach player to permission class");
		} else {
			// load player specific license
			if(this.getPlayer(playerName).getLicenseCount() > 0) {
				String[] licenseArray = this.getPlayer(playerName).getLicenseArray();
				
				// license loop
				for(int i=0 ; i<licenseArray.length ; i++) {
					HashMap<String, Boolean> nodeMap = this.plugin.playerLicenseManager.getPermissionNodeMap(licenseArray[i]);
					
					// permission node loop
					for(String nodeName : nodeMap.keySet()) {
						// set permissions
						this.plugin.permissionManager.setPermissionNode(player, nodeName, nodeMap.get(nodeName));
					}
				}
			}
			
		}
	}
	
	/************************************************************
	 * setOffline()
	 ************************************************************/
	public void setOffline(Player player) {
		String playerName = player.getName();

		if(this.isPlayerExist(playerName) == false) {
			this.addPlayer(playerName);
		}
		
		this.getPlayer(playerName).setOffline();
		this.saveConfig();
		
		// deattach player to permission class
		if(this.plugin.permissionManager.deattachPlayer(player) == false)
			this.plugin.getLogger().log(Level.SEVERE, "Could not deattach player to permission class");
	}
	
	/************************************************************
	 * hasPlayerLicense()
	 ************************************************************/
	public boolean hasPlayerLicense(String name, String licenseName) {
		if(this.isPlayerExist(name) == false) 
			return false;
		
		return this.getPlayer(name).hasLicense(licenseName); 
	}
	
	/************************************************************
	 * setPlayerLicense()
	 ************************************************************/
	public boolean setPlayerLicense(String name, String licenseName) {
		boolean ret;
		if(this.isPlayerExist(name) == false)
			return false;
		
		ret = this.getPlayer(name).addLicense(licenseName);
		this.saveConfig();
		return ret;
	}
	
	/************************************************************
	 * removePlayerLicense()
	 ************************************************************/
	public boolean removePlayerLicense(String name, String licenseName) {
		boolean ret;
		if(this.isPlayerExist(name) == false)
			return false;
		
		ret = this.getPlayer(name).removeLicense(licenseName);
		this.saveConfig();
		return ret;
	}
	
}
