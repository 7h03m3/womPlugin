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
	 * commandHandler()
	 ************************************************************/
	public void commandHandler(CommandSender sender, Command cmd, String label, String[] args, Location pos1, Location pos2) {
		Player player = (Player) sender;
		
		if(args.length < 2) {
			this.chat.info(player, "Show command list");
		} else {
			if(args[1].equals("list")) {
				this.list(player, cmd, label, args);
			} else if(args[1].equals("info")) {
				this.info(player, cmd, label, args);
			} else if(args[1].equals("create")) {
				this.create(player, cmd, label, args);
			} else if(args[1].equals("remove")) {
				this.remove(player, cmd, label, args);
			} else if(args[1].equals("setRegion")) {
				this.setRegion(player, cmd, label, args, pos1, pos2);
			} else if(args[1].equals("link")) {
				this.linkTeleport(player, cmd, label, args);
			} else if(args[1].equals("linkPlayer")) {
				this.linkTeleportPlayer(player, cmd, label, args);
			} else if(args[1].equals("setArrivePos")) {
				this.setArrivePosition(player, cmd, label, args, pos1);
			} else if(args[1].equals("activate")) {
				this.activate(player, cmd, label, args);
			} else if(args[1].equals("deactivate")) {
				this.deactivate(player, cmd, label, args);
			} else {
				this.chat.error(player, "unkown command");
			}
		}
	}
	
	/************************************************************
	 * list()
	 ************************************************************/
	private void list(Player player, Command cmd, String label, String[] args) {	
		String listStr = "Teleport list : ";
		womRegionTeleport[] array = this.plugin.regionManager.getRegionArray();
		
		for(int i=0 ; i<array.length-1 ; i++) {
			listStr = listStr + array[i].getName() + ", ";
		}
		listStr = listStr + array[array.length-1].getName();
		
		this.chat.info(player, listStr);
	}
	
	/************************************************************
	 * info()
	 ************************************************************/
	private void info(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			womRegionTeleport teleport = this.plugin.regionManager.getRegion(args[2]);
			if(teleport != null) {
				String tmpStr;
				Location loc = teleport.getArrivePosition();
				womRegionTeleport dstTeleport = teleport.getDestinationTeleport();
				
				this.chat.separator(player);
				this.chat.info(player, "Name              : "+args[2]);
				
				if(dstTeleport != null) {
					tmpStr = dstTeleport.getName();
				} else {
					tmpStr = "none";
				}
				this.chat.info(player, "Destination      : "+tmpStr);
				
				if(loc != null) {
					tmpStr = "x="+loc.getBlockX()+" y="+loc.getBlockY()+" z="+loc.getBlockZ();
				} else {
					tmpStr = "none";
				}
				this.chat.info(player, "Arrive Position : "+tmpStr);
				
				HashMap<String, womRegionTeleport> dstTeleportPlayer = teleport.getDestinationTeleportPlayerHashMap();
				
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
	private void create(Player player, Command cmd, String label, String[] args) {
		if(args.length == 3) {
			if(this.plugin.regionManager.addRegion(args[2]) != null) {
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
			if(this.plugin.regionManager.removeRegion(args[2])) {
				this.chat.info(player, "Teleport region \""+args[2]+"\" removed");
			} else {
				this.chat.error(player, "Could not remove teleport region \""+args[2]+"\"");
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * setRegion()
	 ************************************************************/
	private void setRegion(Player player, Command cmd, String label, String[] args, Location edge1, Location edge2) {
		if(args.length == 3) {
			if(edge1 == null) 
				this.chat.error(player, "Position 1 is not set");			
			if(edge2 == null) 
				this.chat.error(player, "Position 2 is not set");
			
			womRegionTeleport teleportTmp = this.plugin.regionManager.getRegion(args[2]);
			if(teleportTmp != null) {
				teleportTmp.setRegion(edge1, edge2);
				this.chat.info(player, "Teleport region set");
			} else {
				this.chat.error(player, "Teleport region does not exist");
			}
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * setArrivePosition()
	 ************************************************************/
	private void setArrivePosition(Player player, Command cmd, String label, String[] args, Location loc1) {
		if(args.length == 3) {
			if(loc1 == null) 
				this.chat.error(player, "Position 1 is not set");			
			
			womRegionTeleport teleportTmp = this.plugin.regionManager.getRegion(args[2]);
			if(teleportTmp != null) {
				if(teleportTmp.setArrivePosition(loc1) == true) {
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
	private void linkTeleport(Player player, Command cmd, String label, String[] args) {
		if(args.length == 4) {
			if(this.plugin.regionManager.linkTeleport(args[2], args[3]) == true) {
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
	private void linkTeleportPlayer(Player player, Command cmd, String label, String[] args) {
		if(args.length == 5) {
			if(this.plugin.regionManager.linkTeleportPlayer(args[2], args[3], args[4]) == true) {
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
			womRegionTeleport teleportTmp = this.plugin.regionManager.getRegion(args[2]);
			
			if(teleportTmp.setActive() == true) {
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
			womRegionTeleport teleportTmp = this.plugin.regionManager.getRegion(args[2]);
			
			teleportTmp.setInactive();
			this.chat.info(player, "Teleport region deactivated");
		} else {
			this.chat.error(player, "Amount of arguments does not match");
		}
	}
}
