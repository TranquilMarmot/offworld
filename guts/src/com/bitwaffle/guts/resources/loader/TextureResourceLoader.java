package com.bitwaffle.guts.resources.loader;

import java.nio.Buffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.BufferUtils;
import com.bitwaffle.guts.Game;
import com.bitwaffle.guts.graphics.graphics2d.animation.Animation;
import com.bitwaffle.guts.graphics.graphics2d.animation.AnimationPart;
import com.bitwaffle.guts.graphics.graphics2d.animation.Frame;
import com.bitwaffle.guts.graphics.graphics2d.shapes.quad.SubImage;

/**
 * Handles loading textures and animations
 * 
 * @author TranquilMarmot
 */
public class TextureResourceLoader {
	private static final String LOGTAG = "TextureLoader";
	
	/** @param arr Array of textures to load */
	public static void parseTextureArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject texObj = arr.getJSONObject(i);
				
				String texName = texObj.getString("name");
				String texPath = texObj.getString("path");
				
				int minFilter = getFilter(texObj, "minFilter");
				int magFilter = getFilter(texObj, "magFilter");
				
				boolean wrapU = texObj.optBoolean("wrapU");
				boolean wrapV = texObj.optBoolean("wrapV");
				
				int handle = initTexture(texPath, minFilter, magFilter, wrapU, wrapV).getTextureObjectHandle();
				Game.resources.textures.addTexture(texName, handle);
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing texture array in resource file!");
			e.printStackTrace();
		}
	}
	
	/** @param arr Array of sprite sheet objects to parse */
	public static void parseSpriteSheetArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject sheetObj = arr.getJSONObject(i);
				
				String imagePath = sheetObj.getString("path");
				int minFilter = getFilter(sheetObj, "minFilter");
				int magFilter = getFilter(sheetObj, "magFilter");
				
				Texture sheetTexture = initTexture(imagePath, minFilter, magFilter, false, false);
				
				int sheetHandle = sheetTexture.getTextureObjectHandle();
				
				JSONArray spritesArr = sheetObj.getJSONArray("sprites");
				for(int j = 0; j < spritesArr.length(); j++){
					JSONObject spriteObj = spritesArr.getJSONObject(j);
					
					String spriteName = spriteObj.getString("name");
					int spriteX = spriteObj.getInt("x");
					int spriteY = spriteObj.getInt("y");
					int spriteW = spriteObj.getInt("w");
					int spriteH = spriteObj.getInt("h");
					
					Buffer spriteBuff = getSubImageTexCoords(
							spriteX, 
							spriteY,
							spriteW,
							spriteH,
							sheetTexture.getWidth(),
							sheetTexture.getHeight()
					);
					Game.resources.textures.addSubImage(spriteName, new SubImage(sheetHandle, spriteW, spriteH, spriteBuff));
				}
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing spritesheet array in resource file!");
			e.printStackTrace();
		}
	}
	
	/** @param arr Array of animations to parse */
	public static void parseAnimationArray(JSONArray arr){
		try{
			for(int i = 0; i < arr.length(); i++){
				JSONObject animObj = arr.getJSONObject(i);
				
				String animName = animObj.getString("name");
				
				JSONArray partsArr = animObj.getJSONArray("parts");
				AnimationPart[] parts = new AnimationPart[partsArr.length()];
				for(int j = 0; j < parts.length; j++){
					JSONObject partObj = partsArr.getJSONObject(j);
					
					String textureName = partObj.getString("texture");
					int textureHandle = Game.resources.textures.getTextureHandle(textureName);
					int sourceWidth = partObj.getInt("sourceWidth");
					int sourceHeight = partObj.getInt("sourceHeight");
					float xScale = (float)partObj.getDouble("xScale");
					float yScale = (float)partObj.getDouble("yScale");
					
					JSONArray frameArr = partObj.getJSONArray("frames");
					Frame[] frames = new Frame[frameArr.length()];
					for(int k = 0; k < frameArr.length(); k++){
						JSONObject frameInfoObj = frameArr.getJSONObject(k);
						
						float frameLength = (float)frameInfoObj.getDouble("length");
						
						JSONObject frameObj = frameInfoObj.getJSONObject("frame");
						int frameX = frameObj.getInt("x");
						int frameY = frameObj.getInt("y");
						int frameW = frameObj.getInt("w");
						int frameH = frameObj.getInt("h");
						
						Buffer frameBuff = getSubImageTexCoords(
								frameX,
								frameY,
								frameW,
								frameH,
								sourceWidth,
								sourceHeight
						);
						frames[k] = new Frame(frameLength, textureHandle, frameW, frameH, frameBuff);
					}
					parts[j] = new AnimationPart(frames, xScale, yScale);
				}
				
				Game.resources.textures.addAnimation(animName, new Animation(parts));
			}
		} catch(JSONException e){
			Gdx.app.error(LOGTAG, "Error parsing animation array in resource file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get an OpenGL filter from a JSON object
	 * The filter is optional and defaults to GL_NEAREST
	 * @return GL int representing filter
	 */
	private static int getFilter(JSONObject jobj, String filterType){
		String filterString = jobj.optString(filterType);
		int filter = GL20.GL_NEAREST;
		if(filterString.equalsIgnoreCase("linear"))
			filter = GL20.GL_LINEAR;
		
		return filter;
	}
	
	/**
	 * Initializes a texture with the given parameters and returns its handle
	 * @param path Path to texture file
	 * @param minFilter Min filter to use when loading
	 * @param magFilter Mag filter to use when loading
	 * @param wrapU Whether or not the texture should repeat on the X axis
	 * @param wrapV Whether or not the texture should repeat on the Y axis
	 * @return Texture object (handle can be obtained with getTextureObjectHandle())
	 */
	private static Texture initTexture(String path, int minFilter, int magFilter, boolean wrapU, boolean wrapV){
		Texture texture = new Texture(Game.resources.getFileHandle(path));
		
		TextureFilter min = TextureFilter.Linear, mag = TextureFilter.Linear;
		if(minFilter == GL20.GL_LINEAR)
			min = TextureFilter.Linear;
		else if(minFilter == GL20.GL_NEAREST)
			min = TextureFilter.Nearest;
		
		if(magFilter == GL20.GL_LINEAR)
			mag = TextureFilter.Linear;
		else if(magFilter == GL20.GL_NEAREST)
			mag = TextureFilter.Nearest;
		texture.setFilter(min, mag);
		
		
		TextureWrap 
			uWrap = wrapU ? TextureWrap.Repeat : TextureWrap.ClampToEdge,
			vWrap = wrapV ? TextureWrap.Repeat : TextureWrap.ClampToEdge;
		texture.setWrap(uWrap,vWrap);
		
		return texture;
	}
	
	/**
	 * Gets the texture coordinates for a sub-image
	 * @return FloatBuffer containing texture coordinates to render sub-image with a Quad
	 */
	private static Buffer getSubImageTexCoords(int xOffset, int yOffset, int subImageWidth, int subImageHeight, int sourceWidth, int sourceHeight){
		float texX = (float)xOffset / (float)sourceWidth;
		float texY = (float)yOffset / (float)sourceHeight;
		float texWidth = (float)subImageWidth / (float)sourceWidth;
		float texHeight = (float)subImageHeight / (float)sourceHeight;
		
		// create texture coordinate array and fill a buffer (texture coordinates are used when rendering quad)
		float[] texCoords = {
				texX, texY,
				texX + texWidth, texY,
				texX + texWidth, texY + texHeight,
				
				texX + texWidth, texY + texHeight,
				texX, texY + texHeight,
				texX, texY
		};
		Buffer buff = com.badlogic.gdx.utils.BufferUtils.newByteBuffer(texCoords.length * 4);
		BufferUtils.copy(texCoords, buff, texCoords.length, 0);
		buff.rewind();
		
		return buff;
	}
	

}
