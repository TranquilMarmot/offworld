package com.bitwaffle.guts.android.input;

import android.view.KeyEvent;

import com.bitwaffle.guts.gui.GUI;
import com.bitwaffle.guts.input.KeyBindings;
import com.bitwaffle.guts.input.Keys;

public class KeyboardManager {
	/** These keys are checked even when the console is on (if they weren't on this list, the game would attempt to print them to the console which wouldn't do anything) */
	private final static KeyBindings[] checkedWhenConsoleIsOn = {
			KeyBindings.SYS_PAUSE,
			KeyBindings.SYS_CONSOLE,
			KeyBindings.SYS_SCREENSHOT,
			KeyBindings.SYS_DEBUG,
			KeyBindings.SYS_DEBUG_PHYSICS,
			KeyBindings.SYS_CONSOLE_BACKSPACE,
			KeyBindings.SYS_CONSOLE_NEXT_COMMAND,
			KeyBindings.SYS_CONSOLE_PREVIOUS_COMMAND,
			KeyBindings.SYS_CONSOLE_SCROLL_DOWN,
			KeyBindings.SYS_CONSOLE_SCROLL_UP,
			KeyBindings.SYS_CONSOLE_SUBMIT,
	};
	
	public static boolean keyDown(int keyCode, KeyEvent event){
		Keys key = getKey(keyCode);
		if(key != null){
			// if the console is on, we'll check if the key being pressed is a special key, else we'll write to the console
			if(GUI.console.isOn()){
				//if this turns to true, it means the key isn't written to the console and is pressed instead
				boolean specialKey = false;

				// go through all the special keys
				for(KeyBindings binding : checkedWhenConsoleIsOn){
					if(binding.isInputButton(key)){
						//found a special key
						specialKey = true;
						break;
					}
				}

				// print to the console if it's not a special key
				if(specialKey){
					key.press();
				} else{
					Character c =  getPrintableChar(event);
					if (!Character.isIdentifierIgnorable(c) && !c.equals('`')
							&& !c.equals('\n') && !c.equals('\r')) {
						GUI.console.putCharacter(c);
					}
				} 
			}else {
				key.press();
			}
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * Get a printable character from a KeyEvent
	 * @param event Event to get char from
	 * @return Printable char (or \0 if char not found)
	 */
	private static Character getPrintableChar(KeyEvent event){		
		if(event.isPrintingKey()){
			return (char) event.getUnicodeChar(event.getMetaState());
		} else{
			// TODO so far this is the only special case I've found that needs printed
			switch(event.getKeyCode()){
			case KeyEvent.KEYCODE_SPACE:
				return ' ';
			default:
				return '\0';
			}
		}
	}
	
	public static boolean keyUp(int keyCode, KeyEvent event){
		Keys key = getKey(keyCode);
		if(key != null){
			key.release();
			return true;
		} else{
			return false;
		}
	}
	
	
	public static Keys getKey(int keyCode){
		switch(keyCode){
		case KeyEvent.KEYCODE_0:
			return Keys.ZERO;
		case KeyEvent.KEYCODE_1:
			return Keys.ONE;
		case KeyEvent.KEYCODE_2:
			return Keys.TWO;
		case KeyEvent.KEYCODE_3:
			return Keys.THREE;
		case KeyEvent.KEYCODE_4:
			return Keys.FOUR;
		case KeyEvent.KEYCODE_5:
			return Keys.FIVE;
		case KeyEvent.KEYCODE_6:
			return Keys.SIX;
		case KeyEvent.KEYCODE_7:
			return Keys.SEVEN;
		case KeyEvent.KEYCODE_8:
			return Keys.EIGHT;
		case KeyEvent.KEYCODE_9:
			return Keys.NINE;
		case KeyEvent.KEYCODE_A:
			return Keys.A;
		case KeyEvent.KEYCODE_B:
			return Keys.B;
		case KeyEvent.KEYCODE_C:
			return Keys.C;
		case KeyEvent.KEYCODE_D:
			return Keys.D;
		case KeyEvent.KEYCODE_E:
			return Keys.E;
		case KeyEvent.KEYCODE_F:
			return Keys.F;
		case KeyEvent.KEYCODE_G:
			return Keys.G;
		case KeyEvent.KEYCODE_H:
			return Keys.H;
		case KeyEvent.KEYCODE_I:
			return Keys.I;
		case KeyEvent.KEYCODE_J:
			return Keys.J;
		case KeyEvent.KEYCODE_K:
			return Keys.K;
		case KeyEvent.KEYCODE_L:
			return Keys.L;
		case KeyEvent.KEYCODE_M:
			return Keys.M;
		case KeyEvent.KEYCODE_N:
			return Keys.N;
		case KeyEvent.KEYCODE_O:
			return Keys.O;
		case KeyEvent.KEYCODE_P:
			return Keys.P;
		case KeyEvent.KEYCODE_Q:
			return Keys.Q;
		case KeyEvent.KEYCODE_R:
			return Keys.R;
		case KeyEvent.KEYCODE_S:
			return Keys.S;
		case KeyEvent.KEYCODE_T:
			return Keys.T;
		case KeyEvent.KEYCODE_U:
			return Keys.U;
		case KeyEvent.KEYCODE_V:
			return Keys.V;
		case KeyEvent.KEYCODE_W:
			return Keys.W;
		case KeyEvent.KEYCODE_X:
			return Keys.X;
		case KeyEvent.KEYCODE_Y:
			return Keys.Y;
		case KeyEvent.KEYCODE_Z:
			return Keys.Z;
		case KeyEvent.KEYCODE_BACK:
			return Keys.ESCAPE;
		case KeyEvent.KEYCODE_APOSTROPHE:
			return Keys.APOSTROPHE;
		case KeyEvent.KEYCODE_AT:
			return Keys.AT;
		case KeyEvent.KEYCODE_BACKSLASH:
			return Keys.BACKSLASH;
		case KeyEvent.KEYCODE_COMMA:
			return Keys.COMMA;
		case KeyEvent.KEYCODE_DEL:
			return Keys.BACK;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			return Keys.RETURN;
		case KeyEvent.KEYCODE_DPAD_UP:
			return Keys.UP;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			return Keys.DOWN;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			return Keys.LEFT;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return Keys.RIGHT;
		case KeyEvent.KEYCODE_ENTER:
			return Keys.RETURN;
		case KeyEvent.KEYCODE_EQUALS:
			return Keys.EQUALS;
		case KeyEvent.KEYCODE_GRAVE:
			return Keys.GRAVE;
		case KeyEvent.KEYCODE_LEFT_BRACKET:
			return Keys.LBRACKET;
		case KeyEvent.KEYCODE_RIGHT_BRACKET:
			return Keys.RBRACKET;
		case KeyEvent.KEYCODE_MENU:
			return Keys.ESCAPE; // TODO handle this maybe?
		case KeyEvent.KEYCODE_MINUS:
			return Keys.MINUS;
		case KeyEvent.KEYCODE_PERIOD:
			return Keys.PERIOD;
		//case KeyEvent.KEYCODE_PLUS:
		//	return Keys.PLUS;
		//case KeyEvent.KEYCODE_POUND:
		//	return Keys.POUNS
		case KeyEvent.KEYCODE_SEMICOLON:
			return Keys.SEMICOLON;
		case KeyEvent.KEYCODE_SLASH:
			return Keys.SLASH;
		case KeyEvent.KEYCODE_SPACE:
			return Keys.SPACE;
		case KeyEvent.KEYCODE_SHIFT_LEFT:
			return Keys.LSHIFT;
		case KeyEvent.KEYCODE_SHIFT_RIGHT:
			return Keys.RSHIFT;
		//case KeyEvent.KEYCODE_STAR:
		//	return Keys.STAR;
		case KeyEvent.KEYCODE_TAB:
			return Keys.TAB;
		case 113/* KeyEvent.KEYCODE_CTRL_LEFT FIXME for some reason this isn't in the KeyEvent class??? */:
			return Keys.LCONTROL;
		//case KeyEvent.KEYCODE_CTRL_RIGHT:
		//	return Keys.RCONTROL;
		case KeyEvent.KEYCODE_UNKNOWN:
			return null;
		}
		
		return null;
	}
}
