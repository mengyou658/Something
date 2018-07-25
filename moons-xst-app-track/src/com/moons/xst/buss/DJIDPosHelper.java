package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.CycleByIDPos;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.DJ_TaskIDPosHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_DataCodeGroupItem;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.dao.CycleByIDPosDao;
import com.moons.xst.track.dao.DJ_IDPosDao;
import com.moons.xst.track.dao.DJ_TaskIDPosActiveDao;
import com.moons.xst.track.dao.DJ_TaskIDPosHisDao;
import com.moons.xst.track.dao.SRLineTreeByIDPosDao;
import com.moons.xst.track.dao.XJ_TaskIDPosHisDao;
import com.moons.xst.track.dao.Z_AlmLevelDao;
import com.moons.xst.track.dao.Z_DataCodeGroupDao;
import com.moons.xst.track.dao.Z_DataCodeGroupItemDao;

public class DJIDPosHelper {

	private static final String TAG = "DJIDHelper";
	
	private final static String STATE_PROCESSING = "PROCESSING";
	private final static String STATE_FINISHED= "FINISHED";
	
	/**
	 * 加载路线下的ID位置记录
	 */
	private DJDAOSession djdaoSession;
	
	private DJResultDAOSession djresultdaoSession;
	
	private XJHisDataBaseSession xjHisDBSession;

	private DJ_IDPosDao djidposDAO;

	private DJ_IDPosDao dlPlanDAO;

	private Z_DataCodeGroupDao dlDataCodeGroupDao;

	private Z_DataCodeGroupItemDao dlDataCodeGroupItemDao;

	private Z_AlmLevelDao dlAlmLevelDao;

	private DJ_TaskIDPosHisDao djtaskIDPosHisDao;
	
	private DJ_TaskIDPosActiveDao djtaskIDPosActiveDao;
	
	private XJ_TaskIDPosHisDao xjtaskIDPosHisDao;

	private CycleByIDPosDao cycleByIDPosDao;

	private DJ_TaskIDPosHisDao dj_TaskIDPosHisDao;

	private HashMap<String, String> abHashMap = new HashMap<String, String>();

	private HashMap<String, String> unabHashMap = new HashMap<String, String>();

	InitDJsqlite init = new InitDJsqlite();

	static DJIDPosHelper _intance;

	public static DJIDPosHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJIDPosHelper();
		}
		return _intance;
	}

	public boolean loadDJPosData(Context context) {
		try {
			Intent mIntent = new Intent();
			// 钮扣总数
			AppContext.TotalCount = 0;
			// 有计划的钮扣个数
			AppContext.NeedArriveCount = 0;
			// 已完成的或没计划钮扣个数
			AppContext.CompleteCount = 0;
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			djidposDAO = djdaoSession.getDJ_IDPosDao();
			dlDataCodeGroupDao = djdaoSession.getZ_DataCodeGroupDao();
			dlDataCodeGroupItemDao = djdaoSession.getZ_DataCodeGroupItemDao();
			dlAlmLevelDao = djdaoSession.getZ_AlmLevelDao();
			AppContext.almLevelBuffer.clear();
			AppContext.almLevelBuffer = dlAlmLevelDao.loadAll();
			AppContext.dataCodeBuffer.clear();
			AppContext.dataCodeBuffer = dlDataCodeGroupDao.loadAll();
			List<Z_DataCodeGroupItem> dataCodeGroupItems = dlDataCodeGroupItemDao
					.loadAll();
			if (AppContext.dataCodeBuffer != null
					&& AppContext.dataCodeBuffer.size() > 0
					&& dataCodeGroupItems != null
					&& dataCodeGroupItems.size() > 0) {
				for (Z_DataCodeGroup z_DataCodeGroup : AppContext.dataCodeBuffer) {
					ArrayList<Z_DataCodeGroupItem> _temDataCodeGroupItems = new ArrayList<Z_DataCodeGroupItem>();
					for (Z_DataCodeGroupItem z_DataCodeGroupItem : dataCodeGroupItems) {
						if (z_DataCodeGroup.getDataCodeGroup_ID().equals(
								String.valueOf(z_DataCodeGroupItem
										.getDataCodeGroup_ID()))) {
							_temDataCodeGroupItems.add(z_DataCodeGroupItem);
						}
					}
					z_DataCodeGroup
							.setDataCodeGroupItems(_temDataCodeGroupItems);
				}
			}
			
			List<DJ_IDPos> lvDJIDPosStatusData = new ArrayList<DJ_IDPos>();
			/**
			 * 根据路线类型加载钮扣
			 * 目前只有巡点检和条件巡检
			 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线;8-条件巡检
			 */
			switch (AppContext.getCurrLineType()) {
			case 0:
				lvDJIDPosStatusData = djidposDAO.loadAllForXDJ();
				break;
			case 1:
				lvDJIDPosStatusData = djidposDAO.loadAllForXDJ();
				break;
			case 8:
				lvDJIDPosStatusData = djidposDAO.loadAllForCASEXJ();
				break;
			default:
				lvDJIDPosStatusData = djidposDAO.loadAll();
				break;
			}
			// 加载数据
			AppContext.DJIDPosBuffer.clear();
			AppContext.orderDJIDPosBuffer.clear();
			for (DJ_IDPos djIDPosStatus : lvDJIDPosStatusData) {

				DJPlanHelper djplanHelper = new DJPlanHelper();
				djplanHelper.loadPlanData(context, djIDPosStatus.getIDPos_ID());

				djIDPosStatus.setLastUser(djIDPosStatus.getLastUser());
				djIDPosStatus.setLastArrivedTime_DT(djIDPosStatus
						.getLastArrivedTime_DT());
				Integer canDoCount = LoadCanDoPlan(djIDPosStatus
						.getIDPos_ID());
				Integer completeCount = GetCompleteNum(djIDPosStatus
						.getIDPos_ID());
				Integer srNotNeedDoNum = GetSRNotNeedDoNumForIDPosState(djIDPosStatus
						.getIDPos_ID());
//				Integer srNotNeedDoNum = DJIDPosHelper
//						.GetSKNotNeedDoNum(djIDPosStatus.getIDPos_ID());
				
				String planCount = XSTMethodByLineTypeHelper.getInstance()
						.setPlanCount(context, completeCount, srNotNeedDoNum, canDoCount);
				djIDPosStatus.setPlanCount(planCount);
				/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					if (AppContext.DJSpecCaseFlag == 1) {
						djIDPosStatus.setPlanCount(context.getString(R.string.plan_speccasecount, canDoCount));
					} else {
						djIDPosStatus
						.setPlanCount((completeCount + srNotNeedDoNum) + "/" + canDoCount);
					}
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					djIDPosStatus.setPlanCount(completeCount + "/" + canDoCount);
				}*/
				
				if (completeCount + srNotNeedDoNum < canDoCount) {
					AppContext.NeedArriveCount++;
				}
				
				if (canDoCount <= 0) {
					djIDPosStatus
							.setIDPosState(AppConst.IDPOS_STATUS_NOTNEEDED);
				}
				else if (completeCount > 0 && completeCount < canDoCount) {
					if (completeCount + srNotNeedDoNum >= canDoCount) {
						djIDPosStatus
								.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
						AppContext.CompleteCount++;
					} else {
						djIDPosStatus
								.setIDPosState(AppConst.IDPOS_STATUS_NOTFINISHED);
					}
				} else if (completeCount > 0 && completeCount.equals(canDoCount)) {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
					AppContext.CompleteCount++;
				} else if (completeCount == 0) {
					if (srNotNeedDoNum.equals(canDoCount)) {
						djIDPosStatus
								.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
						AppContext.CompleteCount++;
					} else {
						djIDPosStatus
								.setIDPosState(AppConst.IDPOS_STATUS_NEEDED);
					}
				} else {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_NULL);
				}
				AppContext.orderIDPosYN = djIDPosStatus.getOrder_YN().equals("1");
//				AppContext.orderDJIDPosBuffer.add(djIDPosStatus);
				AppContext.TotalCount++;
				
				mIntent.setAction(STATE_PROCESSING); 
				mIntent.putExtra("idposinfo", getArraylist(djIDPosStatus));
				mIntent.putExtra("percent", (AppContext.TotalCount * 100) / lvDJIDPosStatusData.size());
                context.sendBroadcast(mIntent);
			}
			/*if (AppContext.orderDJIDPosBuffer!=null
					&& AppContext.orderDJIDPosBuffer.size()>0) {
				sortIDPos(AppContext.orderDJIDPosBuffer);
			}*/
			
			mIntent.setAction(STATE_FINISHED); 
			context.sendBroadcast(mIntent);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	private ArrayList<DJ_IDPos> getArraylist(DJ_IDPos idposinfo) {
		ArrayList<DJ_IDPos> res = new ArrayList<DJ_IDPos>();
		res.add(idposinfo);
		
		return res;
	}

	/**
	 * 根据强制顺序排序
	 * 
	 * @param cpList
	 */
	public static void sortIDPos(List<DJ_IDPos> cpList) {
		if (cpList == null || cpList.size() <= 0)
			return;
		Collections.sort(cpList, new Comparator<DJ_IDPos>() {
			@Override
			public int compare(DJ_IDPos lhs, DJ_IDPos rhs) {
				// TODO 自动生成的方法存根
				int a = lhs.getSort_NR();
				int b = rhs.getSort_NR();
				if (b > a)
					return -1;
				if (b < a)
					return 1;
				return 0;
			}
		});
	}
	/**
	 * 传入路线id加载钮扣
	 * 
	 */
	public synchronized List<DJ_IDPos> loadDJPosData(Context context, boolean first_yn, int lineid,int offset, int maxResult) {
		try {
			List<DJ_IDPos> lvDJIDPosStatusData = new ArrayList<DJ_IDPos>();
			List<DJ_IDPos> result = new ArrayList<DJ_IDPos>();
			
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( lineid);
			djidposDAO = djdaoSession.getDJ_IDPosDao();
			if (first_yn) {
				AppContext.DJIDPosStatusDataBuffer = djidposDAO.loadAll();
			}
			 if(AppContext.DJIDPosStatusDataBuffer.size()<offset * maxResult + 10 ){
				lvDJIDPosStatusData=AppContext.DJIDPosStatusDataBuffer.subList(offset * maxResult,AppContext.DJIDPosStatusDataBuffer.size());
			}else{
				lvDJIDPosStatusData=AppContext.DJIDPosStatusDataBuffer.subList(offset * maxResult, offset * maxResult + 10 );
			}
			for (DJ_IDPos djIDPosStatus : lvDJIDPosStatusData) {

				List<DJPlan> plans = new ArrayList<DJPlan>();
				DJPlanHelper djplanHelper = new DJPlanHelper();
				plans = djplanHelper.loadPlanData(context,
						djIDPosStatus.getIDPos_ID(), lineid);
				djIDPosStatus.setPlanList(plans);
				djIDPosStatus.setLastUser(djIDPosStatus.getLastUser());
				djIDPosStatus.setLastArrivedTime_DT(djIDPosStatus
						.getLastArrivedTime_DT());
				Integer canDoCount = LoadCanDoPlan(djIDPosStatus.getIDPos_ID());
				Integer completeCount = GetCompleteNum(djIDPosStatus
						.getIDPos_ID());				
				Integer srNotNeedDoNum = GetSRNotNeedDoNumForIDPosState(djIDPosStatus
						.getIDPos_ID());
				
				String planCount = XSTMethodByLineTypeHelper.getInstance()
						.setPlanCount(context, completeCount, srNotNeedDoNum, canDoCount);
				djIDPosStatus.setPlanCount(planCount);
				/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					djIDPosStatus.setPlanCount((completeCount + srNotNeedDoNum) + "/" + canDoCount);
				}
				else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType())
					djIDPosStatus.setPlanCount(context.getString(R.string.plan_count, canDoCount));
				else
					djIDPosStatus.setPlanCount(completeCount + "/" + canDoCount);*/
							
				if (canDoCount <= 0)
					djIDPosStatus
							.setIDPosState(AppConst.IDPOS_STATUS_NOTNEEDED);
				else if (completeCount > 0 && completeCount < canDoCount) {
					djIDPosStatus
							.setIDPosState(AppConst.IDPOS_STATUS_NOTFINISHED);
				} else if (completeCount > 0 && completeCount.equals(canDoCount)) {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
				} else if (completeCount == 0) {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_NEEDED);
				} else {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_NULL);
				}
				result.add(djIDPosStatus);
			}
			return result;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 传入路线id加载钮扣
	 * 
	 */
	/*public List<DJ_IDPos> loadDJPosData(Context context, int lineid) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( lineid);
			djidposDAO = djdaoSession.getDJ_IDPosDao();
			List<DJ_IDPos> lvDJIDPosStatusData = djidposDAO.loadAll();

			for (DJ_IDPos djIDPosStatus : lvDJIDPosStatusData) {

				List<DJPlan> plans = new ArrayList<DJPlan>();
				DJPlanHelper djplanHelper = new DJPlanHelper();
				plans = djplanHelper.loadPlanData(context,
						djIDPosStatus.getIDPos_ID(), lineid);
				djIDPosStatus.setPlanList(plans);
				djIDPosStatus.setLastUser(djIDPosStatus.getLastUser());
				djIDPosStatus.setLastArrivedTime_DT(djIDPosStatus
						.getLastArrivedTime_DT());
				Integer canDoCount = LoadCanDoPlan(djIDPosStatus.getIDPos_ID());
				Integer completeCount = GetCompleteNum(djIDPosStatus
						.getIDPos_ID());				
				Integer srNotNeedDoNum = GetSRNotNeedDoNumForIDPosState(djIDPosStatus
						.getIDPos_ID());
				
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					djIDPosStatus.setPlanCount((completeCount + srNotNeedDoNum) + "/" + canDoCount);
				}
				else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType())
					djIDPosStatus.setPlanCount(context.getString(R.string.plan_count) + canDoCount);
				else
					djIDPosStatus.setPlanCount(completeCount + "/" + canDoCount);
							
				if (canDoCount <= 0)
					djIDPosStatus
							.setIDPosState(AppConst.IDPOS_STATUS_NOTNEEDED);
				else if (completeCount > 0 && completeCount < canDoCount) {
					djIDPosStatus
							.setIDPosState(AppConst.IDPOS_STATUS_NOTFINISHED);
				} else if (completeCount > 0 && completeCount.equals(canDoCount)) {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_FINISHED);
				} else if (completeCount == 0) {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_NEEDED);
				} else {
					djIDPosStatus.setIDPosState(AppConst.IDPOS_STATUS_NULL);
				}

			}
			return lvDJIDPosStatusData;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}*/

	/**
	 * 查询某路线下某钮扣历史到位信息
	 * 
	 */
	public List<XJ_TaskIDPosHis> loadIDPosHis(Context context, int lineid,
			String idposid) {
		try {
			List<XJ_TaskIDPosHis> idhislist = new ArrayList<XJ_TaskIDPosHis>();
			xjHisDBSession = ((AppContext) context.getApplicationContext()).getXJHisDataBaseSession();
			xjtaskIDPosHisDao = xjHisDBSession.getXJ_TaskIDPosHisDao();
			idhislist = xjtaskIDPosHisDao.getDJIDPosHisDataByLineID(String.valueOf(lineid), idposid);
			return idhislist;
			
			
			/*List<DJ_TaskIDPosHis> idhislist = new ArrayList<DJ_TaskIDPosHis>();
			List<DJ_TaskIDPosHis> reidhislist = new ArrayList<DJ_TaskIDPosHis>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(lineid);
			djtaskIDPosHisDao = djdaoSession.getDJ_TaskIDPosHisDao();
			idhislist = djtaskIDPosHisDao.loadAll();
			for (DJ_TaskIDPosHis posHis : idhislist) {
				if (posHis.getIDPos_ID().compareTo(idposid) == 0) {
					reidhislist.add(posHis);
				}
			}
			return reidhislist;*/
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/**
	 * 计算钮扣下可做计划数
	 * 
	 * @param idPosID
	 * @param dt
	 * @return
	 */
	public static Integer LoadCanDoPlan(String idPosID) {
		try {
			List<DJPlan> djPlanlist = AppContext.DJPlanBuffer.get(idPosID);
			if (djPlanlist == null || djPlanlist.size() <= 0)
				return 0;
			Integer cycCount = 0;
			if (!AppContext.YXDJPlanByIDPosBuffer.containsKey(idPosID)) {
				AppContext.YXDJPlanByIDPosBuffer.put(idPosID, new ArrayList<DJPlan>());
			} else {
				AppContext.YXDJPlanByIDPosBuffer.get(idPosID).clear();
			}
			
			
			Date dt = DateTimeHelper.StringToDate(DateTimeHelper
					.getDateTimeNow());
			for (DJPlan _planInfo : djPlanlist) {
				if (XSTMethodByLineTypeHelper.getInstance().loadYXDJPlanByIDPos(dt, _planInfo)) {
					AppContext.YXDJPlanByIDPosBuffer.get(idPosID).add(_planInfo);
					cycCount = cycCount + 1;
				}
			}
			/**
			 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线;8-条件巡检
			 */
			/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				if (AppContext.DJSpecCaseFlag == 1) {
					for (DJPlan _planInfo : djPlanlist) {
						if (_planInfo
								.JudgePlanSpecCase(AppContext.selectedDJSpecCaseStr)) {
							_planInfo.getDJPlan()
							   .setLastResultForDifferent_TX(_planInfo.getDJPlan().getLastResult_TX());
							AppContext.YXDJPlanByIDPosBuffer.get(idPosID).add(_planInfo);
							cycCount = cycCount + 1;
						}
					}
				} else {
					Date dt = DateTimeHelper.StringToDate(DateTimeHelper
							.getDateTimeNow());
					for (DJPlan _planInfo : djPlanlist) {
						if (_planInfo.JudgePlanCycle(dt)) {
							String sdate = DateTimeHelper.DateToString(_planInfo.GetCycle().getTaskBegin());
							String edate = DateTimeHelper.DateToString(_planInfo.GetCycle().getTaskEnd());
							if (!_planInfo.JudgePlanDisable()) {
								_planInfo.getDJPlan()
								   .setLastResultForDifferent_TX(_planInfo.getDJPlan().getLastResult_TX());
								AppContext.YXDJPlanByIDPosBuffer.get(idPosID).add(_planInfo);
								cycCount = cycCount + 1;
							}
						}
	
					}
				}
			}
			else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				for (DJPlan _planInfo : djPlanlist) {
					AppContext.YXDJPlanByIDPosBuffer.get(idPosID).add(_planInfo);
					cycCount = cycCount + 1;
				}
			}*/
			
			return cycCount;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取完成计划数
	 * 
	 * @param idPosID
	 * @return
	 */
	public static Integer GetCompleteNum(String idPosID) {
		try {

			if (!AppContext.YXDJPlanByIDPosBuffer.containsKey(idPosID) 
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID) == null
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID).size() <= 0)
				return 0;
			
			return XSTMethodByLineTypeHelper.getInstance().getCompleteNum(idPosID);
			
			// 特巡返回0,条件巡检返回0
			/*if (AppContext.DJSpecCaseFlag == 1 
					|| AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType())
				return 0;
			Integer completeCount = 0;
			Date dt = DateTimeHelper.StringToDate(DateTimeHelper
					.getDateTimeNow());
			for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idPosID)) {

				if (_planInfo.JudgePlanIsCompleted(dt)
						&& _planInfo.JudgePlanBySrPoint())
					completeCount = completeCount + 1;
			}

			return completeCount;*/
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Integer GetCompleteNum(Context context, String idPosID) {
		try {
			if (!AppContext.YXDJPlanByIDPosBuffer.containsKey(idPosID) 
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID) == null
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID).size() <= 0)
				return 0;
			
			Integer completeCount = 0;
			for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idPosID)) {

				if (_planInfo.JudgePlanIsCompletedForCASEXJ(context)
						&& _planInfo.JudgePlanBySrPoint())
					completeCount = completeCount + 1;
			}

			return completeCount;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 获取启停点下无需做的计划数目（钮扣加载计算时使用）
	 * @param idPosID
	 * @return
	 */
	public static Integer GetSRNotNeedDoNumForIDPosState(String idPosID) {
		try {

			if (!AppContext.YXDJPlanByIDPosBuffer.containsKey(idPosID) 
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID) == null
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID).size() <= 0)
				return 0;

			Integer notNeedDoCount = 0;
			Date dt = DateTimeHelper.StringToDate(DateTimeHelper
					.getDateTimeNow());
			for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idPosID)) {

				if (Integer.parseInt(_planInfo.getDJPlan().getSRPoint_ID()) > 0) {
					//增加是否在周期内已经设置了启停点
					if (_planInfo.SrIsSetting() && !_planInfo.JudgePlanBySrPoint())
						notNeedDoCount = notNeedDoCount + 1;
				}
			}

			return notNeedDoCount;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取启停点下无需做的计划数目
	 * 
	 * @param idPosID
	 * @return
	 */
	public static Integer GetSKNotNeedDoNum(String idPosID) {
		try {

			if (!AppContext.YXDJPlanByIDPosBuffer.containsKey(idPosID) 
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID) == null
					|| AppContext.YXDJPlanByIDPosBuffer.get(idPosID).size() <= 0)
				return 0;

			Integer notNeedDoCount = 0;
			Date dt = DateTimeHelper.StringToDate(DateTimeHelper
					.getDateTimeNow());
			for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idPosID)) {

				if (!_planInfo.JudgePlanBySrPoint())
					notNeedDoCount = notNeedDoCount + 1;
			}

			return notNeedDoCount;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 更新钮扣信息
	 * 
	 * @param plan
	 * @return
	 */
	public void updateDJIDPos(Context context, DJ_IDPos idpos) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlPlanDAO = djdaoSession.getDJ_IDPosDao();
			dlPlanDAO.UpdateDJIDPos(idpos);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新钮扣到位结束时间
	 * @param context
	 * @param idpos
	 */
	public void updateDJTaskIDPos(Context context, DJ_IDPos idpos, String lineID){
		try{
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			
			djresultdaoSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			
			xjHisDBSession = ((AppContext) context.getApplicationContext()).getXJHisDataBaseSession();
			
			String endTM = DateTimeHelper.getDateTimeNow();
			/* 更新活动表  */
			djtaskIDPosActiveDao = djresultdaoSession.getDJ_TaskIDPosActiveDao();
			DJ_TaskIDPosActive activityEntity = new DJ_TaskIDPosActive();
			activityEntity.setIDPosEnd_TM(endTM);
			activityEntity.setTimeCount_NR(DateTimeHelper.getDiffSeconds(
					DateTimeHelper.StringToDate(idpos.getLastArrivedTime_DT()),
					DateTimeHelper.StringToDate(endTM)));
			activityEntity.setIDPos_ID(idpos.getIDPos_ID());
			activityEntity.setIDPosStart_TM(idpos.getLastArrivedTime_DT());
			djtaskIDPosActiveDao.UpdateDJIDPosActive(activityEntity);
			
			/* 更新计划库历史表  */
			/*djtaskIDPosHisDao = djdaoSession.getDJ_TaskIDPosHisDao();
			DJ_TaskIDPosHis entity = new DJ_TaskIDPosHis();
			entity.setIDPosEnd_TM(endTM);
			entity.setTimeCount_NR(DateTimeHelper.getDiffSeconds(
					DateTimeHelper.StringToDate(idpos.getLastArrivedTime_DT()),
					DateTimeHelper.StringToDate(endTM)));
			entity.setIDPos_ID(idpos.getIDPos_ID());
			entity.setIDPosStart_TM(idpos.getLastArrivedTime_DT());
			djtaskIDPosHisDao.UpdateDJIDPosHis(entity);*/
			
			/* 更新历史库到位表*/
			xjtaskIDPosHisDao = xjHisDBSession.getXJ_TaskIDPosHisDao();
			XJ_TaskIDPosHis hisEntity = new XJ_TaskIDPosHis();
			hisEntity.setIDPosEnd_TM(endTM);
			hisEntity.setTimeCount_NR(DateTimeHelper.getDiffSeconds(
					DateTimeHelper.StringToDate(idpos.getLastArrivedTime_DT()),
					DateTimeHelper.StringToDate(endTM)));
			hisEntity.setIDPos_ID(idpos.getIDPos_ID());
			hisEntity.setIDPosStart_TM(idpos.getLastArrivedTime_DT());
			hisEntity.setLine_ID(lineID);
			xjtaskIDPosHisDao.UpdateDJIDPosHis(hisEntity);
			
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 获取到位钮扣信息
	 * 
	 * @param
	 * @return
	 */
	public void unAbsenceIDGet(Context context) {
		try {
			AppContext.calculateCycleService.calculateCycleNow(true);
			List<DJ_IDPos> reIDlist = new ArrayList<DJ_IDPos>();
			List<CycleByIDPos> cycleByIDPoslist = new ArrayList<CycleByIDPos>();
			List<CycleByIDPos> templist = new ArrayList<CycleByIDPos>();
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dj_TaskIDPosHisDao = djdaoSession.getDJ_TaskIDPosHisDao();
			cycleByIDPosDao = djdaoSession.getCycleByIDPosDao();
			cycleByIDPoslist = cycleByIDPosDao.loadAll();
			List<DJ_TaskIDPosHis> idPosHislist = dj_TaskIDPosHisDao.loadAll();
			for (int i = 0; i < AppContext.DJIDPosBuffer.size(); i++) {
				templist.clear();
				for (int j = 0; j < cycleByIDPoslist.size(); j++) {
					if (AppContext.DJIDPosBuffer.get(i).getIDPos_ID()
							.equals(cycleByIDPoslist.get(j).getIDPos_ID())) {
						templist.add(cycleByIDPoslist.get(j));
					}
				}
				int uncount = 0;
				int count = 0;
				for (CycleByIDPos pos : templist) {
					for (Cycle cyc : AppContext.DJCycleBuffer) {
						if (cyc.isActiveCyc()
								&& pos.getCycle_ID().equals(
										cyc.getDJCycle().getCycle_ID())) {
							if (AppContext.DJIDPosBuffer.get(i)
									.getLastArrivedTime_DT() == null)
								uncount++;
							else if (AppContext.DJIDPosBuffer.get(i)
									.getLastArrivedTime_DT() != null
									|| DateTimeHelper.StringToDate(
											AppContext.DJIDPosBuffer.get(i)
													.getLastArrivedTime_DT())
											.compareTo(cyc.getTaskBegin()) < 0
									|| DateTimeHelper.StringToDate(
											AppContext.DJIDPosBuffer.get(i)
													.getLastArrivedTime_DT())
											.compareTo(cyc.getTaskEnd()) > 0) {
								uncount++;
							} else if (AppContext.DJIDPosBuffer.get(i)
									.getLastArrivedTime_DT() != null
									|| DateTimeHelper.StringToDate(
											AppContext.DJIDPosBuffer.get(i)
													.getLastArrivedTime_DT())
											.compareTo(cyc.getTaskBegin()) > 0
									|| DateTimeHelper.StringToDate(
											AppContext.DJIDPosBuffer.get(i)
													.getLastArrivedTime_DT())
											.compareTo(cyc.getTaskEnd()) < 0) {
								count++;
							}
						}
					}
				}
				if (uncount == templist.size()) {
					unabHashMap.put(templist.get(0).getIDPos_ID(), templist
							.get(0).getCycle_ID());
				}
				if (count == templist.size()) {
					abHashMap.put(templist.get(0).getIDPos_ID(), templist
							.get(0).getCycle_ID());
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getAbsenceMap() {
		return abHashMap;
	}

	public HashMap<String, String> getunAbsenceMap() {
		return unabHashMap;
	}
}
