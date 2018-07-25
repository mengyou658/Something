package com.moons.xst.track.common.factory.myworkfactory;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.UIHelper;

import android.content.Context;
import android.location.LocationManager;

public class GPSXXWork implements MyWork {
	
	private Context mContext;
	
	@Override
	public void enterMyWork(Context context, final DJLine djline) {
		
		mContext = context;
		AppContext.voiceConvertService.Speaking(R.string.dj_confirm_speaking
				+ djline.getLineName());
		
		if (AppContext.locationManager == null)
			AppContext.locationManager = (LocationManager) (mContext.getSystemService(Context.LOCATION_SERVICE));
		
		UIHelper.showMainMap(context, djline);
	}
}