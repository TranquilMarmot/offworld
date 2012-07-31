package com.bitwaffle.moguts.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	public static ByteBuffer getByteBuffer(int size){
		ByteBuffer b = ByteBuffer.allocateDirect(size);
		b.order(ByteOrder.nativeOrder());
		return b;
	}
	
	public static FloatBuffer getFloatBuffer(int size){
		ByteBuffer b = getByteBuffer(size);
		return b.asFloatBuffer();
	}
	
	public static IntBuffer getIntBuffer(int size){
		ByteBuffer b = getByteBuffer(size);
		return b.asIntBuffer();
	}
}
