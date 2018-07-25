package com.moons.xst.buss;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import Decoder.BASE64Encoder;
import android.content.Context;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_PhotoByResult;
import com.moons.xst.track.bean.DJ_ResultActive;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.MObject_State;
import com.moons.xst.track.bean.PlanInfoCaseXJJIT;
import com.moons.xst.track.bean.SRLineTreeLastResult;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DJ_PhotoByResultDao;
import com.moons.xst.track.dao.DJ_ResultActiveDao;
import com.moons.xst.track.dao.DJ_TaskIDPosActiveDao;
import com.moons.xst.track.dao.MObject_StateDao;
import com.moons.xst.track.dao.SRLineTreeLastResultDao;
import com.moons.xst.track.dao.Z_ConditionDao;
import com.moons.xst.track.dao.Z_DJ_PlanDao;

/**
 * 条件巡检实时上传
 * 
 * @author 吴俊宜
 * 
 */
public class CaseXJResultHelper {
	private static final String TAG = "CaseXJResultHelper";

	private DJDAOSession djdaoSession;
	private DJResultDAOSession djresultdaoSession;

	private Z_DJ_PlanDao djplanDao;
	private Z_ConditionDao conditionDao;
	private SRLineTreeLastResultDao srlinetreelastresultDao;

	private DJ_ResultActiveDao djresultActiveDao;
	private DJ_PhotoByResultDao djphotobyresultDao;
	private DJ_TaskIDPosActiveDao djtaskIDPosActiveDao;
	private MObject_StateDao mobjectstateDao;

	List<Z_DJ_Plan> UpLoadPlanList;
	List<Z_Condition> UpLoadConditionList;
	List<SRLineTreeLastResult> UpLoadSRLineTreeLastResultList;

	InitDJsqlite init = new InitDJsqlite();

	private List<DJ_PhotoByResult> filesList;
	private List<DJ_ResultActive> djResultList;
	private String imagePath;
	private String audioPath;

	private String use_line_id;
	private String con_line_id;
	private String point_id;
	private String unit_id;
	private String content_id;
	private String times_id;

	static String result_route_id = "0";
	static String result_rfid_id = "0";
	static String idposKeyStr = "";

	List<String> m_DJResultHash = new ArrayList<String>();
	Hashtable<String, String> m_MobjectStateHash = new Hashtable<String, String>();

	static CaseXJResultHelper _intance;

	public static CaseXJResultHelper GetIntance() {
		if (_intance == null) {
			_intance = new CaseXJResultHelper();
		}
		return _intance;
	}

	/**
	 * 实时上传条件巡检结果，到位及结果打包一起上传每次 上传一条到位结果及该到位结果下的所有点检结果数据
	 * 
	 * @param context
	 * @param djLine
	 */
	public void uploadCaseXJResultTogetherJIT(Context context, DJLine djLine) {
		BASE64Encoder encode = new BASE64Encoder();
		try {
			if (!FileUtils.checkFileExists(AppConst.CurrentResultPath(djLine
					.getLineID()) + AppConst.ResultDBName(djLine.getLineID())))
				return;

			djresultdaoSession = ((AppContext) context.getApplicationContext())
					.getUploadingSession(AppContext.GetCurrLineID());

			djdaoSession = ((AppContext) context.getApplicationContext())
					.getDJSession(AppContext.GetCurrLineID());

			djplanDao = djdaoSession.getZ_DJ_PlanDao();
			conditionDao = djdaoSession.getZ_ConditionDao();
			srlinetreelastresultDao = djdaoSession.getSRLineTreeLastResultDao();

			UpLoadPlanList = djplanDao.loadAll();
			UpLoadConditionList = conditionDao.loadAll();
			UpLoadSRLineTreeLastResultList = srlinetreelastresultDao.loadAll();

			djtaskIDPosActiveDao = djresultdaoSession
					.getDJ_TaskIDPosActiveDao();
			djresultActiveDao = djresultdaoSession.getDJ_ResultActiveDao();
			mobjectstateDao = djresultdaoSession.getMObject_StateDao();

			List<DJ_TaskIDPosActive> taskIDPosList = djtaskIDPosActiveDao
					.loadforUploadJIT(1);
			if (taskIDPosList == null || taskIDPosList.size() <= 0)
				return;

			boolean result = false;
			boolean srResult = false;
			// 如果钮扣的开始和结束时间相同，则只有到位，没有结果
			if (taskIDPosList.get(0).getIDPosStart_TM()
					.equals(taskIDPosList.get(0).getIDPosEnd_TM())) {
				if (UpLoadSRLineTreeLastResultList != null
						&& UpLoadSRLineTreeLastResultList.size() > 0) {
					for (Z_DJ_Plan plan : UpLoadPlanList) {
						for (SRLineTreeLastResult mob : UpLoadSRLineTreeLastResultList) {
							if (plan.getSRPoint_ID().equals(
									mob.getSRControlPoint_ID())) {
								plan.setLastSRResult_TM(mob
										.getLastSRResult_TM());
								String str = mob.getLastSRResult_TX().equals(
										"_______0") ? "0" : "1";
								plan.setLastSRResult_TX(str);
							}
						}
					}
				}

				String use_line_id = "";
				String idpos_id = "";
				String con_line_id = "";
				String times_id = "";
				String[] idposTransInfoStrs = taskIDPosList.get(0)
						.getTransInfo_TX().split("\\|");
				if (idposTransInfoStrs.length == 4) {
					use_line_id = idposTransInfoStrs[0].toString();
					con_line_id = idposTransInfoStrs[1].toString();
					idpos_id = idposTransInfoStrs[2].toString();
					times_id = idposTransInfoStrs[3].toString();
				}
				String nextPlanStartTime = "";
				List<Z_DJ_Plan> planDRs = new ArrayList<Z_DJ_Plan>();
				for (Z_DJ_Plan z : UpLoadPlanList) {
					if (z.getConLevel_TX().equalsIgnoreCase(con_line_id)
							&& z.getLastSRResult_TX().equals("0")) {
						planDRs.add(z);
					}
				}
				List<Z_Condition> timesDRs = new ArrayList<Z_Condition>();
				for (Z_Condition c : UpLoadConditionList) {
					if (c.getConLevel_TX().equalsIgnoreCase(
							con_line_id + "|" + times_id)
							&& c.getConType_TX().equalsIgnoreCase("TIME")) {
						timesDRs.add(c);
					}
				}
				String planStartTime = timesDRs.get(0).getConValue_TX()
						.split("~")[0].toString();
				String planEndTime = timesDRs.get(0).getConValue_TX()
						.split("~")[1].toString();
				List<Z_Condition> nexttimesDRs = new ArrayList<Z_Condition>();
				for (Z_Condition zc : UpLoadConditionList) {
					if (zc.getSort_TX().equalsIgnoreCase(
							String.valueOf(Integer.parseInt(timesDRs.get(0)
									.getSort_TX()) + 1))
							&& zc.getParentCon_id().equalsIgnoreCase(
									timesDRs.get(0).getParentCon_id())
							&& zc.getConType_TX().equalsIgnoreCase("TIME")) {
						nexttimesDRs.add(zc);
					}
				}
				if (nexttimesDRs != null && nexttimesDRs.size() > 0)
					nextPlanStartTime = nexttimesDRs.get(0).getConValue_TX()
							.split("~")[0].toString();
				List<Z_DJ_Plan> planUndoDRs = new ArrayList<Z_DJ_Plan>();
				for (Z_DJ_Plan zdp : UpLoadPlanList) {
					if (zdp.getConLevel_TX().equalsIgnoreCase(con_line_id)
							&& zdp.getLastSRResult_TX().equals("0")
							&& zdp.getPlanCycleIDsStr_TX().contains(times_id)) {
						planUndoDRs.add(zdp);
					}
				}

				PlanInfoCaseXJJIT plan = new PlanInfoCaseXJJIT();
				plan.setcontent_count(planDRs.size());
				plan.setuse_line_id(use_line_id);
				plan.setcon_line_id(con_line_id);
				plan.setplan_id(times_id);
				plan.setplanStartTime(planStartTime);
				plan.setplanEndTime(planEndTime);
				if (!StringUtils.isEmpty(nextPlanStartTime))
					plan.setnextplanStartTime(nextPlanStartTime);
				else
					plan.setnextplanStartTime("1900-01-01 00:00:00");
				plan.setuser_id(taskIDPosList.get(0).getAppUser_CD());
				plan.setstart_time(taskIDPosList.get(0).getIDPosStart_TM());
				plan.setend_time(taskIDPosList.get(0).getIDPosEnd_TM());
				plan.seterror_count(0);
				plan.setrecord_count(planDRs.size() - planUndoDRs.size());

				String resultJsonStr = CreateOneRfidJsonData(idpos_id,
						plan.getuse_line_id(), "$result_rfid_id$",
						"$result_route_id$",
						NormalDateToUnixDate(plan.getstart_time()),
						NormalDateToUnixDate(plan.getend_time()));

				String rfidResultJsonStr = resultJsonStr + "|"
						+ plan.getcon_line_id() + plan.getplan_id() + idpos_id
						+ "|" + idpos_id;

				result_route_id = "0";
				String OneLineDataJson = GetOneLineDataTogether(plan);

				String OneRfidJson = "";
				String djResDataTotal = "";

				result_rfid_id = "0";
				OneRfidJson = GetOneRfidDataTogether(rfidResultJsonStr,
						result_route_id);

				djResDataTotal = OneRfidJson + "\"ptrlPointRecContentList\":["
						+ djResDataTotal + "]},";
				djResDataTotal = djResDataTotal.substring(0,
						djResDataTotal.length() - 1);
				djResDataTotal = OneLineDataJson + "\"ptrlPointRecList\":["
						+ djResDataTotal + "]}";

				result = WebserviceFactory
						.InsertMultiDJResultJITForAndroidByCaseXJ(djResDataTotal);

				List<MObject_State> mobjectstateList = mobjectstateDao
						.loadforUploadJITCaseXJ(con_line_id, times_id);
				if (result) {
					if ((mobjectstateList != null && mobjectstateList.size() > 0)
							|| (taskIDPosList != null && taskIDPosList.size() > 0)) {
						srResult = updateMobjectState(mobjectstateList,
								taskIDPosList);
					}
				}

				if (result && srResult) {
					djtaskIDPosActiveDao.deleteUploadedData(taskIDPosList);
					mobjectstateDao.deleteUploadedData(mobjectstateList);
				}

				clearReferData();
			}
			// 到位钮扣下有点检结果
			else {
				djphotobyresultDao = djresultdaoSession
						.getDJ_PhotoByResultDao();
				List<Z_DJ_Plan> m_PlanByIDPos = djplanDao
						.getDJPlanByIDPosIDForCaseXJJIT(taskIDPosList.get(0)
								.getIDPos_ID());
				StringBuilder sb = new StringBuilder();
				for (Z_DJ_Plan plan : m_PlanByIDPos) {
					sb.append(plan.getDJ_Plan_ID());
					sb.append(",");
				}
				String djplanIDStr = sb.toString()
						.substring(0, sb.length() - 1);
				String[] len = taskIDPosList.get(0).getTransInfo_TX()
						.split("\\|");

				String time_id = "";
				if (len.length == 4) {
					time_id = len[3].toString();
				} else {
					time_id = "0";
				}

				djResultList = djresultActiveDao.loadDJResultForCaseXJJIT(
						djplanIDStr, time_id);

				// 到位结果下有点检结果数据
				if (djResultList.size() > 0) {
					if (UpLoadSRLineTreeLastResultList != null
							&& UpLoadSRLineTreeLastResultList.size() > 0) {
						for (Z_DJ_Plan plan : UpLoadPlanList) {
							for (SRLineTreeLastResult mob : UpLoadSRLineTreeLastResultList) {
								if (plan.getSRPoint_ID().equals(
										mob.getSRControlPoint_ID())) {
									plan.setLastSRResult_TM(mob
											.getLastSRResult_TM());
									String str = mob.getLastSRResult_TX()
											.equals("_______0") ? "0" : "1";
									plan.setLastSRResult_TX(str);
								}
							}
						}
					}

					List<String> MyfileList = new ArrayList<String>();
					String resultJsonData = "";
					// String resKey = "";
					PlanInfoCaseXJJIT plan = null;
					for (DJ_ResultActive resInfo : djResultList) {
						resultJsonData = "";
						String dj_Plan_ID = resInfo.getDJ_Plan_ID();
						String completeDT = DateTimeHelper.DateToString(
								DateTimeHelper.StringToDate(resInfo
										.getCompleteTime_DT()),
								"yyyyMMddHHmmss");
						filesList = djphotobyresultDao.loadDJResultFiles(
								dj_Plan_ID, completeDT);
						for (DJ_PhotoByResult dj_PhotoByResult : filesList) {
							if (dj_PhotoByResult.getLCType().equals("ZP")) {
								imagePath = AppConst
										.CurrentResultPath_Pic(djLine
												.getLineID())
										+ File.separator
										+ dj_PhotoByResult.getGUID()
										+ "_"
										+ DateTimeHelper
												.DateToString(
														DateTimeHelper
																.StringToDate(dj_PhotoByResult
																		.getPhoto_DT()),
														"yyyyMMddHHmmss")
										+ ".jpg";

								byte[] picBytes = FileUtils.getBytes(imagePath);
								if (picBytes != null) {
									String ZPEncodeToString = encode
											.encode(picBytes);
									dj_PhotoByResult
											.setFile_Bytes(ZPEncodeToString);
									MyfileList.add(imagePath);
								}
							} else if (dj_PhotoByResult.getLCType()
									.equals("LY")) {
								audioPath = AppConst
										.CurrentResultPath_Pic(djLine
												.getLineID())
										+ File.separator
										+ dj_PhotoByResult.getGUID() + ".arm";
								byte[] audioBytes = FileUtils
										.getBytes(audioPath);
								if (audioBytes != null) {
									String LYEncodeToString = encode
											.encode(audioBytes);
									dj_PhotoByResult
											.setFile_Bytes(LYEncodeToString);
									MyfileList.add(audioPath);
								}
							}
						}
						resInfo.setResultFiles(filesList);

						/*String ResFileListJsonStr = "";
						if (resInfo.getResultFiles() != null
								&& resInfo.getResultFiles().size() > 0) {
							ResFileListJsonStr = CreateOneResultFileListJsonData(resInfo
									.getResultFiles());
						}*/
						String completeStartTime;
						String completeEndTime;
						completeStartTime = resInfo.getCompleteTime_DT();
						Date tempDate = DateTimeHelper.StringToDate(resInfo
								.getCompleteTime_DT());
						completeEndTime = resInfo.getCompleteTime_DT();
						/*completeEndTime = DateTimeHelper
								.DateToString(DateTimeHelper.AddSeconds(
										tempDate, resInfo.getTime_NR()));*/
						DecondePlanTransInfo(resInfo.getTransInfo_TX());

						String Data8K64String = "";
						if (resInfo.getData8K_TX() != null) {
							Data8K64String = encode.encode(resInfo
									.getData8K_TX());
						} else {
							Data8K64String = "";
						}

						resultJsonData = CreateOneDJResultJsonData(
								content_id,
								resInfo.getDJ_Plan_ID()
										+ NormalDateToUnixDate(resInfo
												.getCompleteTime_DT()),
								"$result_rfid_id$",
								NormalDateToUnixDate(completeStartTime),
								NormalDateToUnixDate(completeEndTime),
								resInfo.getResult_TX(), resInfo.getMemo_TX(),
								resInfo.getException_YN(),
								resInfo.getAppUser_CD(),
								GetStateID(resInfo.getMObjectStateName_TX()),
								resInfo.getResultFiles(), Data8K64String);

						// resKey = String.format("{0}|{1}|{2}|{3}",
						// use_line_id, con_line_id, times_id, point_id);
						if (m_DJResultHash.size() <= 0) {
							String nextPlanStartTime = "";
							List<Z_DJ_Plan> planDRs = new ArrayList<Z_DJ_Plan>();
							for (Z_DJ_Plan z : UpLoadPlanList) {
								if (z.getConLevel_TX().equalsIgnoreCase(
										con_line_id)
										&& z.getLastSRResult_TX().equals("0")) {
									planDRs.add(z);
								}
							}
							List<Z_Condition> timesDRs = new ArrayList<Z_Condition>();
							for (Z_Condition c : UpLoadConditionList) {
								if (c.getConLevel_TX().equalsIgnoreCase(
										con_line_id + "|" + times_id)
										&& c.getConType_TX().equalsIgnoreCase(
												"TIME")) {
									timesDRs.add(c);
								}
							}
							String planStartTime = timesDRs.get(0)
									.getConValue_TX().split("~")[0].toString();
							String planEndTime = timesDRs.get(0)
									.getConValue_TX().split("~")[1].toString();
							List<Z_Condition> nexttimesDRs = new ArrayList<Z_Condition>();
							for (Z_Condition zc : UpLoadConditionList) {
								if (zc.getSort_TX().equalsIgnoreCase(
										String.valueOf(Integer
												.parseInt(timesDRs.get(0)
														.getSort_TX()) + 1))
										&& zc.getParentCon_id()
												.equalsIgnoreCase(
														timesDRs.get(0)
																.getParentCon_id())
										&& zc.getConType_TX().equalsIgnoreCase(
												"TIME")) {
									nexttimesDRs.add(zc);
								}
							}
							if (nexttimesDRs != null && nexttimesDRs.size() > 0)
								nextPlanStartTime = nexttimesDRs.get(0)
										.getConValue_TX().split("~")[0]
										.toString();
							List<Z_DJ_Plan> planUndoDRs = new ArrayList<Z_DJ_Plan>();
							for (Z_DJ_Plan zdp : UpLoadPlanList) {
								if (zdp.getConLevel_TX().equalsIgnoreCase(
										con_line_id)
										&& zdp.getLastSRResult_TX().equals("0")
										&& zdp.getPlanCycleIDsStr_TX()
												.contains(times_id)) {
									planUndoDRs.add(zdp);
								}
							}
							plan = new PlanInfoCaseXJJIT();
							plan.setcontent_count(planDRs.size());
							plan.setuse_line_id(use_line_id);
							plan.setcon_line_id(con_line_id);
							plan.setplan_id(times_id);
							plan.setplanStartTime(planStartTime);
							plan.setplanEndTime(planEndTime);
							if (!StringUtils.isEmpty(nextPlanStartTime))
								plan.setnextplanStartTime(nextPlanStartTime);
							else
								plan.setnextplanStartTime("1900-01-01 00:00:00");
							plan.setuser_id(resInfo.getAppUser_CD());
							plan.setstart_time(taskIDPosList.get(0).getIDPosStart_TM());
							plan.setend_time(taskIDPosList.get(0).getIDPosEnd_TM());
							/*plan.setstart_time("9999-12-31 23:59:59");
							plan.setend_time("1900-01-01 00:00:00");*/
							plan.seterror_count(0);
							plan.setrecord_count(planDRs.size()
									- planUndoDRs.size());
						}
						m_DJResultHash.add(resultJsonData);

						if (!m_MobjectStateHash.containsKey(unit_id + "|"
								+ times_id)) {
							m_MobjectStateHash.put(unit_id + "|" + times_id,
									resInfo.getMObjectStateName_TX() + "|"
											+ resInfo.getAppUser_CD() + "|"
											+ resInfo.getCompleteTime_DT());
						}

						if (!plan.getnextplanStartTime().equalsIgnoreCase(
								"1900-01-01 00:00:00")
								&& DateTimeHelper.StringToDate(
										plan.getnextplanStartTime()).before(
										DateTimeHelper.StringToDate(resInfo
												.getCompleteTime_DT())))
							plan.setrecord_count(plan.getrecord_count() - 1);
						else {
							if (resInfo.getException_YN().equals("1"))
								plan.seterror_count(plan.geterror_count() + 1);
						}

						/*if (DateTimeHelper.StringToDate(plan.getstart_time())
								.after(DateTimeHelper
										.StringToDate(completeStartTime)))
							plan.setstart_time(completeStartTime);
						if (DateTimeHelper.StringToDate(plan.getend_time())
								.before(DateTimeHelper
										.StringToDate(completeEndTime)))
							plan.setend_time(completeEndTime);*/
					}

					String resultJsonStr = CreateOneRfidJsonData(taskIDPosList
							.get(0).getTransInfo_TX().split("\\|")[2],
							plan.getuse_line_id(), "$result_rfid_id$",
							"$result_route_id$",
							NormalDateToUnixDate(plan.getstart_time()),
							NormalDateToUnixDate(plan.getend_time()));

					String rfidResultJsonStr = resultJsonStr
							+ "|"
							+ plan.getcon_line_id()
							+ plan.getplan_id()
							+ taskIDPosList.get(0).getTransInfo_TX()
									.split("\\|")[2]
							+ "|"
							+ taskIDPosList.get(0).getTransInfo_TX()
									.split("\\|")[2];

					result_route_id = "0";
					String OneLineDataJson = GetOneLineDataTogether(plan);

					String OneRfidJson = "";
					String djResDataTotal = "";

					result_rfid_id = "0";
					OneRfidJson = GetOneRfidDataTogether(rfidResultJsonStr,
							result_route_id);

					for (String djRstJsonResult : m_DJResultHash) {
						djResDataTotal += GetOneDJResultDataTogether(
								djRstJsonResult, result_rfid_id, idposKeyStr)
								+ ",";
					}

					djResDataTotal = djResDataTotal.substring(0,
							djResDataTotal.length() - 1);
					djResDataTotal = OneRfidJson
							+ "\"ptrlPointRecContentList\":[" + djResDataTotal
							+ "]},";
					djResDataTotal = djResDataTotal.substring(0,
							djResDataTotal.length() - 1);
					djResDataTotal = OneLineDataJson + "\"ptrlPointRecList\":["
							+ djResDataTotal + "]}";

					result = WebserviceFactory
							.InsertMultiDJResultJITForAndroidByCaseXJ(djResDataTotal);

					List<MObject_State> mobjectstateList = mobjectstateDao
							.loadforUploadJITCaseXJ(con_line_id, times_id);
					if (result) {
						if ((mobjectstateList != null && mobjectstateList
								.size() > 0)
								|| (taskIDPosList != null && taskIDPosList
										.size() > 0)) {
							srResult = updateMobjectState(mobjectstateList,
									taskIDPosList);
						}
					}
					if (result && srResult) {
						djresultActiveDao
								.deleteDJResultUploadedData(djResultList);
						djphotobyresultDao
								.deleteDJResultForFilesUploadedData(filesList);
						for (int n = 0; n < MyfileList.size(); n++) {
							File f = new File(MyfileList.get(n));
							f.delete();
						}
						djtaskIDPosActiveDao.deleteUploadedData(taskIDPosList);
						mobjectstateDao.deleteUploadedData(mobjectstateList);
					}
					clearReferData();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 解析计划TransInfo信息
	 * 
	 * @param transInfo
	 * @return
	 */
	private boolean DecondePlanTransInfo(String transInfo) {
		String[] tempInfos = transInfo.split("\\|");
		if (tempInfos.length < 6)
			return false;
		use_line_id = tempInfos[0];
		con_line_id = tempInfos[1];
		point_id = tempInfos[2];
		unit_id = tempInfos[3];
		content_id = tempInfos[4];
		times_id = tempInfos[5];
		return true;
	}

	/**
	 * 根据文件类型名称获取对应的ID
	 * 
	 * @param stateName
	 * @return
	 */
	private String GetFileTypeID(String stateName) {
		String stateID = "0";
		String stateStrs = "图片,1;视频,2;音频,3";
		String[] states = stateStrs.split(";");
		for (String sta : states) {
			if (sta.split(",")[0].equalsIgnoreCase(stateName)) {
				stateID = sta.split(",")[1];
				break;
			}
		}
		return stateID;
	}

	/**
	 * 根据启停状态名称获取对应的ID
	 * 
	 * @param stateName
	 * @return
	 */
	private String GetStateID(String stateName) {
		// 默认设备状态为启用状态
		String stateID = "1";
		String stateStrs = "启用,1;备用,2;停用,3;检修,4";
		String[] states = stateStrs.split(";");
		for (String sta : states) {
			if (sta.split(",")[0].equalsIgnoreCase(stateName)) {
				stateID = sta.split(",")[1];
				break;
			}
		}
		return stateID;
	}

	private int RSHash(String str) {
		int b = 378551;
		int a = 63689;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * a
					+ Integer.valueOf(String.valueOf(str.toCharArray()[i]));
			a = a * b;
		}
		String hashstr = String.valueOf(hash);
		return Integer.valueOf(hashstr.substring(hashstr.length() - 8,
				hashstr.length()));
	}

	/**
	 * 将日期转换为Unix格式串
	 * 
	 * @param dateTime
	 * @return
	 */
	private static String NormalDateToUnixDate(String dateTime) {
		Date date = DateTimeHelper.StringToDate(dateTime);
		long unixTime = date.getTime();
		return String.valueOf(unixTime).substring(0,
				String.valueOf(unixTime).length() - 3);
	}

	private void clearReferData() {
		m_DJResultHash.clear();
		m_MobjectStateHash.clear();
	}

	/**
	 * 设备状态更新
	 * 
	 * @param mObjectStateHis
	 * @param mDJTaskIDInfoList
	 * @return
	 */
	private boolean updateMobjectState(List<MObject_State> mObjectStateList,
			List<DJ_TaskIDPosActive> mDJTaskIDInfoList) {
		boolean result = true;
		List<String> mPlanIDs = getPlanIDs(mObjectStateList, mDJTaskIDInfoList);
		for (String _planid : mPlanIDs) {
			if (!result)
				break;
			for (SRLineTreeLastResult sr : UpLoadSRLineTreeLastResultList) {
				String _mobjectID = sr.getSRControlPoint_ID();
				String _mobState = sr.getLastSRMemo_TX();
				String _appUserCD = "";

				String _changeTime = NormalDateToUnixDate(DateTimeHelper
						.getDateTimeNow());

				if (m_MobjectStateHash.containsKey(_mobjectID + "|" + _planid)) {
					String tempInfo = m_MobjectStateHash.get(_mobjectID + "|"
							+ _planid);
					_mobState = tempInfo.split("\\|")[0];
					_appUserCD = tempInfo.split("\\|")[1];
					_changeTime = NormalDateToUnixDate((tempInfo.split("\\|")[2]));
				}

				String planid = "";
				String mobjectid = "";
				String mobState = "";
				String appUserCD = "";
				String changeTime = "";
				for (MObject_State rstInfo : mObjectStateList) {
					if (rstInfo.getTransInfo_TX().length() > 0)
						planid = rstInfo.getTransInfo_TX().split("\\|")[rstInfo
								.getTransInfo_TX().split("\\|").length - 1];
					mobjectid = rstInfo.getStartAndEndPoint_ID();
					mobState = rstInfo.getMObjectStateName_TX();
					appUserCD = rstInfo.getAppUser_CD();
					changeTime = NormalDateToUnixDate(rstInfo
							.getCompleteTime_DT());
					if (_planid.equalsIgnoreCase(planid)
							&& _mobjectID.equalsIgnoreCase(mobjectid)) {
						_mobState = mobState;
						_appUserCD = appUserCD;
						_changeTime = changeTime;
						break;
					}
				}
				String jsonData = CreateOneMobjectStateJsonData(_planid,
						_mobjectID, GetStateID(_mobState), _appUserCD,
						_changeTime);
				result = WebserviceFactory
							.InsertSRResultJITForAndroidByCaseXJ(jsonData);

				if (!result)
					break;
			}
		}

		return result;
	}

	/**
	 * 获取时段区间内有过操作的时段ID(按时序) 有过操作是指：在PDA端有更新过设备状态或有碰过纽扣的动作。
	 * 
	 * @param mObjectStateHis
	 * @param mDJTaskIDInfoList
	 * @return
	 */
	private List<String> getPlanIDs(List<MObject_State> mObjectStateList,
			List<DJ_TaskIDPosActive> mDJTaskIDInfoList) {
		// 算法描述：
		// 通过设备状态结果以及到位结果获取所有有过操作的时段ID
		List<String> m_PlanIDs = new ArrayList<String>();
		List<String> PlanIDs = new ArrayList<String>();
		int minPlanId = 0;
		int maxPlanId = 0;
		if (mObjectStateList != null && mObjectStateList.size() > 0) {
			for (MObject_State rstInfo : mObjectStateList) {
				String planid = "";
				if (rstInfo.getTransInfo_TX().length() > 0) {
					planid = rstInfo.getTransInfo_TX().split("\\|")[rstInfo
							.getTransInfo_TX().split("\\|").length - 1];
					if (!m_PlanIDs.contains(planid))
						m_PlanIDs.add(planid);
					if (Integer.parseInt(planid) > maxPlanId)
						maxPlanId = Integer.parseInt(planid);
					if (minPlanId > 0 && Integer.parseInt(planid) < minPlanId)
						minPlanId = Integer.parseInt(planid);
					else if (minPlanId == 0)
						minPlanId = Integer.parseInt(planid);
				}
			}
		}
		if (mDJTaskIDInfoList != null && mDJTaskIDInfoList.size() > 0) {
			for (DJ_TaskIDPosActive rstInfo : mDJTaskIDInfoList) {
				String planid = "";
				if (rstInfo.getTransInfo_TX().length() > 0) {
					String IDPosTransInfo_TX = rstInfo.getTransInfo_TX().split(
							"\\|")[0]
							+ "|"
							+ rstInfo.getTransInfo_TX().split("\\|")[1]
							+ "|" + rstInfo.getTransInfo_TX().split("\\|")[3];
					planid = IDPosTransInfo_TX.split("\\|")[IDPosTransInfo_TX
							.split("\\|").length - 1];
					if (!m_PlanIDs.contains(planid))
						m_PlanIDs.add(planid);
					if (Integer.parseInt(planid) > maxPlanId)
						maxPlanId = Integer.parseInt(planid);
					if (minPlanId > 0 && Integer.parseInt(planid) < minPlanId)
						minPlanId = Integer.parseInt(planid);
					else if (minPlanId == 0)
						minPlanId = Integer.parseInt(planid);
				}
			}
		}
		if (minPlanId > 0 && maxPlanId > 0) {
			if (minPlanId == maxPlanId)
				PlanIDs.add(String.valueOf(maxPlanId));
			else {
				PlanIDs = m_PlanIDs;
			}
		}
		return PlanIDs;
	}

	/**
	 * 生成路线数据Json字符串
	 * 
	 * @param use_line_id
	 * @param con_line_id
	 * @param plan_id
	 * @param result_route_id
	 * @param start_time
	 * @param end_time
	 * @param user_id
	 * @param lost_count
	 * @param error_count
	 * @param content_count
	 * @return
	 */
	private String CreateOneLineJsonData(String use_line_id,
			String con_line_id, String plan_id, String result_route_id,
			String start_time, String end_time, String user_id,
			String lost_count, String error_count, String content_count) {
		YKSHLineData obj = new YKSHLineData();
        obj.setUse_line_id(use_line_id);
        obj.setCon_line_id(con_line_id);
        obj.setPlan_id(plan_id);
        obj.setResult_route_id(result_route_id);
        obj.setStart_time(start_time);
        obj.setEnd_time(end_time);
        obj.setUser_id(user_id);
        obj.setLost_count(lost_count);
        obj.setError_count(error_count);
        obj.setContent_count(content_count);
        
        Gson gson = new Gson();
        String result = gson.toJson(obj);
        return result;
        
		/*String result = "{\"use_line_id\":\"" + use_line_id
				+ "\",\"con_line_id\":\"" + con_line_id + "\",\"plan_id\":\""
				+ plan_id + "\",\"result_route_id\":\"" + result_route_id
				+ "\",\"start_time\":\"" + start_time + "\",\"end_time\":\""
				+ end_time + "\",\"user_id\":\"" + user_id
				+ "\",\"lost_count\":\"" + lost_count + "\",\"error_count\":\""
				+ error_count + "\",\"content_count\":\"" + content_count
				+ "\"}";
		return result;*/
	}

	/**
	 * 生成到位Json字符串
	 * 
	 * @param ptrl_point_id
	 * @param use_line_id
	 * @param result_rfid_id
	 * @param result_route_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	private String CreateOneRfidJsonData(String ptrl_point_id,
			String use_line_id, String result_rfid_id, String result_route_id,
			String start_time, String end_time) {
		YKSHRFIDData obj = new YKSHRFIDData();
        obj.setPtrl_point_id(ptrl_point_id);
        obj.setUse_line_id(use_line_id);
        obj.setResult_rfid_id(result_rfid_id);
        obj.setResult_route_id(result_route_id);
        obj.setStart_time(start_time);
        obj.setEnd_time(end_time);
        
        Gson gson = new Gson();
        String result = gson.toJson(obj);
        return result;
		/*String result = "{\"ptrl_point_id\":\"" + ptrl_point_id
				+ "\",\"use_line_id\":\"" + use_line_id
				+ "\",\"result_rfid_id\":\"" + result_rfid_id
				+ "\",\"result_route_id\":\"" + result_route_id
				+ "\",\"start_time\":\"" + start_time + "\",\"end_time\":\""
				+ end_time + "\"}";
		return result;*/

	}

	/**
	 * 生成点检结果JsonData
	 * 
	 * @param ptrl_content_id
	 * @param result_point_id
	 * @param result_rfid_id
	 * @param start_time
	 * @param end_time
	 * @param rec_content
	 * @param des
	 * @param is_alarm
	 * @param loginName
	 * @param status
	 * @param fileplanpartionstr
	 * @param Data8k
	 * @return
	 */
	private String CreateOneDJResultJsonData(String ptrl_content_id,
			String result_point_id, String result_rfid_id, String start_time,
			String end_time, String rec_content, String des, String is_alarm,
			String loginName, String status, /*String fileplanpartionstr,*/
			List<DJ_PhotoByResult> FileInfoList,
			String Data8k) {
		
		YKSHDJResultData obj = new YKSHDJResultData();
        obj.setPtrl_content_id(ptrl_content_id);
        obj.setResult_point_id(result_point_id);
        obj.setResult_rfid_id(result_rfid_id);
        obj.setStart_time(start_time);
        obj.setEnd_time(end_time);
        obj.setRec_content(rec_content);
        obj.setDes(des);        
        obj.setIs_alarm(is_alarm);
        obj.setLoginname(loginName);
        obj.setStatus(status);      
        obj.setQuake_data(Data8k);

        if (FileInfoList != null && FileInfoList.size() > 0)
        {
        	filestruc[] fss = new filestruc[FileInfoList.size()];
            int index = 0;
            for (DJ_PhotoByResult fileInfo : FileInfoList) {
                String FileType = "";
                if (fileInfo.getLCType().equalsIgnoreCase("ZP")) {
					FileType = "图片";
				} else if (fileInfo.getLCType().equalsIgnoreCase("LY")) {
					FileType = "音频";
				} else if (fileInfo.getLCType().equalsIgnoreCase("LX")) {
					FileType = "视频";
				}
                
                String fileTypeID = GetFileTypeID(FileType);
				String filebase64str = String.valueOf(fileInfo.getFile_Bytes());
				filestruc fs = new filestruc();
				fs.setFileType(fileTypeID);
				fs.setfileData(filebase64str);
                fss[index] = fs;
                index = index + 1;
            }
            obj.setFiles(fss);
        }
        else
            obj.setFiles(new filestruc[0]);
        
        Gson gson = new Gson();
        String result = gson.toJson(obj);
        return result;
        
		/*String result = "{\"ptrl_content_id\":\"" + ptrl_content_id
				+ "\",\"result_point_id\":\"" + result_point_id
				+ "\",\"result_rfid_id\":\"" + result_rfid_id
				+ "\",\"start_time\":\"" + start_time + "\",\"end_time\":\""
				+ end_time + "\",\"rec_content\":\"" + rec_content
				+ "\",\"quake_data\":\"" + Data8k + "\",\"des\":\"" + des
				+ "\",\"is_alarm\":\"" + is_alarm + "\",\"loginname\":\""
				+ loginName + "\",\"status\":\"" + status + "\",\"files\":["
				+ fileplanpartionstr + "]}";
		return result;*/
	}

	/**
	 * 生成设备状态数据Json字符串
	 * 
	 * @param plan_id
	 * @param ptrl_unit_id
	 * @param status
	 * @param user_id
	 * @param time
	 * @return
	 */
	private String CreateOneMobjectStateJsonData(String plan_id,
			String ptrl_unit_id, String status, String user_id, String time) {
		YKSHMobjectStateData obj = new YKSHMobjectStateData();
        obj.setPlan_id(plan_id);
        obj.setPtrl_unit_id(ptrl_unit_id);
        obj.setStatus(status);
        obj.setUser_id(user_id);
        obj.setTime(time);

        Gson gson = new Gson();
        String result = gson.toJson(obj);
        return result;
		/*String result = "{\"plan_id\":\"" + plan_id + "\",\"ptrl_unit_id\":\""
				+ ptrl_unit_id + "\",\"status\":\"" + status
				+ "\",\"user_id\":\"" + user_id + "\",\"time\":\"" + time
				+ "\"}";
		return result;*/
	}

	/**
	 * 生成文件Json字符串
	 * 
	 * @param FileInfoList
	 * @return
	 */
	private String CreateOneResultFileListJsonData(
			List<DJ_PhotoByResult> FileInfoList) {
		String result = "";
		if (FileInfoList.size() > 0) {
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			for (DJ_PhotoByResult fileInfo : FileInfoList) {
				String FileType = "";
				if (fileInfo.getLCType().equalsIgnoreCase("ZP")) {
					FileType = "图片";
				} else if (fileInfo.getLCType().equalsIgnoreCase("LY")) {
					FileType = "音频";
				} else if (fileInfo.getLCType().equalsIgnoreCase("LX")) {
					FileType = "视频";
				}

				String fileTypeID = GetFileTypeID(FileType);
				String filebase64str = String.valueOf(fileInfo.getFile_Bytes());
				filestruc fs = new filestruc();
				fs.setFileType(fileTypeID);
				fs.setfileData(filebase64str);
				String str = gson.toJson(fs);

				if (!StringUtils.isEmpty(result)) {
					result += "," + str;
				} else {
					result += str;
				}
			}
		}
		return result;
	}

	/**
	 * 路线数据
	 * 
	 * @param plan
	 * @return
	 */
	private String GetOneLineDataTogether(PlanInfoCaseXJJIT plan) {
		result_route_id = String.valueOf(RSHash(plan.getuse_line_id()
				+ plan.getcon_line_id() + plan.getplan_id())); // 计划时段的开始时间
		String jsonData = CreateOneLineJsonData(
				plan.getuse_line_id(),
				plan.getcon_line_id(),
				plan.getplan_id(),
				result_route_id,
				NormalDateToUnixDate(plan.getstart_time()),
				NormalDateToUnixDate(plan.getend_time()),
				plan.getuser_id(),
				String.valueOf(plan.getcontent_count() - plan.getrecord_count()),
				String.valueOf(plan.geterror_count()), String.valueOf(plan
						.getcontent_count()));
		return jsonData.substring(0, jsonData.length() - 1) + ",";
	}

	/**
	 * 到位数据
	 * 
	 * @param jsonResult
	 * @param result_route_id
	 * @return
	 */
	private String GetOneRfidDataTogether(String jsonResult,
			String result_route_id) {
		result_rfid_id = String.valueOf(RSHash(result_route_id
				+ jsonResult.split("\\|")[1]));
		idposKeyStr = jsonResult.split("\\|")[1];
		String jsonData = jsonResult.split("\\|")[0].replace(
				"$result_route_id$", result_route_id).replace(
				"$result_rfid_id$", result_rfid_id);
		return jsonData.substring(0, jsonData.length() - 1) + ",";
	}

	/**
	 * 结果数据
	 * 
	 * @param jsonResult
	 * @param result_rfid_id
	 * @param idposKeyStr
	 * @param string
	 * @return
	 */
	private String GetOneDJResultDataTogether(String jsonResult,
			String result_rfid_id, String idposKeyStr) {
		String jsonData = jsonResult
				.replace("$result_rfid_id$", result_rfid_id);
		return jsonData;
	}
	
	public class YKSHLineData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1516526210678514695L;
		private String use_line_id;
		public String getUse_line_id() {
			return use_line_id;
		}
		public void setUse_line_id(String s) {
			use_line_id = s;
		}
		
		private String con_line_id;
		public String getCon_line_id() {
			return con_line_id;
		}
		public void setCon_line_id(String s) {
			con_line_id = s;
		}
		
		private String plan_id;
		public String getPlan_id() {
			return plan_id;
		}
		public void setPlan_id(String s) {
			plan_id = s;
		}
		
		private String result_route_id;
		public String getResult_route_id() {
			return result_route_id;
		}
		public void setResult_route_id(String s) {
			result_route_id = s;
		}
		
		private String start_time;
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String s) {
			start_time = s;
		}
		
		private String end_time;
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String s) {
			end_time = s;
		}
		
		private String user_id;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String s) {
			user_id = s;
		}
		
		private String lost_count;
		public String getLost_count() {
			return lost_count;
		}
		public void setLost_count(String s) {
			lost_count = s;
		}
		
		private String error_count;
		public String getError_count() {
			return error_count;
		}
		public void setError_count(String s) {
			error_count = s;
		}
		
		private String content_count;
		public String getContent_count() {
			return content_count;
		}
		public void setContent_count(String s) {
			content_count = s;
		}
	}
	
	public class YKSHRFIDData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6548271770792646274L;
		
		private String ptrl_point_id;
		public String getPtrl_point_id() {
			return ptrl_point_id;
		}
		public void setPtrl_point_id(String s) {
			ptrl_point_id = s;
		}
		
		private String use_line_id;
		public String getUse_line_id() {
			return use_line_id;
		}
		public void setUse_line_id(String s) {
			use_line_id = s;
		}
		
		private String result_rfid_id;
		public String getResult_rfid_id() {
			return result_rfid_id;
		}
		public void setResult_rfid_id(String s) {
			result_rfid_id = s;
		}
		
		private String result_route_id;
		public String getResult_route_id() {
			return result_route_id;
		}
		public void setResult_route_id(String s) {
			result_route_id = s;
		}
		
		private String start_time;
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String s) {
			start_time = s;
		}
		
		private String end_time;
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String s) {
			end_time = s;
		}
	}
	
	public class YKSHDJResultData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4730630276577214478L;
		private String ptrl_content_id;
		public String getPtrl_content_id() {
			return ptrl_content_id;
		}
		public void setPtrl_content_id(String s) {
			ptrl_content_id = s;
		}
		
        private String result_point_id;
        public String getResult_point_id() {
        	return result_point_id;
        }
        public void setResult_point_id(String s) {
        	result_point_id = s;
        }
        
        private String result_rfid_id;
        public String getResult_rfid_id() {
        	return result_rfid_id;
        }
        public void setResult_rfid_id(String s) {
        	result_rfid_id = s;
        }
        
        private String start_time;
        public String getStart_time() {
        	return start_time;
        }
        public void setStart_time(String s) {
        	start_time = s;
        }
        
        private String end_time;
        public String getEnd_time() {
        	return end_time;
        }
        public void setEnd_time(String s) {
        	end_time = s;
        }
        
        private String rec_content;
        public String getRec_content() {
        	return rec_content;
        }
        public void setRec_content(String s) {
        	rec_content = s;
        }
        
        private String quake_data;
        public String getQuake_data() {
        	return quake_data;
        }
        public void setQuake_data(String s) {
        	quake_data = s;
        }
        
        private String des;
        public String getDes() {
        	return des;
        }
        public void setDes(String s) {
        	des = s;
        }
        
        private String is_alarm;
        public String getIs_alarm() {
        	return is_alarm;
        }
        public void setIs_alarm(String s) {
        	is_alarm = s;
        }
        
        private String loginname;
        public String getLoginname() {
        	return loginname;
        }
        public void setLoginname(String s) {
        	loginname = s;
        }
        
        private String status;
        public String getStatus() {
        	return status;
        }
        public void setStatus(String s) {
        	status = s;
        }
        
        private filestruc[] files;
        public filestruc[] getFiles() {
        	return files;
        }
        public void setFiles(filestruc[] s) {
        	files = s;
        }
	}

	public class filestruc implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7438774674157644368L;
		private String type;

		public String getFileType() {
			return type;
		}

		public void setFileType(String s) {
			type = s;
		}

		private String data;

		public String getfileData() {
			return data;
		}

		public void setfileData(String s) {
			data = s;
		}
	}
	
	public class YKSHMobjectStateData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4990835465124516239L;
		
		private String plan_id;
		public String getPlan_id() {
			return plan_id;
		}
		public void setPlan_id(String s) {
			plan_id = s;
		}
		
		private String ptrl_unit_id;
		public String getPtrl_unit_id() {
			return ptrl_unit_id;
		}
		public void setPtrl_unit_id(String s) {
			ptrl_unit_id = s;
		}
		
		private String status;
		public String getStatus() {
			return status;
		}
		public void setStatus(String s) {
			status = s;
		}
		
		private String user_id;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String s) {
			user_id = s;
		}
		
		private String time;
		public String getTime() {
			return time;
		}
		public void setTime(String s) {
			time = s;
		}
	}
}