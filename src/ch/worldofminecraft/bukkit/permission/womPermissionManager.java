package ch.worldofminecraft.bukkit.permission;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womPermissionManager {
	private womTestPlugin plugin;
	private HashMap<String, PermissionAttachment> attachedPlayer = new HashMap<String, PermissionAttachment>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womPermissionManager(womTestPlugin plugin) {
		this.plugin = plugin;
	}
	
	/************************************************************
	 * getAttachment
	 ************************************************************/
	private PermissionAttachment  getAttachment(Player player) {
		if(this.isPlayerAttached(player) == true) 
			return null;
		
		return this.attachedPlayer.get(player.getName());
	}
	
	/************************************************************
	 * isPlayerAttached
	 ************************************************************/
	public boolean isPlayerAttached(Player player) {
		return this.attachedPlayer.containsKey(player.getName());
	}
	
	/************************************************************
	 * attachPlayer
	 ************************************************************/
	public boolean attachPlayer(Player player) {
		if(this.isPlayerAttached(player) == true) 
			return false;
		
		this.attachedPlayer.put(player.getName(), player.addAttachment(this.plugin));
		
		return true;
	}
	
	/************************************************************
	 * deattachPlayer
	 ************************************************************/
	public boolean deattachPlayer(Player player) {
		if(this.isPlayerAttached(player) == false) 
			return false;
		
		player.removeAttachment(this.attachedPlayer.get(player.getName()));
		this.attachedPlayer.remove(player.getName());
		return true;
	}
	
	/************************************************************
	 * setPermissionNode
	 ************************************************************/
	public boolean setPermissionNode(Player player, String node, boolean value) {
		if(this.isPlayerAttached(player) == false) {
			if(this.attachPlayer(player) == false) 
				return false;
		}			
		
		this.getAttachment(player).setPermission(node, value);
		
		return true;
	}
}
