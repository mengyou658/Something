package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.moons.xst.buss.CycleHelper;
import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.DJResultHelper;
import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.buss.XSTMethodByLineTypeHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJIDPos_ListViewAdapter;
import com.moons.xst.track.bean.DJ_ControlPoint;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.bean.DJ_TaskIDPosActive;
import com.moons.xst.track.bean.GPSPosition;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.GPSHelper;
import com.moons.xst.track.common.RFIDManager;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.xstinterface.SimpleMultiListViewDialogListener;

/**
 * 碰钮扣处理
 * 
 * 主要完成钮扣钮扣加载、钮扣下计划的计算 、NFC读取钮扣编码
 * 
 * @author LKZ
 * 
 */
public class DianjianTouchIDPos extends BaseActivity {

	static DianjianTouchIDPos instance = null;

	private final static String TOUCHIDPOS_DJIDPOS = "DJIDPos";

	private final static String STATE_PROCESSING = "PROCESSING";
	private final static String STATE_FINISHED = "FINISHED";

	private static boolean isFinished = false;

	private boolean canScanYn = false;
	private ImageButton returnButton, moreImageButton;
	private RelativeLayout rlidposStatus, rlidposStatusTotal,
			rlidposStatusUncomplete, rlidposStatusComplete;
	private View view1, viewTotal, viewUncomplete, viewComplete;
	private ImageView iv_total, iv_uncomplete, iv_complete;
	private LoadingDialog loading;
	private RelativeLayout scanShowLayout;
	private AppContext appContext;// 全局Context
	private RelativeLayout idposSelect, srSetup, scanBtn;
	private ProgressBar loadProgressBar;
	private Boolean isRFID = false;

	NfcAdapter nfcAdapter;
	PendingIntent mPendingIntent;
	String RFIDString = "";
	String RFIDExpMessageString = "";
	private static DJIDPos_ListViewAdapter lvDJIDPosStatusAdapter;
	private ListView lvDJIDPosStatus;
	private ImageButton scanShowBtn;
	private int lastIDPosSort = -1;

	// 历史结果相关
	/*private RelativeLayout resHisRelativeLayout;
	private TextView hisDataTextView;
	private Button hisDataButton;
	private ListView planhisdataListView;
	List<DJ_TaskIDPosHis> planidhislist = new ArrayList<DJ_TaskIDPosHis>();
	private DJQuery_hisdataAdapter planhisadapter;*/

	// 当前的路线ID
	private int current_djlineID = 0;
	private String current_djlineName = "";
	private final static int REQUEST_CODE = 2;

	private GPSPosition lastGpsPosition = null;
	private GPSPositionHelper gpsPohelper;

//	MediaPlayer player;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO 自动生成的方法存根
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianjian_touchidpos);
		appContext = (AppContext) getApplication();
		if (savedInstanceState != null) {
			current_djlineID = savedInstanceState.getInt("current_djlineID");
			current_djlineName = savedInstanceState.getString("line_name");
			currCaseIndex = savedInstanceState.getInt("currCaseIndex");
		} else {
			current_djlineID = Integer.parseInt(getIntent().getExtras()
					.getString("line_id"));
			current_djlineName = getIntent().getStringExtra("line_name");
		}

		lastGpsPosition = new GPSPosition();
		gpsPohelper = new GPSPositionHelper();

		// 注册广播
		registerBoradcastReceiver();
		// 初始化Head
		this.initHeadViewAndMoreBar();

		initListView();
		// 异步加载钮扣
		isFinished = false;		
		asyncLoadIDPos();

		instance = this;
		/* 不是600机型，获取NFC控制器 */
		if (!AppContext.getRFIDUseYN()) {
			// 获取默认的NFC控制器
			isNFCUse();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putInt("current_djlineID", current_djlineID);
		outState.putString("line_name", current_djlineName);
		outState.putInt("currCaseIndex", currCaseIndex);
	}

	private void insertGPSInfo() {
		currLocation = AppContext.getCurrLocation();
		if (currLocation != null) {
			genGPSPoentity(currLocation);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppContext.setIsUploading(true);
		if (isFinished){
			
			iv_total.setImageResource(R.drawable.ic_idpos_total_xh);
			iv_uncomplete
					.setImageResource(R.drawable.ic_idpos_uncomplete_selected_xh);
			iv_complete.setImageResource(R.drawable.ic_idpos_complete_xh);
			viewTotal.setVisibility(View.INVISIBLE);
			viewUncomplete.setVisibility(View.VISIBLE);
			viewComplete.setVisibility(View.INVISIBLE);
			currCaseIndex = 1;
			//selectIDPosbyCase(currCaseIndex);
			
			getIDPosState();
		}
		bindingService();
	}
	
	private void bindingService(){
		/* 600机型，注册接收器及绑定Service */
		if (AppContext.getRFIDUseYN()) {
			AppContext.mStartRFID = AppContext.StartRFID.DJ_IDPOS.toString();
		 	RFIDManager.getRFIDManager(DianjianTouchIDPos.this,
		 			TOUCHIDPOS_DJIDPOS).regReceiver_and_bindService();
		} else {
			if (this.nfcAdapter != null && this.nfcAdapter.isEnabled()) {			
				this.nfcAdapter.enableForegroundDispatch(this,
						this.mPendingIntent, null, null);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lastGpsPosition = null;
		AppContext.setIDCD("");
		AppContext.setIDPosName("");
		AppContext.locationManager.removeUpdates(locationListener);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onRestoreInstanceState(savedInstanceState);
		savedInstanceState.putInt("current_djlineID", current_djlineID);
		savedInstanceState.putString("current_djlineName", current_djlineName);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeReust = bundle.getString("codeResult");
				if (!StringUtils.isEmpty(codeReust))
					touchIDPos(codeReust, false);
			}
		}
	}

	public static DianjianTouchIDPos getInstance() {
		return instance;
	}

	private View.OnClickListener MyListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			if (!isFinished) {
				UIHelper.ToastMessage(DianjianTouchIDPos.this, R.string.djidpos_message_idposloding);
				return;
			}
			switch (v.getId()) {
			case (R.id.rl_dianjian_touchidpos_statistics_total):
				iv_total.setImageResource(R.drawable.ic_idpos_total_selected_xh);
				iv_uncomplete
						.setImageResource(R.drawable.ic_idpos_uncomplete_xh);
				iv_complete.setImageResource(R.drawable.ic_idpos_complete_xh);
				viewTotal.setVisibility(View.VISIBLE);
				viewUncomplete.setVisibility(View.INVISIBLE);
				viewComplete.setVisibility(View.INVISIBLE);
				currCaseIndex = 2;
				selectIDPosbyCase(currCaseIndex);
				break;
			case (R.id.rl_dianjian_touchidpos_statistics_complete):
				iv_total.setImageResource(R.drawable.ic_idpos_total_xh);
				iv_uncomplete
						.setImageResource(R.drawable.ic_idpos_uncomplete_xh);
				iv_complete
						.setImageResource(R.drawable.ic_idpos_complete_selected_xh);
				viewTotal.setVisibility(View.INVISIBLE);
				viewUncomplete.setVisibility(View.INVISIBLE);
				viewComplete.setVisibility(View.VISIBLE);
				currCaseIndex = 0;
				selectIDPosbyCase(currCaseIndex);
				break;
			case (R.id.rl_dianjian_touchidpos_statistics_uncomplete):
				iv_total.setImageResource(R.drawable.ic_idpos_total_xh);
				iv_uncomplete
						.setImageResource(R.drawable.ic_idpos_uncomplete_selected_xh);
				iv_complete.setImageResource(R.drawable.ic_idpos_complete_xh);
				viewTotal.setVisibility(View.INVISIBLE);
				viewUncomplete.setVisibility(View.VISIBLE);
				viewComplete.setVisibility(View.INVISIBLE);
				currCaseIndex = 1;
				selectIDPosbyCase(currCaseIndex);
				break;
			}
		}
	};

	private void isNFCUse() {
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (this.nfcAdapter == null) {
			UIHelper.ToastMessage(getBaseContext(), R.string.djidpos_message_notsupportnfc);
			return;
		}
		
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		if (!this.nfcAdapter.isEnabled()) {
			UIHelper.ToastMessage(getBaseContext(), R.string.djidpos_message_opennfc);
			return;
		}
		/*mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);*/
	}

	/**
	 * 初始化头部视图
	 */
	private void initHeadViewAndMoreBar() {

		TextView head_title = (TextView) findViewById(R.id.djanjian_touchidpos_head_title);
		head_title.setText(current_djlineName);
		TextView touchidpos_username = (TextView) findViewById(R.id.dianjian_touchidpos_username);
		touchidpos_username.setText(AppContext.getUserName());
		scanShowLayout = (RelativeLayout) findViewById(R.id.ll_dianjian_touchidpos_scanImge);
		scanShowLayout.setVisibility(View.VISIBLE);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				goBack();
			}
		});
		scanShowBtn = (ImageButton) findViewById(R.id.btn_idpos_scan);
		// scanShowBtn.setBackgroundResource(R.drawable.scan_no);
		// scanShowBtn.setTag("no");
		scanShowBtn.setBackgroundResource(R.drawable.scan_yes);
		scanShowBtn.setTag("yes");
		scanShowBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (String.valueOf(((ImageButton) v).getTag()).equals("no")) {
					scanShowBtn.setBackgroundResource(R.drawable.scan_yes);
					scanShowBtn.setTag("yes");
					scanShowLayout.setVisibility(View.VISIBLE);
					canScanYn = true;
					AppContext.voiceConvertService.Speaking(DianjianTouchIDPos.this
							.getString(R.string.djidpos_message_scan_notice));
				} else if (String.valueOf(((ImageButton) v).getTag()).equals(
						"yes")) {
					scanShowBtn.setBackgroundResource(R.drawable.scan_no);
					scanShowBtn.setTag("no");
					scanShowLayout.setVisibility(View.GONE);
					canScanYn = false;
				}
			}
		});
		
		scanShowLayout.setVisibility(View.VISIBLE);
		canScanYn = String.valueOf(scanShowBtn.getTag()).equals("yes");
		moreImageButton = (ImageButton) findViewById(R.id.home_head_morebutton);
		moreImageButton.setVisibility(View.GONE);
		moreImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				UIHelper.CalacCycle(DianjianTouchIDPos.this);
			}
		});

		idposSelect = (RelativeLayout) findViewById(R.id.ll_idpos_memu_idpos_select);
		idposSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (AppContext.getConfigDoPlanByScan()) {
					Intent intent = new Intent();
					intent.setClass(DianjianTouchIDPos.this, CaptureActivity.class);
					intent.putExtra("ScanType", "ISPOS");
					startActivityForResult(intent, REQUEST_CODE);
				} else {
					UIHelper.ToastMessage(DianjianTouchIDPos.this,
							getString(R.string.idpos_scannotuse));
				}
			}
		});
		srSetup = (RelativeLayout) findViewById(R.id.ll_idpos_memu_srsetup);
		srSetup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				//
				
				if (XSTMethodByLineTypeHelper
						.getInstance()
						.showWWSRYN(DianjianTouchIDPos.this, isFinished)) {
					setSR();
				}
					
				/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType()) {
					if (AppContext.DJSpecCaseFlag == 1) {
						UIHelper.ToastMessage(DianjianTouchIDPos.this,
								R.string.djidpos_message_cannotsetsrforspeccase);
					} else {
						if (isFinished)
							setSR();
						else
							UIHelper.ToastMessage(DianjianTouchIDPos.this,
									R.string.djidpos_message_idposloding);
					}
				} else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					UIHelper.ToastMessage(DianjianTouchIDPos.this,
							R.string.djidpos_message_cannotsetsrforcasexj);
				}*/
			}
		});
		scanBtn = (RelativeLayout) findViewById(R.id.ll_idpos_memu_scan);
		scanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

			}
		});

		loadProgressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			if (isFinished) {
				goBack();
				return true;
			} else {
				UIHelper.ToastMessage(DianjianTouchIDPos.this, R.string.djidpos_message_idposloding);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void registerGPS() {
		if (AppContext.locationManager == null)
			AppContext.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 为获取地理位置信息时设置查询条件
		String bestProvider = AppContext.locationManager.getBestProvider(
				getCriteria(), true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传入的参数为LocationManager.GPS_PROVIDER
		Location location = AppContext.locationManager
				.getLastKnownLocation(bestProvider);
		// 监听状态
		AppContext.locationManager.addGpsStatusListener(listener);
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 1秒更新一次，或最小位移变化超过1米更新一次；
		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		AppContext.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
	}

	/**
	 * GPS位置监听
	 */
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			// 记录轨迹
			// genGPSPoentity(location);
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = AppContext.locationManager
					.getLastKnownLocation(provider);
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
		}
	};
	// 状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
		};
	};

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

				// lastGpsPosition.setLatitude_TX(String.valueOf(location
				// .getLatitude()));
				// lastGpsPosition.setLongitude_TX(String.valueOf(location
				// .getLongitude()));

			}
			lastGpsPosition.setXJQGUID_TX(AppContext.GetUniqueID());
			lastGpsPosition.setXJQ_CD(AppContext.GetxjqCD());
			lastGpsPosition.setLine_ID(String.valueOf(AppContext
					.GetCurrLineID()));
			lastGpsPosition.setAppUser_CD(AppContext.getUserCD());
			lastGpsPosition.setAppUserName_TX(AppContext.getUserName());
			lastGpsPosition.setID_CD(AppContext.getIDCD());
			lastGpsPosition.setPlace_TX(AppContext.getIDPosName());
			// String curDate = DateTimeHelper.DateToString(new java.util.Date(
			// System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
			lastGpsPosition.setUTC_DT(location.getTime());
			lastGpsPosition.setGpsDistance_TX("0");
			lastGpsPosition.setGpsCostTime_TX("0");
			lastGpsPosition.setGpsPointType_TX("START");
			lastGpsPosition.setOverSpeedYN("0");
			lastGpsPosition.setSpeedNum("0");
			// gpsPohelper.InsertGPSPo(this, lastGpsPosition);
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

				// gpsPo.setLatitude_TX(String.valueOf(location.getLatitude()));
				// gpsPo.setLongitude_TX(String.valueOf(location.getLongitude()));

				double _distance = GPSHelper.GPSDistance(
						Double.valueOf(lastGpsPosition.getLatitude_TX()),
						Double.valueOf(lastGpsPosition.getLongitude_TX()),
						Double.valueOf(gpsPo.getLatitude_TX()),
						Double.valueOf(gpsPo.getLongitude_TX()));
				gpsPo.setGpsDistance_TX(String.valueOf(_distance
						+ Double.valueOf(lastGpsPosition.getGpsDistance_TX())));
			} else {
				return;
			}
			gpsPo.setXJQGUID_TX(AppContext.GetUniqueID());
			gpsPo.setXJQ_CD(AppContext.GetxjqCD());
			gpsPo.setLine_ID(String.valueOf(AppContext.GetCurrLineID()));
			gpsPo.setAppUser_CD(AppContext.getUserCD());
			gpsPo.setAppUserName_TX(AppContext.getUserName());
			gpsPo.setID_CD(AppContext.getIDCD());
			gpsPo.setPlace_TX(AppContext.getIDPosName());
			// String curDate = DateTimeHelper.DateToString(new java.util.Date(
			// System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
			gpsPo.setUTC_DT(location.getTime());

			int _timespan = GPSHelper.GPSTimeSpan(lastGpsPosition.getUTC_DT(),
					gpsPo.getUTC_DT());
			gpsPo.setGpsCostTime_TX(String.valueOf(_timespan
					+ Integer.valueOf(lastGpsPosition.getGpsCostTime_TX())));
			gpsPo.setGpsPointType_TX("ING");
			gpsPo.setOverSpeedYN("0");
			gpsPo.setSpeedNum(String.format("%.3f",
					Double.valueOf(location.getSpeed() * 3.6)));// 速度小数
			// gpsPohelper.InsertGPSPo(this, gpsPo);

			lastGpsPosition.setLatitude_TX(gpsPo.getLatitude_TX());
			lastGpsPosition.setLongitude_TX(gpsPo.getLongitude_TX());
			lastGpsPosition.setUTC_DT(gpsPo.getUTC_DT());
			lastGpsPosition.setGpsDistance_TX(gpsPo.getGpsDistance_TX());
			lastGpsPosition.setGpsCostTime_TX(gpsPo.getGpsCostTime_TX());
			lastGpsPosition.setGpsPointType_TX(gpsPo.getGpsPointType_TX());

			if ("0".equals(gpsPo.getLongitude_TX())) {
				gpsPo.setLatitude_TX("");
			} else if ("0".equals(gpsPo.getLongitude_TX())) {
				gpsPo.setLongitude_TX("");
			}
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
	 * 加载路线下的ID位置记录
	 */
	private void loadDJIDPosStatusData() {
		// 加载数据
		if (DJPlanHelper.GetIntance().loadControlPoint(this)
				|| CycleHelper.GetIntance().loadCycleData(this)
				|| DJPlanHelper.GetIntance().loadMobjectState(this)
				|| DJIDPosHelper.GetIntance().loadDJPosData(this)) {
			isFinished = true;
			//发送handler提示
			Message msg = Message.obtain();
			msg.what=1;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 点击Item
	 * 
	 * @author LKZ
	 * 
	 */
	private class myListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long ID) {
			// TODO Auto-generated method stub
			TextView viewTitile = (TextView) view
					.findViewById(R.id.djidpos_listitem_title);
			DJ_IDPos _idPos = (DJ_IDPos) viewTitile.getTag();
			if (!appContext.isDoPlanByIDPos()) {
				touchIDPos(_idPos.getID_CD(), true);
			}
//				enterDJPlan(_idPos);
		}
	}

	/**
	 * 创建menu TODO 停用原生菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		// if (canScanYn) {
		RFIDString = "";
		setIntent(paramIntent);
		processIntent(paramIntent);
		// }
	}
	private Handler  BDHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1){
				List<DJ_IDPos> selectedIDPos=(List<DJ_IDPos>) msg.obj;
				
				lvDJIDPosStatusAdapter = new DJIDPos_ListViewAdapter(DianjianTouchIDPos.this,
						selectedIDPos, R.layout.listitem_djidpos);
				lvDJIDPosStatus = (ListView) findViewById(R.id.dianjian_djidpos_listview_status);
				lvDJIDPosStatus.setAdapter(lvDJIDPosStatusAdapter);
				lvDJIDPosStatus.setOnItemClickListener(new myListener());
			}
		}
	};
	Thread thread;
	private void getIDPosState() {		
		
		thread=new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				selectIDPosbyCase(currCaseIndex);
			}
		});
		thread.start();
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
				// 调用振动
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(500);

				RFIDString = bytesToHexString(bytesId);
				// RFIDExpMessageString = readFromNFCTag(intent);
				touchIDPos(RFIDString, true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// writeNfcMessage(ndef);
			}
		}
	}

	/**
	 * @param ndef
	 */
	private void writeNfcMessage(Ndef ndef) {
		try {
			String pdaIMEI = AppContext.GetIMEI();
			String timeNowString = DateTimeHelper.getDateTimeNow();
			String userNowString = AppContext.getUserCD() + "&&"
					+ AppContext.getUserName();
			String dataString = pdaIMEI + "&&" + timeNowString + "&&"
					+ userNowString;
			byte[] data = dataString.getBytes("utf-8");
			ndef.connect();
			if (!ndef.isWritable())
				return;
			NdefRecord ndefRecord = new NdefRecord(data);
			NdefRecord[] records = { ndefRecord };
			NdefMessage ndefMessage = new NdefMessage(records);
			ndef.writeNdefMessage(ndefMessage);
			ndef.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private String readFromNFCTag(Intent intent) {
		String readResult;
		Parcelable[] rawArray = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
		NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
		try {
			if (mNdefRecord != null) {
				readResult = new String(mNdefRecord.getPayload(), "UTF-8");
				return readResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 触碰钮扣
	 * 
	 * @param idposCDString
	 * @param isQRScan
	 *            true为纽扣扫描，false为二维码扫描
	 */
	public void touchIDPos(String idposCDString, Boolean isQRScan) {
		if (!isFinishing()) {

			if (!canScanYn && isQRScan)
				return;

			if (isFinished)
				RFIDExpMessageString = "";
			else
				RFIDExpMessageString = getResources().getString(R.string.djidpos_message_idposisloding);

			try {
				if (AppContext.DJIDPosBuffer == null
						|| AppContext.DJIDPosBuffer.isEmpty()) {
					UIHelper.ToastMessage(getApplication(),
							R.string.idpos_noidpos);
				}
//				if (!AppContext.orderIDPosYN || lastIDPosSort == -1)
				if (!AppContext.orderIDPosYN) {
					Iterator<DJ_IDPos> iter = AppContext.DJIDPosBuffer
							.iterator();
					while (iter.hasNext()) {
						DJ_IDPos _idpos = iter.next();
						if (_idpos.getID_CD().equals(idposCDString)
								|| idposCDString.contains(_idpos.getID_CD())) {
							enterDJPlan(_idpos);
							return;
						}
					}
				} else {
					if (isFinished) {
						int No = _indexOf(AppContext.orderDJIDPosBuffer,
								idposCDString);
						DJ_IDPos _idpos = null;
						try {
							_idpos = AppContext.orderDJIDPosBuffer.get(No);
						} catch (Exception ex) {
							String meString = getString(R.string.djidpos_message_idposname, idposCDString)
									+ getString(R.string.idpos_notuse);
							UIHelper.ToastMessage(getApplication(), meString
									+ "\n" + RFIDExpMessageString, false);
						}
						if (_idpos.getSort_NR() >= lastIDPosSort) {
							enterDJPlan(_idpos);
							return;
						} else {
							if (_indexOf(AppContext.orderDJIDPosBuffer,
									lastIDPosSort) == AppContext.orderDJIDPosBuffer
										.size() - 1) {
								UIHelper.ToastMessage(getApplication(),
										R.string.djidpos_message_sortidposcompleted, false);
								return;
							} else {
								String idposName = AppContext.orderDJIDPosBuffer
										.get(_indexOf(AppContext.orderDJIDPosBuffer,
												lastIDPosSort) + 1).getPlace_TX();
								UIHelper.ToastMessage(getApplication(),
										getResources().getString(R.string.djidpos_message_wrongsortnoidpos, idposName),
										false);
								return;
							}
						}
					} else {
						UIHelper.ToastMessage(getApplication(),
								R.string.djidpos_message_sortidposmustloadcompleted, false);
						return;
					}
				}
				String meString = getString(R.string.djidpos_message_idposname, idposCDString)
						+ getString(R.string.idpos_notuse);
				UIHelper.ToastMessage(getApplication(), meString + "\n"
						+ RFIDExpMessageString, false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private int _indexOf(List<DJ_IDPos> arrayList, String CD) {
		int index = -1;
		if (arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<DJ_IDPos> iter = arrayList.iterator();
		while (iter.hasNext()) {
			++index;
			if (iter.next().getID_CD().equals(CD)) {
				return index;
			}
		}
		index = -1;
		return index;
	}
	
	private int _indexOf(List<DJ_IDPos> arrayList, int sortNo) {
		int index = -1;
		if (arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<DJ_IDPos> iter = arrayList.iterator();
		while (iter.hasNext()) {
			++index;
			if (iter.next().getSort_NR() == sortNo) {
				return index;
			}
		}
		index = -1;
		return index;
	}

	/**
	 * 进入点检计划界面
	 * 
	 * @param _idPos
	 */
	private void enterDJPlan(DJ_IDPos _idPos) {

		/* 如果是600的机型，关闭读取钮扣服务 */
		if (AppContext.getRFIDUseYN()) {
//			RFIDManager.getRFIDManager(DianjianTouchIDPos.this,
//					TOUCHIDPOS_DJIDPOS).unbindService();
			RFIDManager.getRFIDManager(DianjianTouchIDPos.this,
					TOUCHIDPOS_DJIDPOS).unRegReceiver_and_unbindRemoteService();
		}
		AppContext.setIDCD(_idPos.getID_CD());
		AppContext.setIDPosName(_idPos.getPlace_TX());
		// 设置当前钮扣信息，用于实时上传
		AppContext.setCurIDPos(_idPos);
				
		UIHelper.showDianjian(DianjianTouchIDPos.this,
				String.valueOf(current_djlineID), _idPos.getIDPos_ID(),
				_idPos.getPlace_TX());
		_idPos.setLastUser(AppContext.getUserName());
		_idPos.setLastArrivedTime_DT(DateTimeHelper.getDateTimeNow());
		saveDJIDPosTask(_idPos);
		
	}

	/**
	 * 保存到位结果
	 */
	private void saveDJIDPosTask(DJ_IDPos idpos) {
		try {
			DJ_TaskIDPosActive idPosTask = new DJ_TaskIDPosActive();
			idPosTask.setAppUser_CD(AppContext.getUserCD());
			idPosTask.setAppUserName_TX(AppContext.getUserName());
			if (AppContext.getCurrLocation() != null
					&& AppContext.getCurrLocation().getLongitude() > 0
					&& AppContext.getCurrLocation().getLatitude() > 0) {
				String gpsInfo = String.valueOf(AppContext.getCurrLocation()
						.getLongitude())
						+ "|"
						+ String.valueOf(AppContext.getCurrLocation()
								.getLatitude());
				idPosTask.setGPSInfo_TX(gpsInfo);
			} else {
				idPosTask.setGPSInfo_TX("");
			}
			idPosTask.setIDPos_ID(idpos.getIDPos_ID());
			idPosTask.setIDPosStart_TM(idpos.getLastArrivedTime_DT());
			idPosTask.setIDPosEnd_TM(idpos.getLastArrivedTime_DT());
			idPosTask.setQuery_DT(DateTimeHelper.getDateTimeNow());
			
			String shiftname = "";
			String shiftGroupName = "";
			// 巡点检计算班次，职别	
			shiftname = XSTMethodByLineTypeHelper.getInstance()
				.getShiftName(DianjianTouchIDPos.this, null);
			shiftGroupName = XSTMethodByLineTypeHelper.getInstance()
					.getShiftGroupName(DianjianTouchIDPos.this, null);
			idPosTask.setShiftName_TX(shiftname);
			idPosTask.setShiftGroupName_TX(shiftGroupName);
			
			idPosTask.setTimeCount_NR(0);
			if (!StringUtils.isEmpty(idpos.getTransInfo_TX())) {
				idPosTask.setTransInfo_TX(XSTMethodByLineTypeHelper.getInstance()
					.setTransInfo(idpos.getTransInfo_TX()));
				
				/*if (AppContext.getCurrLineType() == AppConst.LineType.XDJ.getLineType())
					idPosTask.setTransInfo_TX(idpos.getTransInfo_TX());
				else if (AppContext.getCurrLineType() == AppConst.LineType.CaseXJ.getLineType()) {
					if (!StringUtils.isEmpty(AppConst.PlanTimeIDStr)) {
						idPosTask.setTransInfo_TX(idpos.getTransInfo_TX() + "|"
								+ AppConst.PlanTimeIDStr);
					} else
						idPosTask.setTransInfo_TX(idpos.getTransInfo_TX());
				}*/
			} else {
				idPosTask.setTransInfo_TX("");
			}
			
			// 历史库到位表
			XJ_TaskIDPosHis xj_TaskIDPosHis = new XJ_TaskIDPosHis();
			xj_TaskIDPosHis.setLine_ID(String.valueOf(current_djlineID));
			xj_TaskIDPosHis.setIDPos_ID(idPosTask.getIDPos_ID());
			xj_TaskIDPosHis.setIDPosStart_TM(idPosTask.getIDPosStart_TM());
			xj_TaskIDPosHis.setIDPosDesc_TX(idpos.getPlace_TX());
			xj_TaskIDPosHis.setIDPosEnd_TM(idPosTask.getIDPosEnd_TM());
			xj_TaskIDPosHis.setQuery_DT(idPosTask.getQuery_DT());
			xj_TaskIDPosHis.setAppUser_CD(idPosTask.getAppUser_CD());
			xj_TaskIDPosHis.setAppUserName_TX(idPosTask.getAppUserName_TX());
			xj_TaskIDPosHis.setShiftGroupName_TX(idPosTask.getShiftGroupName_TX());
			xj_TaskIDPosHis.setShiftName_TX(idPosTask.getShiftName_TX());
			xj_TaskIDPosHis.setTimeCount_NR(idPosTask.getTimeCount_NR());
			xj_TaskIDPosHis.setTransInfo_TX(idPosTask.getTransInfo_TX());
			xj_TaskIDPosHis.setGPSInfo_TX(idPosTask.getGPSInfo_TX());
			
			long row = DJResultHelper.GetIntance().InsertDJIDPosResult(this, idPosTask, xj_TaskIDPosHis);
			if (row > 0) {
				DJIDPosHelper.GetIntance().updateDJIDPos(this, idpos);
				lastIDPosSort = idpos.getSort_NR();
			} else {
				UIHelper.ToastMessage(getApplication(), getString(R.string.nfc_defeated_hint));
			}
		} catch (Exception ex) {
			UIHelper.ToastMessage(getApplication(), ex.getMessage());
		}
	}

	final static Integer casehasFinished = 0;
	final static Integer casenotFinish = 1;	
	final static Integer caseAll = 2;
	final static Integer caseNotArrive = 3;
	final static Integer caseHasArrived = 4;
	Integer currCaseIndex = 2;

	/**
	 * 根据条件提取钮扣
	 */
	private void selectIDPosbyCase(Integer caseIndex) {
		List<DJ_IDPos> selectedIDPos = new ArrayList<DJ_IDPos>();
		selectedIDPos.clear();
		
		if (!AppContext.orderIDPosYN) {
			if (AppContext.DJIDPosBuffer != null
					&& AppContext.DJIDPosBuffer.size() > 0) {
				if (AppContext.DJSpecCaseFlag == 0) {
					if (caseIndex == casenotFinish) {
						for (DJ_IDPos _item : AppContext.DJIDPosBuffer) {
							if (_item.getIDPosState() == AppConst.IDPOS_STATUS_NEEDED
									|| _item.getIDPosState() == AppConst.IDPOS_STATUS_NOTFINISHED)
								selectedIDPos.add(_item);
						}
					}
					if (caseIndex == casehasFinished) {
						for (DJ_IDPos _item : AppContext.DJIDPosBuffer) {
							if (_item.getIDPosState() == AppConst.IDPOS_STATUS_FINISHED)
								selectedIDPos.add(_item);
						}
					}
					if (caseIndex == caseAll) {
						selectedIDPos = AppContext.DJIDPosBuffer;
					}
	
					Message msg = new Message();
					msg.what = 1;
					msg.obj=selectedIDPos;
					BDHandler.sendMessage(msg);
				}
			}
		} else {
			if (AppContext.orderDJIDPosBuffer != null
					&& AppContext.orderDJIDPosBuffer.size() > 0) {
				if (AppContext.DJSpecCaseFlag == 0) {
					if (caseIndex == casenotFinish) {
						for (DJ_IDPos _item : AppContext.orderDJIDPosBuffer) {
							if (_item.getIDPosState() == AppConst.IDPOS_STATUS_NEEDED
									|| _item.getIDPosState() == AppConst.IDPOS_STATUS_NOTFINISHED)
								selectedIDPos.add(_item);
						}
					}
					if (caseIndex == casehasFinished) {
						for (DJ_IDPos _item : AppContext.orderDJIDPosBuffer) {
							if (_item.getIDPosState() == AppConst.IDPOS_STATUS_FINISHED)
								selectedIDPos.add(_item);
						}
					}
					if (caseIndex == caseAll) {
						selectedIDPos = AppContext.orderDJIDPosBuffer;
					}
	
					Message msg = new Message();
					msg.what = 1;
					msg.obj=selectedIDPos;
					BDHandler.sendMessage(msg);
				}
			}
		}
	}

	/**
	 * 设置外围启停
	 */
	private void setSR() {
		if (AppContext.SRBuffer != null && AppContext.SRBuffer.size() > 0) {
			String selfTitleContent = getString(R.string.dj_sr_list);// + current_djlineName;
			List<List<String>> data = new ArrayList<List<String>>();
			for (DJ_ControlPoint _item : AppContext.SRBuffer) {
				List<String> _temList = new ArrayList<String>();
				_temList.add(String.valueOf(AppContext.SRBuffer.indexOf(_item)));// ID
				_temList.add(_item.getName_TX());// Name
				_temList.add("0");// 选中标识
				_temList.add(_item.getLastSRResult_TX());
				_temList.add(_item.getLastSRResult_TM());
				data.add(_temList);
			}
			WWSRDialog _dialog = new WWSRDialog(DianjianTouchIDPos.this,
					new SimpleMultiListViewDialogListener() {

						@Override
						public void refreshParentUI() {
							// TODO 自动生成的方法存根

						}

						@Override
						public void goBackToParentUI() {
							// TODO 自动生成的方法存根
							AppContext.DJIDPosBuffer.clear();
							AppContext.orderDJIDPosBuffer.clear();
							lvDJIDPosStatusAdapter.notifyDataSetChanged();
							loadProgressBar.setVisibility(View.VISIBLE);
							isFinished = false;
							asyncLoadIDPos();
						}

						@Override
						public void btnOK(DialogInterface dialog,
								List<List<String>> mData) {
							dialog.dismiss();
						}
					}, data, false, selfTitleContent);
			_dialog.setCancelButton(true);
			_dialog.setCancelable(true);
			_dialog.setTitle(R.string.idpos_srset_dialog_title);
			_dialog.show();
		} else {
			UIHelper.ToastMessage(DianjianTouchIDPos.this, R.string.djidpos_message_nosr);
		}
	}

	private void goBack() {
		if (isFinished) {
			if (AppContext.getRFIDUseYN()) {
//				RFIDManager.getRFIDManager(DianjianTouchIDPos.this,
//						TOUCHIDPOS_DJIDPOS).unbindService();
				RFIDManager.getRFIDManager(DianjianTouchIDPos.this,
						TOUCHIDPOS_DJIDPOS).unRegReceiver_and_unbindRemoteService();
			}
			unregisterBoradcastReceiver();
			genGPSPoentityForEnd();
			//AppContext.deleteHisData=true;
			AppManager.getAppManager().finishActivity(DianjianTouchIDPos.this);
		} else {
			UIHelper.ToastMessage(DianjianTouchIDPos.this, R.string.djidpos_message_idposloding);
		}
	}

	Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				UIHelper.ToastMessage(DianjianTouchIDPos.this, R.string.djidpos_message_dbwrong);
				AppManager.getAppManager().finishActivity(DianjianTouchIDPos.this);
			}
		};
	};
	// [start] 异步加载钮扣
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				@SuppressWarnings("unchecked")
				DJ_IDPos info = ((ArrayList<DJ_IDPos>) msg.obj).get(0);
				int progBarDoNum = msg.arg1;
				AppContext.DJIDPosBuffer.add(info);
				lvDJIDPosStatusAdapter.notifyDataSetChanged();
				loadProgressBar.setProgress(progBarDoNum);
			} else {
				if (AppContext.orderIDPosYN)
					AppContext.orderDJIDPosBuffer = AppContext.DJIDPosBuffer;
				loadProgressBar.setProgress(0);
				loadProgressBar.setVisibility(View.GONE);
			}
		};
	};

	/**
	 * 异步加载钮扣列表(逐条加载)
	 */
	private void asyncLoadIDPos() {
//		initListView();
		loadIDPosStatusThread();
	}

	private void loadIDPosStatusThread() {
		new Thread() {
			public void run() {
				try {
					// Thread.sleep(500);
					// 重新加载ID位置数据
					loadDJIDPosStatusData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void initListView() {
		AppContext.DJIDPosBuffer.clear();
		AppContext.orderDJIDPosBuffer.clear();
		loadProgressBar.setVisibility(View.VISIBLE);
		// 绑定列表
		lvDJIDPosStatusAdapter = new DJIDPos_ListViewAdapter(this,
				AppContext.DJIDPosBuffer, R.layout.listitem_djidpos);
		lvDJIDPosStatus = (ListView) findViewById(R.id.dianjian_djidpos_listview_status);
		lvDJIDPosStatus.setAdapter(lvDJIDPosStatusAdapter);

		rlidposStatus = (RelativeLayout) findViewById(R.id.ll_dianjian_touchidpos_statussummary);
		view1 = (View) findViewById(R.id.view_1);
		// idposStatusStatistic.setVisibility(View.INVISIBLE);

		iv_total = (ImageView) findViewById(R.id.dianjian_touchidpos_statistics_total_pic);
		iv_uncomplete = (ImageView) findViewById(R.id.dianjian_touchidpos_statistics_uncomplete_pic);
		iv_complete = (ImageView) findViewById(R.id.dianjian_touchidpos_statistics_complete_pic);
		viewTotal = (View) findViewById(R.id.view_total);
		viewUncomplete = (View) findViewById(R.id.view_uncomplete);
		viewComplete = (View) findViewById(R.id.view_complete);
		rlidposStatusTotal = (RelativeLayout) findViewById(R.id.rl_dianjian_touchidpos_statistics_total);
		rlidposStatusTotal.setOnClickListener(MyListener);
		rlidposStatusUncomplete = (RelativeLayout) findViewById(R.id.rl_dianjian_touchidpos_statistics_uncomplete);
		rlidposStatusUncomplete.setOnClickListener(MyListener);
		rlidposStatusComplete = (RelativeLayout) findViewById(R.id.rl_dianjian_touchidpos_statistics_complete);
		rlidposStatusComplete.setOnClickListener(MyListener);

		if (AppContext.DJSpecCaseFlag == 0) {
			rlidposStatus.setVisibility(View.VISIBLE);
			view1.setVisibility(View.VISIBLE);
		} else {
			rlidposStatus.setVisibility(View.GONE);
			view1.setVisibility(View.GONE);
		}

		lvDJIDPosStatus.setOnItemClickListener(new myListener());
		lvDJIDPosStatus
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						TextView viewTitile = (TextView) arg1
								.findViewById(R.id.djidpos_listitem_title);
						DJ_IDPos _idPos = (DJ_IDPos) viewTitile.getTag();

						UIHelper.showQueryDataIDPosHisResult(
								DianjianTouchIDPos.this,
								String.valueOf(AppContext.GetCurrLineID()),
								_idPos.getIDPos_ID(), _idPos.getPlace_TX());
						return true;
					}
				});
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = Message.obtain();
			if (intent.getAction().equals(STATE_PROCESSING)) {
				msg.what = 1;
				msg.arg1 = intent.getIntExtra("percent", 0);
				msg.obj = intent.getSerializableExtra("idposinfo");

				handler.sendMessage(msg);
			} else if (intent.getAction().equals(STATE_FINISHED)) {
				msg.what = 1;
				msg.arg1 = 0;
				msg.obj = null;

				handler.sendMessage(msg);
				isFinished = true;
			} else if (intent.getAction().equals(
					"com.xst.track.service.updataCurrentLoction")) {
				if (AppContext.getXDJRecordJWYN())
					insertGPSInfo();
			}
		}
	};

	private BDLocation currLocation = null;

	public void unregisterBoradcastReceiver() {
		try {
			if (mBroadcastReceiver != null)
				this.unregisterReceiver(mBroadcastReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception
			}
		}
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(STATE_PROCESSING);
		myIntentFilter.addAction(STATE_FINISHED);
		myIntentFilter.addAction("com.xst.track.service.updataCurrentLoction");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	// [end]
}
