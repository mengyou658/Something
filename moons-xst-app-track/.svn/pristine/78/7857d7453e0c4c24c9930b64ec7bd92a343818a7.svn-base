/**
 * 
 */
package com.moons.xst.track.common;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.GPSPosition;

/**
 * GPS相关算法
 * 
 * @author LKZ
 * 
 */
public class GPSHelper {

	/**
	 * 计算两个坐标间距离
	 * 
	 * @param n1第一点的纬度坐标
	 * @param e1第一点的经度坐标
	 * @param n2第二点的纬度坐标
	 * @param e2第二点的经度坐标
	 * @return
	 */

	public static Integer GPSDistance(double n1, double e1, double n2, double e2) {
		try {
			LatLng latLng1 = new LatLng(n1, e1);
			LatLng latLng2 = new LatLng(n2, e2);
			double distance = DistanceUtil.getDistance(latLng1, latLng2);
			return new Double(distance).intValue();
		} catch (Exception e) {
			return null;
		}
		// double jl_jd = 102834.74258026089786013677476285;
		// double jl_wd = 111712.69150641055729984301412873;
		// double b = Math.abs((e1 - e2) * jl_jd);
		// double a = Math.abs((n1 - n2) * jl_wd);
		// return (int) Math.sqrt((a * a + b * b));
	}

	/**
	 * 计算两个时间的间隔（单位：秒）
	 * 
	 * @param BeginTime
	 * @param EndTime
	 * @return
	 */
	public static int GPSTimeSpan(String BeginTime, String EndTime) {
		int seconds = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date bDate = df.parse(BeginTime);
			Date eDate = df.parse(EndTime);
			long diff = eDate.getTime() - bDate.getTime();// 这样得到的差值是微秒级别
			seconds = (int) (diff / 1000);
		} catch (Exception e) {
		}
		return seconds;
	}

	/**
	 * 根据距当前位置由近及远排序
	 * 
	 * @param cpList
	 * @param currGpsPosition
	 */
	public static void sortCheckPoint(List<CheckPointInfo> cpList,
			GPSPosition currGpsPosition) {
		if (currGpsPosition == null || currGpsPosition.getLatitude_TX() == null) {
			for (CheckPointInfo checkPointInfo : cpList) {
				checkPointInfo.setNearDistance("");
			}
			if (AppContext.orderSortTrackCheckPointBuffer != null
					&& AppContext.orderSortTrackCheckPointBuffer.size() > 0) {
				for (CheckPointInfo checkPointInfo : AppContext.orderSortTrackCheckPointBuffer) {
					checkPointInfo.setNearDistance("");
				}
			}
		} else {
			if (cpList == null || cpList.size() <= 0
				 || StringUtils.isEmpty(currGpsPosition.getLongitude_TX())
				 || StringUtils.isEmpty(currGpsPosition.getLatitude_TX()))
				return;
			
			for (CheckPointInfo checkPointInfo : cpList) {
				checkPointInfo.setNearDistance(String.valueOf(GPSDistance(
						Double.valueOf(currGpsPosition.getLatitude_TX()),
						Double.valueOf(currGpsPosition.getLongitude_TX()),
						Double.valueOf(checkPointInfo.getLatitude()),
						Double.valueOf(checkPointInfo.getLongitude()))));
			}
			if (AppContext.orderSortTrackCheckPointBuffer != null
					&& AppContext.orderSortTrackCheckPointBuffer.size() > 0) {
				for (CheckPointInfo checkPointInfo : AppContext.orderSortTrackCheckPointBuffer) {
					checkPointInfo.setNearDistance(String.valueOf(GPSDistance(
							Double.valueOf(currGpsPosition.getLatitude_TX()),
							Double.valueOf(currGpsPosition.getLongitude_TX()),
							Double.valueOf(checkPointInfo.getLatitude()),
							Double.valueOf(checkPointInfo.getLongitude()))));
				}
			}
			Collections.sort(cpList, new Comparator<CheckPointInfo>() {
	
				@Override
				public int compare(CheckPointInfo lhs, CheckPointInfo rhs) {
					// TODO 自动生成的方法存根
					if (lhs.getNearDistance().length() > 0
							|| rhs.getNearDistance().length() > 0) {
						int a = Integer.parseInt(lhs.getNearDistance());
						int b = Integer.parseInt(rhs.getNearDistance());
						if (b > a)
							return -1;
						if (b < a)
							return 1;
					}
					return 0;
				}
			});
		}
	}

	/**
	 * 根据强制顺序排序
	 * 
	 * @param cpList
	 */
	public static void sortCheckPoint(List<CheckPointInfo> cpList) {
		if (cpList == null || cpList.size() <= 0)
			return;
		Collections.sort(cpList, new Comparator<CheckPointInfo>() {
			@Override
			public int compare(CheckPointInfo lhs, CheckPointInfo rhs) {
				// TODO 自动生成的方法存根
				int a = lhs.getSort_NR();
				int b = rhs.getSort_NR();
				if (b > a)
					return -1;
				if (b < a)
					return 1;
				return 0;
			}
		});
	}

	public static Integer notArrivedCheckPointNum(List<CheckPointInfo> cpList) {
		if (cpList == null || cpList.size() <= 0)
			return 0;
		Integer ArrivedNum = 0;
		Integer NotInitNum = 0;
		for (CheckPointInfo checkPointInfo : cpList) {
			if (StringUtils.isEmpty(checkPointInfo.getLatitude()) || checkPointInfo.getLatitude().equals("0")
					|| StringUtils.isEmpty(checkPointInfo.getLongitude()) || checkPointInfo.getLongitude().equals("0"))
				NotInitNum++;
			if (checkPointInfo.getShakeNum() > 0)
				ArrivedNum++;
		}
		return cpList.size() - NotInitNum - ArrivedNum;
	}

	/**
	 * GPS原生坐标转百度坐标
	 * 
	 * @param sourceLatLng
	 * @return
	 */
	public static LatLng convertGPSToBaidu(LatLng sourceLatLng) {
		// 将GPS设备采集的原始GPS坐标转换成百度坐标
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}

	/**
	 * 百度坐标转GPS原生坐标
	 * 
	 * @param sourceLatLng
	 * @return
	 */
	public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
		// 将GPS设备采集的原始GPS坐标转换成百度坐标
		CoordinateConverter converter = new CoordinateConverter();
		converter.from(CoordType.GPS);
		// sourceLatLng待转换坐标
		converter.coord(sourceLatLng);
		LatLng desLatLng = converter.convert();
		double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
		double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
		BigDecimal bdLatitude = new BigDecimal(latitude);
		bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
		BigDecimal bdLongitude = new BigDecimal(longitude);
		bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
		return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
	}
}
