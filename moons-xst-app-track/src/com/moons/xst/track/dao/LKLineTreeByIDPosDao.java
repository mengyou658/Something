package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.LKLineTreeByIDPos;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LKLINE_TREE_BY_IDPOS.
*/
public class LKLineTreeByIDPosDao extends AbstractDao<LKLineTreeByIDPos, Void> {

    public static final String TABLENAME = "LKLINETREEBYIDPOS";

    /**
     * Properties of entity LKLineTreeByIDPos.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property IDPos_ID = new Property(0, String.class, "IDPos_ID", false, "IDPOS_ID");
        public final static Property LKControlPoint_ID = new Property(1, String.class, "LKControlPoint_ID", false, "LKCONTROLPOINT_ID");
        public final static Property Name_TX = new Property(2, String.class, "Name_TX", false, "NAME_TX");
    };


    public LKLineTreeByIDPosDao(DaoConfig config) {
        super(config);
    }
    
    public LKLineTreeByIDPosDao(DaoConfig config, DJDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LKLINE_TREE_BY_IDPOS' (" + //
                "'IDPOS_ID' TEXT NOT NULL ," + // 0: IDPos_ID
                "'LKCONTROL_POINT_ID' TEXT NOT NULL ," + // 1: LKControlPoint_ID
                "'NAME_TX' TEXT NOT NULL );"); // 2: Name_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LKLINE_TREE_BY_IDPOS'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LKLineTreeByIDPos entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getIDPos_ID());
        stmt.bindString(2, entity.getLKControlPoint_ID());
        stmt.bindString(3, entity.getName_TX());
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public LKLineTreeByIDPos readEntity(Cursor cursor, int offset) {
        LKLineTreeByIDPos entity = new LKLineTreeByIDPos( //
            cursor.getString(offset + 0), // IDPos_ID
            cursor.getString(offset + 1), // LKControlPoint_ID
            cursor.getString(offset + 2) // Name_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LKLineTreeByIDPos entity, int offset) {
        entity.setIDPos_ID(cursor.getString(offset + 0));
        entity.setLKControlPoint_ID(cursor.getString(offset + 1));
        entity.setName_TX(cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(LKLineTreeByIDPos entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(LKLineTreeByIDPos entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}