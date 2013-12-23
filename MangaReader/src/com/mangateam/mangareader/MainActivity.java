package com.mangateam.mangareader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {

	private MangaDB mdb;
	private SQLiteDatabase sqdb;
	
	private ArrayList<Integer> mangaId = null;
	private ArrayList<String> mangaName = null;
	
	Button btnFM;
	ListView lvManga;
	
	private static final int REQUEST_LOAD = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// указатель на кнопку добавления манги из ФМ
		btnFM = (Button) findViewById(R.id.btnFM);
		// привязываем к ней обработчик нажатий
		btnFM.setOnClickListener(this);
		
		// указатель на список манги
		lvManga = (ListView) findViewById(R.id.lvManga);
		// привязываем обработчик нажатий на пункты
		lvManga.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				Intent svIntent = new Intent(getBaseContext(), SceneViewActivity.class);
	            svIntent.putExtra(SceneViewActivity.MANGA_ID, mangaId.get(position));
	            startActivity(svIntent);
			}

		});

		// Конектимся к базе данных
	    mdb = new MangaDB(this);
		sqdb = mdb.getWritableDatabase();
		
		// списки имен и ид манги
		mangaId = new ArrayList<Integer>();
		mangaName = new ArrayList<String>();
		
		// Составим и отобразим список
		viewListManga();
	}
	
	private void viewListManga() {
		
		mangaId.clear();
		mangaName.clear();
		
		String query = "SELECT " + MangaDB.UID + ", " + MangaDB.SOURCE + " FROM " + MangaDB.TABLE_NAME_MANGA_LIST;
		Cursor cursor = sqdb.rawQuery(query, null);
		while (cursor.moveToNext()) {
			mangaId.add(cursor.getInt(cursor.getColumnIndex(MangaDB.UID)));
			mangaName.add(cursor.getString(cursor.getColumnIndex(MangaDB.SOURCE)));
		}
		cursor.close();
		
	    // создаем адаптер
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mangaName);
	    // присваиваем адаптер списку
	    lvManga.setAdapter(adapter);
	}

	
	@Override
	protected void onDestroy(){
		// закрываем соединения с базой данных
		sqdb.close();
		mdb.close();
		super.onDestroy();
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
            
            String insertQuery = "INSERT INTO " + 
    				MangaDB.TABLE_NAME_MANGA_LIST + " (" + MangaDB.TYPE + ", " + MangaDB.SOURCE + ", " + MangaDB.CURRENT_PAGE 
    			     + ") VALUES ('" + SceneViewActivity.SOURCE_TYPE_PATH + "', '" + filePath +"', '0')";
            Log.d("SQL", insertQuery);
    		sqdb.execSQL(insertQuery);
			
    		viewListManga();
    		
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("FM", "file not selected");
        }

    }

}
