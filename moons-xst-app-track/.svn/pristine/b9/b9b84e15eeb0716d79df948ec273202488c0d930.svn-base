package com.moons.xst.track.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.GPSHelper;
import com.tencent.mm.sdk.platformtools.Log;

public class BDGpsManagerService extends Service {
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;

	private static final String TAG = "BDGpsManagerService";
	Intent updateSysDatetime, updataCurrentLoction;
	// private Timer timercheckNetAndGps = new Timer();
	private static BDLocation currLocation = null;
	public BDLocationListener myListener = new MyLocationListener();
	private static AppContext appContext;
	private LocationClient locationClient;
	private LocationClientOption option;
	private static int mSaveMode = 0;

	private static boolean netWorkStaus = false;// 网络状态
	private static boolean gpsStaus = false;// gps状态

	private static boolean flag = false;
	private static BDLocation lastGpsLocation = null;
	private Location lastLocation;
	private int mSaveTime;
	Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		updateSysDatetime = new Intent();
		updateSysDatetime.setAction("com.xst.track.service.updateSysDateTime");
		updataCurrentLoction = new Intent();
		updataCurrentLoction
				.setAction("com.xst.track.service.updataCurrentLoction");
		appContext = (AppContext) getApplication();
		registerGPS();
		initLocation();
		checkNetAndGps();
		locationClient = AppContext.bdLocationClient;
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(myListener);
		locationClient.start();
		locationClient.requestLocation();
		mSaveMode = appContext.getGPSSavedMode();
		mSaveTime = appContext.getGPSSavedTimeSpase();

		/*
		 * // 创建PowerManager对象 pm = (PowerManager)
		 * getSystemService(Context.POWER_SERVICE); // 保持cpu一直运行，不管屏幕是否黑屏
		 * wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
		 * "CPUKeepRunning"); wakeLock.acquire();
		 * System.out.println("aaaaopen lock"); Log.i(TAG, "open lock");
		 */
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (mSaveMode != appContext.getGPSSavedMode()
				|| mSaveTime != appContext.getGPSSavedTimeSpase()) {
			flag = true;
			locationClient.unRegisterLocationListener(myListener);
			locationClient.stop();
			initLocation();
			locationClient.setLocOption(option);
			locationClient.registerLocationListener(myListener);
			locationClient.start();
			locationClient.requestLocation();
			mSaveMode = appContext.getGPSSavedMode();
			mSaveTime = appContext.getGPSSavedTimeSpase();
		}
		flag = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppContext.LocationList = null;

		/*
		 * if (wakeLock != null) wakeLock.release(); wakeLock = null; Log.i(TAG,
		 * "close lock"); System.out.println("aaaclose lock");
		 */
		locationClient.unRegisterLocationListener(myListener);
		locationClient.stop();
	}

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

	private final ContentObserver mGpsMonitor = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			boolean enabled = AppContext.locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!enabled) {
			} else {
				registerGPS();
			}
		}
	};

	/**
	 * GPS位置监听
	 */
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			try {
				if (location != null) {
					/*
					 * if ((!StringUtils
					 * .isEmpty(AppContext.getUpdateSysDateType())) &&
					 * AppContext.getUpdateSysDateType()
					 * .equals(AppConst.UpdateSysDate_GPS) &&
					 * AppContext.getUpdateSysDateYN()) {
					 * sendUpdateSysTime(location.getTime()); }
					 */
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			lastLocation = AppContext.locationManager
					.getLastKnownLocation(provider);
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
		}
	};

	private void initLocation() {
		option = new LocationClientOption();
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int gpsSavedTimeSpase = appContext.getGPSSavedTimeSpase();
		int span = 1000;
		int lastSpan = gpsSavedTimeSpase * span;
		option.setScanSpan(lastSpan);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		int gpsSavedMode = appContext.getGPSSavedMode();
		switch (gpsSavedMode) {
		case 0:
			option.setLocationMode(LocationMode.Hight_Accuracy);
			break;
		case 1:
			option.setLocationMode(LocationMode.Battery_Saving);
			break;
		case 2:
			option.setLocationMode(LocationMode.Device_Sensors);
			break;
		default:
			break;
		}
		option.setPriority(LocationClientOption.GpsFirst);// 设置GPS优先
		option.setIsNeedAddress(false);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.disableCache(true);// 不使用定位缓存
	}

	Runnable timercheckNetAndGps = new Runnable() {
		@Override
		public void run() {
			// 检测网络状态
			checkNetIsAnable();
			// 检测GPS开关状态
			checkGPSIsAnable();
			// checkedEnadle();
			handler.postDelayed(timercheckNetAndGps, 1000);
		}
	};

	/**
	 * 定时器（用于实时监测网络状态和GPS的状态）
	 */
	private void checkNetAndGps() {
		handler.postDelayed(timercheckNetAndGps, 100);
	}

	/**
	 * 检测网络状态
	 * 
	 */
	private static void checkNetIsAnable() {
		ConnectivityManager connectivity = (ConnectivityManager) appContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			netWorkStaus = false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				netWorkStaus = true;
			} else {
				netWorkStaus = false;
			}
		}

	}

	/**
	 * 检测GPS开关状态 注：后面加入信噪比，检测GPS的信号强弱
	 * 
	 */
	private static void checkGPSIsAnable() {
		LocationManager lManager = (LocationManager) appContext
				.getSystemService(LOCATION_SERVICE);
		boolean gpsAnable = lManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (gpsAnable) {
			gpsStaus = true;
		} else {
			gpsStaus = false;
		}

	}

	/**
	 * 百度定位结果回调函数
	 * 
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 高精度定位时，只取网络定位的值和GPS定位的值，其他方式定位的值去除
			if (mSaveMode == 0) {
				if ((location.getLocType() == BDLocation.TypeNetWorkLocation && netWorkStaus)
						|| (location.getLocType() == BDLocation.TypeGpsLocation && gpsStaus)) {
					currLocation = location;
				} else {
					AppContext.setCurrLocation(null);
					sendBroadcast(updataCurrentLoction);
					return;
				}
			}
			// 低功耗定位时，只取网络定位的值，其他方式定位的值去除
			else if (mSaveMode == 1) {
				if (location.getLocType() == BDLocation.TypeNetWorkLocation
						&& netWorkStaus) {
					currLocation = location;
				} else {
					AppContext.setCurrLocation(null);
					sendBroadcast(updataCurrentLoction);
					return;
				}
			}
			// 仅设备定位时，只取GPS定位的值，网络定位的值去除
			else if (mSaveMode == 2) {
				if (location.getLocType() == BDLocation.TypeGpsLocation
						&& gpsStaus) {
					currLocation = location;
				} else {
					AppContext.setCurrLocation(null);
					sendBroadcast(updataCurrentLoction);
					return;
				}
			}

			if (lastGpsLocation != null) {
				boolean checkLocation = checkLocation(currLocation);
				if (checkLocation == true) {
					AppContext.setCurrLocation(currLocation);
				} else {
					AppContext.setCurrLocation(null);
				}
			}
			lastGpsLocation = currLocation;
			sendBroadcast(updataCurrentLoction);
			/*
			 * if (currLocation != null) { if
			 * ((!StringUtils.isEmpty(AppContext.getUpdateSysDateType())) &&
			 * AppContext.getUpdateSysDateType().equals("GPS校准") &&
			 * AppContext.getUpdateSysDateYN()) { String gpsTime =
			 * currLocation.getTime(); if (!TextUtils.isEmpty(gpsTime) &&
			 * currLocation.getLocType() == BDLocation.TypeGpsLocation) { long
			 * formatToLong = formatToLong(gpsTime);
			 * sendUpdateSysTime(formatToLong); } } }
			 */
		}
	}

	// 检测定位的距离和时间间隔，判断定位的location是否可用
	private static boolean checkLocation(BDLocation location) {
		try {
			if (lastGpsLocation == null || location == null) {
				return false;
			}
			Integer _distance = 0;
			_distance = GPSHelper.GPSDistance(
					Double.valueOf(lastGpsLocation.getLatitude()),
					Double.valueOf(lastGpsLocation.getLongitude()),
					Double.valueOf(location.getLatitude()),
					Double.valueOf(location.getLongitude()));
			if(_distance==null){
				return false;
			}
			String lastGpsLocationTime = lastGpsLocation.getTime();
			String currGpsLocationTime = location.getTime();
			int days = (int) (DateTimeHelper.getDistanceTimes(
					lastGpsLocationTime, currGpsLocationTime)[0]);
			int hours = (int) (DateTimeHelper.getDistanceTimes(
					lastGpsLocationTime, currGpsLocationTime)[1]);
			int mins = (int) (DateTimeHelper.getDistanceTimes(
					lastGpsLocationTime, currGpsLocationTime)[2]);
			int seconds = (int) (DateTimeHelper.getDistanceTimes(
					lastGpsLocationTime, currGpsLocationTime)[3]);
			int getCostSeconds = days * 24 * 60 * 60 + hours * 60 * 60 + mins
					* 60 + seconds;
			int LocationCheckedTimeSpase = AppContext
					.getOnLocationCheckedTimeSpase() * 60;// 默认时间间隔为1分钟
			int LocationCheckedDistanceSpase = AppContext
					.getOnLocationCheckedDistanceSpase();// 默认距离间隔为3公里
			if (_distance >= Double.valueOf(LocationCheckedDistanceSpase)
					&& getCostSeconds <= LocationCheckedTimeSpase) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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

	/**
	 * 关闭
	 */
	public synchronized void stopMySelf() {
		stopSelf();
	}

}
