package com.moons.xst.track.adapter.searchadapter.factorymethod;

import android.content.Context;
import android.widget.BaseAdapter;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.CommonSearchBean;

public class QueryDJLineAdapter implements SearchAdapter {
	
	@Override
	public BaseAdapter get(Context context, @SuppressWarnings("rawtypes") CommonSearchBean commonSearchBean) {
		@SuppressWarnings("unchecked")
		BaseAdapter mAdapter = new DJLine_ListViewAdapter(
				context, 
				commonSearchBean.getList(),
				R.layout.listitem_djline);
		
		return mAdapter;
	}
}