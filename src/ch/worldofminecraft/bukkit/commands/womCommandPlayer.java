package ch.worldofminecraft.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ch.worldofminecraft.bukkit.test.womPlayerChat;
import ch.worldofminecraft.bukkit.test.womTestPlugin;
import ch.worldofminecraft.bukkit.player.womPlayer;
import ch.worldofminecraft.bukkit.player.womPlayerManager;
import ch.worldofminecraft.bukkit.player.womPlayerLicenseManager;

public class womCommandPlayer {
	private womTestPlugin plugin;
	private womPlayerChat chat;
	private womPlayerManager playerManager;
	private womPlayerLicenseManager licenseManager;

	/************************************************************
	 * Constructor
	 ************************************************************/
	public womCommandPlayer(womTestPlugin plugin) {
		this.plugin = plugin;
		this.chat = this.plugin.chat;
		this.playerManager = this.plugin.playerManager;
		this.licenseManager = this.plugin.playerLicenseManager;
	}
	
	/************************************************************
	 * commandList()
	 ************************************************************/
	public void commandList(Player player) {
		this.chat.info(player, "list");
		this.chat.info(player, "info");
		this.chat.info(player, "addLicense");
		this.chat.info(player, "removeLicense");
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
			} else if(args[1].equals("addLicense")) {
				this.addLicense(player, cmd, label, args);
			} else if(args[1].equals("removeLicense")) {
				this.removeLicense(player, cmd, label, args);
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
	}
	
	/************************************************************
	 * info()
	 ************************************************************/
	private void info(Player player, Command cmd, String label, String[] args) {	
		if(args.length == 3) {
			if(this.playerManager.isPlayerExist(args[2])) {
				String tmpStr;
				womPlayer internalPlayer = this.playerManager.getPlayer(args[2]);
				
				this.chat.separator(player);
				this.chat.info(player, "Name : "+internalPlayer.getName());
				
				
				String[] license = internalPlayer.getLicenseArray();
				if(license.length > 0) {
					tmpStr = license[0];
					for(int i=1 ; i<license.length ; i++) {
						tmpStr = tmpStr+", "+license[i];
					}
				} else {
					tmpStr = "none";
				}
				this.chat.info(player, "License(s) : "+tmpStr);
				
				this.chat.separator(player);
			} else {
				this.chat.error(player, "player \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * addLicense()
	 ************************************************************/
	private void addLicense(Player player, Command cmd, String label, String[] args) {	
		if(args.length == 4) {
			if(this.playerManager.isPlayerExist(args[2])) {
				if(this.licenseManager.isLicenseExist(args[3])) {
					if(this.playerManager.setPlayerLicense(args[2], args[3]) == true) {
						this.chat.info(player, "set license \""+args[3]+"\" for "+args[2]);
					} else {
						this.chat.error(player, "failed to set license \""+args[3]+"\" for "+args[2]);	
					}
				} else {
					this.chat.error(player, "license \""+args[3]+"\" does not exist");	
				}
			} else {
				this.chat.error(player, "player \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
	
	/************************************************************
	 * removeLicense()
	 ************************************************************/
	private void removeLicense(Player player, Command cmd, String label, String[] args) {	
		if(args.length == 4) {
			if(this.playerManager.isPlayerExist(args[2])) {
				if(this.playerManager.hasPlayerLicense(args[2], args[3])) {
					if(this.playerManager.removePlayerLicense(args[2], args[3]) == true) {
						this.chat.info(player, "remove license \""+args[3]+"\" from "+args[2]);
					} else {
						this.chat.error(player, "failed to remove license \""+args[3]+"\" for "+args[2]);	
					}
				} else {
					this.chat.error(player, args[2]+" has no license \""+args[3]+"\"");	
				}
			} else {
				this.chat.error(player, "player \""+args[2]+"\" does not exist");
			}
		} else {
			this.chat.error(player, "amount of arguments does not match");
		}
	}
}
