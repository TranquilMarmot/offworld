package com.bitwaffle.offworld.mguts.graphics.render;

import java.io.IOException;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import android.content.Context;
import android.content.res.AssetManager;

import com.bitwaffle.offworld.mguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.offworld.mguts.graphics.glsl.GLSLShader;
import com.bitwaffle.offworld.mguts.graphics.glsl.ShaderTypes;
import com.bitwaffle.offworld.mguts.util.MatrixHelper;

public class Render3D {
	/** default diffuse color */
	private static final Vector3f DEFAULT_KD = new Vector3f(0.5f, 0.5f, 0.5f);
	/** default ambient color */
	private static final Vector3f DEFAULT_KA = new Vector3f(0.5f, 0.5f, 0.5f);
	/** default specular color */
	private static final Vector3f DEFAULT_KS = new Vector3f(0.8f, 0.8f, 0.8f);
	/** default shiny factor */
	private static final float DEFAULT_SHINY = 50.0f;
	
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";
	
	public GLSLProgram program;
	
	public static Matrix4f projection, modelview;
	
	public static float fov = 45.0f;
	public static float drawDistance = 1000.0f;
	
	private Context context;
	private AssetManager assets;
	
	public Render3D(Context context){
		this.context = context;
		assets = context.getAssets();
		initShaders();
		
		float aspect = (float)GLRenderer.windowWidth / (float)GLRenderer.windowHeight;
		
		projection = MatrixHelper.perspective(fov, aspect, 1.0f, drawDistance);
	}
	
    private void initShaders(){
        GLSLShader vert = new GLSLShader(ShaderTypes.VERTEX);
        GLSLShader frag = new GLSLShader(ShaderTypes.FRAGMENT);
        try {
			if(!vert.compileShaderFromStream(assets.open(VERTEX_SHADER)))
				System.err.println("Error compiling vertex shader! Result: " + vert.log());
			if(!frag.compileShaderFromStream(assets.open(FRAGMENT_SHADER)))
				System.err.println("Error compiling vertex shader! Result: " + frag.log());
		} catch (IOException e) {
			e.printStackTrace();
		}
        program = new GLSLProgram();
        program.addShader(vert);
        program.addShader(frag);
        program.link();
        program.use();
    }
    
    public int getProgramHandle(){
    	return program.getHandle();
    }
}
