package com.bitwaffle.guts.gui.elements.console;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.graphics2d.font.BitmapFont;
import com.bitwaffle.guts.gui.elements.GUIObject;


/**
 * Console for printing text and interacting with the game world.
 * See {@link ConsoleCommands} for a list of available commands.
 * Check out {@link KeyboardManager} for keys checked when the console is up, and for the code that sends chars to the console
 * If you want to see how printing everything and logging work, check out {@link ConsoleOutputStream}
 * 
 * @author TranquilMarmot
 * @author arthurdent
 * 
 */
public class Console extends GUIObject{
	/*
	 * TODO: Scroll left/right in current input
	 * TODO: Fix backspace erratic behvaior
	 * TODO: Chat colors
	 * TODO: Implement tabs 
	 * TODO: Fix scrolling so it only happens in the tab you have open
	 */
	
	/**
	 * Info on what to do with text sent to this console.
	 * These can be changed at runtime by calling methods in the ConsoleOutputstream class.
	 */
	private static final boolean PRINT_TO_LOG = false;
	/** This replaces Game.out and System.err, and also enables printing to a log file */
	protected ConsoleOutputStream outputStream;
	public PrintStream out;
	
	
	/** Whether or not the console is currently on */
	private boolean consoleOn = false;
	
	/** The text currently being typed into the console */
	private String input;
	
	/** All the text that the console contains and will print out */
	private ArrayList<String> text;
	/** If scroll is 0, we're at the most recent line, 1 is one line up, 2 is two lines up, etc */
	private int scroll;
	
	/** History of input */
	private ArrayList<String> inputHistory;
	/** Index into history list, for editing previous inputs */
	private int inputHistoryIndex;
	
	/** If blink is true, there's an underscore at the end of the input string, else there's not */
	private boolean blink = true;
	/** Counter for blink */
	private int blinkCount = 0;
	/** How often to blink (this is changed to match the current FPS) */
	private int blinkInterval = 30;
	
	/** Whether or not to close the console when line is submitted */
	public boolean autoClose = false;

	/** Maximum alpha for console text */
	private float textMaxAlpha = 0.9f;
	/** Minimum alpha for console text */
	private float textMinAlpha = 0.0f;

	/** Alpha difference for each update */
	private float textFadeStep = 0.012f;
	/** Time in seconds before the text begins to fade */
	private float textFadeDelay = 0.6f;

	/** Current alpha for console text */
	private float textAlpha = textMinAlpha;
	/** How long since the console went off */
	private float textFadeDelayTimer = 0.0f;
	
	/** Maximum alpha for console background */
	private float consoleMaxAlpha = 0.3f;
	/** Minimum alpha for console background */
	private float consoleMinAlpha = 0.0f;
	
	/** Alpha difference for each update */
	private float consoleFadeStep = 0.009f;
	/** Time in seconds before background begins to fade */
	private float consoleFadeDelay = 0.25f;
	
	/** Current background alpha */
	private float consoleAlpha = consoleMinAlpha;
	/** How long the console has been off */
	private float consoleFadeDelayTimer = 0.0f;
	
	/** Alpha of box behind input */
	private float inputBoxAlpha = 0.4f;
	
	/** Height and width of the console, in characters (multiply by character size to get total console size) */
	private int numRows, numCols;
	
	/** How big the console gets rendered (1.0 = normal size, 0.5 = half, 2.0 = twice, etc.) */
	private float scale = 0.5f;
	
	/**
	 * Default console constructor
	 */
	public Console() {
		this(BitmapFont.FONT_GLYPH_WIDTH, Game.windowHeight - BitmapFont.FONT_GLYPH_HEIGHT, 65, 14);
	}

	/**
	 * Creates a new console with the specified parameters
	 * 
	 * @param x
	 *            distance from the left side of the screen
	 * @param y
	 *            distance from the bottom of the screen
	 * @param numCols
	 *            window width expanding to the right
	 * @param numRows
	 *            window height expanding up
	 */
	public Console(float x, float y, int numCols, int numRows) {
		super(new ConsoleRenderer(), x, y);
		this.x = x;
		this.y = y;
		this.numCols = numCols;
		this.numRows = numRows;

		input = "";
		autoClose = false;
		scroll = 0;

		text = new ArrayList<String>();
		inputHistory = new ArrayList<String>();
		inputHistory.add("");
		inputHistoryIndex = 0;
		updateCurrentCommandHistory();
		
		// set Game.out to print to the console
		outputStream = new ConsoleOutputStream(this, PRINT_TO_LOG);
		PrintStream stream = new PrintStream(outputStream);
		Game.out = stream;
		
		out = new PrintStream(outputStream);
	}
	
	public boolean consoleOn(){ return consoleOn; }
	public float consoleAlpha(){ return consoleAlpha; }
	public float textAlpha(){ return textAlpha; }
	public ArrayList<String> text(){ return text; }
	public int numRows(){ return numRows; }
	public int numCols(){ return numCols; }
	public int currentScroll(){ return scroll; }
	public float scale(){ return scale; }
	public String input(){ return input; }
	public boolean blink(){ return blink; }
	public float inputBoxAlpha(){ return inputBoxAlpha; }
	
	/** Update the console */
	public void update(float timeStep) {
		this.x = BitmapFont.FONT_GLYPH_WIDTH * scale;
		this.y = Game.windowHeight ;
		

		if (consoleOn) {
			// Brighten the console
			this.wake();
			
			// do blinking effect
			updateBlink();
		} else {
			fadeText();
			fadeBackground();
		}
		
	}
	
	/** Fades out the text on a per-frame basic */
	private void fadeText(){
		if (textAlpha > textMinAlpha
				&& textFadeDelayTimer >= textFadeDelay) {
			// subtract alpha from the current text alpha
			textAlpha -= textFadeStep;
		} else if (textFadeDelayTimer < textFadeDelay) {
			// add time to timer (text only fades once timer is done)
			textFadeDelayTimer += 1.0f / Game.currentFPS;
		}
	}
	
	/** Fade the background on a per-frame basis */
	private void fadeBackground(){
		if(consoleAlpha > consoleMinAlpha
		&& consoleFadeDelayTimer >= consoleFadeDelay){
			// subtract alpha from current console alpha
			consoleAlpha -= consoleFadeStep;
		} else if(consoleFadeDelayTimer < consoleFadeDelay){
			// add time to timer (text only fades once timer is done)
			consoleFadeDelayTimer += 1.0f / Game.currentFPS;
		}
	}
	
	/** Opens the console with a slash and closes on submission */
	public void openWithSlash(){
		this.show();
		this.input = "/";
		this.autoClose = true;
	}

	/**
	 * Puts a character into the input for the console
	 * These generally come from a KeyboardManager
	 * 
	 * @param c
	 *            The character to add to the input
	 */
	public void putCharacter(Character c) {
		input += c;
		updateCurrentCommandHistory();
	}
	
	/** Goes back one space in the input string */
	public void backspace() {
		if (input.length() > 0)
			input = input.substring(0, input.length() - 1);
		updateCurrentCommandHistory();
	}
	
	/**
	 * Deletes multiple chars
	 * @param amount Number of chars to delete
	 */
	public void backspace(int amount){
		for(int i = 0; i < amount; i++)
			backspace();
	}
	
	/**
	 * Adds a string to the console, wraps if necessary. Also, the console will
	 * brighten if dim.
	 * 
	 * @param s
	 *            The string to print to the console
	 */
	protected void print(String s) {
		// TODO should have some sort of super transparent mode to 'notify' of stuff
		// make the console text opaque
		this.wake();

		// make the string an array of words
		String words[] = s.split(" ");
		
		if(words.length <= 0)
			return;

		// the current number of characters, and the iterator
		int currentWidth = 0;

		// if the next word is too big, time to split the line.
		if (words[0].length() >= numCols) {
			currentWidth = numCols;
		} else {
			// otherwise, for each word:
			for (int i = 0;
			     i < words.length && (currentWidth + words[i].length()) <= numCols;
			     i++)
				// Add the width of that word and a space, unless the combined number of characters exceeds the width of the console.
				currentWidth += words[i].length() + 1;
		}

		// add the words to the scrollback
		try{
			text.add(s.substring(0, currentWidth - 1));
		} catch (StringIndexOutOfBoundsException e){
			currentWidth++;
			text.add(s.substring(0, currentWidth - 1));
		}

		// recurse and print the remaining stuff.
		if (s.length() >= numCols){
			try{
				print(s.substring(currentWidth));
			} catch(StringIndexOutOfBoundsException e){}
		}
	}
	
	/**
	 * What happens when the SYS_CONSOLE_SUBMIT key is pressed.
	 * If the current input starts with a /, this issues a command.
	 */
	public void submit() {
		if (input.length() > 0) {
			// trim off any whitespace
			input.trim();
			// do a command if the input starts with a /
			if (input.charAt(0) == '/')
				issueCommand(input);
			// otherwise just add it to the text
			else
				Game.out.println("<" + System.getProperty("user.name") + "> " + input); // TODO give the player a name

			// adds the input to the command history
			if (!inputHistory.get(0).equals(""))
				inputHistory.add(0, "");
			else
				inputHistory.add(1, input);

			// rolls to the new empty spot in the commandHistory List
			scrollHistory(0);

			scroll = 0;
		}
		// clear the input string (this is important!)
		input = "";

		// close the console if autoclose is enabled
		if (autoClose) {
			autoClose = false;
			this.hide();
		}
	}
	
	
	/**
	 * Issues the given command. See {@link ConsoleCommands}.
	 * 
	 * @param comm
	 *            Command to issue
	 */
	public void issueCommand(String comm) {
		// make sure the command isn't empty
		if (comm.length() > 1) {
			// split the command at the spaces
			StringTokenizer toker = new StringTokenizer(comm, " ");

			/*
			 * Grab the actual command and lop off the / at the beginning
			 * Note that calling toker.nextToken() advances the tokenizer to the next token.
			 * This way, when the tokenizer is passed on to the command being executed,
			 * calling toker.nextToken() again will give you the first argument for the command.
			 */
			String commandString = toker.nextToken();
			commandString = commandString.substring(1, commandString.length());

			if(commandString.charAt(0) == '?')
				commandString = "help";

			try{
				ConsoleCommands.valueOf(commandString).issue(toker);
			} catch (NumberFormatException e) {
				Game.out.println("Incorrect number format " + e.getLocalizedMessage().toLowerCase());
			} catch (IllegalArgumentException e) {
				Game.out.println("Command not found! (" + commandString + ")");
			} catch (NoSuchElementException e) {
				Game.out.println("Not enough vairbales for command '"
						+ commandString + "'!");
			}
		}
	}

	/** Changes the value of the current spot in the command history */
	private void updateCurrentCommandHistory() {
		inputHistory.set(inputHistoryIndex, input);
	}
	
	/** Clears all text in the console */
	public void clear() { text.clear(); }

	/**
	 * Scroll up
	 * 
	 * @param amount
	 *            Number of lines to scroll up
	 */
	public void scrollUp(int amount) {
		this.wake();
		if (scroll - amount >= 0)
			scroll -= amount;
	}

	/**
	 * Scroll down
	 * 
	 * @param amount
	 *            Number of lines to scroll down
	 */
	public void scrollDown(int amount) {
		this.wake();
		if (scroll + amount < text.size())
			scroll += amount;
	}
	
	/** Scroll through lines in console. Negative values ok. */
	public void scroll(int amount){
		this.wake();
		scroll += amount;
		if(scroll >= text.size())
			scroll = text.size() - 1;
		else if(scroll <= 0)
			scroll = 0;
	}

	/**
	 * Scroll through console submission history
	 * 
	 * @param amount
	 *            How much to scroll through history- negative number goes to older submissions, positive goes to newer submissions
	 */
	public void scrollHistory(int amount) {
		if(amount < 0){
			// scroll to older submission
			if(inputHistoryIndex - amount < inputHistory.size())
				inputHistoryIndex -= amount;
			else
				inputHistoryIndex = inputHistory.size() - 1;
		} else if (amount > 0){
			// scroll to newer submission
			if(inputHistoryIndex - amount > 0)
				inputHistoryIndex -= amount;
			else
				inputHistoryIndex = 0;
		} else
			inputHistoryIndex = 0;
		
		input = inputHistory.get(inputHistoryIndex);
	}

	/** Makes the text bright until the fade delay is reached again. */
	public void wake() {
		// Set the alpha  values to the max alpha values
		textAlpha = textMaxAlpha;
		consoleAlpha = consoleMaxAlpha;
		// Reset the current fade delay timer
		textFadeDelayTimer = 0.0f;
		consoleFadeDelayTimer = 0.0f;
	}
	
	/** @return Whether or not this console is on */
	public boolean isOn(){ return this.consoleOn; }
	
	/** Hides the console */
	public void hide(){
		this.consoleOn = false;
	}
	
	/** Show the console */
	public void show(){
		this.consoleOn = true;
		wake();
	}
	
	/** Toggles console visibility */
	public void toggle(){
		if(this.isOn())
			this.hide();
		else
			this.show();
	}

	/**
	 * This gets called every time that the console is drawn,
	 * to make the underscore at the end of the input blink
	 */
	private void updateBlink() {
		// blink twice every second
		blinkInterval = Game.currentFPS / 2;

		if (blink) {
			blinkCount++;
			if (blinkCount >= blinkInterval)
				blink = false;
		} else {
			blinkCount--;
			if (blinkCount <= 0)
				blink = true;
		}
	}

	@Override
	public void cleanup() {
		try{
			outputStream.flush();
			outputStream.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
