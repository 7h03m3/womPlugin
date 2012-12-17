package ch.worldofminecraft.bukkit.test;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import ch.worldofminecraft.bukkit.commands.womCommandExecutor;
import ch.worldofminecraft.bukkit.listeners.womPlayerListener;
import ch.worldofminecraft.bukkit.region.womRegionTeleportManager;

public final class womTestPlugin extends JavaPlugin {
	
	private womLogger logger;
	private womPlayerListener playerListener;
	public womRegionTeleportManager regionManager;
	public womPlayerChat chat;
	
	/************************************************************
	 * onEnable()
	 ************************************************************/
	public void onEnable() {
		
		// Initialize logger
		this.logger = new womLogger();
		
		// Initialize player event listener
		this.playerListener = new womPlayerListener(this);
		
		// Chat class
		this.chat = new womPlayerChat();
		
		// Initialize region Manager
		this.regionManager = new womRegionTeleportManager(this, getServer().getWorld("flat_world"));
		
		// Initialize command executors
		getCommand("testPlugin").setExecutor(new womCommandExecutor(this));
		
		// Register event handlers
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(this.playerListener, this);
		
		info("onEnable()");
	}
	
	/************************************************************
	 * onDisable()
	 ************************************************************/
	public void onDisable() {
		info("onDisable()");		
	}
	
	/************************************************************
	 * info()
	 ************************************************************/
	public void info(String msg) {
		this.logger.info(msg);
	}
}
