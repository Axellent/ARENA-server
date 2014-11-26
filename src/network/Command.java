package network;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author Axel Sigl
 *
 */
public class Command {
	private Account[] allAccounts;
	private int nAccounts = 0;
	private Account account;
	private String testUsername = "testAccount";
	private String testPassword = "testPassword";
	
	public Command(){
		loadAccounts();
	}
	
	/**
	 * Splits client input (with whitespace characters as separators) into command and arguments,
	 * and tries to execute the relevant command.
	 * @author Axel Sigl
	 * @param input
	 * @return
	 */
	public String parseUserCommand(String input) {
		String cmd[] = new String[100];

		cmd = input.split("\\s");

		switch (cmd[0]) {
		
		case("quit"):
			return quit();
		
		case("help"):
			return help();
		
		case("login"):
			return login(cmd[1], cmd[2]);
		
		case("register"):
			return register(cmd[1], cmd[2], cmd[3]);
		}
		
		return unknownCommand(cmd[0]);
	}

	/**
	 * Splits root input (with whitespace characters as separators) into command and arguments,
	 * and tries to execute the relevant command.
	 * @author Axel Sigl
	 * @param input
	 * @return
	 */
	public String parseRootCommand(String input) {
		String cmd[] = new String[100];

		cmd = input.split("\\s");

		switch (cmd[0]) {

		case ("shutdown"):
			shutdown();
			
		case("help"):
			return help();
		}
		
		return unknownCommand(cmd[0]);
	}
	
	private String quit(){
		return "quit";
	}
	
	private void shutdown(){
		Server.println("ARENA-server shutting down \r");
		System.exit(0);
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	private String help(){
		return "Welcome to the ARENA-server, enter a command to begin\n"
				+ "Syntax: COMMAND [ARGUMENTS]\r"
				+ "Example: login yourAccountName yourPassword\r\r"
				+ "List of commands:\r"
				+ "quit - Close client connection(client only)\r"
				+ "shutdown - Shutdown ARENA-server(root only)\r"
				+ "help - This command\r"
				+ "login acountName password - Login with ARENA account\r"
				+ "register accountName password type - Register a new ARENA account as user type";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param accountName
	 * @param password
	 * @param ID
	 * @return
	 */
	private String login(String accountName, String password){
		
		if(accountName.equals(testUsername) && password.equals(testPassword)){
			account = new Account(accountName, password, nAccounts);
			account.setAuthenticated(true);
			return "User : " + account.getName() + " logged in succesfully";
		}
		
		return "Incorrect username or password";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param accountName
	 * @param password
	 * @return
	 */
	private String register(String accountName, String password, String type){
		
		if(accountName.length() > 3 && password.length() > 3){
			allAccounts[nAccounts] = new Account(accountName, password, nAccounts++);
			return "User : " + accountName + " registered successfully";
		}
		
		return "Accountname and password must both be more than three characters long";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param cmd
	 * @return
	 */
	private String unknownCommand(String cmd){
		return "Unknown command " + cmd + "\r";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void loadAccounts(){
		allAccounts = new Account[1000000];
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("accounts.ser"));
			
			allAccounts = (Account[]) in.readObject();
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void saveAccounts(){
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream("accounts.ser"));
			
			out.writeObject(allAccounts);
	        out.flush();
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public Account getAccount(){
		return account;
	}
}
