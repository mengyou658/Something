package com.moons.xst.sqlite;

import java.util.Map;

import com.moons.xst.track.bean.OperDetailResult;
import com.moons.xst.track.bean.OperMainResult;
import com.moons.xst.track.bean.Shift_REsult;
import com.moons.xst.track.bean.WH_DQTask;
import com.moons.xst.track.dao.OperDetailResultDao;
import com.moons.xst.track.dao.OperMainResultDao;
import com.moons.xst.track.dao.Shift_REsultDao;
import com.moons.xst.track.dao.WH_DQTaskDao;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class TwoBillResultDaoSession extends AbstractDaoSession {
	private final DaoConfig OperDetailResultDaoConfig;
	private final DaoConfig OperMainResultDaoConfig;
	private final DaoConfig Shift_REsultDaoConfig;
	private final DaoConfig WH_DQTaskDaoConfig;
	
	private final OperDetailResultDao OperDetailResultDao;
	private final OperMainResultDao OperMainResultDao;
	private final Shift_REsultDao Shift_REsultDao;
	private final WH_DQTaskDao WH_DQTaskDao;

	public TwoBillResultDaoSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);
		OperDetailResultDaoConfig = daoConfigMap.get(OperDetailResultDao.class).clone();
		OperDetailResultDaoConfig.initIdentityScope(type);
		OperMainResultDaoConfig = daoConfigMap.get(OperMainResultDao.class).clone();
		OperMainResultDaoConfig.initIdentityScope(type);
		Shift_REsultDaoConfig = daoConfigMap.get(Shift_REsultDao.class).clone();
		Shift_REsultDaoConfig.initIdentityScope(type);
		WH_DQTaskDaoConfig = daoConfigMap.get(WH_DQTaskDao.class).clone();
		WH_DQTaskDaoConfig.initIdentityScope(type);

		OperDetailResultDao = new OperDetailResultDao(OperDetailResultDaoConfig, this);
		OperMainResultDao = new OperMainResultDao(OperMainResultDaoConfig, this);
		Shift_REsultDao = new Shift_REsultDao(Shift_REsultDaoConfig, this);
		WH_DQTaskDao = new WH_DQTaskDao(WH_DQTaskDaoConfig, this);

		registerDao(OperDetailResult.class, OperDetailResultDao);
		registerDao(OperMainResult.class, OperMainResultDao);
		registerDao(Shift_REsult.class, Shift_REsultDao);
		registerDao(WH_DQTask.class, WH_DQTaskDao);
		
	}

	public void clear() {
		OperDetailResultDaoConfig.getIdentityScope().clear();
		OperMainResultDaoConfig.getIdentityScope().clear();
		Shift_REsultDaoConfig.getIdentityScope().clear();
		WH_DQTaskDaoConfig.getIdentityScope().clear();
	}

	public OperDetailResultDao getOperDetailResultDao() {
		return OperDetailResultDao;
	}
	
	public OperMainResultDao getOperMainResultDao(){
		return OperMainResultDao;
	}
	
	public Shift_REsultDao getShift_REsultDao(){
		return Shift_REsultDao;
	}
	
	public WH_DQTaskDao getWH_DQTaskDao(){
		return WH_DQTaskDao;
	}

}
