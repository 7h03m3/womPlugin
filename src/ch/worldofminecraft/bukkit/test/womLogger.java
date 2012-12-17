package ch.worldofminecraft.bukkit.test;

import java.util.logging.Logger;

public class womLogger {
	private final Logger log;
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womLogger() {
		 this.log = Logger.getLogger("Minecraft");		
	}
	
	/************************************************************
	 * info()
	 ************************************************************/
	public void info(String msg) {
		this.log.info(msg);
	}
}
