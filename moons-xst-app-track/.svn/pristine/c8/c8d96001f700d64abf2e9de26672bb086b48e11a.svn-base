package com.moons.xst.track.adapter.searchadapter.factorymethod;

import java.util.List;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OperateAdapter;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.Operate_Detail_Bill;

import android.content.Context;
import android.widget.BaseAdapter;

public class OperateBillAdapter implements SearchAdapter {
	
	@Override
	public BaseAdapter get(Context context, @SuppressWarnings("rawtypes") CommonSearchBean commonSearchBean) {
		@SuppressWarnings("unchecked")
		BaseAdapter mAdapter = new OperateAdapter(context, commonSearchBean.getList(),
				R.layout.operate_item, (List<Operate_Detail_Bill>) commonSearchBean.getStandbyList());
		
		return mAdapter;
	}
}