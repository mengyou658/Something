package com.moons.xst.track.ui;

import java.io.File;
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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.moons.xst.buss.BluetoothHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.xstinterface.ValueForBackInterface;

import de.greenrobot.event.EventBus;

public class TemperatureForOuter extends BaseActivity {

	private final static String Temperature = "Temperature";
	// [start] 自定义传入传出参数
	private static int mFsl = 95;
	private String mSaveType = "Current";

	// [start] 自定义内部使用参数
	private static int mInitYmax = 10;
	private static float mCurValue = 0;
	private static float mMaxValue = Float.MIN_VALUE;
	private static float mMinValue = Float.MAX_VALUE;
	private static int mXChartCount = 100;
	public static final int REQCODE_TEMPERATURESET = 1;// 测温设置

	private RelativeLayout setupBtn, operationBtn, saveBtn;
	private ImageButton returnButton;

	private ImageView iv_battery;

	private TextView temperature_memu_operation,
			temperature_memu_operation_desc;
	private static TextView txtValue = null;
	private static TextView txtMaxValue = null;
	private static TextView txtMinValue = null;
	private static LineChart mLineChart = null;
	private static LineData mLineData = null;
	private TextView temperatureYValue;
	private LoadingDialog loading;

	private String STOP = "stop";
	private String START = "start";
	private ValueForBackInterface temperatureInterface;

	private String _btAddress = "";
	private String _btPassword = "0000";
	private String _outerType;
	private boolean isConnect = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperatureforouter);
		EventBus.getDefault().register(this);
		/* 先默认从配置文件读取参数信息，如果页面传递了参数则覆盖配置文件参数 */
		readConfig();
		_btAddress = AppContext.getBlueToothAddressforTemperature();
		_btPassword = AppContext.getBTConnectPwdforTemperature();
		_outerType = AppContext.getOuterMeasureType();
		initView();
		mLineData = InitLineData(Color.RED);
		initChart(Color.WHITE);
		RefreshChart();
		startforouter();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		EventBus.getDefault().unregister(this);
		BluetoothHelper.getIntance(this, _outerType, Temperature)
				.disposeHelper();
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

	private void initView() {
		iv_battery = (ImageView) findViewById(R.id.iv_battery_icon);
		txtValue = (TextView) findViewById(R.id.valueTxt);
		txtMaxValue = (TextView) findViewById(R.id.maxValueTxt);
		txtMinValue = (TextView) findViewById(R.id.minValueTxt);
		txtValue.setTextColor(Color.BLUE);
		txtMaxValue.setTextColor(Color.RED);
		txtMinValue.setTextColor(Color.RED);
		mLineChart = (LineChart) findViewById(R.id.chart);
		temperatureYValue = (TextView) findViewById(R.id.tv_temperature_yvalue);
		initHeadView();
		initTipView();
	}

	private void initHeadView() {
		returnButton = (ImageButton) findViewById(R.id.temperature_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (saveBtn.isEnabled()) {
					goBack();
				}
			}
		});
	}

	private void initTipView() {
		saveBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_ok);
		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("rsts", Float.toString(getSaveData()));
				setResult(RESULT_OK, intent);
				
				goBack();
			}
		});

		temperature_memu_operation = (TextView) findViewById(R.id.temperature_memu_operation);
		temperature_memu_operation_desc = (TextView) findViewById(R.id.temperature_memu_operation_desc);
		operationBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_operation);
		operationBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String oldTextString = (String) temperature_memu_operation
						.getText();
				if (oldTextString
						.contains(getString(R.string.temperature_stop)))
					stop(false,false);
				else {
					operationBtn.setEnabled(false);
					startforouter();
				}
			}
		});

		setupBtn = (RelativeLayout) findViewById(R.id.ll_temperature_memu_setup);
		setupBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stop(true,false);
				showCLSet(TemperatureForOuter.this, mFsl, mSaveType,
						_btAddress, _btPassword);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mCurValue = 0;
		mMaxValue = Float.MIN_VALUE;
		mMinValue = Float.MAX_VALUE;

		List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
		for (int i = 0; i < yValues.size(); i++) {
			yValues.get(i).setVal(0);
		}
	}

	/**
	 * 设置图表显示样式
	 */
	private void initChart(int color) {

		LineChart lineChart = mLineChart;
		lineChart.setDrawBorders(false); // 是否在折线图上添加边框
		lineChart
				.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

					@Override
					public void onValueSelected(Entry arg0, int arg1,
							Highlight arg2) {
						mLineChart.getXAxis().removeAllLimitLines();
						// 游标获取到X轴的值
						int xIndex = arg2.getXIndex();
						addLine(xIndex);
						// 游标获取到Y轴的值
						float val = arg0.getVal();
						temperatureYValue.setText(String.valueOf(val));
					}

					@Override
					public void onNothingSelected() {

					}
				});

		// 隐藏右边坐标轴横网格线
		lineChart.getAxisRight().setDrawGridLines(false);
		// 隐藏X轴竖网格线
		lineChart.getXAxis().setDrawGridLines(false);
		lineChart.getAxisLeft().setStartAtZero(false);
		// 是否在Y轴显示数据，就是曲线上的数据

		// 设置Y轴坐标最小为多少
		lineChart.getAxisLeft().setAxisMaxValue(mInitYmax);
		lineChart.getAxisLeft().setAxisMinValue(0 - mInitYmax);
		// 第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
		lineChart.getAxisRight().setEnabled(false);
		lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		lineChart.setDescription("");// 数据描述
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		lineChart
				.setNoDataTextDescription("You need to provide data for the chart.");
		lineChart.setDrawGridBackground(false); // 是否显示表格颜色
		lineChart.setGridBackgroundColor(color & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
		lineChart.setTouchEnabled(true); // 设置是否可以触摸
		lineChart.setDragEnabled(false);// 是否可以拖拽
		lineChart.setScaleEnabled(true);// 是否可以缩放

		// if disabled, scaling can be done on x- and y-axis separately
		lineChart.setPinchZoom(false);//
		lineChart.setBackgroundColor(color);// 设置背景
		Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

		// mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
		mLegend.setForm(LegendForm.CIRCLE);// 样式
		mLegend.setFormSize(6f);// 字体
		mLegend.setTextColor(color);// 颜色

		// 是否可以缩放 仅y轴
		lineChart.setScaleYEnabled(false);
		// 是否显示X坐标轴上的刻度，默认是true
		lineChart.getXAxis().setDrawLabels(false);
	}

	/**
	 * 从配置文件读取参数配置信息
	 */
	private void readConfig() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			String path = AppConst.OuterTemperatureConfigPath();
			File f = new File(path);
			Document doc = db.parse(f);
			doc.normalize();
			/* 找到根Element */
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));
				/* 获取river中name属性值 */
				if (element.getAttribute("Name").equals("SaveType")) {
					mSaveType = element.getAttribute("Using");
				} else if (element.getAttribute("Name").equals("FSL")) {
					mFsl = Integer.parseInt(element.getAttribute("Using"));
					String fsl = String.valueOf((float) mFsl / 100);
					AppContext.setBTFSLforTemputer(fsl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取要保存的值
	 */
	private float getSaveData() {
		// float rvalue = mCurValue;
		float rvalue = Float.valueOf(txtValue.getText().toString());
		if (mSaveType.equals("MAX")) {
			rvalue = Float.valueOf(txtMaxValue.getText().toString());
		} else if (mSaveType.equals("MIN")) {
			rvalue = Float.valueOf(txtMinValue.getText().toString());
		}
		return rvalue;
	}

	/**
	 * 外接测量启动
	 */
	private void startforouter() {

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			UIHelper.ToastMessage(this,
					R.string.temperature_start_bluetooth_hint);
			return;
		}

		if (!adapter.isEnabled()) {
			LayoutInflater factory = LayoutInflater
					.from(TemperatureForOuter.this);
			final View view = factory.inflate(R.layout.textview_layout, null);
			new com.moons.xst.track.widget.AlertDialog(TemperatureForOuter.this)
					.builder()
					.setTitle(getString(R.string.system_notice))
					.setView(view)
					.setMsg(getString(R.string.msg_bluetooth_open_message))
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									BluetoothHelper.getIntance(
											TemperatureForOuter.this,
											_outerType, Temperature)
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

			if (temperatureInterface == null) {
				temperatureInterface = new ValueForBackInterface() {

					@Override
					public void onSuccess(float value) {
						operationBtn.setEnabled(true);
						isConnect = true;
						mCurValue = value;
						RefreshData();
						if (loading != null) {
							loading.dismiss();
						}
						resetOperationBtn(STOP);
					}

					@Override
					public void onFail(String msg) {
						operationBtn.setEnabled(true);
						isConnect = false;
						if (loading != null) {
							loading.dismiss();
						}
						if ("ConnError".equals(msg)) {
							return;
						}
						if ("ValueFail".equals(msg)) {
							Toast.makeText(
									TemperatureForOuter.this,
									TemperatureForOuter.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_getvalue_fail),
									Toast.LENGTH_SHORT).show();
						} else if ("ConnFail".equals(msg)) {
							Toast.makeText(
									TemperatureForOuter.this,
									TemperatureForOuter.this
											.getResources()
											.getString(
													R.string.msg_bluetooth_conn_fail),
									Toast.LENGTH_SHORT).show();
						}
						stop(true,false);
						BluetoothHelper.getIntance(TemperatureForOuter.this, _outerType, Temperature)
							.disposeHelper();
					}

					@Override
					public void onVibrationPPVauesSuccess(float[] values) {

					}

					@Override
					public void onVibrationBXVauesSuccess(float[] values) {

					}

					@Override
					public void onExtraValue(String value) {
					}

				};
			}
			if (!isConnect) {
				initData();
			}
			BluetoothHelper.getIntance(this, _outerType, Temperature)
					.getTemperature(isConnect, temperatureInterface);			
		}
	}

	private void resetOperationBtn(String operationYN) {
		if (STOP.equals(operationYN)) {
			temperature_memu_operation
					.setText(getString(R.string.temperature_stop));
			temperature_memu_operation_desc
					.setText(getString(R.string.temperature_stop_desc));
		} else if (START.equals(operationYN)) {
			temperature_memu_operation.setText(R.string.temperature_start);
			temperature_memu_operation_desc
					.setText(R.string.temperature_start_desc);
		}
	}

	/**
	 * 停止测量
	 */
	private void stop(boolean disConnect,boolean isWifiEnable) {
		if (disConnect) {
			isConnect = false;
			BluetoothHelper.getIntance(this, _outerType, Temperature)
					.disConnBluetoothDevice(isWifiEnable);
		} else {
			BluetoothHelper.getIntance(this, _outerType, Temperature)
					.stopTemperature();
		}
		if(!isWifiEnable){
			resetOperationBtn(START);
		}
	}

	private static void RefreshData() {
		if (mMaxValue < mCurValue) {
			mMaxValue = mCurValue;
			ResetYaxMaxValueAndMinValue();
		}
		if (mMinValue > mCurValue) {
			mMinValue = mCurValue;
			ResetYaxMaxValueAndMinValue();
		}
		String vl = Float.toString(mCurValue);
		txtValue.setText(vl);
		txtMaxValue.setText(Float.toString(mMaxValue));
		txtMinValue.setText(Float.toString(mMinValue));
		AddData(vl);
		RefreshChart();
	}

	/**
	 * 转至设置页面
	 */
	private void showCLSet(Context context, int fsl, String saveType,
			String btaddress, String btpwd) {
		Intent intent = new Intent(context, TemperatureSettingForOuter.class);
		intent.putExtra("fsl", fsl);
		intent.putExtra("saveType", saveType);
		intent.putExtra("btAddress", btaddress);
		intent.putExtra("btPassword", btpwd);
		startActivityForResult(intent, REQCODE_TEMPERATURESET);
	}

	/**
	 * 往图表数据里加数据
	 */
	private static void AddData(String data) {
		if (mLineData == null)
			return;

		List<Entry> yValues = mLineData.getDataSetByIndex(0).getYVals();
		float value = (float) Double.parseDouble(data);
		int size = yValues.size();
		if (size < mXChartCount) {
			yValues.add(new Entry(value, mXChartCount - size - 1));
		} else {
			for (int i = size - 1; i > 0; i--) {
				yValues.get(i).setVal(yValues.get(i - 1).getVal());
			}

			yValues.get(0).setVal(value);
		}
	}

	/**
	 * 重新刷新坐标值
	 */
	private static void ResetYaxMaxValueAndMinValue() {
		float axMax = mLineChart.getAxisLeft().getAxisMaxValue();
		float axMin = mLineChart.getAxisLeft().getAxisMinValue();
		if (mMaxValue > axMax) {
			int bs = (int) (mMaxValue % mInitYmax == 0 ? mMaxValue / mInitYmax
					: mMaxValue / mInitYmax + 1);
			mLineChart.getAxisLeft().setAxisMaxValue(bs * mInitYmax);
		}
		if (mMinValue < axMin) {
			int bs = (int) (mMinValue % mInitYmax == 0 ? mMinValue / mInitYmax
					: mMinValue / mInitYmax - 1);
			mLineChart.getAxisLeft().setAxisMinValue(bs * mInitYmax);
		}
	}

	/**
	 * 刷新图
	 */
	private static void RefreshChart() {
		mLineChart.setData(mLineData);
		mLineChart.invalidate();
	}

	/**
	 * 初始化数据
	 */
	private LineData InitLineData(int color) {
		ArrayList<String> xValues = new ArrayList<String>();
		for (int i = 0; i < mXChartCount; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			xValues.add("" + i);
		}
		// y轴的数据
		ArrayList<Entry> yValues = new ArrayList<Entry>();
		for (int i = 0; i < mXChartCount; i++) {
			// x轴显示的数据，这里默认使用数字下标显示
			yValues.add(new Entry(0, i));
		}
		LineDataSet lineDataSet = new LineDataSet(yValues, null/* 显示在比例图上 */);
		lineDataSet.setLineWidth(1.75f); // 线宽
		lineDataSet.setCircleSize(0f);// 显示的圆形大小
		lineDataSet.setColor(color);// 显示颜色
		lineDataSet.setCircleColor(color);// 圆形的颜色
		lineDataSet.setHighLightColor(Color.TRANSPARENT); // 高亮的线的颜色
		lineDataSet.setValueTextColor(Color.TRANSPARENT);// 设置折线上点的文字颜色
		ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
		lineDataSets.add(lineDataSet); // add the datasets
		LineData lineData = new LineData(xValues, lineDataSets);

		return lineData;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (saveBtn.isEnabled()) {
				goBack();
				return true;
			} else {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == REQCODE_TEMPERATURESET) {
			if (resultCode == Activity.RESULT_OK) {
				// txtValue.setText("0.00");
				mFsl = intent.getExtras().getInt("fsl");
				AppContext.setBTFSLforTemputer(String
						.valueOf((float) mFsl / 100));
				mSaveType = intent.getExtras().getString("saveType");
				_btAddress = intent.getExtras().getString("btAddress");
				_btPassword = intent.getExtras().getString("btPwd");

				iv_battery.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 添加图表上面的竖线
	 * */
	private void addLine(int xHighLight) {
		LimitLine ll = new LimitLine(xHighLight, "");
		ll.setLineWidth(1);
		ll.setLineColor(Color.rgb(0, 0, 255));
		mLineChart.getXAxis().addLimitLine(ll);
	}

	private void goBack() {
		stop(true,true);
		// 回收Helper
		BluetoothHelper.getIntance(TemperatureForOuter.this, _outerType, Temperature)
				.disposeHelper();
		// 关闭界面
		AppManager.getAppManager().finishActivity(TemperatureForOuter.this);
	}

	/**
	 * OK键点击确认
	 */
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_UP) {
//			if (temperature_memu_operation.getText().toString()
//					.contains(getString(R.string.temperature_stop))) {
//				stop(true,true);
//			}
			Intent intent = new Intent();
			intent.putExtra("rsts", Float.toString(getSaveData()));

			setResult(RESULT_OK, intent);
			goBack();
		}
		return super.dispatchKeyEvent(event);
	}
}
