package ch.worldofminecraft.bukkit.player;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class womPlayer {
	private boolean online = false;
	private String name = null;
	private List<String> licenseList = new ArrayList<String>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	public womPlayer(String playerName) {
		this.name = playerName;
	}
	
	/************************************************************
	 * getName()
	 ************************************************************/
	public String getName() {
		return this.name;
	}
	
	/************************************************************
	 * isOnline()
	 ************************************************************/
	public boolean isOnline() {
		return this.online;
	}
	
	/************************************************************
	 * setOnline()
	 ************************************************************/
	public void setOnline() {
		this.online = true;
	}
	
	/************************************************************
	 * setOffline()
	 ************************************************************/
	public void setOffline() {
		this.online = false;
	}
	
	/************************************************************
	 * hasLicense()
	 ************************************************************/
	public boolean hasLicense(String licenseName) {
		return this.licenseList.contains(licenseName);
	}
	
	/************************************************************
	 * addLicense()
	 ************************************************************/
	public boolean addLicense(String licenseName) {
		if(this.hasLicense(licenseName)) 
			return false;
		
		return this.licenseList.add(licenseName);
	}
	
	/************************************************************
	 * removeLicense()
	 ************************************************************/
	public boolean removeLicense(String licenseName) {
		if(this.hasLicense(licenseName)) 
			return false;
		
		return this.licenseList.remove(licenseName);
	}
	
	/************************************************************
	 * getLicenseCount()
	 ************************************************************/
	public int getLicenseCount() {
		return this.licenseList.size();
	}
	
	/************************************************************
	 * getLicenseList()
	 ************************************************************/
	public List<String> getLicenseList() {
		return this.licenseList;
	}
	
	/************************************************************
	 * getLicenseArray()
	 ************************************************************/
	public String[] getLicenseArray() {
		String[] arr = new String[this.getLicenseCount()];
		
		int i=0;
		Iterator<String> it = this.licenseList.iterator();
		while(it.hasNext()) {
			arr[i] = it.next();
			i++;
		}
		
		return arr;
	}
}
