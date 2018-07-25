package com.moons.xst.sqlite;

import java.util.Map;

import com.moons.xst.track.bean.BillUsers;
import com.moons.xst.track.bean.Operate_Bill;
import com.moons.xst.track.bean.Operate_Detail_Bill;
import com.moons.xst.track.dao.Operate_BillDao;
import com.moons.xst.track.dao.Operate_DetailBillDao;
import com.moons.xst.track.dao.UsersDao;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class TwoBillSession extends AbstractDaoSession {

	private final DaoConfig Operate_BillDaoConfig;
	private final DaoConfig Operate_DetailBillDaoConfig;
	private final DaoConfig UsersDaoConfig;

	private final Operate_BillDao Operate_BillDao;
	private final Operate_DetailBillDao Operate_DetailBillDao;
	private final UsersDao UsersDao;

	public TwoBillSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);
		Operate_BillDaoConfig = daoConfigMap.get(Operate_BillDao.class).clone();
		Operate_BillDaoConfig.initIdentityScope(type);
		Operate_DetailBillDaoConfig = daoConfigMap.get(Operate_DetailBillDao.class).clone();
		Operate_DetailBillDaoConfig.initIdentityScope(type);
		UsersDaoConfig=daoConfigMap.get(UsersDao.class).clone();
		UsersDaoConfig.initIdentityScope(type);

		Operate_BillDao = new Operate_BillDao(Operate_BillDaoConfig, this);
		Operate_DetailBillDao = new Operate_DetailBillDao(Operate_DetailBillDaoConfig, this);
		UsersDao=new UsersDao(UsersDaoConfig,this);

		registerDao(Operate_Bill.class, Operate_BillDao);
		registerDao(Operate_Detail_Bill.class, Operate_DetailBillDao);
		registerDao(BillUsers.class, UsersDao);
	}

	public void clear() {
		Operate_BillDaoConfig.getIdentityScope().clear();
		Operate_DetailBillDaoConfig.getIdentityScope().clear();
		UsersDaoConfig.getIdentityScope().clear();
	}

	public Operate_BillDao getOperate_BillDao() {
		return Operate_BillDao;
	}

	public Operate_DetailBillDao getOperate_DetailBillDao() {
		return Operate_DetailBillDao;
	}
	
	public UsersDao getUsersDao(){
		return UsersDao;
	}

}
