package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.Z_ShiftGroup;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Z_SHIFT_GROUP.
*/
public class Z_ShiftGroupDao extends AbstractDao<Z_ShiftGroup, Void> {

    public static final String TABLENAME = "Z_SHIFTGROUP";

    /**
     * Properties of entity Z_ShiftGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ShiftGroup_CD = new Property(0, String.class, "ShiftGroup_CD", false, "SHIFTGROUP_CD");
        public final static Property ShiftGroupName_TX = new Property(1, String.class, "ShiftGroupName_TX", false, "SHIFTGROUPNAME_TX");
    };


    public Z_ShiftGroupDao(DaoConfig config) {
        super(config);
    }
    
    public Z_ShiftGroupDao(DaoConfig config, DJDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Z_SHIFT_GROUP' (" + //
                "'SHIFT_GROUP_CD' TEXT NOT NULL ," + // 0: ShiftGroup_CD
                "'SHIFT_GROUP_NAME_TX' TEXT NOT NULL );"); // 1: ShiftGroupName_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Z_SHIFT_GROUP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Z_ShiftGroup entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getShiftGroup_CD());
        stmt.bindString(2, entity.getShiftGroupName_TX());
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Z_ShiftGroup readEntity(Cursor cursor, int offset) {
        Z_ShiftGroup entity = new Z_ShiftGroup( //
            cursor.getString(offset + 0), // ShiftGroup_CD
            cursor.getString(offset + 1) // ShiftGroupName_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Z_ShiftGroup entity, int offset) {
        entity.setShiftGroup_CD(cursor.getString(offset + 0));
        entity.setShiftGroupName_TX(cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Z_ShiftGroup entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Z_ShiftGroup entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
