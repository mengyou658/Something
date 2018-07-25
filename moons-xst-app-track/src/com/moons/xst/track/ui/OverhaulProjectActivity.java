package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.OverhaulHelper;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.OverhaulProjectAdapter;
import com.moons.xst.track.bean.CommonSearchBean;
import com.moons.xst.track.bean.OverhaulPlan;
import com.moons.xst.track.bean.OverhaulProject;

import de.greenrobot.event.EventBus;

public class OverhaulProjectActivity extends BaseActivity implements
		OnClickListener {

	private ImageButton returnButton;
	private OverhaulHelper overhaulHelper;
	private ListView lv_overhaulProject;
	private OverhaulProjectAdapter overhaulProjectAdapter;
	private ImageButton btn_searchContent;
	private static final int REQUESTCODE = 250;
	private static final int RESULTCODE = 200;
	private int plan_ID;
	private OverhaulPlan overhaulPlan;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overhaul_project);
		Bundle bundle = this.getIntent().getExtras();
		overhaulPlan = (OverhaulPlan) bundle
				.getSerializable("overhaulPaln");
		plan_ID = overhaulPlan.getJXPlan_ID();
		overhaulHelper = OverhaulHelper.getInstance();
		EventBus.getDefault().register(this);
		AppContext.overhaulProjectBufferSelect = overhaulHelper.getOverhaulProjectListByPlanID(
				this, plan_ID);

		lv_overhaulProject = (ListView) findViewById(R.id.overhaul_project_listview);
		overhaulProjectAdapter = new OverhaulProjectAdapter(
				OverhaulProjectActivity.this, AppContext.overhaulProjectBufferSelect,
				R.layout.overhaul_project_list_item);
		lv_overhaulProject.setAdapter(overhaulProjectAdapter);
		lv_overhaulProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OverhaulProject mOverhaulProject = (OverhaulProject) overhaulProjectAdapter
						.getItem(position);
				chooseActivityToGo(mOverhaulProject);
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(this);
		btn_searchContent = (ImageButton) findViewById(R.id.btn_serach_title);
		btn_searchContent.setOnClickListener(this);
	}


	private void chooseActivityToGo(OverhaulProject overhaulProject) {
		if (StringUtils.isEmpty(overhaulProject.getQC0())) {
			goToSelectUI(OverhaulProjectImplementationAty.class,
					overhaulProject);
		} else {
			goToSelectUI(OverhaulProjectDetailAty.class, overhaulProject);
		}
	}

	private void goToSelectUI(Class<?> activity, OverhaulProject overhaulProject) {
		Intent intent = new Intent(OverhaulProjectActivity.this, activity);
		Bundle bundle = new Bundle();
		bundle.putSerializable("overhaulProject", overhaulProject);
		intent.putExtras(bundle);
		startActivityForResult(intent, REQUESTCODE);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_serach_title:
			// 搜索按钮点击事件
			CommonSearchBean<OverhaulProject> commonSearchBean = new CommonSearchBean<OverhaulProject>();
			commonSearchBean.setList(AppContext.overhaulProjectBufferSelect);
			commonSearchBean.setSearchType(AppConst.SearchType.OverhaulProject
					.toString());
			commonSearchBean
					.setHint(getString(R.string.search_overhaulproject_hint_message));
			Intent intent = new Intent(OverhaulProjectActivity.this,
					UnifySearchAty.class);
			intent.putExtra("seachBean", (Serializable) commonSearchBean);
			startActivity(intent);

			break;
		case R.id.home_head_Rebutton:
			AppManager.getAppManager().finishActivity(
					OverhaulProjectActivity.this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUESTCODE && resultCode == RESULTCODE) {
				Bundle extras = data.getExtras();
				OverhaulProject currentItem = (OverhaulProject) extras
						.getSerializable("detailProject");
				for (OverhaulProject overhaulProjectInBuffer : AppContext.overhaulProjectBuffer) {
					if(overhaulProjectInBuffer.getWorkOrderForHNYN_ID()==currentItem.getWorkOrderForHNYN_ID()){
						overhaulProjectInBuffer.setState(currentItem.getState());
						overhaulProjectInBuffer
						.setCurrentQC(currentItem.getCurrentQC());
						overhaulProjectInBuffer.setQC0(currentItem.getQC0());
						overhaulProjectInBuffer.setQC0_TX(currentItem.getQC0_TX());
						overhaulProjectInBuffer.setQC0_MemoTX(currentItem
								.getQC0_MemoTX());
						overhaulProjectInBuffer.setQC1(currentItem.getQC1());
						overhaulProjectInBuffer.setQC1_TX(currentItem.getQC1_TX());
						overhaulProjectInBuffer.setQC1_MemoTX(currentItem
								.getQC1_MemoTX());
						overhaulProjectInBuffer.setQC2(currentItem.getQC2());
						overhaulProjectInBuffer.setQC2_TX(currentItem.getQC2_TX());
						overhaulProjectInBuffer.setQC2_MemoTX(currentItem
								.getQC2_MemoTX());
						overhaulProjectInBuffer.setQC3(currentItem.getQC3());
						overhaulProjectInBuffer.setQC3_TX(currentItem.getQC3_TX());
						overhaulProjectInBuffer.setQC3_MemoTX(currentItem
								.getQC3_MemoTX());
						overhaulProjectInBuffer
						.setFinish_YN(currentItem.getFinish_YN());
					}
				}
			}
		}
	}

	public void onEvent(OverhaulProject currentItem) {
		chooseActivityToGo(currentItem);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		overhaulProjectAdapter.refresh(AppContext.overhaulProjectBufferSelect);
	}
	@Override
	protected void onPause() {
		super.onPause();
		boolean flag=true;
		for (OverhaulProject overhaulProject : AppContext.overhaulProjectBufferSelect) {
			if(StringUtils.isEmpty(overhaulProject.getFinish_YN())||"0".equals(overhaulProject.getFinish_YN())){
				flag=false;
				overhaulPlan.setisPlanFinished(flag);
				break;
			}
			overhaulPlan.setisPlanFinished(flag);
		}
		for (OverhaulPlan overhaulPlanInBuffer : AppContext.overhaulPlanBuffer) {
			if(overhaulPlan.getJXPlan_ID()==overhaulPlanInBuffer.getJXPlan_ID()){
				overhaulPlanInBuffer.setisPlanFinished(overhaulPlan.getisPlanFinished());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
