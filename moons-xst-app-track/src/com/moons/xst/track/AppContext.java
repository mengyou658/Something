package com.moons.xst.track;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CacheManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.iflytek.cloud.SpeechUtility;
import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJPlanForUnCheck;
import com.moons.xst.buss.InitDJsqlite;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.sqlite.ComDaoMaster;
import com.moons.xst.sqlite.ComDaoSession;
import com.moons.xst.sqlite.DJDAOMaster;
import com.moons.xst.sqlite.DJDAOMaster.DevOpenHelper;
import com.moons.xst.sqlite.DJDAOSession;
import com.moons.xst.sqlite.DJResultDAOMaster;
import com.moons.xst.sqlite.DJResultDAOSession;
import com.moons.xst.sqlite.OverhaulDaoMaster;
import com.moons.xst.sqlite.OverhaulDaoSession;
import com.moons.xst.sqlite.TempMeasureDaoMaster;
import com.moons.xst.sqlite.TempMeasureDaoSession;
import com.moons.xst.sqlite.TwoBillMaster;
import com.moons.xst.sqlite.TwoBillResultDaoMaster;
import com.moons.xst.sqlite.TwoBillResultDaoSession;
import com.moons.xst.sqlite.TwoBillSession;
import com.moons.xst.sqlite.WorkBillMaster;
import com.moons.xst.sqlite.WorkBillSession;
import com.moons.xst.sqlite.WorkResultMaster;
import com.moons.xst.sqlite.WorkResultSession;
import com.moons.xst.sqlite.XJHisDataBaseMaster;
import com.moons.xst.sqlite.XJHisDataBaseSession;
import com.moons.xst.track.api.ApiClient;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.GPSXXPlan;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.bean.HomeMessageInfo;
import com.moons.xst.track.bean.LocationInfo;
import com.moons.xst.track.bean.MobjectBugCodeInfo;
import com.moons.xst.track.bean.OperateBillBaseInfo;
import com.moons.xst.track.bean.OverhaulPlan;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.bean.SecondMenuInfo;
import com.moons.xst.track.bean.ToDoThings;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.bean.Z_AlmLevel;
import com.moons.xst.track.bean.Z_Condition;
import com.moons.xst.track.bean.Z_DataCodeGroup;
import com.moons.xst.track.bean.Z_MObjectState;
import com.moons.xst.track.bean.Z_SpecCase;
import com.moons.xst.track.bean.Z_WorkDate;
import com.moons.xst.track.common.CyptoUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.MethodsCompat;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.service.CalculateCycleService;
import com.moons.xst.track.service.VoiceConvertService;
import com.moons.xst.track.ui.Main_Page;
import com.moons.xst.track.widget.AlertDialog;

import de.greenrobot.event.EventBus;

/**
 * 全局应用程序类
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class AppContext extends Application {
	/**
	 * 调试模式
	 */
	public static boolean debug = true;

	/**
	 * 巡线-0; 巡点检-1; 综合-2;小神探在线系统(平板上横屏运行)-3;
	 */
	public static Integer AppName = 1;

	/**
	 * 是否执行模块测试
	 */
	public static boolean ModTestYN = false;
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	private static boolean login = false; // 登录状态
	private int loginUid = 0; // 登录用户的id

	public static boolean deleteHisData = false;// 设置是否删除历史数据（纽扣页面置为true）
	/**
	 * 测温是否可用
	 */
	private static boolean temperatureYN = false;

	public static boolean getTemperatureUseYN() {
		return temperatureYN;
	}

	public static void setTemperatureUseYN(boolean b) {
		temperatureYN = b;
	}
	
	/**
	 * 获取Android版本
	 * @return
	 */
	public static int getOSAPIVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static String getOSVersion() {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getAppVersion(Context contetex) {

		try {
			PackageInfo info = contetex.getPackageManager().getPackageInfo(
					contetex.getPackageName(), 0);
			String verString = info.versionName + "." + info.versionCode;
			verString = verString.replace(".", "-");
			return verString;
		} catch (NameNotFoundException e) {
			return "";
		}

	}

	/**
	 * 巡检器标识符或无线通信方式是否更改
	 */
	private static boolean xjqCDIsAlter = false;

	public static boolean getxjqCDIsAlter() {
		return xjqCDIsAlter;
	}

	public static void setxjqCDIsAlter(boolean b) {
		xjqCDIsAlter = b;
	}

	/**
	 * 测速是否可用
	 */
	private static boolean speedYN = false;

	public static boolean getSpeedUseYN() {
		return speedYN;
	}

	public static void setSpeedUseYN(boolean b) {
		speedYN = b;
	}

	/**
	 * 对讲机是否可用
	 */
	private static boolean wakietalkieYN = false;

	public static boolean getWalkieTalkieUseYN() {
		return wakietalkieYN;
	}

	public static void setWalkieTalkieUseYN(boolean b) {
		wakietalkieYN = b;
	}

	/**
	 * RFID是否可用
	 */
	private static boolean rfidYN = false;

	public static boolean getRFIDUseYN() {
		return rfidYN;
	}

	public static void setRFIDUseYN(boolean b) {
		rfidYN = b;
	}

	public static List<BDLocation> LocationList = null;
	/**
	 * 测振是否可用
	 */
	private static boolean vibrationYN = false;

	public static boolean getVibrationUseYN() {
		return vibrationYN;
	}

	public static void setVibrationUseYN(boolean b) {
		vibrationYN = b;
	}

	/**
	 * 是否旋转照片
	 */
	private static boolean rotateCameraYN = false;

	public static boolean getRotateCameraYN() {
		return rotateCameraYN;
	}

	public static void setRotateCameraYN(boolean b) {
		rotateCameraYN = b;
	}

	/**
	 * 是否刷新休眠锁
	 */
	private static boolean isRefreshWakeLock = false;

	public static boolean getRefreshWakeLock() {
		return isRefreshWakeLock;
	}

	public static void setRefreshWakeLock(boolean b) {
		isRefreshWakeLock = b;
	}

	/**
	 * 登录验证方式
	 */
	public enum LoginType {
		Account, Scan, RFID
	}

	/**
	 * 一级缓存池（内存缓存）
	 */
	public static Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	// PackageInfo
	public static PackageInfo xstTrackPackageInfo = new PackageInfo();

	// 用来保存点检页面浏览图片后返回的图片路径
	public static List<String> DJImagePath = new ArrayList<String>();

	static TelephonyManager tm;

	/**
	 * 本次是新安装第一次启动吗？
	 */
	public static Boolean isNewInstall = false;
	/**
	 * 本次是升级后第一次启动吗？
	 */
	public static Boolean isNowUpdate = false;

	/**
	 * 是否需要更新本地位置信息
	 */
	public static Boolean isGetLocationInfo = true;
	/**
	 * 开机时是否已经询问是否打开GPS
	 */
	public static Boolean isAskByStartApp = false;

	/**
	 * 清理计划相关Buffer
	 */
	public static void clearDJBuffer() {
		DJPlanBuffer.clear();
		YXDJPlanByIDPosBuffer.clear();
		DJCycleBuffer.clear();
		DJIDPosBuffer.clear();
	}

	/**
	 * 路线列表
	 */
	public static List<DJLine> DJLineBuffer = Collections
			.synchronizedList(new ArrayList<DJLine>());
	/**
	 * 特巡条件
	 */
	public static Hashtable<String, ArrayList<Z_SpecCase>> DJSpecCaseBuffer = new Hashtable<String, ArrayList<Z_SpecCase>>();
	/**
	 * 特巡标识（0/普通 1/特巡）
	 */
	public static Integer DJSpecCaseFlag = 0;
	/**
	 * 选择设置的特巡条件字符串
	 */
	public static String selectedDJSpecCaseStr = "";

	/**
	 * 计划
	 */
	public static Hashtable<String, ArrayList<DJPlan>> DJPlanBuffer = new Hashtable<String, ArrayList<DJPlan>>();

	public static Hashtable<String, ArrayList<DJPlan>> YXDJPlanByIDPosBuffer = new Hashtable<String, ArrayList<DJPlan>>();

	public static Hashtable<String, ArrayList<DJPlanForUnCheck>> UnCheckPlanBuffer = new Hashtable<String, ArrayList<DJPlanForUnCheck>>();
	// /**
	// * 钮扣下有效计划列表（按碰钮扣时计算）
	// */
	// public static List<DJPlan> YXDJPlanByIDPosBuffer = Collections
	// .synchronizedList(new ArrayList<DJPlan>());
	/**
	 * 启停点列表
	 */
	public static List<DJ_ControlPoint> SRBuffer = Collections
			.synchronizedList(new ArrayList<DJ_ControlPoint>());
	/**
	 * 主控点列表
	 */
	public static List<DJ_ControlPoint> LKBuffer = Collections
			.synchronizedList(new ArrayList<DJ_ControlPoint>());
	/**
	 * 启停状态列表
	 */
	public static List<Z_MObjectState> SRStateBuffer = Collections
			.synchronizedList(new ArrayList<Z_MObjectState>());
	/**
	 * 周期
	 */
	public static List<Cycle> DJCycleBuffer = Collections
			.synchronizedList(new ArrayList<Cycle>());
	/**
	 * 钮扣
	 */
	public static List<DJ_IDPos> DJIDPosBuffer = Collections
			.synchronizedList(new ArrayList<DJ_IDPos>());
	/**
	 * 钮扣是否强制顺序
	 */
	public static boolean orderIDPosYN = false;
	/**
	 * 强制次序的钮扣
	 */
	public static List<DJ_IDPos> orderDJIDPosBuffer = Collections
			.synchronizedList(new ArrayList<DJ_IDPos>());
	/**
	 * 结果选项
	 */
	public static List<Z_DataCodeGroup> dataCodeBuffer = Collections
			.synchronizedList(new ArrayList<Z_DataCodeGroup>());
	/**
	 * 报警等级
	 */
	public static List<Z_AlmLevel> almLevelBuffer = Collections
			.synchronizedList(new ArrayList<Z_AlmLevel>());
	/**
	 * 节假日
	 */
	public static List<Z_WorkDate> workDateBuffer = Collections
			.synchronizedList(new ArrayList<Z_WorkDate>());

	/**
	 * 点检排程路线下载地址
	 */
	public static Map<String, String> DJPCLineMap = new HashMap();
	/**
	 * 条件巡检条件
	 */
	public static List<Z_Condition> conditionBuffer = Collections
			.synchronizedList(new ArrayList<Z_Condition>());
	/**
	 * 选择的待删除的照片
	 */
	public static List<String> selectedPhotoBuffer = Collections
			.synchronizedList(new ArrayList<String>());
	/**
	 * 选择的待删除的视频
	 */
	public static List<String> selectedVideoBuffer = Collections
			.synchronizedList(new ArrayList<String>());
	/**
	 * 选择的错误日志
	 */
	public static List<String> selectedBugInfoBuffer = Collections
			.synchronizedList(new ArrayList<String>());
	/**
	 * 纽扣计划
	 */
	public static List<DJ_IDPos> DJIDPosStatusDataBuffer = Collections
			.synchronizedList(new ArrayList<DJ_IDPos>());

	public static List<CheckPointInfo> CheckPointIDPosStatusDataBuffer = Collections
			.synchronizedList(new ArrayList<CheckPointInfo>());

	/**
	 * 未到位的钮扣个数（应到位，但未到位）
	 */
	public static int NeedArriveCount = 0;
	/**
	 * 钮扣总数
	 */
	public static int TotalCount = 0;
	/**
	 * 钮扣完成数
	 */
	public static int CompleteCount = 0;
	/**
	 * 当前手机型号
	 */
	private static String model = "";

	public static String getModel() {
		return model;
	}

	public static void setModel(String s) {
		model = s;
	}

	public static LocationManager locationManager;

	public static Context baseContext;

	public static CalculateCycleService calculateCycleService = null;

	public static VoiceConvertService voiceConvertService = null;
	/**
	 * 下载模块路线列表
	 */
	public static List<DJLine> CommDJLineBuffer = new ArrayList<DJLine>();
	/**
	 * 结果库文件夹列表
	 */
	public static CopyOnWriteArrayList<DJLine> resultDataFileListBuffer = new CopyOnWriteArrayList<DJLine>();
	/**
	 * GPS考核点(根据距离当前位置由近及远排序的考核点信息)
	 */
	public static List<CheckPointInfo> trackCheckPointBuffer = Collections
			.synchronizedList(new ArrayList<CheckPointInfo>());
	/**
	 * GPS坐标考核点是否强制顺序
	 */
	public static boolean orderCheckPointYN = false;
	/**
	 * GPS考核点(根据上位机设置的强制顺序排序的考核点信息)
	 */
	public static List<CheckPointInfo> orderSortTrackCheckPointBuffer = Collections
			.synchronizedList(new ArrayList<CheckPointInfo>());
	public static List<MobjectBugCodeInfo> trackMobjectBugCodeBuffer = Collections
			.synchronizedList(new ArrayList<MobjectBugCodeInfo>());
	/**
	 * 巡线计划
	 */
	public static Hashtable<String, ArrayList<GPSXXPlan>> gpsXXPlanBuffer = new Hashtable<String, ArrayList<GPSXXPlan>>();
	public static List<ToDoThings> todoDataBuffer = Collections
			.synchronizedList(new ArrayList<ToDoThings>());

	public static List<HomeMessageInfo> homeMessageBuffer = Collections
			.synchronizedList(new ArrayList<HomeMessageInfo>());

	public static List<SecondMenuInfo> secondMenuBuffer = Collections
			.synchronizedList(new ArrayList<SecondMenuInfo>());
	public static Hashtable<String, ArrayList<SecondMenuInfo>> allSecondMenuBuffer = new Hashtable<String, ArrayList<SecondMenuInfo>>();
	private Handler unLoginHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				UIHelper.ToastMessage(AppContext.this,
						getString(R.string.msg_login_error));
				UIHelper.showLoginDialog(AppContext.this);
			}
		}
	};
	/**
	 * 已下载的离线地图信息列表
	 */
	public static List<MKOLUpdateElement> localMapList = Collections
			.synchronizedList(new ArrayList<MKOLUpdateElement>());
	/**
	 * 系统级别消息提示
	 */
	public static Handler sysLevelMessageHandler;
	public static Handler sysLevelIconHandler;

	/**
	 * 当前临时任务
	 */
	public static Queue<GPSXXTempTask> nowTempTask = new ConcurrentLinkedQueue();

	/**
	 * 事件类型buffer
	 */
	public static Hashtable<String, String> eventTypeBuffer = new Hashtable<String, String>();

	/**
	 * 操作票基本信息buffer
	 */
	public static List<OperateBillBaseInfo> operateBillBaseInfoBuffer = new ArrayList<OperateBillBaseInfo>();

	/**
	 * 检修项目信息buffer
	 */
	public static List<OverhaulProject> overhaulProjectBuffer = Collections
			.synchronizedList(new ArrayList<OverhaulProject>());
	/**
	 * 检修项目信息buffer
	 */
	public static List<OverhaulProject> overhaulProjectBufferSelect = Collections
			.synchronizedList(new ArrayList<OverhaulProject>());
	/**
	 * 检修计划信息buffer
	 */
	public static List<OverhaulPlan> overhaulPlanBuffer = Collections
			.synchronizedList(new ArrayList<OverhaulPlan>());

	public static Properties globalProperties = new Properties();

	@Override
	public void onCreate() {
		super.onCreate();
		String processName = getProcessName(this);
		if (processName != null) {
			if (processName.equals("com.moons.xst.track")) {
				// 初始化com.moons.xst.track以包名为进程名，项目默认的进程
				// 注册App异常崩溃处理器
				Thread.setDefaultUncaughtExceptionHandler(AppException
						.getAppExceptionHandler());
				SpeechUtility.createUtility(AppContext.this, "appid="
						+ getString(R.string.kdxf_app_id));
				// 注册日志记录类
				OperatingConfigHelper.getInstance();
				init();
				AppContext.setModel(AppConst.GetPhoneBuildModel());
				tm = (TelephonyManager) this
						.getSystemService(TELEPHONY_SERVICE);				
				//readXSTKeyConfig();
				sysLevelMessageHandler = new Handler() {
					public void handleMessage(Message msg) {
						UIHelper.ToastMessage(AppContext.this,
								msg.obj.toString());
					}
				};
				sysLevelIconHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.obj != null) {
							if (StringUtils.isEmpty(msg.obj.toString())
									|| msg.obj.toString().toUpperCase()
											.contains("ANYTYPE{}")) {
								EventBus.getDefault().post("WEBSERVICEOK");
							} else {
								EventBus.getDefault().post("WEBSERVICEERROR");
							}
						}
					}
				};
			}
			
		}
	}

	/**
	 * 获取项目进程名，只有在进程为com.moons.xst.track时，才执行oncreate()的方法。
	 * 不加此判断，由于加入了多进程方式的SERVICE，会导致多次执行oncreate()方法
	 * 
	 * @param context
	 * @return
	 */
	private String getProcessName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am
				.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
			if (proInfo.pid == android.os.Process.myPid()) {
				if (proInfo.processName != null) {
					return proInfo.processName;
				}
			}
		}
		return null;
	}

	/**
	 * 初始化
	 */
	private void init() {
		LocationList = new ArrayList<BDLocation>();
		// 转移XSTConfig文件夹，放到XST目录下，为了解决2.0.1版本临时放在数据目录下
		String fromPath = AppConst.SDCardBasePath() + "/XSTConfig/";
		String toPath = AppConst.XSTConfigFilePath();
		File f = new File(fromPath);
		if (f.exists()) {
			if (FileUtils.copyFolder(fromPath, toPath))
				FileUtils.deleteDirectory(fromPath);
		}
		// 读取参数信息，一次读取
		initProperties();

		String locationInfoString = globalProperties
				.getProperty(AppConfig.CONF_LOCATIONINFO);
		if (!StringUtils.isEmpty(locationInfoString)
				&& locationInfoString.contains(";")) {
			LocationInfo _infoInfo = new LocationInfo();
			_infoInfo.setcityCode(locationInfoString.split(";")[0]);
			_infoInfo.setcity(locationInfoString.split(";")[1]);
			setLocationInfo(_infoInfo);
		}

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		bdLocationClient = new LocationClient(getApplicationContext());
		baseContext = getBaseContext();
		checkOS();
	}

	private void checkOS() {
		sp = this.getSharedPreferences("UpDateConfig", MODE_PRIVATE);
		boolean isFirstStart = sp.getBoolean("isFirstStart", true);
		if (isFirstStart) {
			// Android 某些型号需要重启
			String deviceModel = AppContext.getModel();
			// MS601
			String[] deviceModelArray = this.getResources().getStringArray(
					R.array.equipment_type);

			if (!Arrays.toString(deviceModelArray).contains(deviceModel)) {
				Editor editor = sp.edit();
				editor.putBoolean("isFirstStart", false);
				editor.commit();
				SDKInitializer.initialize(this);	
			}

		}else{
			SDKInitializer.initialize(this);	
		}
		
	}
	
	/**
	 * 检测当前系统声音是否为正常模式
	 * 
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	/**
	 * 应用程序是否发出提示音
	 * 
	 * @return
	 */
	public boolean isAppSound() {
		return isAudioNormal() && getVoice();
	}

	// 是否允许实时上传
	private static boolean isUploading = false;

	public static boolean getIsUploading() {
		return isUploading;
	}

	public static void setIsUploading(boolean b) {
		isUploading = b;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 用户是否登录
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return login;
	}

	/**
	 * 获取登录用户id
	 * 
	 * @return
	 */
	public int getLoginUid() {
		return this.loginUid;
	}

	/**
	 * 用户注销
	 */
	public void Logout() {
		ApiClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;
		lineListStr = "";
	}

	/**
	 * 未登录或修改密码后的处理
	 */
	public Handler getUnLoginHandler() {
		return this.unLoginHandler;
	}

	/**
	 * 初始化用户登录信息
	 */
	public void LoginInfo(boolean initYn) {
		User loginUser = getLoginInfo();
		if (initYn) {
			if (loginUser != null && loginUser.getUserID() > 0
					&& loginUser.isRememberMe()) {
				this.loginUid = loginUser.getUserID();
				this.login = true;
				setLoginUserToAppGlobal(loginUser);
			} else {
				this.Logout();
			}
		} else {
			if (loginUser != null && loginUser.getUserID() > 0) {
				this.loginUid = loginUser.getUserID();
				this.login = true;
				setLoginUserToAppGlobal(loginUser);
			} else {
				this.Logout();
			}
		}
	}

	/**
	 * 保存登录信息到AppGlobal
	 */
	private void setLoginUserToAppGlobal(User loginUser) {
		setAccount(loginUser.getUserAccount());
		setUserCD(loginUser.getUserCD());
		setUserID(loginUser.getUserID());
		setUserName(loginUser.getUserName());
		setUserPassword(loginUser.getUserPwd());
	}

	/**
	 * 用户登录验证
	 * 
	 * @param userInfo
	 * @param loginType
	 * @return
	 * @throws AppException
	 */
	public User loginVerify(User userInfo, AppContext.LoginType loginType)
			throws AppException {
		return ApiClient.login(this, userInfo, loginType);
	}

	/**
	 * 保存登录信息
	 * 
	 * @param username
	 * @param pwd
	 */
	public void saveLoginInfo(final User user) {
		this.loginUid = user.getUserID();
		this.login = true;
		setUserProperties(new Properties() {
			{
				setUserProperty("user.id", String.valueOf(user.getUserID()));
				setUserProperty("user.usercd", user.getUserCD());
				setUserProperty("user.name", user.getUserName());
				setUserProperty("user.account", user.getUserAccount());
				setUserProperty("user.pwd",
						CyptoUtils.encode("moonsXstApp", user.getUserPwd()));
				setUserProperty("user.isRememberMe",
						String.valueOf(user.isRememberMe()));// 是否记住我的信息
				setUserProperty("user.lineListStr", user.getUserLineListStr());
				setUserProperty("user.useraccess", user.getUserAccess());
			}
		});
		setLoginUserToAppGlobal(user);
	}

	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		this.loginUid = 0;
		this.login = false;
		SetlineList("");
		removeUserProperty("user.id", "user.usercd", "user.name",
				"user.account", "user.pwd", "user.isRememberMe",
				"user.lineListStr", "user.useraccess");
		Toast.makeText(AppContext.this, getString(R.string.msg_login_logout),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 清除登录不弹“已注销”框
	 */
	public void cleanLoginInfoWithoutMessage() {
		this.loginUid = 0;
		this.login = false;
		SetlineList("");
		removeUserProperty("user.id", "user.usercd", "user.name",
				"user.account", "user.pwd", "user.isRememberMe",
				"user.lineListStr", "user.useraccess");
	}

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public User getLoginInfo() {
		User lu = new User();
		lu.setUserID(StringUtils.toInt(getUserProperty("user.id"), 0));
		lu.setUserCD(getUserProperty("user.usercd"));
		lu.setUserName(getUserProperty("user.name"));
		lu.setAccount(getUserProperty("user.account"));
		lu.setPwd(CyptoUtils.decode("moonsXstApp", getUserProperty("user.pwd")));
		lu.setRememberMe(StringUtils
				.toBool(getUserProperty("user.isRememberMe")));
		lu.setUserLineListStr(getUserProperty("user.lineListStr"));
		lu.setUserAccess(getUserProperty("user.useraccess"));
		return lu;
	}

	/**
	 * 是否加载显示文章图片
	 * 
	 * @return
	 */
	public boolean isLoadImage() {
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		// 默认是加载的
		if (StringUtils.isEmpty(perf_loadimage))
			return true;
		else
			return StringUtils.toBool(perf_loadimage);
	}

	/**
	 * 设置是否加载文章图片
	 * 
	 * @param b
	 */
	public void setConfigLoadimage(boolean b) {
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}

	/**
	 * 创建Userconfig
	 * 
	 */
	public void newUserConfig() {
		Properties props = AppConfig.getAppConfig(this).get();
		if (props.containsKey("user.pwd")) {
			setUserProperty("user.pwd", getProperty("user.pwd"));
		}
		if (props.containsKey("user.useraccess")) {
			setUserProperty("user.useraccess", getProperty("user.useraccess"));
		}
		if (props.containsKey("user.name")) {
			setUserProperty("user.name", getProperty("user.name"));
		}
		if (props.containsKey("user.usercd")) {
			setUserProperty("user.usercd", getProperty("user.usercd"));
		}
		if (props.containsKey("user.account")) {
			setUserProperty("user.account", getProperty("user.account"));
		}
		if (props.containsKey("user.id")) {
			setUserProperty("user.id", getProperty("user.id"));
		}
		if (props.containsKey("user.isRememberMe")) {
			setUserProperty("user.isRememberMe",
					getProperty("user.isRememberMe"));
		}
		if (props.containsKey("user.lineListStr")) {
			setUserProperty("user.lineListStr", getProperty("user.lineListStr"));
		}
		removeProperty("user.id", "user.usercd", "user.name", "user.account",
				"user.pwd", "user.isRememberMe", "user.lineListStr",
				"user.useraccess");
	}

	/**
	 * 退出时，是否关闭WIFI
	 * 
	 * @return
	 */
	public boolean isWifiClose() {
		String pref_wificloseyn = getProperty(AppConfig.CONF_WIFICLOSEYN);
		// 默认退出时关闭
		if (StringUtils.isEmpty(pref_wificloseyn))
			return true;
		else
			return StringUtils.toBool(pref_wificloseyn);
	}

	/**
	 * 设置是否关闭WIFI
	 * 
	 * @param b
	 */
	public void setWifiClose(boolean b) {
		setProperty(AppConfig.CONF_WIFICLOSEYN, String.valueOf(b));
	}

	/**
	 * 退出时，是否关闭GPS
	 * 
	 * @return
	 */
	public boolean isGPSClose() {
		String pref_gpscloseyn = getProperty(AppConfig.CONF_GPSCLOSEYN);
		// 默认退出时关闭
		if (StringUtils.isEmpty(pref_gpscloseyn))
			return true;
		else
			return StringUtils.toBool(pref_gpscloseyn);
	}

	/**
	 * 设置是否关闭GPS
	 * 
	 * @param b
	 */
	public void setGPSClose(boolean b) {
		setProperty(AppConfig.CONF_GPSCLOSEYN, String.valueOf(b));
	}

	/**
	 * 判断是否需要碰钮扣做计划
	 * 
	 * @param b
	 */
	public boolean isDoPlanByIDPos() {
		return getConfigDoPlanByIDPos();
	}

	static boolean ConfigDoPlanByIDPos = true;

	/**
	 * 设置是否需要碰钮扣做计划
	 * 
	 * @param b
	 */
	public static void setConfigDoPlanByIDPos(boolean b) {
		ConfigDoPlanByIDPos = b;
	}

	public static boolean getConfigDoPlanByIDPos() {
		return ConfigDoPlanByIDPos;
	}

	static boolean ConfigDoPlanByScan = true;

	/**
	 * 设置是否允许扫码做计划
	 * 
	 * @param b
	 */
	public static void setConfigDoPlanByScan(boolean b) {
		ConfigDoPlanByScan = b;
	}

	public static boolean getConfigDoPlanByScan() {
		return ConfigDoPlanByScan;
	}

	static boolean ConfigLongClickDownloadLine = true;

	/**
	 * 数据同步长按路线是否允许下载
	 * 
	 * @param b
	 */
	public static void setConfigLongClickDownloadLine(boolean b) {
		ConfigLongClickDownloadLine = b;
	}

	public static boolean getConfigLongClickDownloadLine() {
		return ConfigLongClickDownloadLine;
	}

	/**
	 * 点击路线后是否检查登录
	 */
	static boolean CheckInAfterEnterLineYN = false;

	public static void setCheckInAfterEnterLine(boolean b) {
		CheckInAfterEnterLineYN = b;
	}

	public static boolean getCheckInAfterEnterLine() {
		return CheckInAfterEnterLineYN;
	}

	/**
	 * 是否允许使用左拉抽屉菜单
	 */
	static boolean DrawleftLayoutYN = false;

	public static void setDrawleftLayoutYN(boolean b) {
		DrawleftLayoutYN = b;
	}

	public static boolean getDrawleftLayoutYN() {
		return DrawleftLayoutYN;
	}

	/**
	 * 无线通信时是否检查网络
	 */
	static boolean CheckNetworkYN = false;

	public static void setCheckNetworkYN(boolean b) {
		CheckNetworkYN = b;
	}

	public static boolean getCheckNetworkYN() {
		return CheckNetworkYN;
	}

	/**
	 * 是否允许屏蔽OS时间设置
	 */
	static boolean ShieldOSSetTimeYN = false;

	public static void setShieldOSSetTimeYN(boolean b) {
		ShieldOSSetTimeYN = b;
	}

	public static boolean getShieldOSSetTimeYN() {
		return ShieldOSSetTimeYN;
	}
	
	/**
	 * 时区设置
	 */
	static String TimeZoneSetting = "GMT+08:00";
	public static void setTimeZone(String s) {
		TimeZoneSetting = s;
	}
	public static String getTimeZone() {
		return TimeZoneSetting;
	}
	
	/**
	 * 异常结果是否转出缺陷单(万华接口)
	 */
	static boolean TransExceptionYN = false;
	public static void setTransExceptionYN(boolean b) {
		TransExceptionYN = b;
	}
	public static boolean getTransException() {
		return TransExceptionYN;
	}
	
	/**
	 * 通知单类型(万华接口)
	 */
	static String NoticeType = "Z1;Z2;Z3;Z4;Z5;Z9";
	public static void setNoticeType(String s) {
		NoticeType = s;
	}
	public static String getNoticeType() {
		return NoticeType;
	}
	
	/**
	 * 清理点检排程路线方式
	 * Other：下载后清理
	 * Auto：自动清理
	 * Manual：手动清理
	 */
	static String ClearDJPCLineMode = "Other";
	
	public static void setClearDJPCLineMode(String s) {
		ClearDJPCLineMode = s;
	}
	public static String getClearDJPCLineMode() {
		return ClearDJPCLineMode;
	}

	static String UserLoginType = AppConst.AppLoginType.All.toString();

	public static void setUserLoginType(String s) {
		UserLoginType = s;
	}

	public static String getUserLoginType() {
		return UserLoginType;
	}

	public String getLoginType() {
		String perf_logintype = getProperty(AppConfig.CONF_LOGINTYPE);
		if (StringUtils.isEmpty(perf_logintype))
			/* 默认只有输入账号密码登录 */
			return "Account||";
		else
			return perf_logintype;
	}

	/**
	 * 设置登录的方式，有三种可配，账号密码方式，扫二维码方式和碰钮扣方式 格式：Account|Scan|RFID
	 * 
	 * @param loginType
	 */
	public void setConfigLoginType(String loginType) {
		setProperty(AppConfig.CONF_LOGINTYPE, loginType);
	}

	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
		removeUserProperty(AppConfig.CONF_COOKIE);
	}

	/**
	 * 清除app缓存
	 */
	public void clearAppCache() {
		// 清除webview缓存

		File file = CacheManager.getCacheFileBaseDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
		deleteDatabase("webview.db");
		deleteDatabase("webview.db-shm");
		deleteDatabase("webview.db-wal");
		deleteDatabase("webviewCache.db");
		deleteDatabase("webviewCache.db-shm");
		deleteDatabase("webviewCache.db-wal");
		// 清除数据缓存
		clearCacheFolder(getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(getCacheDir(), System.currentTimeMillis());
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),
					System.currentTimeMillis());
		}
		// 清除编辑器保存的临时内容
		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp"))
				removeProperty(_key);
		}
	}

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	/*
	 * public void setProperty(String key, String value) {
	 * AppConfig.getAppConfig(this).set(key, value); }
	 */
	public void setProperty(String key, String value) {
		globalProperties.setProperty(key, value);
		AppConfig.getAppConfig(this).setAll(globalProperties);
	}

	public String getProperty(String key) {
		return AppConfig.getAppConfig(this).get(key);
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	// Userconfig
	public boolean containsUserProperty(String key) {
		Properties props = getUserProperties();
		return props.containsKey(key);
	}

	// Userconfig
	public void setUserProperties(Properties ps) {
		AppConfig.getAppConfig(this).setUser(ps);
	}

	// Userconfig
	public Properties getUserProperties() {
		return AppConfig.getAppConfig(this).getUser();
	}

	// Userconfig
	public void setUserProperty(String key, String value) {
		AppConfig.getAppConfig(this).setUser(key, value);
	}

	// Userconfig
	public String getUserProperty(String key) {
		return AppConfig.getAppConfig(this).getUser(key);
	}

	// 清理Userconfig
	public void removeUserProperty(String... key) {
		AppConfig.getAppConfig(this).removeUser(key);
	}

	/*****************************************************
	 * 参数设置
	 ****************************************************/
	// 版本控制码
	private static String preVersionCode = "0";

	public static String getPreVersionCode() {
		return preVersionCode;
	}

	public static void setPreVersionCode(String str) {
		preVersionCode = str;
	}

	/**
	 * 设置版本控制码到配置文件
	 * 
	 * @param type
	 */
	public void setConfigPreVersionCode(String s) {
		setProperty(AppConfig.CONF_PreVersionCode, s);
		setPreVersionCode(s);
	}

	// APP语言
	private static String appLanguage = "zh";

	public static String getAppLanguage() {
		return appLanguage;
	}

	public static void setAppLanguage(String str) {
		appLanguage = str;
	}

	/**
	 * 设置APP语言到配置文件
	 * 
	 * @param type
	 */
	public void setConfigAppLanguage(String s) {
		setProperty(AppConfig.CONFIG_LANGUAGE, s);
		setAppLanguage(s);
	}

	// 通信方式
	private static String communicationType = "0";

	public static String getCommunicationType() {
		return communicationType;
	}

	public static void setCommunicationType(String type) {
		communicationType = type;
	}

	/**
	 * 设置通信类型到配置文件
	 * 
	 * @param type
	 */
	public void setConfigCommunicationType(String type) {
		setProperty(AppConfig.CONF_COMMUNICATION_TYPE, type);
		setCommunicationType(type);
	}

	// 计划类型 0巡点检，1点检排程
	private static String planType = "0";

	public static String getPlanType() {

		return planType;
	}

	public static void setPlanType(String s) {
		planType = s;
	}

	/**
	 * 设置计划类型到配置文件
	 * 
	 * @param type
	 */
	public void setConfigPlanType(String type) {
		setProperty(AppConfig.CONF_PLAN_TYPE, type);
		setPlanType(type);
	}

	// 巡检器标示符
	private static String xjqCD = "";

	public static String getxjqCD() {
		return xjqCD;
	}

	public static void setxjqCD(String xjqcd) {
		xjqCD = xjqcd;
	}

	// 设置巡检器标识符到配置文件
	public void setConfigXJQCD(String xjqcd) {
		setProperty(AppConfig.CONF_XJQCD, xjqcd);
		setxjqCD(xjqcd);
	}

	// WEBSERVICE地址
	private static String webserviceAddress = "";

	public static String getWebServiceAddress() {
		return webserviceAddress;
	}

	public static void setWebServiceAddress(String wsAddress) {
		webserviceAddress = wsAddress;
	}

	/**
	 * 设置webservice地址到配置文件
	 * 
	 * @param address
	 */
	public void setConfigWSAddress(String address) {
		setProperty(AppConfig.CONF_WSADDRESS, address);
		setWebServiceAddress(address);
	}

	// WEBSERVICE地址外网
	private static String webserviceAddressForWlan = "";

	public static String getWebServiceAddressForWlan() {
		return webserviceAddressForWlan;
	}

	public static void setWebServiceAddressForWlan(String wsAddress) {
		webserviceAddressForWlan = wsAddress;
	}

	/**
	 * 设置webservice外网地址到配置文件
	 * 
	 * @param address
	 */
	public void setConfigWSAddressForWlan(String address) {
		setProperty(AppConfig.CONF_WSADDRESSFORWLAN, address);
		setWebServiceAddressForWlan(address);
	}

	// WEB管线地址
	private static String webserviceAddressForOther = "";

	public static String getWebServiceAddressForOther() {
		return webserviceAddressForOther;
	}

	public static void setWebServiceAddressForOther(String wsAddress) {
		webserviceAddressForOther = wsAddress;
	}

	/**
	 * 设置webservice管线地址到配置文件
	 * 
	 * @param address
	 */
	public void setConfigWSAddressForOther(String address) {
		setProperty(AppConfig.CONF_WSADDRESSFOROTHER, address);
		setWebServiceAddressForOther(address);
	}

	// 语音提示
	private static boolean voice = false;

	public static boolean getVoice() {
		return voice;
	}

	public static void setVoice(boolean b) {
		voice = b;
	}

	/**
	 * 设置语音提示到配置文件
	 * 
	 * @param b
	 */
	public void setConfigVoice(boolean b) {
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
		setVoice(b);
	}

	// 默认输入上次结果
	private static boolean lastResult = false;

	public static boolean getLastResult() {
		return lastResult;
	}

	public static void setLastResult(boolean b) {
		lastResult = b;
	}

	/**
	 * 设置是否默认上次数据到配置文件
	 * 
	 * @param b
	 */
	public void setConfigLastResult(boolean b) {
		setProperty(AppConfig.CONF_LASTRESULT, String.valueOf(b));
		setLastResult(b);
	}

	// 启动检查更新
	private static boolean checkUp = true;

	public static boolean getCheckUp() {
		return checkUp;
	}

	public static void setCheckUp(boolean b) {
		checkUp = b;
	}

	/**
	 * 设置是否检查更新到配置文件
	 * 
	 * @param b
	 */
	public void setConfigCheckUp(boolean b) {
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
		setCheckUp(b);
	}

	// 启动文件后台上传
	private static boolean uploadFile = false;

	public static boolean getUploadFile() {
		return uploadFile;
	}

	public static void setUploadFile(boolean b) {
		uploadFile = b;
	}

	/**
	 * 设置是否后台上传文件到配置文件
	 * 
	 * @param b
	 */
	public void setConfigUploadFile(boolean b) {
		setProperty(AppConfig.CONF_UPLOADFILE, String.valueOf(b));
		setUploadFile(b);
	}

	// 是否启动休眠锁
	private static boolean openWakeLock = false;

	public static boolean getOpenWakeLock() {
		return openWakeLock;
	}

	public static void setOpenWakeLock(boolean b) {
		openWakeLock = b;
	}

	/**
	 * 设置是否启动休眠锁到配置文件
	 * 
	 * @param b
	 */
	public void setConfigWakeLock(boolean b) {
		setProperty(AppConfig.CONF_WAKELOCK, String.valueOf(b));
		setOpenWakeLock(b);
	}

	// 实时上传(总开关)
	private static boolean jitUpload = false;

	public static boolean getJITUpload() {
		return jitUpload;
	}

	public static void setJITUpload(boolean b) {
		jitUpload = b;
	}

	/**
	 * 设置实时上传(总开关)到配置文件
	 * 
	 * @param s
	 */
	public void setConfigJITUpload(boolean b) {
		setProperty(AppConfig.CONF_JITUPLOAD, String.valueOf(b));
		setJITUpload(b);
	}

	// 实时上传(巡点检)
	private static boolean jitUploadXDJ = false;

	public static boolean getJITUploadXDJ() {
		return jitUploadXDJ;
	}

	public static void setJITUploadXDJ(boolean b) {
		jitUploadXDJ = b;
	}

	/**
	 * 设置巡点检相关内容实时上传到配置文件
	 * 
	 * @param b
	 */
	public void setConfigJITUploadXDJ(boolean b) {
		setProperty(AppConfig.CONF_JITUPLOADXDJ, String.valueOf(b));
		setJITUploadXDJ(b);
	}

	// 实时上传(巡点检)单次上传条数
	private static int jituploadxdjoncenum = 10;

	public static int getJITUploadXDJOnceNum() {
		return jituploadxdjoncenum;
	}

	public static void setJITUploadXDJOnceNum(int i) {
		jituploadxdjoncenum = i;
	}

	/**
	 * 设置单次上传条数到配置文件
	 * 
	 * @param value
	 */
	public void setConfigOnceUploadNum(String value) {
		setProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE, value);
		setJITUploadXDJOnceNum(Integer.valueOf(value));
	}

	// 实时上传(巡点检)单次上传时间
	private static int jituploadxdjoncetime = 60;

	public static int getJITUploadXDJOnceTime() {
		return jituploadxdjoncetime;
	}

	public static void setJITUploadXDJOnceTime(int i) {
		jituploadxdjoncetime = i;
	}

	/**
	 * 设置单次上传时间到配置文件
	 * 
	 * @param value
	 */
	public void setConfigOnceLoadTime(String value) {
		setProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE_TIME, value);
		setJITUploadXDJOnceTime(Integer.valueOf(value));
	}

	// 实时上传(巡线)
	private static boolean jitUploadXX = false;

	public static boolean getJITUploadXX() {
		return jitUploadXX;
	}

	public static void setJITUploadXX(boolean b) {
		jitUploadXX = b;
	}

	/**
	 * 设置巡线相关内容实时上传
	 * 
	 * @param b
	 */
	public void setConfigJITUploadXX(boolean b) {
		setProperty(AppConfig.CONF_JITUPLOADXX, String.valueOf(b));
		setJITUploadXX(b);
	}

	// 视频录制设置
	private static String videoQuality = "LOW";

	public static String getVideoQuality() {
		return videoQuality;
	}

	public static void setVideoQuality(String s) {
		videoQuality = s;
	}

	/**
	 * 保存视频录制设置到配置文件
	 * 
	 * @param s
	 */
	public void setConfigVideoQuality(String s) {
		setProperty(AppConfig.CONF_VIDEOQUALITY, s);
		setVideoQuality(s);
	}

	/**
	 * 保存字体大小设置到配置文件
	 * 
	 * @param s
	 */
	public void setConfigFontSize(String s) {
		setProperty(AppConfig.CONF_FONTSIZE, s);
		setFontSize(s);
	}

	// 字体大小设置
	private static String fontSize = "normal";

	public static String getFontSize() {
		return fontSize;
	}

	public static void setFontSize(String s) {
		fontSize = s;
	}

	// 设置历史数据保存的天数
	private static String mHisDataSavedDays;

	public static void setHisDataSavedDays(String s) {
		mHisDataSavedDays = s;
	}

	public static String getHisDataSavedDays() {
		if(mHisDataSavedDays==null){
			return "0";
		}
		return mHisDataSavedDays;
	}

	/**
	 * 设置历史数据保存天数到配置文件
	 * 
	 * @param days
	 */
	public void setConfigHisDataSavedDays(String days) {
		setProperty(AppConfig.CONF_SYS_HISDATASAVEDDAYS, days);
		setHisDataSavedDays(days);
	}

	// 测量模块接入方式
	private static String measureType = "0";

	public static String getMeasureType() {
		return measureType;
	}

	public static void setMeasureType(String type) {
		measureType = type;
	}

	/**
	 * 设置测量模块类型到配置文件
	 * 
	 * @param type
	 */
	public void setConfigMeasureType(String type) {
		setProperty(AppConfig.CONF_MEASURE_TYPE, type);
		setMeasureType(type);
	}

	// 外接测量模块类型
	private static String mOuterMeasureType;

	public static String getOuterMeasureType() {

		return mOuterMeasureType;
	}

	public static void setOuterMeasureType(String s) {
		mOuterMeasureType = s;
	}

	/**
	 * 设置外接测量类型到配置文件
	 * 
	 * @param type
	 */
	public void setConfigOuterMeasureType(String type) {
		setProperty(AppConfig.CONFIG_OUTER_MEASURETYPE, type);
		setOuterMeasureType(type);
	}

	// 校准时间的方式
	private static String updateSysDateType = "2";

	public static String getUpdateSysDateType() {
		return updateSysDateType;
	}

	public static void setUpdateSysDateType(String updateType) {
		updateSysDateType = updateType;
	}

	/**
	 * 设置校准时间方式到配置文件
	 * 
	 * @param type
	 */
	public void setConfigUpdateSysDate(String type) {
		setProperty(AppConfig.CONF_UPDATE_SYS_DATE_TYPE, type);
		setUpdateSysDateType(type);
	}

	// GPS开启检查
	private static boolean gpsOpen = true;

	public static boolean getGPSOpen() {
		return gpsOpen;
	}

	public static void setGPSOpen(boolean b) {
		gpsOpen = b;
	}

	/**
	 * 设置GPS是否开启检查到配置文件
	 * 
	 * @param b
	 */
	public void setConfigGps(boolean b) {
		setProperty(AppConfig.CONF_GPS, String.valueOf(b));
		setGPSOpen(b);
	}

	// GPS定位模式
	private static int gpsSavedMode = 0;

	public static int getGPSSavedMode() {
		return gpsSavedMode;
	}

	public static void setGPSSavedMode(int i) {
		gpsSavedMode = i;
	}

	/**
	 * 设置获取GPS模式到配置文件
	 * 
	 * @param mode
	 */
	public void setConfigGPSSavedMode(String mode) {
		setProperty(AppConfig.CONF_LOCATION_GPS_MODE, mode);
		setGPSSavedMode(Integer.parseInt(mode));
	}

	// GPS定位时间间隔
	private static int gpsSavedTimeSpase = 5;

	public static int getGPSSavedTimeSpase() {
		return gpsSavedTimeSpase;
	}

	public static void setGPSSavedTimeSpase(int i) {
		gpsSavedTimeSpase = i;
	}

	/**
	 * 设置获取GPS定位时间到配置文件
	 * 
	 * @param mode
	 */
	public void setConfigGPSSavedTimeSpase(String timespase) {
		setProperty(AppConfig.CONF_LOCATION_TIME_SPASE, timespase);
		setGPSSavedTimeSpase(Integer.parseInt(timespase));
	}

	// 自定义快捷操作
	private static String mShortcutFunction;

	public static void setShortcutFunction(String str) {
		mShortcutFunction = str;
	}

	public static String getShortcutFunction() {
		return mShortcutFunction;
	}

	/**
	 * 设置显示快捷功能的配置文件
	 * 
	 * @param mode
	 */
	public void setConfigShortcutFunction(String str) {
		setProperty(AppConfig.CONF_SHORTCUT_FUNCTION, str);
		setShortcutFunction(str);
	}

	// 设置GPS定位后时间间隔的判断
	private static int mOnLocationTimeSpase;

	public static void setOnLocationCheckedTimeSpase(int timeSpase) {
		mOnLocationTimeSpase = timeSpase;
	}

	public static int getOnLocationCheckedTimeSpase() {
		return mOnLocationTimeSpase;
	}

	/**
	 * 设置GPS定位后时间间隔的判断到配置文件
	 * 
	 * @param timespace
	 */
	public void setConfigOnLocationCheckedTimeSpase(String timespace) {
		setProperty(AppConfig.CONF_LOCATION_CHECKED_TIME_SPASE, timespace);
		setOnLocationCheckedTimeSpase(Integer.parseInt(timespace));
	}

	// 设置GPS定位后距离间隔的判断
	private static int mOnLocationDistanceSpase;

	public static void setOnLocationCheckedDistanceSpase(int timeSpase) {
		mOnLocationDistanceSpase = timeSpase;
	}

	public static int getOnLocationCheckedDistanceSpase() {
		return mOnLocationDistanceSpase;
	}

	/**
	 * 设置GPS定位后距离间隔的判断到配置文件
	 * 
	 * @param distance
	 */
	public void setConfigOnLocationCheckedDistanceSpase(String distance) {
		setProperty(AppConfig.CONF_LOCATION_CHECKED_DISTANCE_SPASE, distance);
		setOnLocationCheckedDistanceSpase(Integer.parseInt(distance));
	}

	// 设置巡点检是否需要记录经纬度值
	private static boolean mXDJRecordJWYN = true;

	public static void setXDJRecordJWYN(boolean b) {
		mXDJRecordJWYN = b;
	}

	public static boolean getXDJRecordJWYN() {
		return mXDJRecordJWYN;
	}

	/**
	 * 设置巡点检是否需要记录经纬度值到配置文件
	 * 
	 * @param distance
	 */
	public void setConfigXDJRecordJWYN(boolean b) {
		setProperty(AppConfig.CONF_XDJRECORDJW, String.valueOf(b));
		setXDJRecordJWYN(b);
	}

	// 标牌识别工具里的几个参数
	private static boolean mChSave = false;
	private static boolean mChInput = false;
	private static boolean mChScan = false;
	private static boolean mChNFC = false;

	public static void setChSave(boolean b) {
		mChSave = b;
	}

	public static boolean getChSave() {
		return mChSave;
	}

	public void setConfigChSave(boolean b) {
		setProperty("chSave", String.valueOf(b));
		setChSave(b);
	}

	public static void setChInput(boolean b) {
		mChInput = b;
	}

	public static boolean getChInput() {
		return mChInput;
	}

	public void setConfigChInput(boolean b) {
		setProperty("chInput", String.valueOf(b));
		setChInput(b);
	}

	public static void setChScan(boolean b) {
		mChScan = b;
	}

	public static boolean getChScan() {
		return mChScan;
	}

	public void setConfigChScan(boolean b) {
		setProperty("chScan", String.valueOf(b));
		setChScan(b);
	}

	public static void setChNFC(boolean b) {
		mChNFC = b;
	}

	public static boolean getChNFC() {
		return mChNFC;
	}

	public void setConfigChNFC(boolean b) {
		setProperty("chNfc", String.valueOf(b));
		setChNFC(b);
	}

	private void initProperties() {
		try {
			globalProperties = AppConfig.getAppConfig(this).getAll();

			// 版本控制码
			preVersionCode = globalProperties
					.getProperty(AppConfig.CONF_PreVersionCode);
			if (StringUtils.isEmpty(preVersionCode)) {
				preVersionCode = AppConfig.DEFAULT_CONF_PREVERSIONCODE;
			}
			// APP语言
			appLanguage = globalProperties
					.getProperty(AppConfig.CONFIG_LANGUAGE);
			if (StringUtils.isEmpty(appLanguage)) {
				appLanguage = AppConfig.DEFAULT_CONF_APPLANGUAGE;
			}
			// 通信方式
			communicationType = globalProperties
					.getProperty(AppConfig.CONF_COMMUNICATION_TYPE);
			if (StringUtils.isEmpty(communicationType)) {
				communicationType = AppConfig.DEFAULT_CONF_COMMUNICATIONTYPE;
			} else {
				if (communicationType.equals("无线方式")) {
					communicationType = "0";
				} else if (communicationType.equals("USB方式")) {
					communicationType = "1";
				}
			}
			// 计划类型
			planType = globalProperties.getProperty(AppConfig.CONF_PLAN_TYPE);
			if (StringUtils.isEmpty(planType)) {
				planType = AppConfig.DEFAULT_CONF_PLANTYPE;
			}

			// 设置巡检器标示符
			xjqCD = globalProperties.getProperty(AppConfig.CONF_XJQCD);
			if (StringUtils.isEmpty(xjqCD)) {
				xjqCD = AppConfig.DEFAULT_CONF_XJQCD;
			}
			// WEBSERVICE地址
			webserviceAddress = globalProperties
					.getProperty(AppConfig.CONF_WSADDRESS);
			if (StringUtils.isEmpty(webserviceAddress)) {
				webserviceAddress = AppConfig.DEFAULT_CONF_WSADDRESS;
			}
			// WEBSERVICE外网地址
			webserviceAddressForWlan = globalProperties
					.getProperty(AppConfig.CONF_WSADDRESSFORWLAN);
			if (StringUtils.isEmpty(webserviceAddressForWlan)) {
				webserviceAddressForWlan = AppConfig.DEFAULT_CONF_WSADDRESS;
			}
			// WEB管线地址
			webserviceAddressForOther = globalProperties
					.getProperty(AppConfig.CONF_WSADDRESSFOROTHER);
			if (StringUtils.isEmpty(webserviceAddressForOther)) {
				webserviceAddressForOther = AppConfig.DEFAULT_CONF_WSADDRESS;
			}
			// 语音提示
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_VOICE))) {
				voice = AppConfig.DEFAULT_CONF_VOICE;
			} else {
				voice = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_VOICE));
			}
			// 默认上次结果值
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_LASTRESULT))) {
				lastResult = AppConfig.DEFAULT_CONF_LASTRESULT;
			} else {
				lastResult = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_LASTRESULT));
			}
			// 启动检查更新
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_CHECKUP))) {
				checkUp = AppConfig.DEFAULT_CONF_CHECKUP;
			} else {
				checkUp = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_CHECKUP));
			}
			// 启动文件后台上传
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_UPLOADFILE))) {
				uploadFile = AppConfig.DEFAULT_CONF_UPLOADFILE;
			} else {
				uploadFile = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_UPLOADFILE));
			}
			// 是否启动休眠锁
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_WAKELOCK))) {
				openWakeLock = AppConfig.DEFAULT_CONF_OPENWAKELOCK;
			} else {
				openWakeLock = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_WAKELOCK));
			}
			// 实时上传总开关
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_JITUPLOAD))) {
				jitUpload = AppConfig.DEFAULT_CONF_JITUPLOAD;
			} else {
				jitUpload = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_JITUPLOAD));
			}
			// 实时上传(巡点检)
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_JITUPLOADXDJ))) {
				if (jitUpload) {
					jitUploadXDJ = jitUpload;
				} else {
					jitUploadXDJ = AppConfig.DEFAULT_CONF_JITUPLOADXDJ;
				}
			} else {
				jitUploadXDJ = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_JITUPLOADXDJ));
			}
			// 实时上传(巡点检)单次上传条数
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE))) {
				jituploadxdjoncenum = AppConfig.DEFAULT_CONF_JITUPLOADXDJONCENUM;
			} else {
				jituploadxdjoncenum = Integer.parseInt(globalProperties
						.getProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE));
			}
			// 实时上传(巡点检)单次上传时间
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE_TIME))) {
				jituploadxdjoncetime = AppConfig.DEFAULT_CONF_JITUPLOADXDJONCETIME;
			} else {
				jituploadxdjoncetime = Integer.parseInt(globalProperties
						.getProperty(AppConfig.CONF_JITUPLOADXDJ_ONCE_TIME));
			}
			// 实时上传(巡线)
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_JITUPLOADXX))) {
				if (jitUpload) {
					jitUploadXX = jitUpload;
				} else {
					jitUploadXX = AppConfig.DEFAULT_CONF_JITUPLOADXX;
				}
			} else {
				jitUploadXX = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_JITUPLOADXX));
			}
			// 视频录制设置
			videoQuality = globalProperties
					.getProperty(AppConfig.CONF_VIDEOQUALITY);
			if (StringUtils.isEmpty(videoQuality)) {
				videoQuality = AppConfig.DEFAULT_CONF_VIDEOQUALITY;
			}
			// 字体大小设置
			fontSize = globalProperties.getProperty(AppConfig.CONF_FONTSIZE);
			if (StringUtils.isEmpty(fontSize)) {
				fontSize = AppConfig.DEFAULT_CONF_FONTSIZE;
			}
			// 历史数据保存天数
			mHisDataSavedDays = globalProperties
					.getProperty(AppConfig.CONF_SYS_HISDATASAVEDDAYS);
			if (StringUtils.isEmpty(mHisDataSavedDays)) {
				mHisDataSavedDays = AppConfig.DEFAULT_CONF_HISDATASAVEDDAYS;
			}
			// 测量模块接入方式
			measureType = globalProperties
					.getProperty(AppConfig.CONF_MEASURE_TYPE);
			if (StringUtils.isEmpty(measureType)) {
				measureType = AppConfig.DEFAULT_CONF_MEASURETYPE;
			} else {
				if (measureType.equals("内置")) {
					measureType = "0";
				} else if (measureType.equals("外接")) {
					measureType = "1";
				}
			}
			// 外接模块的类型
			mOuterMeasureType = globalProperties
					.getProperty(AppConfig.CONFIG_OUTER_MEASURETYPE);
			if (StringUtils.isEmpty(mOuterMeasureType)) {
				mOuterMeasureType = AppConfig.DEFAULT_CONF_OUTER_MEASURETYPE;
			}

			// 校准时间的方式
			updateSysDateType = globalProperties
					.getProperty(AppConfig.CONF_UPDATE_SYS_DATE_TYPE);
			if (StringUtils.isEmpty(updateSysDateType)) {
				updateSysDateType = AppConfig.DEFAULT_CONF_UPDATEDATETYPE;
			} else {
				if (updateSysDateType.equals("不校准")) {
					updateSysDateType = "0";
				} else if (updateSysDateType.equals("GPS校准")) {
					updateSysDateType = "1";
				} else if (updateSysDateType.equals("WEB校准")) {
					updateSysDateType = "2";
				}
				
				// 校时方式改为0不校准，1通信校准
				// 为兼容以前版本，如果为2，改为1
				if (updateSysDateType.equals("2"))
					updateSysDateType = "1";
			}
			// GPS开启检查
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_GPS))) {
				gpsOpen = AppConfig.DEFAULT_CONF_GPSOPEN;
			} else {
				gpsOpen = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_GPS));
			}
			// GPS定位模式
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_LOCATION_GPS_MODE))) {
				gpsSavedMode = AppConfig.DEFAULT_CONF_GPSSAVEDMODE;
			} else {
				gpsSavedMode = Integer.parseInt(globalProperties
						.getProperty(AppConfig.CONF_LOCATION_GPS_MODE));
			}
			// GPS定位时间间隔
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_LOCATION_TIME_SPASE))) {
				gpsSavedTimeSpase = AppConfig.DEFAULT_CONF_GPSSAVEDTIMESPACE;
			} else {
				gpsSavedTimeSpase = Integer.parseInt(globalProperties
						.getProperty(AppConfig.CONF_LOCATION_TIME_SPASE));
			}
			// GPS定位后时间间隔的判断
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_LOCATION_CHECKED_TIME_SPASE))) {
				mOnLocationTimeSpase = AppConfig.DEFAULT_CONF_ONLOCATIONTIMESPACE;
			} else {
				mOnLocationTimeSpase = Integer
						.parseInt(globalProperties
								.getProperty(AppConfig.CONF_LOCATION_CHECKED_TIME_SPASE));
			}
			// GPS定位后距离间隔的判断
			if (StringUtils
					.isEmpty(globalProperties
							.getProperty(AppConfig.CONF_LOCATION_CHECKED_DISTANCE_SPASE))) {
				mOnLocationDistanceSpase = AppConfig.DEFAULT_CONF_ONLOCATIONDISTANCESPACE;
			} else {
				mOnLocationDistanceSpase = Integer
						.parseInt(globalProperties
								.getProperty(AppConfig.CONF_LOCATION_CHECKED_DISTANCE_SPASE));
			}
			// 巡点检是否记录经纬度值
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_XDJRECORDJW))) {
				mXDJRecordJWYN = AppConfig.DEFAULT_CONF_XDJRECORDJW;
			} else {
				mXDJRecordJWYN = StringUtils.toBool(globalProperties
						.getProperty(AppConfig.CONF_XDJRECORDJW));
			}

			// 显示快捷功能的判断
			if (StringUtils.isEmpty(globalProperties
					.getProperty(AppConfig.CONF_SHORTCUT_FUNCTION))) {
				mShortcutFunction = null;
			} else {
				mShortcutFunction = globalProperties
						.getProperty(AppConfig.CONF_SHORTCUT_FUNCTION);
			}

			// 标牌识别工具的几个参数
			if (StringUtils.isEmpty(globalProperties.getProperty("chSave"))) {
				mChSave = false;
			} else {
				mChSave = StringUtils.toBool(globalProperties
						.getProperty("chSave"));
			}

			if (StringUtils.isEmpty(globalProperties.getProperty("chInput"))) {
				mChInput = false;
			} else {
				mChInput = StringUtils.toBool(globalProperties
						.getProperty("chInput"));
			}

			if (StringUtils.isEmpty(globalProperties.getProperty("chScan"))) {
				mChScan = false;
			} else {
				mChScan = StringUtils.toBool(globalProperties
						.getProperty("chScan"));
			}

			if (StringUtils.isEmpty(globalProperties.getProperty("chNfc"))) {
				mChNFC = false;
			} else {
				mChNFC = StringUtils.toBool(globalProperties
						.getProperty("chNfc"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 还原所有配置
	 */
	public void reductionAllConfig() {
		setConfigPreVersionCode("0");
		setConfigCommunicationType("0");
		setConfigPlanType("0");
		setConfigXJQCD("");
		setConfigWSAddress("");
		setConfigWSAddressForWlan("");
		setConfigWSAddressForOther("");
		setConfigVoice(false);
		setConfigLastResult(false);
		setConfigCheckUp(true);
		setConfigUploadFile(false);
		setConfigWakeLock(false);
		setConfigJITUpload(false);
		setConfigJITUploadXDJ(false);
		setConfigOnceUploadNum("10");
		setConfigOnceLoadTime("60");
		setConfigJITUploadXX(false);
		setConfigVideoQuality("LOW");
		setConfigHisDataSavedDays("31");
		setConfigMeasureType("0");
		setConfigOuterMeasureType("MS-101");
		setConfigUpdateSysDate("2");
		setConfigGps(true);
		setConfigGPSSavedMode("0");
		setConfigGPSSavedTimeSpase("5");
		setConfigOnLocationCheckedTimeSpase("1");
		setConfigOnLocationCheckedDistanceSpase("3000");
		setConfigShortcutFunction("");
		setConfigChSave(false);
		setConfigChInput(false);
		setConfigChScan(false);
		setConfigChNFC(false);
		setConfigFontSize("normal");
		setXDJRecordJWYN(true);
		setPwdYN(false);
	}

	// 通信方式(临时保存)
	private static String tempCommunicationType = getCommunicationType();

	public static String getTempCommunicationType() {
		return tempCommunicationType;
	}

	public static void setTempCommunicationType(String type) {
		tempCommunicationType = type;
	}

	/**
	 * 弹出更新时间对话框后，是否点击了立即重启
	 */
	static boolean updateSysDateYN = true;

	public static boolean getUpdateSysDateYN() {
		return updateSysDateYN;
	}

	public static void setUpdateSysDateYN(boolean b) {
		updateSysDateYN = b;
	}

	static String bluetoothAddressforTemperature = "";

	public static String getBlueToothAddressforTemperature() {
		return bluetoothAddressforTemperature;
	}

	public static void setBlueToothAddressforTemperature(String addr) {
		bluetoothAddressforTemperature = addr;
	}

	static String btConnectPwdforTemperature = "";

	public static String getBTConnectPwdforTemperature() {
		return btConnectPwdforTemperature;
	}

	public static void setBTConnectPwdforTemperature(String pwd) {
		btConnectPwdforTemperature = pwd;
	}

	static String bluetoothAddressforVibration = "";

	public static String getBlueToothAddressforVibration() {
		return bluetoothAddressforVibration;
	}

	public static void setBlueToothAddressforVibration(String addr) {
		bluetoothAddressforVibration = addr;
	}

	static String btConnectPwdforVibration = "";

	public static String getBTConnectPwdforVibration() {
		return btConnectPwdforVibration;
	}

	public static void setBTConnectPwdforVibration(String pwd) {
		btConnectPwdforVibration = pwd;
	}

	static String btFSLforTemputer = "0.95";

	public static String getBTFSLforTemputer() {
		return btFSLforTemputer;
	}

	public static void setBTFSLforTemputer(String fsl) {
		btFSLforTemputer = fsl;
	}

	static boolean twoBillYN = false;

	public static boolean getTwoBillYN() {
		return twoBillYN;
	}

	public static void setTwoBillYN(boolean b) {
		twoBillYN = b;
	}

	static boolean overhaulYN = false;

	public static boolean getOverhaulYN() {
		return overhaulYN;
	}

	public static void setOverhaulYN(boolean b) {
		overhaulYN = b;
	}

	static boolean pwdYN = false;

	public static boolean getPwdYN() {
		return pwdYN;
	}

	public static void setPwdYN(boolean pwd) {
		pwdYN = pwd;
	}

	static String pwd = "";

	public static String getPwd() {
		return pwd;
	}

	public static void setPwd(String mPwd) {
		pwd = mPwd;
	}

	static String casexjCondition = "";

	public static String getCondition() {
		return casexjCondition;
	}

	public static void setCondition(String condition) {
		casexjCondition = condition;
	}

	/**
	 * WIFI电源控制
	 * 
	 * @param yn
	 */
	public void setWifiOpenOrClose(boolean yn) {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (yn) {
			if (!wifiManager.isWifiEnabled())
				wifiManager.setWifiEnabled(true);
		} else {
			if (wifiManager.isWifiEnabled())
				wifiManager.setWifiEnabled(false);
		}
	}

	private static String WSAddress;
	private static String WSAddressForWlan;

	public static void SetWSAddress(String wsAddress, String wsAddressWLan) {
		WSAddress = wsAddress;
		WSAddressForWlan = wsAddressWLan;
	}

	/**
	 * 当前Web服务地址
	 * 
	 * @return
	 */
	public static String GetWSAddress() {
		String lanString = StringUtils.isEmpty(WSAddress) ? "" : WSAddress
				+ "/wsmemscomm.asmx";// 内网(wifi)
		String wlanString = StringUtils.isEmpty(WSAddressForWlan) ? ""
				: WSAddressForWlan + "/wsmemscomm.asmx";// 外网(3G、4G)
		String currWS = lanString;
		if (NetWorkHelper.isWifi(baseContext)) {
			currWS = StringUtils.isEmpty(lanString) ? wlanString : lanString;
		} else {
			currWS = StringUtils.isEmpty(wlanString) ? lanString : wlanString;
		}
		return currWS;
	}

	/**
	 * String[]{内网地址，外网地址}
	 * 
	 * @return
	 */
	public static String[] GetWSAddressList() {
		String lanString = StringUtils.isEmpty(WSAddress) ? "" : WSAddress
				+ "/wsmemscomm.asmx";
		String wlanString = StringUtils.isEmpty(WSAddressForWlan) ? ""
				: WSAddressForWlan + "/wsmemscomm.asmx";
		return new String[] { lanString, wlanString };
	}

	public static String GetWSAddressBasePath() {

		String lanString = StringUtils.isEmpty(WSAddress) ? "" : WSAddress;// 内网(wifi)
		String wlanString = StringUtils.isEmpty(WSAddressForWlan) ? ""
				: WSAddressForWlan;// 外网(3G、4G)
		String currWS = lanString;
		if (NetWorkHelper.isWifi(baseContext)) {
			currWS = StringUtils.isEmpty(lanString) ? wlanString : lanString;
		} else {
			currWS = StringUtils.isEmpty(wlanString) ? lanString : wlanString;
		}
		return currWS;

		// return WSAddress;
	}

	private static String GlobalxjqCD;

	public static void SetxjqCD(String xjqCD) {
		GlobalxjqCD = xjqCD;
	}

	public static String GetxjqCD() {
		return GlobalxjqCD;
	}

	private static String lineListStr;

	public static void SetlineList(String LineList) {
		lineListStr = LineList;
	}

	public static String GetlineList() {
		return lineListStr;
	}

	private static boolean refreshLineYN = false;

	public static boolean GetRefreshLineYN() {
		return refreshLineYN;
	}

	public static void SetRefreshLineYN(boolean refreshYN) {
		refreshLineYN = refreshYN;
	}

	private static boolean downLoadYN = false;

	public static boolean GetDownLoadYN() {
		return downLoadYN;
	}

	public static void SetDownLoadYN(boolean download) {
		downLoadYN = download;
	}

	private static boolean fromLoginYN = false;

	public static boolean getFromLoginYN() {
		return fromLoginYN;
	}

	public static void setFromLoginYN(boolean b) {
		fromLoginYN = b;
	}

	/**
	 * 当前路线信息
	 */
	private static DJLine _currDJLine;

	public static void setCurrLineInfo(DJLine lineInfo) {
		_currDJLine = lineInfo;
	}

	public static DJLine getCurrLineInfo() {
		return _currDJLine;
	}

	private static int _currlineID = 0;

	/**
	 * 当前路线ID
	 * 
	 * @param lineID
	 */
	public static void SetCurrLineID(int lineID) {
		_currlineID = lineID;
	}

	/**
	 * 当前路线ID
	 * 
	 * @return
	 */
	public static int GetCurrLineID() {
		return _currlineID;
	}

	private static int _currlineIDforjit = 0;

	/**
	 * 当前路线ID(实时上传专用)
	 * 
	 * @param lineID
	 */
	public static void SetCurrLineIDForJIT(int lineID) {
		_currlineIDforjit = lineID;
	}

	/**
	 * 当前路线ID(实时上传专用)
	 * 
	 * @return
	 */
	public static int GetCurrLineIDForJIT() {
		return _currlineIDforjit;
	}

	/**
	 * 当前路线类型
	 */
	private static int _currlinetype = -1;

	public static void setCurrLineType(int type) {
		_currlinetype = type;
	}

	public static int getCurrLineType() {
		return _currlinetype;
	}

	public static String GetUniqueID() {
		// 通信设备国际识别码作为PDA的唯一编码
		String result = StringUtils.isEmpty(GetIMEI()) ? GetMAC() : GetIMEI();
		return StringUtils.isEmpty(result) ? GetGUID() : result;
	}
	
	private static String _guid = "";
	public static void SetGUID(String guid) {
		_guid = guid;
	}
	public static String GetGUID() {
		return _guid;
	}

	private static String _mac = "";

	public static void SetMAC(String mac) {
		_mac = mac;
	}

	public static String GetMAC() {
		return _mac;
	}

	private static String _imei = "";

	/**
	 * IMEI
	 * 
	 * @param imei
	 */
	public static void SetIMEI(String imei) {
		_imei = imei;
	}

	/**
	 * IMEI
	 * 
	 * @return
	 */
	public static String GetIMEI() {
		// _imei = "123456789";
		return _imei;
	}

	/**
	 * 运营商名称
	 * 
	 * @return
	 */
	public static String getNetworkOperatorName() {
		return tm.getNetworkOperatorName();
	}

	/**
	 * sim卡序列号
	 * 
	 * @return
	 */
	public static String getSimSerialNumber() {
		return tm.getSimSerialNumber();
	}

	/**
	 * IMSI
	 * 
	 * @return
	 */
	public static String getSubscriberId() {
		return tm.getSubscriberId();
	}

	/**
	 * sim卡所在国家
	 * 
	 * @return
	 */
	public static String getNetworkCountryIso() {
		return tm.getNetworkCountryIso();
	}

	/**
	 * 运营商编号
	 * 
	 * @return
	 */
	public static String getNetworkOperator() {
		return tm.getNetworkOperator();
	}

	/**
	 * 手机生产厂商名称
	 * 
	 * @return
	 */
	public static String getFactoryName() {
		return android.os.Build.MANUFACTURER.toUpperCase();
	}
	
	/**
	 * 获取硬件信息，用于设备注册，格式为：品牌,型号,产品名,制造商
	 * @return
	 */
	public static String getHardwareInfo() {
		return android.os.Build.BRAND.toUpperCase() + ","
				+ android.os.Build.MODEL.toUpperCase() + ","
				+ android.os.Build.PRODUCT.toUpperCase() + ","
				+ android.os.Build.MANUFACTURER.toUpperCase();
	}

	/**
	 * 是否正在传输
	 */
	private static boolean IsUpload = false;

	public static void SetIsUpload(boolean YN) {
		IsUpload = YN;
	}

	public static boolean GetIsUpload() {
		return IsUpload;
	}

	private static int userID;
	private static String userName;
	private static String userAccount;
	private static String userCD;
	private static String userPassword;

	public static int getUserID() {
		return userID;
	}

	public static void setUserID(int userid) {
		userID = userid;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String username) {
		userName = username;
	}

	public static String getUserAccount() {
		return userAccount;
	}

	public static void setAccount(String account) {
		userAccount = account;
	}

	public static String getUserCD() {
		return userCD;
	}

	public static void setUserCD(String usercd) {
		userCD = usercd;
	}

	public static String getUserPassword() {
		return userPassword;
	}

	public static void setUserPassword(String userpassword) {
		userPassword = userpassword;
	}

	private static String idCD = "";
	private static String idposName = "";
	private static DJ_IDPos djidpos = null;

	public static String getIDCD() {
		return idCD;
	}

	public static void setIDCD(String idcd) {
		idCD = idcd;
	}

	public static String getIDPosName() {
		return idposName;
	}

	public static void setIDPosName(String idposname) {
		idposName = idposname;
	}

	public static DJ_IDPos getCurIDPos() {
		return djidpos;
	}

	public static void setCurIDPos(DJ_IDPos info) {
		djidpos = info;
	}

	private static Location _locLocation;

	/**
	 * 上次GPS坐标最后位置
	 * 
	 * @return
	 */
	public static Location getLastKnownLocation() {
		return _locLocation;
	}

	/**
	 * 上次GPS坐标最后位置
	 * 
	 * @return
	 */
	public static void setLastKnownLocation(Location locLocation) {
		_locLocation = locLocation;
	}

	public static BDLocation _currLocation = null;

	/**
	 * 当前GPS位置信息
	 * 
	 * @return
	 */
	// public static BDLocation getCurrLocation() {
	// if (LocationList != null || LocationList.size() > 0) {
	// _currLocation = null;
	// for (BDLocation location : LocationList) {
	// _currLocation = location;
	// }
	// return _currLocation;
	// } else {
	// return null;
	// }
	// }
	public static BDLocation getCurrLocation() {
		return _currLocation;
	}

	public static void setCurrLocation(BDLocation currLocation) {
		_currLocation = currLocation;
	}

	/**
	 * 保存当前location
	 */

	public static List<BDLocation> cleanLocation() {
		LocationList.clear();
		return LocationList;
	}

	private static String _gpsState;

	/**
	 * 当前GPS状态
	 * 
	 * @return
	 */
	public static String getGpsState() {
		return _gpsState;
	}

	/**
	 * 当前GPS状态
	 * 
	 * @param currLocation
	 */
	public static void setGpsState(String gpsState) {
		_gpsState = gpsState;
	}

	private static String _currCityName = "";

	/**
	 * 当前所在城市
	 * 
	 * @return
	 */
	public static String getCurrCityName() {
		return _currCityName;
	}

	/**
	 * 当前所在城市
	 * 
	 * @param currCityName
	 */
	public static void setCurrCityName(String currCityName) {
		_currCityName = currCityName;
	}

	private static String _currCityCode = "";

	/**
	 * 当前所在城市编码
	 * 
	 * @return
	 */
	public static String getCurrCityCode() {
		return _currCityCode;
	}

	/**
	 * 当前所在城市编码
	 * 
	 * @param currCityCode
	 */
	public static void setCurrCityCode(String currCityCode) {
		_currCityCode = currCityCode;
	}

	private static LocationInfo _LocationInfo;

	/**
	 * 当前地理位置信息
	 * 
	 * @param _info
	 */
	public static void setLocationInfo(LocationInfo _info) {
		_LocationInfo = _info;
		if (_info != null) {
			setCurrCityCode(_info.getcityCode());
			setCurrCityName(_info.getcity());
		}
	}

	/**
	 * 当前地理位置信息
	 * 
	 * @return
	 */
	public static LocationInfo getLocationInfo() {
		return _LocationInfo;
	}

	static boolean xjdYN = true;

	/**
	 * 是否启用巡点检模块
	 * 
	 * @param yn
	 */
	public static void setStartXDJYN(boolean yn) {
		xjdYN = yn;
	}

	/**
	 * 是否启用巡点检模块
	 * 
	 * @param yn
	 */
	public static boolean getStartXDJYN() {
		return xjdYN;
	}

	public static String getLocalMacAddress() {
		String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02-00-00-00-00-02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X-", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02-00-00-00-00-02";
        }
        return macAddress;
		/*String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim().replace(":", "-");// 去空格并替换:为-
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;*/
	}

	public static Hashtable<String, String[]> gestureUserInfo = new Hashtable<String, String[]>();

	public static String mStartRFID = "";

	public static LocationClient bdLocationClient;

	public enum StartRFID {
		DJ_IDPOS, Tool_NFC, Map_main
	}

	/******************************************************
	 * 数据库相关
	 ******************************************************/
	// 两票结果库
	public static SQLiteDatabase TwoBillResultdb;
	private TwoBillResultDaoMaster twoBillResultMaster;
	private TwoBillResultDaoSession twoBillResultSession;
	// 两票下载库
	public static SQLiteDatabase TowBill;
	private TwoBillMaster twoBillMaster;
	private TwoBillSession twoBillSession;
	// 点检下载库对象
	public static SQLiteDatabase DJdb;
	private DJDAOMaster djdaoMaster;
	private DJDAOSession djdaoSession;
	// 点检上传库对象
	public static SQLiteDatabase DJResultdb;
	public DJResultDAOMaster djredaoMaster;
	public DJResultDAOSession djredaoSession;
	// 点检实时上传对象
	public static SQLiteDatabase DJUploadingdb;
	public DJResultDAOMaster djUploadingMaster;
	public DJResultDAOSession djUploadingSession;
	// 临时库对象
	private SQLiteDatabase TempMeasuredb;
	private TempMeasureDaoMaster tempmeasuredaoMaster;
	private TempMeasureDaoSession tempmeasuredaoSession;
	// 公共库
	public static SQLiteDatabase comdb;
	private ComDaoMaster comdaoMaster;
	private ComDaoSession comdaoSession;
	// 历史数据库
	public static SQLiteDatabase xjHisDataDB;
	private XJHisDataBaseMaster xjHisDataBaseMaster;
	private XJHisDataBaseSession xJHisDataBaseSession;
	// 检修下载库
	private OverhaulDaoMaster overhaulDaoMaster;
	private OverhaulDaoSession overhaulDaosession;
	private SQLiteDatabase overhauldb;
	// 工作票下载库
	private WorkBillMaster WorkBillMaster;
	private WorkBillSession WorkBillSession;
	private SQLiteDatabase WorkBilldb;
	// 工作票上传库
	private WorkResultMaster WorkResultMaster;
	private WorkResultSession WorkResultSession;
	private SQLiteDatabase WorkResultdb;

	private SharedPreferences sp;

	// 操作票结果库
	public synchronized TwoBillResultDaoSession getTwoBillResultSession() {
		String DataBaseName = AppConst.TwoTicketRecordPath() + "D-5.sdf";
		try {
			if (!FileUtils.checkFileExists(DataBaseName)) {
				twoBillResultSession = null;
			}
			if (twoBillResultSession == null) {
				if (TwoBillResultdb != null && TwoBillResultdb.isOpen()) {
					Log.i("", "XST关闭操作票结果库");
					TwoBillResultdb.close();
				}
				com.moons.xst.sqlite.TwoBillResultDaoMaster.DevOpenHelper helper = new TwoBillResultDaoMaster.DevOpenHelper(
						this, DataBaseName, null);
				TwoBillResultdb = helper.getWritableDatabase();
				Log.i("", "XST打开操作票结果库");
				twoBillResultMaster = new TwoBillResultDaoMaster(
						TwoBillResultdb);
				twoBillResultSession = twoBillResultMaster.newSession();
			} else {
				twoBillResultSession = twoBillResultMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return twoBillResultSession;
	}

	// 检修库
	public synchronized OverhaulDaoSession getOverhaulDaoSession() {
		String DataBaseName = AppConst.XSTDBPath() + "Overhaul.sdf";
		try {
			if (!FileUtils.checkFileExists(DataBaseName)) {
				return null;
			}
			if (overhaulDaosession == null) {
				if (overhauldb != null && overhauldb.isOpen()) {
					Log.i("", "XST关闭检修下载库");
					overhauldb.close();
				}
				OverhaulDaoMaster.DevOpenHelper helper = new OverhaulDaoMaster.DevOpenHelper(
						this, DataBaseName, null);
				overhauldb = helper.getWritableDatabase();
				Log.i("", "XST打开检修下载库");
				overhaulDaoMaster = new OverhaulDaoMaster(overhauldb);
				overhaulDaosession = overhaulDaoMaster.newSession();
			} else {
				overhaulDaosession = overhaulDaoMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return overhaulDaosession;
	}

	// 操作票下载库
	public synchronized TwoBillSession getTwoBillSession() {
		String DataBaseName = AppConst.XSTDBPath() + "OperateBill.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			return null;
		}
		try {
			if (twoBillSession == null) {
				if (TowBill != null && TowBill.isOpen()) {
					Log.i("", "XST关闭操作票下载库");
					TowBill.close();
				}
				com.moons.xst.sqlite.TwoBillMaster.DevOpenHelper helper = new com.moons.xst.sqlite.TwoBillMaster.DevOpenHelper(
						this, DataBaseName, null);
				TowBill = helper.getWritableDatabase();
				Log.i("", "XST打开操作票下载库");
				twoBillMaster = new TwoBillMaster(TowBill);
				twoBillSession = twoBillMaster.newSession();
			} else {
				twoBillSession = twoBillMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return twoBillSession;
	}

	// 工作票下载库
	public synchronized WorkBillSession getWorkBillSession() {
		String DataBaseName = AppConst.XSTDBPath() + "WorkBill.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			return null;
		}
		try {
			if (WorkBillSession == null) {
				if (WorkBilldb != null && WorkBilldb.isOpen()) {
					Log.i("", "XST关闭工作票下载库");
					WorkBilldb.close();
				}
				com.moons.xst.sqlite.WorkBillMaster.DevOpenHelper helper = new com.moons.xst.sqlite.WorkBillMaster.DevOpenHelper(
						this, DataBaseName, null);
				WorkBilldb = helper.getWritableDatabase();
				Log.i("", "XST打开工作票下载库");
				WorkBillMaster = new WorkBillMaster(WorkBilldb);
				WorkBillSession = WorkBillMaster.newSession();
			} else {
				WorkBillSession = WorkBillMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return WorkBillSession;
	}

	// 工作票结果库
	public synchronized WorkResultSession getWorkResultSession() {
		String DataBaseName = AppConst.WorkBillRecordPath()
				+ "WorkBillResult.sdf";
		try {
			if (!FileUtils.checkFileExists(DataBaseName)) {
				WorkResultSession = null;
			}
			if (WorkResultSession == null) {
				if (WorkResultdb != null && WorkResultdb.isOpen()) {
					Log.i("", "XST关闭工作票结果库");
					WorkResultdb.close();
				}
				com.moons.xst.sqlite.WorkResultMaster.DevOpenHelper helper = new WorkResultMaster.DevOpenHelper(
						this, DataBaseName, null);
				WorkResultdb = helper.getWritableDatabase();
				Log.i("", "XST打开工作票结果库");
				WorkResultMaster = new WorkResultMaster(WorkResultdb);
				WorkResultSession = WorkResultMaster.newResultSession();
			} else {
				WorkResultSession = WorkResultMaster.newResultSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return WorkResultSession;
	}

	public synchronized ComDaoSession getComSession() {
		try {
			String DataBaseName = AppConst.XSTDBPath() + "Common.sdf";
			if (!FileUtils.checkFileExists(DataBaseName)) {
				comdaoSession = null;
			}
			if (comdaoSession == null) {
				/*
				 * if (comdb != null && comdb.isOpen()) { Log.i("",
				 * "aaa强制关闭comdb库"); comdb.close(); }
				 */
				com.moons.xst.sqlite.ComDaoMaster.DevOpenHelper helper = new ComDaoMaster.DevOpenHelper(
						this, DataBaseName, null);
				comdb = helper.getWritableDatabase();
				Log.i("", "XST打开Common库");
				comdaoMaster = new ComDaoMaster(comdb);
				comdaoSession = comdaoMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return comdaoSession;
	}

	/**
	 * 点检下载库
	 * 
	 * @param lineid
	 * @return
	 */
	public synchronized DJDAOSession getDJSession(int lineid) {
		try {
			String DataBaseName = AppConst.XSTDBPath()
					+ AppConst.PlanDBName(lineid);
			if (!FileUtils.checkFileExists(DataBaseName)) {
				UIHelper.ToastMessage(this,
						R.string.home_line_noplanfile_notice);
				djdaoSession = null;
				return null;
			}
			if (djdaoSession == null || djdaoSession.getLineID() != lineid) {

				if (DJdb != null && DJdb.isOpen()) {
					Log.i("", "aaa关闭下载库");
					DJdb.close();
				}

				DevOpenHelper helper = new DJDAOMaster.DevOpenHelper(this,
						DataBaseName, null);
				DJdb = helper.getWritableDatabase();
				Log.i("", "XST打开下载库");
				djdaoMaster = new DJDAOMaster(DJdb);
				djdaoSession = djdaoMaster.newSession();
				djdaoSession.setLineID(lineid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return djdaoSession;
	}

	/**
	 * 历史结果数据查询（点检结果，到位结果，设备状态）
	 * 
	 * @param lineid
	 * @return
	 */
	public synchronized XJHisDataBaseSession getXJHisDataBaseSession() {
		try {
			String DataBaseName = AppConst.CurrentResultPath("XJHisDataBase")
					+ AppConst.ResultDBName("XJHisDataBase");
			if (!FileUtils.checkFileExists(DataBaseName)) {
				xJHisDataBaseSession = null;
			}
			if (xJHisDataBaseSession == null) {
				com.moons.xst.sqlite.XJHisDataBaseMaster.DevOpenHelper devOpenHelper = new XJHisDataBaseMaster.DevOpenHelper(
						this, DataBaseName, null);
				xjHisDataDB = devOpenHelper.getWritableDatabase();
				Log.i("", "XST打开历史数据库");
				xjHisDataBaseMaster = new XJHisDataBaseMaster(xjHisDataDB);
				xJHisDataBaseSession = (XJHisDataBaseSession) xjHisDataBaseMaster
						.newSession();
				// xJHisDataBaseSession.setLineID(lineid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return xJHisDataBaseSession;
	}

	// 临时测量库
	public synchronized TempMeasureDaoSession getTempMeasureSession(int lineid) {
		String DataBaseName = AppConst.CurrentResultPath(lineid) + "D0.sdf";
		if (!FileUtils.checkFileExists(DataBaseName)) {
			tempmeasuredaoSession = null;
		}
		try {
			if (tempmeasuredaoSession == null
					|| tempmeasuredaoSession.getLineID() != lineid) {
				com.moons.xst.sqlite.TempMeasureDaoMaster.DevOpenHelper helper = new TempMeasureDaoMaster.DevOpenHelper(
						this, DataBaseName, null);
				addFileList(lineid, DataBaseName);
				Log.i("", "XST打开临时测量库");
				TempMeasuredb = helper.getWritableDatabase();
				tempmeasuredaoMaster = new TempMeasureDaoMaster(TempMeasuredb);
				tempmeasuredaoSession = tempmeasuredaoMaster.newSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tempmeasuredaoSession;
	}

	/**
	 * 路线结果存放库
	 * 
	 * @param lineid
	 * @return
	 */
	public synchronized DJResultDAOSession getResultSession(int lineid) {

		// 备份数据库路径
		final String newPath = AppConst.XSTDamageDB() + "D" + lineid + "_"
				+ DateTimeHelper.getDateTimeNow1() + ".sdf";
		// 返回库保存路径
		final String DataBaseName = AppConst.CurrentResultPath(lineid)
				+ AppConst.ResultDBName(lineid);
		// 本地没有返回库文件则把session和master设为null
		if (!FileUtils.checkFileExists(DataBaseName)) {
			djredaoSession = null;
		} else {
			InitDJsqlite.DBLog(DataBaseName);
		} /*
		 * else { // 备份数据库 FileUtils.copyFile(DataBaseName, newPath); if
		 * (InitDJsqlite.checkDB(DataBaseName)) {
		 * UIHelper.ToastMessage(getBaseContext(),
		 * getString(R.string.db_run_code_error)); } else { File file = new
		 * File(newPath); file.delete(); } }
		 */
		try {
			if (djredaoSession == null || djredaoSession.getLineID() != lineid) {

				if (DJResultdb != null && DJResultdb.isOpen()) {
					Log.i("", "XST关闭结果库");
					DJResultdb.close();
				}

				com.moons.xst.sqlite.DJResultDAOMaster.DevOpenHelper helper = new DJResultDAOMaster.DevOpenHelper(
						this, DataBaseName, null);
				addFileList(lineid, DataBaseName);
				Log.i("", "XST打开结果库");
				DJResultdb = helper.getWritableDatabase();
				djredaoMaster = new DJResultDAOMaster(DJResultdb);
				djredaoSession = djredaoMaster.newSession();
				djredaoSession.setLineID(lineid);

				// 条件巡检冗余三张表的数据
				/*
				 * if (getCurrLineType() == AppConst.LineType.XDJ.getLineType())
				 * { DJDAOSession djdaoSession = getDJSession(lineid); if
				 * (djredaoSession
				 * .getZ_DJ_PlanForResultDao().isTableExist(DJResultdb) &&
				 * djredaoSession.getZ_DJ_PlanForResultDao().loadAll().size() <=
				 * 0) { Z_DJ_PlanDao djplanDao = djdaoSession.getZ_DJ_PlanDao();
				 * List<Z_DJ_Plan> planList = djplanDao.loadAll();
				 * 
				 * for (Z_DJ_Plan plan : planList) {
				 * djredaoSession.getZ_DJ_PlanForResultDao().insert(plan); } }
				 * if (djredaoSession.getSRLineTreeLastResultForResultDao().
				 * isTableExist(DJResultdb) &&
				 * djredaoSession.getSRLineTreeLastResultForResultDao
				 * ().loadAll().size() <= 0) { SRLineTreeLastResultDao dao =
				 * djdaoSession.getSRLineTreeLastResultDao();
				 * List<SRLineTreeLastResult> list = dao.loadAll();
				 * 
				 * for (SRLineTreeLastResult info : list) {
				 * djredaoSession.getSRLineTreeLastResultForResultDao
				 * ().insert(info); } } if
				 * (djredaoSession.getZ_ConditionForResultDao
				 * ().isTableExist(DJResultdb) &&
				 * djredaoSession.getZ_ConditionForResultDao().loadAll().size()
				 * <= 0) { Z_ConditionDao conditionDao =
				 * djdaoSession.getZ_ConditionDao(); List<Z_Condition>
				 * conditions = conditionDao.loadAll();
				 * 
				 * for (Z_Condition condition : conditions) {
				 * djredaoSession.getZ_ConditionForResultDao
				 * ().insert(condition); } } }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return djredaoSession;
	}

	/**
	 * 路线结果上传库
	 * 
	 * @param lineid
	 * @return
	 */
	public synchronized DJResultDAOSession getUploadingSession(int lineid) {
		// 备份数据库路径
		String newPath = AppConst.XSTDamageDB() + "D" + lineid + "_"
				+ DateTimeHelper.getDateTimeNow1() + ".sdf";
		// 返回库保存路径
		String DataBaseName = AppConst.CurrentResultPath(lineid)
				+ AppConst.ResultDBName(lineid);
		// 本地没有返回库文件则把session和master设为null
		if (!FileUtils.checkFileExists(DataBaseName)) {
			djUploadingSession = null;
		}
		try {
			if (djUploadingSession == null
					|| djUploadingSession.getLineID() != lineid) {
				/*
				 * if (DJUploadingdb != null && DJUploadingdb.isOpen()) {
				 * Log.i("", "aaa关闭上传库" + lineid + "   " +
				 * djUploadingSession.getLineID()); DJUploadingdb.close(); }
				 */
				com.moons.xst.sqlite.DJResultDAOMaster.DevOpenHelper helper = new DJResultDAOMaster.DevOpenHelper(
						this, DataBaseName, null);
				addFileList(lineid, DataBaseName);
				DJUploadingdb = helper.getWritableDatabase();
				Log.i("", "XST打开上传库");
				djUploadingMaster = new DJResultDAOMaster(DJUploadingdb);
				djUploadingSession = djUploadingMaster.newSession();
				djUploadingSession.setLineID(lineid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return djUploadingSession;
	}

	// 下载两票是删除两票下载库和结果库
	public void refreshTwoBil() {
		twoBillSession = null;
	}

	// 下载检修数据后清空检修session
	public void refreshOverhaul() {
		overhaulDaosession = null;
	}

	// 下载工作票后清空工作票session
	public void refreshWorkBill() {
		WorkBillSession = null;
	}

	/**
	 * 路线下载完成清空相应session
	 */
	public void refreshDao() {
		twoBillSession = null;
		twoBillResultSession = null;
		djdaoSession = null;
		djredaoSession = null;
		djUploadingSession = null;
		comdaoSession = null;
		tempmeasuredaoSession = null;
		xJHisDataBaseSession = null;
		overhaulDaosession = null;
		WorkBillSession = null;
	}

	// 强制关闭数据库
	public void closeDB() {
		if (DJUploadingdb != null && DJUploadingdb.isOpen()) {
			DJUploadingdb.close();
			Log.i("", "XST关闭上传库");
		}
		if (DJResultdb != null && DJResultdb.isOpen()) {
			Log.i("", "XST关闭结果库");
			DJResultdb.close();
		}
		if (DJdb != null && DJdb.isOpen()) {
			Log.i("", "XST关闭下载库");
			DJdb.close();
		}
		if (comdb != null && comdb.isOpen()) {
			Log.i("", "XST关闭comdb库");
			comdb.close();
		}
		if (TempMeasuredb != null && TempMeasuredb.isOpen()) {
			Log.i("", "XST关闭临时库");
			TempMeasuredb.close();
		}
		if (xjHisDataDB != null && xjHisDataDB.isOpen()) {
			Log.i("", "XST关闭历史数据库");
			xjHisDataDB.close();
		}
		if (overhauldb != null && overhauldb.isOpen()) {
			Log.i("", "XST关闭检修下载库");
			overhauldb.close();
		}
		if (WorkBilldb != null && WorkBilldb.isOpen()) {
			Log.i("", "XST关闭工作票下载库");
			WorkBilldb.close();
		}
		if (TowBill != null && TowBill.isOpen()) {
			Log.i("", "XST关闭两票下载库");
			TowBill.close();
		}
		if (TwoBillResultdb != null && TwoBillResultdb.isOpen()) {
			Log.i("", "XST关闭两票结果库");
			TwoBillResultdb.close();
		}
		djdaoSession = null;
		djredaoSession = null;
		djUploadingSession = null;
		comdaoSession = null;
		tempmeasuredaoSession = null;
		xJHisDataBaseSession = null;
		overhaulDaosession = null;
		twoBillSession = null;
		twoBillResultSession = null;
		WorkBillSession = null;
	}

	private synchronized void addFileList(int lineID, String filepath) {
		DJLine djLine = new DJLine();
		djLine.setLineID(lineID);
		djLine.setResultDBPath(filepath);
		djLine.setPlanDBPath(AppConst.XSTDBPath() + File.separator
				+ AppConst.PlanDBName(lineID));
		boolean exit = false;
		if (AppContext.resultDataFileListBuffer != null
				&& AppContext.resultDataFileListBuffer.size() > 0) {
			for (int i = 0; i < AppContext.resultDataFileListBuffer.size(); i++) {
				if (AppContext.resultDataFileListBuffer.get(i).getLineID() == lineID) {
					exit = true;
					break;
				}
			}
		}
		if (!exit) {
			AppContext.resultDataFileListBuffer.add(djLine);
		}
	}
	
	public void readXSTKeyConfig() {		
		File file = new File(AppConst.XSTKeyConfigPath);
		if (!file.exists()) {
			String imei = tm.getDeviceId();
			String macAddrs = getLocalMacAddress();
			String guid = UUID.randomUUID().toString();
			AppContext.SetIMEI(imei);
			AppContext.SetMAC(macAddrs);
			AppContext.SetGUID(guid);
			
			try {
				// 在指定的文件夹中创建文件
				file.createNewFile();
			} catch (Exception e) {
			}
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(AppConst.XSTKeyConfigPath, false);
				bw = new BufferedWriter(fw);
				bw.write(AppConst.getXSTKeyStr(imei, macAddrs, guid));
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
				}
			}
		} else {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			boolean updateConfigYN = false;
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);
				doc.normalize();
				// 找到根Element
				Element root = doc.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("Setting");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) (nodes.item(i));
					// 获取river中name属性值
					if (element.getAttribute("Name").equals("IMEI")) {
						if (!StringUtils.isEmpty(tm.getDeviceId())
								&& !element.getAttribute("Value").equalsIgnoreCase(tm.getDeviceId())) {
							AppContext.SetIMEI(tm.getDeviceId());
							element.setAttribute("Value", AppContext.GetIMEI());
							updateConfigYN = true;	
						} else {
							AppContext.SetIMEI(element.getAttribute("Value"));
						}
					} else if (element.getAttribute("Name").equals("MAC")) {
						if (!StringUtils.isEmpty(getLocalMacAddress())
								&& !element.getAttribute("Value").equalsIgnoreCase(getLocalMacAddress())) {
							AppContext.SetMAC(getLocalMacAddress());
							element.setAttribute("Value", AppContext.GetMAC());
							updateConfigYN = true;
						} else {
							AppContext.SetMAC(element.getAttribute("Value"));
						}
					} else if (element.getAttribute("Name").equals("GUID")) {
						if (!updateConfigYN) {
							AppContext.SetGUID(element.getAttribute("Value"));
						}
						else {
							AppContext.SetGUID(UUID.randomUUID().toString());
							element.setAttribute("Value", AppContext.GetGUID());
						}
					}
				}
				
				if (updateConfigYN) {
					TransformerFactory transformerFactory=TransformerFactory.newInstance();
					javax.xml.transform.Transformer transformer=transformerFactory.newTransformer();
					DOMSource domSource=new DOMSource(doc);
					//设置编码类型
					transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
					FileOutputStream fs = new FileOutputStream(AppConst.XSTKeyConfigPath);
					StreamResult result=new StreamResult(fs);
					//把DOM树转换为xml文件
					transformer.transform(domSource, result);
					fs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}