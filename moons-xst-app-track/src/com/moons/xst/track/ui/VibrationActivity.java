package com.moons.xst.track.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.IImeasureService;
import android.os.Message;
import android.os.ServiceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.util.common.FileUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.R.color;
import com.moons.xst.track.adapter.VibrationPlaybackAdapter;
import com.moons.xst.track.bean.ProvisionalM;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.common.ByteUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.HexUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import android.widget.LinearLayout.LayoutParams;


public class VibrationActivity extends BaseActivity implements OnClickListener {

	private final String Activity_Tag = "VibrationActivity";
	// [start] 自定义参数
	private static int mcyl_A = 5120; // 加速度采样率
	private static int mcyl_V = 5120; // 速度采样率
	private static int mcyl_S = 5120; // 位移采用率

	private static int mcyds_A = 4096;// 加速度采样点数
	private static int mcyds_V = 1024;// 速度采样点数
	private static int mcyds_S = 1024;// 位移采样点数

	private static boolean mReadDataFlag = false; // 是否读到有效数据标示
	private static boolean mCurIsPY = false; // 当前是否读取频谱数据
	private static int mCurCyNOBT = 0; // 当前采用编号
	private static int mCurStartBT = 0; // 当前采样开始采样点数
	private static int mCurCyDSBT = 256; // 当前采样点数
	private static int mReadTotalCountBT = 0;// 当前已经读取的点数
	private static int mMaxCountPerPackage = 256; // 每次读取最大点数

	private static byte[] packStart = new byte[] { 0x33, (byte) 0xcc };
	private static byte[] packEnd = new byte[] { 0x66, (byte) 0x99 };
	private static int packageMinLen = 16;
	private BluetoothAdapter _bluetooth = null;
	BluetoothDevice _device = null; // 蓝牙设备
	static BluetoothSocket _socket = null; // 蓝牙通信socket

	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; // SPP服务UUID号
	private InputStream is; // 输入流，用来接收蓝牙数据
	boolean bRun = true;
	boolean bThread = false;
	private String mReceiveDataStr = ""; // 显示用数据缓存

	String mbtAddress = "";
	String mbtPassword = "0000";

	String mvibPackage = "";
	String mbandWidth = "0xc0000"; // 带宽默认为Highpass12KHz
	boolean running = true;
	private static int mCLModuleType = 0;// 0-表示内置，1-表示外置
	private int mDebugFlag = 0;
	private String mParms = "";
	private static int mZDType = 1;
	private float mCurValue = 0;
	private Boolean isRtnValue;

	public static final int REQCODE_VIBRATIONSET = 1;// 测振设置
	private float mMinitSYYmax = (float) 0.01;
	private float mMinitPYYmax = (float) 0.01;

	private float mSYMax = Float.MIN_VALUE;
	private float mSYMin = Float.MAX_VALUE;
	private float mPYMax = Float.MIN_VALUE;
	private float mPYMin = Float.MAX_VALUE;

	/**
	 * u32 samplePoints; 采样点数，保留变量 u32 maxAnaFreq; 最大分析频率 u32 fftLines; 频谱线数 点数为
	 * 800 * 2.56=2048 u32 windowType; 窗类型，保留，默认2 u32 trigerType; 触发类型，保留，默认0
	 * u32 signalType; 信号类型，1-加速度 2-速度 4-位移 8-冲击 u32 averNum; 平均次数 u32 averWrap;
	 * 重叠率，0-不重叠，1-25% 2-50% 3-75% u32 mode; 模式，保留，默认0 u32 coeff;
	 * 传感器系数，保留，赋值x1000
	 */
	private static int[] mCZPars = new int[10];// {0,5000,800,2,0,1,1,0,0,5000};
	private static int[] mCZParsConfig = new int[10];
	
	private float[] mSYDataAll;
	private float[] mSYData;// = new float[2048];
	private float[] mPYData;// = new float[1024];
	private float[] mZBData = new float[13];

	// [end]

	// [start] 控件
	private RelativeLayout moreInfo, showmoreinfoBtn;
	private RelativeLayout setupBtn, operationBtn, saveBtn;
	private TextView vibration_memu_operation, vibration_memu_operation_desc;
	private ImageView vibration_more;
	private ImageButton returnButton;

	TextView txtValueTitle = null;
	TextView txtValue = null;
	TextView txtFZ = null;
	TextView txtFFZ = null;
	TextView txtPJZ = null;
	TextView txtPJFZ = null;
	TextView txtFGFZ = null;
	TextView txtYXZ = null;
	TextView txtBXZB = null;
	TextView txtMCZB = null;
	TextView txtFZZB = null;
	TextView txtYDZB = null;
	TextView txtWDZB = null;
	TextView txtDDZB = null;
	TextView tv_playback;

	private TempMeasureDBHelper tempMeasure;

	private LineChart mSYChart = null;
	private LineChart mPYChart = null;

	private LineData mSYLineData = null;
	private LineData mPYLineData = null;

	private LoadingDialog loading;
	private CheckBox cbA;
	private CheckBox cbB;
	private CheckBox cbC;
	private CheckBox cbD;
	private float AxHighLigh;
	private float BxHighLigh;
	private float xHighLigh;
	private XAxis xAxis;
	private float redVal;
	private float ziVal;
	private float redVal1;
	private float ziVal1;
	private float xHighLight1;
	private float cxHighLigh;
	private float dxHighLigh;

	// [end]

	// [start] 初始化自定义方法
	private void initHeadView() {
		txtValueTitle = (TextView) findViewById(R.id.valueTitleTxt);

		txtValue = (TextView) findViewById(R.id.valueTxt);
		txtValue.setTextColor(Color.BLUE);
		txtValueUnit = (TextView) findViewById(R.id.valueTxtUnit);
		
		tv_playback=(TextView) findViewById(R.id.tv_playback);
		if(!isRtnValue){//临时测量才能回放
			tv_playback.setVisibility(View.VISIBLE);
		}
		tv_playback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(isPlayback){
					return;
				}else{
					isPlayback=true;
				}
				new Thread(){
					public void run() {
						List<ProvisionalM> list=TempMeasureDBHelper.GetIntance().loadVibrationRecord(VibrationActivity.this);
						try {
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Message msg = Message.obtain();
						msg.what=1;
						msg.obj=list;
						handler.sendMessage(msg);
					}
				}.start();
			}
		});

		returnButton = (ImageButton) findViewById(R.id.vibration_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (saveBtn.isEnabled()) {
					if (vibration_memu_operation.getText().toString()
							.contains(getString(R.string.vibration_stop)))
						stop();
					closePower();
					AppManager.getAppManager().finishActivity(
							VibrationActivity.this);
				}
			}
		});
	}

	private void initChart() {
		mSYChart = (LineChart) findViewById(R.id.sychart);
		mSYLineData = InitSYLineData(Color.RED); // 初始化数据
		initSYChart(Color.WHITE); // 初始化表样式
		RefreshSYChart();

		mPYChart = (LineChart) findViewById(R.id.pychart);
		mPYLineData = InitPYLineData(Color.RED);
		initPYChart(Color.WHITE);
		RefreshPYChart();
	}

	private void initMoreInfoView() {
		vibration_more = (ImageView) findViewById(R.id.vibration_more);
		moreInfo = (RelativeLayout) findViewById(R.id.ll_vibration_menu_moreinfo);
		moreInfo.setVisibility(View.GONE);

		txtFZ = (TextView) findViewById(R.id.fzTxt);
		txtFFZ = (TextView) findViewById(R.id.ffzTxt);
		txtPJZ = (TextView) findViewById(R.id.pjzTxt);
		txtPJFZ = (TextView) findViewById(R.id.pjfzTxt);
		txtFGFZ = (TextView) findViewById(R.id.fgfzTxt);
		txtYXZ = (TextView) findViewById(R.id.yxzTxt);
		txtBXZB = (TextView) findViewById(R.id.bxzbTxt);
		txtMCZB = (TextView) findViewById(R.id.mczbTxt);
		txtFZZB = (TextView) findViewById(R.id.fzzbTxt);
		txtYDZB = (TextView) findViewById(R.id.ydzbTxt);
		txtWDZB = (TextView) findViewById(R.id.wdzbTxt);
		txtDDZB = (TextView) findViewById(R.id.ddzbTxt);

		showmoreinfoBtn = (RelativeLayout) findViewById(R.id.ll_vibration_memu_more);
		showmoreinfoBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (moreInfo.getVisibility() == View.GONE) {
					moreInfo.setVisibility(View.VISIBLE);
					vibration_more.setImageResource(R.drawable.icon_up);
				} else {
					moreInfo.setVisibility(View.GONE);
					vibration_more.setImageResource(R.drawable.icon_down);
				}
			}
		});
	}

	private void initTipView() {
		tempMeasure = new TempMeasureDBHelper();
		sb = new StringBuilder();
		for (int i = 0; i < mZBData.length; i++) {
			sb.append(mZBData[i] + ";");
		}

		saveBtn = (RelativeLayout) findViewById(R.id.ll_vibration_memu_ok);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goBack();
			}

		});

		vibration_memu_operation = (TextView) findViewById(R.id.vibration_memu_operation);
		vibration_memu_operation_desc = (TextView) findViewById(R.id.vibration_memu_operation_desc);
		operationBtn = (RelativeLayout) findViewById(R.id.ll_vibration_memu_operation);
		operationBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				String oldTextString = (String) vibration_memu_operation
						.getText();

				if (oldTextString.contains(getString(R.string.vibration_stop))) {
					setenable(true);
					//SendCloseBtComand();
					stop();
				} else {
					setenable(false);
					if (mDebugFlag == 0 && mCLModuleType == 1){
						vibration_memu_operation.setText(R.string.vibration_stop);
						vibration_memu_operation_desc.setText(R.string.vibration_stop_desc);

						startforouter();
					}
					else
						start();
				}
			}

		});
		setupBtn = (RelativeLayout) findViewById(R.id.ll_vibration_memu_setup);
		setupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (vibration_memu_operation.getText().toString()
						.contains(getString(R.string.vibration_stop)))
					stop();
				showCLSet(VibrationActivity.this, mZDType, mbtAddress,
						mbtPassword);
			}
		});
	}

	// [end]

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vibration);
		/* 设置在此界面不待机 */
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数
		ReadConfig();
		isRtnValue = getIntent().getBooleanExtra("isRtnValue", false);
		try {
			mDebugFlag = getIntent().getExtras().getInt("debugFlag");
		} catch (Exception e) {
		}
		try {
			mCLModuleType = getIntent().getExtras().getInt("clModuleType");
		} catch (Exception e) {
		}
		try {
			mbtAddress = getIntent().getStringExtra("btAddress");
		} catch (Exception e) {
		}
		try {
			mbtPassword = getIntent().getStringExtra("btPassword");
		} catch (Exception e) {
		}
		try {
			if (getIntent().getExtras().getString("parms").length() > 0) {
				mParms = getIntent().getExtras().getString("parms");
				String[] tempStr = mParms.split(",", 11);
				mvibPackage = tempStr[0].toString();
				mCZPars[0] = StringUtils.isEmpty(tempStr[1]) ? mCZParsConfig[0]
						: StringUtils.toInt(tempStr[1]);
				mCZPars[1] = StringUtils.isEmpty(tempStr[2]) ? mCZParsConfig[1]
						: StringUtils.toInt(tempStr[2]);
				mCZPars[2] = StringUtils.isEmpty(tempStr[3]) ? mCZParsConfig[2]
						: StringUtils.toInt(tempStr[3]);
				mCZPars[3] = StringUtils.isEmpty(tempStr[4]) ? mCZParsConfig[3]
						: StringUtils.toInt(tempStr[4]);
				mCZPars[4] = StringUtils.isEmpty(tempStr[5]) ? mCZParsConfig[4]
						: StringUtils.toInt(tempStr[5]);
				mCZPars[5] = StringUtils.isEmpty(tempStr[6]) ? mCZParsConfig[5]
						: StringUtils.toInt(tempStr[6]);
				mCZPars[6] = StringUtils.isEmpty(tempStr[7]) ? mCZParsConfig[6]
						: StringUtils.toInt(tempStr[7]);
				mCZPars[7] = StringUtils.isEmpty(tempStr[8]) ? mCZParsConfig[7]
						: StringUtils.toInt(tempStr[8]);
				mCZPars[8] = StringUtils.isEmpty(tempStr[9]) ? mCZParsConfig[8]
						: StringUtils.toInt(tempStr[9]);
				mCZPars[9] = StringUtils.isEmpty(tempStr[10]) ? mCZParsConfig[9]
						: StringUtils.toInt(tempStr[10]);
			} else
				mCZPars = mCZParsConfig;
		} catch (Exception e) {
		}

		mZDType = mCZPars[5];
		if(mZDType==1){
			mcyds_A=mCZPars[0];
		}else if(mZDType==2){
			mcyds_V=mCZPars[0];
		}else if(mZDType==4){
			mcyds_S=mCZPars[0];
		}
		
		/* 波形数据长度 fftline * 2.56 * 平均次数 */
		int len = (int) (mCZPars[2] * 2.56);
		mSYData = new float[len];
		mPYData = new float[mCZPars[2] + 1];
		
		// 冲击加上带宽
		if (mZDType == 8 && checkOSVersion()) {
			int x = Integer.parseInt(mbandWidth.substring(2),16);
			mZDType = mZDType | x;
			mCZPars[5] = mZDType;
			// 原始数据长度为20000 / 最大频率 * 采样点数 * 平均次数(目前平均次数为1，没有乘)		
			int dklen = 20000 / mCZPars[1] * mCZPars[0] * 1; // mCZPars[6]
			mSYDataAll = new float[len + dklen];
		} else {
			mSYDataAll = new float[len];
		}						
		
		
		/* 波形数据长度 fftline * 2.56 * 平均次数 
		int len = (int) (mCZPars[2] * 2.56);
		// mSYXChartCount = len;
		mSYData = new float[len];
		mPYData = new float[mCZPars[2] + 1];*/

		/* 初始化Head */
		initHeadView();
		/* 初始化特征参数 */
		initMoreInfoView();
		/* 初始化图表 */
		initChart();
		/* 初始化Tip */
		initTipView();
		initView();

		RefreshValueTitle();

		if (mCLModuleType == 0) {
			operationBtn.setEnabled(false);
			saveBtn.setEnabled(false);
			setupBtn.setEnabled(false);
			openPower();
		}

		if (vibration_memu_operation.getText().toString()
				.contains(getString(R.string.vibration_start))) {
			setenable(false);
		}
		//开启后台线程发送指令
		new Thread(sendCommondRunnable).start();
		
	}
	private boolean isToSendCommond=true;
	Runnable sendCommondRunnable=new Runnable() {
		
		@Override
		public void run() {
			
			while (isToSendCommond) {
				
				SendCZWaveValueComand(mCurCyNOBT, mCurStartBT, mCurCyDSBT,
						mCurIsPY);
			}
			
		}
	};
	
	
	
	private boolean checkOSVersion() {
		try {
			String versionToDate = AppContext.getOSVersion().substring(0,4) 
					+ "-" + AppContext.getOSVersion().substring(4,6) 
					+ "-" + AppContext.getOSVersion().substring(6,8);
			if (DateTimeHelper.StringToDate(versionToDate, "yyyy-MM-dd")
				.compareTo(DateTimeHelper.StringToDate(AppConst.CONST_OS_VERSION, "yyyy-MM-dd")) >= 0)
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private void setenable(boolean b) {
		addSYYValue.setEnabled(b);
		reduceSYYValue.setEnabled(b);
		addPYYValue.setEnabled(b);
		reducePYYValue.setEnabled(b);
	}

	private void initView() {
		cbA = (CheckBox) findViewById(R.id.tool_cbA);
		cbB = (CheckBox) findViewById(R.id.tool_cbB);
		cbC = (CheckBox) findViewById(R.id.tool_cbC);
		cbD = (CheckBox) findViewById(R.id.tool_cbD);
		addSYYValue = (ImageButton) findViewById(R.id.btn_add_sy_value);
		reduceSYYValue = (ImageButton) findViewById(R.id.btn_reduce_sy_value);
		addPYYValue = (ImageButton) findViewById(R.id.btn_add_py_value);
		reducePYYValue = (ImageButton) findViewById(R.id.btn_reduce_py_value);
		addSYYValue.setOnClickListener(this);
		reduceSYYValue.setOnClickListener(this);
		addPYYValue.setOnClickListener(this);
		reducePYYValue.setOnClickListener(this);
		cbA.setOnClickListener(this);
		cbB.setOnClickListener(this);
		cbC.setOnClickListener(this);
		cbD.setOnClickListener(this);
		redLineXValue = (TextView) findViewById(R.id.tv_red_line_xvalue);
		redLineYValue = (TextView) findViewById(R.id.tv_red_line_yvalue);
		redLineYValueUnit = (TextView) findViewById(R.id.tv_red_line_yvalue_unit);
		ziLineXValue = (TextView) findViewById(R.id.tv_zi_line_xvalue);
		ziLineYValue = (TextView) findViewById(R.id.tv_zi_line_yvalue);
		ziLineYValueUnit = (TextView) findViewById(R.id.tv_zi_line_yvalue_unit);
		twoLineDifferentXValue = (TextView) findViewById(R.id.tv_two_line_different_xvalue);
		twoLineDifferentYValue = (TextView) findViewById(R.id.tv_two_line_different_yvalue);
		twoLineDifferentYValueUnit = (TextView) findViewById(R.id.tv_two_line_different_yvalue_unit);
		redLineXValue1 = (TextView) findViewById(R.id.tv_red_line_xvalue1);
		redLineYValue1 = (TextView) findViewById(R.id.tv_red_line_yvalue1);
		redLineYValue1Unit = (TextView) findViewById(R.id.tv_red_line_yvalue1_unit);
		ziLineXValue1 = (TextView) findViewById(R.id.tv_zi_line_xvalue1);
		ziLineYValue1 = (TextView) findViewById(R.id.tv_zi_line_yvalue1);
		ziLineYValue1Unit = (TextView) findViewById(R.id.tv_zi_line_yvalue1_unit);
		twoLineDifferentXValue1 = (TextView) findViewById(R.id.tv_two_line_different_xvalue1);
		twoLineDifferentYValue1 = (TextView) findViewById(R.id.tv_two_line_different_yvalue1);
		twoLineDifferentYValue1Unit = (TextView) findViewById(R.id.tv_two_line_different_yvalue1_unit);
	}

	/**
	 * 刷新测量类型
	 */
	private void RefreshValueTitle() {
		String valueTitle = getString(R.string.vibration_type_acceleration);
		if (mZDType == 1) {
			valueTitle = getString(R.string.vibration_type_accelerationvalue);
		} else if (mZDType == 2) {
			valueTitle = getString(R.string.vibration_type_tempovalue);
		} else if (mZDType == 4) {
			valueTitle = getString(R.string.vibration_type_displacementvalue);
		} else {
			valueTitle = getString(R.string.vibration_type_impactvalue);
		}
		txtValueTitle.setText(valueTitle);

		// TODO根据显示的信息来配置sy的单位值
		if (getString(R.string.vibration_type_accelerationvalue).equals(
				valueTitle)) {
			txtValueUnit.setText("m/s²");
			redLineYValueUnit.setText("m/s²");
			ziLineYValueUnit.setText("m/s²");
			twoLineDifferentYValueUnit.setText("m/s²");
			redLineYValue1Unit.setText("m/s²");
			ziLineYValue1Unit.setText("m/s²");
			twoLineDifferentYValue1Unit.setText("m/s²");
		} else if (getString(R.string.vibration_type_tempovalue).equals(
				valueTitle)) {
			txtValueUnit.setText("mm/s");
			redLineYValueUnit.setText("mm/s");
			ziLineYValueUnit.setText("mm/s");
			twoLineDifferentYValueUnit.setText("mm/s");
			redLineYValue1Unit.setText("mm/s");
			ziLineYValue1Unit.setText("mm/s");
			twoLineDifferentYValue1Unit.setText("mm/s");
		} else if (getString(R.string.vibration_type_displacementvalue).equals(
				valueTitle)) {
			txtValueUnit.setText("mm");
			redLineYValueUnit.setText("mm");
			ziLineYValueUnit.setText("mm");
			twoLineDifferentYValueUnit.setText("mm");
			redLineYValue1Unit.setText("mm");
			ziLineYValue1Unit.setText("mm");
			twoLineDifferentYValue1Unit.setText("mm");
		} else if (getString(R.string.vibration_type_impactvalue).equals(
				valueTitle)) {
			txtValueUnit.setText("m/s²");
			redLineYValueUnit.setText("m/s²");
			ziLineYValueUnit.setText("m/s²");
			twoLineDifferentYValueUnit.setText("m/s²");
			redLineYValue1Unit.setText("m/s²");
			ziLineYValue1Unit.setText("m/s²");
			twoLineDifferentYValue1Unit.setText("m/s²");
		}
	}

	/**
	 * 从配置文件读取参数配置信息
	 */
	private void ReadConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.VibrationConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				// 获取river中name属性值
				if (element.getAttribute("Name").equals("vibrationPackage")) {
					mvibPackage = element.getAttribute("Using").toString();
				}
				if (element.getAttribute("Name").equals("samplePoints")) {
					mCZParsConfig[0] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("maxAnaFreq")) {
					mCZParsConfig[1] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("fftLines")) {
					mCZParsConfig[2] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("windowType")) {
					mCZParsConfig[3] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("trigerType")) {
					mCZParsConfig[4] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("signalType")) {
					if (element.getAttribute("Using").equals("A"))
						mCZParsConfig[5] = 1;
					else if (element.getAttribute("Using").equals("V"))
						mCZParsConfig[5] = 2;
					else if (element.getAttribute("Using").equals("S"))
						mCZParsConfig[5] = 4;
					else
						mCZParsConfig[5] = 8;
				} else if (element.getAttribute("Name").equals("averNum")) {
					mCZParsConfig[6] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("averWrap")) {
					mCZParsConfig[7] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("mode")) {
					mCZParsConfig[8] = StringUtils.toInt(element
							.getAttribute("Using"));
				} else if (element.getAttribute("Name").equals("coeff")) {
					mCZParsConfig[9] = StringUtils.toInt(element
							.getAttribute("Using"));
				}
				
				if (element.getAttribute("Name").equals("bandWidth")) {
					mbandWidth = element.getAttribute("Using").toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动测量
	 */
	private void start() {
		initData();

		if (mDebugFlag != 1 && mCLModuleType == 1) {
			return;
		} else {
			setPars();
			localReceiveStart();

			vibration_memu_operation.setText(R.string.vibration_stop);
			vibration_memu_operation_desc.setText(R.string.vibration_stop_desc);
		}
	}

	private void startforouter() {
		OpenBTDevice();

		initData();

		connectBTDevice();
		// 外接设备准备定时读取数据
		int cyds = getCurCYDSBT();
		int mCurCyDSBT = mMaxCountPerPackage;

		if (cyds < mCurCyDSBT)
			mCurCyDSBT = cyds;
		mCurCyNOBT = 0;
		mCurStartBT = 0;
		mReadTotalCountBT = 0;
		mReadDataFlag = false;
		mCurIsPY = false;
		// 启动定时器发送读取波形数据请求
		reSendBThandler.postDelayed(reSendBTrunnable, 1000); // 开始Timer
	}
	/**
	 * 停止测量
	 */
	private void stop() {

		vibration_memu_operation.setText(R.string.vibration_start);
		vibration_memu_operation_desc.setText(R.string.vibration_start_desc);

		if (mDebugFlag != 1 && mCLModuleType == 1) // 外置
		{
			reSendBThandler.removeCallbacks(reSendBTrunnable); // 停止Timer

			diconnectBTDevice();
		} else {

			controlVibration(0);
			running = false;
			// CloseRecSocket();
		}
	}

	Handler mRefreshBtnEnableHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				// 刷新数据
				operationBtn.setEnabled(true);
				saveBtn.setEnabled(true);
				setupBtn.setEnabled(true);
				start();
			} else {
				UIHelper.ToastMessage(getApplication(),
						((Exception) (msg.obj)).getMessage());
			}
			// super.handleMessage(msg);
		}
	};

	/**
	 * 关闭蓝牙连接
	 */
	private void diconnectBTDevice() {
		// 关闭连接socket
		bRun = false;
		bThread = false;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {

		}
		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {
				// TODO: handle exception
			}

			is = null;
		}
		if (_socket != null) {
			try {
				_socket.close();
			} catch (Exception e) {
				// TODO: handle exception
			}

			_socket = null;
		}
	}

	/**
	 * 打开蓝牙
	 */
	private void OpenBTDevice() {
		// _bluetooth = BluetoothAdapter.getDefaultAdapter(); //获取本地蓝牙适配器，即蓝牙设备

		SearchBT();
		// 如果打开本地蓝牙设备不成功，提示信息，结束程序
		if (_bluetooth == null) {
			UIHelper.ToastMessage(this,
					R.string.temperature_start_bluetooth_hint);
			finish();
			return;
		}

		// 设置设备可以被搜索
		if (_bluetooth.isEnabled() == false) {
			_bluetooth.enable();
		}

	}

	/**
	 * 通过蓝牙连接外置测量设备
	 */
	private void connectBTDevice() {

		if (_bluetooth.isEnabled() == false) { // 如果蓝牙服务不可用则提示
			UIHelper.ToastMessage(this,
					R.string.temperature_underway_start_bluetooth);
			return;
		}
		if (mbtAddress.equals("")) {
			UIHelper.ToastMessage(this, R.string.temperature_no_bluetooth);
			return;
		}

		// MAC地址
		String address = mbtAddress;
		// 得到蓝牙设备句柄
		_device = _bluetooth.getRemoteDevice(address);

		if (!AppConst.isEffectDevice(VibrationActivity.this, _device.getName())) {
			UIHelper.ToastMessage(this,
					R.string.temperature_bluetooth_connect_hint2);
			return;
		}
		// 用服务号得到socket
		try {
			_socket = _device.createRfcommSocketToServiceRecord(UUID
					.fromString(MY_UUID));
		} catch (IOException e) {
			UIHelper.ToastMessage(this,
					R.string.temperature_bluetooth_connect_hint2);
		}
		// 连接socket

		try {
			_socket.connect();
			UIHelper.ToastMessage(
					this,
					getString(R.string.temperature_bluetooth_connect_hint1,
							_device.getName()));

		} catch (IOException e) {
			try {
				UIHelper.ToastMessage(this,
						R.string.temperature_bluetooth_connect_hint2);
				_socket.close();
				_socket = null;
				return;
			} catch (IOException ee) {
				UIHelper.ToastMessage(this,
						R.string.temperature_bluetooth_connect_hint3);
				_socket = null;
				return;
			}

		}

		// 打开接收线程
		try {
			is = _socket.getInputStream(); // 得到蓝牙数据输入流
		} catch (IOException e) {
			UIHelper.ToastMessage(this, R.string.temperature_reception_defeated);
			return;
		}
		if (bThread == false) {
			// 接收数据线程
			new Thread() {

				public void run() {
					int num = 0;
					byte[] buffer = new byte[1024];

					int i = 0;

					bRun = true;
					String smsg = "";
					// 接收线程
					while (bRun == true) {
						try {

							while (bRun == true) {

								num = is.read(buffer); // 读入数据
								byte[] receveData = new byte[num];
								for (i = 0; i < num; i++) {

									receveData[i] = buffer[i];
								}
							//	System.out.println("ReceveData"+Arrays.toString(receveData));
								
								String s = HexUtils.encodeHexStr(receveData);
								smsg += s; // 写入接收缓存
								if (is.available() == 0)
									break; // 短时间没有数据才跳出进行显示
							}
							// 收到结尾为6699就处理数据
							if (smsg.length() > 4
									&& smsg.substring(smsg.length() - 4,
											smsg.length()).equals("6699")) {
								if (CheckPackage(smsg)) {
									mReceiveDataStr = smsg;
									// 发送显示消息，进行显示刷新
									refreshwjclhandler
											.sendMessage(refreshwjclhandler
													.obtainMessage());
								}

								smsg = "";
							}
						} catch (IOException e) {
						}
					}
				}
			}.start();
			bThread = true;
		} else {
			bRun = true;
		}

		vibration_memu_operation.setText(R.string.vibration_stop);
		vibration_memu_operation_desc.setText(R.string.vibration_stop_desc);

	}

	private boolean CheckPackage(String revStr) {
		// 接收包长度最小16字节（无数据情况下）
		if (revStr.length() < packageMinLen * 2)
			return false;
		String dataLenHex = revStr.substring(22, 24) + revStr.substring(20, 22);
		int packageLen = packageMinLen + Integer.parseInt(dataLenHex, 16);
		if (revStr.length() < packageLen * 2)
			return false;
		if (!revStr.substring(0, 4).equals("33cc")
				|| !revStr.substring(packageLen * 2 - 4, packageLen * 2)
						.equals("6699"))
			return false;
		return true;
	}

	// 外接设备消息处理队列
	Handler refreshwjclhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mReadDataFlag = true;
			String valueHexStr = "";
			String dataLenHex = "";
			String gNM = "";
			int dataCount = 0;
			int index = 0;
			int dataType = 0x00;
			// 33cc07080900000002020200fd00528c6699
			// 根据mReceiveDataStr解析出温度值进行刷新

			String recString = mReceiveDataStr;
			try {
				gNM = recString.substring(16, 20);
				if (!gNM.equals("0303")) // 重发上一个读取数据包
				{
					mReceiveDataStr = "";
					mReadDataFlag = false;
					
					SendCZWaveValueComand(mCurCyNOBT, mCurStartBT, mCurCyDSBT,
							mCurIsPY);

					String temp1 = mCurentSendDataString;
					String temp2 = mCurrentSendDataCRCString;

				} else {
					dataLenHex = recString.substring(22, 24)
							+ recString.substring(20, 22);

					dataCount = Integer.parseInt(dataLenHex, 16);

					// 数据从13字节开始
					index = 2 * 12;
					// 类型1字节，没用
					valueHexStr = mReceiveDataStr.substring(index, index + 2);
					dataType = Integer.parseInt(valueHexStr, 16);
					index = index + 2;
					// 测量值 2字节
					valueHexStr = mReceiveDataStr.substring(index + 2,
							index + 4)
							+ mReceiveDataStr.substring(index, index + 2);
					int tempValue = Integer.parseInt(valueHexStr, 16);
					mZBData[12] = getCurCZValueBT(tempValue);

					index = index + 4;
					// 备用2字节
					index = index + 4;
					// 采样编号2字节
					valueHexStr = mReceiveDataStr.substring(index + 2,
							index + 4)
							+ mReceiveDataStr.substring(index, index + 2);
					tempValue = Integer.parseInt(valueHexStr, 16);
					index = index + 4;
					// 读取开始采样点数2字节
					valueHexStr = mReceiveDataStr.substring(index + 2,
							index + 4)
							+ mReceiveDataStr.substring(index, index + 2);
					int cyStart = Integer.parseInt(valueHexStr, 16);
					index = index + 4;
					// 读取采样点数2字节
					valueHexStr = mReceiveDataStr.substring(index + 2,
							index + 4)
							+ mReceiveDataStr.substring(index, index + 2);
					int cyCount = Integer.parseInt(valueHexStr, 16);
					index = index + 4;

					for (int i = 0; i < cyCount; i++) { // 每个采用值占用4个字节

						valueHexStr = mReceiveDataStr.substring(index + 2,
								index + 4)
								+ mReceiveDataStr.substring(index, index + 2);
						byte bstest1 = (byte) Integer
								.parseInt(mReceiveDataStr.substring(index + 2,
										index + 4), 16);
						byte bstest2 = (byte) Integer
								.parseInt(mReceiveDataStr.substring(index,
										index + 2), 16);

						tempValue = HexUtils.convertTwoBytesToInt1(bstest2,
								bstest1);
						if (mCurIsPY == true) // 频域数据
						{
							mPYData[cyStart + i] = getCurWaveDataBT(tempValue);
						} else { // 时域数据
							mSYData[cyStart + i] = getCurWaveDataBT(tempValue);
						}

						index = index + 4;
					}

					mReadTotalCountBT = cyStart + cyCount;
					mReceiveDataStr = "";
					int cyds = getCurCYDSBT();
					int nextcyCount = mMaxCountPerPackage;
					if (mReadTotalCountBT < cyds) // 一次采样数据未读完，继续读取
					{
						if (cyds - mReadTotalCountBT < nextcyCount)
							nextcyCount = cyds - mReadTotalCountBT;
						mReadDataFlag = false;
						SendCZWaveValueComand(mCurCyNOBT, mReadTotalCountBT,
								nextcyCount, mCurIsPY);

					} else {
						if (mCurIsPY == false) // 时域波形数据读取完后继续读频域数据
						{
							mHandler.sendMessage(new Message());// 向Handler发送消息，
							mCurIsPY = true;
							mReadTotalCountBT = 0;
							SendCZWaveValueComand(mCurCyNOBT,
									mReadTotalCountBT, nextcyCount, mCurIsPY);
						} else { // 频域数据读取完成后，刷新图表，等待定时器继续下一次采样
							mHandler.sendMessage(new Message());// 向Handler发送消息，

							// 刷新数据
							// RefreshSYChart();
							// RefreshZBData();
							/*
							 * mCurCyNOBT = mCurCyNOBT + 1;
							 * 
							 * int cyds1 = getCurCYDSBT(); int mCurCyDSBT =
							 * mMaxCountPerPackage;
							 * 
							 * if (cyds1 < mCurCyDSBT) mCurCyDSBT = cyds1;
							 * 
							 * mCurStartBT = 0; mReadTotalCountBT = 0;
							 * mReadDataFlag = false;
							 * SendCZWaveValueComand(mCurCyNOBT,0,mCurCyDSBT);
							 */
						}

					}
				}

			} catch (Exception e) {
				String ss = recString;
			}
		}

	};
	private static Handler reSendBThandler = new Handler();
	private static Runnable reSendBTrunnable = new Runnable() {
		public void run() {
			// if (mReadDataFlag == false)
			{
				if (_socket != null) {
					mCurCyNOBT = mCurCyNOBT + 1;

					int cyds = getCurCYDSBT();
					int mCurCyDSBT = mMaxCountPerPackage;

					if (cyds < mCurCyDSBT)
						mCurCyDSBT = cyds;

					mCurStartBT = 0;
					mReadTotalCountBT = 0;
					mReadDataFlag = false;
					// SendCZWaveValueComand(mCurCyNOBT,0,mCurCyDSBT);
					mCurIsPY = false;
					SendCZWaveValueComand(mCurCyNOBT, mCurStartBT, mCurCyDSBT,
							mCurIsPY);
					mReadDataFlag = false;
				}
			}

			reSendBThandler.postDelayed(this, 3000); // 没有接收到有效数据，超时重发上一次读取波形数据请求

		}
	};

	private static float getCurWaveDataBT(int tempValue) {
		float rst = tempValue;
		if (mZDType == 1)
			rst = (float) (((float) tempValue) * 0.025431315104);
		else if (mZDType == 2)
			rst = (float) (((float) tempValue) * 0.025431315104);
		else if (mZDType == 4)
			rst = (float) (((float) tempValue) * 0.0006326197787064677);
		return rst;
	}

	private static float getCurCZValueBT(int tempValue) {
		float rst = tempValue;
		if (mZDType == 1)
			rst = ((float) tempValue) / 10;
		else if (mZDType == 2)
			rst = ((float) tempValue) / 10;
		else if (mZDType == 4)
			rst = ((float) tempValue) / 1000;
		return rst;
	}

	private static byte getCurCZTypeBT(Boolean isPY) {
		byte rst = 0x00;
		if (isPY == true) // 频谱数据
		{
			if (mZDType == 1)
				rst = 0x04;
			else if (mZDType == 2)
				rst = 0x05;
			else if (mZDType == 4)
				rst = 0x06;

		} else // 波形数据
		{
			if (mZDType == 1)
				rst = 0x01;
			else if (mZDType == 2)
				rst = 0x02;
			else if (mZDType == 4)
				rst = 0x03;
		}
		return rst;
	}

	private static int getCurCYLBT() {
		int rst = mcyl_A;
		if (mZDType == 1)
			rst = mcyl_A;
		else if (mZDType == 2)
			rst = mcyl_V;
		else if (mZDType == 4)
			rst = mcyl_S;
		return rst;
	}

	private static int getCurCYDSBT() {
		int rst = mcyds_A;
		if (mZDType == 1)
			rst = mcyds_A;
		else if (mZDType == 2)
			rst = mcyds_V;
		else if (mZDType == 4)
			rst = mcyds_S;

		if (mCurIsPY) // 如果是波形数据，总采样点数折半
			rst = rst / 2;
		return rst;
	}

	private static boolean SendCZValueComand() {
		byte[] comandData = getCZValueComandData();
		return SendComand(comandData);

	}

	private static boolean SendCZWaveValueComand(int cyNO, int startIndex,
			int count, Boolean isPY) {
		byte type = getCurCZTypeBT(isPY);
		int cyl = getCurCYLBT();
		int cyds = getCurCYDSBT();
		mCurStartBT = startIndex;
		mCurCyDSBT = count;
		byte[] comandData = getCZWaveComandData(type, cyl, cyds, cyNO,
				startIndex, count);

		return SendComand(comandData);

	}

	private static boolean SendCloseBtComand() {
		byte[] comandData = getCloseBtComandData();
		return SendComand(comandData);

	}

	private static boolean SendStatusBtComand() {
		byte[] comandData = getStatusBtComandData();
		return SendComand(comandData);

	}

	private static String mCurentSendDataString = "";
	private static String mCurrentSendDataCRCString = "";

	private static boolean SendComand(byte[] data) {

		byte[] crcRst = HexUtils.CRC16_Check(data, data.length);
		
		//System.out.println("MyData"+Arrays.toString(data));
		mCurentSendDataString = HexUtils.encodeHexStr(data);
		mCurrentSendDataCRCString = HexUtils.encodeHexStr(crcRst);
		try {
			OutputStream os = _socket.getOutputStream();
			
			byte[] all = new byte[packStart.length + data.length + crcRst.length + packEnd.length];
			System.arraycopy(packStart, 0, all, 0, packStart.length);
			System.arraycopy(data, 0, all, packStart.length, data.length);
			System.arraycopy(crcRst, 0, all, packStart.length + data.length, crcRst.length);
			System.arraycopy(packEnd, 0, all, packStart.length + data.length + crcRst.length, packEnd.length);
			
			os.write(all);
			
			/*os.write(packStart);
			os.write(data);
			os.write(crcRst);
			os.write(packEnd);*/
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} // 蓝牙连接输出流

		return true;

	}

	private static byte[] getCZValueComandData() {
		byte[] rst = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
				0x00, 0x0c, 0x00, (byte) (mcyl_A % 256), (byte) (mcyl_A / 256),
				(byte) (mcyds_A % 256), (byte) (mcyds_A / 256),
				(byte) (mcyl_V % 256), (byte) (mcyl_V / 256),
				(byte) (mcyds_V % 256), (byte) (mcyds_V / 256),
				(byte) (mcyl_S % 256), (byte) (mcyl_S / 256),
				(byte) (mcyds_S % 256), (byte) (mcyds_S / 256) };
		return rst;
	}

	private static byte[] getCZWaveComandData(byte type, int cyl, int cyds,
			int cyNO, int startIndex, int count) {
		byte[] rst = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
				0x00, 0x0b, 0x00, type, (byte) (cyl % 256), (byte) (cyl / 256),
				(byte) (cyds % 256), (byte) (cyds / 256), (byte) (cyNO % 256),
				(byte) (cyNO / 256), (byte) (startIndex % 256),
				(byte) (startIndex / 256), (byte) (count % 256),
				(byte) (count / 256) };
		return rst;
	}

	private static byte[] getCloseBtComandData() {
		byte[] rst = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04,
				0x00, 0x00, 0x00 };
		return rst;
	}

	private static byte[] getStatusBtComandData() {
		byte[] rst = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
				0x00, 0x00, 0x00 };
		return rst;
	}

	/**
	 * 打开电源
	 */
	private int openPower() {
		loading = new LoadingDialog(this);
		loading.setLoadText(getString(R.string.vibration_start_process));
		loading.setCancelable(false);
		loading.show();
		new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				if (mDebugFlag == 0) {
					if (mCLModuleType == 1) // 外置
					{
						// OpenBTDevice();
					} else // 内置
					{
						try {
							IImeasureService imeasureService = IImeasureService.Stub
									.asInterface(ServiceManager
											.getService("imeasure"));
							int rst = imeasureService.openVibration();
							msg.what = 1;
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = -1;
							msg.obj = e;
						}
					}
				} else {
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
					}
				}
				mRefreshBtnEnableHandler.sendMessage(msg);// 向Handler发送消息，
			}
		}.start();
		return 0;
	}

	/**
	 * 关闭电源
	 */
	private int closePower() {
		int rst = -1;
		if (mDebugFlag == 0) {
			if (mCLModuleType == 1) // 外置
			{
				rst = 0;
			} else {
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));

					rst = imeasureService.closeVibration();

				} catch (Exception e) {
					rst = -1;
				}
			}
		} else
			rst = 0;
		return rst;
	}

	/**
	 * 启动和停止，不关电源
	 * 
	 * @param startflag
	 */
	private int controlVibration(int startflag) {
		int rst = -1;
		if (mDebugFlag == 0) {
			if (mCLModuleType == 1) // 外置
			{
				rst = 0;
			} else {
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));

					rst = imeasureService.controlVibration(startflag);

				} catch (Exception e) {
					rst = -1;
				}

			}

		} else {
			rst = 0;
		}
		return rst;
	}

	/**
	 * 设置参数
	 */
	private int setPars() {
		int rst = -1;
		if (mDebugFlag == 0) {
			if (mCLModuleType == 1) // 外置
			{
				rst = 0;
			} else {
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));
					rst = imeasureService.configVibration(mCZPars, 1);

				} catch (Exception e) {
					rst = -1;
				}
			}
		} else {
			rst = 0;
		}
		return rst;
	}

	/**
	 * 读取测量数据
	 */
	private int ReadData() {
		int rst = -1;
		if (mDebugFlag == 0) {
			if (mCLModuleType == 1) // 外置
			{
				rst = 0;
			}
			{
				try {
					IImeasureService imeasureService = IImeasureService.Stub
							.asInterface(ServiceManager.getService("imeasure"));

					rst = imeasureService.getVibrationWaveFloat(mSYDataAll,
							mZDType);
					for (int i = 0; i < mSYData.length; i++) {
						mSYData[i] = mSYDataAll[i];
					}
					
					rst = imeasureService
							.getVibrationFftFloat(mPYData, mZDType);
					rst = imeasureService
							.getVibrationValFloat(mZBData, mZDType);

				} catch (Exception e) {
					rst = -1;
				}
			}
		} else {

			DecimalFormat df = new DecimalFormat("#0.00000");
			for (int i = 0; i < mSYData.length; i++) {
				String cl = df.format(50 * (Math.random()));
				mSYData[i] = Float.parseFloat(cl);
			}

			for (int i = 0; i < mPYData.length; i++) {
				String cl = df.format(50 * (Math.random()));
				mPYData[i] = Float.parseFloat(cl);
			}
			df = new DecimalFormat("#0.00");
			for (int i = 0; i < mZBData.length; i++) {
				String cl = df.format(50 * (Math.random()));
				mZBData[i] = Float.parseFloat(cl);
			}
			rst = 0;
		}
		return rst;
	}

	public void ReceiveData() {

		try {

			// 读数据
			ReadData();

			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_stop))) {
				// 再次启动
				controlVibration(1);

			}

			mHandler.sendMessage(new Message());// 向Handler发送消息，

		} catch (Exception e) {
			// TODO: handle exception
		}
	};

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 刷新数据
			RefreshSYChart();
			RefreshPYChart();
			RefreshZBData();
			super.handleMessage(msg);
		}
	};

	private void CloseRecSocket() {
		try {

			if (skReceiver != null) {
				System.out.println("Close skReceiver");
				skReceiver.close();
				skReceiver = null;
			}
			if (skServer != null) {
				System.out.println("Close skServer");
				skServer.close();
				skServer = null;
			}

		} catch (Exception e) {

		}

	}

	LocalServerSocket skServer = null;
	LocalSocket skReceiver = null;
	private LineDataSet lineDataSet;
	private TextView redLineXValue;
	private TextView redLineYValue;
	private TextView ziLineXValue;
	private TextView ziLineYValue;
	private TextView twoLineDifferentXValue;
	private TextView twoLineDifferentYValue;
	private TextView redLineXValue1;
	private TextView redLineYValue1;
	private TextView ziLineXValue1;
	private TextView ziLineYValue1;
	private TextView twoLineDifferentXValue1;
	private TextView twoLineDifferentYValue1;
	private float xSYAxis_Spacing;
	private float xPYAxis_Spacing;
	private TextView txtValueUnit;
	private TextView redLineYValueUnit;
	private TextView ziLineYValueUnit;
	private TextView twoLineDifferentYValueUnit;
	private TextView redLineYValue1Unit;
	private TextView ziLineYValue1Unit;
	private TextView twoLineDifferentYValue1Unit;
	private ImageButton addSYYValue;
	private ImageButton reduceSYYValue;
	private ImageButton addPYYValue;
	private ImageButton reducePYYValue;

	private void localReceiveStart() {
		// 接收线程
		Thread localReceive = new Thread() {
			@Override
			public void run() {
				if (mDebugFlag == 0) {
					try {

						System.out.println("create imeasure.localsocket");
						skServer = new LocalServerSocket("imeasure.localsocket");

						running = true;

						controlVibration(1);
						while (running) {
							System.out.println("Wait Data...");
							skReceiver = skServer.accept();

							System.out.println("Accept Start");
							if (skReceiver != null) {
								BufferedReader rd = new BufferedReader(
										new InputStreamReader(
												skReceiver.getInputStream(),
												"UTF-8"));
								String str = rd.readLine().toUpperCase();
								rd.close();
								System.out.println("Accept Data:" + str);
								if (str.equals("::DATA_READY::")) {
									if (vibration_memu_operation
											.getText()
											.toString()
											.contains(
													getString(R.string.vibration_stop))) {

										System.out.println("Start ReadData...");
										// 开始读取数据
										ReceiveData();

										System.out.println("End ReadData");
									} else {
										break;
									}
								} else {
									break;
								}
							} else {

								break;
							}
						}
						CloseRecSocket();
					} catch (IOException e) {

						System.out.println("socket Error");
					}
				} else {
					while (true) {

						if (vibration_memu_operation.getText().toString()
								.contains(getString(R.string.vibration_stop))) {
							// 开始读取数据
							ReceiveData();
						} else {
							break;
						}
						try {
							sleep(500);
						} catch (InterruptedException e) {

						}
					}

				}
			}

		};
		localReceive.start();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mCurValue = 0;

		List<Entry> yValues = mSYLineData.getDataSetByIndex(0).getYVals();
		for (int i = 0; i < yValues.size(); i++) {
			yValues.get(i).setVal(0);
		}

		List<Entry> yValues1 = mPYLineData.getDataSetByIndex(0).getYVals();
		for (int i = 0; i < yValues1.size(); i++) {
			yValues1.get(i).setVal(0);

		}

		mSYMax = Float.MIN_VALUE;
		mSYMin = Float.MAX_VALUE;
		mPYMax = Float.MIN_VALUE;
		mPYMin = Float.MAX_VALUE;
		// 重新恢复坐标显示
		mSYChart.getAxisLeft().setAxisMaxValue(mMinitSYYmax);
		mSYChart.getAxisLeft().setAxisMinValue(0 - mMinitSYYmax);

		mPYChart.getAxisLeft().setAxisMaxValue(mMinitSYYmax);
		mPYChart.getAxisLeft().setAxisMinValue(0 - mMinitSYYmax);
	}

	/**
	 * 获取测量值
	 */
	private float getCLData() {

		float rst = mZBData[12];
		/*
		 * if(mZDType == 1) { rst = mZBData[0]; } else if(mZDType == 2) { rst =
		 * mZBData[5]; } else if(mZDType == 4) { rst = mZBData[1]; } else
		 * if(mZDType == 8) { rst = mZBData[1]; }
		 */
		return rst;
	}

	/**
	 * 重新刷新坐标值
	 */
	private static void ResetYaxMaxValueAndMinValue(LineChart chart,
			float maxValue, float minValue, float initYmax) {
		float axMax = chart.getAxisLeft().getAxisMaxValue();
		float axMin = chart.getAxisLeft().getAxisMinValue();
		int bs = (int) (maxValue % initYmax == 0 ? maxValue / initYmax
				: maxValue / initYmax + 1);
		chart.getAxisLeft().setAxisMaxValue(bs * initYmax);

		bs = (int) (minValue % initYmax == 0 ? minValue / initYmax : minValue
				/ initYmax - 1);
		chart.getAxisLeft().setAxisMinValue(bs * initYmax);
	}

	/***
	 * 放大缩小Y轴
	 * 
	 * @param chart
	 * @param isZoom
	 */
	private void ZommAxixValue(LineChart chart, boolean isZoom) {
		float axMax = chart.getAxisLeft().getAxisMaxValue();
		float axMin = chart.getAxisLeft().getAxisMinValue();
		if (isZoom) {
			chart.getAxisLeft().setAxisMaxValue(axMax * 2);
			chart.getAxisLeft().setAxisMinValue(axMin * 2);
		} else {
			chart.getAxisLeft().setAxisMaxValue(axMax / 2);
			chart.getAxisLeft().setAxisMinValue(axMin / 2);
		}

	}

	/**
	 * 刷新图
	 */
	private void RefreshSYChart() {
		if (mSYLineData != null) {
			List<Entry> yValues = mSYLineData.getDataSetByIndex(0).getYVals();
			int size = mSYData.length;
			if (size > yValues.size())
				size = yValues.size();

			mSYMax = 0;
			mSYMin = 0;
			for (int i = 0; i < size; i++) {
				yValues.get(i + 1).setVal(mSYData[i]);
				if (mSYMax < mSYData[i]) {
					mSYMax = mSYData[i];

				}
				if (mSYMin > mSYData[i]) {
					mSYMin = mSYData[i];

				}
			}

			ResetYaxMaxValueAndMinValue(mSYChart, mSYMax, mSYMin, mMinitSYYmax);

		}
		mSYChart.setData(mSYLineData); // 设置数据
		mSYChart.invalidate();
	}

	/**
	 * 刷新图
	 */
	private void RefreshPYChart() {
		if (mPYLineData != null) {
			List<Entry> yValues = mPYLineData.getDataSetByIndex(0).getYVals();
			int size = mPYData.length;
			if (size > yValues.size())
				size = yValues.size();
			mPYMax = 0;
			mPYMin = 0;
			for (int i = 0; i < size; i++) {
				yValues.get(i).setVal(mPYData[i]);
				if (mPYMax < mPYData[i]) {
					mPYMax = mPYData[i];

				}
				if (mPYMin > mPYData[i]) {
					mPYMin = mPYData[i];

				}
			}
			ResetYaxMaxValueAndMinValue(mPYChart, mPYMax, mPYMin, mMinitPYYmax);

		}

		mPYChart.setData(mPYLineData); // 设置数据
		mPYChart.invalidate();
	}

	/**
	 * 刷新各指标值
	 */
	private void RefreshZBData() {
		mCurValue = getCLData();
		DecimalFormat curValuedf = new DecimalFormat("#0.0000");
		DecimalFormat df = new DecimalFormat("#0.00");

		txtValue.setText(curValuedf.format(mCurValue));
		txtFZ.setText(df.format(mZBData[0]));
		txtFFZ.setText(df.format(mZBData[1]));
		txtPJZ.setText(df.format(mZBData[2]));
		txtPJFZ.setText(df.format(mZBData[3]));
		txtFGFZ.setText(df.format(mZBData[4]));
		txtYXZ.setText(df.format(mZBData[5]));
		txtBXZB.setText(df.format(mZBData[6]));
		txtMCZB.setText(df.format(mZBData[7]));
		txtFZZB.setText(df.format(mZBData[8]));
		txtYDZB.setText(df.format(mZBData[9]));
		txtWDZB.setText(df.format(mZBData[10]));
		txtDDZB.setText(df.format(mZBData[11]));
	}

	/**
	 * 设置图表显示样式
	 */
	private void initSYChart(int color) {

		LineChart lineChart = mSYChart;
		lineChart
				.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
					@Override
					public void onValueSelected(Entry arg0, int arg1,
							Highlight arg2) {
						DecimalFormat df = new DecimalFormat("#0.0000");

						mSYChart.getXAxis().removeAllLimitLines();
						xHighLigh = (arg2.getXIndex());
						// Toast.makeText(VibrationActivity2.this, ""+xHighLigh,
						// 0).show();
						if (cbA.isChecked()) {
							AxHighLigh = xHighLigh;
							addLine(AxHighLigh, BxHighLigh, mSYChart);
							redLineXValue.setText("" + AxHighLigh
									* xSYAxis_Spacing);
							redVal = arg0.getVal();
							redLineYValue.setText(String.valueOf(df
									.format(redVal)));

						} else if (cbB.isChecked()) {
							BxHighLigh = xHighLigh;
							addLine(AxHighLigh, BxHighLigh, mSYChart);
							ziLineXValue.setText("" + BxHighLigh
									* xSYAxis_Spacing);
							ziVal = arg0.getVal();
							ziLineYValue.setText(String.valueOf(df
									.format(ziVal)));
						}

						twoLineDifferentXValue.setText(""
								+ Math.abs(AxHighLigh - BxHighLigh)
								* xSYAxis_Spacing);
						twoLineDifferentYValue.setText(""
								+ df.format(Math.abs(redVal - ziVal)));

					}

					@Override
					public void onNothingSelected() {

					}
				});
		lineChart.setDrawBorders(false); // 是否在折线图上添加边框

		// 隐藏左边坐标轴横网格线
		// lineChart.getAxisLeft().setDrawGridLines(false);
		// 隐藏右边坐标轴横网格线
		lineChart.getAxisRight().setDrawGridLines(false);
		// 隐藏X轴竖网格线
		// lineChart.getXAxis().setDrawGridLines(false);

		// 设置在Y轴上是否是从0开始显示
		lineChart.getAxisLeft().setStartAtZero(false);
		// 是否在Y轴显示数据，就是曲线上的数据
		// lineChart.setDrawYValues(true);

		// 设置Y轴坐标最小为多少
		lineChart.getAxisLeft().setAxisMaxValue(mMinitSYYmax);
		lineChart.getAxisLeft().setAxisMinValue(0 - mMinitSYYmax);
		// 第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
		// lineChart.getAxisLeft().setLabelCount(5);
		// lineChart.getLegend().setEnabled(true);
		// 重新设置Y轴坐标最大为多少，自动调整
		// lineChart.resetAxisMaxValue();
		// lineChart.resetAxisMinValue();

		// 参数如果为true Y轴坐标只显示最大值和最小值
		// lineChart.getAxisLeft().setShowOnlyMinMax(true);

		// lineChart setUnit("°C");
		lineChart.getAxisRight().setEnabled(false);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		// no description text
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart
				.setNoDataTextDescription("You need to provide data for the chart.");

		// enable / disable grid background
		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		lineChart.setGridBackgroundColor(color & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
		lineChart.setTouchEnabled(true); // 设置是否可以触摸
		// lineChart.setDragEnabled(true);// 是否可以拖拽
		lineChart.setScaleEnabled(true);// 是否可以缩放
		lineChart.setPinchZoom(true);
		lineChart.setBackgroundColor(color);// 设置背景
		// get the legend (only possible after setting data)
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
		// mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(color);// 颜色
		// mLegend.setTypeface(mTf);// 字体
		// 是否可以缩放 仅y轴
		lineChart.setScaleYEnabled(false);
		// 是否显示X坐标轴上的刻度，默认是true
		lineChart.getXAxis().setDrawLabels(true);
		lineChart.getAxisLeft().setAxisLineColor(Color.BLACK);
		lineChart.getAxisLeft().setGridColor(Color.rgb(192, 192, 192));
		xAxis = lineChart.getXAxis();
		xAxis.setAxisLineColor(Color.BLACK);
		xAxis.setEnabled(true); // 是否显示X坐标轴 及 对应的刻度竖线，默认是true
		xAxis.setDrawAxisLine(true); // 是否绘制坐标轴的线，即含有坐标的那条线，默认是true
		xAxis.setTextColor(Color.BLACK);
		xAxis.setTextSize(8f); // X轴上的刻度的字的大小
		xAxis.setGridColor(Color.rgb(192, 192, 192)); // X轴上的刻度竖线的颜色
		// xAxis.setGridLineWidth(1); //X轴上的刻度竖线的宽 float类型
		xAxis.enableGridDashedLine(5, 3, 0); // 虚线表示X轴上的刻度竖线(float lineLength,
												// float spaceLength, float
												// phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标

		xAxis.resetLabelsToSkip(); // 将自动计算坐标相隔多少
		xAxis.setAvoidFirstLastClipping(true);

		xAxis.setTypeface(Typeface.DEFAULT_BOLD);
		// xAxis.setSpaceBetweenLabels(4);
		// lineChart.zoom(0.5f,1.0f,0f,0f);

	}

	/**
	 * 初始化数据
	 * 
	 * @param color
	 * @return
	 */
	private LineData InitSYLineData(int color) {
		xSYAxis_Spacing = ((float) ((float) 1000 / (mCZPars[1] * 2.56)));
		ArrayList<String> xValues = new ArrayList<String>();

		for (int i = 0; i <= mSYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			xValues.add("" + (int) (i * xSYAxis_Spacing));
		}
		// y轴的数据
		ArrayList<Entry> yValues = new ArrayList<Entry>();

		for (int i = 0; i <= mSYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			yValues.add(new Entry(0, i));
		}

		// lineDataSet = new LineDataSet(yValues, "时域图" /*显示在比例图上*/);
		lineDataSet = new LineDataSet(yValues, null /* 显示在比例图上 */);

		// 用y轴的集合来设置参数
		lineDataSet.setLineWidth(1.75f); // 线宽
		lineDataSet.setCircleSize(0f);// 显示的圆形大小
		lineDataSet.setColor(color);// 显示颜色
		lineDataSet.setCircleColor(color);// 圆形的颜色
		lineDataSet.setValueTextColor(Color.TRANSPARENT);
		lineDataSet.setHighLightColor(Color.TRANSPARENT); // 高亮的线的颜色
		ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
		lineDataSets.add(lineDataSet); // add the datasets
		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);
		return lineData;
	}

	/**
	 * 设置图表显示样式
	 */
	private void initPYChart(int color) {

		LineChart lineChart = mPYChart;

		lineChart.setDrawBorders(false); // 是否在折线图上添加边框
		lineChart
				.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

					@Override
					public void onValueSelected(Entry arg0, int arg1,
							Highlight arg2) {
						DecimalFormat df = new DecimalFormat("#0.0000");
						mPYChart.getXAxis().removeAllLimitLines();
						xHighLight1 = arg2.getXIndex();
						if (cbC.isChecked()) {
							cxHighLigh = xHighLight1;
							if (dxHighLigh == 0)
								dxHighLigh = 0;
							addLine(cxHighLigh, dxHighLigh, mPYChart);
							redLineXValue1.setText(""
									+ df.format(cxHighLigh * xPYAxis_Spacing));
							redVal1 = arg0.getVal();
							redLineYValue1.setText("" + df.format(redVal1));
						} else if (cbD.isChecked()) {
							dxHighLigh = xHighLight1;
							if (cxHighLigh == 0)
								cxHighLigh = 0;
							addLine(cxHighLigh, dxHighLigh, mPYChart);
							ziLineXValue1.setText("" + dxHighLigh
									* xPYAxis_Spacing);
							ziVal1 = arg0.getVal();
							ziLineYValue1.setText("" + df.format(ziVal1));
						}

						twoLineDifferentXValue1.setText(""
								+ Math.abs(cxHighLigh - dxHighLigh)
								* xPYAxis_Spacing);
						twoLineDifferentYValue1.setText(""
								+ df.format(Math.abs(redVal - ziVal)));
					}

					@Override
					public void onNothingSelected() {

					}
				});
		// 隐藏左边坐标轴横网格线
		// lineChart.getAxisLeft().setDrawGridLines(false);
		// 隐藏右边坐标轴横网格线
		lineChart.getAxisRight().setDrawGridLines(false);
		// 隐藏X轴竖网格线
		// lineChart.getXAxis().setDrawGridLines(false);

		// 设置在Y轴上是否是从0开始显示
		// lineChart.setStartAtZero(true);
		// 是否在Y轴显示数据，就是曲线上的数据
		// lineChart.setDrawYValues(true);

		// 设置Y轴坐标最小为多少
		lineChart.getAxisLeft().setAxisMaxValue(mMinitPYYmax);
		lineChart.getAxisLeft().setAxisMinValue(0 - mMinitPYYmax);
		// 第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
		// lineChart.getAxisLeft().setLabelCount(5);
		// lineChart.getLegend().setEnabled(true);
		// 重新设置Y轴坐标最大为多少，自动调整
		// lineChart.getAxisLeft().resetAxisMaxValue();
		// lineChart.getAxisLeft().resetAxisMaxValue();
		// 参数如果为true Y轴坐标只显示最大值和最小值
		// lineChart.getAxisLeft().setShowOnlyMinMax(true);

		// lineChart setUnit("°C");
		lineChart.getAxisRight().setEnabled(false);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart
				.setNoDataTextDescription("You need to provide data for the chart.");

		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		// lineChart.setGridBackgroundColor(color & 0x70FFFFFF); //
		// 表格的的颜色，在这里是是给颜色设置一个透明度
		lineChart.setGridBackgroundColor(Color.rgb(245, 245, 245));
		lineChart.setTouchEnabled(true); // 设置是否可以触摸
		lineChart.setDragEnabled(false);// 是否可以拖拽
		lineChart.setScaleEnabled(true);// 是否可以缩放
		lineChart.setPinchZoom(true);//

		lineChart.setBackgroundColor(color);// 设置背景
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
		// mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(color);// 颜色
		// mLegend.setTypeface(mTf);// 字体

		// 是否可以缩放 仅y轴
		lineChart.setScaleYEnabled(false);
		// 是否显示X坐标轴上的刻度，默认是true
		lineChart.getXAxis().setDrawLabels(true);
		lineChart.getAxisLeft().setAxisLineColor(Color.BLACK);
		lineChart.getAxisLeft().setGridColor(Color.rgb(192, 192, 192));
		xAxis = lineChart.getXAxis();
		xAxis.setGridColor(Color.rgb(192, 192, 192)); // X轴上的刻度竖线的颜色
		xAxis.setAxisLineColor(Color.BLACK);
		xAxis.setEnabled(true); // 是否显示X坐标轴 及 对应的刻度竖线，默认是true
		xAxis.setDrawAxisLine(true); // 是否绘制坐标轴的线，即含有坐标的那条线，默认是true
		xAxis.setTextColor(Color.BLACK);
		xAxis.setTextSize(8f); // X轴上的刻度的字的大小
		// xAxis.setGridLineWidth(1); //X轴上的刻度竖线的宽 float类型
		xAxis.enableGridDashedLine(5, 3, 0); // 虚线表示X轴上的刻度竖线(float lineLength,
												// float spaceLength, float
												// phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
		xAxis.resetLabelsToSkip(); // 将自动计算坐标相隔多少
		xAxis.setAvoidFirstLastClipping(true);
		xAxis.setTypeface(Typeface.DEFAULT_BOLD);
		// xAxis.setSpaceBetweenLabels(4);
	}

	/**
	 * 初始化数据
	 * 
	 * @param color
	 * @return
	 */
	private LineData InitPYLineData(int color) {
		xPYAxis_Spacing = ((float) mCZPars[1] / mCZPars[2]);
		ArrayList<String> xValues = new ArrayList<String>();
		for (int i = 0; i < mPYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			xValues.add("" + (int) (i * xPYAxis_Spacing));
		}

		// y轴的数据
		ArrayList<Entry> yValues = new ArrayList<Entry>();

		for (int i = 0; i < mPYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			yValues.add(new Entry(0, i));
		}

		// create a dataset and give it a type
		// y轴的数据集合
		// LineDataSet lineDataSet = new LineDataSet(yValues, "频域图"
		// /*显示在比例图上*/);
		LineDataSet lineDataSet = new LineDataSet(yValues, "" /* 显示在比例图上 */);
		// mLineDataSet.setFillAlpha(110);
		// mLineDataSet.setFillColor(Color.RED);

		// 用y轴的集合来设置参数
		lineDataSet.setLineWidth(1.75f); // 线宽
		lineDataSet.setCircleSize(0f);// 显示的圆形大小
		lineDataSet.setColor(color);// 显示颜色
		lineDataSet.setCircleColor(color);// 圆形的颜色
		lineDataSet.setValueTextColor(Color.TRANSPARENT);// 设置折线点上面的文字描述的颜色
		lineDataSet.setHighLightColor(Color.TRANSPARENT); // 高亮的线的颜色

		ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
		lineDataSets.add(lineDataSet); // add the datasets

		// create a data object with the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
	}

	/**
	 * 点击返回
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (saveBtn.isEnabled()) {
				if (vibration_memu_operation.getText().toString()
						.contains(getString(R.string.vibration_stop)))
					stop();
				closePower();
				AppManager.getAppManager().finishActivity(
						VibrationActivity.this);
				return true;
			} else {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 转至测振设置
	 * 
	 * @param fsl
	 */
	private void showCLSet(Context context, int zdType, String btaddress,
			String btpwd) {
		Intent intent = new Intent(context, VibrationSetting.class);
		// intent.putExtra("zdType", zdType);
		intent.putExtra("vibPackage", mvibPackage);
		intent.putExtra("czPars", mCZPars);
		intent.putExtra("btAddress", btaddress);
		intent.putExtra("btPassword", btpwd);
		intent.putExtra("bandWidth", mbandWidth);
		startActivityForResult(intent, REQCODE_VIBRATIONSET);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQCODE_VIBRATIONSET) {
			if (resultCode == Activity.RESULT_OK) {
				mvibPackage = intent.getExtras().getString("vibpackage");
				mCZPars = intent.getExtras().getIntArray("czPars");
				mbandWidth = intent.getExtras().getString("bandwidth");
				mZDType = mCZPars[5];
				if(mZDType==1){
					mcyds_A=mCZPars[0];
				}else if(mZDType==2){
					mcyds_V=mCZPars[0];
				}else if(mZDType==4){
					mcyds_S=mCZPars[0];
				}
				
				/* 波形数据长度 fftline * 2.56 * 平均次数 */
				int len = (int) (mCZPars[2] * 2.56);
				mSYData = new float[len];
				mPYData = new float[mCZPars[2] + 1];
				
				// 冲击加上带宽
				if (mZDType == 8 && checkOSVersion()) {
					int x = Integer.parseInt(mbandWidth.substring(2),16);
					mZDType = mZDType | x;
					mCZPars[5] = mZDType;
						
					// 原始数据长度为20000 / 最大频率 * 采样点数 * 平均次数(目前平均次数为1，没有乘)		
					int dklen = 20000 / mCZPars[1] * mCZPars[0] * 1; // mCZPars[6]
					mSYDataAll = new float[len + dklen];
				} else {
					mSYDataAll = new float[len];
				}

				if (AppContext.getMeasureType().equals(
						AppConst.MeasureType_Outer)) {
					mbtAddress = intent.getExtras().getString("btAddress");
					mbtPassword = intent.getExtras().getString("btPwd");
				}
				RefreshValueTitle();
				initChart();
				cleanTxtParms();
			}
		}
	}

	/**
	 * 点击设置返回后，将图表的相关显示信息清除
	 * */
	private void cleanTxtParms() {
		xAxis.removeAllLimitLines();
		redLineXValue.setText("");
		redLineYValue.setText("");
		ziLineXValue.setText("");
		ziLineYValue.setText("");
		twoLineDifferentXValue.setText("");
		twoLineDifferentYValue.setText("");
		redLineXValue1.setText("");
		redLineYValue1.setText("");
		ziLineXValue1.setText("");
		ziLineYValue1.setText("");
		twoLineDifferentXValue1.setText("");
		twoLineDifferentYValue1.setText("");

	}

	StringBuilder sb;

	private void goBack() {
		if (!isRtnValue) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(R.layout.editbox_layout, null);
			final EditText edit = (EditText) view.findViewById(R.id.editText);// 获得输入框对象
			edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
			edit.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					String editable = edit.getText().toString();
					String str = StringUtils.stringFilter(editable.toString());
					if (!editable.equals(str)) {
						edit.setText(str);
						// 设置新的光标所在位置
						edit.setSelection(str.length());
					}
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			new com.moons.xst.track.widget.AlertDialog(this)
					.builder()
					.setTitle(getString(R.string.vibration_save_name))
					.setEditView(view, InputType.TYPE_CLASS_TEXT,
							getString(R.string.vibration_save_name))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {

									if (edit.getText().toString().length() > 0) {
										try {
											ProvisionalM provisional = new ProvisionalM();
											provisional.setDeviceName(edit
													.getText()
													+ "_"
													+ mvibPackage);
											
											sb = new StringBuilder();
											for (int i = 0; i < mZBData.length; i++) {
												sb.append(mZBData[i] + ";");
											}
											// 测量值取特征值的最后一个
											try {
												provisional.setDJResult(sb.toString().split(";")[12].toString());
											} catch (Exception e) {
												provisional.setDJResult("0.0");
											}
											provisional.setFeatureValue_TX(sb
													.toString());
											provisional.setDJTime(DateTimeHelper.DateToString(
													DateTimeHelper
															.GetDateTimeNow(),
													"yyyyMMddHHmmss"));
											if (mSYDataAll != null) {
												provisional.setData8K(ByteUtils
														.floatToByte(mSYDataAll));
											} else {
												provisional.setData8K(null);
											}
											provisional.setRatio(1);
											provisional.setRatio1(1);
											provisional
													.setRate((int) (mCZPars[1] * 2.56));
											provisional
													.setDataLen(mSYData.length);
											provisional
													.setKDBZUNIT(txtValueUnit
															.getText()
															.toString());
											provisional
													.setZHENDONGTYPE(mvibPackage);
											provisional
													.setVibParam_TX(mvibPackage
															+ ";" + mCZPars[3]
															+ ";0");
											
											provisional.setFFTData_TX(ByteUtils
													.floatToByte(mPYData));
											boolean isExit = tempMeasure
													.InsertCeZhenBaseInfo(
															VibrationActivity.this,
															provisional);
											if (isExit) {
												if (vibration_memu_operation
														.getText()
														.toString()
														.contains(
																getString(R.string.vibration_stop))) {
													stop();
												}
												closePower();
												AppManager
														.getAppManager()
														.finishActivity(
																VibrationActivity.this);
											} else {
												UIHelper.ToastMessage(
														VibrationActivity.this,
														R.string.initcp_message_savedefeated);
											}

										} catch (IOException e) {
											e.printStackTrace();
										}
									} else {
										UIHelper.ToastMessage(
												VibrationActivity.this,
												getString(R.string.vibration_import_name));
									}
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
								}
							}).show();
		} else {
			//SendCloseBtComand();
			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_stop)))
				stop();
			closePower();
			
			sb = new StringBuilder();
			for (int i = 0; i < mZBData.length; i++) {
				sb.append(mZBData[i] + ";");
			}
			
			DecimalFormat df = new DecimalFormat("#0.0000");
			Intent intent = new Intent();
			try {
				intent.putExtra("rsts", df.format(Float.valueOf(sb.toString().split(";")[12])));
			} catch (Exception e) {
				intent.putExtra("rsts", df.format(0.0));
			}
			intent.putExtra("datalen", mSYData.length);
			intent.putExtra("rate", (int) (mCZPars[1] * 2.56));			
			intent.putExtra("featurevalue", sb.toString());
			try {
				intent.putExtra("sydata", StringUtils.gZip(ByteUtils.floatToByte(mSYDataAll)));
				intent.putExtra("pydata", ByteUtils.floatToByte(mPYData));				
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			setResult(RESULT_OK, intent);

			AppManager.getAppManager().finishActivity(VibrationActivity.this);
		}
	}

	public void SearchBT() {
		_bluetooth = BluetoothAdapter.getDefaultAdapter();

		if (!_bluetooth.isEnabled()) {
			_bluetooth.enable();
		}

		// 关闭再进行的服务查找
		if (_bluetooth.isDiscovering()) {
			_bluetooth.cancelDiscovery();
		}
		// 并重新开始
		_bluetooth.startDiscovery();
		_bluetooth.cancelDiscovery();

	}

	/**
	 * 点击事件处理
	 * 
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_sy_value:
			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_start))) {
				ZommAxixValue(mSYChart, true);
				mSYChart.setData(mSYLineData); // 设置数据
				mSYChart.invalidate();
			}
			break;
		case R.id.btn_reduce_sy_value:
			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_start))) {
				ZommAxixValue(mSYChart, false);
				mSYChart.setData(mSYLineData);
				mSYChart.invalidate();
			}
			break;
		case R.id.btn_add_py_value:
			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_start))) {
				ZommAxixValue(mPYChart, true);
				mPYChart.setData(mPYLineData);
				mPYChart.invalidate();
			}
			break;
		case R.id.btn_reduce_py_value:
			if (vibration_memu_operation.getText().toString()
					.contains(getString(R.string.vibration_start))) {
				ZommAxixValue(mPYChart, false);
				mPYChart.setData(mPYLineData);
				mPYChart.invalidate();
			}
			break;
		case R.id.tool_cbA:
			if (cbA.isChecked()) {
				cbB.setChecked(false);
			}
			break;
		case R.id.tool_cbB:
			if (cbA.isChecked()) {
				cbA.setChecked(false);
			}
			break;

		case R.id.tool_cbC:
			if (cbD.isChecked()) {
				cbD.setChecked(false);
			}
			break;
		case R.id.tool_cbD:
			if (cbC.isChecked()) {
				cbC.setChecked(false);
			}
			break;
		}
		mSYChart.setDragEnabled((!cbA.isChecked()) && (!cbB.isChecked()));
		mPYChart.setDragEnabled((!cbC.isChecked()) && (!cbD.isChecked()));
	}

	/**
	 * 添加图表上面的竖线
	 * */
	private void addLine(Float AxHighLight, Float BxHighLight,
			LineChart lineChart) {
		LimitLine lla = new LimitLine(AxHighLight, "");
		lla.setLineWidth(1);
		lla.setLineColor(Color.rgb(0, 0, 255));
		lineChart.getXAxis().addLimitLine(lla);

		LimitLine llb = new LimitLine(BxHighLight, "");
		llb.setLineWidth(1);
		llb.setLineColor(getResources().getColor(color.purple));
		lineChart.getXAxis().addLimitLine(llb);
	}

	/**
	 * OK键点击确认
	 * 
	 * @param event
	 * @return
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_UP) {
			goBack();
		}
		return super.dispatchKeyEvent(event);
	}
	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				showVibrationPlaybackDialog((List<ProvisionalM>) msg.obj);
				isPlayback=false;
			}
		}
	};
	boolean isPlayback=false;
	Dialog dialog;
	/**
	 * 显示测振回放选择框
	 * 
	 */
	public void showVibrationPlaybackDialog(final List<ProvisionalM> list){
		if(list.size()<=0){
			UIHelper.ToastMessage(this, getString(R.string.vibration_playback_none_value));
			return;
		}
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		LayoutInflater factory = LayoutInflater.from(VibrationActivity.this);
		final View view = factory.inflate(R.layout.playback_dialog_layout, null);
		dialog = new Dialog(this, R.style.AlertDialogStyle);
		ListView listView = (ListView) view.findViewById(R.id.listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				XJ_ResultHis entity=new XJ_ResultHis();
				entity.setResult_TX(list.get(position).getDJResult());
				entity.setData8K_TX(list.get(position).getData8K());
				entity.setDataLen(list.get(position).getDataLen());
				entity.setFeatureValue_TX(list.get(position).getFeatureValue_TX());
				entity.setFFTData_TX(list.get(position).getFFTData_TX());
				entity.setZhenDong_Type(list.get(position).getZHENDONGTYPE());
				UIHelper.showVibrationPlayback(VibrationActivity.this,entity);
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});
		
		if (list.size() > 5) {
			LinearLayout.LayoutParams params = (LayoutParams) listView
					.getLayoutParams();
			params.height = getWindowManager().getDefaultDisplay()
					.getHeight() / 2;
			listView.setLayoutParams(params);
		}
		
		VibrationPlaybackAdapter adapter=new VibrationPlaybackAdapter(this, list);
		listView.setAdapter(adapter);
		
		dialog.setContentView(view);
		dialog.show();
		
	}
}
