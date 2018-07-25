/**
 * 
 */
package com.moons.xst.track.service;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.baidu.location.BDLocation;
import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.bean.LocationInfo;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.WifiManagerHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.tencent.mm.sdk.platformtools.Log;

/**
 * GPS相关服务
 * 
 * @author LKZ
 * 
 */
public class GpsManagerService extends Service {
	private static final String TAG = "GpsManagerService";
	Intent intent, mesIntent, updateSysDatetime;
	private Timer timerReflashGps = new Timer();
	private Timer timerReflashLoca = new Timer();
	private BDLocation currLocation;
	private Location lastLocation;
	private String gpsState;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		intent = new Intent();
		intent.setAction("com.xst.track.service.gpsSetting");
		mesIntent = new Intent();
		mesIntent.setAction("com.xst.track.service.gpsInfo");
		updateSysDatetime = new Intent();
		updateSysDatetime.setAction("com.xst.track.service.updateSysDateTime");
		registerGPS();
		ReflashGpsInfo();
		getLocationInfo();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mGpsMonitor);
		AppContext.locationManager.removeUpdates(locationListener);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		intent = new Intent();
		intent.setAction("com.xst.track.service.gpsSetting");
		mesIntent = new Intent();
		mesIntent.setAction("com.xst.track.service.gpsInfo");
		updateSysDatetime = new Intent();
		updateSysDatetime.setAction("com.xst.track.service.updateSysDateTime");
		ReflashGpsInfo();
	}

	/**
	 * 定时器（用于实时刷新数据）
	 * 
	 * @param count
	 * @param seconds
	 */
	private void ReflashGpsInfo() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					public void run() {
						AppContext.setCurrLocation(currLocation);
						AppContext.setGpsState(gpsState);
						sendGpsInfo();
					}
				}).start();
			}
		};
		timerReflashGps.schedule(task, 10000, 1000);
	}

	private final ContentObserver mGpsMonitor = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			boolean enabled = AppContext.locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!enabled) {
				startGpsSetting();
			} else {
				registerGPS();
				ReflashGpsInfo();
				getLocationInfo();
			}
		}
	};

	/**
	 * 注册GPS
	 * 
	 * @param count
	 */
	private void registerGPS() {
		if (AppContext.locationManager == null)
			AppContext.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		getContentResolver()
				.registerContentObserver(
						Settings.Secure
								.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
						false, mGpsMonitor);
		if (!AppContext.locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return;
		// 为获取地理位置信息时设置查询条件
		String bestProvider = AppContext.locationManager.getBestProvider(
				getCriteria(), true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传入的参数为LocationManager.GPS_PROVIDER
		lastLocation = AppContext.locationManager
				.getLastKnownLocation(bestProvider);
		// 监听状态
		AppContext.locationManager.addGpsStatusListener(listener);
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 1秒更新一次，或最小位移变化超过1米更新一次
		AppContext.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	private void startGpsSetting() {
		sendBroadcast(intent);
	}

	private void sendGpsInfo() {
		sendBroadcast(mesIntent);
	}

	private void sendUpdateSysTime(long sysDateTime) {
		if (sysDateTime > 0) {
			long timeSpan = sysDateTime - System.currentTimeMillis();
			if (Math.abs(timeSpan) > 1000 * 60) {
				updateSysDatetime.putExtra("sysDateTime", sysDateTime);
				sendBroadcast(updateSysDatetime);
			}
		}
	}

	String _locationInfoString = null;

	private void getLocationInfo() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				new Thread(new Runnable() {
					public void run() {
						if (NetWorkHelper.isNetworkAvailable(getBaseContext())
								&& ((AppContext.getLocationInfo() == null || StringUtils
										.isEmpty(AppContext.getLocationInfo()
												.getcityCode())) || AppContext.isGetLocationInfo)) {
							_locationInfoString = WebserviceFactory
									.getLocationInfo();
							if (!StringUtils.isEmpty(_locationInfoString)) {
								LocationInfo info = new LocationInfo();
								DocumentBuilderFactory dbf = DocumentBuilderFactory
										.newInstance();
								try {
									StringReader stringReader = new StringReader(
											_locationInfoString);
									InputSource inputSource = new InputSource(
											stringReader);
									DocumentBuilder db = dbf
											.newDocumentBuilder();
									Document doc = db.parse(inputSource);
									doc.normalize();
									NodeList tableList = doc
											.getElementsByTagName("addressComponent");
									for (int i = 0; i < tableList.getLength(); i++) {
										Node table = tableList.item(i);
										Element elem = (Element) table;
										String cityName = "";
										String countryName = "";
										String districtName = "";
										String provinceName = "";
										String streetName = "";
										for (Node node = table.getFirstChild(); node != null; node = node
												.getNextSibling()) {
											if (node.getNodeType() == Node.ELEMENT_NODE) {
												if (node.getNodeName()
														.toUpperCase()
														.equals("CITY")) {
													cityName = String.valueOf(node
															.getTextContent());
												} else if (node.getNodeName()
														.toUpperCase()
														.equals("COUNTRY")) {
													countryName = node
															.getTextContent();
												} else if (node.getNodeName()
														.toUpperCase()
														.equals("DISTRICT")) {
													districtName = String.valueOf(node
															.getTextContent());
												} else if (node.getNodeName()
														.toUpperCase()
														.equals("PROVINCE")) {
													provinceName = String.valueOf(node
															.getTextContent());
												} else if (node.getNodeName()
														.toUpperCase()
														.equals("STREET")) {
													streetName = String.valueOf(node
															.getTextContent());
												}
											}
										}

										info.setcity(cityName);
										info.setcountry(countryName);
										info.setdistrict(districtName);
										info.setprovince(provinceName);
										info.setstreet(streetName);
									}
									tableList = doc
											.getElementsByTagName("result");
									for (int i = 0; i < tableList.getLength(); i++) {
										Node table = tableList.item(i);
										Element elem = (Element) table;
										String cityCode = "";
										for (Node node = table.getFirstChild(); node != null; node = node
												.getNextSibling()) {
											if (node.getNodeType() == Node.ELEMENT_NODE) {
												if (node.getNodeName()
														.toUpperCase()
														.equals("CITYCODE")) {
													cityCode = String.valueOf(node
															.getTextContent());
												}
											}
										}
										info.setcityCode(cityCode);
										Log.e("citicode", cityCode);
									}
								} catch (Exception e) {
								}
								AppContext.setLocationInfo(info);
								AppConfig.getAppConfig(getBaseContext()).set(
										AppConfig.CONF_LOCATIONINFO,
										info.getcityCode() + ";"
												+ info.getcity());
								AppContext.isGetLocationInfo = false;
							}
						}
					}
				}).start();
			}
		};
		timerReflashLoca.schedule(task, 1000, 1000 * 10);
	}

	/**
	 * GPS位置监听
	 */
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			try {
//				currLocation = location;
				gpsState = "定位成功，精度：" + location.getAccuracy();
				if (currLocation != null
						&& (!StringUtils.isEmpty(AppContext
								.getUpdateSysDateType()))
						&& AppContext.getUpdateSysDateType().equals("GPS校准")
						&& AppContext.getUpdateSysDateYN()) {
//					sendUpdateSysTime(currLocation.getTime());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				gpsState = "可见状态";
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				gpsState = "服务区外";
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				gpsState = "暂停服务";
				break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			lastLocation = AppContext.locationManager
					.getLastKnownLocation(provider);
			gpsState = "开启GPS";
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
			gpsState = "禁用GPS";
		}
	};

	// 状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			// 第一次定位
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				gpsState = "首次定位";
				break;
			// 卫星状态改变
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				// 获取当前状态
				GpsStatus gpsStatus = AppContext.locationManager
						.getGpsStatus(null);
				// 获取卫星颗数的默认最大值
				int maxSatellites = gpsStatus.getMaxSatellites();
				// 创建一个迭代器保存所有卫星
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				gpsState = "" + count + "颗卫星";
				break;
			// 定位启动
			case GpsStatus.GPS_EVENT_STARTED:
				gpsState = "定位中";
				break;
			// 定位结束
			case GpsStatus.GPS_EVENT_STOPPED:
				gpsState = "定位结束";
				break;
			}
		};
	};

	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(true);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(true);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	/**
	 * 关闭
	 */
	public synchronized void stopMySelf() {
		stopSelf();
	}
}
