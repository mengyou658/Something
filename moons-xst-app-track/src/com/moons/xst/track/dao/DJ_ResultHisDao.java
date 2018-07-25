package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.DJ_ResultHis;
import com.moons.xst.track.common.DateTimeHelper;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table DJ_RESULT_HIS.
 */
public class DJ_ResultHisDao extends AbstractDao<DJ_ResultHis, Void> {

	public static final String TABLENAME = "DJ_RESULTHIS";

	/**
	 * Properties of entity DJ_ResultHis.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property DJ_Plan_ID = new Property(0, String.class,
				"DJ_Plan_ID", false, "DJ_PLAN_ID");
		public final static Property CompleteTime_DT = new Property(1,
				String.class, "CompleteTime_DT", false, "COMPLETETIME_DT");
		public final static Property AppUser_CD = new Property(2, String.class,
				"AppUser_CD", false, "APPUSER_CD");
		public final static Property AppUserName_TX = new Property(3,
				String.class, "AppUserName_TX", false, "APPUSERNAME_TX");
		public final static Property Result_TX = new Property(4, String.class,
				"Result_TX", false, "RESULT_TX");
		public final static Property Data8K_TX = new Property(5, byte[].class,
				"Data8K_TX", false, "DATA8K_TX");
		public final static Property Query_DT = new Property(6, String.class,
				"Query_DT", false, "QUERY_DT");
		public final static Property MObjectStateName_TX = new Property(7,
				String.class, "MObjectStateName_TX", false,
				"MOBJECTSTATENAME_TX");
		public final static Property Exception_YN = new Property(8,
				String.class, "Exception_YN", false, "EXCEPTION_YN");
		public final static Property AlarmLevel_ID = new Property(9,
				Integer.class, "AlarmLevel_ID", false, "ALARMLEVEL_ID");
		public final static Property SpecCase_YN = new Property(10,
				String.class, "SpecCase_YN", false, "SPECCASE_YN");
		public final static Property SpecCase_TX = new Property(11,
				String.class, "SpecCase_TX", false, "SPECCASE_TX");
		public final static Property Memo_TX = new Property(12, String.class,
				"Memo_TX", false, "MEMO_TX");
		public final static Property Time_NR = new Property(13, int.class,
				"Time_NR", false, "TIME_NR");
		public final static Property DataFlag_CD = new Property(14,
				String.class, "DataFlag_CD", false, "DATAFLAG_CD");
		public final static Property ShiftName_TX = new Property(15,
				String.class, "ShiftName_TX", false, "SHIFTNAME_TX");
		public final static Property ShiftGroupName_TX = new Property(16,
				String.class, "ShiftGroupName_TX", false, "SHIFTGROUPNAME_TX");
		public final static Property Ratio_NR = new Property(17, Boolean.class,
				"Ratio_NR", false, "RATIO_NR");
		public final static Property Ratio1_NR = new Property(18, String.class,
				"Ratio1_NR", false, "RATIO1_NR");
		public final static Property Rate = new Property(19, Integer.class,
				"Rate", false, "RATE");
		public final static Property DataLen = new Property(20, Integer.class,
				"DataLen", false, "DATALEN");
		public final static Property LastResult_TX = new Property(21,
				String.class, "LastResult_TX", false, "LASTRESULT_TX");
		public final static Property TransInfo_TX = new Property(22,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
		public final static Property VibParam_TX = new Property(23,
				String.class, "VibParam_TX", false, "VIBPARAM_TX");
		public final static Property FeatureValue_TX = new Property(24,
				String.class, "FeatureValue_TX", false, "FEATUREVALUE_TX");
		public final static Property GPSInfo_TX = new Property(25,
				String.class, "GPSInfo_TX", false, "GPSINFO_TX");
	};

	public DJ_ResultHisDao(DaoConfig config) {
		super(config);
	}

	public DJ_ResultHisDao(DaoConfig config, DJDAOSession daoSession) {
		super(config, daoSession);
	}

	public DJ_ResultHisDao(DaoConfig dJ_ResultHisDaoConfig,
			XJHisDataBaseSession xjHisDataBaseSession) {
		super(dJ_ResultHisDaoConfig, xjHisDataBaseSession);
	}

	/** Creates the underlying database table. */
//	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
//		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
//		db.execSQL("CREATE TABLE " + constraint + "'DJ_RESULTHIS' (" + //
//				"'DJ_PLAN_ID' TEXT NOT NULL ," + // 0: DJ_Plan_ID
//				"'COMPLETE_TIME_DT' TEXT NOT NULL ," + // 1: CompleteTime_DT
//				"'APP_USER_CD' TEXT NOT NULL ," + // 2: AppUser_CD
//				"'APP_USER_NAME_TX' TEXT," + // 3: AppUserName_TX
//				"'RESULT_TX' TEXT NOT NULL ," + // 4: Result_TX
//				"'DATA8_K_TX' BLOB," + // 5: Data8K_TX
//				"'QUERY_DT' TEXT NOT NULL ," + // 6: Query_DT
//				"'MOBJECT_STATE_NAME_TX' TEXT," + // 7: MObjectStateName_TX
//				"'EXCEPTION_YN' TEXT NOT NULL ," + // 8: Exception_YN
//				"'ALARM_LEVEL_ID' INTEGER," + // 9: AlarmLevel_ID
//				"'SPEC_CASE_YN' TEXT NOT NULL ," + // 10: SpecCase_YN
//				"'SPEC_CASE_TX' TEXT," + // 11: SpecCase_TX
//				"'MEMO_TX' TEXT," + // 12: Memo_TX
//				"'TIME_NR' INTEGER NOT NULL ," + // 13: Time_NR
//				"'DATA_FLAG_CD' TEXT NOT NULL ," + // 14: DataFlag_CD
//				"'SHIFT_NAME_TX' TEXT," + // 15: ShiftName_TX
//				"'SHIFT_GROUP_NAME_TX' TEXT," + // 16: ShiftGroupName_TX
//				"'RATIO_NR' INTEGER," + // 17: Ratio_NR
//				"'RATIO1_NR' TEXT," + // 18: Ratio1_NR
//				"'RATE' INTEGER," + // 19: Rate
//				"'DATA_LEN' INTEGER," + // 20: DataLen
//				"'LAST_RESULT_TX' TEXT," + // 21: LastResult_TX
//				"'TRANS_INFO_TX' TEXT," + // 22: TransInfo_TX
//				"'VIB_PARAM_TX' TEXT," + // 23: VibParam_TX
//				"'FEATURE_VALUE_TX' TEXT," + // 24: FeatureValue_TX
//				"'GPSINFO_TX' TEXT);"); // 25: GPSInfo_TX
//	}
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'DJ_RESULTHIS' (" + //
				"'DJ_Plan_ID' TEXT NOT NULL ," + // 0: DJ_Plan_ID
				"'CompleteTime_DT' TEXT NOT NULL ," + // 1: CompleteTime_DT
				"'AppUser_CD' TEXT NOT NULL ," + // 2: AppUser_CD
				"'AppUserName_TX' TEXT," + // 3: AppUserName_TX
				"'Result_TX' TEXT NOT NULL ," + // 4: Result_TX
				"'Data8K_TX' BLOB," + // 5: Data8K_TX
				"'Query_DT' TEXT NOT NULL ," + // 6: Query_DT
				"'MObjectStateName_TX' TEXT," + // 7: MObjectStateName_TX
				"'Exception_YN' TEXT NOT NULL ," + // 8: Exception_YN
				"'AlarmLevel_ID' INTEGER," + // 9: AlarmLevel_ID
				"'SpecCase_YN' TEXT NOT NULL ," + // 10: SpecCase_YN
				"'SpecCase_TX' TEXT," + // 11: SpecCase_TX
				"'Memo_TX' TEXT," + // 12: Memo_TX
				"'Time_NR' INTEGER NOT NULL ," + // 13: Time_NR
				"'DataFlag_CD' TEXT NOT NULL ," + // 14: DataFlag_CD
				"'ShiftName_TX' TEXT," + // 15: ShiftName_TX
				"'ShiftGroupName_TX' TEXT," + // 16: ShiftGroupName_TX
				"'Ratio_NR' INTEGER," + // 17: Ratio_NR
				"'Ratio1_NR' TEXT," + // 18: Ratio1_NR
				"'Rate' INTEGER," + // 19: Rate
				"'DataLen' INTEGER," + // 20: DataLen
				"'LastResult_TX' TEXT," + // 21: LastResult_TX
				"'TransInfo_TX' TEXT," + // 22: TransInfo_TX
				"'VibParam_TX' TEXT," + // 23: VibParam_TX
				"'FeatureValue_TX' TEXT," + // 24: FeatureValue_TX
				"'GPSInfo_TX' TEXT);"); // 25: GPSInfo_TX
	}
	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'DJ_RESULTHIS'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, DJ_ResultHis entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getDJ_Plan_ID());
		stmt.bindString(2, entity.getCompleteTime_DT());
		stmt.bindString(3, entity.getAppUser_CD());

		String AppUserName_TX = entity.getAppUserName_TX();
		if (AppUserName_TX != null) {
			stmt.bindString(4, AppUserName_TX);
		}
		stmt.bindString(5, entity.getResult_TX());

		byte[] Data8K_TX = entity.getData8K_TX();
		if (Data8K_TX != null) {
			stmt.bindBlob(6, Data8K_TX);
		}
		stmt.bindString(7, entity.getQuery_DT());

		String MObjectStateName_TX = entity.getMObjectStateName_TX();
		if (MObjectStateName_TX != null) {
			stmt.bindString(8, MObjectStateName_TX);
		}
		stmt.bindString(9, entity.getException_YN());

		Integer AlarmLevel_ID = entity.getAlarmLevel_ID();
		if (AlarmLevel_ID != null) {
			stmt.bindLong(10, AlarmLevel_ID);
		}
		stmt.bindString(11, entity.getSpecCase_YN());

		String SpecCase_TX = entity.getSpecCase_TX();
		if (SpecCase_TX != null) {
			stmt.bindString(12, SpecCase_TX);
		}

		String Memo_TX = entity.getMemo_TX();
		if (Memo_TX != null) {
			stmt.bindString(13, Memo_TX);
		}
		stmt.bindLong(14, entity.getTime_NR());
		stmt.bindString(15, entity.getDataFlag_CD());

		String ShiftName_TX = entity.getShiftName_TX();
		if (ShiftName_TX != null) {
			stmt.bindString(16, ShiftName_TX);
		}

		String ShiftGroupName_TX = entity.getShiftGroupName_TX();
		if (ShiftGroupName_TX != null) {
			stmt.bindString(17, ShiftGroupName_TX);
		}

		Boolean Ratio_NR = entity.getRatio_NR();
		if (Ratio_NR != null) {
			stmt.bindLong(18, Ratio_NR ? 1l : 0l);
		}

		String Ratio1_NR = entity.getRatio1_NR();
		if (Ratio1_NR != null) {
			stmt.bindString(19, Ratio1_NR);
		}

		Integer Rate = entity.getRate();
		if (Rate != null) {
			stmt.bindLong(20, Rate);
		}

		Integer DataLen = entity.getDataLen();
		if (DataLen != null) {
			stmt.bindLong(21, DataLen);
		}

		String LastResult_TX = entity.getLastResult_TX();
		if (LastResult_TX != null) {
			stmt.bindString(22, LastResult_TX);
		}

		String TransInfo_TX = entity.getTransInfo_TX();
		if (TransInfo_TX != null) {
			stmt.bindString(23, TransInfo_TX);
		}

		String VibParam_TX = entity.getVibParam_TX();
		if (VibParam_TX != null) {
			stmt.bindString(24, VibParam_TX);
		}

		String FeatureValue_TX = entity.getFeatureValue_TX();
		if (FeatureValue_TX != null) {
			stmt.bindString(25, FeatureValue_TX);
		}

		String GPSInfo_TX = entity.getGPSInfo_TX();
		if (GPSInfo_TX != null) {
			stmt.bindString(26, GPSInfo_TX);
		}
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public DJ_ResultHis readEntity(Cursor cursor, int offset) {
		DJ_ResultHis entity = new DJ_ResultHis(
				//
				cursor.getString(offset + 0), // DJ_Plan_ID
				cursor.getString(offset + 1), // CompleteTime_DT
				cursor.getString(offset + 2), // AppUser_CD
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // AppUserName_TX
				cursor.getString(offset + 4), // Result_TX
				cursor.isNull(offset + 5) ? null : cursor.getBlob(offset + 5), // Data8K_TX
				cursor.getString(offset + 6), // Query_DT
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // MObjectStateName_TX
				cursor.getString(offset + 8), // Exception_YN
				cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // AlarmLevel_ID
				cursor.getString(offset + 10), // SpecCase_YN
				cursor.isNull(offset + 11) ? null : cursor
						.getString(offset + 11), // SpecCase_TX
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12), // Memo_TX
				cursor.getInt(offset + 13), // Time_NR
				cursor.getString(offset + 14), // DataFlag_CD
				cursor.isNull(offset + 15) ? null : cursor
						.getString(offset + 15), // ShiftName_TX
				cursor.isNull(offset + 16) ? null : cursor
						.getString(offset + 16), // ShiftGroupName_TX
				cursor.isNull(offset + 17) ? null : cursor
						.getShort(offset + 17) != 0, // Ratio_NR
				cursor.isNull(offset + 18) ? null : cursor
						.getString(offset + 18), // Ratio1_NR
				cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // Rate
				cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20), // DataLen
				cursor.isNull(offset + 21) ? null : cursor
						.getString(offset + 21), // LastResult_TX
				cursor.isNull(offset + 22) ? null : cursor
						.getString(offset + 22), // TransInfo_TX
				cursor.isNull(offset + 23) ? null : cursor
						.getString(offset + 23), // VibParam_TX
				cursor.isNull(offset + 24) ? null : cursor
						.getString(offset + 24), // FeatureValue_TX
				cursor.isNull(offset + 25) ? null : cursor
						.getString(offset + 25) // GPSInfo_TX
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, DJ_ResultHis entity, int offset) {
		entity.setDJ_Plan_ID(cursor.getString(offset + 0));
		entity.setCompleteTime_DT(cursor.getString(offset + 1));
		entity.setAppUser_CD(cursor.getString(offset + 2));
		entity.setAppUserName_TX(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setResult_TX(cursor.getString(offset + 4));
		entity.setData8K_TX(cursor.isNull(offset + 5) ? null : cursor
				.getBlob(offset + 5));
		entity.setQuery_DT(cursor.getString(offset + 6));
		entity.setMObjectStateName_TX(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setException_YN(cursor.getString(offset + 8));
		entity.setAlarmLevel_ID(cursor.isNull(offset + 9) ? null : cursor
				.getInt(offset + 9));
		entity.setSpecCase_YN(cursor.getString(offset + 10));
		entity.setSpecCase_TX(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		entity.setMemo_TX(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
		entity.setTime_NR(cursor.getInt(offset + 13));
		entity.setDataFlag_CD(cursor.getString(offset + 14));
		entity.setShiftName_TX(cursor.isNull(offset + 15) ? null : cursor
				.getString(offset + 15));
		entity.setShiftGroupName_TX(cursor.isNull(offset + 16) ? null : cursor
				.getString(offset + 16));
		entity.setRatio_NR(cursor.isNull(offset + 17) ? null : cursor
				.getShort(offset + 17) != 0);
		entity.setRatio1_NR(cursor.isNull(offset + 18) ? null : cursor
				.getString(offset + 18));
		entity.setRate(cursor.isNull(offset + 19) ? null : cursor
				.getInt(offset + 19));
		entity.setDataLen(cursor.isNull(offset + 20) ? null : cursor
				.getInt(offset + 20));
		entity.setLastResult_TX(cursor.isNull(offset + 21) ? null : cursor
				.getString(offset + 21));
		entity.setTransInfo_TX(cursor.isNull(offset + 22) ? null : cursor
				.getString(offset + 22));
		entity.setVibParam_TX(cursor.isNull(offset + 23) ? null : cursor
				.getString(offset + 23));
		entity.setFeatureValue_TX(cursor.isNull(offset + 24) ? null : cursor
				.getString(offset + 24));
		entity.setGPSInfo_TX(cursor.isNull(offset + 25) ? null : cursor
				.getString(offset + 25));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(DJ_ResultHis entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(DJ_ResultHis entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<DJ_ResultHis> loadOneDJResultHis(String planID,
			String startTime, String endTime) {
		String sqlString = "select * from DJ_ResultHis where DJ_PLAN_ID='"
				+ planID + "' " + "and COMPLETETIME_DT >='" + startTime
				+ "' and COMPLETETIME_DT <= '" + endTime + "'"
				+ " and SPECCASE_TX = ''";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}
	
	public List<DJ_ResultHis> loadOneDJResultHisForCaseXJ(String planID, String conditionStr) {
		String sqlString = "select * from DJ_ResultHis where DJ_PLAN_ID = '"
				+ planID + "' and TransInfo_TX like '%" 
				+ conditionStr + "%'";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public List<DJ_ResultHis> loadDJResultHisByPlanID(String planID) {
		String sqlString = "select * from DJ_ResultHis where DJ_PLAN_ID='"
				+ planID + "' order by QUERY_DT desc ";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public synchronized List<DJ_ResultHis> loadDJResultHisByCompleteTime() {
		String sqlString = "select * from DJ_ResultHis asc";
		Cursor cursor = db.rawQuery(sqlString, null);
		int a=cursor.getCount();
		return loadAllAndCloseCursor(cursor);
	}
	
	public void updateDJResultHis(DJ_ResultActive entity, DJPlan mDjPlan) {
		String sqlString = "update DJ_ResultHis set COMPLETETIME_DT ='"
				+ entity.getCompleteTime_DT()
				+ "',RESULT_TX ='"
				+ entity.getResult_TX()
				+ "',LASTRESULT_TX ='"
				+ entity.getLastResult_TX()
				+ "',MEMO_TX ='"
				+ entity.getMemo_TX()
				+ "',EXCEPTION_YN ='"
				+ entity.getException_YN()
				+ "',ALARMLEVEL_ID ='"
				+ entity.getAlarmLevel_ID()
				+ "' where DJ_PLAN_ID='"
				+ mDjPlan.getDJPlan().getDJ_Plan_ID()
				+ "' "
				+ "and COMPLETETIME_DT >='"
				+ DateTimeHelper
						.DateToString(mDjPlan.GetCycle().getTaskBegin())
				+ "' and COMPLETETIME_DT <= '"
				+ DateTimeHelper.DateToString(mDjPlan.GetCycle().getTaskEnd())
				+ "'" +" and SPECCASE_TX = ''";
		db.execSQL(sqlString);
	}
	
	public void updateDJResultHisForCaseXJ(DJ_ResultActive entity, DJPlan mDjPlan, String mConditionStr) {
		String sqlString = "update DJ_ResultHis set COMPLETETIME_DT ='"
				+ entity.getCompleteTime_DT()
				+ "',RESULT_TX ='"
				+ entity.getResult_TX()
				+ "',LASTRESULT_TX ='"
				+ entity.getLastResult_TX()
				+ "',MEMO_TX ='"
				+ entity.getMemo_TX()
				+ "',EXCEPTION_YN ='"
				+ entity.getException_YN()
				+ "',ALARMLEVEL_ID ='"
				+ entity.getAlarmLevel_ID()
				+ "' where DJ_PLAN_ID='"
				+ mDjPlan.getDJPlan().getDJ_Plan_ID()
				+ "' and TransInfo_TX like '%" 
				+ mConditionStr + "%'";
		db.execSQL(sqlString);
	}
	public void clearHisData(){
		String sqlString = "Delete from DJ_ResultHis";
//		db.delete(DJ_ResultHis, null, null);
		db.execSQL(sqlString);
	}
}