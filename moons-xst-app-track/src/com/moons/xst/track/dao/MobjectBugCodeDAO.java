/**
 * @MobjectBugCodeDAO.java
 * @author LKZ
 * @2015-1-20
 */
package com.moons.xst.track.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.MobjectBugCodeInfo;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 *@com.moons.xst.track.dao
 *
 */
public class MobjectBugCodeDAO  extends AbstractDao<MobjectBugCodeInfo, Long> {

    public static final String TABLENAME = "Z_MOBJECTBUGCODE";

    /**
     * Properties of entity Z_MobjectBugCode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property MOBJECTBUGCODE_ID = new Property(0, int.class, "MOBJECTBUGCODE_ID", false, "MOBJECTBUGCODE_ID");
        public final static Property MOBJECTBUGCODE_CD = new Property(1, String.class, "MOBJECTBUGCODE_CD", false, "MOBJECTBUGCODE_CD");
        public final static Property MOBJECTBUGCODE_TX = new Property(2, String.class, "MOBJECTBUGCODE_TX", false, "MOBJECTBUGCODE_TX");
        public final static Property MOBJECTBUGCODETYPE_TX = new Property(3, String.class, "MOBJECTBUGCODETYPE_TX", false, "MOBJECTBUGCODETYPE_TX");
        public final static Property PARENTMOBJECTBUGCODE_ID = new Property(4, Integer.class, "PARENTMOBJECTBUGCODE_ID", false, "PARENTMOBJECTBUGCODE_ID");
        public final static Property PARENTLIST_TX = new Property(5, String.class, "PARENTLIST_TX", false, "PARENTLIST_TX");
        public final static Property ORG_ID = new Property(6, Integer.class, "ORG_ID", false, "ORG_ID");
        public final static Property ACTIVE_YN = new Property(7, String.class, "ACTIVE_YN", false, "ACTIVE_YN");
    };


    public MobjectBugCodeDAO(DaoConfig config) {
        super(config);
    }
    
    public MobjectBugCodeDAO(DaoConfig config, DJDAOSession daoSession) {
        super(config, daoSession);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MobjectBugCodeInfo entity) {
        stmt.clearBindings();
        
        stmt.bindLong(1, entity.getMOBJECTBUGCODE_ID());
 
        String MOBJECTBUGCODE_CD = entity.getMOBJECTBUGCODE_CD();
        if (MOBJECTBUGCODE_CD != null) {
            stmt.bindString(2, MOBJECTBUGCODE_CD);
        }
 
        String MOBJECTBUGCODE_TX = entity.getMOBJECTBUGCODE_TX();
        if (MOBJECTBUGCODE_TX != null) {
            stmt.bindString(3, MOBJECTBUGCODE_TX);
        }
 
        String MOBJECTBUGCODETYPE_TX = entity.getMOBJECTBUGCODETYPE_TX();
        if (MOBJECTBUGCODETYPE_TX != null) {
            stmt.bindString(4, MOBJECTBUGCODETYPE_TX);
        }
 
        Integer PARENTMOBJECTBUGCODE_ID = entity.getPARENTMOBJECTBUGCODE_ID();
        if (PARENTMOBJECTBUGCODE_ID != null) {
            stmt.bindLong(5, PARENTMOBJECTBUGCODE_ID);
        }
 
        String PARENTLIST_TX = entity.getPARENTLIST_TX();
        if (PARENTLIST_TX != null) {
            stmt.bindString(6, PARENTLIST_TX);
        }
 
        Integer ORG_ID = entity.getORG_ID();
        if (ORG_ID != null) {
            stmt.bindLong(7, ORG_ID);
        }
 
        String ACTIVE_YN = entity.getACTIVE_YN();
        if (ACTIVE_YN != null) {
            stmt.bindString(8, ACTIVE_YN);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MobjectBugCodeInfo readEntity(Cursor cursor, int offset) {
    	MobjectBugCodeInfo entity = new MobjectBugCodeInfo( 
            cursor.getInt(offset + 0), // MOBJECTBUGCODE_ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // MOBJECTBUGCODE_CD
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // MOBJECTBUGCODE_TX
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // MOBJECTBUGCODETYPE_TX
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // PARENTMOBJECTBUGCODE_ID
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // PARENTLIST_TX
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // ORG_ID
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // ACTIVE_YN
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MobjectBugCodeInfo entity, int offset) {
        entity.setMOBJECTBUGCODE_ID(cursor.getInt(offset + 0));
        entity.setMOBJECTBUGCODE_CD(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMOBJECTBUGCODE_TX(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMOBJECTBUGCODETYPE_TX(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPARENTMOBJECTBUGCODE_ID(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setPARENTLIST_TX(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setORG_ID(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setACTIVE_YN(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MobjectBugCodeInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MobjectBugCodeInfo entity) {
        if(entity != null) {
            return entity.getId();
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