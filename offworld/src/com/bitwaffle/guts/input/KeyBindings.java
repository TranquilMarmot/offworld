package com.bitwaffle.guts.input;


/**
 * Bindings for all the special input for the game. They will all be initialized to their default values,
 * but they can easily be changed using the setKey() method.
 * Each binding it a InputButton[] so that multiple InputButtons can control one key binding.
 * @author TranquilMarmot
 * @see InputButton
 * @see Keys
 * @see InputButtons
 *
 */
public enum KeyBindings {
	CONTROL_LEFT(new InputButton[]{Keys.A, Keys.DPAD_LEFT, Keys.LEFT}),
	CONTROL_RIGHT(new InputButton[]{Keys.D, Keys.DPAD_RIGHT, Keys.RIGHT}),
	CONTROL_JUMP(new InputButton[]{Keys.SPACE, Keys.W, Keys.DPAD_UP, Keys.UP, Keys.BUTTON_A}),

	SYS_CAMERA_MODE(new InputButton[]{Keys.C}),
	SYS_CHAT(new InputButton[]{Keys.T}),
	SYS_PAUSE(new InputButton[]{Keys.ESCAPE, Keys.BACK, Keys.BUTTON_START}),
	SYS_FULLSCREEN(new InputButton[]{Keys.F11}),
	SYS_DEBUG(new InputButton[]{Keys.F3}),
	SYS_SCREENSHOT(new InputButton[]{Keys.F2}),
	SYS_DEBUG_PHYSICS(new InputButton[]{Keys.F4}),

	SYS_CONSOLE_TOGGLE(new InputButton[]{Keys.GRAVE}),
	SYS_CONSOLE_PREVIOUS_COMMAND(new InputButton[]{Keys.UP}),
	SYS_CONSOLE_NEXT_COMMAND(new InputButton[]{Keys.DOWN}),
	SYS_CONSOLE_SUBMIT(new InputButton[]{Keys.ENTER}),
	SYS_CONSOLE_BACKSPACE(new InputButton[]{Keys.DEL}),
	SYS_CONSOLE_SCROLL_UP(new InputButton[]{Keys.PAGE_UP}),
	SYS_CONSOLE_SCROLL_DOWN(new InputButton[]{Keys.PAGE_DOWN}),
	SYS_CONSOLE_LEFT(new InputButton[]{Keys.DPAD_LEFT, Keys.LEFT}),
	SYS_CONSOLE_RIGHT(new InputButton[]{Keys.DPAD_RIGHT, Keys.RIGHT}),
	SYS_CONSOLE_HOME(new InputButton[]{Keys.HOME}),
	SYS_CONSOLE_END(new InputButton[]{Keys.END}),
	SYS_CONSOLE_DELETE(new InputButton[]{Keys.FORWARD_DEL});

	/** the InputButtons that activate this binding */
	private InputButton[] inputButtons;
	
	/** Override */
	private boolean pressed = false, stillDown = false;

	/**
	 * KeyBindings constructor
	 * @param InputButtons InputButtons to use for the binding
	 */
	private KeyBindings(InputButton[] InputButtons){
		this.inputButtons = InputButtons;
	}
	
	/**
	 * Press a key binding
	 */
	public void press(){
		this.pressed = true;
		this.stillDown = true;
	}

	/**
	 * Release a key binding
	 */
	public void release(){
		this.pressed = false;
		this.stillDown = false;
	}

	/**
	 * @return Whether or not the binding is being pressed
	 */
	public boolean isPressed(){
		if(pressed){
			return true;
		} else{
			for(InputButton inputButton : inputButtons){
				if(inputButton.isPressed())
					return true;
			}
		}
		return false;
	}

	/**
	 * @return True if this is the first call to pressedOnce since the key was pressed, else false
	 */
	public boolean pressedOnce(){
		if(pressed){
			if(stillDown){
				stillDown = false;
				return true;
			}else{
				return false;
			}
		} else {
			for(InputButton inputButton : inputButtons){
				if(inputButton.pressedOnce())
					return true;
			}
		}
		return false;
	}

	/**
	 * Set the binding to have a new set of InputButtons
	 * @param newKey Key to set binding to
	 */
	public void setInputButtons(InputButton[] newInputButtons){
		this.inputButtons = newInputButtons;
	}
	
	/**
	 * @return All the input buttons that activate this KeyBinding
	 */
	public InputButton[] getInputButtons(){
		return inputButtons;
	}

	/**
	 * Adds a InputButton that will activate the binding
	 * @param newInputButton InputButton to add to binding
	 */
	public void addInputButton(InputButton newInputButton){
		InputButton[] newKeys = new Keys[inputButtons.length + 1];
		for(int i = 0; i < inputButtons.length; i++)
			newKeys[i] = inputButtons[i];
		newKeys[inputButtons.length] = newInputButton;
		this.setInputButtons(newKeys);
	}

	/**
	 * Removes a InputButton that activated the binding (does nothing if InputButton doesn't already activate binding)
	 * @param oldInputButton InputButton to remove
	 */
	public void removeInputButton(InputButton oldInputButton){
		// index of oldInputButton in InputButtons
		int bIndex = -1;

		// find bIndex
		for(int i = 0; i < inputButtons.length; i++){
			if(inputButtons[i] == oldInputButton)
				bIndex = i;
		}

		// found oldInputButton
		if(bIndex != -1){
			InputButton[] newInputButtons = new InputButton[inputButtons.length - 1];
			for(int i = 0; i < inputButtons.length; i++){
				// skip the InputButton being removed
				if(i == bIndex)
					continue;

				// add old InputButtons, minus the old InputButton
				if(i < bIndex)
					newInputButtons[i] = inputButtons[i];
				else
					newInputButtons[i - 1] = inputButtons[i];
			}

			this.setInputButtons(newInputButtons);
		}
	}

	/**
	 * Checks to see if the binding is activated by the given InputButton
	 * @param other InputButton to check for
	 * @return Whether or not the InputButton activates the binding
	 */
	public boolean isInputButton(InputButton other){
		for(InputButton key : inputButtons){
			if(key == other)
				return true;
		}
		return false;
	}

	/**
	 * To string, or not to string -- that is the method
	 * Whether 'tis nobler in the mind to return
	 * The strings and arrows of outrageous fortune
	 * Or to take arms against a sea of exceptions
	 * And by opposing them. To System.exit(0), to sleep()--
	 * No more
	 * 
	 * Shakespeare while you code!
	 */
	public String toString(){
		String ret = "";
		for(int i = 0; i < inputButtons.length; i++){
			ret += inputButtons[i].toString() + " ";
		}

		return ret;
	}
}