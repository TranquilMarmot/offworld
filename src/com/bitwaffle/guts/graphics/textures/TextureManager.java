package com.bitwaffle.guts.graphics.textures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.bitwaffle.guts.graphics.textures.animation.Animation;
import com.bitwaffle.guts.graphics.textures.animation.Frame;
import com.bitwaffle.guts.util.BufferUtils;
import com.bitwaffle.guts.util.XMLHelper;
import com.bitwaffle.offworld.Game;

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
	
	private HashMap<String, SubImage> subImages;
	
	/** Keeps track of animations */
	private HashMap<String, Animation> animations;
	
	/**
	 * Create a new texture manager
	 */
	public TextureManager(){
		textures = new HashMap<String, Integer>();
		animations = new HashMap<String, Animation>();
		subImages = new HashMap<String, SubImage>();
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
	 * @param subImage SubImage to get
	 * @return SubImage, for to render with
	 */
	public SubImage getSubImage(String subImage){
		return subImages.get(subImage);
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
	 * Get an instance of an animation
	 * Whatever class is handling the animation needs
	 * to update and bind its current frame on draw.
	 * This way, multiple instance of the same animation
	 * can be going and be on different frames.
	 * @param animationName Name of animation (from XML resource file)
	 * @return Instance of animation
	 */
	public Animation getAnimation(String animationName){
		return new Animation(animations.get(animationName));
	}
	
	/* ---- From here on down, it's all XML loading stuff ---- */
	
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
					if(n.getNodeName().equals("texture"))
						loadTexture((Element) n);
					else if(n.getNodeName().equals("animation"))
						loadAnimation((Element) n);
				}
			}
		} else {
			Log.e("XML", "Error parsing XML in TextureManager! Either there was nothing in the given file or the parser simply just didn't want to work");
		}
	}
	
	/**
	 * Get a GL filter mode from an XML element
	 * @param ele Element to get filter from
	 * @param filterType "magFilter" or "minFilter"
	 * @return Filter from element from filterType
	 */
	private int getFilter(Element ele, String filterType){
		int filter = GLES20.GL_NEAREST;
		String filterString = XMLHelper.getString(ele, filterType);
		if(filterString == null)
			return GLES20.GL_LINEAR;
		else if(filterString.equals("GL_LINEAR"))
			filter = GLES20.GL_LINEAR;
		else if(filterString.equals("GL_NEAREST"))
			filter = GLES20.GL_NEAREST;
		return filter;
	}
	
	/**
	 * Gets the texture coordinates for a sub-image
	 * @param xOffset Xoffset into source image in pixels
	 * @param yOffset Y offset into source image in pixels
	 * @param subImageWidth Width of sub-image
	 * @param subImageHeight Height of sub-image
	 * @param sourceWidth Width of source image
	 * @param sourceHeight Height of source image
	 * @return FloatBuffer containing texture coordinates to render sub-image with a Quad
	 */
	private FloatBuffer getSubImageTexCoords(float xOffset, float yOffset, float subImageWidth, float subImageHeight, float sourceWidth, float sourceHeight){
		float texX = xOffset / sourceWidth;
		float texY = yOffset / sourceHeight;
		float texWidth = subImageWidth / sourceWidth;
		float texHeight = subImageHeight / sourceHeight;
		
		// create texture coordinate array and fill a buffer (texture coordinates are used when rendering quad)
		float[] texCoords = {
				texX, texY,
				texX + texWidth, texY,
				texX + texWidth, texY + texHeight,
				
				texX + texWidth, texY + texHeight,
				texX, texY + texHeight,
				texX, texY
		};
		FloatBuffer buff = BufferUtils.getFloatBuffer(texCoords.length);
		buff.put(texCoords);
		buff.rewind();
		
		return buff;
	}
	
	/**
	 * Load a texture from an XML Element
	 * @param ele Element to parse
	 */
	private void loadTexture(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		
		int minFilter = getFilter(ele, "minFilter");
		int magFilter = getFilter(ele, "magFilter");

		try {
			InputStream in = Game.resources.openAsset(path);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			int handle = initTexture(bitmap, minFilter, magFilter);
			textures.put(name, handle);
			
			NodeList subImageNodes = ele.getElementsByTagName("subImage");
			for(int i = 0; i < subImageNodes.getLength(); i++){
				Element subimg = (Element) subImageNodes.item(i);
				String subImageName = subimg.getAttribute("name");
				float xOffset = XMLHelper.getFloat(subimg, "xOffset");
				float yOffset = XMLHelper.getFloat(subimg, "yOffset");
				float subImageWidth = XMLHelper.getFloat(subimg, "width");
				float subImageHeight = XMLHelper.getFloat(subimg, "height");

				subImages.put(
						subImageName, 
						new SubImage(
								handle,
								getSubImageTexCoords(
									xOffset,
									yOffset,
									subImageWidth,
									subImageHeight,
									(float)bitmap.getWidth(),
									(float)bitmap.getHeight()
								)
						)
				);
			}
			
			bitmap.recycle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize a texture
	 * @param in InputStream from texture's file
	 * @param minFilter What to use for GL_TEXTURE_MIN_FILTER
	 * @param magFilter What to use for GL_TEXTURE_MAG_FILTER
	 * @return Handle for newly created texture
	 */
	public static int initTexture(InputStream in, int minFilter, int magFilter){
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		
		int handle = initTexture(bitmap, minFilter, magFilter);
		
		bitmap.recycle();
		
		return handle;
	}
	
	/**
	 * Initialize a texture
	 * @param bitmap Bitmap to send to OpenGL
	 * @param minFilter What to use for GL_TEXTURE_MIN_FILTER
	 * @param magFilter What to use for GL_TEXTURE_MAG_FILTER
	 * @return Handle for newly created texture
	 */
	public static int initTexture(Bitmap bitmap, int minFilter, int magFilter){
		int[] handles = new int[1];
		
		GLES20.glGenTextures(1, handles, 0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
		
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, magFilter);
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		
		int error = GLES20.glGetError();
		if(error != GLES20.GL_NO_ERROR)
			Log.e("Render2D", "Error loading texture! (error number " + error + " string: "+ GLES20.glGetString(error) + ")");
		
		return handles[0];
	}
	
	/**
	 * Load an animation from an XML element
	 * @param ele Element to load animation from
	 */
	private void loadAnimation(Element ele){
		String name = ele.getAttribute("name");
		String path = XMLHelper.getString(ele, "path");
		int numFrames = Integer.parseInt(ele.getAttribute("frames"));
		int minFilter = getFilter(ele, "minFilter");
		int magFilter = getFilter(ele, "magFilter");
		
		Frame[] frames = new Frame[numFrames];
		
		try{
			InputStream in = Game.resources.openAsset(path);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			NodeList frameNodes = ele.getElementsByTagName("frame");
			
			// grab data for each frame
			for(int i = 0; i < frameNodes.getLength(); i++){
				Element frameEle = (Element) frameNodes.item(i);
				int index = Integer.parseInt(frameEle.getAttribute("number"));
				int xOffset = XMLHelper.getInt(frameEle, "xOffset");
				int yOffset = XMLHelper.getInt(frameEle, "yOffset");
				int width = XMLHelper.getInt(frameEle, "width");
				int height = XMLHelper.getInt(frameEle, "height");
				float length = XMLHelper.getFloat(frameEle, "length");
				
				frames[index] = initFrame(length, bitmap, xOffset, yOffset, width, height);
			}
			
			// intialize the sheet's texture image
			int sheetHandle = initTexture(bitmap, minFilter, magFilter);
			animations.put(name, new Animation(sheetHandle, frames));
			bitmap.recycle();
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initialize a frame of an animation
	 * @param length How long to stay on the frame
	 * @param source Bitmap that contains the frame
	 * @param xOffset Row of top left pixel in frmae
	 * @param yOffset Column of top left pixel in frame
	 * @param width Number of pixels wide the frame is
	 * @param height Number of pixels tall the frame is
	 * @return Initialized frame
	 */
	private Frame initFrame(float length, Bitmap source, int xOffset, int yOffset, int width, int height){
		FloatBuffer buff = getSubImageTexCoords(
				(float)xOffset,
				(float)yOffset,
				(float)width,
				(float)height,
				(float)source.getWidth(),
				(float)source.getHeight()
		);
		
		return new Frame(length, buff);
	}
}
