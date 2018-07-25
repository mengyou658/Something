package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.track.bean.Operate_Bill;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class Operate_BillDao extends AbstractDao<Operate_Bill, String> {

	public static final String TABLENAME = "Operate_Bill";

	/**
	 * Properties of entity Operate_Bill.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Operate_Bill_Code = new Property(0,
				String.class, "Operate_Bill_Code", true, "Operate_Bill_Code");
		public final static Property Operate_Begin_Time = new Property(1,
				String.class, "Operate_Begin_Time", false, "Operate_Begin_Time");
		public final static Property Operate_End_Time = new Property(2,
				String.class, "Operate_End_Time", false, "Operate_End_Time");
		public final static Property Tache_ID = new Property(3, String.class,
				"Tache_ID", false, "Tache_ID");
		public final static Property Operate_Task_Content = new Property(4,
				String.class, "Operate_Task_Content", false,
				"Operate_Task_Content");
		public final static Property Operate_Memo = new Property(5,
				String.class, "Operate_Memo", false, "Operate_Memo");
		public final static Property Operate_Bill_Summary = new Property(6,
				String.class, "Operate_Bill_Summary", false,
				"Operate_Bill_Summary");
		public final static Property Operate_Bill_Operator = new Property(7,
				String.class, "Operate_Bill_Operator", false,
				"Operate_Bill_Operator");
		public final static Property Operate_Bill_Guardian = new Property(8,
				String.class, "Operate_Bill_Guardian", false,
				"Operate_Bill_Guardian");
		public final static Property Operate_Bill_Watch = new Property(9,
				String.class, "Operate_Bill_Watch", false, "Operate_Bill_Watch");
		public final static Property Operate_Bill_Duty = new Property(10,
				String.class, "Operate_Bill_Duty", false, "Operate_Bill_Duty");
		public final static Property ID_CD = new Property(11, String.class,
				"ID_CD", false, "ID_CD");
		public final static Property Place_TX = new Property(12, String.class,
				"Place_TX", false, "Place_TX");
	};

	public Operate_BillDao(DaoConfig config) {
		super(config);
	}

	public Operate_BillDao(DaoConfig config, TwoBillSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'Operate_Bill' (" + //
				"'Operate_Bill_Code' nvarchar(100) PRIMARY KEY NOT NULL ," + // 0:
																				// Operate_Bill_Code
				"'Operate_Begin_Time' datetime NOT NULL ," + // 1:
																// Operate_Begin_Time
				"'Operate_End_Time' datetime ," + // 2: Operate_End_Time
				"'Operate_Task_Content' nvarchar(500) NOT NULL ," + // 3:
																	// Operate_Task_Content
				"'Operate_Memo' nvarchar(1000) ," + // 4: Operate_Memo
				"'Operate_Bill_Summary' nvarchar(200) ," + // 5:
															// Operate_Bill_Summary
				"'Operate_Bill_Operator' nvarchar(100) ," + // 6:
															// Operate_Bill_Operator
				"'Operate_Bill_Guardian' nvarchar(100) ," + // 7:
															// Operate_Bill_Guardian
				"'Operate_Bill_Watch' nvarchar(100) ," + // 8:
															// Operate_Bill_Watch
				"'Operate_Bill_Duty' nvarchar(100) ," + // 9: Operate_Bill_Duty
				"'ID_CD' nvarchar(20) ," + // 10: ID_CD
				"'Place_TX' nvarchar(100)  );"); // 11: Place_TX
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'Operate_Bill'";
		db.execSQL(sql);
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, Operate_Bill entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindString(1, entity.getOperate_Bill_Code());
		stmt.bindString(2, entity.getOperate_Begin_Time());
		stmt.bindString(3, entity.getOperate_End_Time());
		stmt.bindString(4, entity.getTache_ID());
		stmt.bindString(5, entity.getOperate_Task_Content());
		stmt.bindString(6, entity.getOperate_Memo());
		stmt.bindString(7, entity.getOperate_Bill_Summary());
		stmt.bindString(8, entity.getOperate_Bill_Operator());
		stmt.bindString(9, entity.getOperate_Bill_Guardian());
		stmt.bindString(10, entity.getOperate_Bill_Watch());
		stmt.bindString(11, entity.getOperate_Bill_Duty());
		stmt.bindString(12, entity.getID_CD());
		stmt.bindString(13, entity.getPlace_TX());
	}

	@Override
	protected String getKey(Operate_Bill entity) {
		// TODO 自动生成的方法存根
		if (entity != null) {
			return entity.getOperate_Bill_Code();
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
	protected Operate_Bill readEntity(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		Operate_Bill entity = new Operate_Bill( //
				cursor.getString(offset + 0), // Operate_Bill_Code
				cursor.getString(offset + 1), // Operate_Begin_Time
				cursor.getString(offset + 2), // Operate_End_Time
				cursor.getString(offset + 3), //
				cursor.getString(offset + 4), // Operate_Task_Content
				cursor.getString(offset + 5), // Operate_Memo
				cursor.getString(offset + 6), // Operate_Bill_Summary
				cursor.getString(offset + 7), // Operate_Bill_Operator
				cursor.getString(offset + 8), // Operate_Bill_Guardian
				cursor.getString(offset + 9), // Operate_Bill_Watch
				cursor.getString(offset + 10), // Operate_Bill_Duty
				cursor.getString(offset + 11), // ID_CD
				cursor.getString(offset + 12)// Place_TX
		);
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, Operate_Bill entity, int offset) {
		// TODO 自动生成的方法存根
		entity.setOperate_Bill_Code(cursor.getString(offset + 0));
		entity.setOperate_Begin_Time(cursor.getString(offset + 1));
		entity.setOperate_End_Time(cursor.getString(offset + 2));
		entity.setTache_ID(cursor.getString(offset + 3));
		entity.setOperate_Task_Content(cursor.getString(offset + 4));
		entity.setOperate_Memo(cursor.getString(offset + 5));
		entity.setOperate_Bill_Summary(cursor.getString(offset + 6));
		entity.setOperate_Bill_Operator(cursor.getString(offset + 7));
		entity.setOperate_Bill_Guardian(cursor.getString(offset + 8));
		entity.setOperate_Bill_Watch(cursor.getString(offset + 9));
		entity.setOperate_Bill_Duty(cursor.getString(offset + 10));
		entity.setID_CD(cursor.getString(offset + 11));
		entity.setPlace_TX(cursor.getString(offset + 12));
	}

	@Override
	protected String readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return cursor.getString(offset + 0);
	}

	@Override
	protected String updateKeyAfterInsert(Operate_Bill entity, long arg1) {
		// TODO 自动生成的方法存根
		return entity.getOperate_Bill_Code();
	}

}
