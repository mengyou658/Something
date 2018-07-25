package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.GPSXXPlan;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class GPSXXPlanDao extends AbstractDao<GPSXXPlan, String> {

	public static final String TABLENAME = "GPSXX_Plan";

	/**
	 * Properties of entity Model_GPSXXPlan.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property GpsPointXXItem_ID = new Property(0,
				String.class, "GpsPointXXItem_ID", true, "GpsPointXXItem_ID");
		public final static Property GPSPoint_ID = new Property(1,
				String.class, "GPSPoint_ID", false, "GPSPoint_ID");
		public final static Property GpsXXItem_ID = new Property(2,
				String.class, "GpsXXItem_ID", false, "GpsXXItem_ID");
		public final static Property Line_ID = new Property(3, String.class,
				"Line_ID", false, "Line_ID");
		public final static Property Name_TX = new Property(4, String.class,
				"Name_TX", false, "Name_TX");
		public final static Property XXContent_TX = new Property(5,
				String.class, "XXContent_TX", false, "XXContent_TX");
		public final static Property PipeLine_ID = new Property(6,
				String.class, "PipeLine_ID", false, "PipeLine_ID");
		public final static Property PipeLine_TX = new Property(7,
				String.class, "PipeLine_TX", false, "PipeLine_TX");
		public final static Property Ext1_TX = new Property(8, String.class,
				"Ext1_TX", false, "Ext1_TX");
		public final static Property Ext2_TX = new Property(9, String.class,
				"Ext2_TX", false, "Ext2_TX");
		public final static Property Ext3_TX = new Property(10, String.class,
				"Ext3_TX", false, "Ext3_TX");
		public final static Property Ext4_TX = new Property(11, String.class,
				"Ext4_TX", false, "Ext4_TX");
	};

	public GPSXXPlanDao(DaoConfig config) {
		super(config);
	}

	public GPSXXPlanDao(DaoConfig config, DJDAOSession daoSession) {
		super(config, daoSession);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, GPSXXPlan entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getGpsPointXXItem_ID());
		stmt.bindString(2, entity.getGPSPoint_ID());
		stmt.bindString(3, entity.getGpsXXItem_ID());
		stmt.bindString(4, entity.getLine_ID());
		stmt.bindString(5, entity.getName_TX());

		String XXContent_TX = entity.getXXContent_TX();
		if (XXContent_TX != null) {
			stmt.bindString(6, XXContent_TX);
		}
		stmt.bindString(7, entity.getPipeLine_ID());
		stmt.bindString(8, entity.getPipeLine_TX());

		String Ext1_TX = entity.getExt1_TX();
		if (Ext1_TX != null) {
			stmt.bindString(9, Ext1_TX);
		}

		String Ext2_TX = entity.getExt2_TX();
		if (Ext2_TX != null) {
			stmt.bindString(10, Ext2_TX);
		}

		String Ext3_TX = entity.getExt3_TX();
		if (Ext3_TX != null) {
			stmt.bindString(11, Ext3_TX);
		}

		String Ext4_TX = entity.getExt4_TX();
		if (Ext4_TX != null) {
			stmt.bindString(12, Ext4_TX);
		}
		
	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public GPSXXPlan readEntity(Cursor cursor, int offset) {
		GPSXXPlan entity = new GPSXXPlan(
				//
				cursor.getString(offset + 0), // GpsPointXXItem_ID
				cursor.getString(offset + 1), // GPSPoint_ID
				cursor.getString(offset + 2), // GpsXXItem_ID
				cursor.getString(offset + 3), // Line_id
				cursor.getString(offset + 4), // Name_TX
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // XXContent_TX
				cursor.getString(offset + 6), // PipeLine_ID
				cursor.getString(offset + 7), // PipeLine_TX
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Ext1_TX
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // Ext2_TX
				cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // Ext3_TX
				cursor.isNull(offset + 11) ? null : cursor
						.getString(offset + 11) // Ext4_TX
				
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, GPSXXPlan entity, int offset) {
		entity.setGpsPointXXItem_ID(cursor.getString(offset + 0));
		entity.setGPSPoint_ID(cursor.getString(offset + 1));
		entity.setGpsXXItem_ID(cursor.getString(offset + 2));
		entity.setLine_ID(cursor.getString(offset + 3));
		entity.setName_TX(cursor.getString(offset + 4));
		entity.setXXContent_TX(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setPipeLine_ID(cursor.getString(offset + 6));
		entity.setPipeLine_TX(cursor.getString(offset + 7));
		entity.setExt1_TX(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setExt2_TX(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setExt3_TX(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setExt4_TX(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		
	}

	/** @inheritdoc */
	@Override
	protected String updateKeyAfterInsert(GPSXXPlan entity, long rowId) {
		return entity.getGpsPointXXItem_ID();
	}

	/** @inheritdoc */
	@Override
	public String getKey(GPSXXPlan entity) {
		if (entity != null) {
			return entity.getGpsPointXXItem_ID();
		} else {
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<GPSXXPlan> loadAll(String cpID) {
		String sqlString = "select * from GPSXX_Plan where GPSPoint_ID ='"
				+ cpID + "'";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}
}
