package com.bitwaffle.guts.gui.elements.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Used for creating an OutputStream that prints to a console
 * Also handles writing everything printed to the console to a log file
 * @author TranquilMarmot
 */
class ConsoleOutputStream extends OutputStream{
	/** Console to print to */
	private Console console;
	
	/** Stream for saving console text to a log */
	private FileOutputStream logStream;
	
	/** Whether or not we're writing to the log or System.out */
	private boolean writeToLog;
	
	/** 
	 * Each character printed is added to this string 
	 * Whenever a newline character is encountered, the string is printed
	 * to the console and then reset.
	 * A result of this is that using print() will add characters, but they won't
	 * actually be printed until println() is used or a newline character is printed. 
	 */
	String s;
	
	/**
	 * Create a console output stream
	 * @param console Console to print to
	 */
	public ConsoleOutputStream(Console console, boolean writeToLog){
		this.console = console;
		this.writeToLog = writeToLog;
		s = "";
		
		// create stream to log file
		if(writeToLog)
			logStream = getLogStream();
	}
	
	/** @return OutputStream that will write to log file */
	private FileOutputStream getLogStream(){
		try{
			// make sure logs directory exists
			File logDirectory = new File("logs/");
			if(!logDirectory.exists())
				logDirectory.mkdir();
			
			// create log file
			File log = new File("logs/" + getCurrentDate() + "-" + getCurrentTime() + ".log");
			log.createNewFile();
			
			return new FileOutputStream(log);
		}catch(IOException e){
			return null;
		}
	}
	
	@Override
	/** Writes a characater (or chaarcters) to the output stream */
	public void write(int wat) throws IOException {
		// get chars from int
		char[] chars = null;
		try{
			chars = Character.toChars(wat);
		} catch(IllegalArgumentException e){ return; }
		
		// only write if there's characters
		for(char c : chars){
			// handle newline
			if(c == '\n'){
				console.print(s);
				
				// write to log if enabled
				// TODO write "Day changed to MM-DD-YYYY" when the day changes
				if(writeToLog)
					logStream.write((getCurrentTime() + " - " + s + "\n").getBytes());

				s = "";
			} else{
				// append character(s) to current string
				s += Character.toString(c);
			}
		}
	}
	
	/** Enables writing console input to log file */
	public void enableLog(){
		writeToLog = true;
		
		if(logStream == null)
			logStream = getLogStream();
	}
	
	/** Disables writing to log */
	public void disableLog(){ writeToLog = false; }
	
	/** @return Whether or not the console is writing to a log right now */
	public boolean isWritingToLog(){ return writeToLog; }
	
	/** @return A string representing the current time, used for logging */
	private static String getCurrentTime(){
		// instantiating a gergorian calendar sets it to the current time and date
		Calendar calendar = new GregorianCalendar();

		int hours = calendar.get(Calendar.HOUR);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		String am_pm = (calendar.get(Calendar.AM_PM) == 0) ? "am" : "pm";

		//Format the string to match hh.mm.ss[am/pm]
		return String.format("%02d.%02d.%02d" + am_pm, hours, minutes, seconds);
	}
	
	/** @return A string representing the current date, used for logging */
	private static String getCurrentDate(){
		// instantiating a gergorian calendar sets it to the current time and date
		Calendar calendar = new GregorianCalendar();

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);

		/*
		 * Format the string to match mm.dd.yy NOTE:
		 * This won't work properly past the year 2100.
		 * Nick Achatz made me put in this comment, in case
		 * anyone past the year 2100 wants to thank him.
		 */
		return String.format("%02d.%02d.%d", month, day, year - 2000);
	}
}
