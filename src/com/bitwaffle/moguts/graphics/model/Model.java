package com.bitwaffle.moguts.graphics.model;

import java.util.ArrayList;

import android.opengl.GLES20;

/**
 * This class defines a 3D model. A model is a set of vertices to draw, what texture to use when drawing them,
 * and a collision shape to use with JBullet
 * @author TranquilMarmot
 *
 */
public class Model {
	/** Handle corresponding to the model's vertex array */
	//private int vaoHandle;
	
	
	private int vertexLocationHandle, vertexNormalHandle, vertexTexCoordHandle;
	
	/** Collision shape used for physics sim */
	//private CollisionShape collisionShape;
	/** Texture to use for rendering */
	//private Texture texture;
	/** All the parts of the model (each has a different material) */
	private ArrayList<ModelPart> parts;
	
	/**
	 * Create a model
	 * @param collisionShape Collision shape to use for the model
	 * @param vaoHandle VAO Handle for drawing model
	 * @param parts Parts of the model
	 * @param texture Texture that the model uses
	 */
	public Model(int vertexLocationHandle, int vertexNormalHandle, int vertexTexCoordHandle, ArrayList<ModelPart> parts/*TODO, Textures texture*/){
		// these all come from the model loader
		this.vertexLocationHandle = vertexLocationHandle;
		this.vertexNormalHandle = vertexNormalHandle;
		this.vertexTexCoordHandle = vertexTexCoordHandle;
		//this.collisionShape = collisionShape;
		//this.texture = texture;
		this.parts = parts;
	}
	
	/**
	 * @return Collision shape for this model
	 */
	/*
	public CollisionShape getCollisionShape(){
		return collisionShape;
	}
	*/
	
	/**
	 * Renders the model.
	 * To render the model, we bind its vertex array and then draw all of its parts,
	 * after setting the right material for rendering
	 */
	public void render(){
		//GLES20.glBindVertexArray(vaoHandle);
		
		GLES20.glEnableVertexAttribArray(vertexLocationHandle);
		GLES20.glEnableVertexAttribArray(vertexNormalHandle);
		GLES20.glEnableVertexAttribArray(vertexTexCoordHandle);
		
		// loop through each part, set its material and draw
		for(ModelPart p : parts){
			//Game.render3D.setCurrentMaterial(p.getMaterial());
			p.draw();
		}
		
		GLES20.glDisableVertexAttribArray(vertexLocationHandle);
		GLES20.glDisableVertexAttribArray(vertexNormalHandle);
		GLES20.glDisableVertexAttribArray(vertexTexCoordHandle);
		
		//GLES20.glBindVertexArray(0);
	}

	/**
	 * @return Texture to use to render model
	 */
	/*
	public Textures getTexture() {
		return texture;
	}
	*/	
}
