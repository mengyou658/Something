/**
 * @CheckPointDAO.java
 * @author LKZ
 * @2015-1-16
 */
package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.CheckPointInfo;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * @com.moons.xst.track.dao
 * @author LKZ
 * 
 */
public class CheckPointDAO extends AbstractDao<CheckPointInfo, Long> {
	public CheckPointDAO(DaoConfig config) {
		super(config);
		// TODO 自动生成的构造函数存根
	}

	public CheckPointDAO(DaoConfig config, DJDAOSession daoSession) {
		super(config, daoSession);
	}

	public static final String TABLENAME = "XX_CheckPoint";

	public static class Properties {
		public final static Property GPSPosition_ID = new Property(0,
				int.class, "GPSPosition_ID", false, "GPSPOSITION_ID");
		public final static Property GPSPosition_CD = new Property(1,
				String.class, "GPSPosition_CD", false, "GPSPOSITION_CD");
		public final static Property Sort_NR = new Property(2, int.class,
				"Sort_NR", false, "SORT_NR");
		public final static Property Longitude = new Property(3, String.class,
				"Longitude", false, "LONGITUDE");
		public final static Property Latitude = new Property(4, String.class,
				"Latitude", false, "LATITUDE");
		public final static Property UTCDateTime = new Property(5,
				String.class, "UTCDateTime", false, "UTCDATETIME");
		public final static Property Desc_TX = new Property(6, String.class,
				"Desc_TX", false, "DESC_TX");
		public final static Property LineID = new Property(7, String.class,
				"LineID", false, "LINEID");
		public final static Property NextPointHour_NR = new Property(8,
				String.class, "NextPointHour_NR", false, "NEXTPOINTHOUR_NR");
		public final static Property Deviation = new Property(9, String.class,
				"Deviation", false, "DEVIATION");
		public final static Property CheckPoint_Type = new Property(10,
				String.class, "CheckPoint_Type", false, "CHECKPOINT_TYPE");
		public final static Property Order_YN = new Property(11, String.class,
				"Order_YN", false, "ORDER_YN");
		public final static Property KHDate_DT = new Property(12,
				java.util.Date.class, "KHDate_DT", false, "KHDATE_DT");
		
		public final static Property SpeedLimit_Num = new Property(13,
				String.class, "SpeedLimit_Num", false, "SpeedLimit_Num");
		public final static Property Cycle_ID = new Property(14,
				int.class, "Cycle_ID", false, "Cycle_ID");
		public final static Property CycleBaseDate_DT = new Property(15,
				String.class, "CycleBaseDate_DT", false, "CycleBaseDate_DT");
	};

	@Override
	protected void bindValues(SQLiteStatement stmt, CheckPointInfo entity) {
		stmt.clearBindings();

		stmt.bindLong(1, entity.getGPSPosition_ID());

		String GPSPosition_CD = entity.getGPSPosition_CD();
		if (GPSPosition_CD != null) {
			stmt.bindString(2, GPSPosition_CD);
		}
		stmt.bindLong(3, entity.getSort_NR());

		String Longitude = entity.getLongitude();
		if (Longitude != null) {
			stmt.bindString(4, Longitude);
		}

		String Latitude = entity.getLatitude();
		if (Latitude != null) {
			stmt.bindString(5, Latitude);
		}

		String UTCDateTime = entity.getUTCDateTime();
		if (UTCDateTime != null) {
			stmt.bindString(6, UTCDateTime);
		}

		String Desc_TX = entity.getDesc_TX();
		if (Desc_TX != null) {
			stmt.bindString(7, Desc_TX);
		}

		String LineID = entity.getLineID();
		if (LineID != null) {
			stmt.bindString(8, LineID);
		}

		String NextPointHour_NR = entity.getNextPointHour_NR();
		if (NextPointHour_NR != null) {
			stmt.bindString(9, NextPointHour_NR);
		}

		String Deviation = entity.getDeviation();
		if (Deviation != null) {
			stmt.bindString(10, Deviation);
		}

		String CheckPoint_Type = entity.getCheckPoint_Type();
		if (CheckPoint_Type != null) {
			stmt.bindString(11, CheckPoint_Type);
		}

		String Order_YN = entity.getOrder_YN();
		if (Order_YN != null) {
			stmt.bindString(12, Order_YN);
		}

		String KHDate_DT = entity.getKHDate_DT();
		if (KHDate_DT != null) {
			stmt.bindString(13, KHDate_DT);
		}
		String SpeedLimit_Num = entity.getSpeedLimit_Num();
		if (SpeedLimit_Num != null) {
			stmt.bindString(14, SpeedLimit_Num);
		}
		
		int Cycle_ID = entity.getCycle_ID();
		if (SpeedLimit_Num != null) {
			stmt.bindLong(15, Cycle_ID);
		}
		
		String CycleBaseDate_DT = entity.getCycleBaseDate_DT();
		if (SpeedLimit_Num != null) {
			stmt.bindString(16, CycleBaseDate_DT);
		}
	}

	@Override
	protected Long getKey(CheckPointInfo arg0) {
		if (arg0 != null) {
			return Long.valueOf((arg0.getGPSPosition_ID()));
		} else {
			return null;
		}
	}

	@Override
	protected boolean isEntityUpdateable() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected CheckPointInfo readEntity(Cursor cursor, int offset) {
		CheckPointInfo entity = new CheckPointInfo(
				cursor.getInt(offset + 0), // GPSPosition_ID
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // GPSPosition_CD
				cursor.getInt(offset + 2), // Sort_NR
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Longitude
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Latitude
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // UTCDateTime
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Desc_TX
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // LineID
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // NextPointHour_NR
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Deviation
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10), // CheckPoint_Type
				cursor.isNull(offset + 11) ? null : cursor
						.getString(offset + 11), // Order_YN
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12), // KHDate_DT
				cursor.isNull(offset + 13) ? null : cursor
						.getString(offset + 13), // SpeedLimit_Num
				cursor.isNull(offset + 14) ? null : cursor
						.getInt(offset + 14), // Cycle_id
				cursor.isNull(offset + 15) ? null : cursor
						.getString(offset + 15) // Cyc_DT
		);
		return entity;
	}

	@Override
	protected void readEntity(Cursor cursor, CheckPointInfo entity, int offset) {
		entity.setGPSPosition_ID(cursor.getInt(offset + 0));
		entity.setGPSPosition_CD(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setSort_NR(cursor.getInt(offset + 2));
		entity.setLongitude(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setLatitude(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setUTCDateTime(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setDesc_TX(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setLineID(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setNextPointHour_NR(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setDeviation(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setCheckPoint_Type(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setOrder_YN(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		entity.setKHDate_DT(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
		entity.setSpeedLimit_Num(cursor.isNull(offset + 13) ? null : cursor
				.getString(offset + 13));
		entity.setCycle_ID(cursor.isNull(offset + 14) ? null : cursor
				.getInt(offset + 14));
		entity.setCycleBaseDate_DT(cursor.isNull(offset + 15) ? null : cursor
				.getString(offset + 15));
	}

	@Override
	protected Long readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	protected Long updateKeyAfterInsert(CheckPointInfo entity, Long rowId) {
		entity.setGPSPosition_ID(Integer.valueOf(String.valueOf(rowId)));
		return rowId;
	}

	@Override
	protected Long updateKeyAfterInsert(CheckPointInfo arg0, long arg1) {
		// TODO 自动生成的方法存根
		return null;
	}

	/**
	 * 更新考核点（KHDate）
	 * 
	 * @param entity
	 */
	public void UpdateKHDate(CheckPointInfo entity) {
		String sql = "update XX_CheckPoint set KHDate_DT ='"
				+ entity.getKHDate_DT() + "' where GPSPosition_ID ="
				+ entity.getGPSPosition_ID();
		db.execSQL(sql);
	}

	public void UpdateGps(CheckPointInfo entity) {
		String sql = "update XX_CheckPoint set LONGITUDE ='"
				+ entity.getLongitude() + "',LATITUDE ='"
				+ entity.getLatitude() + "' where GPSPosition_ID ="
				+ entity.getGPSPosition_ID();
		db.execSQL(sql);
	}
}