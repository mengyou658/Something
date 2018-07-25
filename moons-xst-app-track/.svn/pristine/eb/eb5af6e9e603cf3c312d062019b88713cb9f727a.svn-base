package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OverhaulPlanAdapter;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.OverhaulPlan;
import com.moons.xst.track.bean.OverhaulProject;
import com.moons.xst.track.bean.OverhaulUser;

import de.greenrobot.event.EventBus;

public class OverhaulPlanActivity extends BaseActivity implements
		OnClickListener {
	EditText search_content;
	private ImageButton returnButton;
	private OverhaulHelper overhaulHelper;
	private ListView lv_overhaulPlan;
	private OverhaulPlanAdapter overhaulPlanAdapter;
	private List<Integer> planIDList = new ArrayList<Integer>();
	private ImageButton btn_serach;
	private List<OverhaulPlan> list;
	public boolean isFinished = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overhaul_plan);
		EventBus.getDefault().register(this);
		Bundle bundle = this.getIntent().getExtras();
		OverhaulUser overhaulUser = (OverhaulUser) bundle
				.getSerializable("overhaulUser");
		int dept_ID = overhaulUser.getDEPT_ID() == null ? -1 : overhaulUser
				.getDEPT_ID();
		overhaulHelper = OverhaulHelper.getInstance();
		lv_overhaulPlan = (ListView) findViewById(R.id.overhaul_plan_listview);
		loadDataThread(dept_ID);
		lv_overhaulPlan.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goProjectAty((OverhaulPlan) overhaulPlanAdapter
						.getItem(position));
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(this);
		btn_serach = (ImageButton) findViewById(R.id.btn_serach_title);
		btn_serach.setOnClickListener(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				list = (List<OverhaulPlan>) msg.obj;
				if (list != null) {
					overhaulPlanAdapter = new OverhaulPlanAdapter(
							OverhaulPlanActivity.this, list,
							R.layout.overhaul_list_item);
					lv_overhaulPlan.setAdapter(overhaulPlanAdapter);
				}
			}
		};
	};

	/**
	 * 加载检修计划
	 * 
	 * @param overhaulPlanActivity
	 * @param dept_ID
	 */
	private void loadDataThread(final int dept_ID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				overhaulHelper.OverhaulProjectList(OverhaulPlanActivity.this,
						dept_ID);// 获取所有的检修项目
				String planIDStr = "";
				StringBuilder sb = new StringBuilder();
				if (AppContext.overhaulProjectBuffer != null
						&& AppContext.overhaulProjectBuffer.size() > 0) {

					for (int i = 0; i < AppContext.overhaulProjectBuffer.size(); i++) {
						if(StringUtils.isEmpty(AppContext.overhaulProjectBuffer.get(i).getFinish_YN())){
							AppContext.overhaulProjectBuffer.get(i).setFinish_YN("0");
						}
						if (planIDList
								.contains(AppContext.overhaulProjectBuffer.get(
										i).getJXPlan_ID())) {
							continue;
						} else {
							planIDList.add(AppContext.overhaulProjectBuffer
									.get(i).getJXPlan_ID());
							sb.append(String.valueOf(AppContext.overhaulProjectBuffer.get(i)
									.getJXPlan_ID()));
							sb.append(",");
						}
					}
					planIDStr = sb.toString().substring(0, sb.length() - 1);
				}

				overhaulHelper.OverhaulPlanList(OverhaulPlanActivity.this,
						planIDStr);// 获取所有的检修计划
				if (AppContext.overhaulPlanBuffer != null
						&& AppContext.overhaulPlanBuffer.size() > 0) {
					for (OverhaulPlan overhaulPlan : AppContext.overhaulPlanBuffer) {
						boolean flag = true;
						List<OverhaulProject> orderedProjects = overhaulHelper
								.getOverhaulProjectListByPlanID(
										OverhaulPlanActivity.this,
										overhaulPlan.getJXPlan_ID());
						for (OverhaulProject overhaulProject : orderedProjects) {
							if (StringUtils.isEmpty(overhaulProject.getFinish_YN())||"0".equals(overhaulProject.getFinish_YN())) {
								flag = false;
								overhaulPlan.setisPlanFinished(flag);
								break;
							}
							overhaulPlan.setisPlanFinished(flag);
						}
					}
				}
				Message msg = handler.obtainMessage();
				msg.obj = AppContext.overhaulPlanBuffer;
				msg.what = 1;
				msg.sendToTarget();
			}
		}).start();
	}

	// 搜索回调事件
	public void onEvent(OverhaulPlan entity) {
		goProjectAty(entity);
	}

	public void goProjectAty(OverhaulPlan overhaulPlan) {
		Intent intent = new Intent(OverhaulPlanActivity.this,
				OverhaulProjectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("overhaulPaln", overhaulPlan);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_serach_title:
			CommonSearchBean<OverhaulPlan> commonSearchBean = new CommonSearchBean<OverhaulPlan>();
			commonSearchBean.setList(AppContext.overhaulPlanBuffer);
			commonSearchBean.setSearchType(AppConst.SearchType.OverhaulPlan
					.toString());
			commonSearchBean
					.setHint(getString(R.string.search_overhaulplan_hint_message));
			Intent intent = new Intent(OverhaulPlanActivity.this,
					UnifySearchAty.class);
			intent.putExtra("seachBean", (Serializable) commonSearchBean);
			startActivity(intent);
			break;
		case R.id.home_head_Rebutton:
			AppManager.getAppManager()
					.finishActivity(OverhaulPlanActivity.this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		overhaulPlanAdapter.refresh(list);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
