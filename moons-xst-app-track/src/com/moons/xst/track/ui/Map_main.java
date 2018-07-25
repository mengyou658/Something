package com.moons.xst.track.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppConfig;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.MapPointAdapter;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.GPSPosition;
import com.moons.xst.track.bean.GPSPositionForInit;
import com.moons.xst.track.bean.GPSPositionForResult;
import com.moons.xst.track.bean.OverSpeedRecordInfo;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.GPSHelper;
import com.moons.xst.track.common.LogUtils;
import com.moons.xst.track.common.RFIDManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.service.CommuJITService;
import com.moons.xst.track.widget.ActionSheetDialog;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.moons.xst.track.widget.ActionSheetDialog.SheetItemColor;

/**
 * @com.moons.xst.track.ui
 * 
 * @说明：此类中涉及复杂的逻辑，在此添加更新代码时，请严格按照以下代码规范 变量、方法、类等做好备注，分类存放
 */
public class Map_main extends BaseActivity implements MKOfflineMapListener {

	// ******模块测试相关*******/
	/**
	 * 测试标签（执行测试的相关模块）
	 */
	boolean testtag = false;

	// **************************/
	// 变量---地图基础变量
	/**
	 * 地图日志key设置
	 */
	private static final String LTAG = Map_main.class.getSimpleName();
	private TextView mapModeTV, listModeTV, zoomlargeTV, zoomsmallTV;
	RelativeLayout zoomBarLL;
	private ImageView lineTypeIV;
	private ImageButton quickMemuShowBtn;
	private RelativeLayout quickMemull, quickMemuPlanLL, quickMemuBugLL,
			quickMemuHistoryLL;
	private RelativeLayout gpsStatell;
	private TextView gpsStateTextView;
	/**
	 * 地图显示控件
	 */
	private MapView mMapView;
	/**
	 * 百度地图Map
	 */
	private BaiduMap mBaiduMap;
	private float zoomMaxValue, zoomMinValue;
	private Vibrator mVibrator;
	/**
	 * 是否首次定位
	 */
	boolean isFirstLoc = true;
	/**
	 * 运行状态
	 */
	boolean IsRun = true;
	/**
	 * 全局Context
	 */
	private AppContext appContext;

	// 变量--- 地图定位变量
	/**
	 * 百度地图位置对象
	 */
	// LocationClient mLocClient;
	/**
	 * 百度地图位置监听
	 */
	// public MyLocationListenner myListener = new MyLocationListenner();

	/**
	 * 地图模式
	 */
	private LocationMode mCurrentMode;
	/**
	 * 当前覆盖物信息
	 */

	BitmapDescriptor mCurrentMarker;
	/**
	 * 地图跟随图标按钮（地图左下角）
	 */
	ImageButton requestLocButton;
	/**
	 * 地图导航按钮（地图左下角上）
	 */
	ImageButton nativeButton;
	/**
	 * 当前位置
	 */
	private LatLng curLocation;

	// 变量--时长、位移、速度等控件变量及显示值变量
	/**
	 * 速度
	 */
	private TextView v_info;
	/**
	 * 时长
	 */
	private TextView t_info;
	/**
	 * 位移
	 */
	private TextView s_info;

	// private TextView cp_oneTextView, cp_twoTextView, cp_threeTextView;
	/**
	 * 速度大小
	 */
	private float speed = 0;
	/**
	 * 耗时时长
	 */
	private int linecosttime = 0;
	/**
	 * 位移大小
	 */
	private int distance = 0;

	/**
	 * 考核点间距离耗时时长
	 */
	private int p2pcosttime = 0;

	/**
	 * 超速持续时长
	 */
	private int overspeedtime = 0;

	/**
	 * 是否超速
	 */
	private boolean isoverspeed = false;

	// 地图模式--覆盖物相关
	BitmapDescriptor cpPointRed = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor cpPointGreen = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka_green);
	BitmapDescriptor nfcPointRed = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_nfc_red);
	BitmapDescriptor nfcPointGreen = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_nfc_green);
	BitmapDescriptor bdGround = BitmapDescriptorFactory
			.fromResource(R.drawable.ground_overlay);
	private InfoWindow mInfoWindow;
	HashMap<Integer, Marker> cpMarkers = new HashMap<Integer, Marker>();

	// 地图底部快捷菜单--popupWindow
	private PopupWindow menu;
	private LayoutInflater inflater;
	private View layout;
	private TextView cpName, cpJD, cpWD, cpDate;

	// Map列表模式--popupWindow
	private PopupWindow listmode;
	private TextView lm_lineName, lm_CurrCPName;
	private ListView lm_cpListView;
	private RelativeLayout listModeLL;

	// 考核点弹出--popupWindow
	private PopupWindow cplistPW;
	private LayoutInflater lm_cpinflater;
	private View lm_cplayout;
	private ListView lm_myCPListView;
	private ImageButton cplistImgBtn, mapChangeModeImgBtn;

	// 右上角--菜单相关
	public static final int PopMenu_Repair = 0;
	public static final int PopMenu_AddPo = 1;
	public static final int PopMenu_offmap = 2;
	private ImageButton fbMore; // 更多图标
	private ImageButton returnButton;
	private QuickActionWidget mGrid;// 快捷栏控件

	// 变量--运动轨迹相关
	private List<LatLng> latLngPolygon = new ArrayList<LatLng>();
	private Queue<LatLng> mQueueLocation = new ConcurrentLinkedQueue<LatLng>();
	private GPSPosition lastGpsPosition = null;

	// 消息Hander相关
	Handler mHandler;

	Handler handler = new Handler();

	// 基础变量
	DJLine djLineInfo;
	int djLineID, lastPointid = -1, cycid;
	String djLineName, idPosName;
	private LoadingDialog loading;
	private Handler mloaddataHandler;
	private TrackHelper trackHelper;
	private GPSPositionHelper gpsPohelper;
	Timer timerUploadGps = new Timer();
	// Timer timer = new Timer();
	// Timer pointtimer = new Timer();
	SoundPool soundPool;
	private CheckPointInfo currCheckPointInfo;

	int position;

	// ListMode下变量
	private MapPointAdapter adapter;
	// com.moons.xst.track.widget.SliderSwitch btn_switch_MaporList;

	// 路线列表长按菜单三项操作
	private static final int MENUACTION_DOPLAN = 0;// 做计划
	private static final int MENUACTION_QUERY = 1;// 查询数据
	private static final int MENUACTION_RETURN = 2;// 返回
	private static final int LASTGPSPOSITIONDISTANCE = 3;// 上一次记录的GPS坐标
	private static final int GPSPOSITIONDISTANCE = 4;// 当前记录的GPS坐标
	private static final int REFLASHLISTMODE = 5;// 刷新List模式
	private static final int BADLOCATION = 6;// 两点之间间距大于1公里，记为无效

	private static boolean flag = false;// 标记checkPointList是否加载完毕
	private ArrayList<CheckPointInfo> checkPointList; // 最终显示在地图上的点，不包括参与计算的初始化点

	NfcAdapter nfcAdapter;
	PendingIntent mPendingIntent;

	MediaPlayer player;

	// 方法 ---BaseActivity重写的方法
	int textureId = -1;
	private AppConfig appConfig;

	private BDLocation onReceivedLocation = null;
	ExecutorService threadPool = Executors.newFixedThreadPool(3);// 固定线程池
	private double _distance;

	private final static String MAP_NFC = "MAP_NFC";

	// private LocationClient bdLocationClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplication();
		setContentView(R.layout.main_map);
		appConfig = AppConfig.getAppConfig(Map_main.this);
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		flag = false;
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			djLineInfo = (DJLine) (savedInstanceState
					.getSerializable("lineinfo"));
			// djLineID = savedInstanceState.getInt("djline_id");
			// djLineName = savedInstanceState.getString("line_name");
		} else {
			// 这个是从另外一个界面进入这个时传入的
			djLineInfo = (DJLine) (getIntent().getSerializableExtra("lineinfo"));
			// djLineID = getIntent().getIntExtra("line_id", 0);
			// djLineName = getIntent().getStringExtra("line_name");
		}
		djLineID = djLineInfo.getLineID();
		djLineName = djLineInfo.getLineName();

		lastGpsPosition = new GPSPosition();
		gpsPohelper = new GPSPositionHelper();
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 是否显示Zoom
		mMapView.showZoomControls(false);
		// 是否显示比例尺
		mMapView.showScaleControl(false);
		v_info = (TextView) findViewById(R.id.sppedtv);
		v_info.setText("0");
		t_info = (TextView) findViewById(R.id.timeshorttv);
		t_info.setText("0");
		s_info = (TextView) findViewById(R.id.timelongtv);
		s_info.setText("0");
		mCurrentMode = LocationMode.NORMAL;
		initListener();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(20));
		zoomMaxValue = mBaiduMap.getMaxZoomLevel();
		zoomMinValue = mBaiduMap.getMinZoomLevel();

		// 获取默认的NFC控制器 (如果是MS600的机器，则注册广播)
		/*
		 * if (AppContext.getRFIDUseYN()) { AppContext.mStartRFID =
		 * AppContext.StartRFID.Map_main.toString();
		 * RFIDManager.getRFIDManager(Map_main.this,
		 * MAP_NFC).regReceiver_and_bindService(); player =
		 * MediaPlayer.create(this, R.raw.notificationsound); }
		 */

		// 0：离线定位请求成功 1:service没有启动 2：无监听函数 6：两次请求时间太短
		// if (mLocClient.requestLocation() == 0) {
		// // updateView("开始定位");
		// }
		requestLocButton = (ImageButton) findViewById(R.id.btn_pomode);
		OnClickListener requestLocListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					((ImageButton) v).setImageDrawable(getResources()
							.getDrawable(R.drawable.po_fol));
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					((ImageButton) v).setImageDrawable(getResources()
							.getDrawable(R.drawable.po_nor));
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(requestLocListener);

		nativeButton = (ImageButton) findViewById(R.id.btn_native);
		nativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CoordinateConverter converter = new CoordinateConverter();
				converter.from(CoordType.GPS);
				if (AppContext.getCurrLocation() == null) {
					UIHelper.ToastMessage(
							Map_main.this,
							getString(R.string.mapmain_message_noacquirelocation));
					return;
				}
				// 当前位置坐标
				converter.coord(new LatLng(AppContext.getCurrLocation()
						.getLatitude(), AppContext.getCurrLocation()
						.getLongitude()));
				LatLng pt1 = converter.convert();
				// 最近考核点坐标
				CheckPointInfo cp = AppContext.trackCheckPointBuffer.get(0);
				converter.coord(new LatLng((Double.valueOf(cp.getLatitude())),
						(Double.valueOf(cp.getLongitude()))));
				LatLng pt2 = converter.convert();
				/*
				 * // 天安门坐标 double mLat1 = 39.915291; double mLon1 = 116.403857;
				 * // 百度大厦坐标 double mLat2 = 40.056858; double mLon2 =
				 * 116.308194; LatLng pt1 = new LatLng(mLat1, mLon1); LatLng pt2
				 * = new LatLng(mLat2, mLon2);
				 */
				// 构建 导航参数
				NaviParaOption para = new NaviParaOption()
						.startPoint(pt1)
						.endPoint(pt2)
						.startName(
								getString(R.string.mapmain_message_nowlocation))
						.endName(
								getString(R.string.mapmain_message_recentlysite));

				try {
					BaiduMapNavigation.openBaiduMapNavi(para, Map_main.this);
				} catch (BaiduMapAppNotSupportNaviException e) {
					e.printStackTrace();
					showDialog();
				}
			}
		});

		// 默认跟随
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, mCurrentMarker));
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		this.initUI();
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				// if (loading != null)
				// loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// RefreshLine();//刷新路线轨迹
					RefreshLine();
				} else if (msg.obj != null) {
					// ((AppException) msg.obj).makeToast(Main.this);
				} else if (msg.what == 3 && msg.obj != null) {
					double lastGPSDistance = (Double) msg.obj;
					s_info.setText(String.format("%.2f", lastGPSDistance));
				} else if (msg.what == 4 && msg.obj != null) {
					double currGPSDistance = (Double) msg.obj;
					s_info.setText(String.format("%.2f", currGPSDistance));
				} else if (msg.what == 5) {
					reflashListMode();
				}

			}
		};
		initOverlay();
		// 初始化弹出菜单
		this.initQuickActionGrid();
		// 实例化PopupWindow创建菜单
		initMenu();
		loadCheckPointToMapView();
		// initLocation();
		initCPList();
		uiReflashClock();
		pointClock();
		LogUtils.WriteLog(LTAG, AppContext.GetCurrLineID());
		// registerGPS();
		// registerBoradcastReceiver();
		// 获取默认的NFC控制器
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		// tempCheckPointInfo = AppContext.trackCheckPointBuffer.get(0);
		/*
		 * MKOfflineMap mOffline = new MKOfflineMap(); // 传入接口事件，离线地图更新会触发该回调
		 * mOffline.init(this); // 开始下载离线地图，传入参数为cityID, cityID表示城市的数字标识。
		 * mOffline.start(289);
		 */
	}

	/**
	 * 接收到广播的定位信息后处理此定位信息
	 * 
	 * @param currLocation
	 */
	private synchronized void initLocation(final BDLocation currLocation) {
		new Thread() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mMapView == null)
							return;
						if (currLocation == null) {
							updateView(currLocation);// 定位以后更新相关显示
							GPSHelper.sortCheckPoint(
									AppContext.trackCheckPointBuffer, null);
							adapter.notifyDataSetChanged();
							lm_cpListView.setSelection(position);
							return;
						}

						int locType = currLocation.getLocType();
						if (locType == BDLocation.TypeGpsLocation
								|| locType == BDLocation.TypeNetWorkLocation) {
							updateView(currLocation);// 定位以后更新相关显示
							genGPSPoentity(currLocation);// 保存定位结果到数据库
							alertAndAddRecord();
							if (AppContext.ModTestYN
									&& testtag
									&& currLocation.getLocType() == BDLocation.TypeGpsLocation)
								AppManager.getAppManager().finishActivity(
										Map_main.this);

							AppContext.setCurrCityCode(currLocation
									.getCityCode());
							AppContext.setCurrCityName(currLocation.getCity());
							speed = currLocation.getSpeed();
							speed = speed > 0 ? speed : 0;
							v_info.setText(String.valueOf((int) speed));

							alertAndAddOverSpeed(speed);

							if (currLocation.getLocType() == BDLocation.TypeGpsLocation
									|| currLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
								pCount++;
								curLocation = new LatLng(
										currLocation.getLatitude(),
										currLocation.getLongitude());
								mQueueLocation.add(curLocation);
								if (mQueueLocation.size() > 6)
									mQueueLocation.remove();
							}
							adapter.notifyDataSetChanged();
							lm_cpListView.setSelection(position);
							MyLocationData locData = new MyLocationData.Builder()
									.accuracy(currLocation.getRadius())
									// 此处设置开发者获取到的方向信息，顺时针0-360
									.direction(currLocation.getDirection())
									.latitude(currLocation.getLatitude())
									.longitude(currLocation.getLongitude())
									.build();
							mBaiduMap.setMyLocationData(locData);
							if (isFirstLoc) {
								isFirstLoc = false;
								LatLng ll = new LatLng(
										currLocation.getLatitude(),
										currLocation.getLongitude());
								MapStatusUpdate u = MapStatusUpdateFactory
										.newLatLng(ll);
								mBaiduMap.animateMapStatus(u);
							}
							if (pCount == 4) {
								Message msg = Message.obtain();
								try {
									for (int i = 0; i < 3; i++) {
										LatLng Po = mQueueLocation.remove();
										latLngPolygon.add(Po);
									}
									msg.what = 1;
									msg.obj = latLngPolygon;

								} catch (Exception e) {
									e.printStackTrace();
									msg.what = -1;
									msg.obj = e;
								}
								mHandler.sendMessage(msg);
								pCount = 0;
							}
						} else {
							gpsStateTextView.setCompoundDrawables(null, null,
									null, null);
							gpsStateTextView
									.setText(getString(R.string.mapmain_message_locationunderway));
							return;
						}
					}
				});
			}
		}.start();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("lineinfo", djLineInfo);
		// outState.putInt("djline_id", djLineID);
		// outState.putString("djLineName", djLineName);
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		IsRun = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		IsRun = true;
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		// onResume 纹理失效
		textureId = -1;
		LogUtils.WriteLog(TAG, AppContext.GetCurrLineID());
		super.onResume();

		if (AppContext.getRFIDUseYN()) {
			AppContext.mStartRFID = AppContext.StartRFID.Map_main.toString();
			RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
					.regReceiver_and_bindService();
			player = MediaPlayer.create(this, R.raw.notificationsound);
		}

		if (this.nfcAdapter == null)
			return;
		if (!this.nfcAdapter.isEnabled()) {
			return;
		}
		this.nfcAdapter.enableForegroundDispatch(this, this.mPendingIntent,
				null, null);
	}

	@Override
	protected void onRestart() {
		IsRun = true;
		super.onRestart();
		if (AppContext.trackCheckPointBuffer != null
				|| AppContext.trackCheckPointBuffer.size() > 0) {
			mBaiduMap.clear();
			addCheckPointOverlay();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		IsRun = true;
		if (AppContext.trackCheckPointBuffer != null
				|| AppContext.trackCheckPointBuffer.size() > 0) {
			mBaiduMap.clear();
			addCheckPointOverlay();
		}

		processIntent(intent);
	}

	public void mapReceive(String curRfidNo) {
		if (curRfidNo != null && curRfidNo.length() > 0) {
			player.start();
			IsRun = true;
			if (AppContext.trackCheckPointBuffer != null
					|| AppContext.trackCheckPointBuffer.size() > 0) {
				mBaiduMap.clear();
				addCheckPointOverlay();
			}
			RefreshNFC(curRfidNo);
			// processIntent(intent);
		}
	}

	@Override
	protected void onDestroy() {
		SaveOverSpeedRecord();
		IsRun = false;
		// 退出时销毁定位
		BaiduMapNavigation.finish(this);
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		mMapView = null;
		handler.removeCallbacks(timer);
		// 回收 bitmap 资源
		cpPointRed.recycle();
		cpPointGreen.recycle();
		super.onDestroy();
		handler.removeCallbacks(pointtimer);
		timerUploadGps.cancel();
		lastGpsPosition = null;
		if (AppContext._currLocation != null)
			AppContext._currLocation = null;
		flag = false;
		// AppContext.locationManager.removeUpdates(locationListener);
		if (AppContext.getRFIDUseYN()) {
			RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
					.unRegReceiver_and_unbindRemoteService();
			if (player != null)
				player.release();
			// RFIDManager.getRFIDManager(Tool_NFC.this,
			// TOUCHIDPOS_TOOL_NFC).unRegReceiver_and_unbindRemoteService();
		}
		unregisterBoradcastReceiver();
	}

	OnKeyListener keyListener = new DialogInterface.OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			returnMapMain();
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (loading != null) {
				if (loading.isShowing()) {
					loading.setCancelable(false);
				}
				// else {
				// // loading.setCancelable(true);
				// returnMapMain();
				// }
			}
		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		View v = (View) lm_cpListView.getChildAt(info.position);
		CheckPointInfo cpInfo = null;
		if (v == null) {
			return false;
		}
		TextView aTitle = (TextView) v.findViewById(R.id.point_listitem_title);
		if (aTitle == null) {
			return false;
		}
		cpInfo = (CheckPointInfo) aTitle.getTag();

		if (cpInfo == null) {
			return false;
		}
		UIHelper.ToastMessage(
				Map_main.this,
				String.valueOf(item.getTitle()) + "-"
						+ String.valueOf(info.position) + "-"
						+ cpInfo.getDesc_TX());

		switch (item.getItemId()) {
		case MENUACTION_DOPLAN:
			enterDJResult();
			break;
		case MENUACTION_QUERY:
			UIHelper.ToastMessage(
					Map_main.this,
					getString(R.string.mapmain_message_inquiredata)
							+ cpInfo.getDesc_TX());
			break;
		case MENUACTION_RETURN:
			return false;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onGetOfflineMapState(int arg0, int arg1) {

	}

	private void changetabstyle(String mode) {
		if (mode.equals("MAP")) {
			mapModeTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			mapModeTV.getPaint().setAntiAlias(true);
			mapModeTV.setTextColor(Color.WHITE);
			listModeTV.getPaint().setFlags(0);
			listModeTV.setTextColor(Color.GRAY);
		} else if (mode.equals("LIST")) {
			listModeTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			listModeTV.getPaint().setAntiAlias(true);
			listModeTV.setTextColor(Color.WHITE);
			mapModeTV.getPaint().setFlags(0);
			mapModeTV.setTextColor(Color.GRAY);
		}
	}

	// 方法--初始化
	/**
	 * 初始化头部视图
	 */
	private void initUI() {
		listModeLL = (RelativeLayout) findViewById(R.id.ll_track_listmode);
		listModeLL.setVisibility(View.GONE);
		initListMode();
		mapModeTV = (TextView) findViewById(R.id.map_mode_mapmode);
		listModeTV = (TextView) findViewById(R.id.map_mode_listmode);
		changetabstyle("MAP");
		mapModeTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changetabstyle("MAP");
				listModeLL.setVisibility(View.GONE);
			}
		});
		listModeTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Map_main.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						changetabstyle("LIST");
						reflashListMode();
						// lm_CurrCPName.setText("");
						changeToListMode();
						listModeLL.setVisibility(View.VISIBLE);
						if (cplistPW.isShowing()) {
							cplistPW.dismiss();
						}
					}
				});

			}
		});
		fbMore = (ImageButton) findViewById(R.id.home_head_morebutton);
		fbMore.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mGrid.show(v);
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				returnMapMain();
			}
		});
		zoomBarLL = (RelativeLayout) findViewById(R.id.ll_map_zoombar);
		zoomlargeTV = (TextView) findViewById(R.id.map_zoom_large);
		zoomlargeTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float currZoomValue = mBaiduMap.getMapStatus().zoom + 1 > zoomMaxValue ? zoomMaxValue
						: mBaiduMap.getMapStatus().zoom + 1;
				if (mBaiduMap.getMapStatus().zoom + 1 > zoomMaxValue) {
					UIHelper.ToastMessage(Map_main.this,
							R.string.mapmain_message_zoomMaxMes);
					zoomBarLL
							.setBackgroundResource(R.drawable.map_zoombar_no_large);
					zoomlargeTV.setVisibility(View.INVISIBLE);
					zoomsmallTV.setVisibility(View.VISIBLE);
					// return;
				} else {
					zoomBarLL.setBackgroundResource(R.drawable.map_zoombar);
					zoomlargeTV.setVisibility(View.VISIBLE);
					zoomsmallTV.setVisibility(View.VISIBLE);
				}
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.zoomTo(currZoomValue));
			}
		});
		zoomsmallTV = (TextView) findViewById(R.id.map_zoom_small);
		zoomsmallTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float currZoomValue = mBaiduMap.getMapStatus().zoom - 1 < zoomMinValue ? zoomMinValue
						: mBaiduMap.getMapStatus().zoom - 1;
				if (mBaiduMap.getMapStatus().zoom - 1 < zoomMinValue) {
					UIHelper.ToastMessage(Map_main.this,
							R.string.mapmain_message_zoomMinMes);
					zoomBarLL
							.setBackgroundResource(R.drawable.map_zoombar_no_small);
					zoomsmallTV.setVisibility(View.INVISIBLE);
					zoomlargeTV.setVisibility(View.VISIBLE);
					// return;
				} else {
					zoomBarLL.setBackgroundResource(R.drawable.map_zoombar);
					zoomsmallTV.setVisibility(View.VISIBLE);
					zoomlargeTV.setVisibility(View.VISIBLE);
				}
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.zoomTo(currZoomValue));
			}
		});
		quickMemull = (RelativeLayout) findViewById(R.id.map_quick_memu);
		quickMemull.setVisibility(View.GONE);
		gpsStatell = (RelativeLayout) findViewById(R.id.map_gps_state);
		gpsStatell.setVisibility(View.VISIBLE);
		gpsStateTextView = (TextView) findViewById(R.id.map_gps_state_tv);
		gpsStateTextView
				.setText(getString(R.string.mapmain_message_locationunderway));
		quickMemuShowBtn = (ImageButton) findViewById(R.id.btn_map_quick_memu);
		quickMemuShowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (String.valueOf(((ImageButton) v).getTag()).equals("up")) {
					quickMemuShowBtn
							.setBackgroundResource(R.drawable.map_buttom_down_btn);
					quickMemuShowBtn.setTag("down");
					quickMemull.setVisibility(View.VISIBLE);
					gpsStatell.setVisibility(View.GONE);
				} else if (String.valueOf(((ImageButton) v).getTag()).equals(
						"down")) {
					quickMemuShowBtn
							.setBackgroundResource(R.drawable.map_buttom_up_btn);
					quickMemuShowBtn.setTag("up");
					quickMemull.setVisibility(View.GONE);
					gpsStatell.setVisibility(View.VISIBLE);
				}
			}
		});
		quickMemuPlanLL = (RelativeLayout) findViewById(R.id.ll_idpos_memu_idpos_select);
		quickMemuPlanLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enterDJResult();
			}
		});
		quickMemuBugLL = (RelativeLayout) findViewById(R.id.ll_idpos_memu_srsetup);
		quickMemuBugLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppContext.getRFIDUseYN()) {
					RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
							.unRegReceiver_and_unbindRemoteService();
					if (player != null)
						player.release();
				}
				UIHelper.showTaskInputbug(Map_main.this, "0", "LINE");
			}
		});
		quickMemuHistoryLL = (RelativeLayout) findViewById(R.id.ll_idpos_memu_scan);
		quickMemuHistoryLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppContext.getRFIDUseYN()) {
					RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
							.unRegReceiver_and_unbindRemoteService();
					if (player != null)
						player.release();
				}
				// UIHelper.showInitCheckPoint(Map_main.this);
				showInitCPFromMap(Map_main.this);
			}
		});
	}

	/**
	 * 退出导航界面（包括列表页面）
	 */
	private void returnMapMain() {
		// if(AppContext.trackCheckPointBuffer.size()<trackHelper.loadCheckPointData(this).size()){
		// return;
		// }

		int notArrivedNum = GPSHelper
				.notArrivedCheckPointNum(AppContext.trackCheckPointBuffer);
		if (notArrivedNum > 0) {
			String noticeString = getString(R.string.mapmain_message_quithint,
					notArrivedNum);
			AppContext.voiceConvertService.Speaking(noticeString);
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(
							getString(R.string.track_confirm_gobacktrack_title))
					.setView(view)
					.setMsg(noticeString)
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									genGPSPoentityForEnd();
									unregisterBoradcastReceiver();
									AppManager.getAppManager().finishActivity(
											Map_main.this);

								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									flag = false;
									threadPool.shutdown();

								}
							}).show();

		} else {
			genGPSPoentityForEnd();
			unregisterBoradcastReceiver();
			AppManager.getAppManager().finishActivity(Map_main.this);
			flag = false;
			threadPool.shutdown();
		}
	}

	public void unregisterBoradcastReceiver() {
		try {
			if (mBroadcastReceiver != null)
				this.unregisterReceiver(mBroadcastReceiver);
		} catch (IllegalArgumentException e) {
		}
	}

	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(Map_main.this);
		// 报缺
		mGrid.addQuickAction(new MyQuickAction(Map_main.this,
				R.drawable.map_pop_inputbug,
				R.string.mapmain_message_menu_repair));
		// 初始化
		mGrid.addQuickAction(new MyQuickAction(Map_main.this,
				R.drawable.map_pop_initcheckpoint,
				R.string.mapmain_message_menu_addpo));
		// 离线地图
		mGrid.addQuickAction(new MyQuickAction(Map_main.this,
				R.drawable.map_pop_offmap, R.string.mapmain_message_menu_offmap));
		// 做计划
		// mGrid.addQuickAction(new MyQuickAction(Map_main.this,
		// R.drawable.map_pop_initcheckpoint, R.string.map_menu_doplan));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			if (AppContext.getRFIDUseYN()) {
				RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
						.unRegReceiver_and_unbindRemoteService();
				if (player != null)
					player.release();
			}
			switch (position) {
			case PopMenu_Repair:// 报缺
				// UIHelper.showInputBug(Map_main.this, 1);
				UIHelper.showTaskInputbug(Map_main.this, "0", "LINE");
				break;
			case PopMenu_AddPo:// 初始化
				UIHelper.showInitCheckPoint(Map_main.this);
				break;
			case PopMenu_offmap:// 离线下载页面
				UIHelper.showOffLineMap(Map_main.this);
				break;
			}
		}
	};

	/**
	 * 初始化地图相关监听
	 */
	private void initListener() {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				if (menu != null && menu.isShowing())
					menu.dismiss();
				mBaiduMap.hideInfoWindow();
				((ImageButton) requestLocButton)
						.setImageDrawable(getResources().getDrawable(
								R.drawable.po_nor));
				mCurrentMode = LocationMode.NORMAL;
				mBaiduMap
						.setMyLocationConfigeration(new MyLocationConfiguration(
								mCurrentMode, true, mCurrentMarker));
				if (cplistPW != null && cplistPW.isShowing())
					cplistPW.dismiss();
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {

			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {

			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {

			}

			public void onMapStatusChangeFinish(MapStatus status) {

			}

			public void onMapStatusChange(MapStatus status) {

			}
		});
	}

	/**
	 * 初始化覆盖物
	 */
	public void initOverlay() {
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {

			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker arg0) {
				OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
					}
				};
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.map_popup_label);
				setMapLabelData(arg0, button);
				LatLng ll = arg0.getPosition();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(button), ll, -47, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}

		});
	}

	int pCount = 0;

	/**
	 * 列表————初始化
	 */
	private void initListMode() {
		lineTypeIV = (ImageView) findViewById(R.id.djline_type_icon);
		int linetypePic = R.drawable.widget_bar_xj_line;
		switch (djLineInfo.getlineType()) {
		case 0:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		case 1:
			linetypePic = R.drawable.widget_bar_djpc_line_xh;
			break;
		case 2:
			linetypePic = R.drawable.widget_bar_xg_line;
			break;
		case 3:
			linetypePic = R.drawable.widget_bar_jm_line;
			break;
		case 4:
			linetypePic = R.drawable.widget_bar_xs_line_xh;
			break;
		case 5:
			linetypePic = R.drawable.widget_bar_sis_line;
			break;
		case 6:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 7:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 8:
			linetypePic = R.drawable.widget_bar_case_line_xh;
			break;
		default:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		}
		lineTypeIV.setImageResource(linetypePic);
		lm_lineName = (TextView) findViewById(R.id.track_listmode_line_Value);
		lm_lineName.setText(djLineName);
		lm_CurrCPName = (TextView) findViewById(R.id.track_listmode_currcp_Value);
		lm_cpListView = (ListView) findViewById(R.id.track_listmode_cplist);
		reflashListMode();
		lm_cpListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// View v = (View) lm_cpListView.getChildAt(position);
				//
				// if (v == null) {
				// return;
				// }
				// TextView aTitle = (TextView) v
				// .findViewById(R.id.point_listitem_title);
				// if (aTitle == null) {
				// return;
				// }
				// cpInfo = (CheckPointInfo) aTitle.getTag();

				CheckPointInfo cpInfo = null;
				cpInfo = (CheckPointInfo) adapter.getItem(position);
				if (cpInfo == null) {
					return;
				}
				lastPointid = cpInfo.getGPSPosition_ID();
				enterDJResult();
			}

		});
		lm_cpListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					position = lm_cpListView.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				position = lm_cpListView.getFirstVisiblePosition();
			}
		});
	}

	/**
	 * 初始化PopupWindow菜单
	 */
	private void initMenu() {

		// 获取LayoutInflater实例
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		layout = inflater.inflate(R.layout.map_popwindow, null);
		// 设置popupWindow的布局
		menu = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		menu.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.rounded_corners_pop));
		cpName = (TextView) layout.findViewById(R.id.map_popwindow_cpname);
		cpJD = (TextView) layout.findViewById(R.id.map_popwindow_jd);
		cpWD = (TextView) layout.findViewById(R.id.map_popwindow_txt_wd);
		cpDate = (TextView) layout.findViewById(R.id.map_popwindow_txt_time);
		cpDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				enterDJResult();
			}
		});
	}

	private void initCPList() {
		cplistImgBtn = (ImageButton) findViewById(R.id.btn_showcp);
		cplistImgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cplistPW != null) {
					if (!cplistPW.isShowing())
						showCheckPointList();
					else {

						cplistPW.dismiss();
					}
				}
			}
		});
		mapChangeModeImgBtn = (ImageButton) findViewById(R.id.btn_mapmodechange);
		mapChangeModeImgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (mBaiduMap.getMapType()) {
				case BaiduMap.MAP_TYPE_NORMAL:
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
					break;
				case BaiduMap.MAP_TYPE_SATELLITE:
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					break;
				default:
					mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
					break;
				}
			}
		});
		// 获取LayoutInflater实例
		lm_cpinflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		lm_cplayout = lm_cpinflater.inflate(R.layout.main_map_checkpoint, null);
		// 设置popupWindow的布局
		cplistPW = new PopupWindow(lm_cplayout, getWindowManager()
				.getDefaultDisplay().getWidth() / 2, getWindowManager()
				.getDefaultDisplay().getHeight() / 2);
		lm_myCPListView = (ListView) lm_cplayout
				.findViewById(R.id.map_track_cplist);
	}

	// 自定义方法
	/**
	 * 刷新路线轨迹
	 */
	private void RefreshLine() {
		if (latLngPolygon.size() > 9999) {
			synchronized (latLngPolygon) {
				for (int i = 0; i < 1000; i++) {
					latLngPolygon.remove(i);
				}
			}
		}
		List<LatLng> points = latLngPolygon;
		OverlayOptions ooPolyline = new PolylineOptions().width(5)
				.color(0xAAFF0000).points(points);
		mBaiduMap.addOverlay(ooPolyline);
	}

	/**
	 * 跳转去做计划
	 */
	private void enterDJResult() {
		cycid = 1;
		if (lastPointid != -1 && cycid > 0) {
			// UIHelper.showDianjian(Map_main.this, djLineID, idPosID,
			// idPosName,cycid);
			int size = AppContext.gpsXXPlanBuffer.get(
					String.valueOf(lastPointid)).size();
			if (size != 0) {
				if (AppContext.getRFIDUseYN()) {
					RFIDManager.getRFIDManager(Map_main.this, MAP_NFC)
							.unRegReceiver_and_unbindRemoteService();
					if (player != null)
						player.release();
				}
				UIHelper.showGPSXXPlanList(Map_main.this, "", "z",
						String.valueOf(lastPointid));
			} else {
				UIHelper.ToastMessage(Map_main.this,
						getString(R.string.mapmain_message_nodoablehint));
			}
		} else {
			UIHelper.ToastMessage(Map_main.this,
					getString(R.string.mapmain_message_nodoablehint));
		}
	}

	/**
	 * 切换至list模式
	 * 
	 */
	private void changeToListMode() {
		// TODO:切换到列表模式做一些事情
	}

	private void reflashListMode() {
		if (!AppContext.orderCheckPointYN
				&& AppContext.trackCheckPointBuffer != null
				&& AppContext.trackCheckPointBuffer.size() > 0
				&& AppContext.trackCheckPointBuffer.get(0).getDesc_TX() != null) {
			lm_CurrCPName.setText(AppContext.trackCheckPointBuffer.get(0)
					.getDesc_TX());
		} else if (AppContext.orderCheckPointYN
				&& AppContext.orderSortTrackCheckPointBuffer != null
				&& AppContext.orderSortTrackCheckPointBuffer.size() > 0
				&& AppContext.orderSortTrackCheckPointBuffer.get(0)
						.getDesc_TX() != null) {
			lm_CurrCPName.setText(AppContext.orderSortTrackCheckPointBuffer
					.get(0).getDesc_TX());
		}

		if (AppContext.orderCheckPointYN
				&& AppContext.orderSortTrackCheckPointBuffer != null) {
			adapter = new MapPointAdapter(this,
					AppContext.orderSortTrackCheckPointBuffer,
					R.layout.listitem_pointinfo);
		} else {
			adapter = new MapPointAdapter(this,
					AppContext.trackCheckPointBuffer,
					R.layout.listitem_pointinfo);
		}
		lm_cpListView.setAdapter(adapter);
		lm_cpListView.refreshDrawableState();
	}

	/**
	 * 显示考核点列表 `
	 * 
	 * @param marker
	 */
	private void showCheckPointList() {
		if (AppContext.orderCheckPointYN
				&& AppContext.orderSortTrackCheckPointBuffer != null) {
			adapter = new MapPointAdapter(this,
					AppContext.orderSortTrackCheckPointBuffer,
					R.layout.listitem_checkpoint);
		} else {
			adapter = new MapPointAdapter(this,
					AppContext.trackCheckPointBuffer,
					R.layout.listitem_checkpoint);
		}
		lm_myCPListView.setAdapter(adapter);
		View parentView = findViewById(R.id.btn_showcp);
		cplistPW.showAsDropDown(parentView, 0, 0); // 设置在屏幕中的显示位置

	}

	/**
	 * 设置百度InfoWindow内信息
	 * 
	 * @param marker
	 * @param button
	 */
	private void setMapLabelData(Marker marker, Button button) {
		String text = "";
		text = marker.getTitle() + "\n";
		text += getString(R.string.mapmain_message_latitude)
				+ String.valueOf(marker.getPosition().latitude) + "  ";
		text += getString(R.string.mapmain_message_longitude)
				+ String.valueOf(marker.getPosition().longitude) + "  ";
		// text += getString(R.string.mapmain_message_plan);
		button.setText(text);
		button.setTextColor(Color.BLUE);
		button.setTextSize(12);
	}

	/**
	 * UI信息刷新计时器
	 */
	Runnable timer = new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					linecosttime++;
					int mins = linecosttime / 60;
					String minString = mins < 10 ? "0" + mins : String
							.valueOf(mins);
					int seconds = linecosttime % 60;
					String secondsString = seconds < 10 ? "0" + seconds
							: String.valueOf(seconds);
					t_info.setText(String.format(minString + ":"
							+ secondsString));

					// 每5分钟清理一次图层
					if (linecosttime % 300 == 0) {
						mBaiduMap.clear();
						addCheckPointOverlay();
						// RefreshLine();
					}
					handler.postDelayed(timer, 1000);
				}
			});

		}
	};

	private void uiReflashClock() {
		linecosttime = 0;
		handler.postDelayed(timer, 1000);
	}

	Runnable pointtimer = new Runnable() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					p2pcosttime++;
					overspeedtime++;
					handler.postDelayed(pointtimer, 1000);
				}
			});
		}
	};

	/**
	 * 考核点时间计时器
	 */
	private void pointClock() {
		p2pcosttime = 0;
		overspeedtime = 0;
		handler.postDelayed(pointtimer, 1000);
	}

	/**
	 * 加载数据
	 */
	private void loadData() {

		// 加载数据
		Context context = getBaseContext();
		trackHelper = new TrackHelper();
		// 加载考核点数据
		trackHelper.loadCheckPointData(context);
		// 加载故障代码库
		trackHelper.loadMobjectBugCodeData(context);
	}

	/**
	 * 加载数据的线程
	 */
	private void loadDataThread(final boolean isRefresh) {
		loading = new LoadingDialog(this);
		loading.setCanceledOnTouchOutside(true);
		loading.setOnKeyListener(keyListener);
		loading.setCancelable(false);
		loading.show();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					// 加载数据
					loadData();
					Thread.sleep(1000);
					msg.what = 1;
					msg.obj = AppContext.trackCheckPointBuffer;
				} catch (InterruptedException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mloaddataHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 加载路线下考核点信息并显示在地图上
	 */
	private void loadCheckPointToMapView() {
		mloaddataHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					addCheckPointOverlay();
					registerBoradcastReceiver();
				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(Map_main.this);
				}
			}
		};
		this.loadDataThread(false);
	}

	/**
	 * 在百度地图上显示考核点信息
	 */
	private synchronized void addCheckPointOverlay() {
		new Thread() {
			@Override
			public void run() {
				// add marker overlay
				// 这里要将GPS坐标按百度算法进行偏移
				if (cpMarkers != null && cpMarkers.size() > 0)
					cpMarkers.clear();
				CoordinateConverter converter = new CoordinateConverter();
				converter.from(CoordType.GPS);
				for (CheckPointInfo cpInfo : AppContext.trackCheckPointBuffer) {
					if (!IsRun)
						return;
					if (!(StringUtils.isEmpty(cpInfo.getLatitude()) && StringUtils
							.isEmpty(cpInfo.getLongitude()))) {
						converter.coord(new LatLng((Double.valueOf(cpInfo
								.getLatitude())), (Double.valueOf(cpInfo
								.getLongitude()))));
						LatLng ll = converter.convert();
						// LatLng ll = new LatLng(
						// (Double.valueOf(cpInfo.getLatitude())), (Double
						// .valueOf(cpInfo.getLongitude())));
						BitmapDescriptor pRed = cpInfo.getCheckPoint_Type()
								.toUpperCase().equals("P") ? cpPointRed
								: nfcPointRed;
						BitmapDescriptor pGreen = cpInfo.getCheckPoint_Type()
								.toUpperCase().equals("P") ? cpPointGreen
								: nfcPointGreen;
						OverlayOptions ooRed = new MarkerOptions().position(ll)
								.icon(pRed).zIndex(9)
								.title(cpInfo.getDesc_TX()).draggable(false);
						OverlayOptions ooGreen = new MarkerOptions()
								.position(ll).icon(pGreen).zIndex(9)
								.title(cpInfo.getDesc_TX()).draggable(false);
						Marker _tempMarker;
						if (cpInfo.getShakeNum() > 0)
							_tempMarker = (Marker) mBaiduMap
									.addOverlay(ooGreen);
						else {
							_tempMarker = (Marker) mBaiduMap.addOverlay(ooRed);
						}
						cpMarkers.put(cpInfo.getGPSPosition_ID(), _tempMarker);
					}
				}
				testtag = true;
			}
		}.start();
	}

	/**
	 * 生成GPS保存点实体并保存
	 * 
	 * @param latlng
	 */
	private void genGPSPoentity(BDLocation location) {

		if (lastGpsPosition == null
				|| StringUtils.isEmpty(lastGpsPosition.getUTC_DT())) {
			lastGpsPosition = new GPSPosition();
			gpsPohelper = new GPSPositionHelper();
			int locType = location.getLocType();
			if (locType == BDLocation.TypeGpsLocation) {
				lastGpsPosition.setBDLocationType_TX("GPS");
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				lastGpsPosition.setBDLocationType_TX("NETWORK");
			}
			if (locType == BDLocation.TypeGpsLocation
					|| locType == BDLocation.TypeNetWorkLocation) {
				LatLng desLatLng = GPSHelper.convertBaiduToGPS(new LatLng(
						location.getLatitude(), location.getLongitude()));
				lastGpsPosition.setLatitude_TX(String
						.valueOf(desLatLng.latitude));
				lastGpsPosition.setLongitude_TX(String
						.valueOf(desLatLng.longitude));
			}
			lastGpsPosition.setXJQGUID_TX(AppContext.GetUniqueID());
			lastGpsPosition.setXJQ_CD(AppContext.GetxjqCD());
			lastGpsPosition.setLine_ID(String.valueOf(AppContext
					.GetCurrLineID()));
			lastGpsPosition.setAppUser_CD(AppContext.getUserCD());
			lastGpsPosition.setAppUserName_TX(AppContext.getUserName());
			String curDate = DateTimeHelper.DateToString(new java.util.Date(
					System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
			lastGpsPosition.setUTC_DT(curDate);
			lastGpsPosition.setGpsDistance_TX("0");
			lastGpsPosition.setGpsCostTime_TX("0");

			lastGpsPosition.setGpsPointType_TX("START");
			lastGpsPosition.setOverSpeedYN(this
					.checkGpsinfoOverSpeed((int) (location.getSpeed() * 3.6)));
			lastGpsPosition
					.setSpeedNum(String.valueOf((location.getSpeed() * 3.6)));
			// gpsPohelper.InsertGPSPo(this, lastGpsPosition);
			s_info.setText(String.format(
					"%.2f",
					(Double.valueOf(lastGpsPosition.getGpsDistance_TX()) / 1000)));
			// mHandler.obtainMessage(LASTGPSPOSITIONDISTANCE,(Double.valueOf(lastGpsPosition.getGpsDistance_TX())
			// / 1000)).sendToTarget();
			if ("0".equals(lastGpsPosition.getLongitude_TX())) {
				lastGpsPosition.setLatitude_TX("");
			} else if ("0".equals(lastGpsPosition.getLongitude_TX())) {
				lastGpsPosition.setLongitude_TX("");
			}
			gpsPohelper.InsertGPSPo(this, lastGpsPosition);
		} else {
			GPSPosition gpsPo = new GPSPosition();
			gpsPohelper = new GPSPositionHelper();
			int locType = location.getLocType();
			if (locType == BDLocation.TypeGpsLocation) {
				gpsPo.setBDLocationType_TX("GPS");
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				gpsPo.setBDLocationType_TX("NETWORK");
			}
			if (locType == BDLocation.TypeGpsLocation
					|| locType == BDLocation.TypeNetWorkLocation) {
				LatLng desLatLng = GPSHelper.convertBaiduToGPS(new LatLng(
						location.getLatitude(), location.getLongitude()));

				gpsPo.setLatitude_TX(String.valueOf(desLatLng.latitude));
				gpsPo.setLongitude_TX(String.valueOf(desLatLng.longitude));
				double _distance = GPSHelper.GPSDistance(
						Double.valueOf(lastGpsPosition.getLatitude_TX()),
						Double.valueOf(lastGpsPosition.getLongitude_TX()),
						Double.valueOf(gpsPo.getLatitude_TX()),
						Double.valueOf(gpsPo.getLongitude_TX()));
				gpsPo.setGpsDistance_TX(String.valueOf(_distance
						+ Double.valueOf(lastGpsPosition.getGpsDistance_TX())));
				if (locType == BDLocation.TypeGpsLocation) {
					lastGpsPosition.setBDLocationType_TX("GPS");
				} else if (locType == BDLocation.TypeNetWorkLocation) {
					lastGpsPosition.setBDLocationType_TX("NETWORK");
				}
			} else {
				gpsPo.setLatitude_TX(lastGpsPosition.getLatitude_TX());
				gpsPo.setLongitude_TX(lastGpsPosition.getLongitude_TX());
				gpsPo.setGpsDistance_TX(lastGpsPosition.getGpsDistance_TX());
				return;
			}
			gpsPo.setXJQGUID_TX(AppContext.GetUniqueID());
			gpsPo.setXJQ_CD(AppContext.GetxjqCD());
			gpsPo.setLine_ID(String.valueOf(AppContext.GetCurrLineID()));
			gpsPo.setAppUser_CD(AppContext.getUserCD());
			gpsPo.setAppUserName_TX(AppContext.getUserName());
			String curDate = DateTimeHelper.DateToString(new java.util.Date(
					System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
			gpsPo.setUTC_DT(curDate);
			int _timespan = GPSHelper.GPSTimeSpan(lastGpsPosition.getUTC_DT(),
					gpsPo.getUTC_DT());
			gpsPo.setGpsCostTime_TX(String.valueOf(_timespan
					+ Integer.valueOf(lastGpsPosition.getGpsCostTime_TX())));
			gpsPo.setGpsPointType_TX("ING");
			gpsPo.setOverSpeedYN(this.checkGpsinfoOverSpeed((int) (location
					.getSpeed() * 3.6)));
			gpsPo.setSpeedNum(String.format("%.3f",
					Double.valueOf(location.getSpeed() * 3.6)));// 速度小数
			// gpsPohelper.InsertGPSPo(this, gpsPo);
			s_info.setText(String.format("%.2f",
					(Double.valueOf(gpsPo.getGpsDistance_TX()) / 1000)));
			// mHandler.obtainMessage(GPSPOSITIONDISTANCE,(Double.valueOf(gpsPo.getGpsDistance_TX())
			// / 1000)).sendToTarget();
			lastGpsPosition.setLatitude_TX(gpsPo.getLatitude_TX());
			lastGpsPosition.setLongitude_TX(gpsPo.getLongitude_TX());
			lastGpsPosition.setUTC_DT(gpsPo.getUTC_DT());
			lastGpsPosition.setGpsDistance_TX(gpsPo.getGpsDistance_TX());
			lastGpsPosition.setGpsCostTime_TX(gpsPo.getGpsCostTime_TX());
			lastGpsPosition.setGpsPointType_TX(gpsPo.getGpsPointType_TX());

			gpsPohelper.InsertGPSPo(this, gpsPo);
		}

	}

	/**
	 * 保存退出时的轨迹结束点
	 */
	private void genGPSPoentityForEnd() {
		if (lastGpsPosition == null
				|| StringUtils.isEmpty(lastGpsPosition.getLatitude_TX())
				|| StringUtils.isEmpty(lastGpsPosition.getLongitude_TX()))
			return;
		lastGpsPosition.setGpsPointType_TX("END");
		String curDate = DateTimeHelper.DateToString(
				DateTimeHelper.AddSeconds(
						new java.util.Date(System.currentTimeMillis()), 5),
				"yyyy-MM-dd HH:mm:ss");
		lastGpsPosition.setUTC_DT(curDate);
		gpsPohelper.InsertGPSPo(this, lastGpsPosition);
	}

	/**
	 * 插入巡线到位考核记录
	 * 
	 * @param location
	 */
	private void genTrackArriveResultentity(CheckPointInfo cPointInfo,
			String latitude, String longitude, String disTime) {
		lastPointid = cPointInfo.getGPSPosition_ID();
		GPSPositionForResult entity = new GPSPositionForResult();
		entity.setAppUser_ID(AppContext.getUserID());
		entity.setAppUserName_TX(AppContext.getUserName());
		entity.setGPSDesc("");
		entity.setGPSPosition_CD(cPointInfo.getGPSPosition_CD());
		entity.setGPSPosition_ID(cPointInfo.getGPSPosition_ID());
		entity.setLatitude(latitude);
		entity.setLineID(String.valueOf(AppContext.GetCurrLineID()));
		entity.setLongitude(longitude);
		String curDate = DateTimeHelper.DateToString(
				new java.util.Date(System.currentTimeMillis()),
				"yyyy-MM-dd HH:mm:ss");
		entity.setPost_ID(AppContext.getUserID());
		entity.setUTCDateTime(curDate);
		entity.setXJQMark(AppContext.GetxjqCD());
		entity.setDistanceTime(disTime);
		gpsPohelper.InsertGPSTrackArriveResult(this, entity);
		cPointInfo.setKHDate_DT(curDate);
		gpsPohelper.updateCheckPoint(this, cPointInfo);

		SaveOverSpeedRecord();

		if (cPointInfo.getCycle_ID() > -1) {
			trackHelper.loadGPSXXPlanData(this,
					String.valueOf(cPointInfo.getGPSPosition_ID()));
			if (AppContext.gpsXXPlanBuffer.containsKey(String
					.valueOf(cPointInfo.getGPSPosition_ID()))) {
				int size = AppContext.gpsXXPlanBuffer.get(
						String.valueOf(cPointInfo.getGPSPosition_ID())).size();
				if (size != 0) {
					UIHelper.showGPSXXPlanList(Map_main.this, "", "",
							String.valueOf(cPointInfo.getGPSPosition_ID()));
				} else {
					UIHelper.ToastMessage(Map_main.this,
							getString(R.string.mapmain_message_nodoablehint));
				}
			} else {
				UIHelper.ToastMessage(Map_main.this,
						getString(R.string.mapmain_message_nodoablehint));
			}
		} else {
			UIHelper.ToastMessage(Map_main.this,
					getString(R.string.mapmain_message_nodoablehint));
		}
	}

	private static final String TAG = "GPS Services";

	int notArrivedNum = 0;

	/**
	 * 提示并添加到位记录
	 * 
	 * @param currGpsPosition
	 * @return
	 */
	private void alertAndAddRecord() {
		if (AppContext.trackCheckPointBuffer == null || lastGpsPosition == null)
			return;
		GPSHelper.sortCheckPoint(AppContext.trackCheckPointBuffer,
				lastGpsPosition);
		reflashListMode();
		if (AppContext.trackCheckPointBuffer != null
				&& AppContext.trackCheckPointBuffer.size() > 0) {
			currCheckPointInfo = AppContext.trackCheckPointBuffer.get(0);
			if (AppContext.trackCheckPointBuffer.size() > 0) {
				String lastCheckPiontName = "";
				int min_distance = Integer
						.parseInt(AppContext.trackCheckPointBuffer.get(0)
								.getNearDistance());
				if (min_distance < Integer
						.parseInt(AppContext.trackCheckPointBuffer.get(0)
								.getDeviation())) {
					int shakeNum = AppContext.trackCheckPointBuffer.get(0)
							.getShakeNum();
					boolean shouldArltYN = false;
					int currKey = _indexOf(
							AppContext.orderSortTrackCheckPointBuffer,
							AppContext.trackCheckPointBuffer.get(0)
									.getGPSPosition_ID());
					if (currKey == 0
							&& AppContext.trackCheckPointBuffer.get(0)
									.getShakeNum() <= 0) {
						lastCheckPiontName = getString(R.string.mapmain_message_beginpatrol);
						shouldArltYN = true;
					}
					if (currKey > 0
							&& AppContext.orderSortTrackCheckPointBuffer.get(
									currKey - 1).getShakeNum() > 0) {
						lastCheckPiontName = AppContext.orderSortTrackCheckPointBuffer
								.get(currKey - 1).getDesc_TX();
						shouldArltYN = true;
					}
					if ((!AppContext.orderCheckPointYN) || shouldArltYN) {
						if (shakeNum == 0) {
							String noticeString = "";
							int mins = p2pcosttime / 60;
							if (AppContext.orderCheckPointYN
									&& (!StringUtils
											.isEmpty(lastCheckPiontName))) {
								try {
									int pCost = Integer
											.valueOf(AppContext.trackCheckPointBuffer
													.get(0)
													.getNextPointHour_NR());
									if (mins <= pCost) {
										noticeString = getString(
												R.string.mapmain_message_consumingtimehint,
												lastCheckPiontName, pCost, mins);
										UIHelper.ToastMessage(Map_main.this,
												noticeString);
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							CheckPointInfo _cp = AppContext.trackCheckPointBuffer
									.get(0);
							String emsg = "";
							if (!_cp.JudgePointTemp(
									DateTimeHelper.GetDateTimeNow(), emsg)) {
								// if (false) {
								if (!_cp.getremsg().equals("")) {
									LayoutInflater factory = LayoutInflater
											.from(this);
									final View view = factory.inflate(
											R.layout.textview_layout, null);
									new com.moons.xst.track.widget.AlertDialog(
											this)
											.builder()
											.setTitle(
													getString(R.string.track_confirm_gobacktrack_title))
											.setView(view)
											.setMsg(_cp.getremsg().toString())
											.setPositiveButton(
													getString(R.string.sure),
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															AppManager
																	.getAppManager()
																	.finishActivity(
																			Map_main.this);
														}
													}).show();

								} else {
									UIHelper.ToastMessage(
											getApplication(),
											getString(R.string.mapmain_message_absentscope),
											false);
									return;
								}

							} else {
								if (!_cp.JudgeIsCompleted(DateTimeHelper
										.GetDateTimeNow())) {
									if (_cp.getCheckPoint_Type().toUpperCase()
											.equals("P")) {
										p2pcosttime = 0;
										genTrackArriveResultentity(
												AppContext.trackCheckPointBuffer
														.get(0),
												lastGpsPosition
														.getLatitude_TX(),
												lastGpsPosition
														.getLongitude_TX(),
												String.valueOf(mins));

										noticeString += getString(
												R.string.mapmain_message_entranceareahint,
												AppContext.trackCheckPointBuffer
														.get(0).getDesc_TX());
										AppContext.voiceConvertService
												.Speaking(noticeString);
										UIHelper.ToastMessage(Map_main.this,
												noticeString);
										if (cpMarkers
												.containsKey(AppContext.trackCheckPointBuffer
														.get(0)
														.getGPSPosition_ID())) {
											cpMarkers
													.get(AppContext.trackCheckPointBuffer
															.get(0)
															.getGPSPosition_ID())
													.setIcon(cpPointGreen);
										}
									}

								} else {
									UIHelper.ToastMessage(
											getApplication(),
											getString(R.string.mapmain_message_assessaccomplishhint),
											false);
								}
							}
						}
					}
					if (shakeNum < 3
							&& ((!AppContext.orderCheckPointYN) || shouldArltYN)
							&& AppContext.trackCheckPointBuffer.get(0)
									.getCheckPoint_Type().toUpperCase()
									.equals("P")) {
						mVibrator.vibrate(1000);
						Toast.makeText(
								Map_main.this,
								getString(
										R.string.mapmain_message_entranceareahint,
										AppContext.trackCheckPointBuffer.get(0)
												.getDesc_TX()),
								Toast.LENGTH_LONG).show();
						AppContext.trackCheckPointBuffer.get(0).setShakeNum(
								shakeNum + 1);
						if (currKey != -1)
							AppContext.orderSortTrackCheckPointBuffer.get(
									currKey).setShakeNum(shakeNum + 1);
					}
				}
			}
		}
	}

	/**
	 * 超速提醒并记录
	 */
	private void alertAndAddOverSpeed(Float speed) {
		try {
			if (currCheckPointInfo != null
					&& (!StringUtils.isEmpty(currCheckPointInfo
							.getSpeedLimit_Num()))
					&& (!currCheckPointInfo.getSpeedLimit_Num().equals("0"))
					&& currCheckPointInfo.getOrder_YN().equals("1")) {
				float uperLimitSpeed = Float.parseFloat(currCheckPointInfo
						.getSpeedLimit_Num());
				if (uperLimitSpeed > 0 && speed > 0) {
					if (speed > uperLimitSpeed) {
						Uri notification = RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(
								getApplicationContext(), notification);
						r.play();

						isoverspeed = true;
					} else {
						SaveOverSpeedRecord();
						overspeedtime = 0;
					}
				}
			} else {
				overspeedtime = 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static int _indexOf(List<CheckPointInfo> arrayList, int ID) {
		int index = -1;
		if (arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<CheckPointInfo> iter = arrayList.iterator();
		while (iter.hasNext()) {
			index++;
			if (iter.next().getGPSPosition_ID() == ID) {
				return index;
			}
		}
		index = -1;
		return index;
	}

	private static int _indexOf(List<CheckPointInfo> arrayList, String name) {
		int index = -1;
		if (arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<CheckPointInfo> iter = arrayList.iterator();
		while (iter.hasNext()) {
			index++;
			if (iter.next().getDesc_TX().equals(name)) {
				return index;
			}
		}
		index = -1;
		return index;
	}

	private CheckPointInfo getCheckPointInfoByCD(
			List<CheckPointInfo> arrayList, String CD) {
		CheckPointInfo checkPointInfo = new CheckPointInfo();
		if (arrayList == null || arrayList.isEmpty()) {
			return null;
		}
		Iterator<CheckPointInfo> iter = arrayList.iterator();
		while (iter.hasNext()) {
			CheckPointInfo _cpInfo = iter.next();
			if (_cpInfo.getGPSPosition_CD().equals(CD)) {
				checkPointInfo = _cpInfo.Copy();
				return checkPointInfo;
			}
		}
		return null;
	}

	/**
	 * 实时更新文本内容
	 * 
	 * @param location
	 */
	private void updateView(BDLocation location) {
		try {
			String strLocationInfo = "";
			if (location != null) {
				int locType = location.getLocType();
				if (locType == BDLocation.TypeGpsLocation) {
					strLocationInfo = getString(R.string.mapmain_message_longitude2)
							+ location.getLongitude()
							+ "      "
							+ getString(R.string.mapmain_message_latitude2)
							+ location.getLatitude();
					gpsStateTextView.setText(strLocationInfo);
					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_gps);
					drawable.setBounds(10, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					gpsStateTextView.setCompoundDrawables(drawable, null, null,
							null);
					gpsStateTextView.setText(strLocationInfo);
				} else if (locType == BDLocation.TypeNetWorkLocation) {
					strLocationInfo = getString(R.string.mapmain_message_longitude2)
							+ location.getLongitude()
							+ "      "
							+ getString(R.string.mapmain_message_latitude2)
							+ location.getLatitude();

					Drawable drawable = getResources().getDrawable(
							R.drawable.icon_from_network);
					drawable.setBounds(10, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					gpsStateTextView.setCompoundDrawables(drawable, null, null,
							null);
					gpsStateTextView.setText(strLocationInfo);
				} else {
					gpsStateTextView.setCompoundDrawables(null, null, null,
							null);
					gpsStateTextView
							.setText(getString(R.string.mapmain_message_locationunderway));
				}
			} else {
				gpsStateTextView.setCompoundDrawables(null, null, null, null);
				gpsStateTextView
						.setText(getString(R.string.mapmain_message_locationunderway));
			}
		} catch (Exception e) {
			gpsStateTextView.setCompoundDrawables(null, null, null, null);
			gpsStateTextView
					.setText(getString(R.string.mapmain_message_locationunderway));
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"com.xst.track.service.updataCurrentLoction")) {
				initLocation(AppContext.getCurrLocation());
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.xst.track.service.gpsInfo");
		// 注册广播
		myIntentFilter.addAction("com.xst.track.service.updataCurrentLoction");
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 读取NFC标签信息
	 */
	protected void processIntent(Intent intent) {

		// 取出封装在intent中的TAG
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Ndef ndef = Ndef.get(tagFromIntent);
			byte[] bytesId = tagFromIntent.getId();
			try {
				String RFIDString = bytesToHexString(bytesId);
				CheckPointInfo _cp = getCheckPointInfoByCD(
						AppContext.trackCheckPointBuffer, RFIDString);
				for (int i = 0; i < AppContext.orderSortTrackCheckPointBuffer
						.size(); i++) {
					if (AppContext.orderSortTrackCheckPointBuffer.get(i)
							.getShakeNum() > 0) {
						lastPointid = AppContext.orderSortTrackCheckPointBuffer
								.get(i).getGPSPosition_ID();
					}
				}
				if (_cp == null) {
					String meString = getString(
							R.string.mapmain_messagez_fasteneridposnotuse,
							RFIDString);
					UIHelper.ToastMessage(getApplication(), meString, false);
				} else {
					BDLocation location = AppContext.getCurrLocation();
					String JD = "", WD = "";
					if (location != null) {
						JD = (String.valueOf(location.getLongitude()) + "00000000")
								.substring(0, 9);
						WD = (String.valueOf(location.getLatitude()) + "00000000")
								.substring(0, 9);
					}
					String emsg = "";
					// 非强制次序的情况
					if (!AppContext.orderCheckPointYN) {
						if (_cp.JudgePointTemp(DateTimeHelper.GetDateTimeNow(),
								emsg)
								&& !_cp.JudgeIsCompleted(DateTimeHelper
										.GetDateTimeNow())) {
							genTrackArriveResultentity(_cp, WD, JD, "0");
							String noticeString = getString(
									R.string.mapmain_message_entranceareahint,
									_cp.getDesc_TX());
							AppContext.voiceConvertService
									.Speaking(noticeString);
							UIHelper.ToastMessage(Map_main.this, noticeString);
							if (cpMarkers.containsKey(_cp.getGPSPosition_ID())) {
								cpMarkers.get(_cp.getGPSPosition_ID()).setIcon(
										nfcPointGreen);
							}
							int pono = _indexOf(
									AppContext.trackCheckPointBuffer,
									_cp.getGPSPosition_ID());
							AppContext.trackCheckPointBuffer.get(pono)
									.setShakeNum(3);
						} else {
							UIHelper.ToastMessage(
									getApplication(),
									getString(R.string.mapmain_message_fastenerpastduehint),
									false);
						}
					} else {// 强制次序

						int no = 0;
						if (lastPointid != -1) {
							no = _indexOf(
									AppContext.orderSortTrackCheckPointBuffer,
									lastPointid) + 1;
						}
						if (_cp.getSort_NR() == no + 1) {

							if (_cp.JudgePointTemp(
									DateTimeHelper.GetDateTimeNow(), emsg)
									&& !_cp.JudgeIsCompleted(DateTimeHelper
											.GetDateTimeNow())) {
								genTrackArriveResultentity(_cp, WD, JD, "0");
								String noticeString = getString(
										R.string.mapmain_message_entranceareahint,
										_cp.getDesc_TX());
								AppContext.voiceConvertService
										.Speaking(noticeString);
								UIHelper.ToastMessage(Map_main.this,
										noticeString);
								if (cpMarkers.containsKey(_cp
										.getGPSPosition_ID())) {
									cpMarkers.get(_cp.getGPSPosition_ID())
											.setIcon(nfcPointGreen);
								}
								int pono = _indexOf(
										AppContext.trackCheckPointBuffer,
										_cp.getGPSPosition_ID());
								AppContext.trackCheckPointBuffer.get(pono)
										.setShakeNum(3);
							} else {
								UIHelper.ToastMessage(
										getApplication(),
										getString(R.string.mapmain_message_fastenerpastduehint),
										false);
							}
						} else {
							UIHelper.ToastMessage(
									getApplication(),
									getString(
											R.string.mapmain_message_fastenercannot,
											AppContext.orderSortTrackCheckPointBuffer
													.get(no + 1).getDesc_TX()),
									false);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	public void RefreshNFC(String RFIDString) {
		try {
			// String RFIDString = bytesToHexString(bytesId);
			CheckPointInfo _cp = getCheckPointInfoByCD(
					AppContext.trackCheckPointBuffer, RFIDString);
			for (int i = 0; i < AppContext.orderSortTrackCheckPointBuffer
					.size(); i++) {
				if (AppContext.orderSortTrackCheckPointBuffer.get(i)
						.getShakeNum() > 0) {
					lastPointid = AppContext.orderSortTrackCheckPointBuffer
							.get(i).getGPSPosition_ID();
				}
			}
			if (_cp == null) {
				String meString = getString(
						R.string.mapmain_messagez_fasteneridposnotuse,
						RFIDString);
				UIHelper.ToastMessage(getApplication(), meString, false);
			} else {
				BDLocation location = AppContext.getCurrLocation();
				String JD = "", WD = "";
				if (location != null) {
					JD = (String.valueOf(location.getLongitude()) + "00000000")
							.substring(0, 9);
					WD = (String.valueOf(location.getLatitude()) + "00000000")
							.substring(0, 9);
				}
				String emsg = "";
				// 非强制次序的情况
				if (!AppContext.orderCheckPointYN) {
					if (_cp.JudgePointTemp(DateTimeHelper.GetDateTimeNow(),
							emsg)
							&& !_cp.JudgeIsCompleted(DateTimeHelper
									.GetDateTimeNow())) {
						genTrackArriveResultentity(_cp, WD, JD, "0");
						String noticeString = getString(
								R.string.mapmain_message_entranceareahint,
								_cp.getDesc_TX());
						AppContext.voiceConvertService.Speaking(noticeString);
						UIHelper.ToastMessage(Map_main.this, noticeString);
						if (cpMarkers.containsKey(_cp.getGPSPosition_ID())) {
							cpMarkers.get(_cp.getGPSPosition_ID()).setIcon(
									nfcPointGreen);
						}
						int pono = _indexOf(AppContext.trackCheckPointBuffer,
								_cp.getGPSPosition_ID());
						AppContext.trackCheckPointBuffer.get(pono).setShakeNum(
								3);
					} else {
						UIHelper.ToastMessage(
								getApplication(),
								getString(R.string.mapmain_message_fastenerpastduehint),
								false);
					}
				} else {// 强制次序

					int no = 0;
					if (lastPointid != -1) {
						no = _indexOf(
								AppContext.orderSortTrackCheckPointBuffer,
								lastPointid) + 1;
					}
					if (_cp.getSort_NR() == no + 1) {

						if (_cp.JudgePointTemp(DateTimeHelper.GetDateTimeNow(),
								emsg)
								&& !_cp.JudgeIsCompleted(DateTimeHelper
										.GetDateTimeNow())) {
							genTrackArriveResultentity(_cp, WD, JD, "0");
							String noticeString = getString(
									R.string.mapmain_message_entranceareahint,
									_cp.getDesc_TX());
							AppContext.voiceConvertService
									.Speaking(noticeString);
							UIHelper.ToastMessage(Map_main.this, noticeString);
							if (cpMarkers.containsKey(_cp.getGPSPosition_ID())) {
								cpMarkers.get(_cp.getGPSPosition_ID()).setIcon(
										nfcPointGreen);
							}
							int pono = _indexOf(
									AppContext.trackCheckPointBuffer,
									_cp.getGPSPosition_ID());
							AppContext.trackCheckPointBuffer.get(pono)
									.setShakeNum(3);
						} else {
							UIHelper.ToastMessage(
									getApplication(),
									getString(R.string.mapmain_message_fastenerpastduehint),
									false);
						}
					} else {

						UIHelper.ToastMessage(
								getApplication(),
								getString(
										R.string.mapmain_message_fastenercannot,
										AppContext.orderSortTrackCheckPointBuffer
												.get(no + 1).getDesc_TX()),
								false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private String checkGpsinfoOverSpeed(int speed) {
		if (currCheckPointInfo != null
				&& (!StringUtils
						.isEmpty(currCheckPointInfo.getSpeedLimit_Num()))
				&& (!currCheckPointInfo.getSpeedLimit_Num().equals("0"))
				&& currCheckPointInfo.getOrder_YN().equals("1")) {
			float uperLimitSpeed = Float.parseFloat(currCheckPointInfo
					.getSpeedLimit_Num());
			if (uperLimitSpeed < (float) speed)
				return "1";
			else
				return "0";
		}

		return "0";
	}

	private void SaveOverSpeedRecord() {
		if (isoverspeed) {
			OverSpeedRecordInfo _info = new OverSpeedRecordInfo();
			UUID uuid = UUID.randomUUID();
			_info.setOverSpeedRecord_ID(uuid.toString());
			_info.setCheckPoint_ID(Integer.toString(currCheckPointInfo
					.getGPSPosition_ID()));
			_info.setBeginDT(DateTimeHelper.DateToString(DateTimeHelper
					.AddSeconds(DateTimeHelper.GetDateTimeNow(), -overspeedtime)));
			_info.setLine_ID(String.valueOf(AppContext.GetCurrLineID()));
			_info.setLimitSpeed(currCheckPointInfo.getSpeedLimit_Num());
			_info.setEndDT(DateTimeHelper.getDateTimeNow());
			_info.setTopSpeed("");
			_info.setOverTimeLong(String.valueOf(overspeedtime));
			if (overspeedtime > 10) {
				gpsPohelper.InsertOverSpeedRecord(getApplicationContext(),
						_info);
			}
			isoverspeed = false;
			overspeedtime = 0;
		}
	}

	/**
	 * 字符序列转换为16进制字符串
	 * 
	 * @param src
	 * @return
	 */
	private String bytesToHexString(byte[] src) {
		StringBuffer sb = new StringBuffer(src.length);
		String sTemp;
		for (int i = src.length - 1; i >= 0; i--) {
			sTemp = Integer.toHexString(0xFF & src[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.tips))
				.setView(view)
				.setMsg(getString(R.string.mapmain_message_baiduversionshint))
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								OpenClientUtil
										.getLatestBaiduMapApp(Map_main.this);
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

	}

	/**
	 * 地图界面初始化考核点
	 * 
	 * @param context
	 */
	private void showInitCPFromMap(final Context context) {

		LayoutInflater factory = LayoutInflater.from(context);
		final View view = factory.inflate(R.layout.editbox_layout, null);
		final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
		new com.moons.xst.track.widget.AlertDialog(context)
				.builder()
				.setTitle(context.getString(R.string.mapmain_message_importpwd))
				.setEditView(view, InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT, "", true)
				.setPositiveButton(context.getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								final LoadingDialog loading = new LoadingDialog(
										context);
								final Handler scHandler;
								final String p = edit.getText().toString();
								scHandler = new Handler() {
									public void handleMessage(Message msg) {
										if (loading != null)
											loading.dismiss();
										if (msg.what == 1 && msg.obj != null) {
											String psw = msg.obj.toString();
											if (p.equals(psw)) {
												final List<String> cpNames = new ArrayList<String>();
												for (int i = 0; i < AppContext.trackCheckPointBuffer
														.size(); i++) {
													cpNames.add(AppContext.trackCheckPointBuffer
															.get(i)
															.getDesc_TX());
												}

												new ActionSheetDialog(context)
														.builder()
														.setTitle(
																context.getString(R.string.choice))
														.setCancelable(false)
														.setCanceledOnTouchOutside(
																false)
														.addSheetItems(
																cpNames,
																SheetItemColor.Blue,
																new OnSheetItemClickListener() {
																	@Override
																	public void onClick(
																			int which) {
																		final int pos = _indexOf(
																				AppContext.trackCheckPointBuffer,
																				cpNames.get(which - 1));
																		String infoString = "";
																		if (AppContext
																				.getCurrLocation() == null
																				|| AppContext
																						.getCurrLocation()
																						.getLatitude() <= 0
																				|| AppContext
																						.getCurrLocation()
																						.getLongitude() <= 0) {
																			infoString = context
																					.getString(R.string.track_confirm_init_desc4);
																		} else {
																			infoString = context
																					.getString(
																							R.string.track_confirm_init_desc1)
																					.replace(
																							"%s",
																							AppContext.trackCheckPointBuffer
																									.get(pos)
																									.getDesc_TX());
																		}

																		LayoutInflater factory = LayoutInflater
																				.from(Map_main.this);
																		final View view = factory
																				.inflate(
																						R.layout.textview_layout,
																						null);
																		new com.moons.xst.track.widget.AlertDialog(
																				Map_main.this)
																				.builder()
																				.setTitle(
																						getString(R.string.track_initcp_title))
																				.setView(
																						view)
																				.setMsg(infoString)
																				.setPositiveButton(
																						getString(R.string.sure),
																						new OnClickListener() {
																							@Override
																							public void onClick(
																									View v) {
																								try {
																									if (AppContext
																											.getCurrLocation() == null
																											|| AppContext
																													.getCurrLocation()
																													.getLatitude() <= 0
																											|| AppContext
																													.getCurrLocation()
																													.getLongitude() <= 0) {
																										return;
																									}
																									if (updateCheckPo(AppContext.trackCheckPointBuffer
																											.get(pos))
																											&& insertGPSInit(AppContext.trackCheckPointBuffer
																													.get(pos))) {
																										UIHelper.ToastMessage(
																												context,
																												R.string.initcp_message_savesucceed);
																										if (AppContext.trackCheckPointBuffer != null
																												|| AppContext.trackCheckPointBuffer
																														.size() > 0) {
																											mBaiduMap
																													.clear();
																											addCheckPointOverlay();
																										}
																									}
																								} catch (Exception e) {
																									// TODO:
																									// handle
																									// exception
																									e.printStackTrace();
																									UIHelper.ToastMessage(
																											context,
																											R.string.initcp_message_savedefeated
																													+ e.getMessage());
																								}
																							}
																						})
																				.setNegativeButton(
																						getString(R.string.cancel),
																						new OnClickListener() {
																							@Override
																							public void onClick(
																									View v) {
																							}
																						})
																				.show();
																	}
																}).show();
											} else {
												UIHelper.ToastMessage(
														context,
														R.string.mapmain_message_pwdmistake);
											}
										} else if (msg.obj != null) {

										}
									}
								};
								loading.show();
								new Thread() {
									public void run() {
										Message msg = Message.obtain();
										String psw = "";
										try {
											if (!WebserviceFactory.checkWS()) {
												String filePathString = AppConst
														.XSTBasePath()
														+ AppConst.InitPSWXmlFile;
												String temppath = AppConst
														.XSTBasePath()
														+ AppConst.tempInitPSWXmlFile;
												//FileEncryptAndDecrypt.decrypt(
												//		filePathString,
												//		temppath);
												psw = FileUtils.read(temppath);
												File f = new File(temppath);
												f.delete();
											} else {
												psw = WebserviceFactory
														.getGPSInitPsw(context);
											}
											// 重新加载路线
											msg.what = 1;
											msg.obj = psw;
										} catch (Exception e) {
											e.printStackTrace();
											msg.what = -1;
											msg.obj = e;
										}
										scHandler.sendMessage(msg);
									}
								}.start();

							}
						})
				.setNegativeButton(context.getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();
	}

	/**
	 * 更新考核点
	 * 
	 */
	public boolean updateCheckPo(CheckPointInfo Point) {
		int pos = _indexOf(AppContext.trackCheckPointBuffer,
				Point.getGPSPosition_ID());
		String lat = String.valueOf(AppContext.getCurrLocation().getLatitude());
		String lot = String
				.valueOf(AppContext.getCurrLocation().getLongitude());

		LatLng desLatLng = GPSHelper.convertBaiduToGPS(new LatLng(Double
				.parseDouble(lat), Double.parseDouble(lot)));
		AppContext.trackCheckPointBuffer.get(pos).setLatitude(
				String.valueOf(desLatLng.latitude));
		AppContext.trackCheckPointBuffer.get(pos).setLongitude(
				String.valueOf(desLatLng.longitude));
		CheckPointInfo PointInfo = AppContext.trackCheckPointBuffer.get(pos);
		trackHelper.updateCheckPointData(this, PointInfo);
		return true;
	}

	/**
	 * 插入初始化库
	 * 
	 */
	public boolean insertGPSInit(CheckPointInfo Point) {
		int pos = _indexOf(AppContext.trackCheckPointBuffer,
				Point.getGPSPosition_ID());
		GPSPositionForInit gpsinit = new GPSPositionForInit();
		gpsinit.setGPSPosition_ID(Point.getGPSPosition_ID());
		gpsinit.setGPSPosition_CD(Point.getGPSPosition_CD());
		gpsinit.setXJQMark(AppContext.GetxjqCD());
		gpsinit.setLongitude(AppContext.trackCheckPointBuffer.get(pos)
				.getLongitude());
		gpsinit.setLatitude(AppContext.trackCheckPointBuffer.get(pos)
				.getLatitude());
		gpsinit.setUTCDateTime(Point.getUTCDateTime());
		gpsinit.setGPSDesc(Point.getDesc_TX());
		gpsinit.setLineID(String.valueOf(AppContext.GetCurrLineID()));
		if (StringUtils.isEmpty(gpsinit.getLatitude())
				|| StringUtils.isEmpty(gpsinit.getLongitude())) {
			UIHelper.ToastMessage(getApplication(),
					R.string.initcp_message_insertdefeatedhint);
			return false;
		}
		gpsPohelper.InsertGPSInit(this, gpsinit);
		return true;
	}
}