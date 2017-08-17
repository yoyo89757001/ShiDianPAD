package com.kaeridcard.tools;

import android.graphics.Bitmap;


public class Tool {
	
	public static int convertByteToInt(byte data){
		int heightBit = (int) ((data>>4) & 0x0F);
		int lowBit = (int) (0x0F & data);
		return heightBit * 16 + lowBit;
	}


	// """"""""RGB""""""""""""""ת""""""""int""""""""""""""""
	public static int[] convertByteToColor(byte[] data,int width, int height){
		int size = data.length;
		if (size == 0){
			return null;
		}
		//if ((size % 3) != 0)
		//	return null;
		// һ""""""""""""""data""""""""ĳ""""""Ӧ""""""""3""ı""""""""""""""""""""""""""""ݣ""""""""""""RGB""""""""ú""ɫ0XFF000000""""""
		int []color = new int[size / 3];
		int red, green, blue;

		{
			/*
			for(int i = 0; i < color.length; ++i){
				red = convertByteToInt(data[i * 3]);
				green = convertByteToInt(data[i * 3 + 1]);
				blue = convertByteToInt(data[i * 3 + 2]); 

				// """"ȡRGB""""""""ֵͨ""""λ""""""""""int""""""""""""ֵ
				color[color.length-i-1] = (red << 16) | (green << 8) | blue | 0xFF000000; 
			}*/
			for (int h = 0;h < height;h ++){
				int line_ptr = h*width;
				for (int w = 0;w < width;w ++){
					int point_ptr = (line_ptr + w)*3;
					red = convertByteToInt(data[point_ptr]);
					green = convertByteToInt(data[point_ptr + 1]);
					blue = convertByteToInt(data[point_ptr + 2]);

					color[(height-h-1)*width + w] = (red << 16) | (green << 8) | blue | 0xFF000000; 
				}
			}
		}
		return color;
	}

	public static Bitmap createRgbBitmap(byte[] data, int width, int height){ 
		int []colors = convertByteToColor(data,width, height);
		if (colors == null){
			return null;
		}

		Bitmap bmp = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.ARGB_8888);      
		return bmp;
	}
	
}
