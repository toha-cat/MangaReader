package com.mangateam.mangareader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MangaSourcePath implements MangaSource {
	
	private ArrayList<String> fileList = null;
	
	private int listPointer;
	
	public MangaSourcePath(String path) {
		this.fileList = new ArrayList<String>();
		File dir = new File(path);
		if (dir.isDirectory()){
			this.addFileToListOfDir(dir.listFiles());
			this.listPointer = 0;
		} else if(dir.isFile()){
			this.addFileToListOfDir(dir.getParentFile().listFiles());
			this.listPointer = fileList.indexOf(path);
		}
	}

	private int addFileToListOfDir(File[] files) {
		Arrays.sort(files);
	    //add every file into list
	    for (File file : files) {
	    	if(file.isFile() && this.isImg(file)){
	    		fileList.add(file.getAbsolutePath());
	    	}else if(file.isDirectory()){
	    		this.addFileToListOfDir(file.listFiles());
	    	}
	    }
		return 0;
	}
	
	private boolean isImg(File file) {
		String name = file.getName();
		if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")){
			return true;
		}
		return false;
	}
	
	// temp method/ load image for fs
	private Bitmap loadImg(String fileName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap image = BitmapFactory.decodeFile(fileName, options);
		if (image == null)
			throw new NullPointerException("The image can't be decoded.");
		
		return image;
	}

	@Override
	public Bitmap loadCurrentPage() {
		Log.d("ITERATOR CURRENT", String.valueOf(this.listPointer));
		if(this.listPointer >= 0 && this.listPointer < fileList.size()){
			return this.loadImg(fileList.get(this.listPointer));
		}
		return null;
	}

	@Override
	public Bitmap next() {
		Log.d("ITERATOR NEXT", String.valueOf(this.listPointer));
		if((this.listPointer+1) < fileList.size()){
			this.listPointer++;
			return this.loadImg(fileList.get(this.listPointer));
		}
		return null;
	}

	@Override
	public Bitmap prev() {
		Log.d("ITERATOR NEXT", String.valueOf(this.listPointer));
		if(this.listPointer > 0){
			this.listPointer--;
			return this.loadImg(fileList.get(this.listPointer));
		}
		return null;
	}
}
