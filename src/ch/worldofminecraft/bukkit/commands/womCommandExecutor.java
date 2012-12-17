package ch.worldofminecraft.bukkit.commands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womCommandExecutor implements CommandExecutor {
	
	private womTestPlugin plugin; 
	private womCommandTeleport cmdTeleport;
	private Location pos1=null, pos2=null;
	 
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womCommandExecutor(womTestPlugin plugin) {
		this.plugin = plugin;
		this.cmdTeleport = new womCommandTeleport(plugin);
	}
 
	/************************************************************
	 * onCommand()
	 ************************************************************/
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
	           Player player = (Player) sender;
	           
	           if(args.length == 0) {
	        	   return false;
	           }
	           
	           /* Show arguments */
	           /*
	           sender.sendMessage("args.length = "+args.length);
	           for(int i=0 ; i<args.length ; i++) {
	        	   sender.sendMessage("args["+i+"] = "+args[i]);   
	           }
	           */
	           
	           if(args[0].equals("getPos")) {
	        	   this.getPos(player, cmd, label, args);
	           } else if(args[0].equals("setPos1")) {
	        	   this.setPos1(player, cmd, label, args);
	           } else if(args[0].equals("setPos2")) {
	        	   this.setPos2(player, cmd, label, args);
	           } else if(args[0].equals("resetPos")) {
	        	   this.resetPos(player, cmd, label, args);
	           } else if(args[0].equals("teleport")) {
	        	   this.cmdTeleport.commandHandler(sender, cmd, label, args, this.pos1, this.pos2);
	           }
	           
	           return true;
	        } else {
	           sender.sendMessage("You must be a player!");
	        }
		
	        return false;
	}

	
	/************************************************************
	 * getPos()
	 ************************************************************/
	public void getPos(Player player, Command cmd, String label, String[] args) {
	   Location loc = player.getLocation();
 	   World world = loc.getWorld();
 	   Chunk chunk = loc.getChunk();
 	   
	   this.plugin.chat.info(player, "World  : "+world.getName());
 	   this.plugin.chat.info(player, "Chunk  : x="+chunk.getX()+" z="+chunk.getZ());
 	   this.plugin.chat.info(player, "Coords : x="+loc.getBlockX()+" y="+loc.getBlockY()+" z="+loc.getBlockZ());
	}
	
	/************************************************************
	 * setPos1()
	 ************************************************************/
	public void setPos1(Player player, Command cmd, String label, String[] args) {
	   this.pos1 = player.getLocation();
 	   
	   this.plugin.chat.info(player, "Position 1 set to : "+"x="+this.pos1.getBlockX()+" y="+this.pos1.getBlockY()+" z="+this.pos1.getBlockZ());
	}
	
	/************************************************************
	 * setPos2()
	 ************************************************************/
	public void setPos2(Player player, Command cmd, String label, String[] args) {
	   this.pos2 = player.getLocation();
 	   
	   this.plugin.chat.info(player, "Position 2 set to : "+"x="+this.pos2.getBlockX()+" y="+this.pos2.getBlockY()+" z="+this.pos2.getBlockZ());
	}
	
	/************************************************************
	 * resetPos()
	 ************************************************************/
	public void resetPos(Player player, Command cmd, String label, String[] args) {
	   this.pos1 = null;
	   this.pos2 = null;
 	   
	   this.plugin.chat.info(player, "Position 1 & 2 reseted");
	}
}
