package network;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import play.Game;
import league.LeagueOwner;
import league.Tournament;

public class FileHandler {
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private Account[] accounts;
	private Tournament[] tournaments;
	private int nAccounts = 1;
	private int nTournaments = 1;
	
	// Account for testing purposes, does not have ANY user privileges for security reasons.
	private static final Account TEST_ACCOUNT = new Account("testName", "testPassword", "N/A", 0);
	
	private static final Tournament TEST_TOURNAMENT = new Tournament("testOwner", new Game("testGame", 1) , "testTournament", 1, 1);
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public FileHandler(){
		loadAccounts();
		loadTournaments();
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param accountName
	 * @return
	 */
	public Account searchAccounts(String accountName){
		for(int i = 0; i < nAccounts; i++){
			if(accountName.equals(accounts[i].getName())){
				return accounts[i];
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void loadAccounts(){
		accounts = new Account[1000000];
		
		try {
			inStream = new ObjectInputStream(new FileInputStream("accounts.ser"));
			
			accounts = (Account[]) inStream.readObject();
			accounts[0] = TEST_ACCOUNT;
			inStream.close();
			
		} catch (FileNotFoundException e) {
			Server.println("account file is empty, initializing");
			accounts[0] = TEST_ACCOUNT;
			saveAccounts();
			loadAccounts();
		} catch (EOFException e){
			Server.println("account file is empty, initializing");
			accounts[0] = TEST_ACCOUNT;
			saveAccounts();
			loadAccounts();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void saveAccounts(){
		accounts[0] = TEST_ACCOUNT;
		
		try {
			outStream = new ObjectOutputStream(new FileOutputStream("accounts.ser"));
			
			outStream.writeObject(accounts);
	        outStream.flush();
	        outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param account
	 */
	public void addAccount(Account account){
		accounts[nAccounts] = account;
		nAccounts++;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public Account[] getAccounts(){
		return accounts;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public int getNAccounts(){
		return nAccounts;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @param tournamentName
	 * @return
	 */
	public Tournament searchTournaments(String tournamentName){
		for(int i = 0; i < nTournaments; i++){
			if(tournamentName.equals(tournaments[i].getName())){
				return tournaments[i];
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void loadTournaments(){
		tournaments = new Tournament[1000000];
		
		try {
			inStream = new ObjectInputStream(new FileInputStream("tournaments.ser"));
			
			tournaments = (Tournament[]) inStream.readObject();
			tournaments[0] = TEST_TOURNAMENT;
			inStream.close();
			
		} catch (FileNotFoundException e) {
			Server.println("tournament  file is empty, initializing");
			tournaments[0] = TEST_TOURNAMENT;
			saveTournaments();
			loadTournaments();
		} catch (EOFException e){
			Server.println("tournament  file is empty, initializing");
			tournaments[0] = TEST_TOURNAMENT;
			saveTournaments();
			loadTournaments();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void saveTournaments(){
		tournaments[0] = TEST_TOURNAMENT;
		
		try {
			outStream = new ObjectOutputStream(new FileOutputStream("tournaments.ser"));
			
			outStream.writeObject(tournaments);
	        outStream.flush();
	        outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public Tournament[] getTournaments(){
		return tournaments;
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 * @return
	 */
	public int getNTournaments(){
		return nTournaments;
	}
}
