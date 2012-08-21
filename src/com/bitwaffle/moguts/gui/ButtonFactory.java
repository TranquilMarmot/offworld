package com.bitwaffle.moguts.gui;

import android.opengl.GLES20;

import com.bitwaffle.moguts.device.TextInput;
import com.bitwaffle.moguts.graphics.Camera;
import com.bitwaffle.moguts.graphics.render.Render2D;
import com.bitwaffle.moguts.gui.button.RectangleButton;
import com.bitwaffle.moguts.serialization.GameSaver;
import com.bitwaffle.moguts.util.PhysicsHelper;
import com.bitwaffle.offworld.Game;
import com.swarmconnect.Swarm;

/**
 * Temporary class for creating buttons
 * 
 * @author TranquilMarmot
 */
public class ButtonFactory {
	// TODO Maybe use XML for GUI stuff like in Spaceout
	
	// FIXME temp alpha values for buttons
	private static final float 
		ACTIVE_ALPHA = 0.3f,
		PRESSED_ALPHA = 0.6f;
			
	
	/**
	 * Make some buttons!
	 * @param gui GUI to add buttons to
	 */
	public static void makeButtons(GUI gui){
		// left movement buttons
		gui.addButton(makeLeftRightButton());
		gui.addButton(makeLeftLeftButton());
		gui.addButton(makeLeftJumpButton());
		
		// right movement buttons
		gui.addButton(makeRightRightButton());
		gui.addButton(makeRightLeftButton());
		gui.addButton(makeRightJumpButton());
		
		// misc buttons
		gui.addButton(makeBoxButton());
		gui.addButton(makeCameraButton());
		gui.addButton(makePauseButton());
	}

	/**
	 * @return Button to move left on the left side of the screen
	 */
	private static RectangleButton makeLeftLeftButton(){
		RectangleButton leftLeftButt = new RectangleButton(50.0f, Game.windowHeight - 50.0f, 50.0f, 50.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goLeft();
				
				this.y = Game.windowHeight - 50.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("leftarrow").draw(renderer.quad, this.width, this.height);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return leftLeftButt;
	}
	
	/**
	 * @return Button to move left on the right side of the screen
	 */
	private static RectangleButton makeRightLeftButton(){
		RectangleButton rightLeftButt = new RectangleButton(Game.windowWidth - 150.0f, Game.windowHeight - 50.0f, 50.0f, 50.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goLeft();
				
				this.y = Game.windowHeight - 50.0f;
				this.x = Game.windowWidth - 150.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("rightarrow").draw(renderer.quad, this.width, this.height, true, false);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return rightLeftButt;
	}
	
	/**
	 * @return Button to move right on the left side of the screen
	 */
	private static RectangleButton makeLeftRightButton(){
		RectangleButton leftRightButt = new RectangleButton(150.0f, Game.windowHeight - 50.0f, 50.0f, 50.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goRight();
				
				this.y = Game.windowHeight - 50.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("rightarrow").draw(renderer.quad, this.width, this.height);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return leftRightButt;
	}
	
	/**
	 * @return Button to move right on the right side of the screen
	 */
	private static RectangleButton makeRightRightButton(){
		RectangleButton rightRightButt = new RectangleButton(Game.windowWidth - 50.0f, Game.windowHeight - 50.0f, 50.0f, 50.0f){
			public void onRelease(){}
			public void onSlideRelease(){}
			
			@Override
			public void update(){
				if(this.isDown())
					Game.player.goRight();
				
				this.x = Game.windowWidth - 50.0f;
				this.y = Game.windowHeight - 50.0f;
			}
			
			@Override
			public void onPress(){
				Game.vibration.vibrate(25);
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("leftarrow").draw(renderer.quad, this.width, this.height, true, false);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return rightRightButt;
	}
	
	/**
	 * @return Button to jump on the right side of the screen
	 */
	private static RectangleButton makeRightJumpButton(){
		RectangleButton rightJumpButt = new RectangleButton(Game.windowWidth - 50.0f, Game.windowHeight - 150.0f, 50.0f, 50.0f){
			protected void onRelease(){ }
			protected void onSlideRelease(){};
			
			@Override
			protected void onPress(){
				Game.player.jump();
			};
			
			@Override
			public void update(){
				this.y = Game.windowHeight - 150.0f;
				this.x = Game.windowWidth - 50.0f;
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("uparrow").draw(renderer.quad, this.width, this.height);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return rightJumpButt;
	}
	
	/**
	 * @return Button to jump on the left side of the screen
	 */
	private static RectangleButton makeLeftJumpButton(){
		RectangleButton leftJumpButt = new RectangleButton(50.0f, Game.windowHeight - 150.0f, 50.0f, 50.0f){
			protected void onRelease(){}
			protected void onSlideRelease(){};
			
			@Override
			protected void onPress(){
				Game.player.jump();
			};
			
			@Override
			public void update(){
				this.y = Game.windowHeight - 150.0f;
			}
			
			@Override
			public void draw(Render2D renderer){
				renderer.program.setUniform("vColor", 1.0f, 1.0f, 1.0f, this.isDown() ? PRESSED_ALPHA : ACTIVE_ALPHA);
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_DST_COLOR);
				Game.resources.textures.getSubImage("uparrow").draw(renderer.quad, this.width, this.height);
				GLES20.glDisable(GLES20.GL_BLEND);
			}
		};
		return leftJumpButt;
	}
	
	/**
	 * @return Button to change camera mode
	 */
	private static RectangleButton makeCameraButton(){
		RectangleButton camButt = new RectangleButton(31.0f, 30.0f, 30.0f, 30.0f){
			@Override
			protected void onRelease(){
				Game.vibration.vibrate(25);
				Camera.Modes mode = Render2D.camera.currentMode();
				if(mode == Camera.Modes.FOLLOW)
					Render2D.camera.setMode(Camera.Modes.FREE);
				else
					Render2D.camera.setMode(Camera.Modes.FOLLOW);
			}
			
			protected void onSlideRelease(){};
			protected void onPress(){};
			public void update(){};
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("camera");
				super.draw(renderer);
			}
		};
		return camButt;
	}
	
	/**
	 * @return Button to make more boxes
	 */
	private static RectangleButton makeBoxButton(){
		RectangleButton boxButt = new RectangleButton(Game.windowWidth - 40.0f, 40.0f, 40.0f, 40.0f){
			@Override
			protected void onRelease(){
				Game.vibration.vibrate(25);
				PhysicsHelper.makeRandomBox(Game.physics);
				Game.resources.sounds.play("test");
			}
			
			protected void onSlideRelease(){};
			protected void onPress(){};
			
			@Override
			public void update(){
				this.x = Game.windowWidth - 40.0f;
				this.active[0] = 0.75f;
				this.active[1] = 0.75f;
				this.active[2] = 0.75f;
			}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("box");
				super.draw(renderer);
			}
		};
		return boxButt;
	}
	
	/**
	 * @return Button to pause the game
	 */
	private static RectangleButton makePauseButton(){
		RectangleButton pauseButt = new RectangleButton(Game.windowWidth / 2, Game.windowHeight - 20, 35.0f, 20.0f){
			// save/load buttons
			private RectangleButton save, load, swarm, debug;
			// whether or not buttons are up
			private boolean buttonsUp = false;
			
			public void update() {
				this.x = Game.windowWidth / 2;
				this.y = Game.windowHeight - 20;
				
				// check if button states match game state
				// (loading a game mucks with Game.paused,
				// so it screws this up because the button isn't pressed to un-pause)
				if(!Game.paused && buttonsUp){
					Render2D.gui.removeButton(save);
					Render2D.gui.removeButton(load);
					Render2D.gui.removeButton(swarm);
					Render2D.gui.removeButton(debug);
					buttonsUp = false;
				} else if(Game.paused && !buttonsUp){
					Render2D.gui.addButton(save);
					Render2D.gui.addButton(load);
					Render2D.gui.addButton(swarm);
					Render2D.gui.addButton(debug);
					buttonsUp = true;
				}
			}

			@Override
			protected void onRelease() {
				Game.paused = !Game.paused;
				
				// initialize buttons if necessary (too lazy to override button's constructor)
				if(save == null)
					save = makeSaveButton();
				if(load == null)
					load = makeLoadButton();
				if(swarm == null)
					swarm = makeSwarmButton();
				if(debug == null)
					debug = makeDebugButton();
				
				// add/remove buttons based on Game's running state
				if(Game.paused){
					Render2D.gui.addButton(save);
					Render2D.gui.addButton(load);
					Render2D.gui.addButton(swarm);
					Render2D.gui.addButton(debug);
					buttonsUp = true;
				} else {
					Render2D.gui.removeButton(save);
					Render2D.gui.removeButton(load);
					Render2D.gui.removeButton(swarm);
					Render2D.gui.removeButton(debug);
					buttonsUp = false;
				}
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("blank");
				super.draw(renderer);
				
				// draw a string if the game is paused
				if(Game.paused){
					String pauseString = "Hello. This is a message to let you know that\nthe game is paused. Have a nice day.";
					float scale = 0.3f;
					float stringWidth = Game.resources.font.stringWidth(pauseString, scale);
					float stringHeight = Game.resources.font.stringHeight(pauseString, scale);
					float textX = ((float)Game.windowWidth / 2.0f) - (stringWidth / 2.0f);
					float textY = ((float)Game.windowHeight / 2.0f) - (stringHeight / 2.0f);
					Game.resources.font.drawString(pauseString, renderer, textX, textY, scale);
				}
			}
		};
		return pauseButt;
	}
	
	/**
	 * @return Button to save the game
	 */
	private static RectangleButton makeSaveButton(){
		RectangleButton saveButt = new RectangleButton(Game.windowWidth / 2 - 100.0f, Game.windowHeight / 2 + 150.0f, 70.0f, 40.0f){

			@Override
			public void update() {
				this.x = Game.windowWidth / 2 - 200.0f;
				this.y = Game.windowHeight / 2 + 150.0f;
			}

			@Override
			protected void onRelease() {
				// ask the user where to save the file to
				final GameSaver saver = new GameSaver();
				TextInput input = new TextInput("Save Game", "Enter save name"){
					@Override
					public void parseInput(String input) {
						saver.saveGame(input + ".ofw", Game.physics);
					}
				};
				input.askForInput();
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("blank");
				super.draw(renderer);
				Game.resources.font.drawString("Save", renderer, x, y + 17.0f, 0.3f);
			}
		};
		return saveButt;
	}
	
	/**
	 * @return Button to load a saved game
	 */
	private static RectangleButton makeLoadButton(){
		RectangleButton loadButt = new RectangleButton(Game.windowWidth / 2 + 100.0f, Game.windowHeight / 2 + 150.0f, 70.0f, 40.0f){

			@Override
			public void update() {
				this.x = Game.windowWidth / 2 + 0.0f;
				this.y = Game.windowHeight / 2 + 150.0f;
			}

			@Override
			protected void onRelease() {
				// ask the user which file to load
				final GameSaver saver = new GameSaver();
				TextInput input = new TextInput("Load Game", "Enter save to load"){
					@Override
					public void parseInput(String input) {
						saver.loadGame(input + ".ofw", Game.physics);
					}
				};
				input.askForInput();
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("blank");
				super.draw(renderer);
				Game.resources.font.drawString("Load", renderer, x, y + 17.0f, 0.3f);
			}
		};
		return loadButt;
	}
	
	/**
	 * @return Button to open swarm
	 */
	private static RectangleButton makeSwarmButton(){
		RectangleButton swarmButt = new RectangleButton(Game.windowWidth / 2 + 100.0f, Game.windowHeight / 2 + 150.0f, 70.0f, 40.0f){

			@Override
			public void update() {
				this.x = Game.windowWidth / 2 + 200.0f;
				this.y = Game.windowHeight / 2 + 150.0f;
			}

			@Override
			protected void onRelease() {
				Swarm.showDashboard();
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("blank");
				super.draw(renderer);
				Game.resources.font.drawString("Swarm", renderer, x, y + 17.0f, 0.3f);
			}
		};
		return swarmButt;
	}
	
	private static RectangleButton makeDebugButton(){
		RectangleButton debugButt = new RectangleButton(Game.windowWidth / 2 + 100.0f, Game.windowHeight / 2 + 150.0f, 70.0f, 40.0f){

			@Override
			public void update() {
				this.x = Game.windowWidth / 2 + 200.0f;
				this.y = Game.windowHeight / 2 - 250.0f;
			}

			@Override
			protected void onRelease() {
				Render2D.drawDebug = !Render2D.drawDebug;
			}

			@Override
			protected void onSlideRelease() {}

			@Override
			protected void onPress() {}
			
			@Override
			public void draw(Render2D renderer){
				Game.resources.textures.bindTexture("blank");
				super.draw(renderer);
				Game.resources.font.drawString("Debug", renderer, x, y + 17.0f, 0.3f);
			}
		};
		return debugButt;
	}
}
