package com.moons.xst.sqlite;

import com.moons.xst.track.dao.Operate_BillDao;
import com.moons.xst.track.dao.Operate_DetailBillDao;
import com.moons.xst.track.dao.UsersDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class TwoBillMaster extends AbstractDaoMaster {

	public static final int SCHEMA_VERSION = 1000;
	private static final String TAG = "TwoBillMaster";

	/** Creates underlying database table using DAOs. */
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		Operate_BillDao.createTable(db, ifNotExists);
		Operate_DetailBillDao.createTable(db, ifNotExists);
		UsersDao.createTable(db, ifNotExists);
	}

	/** Drops underlying database table using DAOs. */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		Operate_BillDao.dropTable(db, ifExists);
		Operate_DetailBillDao.dropTable(db, ifExists);
		UsersDao.dropTable(db, ifExists);
	}

	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("greenDAO", "Creating tables for schema version "
					+ SCHEMA_VERSION);
			//createAllTables(db, false);
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
			//dropAllTables(db, true);
			onCreate(db);
		}
	}

	public TwoBillMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		registerDaoClass(Operate_BillDao.class);
		registerDaoClass(Operate_DetailBillDao.class);
		registerDaoClass(UsersDao.class);
	}

	@Override
	public TwoBillSession newSession() {
		return new TwoBillSession(db, IdentityScopeType.Session, daoConfigMap);
	}

	@Override
	public TwoBillSession newSession(IdentityScopeType type) {
		return new TwoBillSession(db, type, daoConfigMap);
	}

}
