package GUI.Dialogue;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mediator.GameMediator;

/**
 * 
 * @author Axel Sigl
 *
 */
public class InvalidMoveDialogue{

    private String[] options = {"OK"};
    private JLabel label;
    private JPanel panel;
    private int result;
    private GameMediator gameMediator;
    
    public InvalidMoveDialogue(GameMediator gameMediator)
    {
      	this.gameMediator = gameMediator;
    }

    public JPanel createInvalidMoveDialogue(){

    	panel = new JPanel();
    	label = new JLabel("Invalid move! Column is full!");
    	panel.add(label);
    	return panel;
    }
    
	public void showDialogue(){
		result = JOptionPane.showOptionDialog(null, panel, "Invalid Move!", 
				 JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE,null, options , options[0]);

		if(result == 0)
		{
			gameMediator.setShowInvalidMoveDialog(false);
			//reset the mouse clicked flag in the mediator so the gameGrid stops throwing the
			//invalid move exception.
			gameMediator.setMouseClicked(false);
		}
	}
}