package com.moons.xst.track.dao;

import java.util.List;

import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.track.bean.OperDetailResult;
import com.moons.xst.track.bean.OperMainResult;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class OperMainResultDao extends AbstractDao<OperMainResult, String> {
	public static final String TABLENAME = "OperMainResult";

	public static class Properties {
		public final static Property Operate_Bill_Code = new Property(0,
				String.class, "Operate_Bill_Code", true, "Operate_Bill_Code");
		public final static Property Operate_Begin_Time = new Property(1,
				String.class, "Operate_Begin_Time", false, "Operate_Begin_Time");
		public final static Property Operate_End_Time = new Property(2,
				String.class, "Operate_End_Time", false, "Operate_End_Time");
		public final static Property Operate_Detail_Item_Operator = new Property(
				3, String.class, "Operate_Detail_Item_Operator", false,
				"Operate_Detail_Item_Operator");
		public final static Property Operate_Detail_Item_OperName = new Property(
				4, String.class, "Operate_Detail_Item_OperName", false,
				"Operate_Detail_Item_OperName");
		public final static Property Operate_Detail_Item_Guardian = new Property(
				5, String.class, "Operate_Detail_Item_Guardian", false,
				"Operate_Detail_Item_Guardian");
		public final static Property Operate_Detail_Item_Watch = new Property(
				6, String.class, "Operate_Detail_Item_Watch", false,
				"Operate_Detail_Item_Watch");
		public final static Property Operate_Detail_Item_Duty = new Property(7,
				String.class, "Operate_Detail_Item_Duty", false,
				"Operate_Detail_Item_Duty");
	}

	public OperMainResultDao(DaoConfig config) {
		super(config);
		// TODO 自动生成的构造函数存根
	}

	public OperMainResultDao(DaoConfig config,TwoBillResultDaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'OperMainResult' ("
				+ "'Operate_Bill_Code' nvarchar(100) PRIMARY KEY NOT NULL,"
				+ "'Operate_Begin_Time' nvarchar(20) NOT NULL ,"
				+ "'Operate_End_Time' nvarchar(20) ,"
				+ "'Operate_Detail_Item_Operator' nvarchar(100) ,"
				+ "'Operate_Detail_Item_OperName' nvarchar(100) ,"
				+ "'Operate_Detail_Item_Guardian' nvarchar(100) ,"
				+ "'Operate_Detail_Item_Watch' nvarchar(100) ,"
				+ "'Operate_Detail_Item_Duty' nvarchar(100)" + ");");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'OperMainResult'";
		db.execSQL(sql);
	}

	// OperMainResult表插入数据
	public void inserMain(OperMainResult entity) {
		String sql = "insert into OperMainResult"
				+ "(Operate_Bill_Code,Operate_Begin_Time)" + " values('"
				+ entity.getOperate_Bill_Code() + "','"
				+ entity.getOperate_Begin_Time() + "')";
		db.execSQL(sql);
	}

	/**
	 * 修改OperMainResult结束时间
	 * 
	 * @param
	 */
	public void updateOperMain(String code, String time) {
		String sql = "update OperMainResult set Operate_End_Time ='" + time
				+ "' where Operate_Bill_Code ='" + code + "'";
		db.execSQL(sql);
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, OperMainResult entity) {
		// TODO 自动生成的方法存根
		stmt.clearBindings();
		stmt.bindString(1, entity.getOperate_Bill_Code());
		stmt.bindString(2, entity.getOperate_Begin_Time());
		stmt.bindString(3, entity.getOperate_End_Time());
		stmt.bindString(4, entity.getOperate_Detail_Item_Operator());
		stmt.bindString(5, entity.getOperate_Detail_Item_OperName());
		stmt.bindString(6, entity.getOperate_Detail_Item_Guardian());
		stmt.bindString(7, entity.getOperate_Detail_Item_Watch());
		stmt.bindString(8, entity.getOperate_Detail_Item_Duty());
	}

	@Override
	protected String getKey(OperMainResult entity) {
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
	protected OperMainResult readEntity(Cursor cursor, int offset) {
		OperMainResult entity = new OperMainResult(
				cursor.getString(offset + 0), cursor.getString(offset + 1),
				cursor.getString(offset + 2), cursor.getString(offset + 3),
				cursor.getString(offset + 4), cursor.getString(offset + 5),
				cursor.getString(offset + 6), cursor.getString(offset + 7));
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, OperMainResult entity, int offset) {
		// TODO 自动生成的方法存根
		entity.setOperate_Bill_Code(cursor.getString(offset + 0));
		entity.setOperate_Begin_Time(cursor.getString(offset + 1));
		entity.setOperate_End_Time(cursor.getString(offset + 2));
		entity.setOperate_Detail_Item_Operator(cursor.getString(offset + 3));
		entity.setOperate_Detail_Item_OperName(cursor.getString(offset + 4));
		entity.setOperate_Detail_Item_Guardian(cursor.getString(offset + 5));
		entity.setOperate_Detail_Item_Watch(cursor.getString(offset + 6));
		entity.setOperate_Detail_Item_Duty(cursor.getString(offset + 7));
	}

	@Override
	protected String readKey(Cursor cursor, int offset) {
		// TODO 自动生成的方法存根
		return cursor.getString(offset + 0);
	}

	@Override
	protected String updateKeyAfterInsert(OperMainResult entity, long arg1) {
		// TODO 自动生成的方法存根
		return entity.getOperate_Bill_Code();
	}

}
