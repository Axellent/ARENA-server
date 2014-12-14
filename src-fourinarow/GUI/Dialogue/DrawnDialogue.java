package GUI.Dialogue;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Mediator.GameMediator;

/**
 * 
 * @author Andreas Lönnermark
 *
 */
public class DrawnDialogue {
    private String[] options = {"OK"};
    private JLabel label;
    private JPanel panel;
    private int result;
    GameMediator gameMediator;

    public DrawnDialogue(GameMediator gameMediator){
      	this.gameMediator = gameMediator;
    	panel = new JPanel();
    	label = new JLabel("Draw! - No winner of the game.");
    	panel.add(label);
    }
    
	public void showDialogue(){
		result = JOptionPane.showOptionDialog(null, panel, "DRAW!", 
				 JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE,null, options , options[0]);
		
		if(result == 0){
			gameMediator.setStartGame(false);
			gameMediator.reset();
		}
	}
}

