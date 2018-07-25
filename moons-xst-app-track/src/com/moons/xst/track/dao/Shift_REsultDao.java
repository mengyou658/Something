package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.track.bean.OperMainResult;
import com.moons.xst.track.bean.Shift_REsult;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class Shift_REsultDao extends AbstractDao<Shift_REsult, String> {
	public static final String TABLENAME = "Shift_REsultDap";

	public static class Properties {
		public final static Property Shift_Id = new Property(0, String.class,
				"Shift_Id", true, "Shift_Id");
		public final static Property ShiftStartTime_DT = new Property(1,
				String.class, "ShiftStartTime_DT", false, "ShiftStartTime_DT");
		public final static Property ShiftEndTime_DT = new Property(2,
				String.class, "ShiftEndTime_DT", false, "ShiftEndTime_DT");
		public final static Property JiaoBUser_TX = new Property(3,
				String.class, "JiaoBUser_TX", false, "JiaoBUser_TX");
		public final static Property JieBUser_TX = new Property(4,
				String.class, "JieBUser_TX", false, "JieBUser_TX");
	}

	public Shift_REsultDao(DaoConfig config) {
		super(config);
		// TODO 自动生成的构造函数存根
	}

	public Shift_REsultDao(DaoConfig config, TwoBillResultDaoSession daoSession) {
		super(config, daoSession);
	}
	
	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Shift_REsult' (" + 
                "'Shift_Id' nvarchar(20) PRIMARY KEY NOT NULL," +
                "'ShiftStartTime_DT' nvarchar(20) NOT NULL ," + 
                "'ShiftEndTime_DT' nvarchar(20) NOT NULL ," + 
                "'JiaoBUser_TX' nvarchar(100) ," +
                "'JieBUser_TX' nvarchar(100)" +
                ");");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'Shift_REsult'";
		db.execSQL(sql);
	}


	@Override
	protected void bindValues(SQLiteStatement stmt, Shift_REsult entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindString(1, entity.getShift_Id());
		stmt.bindString(2, entity.getShiftStartTime_DT());
		stmt.bindString(3, entity.getShiftEndTime_DT());
		stmt.bindString(4, entity.getJiaoBUser_TX());
		stmt.bindString(5, entity.getJieBUser_TX());
	}

	@Override
	protected String getKey(Shift_REsult entity) {
		if (entity != null) {
			return entity.getShift_Id();
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
	protected Shift_REsult readEntity(Cursor cursor, int offset) {
		Shift_REsult entity = new Shift_REsult(
				cursor.getString(offset + 0), 
				cursor.getString(offset + 1),
				cursor.getString(offset + 2), 
				cursor.getString(offset + 3),
				cursor.getString(offset + 4)
		);
		return null;
	}

	@Override
	protected void readEntity(Cursor cursor, Shift_REsult entity, int offset) {
		// TODO 自动生成的方法存根
		entity.setShift_Id(cursor.getString(offset + 0));
        entity.setShiftStartTime_DT(cursor.getString(offset + 1));
        entity.setShiftEndTime_DT(cursor.getString(offset + 2));
        entity.setJiaoBUser_TX(cursor.getString(offset + 3));
        entity.setJieBUser_TX(cursor.getString(offset + 4));
	}

	@Override
	protected String readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return cursor.getString(offset + 0);
	}

	@Override
	protected String updateKeyAfterInsert(Shift_REsult entity, long arg1) {
		// TODO 自动生成的方法存根
		return entity.getShift_Id();
	}

}
