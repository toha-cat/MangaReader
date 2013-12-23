package com.mangateam.mangareader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MangaDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "manga_database.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME_MANGA_LIST = "manga_list";
	public static final String UID = "_id";
	public static final String TYPE = "type";
	public static final String SOURCE = "source";
	public static final String CURRENT_PAGE = "current_page";

	public MangaDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+ TABLE_NAME_MANGA_LIST + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ TYPE + " INTEGER, " + SOURCE + " VARCHAR(255), " + CURRENT_PAGE + " INTEGER);";
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
