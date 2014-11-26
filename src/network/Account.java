package network;

import java.io.Serializable;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Account implements Serializable{
	private int ID;
	private String name;
	private String password;
	private boolean authenticated = false;
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param name
	 * @param password
	 * @param ID
	 */
	public Account(String name, String password, int ID){
		this.name = name;
		this.password = password;
		this.ID = ID;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param authenticated
	 */
	public void setAuthenticated(boolean authenticated){
		this.authenticated = authenticated;
	}
	
	/**
	 * @author Axel Sigl
	 * @return
	 */
	public int getID(){
		return ID;
	}
	
	/**
	 * @author Axel Sigl
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @author Axel Sigl
	 * @return
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public boolean isAuthenticated(){
		return authenticated;
	}
}
