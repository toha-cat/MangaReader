/**
 * 
 */
package com.mangateam.mangareader;

import android.graphics.Bitmap;

/**
 *
 */
public class PageImages {
	private MangaSource mangaSource;
	private Bitmap currentPageBitmap;
	
	public PageImages(MangaSource mSource) {
		this.mangaSource = mSource;
	}
	
	
	
	/**
	 * Следующая сцена, если сцены на странице закончились 
	 * запрашиваем следующую страницу из источника
	 * @return Bitmap сцены, либо null если изображение не доступно
	 */
	public Bitmap next(){
		
		return null;
	}
	
	/**
	 * Предыдущая сцена, если сцены на странице закончились 
	 * запрашиваем предыдующую страницу из источника
	 * @return Bitmap сцены, либо null если изображение не доступно
	 */
	public Bitmap prev(){
		
		return null;
	}
}
