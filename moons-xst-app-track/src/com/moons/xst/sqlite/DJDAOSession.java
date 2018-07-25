package com.moons.xst.sqlite;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.CycleByIDPos;
import com.moons.xst.track.bean.CycleBySRLineTree;
import com.moons.xst.track.bean.DJPlan;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.DJ_ResultHis;
import com.moons.xst.track.bean.DJ_TaskIDPosHis;
import com.moons.xst.track.bean.GPSMobjectBugResultForFilesHis;
import com.moons.xst.track.bean.GPSMobjectBugResultHis;
import com.moons.xst.track.bean.GPSPositionForResultHis;
import com.moons.xst.track.bean.GPSXXPlan;
import com.moons.xst.track.bean.LKLineTreeByIDPos;
import com.moons.xst.track.bean.MObject_StateHis;
import com.moons.xst.track.bean.MobjectBugCodeInfo;
import com.moons.xst.track.bean.RH_IDPos;
import com.moons.xst.track.bean.RH_TaskActive;
import com.moons.xst.track.bean.SRLineTreeByIDPos;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.SRinLKControlPointRel;
import com.moons.xst.track.bean.Z_AlmLevel;
import com.moons.xst.track.bean.Z_AppUser;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.bean.Z_DJ_Cycle;
import com.moons.xst.track.bean.Z_DJ_CycleDTL;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_DataCodeGroupItem;
import com.moons.xst.track.bean.Z_MObjectState;
import com.moons.xst.track.bean.Z_Relation;
import com.moons.xst.track.bean.Z_Shift;
import com.moons.xst.track.bean.Z_ShiftGroup;
import com.moons.xst.track.bean.Z_ShiftRule;
import com.moons.xst.track.bean.Z_SpecCase;
import com.moons.xst.track.bean.Z_WorkDate;
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

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class DJDAOSession extends AbstractDaoSession {

	private final DaoConfig djPlanDAOConfig;
	private final DaoConfig CheckPointDAOConfig;
	private final DaoConfig MobjectBugCodeDAOConfig;

	private final DaoConfig DJ_ResultHisDAOConfig;
	private final DaoConfig DJ_TaskIDPosHisDAOConfig;
	private final DaoConfig GPSMobjectBugResultForFilesHisDAOConfig;
	private final DaoConfig GPSMobjectBugResultHisDAOConfig;
	private final DaoConfig GPSPositionForResultHisDAOConfig;

	private final DJPlanDAO djPlanDao;
	private final CheckPointDAO trackCheckPointDao;
	private final MobjectBugCodeDAO trackMobjectBugCodeDao;
	private final DJ_ResultHisDao dj_ResultHisDao;
	private final DJ_TaskIDPosHisDao dj_TaskIDPosHisDao;
	private final GPSMobjectBugResultForFilesHisDao gpsMobjectBugResultForFilesHisDao;
	private final GPSMobjectBugResultHisDao gpsMobjectBugResultHisDao;
	private final GPSPositionForResultHisDao gpsPositionForResultHisDao;

	private final DaoConfig cycleByIDPosDaoConfig;
	private final DaoConfig cycleBySRLineTreeDaoConfig;
	private final DaoConfig dJ_ControlPointDaoConfig;
	private final DaoConfig dJ_IDPosDaoConfig;
	private final DaoConfig lKLineTreeByIDPosDaoConfig;
	private final DaoConfig mObject_StateHisDaoConfig;
	private final DaoConfig rH_IDPosDaoConfig;
	private final DaoConfig rH_TaskActiveDaoConfig;
	private final DaoConfig sRinLKControlPointRelDaoConfig;
	private final DaoConfig sRLineTreeByIDPosDaoConfig;
	private final DaoConfig sRLineTreeLastResultDaoConfig;
	private final DaoConfig z_AlmLevelDaoConfig;
	private final DaoConfig z_AppUserDaoConfig;
	private final DaoConfig z_ConditionDaoConfig;
	private final DaoConfig z_DataCodeGroupDaoConfig;
	private final DaoConfig z_DataCodeGroupItemDaoConfig;
	private final DaoConfig z_DJ_CycleDaoConfig;
	private final DaoConfig z_DJ_CycleDTLDaoConfig;
	private final DaoConfig z_DJ_PlanDaoConfig;
	private final DaoConfig z_MObjectStateDaoConfig;
	private final DaoConfig z_RelationDaoConfig;
	private final DaoConfig z_ShiftDaoConfig;
	private final DaoConfig z_ShiftGroupDaoConfig;
	private final DaoConfig z_ShiftRuleDaoConfig;
	private final DaoConfig z_SpecCaseDaoConfig;
	private final DaoConfig z_WorkDateDaoConfig;
	private final DaoConfig GPSXXPlanDaoConfig;

	private final GPSXXPlanDao gpsXXPlanDao;

	private final CycleByIDPosDao cycleByIDPosDao;
	private final CycleBySRLineTreeDao cycleBySRLineTreeDao;
	private final DJ_ControlPointDao dJ_ControlPointDao;
	private final DJ_IDPosDao dJ_IDPosDao;
	private final LKLineTreeByIDPosDao lKLineTreeByIDPosDao;
	private final MObject_StateHisDao mObject_StateHisDao;
	private final RH_IDPosDao rH_IDPosDao;
	private final RH_TaskActiveDao rH_TaskActiveDao;
	private final SRinLKControlPointRelDao sRinLKControlPointRelDao;
	private final SRLineTreeByIDPosDao sRLineTreeByIDPosDao;
	private final SRLineTreeLastResultDao sRLineTreeLastResultDao;
	private final Z_AlmLevelDao z_AlmLevelDao;
	private final Z_AppUserDao z_AppUserDao;
	private final Z_ConditionDao z_ConditionDao;
	private final Z_DataCodeGroupDao z_DataCodeGroupDao;
	private final Z_DataCodeGroupItemDao z_DataCodeGroupItemDao;
	private final Z_DJ_CycleDao z_DJ_CycleDao;
	private final Z_DJ_CycleDTLDao z_DJ_CycleDTLDao;
	private final Z_DJ_PlanDao z_DJ_PlanDao;
	private final Z_MObjectStateDao z_MObjectStateDao;
	private final Z_RelationDao z_RelationDao;
	private final Z_ShiftDao z_ShiftDao;
	private final Z_ShiftGroupDao z_ShiftGroupDao;
	private final Z_ShiftRuleDao z_ShiftRuleDao;
	private final Z_SpecCaseDao z_SpecCaseDao;
	private final Z_WorkDateDao z_WorkDateDao;

	public DJDAOSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		djPlanDAOConfig = daoConfigMap.get(DJPlanDAO.class).clone();
		djPlanDAOConfig.initIdentityScope(type);

		CheckPointDAOConfig = daoConfigMap.get(CheckPointDAO.class).clone();
		CheckPointDAOConfig.initIdentityScope(type);

		MobjectBugCodeDAOConfig = daoConfigMap.get(MobjectBugCodeDAO.class)
				.clone();
		MobjectBugCodeDAOConfig.initIdentityScope(type);

		DJ_ResultHisDAOConfig = daoConfigMap.get(DJ_ResultHisDao.class).clone();
		DJ_ResultHisDAOConfig.initIdentityScope(type);
		DJ_TaskIDPosHisDAOConfig = daoConfigMap.get(DJ_TaskIDPosHisDao.class)
				.clone();
		DJ_TaskIDPosHisDAOConfig.initIdentityScope(type);
		GPSMobjectBugResultForFilesHisDAOConfig = daoConfigMap.get(
				GPSMobjectBugResultForFilesHisDao.class).clone();
		GPSMobjectBugResultForFilesHisDAOConfig.initIdentityScope(type);
		GPSMobjectBugResultHisDAOConfig = daoConfigMap.get(
				GPSMobjectBugResultHisDao.class).clone();
		GPSMobjectBugResultHisDAOConfig.initIdentityScope(type);
		GPSPositionForResultHisDAOConfig = daoConfigMap.get(
				GPSPositionForResultHisDao.class).clone();
		GPSPositionForResultHisDAOConfig.initIdentityScope(type);

		djPlanDao = new DJPlanDAO(djPlanDAOConfig, this);
		trackCheckPointDao = new CheckPointDAO(CheckPointDAOConfig, this);
		trackMobjectBugCodeDao = new MobjectBugCodeDAO(MobjectBugCodeDAOConfig,
				this);
		dj_ResultHisDao = new DJ_ResultHisDao(DJ_ResultHisDAOConfig, this);
		dj_TaskIDPosHisDao = new DJ_TaskIDPosHisDao(DJ_TaskIDPosHisDAOConfig,
				this);
		gpsMobjectBugResultForFilesHisDao = new GPSMobjectBugResultForFilesHisDao(
				GPSMobjectBugResultForFilesHisDAOConfig, this);
		gpsMobjectBugResultHisDao = new GPSMobjectBugResultHisDao(
				GPSMobjectBugResultHisDAOConfig, this);
		gpsPositionForResultHisDao = new GPSPositionForResultHisDao(
				GPSPositionForResultHisDAOConfig, this);

		registerDao(DJPlan.class, djPlanDao);
		registerDao(CheckPointInfo.class, trackCheckPointDao);
		registerDao(MobjectBugCodeInfo.class, trackMobjectBugCodeDao);
		registerDao(DJ_ResultHis.class, dj_ResultHisDao);
		registerDao(DJ_TaskIDPosHis.class, dj_TaskIDPosHisDao);
		registerDao(GPSMobjectBugResultForFilesHis.class,
				gpsMobjectBugResultForFilesHisDao);
		registerDao(GPSMobjectBugResultHis.class, gpsMobjectBugResultHisDao);
		registerDao(GPSPositionForResultHis.class, gpsPositionForResultHisDao);

		cycleByIDPosDaoConfig = daoConfigMap.get(CycleByIDPosDao.class).clone();
		cycleByIDPosDaoConfig.initIdentityScope(type);

		cycleBySRLineTreeDaoConfig = daoConfigMap.get(
				CycleBySRLineTreeDao.class).clone();
		cycleBySRLineTreeDaoConfig.initIdentityScope(type);

		dJ_ControlPointDaoConfig = daoConfigMap.get(DJ_ControlPointDao.class)
				.clone();
		dJ_ControlPointDaoConfig.initIdentityScope(type);

		dJ_IDPosDaoConfig = daoConfigMap.get(DJ_IDPosDao.class).clone();
		dJ_IDPosDaoConfig.initIdentityScope(type);

		lKLineTreeByIDPosDaoConfig = daoConfigMap.get(
				LKLineTreeByIDPosDao.class).clone();
		lKLineTreeByIDPosDaoConfig.initIdentityScope(type);

		mObject_StateHisDaoConfig = daoConfigMap.get(MObject_StateHisDao.class)
				.clone();
		mObject_StateHisDaoConfig.initIdentityScope(type);

		rH_IDPosDaoConfig = daoConfigMap.get(RH_IDPosDao.class).clone();
		rH_IDPosDaoConfig.initIdentityScope(type);

		rH_TaskActiveDaoConfig = daoConfigMap.get(RH_TaskActiveDao.class)
				.clone();
		rH_TaskActiveDaoConfig.initIdentityScope(type);

		sRinLKControlPointRelDaoConfig = daoConfigMap.get(
				SRinLKControlPointRelDao.class).clone();
		sRinLKControlPointRelDaoConfig.initIdentityScope(type);

		sRLineTreeByIDPosDaoConfig = daoConfigMap.get(
				SRLineTreeByIDPosDao.class).clone();
		sRLineTreeByIDPosDaoConfig.initIdentityScope(type);

		sRLineTreeLastResultDaoConfig = daoConfigMap.get(
				SRLineTreeLastResultDao.class).clone();
		sRLineTreeLastResultDaoConfig.initIdentityScope(type);

		z_AlmLevelDaoConfig = daoConfigMap.get(Z_AlmLevelDao.class).clone();
		z_AlmLevelDaoConfig.initIdentityScope(type);

		z_AppUserDaoConfig = daoConfigMap.get(Z_AppUserDao.class).clone();
		z_AppUserDaoConfig.initIdentityScope(type);

		z_ConditionDaoConfig = daoConfigMap.get(Z_ConditionDao.class).clone();
		z_ConditionDaoConfig.initIdentityScope(type);

		z_DataCodeGroupDaoConfig = daoConfigMap.get(Z_DataCodeGroupDao.class)
				.clone();
		z_DataCodeGroupDaoConfig.initIdentityScope(type);

		z_DataCodeGroupItemDaoConfig = daoConfigMap.get(
				Z_DataCodeGroupItemDao.class).clone();
		z_DataCodeGroupItemDaoConfig.initIdentityScope(type);

		z_DJ_CycleDaoConfig = daoConfigMap.get(Z_DJ_CycleDao.class).clone();
		z_DJ_CycleDaoConfig.initIdentityScope(type);

		z_DJ_CycleDTLDaoConfig = daoConfigMap.get(Z_DJ_CycleDTLDao.class)
				.clone();
		z_DJ_CycleDTLDaoConfig.initIdentityScope(type);

		z_DJ_PlanDaoConfig = daoConfigMap.get(Z_DJ_PlanDao.class).clone();
		z_DJ_PlanDaoConfig.initIdentityScope(type);

		z_MObjectStateDaoConfig = daoConfigMap.get(Z_MObjectStateDao.class)
				.clone();
		z_MObjectStateDaoConfig.initIdentityScope(type);

		z_RelationDaoConfig = daoConfigMap.get(Z_RelationDao.class).clone();
		z_RelationDaoConfig.initIdentityScope(type);

		z_ShiftDaoConfig = daoConfigMap.get(Z_ShiftDao.class).clone();
		z_ShiftDaoConfig.initIdentityScope(type);

		z_ShiftGroupDaoConfig = daoConfigMap.get(Z_ShiftGroupDao.class).clone();
		z_ShiftGroupDaoConfig.initIdentityScope(type);

		z_ShiftRuleDaoConfig = daoConfigMap.get(Z_ShiftRuleDao.class).clone();
		z_ShiftRuleDaoConfig.initIdentityScope(type);

		z_SpecCaseDaoConfig = daoConfigMap.get(Z_SpecCaseDao.class).clone();
		z_SpecCaseDaoConfig.initIdentityScope(type);

		z_WorkDateDaoConfig = daoConfigMap.get(Z_WorkDateDao.class).clone();
		z_WorkDateDaoConfig.initIdentityScope(type);

		cycleByIDPosDao = new CycleByIDPosDao(cycleByIDPosDaoConfig, this);
		cycleBySRLineTreeDao = new CycleBySRLineTreeDao(
				cycleBySRLineTreeDaoConfig, this);
		dJ_ControlPointDao = new DJ_ControlPointDao(dJ_ControlPointDaoConfig,
				this);
		dJ_IDPosDao = new DJ_IDPosDao(dJ_IDPosDaoConfig, this);
		lKLineTreeByIDPosDao = new LKLineTreeByIDPosDao(
				lKLineTreeByIDPosDaoConfig, this);
		mObject_StateHisDao = new MObject_StateHisDao(
				mObject_StateHisDaoConfig, this);
		rH_IDPosDao = new RH_IDPosDao(rH_IDPosDaoConfig, this);
		rH_TaskActiveDao = new RH_TaskActiveDao(rH_TaskActiveDaoConfig, this);
		sRinLKControlPointRelDao = new SRinLKControlPointRelDao(
				sRinLKControlPointRelDaoConfig, this);
		sRLineTreeByIDPosDao = new SRLineTreeByIDPosDao(
				sRLineTreeByIDPosDaoConfig, this);
		sRLineTreeLastResultDao = new SRLineTreeLastResultDao(
				sRLineTreeLastResultDaoConfig, this);
		z_AlmLevelDao = new Z_AlmLevelDao(z_AlmLevelDaoConfig, this);
		z_AppUserDao = new Z_AppUserDao(z_AppUserDaoConfig, this);
		z_ConditionDao = new Z_ConditionDao(z_ConditionDaoConfig, this);
		z_DataCodeGroupDao = new Z_DataCodeGroupDao(z_DataCodeGroupDaoConfig,
				this);
		z_DataCodeGroupItemDao = new Z_DataCodeGroupItemDao(
				z_DataCodeGroupItemDaoConfig, this);
		z_DJ_CycleDao = new Z_DJ_CycleDao(z_DJ_CycleDaoConfig, this);
		z_DJ_CycleDTLDao = new Z_DJ_CycleDTLDao(z_DJ_CycleDTLDaoConfig, this);
		z_DJ_PlanDao = new Z_DJ_PlanDao(z_DJ_PlanDaoConfig, this);
		z_MObjectStateDao = new Z_MObjectStateDao(z_MObjectStateDaoConfig, this);
		z_RelationDao = new Z_RelationDao(z_RelationDaoConfig, this);
		z_ShiftDao = new Z_ShiftDao(z_ShiftDaoConfig, this);
		z_ShiftGroupDao = new Z_ShiftGroupDao(z_ShiftGroupDaoConfig, this);
		z_ShiftRuleDao = new Z_ShiftRuleDao(z_ShiftRuleDaoConfig, this);
		z_SpecCaseDao = new Z_SpecCaseDao(z_SpecCaseDaoConfig, this);
		z_WorkDateDao = new Z_WorkDateDao(z_WorkDateDaoConfig, this);

		registerDao(CycleByIDPos.class, cycleByIDPosDao);
		registerDao(CycleBySRLineTree.class, cycleBySRLineTreeDao);
		registerDao(DJ_ControlPoint.class, dJ_ControlPointDao);
		registerDao(DJ_IDPos.class, dJ_IDPosDao);
		registerDao(LKLineTreeByIDPos.class, lKLineTreeByIDPosDao);
		registerDao(MObject_StateHis.class, mObject_StateHisDao);
		registerDao(RH_IDPos.class, rH_IDPosDao);
		registerDao(RH_TaskActive.class, rH_TaskActiveDao);
		registerDao(SRinLKControlPointRel.class, sRinLKControlPointRelDao);
		registerDao(SRLineTreeByIDPos.class, sRLineTreeByIDPosDao);
		registerDao(SRLineTreeLastResult.class, sRLineTreeLastResultDao);
		registerDao(Z_AlmLevel.class, z_AlmLevelDao);
		registerDao(Z_AppUser.class, z_AppUserDao);
		registerDao(Z_Condition.class, z_ConditionDao);
		registerDao(Z_DataCodeGroup.class, z_DataCodeGroupDao);
		registerDao(Z_DataCodeGroupItem.class, z_DataCodeGroupItemDao);
		registerDao(Z_DJ_Cycle.class, z_DJ_CycleDao);
		registerDao(Z_DJ_CycleDTL.class, z_DJ_CycleDTLDao);
		registerDao(Z_DJ_Plan.class, z_DJ_PlanDao);
		registerDao(Z_MObjectState.class, z_MObjectStateDao);
		registerDao(Z_Relation.class, z_RelationDao);
		registerDao(Z_Shift.class, z_ShiftDao);
		registerDao(Z_ShiftGroup.class, z_ShiftGroupDao);
		registerDao(Z_ShiftRule.class, z_ShiftRuleDao);
		registerDao(Z_SpecCase.class, z_SpecCaseDao);
		registerDao(Z_WorkDate.class, z_WorkDateDao);

		GPSXXPlanDaoConfig = daoConfigMap.get(GPSXXPlanDao.class).clone();
		GPSXXPlanDaoConfig.initIdentityScope(type);
		gpsXXPlanDao = new GPSXXPlanDao(GPSXXPlanDaoConfig, this);
		registerDao(GPSXXPlan.class, gpsXXPlanDao);
	}

	private int _lineID = -200;

	public void setLineID(Integer lineID) {
		_lineID = lineID;
	}

	public int getLineID() {
		return _lineID;
	}

	public DJPlanDAO getDJPlanDAO() {
		return djPlanDao;
	}

	public CheckPointDAO getCheckPointDAO() {
		return trackCheckPointDao;
	}

	public MobjectBugCodeDAO getMobjectBugCodeDAO() {
		return trackMobjectBugCodeDao;
	}

	public DJ_ResultHisDao getDJ_ResultHisDao() {
		return dj_ResultHisDao;
	}

	public DJ_TaskIDPosHisDao getDJ_TaskIDPosHisDao() {
		return dj_TaskIDPosHisDao;
	}

	public GPSMobjectBugResultForFilesHisDao getGPSMobjectBugResultForFilesHisDao() {
		return gpsMobjectBugResultForFilesHisDao;
	}

	public GPSMobjectBugResultHisDao getGPSMobjectBugResultHisDao() {
		return gpsMobjectBugResultHisDao;
	}

	public GPSPositionForResultHisDao getGPSPositionForResultHisDao() {
		return gpsPositionForResultHisDao;
	}

	public CycleByIDPosDao getCycleByIDPosDao() {
		return cycleByIDPosDao;
	}

	public CycleBySRLineTreeDao getCycleBySRLineTreeDao() {
		return cycleBySRLineTreeDao;
	}

	public DJ_ControlPointDao getDJ_ControlPointDao() {
		return dJ_ControlPointDao;
	}

	public DJ_IDPosDao getDJ_IDPosDao() {
		return dJ_IDPosDao;
	}

	public LKLineTreeByIDPosDao getLKLineTreeByIDPosDao() {
		return lKLineTreeByIDPosDao;
	}

	public MObject_StateHisDao getMObject_StateHisDao() {
		return mObject_StateHisDao;
	}

	public RH_IDPosDao getRH_IDPosDao() {
		return rH_IDPosDao;
	}

	public RH_TaskActiveDao getRH_TaskActiveDao() {
		return rH_TaskActiveDao;
	}

	public SRinLKControlPointRelDao getSRinLKControlPointRelDao() {
		return sRinLKControlPointRelDao;
	}

	public SRLineTreeByIDPosDao getSRLineTreeByIDPosDao() {
		return sRLineTreeByIDPosDao;
	}

	public SRLineTreeLastResultDao getSRLineTreeLastResultDao() {
		return sRLineTreeLastResultDao;
	}

	public Z_AlmLevelDao getZ_AlmLevelDao() {
		return z_AlmLevelDao;
	}

	public Z_AppUserDao getZ_AppUserDao() {
		return z_AppUserDao;
	}

	public Z_ConditionDao getZ_ConditionDao() {
		return z_ConditionDao;
	}

	public Z_DataCodeGroupDao getZ_DataCodeGroupDao() {
		return z_DataCodeGroupDao;
	}

	public Z_DataCodeGroupItemDao getZ_DataCodeGroupItemDao() {
		return z_DataCodeGroupItemDao;
	}

	public Z_DJ_CycleDao getZ_DJ_CycleDao() {
		return z_DJ_CycleDao;
	}

	public Z_DJ_CycleDTLDao getZ_DJ_CycleDTLDao() {
		return z_DJ_CycleDTLDao;
	}

	public Z_DJ_PlanDao getZ_DJ_PlanDao() {
		return z_DJ_PlanDao;
	}

	public Z_MObjectStateDao getZ_MObjectStateDao() {
		return z_MObjectStateDao;
	}

	public Z_RelationDao getZ_RelationDao() {
		return z_RelationDao;
	}

	public Z_ShiftDao getZ_ShiftDao() {
		return z_ShiftDao;
	}

	public Z_ShiftGroupDao getZ_ShiftGroupDao() {
		return z_ShiftGroupDao;
	}

	public Z_ShiftRuleDao getZ_ShiftRuleDao() {
		return z_ShiftRuleDao;
	}

	public Z_SpecCaseDao getZ_SpecCaseDao() {
		return z_SpecCaseDao;
	}

	public Z_WorkDateDao getZ_WorkDateDao() {
		return z_WorkDateDao;
	}

	public GPSXXPlanDao getGPSXXPlanDao() {
		return gpsXXPlanDao;
	}
}
