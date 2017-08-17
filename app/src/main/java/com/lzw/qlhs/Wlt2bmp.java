package com.lzw.qlhs;


public class Wlt2bmp {

	private static final String TAG = "Wlt2bmp";

	public static int picUnpack(byte[] wlt_src,byte[] bmp_dst){

		return kaerunpack(wlt_src,bmp_dst);
	}
	
	// JNI
	private static native int kaerunpack(byte[] src,byte[] dst);
	static {
		System.loadLibrary("wlt2bmp");
		System.loadLibrary("iskaer");
	}
}

