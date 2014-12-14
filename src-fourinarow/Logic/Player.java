package Logic;

/*
 * 
 * @author Axel Sigl
 */
public class Player {

	private int markerID;
	private String name;
	private boolean opponent;
	private String IP;
	
	/**
	 * 
	 * @author Axel Sigl
	 */
    public Player(int markerID, String name) {
        super();        
        this.markerID = markerID;
        this.name = name;
    }
    
	/**
	 * 
	 * @author Axel Sigl
	 */
    public Player(int markerID, String name, boolean opponent, String IP) {
        super();        
        this.markerID = markerID;
        this.name = name;
        this.opponent = opponent;
        this.IP = IP;
    }
    

    /**
     * 
     * @author Axel Sigl
     */
    public void setMarkerID(int markerID){
    	this.markerID = markerID;
    }
    
    /**
     * 
     * @author Axel Sigl
     */
    public void setPlayerName(String name){
    	this.name = name;
    }
    
    /**
     * 
     * @author Axel Sigl
     */
    public int getMarkerID(){
    	return markerID;
    }
    
    /**
     * 
     * @author Axel Sigl
     */
    public String getPlayerName(){
    	return name;
    }
    
    /**
     *
     * @author Axel Sigl
     * @return
     */
    public boolean isOpponent(){
    	return opponent;
    }
    
}