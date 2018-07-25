package com.moons.xst.buss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.OperDetailResult;
import com.moons.xst.track.bean.OperMainResult;
import com.moons.xst.track.bean.OperateUploading;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.dao.OperDetailResultDao;
import com.moons.xst.track.dao.OperMainResultDao;
import com.moons.xst.track.dao.Shift_REsultDao;
import com.moons.xst.track.dao.WH_DQTaskDao;

public class OperateResultHelper {
	private static final String TAG = "OperateResultHelper";
	private TwoBillResultDaoSession TwoBillResultDaoSession;

	private OperDetailResultDao OperDetailResultDao;
	private OperMainResultDao OperMainResultDao;
	private Shift_REsultDao Shift_REsultDao;
	private WH_DQTaskDao WH_DQTaskDao;

	InitDJsqlite init = new InitDJsqlite();
	static OperateResultHelper _intance;

	public static OperateResultHelper GetIntance() {
		if (_intance == null) {
			_intance = new OperateResultHelper();
		}
		return _intance;
	}

	public void InsertOperDetail(Context context, OperDetailResult entity) {
		try {
			TwoBillResultDaoSession = ((AppContext) context
					.getApplicationContext()).getTwoBillResultSession();
			OperDetailResultDao = TwoBillResultDaoSession
					.getOperDetailResultDao();
			OperDetailResultDao.insertDetail(entity);
			// long row = OperDetailResultDao.insert(entity);
		} catch (Exception e) {
			// TODO: handle exception
			UIHelper.ToastMessage(context, "添加结果库失败");
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public void InsertOperMain(Context context, OperMainResult entity) {
		try {
			TwoBillResultDaoSession = ((AppContext) context
					.getApplicationContext()).getTwoBillResultSession();
			OperMainResultDao = TwoBillResultDaoSession.getOperMainResultDao();
			OperMainResultDao.inserMain(entity);
			// long row = OperMainResultDao.insert(entity);
		} catch (Exception e) {
			UIHelper.ToastMessage(context, "添加结果库失败");
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public void updateOperMain(Context context, String code, String time) {
		try {
			TwoBillResultDaoSession = ((AppContext) context
					.getApplicationContext()).getTwoBillResultSession();
			OperMainResultDao = TwoBillResultDaoSession.getOperMainResultDao();
			OperMainResultDao.updateOperMain(code, time);
		} catch (Exception e) {
			UIHelper.ToastMessage(context, "修改结果库失败");
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 判断操作票是否有未完成的项，能否上传
	 * 
	 * @param context
	 * @return
	 */
	public boolean operBillCanUpload(Context context) {
		boolean canUploadYN = true;
		try {
			TwoBillResultDaoSession = ((AppContext) context
					.getApplicationContext()).getTwoBillResultSession();
			OperMainResultDao = TwoBillResultDaoSession.getOperMainResultDao();
			List<OperMainResult> result = OperMainResultDao.loadAll();
			for (OperMainResult omr : result) {
				if (omr.getOperate_End_Time().trim().length() <= 0) {
					canUploadYN = false;
					break;
				}
			}
		} catch (Exception e) {
			canUploadYN = false;
			e.printStackTrace();
		}
		return canUploadYN;
	}

	// 上传操作票（转json）
	public String getOperateJson(Context context) {
		String json = "";
		try {
			TwoBillResultDaoSession = ((AppContext) context
					.getApplicationContext()).getTwoBillResultSession();
			OperMainResultDao = TwoBillResultDaoSession.getOperMainResultDao();
			OperDetailResultDao = TwoBillResultDaoSession
					.getOperDetailResultDao();
			List<OperMainResult> OperMainResult = OperMainResultDao.loadAll();
			List<OperDetailResult> OperDetailResult = OperDetailResultDao
					.loadAll();
			List<OperateUploading> uploadingList = new ArrayList<OperateUploading>();

			// 获取OperateBill文件夹下所有文件路径
			List<File> MyfileList = FileUtils.listPathFiles(AppConst
					.TwoTicketRecordPath());

			for (int i = 0; i < OperMainResult.size(); i++) {
				OperateUploading entity = new OperateUploading();
				OperMainResult entity2 = OperMainResult.get(i);
				entity.setOperate_Bill_Code(entity2.getOperate_Bill_Code());
				entity.setOperate_Begin_Time(entity2.getOperate_Begin_Time());
				entity.setOperate_End_Time(entity2.getOperate_End_Time());
				entity.setOperate_Detail_Item_Operator(entity2
						.getOperate_Detail_Item_Operator());
				entity.setOperate_Detail_Item_OperName(entity2
						.getOperate_Detail_Item_OperName());
				entity.setOperate_Detail_Item_Guardian(entity2
						.getOperate_Detail_Item_Guardian());
				entity.setOperate_Detail_Item_Watch(entity2
						.getOperate_Detail_Item_Watch());
				entity.setOperate_Detail_Item_Duty(entity2
						.getOperate_Detail_Item_Duty());
				List<OperDetailResult> list = new ArrayList<OperDetailResult>();
				for (int j = 0; j < OperDetailResult.size(); j++) {
					if (OperDetailResult.get(j).getOperate_Bill_Code()
							.equals(entity2.getOperate_Bill_Code())) {
						list.add(OperDetailResult.get(j));
					}
				}
				entity.setOperDetail(list);
				for (File f : MyfileList) {
					if (FileUtils.getFileName(f.getName()).contains(
							"OperaionBill")
							&& f.getName().contains(
									entity2.getOperate_Bill_Code())) {
						String newNmae = f.getPath();
						// 如果之前没改过名则改名（避免重复改名）
						if (!FileUtils.getFileName(f.getName()).contains(
								"_OperaionBill")) {
							newNmae = FileUtils.getPathName(f.getPath())
									+ DateTimeHelper.getDateTimeNow1() + "_"
									+ f.getName();
							FileUtils.reNamePath(f.getPath(), newNmae);
						}

						File path = new File(newNmae);
						String res = new String(FileUtils.File2Bytes(path),
								"ISO-8859-1");
						entity.setRecord(res);
						entity.setRecordName(path.getName());
					}
				}
				uploadingList.add(entity);
			}
			Gson gson = new GsonBuilder().serializeNulls()
					.disableHtmlEscaping().create();
			json = gson.toJson(uploadingList);
		} catch (Exception e) {
			e.printStackTrace();
			return "transform json error" + e;
		}
		return json;
	}
}
