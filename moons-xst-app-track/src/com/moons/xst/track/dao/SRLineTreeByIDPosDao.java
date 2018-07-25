package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.SRLineTreeByIDPos;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SRLINE_TREE_BY_IDPOS.
*/
public class SRLineTreeByIDPosDao extends AbstractDao<SRLineTreeByIDPos, Void> {

    public static final String TABLENAME = "SRLINETREEBYIDPOS";

    /**
     * Properties of entity SRLineTreeByIDPos.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property IDPos_ID = new Property(0, String.class, "IDPos_ID", false, "IDPOS_ID");
        public final static Property SRControlPoint_ID = new Property(1, String.class, "SRControlPoint_ID", false, "SRCONTROLPOINT_ID");
        public final static Property Name_TX = new Property(2, int.class, "Name_TX", false, "NAME_TX");
        public final static Property TransInfo_TX = new Property(3, String.class, "TransInfo_TX", false, "TRANSINFO_TX");
    };


    public SRLineTreeByIDPosDao(DaoConfig config) {
        super(config);
    }
    
    public SRLineTreeByIDPosDao(DaoConfig config, DJDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SRLINE_TREE_BY_IDPOS' (" + //
                "'IDPOS_ID' TEXT NOT NULL ," + // 0: IDPos_ID
                "'SRCONTROL_POINT_ID' TEXT NOT NULL ," + // 1: SRControlPoint_ID
                "'NAME_TX' INTEGER NOT NULL ," + // 2: Name_TX
                "'TRANS_INFO_TX' TEXT);"); // 3: TransInfo_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SRLINE_TREE_BY_IDPOS'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SRLineTreeByIDPos entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getIDPos_ID());
        stmt.bindString(2, entity.getSRControlPoint_ID());
        stmt.bindLong(3, entity.getName_TX());
 
        String TransInfo_TX = entity.getTransInfo_TX();
        if (TransInfo_TX != null) {
            stmt.bindString(4, TransInfo_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public SRLineTreeByIDPos readEntity(Cursor cursor, int offset) {
        SRLineTreeByIDPos entity = new SRLineTreeByIDPos( //
            cursor.getString(offset + 0), // IDPos_ID
            cursor.getString(offset + 1), // SRControlPoint_ID
            cursor.getInt(offset + 2), // Name_TX
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // TransInfo_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SRLineTreeByIDPos entity, int offset) {
        entity.setIDPos_ID(cursor.getString(offset + 0));
        entity.setSRControlPoint_ID(cursor.getString(offset + 1));
        entity.setName_TX(cursor.getInt(offset + 2));
        entity.setTransInfo_TX(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(SRLineTreeByIDPos entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(SRLineTreeByIDPos entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}