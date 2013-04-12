package com.bitwaffle.guts.gui.console;

import java.util.StringTokenizer;

/**
 * Every command class should implement this and override the issue() function to carry out a command
 * 
 * @author TranquilMarmot
 */
public interface Command{
	/**
	 * This should issue the command for the class implementing this interface
	 * @param toker This will contain the rest of the command, excluding the command itself, separated by spaces
	 */
	public void issue(StringTokenizer toker);
	
	
	/** This should print out any help info about the command to the console */
	public void help();
}
