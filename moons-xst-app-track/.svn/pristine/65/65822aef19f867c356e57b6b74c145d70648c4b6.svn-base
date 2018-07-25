package com.moons.xst.track.common.factory.mapfactory;

import com.moons.xst.track.AppConst;

public class MapFactory{
	public static Map getMap(String maptype) throws 
	InstantiationException, IllegalAccessException {
		//百度地图
		if (maptype.equalsIgnoreCase(AppConst.MapType_Baidu)){
			return BaiduMap.class.newInstance();
		}
		else
			return null;
	}
}