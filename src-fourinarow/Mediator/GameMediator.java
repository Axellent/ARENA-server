package Mediator;

/**
 * 
 * @author Johan Forsblom, Axel Sigl & Andreas Lönnermark
 *
 */
public class GameMediator {

	private int [][] gameGrid;
	private int column;
	private int playerTurn;
	private int winner;
	private boolean quitGame;
	private boolean startGame;
	private boolean gameDraw;
	private boolean updateGridCheck;
	private boolean updateMouseClickCheck;
	private boolean showInvalidMoveDialog;
	private boolean clearGrid;
	private boolean enterPlayerNames;
	private String player1,player2;
	private String opponentIP = "No Connection";
	
	public GameMediator(){
		player1 = "";
		player2 = "";
		reset();
	}
	
	public String getPlayerName(int player){
		if(player==1){
			return player1;
		}else if(player==2){
			return player2;
		}else{
			return "";
		}
	}
	
	public int [][] getGameGrid(){
		return gameGrid;
	}
	
	public boolean getClearGrid(){
		return clearGrid;
	}
	
	public boolean getStartGame(){
		return startGame;
	}
	
	public boolean getGameDraw(){
		return gameDraw;
	}
	
	public boolean getInvalidMoveDialogVisible(){
		return showInvalidMoveDialog;
	}
	
	public int getPlayerTurn(){
		return playerTurn;
	}
	
	public int getWinner(){
		return winner;
	}

	public int getColumn(){
		return column;
	}
	
	public boolean getQuitGame(){
		return quitGame;
	}
	
	public boolean getEnterPlayerNames(){
		return enterPlayerNames; 
	}
		
	public boolean getUpdateGridCheck(){
		return this.updateGridCheck;
	}
	
	public boolean getMouseClicked(){
		return this.updateMouseClickCheck;
	}
	
	public void setGameGrid(int [][] gameGrid){
		this.gameGrid = gameGrid;
	}
	
	public void setColumn(int column){
		this.column = column;
	}

	public void getPlayerTurn(int playerTurn){
		this.playerTurn = playerTurn;
	}
		
	public void setUpdateGridCheck(boolean state){
		updateGridCheck = state;
	}
	
	public void setMouseClicked(boolean state){
		updateMouseClickCheck = state;
	}
	
	public void setShowInvalidMoveDialog(boolean visible){
		showInvalidMoveDialog = visible;
	}
	
	public void togglePlayerTurn(){
		if(playerTurn == 1){
			playerTurn = 2;
		}else if(playerTurn == 2){
			playerTurn=1;
		}
	}
	
	public void setPlayerWon(int player){
		winner = player;
	}
	
	public void setStartGame(boolean startGame){
		this.startGame = startGame;
	}
	
	public void setQuitGame(boolean quitGame){
		this.quitGame = quitGame;
	}
	
	public void setGameDraw(){
		gameDraw = true;
	}
	
	public void setClearGrid(boolean clearGrid){
		this.clearGrid = clearGrid;
	}
	
	public void setEnterPlayerNames(boolean enterPlayerNames){
		this.enterPlayerNames = enterPlayerNames;
	}

	public void setPlayerName(int markerID,String name){
		if(markerID == 1){
			player1 = name;
		}else if(markerID == 2){
			player2 = name;
		}
	}	
	
	public void reset(){
		updateGridCheck = false;
		updateMouseClickCheck = false;
		column = 0;
		playerTurn = 1;
		showInvalidMoveDialog = false;
		winner = 0;
		startGame = false;
		quitGame = false;
		gameDraw = false;
		enterPlayerNames = false;
	}

	public String getOpponentIP() {
		return opponentIP;
	}

	public void setOpponentIP(String opponentIP) {
		this.opponentIP = opponentIP;
	}
}
