package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.track.bean.OperDetailResult;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class OperDetailResultDao extends AbstractDao<OperDetailResult, Long> {
	public static final String TABLENAME = "OperDetailResult";

	public static class Properties {
		public final static Property Operate_Bill_Code = new Property(0,
				String.class, "Operate_Bill_Code", false, "Operate_Bill_Code");
		public final static Property Operate_Detail_Item_ID = new Property(1,
				int.class, "Operate_Detail_Item_ID", true,
				"Operate_Detail_Item_ID");
		public final static Property Operate_Detail_Item_State = new Property(
				2, int.class, "Operate_Detail_Item_State", false,
				"Operate_Detail_Item_State");
		public final static Property Operate_Detail_Item_Exe_Time = new Property(
				3, String.class, "Operate_Detail_Item_Exe_Time", false,
				"Operate_Detail_Item_Exe_Time");
	}

	public OperDetailResultDao(DaoConfig config) {
		super(config);
	}

	public OperDetailResultDao(DaoConfig config,
			TwoBillResultDaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'OperDetailResult' (" + //
				"'Operate_Bill_Code' nvarchar(100) NOT NULL ," + // 0:
																	// Operate_Bill_Code
				"'Operate_Detail_Item_ID' INTEGER PRIMARY KEY NOT NULL ," + // 1:
																			// Operate_Detail_Item_ID
				"'Operate_Detail_Item_State' INTEGER ," + // 2:
															// Operate_Detail_Item_State
				"'Operate_Detail_Item_Exe_Time' nvarchar(20));");// Operate_Detail_Item_Exe_Time
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'OperDetailResult'";
		db.execSQL(sql);
	}

	// OperDetailResult表插入数据
	public void insertDetail(OperDetailResult entity) {
		try {
			String sql = "INSERT INTO OperDetailResult"
					+ "(Operate_Bill_Code,Operate_Detail_Item_ID,Operate_Detail_Item_State,Operate_Detail_Item_Exe_Time)"
					+ "VALUES('" + entity.getOperate_Bill_Code() + "',"
					+ entity.getOperate_Detail_Item_ID() + ","
					+ entity.getOperate_Detail_Item_State() + ",'"
					+ entity.getOperate_Detail_Item_Exe_Time() + "')";
			db.execSQL(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, OperDetailResult entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindString(1, entity.getOperate_Bill_Code());
		stmt.bindLong(2, entity.getOperate_Detail_Item_ID());
		stmt.bindLong(3, entity.getOperate_Detail_Item_State());
		stmt.bindString(4, entity.getOperate_Detail_Item_Exe_Time());
	}

	@Override
	protected Long getKey(OperDetailResult entity) {
		// TODO 自动生成的方法存根
		if (entity != null) {
			return (long) entity.getOperate_Detail_Item_ID();
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
	protected OperDetailResult readEntity(Cursor cursor, int offset) {
		OperDetailResult entity = new OperDetailResult(
				cursor.getString(offset + 0), // Operate_Bill_Code
				cursor.getInt(offset + 1), // Operate_Begin_Time
				cursor.getInt(offset + 2), // Operate_End_Time
				cursor.getString(offset + 3)// Operate_Task_Content
		);

		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, OperDetailResult entity, int offset) {
		// TODO 自动生成的方法存根
		entity.setOperate_Bill_Code(cursor.getString(offset + 0));
		entity.setOperate_Detail_Item_ID(cursor.getInt(offset + 1));
		entity.setOperate_Detail_Item_State(cursor.getInt(offset + 2));
		entity.setOperate_Detail_Item_Exe_Time(cursor.getString(offset + 3));
	}

	@Override
	protected Long readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return (long) cursor.getInt(offset + 1);
	}

	@Override
	protected Long updateKeyAfterInsert(OperDetailResult entity, long arg1) {
		// TODO 自动生成的方法存根
		return (long) entity.getOperate_Detail_Item_ID();
	}

	/** @inheritdoc */
	public long InsertOperDetailResult(OperDetailResult entity) {
		long row = insert(entity);
		return row;
	}

	public void Operatesql(String sql) {
		SQLiteDatabase db = getDatabase();

		db.execSQL(sql);
	}
}
