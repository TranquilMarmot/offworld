package com.bitwaffle.moguts.device;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.bitwaffle.offworld.Game;

/**
 * Used to get text input from the user via a popup
 * dialog box
 * 
 * @author TranquilMarmot
 */
public abstract class TextInput {
	/** The dialog box */
	AlertDialog.Builder alert;
	
	/**
	 * Create a new user input prompt
	 * @param alertTitle Title of dialog box
	 * @param alertMessage Message in dialog box
	 */
	public TextInput(String alertTitle, String alertMessage){
    	final EditText input = new EditText(Game.resources.getContext());
    	
    	alert = new AlertDialog.Builder(Game.resources.getContext());
    	alert.setTitle(alertTitle);
    	alert.setMessage(alertMessage);
    	alert.setView(input);
    	
    	CustomOnClickListener listener = new CustomOnClickListener(input);
    	alert.setPositiveButton("Ok", listener);
    	
    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
		});
	}
	
	/**
	 * Anything that asks for user input needs to override this.
	 * When the 'Ok' button is pressed, this gets called with
	 * the input from the user
	 * @param input Input from user
	 */
	public abstract void parseInput(String input);
	
	/**
	 * Pops up the dialog box to prompt for input
	 */
	public void askForInput(){
		alert.show();
	}
	
	/**
	 * A class that calls parseInput(String input) when the button is clicked
	 * (pretty hardcore java trickery right here!)
	 */
	private class CustomOnClickListener implements DialogInterface.OnClickListener{
		/** Where the input is coming from */
		private EditText input;
		
		/**
		 * Create a new listener that will call the parseInput method
		 * @param input Input that will have text from user
		 */
		public CustomOnClickListener(final EditText input){
			this.input = input;
		}

		/**
		 * Just sends to text to the parseInput method
		 */
		public void onClick(DialogInterface dialog, int which) {
			parseInput(input.getText().toString());
		}
	}
}
