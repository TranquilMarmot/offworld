package com.bitwaffle.guts.graphics.shapes.model;


/**
 * Describes a material to use with the Phong reflection model
 * @author TranquilMarmot
 *
 */
public class Material {
	/** Ambient, diffuse, and specular colors (all between 0.0 and 1.0) */
	private float[] Ka, Kd, Ks;
	/** Shininess of the material */
	private float Shininess;
	
	/**
	 * @param Ka Ambient color
	 * @param Kd Diffuse color
	 * @param Ks Specular color
	 * @param Shininess How shiny material is 
	 */
	public Material(float[] Ka, float[] Kd, float[] Ks, float Shininess){
		this.Ka = Ka;
		this.Kd= Kd;
		this.Ks = Ks;
		this.Shininess = Shininess;
	}
	
	public float[] ka(){ return Ka; }
	public float[] ks(){ return Ks; }
	public float[] kd(){ return Kd; }
	public float shininess(){ return Shininess; }
}
