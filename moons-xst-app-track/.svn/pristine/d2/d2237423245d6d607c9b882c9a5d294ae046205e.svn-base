package com.moons.xst.track.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Z_DJ_Plan;
import com.moons.xst.track.common.StringUtils;

public class DianjianPlanDetail extends BaseActivity {
	
	private TextView tv_plandetail_plancontent;
	private TextView tv_plandetail_standard;
	private TextView tv_plandetail_cycle;
	private TextView tv_plandetail_checkmethod;
	private TextView tv_plandetail_unit;
	private TextView tv_plandetail_alarmtype;
	private TextView tv_plandetail_standardvalue;
	private TextView tv_plandetail_lc;
	
	private TextView tv_plandetail_upper1;
	private TextView tv_plandetail_upper2;
	private TextView tv_plandetail_upper3;
	private TextView tv_plandetail_upper4;
	private TextView tv_plandetail_lower1;
	private TextView tv_plandetail_lower2;
	private TextView tv_plandetail_lower3;
	private TextView tv_plandetail_lower4;
	
	private ImageButton reButton;
	
	private Z_DJ_Plan mDJPlan = new Z_DJ_Plan();
	private String mCycleName = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianjian_plan_detail);
		
		if (savedInstanceState != null) {
			// 这个是之前保存的数据
			mDJPlan = (Z_DJ_Plan)(savedInstanceState.getSerializable("currentDJPlan"));
			mCycleName = savedInstanceState.getString("cycleName");
		} else {
			// 这个是从另外一个界面进入这个时传入的
			mDJPlan = (Z_DJ_Plan)(getIntent().getSerializableExtra("currentDJPlan"));	
			mCycleName = getIntent().getStringExtra("cycleName");
		}
		init();
		loadData();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO
		super.onSaveInstanceState(outState);
		outState.putSerializable("currentDJPlan", mDJPlan);
		outState.putString("cycleName", mCycleName);
	}
	
	private void init() {
		tv_plandetail_plancontent = (TextView) findViewById(R.id.tv_plandetail_plancontent);
		tv_plandetail_standard = (TextView) findViewById(R.id.tv_plandetail_standard);
		tv_plandetail_cycle = (TextView) findViewById(R.id.tv_plandetail_cycle);
		tv_plandetail_checkmethod = (TextView) findViewById(R.id.tv_plandetail_checkmethod);
		tv_plandetail_unit = (TextView) findViewById(R.id.tv_plandetail_unit);
		tv_plandetail_alarmtype = (TextView) findViewById(R.id.tv_plandetail_alarmtype);
		tv_plandetail_standardvalue = (TextView) findViewById(R.id.tv_plandetail_standardvalue);
		tv_plandetail_lc = (TextView) findViewById(R.id.tv_plandetail_lc);
		
		tv_plandetail_upper1 = (TextView) findViewById(R.id.tv_plandetail_upper1);
		tv_plandetail_upper2 = (TextView) findViewById(R.id.tv_plandetail_upper2);
		tv_plandetail_upper3 = (TextView) findViewById(R.id.tv_plandetail_upper3);
		tv_plandetail_upper4 = (TextView) findViewById(R.id.tv_plandetail_upper4);
		tv_plandetail_lower1 = (TextView) findViewById(R.id.tv_plandetail_lower1);
		tv_plandetail_lower2 = (TextView) findViewById(R.id.tv_plandetail_lower2);
		tv_plandetail_lower3 = (TextView) findViewById(R.id.tv_plandetail_lower3);
		tv_plandetail_lower4 = (TextView) findViewById(R.id.tv_plandetail_lower4);
		
		reButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		reButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(DianjianPlanDetail.this);
			}
		});
	}
	
	private void loadData() {
		// 计划内容
		tv_plandetail_plancontent.setText(mDJPlan.getPlanDesc_TX());
		// 判断标准
		tv_plandetail_standard.setText(mDJPlan.getESTStandard_TX());
		// 周期
		tv_plandetail_cycle.setText(mCycleName);
		// 检查方法
		tv_plandetail_checkmethod.setText(getCheckMethod());
		// 单位
		tv_plandetail_unit.setText((StringUtils.isEmpty(mDJPlan
						.getMetricUnit_TX()) ? "--" : mDJPlan
						.getMetricUnit_TX()));
		// 报警类型
		String almType = "";
		if (mDJPlan.getAlarmType_ID() != null) {
			String overlapNR = "";
			if (mDJPlan.getAlarmType_ID() == 0)
				almType = getString(R.string.plan_detail_upperwarning);
			else if (mDJPlan.getAlarmType_ID() == 1)
				almType = getString(R.string.plan_detail_lowerwarning);
			else if (mDJPlan.getAlarmType_ID() == 2)
				almType = getString(R.string.plan_detail_innerwarning);
			else if (mDJPlan.getAlarmType_ID() == 3)
				almType = getString(R.string.plan_detail_outterwarning);
			else {
				if (mDJPlan.getOverlap_NR() != null)
					overlapNR = String.valueOf(mDJPlan
							.getOverlap_NR());
				almType = getString(R.string.plan_detail_diffwarning, overlapNR);
			}
		}
		tv_plandetail_alarmtype.setText(almType);
		// 典型值
		tv_plandetail_standardvalue.setText(StringUtils.isEmpty(mDJPlan
				.getStandardValue_TX()) ? "--" : mDJPlan
				.getStandardValue_TX());
		// 量程范围
		String lcString = "";
		String pLower = StringUtils.isEmpty(mDJPlan
				.getParmLowerLimit_TX()) ? "--" : mDJPlan
				.getParmLowerLimit_TX();
		String pUpper = StringUtils.isEmpty(mDJPlan
				.getParmUpperLimit_TX()) ? "--" : mDJPlan
				.getParmUpperLimit_TX();
		if (!StringUtils.isEmpty(pLower) || !StringUtils.isEmpty(pUpper))
			lcString = pLower + " ~ " + pUpper;
		tv_plandetail_lc.setText(lcString);
		// 报警上下限
		String upperString1 = "--", downString1 = "--";
		if (!StringUtils.isEmpty(mDJPlan.getUpperLimit1_TX())) {
			upperString1 = mDJPlan.getUpperLimit1_TX();
		}
		if (!StringUtils.isEmpty(mDJPlan.getLowerLimit1_TX())) {
			downString1 = mDJPlan.getLowerLimit1_TX();
		}
		tv_plandetail_upper1.setText(getString(R.string.plan_alarmlevel_Pre_Alarm) + 
				getString(R.string.plan_detail_upper) + upperString1);
		tv_plandetail_lower1.setText(getString(R.string.plan_alarmlevel_Pre_Alarm) +
				getString(R.string.plan_detail_lower) + downString1);
		
		String upperString2 = "--", downString2 = "--";
		if (!StringUtils.isEmpty(mDJPlan.getUpperLimit2_TX())) {
			upperString2 = mDJPlan.getUpperLimit2_TX();
		}
		if (!StringUtils.isEmpty(mDJPlan.getLowerLimit2_TX())) {
			downString2 = mDJPlan.getLowerLimit2_TX();
		}
		tv_plandetail_upper2.setText(getString(R.string.plan_alarmlevel_Warning) +
				getString(R.string.plan_detail_upper) + upperString2);
		tv_plandetail_lower2.setText(getString(R.string.plan_alarmlevel_Warning) +
				getString(R.string.plan_detail_lower) + downString2);
		
		String upperString3 = "--", downString3 = "--";
		if (!StringUtils.isEmpty(mDJPlan.getUpperLimit3_TX())) {
			upperString3 = mDJPlan.getUpperLimit3_TX();
		}
		if (!StringUtils.isEmpty(mDJPlan.getLowerLimit3_TX())) {
			downString3 = mDJPlan.getLowerLimit3_TX();
		}
		tv_plandetail_upper3.setText(getString(R.string.plan_alarmlevel_Pre_Alert) +
				getString(R.string.plan_detail_upper) + upperString3);
		tv_plandetail_lower3.setText(getString(R.string.plan_alarmlevel_Pre_Alert) +
				getString(R.string.plan_detail_lower) + downString3);
		
		String upperString4 = "--", downString4 = "--";
		if (!StringUtils.isEmpty(mDJPlan.getUpperLimit4_TX())) {
			upperString4 = mDJPlan.getUpperLimit4_TX();
		}
		if (!StringUtils.isEmpty(mDJPlan.getLowerLimit4_TX())) {
			downString4 = mDJPlan.getLowerLimit4_TX();
		}
		tv_plandetail_upper4.setText(getString(R.string.plan_alarmlevel_Pre_Dangerous) +
				getString(R.string.plan_detail_upper) + upperString4);
		tv_plandetail_lower4.setText(getString(R.string.plan_alarmlevel_Pre_Dangerous) +
				getString(R.string.plan_detail_lower) + downString4);
	}
	
	/**
	 * 解析检查方法
	 * 
	 * @return
	 */
	private String getCheckMethod() {
		String res = "";
		int checkmethod = mDJPlan.getCheckMethod();
		String temp = String.valueOf(checkmethod);
		int length = temp.length();
		if (length < 8) {
			for (int i = 0; i < 8 - length; i++) {
				temp = "0" + temp;
			}
		}// { 目视、手摸、解体、其它、精密、嗅觉、敲打、听音 };
		if (temp.substring(0, 1).equals("1"))
			res += getString(R.string.plan_checkmethod_ms);
		if (temp.substring(1, 2).equals("1"))
			res += getString(R.string.plan_checkmethod_sm);
		if (temp.substring(2, 3).equals("1"))
			res += getString(R.string.plan_checkmethod_jt);
		if (temp.substring(3, 4).equals("1"))
			res += getString(R.string.plan_checkmethod_qt);
		if (temp.substring(4, 5).equals("1"))
			res += getString(R.string.plan_checkmethod_jm);
		if (temp.substring(5, 6).equals("1"))
			res += getString(R.string.plan_checkmethod_xj);
		if (temp.substring(6, 7).equals("1"))
			res += getString(R.string.plan_checkmethod_qd);
		if (temp.substring(7, 8).equals("1"))
			res += getString(R.string.plan_checkmethod_ty);
		return res;
	}
}