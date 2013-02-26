package com.bitwaffle.guts.input;


/**
 * All the possible keys on the keyboard
 * @author TranquilMarmot
 * @see KeyBindings
 *
 */
public enum Keys implements InputButton{
	// these all came from LibGDX's Input.Keys class
	NUM_0,
	NUM_1,
	NUM_2,
	NUM_3,
	NUM_4,
	NUM_5,
	NUM_6,
	NUM_7,
	NUM_8,
	NUM_9,
	A,
	ALT_LEFT,
	ALT_RIGHT,
	APOSTROPHE,
	AT,
	B,
	BACK,
	BACKSLASH,
	C,
	CALL,
	CAMERA,
	CLEAR,
	COMMA,
	D,
	DEL,
	BACKSPACE,
	FORWARD_DEL,
	DPAD_CENTER,
	DPAD_DOWN,
	DPAD_LEFT,
	DPAD_RIGHT,
	DPAD_UP,
	CENTER,
	DOWN,
	LEFT,
	RIGHT,
	UP,
	E,
	ENDCALL,
	ENTER,
	ENVELOPE,
	EQUALS,
	EXPLORER,
	F,
	FOCUS,
	G,
	GRAVE,
	H,
	HEADSETHOOK,
	HOME,
	I,
	J,
	K,
	L,
	LEFT_BRACKET,
	M,
	MEDIA_FAST_FORWARD,
	MEDIA_NEXT,
	MEDIA_PLAY_PAUSE,
	MEDIA_PREVIOUS,
	MEDIA_REWIND,
	MEDIA_STOP,
	MENU,
	MINUS,
	MUTE,
	N,
	NOTIFICATION,
	NUM,
	O,
	P,
	PERIOD,
	PLUS,
	POUND,
	POWER,
	Q,
	R,
	RIGHT_BRACKET,
	S,
	SEARCH,
	SEMICOLON,
	SHIFT_LEFT,
	SHIFT_RIGHT,
	SLASH,
	SOFT_LEFT,
	SOFT_RIGHT,
	SPACE,
	STAR,
	SYM,
	T,
	TAB,
	U,
	UNKNOWN,
	V,
	VOLUME_DOWN,
	VOLUME_UP,
	W,
	X,
	Y,
	Z,
	META_ALT_LEFT_ON,
	META_ALT_ON,
	META_ALT_RIGHT_ON,
	META_SHIFT_LEFT_ON,
	META_SHIFT_ON,
	META_SHIFT_RIGHT_ON,
	META_SYM_ON,
	CONTROL_LEFT,
	CONTROL_RIGHT,
	ESCAPE,
	END,
	INSERT,
	PAGE_UP,
	PAGE_DOWN,
	PICTSYMBOLS,
	SWITCH_CHARSET,
	BUTTON_CIRCLE,
	BUTTON_A,
	BUTTON_B,
	BUTTON_C,
	BUTTON_X,
	BUTTON_Y,
	BUTTON_Z,
	BUTTON_L1,
	BUTTON_R1,
	BUTTON_L2,
	BUTTON_R2,
	BUTTON_THUMBL,
	BUTTON_THUMBR,
	BUTTON_START,
	BUTTON_SELECT,
	BUTTON_MODE,
	COLON,
	F1,
	F2,
	F3,
	F4,
	F5,
	F6,
	F7,
	F8,
	F9,
	F10,
	F11,
	F12;

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