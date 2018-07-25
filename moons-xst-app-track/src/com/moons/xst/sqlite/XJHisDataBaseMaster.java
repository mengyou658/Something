package com.moons.xst.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.moons.xst.track.dao.XJ_MObjectStateHisDao;
import com.moons.xst.track.dao.XJ_ResultHisDao;
import com.moons.xst.track.dao.XJ_TaskIDPosHisDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class XJHisDataBaseMaster extends AbstractDaoMaster {
	public static final int SCHEMA_VERSION = 1003;
	private static final String TAG = "XJHisDataBaseMaster";

	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createAllTables(db, false);
		}
	}

	// 创建数据库表的操作
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		XJ_ResultHisDao.createTable(db, ifNotExists);
		XJ_TaskIDPosHisDao.createTable(db, ifNotExists);
		XJ_MObjectStateHisDao.createTable(db, ifNotExists);
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*dropAllTables(db, true);
			onCreate(db);*/
			
			// dropAllTables(db, true);
			// onCreate(db);
			if(oldVersion==1000){
				// 1000升级1001
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'DataType_CD' nvarchar(8)");
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'ZhenDong_Type' nchar(1)");
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'FFTDATA_TX' BLOB");
				
				//1001升到1002
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'TransException_YN' nchar(1)");
				
				//1002升到1003
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'AlarmDesc_TX' nvarchar(40)");
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'NoticeType_TX' nvarchar(20)");
			}
			if(oldVersion==1001){
				
				//1001升到1002
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'TransException_YN' nchar(1)");
				
				//1002升到1003
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'AlarmDesc_TX' nvarchar(40)");
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'NoticeType_TX' nvarchar(20)");
			}
			if(oldVersion==1002){
				//1002升到1003
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'AlarmDesc_TX' nvarchar(40)");
				db.execSQL("ALTER TABLE DJ_RESULTHIS ADD 'NoticeType_TX' nvarchar(20)");
			}
			
		}
	}

	/**
	 * Drops underlying database table using DAOs. 删除数据库表的操作
	 * 
	 */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		XJ_ResultHisDao.dropTable(db, ifExists);
		XJ_TaskIDPosHisDao.dropTable(db, ifExists);
		XJ_MObjectStateHisDao.dropTable(db, ifExists);
	}

	public XJHisDataBaseMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		registerDaoClass(XJ_ResultHisDao.class);
		registerDaoClass(XJ_TaskIDPosHisDao.class);
		registerDaoClass(XJ_MObjectStateHisDao.class);
	}

	@Override
	public AbstractDaoSession newSession() {
		return new XJHisDataBaseSession(db, IdentityScopeType.None,
				daoConfigMap);
	}

	@Override
	public AbstractDaoSession newSession(IdentityScopeType type) {
		return new XJHisDataBaseSession(db, type, daoConfigMap);
	}
}
