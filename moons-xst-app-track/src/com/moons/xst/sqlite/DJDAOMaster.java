package com.moons.xst.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moons.xst.track.dao.CheckPointDAO;
import com.moons.xst.track.dao.CycleByIDPosDao;
import com.moons.xst.track.dao.CycleBySRLineTreeDao;
import com.moons.xst.track.dao.DJPlanDAO;
import com.moons.xst.track.dao.DJ_ControlPointDao;
import com.moons.xst.track.dao.DJ_IDPosDao;
import com.moons.xst.track.dao.DJ_ResultHisDao;
import com.moons.xst.track.dao.DJ_TaskIDPosHisDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesHisDao;
import com.moons.xst.track.dao.GPSMobjectBugResultHisDao;
import com.moons.xst.track.dao.GPSPositionForResultHisDao;
import com.moons.xst.track.dao.GPSXXPlanDao;
import com.moons.xst.track.dao.LKLineTreeByIDPosDao;
import com.moons.xst.track.dao.MObject_StateHisDao;
import com.moons.xst.track.dao.MobjectBugCodeDAO;
import com.moons.xst.track.dao.RH_IDPosDao;
import com.moons.xst.track.dao.RH_TaskActiveDao;
import com.moons.xst.track.dao.SRLineTreeByIDPosDao;
import com.moons.xst.track.dao.SRLineTreeLastResultDao;
import com.moons.xst.track.dao.SRinLKControlPointRelDao;
import com.moons.xst.track.dao.Z_AlmLevelDao;
import com.moons.xst.track.dao.Z_AppUserDao;
import com.moons.xst.track.dao.Z_ConditionDao;
import com.moons.xst.track.dao.Z_DJ_CycleDTLDao;
import com.moons.xst.track.dao.Z_DJ_CycleDao;
import com.moons.xst.track.dao.Z_DJ_PlanDao;
import com.moons.xst.track.dao.Z_DataCodeGroupDao;
import com.moons.xst.track.dao.Z_DataCodeGroupItemDao;
import com.moons.xst.track.dao.Z_MObjectStateDao;
import com.moons.xst.track.dao.Z_RelationDao;
import com.moons.xst.track.dao.Z_ShiftDao;
import com.moons.xst.track.dao.Z_ShiftGroupDao;
import com.moons.xst.track.dao.Z_ShiftRuleDao;
import com.moons.xst.track.dao.Z_SpecCaseDao;
import com.moons.xst.track.dao.Z_WorkDateDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

//import de.greenrobot.daoexample.OrderDao;
/**
 * Master of DAO (schema version 1000): knows all DAOs.
 */
public class DJDAOMaster extends AbstractDaoMaster {
	public static final int SCHEMA_VERSION = 1000;

	private static final String TAG = "DJDAOMaster";

	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);
		}
	}

	public DJDAOMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		try {
			registerDaoClass(DJPlanDAO.class);
			registerDaoClass(CheckPointDAO.class);
			registerDaoClass(MobjectBugCodeDAO.class);

			registerDaoClass(DJ_ResultHisDao.class);
			registerDaoClass(DJ_TaskIDPosHisDao.class);
			registerDaoClass(GPSMobjectBugResultForFilesHisDao.class);
			registerDaoClass(GPSMobjectBugResultHisDao.class);
			registerDaoClass(GPSPositionForResultHisDao.class);

			registerDaoClass(CycleByIDPosDao.class);
			registerDaoClass(CycleBySRLineTreeDao.class);
			registerDaoClass(DJ_ControlPointDao.class);
			registerDaoClass(DJ_IDPosDao.class);
			registerDaoClass(LKLineTreeByIDPosDao.class);
			registerDaoClass(MObject_StateHisDao.class);
			registerDaoClass(RH_IDPosDao.class);
			registerDaoClass(RH_TaskActiveDao.class);
			registerDaoClass(SRinLKControlPointRelDao.class);
			registerDaoClass(SRLineTreeByIDPosDao.class);
			registerDaoClass(SRLineTreeLastResultDao.class);
			registerDaoClass(Z_AlmLevelDao.class);
			registerDaoClass(Z_AppUserDao.class);
			registerDaoClass(Z_ConditionDao.class);
			registerDaoClass(Z_DataCodeGroupDao.class);
			registerDaoClass(Z_DataCodeGroupItemDao.class);
			registerDaoClass(Z_DJ_CycleDao.class);
			registerDaoClass(Z_DJ_CycleDTLDao.class);
			registerDaoClass(Z_DJ_PlanDao.class);
			registerDaoClass(Z_MObjectStateDao.class);
			registerDaoClass(Z_RelationDao.class);
			registerDaoClass(Z_ShiftDao.class);
			registerDaoClass(Z_ShiftGroupDao.class);
			registerDaoClass(Z_ShiftRuleDao.class);
			registerDaoClass(Z_SpecCaseDao.class);
			registerDaoClass(Z_WorkDateDao.class);
			registerDaoClass(GPSXXPlanDao.class);
		} catch (Exception e) {
			Log.w(TAG, e.toString());
			e.printStackTrace();
		}

		// registerDaoClass(CustomerDao.class);
		// registerDaoClass(OrderDao.class);
	}

	@Override
	public DJDAOSession newSession() {
		// IdentityScopeType说明：（By LKZ）
		// IdentityScopeType.None 不缓存；
		// IdentityScopeType.Session 缓存(同样的查询只会执行一次)
		return new DJDAOSession(db, IdentityScopeType.None, daoConfigMap);
	}

	@Override
	public DJDAOSession newSession(IdentityScopeType type) {
		return new DJDAOSession(db, type, daoConfigMap);
	}

	/**
	 * public static void createAllTables(SQLiteDatabase db, boolean
	 * ifNotExists) { DJIDPosDAO.createTable(db, ifNotExists);
	 * DJPlanDAO.createTable(db, ifNotExists); DJCycleDao.createTable(db,
	 * ifNotExists); DJCycleDTLDAO.createTable(db, ifNotExists); //
	 * CustomerDao.createTable(db, ifNotExists); // OrderDao.createTable(db,
	 * ifNotExists); }
	 * 
	 * public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
	 * // NoteDao.dropTable(db, ifExists); //CustomerDao.dropTable(db,
	 * ifExists); // OrderDao.dropTable(db, ifExists); }
	 */
}
