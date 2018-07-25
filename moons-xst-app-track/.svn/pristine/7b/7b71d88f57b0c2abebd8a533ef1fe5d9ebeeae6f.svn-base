package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moons.xst.buss.CycleHelper;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.buss.UnCheckQueryHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.PieChart.piechartview.OnDateChangedLinstener;
import com.moons.xst.track.widget.PieChart.piechartview.StatisticsView;


public class StatisticsUncheckAty extends BaseActivity implements OnDateChangedLinstener{
	
	private final static String STATE_FINISHED = "UNCHECKFINISHED";
	
	private StatisticsView mView;
	private FrameLayout fl_uncheck_pie_chart;
	private TextView tv_detail;
	private ImageButton reButton;
	
	private int current_lineID;
	private String current_lineName;
	private int current_lineType;
	private int total = 0;
	
	private float[] items;
	
	private ConcurrentHashMap<Integer, String[]> types = new ConcurrentHashMap<Integer, String[]>();
	
	LoadingDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_uncheck);
		
		if (savedInstanceState != null) {
			current_lineID = savedInstanceState.getInt("current_lineID");
			current_lineName = savedInstanceState.getString("current_lineName");
			current_lineType = savedInstanceState.getInt("current_lineType");
		} else {
			current_lineID = getIntent().getExtras().getInt("line_id");
			current_lineName = getIntent().getExtras().getString("line_name");
			current_lineType = getIntent().getExtras().getInt("line_type");
		}
		init();
		// 注册广播
		registerBoradcastReceiver();				
		AppContext.SetCurrLineID(current_lineID);		
		getUncheckData();		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putInt("current_lineID", current_lineID);
		outState.putString("current_lineName", current_lineName);
		outState.putInt("current_lineType", current_lineType);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onDateChanged(String startDate, String endDate) {
	}
	
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				loadData();
			} else if (msg.what == 0) {
				UIHelper.ToastMessage(StatisticsUncheckAty.this, msg.obj.toString());
				AppManager.getAppManager().finishActivity(StatisticsUncheckAty.this);
			}
		};
	};
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if(msg.what == -1){
				UIHelper.ToastMessage(StatisticsUncheckAty.this, getString(R.string.djidpos_message_dbwrong));
				AppManager.getAppManager().finishActivity(StatisticsUncheckAty.this);
			}
		};
	};
	
	private void init() {
		fl_uncheck_pie_chart = (FrameLayout) findViewById(R.id.fl_statistics_uncheck_pie_chart);
		reButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		reButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				back();
			}
		});
	}
	
	

	
	private void getUncheckData() {
		getUncheckDataThread();
	}
	
	private void getUncheckDataThread() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCancelable(false);
			loading.setCanceledOnTouchOutside(false);
			loading.show();
		}
		
		new Thread() {
			public void run() {
				try {
					// Thread.sleep(500);
					// 重新加载ID位置数据
					getUncheckDataByLineID();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void getUncheckDataByLineID() {
		// 加载数据
		if (current_lineType == AppConst.LineType.XDJ.getLineType()) {
			if (DJPlanHelper.GetIntance().loadControlPoint(this)
					|| CycleHelper.GetIntance().loadCycleData(this)
					|| UnCheckQueryHelper.GetIntance().loadUnCheckData(this,
							DateTimeHelper.getDateTimeNow())) {
				//发送handler提示
				Message msg = Message.obtain();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		} else if (current_lineType == AppConst.LineType.DJPC.getLineType()) {
			if (UnCheckQueryHelper.GetIntance().loadUnCheckDataForDJPC(this)) {
				//发送handler提示
				Message msg = Message.obtain();
				msg.what = -1;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	private void loadData() {
		
		int uncheckCount;
		String key = "";
		int i = 0;
		for(Iterator<String> itr = AppContext.UnCheckPlanBuffer.keySet().iterator();itr.hasNext();){
			uncheckCount = 0;
			key = (String)itr.next();  
			uncheckCount = AppContext.UnCheckPlanBuffer.get(key).size();
            
            if (uncheckCount > 0) {
				total += uncheckCount;
				types.put(i, new String[] {
						key,
						getString(R.string.query_uncheck_desc),
						String.valueOf(uncheckCount),
						"",
						""});
				i++;
			}
        }
		
		if (types.size() > 0) {
			items = new float[types.size()];
			for (int j = 0; j < types.size(); j++) {
				items[j] = Float.parseFloat(types.get(j)[2]);
			}
			
			mView = new StatisticsView(this, items, total, true, types);
			mView.setCurrText(current_lineName);
			mView.setStatisticsTitle(getString(R.string.query_uncheck_totalcount));
			mView.setCurrCount(total);
			mView.setDateChangedListener(this);
			
			fl_uncheck_pie_chart.addView(mView);
			
			tv_detail = (TextView) mView.findViewById(R.id.txt_detail);
			tv_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					String[] str = new String[types.size()]; 
					for (int i = 0; i < types.size(); i++) {
						str[i] = types.get(i)[0].toString();
					}
					UIHelper.showUncheckDetails(StatisticsUncheckAty.this, str);
				}
			});
		} else {
			LayoutInflater factory = LayoutInflater.from(StatisticsUncheckAty.this);
			final View view = factory.inflate(R.layout.textview_layout,
					null);
			new com.moons.xst.track.widget.AlertDialog(StatisticsUncheckAty.this)
					.builder()
					.setTitle(getString(R.string.tips))
					.setView(view)
					.setMsg(getString(R.string.query_uncheck_nodata))
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.setPositiveButton(getString(R.string.sure),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									back();
								}
							}).show();			
		}
	}
	
	private void back() {
		AppContext.UnCheckPlanBuffer.clear();
		unregisterBoradcastReceiver();
		AppManager.getAppManager().finishActivity(StatisticsUncheckAty.this);
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = Message.obtain();
			if (intent.getAction().equals(STATE_FINISHED)) {
				if (AppContext.UnCheckPlanBuffer.size() <= 0) {
					msg.what = 0;
					msg.obj = getString(R.string.query_uncheck_nodata);
				} else {
					msg.what = 1;
				}
				loadHandler.sendMessage(msg);
			} 
		}
	};
	
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
		myIntentFilter.addAction(STATE_FINISHED);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
}
