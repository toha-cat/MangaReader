/**
 * 
 */
package com.mangateam.mangareader;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * @author toha
 *
 */
public class SceneViewActivity extends Activity {
	
	private SceneView sceneView = null;
	private MangaSource mSource = null;
	
	private int mId = 0;
	
	private int fullPageMode = 0;
	
	public static final String SOURCE_TYPE = "SOURCE_TYPE";
	public static final String SOURCE_ADDR = "MANGA_SOURCE";
	public static final String MANGA_ID = "MANGA_ID";
	
	public static final int SOURCE_TYPE_PATH = 1;
	public static final int SOURCE_TYPE_FILE = 2;
	
	private MangaDB mdb;
	private SQLiteDatabase sqdb;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mId = getIntent().getIntExtra(MANGA_ID, 0);
		int typeSource;
		int page;
		String source;
		
		mdb = new MangaDB(this);
		sqdb = mdb.getWritableDatabase();
		
		String query = "SELECT * FROM " + MangaDB.TABLE_NAME_MANGA_LIST + " WHERE " + MangaDB.UID + " = " + mId;
		Cursor cursor = sqdb.rawQuery(query, null);
		if(cursor.moveToNext()) {
			typeSource = cursor.getInt(cursor.getColumnIndex(MangaDB.TYPE));
			source = cursor.getString(cursor.getColumnIndex(MangaDB.SOURCE));
			page = cursor.getInt(cursor.getColumnIndex(MangaDB.CURRENT_PAGE));
			
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
			
			mSource.setCurPage(page);
			sceneView = new SceneView(this);
			sceneView.setMangaSource(mSource);
			setContentView(sceneView);
			
		} else {
			
		}
		cursor.close();
	}
	
	@Override
	protected void onStop(){
		String insertQuery = "UPDATE  " + 
				MangaDB.TABLE_NAME_MANGA_LIST + " SET " + MangaDB.CURRENT_PAGE + "='" + mSource.getCurPage() 
				+ "' WHERE " + MangaDB.UID + "='" + mId + "';";
		sqdb.execSQL(insertQuery);
		
		super.onStop();
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
		getMenuInflater().inflate(R.menu.scene_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Операции для выбранного пункта меню
		switch (item.getItemId()) {
		case R.id.action_fullpage:
			if(fullPageMode == 0){
				sceneView.viewFullPage();
				item.setTitle(R.string.action_view_scene);
				fullPageMode = 1;
			}else{
				sceneView.viewSceneMode();
				item.setTitle(R.string.action_view_full_page);
				fullPageMode = 0;
			}
			return true;
		case R.id.action_view_filename:
			Toast.makeText(getApplicationContext(), mSource.getSourceName(), 5000).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
