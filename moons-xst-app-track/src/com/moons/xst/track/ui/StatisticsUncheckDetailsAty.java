package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.moons.xst.buss.DJPlanForUnCheck;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.adapter.SelectConditions_UncheckDetailsAdapter;
import com.moons.xst.track.adapter.UncheckDetailsAdapter;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.PinnedHeaderListView;

public class StatisticsUncheckDetailsAty extends BaseActivity {
	
	private ImageButton reButton;
	private RelativeLayout ll_uncheck_details_select;
	private PinnedHeaderListView listview_uncheck_details;
	CommonAdapter mAdapterDetails;
	private PopupWindow idposlistPW;
	private LayoutInflater lm_idposinflater;
	private View lm_idposlayout;
	private ListView lm_idposListView;
	private FrameLayout fl_content;
	private View parentView;
	private Button popup_cancel,popup_ok;
	private List<DJPlanForUnCheck> details = new ArrayList<DJPlanForUnCheck>();
	
	SelectConditions_UncheckDetailsAdapter mAdapter;
	
	LoadingDialog loading;
	
	String[] types = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_uncheck_details);
		
		if (savedInstanceState != null) {
			types = savedInstanceState.getStringArray("serialdata");
		} else {
			Bundle bundle = getIntent().getExtras();
			types = bundle.getStringArray("serialdata");
		}
		
		init();	
		//initPopupwindow();
		loadData(types);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putStringArray("serialdata", types);
	}
	
	private void init() {
		reButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		reButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(StatisticsUncheckDetailsAty.this);
			}
		});
		ll_uncheck_details_select = (RelativeLayout) findViewById(R.id.ll_uncheck_details_select);
		
		listview_uncheck_details = (PinnedHeaderListView) findViewById(R.id.listview_uncheck_details);	
	}
	
	public void openPop(View view) {
		initPopupwindow();
		showSelectFilter();
	}
	
	private void initPopupwindow() {
		// 获取LayoutInflater实例
		lm_idposinflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		lm_idposlayout = lm_idposinflater.inflate(R.layout.select_filter, null);
		fl_content = (FrameLayout) lm_idposlayout
				.findViewById(R.id.fl_content);
		final View view = LayoutInflater.from(StatisticsUncheckDetailsAty.this)
				.inflate(R.layout.select_filter_uncheck,
				null);
		fl_content.addView(view);
		lm_idposListView = (ListView) view
				.findViewById(R.id.select_idpos_list);
		popup_cancel = (Button) lm_idposlayout
				.findViewById(R.id.btn_neg);
		popup_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (idposlistPW.isShowing()) {
					idposlistPW.dismiss();
				}
			}
		});
		popup_ok = (Button) lm_idposlayout
				.findViewById(R.id.btn_pos);
		popup_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				List<String> conditions = new ArrayList<String>();
				for (String[] strs : mAdapter.getlist()) {
					if (strs[0].equalsIgnoreCase(getString(R.string.query_select_conditions_all))
							&& StringUtils.toBool(strs[1])) {
						loadData(types);
						break;
					} else {
						if (StringUtils.toBool(strs[1])) {
							conditions.add(strs[0].toString());
						}
					}
				}
				if (conditions.size() > 0) {
					loadData((String[])conditions.toArray(new String[conditions.size()]));
				}
					
				if (idposlistPW.isShowing()) {
					idposlistPW.dismiss();
				}
			}
		});
		
		// 设置popupWindow的布局
		idposlistPW = new PopupWindow(lm_idposlayout,
		        WindowManager.LayoutParams.MATCH_PARENT,
		        WindowManager.LayoutParams.WRAP_CONTENT);		
		setBackgroundAlpha(this, 0.5f);//设置屏幕透明度
		// 实例化一个ColorDrawable颜色为半透明
		idposlistPW.setBackgroundDrawable(new BitmapDrawable());
		idposlistPW.setAnimationStyle(R.style.mypopwindow_anim_style);
		parentView = findViewById(R.id.statistics_uncheck_details);
		idposlistPW.setFocusable(true);
	}
	
	public void setBackgroundAlpha(Activity activity, float bgAlpha) {	
	    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();  
           lp.alpha = bgAlpha;  
           if (bgAlpha == 1) {  
               activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug  
           } else {  
               activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug  
           }  
        activity.getWindow().setAttributes(lp);
	}
	
	private void loadData(String[] conditions) {
		loadDataThread(conditions);
	}
	
	private void loadDataThread(final String[] conditions) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCancelable(false);
			loading.setCanceledOnTouchOutside(false);
			loading.show();
		}
		
		new Thread() {
			public void run() {
				try {
					getUncheckDetailsData(conditions);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void getUncheckDetailsData(String[] conditions) {
		Message msg = Message.obtain();
		try {
			details.clear();
			for (int i = 0; i < conditions.length; i++) {
				String s = conditions[i].toString();
				for (DJPlanForUnCheck info : AppContext.UnCheckPlanBuffer.get(s)) {
					details.add(info);
				}
			}
			msg.what = 1;
		} catch (Exception e) {
			msg.what = -1;
			msg.obj = e.getMessage();
		}
		loadHandler.sendMessage(msg);
	}
	
	Handler loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				displayData();
			} else if (msg.what == -1) {
				UIHelper.ToastMessage(StatisticsUncheckDetailsAty.this,
						msg.obj.toString(), true);
			}
		};
	};
	
	private void displayData() {
		try {
			// * 创建新的HeaderView，即置顶的HeaderView
	        View HeaderView = getLayoutInflater().inflate(R.layout.listview_item_header, listview_uncheck_details, false);
	        listview_uncheck_details.setPinnedHeader(HeaderView);
	        
	        UncheckDetailsAdapter customAdapter = new UncheckDetailsAdapter(StatisticsUncheckDetailsAty.this,
	        		 details);
	        listview_uncheck_details.setAdapter(customAdapter);
	        
	        listview_uncheck_details.setOnScrollListener(customAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示条件选择POPUPWINDOW
	 */
	private void showSelectFilter() {					
		final List<String[]> array = new ArrayList<String[]>();
		String[] str = null;
		for (int i = 0; i < types.length; i++) {
			str = new String[2];
			str[0] = types[i].toString();
			str[1] = "false";
			array.add(str);
		}
		str = new String[2];
		str[0] = getString(R.string.query_select_conditions_all);
		str[1] = "false";
		array.add(0, str);
		// 如果超过7条，重新设置LISTVIEW的高度为屏幕的1/2
		if (array.size() >= 7) {
			LinearLayout.LayoutParams params = (LayoutParams) lm_idposListView
					.getLayoutParams();
			params.height = getWindowManager()
					.getDefaultDisplay().getHeight() / 2;
			lm_idposListView.setLayoutParams(params);
		}
		
		lm_idposListView.setAdapter(mAdapter = new SelectConditions_UncheckDetailsAdapter(
				StatisticsUncheckDetailsAty.this, array, R.layout.listitem_selectfilter_idpos));
		
		idposlistPW.showAtLocation(parentView, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
		
		idposlistPW.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
            	if (StatisticsUncheckDetailsAty.this != null)
            		setBackgroundAlpha(StatisticsUncheckDetailsAty.this, 1.0f);
            }
        });
		
		lm_idposListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String[] strs = (String[])mAdapter.getItem(position);
				if (strs == null)
					return;
				
				strs[1] = String.valueOf(!StringUtils.toBool(strs[1]));
				if (position == 0) {					
					for (int i = 1; i < mAdapter.getlist().size(); i++) {
						mAdapter.getlist().get(i)[1] = "false";
					}
				} else {
					mAdapter.getlist().get(0)[1] = "false";
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}
}