package com.mangateam.mangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MangaSourceFile implements MangaSource {
	
	private String fname;
	

	public MangaSourceFile(String fn) {
		this.fname = fn;
	}
	
	// temp method/ load image for fs
	public Bitmap loadImg(String fileName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap image = BitmapFactory.decodeFile(fileName, options);
		if (image == null)
			throw new NullPointerException("The image can't be decoded.");

		return image;
	}

	@Override
	public Bitmap loadCurrentPage() {
		return loadImg(this.fname);
	}

	@Override
	public Bitmap next() {
		return null;
	}

	@Override
	public Bitmap prev() {
		return null;
	}
}
