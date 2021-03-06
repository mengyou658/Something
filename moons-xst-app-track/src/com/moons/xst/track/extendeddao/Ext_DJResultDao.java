package com.moons.xst.track.extendeddao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.InitDJsqlite;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.DJ_ResultHis;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.DJ_TaskIDPosHis;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.MObject_StateHis;
import com.moons.xst.track.bean.XJ_MobjectStateHis;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.dao.DJ_PhotoByResultDao;
import com.moons.xst.track.dao.DJ_ResultActiveDao;
import com.moons.xst.track.dao.DJ_ResultHisDao;
import com.moons.xst.track.dao.DJ_TaskIDPosActiveDao;
import com.moons.xst.track.dao.DJ_TaskIDPosHisDao;
import com.moons.xst.track.dao.MObject_StateDao;
import com.moons.xst.track.dao.MObject_StateHisDao;
import com.moons.xst.track.dao.XJ_MObjectStateHisDao;
import com.moons.xst.track.dao.XJ_ResultHisDao;
import com.moons.xst.track.dao.XJ_TaskIDPosHisDao;

/**
 * 扩展的点检结果Dao(DJTaskID、DJResult)
 * 
 * 此类是对原生Dao的封装和扩展，以保证原生Dao不轻易被外部修改
 * 
 * 原生Dao是自动生成，为了代码的易维护性，所以在此进行一次封装
 * 
 * @author LKZ
 */
public class Ext_DJResultDao {

	// private static Ext_DJResultDao instance = null;
	//
	// public static Ext_DJResultDao getExt_DJResultDao(Context context) {
	//
	// synchronized (Ext_DJResultDao.class) {
	// if (instance == null) {
	// instance = new Ext_DJResultDao(context);
	// }
	// }
	// return instance;
	// }

	public Ext_DJResultDao(Context context) {
		// TODO 自动生成的构造函数存根
		djResultDAOSession = ((AppContext) context.getApplicationContext())
				.getResultSession(AppContext.GetCurrLineID());
		djdaoSession = ((AppContext) context.getApplicationContext())
				.getDJSession(AppContext.GetCurrLineID());
		xjHisDataBaseSession = ((AppContext) context.getApplicationContext())
				.getXJHisDataBaseSession();
	}

	InitDJsqlite init = new InitDJsqlite();
	private DJResultDAOSession djResultDAOSession;
	private DJDAOSession djdaoSession;
	private XJHisDataBaseSession xjHisDataBaseSession;

	/**
	 * 获取原生Dao对象（ResultActiveDao）
	 * 
	 * @param context
	 * @return
	 */
	public DJ_ResultActiveDao djResultActiveDao() {
		return djResultDAOSession.getDJ_ResultActiveDao();
	}

	/**
	 * 获取原生Dao对象(TaskIDPosActiveDao)
	 * 
	 * @param context
	 * @return
	 */
	public DJ_TaskIDPosActiveDao djIDTaskActiveDao() {
		return djResultDAOSession.getDJ_TaskIDPosActiveDao();
	}

	/**
	 * 获取原生Dao对象(DJPhotoByResultDao)
	 * 
	 * @param context
	 * @return
	 */
	public DJ_PhotoByResultDao djPhotoByResultDao() {
		return djResultDAOSession.getDJ_PhotoByResultDao();
	}

	/**
	 * 获取原生Dao对象(MObjectStateDao)
	 * 
	 * @param context
	 * @return
	 */
	public MObject_StateDao mobjectStateDao() {
		return djResultDAOSession.getMObject_StateDao();
	}

	public DJ_TaskIDPosHisDao dj_TaskIDPosHisDao() {
		return djdaoSession.getDJ_TaskIDPosHisDao();
	}

	public DJ_ResultHisDao dj_ResultHisDao() {
		return djdaoSession.getDJ_ResultHisDao();
	}

	public XJ_ResultHisDao xj_ResultHisDao() {
		return xjHisDataBaseSession.getXJ_ResultHisDao();
	}

	public XJ_TaskIDPosHisDao xj_TaskIDPosHisDao() {
		return xjHisDataBaseSession.getXJ_TaskIDPosHisDao();
	}

	public XJ_MObjectStateHisDao xj_MObjectStateHisDao() {
		return xjHisDataBaseSession.getXJ_MObjectStateHisDao();
	}

	public MObject_StateHisDao mObject_StateHisDao() {
		return djdaoSession.getMObject_StateHisDao();
	}

	/**
	 * 插入一条任务的结果(不存在则添加，存在则更新)
	 * 
	 * @param entity
	 * @return
	 */
	public long InsertDJResult(DJ_ResultActive entity,
			Hashtable<Integer, ArrayList<DJ_PhotoByResult>> djFileList,
			XJ_ResultHis xj_ResultHis, DJPlan mDjPlan) {
		long row = 0;
		try {
			// 开启事务
//			AppContext.DJResultdb.beginTransaction();
//			AppContext.xjHisDataDB.beginTransaction();

			if (AppContext.DJSpecCaseFlag == 0) {
				List<DJ_ResultActive> hisRes = new ArrayList<DJ_ResultActive>();
				List<XJ_ResultHis> xjhisRes = new ArrayList<XJ_ResultHis>();
				// 巡点检
				if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
						.getLineType()) {
					if (mDjPlan != null) {
						hisRes = loadOneDJResult(mDjPlan.getDJPlan()
								.getDJ_Plan_ID(),
								DateTimeHelper.DateToString(mDjPlan.GetCycle()
										.getTaskBegin()),
								DateTimeHelper.DateToString(mDjPlan.GetCycle()
										.getTaskEnd()));
						xjhisRes = loadOneXJResult(mDjPlan.getDJPlan()
								.getDJ_Plan_ID(),
								DateTimeHelper.DateToString(mDjPlan.GetCycle()
										.getTaskBegin()),
								DateTimeHelper.DateToString(mDjPlan.GetCycle()
										.getTaskEnd()));
					}
				}
				// 点检排程
				else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC
							.getLineType()) {
					if (mDjPlan != null) {
						hisRes = loadOneDJResult(mDjPlan.getDJPlan()
									.getDJ_Plan_ID(),
									 mDjPlan.getDJPlan().getDisStart_TM(),
									 mDjPlan.getDJPlan().getDisEnd_TM());
						xjhisRes = loadOneXJResult(mDjPlan.getDJPlan()
								.getDJ_Plan_ID(),
								mDjPlan.getDJPlan().getDisStart_TM(),
								mDjPlan.getDJPlan().getDisEnd_TM());
					}
				}
				// 条件巡检
				else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
							.getLineType()) {
					if (mDjPlan != null) {
						hisRes = loadOneDJResultForCaseXJ(mDjPlan.getDJPlan()
								.getDJ_Plan_ID(), AppConst.PlanTimeIDStr);
						xjhisRes = loadOneXJResultHisForCaseXJ(mDjPlan
								.getDJPlan().getDJ_Plan_ID(),
								AppConst.PlanTimeIDStr);
					}
				}

				if (hisRes != null && hisRes.size() > 0) {
					row = 1;
					if (AppContext.getCurrLineType() == AppConst.LineType.XDJ
							.getLineType()) {
						// 更新点检结果
						djResultActiveDao().updateDJResult(entity, mDjPlan);
						// 更新结果历史结果
						if (xjhisRes != null && xjhisRes.size() > 0) {
							xj_ResultHisDao().updateDJResultHis(xj_ResultHis,
									mDjPlan);
						}
						// 删除结果对应的附件
						String tempTimeString = DateTimeHelper.TransDateTime(
								hisRes.get(0).getCompleteTime_DT(),
								"yyyyMMddHHmmss");
						djPhotoByResultDao().deleteDJResultFiles(
								entity.getDJ_Plan_ID(), tempTimeString, "");
					} else if (AppContext.getCurrLineType() == AppConst.LineType.DJPC
							.getLineType()) {
						// 更新点检结果
						djResultActiveDao().updateDJResultForDJPC(entity, mDjPlan);
						// 更新结果历史结果
						if (xjhisRes != null && xjhisRes.size() > 0) {
							xj_ResultHisDao().updateDJResultHisForDJPC(xj_ResultHis,
									mDjPlan);
						}
						// 删除结果对应的附件
						String tempTimeString = DateTimeHelper.TransDateTime(
								hisRes.get(0).getCompleteTime_DT(),
								"yyyyMMddHHmmss");
						djPhotoByResultDao().deleteDJResultFiles(
								entity.getDJ_Plan_ID(), tempTimeString, "");
					} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ
							.getLineType()) {
						// 更新点检结果
						djResultActiveDao().updateDJResultForCaseXJ(entity,
								mDjPlan, AppConst.PlanTimeIDStr);
						// 更新结果历史结果
						if (xjhisRes != null && xjhisRes.size() > 0) {
							xj_ResultHisDao().updateDJResultHisForCaseXJ(
									xj_ResultHis, mDjPlan,
									AppConst.PlanTimeIDStr);
						}
						// 删除结果对应的附件
						String tempTimeString = DateTimeHelper.TransDateTime(
								hisRes.get(0).getCompleteTime_DT(),
								"yyyyMMddHHmmss");
						djPhotoByResultDao().deleteDJResultFiles(
								entity.getDJ_Plan_ID(), tempTimeString, "");
					}
				} else {
					// 插入点检结果
					row = djResultActiveDao().insert(entity);
					// 插入历史结果
					if (row > 0) {
						row = xj_ResultHisDao().insert(xj_ResultHis);// 结果库存放历史数据
					}
				}
				if (djFileList != null && djFileList.size() > 0) {
					if (djFileList.containsKey(0)) {
						ArrayList<DJ_PhotoByResult> _tempList = djFileList
								.get(0);
						for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
							dj_PhotoByResult
									.setPartition_ID(DateTimeHelper
											.DateToString(DateTimeHelper
													.StringToDate(entity
															.getQuery_DT()),
													"yyyyMMdd")
											+ DateTimeHelper.DateToString(
													DateTimeHelper
															.StringToDate(entity
																	.getCompleteTime_DT()),
													"HHmmss"));
							djPhotoByResultDao().insert(dj_PhotoByResult);
						}
					}
					if (djFileList.containsKey(1)) {
						ArrayList<DJ_PhotoByResult> _tempList = djFileList
								.get(1);
						for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
							dj_PhotoByResult
									.setPartition_ID(DateTimeHelper
											.DateToString(DateTimeHelper
													.StringToDate(entity
															.getQuery_DT()),
													"yyyyMMdd")
											+ DateTimeHelper.DateToString(
													DateTimeHelper
															.StringToDate(entity
																	.getCompleteTime_DT()),
													"HHmmss"));
							djPhotoByResultDao().insert(dj_PhotoByResult);
						}
					}
					if (djFileList.containsKey(2)) {
						ArrayList<DJ_PhotoByResult> _tempList = djFileList
								.get(2);
						for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
							dj_PhotoByResult
									.setPartition_ID(DateTimeHelper
											.DateToString(DateTimeHelper
													.StringToDate(entity
															.getQuery_DT()),
													"yyyyMMdd")
											+ DateTimeHelper.DateToString(
													DateTimeHelper
															.StringToDate(entity
																	.getCompleteTime_DT()),
													"HHmmss"));
							djPhotoByResultDao().insert(dj_PhotoByResult);
						}
					}
				}
			} else { // 特巡只插入新记录，不做修改
				row = djResultActiveDao().insert(entity);
				// 插入历史结果
				if (row > 0) {
					row = xj_ResultHisDao().insert(xj_ResultHis);// 结果库存放历史数据
					if (djFileList != null && djFileList.size() > 0) {
						if (djFileList.containsKey(0)) {
							ArrayList<DJ_PhotoByResult> _tempList = djFileList
									.get(0);
							for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
								dj_PhotoByResult
										.setPartition_ID(DateTimeHelper.DateToString(
												DateTimeHelper
														.StringToDate(entity
																.getQuery_DT()),
												"yyyyMMdd")
												+ DateTimeHelper.DateToString(
														DateTimeHelper
																.StringToDate(entity
																		.getCompleteTime_DT()),
														"HHmmss"));
								djPhotoByResultDao().insert(dj_PhotoByResult);
							}
						}
						if (djFileList.containsKey(1)) {
							ArrayList<DJ_PhotoByResult> _tempList = djFileList
									.get(1);
							for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
								dj_PhotoByResult
										.setPartition_ID(DateTimeHelper.DateToString(
												DateTimeHelper
														.StringToDate(entity
																.getQuery_DT()),
												"yyyyMMdd")
												+ DateTimeHelper.DateToString(
														DateTimeHelper
																.StringToDate(entity
																		.getCompleteTime_DT()),
														"HHmmss"));
								djPhotoByResultDao().insert(dj_PhotoByResult);
							}
						}
						if (djFileList.containsKey(2)) {
							ArrayList<DJ_PhotoByResult> _tempList = djFileList
									.get(2);
							for (DJ_PhotoByResult dj_PhotoByResult : _tempList) {
								dj_PhotoByResult
										.setPartition_ID(DateTimeHelper.DateToString(
												DateTimeHelper
														.StringToDate(entity
																.getQuery_DT()),
												"yyyyMMdd")
												+ DateTimeHelper.DateToString(
														DateTimeHelper
																.StringToDate(entity
																		.getCompleteTime_DT()),
														"HHmmss"));
								djPhotoByResultDao().insert(dj_PhotoByResult);
							}
						}
					}
				}
			}
			// 设置事务标志为成功，当结束事务时就会提交事务
//			AppContext.DJResultdb.setTransactionSuccessful();
//			AppContext.xjHisDataDB.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("aaa" + e);
			UIHelper.ToastMessage(AppContext.baseContext, e + "");
			row = 0;
		}
		// 结束事务
		/*if (AppContext.DJResultdb != null) {
			AppContext.DJResultdb.endTransaction();
		}
		if (AppContext.xjHisDataDB != null) {
			AppContext.xjHisDataDB.endTransaction();
		}*/
		return row;
	}

	/**
	 * 插入一条钮扣的到位结果
	 * 
	 * @param entity
	 * @return
	 */
	public long InsertDJTaskIDResult(DJ_TaskIDPosActive entity,
			XJ_TaskIDPosHis hisEntity) {
		// 插入到当前结果库
		long row = djIDTaskActiveDao().insert(entity);
		// 插入到计划历史库
		if (row > 0) {
			// 插入历史库到位表
			row = xj_TaskIDPosHisDao().insert(hisEntity);
		}
		return row;
	}

	/**
	 * 插入一条启停状态结果
	 * 
	 * @param entity
	 * @return
	 */
	public long InsertMObjectStateResult(MObject_State entity,
			String pointDesc_TX) {
		long row = mobjectStateDao().insert(entity);
		if (row > 0) {
			XJ_MobjectStateHis xjStateHis = new XJ_MobjectStateHis();
			xjStateHis.setLine_ID(String.valueOf(AppContext.GetCurrLineID()));
			xjStateHis
					.setAppUser_CD(StringUtils.isEmpty(entity.getAppUser_CD()) ? ""
							: entity.getAppUser_CD());
			xjStateHis.setCompleteTime_DT(StringUtils.isEmpty(entity
					.getCompleteTime_DT()) ? "" : entity.getCompleteTime_DT());
			xjStateHis.setControlPointDesc_TX(pointDesc_TX);
			xjStateHis.setDataState_YN(StringUtils.isEmpty(entity
					.getDataState_YN()) ? "" : entity.getDataState_YN());
			xjStateHis.setMObjectState_CD(StringUtils.isEmpty(entity
					.getMObjectState_CD()) ? "" : entity.getMObjectState_CD());
			xjStateHis.setMObjectStateName_TX(StringUtils.isEmpty(entity
					.getMObjectStateName_TX()) ? "" : entity
					.getMObjectStateName_TX());
			xjStateHis.setStartAndEndPoint_ID(StringUtils.isEmpty(entity
					.getStartAndEndPoint_ID()) ? "" : entity
					.getStartAndEndPoint_ID());
			xjStateHis.setTransInfo_TX(StringUtils.isEmpty(entity
					.getTransInfo_TX()) ? "" : entity.getTransInfo_TX());
			row = xj_MObjectStateHisDao().insert(xjStateHis);
		}
		return row;
	}

	/**
	 * 加载一组点检结果数据
	 * 
	 * @param loadcount
	 * @return
	 */
	public List<DJ_ResultActive> loadDJResultforUploadJIT(int loadcount) {
		return null;
	}

	/**
	 * 根据条件加载点检结果数据
	 * 
	 * @param loadcount
	 * @return
	 */
	public List<DJ_ResultActive> loadOneDJResult(String planID,
			String startTime, String endTime) {
		return djResultActiveDao().loadOneDJResult(planID, startTime, endTime);
	}

	public List<DJ_ResultActive> loadOneDJResultForCaseXJ(String planID,
			String conditionStr) {
		return djResultActiveDao().loadOneDJResultForCaseXJ(planID,
				conditionStr);
	}

	/**
	 * 根据条件加载点检历史结果数据
	 * 
	 * @param loadcount
	 * @return
	 */
	public List<XJ_ResultHis> loadOneXJResult(String planID, String startTime,
			String endTime) {
		return xj_ResultHisDao().loadOneDJResultHis(planID, startTime, endTime);
	}

	public List<XJ_ResultHis> loadOneXJResultHisForCaseXJ(String planID,
			String conditionStr) {
		return xj_ResultHisDao().loadOneDJResultHisForCaseXJ(planID,
				conditionStr);
	}

	/**
	 * 根据条件加载点检结果历史数据
	 * 
	 * @param planID
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<DJ_ResultHis> loadOneDJResultHis(String planID,
			String startTime, String endTime) {
		return dj_ResultHisDao().loadOneDJResultHis(planID, startTime, endTime);
	}

	public List<DJ_ResultHis> loadOneDJResultHisForCaseXJ(String planID,
			String conditionStr) {
		return dj_ResultHisDao().loadOneDJResultHisForCaseXJ(planID,
				conditionStr);
	}

	/**
	 * 加载特定一条结果对应的附件信息（照片、录音、录像）
	 * 
	 * @param planID
	 * @param completeTime
	 * @return
	 */
	public List<DJ_PhotoByResult> loadDJResultFile(String planID,
			String completeTime) {
		return djPhotoByResultDao().loadDJResultFiles(planID, completeTime);
	}
}
