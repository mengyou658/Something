/**
 * @TrackHepler.java
 * @author LKZ
 * @2015-1-20
 */
package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.GPSMobjectBugResultHis;
import com.moons.xst.track.bean.GPSXXPlan;
import com.moons.xst.track.bean.MobjectBugCodeInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.GPSHelper;
import com.moons.xst.track.dao.CheckPointDAO;
import com.moons.xst.track.dao.GPSMobjectBugResultHisDao;
import com.moons.xst.track.dao.GPSXXPlanDao;
import com.moons.xst.track.dao.MobjectBugCodeDAO;

/**
 * 巡线业务处理类
 */
public class TrackHelper {

	private static final String TAG = "TrackHepler";
	/**
	 * 加载路线下的ID位置记录
	 */
	private DJDAOSession djdaoSession;

	private CheckPointDAO checkpointDAO;
	private MobjectBugCodeDAO mobjectbugcodeDAO;
	private GPSMobjectBugResultHisDao gpsMobjectBugResultHisDao;
	private GPSXXPlanDao gpsxxPlanDao;

	InitDJsqlite init = new InitDJsqlite();
	private List<CheckPointInfo> lvCheckPointData;

	/**
	 * 加载考核点信息
	 * 
	 * @param context
	 */
	public synchronized List<CheckPointInfo> loadCheckPointData(Context context) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			checkpointDAO = djdaoSession.getCheckPointDAO();
			lvCheckPointData = checkpointDAO.loadAll();
			CycleHelper.GetIntance().loadCycleData(context);
			// 加载数据
			AppContext.trackCheckPointBuffer.clear();
			AppContext.orderSortTrackCheckPointBuffer.clear();
			for (CheckPointInfo cpInfo : lvCheckPointData) {
				if (AppContext.DJCycleBuffer == null
						|| AppContext.DJCycleBuffer.size() <= 0
						|| cpInfo.getCycle_ID() <= 0) {
					if (cpInfo.getKHDate_DT() != null
							&& DateTimeHelper
									.getDateNow()
									.equals(DateTimeHelper.TransDateTime(
											cpInfo.getKHDate_DT(), "yyyy-MM-dd"))) {
						cpInfo.setShakeNum(3);
					}
					AppContext.trackCheckPointBuffer.add(cpInfo);
					AppContext.orderSortTrackCheckPointBuffer.add(cpInfo);
					AppContext.orderCheckPointYN = cpInfo.getOrder_YN().equals(
							"1");
				} else {
					cpInfo.setShakeNum(3);
					Cycle cyc = GetCycleByID(String.valueOf(cpInfo.getCycle_ID()));
					cpInfo.SetCycle(cyc);
					for (Cycle cycle : AppContext.DJCycleBuffer) {
						if (cycle.getDJCycle().getCycle_ID()
								.equals(String.valueOf(cpInfo.getCycle_ID()))&& !cpInfo.JudgeIsCompleted(DateTimeHelper.GetDateTimeNow())) {
							cpInfo.setShakeNum(0);
						}
					}
					AppContext.trackCheckPointBuffer.add(cpInfo);
					AppContext.orderSortTrackCheckPointBuffer.add(cpInfo);
					AppContext.orderCheckPointYN = cpInfo.getOrder_YN().equals(
							"1");
				}
			}
			if (AppContext.orderSortTrackCheckPointBuffer != null
					&& AppContext.orderSortTrackCheckPointBuffer.size() > 0)
				GPSHelper
						.sortCheckPoint(AppContext.orderSortTrackCheckPointBuffer);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
		return lvCheckPointData;
	}
	
	/**
	 * 根据周期ID获取内存中的周期对象
	 * 
	 * @param cycleID
	 * @return
	 */
	private Cycle GetCycleByID(String cycleID) {

		for (Cycle _cycle : AppContext.DJCycleBuffer) {
			if (_cycle.getDJCycle().getCycle_ID().equals(cycleID)) {
				return _cycle;

			}
		}
		return null;
	}

	/**
	 * 加载巡线计划
	 * 
	 * @param context
	 * @param cpInfoID
	 */
	public synchronized void loadGPSXXPlanData(Context context, String cpInfoID) {
		// 加载数据
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsxxPlanDao = djdaoSession.getGPSXXPlanDao();
			List<GPSXXPlan> lvGPSXXPlanData = gpsxxPlanDao.loadAll(String
					.valueOf(cpInfoID));
			// 加载数据
			if (!AppContext.gpsXXPlanBuffer.containsKey(cpInfoID))
				AppContext.gpsXXPlanBuffer.put(cpInfoID,
						new ArrayList<GPSXXPlan>());
			else {
				AppContext.gpsXXPlanBuffer.get(cpInfoID).clear();
			}
			for (GPSXXPlan gpsxxPlan : lvGPSXXPlanData) {
				AppContext.gpsXXPlanBuffer.get(cpInfoID).add(gpsxxPlan);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 加载查询考核点信息
	 * 
	 * @param context
	 */
	public List<CheckPointInfo> queryCheckPointData(Context context, boolean first_yn, int lineid,int offset, int maxResult) {
		try {
			List<CheckPointInfo> lvDJIDPosStatusData = new ArrayList<CheckPointInfo>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( lineid);
			checkpointDAO = djdaoSession.getCheckPointDAO();
			if(first_yn){
				AppContext.CheckPointIDPosStatusDataBuffer= checkpointDAO.loadAll();
			}
			 if(AppContext.CheckPointIDPosStatusDataBuffer.size()<offset * maxResult + 10 ){
					lvDJIDPosStatusData=AppContext.CheckPointIDPosStatusDataBuffer.subList(offset * maxResult,AppContext.CheckPointIDPosStatusDataBuffer.size());
				}else{
					lvDJIDPosStatusData=AppContext.CheckPointIDPosStatusDataBuffer.subList(offset * maxResult, offset * maxResult + 10 );
				}
			return lvDJIDPosStatusData;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 加载故障代码库信息
	 * 
	 * @param context
	 */
	public void loadMobjectBugCodeData(Context context) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			mobjectbugcodeDAO = djdaoSession.getMobjectBugCodeDAO();
			List<MobjectBugCodeInfo> lvMobjectBugCodeData = mobjectbugcodeDAO
					.loadAll();
			// 加载数据
			AppContext.trackMobjectBugCodeBuffer.clear();
			for (MobjectBugCodeInfo mbcInfo : lvMobjectBugCodeData) {
				AppContext.trackMobjectBugCodeBuffer.add(mbcInfo);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 更新考核点信息
	 * 
	 * @param context
	 */
	public void updateCheckPointData(Context context, CheckPointInfo entity) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			checkpointDAO = djdaoSession.getCheckPointDAO();
			checkpointDAO.UpdateGps(entity);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 查询报缺历史记录
	 * 
	 * @param context
	 * @param entity
	 */
	public List<GPSMobjectBugResultHis> loadGPSBugHis(Context context) {
		try {
			List<GPSMobjectBugResultHis> list = new ArrayList<GPSMobjectBugResultHis>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			gpsMobjectBugResultHisDao = djdaoSession
					.getGPSMobjectBugResultHisDao();
			list = gpsMobjectBugResultHisDao.loadAll();
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}

}
