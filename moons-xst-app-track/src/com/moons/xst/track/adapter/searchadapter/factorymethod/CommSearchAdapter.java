package com.moons.xst.track.adapter.searchadapter.factorymethod;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.bean.CommonSearchBean;

import android.content.Context;
import android.widget.BaseAdapter;

public class CommSearchAdapter {

	public static BaseAdapter getAdapterByType(Context context,
			@SuppressWarnings("rawtypes") CommonSearchBean commSearchBean) {
		try {
			SearchAdapter mAdapter = null;
			SearchAdapterFactory mFactory = null;
			// 操作票搜索
			if (commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.Operation.toString())) {
				mFactory = new OperateBillFactory();
			}
			// 检修计划搜索
			else if (commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.OverhaulPlan.toString())) {
				mFactory = new OverhaulPlanFactory();
			}
			// 检修项目搜索
			else if (commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.OverhaulProject.toString())) {
				mFactory = new OverhaulProjectFactory();
			}
			// 工作票搜索
			else if (commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.WorkBill.toString())) {
				mFactory = new WorkBillFactory();
			}
			// 数据查询路线搜索
			else if (commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.QueryDJLine.toString())) {
				mFactory = new QueryDJLineFactory();
			}
			//点检排程人员信息搜索
			else if(commSearchBean.getSearchType().equalsIgnoreCase(
					AppConst.SearchType.CommUser.toString())){
				mFactory = new QueryDJPCUserFactory();
			}
			mAdapter = mFactory.getAdapter();
			return mAdapter.get(context, commSearchBean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}