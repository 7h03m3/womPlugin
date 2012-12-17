package ch.worldofminecraft.bukkit.listeners;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womPlayerListener implements Listener {
	
	private final womTestPlugin plugin;
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womPlayerListener (womTestPlugin plugin) {
		this.plugin = plugin;
	}
	
	/************************************************************
	 * onPlayerMove()
	 ************************************************************/
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location locTo = event.getTo();
		Location locFrom = event.getFrom();
		
		if((locTo.getBlockX() != locFrom.getBlockX()) || 
		   (locTo.getBlockZ() != locFrom.getBlockZ()) || 
		   (locTo.getBlockY() != locFrom.getBlockY())) {
			
			String name = this.plugin.regionManager.isEnteringRegion(locTo,locFrom);
			if(name != null) {
				if(this.plugin.regionManager.handleTeleport(name, player) == true) {
					player.sendMessage(ChatColor.BLUE+"Teleport was successful");
				}
				
			} 
		}
	}
}
