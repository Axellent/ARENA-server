package main;

import Logic.GameManager;

/**
 * 
 * @author Axel Sigl
 *
 */
public class FourInARow {

    public FourInARow(int playerID) {
    	new GameManager(playerID);    		
    }

    public static void main(String args[]) {
    	if(args.length == 0){
    		System.out.println("Please provide an integer (playerID) as command line argument");
    	}
    	else{
    		new FourInARow(Integer.parseInt(args[0]));	
    	}
    }
}
