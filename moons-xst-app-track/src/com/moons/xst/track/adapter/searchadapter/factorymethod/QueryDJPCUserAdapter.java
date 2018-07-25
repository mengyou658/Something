package com.moons.xst.track.adapter.searchadapter.factorymethod;

import android.content.Context;
import android.widget.BaseAdapter;

import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.adapter.DJPCUserAdapter;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.ui.CommDJPCDownload;

public class QueryDJPCUserAdapter implements SearchAdapter{

	@Override
	public BaseAdapter get(Context context, CommonSearchBean commonSearchBean) {
		BaseAdapter mAdapter =new DJPCUserAdapter(context, commonSearchBean.getList(),
				R.layout.djpc_user_item);
		return mAdapter;
	}

}
