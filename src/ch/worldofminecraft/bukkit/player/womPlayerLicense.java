package ch.worldofminecraft.bukkit.player;

import java.util.HashMap;

public class womPlayerLicense {
	private String name;
	private HashMap<String, Boolean> permissionNodes = new HashMap<String, Boolean>();
	
	/************************************************************
	 * Constructor
	 ************************************************************/
	womPlayerLicense(String name) {
		this.name = name;
	}
	
	/************************************************************
	 * getName()
	 ************************************************************/
	public String getName() {
		return this.name;
	}
	
	/************************************************************
	 * isPermissionNodeExist()
	 ************************************************************/
	public boolean isPermissionNodeExist(String node) {
		
		return this.permissionNodes.containsKey(node);
	}
	
	/************************************************************
	 * addPermissionNode()
	 ************************************************************/
	public boolean addPermissionNode(String node, boolean value) {
		if(this.isPermissionNodeExist(node))
			return false;
		
		this.permissionNodes.put(node, value);
		return true;
	}
	
	/************************************************************
	 * getPermissionNodeValue()
	 ************************************************************/
	public boolean getPermissionNodeValue(String node) {
		if(this.isPermissionNodeExist(node))
			return false;
		
		return this.permissionNodes.get(node);
	}
	
	/************************************************************
	 * removePermissionNode()
	 ************************************************************/
	public boolean removePermissionNode(String node) {
		if(this.isPermissionNodeExist(node))
			return false;
		
		this.permissionNodes.remove(node);
		return true;
	}
	
	/************************************************************
	 * getPermissionNodeMap()
	 ************************************************************/
	public HashMap<String, Boolean> getPermissionNodeMap() {
		return this.permissionNodes;
	}
	
	/************************************************************
	 * getPermissionNodeArray()
	 ************************************************************/
	public String[] getPermissionNodeArray() {
		String[] arr = new String[this.permissionNodes.size()];
		
		int i=0;
		for(String name: this.permissionNodes.keySet()) {
			arr[i] = name;
			i++;
		}
		
		return arr;
	}
}
