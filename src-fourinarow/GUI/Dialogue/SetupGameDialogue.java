package GUI.Dialogue;

import javax.swing.*;

import Mediator.GameMediator;

/**
 * 
 * @author Johan Forsblom
 *
 */
public class SetupGameDialogue {
    private String[] options = {"OK"};
    private JLabel label;
    private JTextField text;
    private JPanel panel;
    private GameMediator gameMediator;
   
    public SetupGameDialogue(GameMediator gameMediator) {
    	this.gameMediator = gameMediator;
    }

    public void EnterPlayerName(int player){
    	String tempText="";
    	text.setText("");
    	label.setText("Enter the name of player: " +player);
    	while(tempText.length()==0){
    		
    		int selectedOption = JOptionPane.showOptionDialog(null, panel, "Enter Player Name", 
    							 JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
    		
    		if(selectedOption == 0){       	
        		tempText = text.getText();
        	}    		
    	}
    	
	    if(player == 1){
	    	gameMediator.setPlayerName(player, tempText);
	    }
	    else if(player == 2){
	    	gameMediator.setPlayerName(player, tempText);
		}    	
    }
    
    public JPanel createPlayerDialogue(){
    	panel = new JPanel();
    	label = new JLabel("Enter the name of Player");
    	text = new JTextField(15);
    	panel.add(label);
    	panel.add(text);
    	return panel;
    }

}
