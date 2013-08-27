package com.bitwaffle.guts.gui.elements.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.Renderer;
import com.bitwaffle.guts.graphics.graphics2d.ObjectRenderer2D;
import com.bitwaffle.guts.graphics.graphics2d.font.BitmapFont;

public class ConsoleRenderer implements ObjectRenderer2D {

	/**
	 * Draws the console
	 * 
	 * @param renderer
	 *             What to render the console with
	 * @param flipHorizontal Inherited from GUIObject (ignored)
	 * @param flipVertical Inherited from GUIObject (ignored)
	 */
	public void render(Renderer renderer, Object ent) {
		Console con = (Console) ent;
		if(con.isVisible()){
			// where to draw the console (x stays at its given location)
			//float drawY = (Game.windowHeight) + y;
			//float drawX = (BitmapFont.FONT_GLYPH_WIDTH  * scale) + x + 2.5f;

			Gdx.gl20.glEnable(GL20.GL_BLEND);
			
			if(con.consoleAlpha() > 0.0f)
				drawBackgroundBox(renderer, con);
			
			if (con.consoleOn()){
				drawInputBox(renderer, con);
				renderInput(renderer, con);
			}
			
			if(con.textAlpha() > 0.0f)
				renderScrollback(renderer, con);
			
			Gdx.gl20.glDisable(GL20.GL_BLEND);	
		}
	}
	
	/**
	 * Renders the scrollback text
	 * @param renderer
	 *             Renderer to use to render text
	 * @param drawY
	 *             Y location to draw everything at
	 */
	private void renderScrollback(Renderer renderer, Console con){
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// figure out how many lines to print out
		int stringsToPrint = con.text().size() - (con.numRows() + 1);
		// avoid any possibility of out of bounds (the for loop is kind of
		// weird. if stringsToPrint == -1, then it doesn't print out any
		// lines)
		if (stringsToPrint < 0)
			stringsToPrint = -1;
		
		float[] textColor = new float[]{0.0f, 1.0f, 0.0f, con.textAlpha()};

		// print out however many strings, going backwards
		// (we want the most recent strings to be printed first)
		for (int i = (con.text().size() - 1) - con.currentScroll();
		         i > stringsToPrint - con.currentScroll() && i >= 0;
		         i--){

			// which line we're at in the console itself
			int line = con.text().size() - (i + con.currentScroll());

			// draw the string, going up on the y axis by how tall each line is
			renderer.r2D.font.drawString(con.text().get(i), renderer, con.x, con.y - ((BitmapFont.FONT_GLYPH_HEIGHT * 2.0f) * con.scale() * line), con.scale(), textColor);
		}
	}
	
	/**
	 * Renders the input text, adding a '>' and a '_' is blink is true
	 * @param renderer
	 *             Renderer to draw text with
	 * @param drawX
	 *             X location to draw text at
	 * @param drawY
	 *             Y location to draw text at
	 */
	private void renderInput(Renderer renderer, Console con){
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		// draw the blinking cursor
		String toPrint = "> " + con.input();
		if (con.blink())
			toPrint += "_";
		renderer.r2D.font.drawString(toPrint, renderer, con.x , con.y , con.scale(), new float[]{0.0f, 1.0f, 0.0f, con.textAlpha()});
	}
	
	/**
	 * Draws a box behind the input text
	 * @param renderer
	 *             Renderer to draw box with
	 */
	private void drawInputBox(Renderer renderer, Console con){
		Game.resources.textures.bindTexture("blank");
		
		float[] color = { 0.0f, 0.0f, 0.0f, con.inputBoxAlpha() };
		renderer.r2D.setColor(color);
		
		float boxWidth = (con.numCols() * BitmapFont.FONT_GLYPH_WIDTH * con.scale()) * 2.0f;
		float boxHeight = (BitmapFont.FONT_GLYPH_HEIGHT * con.scale()) * 2.0f;
		float boxX = con.x;
		// add one height-sized row to location to offset for input row
		float boxY = con.y - (boxHeight / 2.0f) /*(Game.windowHeight - (boxHeight * scale))*/;
		
		// translate and scale modelview to be behind text
		renderer.modelview.idt();
		renderer.modelview.translate(boxX, boxY, 0.0f);
		renderer.modelview.scale(con.scale(), con.scale(), 1.0f);
		renderer.r2D.sendMatrixToShader();
		
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.r2D.quad.render(boxWidth, boxHeight);
	}
	
	/**
	 * Draws a box behind all the text
	 * @param renderer
	 *             Renderer to draw box with
	 */
	private void drawBackgroundBox(Renderer renderer, Console con){
		Game.resources.textures.bindTexture("blank");
		
		float[] color = { 0.0f, 0.0f, 0.0f, con.consoleAlpha() };
		renderer.r2D.setColor(color);
		
		float boxWidth = (con.numCols() * BitmapFont.FONT_GLYPH_WIDTH * con.scale()) * 2.0f;
		float boxHeight = (con.numRows() * BitmapFont.FONT_GLYPH_HEIGHT * con.scale()) * 2.0f;
		float boxX = con.x;
		// add one height-sized row to location to offset for input row
		float boxY = con.y - (boxHeight / 2.0f) - ((BitmapFont.FONT_GLYPH_HEIGHT * con.scale()) * 2);
		
		// translate and scale modelview to be behind text
		renderer.modelview.idt();
		renderer.modelview.translate(boxX, boxY, 0.0f);
		renderer.modelview.scale(con.scale(), con.scale(), 1.0f);
		renderer.r2D.sendMatrixToShader();
		
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.r2D.quad.render(boxWidth, boxHeight);
	}

}
