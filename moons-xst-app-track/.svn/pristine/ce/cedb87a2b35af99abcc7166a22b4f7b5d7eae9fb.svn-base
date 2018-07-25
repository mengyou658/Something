package com.moons.xst.sqlite;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.moons.xst.track.bean.XJ_MobjectStateHis;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class XJHisDataBaseSession extends AbstractDaoSession {
	private final DaoConfig XJ_ResultHisDaoConfig;
	private final DaoConfig XJ_TaskIDPosHisDaoConfig;
	private final DaoConfig XJ_MObjectStateHisDaoConfig;

	private final com.moons.xst.track.dao.XJ_ResultHisDao XJ_ResultHisDao;
	private final com.moons.xst.track.dao.XJ_TaskIDPosHisDao XJ_TaskIDPosHisDao;
	private final com.moons.xst.track.dao.XJ_MObjectStateHisDao XJ_MObjectStateHisDao;

	public XJHisDataBaseSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		XJ_ResultHisDaoConfig = daoConfigMap.get(com.moons.xst.track.dao.XJ_ResultHisDao.class).clone();
		XJ_ResultHisDaoConfig.initIdentityScope(type);

		XJ_TaskIDPosHisDaoConfig = daoConfigMap.get(com.moons.xst.track.dao.XJ_TaskIDPosHisDao.class)
				.clone();
		XJ_TaskIDPosHisDaoConfig.initIdentityScope(type);

		XJ_MObjectStateHisDaoConfig = daoConfigMap.get(com.moons.xst.track.dao.XJ_MObjectStateHisDao.class)
				.clone();
		XJ_MObjectStateHisDaoConfig.initIdentityScope(type);

		XJ_ResultHisDao = new com.moons.xst.track.dao.XJ_ResultHisDao(XJ_ResultHisDaoConfig, this);
		XJ_TaskIDPosHisDao = new com.moons.xst.track.dao.XJ_TaskIDPosHisDao(XJ_TaskIDPosHisDaoConfig,
				this);
		XJ_MObjectStateHisDao = new com.moons.xst.track.dao.XJ_MObjectStateHisDao(
				XJ_MObjectStateHisDaoConfig, this);

		registerDao(XJ_ResultHis.class, XJ_ResultHisDao);
		registerDao(XJ_TaskIDPosHis.class, XJ_TaskIDPosHisDao);
		registerDao(XJ_MobjectStateHis.class, XJ_MObjectStateHisDao);

	}
	private int _lineID = -200;

	public void setLineID(Integer lineID) {
		_lineID = lineID;
	}

	public int getLineID() {
		return _lineID;
	}
	public void clear() {
		XJ_ResultHisDaoConfig.getIdentityScope().clear();
		XJ_TaskIDPosHisDaoConfig.getIdentityScope().clear();
		XJ_MObjectStateHisDaoConfig.getIdentityScope().clear();
	}
	
	 public com.moons.xst.track.dao.XJ_ResultHisDao getXJ_ResultHisDao() {
	        return XJ_ResultHisDao;
	    }
	 
	 public com.moons.xst.track.dao.XJ_TaskIDPosHisDao getXJ_TaskIDPosHisDao() {
	        return XJ_TaskIDPosHisDao;
	    }
	 
	 public com.moons.xst.track.dao.XJ_MObjectStateHisDao getXJ_MObjectStateHisDao() {
	        return XJ_MObjectStateHisDao;
	    }
}
