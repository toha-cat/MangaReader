/**
 * 
 */
package com.mangateam.mangareader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public interface MangaSource {

	Bitmap loadCurrentPage();

	Bitmap next();

	Bitmap prev();
	
	void setCurPage(int p);
	
	int getCurPage();
	
	String getSourceName();
}
