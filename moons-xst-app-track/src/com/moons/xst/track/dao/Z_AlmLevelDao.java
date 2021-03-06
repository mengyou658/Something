package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.Z_AlmLevel;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Z_ALM_LEVEL.
*/
public class Z_AlmLevelDao extends AbstractDao<Z_AlmLevel, Void> {

    public static final String TABLENAME = "Z_ALMLEVEL";

    /**
     * Properties of entity Z_AlmLevel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property AlmLevel_ID = new Property(0, int.class, "AlmLevel_ID", false, "ALMLEVEL_ID");
        public final static Property Name_TX = new Property(1, String.class, "Name_TX", false, "NAME_TX");
    };


    public Z_AlmLevelDao(DaoConfig config) {
        super(config);
    }
    
    public Z_AlmLevelDao(DaoConfig config, DJDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Z_ALM_LEVEL' (" + //
                "'ALM_LEVEL_ID' INTEGER NOT NULL ," + // 0: AlmLevel_ID
                "'NAME_TX' TEXT);"); // 1: Name_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Z_ALM_LEVEL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Z_AlmLevel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getAlmLevel_ID());
 
        String Name_TX = entity.getName_TX();
        if (Name_TX != null) {
            stmt.bindString(2, Name_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Z_AlmLevel readEntity(Cursor cursor, int offset) {
        Z_AlmLevel entity = new Z_AlmLevel( //
            cursor.getInt(offset + 0), // AlmLevel_ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // Name_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Z_AlmLevel entity, int offset) {
        entity.setAlmLevel_ID(cursor.getInt(offset + 0));
        entity.setName_TX(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Z_AlmLevel entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Z_AlmLevel entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
