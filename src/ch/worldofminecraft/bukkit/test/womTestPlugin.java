package ch.worldofminecraft.bukkit.test;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import ch.worldofminecraft.bukkit.commands.womCommandExecutor;
import ch.worldofminecraft.bukkit.listeners.womPlayerListener;
import ch.worldofminecraft.bukkit.region.womRegionTeleportManager;
import ch.worldofminecraft.bukkit.player.womPlayerLicenseManager;
import ch.worldofminecraft.bukkit.player.womPlayerManager;
import ch.worldofminecraft.bukkit.permission.womPermissionManager;

public final class womTestPlugin extends JavaPlugin {
	
	private womLogger logger;
	private womPlayerListener playerListener;
	public womRegionTeleportManager regionManager;
	public womPlayerLicenseManager playerLicenseManager;
	public womPlayerManager playerManager;
	public womPermissionManager permissionManager;
	public womPlayerChat chat;
	
	/************************************************************
	 * onEnable()
	 ************************************************************/
	public void onEnable() {
		
		// Initialize logger
		this.logger = new womLogger();
		
		// Initialize permission manager
		this.permissionManager = new womPermissionManager(this);
		
		// Initialize license manager
		this.playerLicenseManager = new womPlayerLicenseManager(this);
		
		// Initialize player manager
		this.playerManager = new womPlayerManager(this);
		
		// Initialize player event listener
		this.playerListener = new womPlayerListener(this);
		
		// Chat class
		this.chat = new womPlayerChat();
		
		// Initialize region Manager
		this.regionManager = new womRegionTeleportManager(this);
		
		// Initialize command executors
		getCommand("testPlugin").setExecutor(new womCommandExecutor(this));
		
		// Register event handlers
		final PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(this.playerListener, this);
		
		info("onEnable() ");
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
