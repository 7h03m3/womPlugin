package ch.worldofminecraft.bukkit.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womConfiguration {
	private womTestPlugin plugin;
	private String fileName;
	private File configFile;
	private boolean fileExist = false;
	private boolean lockWrite = false;
	private char separator = '.';
	private YamlConfiguration config = new YamlConfiguration(); 
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womConfiguration(womTestPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		
		this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
		
		// create new file if none exist
		if(this.configFile.exists() == false) {
			this.fileExist = true;
			try {
				this.configFile.createNewFile();
			} catch (IOException ex) {
				this.plugin.getLogger().log(Level.SEVERE, "create new file \""+this.fileName+"\" failed");
				this.fileExist = false;
			}
		} else {
			this.fileExist = true;
		}
	}
	
	/************************************************************
	 * isWriteLocked
	 ************************************************************/
	public boolean isWriteLocked() {
		return this.lockWrite;
	}

	/************************************************************
	 * lockWrite
	 ************************************************************/
	public void lockWrite() {
		this.lockWrite = true;
	}
	
	/************************************************************
	 * unlockWrite
	 ************************************************************/
	public void unlockWrite() {
		this.lockWrite = false;
	}
	
	/************************************************************
	 * getFileName
	 ************************************************************/
	public String getFileName() {
		return this.fileName;
	}
	
	/************************************************************
	 * getSeparator
	 ************************************************************/
	public char getSeparator() {
		return this.separator;
	}
	
	/************************************************************
	 * setSeparator
	 ************************************************************/
	public void setSeparator(char separator) {
		this.separator = separator;
		this.config.options().pathSeparator(this.separator);
	}
	
	/************************************************************
	 * getConfig
	 ************************************************************/
	public YamlConfiguration getConfig() {
		return this.config;
	}
	
	/************************************************************
	 * loadConfig
	 ************************************************************/
	public boolean loadConfig() {
		boolean ret = true;
		
		if(this.fileExist == true) { 
			try {
				this.config.load(this.configFile);
			} catch (FileNotFoundException e) {
				this.plugin.getLogger().log(Level.SEVERE, "FileNotFoundException");
				ret = false;
			} catch (IOException e) {
				this.plugin.getLogger().log(Level.SEVERE, "IOException");
				ret = false;
			} catch (InvalidConfigurationException e) {
				this.plugin.getLogger().log(Level.SEVERE, "InvalidConfigurationException");
				ret = false;
			}
		}
		return ret;
	}
	
	/************************************************************
	 * saveConfig
	 ************************************************************/
	public boolean saveConfig() {
		boolean ret = true;
		
		if(this.fileExist == true) { 
			try {
			        this.config.save(this.configFile);
			} catch (IOException ex) {
			        this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
			        ret = false;
			}
		}
		return ret;
	}
	
	/************************************************************
	 * setLocation
	 ************************************************************/
	public void setLocation(String path, Location loc) {
		this.config.set(path+"."+"x",loc.getBlockX());
		this.config.set(path+"."+"y",loc.getBlockY());
		this.config.set(path+"."+"z",loc.getBlockZ());
	}
	
	/************************************************************
	 * getLocation
	 ************************************************************/
	public Location getLocation(String path, World world) {
		int x = config.getInt(path+"."+"x");
		int y = config.getInt(path+"."+"y");
		int z = config.getInt(path+"."+"z");
		
		return new Location(world, x, y, z);
	}
}
