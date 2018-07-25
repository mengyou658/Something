package com.moons.xst.track.dao;

import java.math.BigInteger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.TempMeasureDaoSession;
import com.moons.xst.track.bean.ProvisionalM;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

//THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
* DAO for table TEMPMEASUREBASEINFO.
*/
public class ProvisionalMDao extends AbstractDao<ProvisionalM, Void> {
	
	public static final String TABLENAME = "PROVISIONALM";
	
	/**
	 * Properties of entity TempMeasureBaseInfo.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property DeviceName = new Property(0, String.class,
				"DeviceName", true, "DEVICENAME");
		public final static Property DJResult = new Property(1, String.class,
				"DJResult", false, "DJRESULT");
		public final static Property DJTime = new Property(2, String.class,
				"DJTime", true, "DJTIME");
		public final static Property Data8K = new Property(3, byte[].class,
				"Data8K", false, "DATA8K");
		public final static Property Ratio = new Property(4, Integer.class, 
				"Ratio", false, "RATIO");
		public final static Property Ratio1 = new Property(5, Integer.class, 
				"Ratio1", false, "RATIO1");
		public final static Property Rate = new Property(6, Integer.class, 
				"Rate", false, "RATE");
		public final static Property DataLen = new Property(7, Integer.class, 
				"DataLen", false, "DATALEN");
		public final static Property KDBZUNIT = new Property(8, String.class, 
				"KDBZUNIT", false, "KDBZUNIT");
		public final static Property ZHENDONGTYPE = new Property(9, String.class, 
				"ZHENDONGTYPE", false, "ZHENDONGTYPE");
		public final static Property VibParam_TX = new Property(10, String.class, 
				"VibParam_TX", false, "VIBPARAM_TX");
		public final static Property FeatureValue_TX = new Property(11, String.class, 
				"FeatureValue_TX", false, "FEATUREVALUE_TX");
		public final static Property FFTData_TX = new Property(12, byte[].class, 
				"FFTData_TX", false, "FFTDATA_TX");
	};
	
	public ProvisionalMDao(DaoConfig config) {
		super(config);
	}

	public ProvisionalMDao(DaoConfig config, TempMeasureDaoSession daoSession) {
		super(config, daoSession);
	}
	
	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "PROVISIONALM (" + //
				"DEVICENAME nvarchar(100) NOT NULL," + // 0: DEVICENAME
				"DJRESULT nvarchar(15)," + // 1: DJRESULT
				"DJTIME bigint PRIMARY KEY NOT NULL," + // 2: DJTIME
				"DATA8K IMAGE," + // 3: DATA8K
				"RATIO INTEGER," + // 4: RATIO
				"RATIO1 INTEGER," + // 5: RATIO1
				"RATE INTEGER," + // 6: RATE
				"DATALEN INTEGER," + // 7: DATALEN
				"KDBZUNIT nvarchar(6)," + //  8: KDBZUNIT
				"ZHENDONGTYPE nchar(1)," + //  9: ZHENDONGTYPE
				"VIBPARAM_TX nvarchar(100)," + //  10: VIBPARAM_TX
				"FEATUREVALUE_TX nvarchar(400)," + //  11: FEATUREVALUE_TX
				"FFTDATA_TX IMAGE" + //  12: FFTDATA_TX
				")");
	}
	
	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'PROVISIONALM'";
		db.execSQL(sql);
	}
	
	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, ProvisionalM entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getDeviceName());
		String DJResult = entity.getDJResult();
		if (DJResult != null) {
			stmt.bindString(2, DJResult);
		}
		stmt.bindString(3, (entity.getDJTime()).toString());
		
		byte[] Data8K = entity.getData8K();
		if (Data8K != null) {
			stmt.bindBlob(4, Data8K);
		}
		
		Integer Ratio = entity.getRatio();
		if (Ratio != null) {
			stmt.bindLong(5, Ratio);
		}

		Integer Ratio1 = entity.getRatio1();
		if (Ratio1 != null) {
			stmt.bindLong(6, Ratio1);
		}
		
		Integer Rate = entity.getRate();
		if (Rate != null) {
			stmt.bindLong(7, Rate);
		}

		Integer DataLen = entity.getDataLen();
		if (DataLen != null) {
			stmt.bindLong(8, DataLen);
		}
		
		String kdbzunit = entity.getKDBZUNIT();
		if (kdbzunit != null) {
			stmt.bindString(9, kdbzunit);
		}
		
		String zhendongtype = entity.getZHENDONGTYPE();
		if (zhendongtype != null) {
			stmt.bindString(10, zhendongtype);
		}
		
		String VibParam = entity.getVibParam_TX();
		if (VibParam != null) {
			stmt.bindString(11, VibParam);
		}
		
		String FeatureValue = entity.getFeatureValue_TX();
		if (FeatureValue != null) {
			stmt.bindString(12, FeatureValue);
		}
		
		byte[] FFTDATA = entity.getFFTData_TX();
		if (FFTDATA != null) {
			stmt.bindBlob(13, FFTDATA);
		}
	}
	
	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}
	
	/** @inheritdoc */
	@Override
	public ProvisionalM readEntity(Cursor cursor, int offset) {
		ProvisionalM entity = new ProvisionalM(
				//
				cursor.getString(offset + 0), // DEVICENAME
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // DJRESULT
				cursor.getString(offset + 2), // FILENAME
				cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3), //DATA8K
				cursor.isNull(offset + 4) ? null : cursor
						.getInt(offset + 4), // Ratio
				cursor.isNull(offset + 5) ? null : cursor
						.getInt(offset + 5), // Ratio1
				cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // Rate
				cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // DataLen
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // KDBZUNIT
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // ZHENDONGTYPE
				cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // VIBPARAM_TX
				cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // FEATUREVALUE_TX
				cursor.isNull(offset + 12) ? null : cursor.getBlob(offset + 12) // FFTDATA
			);
		return entity;
	}
	
	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, ProvisionalM entity, int offset) {
		entity.setDeviceName(cursor.getString(offset + 0));
		entity.setDJResult(cursor.isNull(offset + 1) ? null :cursor.getString(offset + 1));
		entity.setDJTime(cursor.getString((offset + 2)).toString());
		entity.setData8K(cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3));
		entity.setRatio(cursor.isNull(offset + 4) ? null : cursor
				.getInt(offset + 4));
		entity.setRatio1(cursor.isNull(offset + 5) ? null : cursor
				.getInt(offset + 5));
		entity.setRate(cursor.isNull(offset + 6) ? null : cursor
				.getInt(offset + 6));
		entity.setDataLen(cursor.isNull(offset + 7) ? null : cursor
				.getInt(offset + 7));
		entity.setKDBZUNIT(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setZHENDONGTYPE(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setVibParam_TX(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setFeatureValue_TX(cursor.isNull(offset + 11) ? null : cursor
				.getString(offset + 11));
		entity.setFFTData_TX(cursor.isNull(offset + 12) ? null : cursor.getBlob(offset + 12));
	}
	
	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(ProvisionalM entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(ProvisionalM entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	/** @inheritdoc */
	public long InsertProvisionalM(ProvisionalM entity) {
		long row = insert(entity);
		return row;
	}

	public void Operatesql(String sql) {
		SQLiteDatabase db = getDatabase();

		db.execSQL(sql);
	}
}