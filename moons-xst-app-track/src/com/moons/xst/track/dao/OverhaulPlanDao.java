package com.moons.xst.track.dao;

import java.lang.reflect.Array;
import java.util.List;

import android.R.integer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.OverhaulDaoSession;
import com.moons.xst.track.bean.OverhaulPlan;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table JX_JXPLAN_HNYN.
*/
public class OverhaulPlanDao extends AbstractDao<OverhaulPlan, Void> {

    public static final String TABLENAME = "JX_JXPlanHNYN";

    /**
     * Properties of entity JX_JXPlanHNYN.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property JXPlan_ID = new Property(0, int.class, "JXPlan_ID", false, "JXPLAN_ID");
        public final static Property JXPlan_CD = new Property(1, String.class, "JXPlan_CD", false, "JXPLAN_CD");
        public final static Property Area_ID = new Property(2, Integer.class, "Area_ID", false, "AREA_ID");
        public final static Property Spec_ID = new Property(3, Integer.class, "Spec_ID", false, "SPEC_ID");
        public final static Property ZYX_ID = new Property(4, int.class, "ZYX_ID", false, "ZYX_ID");
        public final static Property JXType_ID = new Property(5, int.class, "JXType_ID", false, "JXTYPE_ID");
        public final static Property PlanName_TX = new Property(6, String.class, "PlanName_TX", false, "PLAN_NAME_TX");
        public final static Property PlanContent_TX = new Property(7, String.class, "PlanContent_TX", false, "PLAN_CONTENT_TX");
        public final static Property PlanStart_DT = new Property(8, String.class, "PlanStart_DT", false, "PLAN_START_DT");
        public final static Property PlanEnd_DT = new Property(9, String.class, "PlanEnd_DT", false, "PLAN_END_DT");
        public final static Property AddPost_ID = new Property(10, int.class, "AddPost_ID", false, "ADD_POST_ID");
        public final static Property Add_DT = new Property(11, String.class, "Add_DT", false, "ADD_DT");
        public final static Property AddUserName_TX = new Property(12, String.class, "AddUserName_TX", false, "ADD_USER_NAME_TX");
        public final static Property Edit_DT = new Property(13, String.class, "Edit_DT", false, "EDIT_DT");
        public final static Property EditUserName_TX = new Property(14, String.class, "EditUserName_TX", false, "EDIT_USER_NAME_TX");
    };


    public OverhaulPlanDao(DaoConfig config) {
        super(config);
    }
    
    public OverhaulPlanDao(DaoConfig config, OverhaulDaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'JX_JXPlanHNYN' (" + //
                "'JXPLAN_ID' INTEGER NOT NULL ," + // 0: JXPlan_ID
                "'JXPLAN_CD' TEXT NOT NULL ," + // 1: JXPlan_CD
                "'AREA_ID' INTEGER," + // 2: Area_ID
                "'SPEC_ID' INTEGER," + // 3: Spec_ID
                "'ZYX_ID' INTEGER NOT NULL ," + // 4: ZYX_ID
                "'JXTYPE_ID' INTEGER NOT NULL ," + // 5: JXType_ID
                "'PLAN_NAME_TX' TEXT NOT NULL ," + // 6: PlanName_TX
                "'PLAN_CONTENT_TX' TEXT NOT NULL ," + // 7: PlanContent_TX
                "'PLAN_START_DT' TEXT NOT NULL ," + // 8: PlanStart_DT
                "'PLAN_END_DT' TEXT NOT NULL ," + // 9: PlanEnd_DT
                "'ADD_POST_ID' INTEGER NOT NULL ," + // 10: AddPost_ID
                "'ADD_DT' TEXT NOT NULL ," + // 11: Add_DT
                "'ADD_USER_NAME_TX' TEXT NOT NULL ," + // 12: AddUserName_TX
                "'EDIT_DT' TEXT," + // 13: Edit_DT
                "'EDIT_USER_NAME_TX' TEXT);"); // 14: EditUserName_TX
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'JX_JXPlanHNYN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OverhaulPlan entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getJXPlan_ID());
        stmt.bindString(2, entity.getJXPlan_CD());
 
        Integer Area_ID = entity.getArea_ID();
        if (Area_ID != null) {
            stmt.bindLong(3, Area_ID);
        }
 
        Integer Spec_ID = entity.getSpec_ID();
        if (Spec_ID != null) {
            stmt.bindLong(4, Spec_ID);
        }
        stmt.bindLong(5, entity.getZYX_ID());
        stmt.bindLong(6, entity.getJXType_ID());
        stmt.bindString(7, entity.getPlanName_TX());
        stmt.bindString(8, entity.getPlanContent_TX());
        stmt.bindString(9, entity.getPlanStart_DT());
        stmt.bindString(10, entity.getPlanEnd_DT());
        stmt.bindLong(11, entity.getAddPost_ID());
        stmt.bindString(12, entity.getAdd_DT());
        stmt.bindString(13, entity.getAddUserName_TX());
 
        String Edit_DT = entity.getEdit_DT();
        if (Edit_DT != null) {
            stmt.bindString(14, Edit_DT);
        }
 
        String EditUserName_TX = entity.getEditUserName_TX();
        if (EditUserName_TX != null) {
            stmt.bindString(15, EditUserName_TX);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public OverhaulPlan readEntity(Cursor cursor, int offset) {
    	OverhaulPlan entity = new OverhaulPlan( //
            cursor.getInt(offset + 0), // JXPlan_ID
            cursor.getString(offset + 1), // JXPlan_CD
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // Area_ID
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // Spec_ID
            cursor.getInt(offset + 4), // ZYX_ID
            cursor.getInt(offset + 5), // JXType_ID
            cursor.getString(offset + 6), // PlanName_TX
            cursor.getString(offset + 7), // PlanContent_TX
            cursor.getString(offset + 8), // PlanStart_DT
            cursor.getString(offset + 9), // PlanEnd_DT
            cursor.getInt(offset + 10), // AddPost_ID
            cursor.getString(offset + 11), // Add_DT
            cursor.getString(offset + 12), // AddUserName_TX
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // Edit_DT
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14) // EditUserName_TX
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OverhaulPlan entity, int offset) {
        entity.setJXPlan_ID(cursor.getInt(offset + 0));
        entity.setJXPlan_CD(cursor.getString(offset + 1));
        entity.setArea_ID(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setSpec_ID(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setZYX_ID(cursor.getInt(offset + 4));
        entity.setJXType_ID(cursor.getInt(offset + 5));
        entity.setPlanName_TX(cursor.getString(offset + 6));
        entity.setPlanContent_TX(cursor.getString(offset + 7));
        entity.setPlanStart_DT(cursor.getString(offset + 8));
        entity.setPlanEnd_DT(cursor.getString(offset + 9));
        entity.setAddPost_ID(cursor.getInt(offset + 10));
        entity.setAdd_DT(cursor.getString(offset + 11));
        entity.setAddUserName_TX(cursor.getString(offset + 12));
        entity.setEdit_DT(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setEditUserName_TX(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(OverhaulPlan entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(OverhaulPlan entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /**
     * 根据planID获取所有的plan
     * @param planID
     * @return
     */
    public List<OverhaulPlan> loadAllBYPalnID(String planidStr){
    	
    	String sql="select * from JX_JXPlanHNYN where JXPlan_ID in (" + planidStr + ")";
    	Cursor cursor = db.rawQuery(sql, null);
        return loadAllAndCloseCursor(cursor);
    	
    }
}
