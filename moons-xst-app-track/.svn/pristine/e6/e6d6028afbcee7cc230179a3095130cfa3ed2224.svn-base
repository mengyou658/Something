package com.moons.xst.track.adapter.searchadapter.factorymethod;

import android.content.Context;
import android.widget.BaseAdapter;

import com.moons.xst.track.R;
import com.moons.xst.track.bean.CommonSearchBean;

public class WorkBillAdapter implements SearchAdapter {

	@Override
	public BaseAdapter get(Context context, CommonSearchBean commonSearchBean) {
		BaseAdapter mAdapter = new com.moons.xst.track.adapter.WorkBillAdapter(
				context, commonSearchBean.getList(), R.layout.operate_item);
		return mAdapter;
	}

}
