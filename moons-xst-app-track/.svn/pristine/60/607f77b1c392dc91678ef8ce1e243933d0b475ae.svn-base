package com.moons.xst.track.ui;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.moons.xst.track.R;

public class HisDataChart {
	private static HisDataChart hisDataChart;
	public static LineChart mChart;
	private static ArrayList<String> xVals;
	private static ArrayList<Entry> yVals;
	private static Context context;

	public HisDataChart() {
		super();
	}

	public HisDataChart(Context context) {
		this.context = context;
	}

	public static HisDataChart getHisDataChart(Context context) {
		if (hisDataChart == null) {
			hisDataChart = new HisDataChart(context);
		}
		return hisDataChart;
	}

	public void initFunction(final Context context, RelativeLayout rl_dataChart) {
		mChart = (LineChart) rl_dataChart.findViewById(R.id.chartview);

		mChart.setDrawGridBackground(false);
		mChart.setDescription("");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		mChart.setTouchEnabled(true);
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setScaleXEnabled(false);
		mChart.setScaleYEnabled(false);
		mChart.setPinchZoom(false);// 设置是否可扩大和缩小
		mChart.setDoubleTapToZoomEnabled(false);// 双击缩放
		mChart.setHighlightEnabled(false);// 设置高亮线不显示

		XAxis xl = mChart.getXAxis();// 设置是否显示表格
		xl.setTextColor(Color.BLACK);
		xl.setDrawGridLines(true);// 设置是否显示X轴表格
		xl.setGridColor(context.getResources().getColor(R.color.graywhite));
		xl.setAvoidFirstLastClipping(true);// 设置x轴起点和终点label不超出屏幕
		xl.setSpaceBetweenLabels(1);// 设置x轴label间隔,刻度之间间隔
		xl.setEnabled(true);
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setAxisLineWidth(1);
		xl.setAxisLineColor(context.getResources().getColor(R.color.gray));
		xl.setDrawLabels(false);// 设置是否要x轴标签
		xl.setDrawAxisLine(true);
		xl.setLabelsToSkip(0);
		xl.enableGridDashedLine(20, 10, 0);// 设置网格虚线

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setStartAtZero(true);
		leftAxis.setDrawGridLines(false);
		leftAxis.setAxisLineWidth(1);
		leftAxis.setAxisLineColor(context.getResources().getColor(R.color.gray));
		mChart.getAxisRight().setEnabled(false);
		mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

		Legend l = mChart.getLegend();
		l.setForm(LegendForm.LINE);
		mChart.animateX(1000); // 立即执行的动画,x轴
		final MyMarkViewLeft markerLeft = new MyMarkViewLeft(context,
				R.layout.custom_marker_view);
		final MyMarkViewNormal markerNormal = new MyMarkViewNormal(context,
				R.layout.custom_marker_view);
		final MyMarkViewRight markerRight = new MyMarkViewRight(context,
				R.layout.custom_marker_view);
		mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry entry, int arg1, Highlight arg2) {
				if (xVals.get(entry.getXIndex()).equals(xVals.get(0))) {
					mChart.setMarkerView(markerLeft);
				} else if (xVals.get(entry.getXIndex()).equals(
						xVals.get(xVals.size() - 1))) {
					mChart.setMarkerView(markerRight);
				} else {
					mChart.setMarkerView(markerNormal);
				}
			}

			@Override
			public void onNothingSelected() {

			}
		});
		mChart.invalidate();

	}

	// 设置X轴 Y轴数据及图表数据
	public void setData(Context context, List<String> xValue, List<Float> yValue) {

		xVals = new ArrayList<String>();
		for (int i = 0; i < yValue.size(); i++) {
			xVals.add(xValue.get(i));
		}

		yVals = new ArrayList<Entry>();

		for (int i = 0; i < yValue.size(); i++) {
			float val = (float) yValue.get(i);
			yVals.add(new Entry(val, i));
		}
		LineDataSet set1 = new LineDataSet(yVals, context.getString(
				R.string.dj_confirm_hisdatachart_des,
				xValue.get(0).substring(0, 10), xValue.get(xValue.size() - 1)
						.substring(0, 10)));

		set1.setColor(context.getResources().getColor(R.color.xstblue));
		set1.setCircleColor(Color.RED);
		set1.setLineWidth(1f);
		set1.setCircleSize(3f);
		set1.setDrawCircleHole(false);
		set1.setValueTextSize(9f);
		set1.setFillAlpha(65);
		set1.setFillColor(Color.BLACK);
		set1.setValueTextSize(12);
		set1.setDrawValues(true);
		set1.setValueFormatter(new ValueFormatter() {

			@Override
			public String getFormattedValue(float arg0) {
				String valueOf = String.valueOf(Float.parseFloat(String.format(
						"%.6f", arg0)));
				BigDecimal bd = new BigDecimal(valueOf);
				return bd.toString();
			}
		});
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(set1);

		LineData data = new LineData(xVals, dataSets);
		mChart.setData(data);

		data.notifyDataChanged();
		mChart.notifyDataSetChanged();
		mChart.setVisibleXRange(4);// 设置一页显示4个点
		mChart.invalidate();
	}

	public class MyMarkViewLeft extends BaseMarkerView {

		public MyMarkViewLeft(Context context, int layoutResource) {
			super(context, layoutResource);
		}

		@Override
		protected int getXaddValue() {
			return 110;
		}

		@Override
		protected int getYaddValue() {
			return -6;
		}
	};

	public class MyMarkViewNormal extends BaseMarkerView {

		public MyMarkViewNormal(Context context, int layoutResource) {
			super(context, layoutResource);
		}
	};

	public class MyMarkViewRight extends BaseMarkerView {

		public MyMarkViewRight(Context context, int layoutResource) {
			super(context, layoutResource);
		}

		@Override
		protected int getXaddValue() {
			return -110;
		}

		@Override
		protected int getYaddValue() {
			return -6;
		}

	};

	public class BaseMarkerView extends MarkerView {

		public BaseMarkerView(Context context, int layoutResource) {
			super(context, layoutResource);
		}

		@Override
		public void refreshContent(Entry e, int arg1) {
			TextView tvContent = (TextView) findViewById(R.id.tvContent);

			String valueOf = String.valueOf(Float.parseFloat(String.format(
					"%.6f", e.getVal())));
			BigDecimal bd = new BigDecimal(valueOf);
			String yValue = bd.toString();
			tvContent.setText(context.getString(
					R.string.dj_confirm_hisdatachart_markerview_des,
					(xVals.get(e.getXIndex())), yValue));
		}

		@Override
		public int getYOffset() {
			return -getHeight();
		}

		@Override
		public int getXOffset() {
			return -(getWidth() / 2);
		}

		@Override
		public void draw(Canvas canvas, float posx, float posy) {
			posx += getXOffset() + getXaddValue();
			posy += getYOffset() + getYaddValue();

			canvas.translate(posx, posy);
			draw(canvas);
			canvas.translate(-posx, -posy);
		}

		protected int getYaddValue() {
			return 0;
		}

		protected int getXaddValue() {
			return 0;
		}
	}
}
