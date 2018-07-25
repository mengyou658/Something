package com.moons.xst.track.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.Z_Condition;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Z_CONDITION.
*/
public class Z_ConditionForResultDao extends AbstractDao<Z_Condition, Void> {

    public static final String TABLENAME = "Z_CONDITION";

    /**
     * Properties of entity Z_Condition.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Condition_id = new Property(0, String.class, "Condition_id", false, "CONDITION_ID");
        public final static Property ParentCon_id = new Property(1, String.class, "ParentCon_id", false, "PARENTCON_ID");
        public final static Property ConName_TX = new Property(2, String.class, "ConName_TX", false, "CONNAME_TX");
        public final static Property ConValue_TX = new Property(3, String.class, "ConValue_TX", false, "CONVALUE_TX");
        public final static Property ConType_TX = new Property(4, String.class, "ConType_TX", false, "CONTYPE_TX");
        public final static Property ConLevel_TX = new Property(5, String.class, "ConLevel_TX", false, "CONLEVEL_TX");
        public final static Property TransInfo_TX = new Property(6, String.class, "TransInfo_TX", false, "TRANSINFO_TX");
        public final static Property LevelNum_TX = new Property(7, String.class, "LevelNum_TX", false, "LEVELNUM_TX");
        public final static Property Sort_TX = new Property(8, String.class, "Sort_TX", false, "SORT_TX");
    };


    public Z_ConditionForResultDao(DaoConfig config) {
        super(config);
    }
    
    public Z_ConditionForResultDao(DaoConfig config, DJResultDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Z_CONDITION' (" + //
                "'CONDITION_ID' TEXT NOT NULL ," + // 0: Condition_id
                "'PARENTCON_ID' TEXT," + // 1: ParentCon_id
                "'CONNAME_TX' TEXT," + // 2: ConName_TX
                "'CONVALUE_TX' TEXT," + // 3: ConValue_TX
                "'CONTYPE_TX' TEXT," + // 4: ConType_TX
                "'CONLEVEL_TX' TEXT," + // 5: ConLevel_TX
                "'TRANSINFO_TX' TEXT," + // 6: TransInfo_TX
                "'LEVELNUM_TX' TEXT," + // 7: LevelNum_TX
                "'SORT_TX' TEXT);"); // 8: Sort_TX
        
        if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
	        DJDAOSession djdaoSession = ((AppContext) AppContext.baseContext.getApplicationContext())
					.getDJSession(AppContext.GetCurrLineID());
	        Z_ConditionDao dao = djdaoSession.getZ_ConditionDao();
			List<Z_Condition> conditions = dao.loadAll();
			
			for (Z_Condition info : conditions) {
				ContentValues cv = new ContentValues();
	            cv.put("CONDITION_ID", info.getCondition_id());
	            cv.put("PARENTCON_ID", info.getParentCon_id());
	            cv.put("CONNAME_TX", info.getConName_TX());
	            cv.put("CONVALUE_TX", info.getConValue_TX());
	            cv.put("CONTYPE_TX", info.getConType_TX());
	            cv.put("CONLEVEL_TX", info.getConLevel_TX());
	            cv.put("TRANSINFO_TX", info.getTransInfo_TX());
	            cv.put("LEVELNUM_TX", info.getLevelNum_TX());
	            cv.put("SORT_TX", info.getSort_TX());
				db.insert(TABLENAME, "", cv);
			}
        }
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Z_CONDITION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Z_Condition entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getCondition_id());
 
        String ParentCon_id = entity.getParentCon_id();
        if (ParentCon_id != null) {
            stmt.bindString(2, ParentCon_id);
        }
 
        String ConName_TX = entity.getConName_TX();
        if (ConName_TX != null) {
            stmt.bindString(3, ConName_TX);
        }
 
        String ConValue_TX = entity.getConValue_TX();
        if (ConValue_TX != null) {
            stmt.bindString(4, ConValue_TX);
        }
 
        String ConType_TX = entity.getConType_TX();
        if (ConType_TX != null) {
            stmt.bindString(5, ConType_TX);
        }
 
        String ConLevel_TX = entity.getConLevel_TX();
        if (ConLevel_TX != null) {
            stmt.bindString(6, ConLevel_TX);
        }
 
        String TransInfo_TX = entity.getTransInfo_TX();
        if (TransInfo_TX != null) {
            stmt.bindString(7, TransInfo_TX);
        }
 
        String LevelNum_TX = entity.getLevelNum_TX();
        if (LevelNum_TX != null) {
            stmt.bindString(8, LevelNum_TX);
        }
 
        String Sort_TX = entity.getSort_TX();
        if (Sort_TX != null) {
            stmt.bindString(9, Sort_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public Z_Condition readEntity(Cursor cursor, int offset) {
        Z_Condition entity = new Z_Condition( //
            cursor.getString(offset + 0), // Condition_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ParentCon_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ConName_TX
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ConValue_TX
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ConType_TX
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ConLevel_TX
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // TransInfo_TX
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // LevelNum_TX
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // Sort_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Z_Condition entity, int offset) {
        entity.setCondition_id(cursor.getString(offset + 0));
        entity.setParentCon_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setConName_TX(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConValue_TX(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setConType_TX(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setConLevel_TX(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTransInfo_TX(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLevelNum_TX(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSort_TX(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(Z_Condition entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(Z_Condition entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    public List<Z_Condition> loadAll() {
    	String sqlString = "Select * from Z_CONDITION Order By Sort_TX";
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
    }
}
