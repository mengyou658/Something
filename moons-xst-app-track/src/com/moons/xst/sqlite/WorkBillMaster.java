package com.moons.xst.sqlite;

import com.moons.xst.track.dao.UsersDao;
import com.moons.xst.track.dao.Work_BillDao;
import com.moons.xst.track.dao.Work_Detail_BillDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 3): knows all DAOs.
*/
public class WorkBillMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        Work_BillDao.createTable(db, ifNotExists);
        UsersDao.createTable(db, ifNotExists);
        Work_Detail_BillDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        Work_BillDao.dropTable(db, ifExists);
        UsersDao.dropTable(db, ifExists);
        Work_Detail_BillDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
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
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
           // dropAllTables(db, true);
            onCreate(db);
        }
    }

    public WorkBillMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(Work_BillDao.class);
        registerDaoClass(UsersDao.class);
        registerDaoClass(Work_Detail_BillDao.class);
    }
    
    public WorkBillSession newSession() {
        return new WorkBillSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public WorkBillSession newSession(IdentityScopeType type) {
        return new WorkBillSession(db, type, daoConfigMap);
    }
    
}