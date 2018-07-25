package com.moons.xst.track.common.factory.myworkfactory;

import android.content.Context;

import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.UIHelper;

public class OthersWork implements MyWork {
	
	private Context mContext;
	
	@Override
	public void enterMyWork(Context context, final DJLine djline) {
		
		mContext = context;
		
		UIHelper.showMainMap(mContext, djline);
	}
}