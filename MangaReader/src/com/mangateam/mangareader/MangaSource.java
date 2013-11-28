/**
 * 
 */
package com.mangateam.mangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class MangaSource {
	
	// temp method/ load image for fs
	public Bitmap loadImg(String fileName){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		Bitmap image = BitmapFactory.decodeFile(fileName, options);
		if (image == null) throw new NullPointerException("The image can't be decoded.");
		
		return image;
	}
	
	public Bitmap loadCurrentPage(){
		String fileName = "test.jpg";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		Bitmap image = BitmapFactory.decodeFile(fileName, options);
		if (image == null) throw new NullPointerException("The image can't be decoded.");
		
		return image;
	}
	
	public Bitmap next() {
		return null;
	}
	
	public Bitmap prev() {
		return null;
	}
}
