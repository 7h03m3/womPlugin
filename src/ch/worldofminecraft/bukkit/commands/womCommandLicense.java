package ch.worldofminecraft.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ch.worldofminecraft.bukkit.player.womPlayerLicense;
import ch.worldofminecraft.bukkit.player.womPlayerLicenseManager;
import ch.worldofminecraft.bukkit.test.womPlayerChat;
import ch.worldofminecraft.bukkit.test.womTestPlugin;

public class womCommandLicense {
	private womTestPlugin plugin;
	private womPlayerChat chat;
	private womPlayerLicenseManager licenseManager;

	/************************************************************
	 * Constructor
	 ************************************************************/
	womCommandLicense(womTestPlugin plugin) {
		this.plugin = plugin;
		this.chat = this.plugin.chat;
		this.licenseManager = this.plugin.playerLicenseManager;
	}
	
	/************************************************************
	 * commandList()
	 ************************************************************/
	public void commandList(Player player) {
		this.chat.info(player, "list");
		this.chat.info(player, "info");
		this.chat.info(player, "create");
		this.chat.info(player, "remove");
		this.chat.info(player, "addNode");
		this.chat.info(player, "removeNode");
	}
	
	/************************************************************
	 * commandHandler()
	 ************************************************************/
	public void commandHandler(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length < 2) {
			this.commandList(player);
		} else {
			if(args[1].equals("list")) {
				this.list(player, cmd, label, args);
			} else if(args[1].equals("info")) {
				this.info(player, cmd, label, args);
			} else if(args[1].equals("create")) {
				this.create(player, cmd, label, args);
			} else if(args[1].equals("remove")) {
				this.remove(player, cmd, label, args);
			} else if(args[1].equals("addNode")) {
				this.addNode(player, cmd, label, args);
			} else if(args[1].equals("removeNode")) {
				this.removeNode(player, cmd, label, args);
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
		String listStr = "License list : ";
		String[] array = this.licenseManager.getLicenseNameList();
		
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
			if(this.licenseManager.isLicenseExist(args[2])) {
				womPlayerLicense license = this.licenseManager.getLicense(args[2]);
				this.chat.separator(player);
				this.chat.info(player, "Name : "+license.getName());
				this.chat.info(player, "");
				
				String[] nodes = license.getPermissionNodeArray();
				
				for(int i=0 ; i<nodes.length ; i++) {
					this.chat.info(player, "       "+nodes[i]+" "+license.getPermissionNodeValue(nodes[i]));	
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
			if(this.licenseManager.isLicenseExist(args[2]) == false) {
				if(this.licenseManager.addLicense(args[2])) {
					this.chat.info(player, "license \""+args[2]+"\" successful created");
				} else {
					this.chat.error(player, "license \""+args[2]+"\" create failed");	
				}
			} else {
				this.chat.error(player, "license \""+args[2]+"\" already exist");
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
			if(this.licenseManager.isLicenseExist(args[2]) == true) {
				if(this.licenseManager.removeLicense(args[2])) {
					this.chat.info(player, "license \""+args[2]+"\" successful removed");
				} else {
					this.chat.error(player, "license \""+args[2]+"\" remove failed");	
				}
			} else {
				this.chat.error(player, "license \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * addNode()
	 ************************************************************/
	private void addNode(Player player, Command cmd, String label, String[] args) {	
		if(args.length == 5) {
			if(this.licenseManager.isLicenseExist(args[2]) == true) {
				if(this.licenseManager.isPermissionNodeExist(args[2], args[3]) == false) {
					if((args[4].equals("true")) || (args[4].equals("false"))) {
						boolean value = false;
						
						if(args[4].equals("true"))
							value = true;
						
						if(this.licenseManager.getLicense(args[2]).addPermissionNode(args[3], value)) {
							this.chat.error(player, "permission node \""+args[3]+"\" successful added to license \""+args[2]+"\"");
						} else {
							this.chat.error(player, "value of permission node \""+args[3]+"\" not valid");	
						}
					} else {
						this.chat.error(player, "value of permission node \""+args[3]+"\" not valid");	
					}
					
				} else {
					this.chat.error(player, "permission node \""+args[3]+"\" already exist");	
				}
			} else {
				this.chat.error(player, "license \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * removeNode()
	 ************************************************************/
	private void removeNode(Player player, Command cmd, String label, String[] args) {	
		if(args.length == 5) {
			if(this.licenseManager.isLicenseExist(args[2]) == true) {
				if(this.licenseManager.isPermissionNodeExist(args[2], args[3]) == true) {
					
					if(this.licenseManager.getLicense(args[2]).removePermissionNode(args[3])) {
						this.chat.error(player, "permission node \""+args[3]+"\" successful added to license \""+args[2]+"\"");
					} else {
						this.chat.error(player, "value of permission node \""+args[3]+"\" not valid");	
					}
				} else {
					this.chat.error(player, "permission node \""+args[3]+"\" does not exist");	
				}
			} else {
				this.chat.error(player, "license \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
}
