package Logic;
import Logic.InvalidMoveException;
import Mediator.GameMediator;

/**
 * @author Johan Forsblom & Andreas Lönnermark
 */
public class GameGrid {
    private final int GRID_WIDTH = 7;
    private final int GRID_HEIGHT = 6;
    private final int DIR_DOWN = 1;
    private final int DIR_LEFT = 2;
    private final int DIR_RIGHT = 3;
    private final int DIR_LEFTUP = 4;
    private final int DIR_LEFTDOWN = 5;
    private final int DIR_RIGHTUP = 6;
    private final int DIR_RIGHTDOWN = 7;
    private final int BOARDFULL = 42;
    private int numDiscsOnBoard = 0;

    private int [][] gameGrid;
    private GameMediator gameMediator;
    private int playerTurn;

    public GameGrid(GameMediator gameMediator) {
    	this.gameMediator = gameMediator;
    	initGameGrid();
    	gameMediator.setGameGrid(gameGrid);
    	playerTurn = gameMediator.getPlayerTurn();
    }

    /*
     * @author Johan Forsblom
     */
    public void initGameGrid(){
    	gameGrid = new int[GRID_HEIGHT][GRID_WIDTH];
    	for(int i = 0; i < GRID_HEIGHT; ++i){
    		for(int j = 0; j < GRID_WIDTH; ++j){
    			gameGrid[i][j] = 0;
    		}
    	}
    }
    
    /*
     * @author Johan Forsblom
     */
    public void addDiscToColumn(int column) throws InvalidMoveException{
    	int invalidMove=1;
	    
    	if(column >=0 && column <GRID_WIDTH){
			for(int i=GRID_HEIGHT-1;i>=0;--i){
				if(gameGrid[i][column]==0){
					gameGrid[i][column] = playerTurn;
					
					numDiscsOnBoard++;
					
					//if the board is full, set the gameDraw flag in the mediator
					if(numDiscsOnBoard == BOARDFULL){
						gameMediator.setGameDraw();
					}
					playerWon(column,i);
					invalidMove = 0;
					break;
				}
			}
    	}
		if(invalidMove == 1){
			throw new InvalidMoveException("Invalid move!");
		}
    }
    
    /*
     * @author Johan Forsblom
     */
    public void update() throws InvalidMoveException{
    	
    	if(gameMediator.getMouseClicked()){
    		playerTurn = gameMediator.getPlayerTurn();
    		addDiscToColumn(gameMediator.getColumn());	//add disc to column
    		gameMediator.setGameGrid(gameGrid);			//get the changed game board
    		gameMediator.setUpdateGridCheck(true);		//set update flag which will be sent to GUI through the mediator
    		gameMediator.setMouseClicked(false);		//reset mouse clicked flag.
    	}
    	
    	if(gameMediator.getClearGrid()){
    		initGameGrid();
    		numDiscsOnBoard=0;
    		gameMediator.setGameGrid(gameGrid);			//update the mediator gameboard
    		gameMediator.setClearGrid(false);
    		
    	}
    }
    
    /**
     * @author Johan Forsblom & Andreas Lönnermark
     * @param column The column the disc was placed in
     * @param row The row the disc was placed in
     */
    private void playerWon(int column,int row){
    	int dir1=0;
    	int dir2=0;
    	
    	//check vertical column for a win
    	dir2 = checkNumDiscsInDirection(column,row,DIR_DOWN,0);
    	    	
    	if(dir2>=4){
    		gameMediator.setPlayerWon(playerTurn);
    		return;
    	}
    	
    	//check horizontal row for a win
    	dir1 = checkNumDiscsInDirection(column,row,DIR_LEFT,0);
    	dir2 = checkNumDiscsInDirection(column,row,DIR_RIGHT,0);
    	
    	if(dir1 + dir2>4){
    		gameMediator.setPlayerWon(playerTurn);
    		return;
    	}
    	
    	//check diagonal slope:"\" row for a win
    	dir1 = checkNumDiscsInDirection(column,row,DIR_LEFTUP,0);
    	dir2 = checkNumDiscsInDirection(column,row,DIR_RIGHTDOWN,0);
    	    	
    	if(dir1 + dir2>4){
    		gameMediator.setPlayerWon(playerTurn);
    		return;
    	}

    	//check diagonal slope:"/" row for a win
    	dir1 = checkNumDiscsInDirection(column,row,DIR_LEFTDOWN,0);
    	dir2 = checkNumDiscsInDirection(column,row,DIR_RIGHTUP,0);
    	    	
    	if(dir1 + dir2>4){
    		gameMediator.setPlayerWon(playerTurn);
    	}
    }
    
    /**
     * Recursive function that counts how many discs that are found in the given
     * direction.
     * @param column The column to check for a disc
     * @param row The row to check for a disc
     * @param direction The direction the check is going
     * @param count Number of discs found in the current direction
     * @return returns number of discs found in the given direction
     */
    private int checkNumDiscsInDirection(int column,int row,int direction,int count){
    	if(column<0 || column >=GRID_WIDTH || row <0 || row>=GRID_HEIGHT){
    		return count;
    	}
    	if(gameGrid[row][column] != playerTurn){
    		return count;
    	}
    	else{	
			switch(direction){
				case DIR_DOWN:
					return checkNumDiscsInDirection(column,row+1,direction,count+1);
				case DIR_LEFT:
					return checkNumDiscsInDirection(column-1,row,direction,count+1);
				case DIR_RIGHT:
					return checkNumDiscsInDirection(column+1,row,direction,count+1);
				case DIR_LEFTDOWN:
					return checkNumDiscsInDirection(column-1,row+1,direction,count+1);
				case DIR_LEFTUP:
					return checkNumDiscsInDirection(column-1,row-1,direction,count+1);
				case DIR_RIGHTUP:
					return checkNumDiscsInDirection(column+1,row-1,direction,count+1);
				case DIR_RIGHTDOWN:
					return checkNumDiscsInDirection(column+1,row+1,direction,count+1);
				default:break;
			}
	    }

    	//this should never happen, but it makes the compiler happy ^^
    	return 0;
    }
    
    
    /*
     * @author Johan Forsblom
     */
    public int [][] getGameGrid(){
    	return gameGrid;
    }

}
