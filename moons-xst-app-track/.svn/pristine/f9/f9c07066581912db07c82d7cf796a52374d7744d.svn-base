package com.moons.xst.track.ui;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.moons.xst.buss.BluetoothHelper;
import com.moons.xst.buss.TempMeasureDBHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.R.color;
import com.moons.xst.track.bean.ProvisionalM;
import com.moons.xst.track.common.ByteUtils;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.xstinterface.ValueForBackInterface;

import de.greenrobot.event.EventBus;

public class VibrationForOuterAty extends BaseActivity implements
		OnClickListener {
	private final static String Vibration = "Vibration";

	boolean bRun = true;
	boolean bThread = false;

	boolean running = true;
	private String mParms = "";
	private static int mZDType = 1;// 震动类型
	private float mCurValue = 0;
	private Boolean isRtnValue;

	public static final int REQCODE_VIBRATIONSET = 1;// 测振设置
	private float mMinitSYYmax = (float) 0.01;
	private float mMinitPYYmax = (float) 0.01;

	private float mSYMax = Float.MIN_VALUE;
	private float mSYMin = Float.MAX_VALUE;
	private float mPYMax = Float.MIN_VALUE;
	private float mPYMin = Float.MAX_VALUE;

	private static int[] mCZPars = new int[10];// {0,5000,800,2,0,1,1,0,0,5000};
	private float[] mSYData;
	private float[] mPYData;
	private float[] mZBData = new float[13];

	private int rate = 0;
	// [end]

	// [start] 控件
	private RelativeLayout moreInfo, showmoreinfoBtn;
	private RelativeLayout setupBtn, operationBtn, saveBtn;
	private TextView vibration_memu_operation, vibration_memu_operation_desc;
	private ImageView vibration_more;
	private ImageButton returnButton;

	private ImageView iv_battery;

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

	private TempMeasureDBHelper tempMeasure;

	private LineChart mSYChart = null;
	private LineChart mPYChart = null;

	private LineData mSYLineData = null;
	private LineData mPYLineData = null;

	private LoadingDialog loading;
	private CheckBox cbA, cbB, cbC, cbD;
	private float AxHighLigh, BxHighLigh, xHighLigh;
	private XAxis xAxis;
	private float redVal, ziVal, redVal1, ziVal1, xHighLight1, cxHighLigh,
			dxHighLigh;
	private LineDataSet lineDataSet;

	private TextView redLineXValue, redLineYValue, ziLineXValue, ziLineYValue,
			twoLineDifferentXValue, twoLineDifferentYValue, redLineXValue1,
			redLineYValue1, ziLineXValue1, ziLineYValue1,
			twoLineDifferentXValue1, twoLineDifferentYValue1;

	private float xSYAxis_Spacing, xPYAxis_Spacing;
	private TextView txtValueUnit, redLineYValueUnit, ziLineYValueUnit,
			twoLineDifferentYValueUnit, redLineYValue1Unit, ziLineYValue1Unit,
			twoLineDifferentYValue1Unit;

	private ImageButton addSYYValue, reduceSYYValue, addPYYValue,
			reducePYYValue;
	private String STOP = "stop";
	private String START = "start";
	private String ZDType = "A";
	private boolean isConnect = false;
	StringBuilder sb;
	private String _btAddress;
	private String _btPassword;
	private ValueForBackInterface vibrationInterface;
	private String outerType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.vibrationforouter);
		EventBus.getDefault().register(this);
		// 先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数
		_btAddress = AppContext.getBlueToothAddressforVibration();
		_btPassword = AppContext.getBTConnectPwdforVibration();
		outerType = AppContext.getOuterMeasureType();
		ReadConfig();
		isRtnValue = getIntent().getBooleanExtra("isRtnValue", false);
		try {
			if (getIntent().getExtras().getString("parms").length() > 0) {
				mParms = getIntent().getExtras().getString("parms");
				String[] tempStr = mParms.split(",", 11);
				ZDType = tempStr[0].toString();
			}
		} catch (Exception e) {
		}
		if ("A".equals(ZDType)) {
			mSYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("A")[0]];
			mPYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("A")[1]];
			rate = BluetoothHelper.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("A")[2];
		} else if ("S".equals(ZDType)) {
			mSYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("S")[0]];
			mPYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("S")[1]];
			rate = BluetoothHelper.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("S")[2];
		} else if ("V".equals(ZDType)) {
			mSYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("V")[0]];
			mPYData = new float[BluetoothHelper
					.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("V")[1]];
			rate = BluetoothHelper.getIntance(this, outerType, Vibration)
					.getVibrationValueLen().get("V")[2];
		} else {
			UIHelper.ToastMessage(VibrationForOuterAty.this,
					getString(R.string.msg_bluetooth_zdtype_notsupport));
			AppManager.getAppManager()
					.finishActivity(VibrationForOuterAty.this);
			return;
		}

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

		if (vibration_memu_operation.getText().toString()
				.contains(getString(R.string.vibration_start))) {
			setenable(false);
		}
		startforouter();
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

	private void initTipView() {
		tempMeasure = new TempMeasureDBHelper();
		sb = new StringBuilder();
		for (int i = 0; i < mZBData.length; i++) {
			sb.append("0.0" + ";");
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

				String oldTextString = (String) vibration_memu_operation
						.getText();
				if (oldTextString.contains(getString(R.string.vibration_stop))) {
					setenable(true);
					stop(false, false);
				} else {
					operationBtn.setEnabled(false);
					setenable(false);
					startforouter();
				}
			}

		});
		setupBtn = (RelativeLayout) findViewById(R.id.ll_vibration_memu_setup);
		setupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stop(true, false);
				mSYData = null;
				mPYData = null;
				showCLSet(VibrationForOuterAty.this, ZDType, _btAddress,
						_btPassword);
			}
		});
	}

	private void setenable(boolean b) {
		addSYYValue.setEnabled(b);
		reduceSYYValue.setEnabled(b);
		addPYYValue.setEnabled(b);
		reducePYYValue.setEnabled(b);
	}

	/**
	 * 刷新测量类型
	 */
	private void RefreshValueTitle() {
		String valueTitle = getString(R.string.vibration_type_acceleration);
		if ("A".equals(ZDType)) {
			valueTitle = getString(R.string.vibration_type_accelerationvalue);
		} else if ("V".equals(ZDType)) {
			valueTitle = getString(R.string.vibration_type_tempovalue);
		} else if ("S".equals(ZDType)) {
			valueTitle = getString(R.string.vibration_type_displacementvalue);
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
			String path = AppConst.OuterVibrationConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				// 获取river中name属性值
				if (element.getAttribute("Name").equals("vibrationType")) {
					ZDType = element.getAttribute("Using").toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startforouter() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			UIHelper.ToastMessage(this,
					R.string.temperature_start_bluetooth_hint);
			return;
		}
		if (!adapter.isEnabled()) {
			LayoutInflater factory = LayoutInflater
					.from(VibrationForOuterAty.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(
					VibrationForOuterAty.this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(getString(R.string.msg_bluetooth_open_message))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BluetoothHelper.getIntance(
											VibrationForOuterAty.this,
											outerType, Vibration)
											.enableBluetooth();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									return;
								}
							}).show();
		} else {
			if (StringUtils.isEmpty(_btAddress)) {
				UIHelper.ToastMessage(this, R.string.temperature_no_bluetooth);
				return;
			}
			loading = new LoadingDialog(this);
			loading.setLoadText(getString(R.string.temperature_start_process));
			loading.setCancelable(false);
			loading.show();

			if (vibrationInterface == null) {
				vibrationInterface = new ValueForBackInterface() {

					@Override
					public void onSuccess(float value) {
						isConnect = true;
						mCurValue = value;
						RefreshValueTitle();
						RefreshZBData();
					}

					@Override
					public void onFail(String msg) {
						operationBtn.setEnabled(true);
						if (loading != null) {
							loading.dismiss();
						}
						if (mSYData == null || mPYData == null) {
							return;
						}
						if ("ValueFail".equals(msg)) {
							Toast.makeText(
									VibrationForOuterAty.this,
									VibrationForOuterAty.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_getvalue_fail),
									Toast.LENGTH_SHORT).show();
							
						}else if("ValueFailForMore".equals(msg)){
							ReSetChartAfterEeor();
							Toast.makeText(
									VibrationForOuterAty.this,
									VibrationForOuterAty.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_getvalue_fail),
									Toast.LENGTH_SHORT).show();
						}else if ("ConnFail".equals(msg)) {
							Toast.makeText(
									VibrationForOuterAty.this,
									VibrationForOuterAty.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_conn_fail),
									Toast.LENGTH_SHORT).show();
						} else if ("BatteryFail".equals(msg)) {
							Toast.makeText(
									VibrationForOuterAty.this,
									VibrationForOuterAty.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_battery_fail),
									Toast.LENGTH_SHORT).show();
						}
						stop(true, false);
						BluetoothHelper.getIntance(VibrationForOuterAty.this,
								outerType, Vibration).disposeHelper();
					}

					@Override
					public void onVibrationBXVauesSuccess(float[] SYvalues) {
						operationBtn.setEnabled(true);
						if (loading != null) {
							loading.dismiss();
						}
						// 时域
						mSYData = SYvalues.clone();
						RefreshSYChart();
						resetOperationBtn(STOP);
					}

					@Override
					public void onVibrationPPVauesSuccess(float[] PYvalues) {
						operationBtn.setEnabled(true);
						if (loading != null) {
							loading.dismiss();
						}
						// 频域
						mPYData = PYvalues.clone();
						RefreshPYChart();
						resetOperationBtn(STOP);
					}

					@Override
					public void onExtraValue(String value) {
					}
				};
			}
			BluetoothHelper.getIntance(this, outerType, Vibration)
					.getVibrationValue(isConnect, ZDType, vibrationInterface);

		}
	}

	private void ReSetChartAfterEeor() {
//		if ("A".equals(ZDType)) {
//			mSYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("A")[0]];
//			mPYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("A")[1]];
//		} else if ("S".equals(ZDType)) {
//			mSYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("S")[0]];
//			mPYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("S")[1]];
//		} else if ("V".equals(ZDType)) {
//			mSYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("V")[0]];
//			mPYData = new float[BluetoothHelper
//					.getIntance(VibrationForOuterAty.this, outerType, Vibration)
//					.getVibrationValueLen().get("V")[1]];
//		}
		txtValue.setText("0.00");
		mSYLineData = InitSYLineData(Color.RED);
		mPYLineData = InitPYLineData(Color.RED);
		RefreshSYChart();
		RefreshPYChart();
		if(iv_battery.getVisibility()==View.VISIBLE){
			iv_battery.setVisibility(View.GONE);
		}
	}

	private void resetOperationBtn(String operationYN) {
		if (STOP.equals(operationYN)) {
			vibration_memu_operation
					.setText(getString(R.string.vibration_stop));
			vibration_memu_operation_desc
					.setText(getString(R.string.vibration_stop_desc));
		} else if (START.equals(operationYN)) {
			vibration_memu_operation.setText(R.string.vibration_start);
			vibration_memu_operation_desc
					.setText(R.string.vibration_start_desc);
		}
	}

	/**
	 * 停止测量 disConnect 是否断开蓝牙连接
	 */
	private void stop(Boolean disConnect, boolean isWifiEnable) {
		if (disConnect) {
			isConnect = false;
			BluetoothHelper.getIntance(this, outerType, Vibration)
					.disConnBluetoothDevice(isWifiEnable);
		} else {
			BluetoothHelper.getIntance(this, outerType, Vibration)
					.stopGetVibrationValue();
		}
		if (!isWifiEnable) {
			resetOperationBtn(START);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		BluetoothHelper.getIntance(this, outerType, Vibration).disposeHelper();
	}

	/**
	 * EVENTBUS事件总线
	 * 
	 * @param str
	 */
	public void onEvent(String str) {
		if (str.equalsIgnoreCase("BATTERY_FIVE")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_five);
		} else if (str.equalsIgnoreCase("BATTERY_FOUR")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_four);
		} else if (str.equalsIgnoreCase("BATTERY_THREE")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_three);
		} else if (str.equalsIgnoreCase("BATTERY_TWO")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_two);
		} else if (str.equalsIgnoreCase("BATTERY_ONE")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_one);
		} else if (str.equalsIgnoreCase("BATTERY_EMPTY")) {
			iv_battery.setVisibility(View.VISIBLE);
			iv_battery.setImageResource(R.drawable.ic_battery_empty);
		} else if (str.equalsIgnoreCase("BATTERY_ERROR")) {
			iv_battery.setVisibility(View.GONE);
		}
	}

	// -------------------------------------------------------------------------------------------//
	/**
	 * 初始化数据
	 */
	private void initData() {
		// mCurValue = 0;

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
			if (0 == size) {
				return;
			}
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
		mSYChart.setData(mSYLineData); // 设置数据iiofr
		mSYChart.invalidate();
	}

	/**
	 * 刷新图
	 */
	private void RefreshPYChart() {
		if (mPYLineData != null) {
			List<Entry> yValues = mPYLineData.getDataSetByIndex(0).getYVals();
			int size = mPYData.length;
			if (0 == size) {
				return;
			}
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
		// mCurValue = getCLData();
		// BigDecimal db = new BigDecimal(String.valueOf(mCurValue));
		// String plainString = db.toPlainString();
		DecimalFormat curValuedf = new DecimalFormat("#0.0000");
		// DecimalFormat df = new DecimalFormat("#0.00");
		String format = curValuedf.format(mCurValue);
		txtValue.setText(format);
		// SU100A不用指标
		// txtFZ.setText(df.format(mZBData[0]));
		// txtFFZ.setText(df.format(mZBData[1]));
		// txtPJZ.setText(df.format(mZBData[2]));
		// txtPJFZ.setText(df.format(mZBData[3]));
		// txtFGFZ.setText(df.format(mZBData[4]));
		// txtYXZ.setText(df.format(mZBData[5]));
		// txtBXZB.setText(df.format(mZBData[6]));
		// txtMCZB.setText(df.format(mZBData[7]));
		// txtFZZB.setText(df.format(mZBData[8]));
		// txtYDZB.setText(df.format(mZBData[9]));
		// txtWDZB.setText(df.format(mZBData[10]));
		// txtDDZB.setText(df.format(mZBData[11]));
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

		// 隐藏右边坐标轴横网格线
		lineChart.getAxisRight().setDrawGridLines(false);

		// 设置在Y轴上是否是从0开始显示
		lineChart.getAxisLeft().setStartAtZero(false);

		// 设置Y轴坐标最小为多少
		lineChart.getAxisLeft().setAxisMaxValue(mMinitSYYmax);
		lineChart.getAxisLeft().setAxisMinValue(0 - mMinitSYYmax);

		lineChart.getAxisRight().setEnabled(false);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart
				.setNoDataTextDescription("You need to provide data for the chart.");

		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		lineChart.setGridBackgroundColor(color & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
		lineChart.setTouchEnabled(true); // 设置是否可以触摸
		lineChart.setScaleEnabled(true);// 是否可以缩放
		lineChart.setPinchZoom(true);
		lineChart.setBackgroundColor(color);// 设置背景
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(color);// 颜色
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
		xAxis.enableGridDashedLine(5, 3, 0); // 虚线表示X轴上的刻度竖线(float lineLength,
												// float spaceLength, float
												// phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标

		xAxis.resetLabelsToSkip(); // 将自动计算坐标相隔多少
		xAxis.setAvoidFirstLastClipping(true);

		xAxis.setTypeface(Typeface.DEFAULT_BOLD);

	}

	/**
	 * 初始化数据
	 */
	private LineData InitSYLineData(int color) {
		// xSYAxis_Spacing = ((float) ((float) 1000 / (mCZPars[1] * 2.56)));
		xSYAxis_Spacing = 1;
		ArrayList<String> xValues = new ArrayList<String>();

		for (int i = 0; i <= mSYData.length; i++) {
			// xValues.add("" + (int) (i * xSYAxis_Spacing));
			xValues.add("" + i);
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
		// 隐藏右边坐标轴横网格线
		lineChart.getAxisRight().setDrawGridLines(false);

		// 设置Y轴坐标最小为多少
		lineChart.getAxisLeft().setAxisMaxValue(mMinitPYYmax);
		lineChart.getAxisLeft().setAxisMinValue(0 - mMinitPYYmax);

		lineChart.getAxisRight().setEnabled(false);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart
				.setNoDataTextDescription("You need to provide data for the chart.");

		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		// 表格的的颜色，在这里是是给颜色设置一个透明度
		lineChart.setGridBackgroundColor(Color.rgb(245, 245, 245));
		lineChart.setTouchEnabled(true); // 设置是否可以触摸
		lineChart.setDragEnabled(false);// 是否可以拖拽
		lineChart.setScaleEnabled(true);// 是否可以缩放
		lineChart.setPinchZoom(true);//

		lineChart.setBackgroundColor(color);// 设置背景
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(color);// 颜色

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
		xAxis.enableGridDashedLine(5, 3, 0); // 虚线表示X轴上的刻度竖线(float lineLength,
												// float spaceLength, float
												// phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
		xAxis.resetLabelsToSkip(); // 将自动计算坐标相隔多少
		xAxis.setAvoidFirstLastClipping(true);
		xAxis.setTypeface(Typeface.DEFAULT_BOLD);
	}

	/**
	 * 初始化数据
	 * 
	 * @param color
	 * @return
	 */
	private LineData InitPYLineData(int color) {
		// xPYAxis_Spacing = ((float) mCZPars[1] / mCZPars[2]);
		xPYAxis_Spacing = 1;
		ArrayList<String> xValues = new ArrayList<String>();
		for (int i = 0; i < mPYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			// xValues.add("" + (int) (i * xPYAxis_Spacing));
			xValues.add("" + i);
		}

		// y轴的数据
		ArrayList<Entry> yValues = new ArrayList<Entry>();

		for (int i = 0; i < mPYData.length; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			yValues.add(new Entry(0, i));
		}
		LineDataSet lineDataSet = new LineDataSet(yValues, "" /* 显示在比例图上 */);

		// 用y轴的集合来设置参数
		lineDataSet.setLineWidth(1.75f); // 线宽
		lineDataSet.setCircleSize(0f);// 显示的圆形大小
		lineDataSet.setColor(color);// 显示颜色
		lineDataSet.setCircleColor(color);// 圆形的颜色
		lineDataSet.setValueTextColor(Color.TRANSPARENT);// 设置折线点上面的文字描述的颜色
		lineDataSet.setHighLightColor(Color.TRANSPARENT); // 高亮的线的颜色

		ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
		lineDataSets.add(lineDataSet); // add the datasets

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
				mSYData = null;
				mPYData = null;
				stop(true, true);
				AppManager.getAppManager().finishActivity(
						VibrationForOuterAty.this);
				BluetoothHelper.getIntance(this, outerType, Vibration)
						.disposeHelper();
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
	private void showCLSet(Context context, String mvibPackage,
			String btaddress, String btpwd) {
		Intent intent = new Intent(context, VibrationSettingForOuter.class);
		// intent.putExtra("zdType", zdType);
		intent.putExtra("vibPackage", mvibPackage);
		intent.putExtra("btAddress", btaddress);
		intent.putExtra("btPassword", btpwd);
		startActivityForResult(intent, REQCODE_VIBRATIONSET);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQCODE_VIBRATIONSET) {
			if (resultCode == Activity.RESULT_OK) {
				ZDType = intent.getExtras().getString("vibpackage");
				if ("A".equals(ZDType)) {
					mSYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("A")[0]];
					mPYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("A")[1]];
				} else if ("S".equals(ZDType)) {
					mSYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("S")[0]];
					mPYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("S")[1]];
				} else if ("V".equals(ZDType)) {
					mSYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("V")[0]];
					mPYData = new float[BluetoothHelper
							.getIntance(this, outerType, Vibration)
							.getVibrationValueLen().get("V")[1]];
				}
				_btAddress = intent.getExtras().getString("btAddress");
				_btPassword = intent.getExtras().getString("btPwd");
				RefreshValueTitle();
				txtValue.setText("0.00");
				initChart();
				mPYChart.getXAxis().removeAllLimitLines();
				mSYChart.getXAxis().removeAllLimitLines();
				cleanTxtParms();
				iv_battery.setVisibility(View.GONE);
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
													.getText() + "_" + ZDType);
											provisional.setDJResult(txtValue
													.getText().toString());

											provisional.setDJTime(DateTimeHelper.DateToString(
													DateTimeHelper
															.GetDateTimeNow(),
													"yyyyMMddHHmmss"));
											provisional.setData8K(ByteUtils
													.floatToByte(mSYData));
											provisional.setRatio(1);
											provisional.setRatio1(1);
											provisional
													.setRate((int) (rate * 2.56));
											provisional
													.setDataLen(mSYData.length);
											provisional
													.setKDBZUNIT(txtValueUnit
															.getText()
															.toString());
											provisional.setZHENDONGTYPE(ZDType);
											provisional.setVibParam_TX(ZDType
													+ ";2;0");
											provisional.setFeatureValue_TX(sb
													.toString());
											provisional.setFFTData_TX(ByteUtils
													.floatToByte(mPYData));
											boolean isExit = tempMeasure
													.InsertCeZhenBaseInfo(
															VibrationForOuterAty.this,
															provisional);
											if (isExit) {
												if (vibration_memu_operation
														.getText()
														.toString()
														.contains(
																getString(R.string.vibration_stop))) {
													mSYData = null;
													mPYData = null;
													stop(true, true);
													BluetoothHelper
															.getIntance(
																	VibrationForOuterAty.this,
																	outerType,
																	Vibration)
															.disposeHelper();

												}
												// closePower();
												AppManager
														.getAppManager()
														.finishActivity(
																VibrationForOuterAty.this);
											} else {
												UIHelper.ToastMessage(
														VibrationForOuterAty.this,
														R.string.initcp_message_savedefeated);
											}

										} catch (IOException e) {
											e.printStackTrace();
										}
									} else {
										UIHelper.ToastMessage(
												VibrationForOuterAty.this,
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
//			if (vibration_memu_operation.getText().toString()
//					.contains(getString(R.string.vibration_stop))) {
//				stop(true, true);
//			}
			stop(true, true);
			DecimalFormat df = new DecimalFormat("#0.0000");
			Intent intent = new Intent();
			intent.putExtra("rsts", df.format(mCurValue));
			intent.putExtra("datalen", mSYData.length);
			intent.putExtra("rate", (int) (rate * 2.56));
			try {
				intent.putExtra("sydata", ByteUtils.floatToByte(mSYData));
				intent.putExtra("pydata", ByteUtils.floatToByte(mPYData));
				intent.putExtra("featurevalue", sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			setResult(RESULT_OK, intent);
			mSYData = null;
			mPYData = null;
			AppManager.getAppManager()
					.finishActivity(VibrationForOuterAty.this);
			BluetoothHelper.getIntance(VibrationForOuterAty.this, outerType,
					Vibration).disposeHelper();
		}
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
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_UP) {
			goBack();
		}
		return super.dispatchKeyEvent(event);
	}

	private void initHeadView() {
		txtValueTitle = (TextView) findViewById(R.id.valueTitleTxt);

		txtValue = (TextView) findViewById(R.id.valueTxt);
		txtValue.setTextColor(Color.BLUE);
		txtValueUnit = (TextView) findViewById(R.id.valueTxtUnit);

		returnButton = (ImageButton) findViewById(R.id.vibration_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saveBtn.isEnabled()) {
					mSYData = null;
					mPYData = null;
					stop(true, true);
					AppManager.getAppManager().finishActivity(
							VibrationForOuterAty.this);
					BluetoothHelper.getIntance(VibrationForOuterAty.this,
							outerType, Vibration).disposeHelper();
				}
			}
		});
	}

	private void initView() {
		iv_battery = (ImageView) findViewById(R.id.iv_battery_icon_vib);
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
		showmoreinfoBtn.setVisibility(View.GONE);
		/*
		 * showmoreinfoBtn.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (moreInfo.getVisibility()
		 * == View.GONE) { moreInfo.setVisibility(View.VISIBLE);
		 * vibration_more.setImageResource(R.drawable.icon_up); } else {
		 * moreInfo.setVisibility(View.GONE);
		 * vibration_more.setImageResource(R.drawable.icon_down); } } });
		 */
	}
}
