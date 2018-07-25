package com.moons.xst.track.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class XJ_ResultHisDao extends AbstractDao<XJ_ResultHis, Void> {

	public static final String TABLENAME = "DJ_RESULTHIS";

	/**
	 * Properties of entity DJ_ResultHis.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {

		public final static Property Line_ID = new Property(0, Integer.class,
				"Line_ID", false, "LINE_ID");
		public final static Property DJ_Plan_ID = new Property(1, String.class,
				"DJ_Plan_ID", false, "DJ_PLAN_ID");
		public final static Property CompleteTime_DT = new Property(2,
				String.class, "CompleteTime_DT", false, "COMPLETETIME_DT");
		public final static Property PlanDesc_TX = new Property(3,
				String.class, "PlanDesc_TX", false, "PLANDESC_TX");
		public final static Property IDPosName_TX = new Property(4,
				String.class, "IDPosName_TX", false, "IDPOSNAME_TX");
		public final static Property AppUser_CD = new Property(5, String.class,
				"AppUser_CD", false, "APPUSER_CD");
		public final static Property AppUserName_TX = new Property(6,
				String.class, "AppUserName_TX", false, "APPUSERNAME_TX");
		public final static Property Result_TX = new Property(7, String.class,
				"Result_TX", false, "RESULT_TX");
		public final static Property Data8K_TX = new Property(8, byte[].class,
				"Data8K_TX", false, "DATA8K_TX");
		public final static Property Query_DT = new Property(9, String.class,
				"Query_DT", false, "QUERY_DT");
		public final static Property MObjectStateName_TX = new Property(10,
				String.class, "MObjectStateName_TX", false,
				"MOBJECTSTATENAME_TX");
		public final static Property Exception_YN = new Property(11,
				String.class, "Exception_YN", false, "EXCEPTION_YN");
		public final static Property AlarmLevel_ID = new Property(12,
				Integer.class, "AlarmLevel_ID", false, "ALARMLEVEL_ID");
		public final static Property SpecCase_YN = new Property(13,
				String.class, "SpecCase_YN", false, "SPECCASE_YN");
		public final static Property SpecCase_TX = new Property(14,
				String.class, "SpecCase_TX", false, "SPECCASE_TX");
		public final static Property Memo_TX = new Property(15, String.class,
				"Memo_TX", false, "MEMO_TX");
		public final static Property Time_NR = new Property(16, int.class,
				"Time_NR", false, "TIME_NR");
		public final static Property DataFlag_CD = new Property(17,
				String.class, "DataFlag_CD", false, "DATAFLAG_CD");
		public final static Property ShiftName_TX = new Property(18,
				String.class, "ShiftName_TX", false, "SHIFTNAME_TX");
		public final static Property ShiftGroupName_TX = new Property(19,
				String.class, "ShiftGroupName_TX", false, "SHIFTGROUPNAME_TX");
		public final static Property Ratio_NR = new Property(20, Boolean.class,
				"Ratio_NR", false, "RATIO_NR");
		public final static Property Ratio1_NR = new Property(21, String.class,
				"Ratio1_NR", false, "RATIO1_NR");
		public final static Property Rate = new Property(22, Integer.class,
				"Rate", false, "RATE");
		public final static Property DataLen = new Property(23, Integer.class,
				"DataLen", false, "DATALEN");
		public final static Property LastResult_TX = new Property(24,
				String.class, "LastResult_TX", false, "LASTRESULT_TX");
		public final static Property TransInfo_TX = new Property(25,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
		public final static Property VibParam_TX = new Property(26,
				String.class, "VibParam_TX", false, "VIBPARAM_TX");
		public final static Property FeatureValue_TX = new Property(27,
				String.class, "FeatureValue_TX", false, "FEATUREVALUE_TX");
		public final static Property GPSInfo_TX = new Property(28,
				String.class, "GPSInfo_TX", false, "GPSINFO_TX");
		public final static Property DataType_CD = new Property(29,
				String.class, "DataType_CD", false, "DATATYPE_CD");
		public final static Property ZhenDong_Type = new Property(30,
				String.class, "ZhenDong_Type", false, "ZHENDONG_TYPE");
		public final static Property FFTDATA_TX = new Property(31,
				byte[].class, "FFTDATA_TX", false, "FFTDATA_TX");
		public final static Property TransException_YN = new Property(32,
				String.class, "TransException_YN", false, "TransException_YN");
		public final static Property AlarmDesc_TX = new Property(33,
				String.class, "AlarmDesc_TX", false, "AlarmDesc_TX");
		public final static Property NoticeType_TX = new Property(34,
				String.class, "NoticeType_TX", false, "NoticeType_TX");
	};

	public XJ_ResultHisDao(DaoConfig config) {
		super(config);
	}

	public XJ_ResultHisDao(DaoConfig XJ_ResultHisDaoConfig,
			XJHisDataBaseSession xjHisDataBaseSession) {
		super(XJ_ResultHisDaoConfig, xjHisDataBaseSession);
	}

	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'DJ_RESULTHIS' ("
				+ //
				"'Line_ID' TEXT NOT NULL ,"
				+ // 0: Line_ID
				"'DJ_Plan_ID' TEXT NOT NULL ,"
				+ // 1: DJ_Plan_ID
				"'CompleteTime_DT' TEXT NOT NULL ,"
				+ // 2: CompleteTime_DT
				"'PlanDesc_TX' TEXT NOT NULL ,"
				+ // 3: PlanDesc_TX
				"'IDPosName_TX' TEXT NOT NULL ,"
				+ // 4: IDPosName_TX
				"'AppUser_CD' TEXT NOT NULL ,"
				+ // 5: AppUser_CD
				"'AppUserName_TX' TEXT,"
				+ // 6: AppUserName_TX
				"'Result_TX' TEXT NOT NULL ,"
				+ // 7: Result_TX
				"'Data8K_TX' BLOB,"
				+ // 8: Data8K_TX
				"'Query_DT' TEXT NOT NULL ,"
				+ // 9: Query_DT
				"'MObjectStateName_TX' TEXT,"
				+ // 10: MObjectStateName_TX
				"'Exception_YN' TEXT NOT NULL ,"
				+ // 11: Exception_YN
				"'AlarmLevel_ID' INTEGER,"
				+ // 12: AlarmLevel_ID
				"'SpecCase_YN' TEXT NOT NULL ,"
				+ // 13: SpecCase_YN
				"'SpecCase_TX' TEXT,"
				+ // 14: SpecCase_TX
				"'Memo_TX' TEXT,"
				+ // 15: Memo_TX
				"'Time_NR' INTEGER NOT NULL ,"
				+ // 16: Time_NR
				"'DataFlag_CD' TEXT NOT NULL ,"
				+ // 17: DataFlag_CD
				"'ShiftName_TX' TEXT,"
				+ // 18: ShiftName_TX
				"'ShiftGroupName_TX' TEXT,"
				+ // 19: ShiftGroupName_TX
				"'Ratio_NR' INTEGER,"
				+ // 20: Ratio_NR
				"'Ratio1_NR' TEXT,"
				+ // 21: Ratio1_NR
				"'Rate' INTEGER,"
				+ // 22: Rate
				"'DataLen' INTEGER,"
				+ // 23: DataLen
				"'LastResult_TX' TEXT,"
				+ // 24: LastResult_TX
				"'TransInfo_TX' TEXT,"
				+ // 25: TransInfo_TX
				"'VibParam_TX' TEXT,"
				+ // 26: VibParam_TX
				"'FeatureValue_TX' TEXT,"
				+ // 27: FeatureValue_TX
				"'GPSInfo_TX' TEXT,"
				+ // 28: GPSInfo_TX
				"'DataType_CD' TEXT," + "'ZhenDong_Type' TEXT,"
				+ "'FFTDATA_TX' BLOB," +
				"TransException_YN nvarchar(1)," +
				"AlarmDesc_TX nvarchar(40)," +
				"NoticeType_TX nvarchar(20));");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'DJ_RESULTHIS'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, XJ_ResultHis entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getLine_ID());
		stmt.bindString(2, entity.getDJ_Plan_ID());
		stmt.bindString(3, entity.getCompleteTime_DT());
		stmt.bindString(4, entity.getPlanDesc_TX());
		stmt.bindString(5, entity.getIDPosName_TX());
		stmt.bindString(6, entity.getAppUser_CD());

		String AppUserName_TX = entity.getAppUserName_TX();
		if (AppUserName_TX != null) {
			stmt.bindString(7, AppUserName_TX);
		}
		stmt.bindString(8, entity.getResult_TX());

		byte[] Data8K_TX = entity.getData8K_TX();
		if (Data8K_TX != null) {
			stmt.bindBlob(9, Data8K_TX);
		}
		stmt.bindString(10, entity.getQuery_DT());

		String MObjectStateName_TX = entity.getMObjectStateName_TX();
		if (MObjectStateName_TX != null) {
			stmt.bindString(11, MObjectStateName_TX);
		}
		stmt.bindString(12, entity.getException_YN());

		Integer AlarmLevel_ID = entity.getAlarmLevel_ID();
		if (AlarmLevel_ID != null) {
			stmt.bindLong(13, AlarmLevel_ID);
		}
		stmt.bindString(14, entity.getSpecCase_YN());

		String SpecCase_TX = entity.getSpecCase_TX();
		if (SpecCase_TX != null) {
			stmt.bindString(15, SpecCase_TX);
		}

		String Memo_TX = entity.getMemo_TX();
		if (Memo_TX != null) {
			stmt.bindString(16, Memo_TX);
		}
		stmt.bindLong(17, entity.getTime_NR());
		stmt.bindString(18, entity.getDataFlag_CD());

		String ShiftName_TX = entity.getShiftName_TX();
		if (ShiftName_TX != null) {
			stmt.bindString(19, ShiftName_TX);
		}

		String ShiftGroupName_TX = entity.getShiftGroupName_TX();
		if (ShiftGroupName_TX != null) {
			stmt.bindString(20, ShiftGroupName_TX);
		}

		Boolean Ratio_NR = entity.getRatio_NR();
		if (Ratio_NR != null) {
			stmt.bindLong(21, Ratio_NR ? 1l : 0l);
		}

		String Ratio1_NR = entity.getRatio1_NR();
		if (Ratio1_NR != null) {
			stmt.bindString(22, Ratio1_NR);
		}

		Integer Rate = entity.getRate();
		if (Rate != null) {
			stmt.bindLong(23, Rate);
		}

		Integer DataLen = entity.getDataLen();
		if (DataLen != null) {
			stmt.bindLong(24, DataLen);
		}

		String LastResult_TX = entity.getLastResult_TX();
		if (LastResult_TX != null) {
			stmt.bindString(25, LastResult_TX);
		}

		String TransInfo_TX = entity.getTransInfo_TX();
		if (TransInfo_TX != null) {
			stmt.bindString(26, TransInfo_TX);
		}

		String VibParam_TX = entity.getVibParam_TX();
		if (VibParam_TX != null) {
			stmt.bindString(27, VibParam_TX);
		}

		String FeatureValue_TX = entity.getFeatureValue_TX();
		if (FeatureValue_TX != null) {
			stmt.bindString(28, FeatureValue_TX);
		}

		String GPSInfo_TX = entity.getGPSInfo_TX();
		if (GPSInfo_TX != null) {
			stmt.bindString(29, GPSInfo_TX);
		}

		String DataType_CD = entity.getDataType_CD();
		if (DataType_CD != null) {
			stmt.bindString(30, DataType_CD);
		}

		String ZhenDong_Type = entity.getZhenDong_Type();
		if (ZhenDong_Type != null) {
			stmt.bindString(31, ZhenDong_Type);
		}

		byte[] FFTData_TX = entity.getFFTData_TX();
		if (FFTData_TX != null) {
			stmt.bindBlob(32, FFTData_TX);
		}
		
		String TransException_YN=entity.getTransException_YN();
		if(TransException_YN != null){
			stmt.bindString(33, TransException_YN);
		}
		
		
		String AlarmDesc_TX=entity.getAlarmDesc_TX();
		if(AlarmDesc_TX!=null){
			stmt.bindString(34, AlarmDesc_TX);
		}
		
		String NoticeType_TX=entity.getNoticeType_TX();
		if(NoticeType_TX!=null){
			stmt.bindString(35, NoticeType_TX);
		}
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public XJ_ResultHis readEntity(Cursor cursor, int offset) {
		XJ_ResultHis entity = new XJ_ResultHis(
				//
				cursor.getString(offset + 0), // Line_ID
				cursor.getString(offset + 1), // DJ_Plan_ID
				cursor.getString(offset + 2), // CompleteTime_DT
				cursor.getString(offset + 3), // PlanDesc_TX
				cursor.getString(offset + 4), // IDPosName_TX
				cursor.getString(offset + 5), // AppUser_CD
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // AppUserName_TX
				cursor.getString(offset + 7), // Result_TX
				cursor.isNull(offset + 8) ? null : cursor.getBlob(offset + 8), // Data8K_TX
				cursor.getString(offset + 9), // Query_DT
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10), // MObjectStateName_TX
				cursor.getString(offset + 11), // Exception_YN
				cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // AlarmLevel_ID
				cursor.getString(offset + 13), // SpecCase_YN
				cursor.isNull(offset + 14) ? null : cursor
						.getString(offset + 14), // SpecCase_TX
				cursor.isNull(offset + 15) ? null : cursor
						.getString(offset + 15), // Memo_TX
				cursor.getInt(offset + 16), // Time_NR
				cursor.getString(offset + 17), // DataFlag_CD
				cursor.isNull(offset + 18) ? null : cursor
						.getString(offset + 18), // ShiftName_TX
				cursor.isNull(offset + 19) ? null : cursor
						.getString(offset + 19), // ShiftGroupName_TX
				cursor.isNull(offset + 20) ? null : cursor
						.getShort(offset + 20) != 0, // Ratio_NR
				cursor.isNull(offset + 21) ? null : cursor
						.getString(offset + 21), // Ratio1_NR
				cursor.isNull(offset + 22) ? null : cursor.getInt(offset + 22), // Rate
				cursor.isNull(offset + 23) ? null : cursor.getInt(offset + 23), // DataLen
				cursor.isNull(offset + 24) ? null : cursor
						.getString(offset + 24), // LastResult_TX
				cursor.isNull(offset + 25) ? null : cursor
						.getString(offset + 25), // TransInfo_TX
				cursor.isNull(offset + 26) ? null : cursor
						.getString(offset + 26), // VibParam_TX
				cursor.isNull(offset + 27) ? null : cursor
						.getString(offset + 27), // FeatureValue_TX
				cursor.isNull(offset + 28) ? null : cursor
						.getString(offset + 28), // GPSInfo_TX
				cursor.isNull(offset + 29) ? null : cursor
						.getString(offset + 29),
				cursor.isNull(offset + 30) ? null : cursor
						.getString(offset + 30),
				cursor.isNull(offset + 31) ? null : cursor.getBlob(offset + 31),
				cursor.isNull(offset + 32) ? null : cursor.getString(offset + 32),
				cursor.isNull(offset + 33) ? null : cursor.getString(offset + 33),
				cursor.isNull(offset + 34) ? null : cursor.getString(offset + 34));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, XJ_ResultHis entity, int offset) {
		entity.setLine_ID(cursor.getString(offset + 0));
		entity.setDJ_Plan_ID(cursor.getString(offset + 1));
		entity.setCompleteTime_DT(cursor.getString(offset + 2));
		entity.setPlanDesc_TX(cursor.getString(offset + 3));
		entity.setIDPosName_TX(cursor.getString(offset + 4));
		entity.setAppUser_CD(cursor.getString(offset + 5));
		entity.setAppUserName_TX(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setResult_TX(cursor.getString(offset + 7));
		entity.setData8K_TX(cursor.isNull(offset + 8) ? null : cursor
				.getBlob(offset + 8));
		entity.setQuery_DT(cursor.getString(offset + 9));
		entity.setMObjectStateName_TX(cursor.isNull(offset + 10) ? null
				: cursor.getString(offset + 10));
		entity.setException_YN(cursor.getString(offset + 11));
		entity.setAlarmLevel_ID(cursor.isNull(offset + 12) ? null : cursor
				.getInt(offset + 12));
		entity.setSpecCase_YN(cursor.getString(offset + 13));
		entity.setSpecCase_TX(cursor.isNull(offset + 14) ? null : cursor
				.getString(offset + 14));
		entity.setMemo_TX(cursor.isNull(offset + 15) ? null : cursor
				.getString(offset + 15));
		entity.setTime_NR(cursor.getInt(offset + 16));
		entity.setDataFlag_CD(cursor.getString(offset + 17));
		entity.setShiftName_TX(cursor.isNull(offset + 18) ? null : cursor
				.getString(offset + 18));
		entity.setShiftGroupName_TX(cursor.isNull(offset + 19) ? null : cursor
				.getString(offset + 19));
		entity.setRatio_NR(cursor.isNull(offset + 20) ? null : cursor
				.getShort(offset + 20) != 0);
		entity.setRatio1_NR(cursor.isNull(offset + 21) ? null : cursor
				.getString(offset + 21));
		entity.setRate(cursor.isNull(offset + 22) ? null : cursor
				.getInt(offset + 22));
		entity.setDataLen(cursor.isNull(offset + 23) ? null : cursor
				.getInt(offset + 23));
		entity.setLastResult_TX(cursor.isNull(offset + 24) ? null : cursor
				.getString(offset + 24));
		entity.setTransInfo_TX(cursor.isNull(offset + 25) ? null : cursor
				.getString(offset + 25));
		entity.setVibParam_TX(cursor.isNull(offset + 26) ? null : cursor
				.getString(offset + 26));
		entity.setFeatureValue_TX(cursor.isNull(offset + 27) ? null : cursor
				.getString(offset + 27));
		entity.setGPSInfo_TX(cursor.isNull(offset + 28) ? null : cursor
				.getString(offset + 28));
		entity.setDataType_CD(cursor.isNull(offset + 29) ? null : cursor
				.getString(offset + 29));
		entity.setZhenDong_Type(cursor.isNull(offset + 30) ? null : cursor
				.getString(offset + 30));
		entity.setFFTData_TX(cursor.isNull(offset + 31) ? null : cursor
				.getBlob(offset + 31));
		entity.setTransException_YN(cursor.isNull(offset + 32) ? null : cursor
				.getString(offset + 32));
		entity.setAlarmDesc_TX(cursor.isNull(offset + 33) ? null : cursor
				.getString(offset + 33));
		entity.setNoticeType_TX(cursor.isNull(offset + 34) ? null : cursor
				.getString(offset + 34));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(XJ_ResultHis entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(XJ_ResultHis entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<XJ_ResultHis> loadOneDJResultHis(String planID,
			String startTime, String endTime) {
		String sqlString = "select * from DJ_RESULTHIS where DJ_PLAN_ID='"
				+ planID + "' " + "and COMPLETETIME_DT >='" + startTime
				+ "' and COMPLETETIME_DT <= '" + endTime + "'"
				+ " and SPECCASE_TX = ''";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public List<XJ_ResultHis> loadOneDJResultHisForCaseXJ(String planID,
			String conditionStr) {
		String sqlString = "select * from DJ_RESULTHIS where DJ_PLAN_ID = '"
				+ planID + "' and TransInfo_TX like '%" + conditionStr + "%'";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public List<XJ_ResultHis> loadDJResultHisByPlanID(String lineID,
			String planID) {
		String sqlString = "select * from DJ_RESULTHIS where Line_ID = "
				+ lineID + " And DJ_PLAN_ID='" + planID
				+ "' and SPECCASE_TX = '' order by QUERY_DT asc ";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 查询历史点检结果
	 * 
	 * @param lineID
	 * @return
	 */
	public List<XJ_ResultHis> loadDJResultHisByTime(int lineID) {
		String sqlString = "select * from DJ_RESULTHIS where Line_ID='"
				+ lineID + "'";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 查询历史点检纽扣
	 * 
	 * @param lineID
	 * @return
	 */
	/*
	 * public List<XJ_ResultHis> loadDJHisNK(int lineID) { String sql =
	 * "select distinct IDPosName_TX from DJ_RESULTHIS WHERE Line_ID='" + lineID
	 * + "'"; Cursor cursor = db.rawQuery(sql, null); return
	 * loadAllAndCloseCursor(cursor); }
	 */

	/**
	 * 根据时间和路线id查询历史点检结果
	 * 
	 * @param startTime
	 * @param endTime
	 * @param lineID
	 * @return
	 */
	public List<XJ_ResultHis> loadDJResultHisByTime(String startTime,
			String endTime, int lineID, String type) {
		String sqlString = "select * from DJ_RESULTHIS where CompleteTime_DT>='"
				+ startTime + "' and CompleteTime_DT<='" + endTime + "' and Line_ID='"
				+ lineID + "'";
		if (!StringUtils.isEmpty(type)) {
			sqlString += "and DataType_CD='" + type + "'";
		}
		
		sqlString += " order by CompleteTime_dt asc";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	public void updateDJResultHis(XJ_ResultHis entity, DJPlan mDjPlan) {
		String sqlString = "update DJ_RESULTHIS set COMPLETETIME_DT ='"
				+ entity.getCompleteTime_DT() + "',RESULT_TX ='"
				+ entity.getResult_TX()
				/*
				 * + "',LASTRESULT_TX ='" + entity.getLastResult_TX()
				 */
				+ "',MEMO_TX ='" + entity.getMemo_TX() + "',EXCEPTION_YN ='"
				+ entity.getException_YN() + "',ALARMLEVEL_ID ='"
				+ entity.getAlarmLevel_ID() + "'";
		if (mDjPlan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
				&& entity.getData8K_TX() != null) {
			ContentValues values = new ContentValues();
			values.put("Data8K_TX", entity.getData8K_TX());
			values.put("FFTDATA_TX", entity.getFFTData_TX());
			db.update(
					"DJ_RESULTHIS",
					values,
					"DJ_PLAN_ID='"
							+ mDjPlan.getDJPlan().getDJ_Plan_ID()
							+ "' "
							+ " and Line_ID="
							+ entity.getLine_ID()
							+ " and COMPLETETIME_DT >='"
							+ DateTimeHelper.DateToString(mDjPlan.GetCycle()
									.getTaskBegin())
							+ "' and COMPLETETIME_DT <= '"
							+ DateTimeHelper.DateToString(mDjPlan.GetCycle()
									.getTaskEnd()) + "'"
							+ " and SPECCASE_TX = ''", null);
			/*
			 * sqlString += ", Data8K_TX = " + entity.getData8K_TX() +
			 * ", FFTDATA_TX = " + entity.getFFTData_TX() + "";
			 */

		}
		sqlString += " where DJ_PLAN_ID='"
				+ mDjPlan.getDJPlan().getDJ_Plan_ID()
				+ "' "
				+ " and Line_ID="
				+ entity.getLine_ID()
				+ " and COMPLETETIME_DT >='"
				+ DateTimeHelper
						.DateToString(mDjPlan.GetCycle().getTaskBegin())
				+ "' and COMPLETETIME_DT <= '"
				+ DateTimeHelper.DateToString(mDjPlan.GetCycle().getTaskEnd())
				+ "'" + " and SPECCASE_TX = ''";
		db.execSQL(sqlString);
	}
	
	public void updateDJResultHisForDJPC(XJ_ResultHis entity, DJPlan mDjPlan) {
		String sqlString = "update DJ_RESULTHIS set COMPLETETIME_DT ='"
				+ entity.getCompleteTime_DT() + "',RESULT_TX ='"
				+ entity.getResult_TX()
				/*
				 * + "',LASTRESULT_TX ='" + entity.getLastResult_TX()
				 */
				+ "',MEMO_TX ='" + entity.getMemo_TX() + "',EXCEPTION_YN ='"
				+ entity.getException_YN() + "',ALARMLEVEL_ID ='"
				+ entity.getAlarmLevel_ID() + "'";
		if (mDjPlan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
				&& entity.getData8K_TX() != null) {
			ContentValues values = new ContentValues();
			values.put("Data8K_TX", entity.getData8K_TX());
			values.put("FFTDATA_TX", entity.getFFTData_TX());
			db.update(
					"DJ_RESULTHIS",
					values,
					"DJ_PLAN_ID='"
							+ mDjPlan.getDJPlan().getDJ_Plan_ID()
							+ "' "
							+ " and Line_ID="
							+ entity.getLine_ID()
							+ " and COMPLETETIME_DT >='"
							+ mDjPlan.getDJPlan().getDisStart_TM()
							+ "' and COMPLETETIME_DT <= '"
							+ mDjPlan.getDJPlan().getDisEnd_TM() + "'"
							+ " and SPECCASE_TX = ''", null);
			/*
			 * sqlString += ", Data8K_TX = " + entity.getData8K_TX() +
			 * ", FFTDATA_TX = " + entity.getFFTData_TX() + "";
			 */

		}
		sqlString += " where DJ_PLAN_ID='"
				+ mDjPlan.getDJPlan().getDJ_Plan_ID()
				+ "' "
				+ " and Line_ID="
				+ entity.getLine_ID()
				+ " and COMPLETETIME_DT >='"
				+ mDjPlan.getDJPlan().getDisStart_TM()
				+ "' and COMPLETETIME_DT <= '"
				+ mDjPlan.getDJPlan().getDisEnd_TM()
				+ "'" + " and SPECCASE_TX = ''";
		db.execSQL(sqlString);
	}

	public void updateDJResultHisForCaseXJ(XJ_ResultHis entity, DJPlan mDjPlan,
			String mConditionStr) {
		String sqlString = "update DJ_RESULTHIS set COMPLETETIME_DT ='"
				+ entity.getCompleteTime_DT() + "',RESULT_TX ='"
				+ entity.getResult_TX()
				/*
				 * + "',LASTRESULT_TX ='" + entity.getLastResult_TX()
				 */
				+ "',MEMO_TX ='" + entity.getMemo_TX() + "',EXCEPTION_YN ='"
				+ entity.getException_YN() + "',ALARMLEVEL_ID ='"
				+ entity.getAlarmLevel_ID() + "'";
		if (mDjPlan.getDJPlan().getDataType_CD()
				.equalsIgnoreCase(AppConst.DJPLAN_DATACODE_CZ)
				&& entity.getData8K_TX() != null) {

			ContentValues values = new ContentValues();
			values.put("Data8K_TX", entity.getData8K_TX());
			values.put("FFTDATA_TX", entity.getFFTData_TX());
			db.update("DJ_RESULTHIS", values, "DJ_PLAN_ID='"
					+ mDjPlan.getDJPlan().getDJ_Plan_ID() + "' and Line_ID = '"
					+ entity.getLine_ID() + "' and TransInfo_TX like '%"
					+ mConditionStr + "%'", null);

			/*
			 * sqlString += ", Data8K_TX = " + entity.getData8K_TX() +
			 * ", FFTDATA_TX = " + entity.getFFTData_TX() + "";
			 */

		}

		sqlString += " where DJ_PLAN_ID='"
				+ mDjPlan.getDJPlan().getDJ_Plan_ID() + "' and Line_ID = '"
				+ entity.getLine_ID() + "' and TransInfo_TX like '%"
				+ mConditionStr + "%'";
		db.execSQL(sqlString);
	}

	public void clearHisData(String data) {
		clearResultData(data);
		clearIDPosData(data);
		clearMobjectStateData(data);
	}

	private void clearResultData(String data) {
		String sqlString = "Delete from DJ_RESULTHIS Where CompleteTime_DT < '"
				+ data + "'";
		db.execSQL(sqlString);
	}

	private void clearIDPosData(String data) {
		String sqlString = "Delete from DJ_TASKIDPOSHIS Where Query_DT < '"
				+ data + "'";
		db.execSQL(sqlString);
	}

	private void clearMobjectStateData(String data) {
		String sqlString = "Delete from MOBJECT_STATEHIS Where CompleteTime_DT < '"
				+ data + "'";
		db.execSQL(sqlString);
	}
}
