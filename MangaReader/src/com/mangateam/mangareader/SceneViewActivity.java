/**
 * 
 */
package com.mangateam.mangareader;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author toha
 *
 */
public class SceneViewActivity extends Activity {
	
	private SceneView sceneView = null;
	private MangaSource mSource = null;
	
	public static final String SOURCE_TYPE = "SOURCE_TYPE";
	public static final String SOURCE_ADDR = "MANGA_SOURCE";
	
	public static final int SOURCE_TYPE_PATH = 1;
	public static final int SOURCE_TYPE_FILE = 2;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		int typeSource = getIntent().getIntExtra(SOURCE_TYPE, SOURCE_TYPE_PATH);
		String source = getIntent().getStringExtra(SOURCE_ADDR);
		
		switch (typeSource) {
		case SOURCE_TYPE_PATH:
			mSource = new MangaSourcePath(source);
			break;
			
		case SOURCE_TYPE_FILE:
			mSource = new MangaSourceFile(source);
			break;
			
		default:
			break;
		}
		
		if(sceneView == null){
			sceneView = new SceneView(this);
		}
		sceneView.setMangaSource(mSource);
		setContentView(sceneView);
	}

}
