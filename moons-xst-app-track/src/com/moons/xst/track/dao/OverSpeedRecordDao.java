package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.bean.OverSpeedRecordInfo;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class OverSpeedRecordDao extends AbstractDao<OverSpeedRecordInfo, Void> {

	public static final String TABLENAME = "OverSpeedRecord";

	/**
	 * Properties of entity GPSPosition.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property OverSpeedRecord_ID = new Property(0,
				String.class, "OverSpeedRecord_ID", false, "OverSpeedRecord_ID");
		public final static Property CheckPoint_ID = new Property(1,
				String.class, "CheckPoint_ID", false, "CheckPoint_ID");
		public final static Property BeginDT = new Property(2, String.class,
				"BeginDT", false, "BeginDT");
		public final static Property EndDT = new Property(3, String.class,
				"EndDT", false, "EndDT");
		public final static Property Line_ID = new Property(4, String.class,
				"Line_ID", false, "Line_ID");
		public final static Property LimitSpeed = new Property(5, String.class,
				"LimitSpeed", false, "LimitSpeed");
		public final static Property TopSpeed = new Property(6, String.class,
				"TopSpeed", false, "TopSpeed");
		public final static Property OverTimeLong = new Property(7,
				String.class, "OverTimeLong", false, "OverTimeLong");
	};

	public OverSpeedRecordDao(DaoConfig config) {
		super(config);
	}

	public OverSpeedRecordDao(DaoConfig config, DJResultDAOSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "OverSpeedRecord (" + //
				"OverSpeedRecord_ID nvarchar(150) NOT NULL ," + // 0:
																// OverSpeedRecord_ID
				"CheckPoint_ID nvarchar(150)," + // 1: CheckPoint_ID
				"BeginDT nvarchar(150)," + // 2: BeginDT
				"EndDT nvarchar(150)," + // 3: EndDT
				"Line_ID nvarchar(150)," + // 4: Line_ID
				"LimitSpeed nvarchar(150)," + // 5: LimitSpeed
				"TopSpeed nvarchar(150)," + // 6: TopSpeed
				"OverTimeLong nvarchar(150) " + ")"); // 7:OverTimeLong
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'OverSpeedRecord'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, OverSpeedRecordInfo entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getOverSpeedRecord_ID());

		String CheckPoint_ID = entity.getCheckPoint_ID();
		if (CheckPoint_ID != null) {
			stmt.bindString(2, CheckPoint_ID);
		}

		String BeginDT = entity.getBeginDT();
		if (BeginDT != null) {
			stmt.bindString(3, BeginDT);
		}

		String EndDT = entity.getEndDT();
		if (EndDT != null) {
			stmt.bindString(4, EndDT);
		}

		String Line_ID = entity.getLine_ID();
		if (Line_ID != null) {
			stmt.bindString(5, Line_ID);
		}

		String LimitSpeed = entity.getLimitSpeed();
		if (LimitSpeed != null) {
			stmt.bindString(6, LimitSpeed);
		}

		String TopSpeed = entity.getTopSpeed();
		if (TopSpeed != null) {
			stmt.bindString(7, TopSpeed);
		}
		stmt.bindString(8, entity.getOverTimeLong());
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public OverSpeedRecordInfo readEntity(Cursor cursor, int offset) {
		OverSpeedRecordInfo entity = new OverSpeedRecordInfo(
				//
				cursor.getString(offset + 0), // XJQGUID_TX
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // XJQ_CD
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Line_ID
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, OverSpeedRecordInfo entity, int offset) {
		entity.setOverSpeedRecord_ID(cursor.getString(offset + 0));
		entity.setCheckPoint_ID(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setBeginDT(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setEndDT(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setLine_ID(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setLimitSpeed(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setTopSpeed(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setOverTimeLong(cursor.getString(offset + 7));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(OverSpeedRecordInfo entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(OverSpeedRecordInfo entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	/** @inheritdoc */
	public long InsertOverSpeedRecord(OverSpeedRecordInfo entity) {
		long row = insert(entity);
		return row;
	}

	public void Operatesql(String sql) {
		SQLiteDatabase db = getDatabase();

		db.execSQL(sql);
	}

	/*
	 * 加载数据（供实时上传巡线到位数据）
	 * 
	 * @param loadcount
	 * 
	 * @return
	 */
	public List<OverSpeedRecordInfo> loadforUploadJIT(int loadcount) {
		String sqlString = "select * from OverSpeedRecord order by BeginDT desc limit 0,"
				+ loadcount;
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}

	/**
	 * 删除一组已上传数据
	 * 
	 * @param uploadedData
	 */
	public void deleteUploadedData(List<OverSpeedRecordInfo> uploadedData) {
		for (OverSpeedRecordInfo gpsPositionResult : uploadedData) {
			deleteDatabyCondition(gpsPositionResult.getOverSpeedRecord_ID());
		}
	}

	public void deleteDatabyCondition(String _id) {
		String sql = "delete from OverSpeedRecord where OverSpeedRecord_ID ='"
				+ _id + "'";
		db.execSQL(sql);
	}
}
