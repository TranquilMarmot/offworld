package com.bitwaffle.guts.graphics.model;

import java.nio.Buffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.render.Render3D;

/**
 * This class defines a 3D model. A model is a set of vertices to draw, what texture to use when drawing them,
 * and a collision shape to use with JBullet
 * @author TranquilMarmot
 *
 */
public class Model {
	/** Handle corresponding to the model's vertex array */
	//private int vaoHandle;
	/** Collision shape used for physics sim */
	//private CollisionShape collisionShape;
	/** Texture to use for rendering */
	private String texture;
	/** All the parts of the model (each has a different material) */
	private ArrayList<ModelPart> parts;
	
	private Buffer coordBuffer, texBuffer, normBuffer;
	
	/** Handles for where to send info when drawing */
	private Integer positionHandle = -1, texCoordHandle = -1, normHandle = -1;
	
	/** Info on coordinates */
	private static final int COORDS_PER_VERTEX = 3, COORDS_PER_TEXCOORD = 2;
	
	/**
	 * Create a model
	 * @param collisionShape Collision shape to use for the model
	 * @param vaoHandle VAO Handle for drawing model
	 * @param parts Parts of the model
	 * @param texture Texture that the model uses
	 */
	public Model(/*CollisionShape collisionShape, int vaoHandle,*/
			Buffer coordBuffer, Buffer texBuffer, Buffer normBuffer,
			ArrayList<ModelPart> parts, String texture){
		// these all come from the model loader
		//this.vaoHandle = vaoHandle;
		//this.collisionShape = collisionShape;
		this.coordBuffer = coordBuffer;
		this.texBuffer = texBuffer;
		this.normBuffer = normBuffer;
		this.texture = texture;
		this.parts = parts;
	}
	
	/**
	 * @return Collision shape for this model
	 */
	/*public CollisionShape getCollisionShape(){
		return collisionShape;
	}*/
	
	/**
	 * Renders the model.
	 * To render the model, we bind its vertex array and then draw all of its parts,
	 * after setting the right material for rendering
	 */
	public void render(Render3D renderer){
		// grab handles if we don't have them
		if(positionHandle == -1)
			positionHandle = renderer.program.getAttribLocation("VertexPosition");
		if(texCoordHandle == -1)
			texCoordHandle = renderer.program.getAttribLocation("VertexTexCoord");
		if(normHandle == -1)
			normHandle = renderer.program.getAttribLocation("VertexNormal");
		
		Gdx.gl20.glEnableVertexAttribArray(positionHandle);
		Gdx.gl20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, coordBuffer);
		
        Gdx.gl20.glEnableVertexAttribArray(texCoordHandle);
        Gdx.gl20.glVertexAttribPointer(texCoordHandle, COORDS_PER_TEXCOORD, GL20.GL_FLOAT, false, 0, texBuffer);
        
        Gdx.gl20.glEnableVertexAttribArray(normHandle);
        Gdx.gl20.glVertexAttribPointer(normHandle, COORDS_PER_VERTEX, GL20.GL_FLOAT, false, 0, normBuffer);
		
		Game.resources.textures.bindTexture(texture);
		
		for(ModelPart p : parts){
			renderer.setCurrentMaterial(p.getMaterial());
			p.draw();
		}
		
		Gdx.gl20.glDisableVertexAttribArray(positionHandle);
        Gdx.gl20.glDisableVertexAttribArray(texCoordHandle);
        Gdx.gl20.glDisableVertexAttribArray(normHandle);
	}

	/**
	 * @return Texture to use to render model
	 */
	public String getTexture() {
		return texture;
	}	
}
