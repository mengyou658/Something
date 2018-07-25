package com.moons.xst.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moons.xst.track.dao.ComGPSPositionDao;
import com.moons.xst.track.dao.GPSMobjectBugResultDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesDao;
import com.moons.xst.track.dao.GPSXXTempTaskDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1,000): knows all DAOs.
*/
public class ComDaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1000;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
    	GPSXXTempTaskDao.createTable(db, ifNotExists);
        ComGPSPositionDao.createTable(db, ifNotExists);
        GPSMobjectBugResultDao.createTable(db, ifNotExists);
        GPSMobjectBugResultForFilesDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
    	GPSXXTempTaskDao.dropTable(db, ifExists);
        ComGPSPositionDao.dropTable(db, ifExists);
        GPSMobjectBugResultDao.dropTable(db, ifExists);
        GPSMobjectBugResultForFilesDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
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
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public ComDaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(GPSXXTempTaskDao.class);
        registerDaoClass(ComGPSPositionDao.class);
        registerDaoClass(GPSMobjectBugResultDao.class);
		registerDaoClass(GPSMobjectBugResultForFilesDao.class);
    }
    
    @Override
	public ComDaoSession newSession() {
        return new ComDaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    @Override
	public ComDaoSession newSession(IdentityScopeType type) {
        return new ComDaoSession(db, type, daoConfigMap);
    }
    
}
