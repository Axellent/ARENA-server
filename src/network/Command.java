package network;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import league.Tournament;

/**
 * 
 * @author Axel Sigl
 */
public class Command {
	private FileHandler fileHandler;
	private Account clientAccount;
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public Command(){
		fileHandler = new FileHandler();
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
		
		case("accountID"):
			return accountID(cmd[1]);
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
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param input
	 */
	public Object parseObjectCommand(String input){
		String cmd[] = new String[100];

		cmd = input.split("\\s");
		
		switch (cmd[0]) {

		case ("-tournament"):
			return tournament(cmd[1]);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	private String quit(){
		clientAccount.setAuthenticated(false);
		return "quit";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	private void shutdown(){
		Server.println("ARENA-server shutting down");
		System.exit(0);
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	private String help(){
		return "Welcome to the ARENA-server, enter a command to begin\r"
				+ " Syntax: COMMAND [ARGUMENTS]\r"
				+ " Example: login yourAccountName yourPassword\r \r"
				+ " List of commands:\r"
				+ " quit - Close client connection(client only)\r"
				+ " shutdown - Shutdown ARENA-server(root only)\r"
				+ " help - This command\r"
				+ " login acountName password - Login with ARENA account\r"
				+ " register accountName password type - Register a new ARENA account as user type\r"
				+ " accountID accountName - returns the ID of the account";
	}
	
	/**
	 * Authenticates the user if the login can be found and verified.
	 * The login persists as long as the connection exists and is connected to the server.
	 * @author Axel Sigl
	 * @param accountName
	 * @param password
	 * @return
	 */
	private String login(String accountName, String password){
			
		clientAccount = fileHandler.searchAccounts(accountName);
		
		if(clientAccount != null && accountName.equals(clientAccount.getName()) && password.equals(clientAccount.getPassword())){
			clientAccount.setAuthenticated(true);
			return "User : " + accountName + " logged in succesfully";
		}
		
		return "Incorrect username or password";
	}
	
	/**
	 * Registers a new ARENA account with a unique name and ID.
	 * The zeroth ID is reserved by the system.
	 * @author Axel Sigl
	 * @param accountName
	 * @param password
	 * @param type
	 * @return
	 */
	private String register(String accountName, String password, String type){
		int nAccounts = fileHandler.getNAccounts();
		
		if(!(accountName.length() > 3 && password.length() > 3)){
			return "Accountname and password must both be more than three characters long";
		}
		
		if(fileHandler.searchAccounts(accountName) != null){
			return "Accountname already in use";
		}
		
		fileHandler.addAccount(new Account(accountName, password, type, nAccounts));
		return "User : " + accountName + " registered successfully";
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param accountName
	 * @return
	 */
	private String accountID(String accountName){
		Account tempAcc;
		
		tempAcc = fileHandler.searchAccounts(accountName);
		
		if(accountName.equals(tempAcc.getName())){
			return Integer.toString(tempAcc.getID());
		}
		
		return "Could not find account : " + accountName;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param tournamentName
	 * @return
	 */
	private Tournament tournament(String tournamentName){
		Tournament tournament;
		
		tournament = fileHandler.searchTournaments(tournamentName);
		
		if(tournament != null){
			return tournament;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param cmd
	 * @return
	 */
	private String unknownCommand(String cmd){
		return "Unknown command : " + cmd + "\r";
	}
	
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return The account currently logged in.
	 */
	public Account getClientAccount(){
		return clientAccount;
	}
}
