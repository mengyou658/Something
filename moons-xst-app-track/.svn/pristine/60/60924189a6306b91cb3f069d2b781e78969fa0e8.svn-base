package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.buss.CycleHelper;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.LocationInfo;
import com.moons.xst.track.bean.TempMeasureBaseInfo;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.bean.Z_SpecCase;
import com.moons.xst.track.common.CyptoUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileMonitor;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.GrantHelper;
import com.moons.xst.track.common.LogUtils;
import com.moons.xst.track.common.MapManager;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.PowerSaveManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.SystemControlHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.common.WifiManagerHelper;
import com.moons.xst.track.common.factory.myworkfactory.MyWork;
import com.moons.xst.track.common.factory.myworkfactory.MyWorkFactory;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.service.BDGpsManagerService;
import com.moons.xst.track.service.CalculateCycleService;
import com.moons.xst.track.service.CommuJITService;
import com.moons.xst.track.service.GpsManagerService;
import com.moons.xst.track.service.RfidAIDLRemoteService;
import com.moons.xst.track.service.VoiceConvertService;
import com.moons.xst.track.service.WalkieTalkieService;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.SimpleMultiListViewDialog;
import com.moons.xst.track.widget.SimpleRadioListViewDialog;
import com.moons.xst.track.widget.SimpleTextDialog;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;
import com.moons.xst.track.xstinterface.SimpleRadioListViewDialogListener;

/**
 * 应用程序首页
 * 
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class Home extends BaseActivity {

	/*private String TAG = "Home";

	public static Home instance = null;

	// 路线列表长按菜单三项操作
	private static final int MENUACTION_DODJ = 0;// 执行点检
	private static final int MENUACTION_QUERY = 1;// 查询数据
	private static final int MENUACTION_RETURN = 2;// 返回
	private static final int MENUACTION_TEMPTASK = 3;// 临时任务

	public static final int PopMenu_LOGIN_OR_LOGOUT = 0;
	// public static final int PopMenu_CONFIG = 1;
	public static final int PopMenu_ABOUT = 1;
	public static final int PopMenu_QUIT = 2;

	private ImageButton fbMore; // 更多图标
	private QuickActionWidget mGrid;// 快捷栏控件

	private LoadingDialog loading;
	private Handler mHandler;

	private AppContext appContext;// 全局Context

	SharedPreferences preferences;

	private boolean isCeWen;// 是否显示测温
	private boolean isTwoTicket;// 是否显示二票
	private boolean isCeZhen;// 是否显示测振
	private boolean isCamera;// 是否显示相机
	private boolean isFiveTicket;// 是否显示五票

	private DoubleClickExitHelper mDoubleClickExitHelper;

	private ViewPager mTabPager;
	private View viewHome, viewCommu, viewTools, viewData;
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1, mTab2, mTab3, mTab4;
	private LinearLayout ll_Tab1, ll_Tab2, ll_Tab3, ll_Tab4;
	private TextView textView1, textView2, textView3, textView4;
	private RelativeLayout rl_cewen, rl_two, rl_cezhen, rl_camera, rl_five;
	private int zero = 0;// 动画图片偏移量
	public static int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;

	private RadioButton wirelessbutton;
	private RadioButton usbbutton;

	// 主页栏变量
	private DJLine_ListViewAdapter lvDJLineAdapter;
	private ListView lvDJLine;
	private EditText etSearch;

	Intent uploadDataIntent, calculateIntent, walkietalkieIntent, RFIDIntent;

	Handler fileMonitorHandler;
	FileMonitor fileMonitorobServer;

	public static final int REQCODE_PICTURE = 0;// 拍照
	public static final int REQCODE_VEDIO = 1;// 录像
	public static final int REQCODE_SOUND = 2;// 录音
	public static final int REQCODE_SEARCH = 3;// 搜索

	public static final int REQCODE_TEMPERATURE = 100;// 测温
	public static final int REQCODE_VIBRATION = 101;// 测振
	public static final int REQCODE_SPEED = 102;// 测转速

	private String imagePath;
	private String audioPath;
	private HashMap<Integer, Boolean> grantHashMap = new HashMap<Integer, Boolean>();
	NfcAdapter nfcAdapter;

	private TempMeasureBaseInfo mTempMeasureBaseInfo;
	private UUID uuid;

	// 通信栏变量
	// 工具栏变量
	// 数据栏变量

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		setCurrentVersion();
		grantHashMap = GrantHelper.loadSettingXml();
		// 程序两次点击退出事件
		mDoubleClickExitHelper = new DoubleClickExitHelper(this);
		appContext = (AppContext) getApplication();

		// 加载硬件模块配置文件
		LoadHardwareConfig();
		// 加载外接模式测量配置文件
		LoadMeasureTypeForOuterConfig();

		Bundle bundle = new Bundle();
		// 参数设置为开启实时上传，启动服务时参数赋值0
		if (appContext.getConfigJITUpload()) {
			bundle.putInt("Runstate", 0);
		} else { // 关闭实时上传，参数赋值2
			bundle.putInt("Runstate", 2);
		}
		// 初始化SharedPreferences用来存储显示哪些标签栏
		preferences = getSharedPreferences("share", this.MODE_PRIVATE);

		uploadDataIntent = new Intent(getApplicationContext(), CommuJITService.class);
		uploadDataIntent.putExtras(bundle);
		startService(uploadDataIntent);

		if (AppContext.getWalkieTalkieUseYN()) {
			walkietalkieIntent = new Intent(getApplicationContext(), WalkieTalkieService.class);
			startService(walkietalkieIntent);
		}

		AppContext.SetlineList(appContext.getLoginInfo().getUserLineListStr());
		AppContext.SetWSAddress(appContext.getWebServiceAddress(),
				appContext.getWebServiceAddressForWlan());
		AppContext.SetxjqCD(appContext.getxjqCD());
		AppContext.setUpdateSysDateType(appContext.getConfigUpdateSysDate());
		AppContext.setMeasureType(appContext.getConfigMeasureType());

		DJLineDAO.loadResultDataFileList();

		startFileObServer();
		// 初始化登录
		appContext.LoginInfo(true);
		// 初始化Head
		this.initHeadViewAndMoreBar();
		// 初始化弹出菜单
		this.initQuickActionGrid();

		currIndex = 0;
		// 初始化ViewPager
		this.initViewPager();
		// 加载任务数据
		this.loadUserDJLine();
		// 初始化搜索栏
		this.initSearchET();
		// 初始化标签栏
		this.initLabel();
		instance = this;
		LogUtils.WriteLog(TAG, 0);
		registerBoradcastReceiver();
		BinderService();
		
		if (appContext.isGpsOpen()) {
			if (!AppContext.locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(Home.this, "GPS未开启", Toast.LENGTH_LONG).show();
			}
		}
//		if (appContext.isGpsOpen() && !AppContext.isAskByStartApp) {
//			if (!AppContext.locationManager
//					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//				Intent gpsintent = new Intent(
//						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//				startActivity(gpsintent);
//			}
//		}
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		CycleHelper cychelper = new CycleHelper();
		cychelper.loadWorkDate();
		goNext();
		LoadEventTypeList();
	}

	*//**
	 * 加载事件类型
	 *//*
	private void LoadEventTypeList() {
		String jsonString = "";
		String filePathString = AppConst.XSTBasePath()
				+ AppConst.EventTypeXmlFile;

		try {
			jsonString = FileUtils.read(filePathString);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (!jsonString.equals(""))
			DownLoad.loadEventTypeList(jsonString);
	}

	long systimeset = 0;
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isComServiceRunning = false;
			String action = intent.getAction();
			if (action.equals("com.xst.track.service.gpsSetting")) {
				// 返回开启GPS导航设置界面
//				Intent gpsintent = new Intent(
//						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//				gpsintent.putExtra("return-data", false);
//				startActivity(gpsintent);
			} else if (action.equals("com.xst.track.service.updateSysDateTime")) {
				long sysDateTime = intent.getLongExtra("sysDateTime",
						System.currentTimeMillis());
				systimeset = sysDateTime;
				String sysDateTimeString = DateTimeHelper
						.DateToString(new Date(sysDateTime));
				new AlertDialog.Builder(Home.this)
						.setTitle("系统时间校准提示")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setCancelable(false)
						.setMessage(
								"系统时间已校准为：" + sysDateTimeString
										+ "，\n为保证程序正常运行你需要执行重启")
						.setPositiveButton("立即重启",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										stopService(uploadDataIntent);
										if (AppContext.getWalkieTalkieUseYN()) {
											stopService(walkietalkieIntent);
										}
										UnBindService();
										SystemControlHelper
												.setSystemDatatime(systimeset);
										AppManager.getAppManager()
												.finishAllActivity();
										Intent i = getBaseContext()
												.getPackageManager()
												.getLaunchIntentForPackage(
														getBaseContext()
																.getPackageName());
										i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(i);
									}
								}).show();
			} else if (action.equals(Intent.ACTION_TIME_TICK)) {
				ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				for (RunningServiceInfo service : manager
						.getRunningServices(100)) {
					if ("com.moons.xst.track.service.CommuJITService"
							.equals(service.service.getClassName())) {
						isComServiceRunning = true;
					}
				}
				if (!isComServiceRunning) {
					Bundle bundle = new Bundle();
					bundle.putInt("op", 0);
					Intent uploaIntent = new Intent(Home.this,
							CommuJITService.class);
					uploaIntent.putExtras(bundle);
					startService(uploaIntent);
				}
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.xst.track.service.gpsSetting");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);

		IntentFilter restartAppIntentFilter = new IntentFilter();
		restartAppIntentFilter
				.addAction("com.xst.track.service.updateSysDateTime");
		// 注册广播
		registerReceiver(mBroadcastReceiver, restartAppIntentFilter);

		IntentFilter restartComserviceIntentFilter = new IntentFilter();
		restartComserviceIntentFilter.addAction(Intent.ACTION_TIME_TICK);
		// 注册广播
		registerReceiver(mBroadcastReceiver, restartComserviceIntentFilter);
	}

	*//**
	 * 初始化ViewPager
	 *//*
	private void initViewPager() {
		mTabPager = (ViewPager) findViewById(R.id.hometabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mTab1 = (ImageView) findViewById(R.id.img_home);
		mTab2 = (ImageView) findViewById(R.id.img_commu);
		mTab3 = (ImageView) findViewById(R.id.img_tools);
		mTab4 = (ImageView) findViewById(R.id.img_data);
		ll_Tab1 = (LinearLayout) findViewById(R.id.ll_home);
		ll_Tab2 = (LinearLayout) findViewById(R.id.ll_commu);
		ll_Tab3 = (LinearLayout) findViewById(R.id.ll_tools);
		ll_Tab4 = (LinearLayout) findViewById(R.id.ll_data);

		textView1 = (TextView) findViewById(R.id.main_menu_home_txt);
		textView2 = (TextView) findViewById(R.id.main_menu_communication_txt);
		textView3 = (TextView) findViewById(R.id.main_menu_tools_txt);
		textView4 = (TextView) findViewById(R.id.main_menu_system_txt);

		ll_Tab1.setOnClickListener(new MyOnClickListener(0));
		ll_Tab2.setOnClickListener(new MyOnClickListener(1));
		ll_Tab3.setOnClickListener(new MyOnClickListener(2));
		ll_Tab4.setOnClickListener(new MyOnClickListener(3));

		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));

		Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
		int displayWidth = currDisplay.getWidth();
		int displayHeight = currDisplay.getHeight();
		one = displayWidth / 4; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;
		// Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);

		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		viewHome = mLi.inflate(R.layout.home_tab_home, null);
		viewCommu = mLi.inflate(R.layout.home_tab_commu, null);
		viewTools = mLi.inflate(R.layout.home_tab_tools, null);
		viewData = mLi.inflate(R.layout.home_tab_system, null);

		RelativeLayout rll_commu_commu = (RelativeLayout) (viewCommu
				.findViewById(R.id.ll_commu_receive));
		RelativeLayout rll_commu_appupdate = (RelativeLayout) (viewCommu
				.findViewById(R.id.ll_commu_appUpdate));
		RelativeLayout rll_commu_mapupdate = (RelativeLayout) (viewCommu
				.findViewById(R.id.ll_commu_mapUpdate));
		RelativeLayout rll_tool_nfc = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_readid));
		RelativeLayout rll_tool_captureScran = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_capture));
		RelativeLayout rll_tool_capture = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_Carema));
		RelativeLayout rll_tool_recorder = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_Recorder));
		RelativeLayout rll_tool_gps = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_GPS));
		RelativeLayout rll_tool_voice = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_Voice));
		RelativeLayout rll_sys_setting = (RelativeLayout) (viewData
				.findViewById(R.id.ll_sys_setting));
		RelativeLayout rll_tool_about = (RelativeLayout) (viewData
				.findViewById(R.id.ll_sys_aboutus));
		RelativeLayout rll_tool_temperature = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_cewen));
		RelativeLayout rll_tool_vibration = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_cezhen));
		RelativeLayout rll_tool_speed = (RelativeLayout) (viewTools
				.findViewById(R.id.ll_tools_cezs));
		rll_commu_commu.setVisibility(View.GONE);
		rll_commu_appupdate.setVisibility(View.GONE);
		rll_commu_mapupdate.setVisibility(View.GONE);
		rll_tool_nfc.setVisibility(View.GONE);
		rll_tool_captureScran.setVisibility(View.GONE);
		rll_tool_capture.setVisibility(View.GONE);
		rll_tool_recorder.setVisibility(View.GONE);
		rll_tool_gps.setVisibility(View.GONE);
		rll_tool_voice.setVisibility(View.GONE);
		rll_sys_setting.setVisibility(View.GONE);
		rll_tool_about.setVisibility(View.GONE);
		rll_tool_temperature.setVisibility(View.GONE);
		rll_tool_vibration.setVisibility(View.GONE);
		rll_tool_speed.setVisibility(View.GONE);
		if (grantHashMap == null || grantHashMap.size() <= 0) {
			UIHelper.ToastMessage(Home.this, "你未被授权使用任何一个模块");
		} else {

			if (grantHashMap.get(GrantHelper.Commu_Commu))
				rll_commu_commu.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Commu_AppUpdate))
				rll_commu_appupdate.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Commu_MapUpdate))
				rll_commu_mapupdate.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_NFC))
				rll_tool_nfc.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Scan))
				rll_tool_captureScran.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Carema))
				rll_tool_capture.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Recorder))
				rll_tool_recorder.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_GPS))
				rll_tool_gps.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Voice))
				rll_tool_voice.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Sys_Setting))
				rll_sys_setting.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Sys_About))
				rll_tool_about.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Temperature))
				rll_tool_temperature.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Vibration))
				rll_tool_vibration.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.Tools_Speed))
				rll_tool_speed.setVisibility(View.VISIBLE);
			if (grantHashMap.get(GrantHelper.XDJ))
				AppContext.setStartXDJYN(true);
			else {
				AppContext.setStartXDJYN(false);
			}

		}
		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(viewHome);
		views.add(viewCommu);
		views.add(viewTools);
		views.add(viewData);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public int getItemPosition(Object object) {
				return super.getItemPosition(object);
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			// @Override
			// public CharSequence getPageTitle(int position) {
			// return titles.get(position);
			// }

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		mTabPager.setCurrentItem(currIndex);

		wirelessbutton = (RadioButton) viewCommu
				.findViewById(R.id.radiowireless);
		wirelessbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppContext
						.setCommunicationType(AppConst.CommunicationType_Wireless);
			}
		});

		usbbutton = (RadioButton) viewCommu.findViewById(R.id.radiousb);
		usbbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppContext.setCommunicationType(AppConst.CommunicationType_USB);
			}
		});

		String str = getResources().getString(R.string.commu_type_desc);
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		// int bstart = str.indexOf("Tips");
		// int bend = bstart + "Tips".length();

		int fstart1 = str.indexOf("通信方式");
		int fend1 = fstart1 + "通信方式".length();

		int fstart2 = str.indexOf("当次通信");
		int fend2 = fstart2 + "当次通信".length();

		// style.setSpan(new
		// AbsoluteSizeSpan(30),bstart,bend,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#56abe4")),
				fstart1, fend1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#56abe4")),
				fstart2, fend2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		TextView tvTips = (TextView) viewCommu.findViewById(R.id.tv_tips_desc);
		tvTips.setText(style);
	}

	*//**
	 * 初始化标签栏
	 *//*
	private void initLabel() {
		rl_cewen = (RelativeLayout) viewHome.findViewById(R.id.rl_cewen);
		rl_two = (RelativeLayout) viewHome.findViewById(R.id.rl_two);
		rl_cezhen = (RelativeLayout) viewHome.findViewById(R.id.rl_cezhen);
		rl_camera = (RelativeLayout) viewHome.findViewById(R.id.rl_camera);
		rl_five = (RelativeLayout) viewHome.findViewById(R.id.rl_five);
	}

	*//**
	 * 初始化头部视图
	 *//*
	private void initHeadViewAndMoreBar() {

		TextView mUserName = (TextView) findViewById(R.id.home_head_username);

		if (appContext.isLogin()) {
			mUserName.setText(appContext.getLoginInfo().getUserName());
		} else {
			mUserName.setText("未登录");
		}

		fbMore = (ImageButton) findViewById(R.id.home_head_morebutton);
		fbMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// UIHelper.showTempTaskList(Home.this);

				// 展示快捷栏
//				UIHelper.showSettingLoginOrLogout(Home.this,
//						mGrid.getQuickAction(0));
				mGrid.show(v);
			}
		});
	}

	*//**
	 * 初始化快捷栏
	 *//*
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		// 登录或注销
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_login_icon, R.string.main_menu_login));
		*//**
		 * //参数设置 mGrid.addQuickAction(new MyQuickAction(this,
		 * R.drawable.main_pop_config_icon, R.string.main_menu_config));
		 **//*
		// 关于
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_about_icon, R.string.main_menu_about));
		// 退出
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_quit_icon, R.string.main_menu_quit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	*//**
	 * 快捷栏item点击事件
	 *//*
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case PopMenu_LOGIN_OR_LOGOUT:// 用户登录-注销
				if (appContext.isLogin()) {
					appContext.cleanLoginInfo();
					loadUserDJLine();
					initHeadViewAndMoreBar();
				} else {
					UIHelper.showLoginDialog(Home.this);
				}
				break;
			case PopMenu_ABOUT:// 关于
				UIHelper.showAbout(Home.this);
				// UIHelper.showTempTask(Home.this);
				break;
			case PopMenu_QUIT:// 退出
				Dialog dialog = new AlertDialog.Builder(Home.this)
						.setTitle(R.string.app_menu_logouttitle)
						.setMessage(R.string.app_menu_surelogout)
						// 相当于点击确认按钮
						.setPositiveButton(R.string.sure,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										stopService(uploadDataIntent);
										if (AppContext.getWalkieTalkieUseYN()) {
											stopService(walkietalkieIntent);
										}
										 根据节电设置，关闭相应的模块 
										PowerSaveManager.getPowerSaveManager(
												Home.this).close();
										AppManager.getAppManager().AppExit(
												Home.this);
									}
								})
						// 相当于点击取消按钮
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).create();
				dialog.show();

				break;
			}
		}
	};

	*//**
	 * ViewPager图标点击监听
	 *//*
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	
	 * 页卡切换监听(原作者:D.Winter)
	 
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			if (arg0 != currIndex) {
				changeTabPage(arg0);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	*//**
	 * @param arg0
	 * @throws NotFoundException
	 *//*
	private void changeTabPage(int arg0) throws NotFoundException {
		Animation animation = null;
		switch (arg0) {
		case 0:
			mTab1.setImageDrawable(getResources().getDrawable(
					R.drawable.tab_home_pressed));
			textView1.setTextColor(getResources().getColor(
					R.color.buttombuttonColor));
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_commu_normal));
				textView2.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_tools_normal));
				textView3.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, 0, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sys_normal));
				textView4.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			}
			checkLoginAfterCommunication();
			refreshLineAfterCommunication();
			break;
		case 1:
			mTab2.setImageDrawable(getResources().getDrawable(
					R.drawable.tab_commu_pressed));
			textView2.setTextColor(getResources().getColor(
					R.color.buttombuttonColor));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, one, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_home_normal));
				textView1.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_tools_normal));
				textView3.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, one, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sys_normal));
				textView4.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			}

			if (appContext.getConfigCommunicationType().equals(
					AppConst.CommunicationType_Wireless)) {
				AppContext
						.setCommunicationType(AppConst.CommunicationType_Wireless);
				wirelessbutton.setChecked(true);
				usbbutton.setChecked(false);
			} else {
				AppContext.setCommunicationType(AppConst.CommunicationType_USB);
				wirelessbutton.setChecked(false);
				usbbutton.setChecked(true);
			}
			break;
		case 2:
			mTab3.setImageDrawable(getResources().getDrawable(
					R.drawable.tab_tools_pressed));
			textView3.setTextColor(getResources().getColor(
					R.color.buttombuttonColor));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, two, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_home_normal));
				textView1.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_commu_normal));
				textView2.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, two, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sys_normal));
				textView4.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			}
			break;
		case 3:
			mTab4.setImageDrawable(getResources().getDrawable(
					R.drawable.tab_sys_pressed));
			textView4.setTextColor(getResources().getColor(
					R.color.buttombuttonColor));
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, three, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_home_normal));
				textView1.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, three, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_commu_normal));
				textView2.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, three, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_tools_normal));
				textView3.setTextColor(getResources().getColor(
						R.color.buttombuttonColorNormal));
			}
			checkLoginAfterCommunication();
			break;
		}

		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
		mTabImg.startAnimation(animation);
		changeViewToPager(arg0);
	}

	private void changeViewToPager(int arg0) {

		switch (arg0) {
		case 0:// 主页
			break;
		case 1:// 通信
			break;
		case 2:// 工具
			break;
		case 3:// 数据
			break;
		}
		currIndex = arg0;

	}

	*//**
	 * 初始化路线列表
	 *//*
	private void loadMyDJLine() {
		DJLineDAO djlineDAO = new DJLineDAO();
		AppContext.DJLineBuffer = DJLineDAO.loadDJLineByUser(appContext
				.getLoginUid());
		if (AppContext.getStartXDJYN())
			DJPlanHelper.GetIntance().loadDJSpecCase(appContext,
					AppContext.DJLineBuffer);
	}

	private void bindingUserDJLine() {

		//
		lvDJLineAdapter = new DJLine_ListViewAdapter(this,
				AppContext.DJLineBuffer, R.layout.listitem_djline);

		lvDJLine = (ListView) viewHome.findViewById(R.id.home_listview_djline);
		if (lvDJLine == null) {
			return;
		}

		lvDJLine.setAdapter(lvDJLineAdapter);

		lvDJLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// View v = lvDJLine.getChildAt(position);
				// if (v == null) {
				// return;
				// }
				TextView aTitle = (TextView) view
						.findViewById(R.id.djline_listitem_title);
				if (aTitle == null) {
					return;
				}
				final DJLine adjline = (DJLine) aTitle.getTag();

				if (adjline == null)
					return;

				enterSwictherModule(view.getContext(), adjline);
			}
		});

		lvDJLine.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				//
				menu.setHeaderTitle(R.string.main_menu_home_lvMemu);
				menu.add(0, MENUACTION_DODJ, 0,
						R.string.main_menu_home_lvMemu_toXX);
				menu.add(0, MENUACTION_QUERY, 0,
						R.string.main_menu_home_lvMemu_Query);
				menu.add(0, MENUACTION_TEMPTASK, 0,
						R.string.main_menu_home_lvMemu_TempTask);
				menu.add(0, MENUACTION_RETURN, 0,
						R.string.main_menu_home_Return);
			}
		});

	}

	*//**
	 * 根据登录的人员信息刷新相关数据
	 *//*
	private void loadUserDJLine() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded
					bindingUserDJLine();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(Home.this);
				}
			}
		};
		this.loadUserDJLineThread(false);
	}

	*//**
	 * 初始化搜索栏
	 *//*
	private void initSearchET() {
		etSearch = (EditText) (viewHome).findViewById(R.id.search_content);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO 自动生成的方法存根
				if (hasFocus) {
					UIHelper.showSearchMyWork(Home.this, REQCODE_SEARCH);
				}
			}
		});
	}

	*//**
	 * --跳转控制
	 * 
	 * --主界面进入具体界面由这里控制
	 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线;8-条件巡检
	 * 
	 * @author LKZ
	 *//*
	private void enterSwictherModule(Context context, final DJLine adjline) {
		// AppContext.SetCurrLineID(adjline.getLineID());
		// AppContext.setCurrLineType(adjline.getlineType());
		if (!FileUtils.checkFileExists(AppConst.XSTDBPath()
				+ AppConst.PlanDBName(adjline.getLineID()))) {
			UIHelper.ToastMessage(Home.this,
					R.string.home_line_noplanfile_notice);
			return;
		}

		UIHelper.enterMyWork(context, adjline);

		
		 * if (adjline.getlineType() == 1 || adjline.getlineType() == 0) {
		 * enterXDJLine(adjline); } else if (adjline.getlineType() == (4))
		 * UIHelper.showMainMap(context, adjline.getLineID(),
		 * adjline.getLineName()); else if (adjline.getlineType() == (6)) { if
		 * (AppContext.locationManager == null) AppContext.locationManager =
		 * (LocationManager) getSystemService(Context.LOCATION_SERVICE); //
		 * 判断GPS是否正常启动 if (!AppContext.locationManager
		 * .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		 * Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show(); //
		 * 返回开启GPS导航设置界面 Intent intent = new Intent(
		 * Settings.ACTION_LOCATION_SOURCE_SETTINGS); startActivity(intent); }
		 * else { testlineid = adjline.getLineID(); testlinename =
		 * adjline.getLineName(); UIHelper.showMainMap(context,
		 * adjline.getLineID(), adjline.getLineName()); } } else if
		 * (adjline.getlineType() == (8)) { UIHelper.showDJCondition(context,
		 * String.valueOf(adjline.getLineID()), adjline.getLineName(), "0"); }
		 * else {
		 * 
		 * UIHelper.showMainMap(context, adjline.getLineID(),
		 * adjline.getLineName()); }
		 
	}

	*//**
	 * 执行巡点检路线
	 * 
	 * @param adjline
	 *//*
	private void enterXDJLine(final DJLine adjline) {
		AppContext.voiceConvertService.Speaking("您将要执行点检操作，路线名称为："
				+ adjline.getLineName());
		AppContext.DJSpecCaseFlag = 0;
		if (adjline.getSpecCaseYN()) {
			String selfTitleContent = adjline.getLineName();
			final List<List<String>> data = new ArrayList<List<String>>();
			List<String> _temList = new ArrayList<String>();
			_temList.add("0");
			_temList.add(this.getString(R.string.line_xjmode_normal));
			_temList.add("1");
			data.add(_temList);
			_temList = new ArrayList<String>();
			_temList.add("1");
			_temList.add(this.getString(R.string.line_xjmode_spec));
			_temList.add("0");
			data.add(_temList);
			SimpleRadioListViewDialog _Dialog = new SimpleRadioListViewDialog(
					Home.this, new SimpleRadioListViewDialogListener() {

						@Override
						public void refreshParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void goBackToParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void btnOK(DialogInterface dialog) {
							// TODO 自动生成的方法存根
							if (data != null && data.size() > 0) {
								List<String> checkedItem = new ArrayList<String>();
								for (List<String> _item : data) {
									if (_item.get(2).equals("1")) {
										checkedItem = _item;
										break;
									}
								}
								Integer index = Integer.parseInt(checkedItem
										.get(0));
								if (index == 1) {
									AppContext.DJSpecCaseFlag = 1;
									selectSpecCase(adjline);
								} else {
									AppContext.DJSpecCaseFlag = 0;
									if (AppContext.debug) {
										UIHelper.showDianjianTouchIDPos(
												Home.this, adjline.getLineID(),
												adjline.getLineName(), false);
									} else {
										if (nfcAdapter == null) {
											UIHelper.ToastMessage(
													getBaseContext(),
													"设备不支持NFC！");
										} else if (!nfcAdapter.isEnabled()) {
											UIHelper.ToastMessage(
													getBaseContext(),
													"请在系统设置中先启用NFC功能！");
										} else {
											UIHelper.showDianjianTouchIDPos(
													Home.this,
													adjline.getLineID(),
													adjline.getLineName(),
													false);
										}
									}
								}
							}
							dialog.dismiss();
						}

						@Override
						public void btnCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}, data, selfTitleContent);
			_Dialog.setTitle("请选择");
			_Dialog.show();
		} else {
			UIHelper.showDianjianTouchIDPos(Home.this, adjline.getLineID(),
					adjline.getLineName(), false);
		}
	}

	private void selectSpecCase(final DJLine adjline) {
		ArrayList<Z_SpecCase> specCaselist = AppContext.DJSpecCaseBuffer
				.get(String.valueOf(adjline.getLineID()));
		final List<List<String>> data = new ArrayList<List<String>>();
		data.clear();
		for (Z_SpecCase _item : specCaselist) {
			List<String> _temList = new ArrayList<String>();
			_temList.add(_item.getSpecCase_ID());
			_temList.add(_item.getName_TX());
			_temList.add("0");
			data.add(_temList);
		}
		SimpleMultiListViewDialog dialog = new SimpleMultiListViewDialog(
				Home.this, new SimpleMultiListViewDialogListener() {

					@Override
					public void refreshParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void goBackToParentUI() {
						// TODO 自动生成的方法存根

					}

					@Override
					public void btnOK(DialogInterface dialog,
							List<List<String>> mData) {
						byte[] code = new byte[30];
						AppContext.selectedDJSpecCaseStr = GetSpecCode(code,
								data);
						// TODO 自动生成的方法存根
						if (AppContext.debug) {
							UIHelper.showDianjianTouchIDPos(Home.this,
									adjline.getLineID(), adjline.getLineName(),
									false);
						} else {
							if (nfcAdapter == null) {
								UIHelper.ToastMessage(getBaseContext(),
										"设备不支持NFC！");
							} else if (!nfcAdapter.isEnabled()) {
								UIHelper.ToastMessage(getBaseContext(),
										"请在系统设置中先启用NFC功能！");
							} else {
								UIHelper.showDianjianTouchIDPos(Home.this,
										adjline.getLineID(),
										adjline.getLineName(), false);
							}
						}
						dialog.dismiss();
					}
				}, data, false, "请选择特巡条件");
		dialog.setTitle("特巡条件列表");
		dialog.show();
	}

	private String GetSpecCode(byte[] b, List<List<String>> mData) {
		String ret = "";
		for (int idx = 0; idx < b.length; idx++) {
			if (idx < mData.size()) {
				if (mData.get(idx).get(2).equals("1")) {
					ret += "1";
				} else if (mData.get(idx).get(2).equals("0")) {
					ret += "0";
				}
			} else {
				ret += "0";
			}
		}
		return ret;
	}

	Intent intent, gpsIntent, voiceIntent;
	ServiceConnection calcuCon, gpsCon, voiceCon;

	*//**
	 * 以Binding方式启动的启动服务
	 *//*
	private void BinderService() {
		intent = new Intent(getApplicationContext(), CalculateCycleService.class);
		calcuCon = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName,
					IBinder binder) {
				// 调用bindService方法启动服务时候，如果服务需要与activity交互，
				// 则通过onBind方法返回IBinder并返回当前本地服务
				AppContext.calculateCycleService = ((CalculateCycleService.LocalBinder) binder)
						.getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				AppContext.calculateCycleService = null;
				// 这里可以提示用户
			}
		};
		bindService(intent, calcuCon, Context.BIND_AUTO_CREATE);
		if (appContext.isGpsOpen()) {
			gpsCon = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName componentName,
						IBinder binder) {
					// 调用bindService方法启动服务时候，如果服务需要与activity交互，
					// 则通过onBind方法返回IBinder并返回当前本地服务
				}

				@Override
				public void onServiceDisconnected(ComponentName componentName) {
					AppContext.locationManager = null;
					// 这里可以提示用户
				}
			};
			gpsIntent = new Intent(getApplicationContext(), BDGpsManagerService.class);
			bindService(gpsIntent, gpsCon, Context.BIND_AUTO_CREATE);
		}
		voiceIntent = new Intent(getApplicationContext(), VoiceConvertService.class);
		voiceCon = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName componentName,
					IBinder binder) {
				// 调用bindService方法启动服务时候，如果服务需要与activity交互，
				// 则通过onBind方法返回IBinder并返回当前本地服务
				AppContext.voiceConvertService = ((VoiceConvertService.LocalBinder) binder)
						.getService();
			}

			@Override
			public void onServiceDisconnected(ComponentName componentName) {
				AppContext.locationManager = null;
				// 这里可以提示用户
			}
		};
		bindService(voiceIntent, voiceCon, Context.BIND_AUTO_CREATE);
	}

	*//**
	 * 以UnBindService方式停止的启动服务
	 *//*
	private void UnBindService() {
		intent = new Intent(this, CalculateCycleService.class);
		if (calcuCon != null)
			unbindService(calcuCon);
		if (gpsCon != null)
			unbindService(gpsCon);
		if (voiceCon != null)
			unbindService(voiceCon);
	}

	private void loadUserDJLineThread(final boolean isRefresh) {
		loading = new LoadingDialog(this);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载路线
					loadMyDJLine();

					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	public void btn_capture(View v) {
		UIHelper.showCamera(Home.this);
	}

	public void btn_Media(View v) {
		UIHelper.showMediaRecorder(Home.this);
	}

	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQCODE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				// File file = new File(imagePath);
				// file.delete();
			}
			break;
		case REQCODE_SOUND:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String path = UriToPath(uri);
				copyFile(path, audioPath);
				File file = new File(path);
				file.delete();

				TempMeasureDBHelper.GetIntance().InsertTempMeasureBaseInfo(
						Home.this, mTempMeasureBaseInfo);
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				File file = new File(audioPath);
				file.delete();
			}
			break;
		case REQCODE_SEARCH:
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				DJLine djline = (DJLine) (bundle.getSerializable("djline"));
				enterSwictherModule(Home.this, djline);
			}
			break;
		default:
			break;
		}

	}

	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
	}

	public String UriToPath(Uri uri) {
		String path = null;
		ContentResolver cr = this.getContentResolver();
		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();
		if (cursor != null) {
			path = cursor.getString(1);
			cursor.close();
		}
		return path;
	}

	public void tv_twoTickets(View v) {
		UIHelper.showTwoTickets(Home.this);
	}

	public void btn_NFCScran(View v) {
		UIHelper.showNFCTool(Home.this);
	}

	public void btn_Voice(View v) {
		UIHelper.showVoice(Home.this);
	}

	public void btn_captureScran(View v) {
		UIHelper.showCapture(Home.this);
	}

	public void btn_cewen(View v) {
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLWDXC(Home.this, "", false, REQCODE_TEMPERATURE,
				clModuleType, AppContext.getBlueToothAddressforTemperature(),
				AppContext.getBTConnectPwdforTemperature(), 0);
	}

	public void btn_cezhen(View v) {
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLZDXC(Home.this, "", false, REQCODE_VIBRATION,
				clModuleType, AppContext.getBlueToothAddressforVibration(),
				AppContext.getBTConnectPwdforVibration(), 0);
	}

	public void btn_cezs(View v) {
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLZSXC(Home.this, "", false, REQCODE_SPEED, clModuleType,
				0);
	}

	public void btn_recorder(View v) {
		new DateFormat();
		// String audioname = DateFormat.format("yyyyMMdd_hhmmss",
		// Calendar.getInstance(Locale.CHINA))
		// + ".arm";
		uuid = UUID.randomUUID();
		Date dt = DateTimeHelper.GetDateTimeNow();
		String audioname = uuid.toString() + ".arm";

		mTempMeasureBaseInfo = new TempMeasureBaseInfo();
		mTempMeasureBaseInfo.setGUID(uuid.toString());
		mTempMeasureBaseInfo.setFileName(DateTimeHelper.DateToString(dt,
				"yyyyMMddHHmmss"));
		mTempMeasureBaseInfo.setCreateTime(DateTimeHelper.DateToString(dt,
				"yyyy-MM-dd HH:mm:ss"));
		mTempMeasureBaseInfo.setMeasureType("LY");
		mTempMeasureBaseInfo.setExpand1(AppContext.GetxjqCD());
		mTempMeasureBaseInfo.setExpand2("LC");

		audioPath = AppConst.XSTTempImagePath() + audioname;
		File f = new File(audioPath);
		File destDir = new File(AppConst.CurrentResultPath_Record(AppContext
				.GetCurrLineID()));
		try {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			f.createNewFile();
			// 这行代码很重要，没有的话会因为写入权限不够出一些问题
			f.setWritable(true, false);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("", e.toString());
		}
		Intent intentFromRecord = new Intent(
				MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		// intentFromRecord.setType("audio/*");
		// intentFromRecord.setAction(Intent.ACTION_GET_CONTENT);
		intentFromRecord.putExtra("return-data", true);
		startActivityForResult(intentFromRecord, REQCODE_SOUND);
	}

	public void btn_planDownLoad(View v) {
		if (AppContext.getCommunicationType().equals(
				AppConst.CommunicationType_Wireless)) {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				UIHelper.ToastMessage(getBaseContext(),
						R.string.network_not_connected);
				return;
			}
			if (NetWorkHelper.checkAppRunBaseCase(getBaseContext()))
				UIHelper.showCommuDownLoad(Home.this);
		} else {
			if (StringUtils.isEmpty(AppContext.GetxjqCD())) {
				UIHelper.ToastMessage(Home.this, "请正确设置巡检器标识符后继续操作");
				return;
			}
			UIHelper.showUSBCommuDownLoad(Home.this);
		}
	}

	public void btn_Setting(View v) {
		UIHelper.showSetting(Home.this);
	}

	public void btn_AboutUS(View v) {
		UIHelper.showAboutUS(Home.this);
	}

	public void btn_appUpdate(View v) {
		if (NetWorkHelper.checkAppRunBaseCase(getBaseContext())) {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				UIHelper.ToastMessage(getBaseContext(),
						R.string.network_not_connected);
				return;
			}
			UpdateManager.getUpdateManager().checkAppUpdate(Home.this, true);
		}
	}

	*//**
	 * 从SD卡导入离线地图安装包
	 *//*
	public void btn_mapUpdate(View view) {
		int cityID = 0;
		getLocationInfo();
		try {
			cityID = Integer.parseInt(AppContext.getLocationInfo()
					.getcityCode());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (NetWorkHelper.checkAppRunBaseCase(getBaseContext())) {
			if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
				UIHelper.ToastMessage(getBaseContext(),
						R.string.network_not_connected);
				return;
			}
			MapManager.getMapManager().UpdateBaiduMap(Home.this, cityID, true);
			// MapManager.getMapManager().UpdateBaiduMap(Home.this, 131, true);
		}
	}

	String _locationInfoString = null;

	private void getLocationInfo() {
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				_locationInfoString = WebserviceFactory.getLocationInfo();
				// TODO Auto-generated method stub
			}
		}).start();
		// String _locationInfoString = WebserviceFactory.getLocationInfo();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!StringUtils.isEmpty(_locationInfoString)) {
			LocationInfo info = new LocationInfo();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				StringReader stringReader = new StringReader(
						_locationInfoString);
				InputSource inputSource = new InputSource(stringReader);
				DocumentBuilder db = dbf.newDocumentBuilder();
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
							if (node.getNodeName().toUpperCase().equals("CITY")) {
								cityName = String
										.valueOf(node.getTextContent());
							} else if (node.getNodeName().toUpperCase()
									.equals("COUNTRY")) {
								countryName = node.getTextContent();
							} else if (node.getNodeName().toUpperCase()
									.equals("DISTRICT")) {
								districtName = String.valueOf(node
										.getTextContent());
							} else if (node.getNodeName().toUpperCase()
									.equals("PROVINCE")) {
								provinceName = String.valueOf(node
										.getTextContent());
							} else if (node.getNodeName().toUpperCase()
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
				tableList = doc.getElementsByTagName("result");
				for (int i = 0; i < tableList.getLength(); i++) {
					Node table = tableList.item(i);
					Element elem = (Element) table;
					String cityCode = "";
					for (Node node = table.getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							if (node.getNodeName().toUpperCase()
									.equals("CITYCODE")) {
								cityCode = String
										.valueOf(node.getTextContent());
							}
						}
					}
					info.setcityCode(cityCode);
					Log.e("citicode", cityCode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			AppContext.setLocationInfo(info);
		}
	}

	public void btn_upLoad(View v) {
		// UIHelper.showSetting(Home.this);
		// UIHelper.showInputBug(Home.this,1);
	}

	public void btn_GPS(View v) {
		UIHelper.showGPSTool(Home.this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
			// back键
			// 是否退出应用
			boolean bet = mDoubleClickExitHelper.onKeyDown(keyCode, event);
			if (bet) {
				stopService(uploadDataIntent);
				if (AppContext.getWalkieTalkieUseYN()) {
					stopService(walkietalkieIntent);
				}
			}
			return bet;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 判断显示哪些标签栏
		isCeWen = preferences.getBoolean("isCeWen", false);
		isTwoTicket = preferences.getBoolean("isTwoTicket", false);
		isCeZhen = preferences.getBoolean("isCeZhen", false);
		isCamera = preferences.getBoolean("isCamera", false);
		isFiveTicket = preferences.getBoolean("isFiveTicket", false);
		if (isCeWen)
			rl_cewen.setVisibility(View.VISIBLE);
		else
			rl_cewen.setVisibility(View.GONE);
		if (isTwoTicket)
			rl_two.setVisibility(View.VISIBLE);
		else
			rl_two.setVisibility(View.GONE);
		if (isCeZhen)
			rl_cezhen.setVisibility(View.VISIBLE);
		else
			rl_cezhen.setVisibility(View.GONE);
		if (isCamera)
			rl_camera.setVisibility(View.VISIBLE);
		else
			rl_camera.setVisibility(View.GONE);
		if (isFiveTicket)
			rl_five.setVisibility(View.VISIBLE);
		else
			rl_five.setVisibility(View.GONE);
		LogUtils.WriteLog(TAG, 0);
		etSearch.clearFocus();
		// 读取左右滑动配置
		// mScrollLayout.setIsScroll(appContext.isScroll());
		if (!AppContext.ModTestYN)
			return;
		try {
			Thread.sleep(5000);
			if (testlineid > 0 && (!StringUtils.isEmpty(testlinename)))
				Modtest();
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(uploadDataIntent);
		if (AppContext.getWalkieTalkieUseYN()) {
			stopService(walkietalkieIntent);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (appContext.isLogin()) {
			AppContext.SetlineList(appContext.getLoginInfo()
					.getUserLineListStr());
			AppContext.SetWSAddress(appContext.getWebServiceAddress(),
					appContext.getWebServiceAddressForWlan());
			AppContext.SetxjqCD(appContext.getxjqCD());
			// 初始化登录
			appContext.LoginInfo(false);
			// 初始化Head
			this.initHeadViewAndMoreBar();
			// 加载路线数据
			this.loadUserDJLine();
		}
	}

	*//**
	 * 创建menu TODO 停用原生菜单
	 *//*
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int po = lvDJLine.getFirstVisiblePosition();

		View v = (View) lvDJLine.getChildAt(info.position - po);
		DJLine adjline = null;
		if (v == null) {
			return false;
		}
		TextView aTitle = (TextView) v.findViewById(R.id.djline_listitem_title);
		if (aTitle == null) {
			return false;
		}
		adjline = (DJLine) aTitle.getTag();

		if (adjline == null) {
			return false;
		}
		switch (item.getItemId()) {
		case MENUACTION_DODJ:
			enterSwictherModule(this, adjline);
			break;
		case MENUACTION_QUERY:
			if (adjline.getlineType() == 0) {
				UIHelper.showDJQuerydata(this, adjline);
				// UIHelper.showDJQuerydata(this, adjline.getLineID(),
				// adjline.getLineName());
			} else if (adjline.getlineType() == 6 || adjline.getlineType() == 7) {
				UIHelper.showQuerydata(this, adjline);
			} else if (adjline.getlineType() == 8) {
				UIHelper.showDJQuerydata(this, adjline);
				// UIHelper.showDJQuerydata(this, adjline.getLineID(),
				// adjline.getLineName());
			}
			break;

		case MENUACTION_TEMPTASK:
			UIHelper.showTempTaskList(this);
			break;

		case MENUACTION_RETURN:
			return false;
		}

		return super.onContextItemSelected(item);
	}

	private void checkLoginAfterCommunication() {
		if (AppContext.GetDownLoadYN()) {
			if (appContext.isLogin()) {
				User loginUser = appContext.getLoginInfo();
				if (checkUserChanged(loginUser)) {
					appContext.cleanLoginInfo();
					loadUserDJLine();
					initHeadViewAndMoreBar();
					AppContext.SetDownLoadYN(false);
				}
			}

		}
	}

	private void refreshLineAfterCommunication() {
		if (AppContext.GetRefreshLineYN()) {
			if (appContext.isLogin()) {
				loadUserDJLine();
				AppContext.SetRefreshLineYN(false);
			}
		}
	}

	*//**
	 * 验证当前登录用户是否变更
	 * 
	 * @author wangyong
	 *//*
	private static boolean checkUserChanged(User user) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			String path = AppConst.XSTBasePath() + AppConst.UserXmlFile;
			String temppath = AppConst.XSTBasePath() + AppConst.tempUserXmlFile;
			FileEncryptAndDecrypt.decrypt(path, temppath);
			File f = new File(temppath);

			Document doc = db.parse(f);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Z_AppUser");
			for (int i = 0; i < tableList.getLength(); i++) {
				Node table = tableList.item(i);
				int userID = 0;
				String AppAccount_TX = "";
				String AppUser_CD = "";
				String Name_TX = "";
				String XJQPWD_TX = "";
				String UserAccess_TX = "";
				String lineListString = "";
				ArrayList<String> LineList = new ArrayList<String>();
				for (Node node = table.getFirstChild(); node != null; node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						if (node.getNodeName().toUpperCase()
								.equals("APPUSER_ID")) {
							userID = Integer.valueOf(node.getTextContent())
									.intValue();
						} else if (node.getNodeName().toUpperCase()
								.equals("APPACCOUNT_TX")) {
							AppAccount_TX = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("APPUSER_CD")) {
							AppUser_CD = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("NAME_TX")) {
							Name_TX = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("XJQPWD_TX")) {
							XJQPWD_TX = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("USERACCESS_TX")) {
							UserAccess_TX = node.getTextContent();
						} else if (node.getNodeName().toUpperCase()
								.equals("LINELIST")) {
							lineListString = node.getTextContent();
							if ((!StringUtils.isEmpty(lineListString))
									&& lineListString.contains(";")) {
								String[] _LineList = lineListString.split(";");
								LineList.clear();
								for (String _line : _LineList) {
									if (!StringUtils.isEmpty(_line)) {
										LineList.add(_line);
									}
								}
							}
						}
					}
				}
				if (user.getUserAccount().equals(AppAccount_TX)) {
					if ((StringUtils.isEmpty(XJQPWD_TX) || CyptoUtils.MD5(
							user.getUserPwd()).equals(XJQPWD_TX))
							&& user.getUserID() == userID
							&& user.getUserCD().equals(AppUser_CD)
							&& user.getUserName().equals(Name_TX)
							&& user.getUserAccess().equals(UserAccess_TX)
							&& user.getUserLineListStr().equals(lineListString)) {
						return false;
					} else {
						return true;
					}
				}
			}
			f.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	*//**
	 * 获取当前客户端版本信息
	 *//*
	private void setCurrentVersion() {
		try {
			AppContext.xstTrackPackageInfo = getBaseContext()
					.getPackageManager().getPackageInfo(
							getBaseContext().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	private void startFileObServer() {
		fileMonitorHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {

					final int msgwhat = msg.what;
					final String filepath = String.valueOf(msg.obj);
					// TODO 自动生成的方法存根
					File _file = new File(filepath);
					boolean sdfYN = (!StringUtils.isEmpty(_file.getName()))
							&& _file.getName().toUpperCase().endsWith(".SDF");
					int lineID = sdfYN ? Integer
							.parseInt(_file.getName().toUpperCase()
									.replace(".SDF", "").replace("D", "")) : 0;
					if (msgwhat == FileObserver.CREATE) {
						if (sdfYN) {
							DJLine djLine = new DJLine();
							djLine.setLineID(lineID);
							djLine.setResultDBPath(filepath);
							djLine.setPlanDBPath(AppConst.XSTDBPath()
									+ File.separator
									+ AppConst.PlanDBName(lineID));
							boolean exit = false;
							if (AppContext.resultDataFileListBuffer != null
									&& AppContext.resultDataFileListBuffer
											.size() > 0) {
								for (int i = 0; i < AppContext.resultDataFileListBuffer
										.size(); i++) {
									if (AppContext.resultDataFileListBuffer
											.get(i).getLineID() == lineID) {
										exit = true;
										break;
									}
								}
							}
							if (!exit) {
								AppContext.resultDataFileListBuffer.add(djLine);
								LogUtils.saveLog(
										"File Create:-"
												+ AppConst.ResultDBName(djLine
														.getLineID()), "JIT");
							}
						}
					} else if (msgwhat == FileObserver.DELETE
							|| msgwhat == FileObserver.DELETE_SELF) {
						if (sdfYN) {
							if (AppContext.resultDataFileListBuffer != null
									&& AppContext.resultDataFileListBuffer
											.size() > 0) {
								for (int i = 0; i < AppContext.resultDataFileListBuffer
										.size(); i++) {
									if (AppContext.resultDataFileListBuffer
											.get(i).getLineID() == lineID) {
										AppContext.resultDataFileListBuffer
												.remove(i);
										LogUtils.saveLog(
												"File Remove:-"
														+ AppConst
																.ResultDBName(lineID),
												"JIT");
										break;
									}
								}

							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		fileMonitorobServer = new FileMonitor(AppConst.XSTResultPath(),
				fileMonitorHandler);
		fileMonitorobServer.startWatching();
	}

	int testlineid;
	String testlinename;

	private void Modtest() {
		if (AppContext.ModTestYN)
			UIHelper.showMainMapTest(this, testlineid, testlinename);
	}

	*//**
	 * 新装/升级APP情况下的跳转
	 *//*
	private void goNext() {
		if (AppContext.isNewInstall) {
			final SimpleTextDialog _dialog = new SimpleTextDialog(Home.this,
					"提示", getString(R.string.appstart_notice2));
			_dialog.setTextSize(22);
			_dialog.setOKButton(R.string.simple_text_dialog_btnok,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNewInstall = false;
							UIHelper.showSetting(Home.this);
							_dialog.dismiss();

						}
					});
			_dialog.setCancelButton(R.string.simple_text_dialog_btncancel,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNewInstall = false;
							_dialog.dismiss();
						}
					});
			_dialog.show();
		} else if (AppContext.isNowUpdate) {
			final SimpleTextDialog _dialog = new SimpleTextDialog(Home.this,
					"提示", getString(R.string.appstart_notice1));
			_dialog.setTextSize(22);
			_dialog.setOKButton(R.string.simple_text_dialog_btnok,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNowUpdate = false;
							UIHelper.showCommuDownLoad(Home.this);
							_dialog.dismiss();
						}
					});
			_dialog.setCancelButton(R.string.simple_text_dialog_btncancel,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNowUpdate = false;
							_dialog.dismiss();
						}
					});
			_dialog.show();
		}
	}

	private void LoadMeasureTypeForOuterConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.MeasureTypeForOuterConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("Temperature")) {
					AppContext.setBlueToothAddressforTemperature(element
							.getAttribute("BTAddress"));
					AppContext.setBTConnectPwdforTemperature(element
							.getAttribute("Password"));
				} else if (element.getAttribute("Name").equals("Vibration")) {
					AppContext.setBlueToothAddressforVibration(element
							.getAttribute("BTAddress"));
					AppContext.setBTConnectPwdforVibration(element
							.getAttribute("Password"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void LoadHardwareConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.HardwareConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				if (element.getAttribute("Name").equals("temperature")) {
					AppContext.setTemperatureUseYN(StringUtils.toBool(element
							.getAttribute("Values")));
				} else if (element.getAttribute("Name").equals("vibration")) {
					AppContext.setVibrationUseYN(StringUtils.toBool(element
							.getAttribute("Values")));
				} else if (element.getAttribute("Name").equals("speed")) {
					AppContext.setSpeedUseYN(StringUtils.toBool(element
							.getAttribute("Values")));
				} else if (element.getAttribute("Name").equals("walkietalkie")) {
					AppContext.setWalkieTalkieUseYN(StringUtils.toBool(element
							.getAttribute("Values")));
				} else if (element.getAttribute("Name").equals("rfid")) {
					AppContext.setRFIDUseYN(StringUtils.toBool(element
							.getAttribute("Values")));
				} else if (element.getAttribute("Name").equals("rotateCamera")) {
					AppContext.setRotateCameraYN(StringUtils.toBool(element
							.getAttribute("Values")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
