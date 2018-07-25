package com.moons.xst.track.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.bean.DJPlan;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class DJPlanDAO extends AbstractDao<DJPlan, Long>{


	public static final String TABLENAME = "DJPlan";
	
	
	 public static class Properties {
	        public final static Property planID = new Property(0, int.class, "planID", true, "planID");
	        public final static Property planDesc = new Property(1, String.class, "planDesc", false, "planDesc");
	        public final static Property lastTime = new Property(2, java.util.Date.class, "lastTime", false, "lastTime");
	        public final static Property lastResult = new Property(3, String.class, "lastResult", false, "lastResult");
	        public final static Property dataCode = new Property(4, String.class, "dataCode", false, "dataCode");
	        public final static Property planUnit = new Property(5, String.class, "planUnit", false, "planUnit");
	        public final static Property Cycle_ID = new Property(6, String.class, "Cycle_ID", false, "CYCLE__ID");
	        public final static Property CycleBaseDate_DT = new Property(7, String.class, "CycleBaseDate_DT", false, "CYCLE_BASE_DATE__DT");
	        public final static Property IDPos_ID = new Property(8, String.class, "IDPos_ID", false, "IDPOS__ID");
	    };
	    
	    private DJDAOSession djDAOSession;
	    
	    
		public DJPlanDAO(DaoConfig config) {
			super(config);
			// TODO Auto-generated constructor stub
		}
		
		public DJPlanDAO(DaoConfig config, DJDAOSession daoSession) {
	        super(config, daoSession); 
	    }
		
		 /** Creates the underlying database table. */
	    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
	        String constraint = ifNotExists? "IF NOT EXISTS ": "";
	        db.execSQL("CREATE TABLE " + constraint + "'DJPlan' (" + //
	                "'planID' INTEGER PRIMARY KEY ," + // 0: planID
	                "'planDesc' TEXT NOT NULL ," + // 1: planDesc
	                "'lastTime' TEXT," + // 2: lastTime
	                "'lastResult' TEXT," + // 3: lastResult
	                "'dataCode' TEXT," + // 4: dataCode
	                "'planUnit' TEXT," + // 7: planUnit 
	                "'CYCLE__ID' TEXT NOT NULL ," + // 6: Cycle_ID
	                "'CYCLE_BASE_DATE__DT' TEXT NOT NULL ," + // 7: CycleBaseDate_DT
	                "'IDPOS__ID' TEXT NOT NULL );"); // 8: IDPos_ID
	        db.execSQL("insert into DJPlan values (1,'我的设备的位置1测试点检标准-观察类','2014-09-01 01:01:01','正常','','GC','1','2013-09-15','1')");
	        db.execSQL("insert into DJPlan values (2,'我的设备的位置1测试点检标准-记录类','2014-09-01 01:01:01','36.8','℃','JL','1','2013-09-15','1')");
	        db.execSQL("insert into DJPlan values (3,'我的设备的位置2测试点检标准-测温类','2014-09-01 01:01:01','36.8','℃','CW','2','2013-09-15','2')");
	        db.execSQL("insert into DJPlan values (4,'我的设备的位置2测试点检标准-测振类','2014-09-01 01:01:01','6.2','mm/S','CZ','2','2013-09-15','2')");
	    }
	/**
	 * 初始化路线列表
	 */
	/*public void loadDJPlanToBufferByIDPosID(Cursor cursor) {
		
		 AppContext.DJPlanBuffer.clear();
		 DJPlan myDJPlan = new DJPlan();
		 myDJPlan.buildObject(1, "我的设备的位置1测试点检标准-观察类", StringUtils.toDate("2014-09-01 01:01:01"),"正常","", "GC");
		 AppContext.DJPlanBuffer.add(myDJPlan);
		 myDJPlan = null;

		 myDJPlan = new DJPlan();
		 myDJPlan.buildObject(2, "我的设备的位置1测试点检标准-记录类", StringUtils.toDate("2014-09-02 02:02:02"),"36.8","℃", "JL");
		 AppContext.DJPlanBuffer.add(myDJPlan);
		 myDJPlan = null;
		 
		 myDJPlan = new DJPlan();
		 myDJPlan.buildObject(3, "我的设备的位置1测试点检标准-测温类", StringUtils.toDate("2014-09-03 03:03:03"),"36.8", "℃","CW");
		 AppContext.DJPlanBuffer.add(myDJPlan);
		 myDJPlan = null;

		 myDJPlan = new DJPlan();
		 myDJPlan.buildObject(4, "我的设备的位置1测试点检标准-测振类", StringUtils.toDate("2014-09-04 04:04:04"),"6.2","mm/S", "CZ");
		 AppContext.DJPlanBuffer.add(myDJPlan);
		 myDJPlan = null;		
		List<DJPlan> lvDJPlanStatusData = loadAllDeepFromCursor( cursor);
		//加载数据
		AppContext.DJPlanBuffer.clear();
		
		
		for(DJPlan djPlanStatus :  lvDJPlanStatusData  )
		{
			AppContext.DJPlanBuffer.add(djPlanStatus);
		}
		
	}**/
	
	public List<DJPlan> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DJPlan> list = new ArrayList<DJPlan>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
	
    protected DJPlan loadCurrentDeep(Cursor cursor, boolean lock) {
    	DJPlan entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;
        return entity;    
    }
	@Override
	protected void bindValues(SQLiteStatement stmt, DJPlan entity) {
		// TODO Auto-generated method stub
		stmt.clearBindings();
		
		int planID = entity.getPlanID();
		if(  planID != 0){
			stmt.bindLong(1, planID);
		}
			
		String planDesc = entity.getPlanDesc();
		stmt.bindString(2,planDesc );
		
		java.util.Date lastTime = entity.getLastTime();
		if(lastTime != null){
			stmt.bindLong(3, lastTime.getTime());
		}
		
        String lastResult = entity.getLastResult();
        if (lastResult != null) {
            stmt.bindString(4, lastResult);
        }
		String dataCode = entity.getDataCode();
		if(dataCode != null){
			stmt.bindString(5, dataCode);
		}
		
		String planUnit = entity.getPlanUnit();
		if(planUnit != null){
			stmt.bindString(6, planUnit);
		}
        stmt.bindString(7, entity.getCycle_ID());
        stmt.bindString(8, entity.getCycleBaseDate_DT());
        stmt.bindString(9, entity.getIDPos_ID());
	}
	@Override
	protected Long getKey(DJPlan entity) {
		// TODO Auto-generated method stub
	    if(entity != null) {
	        return (long) entity.getPlanID();
	    } else {
	        return null;
	    }
	}
	@Override
	protected boolean isEntityUpdateable() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected DJPlan readEntity(Cursor cursor, int offset) {
		// TODO Auto-generated method stub
		DJPlan entity = new DJPlan(
			cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0),
			cursor.getString(offset + 1),
			cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)),
			cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
			cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
			cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
		    cursor.getString(offset + 6), // Cycle_ID
		    cursor.getString(offset + 7), // CycleBaseDate_DT
		    cursor.getString(offset + 8) // IDPos_ID);
		            );
		return entity;
		
	}
	@Override
	protected void readEntity(Cursor cursor, DJPlan entity, int offset) {
		// TODO Auto-generated method stub
		entity.setPlanID(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
		entity.setPlanDesc( cursor.getString(offset + 1));
		entity.setLastTime(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
		entity.setLastResult(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
		entity.setDataCode( cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
		entity.setPlanUnit( cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCycle_ID(cursor.getString(offset + 6));
        entity.setCycleBaseDate_DT(cursor.getString(offset + 7));
        entity.setIDPos_ID(cursor.getString(offset + 8));
		
	}
	@Override
	protected Long readKey(Cursor cursor, int offset) {
		// TODO Auto-generated method stub
		 return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}
	@Override
	protected Long updateKeyAfterInsert(DJPlan entity, long planID) {
		// TODO Auto-generated method stub
		entity.setPlanID((int) planID);
		return planID;
	}
		
	@Override
	public List<DJPlan> loadAll()
	{
	    Cursor cursor = db.rawQuery(statements.getSelectAll(), null);
	    return loadAllAndCloseCursor(cursor);
	}
	
	/*
	public List<Object> getListByUser(int AppUserID) throws AppException{
		String sql = "";
		String[] selectionArgs = null;
		Object object = new DJLine();
		try{
			return AppContext.commonDB.ListqueryData2Object(sql, selectionArgs, object);
		}
		catch(AppException e){
			e.printStackTrace();
			throw AppException.database(e);
			
			return null;
		}
	}
	*/
}
