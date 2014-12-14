package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import GUI.Dialogue.DrawnDialogue;
import GUI.Dialogue.InvalidMoveDialogue;
import GUI.Dialogue.SetupGameDialogue;
import GUI.Dialogue.WinnerDialogue;
import Mediator.GameMediator;

/**
 * 
 * @author Johan Forsblom & Axel Sigl
 *
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {
    private DrawnDialogue drawnDialogue;
    private SetupGameDialogue setupGameDialogue;
    private WinnerDialogue winnerDialogue;
    private InvalidMoveDialogue invalidMoveDialogue;

    private JFrame gameWindow;
    private JPanel buttonPanel;
    private JButton buttonNewGame;
    private JButton buttonQuit;
    private GameBoard gameBoard;
    private GameMediator gameMediator;

    public GameFrame(GameMediator gameMediator) {
    	drawnDialogue = new DrawnDialogue(gameMediator);
    	this.gameMediator = gameMediator;
    }
    
    /**
     * 
     * @author Axel Sigl
     * @param id
     * @return
     */
    public String getPlayerName(int id){
    	setupGameDialogue.EnterPlayerName(id);
		return gameMediator.getPlayerName(id);
    }
        
    /*
     * 
     * @author Axel Sigl
     *
     */
    public String[] getPlayerNames() {
    	String[] names = new String[2];
    	
    	setupGameDialogue.EnterPlayerName(1);
    	setupGameDialogue.EnterPlayerName(2);
    	
    	names[0] = gameMediator.getPlayerName(1);
    	names[1] = gameMediator.getPlayerName(2);
    	
    	return names;
    }
    
    /*
     * @author Andreas Lönnermark
     */
    public void initGUI()
    {
        try {
			SwingUtilities.invokeAndWait(
				new Runnable(){
					public void run(){
						gameWindow = new JFrame("Four In A Row");
						gameBoard = new GameBoard(gameMediator);
						buttonPanel = new JPanel();
						buttonNewGame = new JButton("New Game");
						buttonQuit = new JButton("Quit");
						setupGameDialogue = new SetupGameDialogue(gameMediator);
						setupGameDialogue.createPlayerDialogue();
						winnerDialogue = new WinnerDialogue(gameMediator); 
						invalidMoveDialogue = new InvalidMoveDialogue(gameMediator);
						gameWindow.setLayout(new BorderLayout());
						gameWindow.add(buttonPanel,BorderLayout.EAST);
						buttonPanel.add(buttonNewGame);
						buttonPanel.add(buttonQuit);						
						gameWindow.add(gameBoard.getPanel(),BorderLayout.CENTER);
						gameWindow.getContentPane().setPreferredSize(new Dimension(700,550));
						gameWindow.pack();
						gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						gameWindow.setResizable(false);
						gameWindow.setLocationRelativeTo(null);
						gameWindow.setVisible(true);
			    	}
			    }
			);
			
			buttonNewGame.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gameMediator.setStartGame(true);
					gameMediator.setClearGrid(true);
					gameMediator.setEnterPlayerNames(true);
				}
			});
			buttonQuit.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gameMediator.setStartGame(false);
					gameMediator.setQuitGame(true);	
					System.exit(0);
				}
			});
			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    /* 
     * @author Johan Forsblom & Axel Sigl
     *
     */        
    public void drawGame(){
		gameBoard.getPanel().repaint();
    }    
    
    /* 
     * @author Johan Forsblom & Axel Sigl
     */        
    public void updateGame(){
    	if(gameMediator.getGameDraw()){
    		drawnDialogue.showDialogue();
    	}
    	if(gameMediator.getWinner()!=0){
    		winnerDialogue.showDialogue();
    	}
    	if(gameMediator.getInvalidMoveDialogVisible())
    	{
    		invalidMoveDialogue.showDialogue();
    	}
    	
    	gameBoard.update();
    }
       
}
