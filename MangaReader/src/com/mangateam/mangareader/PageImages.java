/**
 * 
 */
package com.mangateam.mangareader;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.util.Log;

/**
 *
 */
public class PageImages {
	private MangaSource mangaSource;
	private Bitmap currentPageBitmap;
	private ArrayList<Bitmap> listScene = new ArrayList<Bitmap>();
	private int currentIndex = 0;
	
	final int MIN_LEN_SCENA = 30;

	public PageImages(MangaSource mSource) {
		this.mangaSource = mSource;
		this.currentPageBitmap = this.mangaSource.loadCurrentPage();
		this.cutPageToScene();
		//this.listScene.add(this.currentPageBitmap);
	}

	
	/**
	 * проверка пиксела на цвет
	 * @return true - белый, или около того, false - иначе
	 */
	private boolean isWhite(int pixel) {
		if((pixel & 0xE0E0E0) == 0xE0E0E0){ // почти магия :)
											// значение каждого цвета от E0 до FF
			return true;
		}
		return false;
	}
	
	/**
	 * копируем часть изображения в новое
	 * 
	 * @return bitmap нового изображения
	 */
	private Bitmap copySegmentImg(Bitmap img, int x1, int y1, int x2, int y2) {
		int width = x2 - x1;
		int height = y2 - y1;
		Bitmap scene = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		int[] pixels = new int[width * height];
		img.getPixels(pixels, 0, width, x1, y1, width, height);
		scene.setPixels(pixels, 0, width, 0, 0, width, height);
		return scene;
	}

	/**
	 * делим изображение на сцены по высоте
	 */
	private boolean cutByHeight(int index) {
		if (listScene.size() <= index)
			return false;
		Bitmap img = listScene.remove(index); //

		int widthImg = img.getWidth();
		int heightImg = img.getHeight();

		boolean isWhiteLine; // для определения белых линий
		boolean isScena = false; // сцену еще не нашли
		int countScene = 0; // количество найденых сцен

		int startScena = heightImg;

		for (int h = heightImg - 1; h >= 0; h--) { // начнем с низу
			isWhiteLine = true; // предположим что это белая полоса
			for (int w = 0; w < widthImg; w++) {
				if (!isWhite(img.getPixel(w, h))) {
					isWhiteLine = false; // ошиблись
					break;
				}
			}

			if (isWhiteLine && !isScena) {  // белая полоса и на сцену еще не
											// наткнулись

			} else if (isWhiteLine && isScena) { // белая полоса после сцены
				if((startScena-h) > MIN_LEN_SCENA){
					// сцена закончилась
					Bitmap scene = this.copySegmentImg(img, 0, h, widthImg, startScena);
					listScene.add(index, scene);
					countScene++;
					isScena = false;
				}
			} else if (!isWhiteLine && isScena) { 	// идем по сцене, до белой
													// полосы еще не дошли

			} else if (!isWhiteLine && !isScena) { 	// Наткнулись на первую линию
													// сцены
				startScena = h;
				isScena = true;
			}
		}
		if(isScena){ // сцена до конца изображения
			Bitmap scene = this.copySegmentImg(img, 0, 0, widthImg, startScena);
			listScene.add(index, scene);
			countScene++;
		}
		img.recycle();
		img = null;
		if (countScene > 1) {
			return true;
		}
		return false;
	}

	/**
	 * делим изображение на сцены по ширине
	 */
	private boolean cutByWeigth(int index) {
		if (listScene.size() <= index)
			return false;
		Bitmap img = listScene.remove(index);

		int widthImg = img.getWidth();
		int heightImg = img.getHeight();

		boolean isWhiteLine; // для определения белых линий
		boolean isScena = false; // сцену еще не нашли
		int countScene = 0; // количество найденых сцен

		int startScena = 0;

		for (int w = 0; w < widthImg; w++) { // начнем с низу
			isWhiteLine = true; // предположим что это белая полоса
			for (int h = 0; h < heightImg; h++) {
				if (!isWhite(img.getPixel(w, h))) {
					isWhiteLine = false; // ошиблись
					break;
				}
			}

			if (isWhiteLine && !isScena) { // белая полоса и на сцену еще не
											// наткнулись

			} else if (isWhiteLine && isScena) { // белая полоса после сцены
				if((w - startScena) > MIN_LEN_SCENA){
					// сцена закончилась
					Bitmap scene = this.copySegmentImg(img, startScena, 0, w, heightImg);
					listScene.add(index, scene);
					countScene++;
					isScena = false;
				}
			} else if (!isWhiteLine && isScena) { // идем по сцене, до белой
													// полосы еще не дошли

			} else if (!isWhiteLine && !isScena) { // Наткнулись на первую линию
													// сцены
				isScena = true;
				startScena = w;
			}
		}
		if(isScena){ // сцена до конца изображения
			Bitmap scene = this.copySegmentImg(img, startScena, 0, widthImg, heightImg);
			listScene.add(index, scene);
			countScene++;
		}
		img.recycle();
		img = null;
		if (countScene > 1) {
			return true;
		}
		return false;
	}

	/**
	 * Рубим страницу на сцены. Изображение страницы очищаем, память ведь не
	 * резиновая :)
	 * 
	 * @return true - удача, false - что то пошло не так
	 */
	private boolean cutPageToScene() {
		if (this.currentPageBitmap == null) {
			return false;
		}

		listScene.clear();
		listScene.add(currentPageBitmap);

		boolean isCutH = false;
		boolean isCutW = false;

		for (int cur = 0; cur < listScene.size();) {
			isCutH = cutByHeight(cur);
			isCutW = cutByWeigth(cur);
			if (!(isCutH || isCutW)) {
				cur++;
			}
		}
		;

		this.currentIndex = 0;

		if (this.currentPageBitmap != null) {
			this.currentPageBitmap.recycle();
			this.currentPageBitmap = null;
		}
		return true;
	}

	/**
	 * Следующая сцена, если сцены на странице закончились запрашиваем следующую
	 * страницу из источника
	 * 
	 * @return Bitmap сцены, либо null если изображение не доступно
	 */
	public Bitmap next() {
		if (listScene.size() > this.currentIndex + 1) {
			this.currentIndex++;
			return this.listScene.get(this.currentIndex);
		} else {
			this.currentPageBitmap = this.mangaSource.next();
			if (this.cutPageToScene() && this.listScene.size() >= 0) {
				return this.listScene.get(this.currentIndex);
			}
		}
		return null;
	}

	/**
	 * Предыдущая сцена, если сцены на странице закончились запрашиваем
	 * предыдующую страницу из источника
	 * 
	 * @return Bitmap сцены, либо null если изображение не доступно
	 */
	public Bitmap prev() {
		if (0 < this.currentIndex) {
			this.currentIndex--;
			return this.listScene.get(this.currentIndex);
		} else {
			this.currentPageBitmap = this.mangaSource.prev();
			if (this.cutPageToScene() && this.listScene.size() >= 0) {
				this.currentIndex = this.listScene.size()-1;
				return this.listScene.get(this.currentIndex);
			}
		}
		return null;
	}

	/**
	 * Текущая сцена
	 * 
	 * @return Bitmap сцены, либо null если изображение не доступно
	 */
	public Bitmap current() {
		if (listScene.size() <= this.currentIndex) {
			return null;
		}
		return this.listScene.get(this.currentIndex);
	}
}
