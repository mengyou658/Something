package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.buss.DJPlan;
import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJQueryPlan_LvAdpter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.UIHelper;

public class DJQueryDatapoPlanNew extends BaseActivity {

	TextView titleText;
	TextView lineText;
	TextView idposText;
	RelativeLayout llLine;
	ImageView lineTypeImageView;
	ImageButton returnButton;
	ListView planListView;

	private DJLine djline;
	private int djLineID;
	private String djLineName;
	private String pointName;
	private int gpsPositionID;

	private DJQueryPlan_LvAdpter adapter;
	private List<DJPlan> djPlanList = new ArrayList<DJPlan>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_data_poplan_new);
		init();
		initData();
	}

	private void init() {
		titleText = (TextView) findViewById(R.id.query_data_poplan_head_title);
		lineText = (TextView) findViewById(R.id.query_data_polplan_line_Value);
		idposText = (TextView) findViewById(R.id.query_data_polplan_idpos_Value);
		llLine = (RelativeLayout) findViewById(R.id.ll_query_data_poplan_line);
		lineTypeImageView = (ImageView) findViewById(R.id.djline_type_icon);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		planListView = (ListView) findViewById(R.id.query_data_listview);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		djline = (DJLine) (getIntent().getSerializableExtra("djlineinfo"));
		djLineID = djline.getLineID();
		djLineName = djline.getLineName();
		pointName = getIntent().getStringExtra("point_name");
		gpsPositionID = Integer.parseInt(getIntent().getStringExtra(
				"position_ID"));
		titleText.setText(R.string.query_data_djpoplan_title);
		lineText.setText(djLineName);
		int linetypePic = R.drawable.widget_bar_xj_line;
		switch (djline.getlineType()) {
		case 0:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		case 1:
			linetypePic = R.drawable.widget_bar_djpc_line_xh;
			break;
		case 2:
			linetypePic = R.drawable.widget_bar_xg_line;
			break;
		case 3:
			linetypePic = R.drawable.widget_bar_jm_line;
			break;
		case 4:
			linetypePic = R.drawable.widget_bar_xs_line_xh;
			break;
		case 5:
			linetypePic = R.drawable.widget_bar_sis_line;
			break;
		case 6:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 7:
			linetypePic = R.drawable.widget_bar_xx_line_xh;
			break;
		case 8:
			linetypePic = R.drawable.widget_bar_case_line_xh;
			break;
		default:
			linetypePic = R.drawable.widget_bar_xj_line_xh;
			break;
		}
		lineTypeImageView.setImageResource(linetypePic);
		idposText.setText(pointName);
		djPlanList = DJPlanHelper.GetIntance().loadPlanData(
				DJQueryDatapoPlanNew.this, String.valueOf(gpsPositionID),
				djLineID);
		adapter = new DJQueryPlan_LvAdpter(this, djPlanList,
				R.layout.listitem_djplan, djline.getlineType());
		planListView.setAdapter(adapter);

		planListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView aTitle = (TextView) view
						.findViewById(R.id.djplan_listitem_title);
				if (aTitle == null) {
					return;
				}
				// final DJPlan plan = (DJPlan) aTitle.getTag();
				final DJPlan plan = (DJPlan) adapter.getItem(position);

				if (plan == null)
					return;

				UIHelper.showQuerydataHisResult(view.getContext(), String
						.valueOf(djline.getLineID()), plan.getDJPlan()
						.getDJ_Plan_ID(), plan.getDJPlan().getPlanDesc_TX(),
						plan.getDJPlan().getDataType_CD());
			}
		});
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						DJQueryDatapoPlanNew.this);
			}
		});
	}

	/**
	 * 滑动退回上一界面
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			yDown = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			// 滑动的距离
			int distanceX = (int) (xMove - xDown);
			int distanceY = (int) (yMove - yDown);
			// 获取顺时速度
			int ySpeed = getScrollVelocity();
			// 关闭Activity需满足以下条件：
			// 1.点击屏幕时，x轴坐标必须小于15,也就是从屏幕最左边开始
			// 2.x轴滑动的距离>XDISTANCE_MIN
			// 3.y轴滑动的距离在YDISTANCE_MIN范围内
			// 4.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
			if (xDown < 15
					&& distanceX > XDISTANCE_MIN
					&& (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN)
					&& ySpeed < YSPEED_MIN) {
				AppManager.getAppManager().finishActivity(
						DJQueryDatapoPlanNew.this);
			}
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}
}
