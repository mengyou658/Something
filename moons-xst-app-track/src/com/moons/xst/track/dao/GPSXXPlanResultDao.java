package com.moons.xst.track.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.bean.GPSXXPlanResult;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GPSXXPLAN_RESULT.
*/
public class GPSXXPlanResultDao extends AbstractDao<GPSXXPlanResult, Void> {

    public static final String TABLENAME = "GPSXXPLAN_RESULT";

    /**
     * Properties of entity GPSXXPlanResult.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property XXUncheck_ID = new Property(0, Integer.class, "XXUncheck_ID", false, "XXUNCHECK_ID");
        public final static Property Partition_ID = new Property(1, Integer.class, "Partition_ID", false, "PARTITION_ID");
        public final static Property GpsXXItem_ID = new Property(2, Integer.class, "GpsXXItem_ID", false, "GPSXXITEM_ID");
        public final static Property GPSUnCheck_ID = new Property(3, Integer.class, "GPSUnCheck_ID", false, "GPSUN_CHECK_ID");
        public final static Property TaskStart_TM = new Property(4, String.class, "TaskStart_TM", false, "TASK_START_TM");
        public final static Property TaskEnd_TM = new Property(5, String.class, "TaskEnd_TM", false, "TASK_END_TM");
        public final static Property Query_DT = new Property(6, String.class, "Query_DT", false, "QUERY_DT");
        public final static Property Round_NR = new Property(7, Integer.class, "Round_NR", false, "ROUND_NR");
        public final static Property ShiftName_TX = new Property(8, String.class, "ShiftName_TX", false, "SHIFT_NAME_TX");
        public final static Property GPSPoint_ID = new Property(9, String.class, "GPSPoint_ID", false, "GPSPOINT_ID");
        public final static Property ShiftGroupName_TX = new Property(10, String.class, "ShiftGroupName_TX", false, "SHIFT_GROUP_NAME_TX");
        public final static Property Post_ID = new Property(11, Integer.class, "Post_ID", false, "POST_ID");
        public final static Property Post_CD = new Property(12, String.class, "Post_CD", false, "POST_CD");
        public final static Property PostName_TX = new Property(13, String.class, "PostName_TX", false, "POST_NAME_TX");
        public final static Property Appuser_ID = new Property(14, Integer.class, "Appuser_ID", false, "APPUSER_ID");
        public final static Property AppUser_CD = new Property(15, String.class, "AppUser_CD", false, "APP_USER_CD");
        public final static Property AppUserName_TX = new Property(16, String.class, "AppUserName_TX", false, "APP_USER_NAME_TX");
        public final static Property Complete_CD = new Property(17, String.class, "Complete_CD", false, "COMPLETE_CD");
        public final static Property Complete_TM = new Property(18, String.class, "Complete_TM", false, "COMPLETE_TM");
        public final static Property CheckUser_ID = new Property(19, Integer.class, "CheckUser_ID", false, "CHECK_USER_ID");
        public final static Property CheckUser_TX = new Property(20, String.class, "CheckUser_TX", false, "CHECK_USER_TX");
        public final static Property CheckUserPost_ID = new Property(21, Integer.class, "CheckUserPost_ID", false, "CHECK_USER_POST_ID");
        public final static Property ISMemo_YN = new Property(22, String.class, "ISMemo_YN", false, "ISMEMO_YN");
        public final static Property Memo_TX = new Property(23, String.class, "Memo_TX", false, "MEMO_TX");
        public final static Property AuditUserName_TX = new Property(24, String.class, "AuditUserName_TX", false, "AUDIT_USER_NAME_TX");
        public final static Property Audit_DT = new Property(25, String.class, "Audit_DT", false, "AUDIT_DT");
        public final static Property XXResult_TX = new Property(26, String.class, "XXResult_TX", false, "XXRESULT_TX");
        public final static Property XXDesc_TX = new Property(27, String.class, "XXDesc_TX", false, "XXDESC_TX");
    };


    public GPSXXPlanResultDao(DaoConfig config) {
        super(config);
    }
    
    public GPSXXPlanResultDao(DaoConfig config, DJResultDAOSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GPSXXPLAN_RESULT' (" + //
                "'XXUNCHECK_ID' INTEGER," + // 0: XXUncheck_ID
                "'PARTITION_ID' INTEGER," + // 1: Partition_ID
                "'GPSXXITEM_ID' INTEGER," + // 2: GpsXXItem_ID
                "'GPSUN_CHECK_ID' INTEGER," + // 3: GPSUnCheck_ID
                "'TASK_START_TM' TEXT," + // 4: TaskStart_TM
                "'TASK_END_TM' TEXT," + // 5: TaskEnd_TM
                "'QUERY_DT' TEXT," + // 6: Query_DT
                "'ROUND_NR' INTEGER," + // 7: Round_NR
                "'SHIFT_NAME_TX' TEXT," + // 8: ShiftName_TX
                "'GPSPOINT_ID' TEXT," + // 9: GPSPoint_ID
                "'SHIFT_GROUP_NAME_TX' TEXT," + // 10: ShiftGroupName_TX
                "'POST_ID' INTEGER," + // 11: Post_ID
                "'POST_CD' TEXT," + // 12: Post_CD
                "'POST_NAME_TX' TEXT," + // 13: PostName_TX
                "'APPUSER_ID' INTEGER," + // 14: Appuser_ID
                "'APP_USER_CD' TEXT," + // 15: AppUser_CD
                "'APP_USER_NAME_TX' TEXT," + // 16: AppUserName_TX
                "'COMPLETE_CD' TEXT," + // 17: Complete_CD
                "'COMPLETE_TM' TEXT," + // 18: Complete_TM
                "'CHECK_USER_ID' INTEGER," + // 19: CheckUser_ID
                "'CHECK_USER_TX' TEXT," + // 20: CheckUser_TX
                "'CHECK_USER_POST_ID' INTEGER," + // 21: CheckUserPost_ID
                "'ISMEMO_YN' TEXT," + // 22: ISMemo_YN
                "'MEMO_TX' TEXT," + // 23: Memo_TX
                "'AUDIT_USER_NAME_TX' TEXT," + // 24: AuditUserName_TX
                "'AUDIT_DT' TEXT," + // 25: Audit_DT
                "'XXRESULT_TX' TEXT," + // 26: XXResult_TX
                "'XXDESC_TX' TEXT);"); // 27: XXDesc_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GPSXXPLAN_RESULT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GPSXXPlanResult entity) {
        stmt.clearBindings();
 
        Integer XXUncheck_ID = entity.getXXUncheck_ID();
        if (XXUncheck_ID != null) {
            stmt.bindLong(1, XXUncheck_ID);
        }
 
        Integer Partition_ID = entity.getPartition_ID();
        if (Partition_ID != null) {
            stmt.bindLong(2, Partition_ID);
        }
 
        Integer GpsXXItem_ID = entity.getGpsXXItem_ID();
        if (GpsXXItem_ID != null) {
            stmt.bindLong(3, GpsXXItem_ID);
        }
 
        Integer GPSUnCheck_ID = entity.getGPSUnCheck_ID();
        if (GPSUnCheck_ID != null) {
            stmt.bindLong(4, GPSUnCheck_ID);
        }
 
        String TaskStart_TM = entity.getTaskStart_TM();
        if (TaskStart_TM != null) {
            stmt.bindString(5, TaskStart_TM);
        }
 
        String TaskEnd_TM = entity.getTaskEnd_TM();
        if (TaskEnd_TM != null) {
            stmt.bindString(6, TaskEnd_TM);
        }
 
        String Query_DT = entity.getQuery_DT();
        if (Query_DT != null) {
            stmt.bindString(7, Query_DT);
        }
 
        Integer Round_NR = entity.getRound_NR();
        if (Round_NR != null) {
            stmt.bindLong(8, Round_NR);
        }
 
        String ShiftName_TX = entity.getShiftName_TX();
        if (ShiftName_TX != null) {
            stmt.bindString(9, ShiftName_TX);
        }
 
        String GPSPoint_ID = entity.getGPSPoint_ID();
        if (GPSPoint_ID != null) {
            stmt.bindString(10, GPSPoint_ID);
        }
 
        String ShiftGroupName_TX = entity.getShiftGroupName_TX();
        if (ShiftGroupName_TX != null) {
            stmt.bindString(11, ShiftGroupName_TX);
        }
 
        Integer Post_ID = entity.getPost_ID();
        if (Post_ID != null) {
            stmt.bindLong(12, Post_ID);
        }
 
        String Post_CD = entity.getPost_CD();
        if (Post_CD != null) {
            stmt.bindString(13, Post_CD);
        }
 
        String PostName_TX = entity.getPostName_TX();
        if (PostName_TX != null) {
            stmt.bindString(14, PostName_TX);
        }
 
        Integer Appuser_ID = entity.getAppuser_ID();
        if (Appuser_ID != null) {
            stmt.bindLong(15, Appuser_ID);
        }
 
        String AppUser_CD = entity.getAppUser_CD();
        if (AppUser_CD != null) {
            stmt.bindString(16, AppUser_CD);
        }
 
        String AppUserName_TX = entity.getAppUserName_TX();
        if (AppUserName_TX != null) {
            stmt.bindString(17, AppUserName_TX);
        }
 
        String Complete_CD = entity.getComplete_CD();
        if (Complete_CD != null) {
            stmt.bindString(18, Complete_CD);
        }
 
        String Complete_TM = entity.getComplete_TM();
        if (Complete_TM != null) {
            stmt.bindString(19, Complete_TM);
        }
 
        Integer CheckUser_ID = entity.getCheckUser_ID();
        if (CheckUser_ID != null) {
            stmt.bindLong(20, CheckUser_ID);
        }
 
        String CheckUser_TX = entity.getCheckUser_TX();
        if (CheckUser_TX != null) {
            stmt.bindString(21, CheckUser_TX);
        }
 
        Integer CheckUserPost_ID = entity.getCheckUserPost_ID();
        if (CheckUserPost_ID != null) {
            stmt.bindLong(22, CheckUserPost_ID);
        }
 
        String ISMemo_YN = entity.getISMemo_YN();
        if (ISMemo_YN != null) {
            stmt.bindString(23, ISMemo_YN);
        }
 
        String Memo_TX = entity.getMemo_TX();
        if (Memo_TX != null) {
            stmt.bindString(24, Memo_TX);
        }
 
        String AuditUserName_TX = entity.getAuditUserName_TX();
        if (AuditUserName_TX != null) {
            stmt.bindString(25, AuditUserName_TX);
        }
 
        String Audit_DT = entity.getAudit_DT();
        if (Audit_DT != null) {
            stmt.bindString(26, Audit_DT);
        }
 
        String XXResult_TX = entity.getXXResult_TX();
        if (XXResult_TX != null) {
            stmt.bindString(27, XXResult_TX);
        }
 
        String XXDesc_TX = entity.getXXDesc_TX();
        if (XXDesc_TX != null) {
            stmt.bindString(28, XXDesc_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public GPSXXPlanResult readEntity(Cursor cursor, int offset) {
        GPSXXPlanResult entity = new GPSXXPlanResult( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // XXUncheck_ID
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // Partition_ID
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // GpsXXItem_ID
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // GPSUnCheck_ID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // TaskStart_TM
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // TaskEnd_TM
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Query_DT
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // Round_NR
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ShiftName_TX
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // GPSPoint_ID
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ShiftGroupName_TX
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // Post_ID
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Post_CD
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // PostName_TX
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // Appuser_ID
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // AppUser_CD
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // AppUserName_TX
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // Complete_CD
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // Complete_TM
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // CheckUser_ID
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // CheckUser_TX
            cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21), // CheckUserPost_ID
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // ISMemo_YN
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // Memo_TX
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // AuditUserName_TX
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // Audit_DT
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26), // XXResult_TX
            cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27) // XXDesc_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GPSXXPlanResult entity, int offset) {
        entity.setXXUncheck_ID(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setPartition_ID(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setGpsXXItem_ID(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setGPSUnCheck_ID(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTaskStart_TM(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTaskEnd_TM(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setQuery_DT(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRound_NR(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setShiftName_TX(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGPSPoint_ID(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setShiftGroupName_TX(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPost_ID(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setPost_CD(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPostName_TX(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAppuser_ID(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setAppUser_CD(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setAppUserName_TX(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setComplete_CD(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setComplete_TM(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCheckUser_ID(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
        entity.setCheckUser_TX(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCheckUserPost_ID(cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21));
        entity.setISMemo_YN(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setMemo_TX(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setAuditUserName_TX(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setAudit_DT(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setXXResult_TX(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
        entity.setXXDesc_TX(cursor.isNull(offset + 27) ? null : cursor.getString(offset + 27));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(GPSXXPlanResult entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(GPSXXPlanResult entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
	/*
	 * 加载数据（供实时上传巡线到位数据）
	 * 
	 * @param loadcount
	 * 
	 * @return
	 */
	public List<GPSXXPlanResult> loadforUploadJIT(int loadcount) {
		String sqlString = "select * from GPSXXPLAN_RESULT order by Complete_TM desc limit 0,"
				+ loadcount;
		Cursor cursor = db.rawQuery(sqlString, null);
		return loadAllAndCloseCursor(cursor);
	}
	
	/**
	 * 删除一组已上传数据
	 * 
	 * @param uploadedData
	 */
	public void deleteUploadedData(List<GPSXXPlanResult> uploadedData) {
		for (GPSXXPlanResult gpsplanResult : uploadedData) {
			deleteDatabyCondition(String.valueOf(gpsplanResult.getGpsXXItem_ID()),
					gpsplanResult.getComplete_TM());
		}
	}
	
	public void deleteDatabyCondition(String _id, String _completetm) {
		String sql = "delete from GPSXXPLAN_RESULT where GpsXXItem_ID ='"
				+ _id + "' and COMPLETE_TM = '" + _completetm + "'";
		db.execSQL(sql);
	}
    
}