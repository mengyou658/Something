package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.bean.MObject_State;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/*public class HisMObject_StateHisDao extends AbstractDao<MObject_State, Void> {

	public static final String TABLENAME = "MOBJECT_STATE";

	*//**
	 * Properties of entity MObject_State.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 *//*
	public static class Properties {

		public final static Property Line_ID = new Property(0, String.class,
				"Line_ID", false, "LINE_ID");
		public final static Property StartAndEndPoint_ID = new Property(1,
				String.class, "StartAndEndPoint_ID", false,
				"STARTANDENDPOINT_ID");
		public final static Property CompleteTime_DT = new Property(2,
				java.util.Date.class, "CompleteTime_DT", false,
				"COMPLETETIME_DT");
		public final static Property ControlPointDesc_TX = new Property(3,
				java.util.Date.class, "ControlPointDesc_TX", false,
				"CONTROLPOINTDESC_TX");
		public final static Property MObjectState_CD = new Property(4,
				String.class, "MObjectState_CD", false, "MOBJECTSTATE_CD");
		public final static Property MObjectStateName_TX = new Property(5,
				String.class, "MObjectStateName_TX", false,
				"MOBJECTSTATENAME_TX");
		public final static Property AppUser_CD = new Property(6, String.class,
				"AppUser_CD", false, "APPUSER_CD");
		public final static Property DataState_YN = new Property(7,
				String.class, "DataState_YN", false, "DATASTATE_YN");
		public final static Property TransInfo_TX = new Property(8,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
	};

	public HisMObject_StateHisDao(DaoConfig config) {
		super(config);
	}

	public HisMObject_StateHisDao(DaoConfig config, XJHisDataBaseSession daoSession) {
		super(config, daoSession);
	}

	*//** Creates the underlying database table. *//*
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "MOBJECT_STATE (" + //
				"LINE_ID nvarchar NOT NULL ," + // 0:Line_ID
				"STARTANDENDPOINT_ID nvarchar(400) NOT NULL ," + // 1:
																	// StartAndEndPoint_ID
				"COMPLETETIME_DT datetime NOT NULL ," + // 2: CompleteTime_DT
				"CONTROLPOINTDESC_TX datetime NOT NULL ," + // 3: ControlPointDesc_TX												
				"MOBJECTSTATE_CD nvarchar(20)," + // 3: MObjectState_CD
				"MOBJECTSTATENAME_TX nvarchar(20) NOT NULL ," + // 4:
																// MObjectStateName_TX
				"APPUSER_CD nvarchar(30)," + // 5: AppUser_CD
				"DATASTATE_YN nvarchar(1)," + // 6: DataState_YN
				"TRANSINFO_TX nvarchar(500));"); // 7: TransInfo_TX
	}

	*//** Drops the underlying database table. *//*
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'MOBJECT_STATE'";
		db.execSQL(sql);
	}

	*//** @inheritdoc *//*
	@Override
	protected void bindValues(SQLiteStatement stmt, MObject_State entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getLine_ID());
		stmt.bindString(2, entity.getStartAndEndPoint_ID());
		stmt.bindString(3, entity.getCompleteTime_DT());
		stmt.bindString(4, entity.getControlPointDesc_TX());
		String MObjectState_CD = entity.getMObjectState_CD();
		if (MObjectState_CD != null) {
			stmt.bindString(5, MObjectState_CD);
		}
		stmt.bindString(6, entity.getMObjectStateName_TX());

		String AppUser_CD = entity.getAppUser_CD();
		if (AppUser_CD != null) {
			stmt.bindString(7, AppUser_CD);
		}

		String DataState_YN = entity.getDataState_YN();
		if (DataState_YN != null) {
			stmt.bindString(8, DataState_YN);
		}

		String TransInfo_TX = entity.getTransInfo_TX();
		if (TransInfo_TX != null) {
			stmt.bindString(9, TransInfo_TX);
		}
	}

	*//** @inheritdoc *//*
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	*//** @inheritdoc *//*
	@Override
	public MObject_State readEntity(Cursor cursor, int offset) {
		MObject_State entity = new MObject_State(
				//
				cursor.getString(offset + 0),//Line_ID
				cursor.getString(offset + 1), // StartAndEndPoint_ID
				cursor.getString(offset + 2), // CompleteTime_DT
				cursor.getString(offset + 3), // ControlPointDesc_TX
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 2), // MObjectState_CD
				cursor.getString(offset + 5), // MObjectStateName_TX
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 4), // AppUser_CD
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 5), // DataState_YN
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 6) // TransInfo_TX
		);
		return entity;
	}

	*//** @inheritdoc *//*
	@Override
	public void readEntity(Cursor cursor, MObject_State entity, int offset) {
		entity.setLine_ID(cursor.getString(offset + 0));
		entity.setStartAndEndPoint_ID(cursor.getString(offset + 1));
		entity.setCompleteTime_DT(cursor.getString(offset + 2));
		entity.setControlPointDesc_TX(cursor.getString(offset + 3));
		entity.setMObjectState_CD(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 2));
		entity.setMObjectStateName_TX(cursor.getString(offset + 5));
		entity.setAppUser_CD(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 4));
		entity.setDataState_YN(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 5));
		entity.setTransInfo_TX(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 6));
	}

	*//** @inheritdoc *//*
	@Override
	protected Void updateKeyAfterInsert(MObject_State entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	*//** @inheritdoc *//*
	@Override
	public Void getKey(MObject_State entity) {
		return null;
	}

	*//** @inheritdoc *//*
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	*//**
	 * 加载数据（供实时上传巡点检启停信息数据）
	 * 
	 * @param loadcount
	 * @return
	 *//*
	public List<MObject_State> loadforUploadJIT(int loadcount) {
		String sqlString = "Select * from MObject_State Where 1=1 order by CompleteTime_DT asc  limit 0,"
				+ loadcount;
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public List<MObject_State> loadforUploadJITCaseXJ(String con_line_id,
			String time_id) {
		String sqlString = "Select * from MObject_State Where TransInfo_TX like '"
				+ con_line_id
				+ "|%|"
				+ time_id
				+ "' order by CompleteTime_DT asc";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	*//**
	 * 删除一组已上传数据
	 * 
	 * @param uploadedData
	 *//*
	public void deleteUploadedData(List<MObject_State> uploadedData) {
		for (MObject_State mobjectState : uploadedData) {
			deleteDatabyCondition(mobjectState.getStartAndEndPoint_ID(),
					mobjectState.getCompleteTime_DT());
		}
	}

	public void deleteDatabyCondition(String _id, String _utcdatetime) {
		String sql = "Delete from MObject_State where StartAndEndPoint_ID = "
				+ _id + " And CompleteTime_DT = '" + _utcdatetime + "'";
		db.execSQL(sql);
	}
}
*/