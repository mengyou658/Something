package com.moons.xst.track.ui;


import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJLine_ListViewAdapter;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.common.FileUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.dao.DJLineDAO;
import com.moons.xst.track.widget.LoadingDialog;

import de.greenrobot.event.EventBus;

/**
 * 数据查询(路线选择)
 * 
 * @version 1.0
 * @created 2014-9-21
 */
public class QueryDjLine extends BaseActivity {
	
	public static QueryDjLine instance = null;
	
	private AppContext appContext;// 全局Context
	private String currentQueryType = "";
	private DJLine_ListViewAdapter lvDJLineAdapter;
	private ListView  lvDJLine;
	private ImageButton reBtn,searchBtn;

	LoadingDialog loading;
	Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_djline);
		appContext = (AppContext) getApplication();
		EventBus.getDefault().register(this);
		if (savedInstanceState != null) {
			currentQueryType = savedInstanceState.getString("currentQueryType");
		} else {
			currentQueryType = getIntent().getStringExtra("queryType");
		}

		reBtn = (ImageButton) findViewById(R.id.home_head_Rebutton);
		reBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				back();
			}
		});
		searchBtn = (ImageButton) findViewById(R.id.query_djline_serach);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				// 搜索按钮点击事件
				CommonSearchBean<DJLine> commonSearchBean = new CommonSearchBean<DJLine>();
				commonSearchBean.setList(AppContext.DJLineBuffer);
				commonSearchBean.setSearchType(AppConst.SearchType.QueryDJLine
						.toString());
				commonSearchBean
						.setHint(getString(R.string.search_djline_hint_message));
				Intent intent = new Intent(QueryDjLine.this,
						UnifySearchAty.class);
				intent.putExtra("seachBean", (Serializable) commonSearchBean);
				startActivity(intent);
			}
		});
		
		File file = new File(AppConst.XSTBasePath()
				+ AppConst.DJLineXmlFile);
		if (file.exists()) {
			if (AppContext.getCheckInAfterEnterLine())
				loadAllDJLine();
			else {
				if (AppContext.isLogin()) {
					loadUserDJLine();
				}
			}
		}
		instance = this;	
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
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onRestoreInstanceState(savedInstanceState);
		savedInstanceState.putString("currentQueryType", currentQueryType);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 根据登录的人员信息刷新相关数据
	 */
	private void loadUserDJLine() {

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					// disp data loaded

					bindingUserDJLine();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(QueryDjLine.this);
				}
			}
		};
		this.loadUserDJLineThread(false);
	}
	
	private void loadUserDJLineThread(final boolean isRefresh) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.show();
		}

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载路线
					loadMyDJLine();
					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
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
	
	private void loadAllDJLine() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					bindingUserDJLine();// 获取的数据绑定到列表中

				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(QueryDjLine.this);
				}
			}
		};
		this.loadAllDJLineThread(false);
	}
	
	private void loadDJLine() {
		AppContext.DJLineBuffer = DJLineDAO.loadAllDJLine();
	}
	
	/**
	 * 初始化路线列表
	 */
	private void loadMyDJLine() {
		AppContext.DJLineBuffer = DJLineDAO.loadDJLineByUser(appContext
				.getLoginUid());
	}
	
	private void loadAllDJLineThread(final boolean isRefresh) {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.show();
		}

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载路线
					loadDJLine();
					Thread.sleep(1000);// debug

					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
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
	
	private void bindingUserDJLine() {
		lvDJLineAdapter = new DJLine_ListViewAdapter(QueryDjLine.this,
				AppContext.DJLineBuffer, R.layout.listitem_djline);

		lvDJLine = (ListView) findViewById(R.id.query_listview_djline);
		if (lvDJLine == null) {
			return;
		}

		lvDJLine.setAdapter(lvDJLineAdapter);

		lvDJLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView aTitle = (TextView) view
						.findViewById(R.id.djline_listitem_title);
				if (aTitle == null) {
					return;
				}
				final DJLine adjline = (DJLine) aTitle.getTag();

				if (adjline == null)
					return;
				
				queryDataByType(QueryDjLine.this, adjline, currentQueryType);
			}
		});
	}
	
	public void onEvent(DJLine currentItem) {
		queryDataByType(QueryDjLine.this, currentItem, currentQueryType);
	}
	
	private void back(){
		/*if (AppContext.DJLineBuffer != null)
			AppContext.DJLineBuffer.clear();*/
		AppManager.getAppManager().finishActivity(this);
		
    }
	
	private void queryDataByType(Context context, final DJLine adjline, String type) {
		if (!FileUtils.checkFileExists(AppConst.XSTDBPath()
				+ AppConst.PlanDBName(adjline.getLineID()))) {
			UIHelper.ToastMessage(QueryDjLine.this,
					R.string.home_line_noplanfile_notice);
			return;
		}
		
		// 漏检统计
		if (type.equalsIgnoreCase(AppConst.QueryType.Uncheck.toString())) {
			if (adjline.getlineType() == AppConst.LineType.XDJ.getLineType()
					|| adjline.getlineType() == AppConst.LineType.DJPC.getLineType()) {
				UIHelper.uncheckQueryByLine(context,
						adjline);
			} else {
				UIHelper.ToastMessage(QueryDjLine.this,
						R.string.query_uncheck_notsupport);
				return;
			}
		}
		// 任务完成情况
		else if(type.equalsIgnoreCase(AppConst.QueryType.TaskResponse.toString())){
			if (adjline.getlineType() == AppConst.LineType.XDJ.getLineType()) {
				UIHelper.showDJQuerydata(context, adjline);
			} else if (adjline.getlineType() == AppConst.LineType.GPSXX.getLineType()
					|| adjline.getlineType() == AppConst.LineType.GPSXXNew.getLineType()) {
				UIHelper.showQuerydata(context, adjline);
			} else if (adjline.getlineType() == AppConst.LineType.CaseXJ.getLineType()) {
				UIHelper.showDJQuerydata(context, adjline);
			}
		}
		// 历史数据查询
		else if (type.equalsIgnoreCase(AppConst.QueryType.QueryHisData.toString())) {
			if(adjline.getlineType() == AppConst.LineType.XDJ.getLineType()
					|| adjline.getlineType() == AppConst.LineType.DJPC.getLineType()
					|| adjline.getlineType() == AppConst.LineType.CaseXJ.getLineType()){
				UIHelper.hisDataQueryByLine(context, adjline.getLineID());
			}else{
				UIHelper.ToastMessage(QueryDjLine.this,
						R.string.query_hisdata_notsupport);
			}
		}
	}
}
