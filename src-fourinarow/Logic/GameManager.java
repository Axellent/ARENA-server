package Logic;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import network.Connection;
import GUI.GameFrame;
import Mediator.GameMediator;

/**
 * 
 * @author Axel Sigl & Johan Forsblom & Andreas Lönnermark
 */
public class GameManager {
    private GameFrame gameFrame;
    private GameMediator gameMediator;
	private GameGrid gameGrid;
	private Connection connection;
	private Socket socket;
    private Player[] players;
    private int nPlayers;
    private int playerID;

    /**
     * 
     * @author Axel Sigl & Johan Forsblom & Andreas L�nnermark
     * @param names
     */
    public GameManager(int playerID) {
    	this.playerID = playerID;
    	nPlayers = 2;
        players = new Player[nPlayers];
    	
    	gameMediator = new GameMediator();
    	gameGrid = new GameGrid(gameMediator);
    	gameFrame = new GameFrame(gameMediator);
    	gameFrame.initGUI();      
		startGameLoop();        
    }
    
    /**
     * 
     * @author Axel Sigl
     * @param ID
     * @param name
     */
    public void initPlayer(int ID, String name, boolean opponent, String IP){
    	players[ID - 1] = new Player(ID, name, opponent, IP);
    }
    
    /**
     * 
     * @author Axel Sigl
     * @param names
     */
    public void initPlayers(String[] names){
        
    	for(int i = 0; i < nPlayers; i++){
        	players[i] = new Player(i + 1, names[i]);
        }
    }
    
    /**
     * 
     * @author Axel Sigl
     */
    public void multiplayerGameLoop(){
    	while(!gameMediator.getQuitGame()){

    		if(gameMediator.getEnterPlayerNames()){
    			initPlayer(playerID, gameFrame.getPlayerName(playerID), false, "localhost");
    			// Send player to opponent.
    			// Get player from opponent.
    			
    			gameMediator.setEnterPlayerNames(false);
    			gameMediator.setStartGame(true);
    		}
    		
    		if(gameMediator.getStartGame()){
	    		gameFrame.updateGame();
	    		
	    		if(!players[gameMediator.getPlayerTurn() - 1].isOpponent()){
	    			try {
						gameGrid.update();
					} catch (InvalidMoveException e) {
						gameMediator.setShowInvalidMoveDialog(true);
					}
	    		}
	    		else{
	    			// Get move from opponent.
	    		}
	    		
	    		gameFrame.drawGame();
    		}
			try {Thread.sleep(10);} catch(Exception e) {} 
        }
    	Thread.interrupted();
    }

    /*
     * @author Johan Forsblom
     */
    public void gameLoop(){
    	while(!gameMediator.getQuitGame()){

    		if(gameMediator.getEnterPlayerNames()){
    			initPlayers(gameFrame.getPlayerNames());
    			gameMediator.setEnterPlayerNames(false);
    			gameMediator.setStartGame(true);
    		}
    		
    		if(gameMediator.getStartGame()){
	    		gameFrame.updateGame();
				try {
					gameGrid.update();
				} catch (InvalidMoveException e) {
					gameMediator.setShowInvalidMoveDialog(true);
				}
	    		gameFrame.drawGame();
    		}
			try {Thread.sleep(10);} catch(Exception e) {} 
        }
    	Thread.interrupted();
    }
    
    /*
     * @author Andreas Lönnermark
     */
	public void startGameLoop(){
		Thread loop = new Thread(){
			public void run()
			{
				connectToOpponent();
				gameLoop();
			}
		};
		loop.start();
	}
	
	/**
	 * 
	 * @author Axel Sigl
	 */
	public void connectToOpponent(){
		int port;
		
		try {
			if(playerID == 1){
				connection = new Connection(12346);
				connection.start();
				port = 12347;
			}
			else{
				connection = new Connection(12347);
				connection.start();
				port = 12346;
			}
			
			socket = new Socket("localhost", port);
			Thread.sleep(1000);
			gameMediator.setOpponentIP(connection.getAdress() + " " + port);
			gameFrame.updateGame();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println("Could not connect to opponent");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			connectToOpponent();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


