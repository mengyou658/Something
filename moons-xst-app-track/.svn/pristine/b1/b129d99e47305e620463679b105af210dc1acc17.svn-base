package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_ResultHis;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_SpecCase;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.dao.DJ_ControlPointDao;
import com.moons.xst.track.dao.DJ_ResultHisDao;
import com.moons.xst.track.dao.Z_DJ_PlanDao;
import com.moons.xst.track.dao.Z_DJ_PlanForResultDao;
import com.moons.xst.track.dao.Z_MObjectStateDao;
import com.moons.xst.track.dao.Z_SpecCaseDao;

public class DJPlanHelper {

	private static final String TAG = "DJPlanHelper";

	private DJDAOSession djdaoSession;
	private DJResultDAOSession djResultDaoSession;

	private Z_DJ_PlanDao dlPlanDAO;
	private Z_SpecCaseDao dlSpecCaseDao;
	private DJ_ControlPointDao dlControlPointDao;
	private Z_MObjectStateDao dlMobjectMObjectStateDao;

	private DJ_ResultHisDao djResultHisDao;
	
	private Z_DJ_PlanForResultDao djplanforresDao;

	InitDJsqlite init = new InitDJsqlite();
	CycleHelper cycleHelper = new CycleHelper();

	static DJPlanHelper _intance;

	public static DJPlanHelper GetIntance() {
		if (_intance == null) {
			_intance = new DJPlanHelper();
		}
		return _intance;
	}

	/**
	 * 根据周期ID获取内存中的周期对象
	 * 
	 * @param cycleID
	 * @return
	 */
	private Cycle GetDJCycleByID(String cycleID) {

		for (Cycle _cycle : AppContext.DJCycleBuffer) {
			if (_cycle.getDJCycle().getCycle_ID().equals(cycleID)) {
				return _cycle;

			}
		}
		return null;
	}

	/**
	 * 加载钮扣下计划信息（未进行周期计算）
	 * 
	 * @param context
	 * @param current_IDPos_ID
	 */
	public void loadPlanData(Context context, String current_IDPos_ID) {
		// 加载数据
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlPlanDAO = djdaoSession.getZ_DJ_PlanDao();
			List<Z_DJ_Plan> lvDJPlanStatusData = new ArrayList<Z_DJ_Plan>();
			
			switch (AppContext.getCurrLineType()) {
			case 0:
				lvDJPlanStatusData = dlPlanDAO.loadAll(String
						.valueOf(current_IDPos_ID));
				break;
			case 1:
				lvDJPlanStatusData = dlPlanDAO.loadAll(String
						.valueOf(current_IDPos_ID));
				break;
			case 8:
				lvDJPlanStatusData = dlPlanDAO.loadAllForCASEXJ(String
						.valueOf(current_IDPos_ID));
				break;
			default:
				lvDJPlanStatusData = dlPlanDAO.loadAll(String
						.valueOf(current_IDPos_ID));
			}
			
			// 加载数据
			if (!AppContext.DJPlanBuffer.containsKey(current_IDPos_ID))
				AppContext.DJPlanBuffer.put(current_IDPos_ID,
						new ArrayList<DJPlan>());
			else {
				AppContext.DJPlanBuffer.get(current_IDPos_ID).clear();
			}
			for (Z_DJ_Plan djPlanStatus : lvDJPlanStatusData) {
				DJPlan djPlan = new DJPlan();
				djPlan.setDJPlan(djPlanStatus);
				Cycle cycle = GetDJCycleByID(djPlanStatus.getCycle_ID()
						.toString());
				djPlan.SetCycle(cycle);
				DJ_ControlPoint srpoint = DJPlanHelper.GetIntance().getSRbyID(
						context, djPlan.getDJPlan().getSRPoint_ID());
				djPlan.SetSrPoint(srpoint);
				
				if (djPlanStatus.getDataCodeGroup_ID() > 0
						&& AppContext.dataCodeBuffer.size() > 0) {
					for (Z_DataCodeGroup _temCodeGroup : AppContext.dataCodeBuffer) {
						if (_temCodeGroup.getDataCodeGroup_ID().equals(
								String.valueOf(djPlanStatus
										.getDataCodeGroup_ID()))) {
							djPlan.setDataCodeGroup(_temCodeGroup);
							break;
						}
					}
				}
				AppContext.DJPlanBuffer.get(current_IDPos_ID).add(djPlan);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 加载某路线下某钮扣下计划信息（未进行周期计算） 查询数据使用
	 * 
	 * @param context
	 * @param current_IDPos_ID
	 * @param Line_ID
	 */
	public List<DJPlan> loadPlanData(Context context, String current_IDPos_ID,
			int Line_ID) {
		// 加载数据
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( Line_ID);
			dlPlanDAO = djdaoSession.getZ_DJ_PlanDao();
			List<Z_DJ_Plan> lvDJPlanStatusData = dlPlanDAO.loadAll(String
					.valueOf(current_IDPos_ID));

			List<DJPlan> djPlans = new ArrayList<DJPlan>();
			// 加载数据
			for (Z_DJ_Plan djPlanStatus : lvDJPlanStatusData) {
				DJPlan djPlan = new DJPlan();

				djPlan.setDJPlan(djPlanStatus);
				Cycle cycle = GetDJCycleByID(djPlanStatus.getCycle_ID()
						.toString());
				djPlan.SetCycle(cycle);
				djPlans.add(djPlan);
			}
			return djPlans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加载某路线下某钮扣下某计划历史信息 查询数据使用
	 * 
	 * @param context
	 * @param Line_ID
	 * @param planid
	 */
	public List<DJ_ResultHis> loadPlanHisData(Context context, int Line_ID,
			String planid) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( Line_ID);
			djResultHisDao = djdaoSession.getDJ_ResultHisDao();
			List<DJ_ResultHis> allhislist = djResultHisDao.loadAll();
			List<DJ_ResultHis> hislist = new ArrayList<DJ_ResultHis>();
			for (DJ_ResultHis his : allhislist) {
				if (his.getDJ_Plan_ID().equals(planid)) {
					hislist.add(his);
				}
			}
			return hislist;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	

	/**
	 * 根据计划ID获取历史的结果列表（直接通过条件查库）
	 * 
	 * @param context
	 * @param Line_ID
	 * @param planid
	 * @return
	 */
	public List<DJ_ResultHis> loadPlanHisDataByPlanID(Context context,
			int Line_ID, String planid) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession( Line_ID);
			djResultHisDao = djdaoSession.getDJ_ResultHisDao();
			List<DJ_ResultHis> hislist = djResultHisDao
					.loadDJResultHisByPlanID(planid);
			return hislist;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 获取同一主控点下计划列表(强制计划不加载，已做过的计划不加载)
	 * 
	 * @param pointID
	 * @return
	 */
	public List<DJPlan> GetPlanListByControlPoint(Context context, String pointID, String idPosID) {
		ArrayList<DJPlan> planList = new ArrayList<DJPlan>();
		for (DJPlan _planInfo : AppContext.YXDJPlanByIDPosBuffer.get(idPosID)) {

			boolean planIsCompleted = true;
			if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
				planIsCompleted = _planInfo.JudgePlanIsCompleted(DateTimeHelper
						.GetDateTimeNow());
			} else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC.getLineType()) {
				planIsCompleted = _planInfo.JudgePlanIsCompletedForDJPC();
			} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				planIsCompleted = _planInfo.JudgePlanIsCompletedForCASEXJ(context);
			}
			String lkPointID = _planInfo.getDJPlan().getLKPoint_ID();
			if (lkPointID != null
					&& lkPointID.equals(pointID)
					&& (!StringUtils.isEmpty(_planInfo.getDJPlan().getMustCheck_YN()))
					&& (_planInfo.getDJPlan().getMustCheck_YN().equals("0"))
					&& (!planIsCompleted)) {
				if (_planInfo.JudgePlanIsSrControl()) {
					if (_planInfo.JudgePlanBySrPoint())
						planList.add(_planInfo);
				} else {
					planList.add(_planInfo);
				}
			}
		}
		return planList;
	}

	/**
	 * 主控启停信息
	 * 
	 * @param context
	 * @return
	 */
	public boolean loadControlPoint(Context context) {
		try {
			//
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlControlPointDao = djdaoSession.getDJ_ControlPointDao();
			AppContext.SRBuffer.clear();
			AppContext.SRBuffer = Collections
					.synchronizedList(new ArrayList<DJ_ControlPoint>());
			AppContext.LKBuffer.clear();
			AppContext.LKBuffer = Collections
					.synchronizedList(new ArrayList<DJ_ControlPoint>());
			List<DJ_ControlPoint> _temControlPoints = new ArrayList<DJ_ControlPoint>();
			if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) //条件巡检单独取控制点
				_temControlPoints = dlControlPointDao.loadAllCP();
			else
				_temControlPoints = dlControlPointDao.loadAll();
			int index = -1;
			if (_temControlPoints != null && _temControlPoints.size() > 0) {
				Iterator<DJ_ControlPoint> iter = _temControlPoints.iterator();
				while (iter.hasNext()) {
					index++;
					DJ_ControlPoint _temControlPoint = iter.next();
					if (_temControlPoint.getSRPlan_YN()) {
						AppContext.SRBuffer.add(_temControlPoint);
					}
					if (_temControlPoint.getLKPlan_YN()) {
						AppContext.LKBuffer.add(_temControlPoint);
					}
				}
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	/**
	 * 根据ID获取启停对象
	 * 
	 * @param context
	 * @param ID
	 * @return
	 */
	public DJ_ControlPoint getSRbyID(Context context, String ID) {
		try {
			//
			if (AppContext.SRBuffer != null && AppContext.SRBuffer.size() > 0) {
				for (DJ_ControlPoint cp : AppContext.SRBuffer) {
					if (cp.getControlPoint_ID().equals(ID)) {
						return cp;
					}
				}
			}
			/*int index = -1;
			if (AppContext.SRBuffer != null && AppContext.SRBuffer.size() > 0) {
				Iterator<DJ_ControlPoint> iter = AppContext.SRBuffer.iterator();
				while (iter.hasNext()) {
					index++;
					if (iter.next().getControlPoint_ID().equals(ID)) {
						return AppContext.SRBuffer.get(index);
					}
				}
			}*/
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据ID获取主控对象
	 * 
	 * @param context
	 * @param ID
	 * @return
	 */
	public DJ_ControlPoint getLKbyID(Context context, String ID) {
		try {
			//
			int index = -1;
			if (AppContext.LKBuffer != null && AppContext.LKBuffer.size() > 0) {
				Iterator<DJ_ControlPoint> iter = AppContext.LKBuffer.iterator();
				while (iter.hasNext()) {
					index++;
					if (iter.next().getControlPoint_ID().equals(ID)) {
						return AppContext.LKBuffer.get(index);
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取钮扣下所有启停点信息
	 * 
	 * @param context
	 * @param ID
	 * @return
	 */
	public List<DJ_ControlPoint> getSRbyidPosID(Context context, String ID) {
		try {
			List<DJ_ControlPoint> temSRlist = new ArrayList<DJ_ControlPoint>();
			ArrayList<DJPlan> temPlans = AppContext.DJPlanBuffer.get(ID);
			if (temPlans == null || temPlans.size() <= 0) {
				return null;
			}
			for (DJPlan djPlan : AppContext.DJPlanBuffer.get(ID)) {
				String _cpId = djPlan.getDJPlan().getSRPoint_ID();
				if (AppContext.SRBuffer != null
						&& AppContext.SRBuffer.size() > 0) {
					Iterator<DJ_ControlPoint> iter = AppContext.SRBuffer
							.iterator();
					while (iter.hasNext()) {
						DJ_ControlPoint _temControlPoint = iter.next();
						if (_temControlPoint.getControlPoint_ID().equals(_cpId)) {
							if (!temSRlist.contains(_temControlPoint)) {
								_temControlPoint.setSRPlanCount(1);
								temSRlist.add(_temControlPoint);
							} else {
								_temControlPoint
										.setSRPlanCount(_temControlPoint
												.getSRPlanCount() + 1);
							}
						}
					}
				}
			}
			return temSRlist;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取钮扣下所有主控点信息
	 * 
	 * @param context
	 * @param ID
	 * @return
	 */
	public List<DJ_ControlPoint> getLKbyidPosID(Context context, String ID) {
		try {
			List<DJ_ControlPoint> temLKlist = new ArrayList<DJ_ControlPoint>();
			ArrayList<DJPlan> temPlans = AppContext.DJPlanBuffer.get(ID);
			if (temPlans == null || temPlans.size() <= 0) {
				return null;
			}
			for (DJPlan djPlan : AppContext.DJPlanBuffer.get(ID)) {
				String _cpId = djPlan.getDJPlan().getLKPoint_ID();
				if (AppContext.LKBuffer != null
						&& AppContext.LKBuffer.size() > 0) {
					Iterator<DJ_ControlPoint> iter = AppContext.LKBuffer
							.iterator();
					while (iter.hasNext()) {
						DJ_ControlPoint _temControlPoint = iter.next();
						if (_temControlPoint.getControlPoint_ID().equals(_cpId)) {
							if (!temLKlist.contains(_temControlPoint))
								temLKlist.add(_temControlPoint);
						}
					}
				}
			}
			return temLKlist;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 加载设备状态（启停状态列表）
	 * 
	 * @param context
	 */
	public boolean loadMobjectState(Context context) {
		try {
			//
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlMobjectMObjectStateDao = djdaoSession.getZ_MObjectStateDao();
			AppContext.SRStateBuffer.clear();
			AppContext.SRStateBuffer = dlMobjectMObjectStateDao.load();
		} catch (Exception e) {
			return true;
		}
		return false;
	}

	/**
	 * 加载特巡条件
	 * 
	 * @param context
	 */
	public void loadDJSpecCase(Context context, List<DJLine> lineList) {
		// 加载数据
		AppContext.DJSpecCaseBuffer.clear();
		if (lineList != null && lineList.size() > 0) {
			for (DJLine djLine : lineList) {
				try {
					//
					djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
							djLine.getLineID());
					dlSpecCaseDao = djdaoSession.getZ_SpecCaseDao();
					List<Z_SpecCase> specCaseData = dlSpecCaseDao.loadAll();
					// 加载数据
					String lineIDString = String.valueOf(djLine.getLineID());
					if (!AppContext.DJSpecCaseBuffer.containsKey(lineIDString))
						AppContext.DJSpecCaseBuffer.put(lineIDString,
								new ArrayList<Z_SpecCase>());
					else {
						AppContext.DJPlanBuffer.get(lineIDString).clear();
					}
					if (specCaseData != null && specCaseData.size() > 0) {
						for (Z_SpecCase spceCaseItem : specCaseData) {
							Z_SpecCase _specCase = new Z_SpecCase();
							_specCase.setSpecCase_ID(spceCaseItem
									.getSpecCase_ID());
							_specCase.setName_TX(spceCaseItem.getName_TX());
							AppContext.DJSpecCaseBuffer.get(lineIDString).add(
									spceCaseItem);
						}
						djLine.setSpecCaseYN(true);
					} else {
						djLine.setSpecCaseYN(false);
					}
				} catch (Exception e) {
				}
			}

		}
	}
	
	/**
	 * 加载特巡条件(单条路线)
	 * @param context
	 * @param lineID
	 */
	public void loadDJSpecCase(Context context, DJLine djLine, List<DJLine> lineList) {
		try {
			// 加载数据
			AppContext.DJSpecCaseBuffer.clear();
			if (djLine != null && lineList.contains(djLine)) {
				djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
						djLine.getLineID());
				dlSpecCaseDao = djdaoSession.getZ_SpecCaseDao();
				List<Z_SpecCase> specCaseData = dlSpecCaseDao.loadAll();
				AppContext.DJSpecCaseBuffer.put(String.valueOf(djLine.getLineID()),
						new ArrayList<Z_SpecCase>());
				
				if (specCaseData != null && specCaseData.size() > 0) {
					for (Z_SpecCase spceCaseItem : specCaseData) {
						Z_SpecCase _specCase = new Z_SpecCase();
						_specCase.setSpecCase_ID(spceCaseItem
								.getSpecCase_ID());
						_specCase.setName_TX(spceCaseItem.getName_TX());
						AppContext.DJSpecCaseBuffer.get(String.valueOf(djLine.getLineID())).add(
								spceCaseItem);
					}
					djLine.setSpecCaseYN(true);
					lineList.get(lineList.indexOf(djLine)).setSpecCaseYN(true);
				} else {
					djLine.setSpecCaseYN(false);
					lineList.get(lineList.indexOf(djLine)).setSpecCaseYN(false);
				}
			}
		} catch (Exception e) {
			
		}
	}

	/**
	 * 更新计划信息
	 * 
	 * @param plan
	 * @return
	 */
	public void updateDJPlan(Context context, Z_DJ_Plan plan) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlPlanDAO = djdaoSession.getZ_DJ_PlanDao();
			
			djResultDaoSession = ((AppContext) context.getApplicationContext()).getResultSession(
					AppContext.GetCurrLineID());
			djplanforresDao = djResultDaoSession.getZ_DJ_PlanForResultDao();
			// 条件巡检
			if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
				dlPlanDAO.UpdateDJPlanForCASEXJ(plan);
				djplanforresDao.UpdateDJPlanForCASEXJ(plan);
			}
			else
				dlPlanDAO.UpdateDJPlan(plan);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 更新启停信息
	 * 
	 * @param plan
	 * @return
	 */
	public void updateSR(Context context, DJ_ControlPoint cp) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlControlPointDao = djdaoSession.getDJ_ControlPointDao();
			dlControlPointDao.updateSR(cp);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}
	
	public List<Z_DJ_Plan> getDJPlanIsCompleteForCASEXJ(Context context, Z_DJ_Plan plan) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			dlPlanDAO = djdaoSession.getZ_DJ_PlanDao();
			List<Z_DJ_Plan> res = new ArrayList<Z_DJ_Plan>();
			res = dlPlanDAO.getDJPlanIsCompleteForCASEXJ(plan.getDJ_Plan_ID(),
					plan.getLastComplete_DT(),
					AppConst.PlanTimeIDStr);
			return res;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
}
