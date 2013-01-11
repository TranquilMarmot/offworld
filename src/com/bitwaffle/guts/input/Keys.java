package com.bitwaffle.guts.input;


/**
 * All the possible keys on the keyboard
 * @author TranquilMarmot
 * @see KeyBindings
 *
 */
public enum Keys implements InputButton{
	/* 
	 * If you notice any missing keys, add them!
	 * It's important that all of these have the same exact name
	 * as the string returned by Keyboard.getKeyName(eventKey);
	 * If the key you want doesn't start with a letter (i.e. all the numbers),
	 * then add it with a unique name and add it to the switch
	 * statement in the KeyboardManager.getKey() method
	 */
	ESCAPE,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,SYSRQ,SCROLL,PAUSE,
	GRAVE,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,ZERO,MINUS,EQUALS,BACK,
	TAB,Q,W,E,R,T,Y,U,I,O,P,LBRACKET,RBRACKET,BACKSLASH,
	CAPITAL,A,S,D,F,G,H,J,K,L,SEMICOLON,COLON,APOSTROPHE,RETURN,
	LSHIFT,Z,X,C,V,B,N,M,COMMA,PERIOD,SLASH,RSHIFT,
	LCONTROL,LWIN,LMENU,SPACE,RMENU,RWIN,APPS,RCONTROL,
	HOME,END,INSERT,PRIOR,NEXT,DELETE,
	UP,DOWN,LEFT,RIGHT,
	NUMLOCK,DIVIDE,MULTIPLY,SUBTRACT,ADD,DECIMAL,
	NUMPAD1,NUMPAD2,NUMPAD3,NUMPAD4,NUMPAD5,NUMPAD6,NUMPAD7,NUMPAD8,NUMPAD9,NUMPAD0,NUMPADENTER,
	UNDERLINE,CIRCUMFLEX,AT,
	NONE;

	/** Whether or not the key is being pressed right now */
	private boolean isPressed;

	/** Whether or not the key is still down from the previous update */
	private boolean stillDown;
	
	/** Time that key went down (to know how long it's being held) */
	private long timePressed;

	/**
	 * Constructor
	 */
	private Keys(){
		isPressed = false;
		stillDown = false;
		timePressed = 0L;
	}

	@Override
	/**
	 * Notify the key that it's been pressed
	 */
	public void press(){
		this.isPressed = true;
		this.stillDown = true;
		timePressed = System.currentTimeMillis();
	}

	@Override
	/**
	 * Notify the key that it's been released
	 */
	public void release(){
		this.isPressed = false;
		this.stillDown = false;
		timePressed = 0L;
	}

	@Override
	/**
	 * @return Whether or not the key is being pressed
	 */
	public boolean isPressed(){
		return isPressed;
	}

	@Override
	/**
	 * @return True if this is the first call to pressedOnce since the key was pressed, else false
	 */
	public boolean pressedOnce(){
		// if the key is still down from the previous frame, trigger the press event and make it so the button needs to be pressed again
		if(stillDown){
			stillDown = false;
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * @return How long the key has been down (0 if it's not down)
	 */
	public long timeDown(){
		if(timePressed == 0)
			return 0L;
		else
			return System.currentTimeMillis() - timePressed;
	}
}