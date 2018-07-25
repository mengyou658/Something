package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.renderscript.Int2;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.navisdk.util.common.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.sqlite.OverhaulDaoSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.bean.OverhaulUser;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.OverhaulPlanDao;
import com.moons.xst.track.dao.OverhaulProjectDao;
import com.moons.xst.track.ui.OverhaulProjectDetailAty;

/**
 * 检修管理操作相关操作
 * 
 * @author yanglin
 * 
 */
public class OverhaulHelper {
	private static final String TAG = "OverhaulHelper";

	private Context context;
	private static OverhaulHelper overhaulHelper;
	private OverhaulDaoSession OverhaulSession;
	private com.moons.xst.track.dao.OverhaulUserDao OverhaulUserDao;
	private OverhaulProjectDao overhaulProjectDao;
	List<OverhaulProject> overhaulProjectList = new ArrayList<OverhaulProject>();

	public static OverhaulHelper getInstance() {
		if (overhaulHelper == null) {
			overhaulHelper = new OverhaulHelper();
		}
		return overhaulHelper;
	}

	// 根据账号和密码查人员信息
	public List<OverhaulUser> getLoginPerson(Context context, String account,
			String pwd) {
		List<OverhaulUser> list = new ArrayList<OverhaulUser>();
		try {
			OverhaulSession = ((AppContext) context.getApplicationContext())
					.getOverhaulDaoSession();
			OverhaulUserDao = OverhaulSession.getOverhaulUserDao();
			list = OverhaulUserDao.LoginSelect(account, pwd);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
		return list;
	}

	/**
	 * 根据部门ID获取到所有的检修项目
	 * 
	 * @param deptID
	 *            部门ID
	 * @return
	 */
	public void OverhaulProjectList(Context context, Integer deptID) {

		AppContext appContext = (AppContext) context.getApplicationContext();
		AppContext.overhaulProjectBuffer.clear();
		OverhaulDaoSession overhaulDaoSession = appContext
				.getOverhaulDaoSession();
		OverhaulProjectDao overhaulProjectDao = overhaulDaoSession
				.getOverhaulProjectDao();
		AppContext.overhaulProjectBuffer = overhaulProjectDao
				.loadAllBYDeptID(deptID);
	}

	/**
	 * 根据计划ID获取到所有的检修计划
	 * 
	 * @param planID
	 *            计划ID
	 * @return
	 */
	public void OverhaulPlanList(Context context, String planIDStr) {
		AppContext appContext = (AppContext) context.getApplicationContext();

		OverhaulDaoSession overhaulDaoSession = appContext
				.getOverhaulDaoSession();
		OverhaulPlanDao overhaulPlanDao = overhaulDaoSession
				.getOverhaulPlanDao();
		AppContext.overhaulPlanBuffer = overhaulPlanDao
				.loadAllBYPalnID(planIDStr);

	}

	public List<OverhaulProject> getOverhaulProjectListByPlanID(
			Context context, int plan_ID) {
		ArrayList<OverhaulProject> OverhaulProjectListByPlanID = new ArrayList<OverhaulProject>();
		for (int i = 0; i < AppContext.overhaulProjectBuffer.size(); i++) {
			if (plan_ID == AppContext.overhaulProjectBuffer.get(i)
					.getJXPlan_ID()) {
				OverhaulProjectListByPlanID
						.add(AppContext.overhaulProjectBuffer.get(i));
			}
		}
		return OverhaulProjectListByPlanID;
	}

	public void upDateProjectImplement(Context context,
			OverhaulProject overhaulProject) {
		AppContext appContext = (AppContext) context.getApplicationContext();
		OverhaulDaoSession overhaulDaoSession = appContext
				.getOverhaulDaoSession();
		OverhaulProjectDao overhaulProjectDao = overhaulDaoSession
				.getOverhaulProjectDao();
		overhaulProjectDao.upDateProjectImplement(overhaulProject);
	}

	/**
	 * 更新表相关信息(一审)
	 * 
	 * @param context
	 * @param qc
	 *            项目编号
	 * @param Finish_YN
	 *            项目是否完成
	 * @param name
	 *            签名人信息+完成时间
	 * @param remark
	 *            备注信息
	 */
	public void upDateExamine(Context mContext, OverhaulProject overhaulProject) {
		AppContext appContext = (AppContext) mContext.getApplicationContext();
		OverhaulDaoSession overhaulDaoSession = appContext
				.getOverhaulDaoSession();
		OverhaulProjectDao overhaulProjectDao = overhaulDaoSession
				.getOverhaulProjectDao();
		switch (overhaulProject.getJXType_ID()) {
		case 1:
			overhaulProjectDao.upDateExamine1(overhaulProject);
			break;
		case 2:
			if ("0".equals(overhaulProject.getFinish_YN())) {
				overhaulProjectDao.upDateExamine1(overhaulProject);
			} else if ("1".equals(overhaulProject.getFinish_YN())) {
				overhaulProjectDao.upDateExamine2(overhaulProject);
			}
			break;
		case 3:
			if ("0".equals(overhaulProject.getFinish_YN())) {
				if ("1".equals(overhaulProject.getQC2())
						&& "1".equals(overhaulProject.getQC1())) {
					overhaulProjectDao.upDateExamine2(overhaulProject);
				}
				if ("1".equals(overhaulProject.getQC1())
						&& StringUtils.isEmpty(overhaulProject.getQC2())) {
					overhaulProjectDao.upDateExamine1(overhaulProject);
				}
			} else if ("1".equals(overhaulProject.getFinish_YN())) {
				overhaulProjectDao.upDateExamine3(overhaulProject);
			}
			break;
		default:
			break;
		}
	}

//	public static Void upLoadAfterDataChange(){
//		
//	}
	public ReturnResultInfo JXuploading(Context context) {
		ReturnResultInfo info = new ReturnResultInfo();
		try {
			OverhaulSession = ((AppContext) context.getApplicationContext())
					.getOverhaulDaoSession();
			overhaulProjectDao = OverhaulSession.getOverhaulProjectDao();
			List<OverhaulProject> list = overhaulProjectDao.loadAll();
			// 数据库没有数据则无需上传
			if (list.size() <= 0) {
				info.setResult_YN(true);
				return info;
			}
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			String json = gson.toJson(list);
			return WebserviceFactory.OverhaulUploading(context, json);
		} catch (Exception e) {
			info.setResult_YN(false);
			info.setError_Message_TX(e.getMessage());
			return info;
		}
	}

	static String mCurrentQC = "";

	public static void setCurrentQC(String str) {
		mCurrentQC = str;
	}

	public static boolean getCurrentQC(String currentQC,
			OverhaulProject overhaulProject) {
		
		if (currentQC.equalsIgnoreCase(overhaulProject.getCurrentQC())) {
			return true;
		}
		return false;
	}
}
