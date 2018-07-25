package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.moons.xst.sqlite.TempMeasureDaoSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.ProvisionalM;
import com.moons.xst.track.bean.TempMeasureBaseInfo;
import com.moons.xst.track.dao.ProvisionalMDao;
import com.moons.xst.track.dao.TempMeasureBaseInfoDao;

public class TempMeasureDBHelper {

	private static final String TAG = "TempMeasureDBHelper";
	private TempMeasureDaoSession tempMeasureDaoSession;

	private TempMeasureBaseInfoDao tempMeasureBaseInfoDao;

	private ProvisionalMDao provisionalMDao;

	InitDJsqlite init = new InitDJsqlite();
	static TempMeasureDBHelper _intance;

	public static TempMeasureDBHelper GetIntance() {
		if (_intance == null) {
			_intance = new TempMeasureDBHelper();
		}
		return _intance;
	}

	public void InsertTempMeasureBaseInfo(Context context,
			TempMeasureBaseInfo entity) {
		try {
			tempMeasureDaoSession = ((AppContext) context
					.getApplicationContext()).getTempMeasureSession(0);
			tempMeasureBaseInfoDao = tempMeasureDaoSession
					.getTempMeasureBaseInfoDao();
			long row = tempMeasureBaseInfoDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public boolean InsertCeZhenBaseInfo(Context context, ProvisionalM entity) {
		try {
			tempMeasureDaoSession = ((AppContext) context
					.getApplicationContext()).getTempMeasureSession(0);
			if (tempMeasureDaoSession != null) {
				provisionalMDao = tempMeasureDaoSession.getProvisionalMDao();
				long row = provisionalMDao.insert(entity);
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 查询所有临时测量记录
	 * 
	 * @param context
	 * @return
	 */
	public List<ProvisionalM> loadVibrationRecord(Context context){
		List<ProvisionalM> list=new ArrayList<ProvisionalM>();
		try{
			tempMeasureDaoSession = ((AppContext) context
					.getApplicationContext()).getTempMeasureSession(0);
			provisionalMDao = tempMeasureDaoSession.getProvisionalMDao();
			list=provisionalMDao.loadAll();
		}catch(Exception e){
		}
		return list;
	}

	public void DeleteTempMeasureBaseInfo(Context context,
			TempMeasureBaseInfo entity) {
		try {
			String sqlStr = "Delete From TempMeasureBaseInfo Where GUID = '"
					+ entity.getGUID() + "'";
			tempMeasureBaseInfoDao.Operatesql(sqlStr);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
}