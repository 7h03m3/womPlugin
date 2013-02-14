package ch.worldofminecraft.bukkit.commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ch.worldofminecraft.bukkit.region.womRegionTeleport;
import ch.worldofminecraft.bukkit.test.womTestPlugin;
import ch.worldofminecraft.bukkit.test.womPlayerChat;

public class womCommandTeleport {
	
	private womTestPlugin plugin;
	private womPlayerChat chat;

	/************************************************************
	 * Constructor
	 ************************************************************/
	womCommandTeleport(womTestPlugin plugin) {
		this.plugin = plugin;
		this.chat = this.plugin.chat;
	}
	
	/************************************************************
	 * commandList()
	 ************************************************************/
	public void commandList(Player player) {
		this.chat.info(player, "list");
		this.chat.info(player, "create");
		this.chat.info(player, "remove");
		this.chat.info(player, "link");
		this.chat.info(player, "linkPlayer");
		this.chat.info(player, "setArriveLocation");
		this.chat.info(player, "activate");
		this.chat.info(player, "deactivate");
	}
	
	/************************************************************
	 * commandHandler()
	 ************************************************************/
	public void commandHandler(CommandSender sender, Command cmd, String label, String[] args, Location pos1, Location pos2) {
		Player player = (Player) sender;
		
		if(args.length < 2) {
			this.commandList(player);
		} else {
			if(args[1].equals("list")) {
				this.list(player, cmd, label, args);
			} else if(args[1].equals("info")) {
				this.info(player, cmd, label, args);
			} else if(args[1].equals("create")) {
				this.create(player, cmd, label, args, pos1, pos2);
			} else if(args[1].equals("remove")) {
				this.remove(player, cmd, label, args);
			} else if(args[1].equals("link")) {
				this.linkRegion(player, cmd, label, args);
			} else if(args[1].equals("linkPlayer")) {
				this.linkRegionPlayer(player, cmd, label, args);
			} else if(args[1].equals("setArriveLocation")) {
				this.setArriveLocation(player, cmd, label, args, pos1);
			} else if(args[1].equals("activate")) {
				this.activate(player, cmd, label, args);
			} else if(args[1].equals("deactivate")) {
				this.deactivate(player, cmd, label, args);
			} else {
				this.chat.error(player, "unkown command");
				this.commandList(player);
			}
		}
	}
	
	/************************************************************
	 * list()
	 ************************************************************/
	private void list(Player player, Command cmd, String label, String[] args) {	
		String listStr = "Teleport region list : ";
		String[] array = this.plugin.regionManager.getRegionNames();
		
		for(int i=0 ; i<array.length-1 ; i++) {
			listStr = listStr + array[i] + ", ";
		}
		listStr = listStr + array[array.length-1];
		
		this.chat.info(player, listStr);
	}
	
	/************************************************************
	 * info()
	 ************************************************************/
	private void info(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			if(this.plugin.regionManager.isRegionExist(args[2])) {
				womRegionTeleport teleport = this.plugin.regionManager.getRegion(args[2]);
				String tmpStr;
				Location loc = teleport.getArriveLocation();
				womRegionTeleport dstTeleport = teleport.getDestinationRegion();
				
				this.chat.separator(player);
				this.chat.info(player, "Name : "+teleport.getName());
				
				if(teleport.isActive()) {
					tmpStr = "enabled";
				} else {
					tmpStr = "disabled";
				}
				this.chat.info(player, "Status : "+tmpStr);
				
				if(dstTeleport != null) {
					tmpStr = dstTeleport.getName();
				} else {
					tmpStr = "none";
				}
				this.chat.info(player, "Destination : "+tmpStr);
				
				if(loc != null) {
					tmpStr = "x="+loc.getBlockX()+" y="+loc.getBlockY()+" z="+loc.getBlockZ();
				} else {
					tmpStr = "none";
				}
				this.chat.info(player, "Arrive Position : "+tmpStr);
				
				HashMap<String, womRegionTeleport> dstTeleportPlayer = teleport.getDestinationRegionPlayerHashMap();
				
				if(dstTeleportPlayer.isEmpty() == false) {
					tmpStr = "Player Teleport : ";
					for ( String name: dstTeleportPlayer.keySet() ) {
						this.chat.info(player, tmpStr+name+" => "+dstTeleportPlayer.get(name).getName());	
						tmpStr = "                  ";
					}
				}
				
				this.chat.separator(player);
			} else {
				this.chat.error(player, "\""+args[2]+"\" doesn't exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * create()
	 ************************************************************/
	private void create(Player player, Command cmd, String label, String[] args, Location edge1, Location edge2) {
		if(args.length == 3) {
			if(edge1 == null) 
				this.chat.error(player, "Position 1 is not set");			
			if(edge2 == null) 
				this.chat.error(player, "Position 2 is not set");
			
			if(this.plugin.regionManager.addRegion(args[2], edge1, edge2) != null) {
				this.chat.info(player, "Teleport region \""+args[2]+"\" created");
			} else {
				this.chat.error(player, "Could not create teleport region \""+args[2]+"\"");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * remove()
	 ************************************************************/
	private void remove(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			this.plugin.regionManager.removeRegion(args[2]);
			this.chat.info(player, "Teleport region \""+args[2]+"\" removed");
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * setArrivePosition()
	 ************************************************************/
	private void setArriveLocation(Player player, Command cmd, String label, String[] args, Location loc1) {
		if(args.length == 3) {
			if(loc1 == null) 
				this.chat.error(player, "Position 1 is not set");			
			
			if(this.plugin.regionManager.isRegionExist(args[2]) == true) { 
				if(this.plugin.regionManager.setArriveLocation(args[2], loc1) == true) {
					this.chat.info(player, "Arrive position set");
				} else {
					this.chat.error(player, "Could not set arrive position");	
				}
			} else {
				this.chat.error(player, "Teleport Region does not exist");
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * linkTeleport()
	 ************************************************************/
	private void linkRegion(Player player, Command cmd, String label, String[] args) {
		if(args.length == 4) {
			if(this.plugin.regionManager.linkRegion(args[2], args[3]) == true) {
				this.chat.info(player, "Teleport "+args[2]+" linked with "+args[3]);
			} else {
				this.chat.error(player, "Teleport "+args[2]+" could not linked with "+args[3]);
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * linkTeleportPlayer()
	 ************************************************************/
	private void linkRegionPlayer(Player player, Command cmd, String label, String[] args) {
		if(args.length == 5) {
			if(this.plugin.regionManager.linkRegionPlayer(args[2], args[3], args[4]) == true) {
				this.chat.info(player, "Teleport "+args[2]+" linked with "+args[3]+" for "+args[4]);
			} else {
				this.chat.error(player, "Teleport "+args[2]+" could not linked with "+args[3]+" for "+args[4]);
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * activate()
	 ************************************************************/
	private void activate(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			if(this.plugin.regionManager.activate(args[2]) == true) {
				this.chat.info(player, "Teleport region activated");
			} else {
				this.chat.error(player, "Could not activate teleport region");
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * deactivate()
	 ************************************************************/
	private void deactivate(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			this.plugin.regionManager.deactivate(args[2]);
			this.chat.info(player, "Teleport region deactivated");
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
}
