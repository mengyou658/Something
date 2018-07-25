package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.ComDaoSession;
import com.moons.xst.track.bean.ComGPSPosition;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GPSPOSITION.
*/
public class ComGPSPositionDao extends AbstractDao<ComGPSPosition, String> {

    public static final String TABLENAME = "GPSPOSITION";

    /**
     * Properties of entity GPSPosition.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property XJQGUID_TX = new Property(0, String.class, "XJQGUID_TX", true, "XJQGUID_TX");
        public final static Property XJQ_CD = new Property(1, String.class, "XJQ_CD", false, "XJQ_CD");
        public final static Property Line_ID = new Property(2, String.class, "Line_ID", false, "LINE_ID");
        public final static Property AppUser_CD = new Property(3, String.class, "AppUser_CD", false, "APPUSER_CD");
        public final static Property AppUserName_TX = new Property(4, String.class, "AppUserName_TX", false, "APPUSERNAME_TX");
        public final static Property ID_CD = new Property(5, String.class, "ID_CD", false, "ID_CD");
        public final static Property Place_TX = new Property(6, String.class, "Place_TX", false, "PLACE_TX");
        public final static Property Longitude_TX = new Property(7, String.class, "Longitude_TX", false, "LONGITUDE_TX");
        public final static Property Latitude_TX = new Property(8, String.class, "Latitude_TX", false, "LATITUDE_TX");
        public final static Property UTC_DT = new Property(9, String.class, "UTC_DT", false, "UTC_DT");
        public final static Property GPSDISTANCE_TX = new Property(10, String.class, "GPSDISTANCE_TX", false, "GPSDISTANCE_TX");
        public final static Property GPSCOSTTIME_TX = new Property(11, String.class, "GPSCOSTTIME_TX", false, "GPSCOSTTIME_TX");
        public final static Property GPSPOINTTYPE_TX = new Property(12, String.class, "GPSPOINTTYPE_TX", false, "GPSPOINTTYPE_TX");
    };


    public ComGPSPositionDao(DaoConfig config) {
        super(config);
    }
    
    public ComGPSPositionDao(DaoConfig config, ComDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GPSPOSITION' (" + //
                "'XJQGUID_TX' TEXT PRIMARY KEY NOT NULL ," + // 0: XJQGUID_TX
                "'XJQ_CD' TEXT NOT NULL ," + // 1: XJQ_CD
                "'LINE_ID' TEXT NOT NULL ," + // 2: Line_ID
                "'APP_USER_CD' TEXT NOT NULL ," + // 3: AppUser_CD
                "'APPUSERNAME_TX' TEXT NOT NULL ," + // 4: AppUserName_TX
                "'ID_CD' TEXT NOT NULL ," + // 5: ID_CD
                "'PLACE_TX' TEXT NOT NULL ," + // 6: Place_TX
                "'LONGITUDE_TX' TEXT NOT NULL ," + // 7: Longitude_TX
                "'LATITUDE_TX' TEXT NOT NULL ," + // 8: Latitude_TX
                "'UTC_DT' TEXT NOT NULL ," + // 9: UTC_DT
                "'GPSDISTANCE_TX' TEXT NOT NULL ," + // 10: GPSDISTANCE_TX
                "'GPSCOSTTIME_TX' TEXT NOT NULL ," + // 11: GPSCOSTTIME_TX
                "'GPSPOINTTYPE_TX' TEXT NOT NULL );"); // 12: GPSPOINTTYPE_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GPSPOSITION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ComGPSPosition entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getXJQGUID_TX());
        stmt.bindString(2, entity.getXJQ_CD());
        stmt.bindString(3, entity.getLine_ID());
        stmt.bindString(4, entity.getAppUser_CD());
        stmt.bindString(5, entity.getAppUserName_TX());
        stmt.bindString(6, entity.getID_CD());
        stmt.bindString(7, entity.getPlace_TX());
        stmt.bindString(8, entity.getLongitude_TX());
        stmt.bindString(9, entity.getLatitude_TX());
        stmt.bindString(10, entity.getUTC_DT());
        stmt.bindString(11, entity.getGPSDISTANCE_TX());
        stmt.bindString(12, entity.getGPSCOSTTIME_TX());
        stmt.bindString(13, entity.getGPSPOINTTYPE_TX());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ComGPSPosition readEntity(Cursor cursor, int offset) {
        ComGPSPosition entity = new ComGPSPosition( //
            cursor.getString(offset + 0), // XJQGUID_TX
            cursor.getString(offset + 1), // XJQ_CD
            cursor.getString(offset + 2), // Line_ID
            cursor.getString(offset + 3), // AppUser_CD
            cursor.getString(offset + 4), // AppUserName_TX
            cursor.getString(offset + 5), // ID_CD
            cursor.getString(offset + 6), // Place_TX
            cursor.getString(offset + 7), // Longitude_TX
            cursor.getString(offset + 8), // Latitude_TX
            cursor.getString(offset + 9), // UTC_DT
            cursor.getString(offset + 10), // GPSDISTANCE_TX
            cursor.getString(offset + 11), // GPSCOSTTIME_TX
            cursor.getString(offset + 12) // GPSPOINTTYPE_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ComGPSPosition entity, int offset) {
        entity.setXJQGUID_TX(cursor.getString(offset + 0));
        entity.setXJQ_CD(cursor.getString(offset + 1));
        entity.setLine_ID(cursor.getString(offset + 2));
        entity.setAppUser_CD(cursor.getString(offset + 3));
        entity.setAppUserName_TX(cursor.getString(offset + 4));
        entity.setID_CD(cursor.getString(offset + 5));
        entity.setPlace_TX(cursor.getString(offset + 6));
        entity.setLongitude_TX(cursor.getString(offset + 7));
        entity.setLatitude_TX(cursor.getString(offset + 8));
        entity.setUTC_DT(cursor.getString(offset + 9));
        entity.setGPSDISTANCE_TX(cursor.getString(offset + 10));
        entity.setGPSCOSTTIME_TX(cursor.getString(offset + 11));
        entity.setGPSPOINTTYPE_TX(cursor.getString(offset + 12));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(ComGPSPosition entity, long rowId) {
        return entity.getXJQGUID_TX();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(ComGPSPosition entity) {
        if(entity != null) {
            return entity.getXJQGUID_TX();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}