package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Mediator.GameMediator;

/**
 * @author Johan Forsblom, Axel Sigl & Andreas Lönnermark
 */
public class GameBoard implements MouseListener, MouseMotionListener {
	private final int NCOLUMNS = 7;
	private final int NROWS = 6;
	private final int GRID_WIDTH = 70;
	private final int GRID_HEIGHT = 70;
	private final int MIN_DISK_Y = 130;
	private final int ACTIVE_DISK_Y = 60;
	private final int BOARD_Y = 420;
	private static final int NO_COLUMN = -1;
	
	private JPanel gameBoard;
	private GameMediator gameMediator;
	private JLabel playerturn;
	private JLabel invalidMove;
	private JLabel connectedTo;
    private BufferedImage background;
    private BufferedImage board;
    private BufferedImage selectedColumn; 
    private BufferedImage yellowDisc;
    private BufferedImage redDisc;
    private int [][] gameGrid;
    private int mousex;
    private int mouseOverColumn;

    /* 
     * @author Johan Forsblom
     */    
    public GameBoard(GameMediator gameMediator) {
        gameBoard = new MouseListenerPanel(this,this);
        this.gameMediator = gameMediator;
        
        // Layout refused to work...
        connectedTo = new JLabel("                              		          "
        		+ "Connected to: " + gameMediator.getOpponentIP());
        connectedTo.setFont(new Font("Serif", Font.PLAIN, 12));
        connectedTo.setForeground(new Color(255,255,254));
        
        invalidMove = new JLabel("");
        invalidMove.setFont(new Font("Serif", Font.PLAIN, 24));
        invalidMove.setForeground(new Color(255,255,254));
        
        playerturn = new JLabel("Connect Four");
        playerturn.setFont(new Font("Serif", Font.ITALIC, 24));
        playerturn.setForeground(new Color(255,255,254));
        
        gameBoard.add(playerturn);
        gameBoard.add(invalidMove);
        gameBoard.add(connectedTo);

        gameGrid = gameMediator.getGameGrid();
       
        mouseOverColumn = NO_COLUMN;
        
        try{
	        background = ImageIO.read(new File("imgs/bg.png"));
	        board = ImageIO.read(new File("imgs/gameboard.png"));
	        selectedColumn = ImageIO.read(new File("imgs/selectedColumn.png"));
	        yellowDisc = ImageIO.read(new File("imgs/yellowdisc.png"));
	        redDisc = ImageIO.read(new File("imgs/reddisc.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /*
     * @author Johan Forsblom & Axel Sigl
     */    
    @SuppressWarnings("serial")
	class MouseListenerPanel extends JPanel 
    {

    	MouseListenerPanel(MouseMotionListener ml, MouseListener m2)
    	{
            addMouseMotionListener(ml);
            addMouseListener(m2);
    	}
        public void paintComponent(Graphics g){
        	super.paintComponent(g);
         	Graphics2D g2d = (Graphics2D) g;
         	mouseOverColumn = (mousex / GRID_WIDTH) * GRID_WIDTH;
         	
        	g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        	g2d.drawImage(board, 0, getHeight() - 420, null);

        	if(gameMediator.getStartGame()){
	    		drawGameGrid(g2d);
	
	        	if(getColumn() < NCOLUMNS){
	        		if(gameMediator.getPlayerTurn() == 1){
	        			g2d.drawImage(yellowDisc,  mouseOverColumn, ACTIVE_DISK_Y, null);	
	        		}else{
	                	g2d.drawImage(redDisc, mouseOverColumn, ACTIVE_DISK_Y, null);        			
	        		}
	        		g2d.drawImage(selectedColumn, mouseOverColumn, getHeight() - BOARD_Y, null);
	        	}
        	}
        }
    }
        
    /*
     * 
     * @author Johan Forsblom & Axel Sigl
     * @param g
     */
    private void drawGameGrid(Graphics2D g){
    	for(int i = 0; i < NROWS; ++i){
    		for(int j = 0; j < NCOLUMNS; ++j){
    			
    			if(gameGrid[i][j] == 1){
    				g.drawImage(yellowDisc, j * GRID_WIDTH, MIN_DISK_Y + i * GRID_HEIGHT, null);
    			}
    			else if(gameGrid[i][j] == 2){
    				g.drawImage(redDisc, j * GRID_WIDTH, MIN_DISK_Y + i * GRID_HEIGHT, null);
    			}
    		}
    	}
    }    
   
    /* 
     * @author Johan Forsblom
     */    
    public void update()
    {
    	gameGrid = gameMediator.getGameGrid();
    	if(gameMediator.getUpdateGridCheck()){
    		
    		gameMediator.setUpdateGridCheck(false);
			//change turn to the other player.
			gameMediator.togglePlayerTurn();    		
    	}
    	gameMediator.setColumn(getColumn());
    	if(gameMediator.getStartGame())
    	{
    		playerturn.setText("Player Turn: " + gameMediator.getPlayerName(gameMediator.getPlayerTurn()));	
    	}else{
    		playerturn.setText("Connect Four");
    	}
    	
    	// Layout refused to work...
    	connectedTo.setText("                              		          "
    			+ "Connected to: " + gameMediator.getOpponentIP());
    	
    	
    }
    
    /*
     * 
     * @author Andreas Lönnermark
     */
    public int getColumn()
    {
    	return mouseOverColumn / GRID_WIDTH;
    }
    
    /*
     * 
     * @author Andreas Lönnermark
     */
    public JPanel getPanel()
    {
    	return gameBoard;
    }
    
    /*
     * 
     * @author Andreas Lönnermark
     */
    public GameBoard getGameBoard(){
    	return this;
    }
    /*
     * 
     * @author Andreas Lönnermark
     */    
    public void mouseMoved(MouseEvent me) {
		mousex = me.getX();
	}    
    /*
     * 
     * @author Andreas Lönnermark
     */    
	public void mouseDragged(MouseEvent me) {
		mousex = me.getX();
	}    
	
	/*
	 * @author Johan Forsblom & Axel Sigl
	 *
	 */
	public void mouseClicked(MouseEvent me) {
		gameMediator.setColumn(mouseOverColumn);
		if(getColumn() < NCOLUMNS){
			gameMediator.setMouseClicked(true);
		}
	}
	public void mouseReleased(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}	
	public void mousePressed(MouseEvent me) {}	
	public void mouseExited(MouseEvent me) {}      

}
