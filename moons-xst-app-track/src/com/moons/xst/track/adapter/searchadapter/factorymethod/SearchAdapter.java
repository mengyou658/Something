package com.moons.xst.track.adapter.searchadapter.factorymethod;

import com.moons.xst.track.bean.CommonSearchBean;

import android.content.Context;
import android.widget.BaseAdapter;

public interface SearchAdapter {
	
	public BaseAdapter get(Context context,
			@SuppressWarnings("rawtypes") CommonSearchBean commonSearchBean); 
}