package network;

import java.io.Serializable;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Account implements Serializable{
	private static final long serialVersionUID = -7325648617899391951L;
	private int ID;
	private String name;
	private String password;
	private String type;
	private boolean authenticated = false;
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param name
	 * @param password
	 * @param type
	 * @param ID
	 */
	public Account(String name, String password, String type, int ID){
		this.name = name;
		this.password = password;
		this.type = type;
		this.ID = ID;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param type
	 */
	public void setType(String type){
		this.type = type;
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
	 * @author Axel Sigk
	 * @return
	 */
	public String getType(){
		return type;
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
