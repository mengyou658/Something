package com.moons.xst.buss;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.UnCheckDJPlanInfo;
import com.moons.xst.track.common.DateTimeHelper;

public class UnCheckQueryHelper {
	
	private static final String TAG = "UnCheckQueryHelper";
	private final static String STATE_FINISHED= "UNCHECKFINISHED";
	
	static UnCheckQueryHelper _intance;

	public static UnCheckQueryHelper GetIntance() {
		if (_intance == null) {
			_intance = new UnCheckQueryHelper();
		}
		return _intance;
	}

	private DJDAOSession djdaoSession;
	
	public boolean loadUnCheckData(Context context, String queryTime) {
		try {
			Intent mIntent = new Intent();
			List<UnCheckDJPlanInfo> list = new ArrayList<UnCheckDJPlanInfo>();
			list = getUnCheckPlanLists(context, queryTime);
			if (list == null)
				return true;
			AppContext.UnCheckPlanBuffer.clear();
			for (UnCheckDJPlanInfo info : list) {
				DJPlanForUnCheck plan = new DJPlanForUnCheck();
				plan.setDJPlan(info);
				Cycle cycle = GetDJCycleByID(info.getCycle_ID()
						.toString());
				plan.SetCycle(cycle);
				DJ_ControlPoint srpoint = DJPlanHelper.GetIntance().getSRbyID(
						context, plan.getDJPlan().getSRPoint_ID());
				plan.SetSrPoint(srpoint);
				
				Date date = DateTimeHelper.StringToDate(queryTime);
				if (!plan.JudgeCureDataIsInCycle(date)) {
					plan.setUnCheckYN(false);
					continue;
				}
				if (plan.JudgePlanIsCompleted(date)) {
					plan.setUnCheckYN(false);
					continue;
				}
				if (!plan.JudgePlanBySrPoint()) {
					plan.setUnCheckYN(false);
					continue;
				}
				
				plan.setUnCheckYN(true);
				if (!AppContext.UnCheckPlanBuffer.containsKey(info.getPlace_TX())) {
					AppContext.UnCheckPlanBuffer.put(info.getPlace_TX(),
							new ArrayList<DJPlanForUnCheck>());
				}
				AppContext.UnCheckPlanBuffer.get(info.getPlace_TX()).add(plan);
			}
			
			mIntent.setAction(STATE_FINISHED); 
			context.sendBroadcast(mIntent);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public boolean loadUnCheckDataForDJPC(Context context) {
		try {
			Intent mIntent = new Intent();
			List<UnCheckDJPlanInfo> list = new ArrayList<UnCheckDJPlanInfo>();
			list = getUnCheckPlanListsForDJPC(context);
			if (list == null)
				return true;
			AppContext.UnCheckPlanBuffer.clear();
			for (UnCheckDJPlanInfo info : list) {
				DJPlanForUnCheck plan = new DJPlanForUnCheck();
				plan.setDJPlan(info);
				plan.setUnCheckYN(true);
				if (!AppContext.UnCheckPlanBuffer.containsKey(info.getPlace_TX())) {
					AppContext.UnCheckPlanBuffer.put(info.getPlace_TX(),
							new ArrayList<DJPlanForUnCheck>());
				}
				AppContext.UnCheckPlanBuffer.get(info.getPlace_TX()).add(plan);
			}
			
			mIntent.setAction(STATE_FINISHED); 
			context.sendBroadcast(mIntent);
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
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
	 * 获取点检计划(漏检查询用)
	 * @param context
	 * @param queryTime
	 * @return
	 */
	private List<UnCheckDJPlanInfo> getUnCheckPlanLists(Context context, String queryTime) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			List<UnCheckDJPlanInfo> res = new ArrayList<UnCheckDJPlanInfo>();
			String sql = "Select TBN.* ,Name_TX From (SELECT P.DJ_Plan_ID,P.PlanDesc_TX,P.SortNo_NR,"
					 + "P.ESTStandard_TX, P.DataType_CD, P.Cycle_ID, P.IDPos_ID,P.LastComplete_DT,"
					 + "P.MustCheck_YN,P.SpecCase_YN, P.SpecCase_TX,P.CheckMethod, P.SRPoint_ID,P.MObjectState_TX,"
					 + "I.Place_TX "
					 + "FROM Z_DJ_Plan P, DJ_IDPos I "
					 + "WHERE P.IDPos_ID = I.IDPos_ID And P.Cycle_ID > 0 "
					 + "AND ((P.DisStart_TM IS NULL) OR ('"
					 + queryTime 
					 + "' < P.DisStart_TM) OR ( '"
					 + queryTime + "' > P.DisEnd_TM))) TBN "
					 + "left join Z_DJ_Cycle C on TBN.Cycle_ID = C.Cycle_ID Where 1=1";
			Cursor c = ((AppContext) context.getApplicationContext()).DJdb.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				while (c.moveToNext()) {
					UnCheckDJPlanInfo uncheckPlan = new UnCheckDJPlanInfo();
					uncheckPlan.setDJ_Plan_ID(c.getString(c.getColumnIndex("DJ_Plan_ID")));
				    uncheckPlan.setPlanDesc_TX(c.getString(c.getColumnIndex("PlanDesc_TX")));
				    uncheckPlan.setSortNo_NR(c.getInt(c.getColumnIndex("SortNo_NR")));
				    uncheckPlan.setESTStandard_TX(c.getString(c.getColumnIndex("ESTStandard_TX")));
				    uncheckPlan.setDataType_CD(c.getString(c.getColumnIndex("DataType_CD")));
				    uncheckPlan.setCycle_ID(c.getInt(c.getColumnIndex("Cycle_ID")));
				    uncheckPlan.setIDPos_ID(c.getString(c.getColumnIndex("IDPos_ID")));
				    uncheckPlan.setLastComplete_DT(c.getString(c.getColumnIndex("LastComplete_DT")));
				    uncheckPlan.setMustCheck_YN(c.getString(c.getColumnIndex("MustCheck_YN")));
				    uncheckPlan.setSpecCase_YN(c.getString(c.getColumnIndex("SpecCase_YN")));
				    uncheckPlan.setSpecCase_TX(c.getString(c.getColumnIndex("SpecCase_TX")));
				    uncheckPlan.setCheckMethod(c.getInt(c.getColumnIndex("CheckMethod")));
				    uncheckPlan.setSRPoint_ID(c.getString(c.getColumnIndex("SRPoint_ID")));
				    uncheckPlan.setMObjectState_TX(c.getString(c.getColumnIndex("MObjectState_TX")));
				    uncheckPlan.setPlace_TX(c.getString(c.getColumnIndex("Place_TX")));
				    uncheckPlan.setName_TX(c.getString(c.getColumnIndex("Name_TX")));
					res.add(uncheckPlan);
				}
				c.close();
			}
//			((AppContext) context.getApplicationContext()).DJdb.close();
			return res;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取点检计划（点检排程漏检查询用)
	 * @param context
	 * @param queryTime
	 * @return
	 */
	private List<UnCheckDJPlanInfo> getUnCheckPlanListsForDJPC(Context context) {
		try {
			djdaoSession = ((AppContext) context.getApplicationContext()).getDJSession(
					AppContext.GetCurrLineID());
			List<UnCheckDJPlanInfo> res = new ArrayList<UnCheckDJPlanInfo>();
			
			String sql = "Select TBN.* From (SELECT P.DJ_Plan_ID,P.SortNo_NR,"
                    + " P.PlanDesc_TX, P.ESTStandard_TX, P.DataType_CD, P.Cycle_ID, P.IDPos_ID,P.LastComplete_DT, "
                    + " P.MustCheck_YN,P.SpecCase_YN, P.SpecCase_TX,P.CheckMethod, I.Place_TX, "
                    + " Case DataType_CD When 'GC' Then '观察类' When 'JL' Then '记录类' When 'CZ' Then '测振类' When 'CW' Then '测温类' When 'CS' Then '测速类' When 'WC' Then '温差类' End As DataTypeName, P.SRPoint_ID,P.MObjectState_TX, "
                    + " Case SpecCase_YN When '1' Then '是' When '0' Then '否' End As SpecCaseYN, "
                    + " Case MustCheck_YN WHEN '0' THEN '×' WHEN '1' THEN '√' END As MustCheckYN "
                    + " FROM Z_DJ_Plan P, DJ_IDPos I"
                    + " WHERE P.IDPos_ID = I.IDPos_ID And P.LastComplete_DT IS NULL"
                    + " ) TBN where 1=1 ";
			
			Cursor c = ((AppContext) context.getApplicationContext()).DJdb.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				while (c.moveToNext()) {
					UnCheckDJPlanInfo uncheckPlan = new UnCheckDJPlanInfo();
					uncheckPlan.setDJ_Plan_ID(c.getString(c.getColumnIndex("DJ_Plan_ID")));
				    uncheckPlan.setPlanDesc_TX(c.getString(c.getColumnIndex("PlanDesc_TX")));
				    uncheckPlan.setSortNo_NR(c.getInt(c.getColumnIndex("SortNo_NR")));
				    uncheckPlan.setESTStandard_TX(c.getString(c.getColumnIndex("ESTStandard_TX")));
				    uncheckPlan.setDataType_CD(c.getString(c.getColumnIndex("DataType_CD")));
//				    uncheckPlan.setCycle_ID(c.getColumnIndex("Cycle_ID"));
				    uncheckPlan.setIDPos_ID(c.getString(c.getColumnIndex("IDPos_ID")));
				    uncheckPlan.setLastComplete_DT(c.getString(c.getColumnIndex("LastComplete_DT")));
				    uncheckPlan.setMustCheck_YN(c.getString(c.getColumnIndex("MustCheck_YN")));
				    uncheckPlan.setSpecCase_YN(c.getString(c.getColumnIndex("SpecCase_YN")));
				    uncheckPlan.setSpecCase_TX(c.getString(c.getColumnIndex("SpecCase_TX")));
				    uncheckPlan.setCheckMethod(c.getInt(c.getColumnIndex("CheckMethod")));
				    uncheckPlan.setSRPoint_ID(c.getString(c.getColumnIndex("SRPoint_ID")));
				    uncheckPlan.setMObjectState_TX(c.getString(c.getColumnIndex("MObjectState_TX")));
				    uncheckPlan.setPlace_TX(c.getString(c.getColumnIndex("Place_TX")));
//				    uncheckPlan.setName_TX(c.getString(c.getColumnIndex("Name_TX")));
					res.add(uncheckPlan);
				}
				c.close();
			}
			//((AppContext) context.getApplicationContext()).DJdb.close();
			//djdaoSession = null;
			return res;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
}