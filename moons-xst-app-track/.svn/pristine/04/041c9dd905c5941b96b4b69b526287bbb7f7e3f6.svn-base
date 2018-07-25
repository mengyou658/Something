package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.sqlite.WorkBillSession;
import com.moons.xst.sqlite.WorkResultSession;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.WorkAccessory;
import com.moons.xst.track.bean.WorkUploading;
import com.moons.xst.track.bean.Work_Bill;
import com.moons.xst.track.bean.Work_Detail_Bill;
import com.moons.xst.track.dao.Work_BillDao;
import com.moons.xst.track.dao.Work_Detail_BillDao;

/**
 * 工作票结果库Helper
 **/
public class WorkResultHelper {
	private static final String TAG = "WorkResultHelper";
	private WorkResultSession WorkResultSession;

	private Work_BillDao work_BillDao;
	private Work_Detail_BillDao work_Detail_BillDao;

	static WorkResultHelper _intance;

	public static WorkResultHelper GetIntance() {
		if (_intance == null) {
			_intance = new WorkResultHelper();
		}
		return _intance;
	}

	// 工作票转json
	public String getWorkJson(Context context) {
		String json = "";
		try {
			WorkResultSession = ((AppContext) context.getApplicationContext())
					.getWorkResultSession();
			work_BillDao = WorkResultSession.getWork_BillDao();
			work_Detail_BillDao = WorkResultSession.getWork_Detail_BillDao();
			List<Work_Bill> WorkBill = work_BillDao.loadAll();
			List<Work_Detail_Bill> WorkDetailBill = work_Detail_BillDao
					.loadAll();
			List<WorkUploading> uploadingList = new ArrayList<WorkUploading>();
			for (int i = 0; i < WorkBill.size(); i++) {
				WorkUploading entity = new WorkUploading();
				Work_Bill entity2 = WorkBill.get(i);
				entity.setWork_Bill_ID(entity2.getWork_Bill_ID());
				entity.setWork_Bill_Code(entity2.getWork_Bill_Code());
				entity.setWork_Begin_Time(entity2.getWork_Begin_Time());
				entity.setWork_End_Time(entity2.getWork_End_Time());
				entity.setTache_ID(entity2.getTache_ID());
				entity.setJD_ID(entity2.getJD_ID());
				entity.setWork_Bill_Type_TX(entity2.getWork_Bill_Type_TX());
				entity.setWork_Bill_TaskContent_TX(entity2
						.getWork_Bill_TaskContent_TX());
				entity.setWork_Bill_Memo_TX(entity2.getWork_Bill_Memo_TX());
				entity.setWork_Bill_Operator(entity2.getWork_Bill_Operator());
				entity.setWork_Bill_Guardian(entity2.getWork_Bill_Guardian());
				entity.setWork_Bill_Watch(entity2.getWork_Bill_Watch());
				entity.setWork_Bill_Duty(entity2.getWork_Bill_Duty());
				entity.setID_CD(entity2.getID_CD());
				entity.setPlace_TX(entity2.getPlace_TX());
				List<Work_Detail_Bill> WorkDetails = new ArrayList<Work_Detail_Bill>();
				for (int j = 0; j < WorkDetailBill.size(); j++) {
					if (entity2.getWork_Bill_ID() == WorkDetailBill.get(j)
							.getWork_Bill_ID()) {
						WorkDetails.add(WorkDetailBill.get(j));
					}
				}
				entity.setDetailList(WorkDetails);
				// 工作票附件
				List<WorkAccessory> Files = new ArrayList<WorkAccessory>();
				entity.setAccessoryList(Files);
				uploadingList.add(entity);
			}
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			json = gson.toJson(uploadingList);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return "transform json error" + e;
		}
		return json;
	}

	// 插入数据到结果库
	public String saveWorkResult(Context context, int workId) {
		try {
			Work_Bill workEntity = WorkBillHelper.GetIntance().getSingleWork(
					context, workId);
			List<Work_Detail_Bill> workDetailList = WorkBillHelper.GetIntance()
					.getMeasureClause(context, workId);
			WorkResultSession = ((AppContext) context.getApplicationContext())
					.getWorkResultSession();
			work_BillDao = WorkResultSession.getWork_BillDao();
			work_Detail_BillDao = WorkResultSession.getWork_Detail_BillDao();
			for (int i = 0; i < workDetailList.size(); i++) {
				work_Detail_BillDao.insert(workDetailList.get(i));
			}
			work_BillDao.insertWorkResult(workEntity);
		} catch (Exception e) {
			Log.i(TAG, e.toString());
			return e.toString();
		}
		return "true";
	}

}
