package ch.worldofminecraft.bukkit.player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import ch.worldofminecraft.bukkit.test.womTestPlugin;
import ch.worldofminecraft.bukkit.configuration.womConfiguration;

public class womPlayerLicenseManager {
	private womTestPlugin plugin;
	private womConfiguration configuration;
	private HashMap<String, womPlayerLicense> licenseMap = new HashMap<String, womPlayerLicense>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womPlayerLicenseManager(womTestPlugin plugin) {
		this.plugin = plugin;
		
		// Initialize configuration class
		this.configuration = new womConfiguration(this.plugin, "license.yml");
		this.configuration.setSeparator('_');		
		
		// load configuration
		if(this.loadConfig() == false)
			this.plugin.getLogger().log(Level.SEVERE, "License manager could not load configuration");
			
	}
	
	/************************************************************
	 * saveConfig()
	 ************************************************************/
	private boolean saveConfig() {
		if(this.configuration.isWriteLocked()) 
			return false;
		
		if(this.configuration.loadConfig() == false) 
			return false;
		
		for(womPlayerLicense licenseObj: this.licenseMap.values()) {
			this.plugin.getLogger().info(licenseObj.getName());
			String path = licenseObj.getName()+this.configuration.getSeparator()+"permissionNodes";
			
			this.configuration.getConfig().createSection(path, licenseObj.getPermissionNodeMap());
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
		
		// License loop
		Iterator<String> licenseIterator = config.getKeys(false).iterator();
		while(licenseIterator.hasNext()) {
			String licenseName = licenseIterator.next();
			
			if(this.addLicense(licenseName)) {
				womPlayerLicense license = this.getLicense(licenseName);
				String path = licenseName+this.configuration.getSeparator()+"permissionNodes";
				
				// read permission nodes
				configSection = config.getConfigurationSection(path);	
				Iterator<String> nodeIterator = configSection.getKeys(false).iterator();
				while(nodeIterator.hasNext()) {
					String node = nodeIterator.next();
					license.addPermissionNode(node, configSection.getBoolean(node));
				}
			}
		}
		
		this.configuration.unlockWrite();
		
		return true;
	}
	
	/************************************************************
	 * isLicenseExist()
	 ************************************************************/
	public boolean isLicenseExist(String name) {
		return this.licenseMap.containsKey(name);
	}
	
	/************************************************************
	 * addLicense()
	 ************************************************************/
	public boolean addLicense(String name) {
		if(this.isLicenseExist(name) == true)
			return false;
		
		this.licenseMap.put(name, new womPlayerLicense(name));
		this.saveConfig();
		return true;
	}
	
	/************************************************************
	 * removeLicense()
	 ************************************************************/
	public boolean removeLicense(String name) {
		if(this.isLicenseExist(name) == false)
			return false;
		
		this.licenseMap.remove(name);
		this.saveConfig();
		return true;
	}
	
	/************************************************************
	 * getLicense()
	 ************************************************************/
	public womPlayerLicense getLicense(String name) {
		if(this.isLicenseExist(name) == false)
			return null;
		
		return this.licenseMap.get(name);
	}
	
	/************************************************************
	 * getLicenseNameList()
	 ************************************************************/
	public String[] getLicenseNameList() {
		String[] arr = new String[this.licenseMap.size()];
		
		int i=0;
		for(String name: this.licenseMap.keySet()) {
			arr[i] = name;
			i++;
		}
		return arr;
	}
	
	/************************************************************
	 * isPermissionNodeExist()
	 ************************************************************/
	public boolean isPermissionNodeExist(String licenseName, String permissionNode) {
		if(this.isLicenseExist(licenseName) == false) 
			return false;
		
		return this.getLicense(licenseName).isPermissionNodeExist(permissionNode);
	}
	
	/************************************************************
	 * addPermissionNode()
	 ************************************************************/
	public boolean addPermissionNode(String licenseName, String permissionNode, Boolean value) {
		boolean ret;
		
		if(this.isLicenseExist(licenseName) == false) 
			return false;
		
		ret = this.getLicense(licenseName).addPermissionNode(permissionNode, value);
		this.saveConfig();
		return ret;
	}
	
	/************************************************************
	 * removePermissionNode()
	 ************************************************************/
	public boolean removePermissionNode(String licenseName, String permissionNode) {
		boolean ret;
		
		if(this.isLicenseExist(licenseName) == false) 
			return false;
		
		ret = this.licenseMap.get(licenseName).removePermissionNode(permissionNode);
		this.saveConfig();
		return ret;
	}
	
	/************************************************************
	 * getPermissionNodeMap()
	 ************************************************************/
	public HashMap<String, Boolean> getPermissionNodeMap(String licenseName) {
		if(this.isLicenseExist(licenseName) == false) 
			return null;
		
		return this.getLicense(licenseName).getPermissionNodeMap();
	}
}
