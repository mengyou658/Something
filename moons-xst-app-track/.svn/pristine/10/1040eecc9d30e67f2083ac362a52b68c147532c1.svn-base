package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.track.bean.WH_DQTask;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class WH_DQTaskDao extends AbstractDao<WH_DQTask, Long> {
	public static final String TABLENAME = "WH_DQTaskDap";

	public static class Properties {
		public final static Property DQSpec_ID = new Property(0, int.class,
				"DQSpec_ID", true, "DQSpec_ID");
		public final static Property ZXRName_TX = new Property(1, String.class,
				"ZXRName_TX", false, "ZXRName_TX");
		public final static Property JHRName_TX = new Property(2, String.class,
				"JHRName_TX", false, "JHRName_TX");
		public final static Property TaskStatus_CD = new Property(3,
				String.class, "TaskStatus_CD", false, "TaskStatus_CD");
		public final static Property Finish_DT = new Property(4, String.class,
				"Finish_DT", true, "Finish_DT");
		public final static Property Content_TX = new Property(5, String.class,
				"Content_TX", false, "Content_TX");
		public final static Property IDPosTime_DT = new Property(6,
				String.class, "IDPosTime_DT", false, "IDPosTime_DT");
	}

	public WH_DQTaskDao(DaoConfig config) {
		super(config);
	}

	public WH_DQTaskDao(DaoConfig config, TwoBillResultDaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'WH_DQTask' ("
				+ "'DQSpec_ID' INTEGER PRIMARY KEY NOT NULL,"
				+ "'ZXRName_TX' nvarchar(100) ,"
				+ "'JHRName_TX' nvarchar(100) ,"
				+ "'TaskStatus_CD' nvarchar(100),"
				+ "'Finish_DT' nvarchar(20),"
				+ "'Content_TX' nvarchar(500),"
				+ "'IDPosTime_DT' nvarchar(20)" + ");");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'WH_DQTask'";
		db.execSQL(sql);
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, WH_DQTask entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindLong(1, entity.getDQSpec_ID());
		stmt.bindString(2, entity.getZXRName_TX());
		stmt.bindString(3, entity.getJHRName_TX());
		stmt.bindString(4, entity.getTaskStatus_CD());
		stmt.bindString(5, entity.getFinish_DT());
		stmt.bindString(6, entity.getContent_TX());
		stmt.bindString(7, entity.getIDPosTime_DT());
	}

	@Override
	protected Long getKey(WH_DQTask entity) {
		if (entity != null) {
			return (long) entity.getDQSpec_ID();
		} else {
			return null;
		}
	}

	@Override
	protected boolean isEntityUpdateable() {
		// TODO 自动生成的方法存根
		return true;
	}

	@Override
	protected WH_DQTask readEntity(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		WH_DQTask entity = new WH_DQTask(
				cursor.getInt(offset + 0), 
				cursor.getString(offset + 1),
				cursor.getString(offset + 2), 
				cursor.getString(offset + 3),
				cursor.getString(offset + 4), 
				cursor.getString(offset + 5),
				cursor.getString(offset + 6)
		);
		return null;
	}

	@Override
	protected void readEntity(Cursor cursor, WH_DQTask entity, int offset) {
		entity.setDQSpec_ID(cursor.getInt(offset + 0));
        entity.setZXRName_TX(cursor.getString(offset + 1));
        entity.setJHRName_TX(cursor.getString(offset + 2));
        entity.setTaskStatus_CD(cursor.getString(offset + 3));
        entity.setFinish_DT(cursor.getString(offset + 4));
        entity.setContent_TX(cursor.getString(offset + 5));
        entity.setIDPosTime_DT(cursor.getString(offset + 6));
	}

	@Override
	protected Long readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return (long) cursor.getInt(offset + 0);
	}

	@Override
	protected Long updateKeyAfterInsert(WH_DQTask entity, long arg1) {
		// TODO 自动生成的方法存根
		return (long) entity.getDQSpec_ID();
	}

}
