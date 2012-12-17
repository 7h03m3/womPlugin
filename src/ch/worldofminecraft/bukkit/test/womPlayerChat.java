package ch.worldofminecraft.bukkit.test;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class womPlayerChat {
	
	public womPlayerChat() {
		
	}

	public void info(Player player, String msg) {
		player.sendMessage(ChatColor.BLUE + msg);
	}
	
	public void error(Player player, String msg) {
		player.sendMessage(ChatColor.RED + msg);
	}
	
	public void separator(Player player) {
		player.sendMessage(ChatColor.GREEN + "****************************************");
	}
}
