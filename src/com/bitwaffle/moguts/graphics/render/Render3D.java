package com.bitwaffle.moguts.graphics.render;

import java.io.IOException;

//import org.lwjgl.util.vector.Matrix4f;
//import org.lwjgl.util.vector.Vector3f;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.bitwaffle.moguts.graphics.glsl.GLSLProgram;
import com.bitwaffle.moguts.graphics.glsl.GLSLShader;
import com.bitwaffle.moguts.graphics.model.Material;

public class Render3D {
	/** default diffuse color */
	//private static final Vector3f DEFAULT_KD = new Vector3f(0.5f, 0.5f, 0.5f);
	/** default ambient color */
	//private static final Vector3f DEFAULT_KA = new Vector3f(0.5f, 0.5f, 0.5f);
	/** default specular color */
	//private static final Vector3f DEFAULT_KS = new Vector3f(0.8f, 0.8f, 0.8f);
	/** default shiny factor */
	private static final float DEFAULT_SHINY = 50.0f;
	
	private static final String VERTEX_SHADER = "game/shaders/main.vert";
	private static final String FRAGMENT_SHADER = "game/shaders/main.frag";
	
	private int vertexLocationPosition, vertexNormalPosition, vertexTexCoordPosition;
	
	public GLSLProgram program;
	
	//public static Matrix4f projection, modelview;
	
	public static float fov = 45.0f;
	public static float drawDistance = 1000.0f;
	
	@SuppressWarnings("unused")
	private Context context;
	private AssetManager assets;
	
	public Render3D(Context context){
		this.context = context;
		assets = context.getAssets();
		initShaders();
		getPositionsFromShader();
		
		System.out.printf("location: %f normal: %f tex: %f\n", vertexLocationPosition, vertexNormalPosition, vertexTexCoordPosition);
		
		//float aspect = (float)GLRenderer.windowWidth / (float)GLRenderer.windowHeight;
		
		//projection = MatrixHelper.perspective(fov, aspect, 1.0f, drawDistance);
		//modelview = new Matrix4f();
	}
	
    private void initShaders(){
        GLSLShader vert = new GLSLShader(GLSLShader.ShaderTypes.VERTEX);
        GLSLShader frag = new GLSLShader(GLSLShader.ShaderTypes.FRAGMENT);
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
        if(!program.link())
        	System.err.println("Error linking program! " + program.log());
    }
    
    private void getPositionsFromShader(){
    	vertexLocationPosition = GLES20.glGetAttribLocation(getProgramHandle(), "VertexLocation");
    	vertexNormalPosition = GLES20.glGetAttribLocation(getProgramHandle(), "VertexNormal");
    	vertexTexCoordPosition = GLES20.glGetAttribLocation(getProgramHandle(), "VertexTexCoord");
    }
    
    public int getProgramHandle(){
    	return program.getHandle();
    }
    
    private void setUp3DRender(){
    	program.use();
    	//useDefaultMaterial();
    	
    	GLES20.glDisable(GLES20.GL_BLEND);
    	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    	
    	//modelview.setIdentity();
    }
    
    public void renderScene(){
    	setUp3DRender();
    }
    
    public int getVertexLocationPosition(){
    	return vertexLocationPosition;
    }
    
    public int getVertexNormalPosition(){
    	return vertexNormalPosition;
    }
    
    public int getVertexTexCoordPosition(){
    	return vertexTexCoordPosition;
    }
    
	/**
	 * Sets the current material being used for rendering
	 * @param mat Material to use
	 */
	public void setCurrentMaterial(Material mat){
		//program.setUniform("Material.Kd" , mat.getKd());
		//program.setUniform("Material.Ka", mat.getKa());
		//program.setUniform("Material.Ks", mat.getKs());
		program.setUniform("Material.Shininess", mat.getShininess());
	}
	
	/**
	 * Set the current material to the default material
	 */
	public void useDefaultMaterial(){
		//program.setUniform("Material.Kd" , DEFAULT_KD);
		//program.setUniform("Material.Ka", DEFAULT_KA);
		//program.setUniform("Material.Ks", DEFAULT_KS);
		program.setUniform("Material.Shininess", DEFAULT_SHINY);
	}
}
