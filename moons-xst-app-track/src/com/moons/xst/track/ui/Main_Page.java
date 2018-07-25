package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppContext.LoginType;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.FragmentViewPagerAdapter;
import com.moons.xst.track.api.ApiClient;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.Result;
import com.moons.xst.track.bean.TempMeasureBaseInfo;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.AppResourceUtils;
import com.moons.xst.track.common.CyptoUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileMonitor;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.HwCfgInfo;
import com.moons.xst.track.common.LoadAppConfigHelper;
import com.moons.xst.track.common.LogUtils;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.OperatingConfigHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.common.UpdateManager;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.service.BDGpsManagerService;
import com.moons.xst.track.service.CalculateCycleService;
import com.moons.xst.track.service.CommuJITService;
import com.moons.xst.track.service.NotificationService;
import com.moons.xst.track.service.VoiceConvertService;
import com.moons.xst.track.ui.main_fragment.CommuFragment;
import com.moons.xst.track.ui.main_fragment.HomeFragment;
import com.moons.xst.track.ui.main_fragment.QueryFragment;
import com.moons.xst.track.ui.main_fragment.SysFragment;
import com.moons.xst.track.ui.main_fragment.ToolsFragment;
import com.moons.xst.track.widget.MainBottomBar;
import com.moons.xst.track.widget.SimpleTextDialog;

import de.greenrobot.event.EventBus;

public class Main_Page extends BaseActivity {

	private String TAG = "Main_Page";
	private final static String STATE_LOADUSERDJLINE = "LOADUSERDJLINE";
	private final static String STATE_REFRESHRADIOBTN = "REFRESHRADIOBTN";
	private final static String STATE_LOADALLDJLINE = "LOADALLDJLINE";
	public static Main_Page instance = null;

	private AppContext appContext;// 全局Context

	public static final int PopMenu_LOGIN_OR_LOGOUT = 0;
	public static final int PopMenu_ABOUT = 1;
	public static final int PopMenu_QUIT = 2;

	public static final int REQCODE_SOUND = 1;// 录音
	public static final int REQCODE_SEARCH = 2;// 搜索

	public static final int REQCODE_TEMPERATURE = 100;// 测温
	public static final int REQCODE_VIBRATION = 101;// 测振
	public static final int REQCODE_SPEED = 102;// 测转速

	public static final int REQCODE_RFID = 103; // 扫RFID
	public static final int REQCODE_SCAN = 104; // 扫二维码

	public static final int REQCODE_INSTALL_CANCEL = 0;// 安装取消   系统返回不能随意更改
	// App更新提示语
	private String updateMsg = "";
	// 返回的安装包url
	private String apkUrl = "";

	private DoubleClickExitHelper mDoubleClickExitHelper;

	private ImageButton fbMore; // 更多图标
	private QuickActionWidget mGrid;// 快捷栏控件
	private TextView mUserName;
	private TextView tv_home, tv_communication, tv_tools, tv_system, tv_query,
			tv_appName;
	private ImageView iv_wsstate;
	private Fragment fg_drawer_left;
	private LinearLayout ll_comm_hint;
	ImageView img_head;

	private DrawerLayout drawerLayout;

	Handler fileMonitorHandler;
	FileMonitor fileMonitorobServer;

	private MainBottomBar mMainBottomBar;
	FragmentViewPagerAdapter fragmnetpageradapter;
	public ViewPager viewPager;// 页卡内容

	// private HashMap<Integer, Boolean> grantHashMap = new HashMap<Integer,
	// Boolean>();
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;
	private String audioPath;
	private TempMeasureBaseInfo mTempMeasureBaseInfo;
	private UUID uuid;
	Intent loadUserDJLineIntent = new Intent();
	Intent loadAllDJLineIntent = new Intent();
	Intent refreshRadioBtnIntent = new Intent();
	Intent uploadDataIntent, calculateIntent, RFIDIntent;
	Intent intent, gpsIntent, voiceIntent;
	ServiceConnection calcuCon, gpsCon, voiceCon;

	public static Main_Page instance() {
		if (instance != null) {
			return instance;
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());	
		setContentView(R.layout.main);
		EventBus.getDefault().register(this);
		appContext = (AppContext) getApplication();
		initViewPager();
		initView();
		setCurrentVersion();
		// grantHashMap = GrantHelper.loadSettingXml();
		// 程序两次点击退出事件
		mDoubleClickExitHelper = new DoubleClickExitHelper(Main_Page.this);

		// 打开休眠锁
		if (AppContext.getOpenWakeLock()) {
			// 创建PowerManager对象
			pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			// 保持cpu一直运行，不管屏幕是否黑屏
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					"CPUKeepRunning");
			wakeLock.acquire();
		}

		// 加载硬件模块配置
		loadHardwareConfig();
		// 加载外接模式测量配置文件
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.MeasureTypeForOuter.toString())
				.LoadConfigByType();
		// 初始化几个无需登录就可更改的参数值
		initConfigValue();
		DJLineDAO.loadResultDataFileList();
		startFileObServer();
		// 初始化登录
		appContext.LoginInfo(true);
		// 初始化Head
		initHeadViewAndMoreBar();

		// 初始化弹出菜单
		initQuickActionGrid();
		// 判断应用是否需要更新
		thread.start();

		// startService
		startedService();
		registerBoradcastReceiver();
		// bindService
		BinderService();
		if (AppContext.getGPSOpen()) {
			if (!AppContext.locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				LayoutInflater factory = LayoutInflater.from(Main_Page.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
						.builder()
						.setTitle(getString(R.string.tips))
						.setView(view)
						.setMsg(getString(R.string.gps_not_open))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												Settings.ACTION_LOCATION_SOURCE_SETTINGS);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(intent);
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
			}
		}

		// 放到进入路线时加载工作日节假日BUFFER
		/*
		 * CycleHelper cychelper = new CycleHelper(); cychelper.loadWorkDate();
		 */
		goNext();
		LoadEventTypeList();
		instance = this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
			// back键
			// 是否退出应用
			boolean bet = mDoubleClickExitHelper.onKeyDown(keyCode, event);
			if (!bet) {
				goBack();
				// 退出
				AppManager.getAppManager().AppExit(Main_Page.this);
			}
			return bet;
		}
		return false;
	}

	// onNavigationItemSelected
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		// stopService(uploadDataIntent);
		// stopService(gpsIntent);
		// stopService(notificationIntent);
		if (mBroadcastReceiver != null)
			unregisterReceiver(mBroadcastReceiver);
		if (AppContext._currLocation != null)
			AppContext._currLocation = null;
	}

	private void initView() {
		// 初始化侧滑的fragment
		fg_drawer_left = getSupportFragmentManager().findFragmentById(
				R.id.fg_drawer_left);
		ll_comm_hint = (LinearLayout) fg_drawer_left.getView().findViewById(
				R.id.ll_comm_hint);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (AppContext.isLogin()) {
			AppContext.SetlineList(appContext.getLoginInfo()
					.getUserLineListStr());
			AppContext.SetWSAddress(AppContext.getWebServiceAddress(),
					AppContext.getWebServiceAddressForWlan());
			AppContext.SetxjqCD(AppContext.getxjqCD());
			// 初始化登录
			appContext.LoginInfo(false);
			// 初始化Head
			this.initHeadViewAndMoreBar();
			// 加载路线数据(发送广播)
			this.sendBroadcast();
		}
	}

	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQCODE_SOUND:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String path = UriToPath(uri);
				FileUtils.copyFile(path, audioPath);
				File file = new File(path);
				file.delete();

				TempMeasureDBHelper.GetIntance().InsertTempMeasureBaseInfo(
						Main_Page.this, mTempMeasureBaseInfo);
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
				enterSwictherModule(Main_Page.this, djline);
			}
			break;
		case REQCODE_RFID:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					String rfidStr = bundle.getString("RFIDString");

					rfidLogin(rfidStr);
				}
			}
			break;
		case REQCODE_SCAN:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle bundle = data.getExtras();
					String codeResult = bundle.getString("codeResult");
					String account = "";
					String pwd = "";
					try {
						account = codeResult.split("\\|", 2)[0].toString();
						pwd = codeResult.split("\\|", 2)[1].toString();
					} catch (Exception e) {
						account = "";
						pwd = "";
					}
					scanLogin(account, pwd);
				}
			}
		case REQCODE_INSTALL_CANCEL:
			//APk安装页面点击取消的操作 
			Editor editor = getPreferences().edit();
			editor.putBoolean("isFirstStart", false);
			editor.commit();
			break;
		default:
			break;
		}
	}

	/**
	 * EVENTBUS事件总线
	 * 
	 * @param str
	 */
	public void onEvent(String str) {
		if (str.equalsIgnoreCase("Recorder")) {
			startRecorder();
		} else if (str.equalsIgnoreCase("CW")) {
			startCeWen();
		} else if (str.equalsIgnoreCase("CZ")) {
			startCeZhen();
		} else if (str.equalsIgnoreCase("CS")) {
			startCeZS();
		} else if (str.equalsIgnoreCase("Search")) {
			UIHelper.showSearchMyWork(Main_Page.this, REQCODE_SEARCH);
		} else if (str.equalsIgnoreCase("Login")) {
			UIHelper.showLoginDialog(Main_Page.this);
		} else if (str.equalsIgnoreCase("REFRESHLANGUAGE")) {
			refreshAfterChangeLanguage();
			initViewPager();
		} else if (str.equalsIgnoreCase("WEBSERVICEOK")) {
			iv_wsstate.setVisibility(View.GONE);
		} else if (str.equalsIgnoreCase("WEBSERVICEERROR")) {
			iv_wsstate.setVisibility(View.VISIBLE);
		}
	}

	HomeFragment HomeFragment;

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.packpage_vPager);
		List<Fragment> lists = new ArrayList<Fragment>();
		HomeFragment = new HomeFragment();
		lists.add(HomeFragment);
		lists.add(new CommuFragment());
		lists.add(new QueryFragment());
		lists.add(new ToolsFragment());
		lists.add(new SysFragment());

		fragmnetpageradapter = new FragmentViewPagerAdapter(
				getSupportFragmentManager(), viewPager, lists);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setCurrentItem(0);

		viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
			@Override
			public void transformPage(View page, float position) {
				final float normalizedposition = Math.abs(Math.abs(position) - 1);
				page.setAlpha(normalizedposition);
			}
		});

		mMainBottomBar = (MainBottomBar) findViewById(R.id.bottom_tab_bar);
		mMainBottomBar.setCallBack(new MainBottomBar.CallBack() {

			@Override
			public void call(int prevIndex, int currentIndex) {
				viewPager.setCurrentItem(currentIndex);
			}
		});
		mMainBottomBar.setSelected(0);
	}

	/**
	 * 当viewpager切换完成时
	 * 
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int position) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			mMainBottomBar.setSelected(arg0);
			if (arg0 != 0) {
				HomeFragment.refreshAdapter();
			}
			if (arg0 == 0) {
				// checkLoginAfterCommunication();
				refreshLineAfterCommunication();
			} else if (arg0 == 1) {
				sendRefreshRadioBtnBroadcast();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
		HomeFragment.refreshAdapter();
	}

	/**
	 * --跳转控制
	 * 
	 * --主界面进入具体界面由这里控制
	 * 0-巡检;1-点检排程;2-巡更;3-精密点检;4-巡视路线;5-SIS路线;6-GPS巡线;7-新GPS巡线;8-条件巡检
	 * 
	 */
	private void enterSwictherModule(Context context, final DJLine adjline) {
		if (!FileUtils.checkFileExists(AppConst.XSTDBPath()
				+ AppConst.PlanDBName(adjline.getLineID()))) {
			UIHelper.ToastMessage(Main_Page.this,
					R.string.home_line_noplanfile_notice);
			return;
		}

		if (AppContext.getCheckInAfterEnterLine()) {
			AppContext.setCurrLineInfo(adjline);
			AppContext.setFromLoginYN(false);
			UIHelper.showLoginDialog(Main_Page.this);
		} else {
			UIHelper.enterMyWork(context, adjline);
		}

		// UIHelper.enterMyWork(context, adjline);
	}

	/**
	 * 获取当前客户端版本信息
	 */
	private void setCurrentVersion() {
		try {
			AppContext.xstTrackPackageInfo = getBaseContext()
					.getPackageManager().getPackageInfo(
							getBaseContext().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * 获取硬件配置
	 */
	private void loadHardwareConfig() {
		try {
			HwCfgInfo hwconfig = new HwCfgInfo(Main_Page.this);

			AppContext.setTemperatureUseYN(hwconfig.isSupportTemperature());
			AppContext.setVibrationUseYN(hwconfig.isHwSupportVibration());
			AppContext.setSpeedUseYN(hwconfig.isSupportSpeed());
			AppContext.setWalkieTalkieUseYN(hwconfig.isSupportWalkieTalkie());
			AppContext.setRFIDUseYN(hwconfig.isHwSupportRfid());
			AppContext.setRotateCameraYN(hwconfig.isRotateCamera());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				LayoutInflater factory = LayoutInflater.from(Main_Page.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
						.builder()
						.setTitle(getString(R.string.software_version_update))
						.setView(view)
						.setMsg(updateMsg)
						.setPositiveButton(getString(R.string.update_now),
								new OnClickListener() {
									

									@Override
									public void onClick(View v) {
										UpdateManager.getUpdateManager()
												.homeDownloadApk(
														Main_Page.this, apkUrl);
									}
								})
						.setNegativeButton(
								getString(R.string.update_talklater),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).setCanceledOnTouchOutside(false).show();
			}
		};
	};
	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			if (AppContext.getCommunicationType().equals(
					AppConst.CommunicationType_Wireless)) {// 无线方式通信
				if (isUpdataApp()) {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			}
		}
	});

	// 判断是否需要更新app
	private Boolean isUpdataApp() {
		if (!AppContext.getCheckUp()) {
			return false;
		}
		if (!NetWorkHelper.isNetworkAvailable(this))
			return false;
		int oldVsersionCode;
		if (AppContext.xstTrackPackageInfo != null) {
			oldVsersionCode = AppContext.xstTrackPackageInfo.versionCode;
		} else {
			return false;
		}

		try {
			String appversionxml = WebserviceFactory.getAppVersionInfo(
					getBaseContext(), AppContext.getModel());
			if (!StringUtils.isEmpty(appversionxml)) {
				Update update = Update.parse(appversionxml);
				String updateMsgBeforeReplace = update.getUpdateLog();
				updateMsg = updateMsgBeforeReplace.replace("|", "");
				apkUrl = update.getDownloadUrl();
				int versionCode = update.getVersionCode();
				String versionName = update.getVersionName();
				if (oldVsersionCode < versionCode && versionCode != 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 初始化头部视图
	 */
	@SuppressWarnings("deprecation")
	private void initHeadViewAndMoreBar() {
		tv_appName = (TextView) findViewById(R.id.home_head_title);
		tv_home = (TextView) findViewById(R.id.tv_main_menu_home);
		tv_communication = (TextView) findViewById(R.id.tv_main_menu_communication);
		tv_tools = (TextView) findViewById(R.id.tv_main_menu_tools);
		tv_system = (TextView) findViewById(R.id.tv_main_menu_system);
		tv_query = (TextView) findViewById(R.id.tv_main_menu_query);
		iv_wsstate = (ImageView) findViewById(R.id.home_head_wsstate);
		mUserName = (TextView) findViewById(R.id.home_head_username);
		img_head=(ImageView) findViewById(R.id.img_head);
		
		String mainpagelogo = AppResourceUtils.getValue(this, getString(R.string.app_name), "mainpagelogo");
		if (!StringUtils.isEmpty(mainpagelogo)){
			int mainpagelogoID = AppResourceUtils.getDrawableId(this, mainpagelogo);
			img_head.setImageResource(mainpagelogoID);
		}
		
		String hometitle = AppResourceUtils.getValue(this, getString(R.string.app_name), "hometitle");
		if (!StringUtils.isEmpty(hometitle)){
			int hometitleID = AppResourceUtils.getStringId(this, hometitle);
			tv_appName.setText(getString(hometitleID));
		}

		if (AppContext.isLogin()) {
			mUserName.setText(appContext.getLoginInfo().getUserName());
		} else {
			mUserName.setText("");
		}

		fbMore = (ImageButton) findViewById(R.id.home_head_morebutton);
		fbMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// UIHelper.showTempTaskList(Home.this);

				// 展示快捷栏
				UIHelper.showSettingLoginOrLogout(Main_Page.this, mGrid);
				mGrid.show(v);
			}
		});

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {
			}

			@Override
			public void onDrawerSlide(View view, float f) {
			}

			@Override
			public void onDrawerOpened(View view) {
				// 抽屉完全打开时触发
			}

			@Override
			public void onDrawerClosed(View view) {
				// 清空通信提示信息
				ll_comm_hint.removeAllViews();
				// 抽屉完全关闭时触发
				drawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
			}
		});

	}

	private void refreshHeadView() {
		if (AppContext.isLogin()) {
			mUserName.setText(appContext.getLoginInfo().getUserName());
		} else {
			mUserName.setText("");
		}
	}

	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);

		// 登录或注销
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_login_icon, R.string.main_menu_login));
		// 关于
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_about_icon, R.string.main_menu_about));
		// 退出
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.main_pop_quit_icon, R.string.main_menu_quit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case PopMenu_LOGIN_OR_LOGOUT:// 用户登录-注销
				callbackLoginOrOut();
				break;
			case PopMenu_ABOUT:// 关于
				UIHelper.showAbout(Main_Page.this);
				break;
			case PopMenu_QUIT:// 退出
				LayoutInflater factory = LayoutInflater.from(Main_Page.this);
				final View view = factory.inflate(R.layout.textview_layout,
						null);
				new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
						.builder()
						.setTitle(getString(R.string.app_menu_logouttitle))
						.setView(view)
						.setMsg(getString(R.string.app_menu_surelogout))
						.setPositiveButton(getString(R.string.sure),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										goBack();
										AppManager.getAppManager().AppExit(
												Main_Page.this);
									}
								})
						.setNegativeButton(getString(R.string.cancel),
								new OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
				break;
			}
		}
	};

	/**
	 * start方式启动的Service
	 */
	private void startedService() {
		notificationIntent = new Intent(getApplicationContext(),
				NotificationService.class);
		startService(notificationIntent);

		Bundle bundle = new Bundle();
		bundle.putInt("Runstate", AppContext.getJITUpload() == true ? 0 : 2);
		bundle.putInt("XDJRunstate", AppContext.getJITUploadXDJ() == true ? 0
				: 2);
		bundle.putInt("XXRunstate", AppContext.getJITUploadXX() == true ? 0 : 2);
		uploadDataIntent = new Intent(getApplicationContext(),
				CommuJITService.class);
		uploadDataIntent.putExtras(bundle);
		startService(uploadDataIntent);
		gpsIntent = new Intent(getApplicationContext(),
				BDGpsManagerService.class);
		startService(gpsIntent);
	}

	/**
	 * 以Binding方式启动的启动服务
	 */
	private void BinderService() {
		intent = new Intent(getApplicationContext(),
				CalculateCycleService.class);
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
		// if (appContext.isGpsOpen()) {
		// gpsCon = new ServiceConnection() {
		// @Override
		// public void onServiceConnected(ComponentName componentName,
		// IBinder binder) {
		// // 调用bindService方法启动服务时候，如果服务需要与activity交互，
		// // 则通过onBind方法返回IBinder并返回当前本地服务
		// }
		//
		// @Override
		// public void onServiceDisconnected(ComponentName componentName) {
		// AppContext.locationManager = null;
		// AppContext.bdLocationClient = null;
		// AppContext._currLocation = null;
		// // 这里可以提示用户
		// }
		// };
		// gpsIntent = new Intent(getApplicationContext(),
		// BDGpsManagerService.class);
		// bindService(gpsIntent, gpsCon, Context.BIND_AUTO_CREATE);
		// }
		voiceIntent = new Intent(getApplicationContext(),
				VoiceConvertService.class);
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

	/**
	 * 以UnBindService方式停止的启动服务
	 */
	private void UnBindService() {
		intent = new Intent(this, CalculateCycleService.class);
		if (calcuCon != null)
			unbindService(calcuCon);
		// if (gpsCon != null)
		// unbindService(gpsCon);
		if (voiceCon != null)
			unbindService(voiceCon);
	}

	/**
	 * 初始化几个无需登录就可更改的参数值
	 */
	private void initConfigValue() {
		AppContext.SetlineList(appContext.getLoginInfo().getUserLineListStr());
		AppContext.SetWSAddress(AppContext.getWebServiceAddress(),
				AppContext.getWebServiceAddressForWlan());
		AppContext.SetxjqCD(AppContext.getxjqCD());
		// AppContext.setUpdateSysDateType(appContext.getConfigUpdateSysDate());
		// AppContext.setMeasureType(appContext.getConfigMeasureType());
	}

	public void sendRefreshRadioBtnBroadcast() {
		refreshRadioBtnIntent.setAction(STATE_REFRESHRADIOBTN);
		Main_Page.this.sendBroadcast(refreshRadioBtnIntent);
	}

	public void callbackLoginOrOut() {
		if (AppContext.isLogin()) {
			LayoutInflater factory = LayoutInflater.from(Main_Page.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(getString(R.string.user_menu_surelogout))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									appContext.cleanLoginInfo();
									if (AppContext.getCheckInAfterEnterLine()) {
										loadAllDJLineIntent
												.setAction(STATE_LOADALLDJLINE);
										Main_Page.this
												.sendBroadcast(loadAllDJLineIntent);
									} else {
										sendBroadcast();
									}
									initHeadViewAndMoreBar();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							}).show();
		} else {
			if (AppContext.getCheckInAfterEnterLine())
				AppContext.setFromLoginYN(true);
			UIHelper.showLoginDialog(Main_Page.this);
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

	private String UriToPath(Uri uri) {
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

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		AppContext.setIsUploading(true);
		/*// 大于设置的天数，将历史数据清除
		if (AppContext.deleteHisData) {
			if (DJHisDataHelper.GetIntance().checkXJHisDataExist(this)) {
				int hisDataSavedDays = Integer.parseInt(AppContext
						.getHisDataSavedDays());
				String beforeDate = DateTimeHelper.beforeDate(hisDataSavedDays);
				//删除过期历史数据
				DJHisDataHelper.GetIntance().clearHisData(this, beforeDate);
				//删除过期操作日志
				File file = new File(AppConst.XSTLogFilePath());
				OperatingConfigHelper.getInstance().deleteOverdueLog(file, beforeDate);
				AppContext.deleteHisData = false;
			}
		}*/

		XSTMethodByLineTypeHelper.getInstance().disposeHelper();
		// 退出时关闭实时上传结果库和下载库
		appContext.closeDB();

		// 为上位机配置，APP中不可见的配置项赋值
		/*
		 * LoadAppConfigHelper.getAppConfigHelper(
		 * AppConst.AppConfigType.Invisible.toString()).LoadConfigByType();
		 */
		// 更新手势密码配置文件
		updateGestureInfo();
		// 左拉抽屉是否可见
		setDrawleftVisible();

		AppContext.SetCurrLineIDForJIT(0);
		// 如果是进入路线需要登录，则注销用户
		if (AppContext.getCheckInAfterEnterLine()) {
			checkInAfterEnterLine();
		} else {
			if (AppContext.GetDownLoadYN() || viewPager.getCurrentItem() == 0) {
				AppContext.SetlineList(appContext.getLoginInfo()
						.getUserLineListStr());
				this.sendBroadcast();
			}
			checkLoginAfterCommunication();
		}
		// 刷新WakeLock
		refreshWakeLock();
	}

	public void checkLoginAfterCommunication() {
		if (AppContext.GetDownLoadYN()) {
			if (AppContext.isLogin()) {
				User loginUser = appContext.getLoginInfo();
				if (checkUserChanged(loginUser)) {
					appContext.cleanLoginInfo();
					this.sendBroadcast();
					initHeadViewAndMoreBar();
					// AppContext.SetDownLoadYN(false);
				}
			}
			AppContext.SetDownLoadYN(false);
		}
	}

	public void checkInAfterEnterLine() {

		if (!AppContext.getFromLoginYN()) {
			appContext.cleanLoginInfoWithoutMessage();
			refreshHeadView();
			if (AppContext.GetDownLoadYN() || viewPager.getCurrentItem() == 0) {
				loadAllDJLineIntent.setAction(STATE_LOADALLDJLINE);
				Main_Page.this.sendBroadcast(loadAllDJLineIntent);
				AppContext.SetDownLoadYN(false);
			}
		} else {
			refreshHeadView();
			loadAllDJLineIntent.setAction(STATE_LOADALLDJLINE);
			Main_Page.this.sendBroadcast(loadAllDJLineIntent);
			if (AppContext.getUserLoginType().equalsIgnoreCase(
					AppConst.AppLoginType.All.toString())
					|| AppContext.getUserLoginType().equalsIgnoreCase(
							AppConst.AppLoginType.Account.toString())) {
				AppContext.setFromLoginYN(false);
			}
		}

	}

	//
	public void updateGestureInfo() {
		LoadAppConfigHelper.getAppConfigHelper(
				AppConst.AppConfigType.Gesture.toString()).LoadConfigByType();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			String path = AppConst.XSTBasePath() + AppConst.NewUserXmlFile;
			File fi = new File(path);

			if (!fi.exists()) {
				return;
			}
			//String temppath = AppConst.XSTBasePath() + AppConst.tempUserXmlFile;
			//FileEncryptAndDecrypt.decrypt(path, temppath);
			//File f = new File(temppath);
			Document doc = db.parse(fi);
			doc.normalize();
			NodeList tableList = doc.getElementsByTagName("Z_AppUser");

			boolean flag = false;
			Set<String> keys = AppContext.gestureUserInfo.keySet();
			for (String key : keys) {
				flag = false;
				for (int i = 0; i < tableList.getLength(); i++) {
					Node table = tableList.item(i);
					for (Node node = table.getFirstChild(); node != null; node = node
							.getNextSibling()) {
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							if (node.getNodeName().toUpperCase()
									.equals("APPACCOUNT_TX")) {
								// String s=node.getTextContent();
								if (key.equals(node.getTextContent()))
									flag = true;
								// else
								// flag = false;
							}
						}
					}
				}
				if (!flag)
					AppContext.gestureUserInfo.remove(key);
			}

			//f.delete();

			String configStr = ParseObjectListToXml(AppContext.gestureUserInfo);
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(AppConst.XSTBasePath()
						+ "GestureConfig.xml", false);
				bw = new BufferedWriter(fw);
				bw.write(configStr);
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e2) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String ParseObjectListToXml(
			Hashtable<String, String[]> enitities) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("\r\n");
		sb.append("<Configuration>");
		sb.append("\r\n");
		for (String key : enitities.keySet()) {
			sb.append("<Setting Account=\"" + key + "\" ");
			sb.append("Password=\"" + enitities.get(key)[0].toString() + "\" ");
			sb.append("Gesture=\"" + enitities.get(key)[1].toString() + "\" />");
		}
		sb.append("\r\n");
		sb.append("</Configuration>");
		return sb.toString();
	}

	public void refreshLineAfterCommunication() {
		if (AppContext.GetRefreshLineYN()) {
			if (AppContext.isLogin()) {
				this.sendBroadcast();
				AppContext.SetRefreshLineYN(false);
			}
		}
	}

	private void refreshAfterChangeLanguage() {
		tv_home.setText(R.string.main_menu_home);
		tv_communication.setText(R.string.main_menu_communication);
		tv_tools.setText(R.string.main_menu_tools);
		tv_system.setText(R.string.main_menu_system);
		tv_query.setText(R.string.main_menu_query);
		if (!AppContext.isLogin())
			mUserName.setText("");
		
		String hometitle = AppResourceUtils.getValue(this, getString(R.string.app_name), "hometitle");
		if (!StringUtils.isEmpty(hometitle)){
			int hometitleID = AppResourceUtils.getStringId(this, hometitle);
			tv_appName.setText(getString(hometitleID));
		}
		
	}

	/**
	 * 验证当前登录用户是否变更
	 * 
	 * @author wangyong
	 */
	public static boolean checkUserChanged(User user) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String path = AppConst.XSTBasePath() + AppConst.NewUserXmlFile;
		//String temppath = AppConst.XSTBasePath() + AppConst.tempUserXmlFile;

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			//FileEncryptAndDecrypt.decrypt(path, temppath);
			//File f = new File(temppath);

			Document doc = db.parse(new File(path));
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
			// f.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} finally {
			//File tempUser = new File(temppath);
			//tempUser.delete();
		}
	}

	// 刷新休眠锁
	private void refreshWakeLock() {
		if (AppContext.getRefreshWakeLock()) {
			AppContext.setRefreshWakeLock(false);
			if (AppContext.getOpenWakeLock()) {
				// 创建PowerManager对象
				pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
				// 保持cpu一直运行，不管屏幕是否黑屏
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						"CPUKeepRunning");
				wakeLock.acquire();
			} else {
				if (wakeLock != null)
					wakeLock.release();
				wakeLock = null;
			}
		}
	}

	/**
	 * 新装/升级APP情况下的跳转
	 */
	private void goNext() {
		if (AppContext.isNewInstall) {
			final SimpleTextDialog _dialog = new SimpleTextDialog(
					Main_Page.this, getString(R.string.tips),
					getString(R.string.appstart_notice2));
			_dialog.setTextSize(22);
			_dialog.setOKButton(R.string.simple_text_dialog_btnok,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNewInstall = false;
							UIHelper.showSetting(Main_Page.this);
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
			LayoutInflater factory = LayoutInflater.from(Main_Page.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
					.builder()
					.setTitle(getString(R.string.tips))
					.setView(view)
					.setMsg(getString(R.string.appstart_notice2))
					.setPositiveButton(
							getString(R.string.simple_text_dialog_btnok),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									AppContext.isNewInstall = false;
									UIHelper.showSetting(Main_Page.this);
								}
							})
					.setNegativeButton(
							getString(R.string.simple_text_dialog_btncancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									AppContext.isNewInstall = false;
								}
							}).show();

		} else if (AppContext.isNowUpdate) {
			final SimpleTextDialog _dialog = new SimpleTextDialog(
					Main_Page.this, getString(R.string.tips),
					getString(R.string.appstart_notice1));
			_dialog.setTextSize(22);
			_dialog.setOKButton(R.string.simple_text_dialog_btnok,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO 自动生成的方法存根
							AppContext.isNowUpdate = false;
							UIHelper.showCommuDownLoad(Main_Page.this);
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
			LayoutInflater factory = LayoutInflater.from(Main_Page.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(Main_Page.this)
					.builder()
					.setTitle(getString(R.string.tips))
					.setView(view)
					.setMsg(getString(R.string.appstart_notice1))
					.setPositiveButton(
							getString(R.string.simple_text_dialog_btnok),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									AppContext.isNowUpdate = false;
									UIHelper.showCommuDownLoad(Main_Page.this);
								}
							})
					.setNegativeButton(
							getString(R.string.simple_text_dialog_btncancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									AppContext.isNowUpdate = false;
								}
							}).show();
		}
	}

	/**
	 * 加载事件类型
	 */
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

	/**
	 * 发送刷新路线LISTVIEW广播
	 */
	public void sendBroadcast() {
		loadUserDJLineIntent.setAction(STATE_LOADUSERDJLINE);
		Main_Page.this.sendBroadcast(loadUserDJLineIntent);
	}

	private void goBack() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;

		stopService(uploadDataIntent);
		stopService(gpsIntent);
		stopService(notificationIntent);
	}

	/**
	 * 录音
	 */
	private void startRecorder() {
		new DateFormat();
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
		//设置录音文件最大值（大约1000KB）
		long bytes = (long) (1024 * 1000L);
		intentFromRecord.putExtra(MediaStore.Audio.Media.EXTRA_MAX_BYTES,bytes);
		intentFromRecord.putExtra("return-data", true);
		startActivityForResult(intentFromRecord, REQCODE_SOUND);
	}

	/**
	 * 测温
	 */
	private void startCeWen() {
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLWDXC(Main_Page.this, "", false, REQCODE_TEMPERATURE,
				clModuleType, AppContext.getBlueToothAddressforTemperature(),
				AppContext.getBTConnectPwdforTemperature(), 0);
	}

	/**
	 * 测振
	 */
	private void startCeZhen() {
		// Intent intent=new Intent(this,VibrationForOuterAty.class);
		// startActivity(intent);
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLZDXC(Main_Page.this, "", false, REQCODE_VIBRATION,
				clModuleType, AppContext.getBlueToothAddressforVibration(),
				AppContext.getBTConnectPwdforVibration(), 0);
	}

	/**
	 * 测转速
	 */
	private void startCeZS() {
		int clModuleType = AppContext.getMeasureType().equals(
				AppConst.MeasureType_Inner) ? 0 : 1;
		UIHelper.showCLZSXC(Main_Page.this, "", false, REQCODE_SPEED,
				clModuleType, 0);
	}

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

	long systimeset = 0;
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isComServiceRunning = false;
			boolean isGPSServiceRunning = false;
			String action = intent.getAction();
			if (action.equals("com.xst.track.service.gpsSetting")) {

			} else if (action.equals("com.xst.track.service.updateSysDateTime")) {

			} else if (action.equals(Intent.ACTION_TIME_TICK)) {
				ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				for (RunningServiceInfo service : manager
						.getRunningServices(100)) {
					if ("com.moons.xst.track.service.CommuJITService"
							.equals(service.service.getClassName())) {
						isComServiceRunning = true;
					}
					// if ("com.moons.xst.track.service.BDGpsManagerService"
					// .equals(service.service.getClassName())) {
					// isGPSServiceRunning = true;
					// }
				}
				if (!isComServiceRunning) {
					Bundle bundle = new Bundle();
					bundle.putInt("op", 0);
					Intent uploaIntent = new Intent(Main_Page.this,
							CommuJITService.class);
					uploaIntent.putExtras(bundle);
					startService(uploaIntent);
				}
			}
		}
	};
	private Intent notificationIntent;

	private void rfidLogin(String rfidStr) {
		User userInfo = new User();
		userInfo.setAccount(rfidStr);
		userInfo.setPwd("");
		userInfo.setUserRFID(rfidStr);
		userInfo.setRememberMe(true);
		login(userInfo, LoginType.RFID);
	}

	private void scanLogin(String acc, String pwd) {
		User userInfo = new User();
		userInfo.setAccount(acc);
		userInfo.setPwd(pwd);
		userInfo.setRememberMe(true);
		login(userInfo, LoginType.Scan);
	}

	public void setDrawleftVisible() {
		drawerLayout
			.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		
		/*if (AppContext.getDrawleftLayoutYN()
				&& AppContext.getPlanType().equalsIgnoreCase(
						AppConst.PlanType_XDJ)
				&& AppContext.getOSAPIVersion() < 24) {
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		} else {
			drawerLayout
					.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		}*/
	}

	public void refreshCustomShortcut() {
		HomeFragment.refreshCustomShortcut();
	}

	// 登录验证
	private void login(final User userInfo, final AppContext.LoginType loginType) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					User user = (User) msg.obj;
					if (user != null) {
						// 清空原先cookie
						ApiClient.cleanCookie();

						if (AppContext.getCheckInAfterEnterLine()) {
							if (AppContext.getFromLoginYN()) {
								// 提示登陆成功
								Toast.makeText(Main_Page.this,
										getString(R.string.msg_login_success),
										Toast.LENGTH_SHORT).show();
								refreshHeadView();
								AppContext.setFromLoginYN(false);

								OperatingConfigHelper.getInstance().i(AppConst.COMMTYPE_USERLOGIN,
										AppConst.LOGSTATUS_NORMAL,
										"");

							} else {
								boolean flag = false;
								for (String s : user.getUserLineList()) {
									if (s.equals(String.valueOf(AppContext
											.getCurrLineInfo().getLineID()))) {
										flag = true;
										// 提示登陆成功
										Toast.makeText(
												Main_Page.this,
												getString(R.string.msg_login_success),
												Toast.LENGTH_SHORT).show();

										OperatingConfigHelper.getInstance().i(AppConst.COMMTYPE_USERLOGIN,
												AppConst.LOGSTATUS_NORMAL,
												"");

										UIHelper.enterMyWork(Main_Page.this,
												AppContext.getCurrLineInfo());
									}
								}
								if (!flag) {
									// 提示登陆失败
									UIHelper.ToastMessage(Main_Page.this,
											R.string.msg_login_notin_fail);
								}
							}
						} else {
							// 提示登陆成功
							Toast.makeText(Main_Page.this,
									getString(R.string.msg_login_success),
									Toast.LENGTH_SHORT).show();
							refreshHeadView();
							AppContext.SetlineList(appContext.getLoginInfo()
									.getUserLineListStr());
							sendBroadcast();
						}
					}
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(Main_Page.this,
							getString(R.string.msg_login_fail) + "\n" + msg.obj);
				} else if (msg.what == -1) {
					((AppException) msg.obj).makeToast(Main_Page.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					User user = appContext.loginVerify(userInfo, loginType);
					Result res = user.getValidate();
					if (res.OK()) {
						appContext.saveLoginInfo(user);// 保存登录信息
						msg.what = 1;// 成功
						msg.obj = user;
					} else {
						// ac.cleanLoginInfo();//清除登录信息
						msg.what = 0;// 失败
						msg.obj = res.getErrorMessage();
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	public SharedPreferences getPreferences(){
		return this.getSharedPreferences("UpDateConfig",
				MODE_PRIVATE);
		
	}
}