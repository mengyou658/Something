package com.moons.xst.sqlite;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.GPSMobjectBugResult;
import com.moons.xst.track.bean.GPSMobjectBugResultForFiles;
import com.moons.xst.track.bean.GPSPosition;
import com.moons.xst.track.bean.GPSPositionForInit;
import com.moons.xst.track.bean.GPSPositionForResult;
import com.moons.xst.track.bean.GPSXXPlanResult;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.OverSpeedRecordInfo;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_Relation;
import com.moons.xst.track.dao.DJ_PhotoByResultDao;
import com.moons.xst.track.dao.DJ_ResultActiveDao;
import com.moons.xst.track.dao.DJ_TaskIDPosActiveDao;
import com.moons.xst.track.dao.GPSMobjectBugResultDao;
import com.moons.xst.track.dao.GPSMobjectBugResultForFilesDao;
import com.moons.xst.track.dao.GPSPositionDao;
import com.moons.xst.track.dao.GPSPositionForInitDao;
import com.moons.xst.track.dao.GPSPositionForResultDao;
import com.moons.xst.track.dao.GPSXXPlanResultDao;
import com.moons.xst.track.dao.MObject_StateDao;
import com.moons.xst.track.dao.OverSpeedRecordDao;
import com.moons.xst.track.dao.SRLineTreeLastResultForResultDao;
import com.moons.xst.track.dao.Z_ConditionForResultDao;
import com.moons.xst.track.dao.Z_DJ_PlanForResultDao;
import com.moons.xst.track.dao.Z_RelationForResultDao;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class DJResultDAOSession extends AbstractDaoSession {

	private final DaoConfig dJ_PhotoByResultDaoConfig;
	private final DaoConfig dJ_ResultActiveDaoConfig;
	private final DaoConfig dJ_TaskIDPosActiveDaoConfig;
	private final DaoConfig gPSMobjectBugResultDaoConfig;
	private final DaoConfig gPSPositionDaoConfig;
	private final DaoConfig gPSPositionForInitDaoConfig;
	private final DaoConfig gPSPositionForResultDaoConfig;
	private final DaoConfig mObject_StateDaoConfig;
	private final DaoConfig gpsMobjectBugResultForFilesDaoConfig;
	private final DaoConfig overSpeedRecordDaoConfig;
	private final DaoConfig gPSXXPlanResultDaoConfig;
	private final DaoConfig z_relationForResultDaoConfig;
	
	//冗余几张表，如果是条件巡检，才增加数据，否则只有表，没有数据
	private final DaoConfig zdjplanForResultDaoConfig;
	private final DaoConfig srlinetreeLastResultForResultDaoConfig;
	private final DaoConfig zconditionForResultDaoConfig;

	private final DJ_PhotoByResultDao dJ_PhotoByResultDao;
	private final DJ_ResultActiveDao dJ_ResultActiveDao;
	private final DJ_TaskIDPosActiveDao dJ_TaskIDPosActiveDao;
	private final GPSMobjectBugResultDao gPSMobjectBugResultDao;
	private final GPSPositionDao gPSPositionDao;
	private final GPSPositionForInitDao gPSPositionForInitDao;
	private final GPSPositionForResultDao gPSPositionForResultDao;
	private final MObject_StateDao mObject_StateDao;
	private final GPSMobjectBugResultForFilesDao gpsMobjectBugResultForFilesDao;
	private final OverSpeedRecordDao overSpeedRecordDao;
	private final GPSXXPlanResultDao gPSXXPlanResultDao;
	private final Z_RelationForResultDao z_relationForResultDao;
	//
	private final Z_DJ_PlanForResultDao zdjplanForResultDao;
	private final SRLineTreeLastResultForResultDao srlineTreeLastResultForResultDao;
	private final Z_ConditionForResultDao zconditionForResultDao;

	public DJResultDAOSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		dJ_PhotoByResultDaoConfig = daoConfigMap.get(DJ_PhotoByResultDao.class)
				.clone();
		dJ_PhotoByResultDaoConfig.initIdentityScope(type);

		dJ_ResultActiveDaoConfig = daoConfigMap.get(DJ_ResultActiveDao.class)
				.clone();
		dJ_ResultActiveDaoConfig.initIdentityScope(type);

		dJ_TaskIDPosActiveDaoConfig = daoConfigMap.get(
				DJ_TaskIDPosActiveDao.class).clone();
		dJ_TaskIDPosActiveDaoConfig.initIdentityScope(type);

		gPSMobjectBugResultDaoConfig = daoConfigMap.get(
				GPSMobjectBugResultDao.class).clone();
		gPSMobjectBugResultDaoConfig.initIdentityScope(type);

		gPSPositionDaoConfig = daoConfigMap.get(GPSPositionDao.class).clone();
		gPSPositionDaoConfig.initIdentityScope(type);

		gPSPositionForInitDaoConfig = daoConfigMap.get(
				GPSPositionForInitDao.class).clone();
		gPSPositionForInitDaoConfig.initIdentityScope(type);

		gPSPositionForResultDaoConfig = daoConfigMap.get(
				GPSPositionForResultDao.class).clone();
		gPSPositionForResultDaoConfig.initIdentityScope(type);

		overSpeedRecordDaoConfig = daoConfigMap.get(OverSpeedRecordDao.class)
				.clone();
		overSpeedRecordDaoConfig.initIdentityScope(type);

		mObject_StateDaoConfig = daoConfigMap.get(MObject_StateDao.class)
				.clone();
		mObject_StateDaoConfig.initIdentityScope(type);

		gpsMobjectBugResultForFilesDaoConfig = daoConfigMap.get(
				GPSMobjectBugResultForFilesDao.class).clone();
		gpsMobjectBugResultForFilesDaoConfig.initIdentityScope(type);
		
        gPSXXPlanResultDaoConfig = daoConfigMap.get(GPSXXPlanResultDao.class).clone();
        gPSXXPlanResultDaoConfig.initIdentityScope(type);
        
        z_relationForResultDaoConfig = daoConfigMap.get(Z_RelationForResultDao.class).clone();
        z_relationForResultDaoConfig.initIdentityScope(type);
        //
        zdjplanForResultDaoConfig = daoConfigMap.get(Z_DJ_PlanForResultDao.class).clone();
        zdjplanForResultDaoConfig.initIdentityScope(type);
        
        srlinetreeLastResultForResultDaoConfig = daoConfigMap.get(SRLineTreeLastResultForResultDao.class).clone();
        srlinetreeLastResultForResultDaoConfig.initIdentityScope(type);
        
        zconditionForResultDaoConfig = daoConfigMap.get(Z_ConditionForResultDao.class).clone();
        zconditionForResultDaoConfig.initIdentityScope(type);

		dJ_PhotoByResultDao = new DJ_PhotoByResultDao(
				dJ_PhotoByResultDaoConfig, this);
		dJ_ResultActiveDao = new DJ_ResultActiveDao(dJ_ResultActiveDaoConfig,
				this);
		dJ_TaskIDPosActiveDao = new DJ_TaskIDPosActiveDao(
				dJ_TaskIDPosActiveDaoConfig, this);
		gPSMobjectBugResultDao = new GPSMobjectBugResultDao(
				gPSMobjectBugResultDaoConfig, this);
		gPSPositionDao = new GPSPositionDao(gPSPositionDaoConfig, this);
		gPSPositionForInitDao = new GPSPositionForInitDao(
				gPSPositionForInitDaoConfig, this);
		gPSPositionForResultDao = new GPSPositionForResultDao(
				gPSPositionForResultDaoConfig, this);
		mObject_StateDao = new MObject_StateDao(mObject_StateDaoConfig, this);
		gpsMobjectBugResultForFilesDao = new GPSMobjectBugResultForFilesDao(
				gpsMobjectBugResultForFilesDaoConfig, this);
		overSpeedRecordDao = new OverSpeedRecordDao(overSpeedRecordDaoConfig,
				this);
		gPSXXPlanResultDao = new GPSXXPlanResultDao(gPSXXPlanResultDaoConfig, this);
		
		z_relationForResultDao = new Z_RelationForResultDao(z_relationForResultDaoConfig, this);
		//
		zdjplanForResultDao = new Z_DJ_PlanForResultDao(zdjplanForResultDaoConfig, this);
		srlineTreeLastResultForResultDao = new SRLineTreeLastResultForResultDao(srlinetreeLastResultForResultDaoConfig, this);
		zconditionForResultDao = new Z_ConditionForResultDao(zconditionForResultDaoConfig, this);

		registerDao(DJ_PhotoByResult.class, dJ_PhotoByResultDao);
		registerDao(DJ_ResultActive.class, dJ_ResultActiveDao);
		registerDao(DJ_TaskIDPosActive.class, dJ_TaskIDPosActiveDao);
		registerDao(GPSMobjectBugResult.class, gPSMobjectBugResultDao);
		registerDao(GPSPosition.class, gPSPositionDao);
		registerDao(GPSPositionForInit.class, gPSPositionForInitDao);
		registerDao(GPSPositionForResult.class, gPSPositionForResultDao);
		registerDao(MObject_State.class, mObject_StateDao);
		registerDao(GPSMobjectBugResultForFiles.class,
				gpsMobjectBugResultForFilesDao);
		registerDao(OverSpeedRecordInfo.class, overSpeedRecordDao);
		registerDao(GPSXXPlanResult.class, gPSXXPlanResultDao);
		registerDao(Z_Relation.class, z_relationForResultDao);
		//
		registerDao(Z_DJ_Plan.class, zdjplanForResultDao);
		registerDao(SRLineTreeLastResult.class, srlineTreeLastResultForResultDao);
		registerDao(Z_Condition.class, zconditionForResultDao);
	}

	private int _lineID = -200;

	public void setLineID(Integer lineID) {
		_lineID = lineID;
	}

	public int getLineID() {
		return _lineID;
	}

	public void clear() {
		dJ_PhotoByResultDaoConfig.getIdentityScope().clear();
		dJ_ResultActiveDaoConfig.getIdentityScope().clear();
		dJ_TaskIDPosActiveDaoConfig.getIdentityScope().clear();
		gPSMobjectBugResultDaoConfig.getIdentityScope().clear();
		gPSPositionDaoConfig.getIdentityScope().clear();
		gPSPositionForInitDaoConfig.getIdentityScope().clear();
		gPSPositionForResultDaoConfig.getIdentityScope().clear();
		mObject_StateDaoConfig.getIdentityScope().clear();
		gpsMobjectBugResultForFilesDaoConfig.getIdentityScope().clear();
		overSpeedRecordDaoConfig.getIdentityScope().clear();
		gPSXXPlanResultDaoConfig.getIdentityScope().clear();
		z_relationForResultDaoConfig.getIdentityScope().clear();
		//
		zdjplanForResultDaoConfig.getIdentityScope().clear();
		srlinetreeLastResultForResultDaoConfig.getIdentityScope().clear();
		zconditionForResultDaoConfig.getIdentityScope().clear();
	}

	public DJ_PhotoByResultDao getDJ_PhotoByResultDao() {
		return dJ_PhotoByResultDao;
	}

	public DJ_ResultActiveDao getDJ_ResultActiveDao() {
		return dJ_ResultActiveDao;
	}

	public DJ_TaskIDPosActiveDao getDJ_TaskIDPosActiveDao() {
		return dJ_TaskIDPosActiveDao;
	}

	public GPSMobjectBugResultDao getGPSMobjectBugResultDao() {
		return gPSMobjectBugResultDao;
	}

	public GPSPositionDao getGPSPositionDao() {
		return gPSPositionDao;
	}

	public GPSPositionForInitDao getGPSPositionForInitDao() {
		return gPSPositionForInitDao;
	}

	public GPSPositionForResultDao getGPSPositionForResultDao() {
		return gPSPositionForResultDao;
	}

	public MObject_StateDao getMObject_StateDao() {
		return mObject_StateDao;
	}

	public GPSMobjectBugResultForFilesDao getGPSMobjectBugResultForFilesDao() {
		return gpsMobjectBugResultForFilesDao;
	}

	public OverSpeedRecordDao getovOverSpeedRecordDao() {
		return overSpeedRecordDao;
	}
    public GPSXXPlanResultDao getGPSXXPlanResultDao() {
        return gPSXXPlanResultDao;
    }
    
    public Z_RelationForResultDao getZ_RelationForResultDao() {
    	return z_relationForResultDao;
    }
    
    //
    public Z_DJ_PlanForResultDao getZ_DJ_PlanForResultDao() {
    	return zdjplanForResultDao;
    }
    public SRLineTreeLastResultForResultDao getSRLineTreeLastResultForResultDao() {
    	return srlineTreeLastResultForResultDao;
    }
    public Z_ConditionForResultDao getZ_ConditionForResultDao() {
    	return zconditionForResultDao;
    }
}
