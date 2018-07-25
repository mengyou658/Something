package com.moons.xst.track.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.Z_DJ_Plan;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SRLINE_TREE_LAST_RESULT.
*/
public class SRLineTreeLastResultForResultDao extends AbstractDao<SRLineTreeLastResult, Void> {

    public static final String TABLENAME = "SRLINETREELASTRESULT";

    /**
     * Properties of entity SRLineTreeLastResult.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SRControlPoint_ID = new Property(0, String.class, "SRControlPoint_ID", false, "SRCONTROLPOINT_ID");
        public final static Property LastSRResult_TM = new Property(1, String.class, "LastSRResult_TM", false, "LASTSRRESULT_TM");
        public final static Property LastSRResult_TX = new Property(2, String.class, "LastSRResult_TX", false, "LASTSRRESULT_TX");
        public final static Property LastSRMemo_TX = new Property(3, String.class, "LastSRMemo_TX", false, "LASTSRMEMO_TX");
    };


    public SRLineTreeLastResultForResultDao(DaoConfig config) {
        super(config);
    }
    
    public SRLineTreeLastResultForResultDao(DaoConfig config, DJResultDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SRLINETREELASTRESULT' (" + //
                "'SRCONTROLPOINT_ID' TEXT NOT NULL ," + // 0: SRControlPoint_ID
                "'LASTSRRESULT_TM' TEXT," + // 1: LastSRResult_TM
                "'LASTSRRESULT_TX' TEXT," + // 2: LastSRResult_TX
                "'LASTSRMEMO_TX' TEXT);"); // 3: LastSRMemo_TX
        
        if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
	        DJDAOSession djdaoSession = ((AppContext) AppContext.baseContext.getApplicationContext())
					.getDJSession(AppContext.GetCurrLineID());
	        SRLineTreeLastResultDao dao = djdaoSession.getSRLineTreeLastResultDao();
			List<SRLineTreeLastResult> list = dao.loadAll();
			
			for (SRLineTreeLastResult info : list) {
				ContentValues cv = new ContentValues();
	            cv.put("SRCONTROLPOINT_ID", info.getSRControlPoint_ID());
	            cv.put("LASTSRRESULT_TM", info.getLastSRResult_TM());
	            cv.put("LASTSRRESULT_TX", info.getLastSRResult_TX());
	            cv.put("LASTSRMEMO_TX", info.getLastSRMemo_TX());
				db.insert(TABLENAME, "", cv);
			}
        }
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SRLINETREELASTRESULT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SRLineTreeLastResult entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getSRControlPoint_ID());
 
        String LastSRResult_TM = entity.getLastSRResult_TM();
        if (LastSRResult_TM != null) {
            stmt.bindString(2, LastSRResult_TM);
        }
 
        String LastSRResult_TX = entity.getLastSRResult_TX();
        if (LastSRResult_TX != null) {
            stmt.bindString(3, LastSRResult_TX);
        }
 
        String LastSRMemo_TX = entity.getLastSRMemo_TX();
        if (LastSRMemo_TX != null) {
            stmt.bindString(4, LastSRMemo_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public SRLineTreeLastResult readEntity(Cursor cursor, int offset) {
        SRLineTreeLastResult entity = new SRLineTreeLastResult( //
            cursor.getString(offset + 0), // SRControlPoint_ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // LastSRResult_TM
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // LastSRResult_TX
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // LastSRMemo_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SRLineTreeLastResult entity, int offset) {
        entity.setSRControlPoint_ID(cursor.getString(offset + 0));
        entity.setLastSRResult_TM(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLastSRResult_TX(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLastSRMemo_TX(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(SRLineTreeLastResult entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(SRLineTreeLastResult entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
}