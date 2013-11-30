package com.mangateam.mangareader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private SceneView sceneView = null;
	private MangaSource mSource = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSource = new MangaSource("/sdcard/test.jpg");
		sceneView = new SceneView(this);
		sceneView.setMangaSource(mSource);
		setContentView(sceneView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
