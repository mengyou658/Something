package com.moons.xst.sqlite;

import com.moons.xst.track.dao.OperDetailResultDao;
import com.moons.xst.track.dao.OperMainResultDao;
import com.moons.xst.track.dao.Shift_REsultDao;
import com.moons.xst.track.dao.WH_DQTaskDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class TwoBillResultDaoMaster extends AbstractDaoMaster{
	public static final int SCHEMA_VERSION = 1000;
	private static final String TAG = "TwoBillResultDaoMaster";
	
	/** Creates underlying database table using DAOs. */
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		OperDetailResultDao.createTable(db, ifNotExists);
    	OperMainResultDao.createTable(db, ifNotExists);
    	Shift_REsultDao.createTable(db, ifNotExists);
    	WH_DQTaskDao.createTable(db, ifNotExists);
	}

	/** Drops underlying database table using DAOs. */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		OperDetailResultDao.dropTable(db, ifExists);
    	OperMainResultDao.dropTable(db, ifExists);
    	Shift_REsultDao.dropTable(db, ifExists);
    	WH_DQTaskDao.dropTable(db, ifExists);

	}
	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("greenDAO", "Creating tables for schema version "
					+ SCHEMA_VERSION);
			createAllTables(db, false);
		}
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i("greenDAO", "Upgrading schema from version " + oldVersion
					+ " to " + newVersion + " by dropping all tables");
			dropAllTables(db, true);
			onCreate(db);
		}
	}
	public TwoBillResultDaoMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		registerDaoClass(OperDetailResultDao.class);
        registerDaoClass(OperMainResultDao.class);
        registerDaoClass(Shift_REsultDao.class);
        registerDaoClass(WH_DQTaskDao.class);
	}

	@Override
	public TwoBillResultDaoSession newSession() {
		return new TwoBillResultDaoSession(db, IdentityScopeType.Session, daoConfigMap);
	}

	@Override
	public TwoBillResultDaoSession newSession(IdentityScopeType type) {
		return new TwoBillResultDaoSession(db, type, daoConfigMap);
	}

}
