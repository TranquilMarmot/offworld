package com.bitwaffle.guts.android.gui.states.movement.buttons.right;

import com.badlogic.gdx.graphics.Color;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.android.gui.states.movement.MovementGUIState;
import com.bitwaffle.guts.gui.elements.button.rectangle.RectangleButtonRenderer;

/**
 * Button to move right that stays on the left side of the screen
 */
public class RightMoveRightButton extends MoveRightButton{
	public RightMoveRightButton() {
		super(  new RectangleButtonRenderer("leftarrow", true, new Color(0.5f, 0.5f, 0.5f, 0.75f), new Color(0.5f, 0.5f, 0.5f, 1.0f)),
				Game.windowWidth - MovementGUIState.buttonWidth,
				Game.windowHeight - MovementGUIState.buttonHeight, 
				MovementGUIState.buttonWidth,
				MovementGUIState.buttonHeight);
		((RectangleButtonRenderer)this.renderer).flipHorizontal = true;
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
		
		this.x = Game.windowWidth - MovementGUIState.buttonWidth;
		this.y = Game.windowHeight - MovementGUIState.buttonHeight;
	}
	
	/*
	@Override
	public void render(Renderer renderer, boolean flipHorizontal, boolean flipVertical){
		renderer.r2D.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? MovementGUIState.pressedAlpha : MovementGUIState.activeAlpha);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		Game.resources.textures.getSubImage("leftarrow").render(renderer, this.width, this.height, true, false);
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	*/

	@Override
	protected void onSelect() {
	}

	@Override
	protected void onUnselect() {
	}
}
