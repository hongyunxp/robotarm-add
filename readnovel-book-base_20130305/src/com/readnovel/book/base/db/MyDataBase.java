package com.readnovel.book.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBase extends SQLiteOpenHelper {
	public static final String DB_NAME = "singlebook.db";
	public static final String TABLE_NAME_BOOKTAG = "singlebooktag";
	public static final String TABLE_NAME_HISTORY = "singlebookhistory";
	public static final String TABLE_NAME_CHAPTERPAGE_SAMLL = "singlebookpagenumsmall";// 26号字体表
	public static final String TABLE_NAME_CHAPTERPAGE_MEDIE = "singlebookpagenummedie";// 32号字体表
	public static final String TABLE_NAME_CHAPTERPAGE_BIG = "singlebookpagenumbig";// 38号字体表
	public static final String ID = "id";
	public static final String FileName = "filename";
	public static final String BookTitle = "title";
	public static final String LastOffset = "last_offset";
	public static final String Time = "time";
	public static final String ForeText = "foretext";
	public static final String PerCent = "percent";
	public static final String FontSize = "fontsize";
	public static final String CHAPTERNUM = "chapternum";
	public static final String PAGENUM = "pagenum";

	public MyDataBase(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME_BOOKTAG + " (" + ID + " INTEGER PRIMARY KEY," + BookTitle + " TEXT," + ForeText + " TEXT," + Time
				+ " TEXT," + PerCent + " TEXT," + PAGENUM + " TEXT," + FontSize + " INTEGER," + FileName + " TEXT," + LastOffset
				+ " INTEGER default 0 " + ");");
		// db.execSQL("CREATE TABLE " + TABLE_NAME_HISTORY + " (" + ID
		// + " INTEGER PRIMARY KEY," + BookTitle + " TEXT," + ForeText
		// + " TEXT," + Time + " TEXT," + PerCent + " TEXT," + FontSize
		// + " INTEGER," + FileName + " TEXT," + LastOffset
		// + " INTEGER default 0 " + ");");
		db.execSQL("CREATE TABLE " + TABLE_NAME_CHAPTERPAGE_SAMLL + " (" + ID + " INTEGER PRIMARY KEY," + CHAPTERNUM + " TEXT," + FileName + " TEXT"
				+ ");");
		db.execSQL("CREATE TABLE " + TABLE_NAME_CHAPTERPAGE_MEDIE + " (" + ID + " INTEGER PRIMARY KEY," + CHAPTERNUM + " TEXT," + FileName + " TEXT"
				+ ");");

		db.execSQL("CREATE TABLE " + TABLE_NAME_CHAPTERPAGE_BIG + " (" + ID + " INTEGER PRIMARY KEY," + CHAPTERNUM + " TEXT," + FileName + " TEXT"
				+ ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
