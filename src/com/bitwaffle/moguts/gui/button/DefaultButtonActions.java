package com.bitwaffle.moguts.gui.button;

import com.bitwaffle.moguts.graphics.render.Render2D;

public enum DefaultButtonActions {
	L_MOVELEFT(new LeftMoveLeft()),
	R_MOVELEFT(new RightMoveLeft()),
	L_MOVERIGHT(new RightMoveRight()),
	R_MOVERIGHT(new RightMoveRight());
	
	private ButtonAction action;
	private DefaultButtonActions(ButtonAction action){ this.action = action; }
	public ButtonAction getAction(){ return action; }
}

abstract class MoveLeft implements ButtonAction{
	public void onPress() {
		
	}

	public void update(){}
	public abstract void render(Render2D renderer);
	public void onRelease() {}
	public void onSlideRelease() {}
}

class LeftMoveLeft extends MoveLeft{
	public void render(Render2D renderer){
		
	}
}

class RightMoveLeft extends MoveLeft{
	public void render(Render2D renderer){
		
	}
}

abstract class MoveRight implements ButtonAction{
	public void onPress() {
		
	}

	public void update(){}
	public abstract void render(Render2D renderer);
	public void onRelease() {}
	public void onSlideRelease() {}
}

class LeftMoveRight extends MoveRight{
	public void render(Render2D renderer){
		
	}
}

class RightMoveRight extends MoveRight{
	public void render(Render2D renderer){
		
	}
}