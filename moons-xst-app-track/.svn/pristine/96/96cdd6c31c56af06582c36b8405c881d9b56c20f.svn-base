package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.GPSXXPlanAdpter;
import com.moons.xst.track.adapter.TrackinitAdapter;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.GPSXXPlan;
import com.moons.xst.track.bean.GPSXXPlanResult;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;

public class GPSPlanListAty extends BaseActivity {

	private ListView planListView;
	private TrackinitAdapter adapter;
	private LoadingDialog loading;
	private Handler mHandler;
	private TrackHelper trackHelper;
	private String currCpID;
	private ListView lvGPSXXPlanListView;
	private GPSXXPlanAdpter lvGPSXXPlanAdapter;
	private ImageButton rebutton;

	private CheckPointInfo checkinfo;
	
	private String pointname;
	private String pointType;
	private String pointDes;
	
	private TextView pointnameView;
	private TextView pointTypeView;
	private TextView pointDesView;
	
	// 详细弹出--popupWindow
	private PopupWindow listDetailPW;
	private LayoutInflater lm_detailinflater;
	private View detaillayout;
	private Button okbutton;
	private ImageButton closebu;
	private EditText redecsView;
	private TextView destView;
	//radiogroup
	private RadioGroup ragroup;
	private RadioButton nomalbutton;
	private RadioButton unnomalbutton;
	
	private String retx;
	private String planid;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_xx_planlist);
		// 获取LayoutInflater实例
		lm_detailinflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		detaillayout = lm_detailinflater.inflate(R.layout.gps_xxplande,
				null);
		// 设置popupWindow的布局
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度
		listDetailPW = new PopupWindow(detaillayout, width, height);
		listDetailPW.setFocusable(true);
		listDetailPW.setOutsideTouchable(true);
		
		currCpID = getIntent().getStringExtra("cp_ID");
		init();
		trackHelper = new TrackHelper();
		loadIDPosStatusToListView();
		checkinfo  = getCheckPointInfoByCD(AppContext.trackCheckPointBuffer,currCpID);
		pointDes =checkinfo.getGPSPosition_CD();
		pointType = checkinfo.getCheckPoint_Type();
		if (pointType.equals("P")) {
			pointType = "GPS";
		}
		else {
			pointType = "NFC";
		}
		pointname = checkinfo.getDesc_TX();
		pointnameView.setText(pointname);
		pointTypeView.setText(pointType);
		pointDesView.setText(pointDes);
		rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(GPSPlanListAty.this);
			}
		});
		
		okbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nomalbutton.isChecked()) {
					retx = getString(R.string.gpsplan_normal);
					genXXPlanResult();
					listDetailPW.dismiss();
				}else {
					retx = getString(R.string.gpsplan_abnormal);
					genXXPlanResult();
					UIHelper.showTaskInputbug(GPSPlanListAty.this, currCpID, "POINT");
					//AppManager.getAppManager().finishActivity(GPSPlanListAty.this);			
				}
			}
		});
	/*	if (AppContext.gpsXXPlanBuffer.get(currCpID).size() == 0) {
			UIHelper.ToastMessage(this, "当前考核点无计划");
			return;
		} */
	}

	/**
	 * 初始化控件
	 */
	private void  init() {
		okbutton = (Button) detaillayout.findViewById(R.id.btn_gpsxx_plan_ok);
		closebu = (ImageButton)detaillayout.findViewById(R.id.close_button);
		nomalbutton = (RadioButton)detaillayout.findViewById(R.id.radioture);
		redecsView = (EditText)detaillayout.findViewById(R.id.cp_plan_listitem_detai_planmemo);
		destView = (TextView)detaillayout.findViewById(R.id.cp_plan_listitem_detail_plandesc_value);
		pointnameView= (TextView)findViewById(R.id.cp_plan_listitem_detail_gxxxname);
		pointTypeView =(TextView)findViewById(R.id.cp_plan_listitem_detail_bugtype_value);
		pointDesView = (TextView)findViewById(R.id.cp_desc_value);
		rebutton = (ImageButton) findViewById(R.id.gpsxx_head_Rebutton);
		
		
		
	}
	
	/**
	 * 加载路线下的ID位置到位状态
	 */
	private void loadIDPosStatusToListView() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded
					bindingIDPosStatusData();
				}
			}
		};
		this.loadIDPosStatusThread(false);
	}

	/**
	 * 加载ID位置状态数据的线程
	 */
	private void loadIDPosStatusThread(final boolean isRefresh) {
		loading = new LoadingDialog(this);
		loading.setCancelable(false);
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载ID位置数据
					loadData();
					Thread.sleep(1000);// debug
					msg.what = 1;
					msg.obj = AppContext.gpsXXPlanBuffer;
				} catch (InterruptedException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 加载路线下的ID位置记录
	 */
	private void loadData() {
		// 加载数据
		trackHelper.loadGPSXXPlanData(getApplication(), currCpID);
	}

	private void bindingIDPosStatusData() {
			 List<GPSXXPlan> lists =  new ArrayList<GPSXXPlan>();
	/*	 for (int i = 0; i < 10; i++) {
			 GPSXXPlan plan = new GPSXXPlan();
			 plan.setGpsPointXXItem_ID(String.valueOf(i));
			 plan.setGPSPoint_ID(String.valueOf(i));
			 plan.setGpsXXItem_ID(String.valueOf(i));
			 plan.setName_TX("nfjdsnfnsdfaksl");
			 lists.add(plan);
			 
		}
		lvGPSXXPlanAdapter = new GPSXXPlanAdpter(this,lists,R.layout.listitem_gpsxx_plan);*/
		lvGPSXXPlanAdapter = new GPSXXPlanAdpter(this,
				AppContext.gpsXXPlanBuffer.get(currCpID),
				R.layout.listitem_gpsxx_plan);
		lvGPSXXPlanListView = (ListView) findViewById(R.id.gpsxx_planlist);
		lvGPSXXPlanListView.setAdapter(lvGPSXXPlanAdapter);
		lvGPSXXPlanListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView aTitle = (TextView) view
						.findViewById(R.id.cp_plan_listitem_desc);
				if (aTitle == null) {
					return;
				}
				final GPSXXPlan plan = (GPSXXPlan) aTitle.getTag();

				if (plan == null)
					return;
				planid = plan.getGpsXXItem_ID();
				if (!listDetailPW.isShowing())
					showDetail(plan.getXXContent_TX());
				else {
					listDetailPW.dismiss();
				}

			}
		});
	/*	lvGPSXXPlanListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						TextView viewTitile = (TextView) arg1
								.findViewById(R.id.djidpos_listitem_title);
						DJ_IDPos _idPos = (DJ_IDPos) viewTitile.getTag();
						return true;
					}
				});*/
	}
	
	/**
	 * 生成巡视任务并保存
	 */
	private void  genXXPlanResult() {
		GPSXXPlanResult result = new GPSXXPlanResult();
		result.setAppuser_ID(AppContext.getUserID());
		result.setAppUserName_TX(AppContext.getUserName());
		result.setGPSPoint_ID(currCpID);
		result.setGpsXXItem_ID(Integer.parseInt(planid));
		result.setXXResult_TX(retx);
		result.setComplete_TM(DateTimeHelper.getDateTimeNow());
		result.setMemo_TX("");
		if (redecsView.getText() != null) {
			result.setMemo_TX(redecsView.getText().toString());
		}
		GPSPositionHelper.GetIntance().insertXXPlan(this,result);
		
	}
	
	/**
	 * 显示任务详细信息
	 * 
	 * @param marker
	 */
	private void showDetail(String text) {
		View parentView = findViewById(R.id.layout_gpsxx_head);
		//View parentView = findViewById(R.id.layout_cp_detail);
		destView.setText(text);
		closebu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				listDetailPW.dismiss();
			}
		});
		listDetailPW.showAsDropDown(parentView, 0, 0); // 设置在屏幕中的显示位置
	}
	
	private CheckPointInfo getCheckPointInfoByCD(
			List<CheckPointInfo> arrayList, String ID) {
		CheckPointInfo checkPointInfo = new CheckPointInfo();
		if (arrayList == null || arrayList.isEmpty()) {
			return null;
		}
		Iterator<CheckPointInfo> iter = arrayList.iterator();
		while (iter.hasNext()) {
			CheckPointInfo _cpInfo = iter.next();
			if (String.valueOf(_cpInfo.getGPSPosition_ID()).equals(ID)) {
				checkPointInfo = _cpInfo.Copy();
				return checkPointInfo;
			}
		}
		return null;
	}
}
