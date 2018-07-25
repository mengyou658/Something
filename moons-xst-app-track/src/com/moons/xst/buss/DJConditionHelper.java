package com.moons.xst.buss;

import android.content.Context;
import android.util.Log;

import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.dao.Z_ConditionDao;

public class DJConditionHelper {
	
	private static final String TAG = "DJConditionHelper";
	
	private DJDAOSession djdaoSession;
	
	private Z_ConditionDao dlConditionDao; 
	
	InitDJsqlite init = new InitDJsqlite();
	
	static DJConditionHelper _intance;

	public static DJConditionHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJConditionHelper();
		}
		return _intance;
	}
	
	public void loadDJCondition(Context context) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlConditionDao = djdaoSession.getZ_ConditionDao();
			AppContext.conditionBuffer = dlConditionDao.loadAll();
		}
		catch(Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
}