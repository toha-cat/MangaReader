package com.mangateam.mangareader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private SceneView sceneView = null;
	private MangaSource mSource = null;
	Button btnOk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnOk = (Button) findViewById(R.id.button1);
		btnOk.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 
	@Override
	public void onClick(View v) {
	// по id определеяем кнопку, вызвавшую этот обработчик
		switch (v.getId()) {
	    case R.id.button1:
	       // кнопка ОК
	    	mSource = new MangaSource("/sdcard/test.jpg");
			sceneView = new SceneView(this);
			sceneView.setMangaSource(mSource);
			setContentView(sceneView);
	       break;
	    }
	}

}
