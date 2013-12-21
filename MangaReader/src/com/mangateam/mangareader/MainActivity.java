package com.mangateam.mangareader;

import java.util.logging.Level;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private SceneView sceneView = null;
	private MangaSource mSource = null;
	Button btnFM;
	
	private static final int REQUEST_LOAD = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnFM = (Button) findViewById(R.id.btnFM);
		btnFM.setOnClickListener(this);
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
		case R.id.btnFM:
			Log.d("FM", "FM start");
			Intent intent = new Intent(getBaseContext(), FileDialog.class);
	        intent.putExtra(FileDialog.START_PATH, "/sdcard");
	        //can user select directories or not
	        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
	        //alternatively you can set file filter
	        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
	        startActivityForResult(intent, REQUEST_LOAD);
			break;
		}
	}
	
	public synchronized void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {
		Log.d("FM", "OK");

        if (resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
            Log.d("FM", filePath);
            
            
            Intent svIntent = new Intent(getBaseContext(), SceneViewActivity.class);
            
            svIntent.putExtra(SceneViewActivity.SOURCE_TYPE, SceneViewActivity.SOURCE_TYPE_PATH);
            svIntent.putExtra(SceneViewActivity.SOURCE_ADDR, filePath);
            
            startActivity(svIntent);
			
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("FM", "file not selected");
        }

    }

}
