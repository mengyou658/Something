package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.buss.DJHisDataHelper;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJQueryResult_HisAdapter;
import com.moons.xst.track.bean.XJ_ResultHis;
import com.moons.xst.track.widget.LoadingDialog;

public class QueryHisResultPerPlan extends BaseActivity {

	private TextView plandesc;
	private ImageButton rebutton;
	private ListView hisresultlv;

	String lineID = "";
	String planID = "";
	String planDesc = "";
	String planType = "GC";
	List<XJ_ResultHis> planidhislist = new ArrayList<XJ_ResultHis>();
	List<XJ_ResultHis> filterPlanidhislist = new ArrayList<XJ_ResultHis>();
	List<Float> yValue; // 折线对应的数据
	private DJQueryResult_HisAdapter planhisadapter;
	private List<String> xValue = new ArrayList<String>();// x轴坐标对应的数据
	private RelativeLayout rl_dataChart;// 承载阻力效果控件的容器
	private HisDataChart hisDataChart;// 放折线图的控件
	
	LoadingDialog loading;
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_hisresult_perplan);

		//ButterKnife.bind(this);
		rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(QueryHisResultPerPlan.this);
			}
		});
		plandesc = (TextView) findViewById(R.id.query_data_line_Value);
		hisresultlv = (ListView) findViewById(R.id.query_hisresult_perplan_listview);

		initData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AppManager.getAppManager().finishActivity(
					QueryHisResultPerPlan.this);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {
		lineID = getIntent().getStringExtra("lineid");
		planID = getIntent().getStringExtra("planid");
		planDesc = getIntent().getStringExtra("plandesc");
		planType = getIntent().getStringExtra("plantype");
		plandesc.setText(planDesc);

		loadDataAndShow();	
	}
	
	private void loadDataAndShow() {

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1) {
					showPlanHisdataList();
					showChartByHisList();

				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(QueryHisResultPerPlan.this);
				}
			}
		};
		this.loadDataThread();
	}
	
	private void loadDataThread() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.show();
		}

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					loadData();
					msg.what = 1;
				} catch (Exception e) {
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
	
	private void loadData() {
		filterPlanidhislist = DJHisDataHelper.GetIntance().loadDJHisData(
				QueryHisResultPerPlan.this, lineID, planID);
		if (filterPlanidhislist != null && filterPlanidhislist.size() > 0) {
			for (XJ_ResultHis info : filterPlanidhislist) {
				if (info.getPlanDesc_TX().equalsIgnoreCase(planDesc)) {
					planidhislist.add(info);
				}
			}
		}
	}

	/**
	 * 获取计划历史信息列表
	 * 
	 */
	private void showPlanHisdataList() {		
		if (planidhislist != null && planidhislist.size() > 0) {
			planhisadapter = new DJQueryResult_HisAdapter(this, planidhislist,
					R.layout.listitem_djplan);
			hisresultlv.setAdapter(planhisadapter);
		}
	}

	/**
	 * 历史结果图表显示
	 */
	private void showChartByHisList() {
		// 加载阻力效果的图表控件
		rl_dataChart = (RelativeLayout) findViewById(R.id.function1_expand_value);
		hisDataChart = HisDataChart.getHisDataChart(QueryHisResultPerPlan.this);
		hisDataChart.initFunction(getApplicationContext(), rl_dataChart);

		/* 除了观察类，加载趋势图 */
		if (!planType.equalsIgnoreCase("GC")) {			
			if (planidhislist != null && planidhislist.size() > 0) {							
				yValue = new ArrayList<Float>(planidhislist.size());
				for (int i = 0; i < planidhislist.size(); i++) {
					yValue.add(Float.parseFloat(planidhislist.get(i)
							.getResult_TX()));
					xValue.add(planidhislist.get(i).getCompleteTime_DT());
				}
				// hisDataChart.initFunction(getApplicationContext(),rl_dataChart,xValue);
				hisDataChart.setData(this, xValue, yValue);

			} else {
				// 数据源为空时显示空图

			}
		} else {
			// 观察类没有趋势图，显示空图

		}
	}
}