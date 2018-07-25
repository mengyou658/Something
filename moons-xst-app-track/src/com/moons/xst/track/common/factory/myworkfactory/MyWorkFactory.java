package com.moons.xst.track.common.factory.myworkfactory;

import com.moons.xst.buss.XSTMethodByLineTypeHelper;


public class MyWorkFactory{
	public static MyWork getMyWrok(int worktype) throws 
	InstantiationException, IllegalAccessException {
		MyWork res;
		switch (worktype){
		/* 巡点检 */
		case 0 :
			res = XDJWork.class.newInstance();
			break;
		/* 点检排程 */
		case 1:
			res = DJPCWork.class.newInstance();
			break;
		/* GPS巡线 */
		case 6 :
			res = GPSXXWork.class.newInstance();
			break;
		/* 新GPS巡线 */
		case 7 :
			res = GPSXXWork.class.newInstance();
			break;
		/* 条件巡检 */
		case 8 :
			res = CASEXJWork.class.newInstance();
			break;
		default :
			res = OthersWork.class.newInstance();
			break;
		}
		
		XSTMethodByLineTypeHelper.getInstance();
		
		return res;
	}
}