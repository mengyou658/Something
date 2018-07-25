package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.track.bean.Operate_Detail_Bill;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class Operate_DetailBillDao extends
		AbstractDao<Operate_Detail_Bill, String> {

	public static final String TABLENAME = "Operate_Detail_Bill";

	public static class Properties {
		public final static Property Operate_Bill_Code = new Property(0,
				String.class, "Operate_Bill_Code", false, "Operate_Bill_Code");
		public final static Property Operate_Detail_Item_ID = new Property(1,
				String.class, "Operate_Detail_Item_ID", true,
				"Operate_Detail_Item_ID");
		public final static Property Operate_Type_ID = new Property(2,
				String.class, "Operate_Type_ID", false, "Operate_Type_ID");
		public final static Property Operate_Detail_Item_Order = new Property(
				3, String.class, "Operate_Detail_Item_Order", false,
				"Operate_Detail_Item_Order");
		public final static Property Operate_Detail_Item_Content = new Property(
				4, String.class, "Operate_Detail_Item_Content", false,
				"Operate_Detail_Item_Content");
		public final static Property Operate_Detail_Item_State = new Property(
				5, String.class, "Operate_Detail_Item_State", false,
				"Operate_Detail_Item_State");
		public final static Property Operate_Detail_Item_Exe_Time = new Property(
				6, String.class, "Operate_Detail_Item_Exe_Time", false,
				"Operate_Detail_Item_Exe_Time");
	};

	public Operate_DetailBillDao(DaoConfig config) {
		super(config);
	}

	public Operate_DetailBillDao(DaoConfig config, TwoBillSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'Operate_Detail_Bill' (" + //
				"'Operate_Bill_Code' nvarchar(100) NOT NULL ," + // 0:
																	// Operate_Bill_Code
				"'Operate_Detail_Item_ID' INTEGER PRIMARY KEY NOT NULL ," + // 1:
																			// Operate_Detail_Item_ID
				"'Operate_Type_ID' INTEGER NOT NULL ," + // 2: Operate_Type_ID
				"'Operate_Detail_Item_Order' INTEGER NOT NULL ," + // 3:
																	// Operate_Detail_Item_Order
				"'Operate_Detail_Item_Content' nvarchar(1000) NOT NULL ," + // 4:
																			// Operate_Detail_Item_Content
				"'Operate_Detail_Item_State' INTEGER ," + // 5:
															// Operate_Detail_Item_State
				"'Operate_Detail_Item_Exe_Time' datetime) );"); // 6:
																// Operate_Detail_Item_Exe_Time
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'Operate_Detail_Bill'";
		db.execSQL(sql);
	}

	/**
	 * 修改操作项状态
	 * 
	 * @param Operate_Detail_Item_id
	 */
	public void updateOperateState(String Operate_id, String State, String Time) {
		String sql = "update Operate_Detail_Bill set Operate_Detail_Item_State ='"
				+ State
				+ "', Operate_Detail_Item_Exe_Time ='"
				+ Time
				+ "' where Operate_Detail_Item_ID ='" + Operate_id + "'";
		db.execSQL(sql);
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, Operate_Detail_Bill entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindString(1, entity.getOperate_Bill_Code());
		stmt.bindString(2, entity.getOperate_Detail_Item_ID());
		stmt.bindString(3, entity.getOperate_Type_ID());
		stmt.bindString(4, entity.getOperate_Detail_Item_Order());
		stmt.bindString(5, entity.getOperate_Detail_Item_Content());
		stmt.bindString(6, entity.getOperate_Detail_Item_State());
		stmt.bindString(7, entity.getOperate_Detail_Item_Exe_Time());
	}

	@Override
	protected String getKey(Operate_Detail_Bill entity) {
		// TODO 自动生成的方法存根
		if (entity != null) {
			return entity.getOperate_Detail_Item_ID();
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
	protected Operate_Detail_Bill readEntity(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		Operate_Detail_Bill entity = new Operate_Detail_Bill( //
				cursor.getString(offset + 0), // Operate_Bill_Code
				cursor.getString(offset + 1), // Operate_Detail_Item_ID
				cursor.getString(offset + 2), // Operate_Type_ID
				cursor.getString(offset + 3), // Operate_Detail_Item_Order
				cursor.getString(offset + 4), // Operate_Detail_Item_Content
				cursor.getString(offset + 5), // Operate_Detail_Item_State
				cursor.getString(offset + 6) // Operate_Detail_Item_Exe_Time
		);
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, Operate_Detail_Bill entity,
			int offset) {
		// TODO 自动生成的方法存根
		entity.setOperate_Bill_Code(cursor.getString(offset + 0));
		entity.setOperate_Detail_Item_ID(cursor.getString(offset + 1));
		entity.setOperate_Type_ID(cursor.getString(offset + 2));
		entity.setOperate_Detail_Item_Order(cursor.getString(offset + 3));
		entity.setOperate_Detail_Item_Content(cursor.getString(offset + 4));
		entity.setOperate_Detail_Item_State(cursor.getString(offset + 5));
		entity.setOperate_Detail_Item_Exe_Time(cursor.getString(offset + 6));
	}

	@Override
	protected String readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return cursor.getString(offset + 1);
	}

	@Override
	protected String updateKeyAfterInsert(Operate_Detail_Bill entity, long arg1) {
		// TODO 自动生成的方法存根
		return entity.getOperate_Detail_Item_ID();
	}

}
