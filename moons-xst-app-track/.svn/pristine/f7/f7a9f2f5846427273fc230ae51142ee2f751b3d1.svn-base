package com.moons.xst.track.adapter.searchadapter.factorymethod;

import android.content.Context;
import android.widget.BaseAdapter;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OverhaulProjectAdapter;
import com.moons.xst.track.bean.CommonSearchBean;

public class OverhaulProject_Adapter implements SearchAdapter {
	
	@Override
	public BaseAdapter get(Context context, @SuppressWarnings("rawtypes") CommonSearchBean commonSearchBean) {
		@SuppressWarnings("unchecked")
		BaseAdapter mAdapter = new OverhaulProjectAdapter(
				context, commonSearchBean.getList(),
				R.layout.overhaul_project_list_item);
		
		return mAdapter;
	}
}