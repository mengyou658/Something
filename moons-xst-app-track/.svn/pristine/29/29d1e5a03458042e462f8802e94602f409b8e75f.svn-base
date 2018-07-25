package com.moons.xst.track.common.factory.mapfactory;

import android.content.Context;
import android.content.Intent;

import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.ui.Map_main;

public class BaiduMap implements Map{
	@Override
	public void ChangeUI(Context context, final DJLine djline){
		
		Intent intent = new Intent(context,
				Map_main.class);
		intent.putExtra("lineinfo", djline);
		context.startActivity(intent);
	}
}