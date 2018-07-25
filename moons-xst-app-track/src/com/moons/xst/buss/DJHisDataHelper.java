package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_ResultHis;
import com.moons.xst.track.bean.MObject_StateHis;
import com.moons.xst.track.bean.XJ_MobjectStateHis;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.dao.DJ_ResultHisDao;
import com.moons.xst.track.dao.MObject_StateHisDao;
import com.moons.xst.track.dao.XJ_MObjectStateHisDao;
import com.moons.xst.track.dao.XJ_ResultHisDao;
import com.moons.xst.track.dao.XJ_TaskIDPosHisDao;

/**
 * 此操作类为点检数据操作相关类
 * 
 * @author yanglin
 * 
 */
public class DJHisDataHelper {
	private static final String TAG = "DJHisDataHelper";
	static DJHisDataHelper _intance;
	private static XJ_ResultHisDao xjResultHisDao;
	XJ_TaskIDPosHisDao xjTaskIDPosHisDao;
	XJ_MObjectStateHisDao xjMObjectStateHisDao;

	public static DJHisDataHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJHisDataHelper();
		}
		return _intance;
	}

	public DJHisDataHelper() {
	}

	/**
	 * 加载规定时间内的设备状态历史结果
	 * 
	 * @param context
	 * @param lineID
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<XJ_MobjectStateHis> loadTimeHisState(Context context,
			int lineID, String startTime, String endTime) {
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjMObjectStateHisDao = xjHisDataBaseSession
					.getXJ_MObjectStateHisDao();
			List<XJ_MobjectStateHis> hisList = xjMObjectStateHisDao
					.loadHisStateByTime(startTime, endTime, lineID);
			return hisList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加载规定时间内的到位历史结果
	 * 
	 * @param context
	 * @param lineID
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<XJ_TaskIDPosHis> loadTimeTaskHisDate(Context context,
			int lineID, String startTime, String endTime) {
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjTaskIDPosHisDao = xjHisDataBaseSession.getXJ_TaskIDPosHisDao();
			if (startTime.length() <= 0 || endTime.length() <= 0) {// 如果没传时间则查询路线下的所有
				return xjTaskIDPosHisDao.getDJHisDataByLineID(lineID);
			}
			List<XJ_TaskIDPosHis> hisList = xjTaskIDPosHisDao
					.loadTaskResultHisByTime(startTime, endTime, lineID);
			return hisList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加载到位历史结果的纽扣
	 * 
	 * @param context
	 * @param lineID
	 * @return
	 */
	public List<String> loadTaskHisNK(Context context, int lineID) {
		List<String> list = new ArrayList<String>();
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjTaskIDPosHisDao = xjHisDataBaseSession.getXJ_TaskIDPosHisDao();

			String sql = "select distinct IDPosDesc_TX from DJ_TASKIDPOSHIS WHERE Line_ID='"
					+ lineID + "'";
			Cursor c = ((AppContext) context.getApplicationContext()).xjHisDataDB
					.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				while (c.moveToNext()) {
					list.add(c.getString(c.getColumnIndex("IDPosDesc_TX")));
				}
			}
			c.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}

	/**
	 * 加载规定时间内的点检历史结果
	 * 
	 * @param context
	 * @param lineID
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<XJ_ResultHis> loadTimeDJHisDate(Context context, int lineID,
			String startTime, String endTime, String type) {
		List<XJ_ResultHis> hisList=new ArrayList<XJ_ResultHis>();
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjResultHisDao = xjHisDataBaseSession.getXJ_ResultHisDao();
			if (startTime.length() <= 0 || endTime.length() <= 0) {// 如果没传时间则查询路线下的所有
				return xjResultHisDao.loadDJResultHisByTime(lineID);
			}
			hisList = xjResultHisDao.loadDJResultHisByTime(
					startTime, endTime, lineID, type);
			return hisList;
		} catch (Exception e) {
			e.printStackTrace();
			return hisList;
		}
	}

	/**
	 * 加载点检历史纽扣
	 * 
	 * @param context
	 * @param lineID
	 * @return
	 */
	public List<String> loadDJHisNK(Context context, int lineID) {
		List<String> list = new ArrayList<String>();
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjResultHisDao = xjHisDataBaseSession.getXJ_ResultHisDao();

			String sql = "select distinct IDPosName_TX from DJ_RESULTHIS WHERE Line_ID='"
					+ lineID + "'";
			Cursor c = ((AppContext) context.getApplicationContext()).xjHisDataDB
					.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				while (c.moveToNext()) {
					list.add(c.getString(c.getColumnIndex("IDPosName_TX")));
				}
			}
			c.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}
	}

	/**
	 * 结果库查询点检历史结果
	 * 
	 * @param context
	 * @param Line_ID
	 * @param planid
	 * @return
	 */
	public List<XJ_ResultHis> loadDJHisData(Context context, String lineID,
			String planid) {
		try {
			XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
					.getApplicationContext()).getXJHisDataBaseSession();
			xjResultHisDao = xjHisDataBaseSession.getXJ_ResultHisDao();
			List<XJ_ResultHis> allhislist = xjResultHisDao
					.loadDJResultHisByPlanID(lineID, planid);

			return allhislist;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 检测历史数据库是否存在
	 * 
	 * @param context
	 * @return
	 */
	public boolean checkXJHisDataExist(Context context) {
		String DataBaseName = AppConst.CurrentResultPath("XJHisDataBase")
				+ AppConst.ResultDBName("XJHisDataBase");
		if (!FileUtils.checkFileExists(DataBaseName)) {
			return false;
		}
		return true;
	}

	public void clearHisData(Context context, String date) {
		XJHisDataBaseSession xjHisDataBaseSession = ((AppContext) context
				.getApplicationContext()).getXJHisDataBaseSession();
		xjResultHisDao = xjHisDataBaseSession.getXJ_ResultHisDao();
		xjResultHisDao.clearHisData(date);
	}
}
