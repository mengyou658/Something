package com.moons.xst.track.dao;
/**
 * 断点续传记录-DAO
 * @author LKZ
 * @version 1.0.0
 * @created 2014-11-27
 * */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.moons.xst.track.bean.DownLoadResumingInfo;

public class InfoDAO{
	private DBOpenHelper helper;

	public InfoDAO(Context context) {
		helper = new DBOpenHelper(context);
	}
	public void insert(DownLoadResumingInfo info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("INSERT INTO info(path, thid, done) VALUES(?, ?, ?)", new Object[] { info.getUrlpath(), info.getThreadID(), info.getdoneYN()});
	}

	public void delete(String path, int thid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("DELETE FROM info WHERE path=? AND thid=?", new Object[] { path, thid });
	}

	public void update(DownLoadResumingInfo info) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("UPDATE info SET done=? WHERE path=? AND thid=?", new Object[] { info.getdoneYN(), info.getUrlpath(), info.getThreadID()});
	}

	public DownLoadResumingInfo query(String path, int thid) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT path,thid,done FROM info WHERE path=? AND thid=?", new String[] { path, String.valueOf(thid) });
		DownLoadResumingInfo info = null;
		if (c.moveToNext())
			info = new DownLoadResumingInfo(c.getString(0), c.getInt(1), c.getInt(2));
		c.close();

		return info;
	}

	public void deleteAll(String path, int len) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT SUM(done) FROM info WHERE path=?", new String[] { path });
		if (c.moveToNext()) {
			int result = c.getInt(0);
			if (result == len)
				db.execSQL("DELETE FROM info WHERE path=? ", new Object[] { path });
		}
	}

	public List<String> queryUndone() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT DISTINCT path FROM info", null);
		List<String> pathList = new ArrayList<String>();
		while (c.moveToNext())
			pathList.add(c.getString(0));
		c.close();
		return pathList;
	}
}
