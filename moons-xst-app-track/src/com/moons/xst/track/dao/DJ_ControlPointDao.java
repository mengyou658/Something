package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.DJ_ControlPoint;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table DJ_CONTROL_POINT.
 */
public class DJ_ControlPointDao extends AbstractDao<DJ_ControlPoint, Void> {

	public static final String TABLENAME = "DJ_CONTROLPOINT";

	/**
	 * Properties of entity DJ_ControlPoint.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property ControlPoint_ID = new Property(0,
				String.class, "ControlPoint_ID", false, "CONTROLPOINT_ID");
		public final static Property Name_TX = new Property(1, String.class,
				"Name_TX", false, "NAME_TX");
		public final static Property SRPlan_YN = new Property(2, boolean.class,
				"SRPlan_YN", false, "SRPLAN_YN");
		public final static Property LKPlan_YN = new Property(3, boolean.class,
				"LKPlan_YN", false, "LKPLAN_YN");
		public final static Property SortNo_NR = new Property(4, int.class,
				"SortNo_NR", false, "SORTNO_NR");
		public final static Property LastSRResult_TM = new Property(5,
				String.class, "LastSRResult_TM", false, "LASTSRRESULT_TM");
		public final static Property LastSRResult_TX = new Property(6,
				String.class, "LastSRResult_TX", false, "LASTSRRESULT_TX");
		public final static Property TransInfo_TX = new Property(7,
				String.class, "TransInfo_TX", false, "TRANSINFO_TX");
	};

	public DJ_ControlPointDao(DaoConfig config) {
		super(config);
	}

	public DJ_ControlPointDao(DaoConfig config, DJDAOSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'DJ_CONTROLPOINT' (" + //
				"'CONTROLPOINT_ID' TEXT NOT NULL ," + // 0: ControlPoint_ID
				"'NAME_TX' TEXT NOT NULL ," + // 1: Name_TX
				"'SRPLAN_YN' INTEGER NOT NULL ," + // 2: SRPlan_YN
				"'LKPLAN_YN' INTEGER NOT NULL ," + // 3: LKPlan_YN
				"'SORTNO_NR' INTEGER NOT NULL ," + // 4: SortNo_NR
				"'LASTSRRESULT_TM' TEXT NOT NULL ," + // 5: LastSRResult_TM
				"'LASTSRRESULT_TX' TEXT NOT NULL ," + // 6: LastSRResult_TX
				"'TRANS_INFO_TX' TEXT NOT NULL );"); // 7: TransInfo_TX
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'DJ_CONTROL_POINT'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, DJ_ControlPoint entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getControlPoint_ID());
		stmt.bindString(2, entity.getName_TX());
		stmt.bindLong(3, entity.getSRPlan_YN() ? 1l : 0l);
		stmt.bindLong(4, entity.getLKPlan_YN() ? 1l : 0l);
		stmt.bindLong(5, entity.getSortNo_NR());
		stmt.bindString(6, entity.getLastSRResult_TM());
		stmt.bindString(7, entity.getLastSRResult_TX());
		stmt.bindString(8, entity.getTransInfo_TX());
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public DJ_ControlPoint readEntity(Cursor cursor, int offset) {
		DJ_ControlPoint entity = new DJ_ControlPoint( //
				cursor.getString(offset + 0), // ControlPoint_ID
				cursor.getString(offset + 1), // Name_TX
				cursor.getShort(offset + 2) != 0, // SRPlan_YN
				cursor.getShort(offset + 3) != 0, // LKPlan_YN
				cursor.getInt(offset + 4), // SortNo_NR
				cursor.getString(offset + 5), // LastSRResult_TM
				cursor.getString(offset + 6), // LastSRResult_TX
				cursor.getString(offset + 7) // TransInfo_TX
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, DJ_ControlPoint entity, int offset) {
		entity.setControlPoint_ID(cursor.getString(offset + 0));
		entity.setName_TX(cursor.getString(offset + 1));
		entity.setSRPlan_YN(cursor.getShort(offset + 2) != 0);
		entity.setLKPlan_YN(cursor.getShort(offset + 3) != 0);
		entity.setSortNo_NR(cursor.getInt(offset + 4));
		entity.setLastSRResult_TM(cursor.getString(offset + 5));
		entity.setLastSRResult_TX(cursor.getString(offset + 6));
		entity.setTransInfo_TX(cursor.getString(offset + 7));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(DJ_ControlPoint entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(DJ_ControlPoint entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<DJ_ControlPoint> getControlPoint(String id) {
		String sqlString = "select * from DJ_ControlPoint Where ControlPoint_ID ='"
				+ id + "'";
		return loadAllAndCloseCursor(db.rawQuery(sqlString, null));
	}

	/**
	 * 更新启停列表的最新状态
	 * 
	 * @param entity
	 */
	public void updateSR(DJ_ControlPoint entity) {
		String sqlString = "update DJ_ControlPoint set LASTSRRESULT_TM ='"
				+ entity.getLastSRResult_TM() + "',LASTSRRESULT_TX ='"
				+ entity.getLastSRResult_TX() + "'  Where ControlPoint_ID ='"
				+ entity.getControlPoint_ID() + "'";
		db.execSQL(sqlString);
	}
	
	/**
	 * 获取控制点信息
	 * @return
	 */
	public List<DJ_ControlPoint> loadAllCP() {
		String sqlString = "select DC.ControlPoint_ID,DC.Name_TX,DC.SRPlan_YN,DC.LKPlan_YN,"
                         + "DC.SortNo_NR,SC.LASTSRRESULT_TM,SC.LASTSRRESULT_TX,DC.TransInfo_TX"
                         + " From DJ_ControlPoint DC"
                         + " LEFT JOIN SRLineTreeLastResult SC"
                         + " ON DC.ControlPoint_ID = SC.SRControlPoint_ID";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
		
	}
}
