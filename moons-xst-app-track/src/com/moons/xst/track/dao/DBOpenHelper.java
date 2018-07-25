package com.moons.xst.track.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBOpenHelper extends SQLiteOpenHelper {

	/**
	 * 创建断点续传记录库
	 * @author LKZ
	 * @version 1.0.0
	 * @created 2014-11-27
	 * */
	public DBOpenHelper(Context context) {
		super(context,"download.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE info(path VARCHAR(1024), thid INTEGER, done INTEGER, PRIMARY KEY(path, thid))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
