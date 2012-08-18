package com.bitwaffle.moguts.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Utility class for creating directly-allocated buffers
 * for sending datas to OpenGL
 * 
 * @author TranquilMarmot
 */
public class BufferUtils {
	/**
	 * Directly allocate a new byte buffer with native order bytes
	 * @param size Number of bytes to allocate
	 * @return Newly allocated ByteBuffer
	 */
	public static ByteBuffer getByteBuffer(int size){
		ByteBuffer b = ByteBuffer.allocateDirect(size);
		b.order(ByteOrder.nativeOrder());
		return b;
	}
	
	/**
	 * Directly allocate a new float buffer with native order bytes
	 * @param size Number of floats to allocate
	 * @return Newly allocated FloatBuffer
	 */
	public static FloatBuffer getFloatBuffer(int size){
		ByteBuffer b = getByteBuffer(size * 4);
		return b.asFloatBuffer();
	}
	
	/**
	 * Directly allocate a new intt buffer with native order bytes
	 * @param size Number of ints to allocate
	 * @return Newly allocated IntBuffer
	 */
	public static IntBuffer getIntBuffer(int size){
		ByteBuffer b = getByteBuffer(size * 4);
		return b.asIntBuffer();
	}
	
	/**
	 * Deep-copies one array to another array
	 * @param src Source array
	 * @param dst Destination array
	 */
	public static void deepCopyFloatArray(float[] src, float[]dst){
		for(int i = 0; i < src.length; i++){
			dst[i] = src[i];
		}
	}
}
