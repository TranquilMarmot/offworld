package com.bitwaffle.moguts.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.bitwaffle.moguts.Game;
import com.bitwaffle.moguts.graphics.Animation;
import com.bitwaffle.moguts.graphics.Frame;
import com.bitwaffle.moguts.util.XMLHelper;

/**
 * Manages initializing and loading textures, and keeps
 * a HashMap containing all the texture names mapped to their
 * respective handles
 * 
 * @author TranquilMarmot
 */
public class TextureManager {
	/** Hashes texture names to their int GL handles */
	private HashMap<String, Integer> textures;
	
	/** Keeps track of animations */
	private HashMap<String, Animation> animations;
	
	/**
	 * Create a new texture manager
	 */
	public TextureManager(){
		textures = new HashMap<String, Integer>();
		animations = new HashMap<String, Animation>();
		// TODO this should be done on a per-room (level?) basis
		try {
			parseXML(Game.resources.openAsset("resourcelists/textures.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Bind a texture to GL_TEXTURE_2D
	 * @param textureName Name of texture from XML resource file
	 */
	public void bindTexture(String textureName){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures.get(textureName));
	}
	
	/**
	 * Get the handle for a texture
	 * @param textureName Name of texture from XML resource file
	 * @return Handle of texture (if it exists)
	 */
	public int getTextureHandle(String textureName){
		return textures.get(textureName);
	}
	
	/**
	 * Initialize a texture
	 * 
	 * @param in InputStream from texture's file
	 * @param minFilter What to use for GL_TEXTURE_MIN_FILTER
	 * @param magFilter What to use for GL_TEXTURE_MAG_FILTER
	 * @return Handle for newly created texture
	 */
	private int initTexture(InputStream in, int minFilter, int magFilter){
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		
		int[] handles = new int[1];
		
		GLES20.glGenTextures(1, handles, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e("Render2D", "Error loading texture! " + GLES20.glGetString(error));
		
		bitmap.recycle();
		
		return handles[0];
	}
	
	/**
	 * Parses an XML resource list
	 * @param file InputStream from resource list
	 */
	private void parseXML(InputStream file){
		NodeList nodes = XMLHelper.getNodeList(file);
		
		// grab all the resources
		if (nodes != null && nodes.getLength() > 0) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				// we want to skip anything that's not an element node
				if (n.getNodeType() == Node.ELEMENT_NODE){
					if(n.getNodeName().equals("texture")){
						loadTexture((Element) n);
					} else if(n.getNodeName().equals("animation")){
						loadAnimation((Element) n);
					}
				}
			}
		} else {
			Log.e("XML", "Error parsing XML in TextureManager! Either there was nothing in the given file or the parser simply just didn't want to work");
		}
	}
	
	/**
	 * Load a texture from an XML Element
	 * @param ele Element to parse
	 */
	private void loadTexture(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		
		// TODO make sure this doesn't crash when there's no specified filters
		
		int minFilter = getFilter(ele, "minFilter");
		int magFilter = getFilter(ele, "magFilter");
		
		try {
			InputStream in = Game.resources.openAsset(path);
			textures.put(name, initTexture(in, minFilter, magFilter));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getFilter(Element ele, String filterType){
		int filter = GLES20.GL_NEAREST;
		String minFilterString = XMLHelper.getString(ele, filterType);
		if(minFilterString.equals("GL_LINEAR"))
			filter = GLES20.GL_LINEAR;
		else if(minFilterString.equals("GL_NEAREST"))
			filter = GLES20.GL_NEAREST;
		return filter;
	}
	
	private void loadAnimation(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		int frames = Integer.parseInt(ele.getAttribute("frames"));
		int minFilter = getFilter(ele, "minFilter");
		int magFilter = getFilter(ele, "magFilter");
		
		Animation animation = new Animation(frames);
		
		try{
			Bitmap bitmap = BitmapFactory.decodeStream(Game.resources.openAsset(path));
			
			int[] handles = new int[frames];
			GLES20.glGenTextures(frames, handles, 0);
			
			NodeList frameNodes = ele.getElementsByTagName("frame");
			
			for(int i = 0; i < frameNodes.getLength(); i++){
				Element frameEle = (Element) frameNodes.item(i);
				int index = Integer.parseInt(frameEle.getAttribute("index"));
				int xOffset = XMLHelper.getInt(frameEle, "xOffset");
				int yOffset = XMLHelper.getInt(frameEle, "yOffset");
				int width = XMLHelper.getInt(frameEle, "width");
				int height = XMLHelper.getInt(frameEle, "height");
				float time = XMLHelper.getFloat(frameEle, "time");
				initSubTexture(bitmap, handles[index], minFilter, magFilter, xOffset, yOffset, width, height);
				animation.putFrame(
						new Frame(
								index,
								handles[index],
								time,
								width, 
								height
						));
			}
			
			bitmap.recycle();
		} catch(IOException e){
			e.printStackTrace();
		}
		animations.put(name, animation);
		
	}
	
	public Animation getAnimation(String animationName){
		return animations.get(animationName);
	}
	
	/**
	 * Initialize a sub-texture
	 * 
	 * @param in InputStream from texture's file
	 * @param minFilter What to use for GL_TEXTURE_MIN_FILTER
	 * @param magFilter What to use for GL_TEXTURE_MAG_FILTER
	 * @return Handle for newly created texture
	 */
	private void initSubTexture(Bitmap bitmap, int handle, int minFilter, int magFilter, int xOffset, int yOffset, int width, int height){
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
		
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, xOffset, yOffset, width, height);
		
		System.out.println(handle + " " + GLUtils.getInternalFormat(bitmap) + " " + GLES20.GL_RGBA + " " + GLUtils.getType(bitmap) + " " + pixels.length);
		
		// 4 bytes per int
		ByteBuffer byteB = ByteBuffer.allocateDirect(pixels.length * 4);
		byteB.order(ByteOrder.nativeOrder());
		IntBuffer buff = byteB.asIntBuffer();
		buff.put(pixels);
		buff.rewind();
		
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLUtils.getInternalFormat(bitmap), width, height, 0, GLUtils.getInternalFormat(bitmap), GLUtils.getType(bitmap), buff);
		//GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, xOffset, yOffset, width, height, GLUtils.getInternalFormat(bitmap), GLUtils.getType(bitmap), buff);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e("Render2D", "Error loading texture! " + GLES20.glGetString(error));
	}
	
}
