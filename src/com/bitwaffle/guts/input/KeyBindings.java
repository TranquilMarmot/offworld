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
	CONTROL_LEFT(new InputButton[]{Keys.A, Keys.LEFT}),
	CONTROL_RIGHT(new InputButton[]{Keys.D, Keys.RIGHT}),
	CONTROL_JUMP(new InputButton[]{Keys.SPACE, Keys.W, Keys.UP}),

	SYS_CAMERA_MODE(new InputButton[]{Keys.C}),
	SYS_CONSOLE(new InputButton[]{Keys.GRAVE}),
	SYS_COMMAND(new InputButton[]{Keys.SLASH}),
	SYS_CHAT(new InputButton[]{Keys.T}),
	SYS_PAUSE(new InputButton[]{Keys.ESCAPE}),
	SYS_FULLSCREEN(new InputButton[]{Keys.F11}),
	SYS_DEBUG(new InputButton[]{Keys.F3}),
	SYS_SCREENSHOT(new InputButton[]{Keys.F2}),
	SYS_DEBUG_PHYSICS(new InputButton[]{Keys.F4});

	/*
	SYS_CONSOLE_PREVIOUS_COMMAND(new InputButton[]{Keys.UP}),
	SYS_CONSOLE_NEXT_COMMAND(new InputButton[]{Keys.DOWN}),
	SYS_CONSOLE_SUBMIT(new InputButton[]{Keys.RETURN}),
	SYS_CONSOLE_BACKSPACE(new InputButton[]{Keys.BACK}),
	SYS_CONSOLE_SCROLL_UP(new InputButton[]{Keys.NEXT}),
	SYS_CONSOLE_SCROLL_DOWN(new InputButton[]{Keys.PRIOR}),

	BUILDER_OPEN_ADD_MENU(new InputButton[]{Keys.TAB});
	*/

	/** the InputButtons that activate this binding */
	private InputButton[] InputButtons;

	/**
	 * KeyBindings constructor
	 * @param InputButtons InputButtons to use for the binding
	 */
	private KeyBindings(InputButton[] InputButtons){
		this.InputButtons = InputButtons;
	}

	/**
	 * @return Whether or not the binding is being pressed
	 */
	public boolean isPressed(){
		for(InputButton InputButton : InputButtons){
			if(InputButton.isPressed())
				return true;
		}
		return false;
	}

	/**
	 * @return True if this is the first call to pressedOnce since the key was pressed, else false
	 */
	public boolean pressedOnce(){
		for(InputButton InputButton : InputButtons){
			if(InputButton.pressedOnce())
				return true;
		}
		return false;
	}

	/**
	 * Set the binding to have a new set of InputButtons
	 * @param newKey Key to set binding to
	 */
	public void setInputButtons(InputButton[] newInputButtons){
		this.InputButtons = newInputButtons;
	}

	/**
	 * Adds a InputButton that will activate the binding
	 * @param newInputButton InputButton to add to binding
	 */
	public void addInputButton(InputButton newInputButton){
		InputButton[] newKeys = new Keys[InputButtons.length + 1];
		for(int i = 0; i < InputButtons.length; i++)
			newKeys[i] = InputButtons[i];
		newKeys[InputButtons.length] = newInputButton;
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
		for(int i = 0; i < InputButtons.length; i++){
			if(InputButtons[i] == oldInputButton)
				bIndex = i;
		}

		// found oldInputButton
		if(bIndex != -1){
			InputButton[] newInputButtons = new InputButton[InputButtons.length - 1];
			for(int i = 0; i < InputButtons.length; i++){
				// skip the InputButton being removed
				if(i == bIndex)
					continue;

				// add old InputButtons, minus the old InputButton
				if(i < bIndex)
					newInputButtons[i] = InputButtons[i];
				else
					newInputButtons[i - 1] = InputButtons[i];
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
		for(InputButton key : InputButtons){
			if(key == other)
				return true;
		}
		return false;
	}

	/**
	 * To string, or not to string -- that is the question
	 * Whether 'tis nobler in the mind to suffer
	 * The slings and arrows of outrageous fortune
	 * Or to take arms against a sea of troubles
	 * And by opposing them. To die, to sleep--
	 * No more
	 * 
	 * Shakespeare while you code!
	 */
	public String toString(){
		String ret = "";
		for(int i = 0; i < InputButtons.length; i++){
			ret += InputButtons[i].toString() + " ";
		}

		return ret;
	}
}