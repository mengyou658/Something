package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class XJ_TaskIDPosHisDao extends AbstractDao<XJ_TaskIDPosHis, Void> {
	public static final String TABLENAME = "DJ_TASKIDPOSHIS";

	public XJ_TaskIDPosHisDao(DaoConfig config) {
		super(config);
	}

	public XJ_TaskIDPosHisDao(DaoConfig config, XJHisDataBaseSession daoSession) {
		super(config, daoSession);
	}

	public static class Properties {
		public final static Property Line_ID = new Property(0, Integer.class,
				"Line_ID", false, "LINE_ID");
		public final static Property IDPos_ID = new Property(1, String.class,
				"IDPos_ID", false, "IDPOS_ID");
		public final static Property IDPosStart_TM = new Property(2,
				String.class, "IDPosStart_TM", false, "IDPOSSTART_TM");
		public final static Property IDPosDesc_TX = new Property(3,
				String.class, "IDPosDesc_TX", false, "IDPOSDESC_TX");
		public final static Property IDPosEnd_TM = new Property(4,
				String.class, "IDPosEnd_TM", false, "IDPOSEND_TM");
		public final static Property Query_DT = new Property(5, String.class,
				"Query_DT", false, "QUERY_DT");
		public final static Property AppUser_CD = new Property(6, String.class,
				"AppUser_CD", false, "APPUSER_CD");
		public final static Property AppUserName_TX = new Property(7,
				String.class, "AppUserName_TX", false, "APPUSERNAME_TX");
		public final static Property ShiftName_TX = new Property(8,
				String.class, "ShiftName_TX", false, "SHIFTNAME_TX");
		public final static Property ShiftGroupName_TX = new Property(9,
				String.class, "ShiftGroupName_TX", false, "SHIFTGROUPNAME_TX");
		public final static Property TimeCount_NR = new Property(10,
				Integer.class, "TimeCount_NR", false, "TIMECOUNT_NR");
		public final static Property TransInfo_TX = new Property(11,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
		public final static Property GPSInfo_TX = new Property(12,
				String.class, "GPSInfo_TX", false, "GPSINFO_TX");
	};

	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "DJ_TASKIDPOSHIS (" + //
				"Line_ID INTEGER NOT NULL," + // 0: Line_ID
				"IDPOS_ID nvarchar(400) NOT NULL ," + // 1: IDPos_ID
				"IDPOSSTART_TM datetime NOT NULL ," + // 2: IDPosStart_TM
				"IDPosDesc_TX nvarchar(400) NOT NULL," + // 3: IDPosDesc_TX
				"IDPOSEND_TM datetime," + // 4: IDPosEnd_TM
				"QUERY_DT datetime," + // 5: Query_DT
				"APPUSER_CD nvarchar(30) NOT NULL ," + // 6: AppUser_CD
				"APPUSERNAME_TX nvarchar(20)," + // 7: AppUserName_TX
				"ShiftName_TX nvarchar(6)," + // 8: ShiftName_TX
				"ShiftGroupName_TX nvarchar(6)," + // 9: ShiftGroupName_TX
				"TimeCount_NR INTEGER NOT NULL ," + // 10: TimeCount_NR
				"TransInfo_TX nvarchar(500)," + // 11: TransInfo_TX
				"GPSInfo_TX nvarchar(60));"); // 12: GPSInfo_TX
	}

	@Override
	protected void bindValues(SQLiteStatement stmt, XJ_TaskIDPosHis entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getLine_ID());
		stmt.bindString(2, entity.getIDPos_ID());
		stmt.bindString(3, entity.getIDPosStart_TM());
		stmt.bindString(4, entity.getIDPosDesc_TX());
		String IDPosEnd_TM = entity.getIDPosEnd_TM();
		if (IDPosEnd_TM != null) {
			stmt.bindString(5, IDPosEnd_TM);
		}

		String Query_DT = entity.getQuery_DT();
		if (Query_DT != null) {
			stmt.bindString(6, Query_DT);
		}
		stmt.bindString(7, entity.getAppUser_CD());

		String AppUserName_TX = entity.getAppUserName_TX();
		if (AppUserName_TX != null) {
			stmt.bindString(8, AppUserName_TX);
		}

		String ShiftName_TX = entity.getShiftName_TX();
		if (ShiftName_TX != null) {
			stmt.bindString(9, ShiftName_TX);
		}

		String ShiftGroupName_TX = entity.getShiftGroupName_TX();
		if (ShiftGroupName_TX != null) {
			stmt.bindString(10, ShiftGroupName_TX);
		}

		Integer TimeCount_NR = entity.getTimeCount_NR();
		if (TimeCount_NR != null) {
			stmt.bindLong(11, TimeCount_NR);
		}

		String TransInfo_TX = entity.getTransInfo_TX();
		if (TransInfo_TX != null) {
			stmt.bindString(12, TransInfo_TX);
		}
		String GPSInfo_TX = entity.getGPSInfo_TX();
		if (GPSInfo_TX != null) {
			stmt.bindString(13, GPSInfo_TX);
		}
	}

	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'DJ_TASKIDPOSHIS'";
		db.execSQL(sql);
	}

	@Override
	protected Void getKey(XJ_TaskIDPosHis arg0) {
		return null;
	}

	@Override
	protected boolean isEntityUpdateable() {
		return false;
	}

	@Override
	protected XJ_TaskIDPosHis readEntity(Cursor cursor, int offset) {
		XJ_TaskIDPosHis entity = new XJ_TaskIDPosHis(
				//
				cursor.getString(offset + 0), // Line_ID
				cursor.getString(offset + 1), // IDPos_ID
				cursor.getString(offset + 2), // IDPosStart_TM
				cursor.getString(offset + 3), // IDPosDesc_TX
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // IDPosEnd_TM
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Query_DT
				cursor.getString(offset + 6), // AppUser_CD
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // AppUserName_TX
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ShiftName_TX
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // ShiftGroupName_TX
				cursor.getInt(offset + 10), // TimeCount_NR
				cursor.isNull(offset + 11) ? null : cursor
						.getString(offset + 11), // TransInfo_TX
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12) // GPSInfo_TX
		);
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, XJ_TaskIDPosHis entity, int offset) {
		entity.setLine_ID(cursor.getString(offset + 0));
		entity.setIDPos_ID(cursor.getString(offset + 1));
		entity.setIDPosStart_TM(cursor.getString(offset + 2));
		entity.setIDPosDesc_TX(cursor.getString(offset + 3));
		entity.setIDPosEnd_TM(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setQuery_DT(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setAppUser_CD(cursor.getString(offset + 6));
		entity.setAppUserName_TX(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setShiftName_TX(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setShiftGroupName_TX(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setTimeCount_NR(cursor.isNull(offset + 10) ? null : cursor
				.getInt(offset + 10));
		entity.setTransInfo_TX(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		entity.setGPSInfo_TX(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
	}

	@Override
	protected Void readKey(Cursor arg0, int arg1) {
		return null;
	}

	@Override
	protected Void updateKeyAfterInsert(XJ_TaskIDPosHis arg0, long arg1) {
		return null;
	}

	/**
	 * 更新钮扣到位结果结束时间(历史表)
	 * 
	 * @param entity
	 */
	public void UpdateDJIDPosHis(XJ_TaskIDPosHis entity) {
		String sqlStr = "Update DJ_TASKIDPOSHIS Set IDPOSEND_TM = '"
				+ entity.getIDPosEnd_TM() + "', TimeCount_NR = '"
				+ entity.getTimeCount_NR() + "' Where Line_ID = "
				+ entity.getLine_ID() + " And IDPOS_ID = '"
				+ entity.getIDPos_ID() + "' And IDPOSSTART_TM = '"
				+ entity.getIDPosStart_TM() + "'";
		db.execSQL(sqlStr);
	}

	/**
	 * 获取到位历史数据
	 * 
	 * @param lineID
	 * @param idposID
	 * @return
	 */
	public List<XJ_TaskIDPosHis> getDJIDPosHisDataByLineID(String lineID,
			String idposID) {
		String sqlStr = "Select * From DJ_TASKIDPOSHIS Where Line_ID = "
				+ lineID + " And IDPos_ID = '" + idposID + "'";
		Cursor cursor = db.rawQuery(sqlStr, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 获取到位历史数据
	 * 
	 * @param lineID
	 * @return
	 */
	public List<XJ_TaskIDPosHis> getDJHisDataByLineID(int lineID) {
		String sqlStr = "select * from DJ_TASKIDPOSHIS WHERE  Line_ID='"
				+ lineID + "'";
		Cursor cursor = db.rawQuery(sqlStr, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 获取到位历史纽扣
	 * 
	 * @param lineID
	 * @return
	 */
/*	public List<XJ_TaskIDPosHis> getDJHisNKByLineID(int lineID) {
		String sqlStr = "select distinct IDPosDesc_TX from DJ_TASKIDPOSHIS WHERE Line_ID='"
				+ lineID + "'";
		Cursor cursor = db.rawQuery(sqlStr, null);
		return loadAllAndCloseCursor(cursor);
	}*/

	/**
	 * 根据时间和路线id查询历史到位结果
	 * 
	 * @param startTime
	 * @param endTime
	 * @param lineID
	 * @return
	 */
	public List<XJ_TaskIDPosHis> loadTaskResultHisByTime(String startTime,
			String endTime, int lineID) {
		String sqlString = "select * from DJ_TASKIDPOSHIS WHERE Query_DT>='"
				+ startTime + "' and Query_DT<='" + endTime + "' and Line_ID='"
				+ lineID + "'";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}
}