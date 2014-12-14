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
public class WinnerDialogue {
    private String[] options = {"OK"};
    private JLabel label;
    private JPanel panel;
    private int result;
    GameMediator gameMediator;

    public WinnerDialogue(GameMediator gameMediator){
      	this.gameMediator = gameMediator;
    	panel = new JPanel();
    	label = new JLabel("Winner");
    	panel.add(label);
    }
    
	public void showDialogue(){
		label.setText("The winner is:" + gameMediator.getPlayerName(gameMediator.getPlayerTurn()));
		result = JOptionPane.showOptionDialog(null, panel,"WINNER", 
				 JOptionPane.NO_OPTION, JOptionPane.DEFAULT_OPTION,null, options , options[0]);
		
		if(result == 0){
			gameMediator.setStartGame(false);
			gameMediator.reset();
		}
	}
}
