/**
 * 
 */
package com.mangateam.mangareader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 *
 */
public class PageImages {
	private MangaSource mangaSource;
	private Bitmap currentPageBitmap;
	private ArrayList<Bitmap> listScene = new ArrayList<Bitmap>();
	private int currentIndex = 0;
	
	public PageImages(MangaSource mSource) {
		this.mangaSource = mSource;
		this.currentPageBitmap = this.mangaSource.loadCurrentPage();
		this.cutPageToScene();
	}
	
	/**
	 * �������� ����� ����������� � �����
	 * @return bitmap ������ �����������
	 */
	private Bitmap copySegmentImg(Bitmap img, int x1, int y1, int x2, int y2) {
		int width = x2-x1;
		int height = y2-y1;
		Bitmap scene = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		int[] pixels = new int[width * height];
		img.getPixels(pixels, 0, width, x1, y1, width, height);
		scene.setPixels(pixels, 0, width, 0, 0, width, height);
		return scene;
	}
	
	/**
	 * ����� ����������� �� ����� �� ������
	 */
	private boolean cutByHeight(int index){
		Bitmap img = listScene.remove(index); //   
		
		int widthImg = img.getWidth();
		int heightImg = img.getHeight();
		
		boolean isWhiteLine;   // ��� ����������� ����� �����
		boolean isScena = false; // ����� ��� �� �����
		int countScene = 0; // ���������� �������� ����
		
		int startScena = heightImg;
		
		for(int h = heightImg; h >= 0; h++){ // ������ � ����
			isWhiteLine = true; // ����������� ��� ��� ����� ������
			for(int w = 0; w < widthImg; w++){
				if(img.getPixel(w, h) != Color.WHITE){
					isWhiteLine = false; // ��������
					break;
				}
			}
			
			if(isWhiteLine && !isScena){ // ����� ������ � �� ����� ��� �� ����������
				
			} else if(isWhiteLine && isScena){ // ����� ������ ����� �����
				// ����� �����������
				Bitmap scene = this.copySegmentImg(img, 0, h, widthImg, startScena);
				listScene.add(index, scene);
				countScene++;
			} else if(!isWhiteLine && isScena){ // ���� �� �����, �� ����� ������ ��� �� �����
				
			} else if(!isWhiteLine && !isScena){ // ���������� �� ������ ����� �����
				startScena = h;
			}
		}
		img.recycle();
		img = null;
		if(countScene>1){ return true; }
		return false;
	}
	
	/**
	 * ����� ����������� �� ����� �� ������
	 */
	private boolean cutByWeigth(int index){
		Bitmap img = listScene.remove(index);
		
		int widthImg = img.getWidth();
		int heightImg = img.getHeight();
		
		boolean isWhiteLine;   // ��� ����������� ����� �����
		boolean isScena = false; // ����� ��� �� �����
		int countScene = 0; // ���������� �������� ����
		
		int startScena = 0;
		
		for(int w = widthImg; w >= 0; w++){ // ������ � ����
			isWhiteLine = true; // ����������� ��� ��� ����� ������
			for(int h = 0; h < heightImg; h++){
				if(img.getPixel(w, h) != Color.WHITE){
					isWhiteLine = false; // ��������
					break;
				}
			}
			
			if(isWhiteLine && !isScena){ // ����� ������ � �� ����� ��� �� ����������
				
			} else if(isWhiteLine && isScena){ // ����� ������ ����� �����
				// ����� �����������
				Bitmap scene = this.copySegmentImg(img, startScena, 0, w, heightImg);
				listScene.add(index, scene);
				countScene++;
			} else if(!isWhiteLine && isScena){ // ���� �� �����, �� ����� ������ ��� �� �����
				
			} else if(!isWhiteLine && !isScena){ // ���������� �� ������ ����� �����
				startScena = w;
			}
		}
		img.recycle();
		img = null;
		if(countScene>1){ return true; }
		return false;
	}
	
	/**
	 * ����� �������� �� �����. ����������� �������� �������, ������ ���� �� ��������� :)
	 * @return true - �����, false - ��� �� ����� �� ���
	 */
	private boolean cutPageToScene(){
		if(this.currentPageBitmap == null){
			return false;
		}
		
		listScene.clear();
		listScene.add(currentPageBitmap);
		
		for(int cur = 0; cur < listScene.size();){
			if(!(cutByHeight(cur) || cutByWeigth(cur))){
				cur++;
			}
		};
		
		this.currentIndex = 0;
		
		if(this.currentPageBitmap != null) {
			this.currentPageBitmap.recycle();
			this.currentPageBitmap = null;
		}
		return true;
	}
	
	/**
	 * ��������� �����, ���� ����� �� �������� ����������� 
	 * ����������� ��������� �������� �� ���������
	 * @return Bitmap �����, ���� null ���� ����������� �� ��������
	 */
	public Bitmap next(){
		if(listScene.size() > this.currentIndex+1){
			this.currentIndex++;
			return this.listScene.get(this.currentIndex);
		} else {
			this.currentPageBitmap = this.mangaSource.next();
			if(this.cutPageToScene()){
				return this.listScene.get(this.currentIndex);
			}
		}
		return null;
	}
	
	/**
	 * ���������� �����, ���� ����� �� �������� ����������� 
	 * ����������� ����������� �������� �� ���������
	 * @return Bitmap �����, ���� null ���� ����������� �� ��������
	 */
	public Bitmap prev(){
		if(0 < this.currentIndex){
			this.currentIndex--;
			return this.listScene.get(this.currentIndex);
		} else {
			this.currentPageBitmap = this.mangaSource.prev();
			if(this.cutPageToScene()){
				return this.listScene.get(this.currentIndex);
			}
		}
		return null;
	}
	
	/**
	 * ������� ����� 
	 * @return Bitmap �����, ���� null ���� ����������� �� ��������
	 */
	public Bitmap current(){
		if(listScene.size() <= this.currentIndex) return null;
		return this.listScene.get(this.currentIndex);
	}
}
