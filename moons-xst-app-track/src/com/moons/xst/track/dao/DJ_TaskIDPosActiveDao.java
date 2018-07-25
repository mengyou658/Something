package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.GPSPositionForResult;
import com.moons.xst.track.common.StringUtils;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table DJ_TASK_IDPOS_ACTIVE.
 */
public class DJ_TaskIDPosActiveDao extends
		AbstractDao<DJ_TaskIDPosActive, Void> {

	public static final String TABLENAME = "DJ_TASKIDPOSACTIVE";

	/**
	 * Properties of entity DJ_TaskIDPosActive.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property IDPos_ID = new Property(0, String.class,
				"IDPos_ID", false, "IDPOS_ID");
		public final static Property IDPosStart_TM = new Property(1,
				java.util.Date.class, "IDPosStart_TM", false, "IDPOSSTART_TM");
		public final static Property IDPosEnd_TM = new Property(2,
				java.util.Date.class, "IDPosEnd_TM", false, "IDPOSEND_TM");
		public final static Property Query_DT = new Property(3,
				java.util.Date.class, "Query_DT", false, "QUERY_DT");
		public final static Property AppUser_CD = new Property(4, String.class,
				"AppUser_CD", false, "APPUSER_CD");
		public final static Property AppUserName_TX = new Property(5,
				String.class, "AppUserName_TX", false, "APPUSERNAME_TX");
		public final static Property ShiftName_TX = new Property(6,
				String.class, "ShiftName_TX", false, "SHIFTNAME_TX");
		public final static Property ShiftGroupName_TX = new Property(7,
				String.class, "ShiftGroupName_TX", false, "SHIFTGROUPNAME_TX");
		public final static Property TimeCount_NR = new Property(8, int.class,
				"TimeCount_NR", false, "TIMECOUNT_NR");
		public final static Property TransInfo_TX = new Property(9,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
		public final static Property GPSInfo_TX = new Property(10,
				String.class, "GPSInfo_TX", false, "GPSINFO_TX");
	};

	public DJ_TaskIDPosActiveDao(DaoConfig config) {
		super(config);
	}

	public DJ_TaskIDPosActiveDao(DaoConfig config, DJResultDAOSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "DJ_TASKIDPOSACTIVE (" + //
				"IDPOS_ID nvarchar(400) NOT NULL ," + // 0: IDPos_ID
				"IDPOSSTART_TM datetime NOT NULL ," + // 1: IDPosStart_TM
				"IDPOSEND_TM datetime," + // 2: IDPosEnd_TM
				"QUERY_DT datetime," + // 3: Query_DT
				"APPUSER_CD nvarchar(100) NOT NULL ," + // 4: AppUser_CD
				"APPUSERNAME_TX nvarchar(100)," + // 5: AppUserName_TX
				"ShiftName_TX nvarchar(100)," + // 6: ShiftName_TX
				"ShiftGroupName_TX nvarchar(100)," + // 7: ShiftGroupName_TX
				"TimeCount_NR INTEGER NOT NULL ," + // 8: TimeCount_NR
				"TransInfo_TX nvarchar(500)," + // 9: TransInfo_TX
				"GPSInfo_TX nvarchar(60));"); // 10: GPSInfo_TX
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'DJ_TASKIDPOSACTIVE'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, DJ_TaskIDPosActive entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getIDPos_ID());
		stmt.bindString(2, entity.getIDPosStart_TM());

		String IDPosEnd_TM = entity.getIDPosEnd_TM();
		if (IDPosEnd_TM != null) {
			stmt.bindString(3, IDPosEnd_TM);
		}

		String Query_DT = entity.getQuery_DT();
		if (Query_DT != null) {
			stmt.bindString(4, Query_DT);
		}
		stmt.bindString(5, entity.getAppUser_CD());

		String AppUserName_TX = entity.getAppUserName_TX();
		if (AppUserName_TX != null) {
			stmt.bindString(6, AppUserName_TX);
		}

		String ShiftName_TX = entity.getShiftName_TX();
		if (ShiftName_TX != null) {
			stmt.bindString(7, ShiftName_TX);
		}

		String ShiftGroupName_TX = entity.getShiftGroupName_TX();
		if (ShiftGroupName_TX != null) {
			stmt.bindString(8, ShiftGroupName_TX);
		}
		stmt.bindLong(9, entity.getTimeCount_NR());

		String TransInfo_TX = entity.getTransInfo_TX();
		if (TransInfo_TX != null) {
			stmt.bindString(10, TransInfo_TX);
		}

		String GPSInfo_TX = entity.getGPSInfo_TX();
		if (GPSInfo_TX != null) {
			stmt.bindString(11, GPSInfo_TX);
		}
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public DJ_TaskIDPosActive readEntity(Cursor cursor, int offset) {
		DJ_TaskIDPosActive entity = new DJ_TaskIDPosActive(
				//
				cursor.getString(offset + 0), // IDPos_ID
				cursor.getString(offset + 1), // IDPosStart_TM
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // IDPosEnd_TM
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Query_DT
				cursor.getString(offset + 4), // AppUser_CD
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // AppUserName_TX
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // ShiftName_TX
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // ShiftGroupName_TX
				cursor.getInt(offset + 8), // TimeCount_NR
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // TransInfo_TX
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10) // GPSInfo_TX
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, DJ_TaskIDPosActive entity, int offset) {
		entity.setIDPos_ID(cursor.getString(offset + 0));
		entity.setIDPosStart_TM(cursor.getString(offset + 1));
		entity.setIDPosEnd_TM(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setQuery_DT(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setAppUser_CD(cursor.getString(offset + 4));
		entity.setAppUserName_TX(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setShiftName_TX(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setShiftGroupName_TX(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setTimeCount_NR(cursor.getInt(offset + 8));
		entity.setTransInfo_TX(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setGPSInfo_TX(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
	}

	/**
	 * 更新钮扣到位结果结束时间(结果表)
	 * 
	 * @param entity
	 */
	public void UpdateDJIDPosActive(DJ_TaskIDPosActive entity) {
		String sqlStr = "Update DJ_TASKIDPOSACTIVE Set IDPOSEND_TM = '"
				+ entity.getIDPosEnd_TM() + "', TimeCount_NR = '"
				+ entity.getTimeCount_NR() + "' Where IDPOS_ID = '"
				+ entity.getIDPos_ID() + "' And IDPOSSTART_TM = '"
				+ entity.getIDPosStart_TM() + "'";
		db.execSQL(sqlStr);
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(DJ_TaskIDPosActive entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(DJ_TaskIDPosActive entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	/**
	 * 加载数据（供实时上传巡点检到位数据）
	 * 
	 * @param loadcount
	 * @return
	 */
	public List<DJ_TaskIDPosActive> loadforUploadJIT(int loadcount) {
		// String sqlString =
		// "select * from DJ_TaskIDPosActive where IDPosStart_TM <> IDPosEnd_TM order by IDPosStart_TM asc limit 0,"
		// + loadcount;
		String sqlString = "";
		if (!AppContext.getIsUploading()) {
			return null;
		}
		if (AppContext.getCurIDPos() == null) {
			sqlString = "Select * from DJ_TaskIDPosActive order by IDPosStart_TM asc limit 0,"
					+ loadcount;
		} else {
			sqlString = "Select * from DJ_TaskIDPosActive Where IDPos_ID <> '"
					+ AppContext.getCurIDPos().getIDPos_ID()
					+ "' order by IDPosStart_TM asc limit 0," + loadcount;
		}
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 删除一组已上传数据
	 * 
	 * @param uploadedData
	 */
	public void deleteUploadedData(List<DJ_TaskIDPosActive> uploadedData) {
		for (DJ_TaskIDPosActive taskIDPosResult : uploadedData) {
			deleteDatabyCondition(taskIDPosResult.getIDPos_ID(),
					taskIDPosResult.getIDPosStart_TM());
		}
	}

	public void deleteDatabyCondition(String _id, String _utcdatetime) {
		String sql = "delete from DJ_TaskIDPosActive where IDPos_ID = " + _id
				+ " and IDPosStart_TM = '" + _utcdatetime + "'";
		db.execSQL(sql);
	}
}