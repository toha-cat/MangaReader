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
	 * ��������� �����, ���� ����� �� �������� ����������� 
	 * ����������� ��������� �������� �� ���������
	 * @return Bitmap �����, ���� null ���� ����������� �� ��������
	 */
	public Bitmap next(){
		
		return null;
	}
	
	/**
	 * ���������� �����, ���� ����� �� �������� ����������� 
	 * ����������� ����������� �������� �� ���������
	 * @return Bitmap �����, ���� null ���� ����������� �� ��������
	 */
	public Bitmap prev(){
		
		return null;
	}
}
