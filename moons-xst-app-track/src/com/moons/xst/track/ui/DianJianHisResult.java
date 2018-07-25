package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.buss.DJPlanHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;
import com.moons.xst.track.bean.DJ_ResultHis;

public class DianJianHisResult extends BaseActivity {

	private String current_djplanid = "";
	private String lastcompletedt = "";

	List<DJ_ResultHis> list = new ArrayList<DJ_ResultHis>();
	CommonAdapter mAdapter;

	ImageButton returnButton;

	ListView hisresultListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dianjian_hisresult);

		if (savedInstanceState != null) {
			current_djplanid = savedInstanceState.getString("current_djplanid");
			lastcompletedt = savedInstanceState.getString("lastcompletedt");
		} else {
			current_djplanid = getIntent().getStringExtra("djplanid");
			lastcompletedt = getIntent().getStringExtra("lastcompletedt");
		}

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						DianJianHisResult.this);
			}
		});

		hisresultListView = (ListView) findViewById(R.id.lv_hisresult_list);
		list = DJPlanHelper.GetIntance().loadPlanHisDataByPlanID(
				DianJianHisResult.this, AppContext.GetCurrLineID(),
				current_djplanid);
		// hisresultListView.setAdapter(mAdapter = new
		// CommonAdapter<DJ_ResultHis>(
		// DianJianHisResult.this, list, R.layout.listitem_dianjian_hisresult){
		//
		// @Override
		// public void convert(com.moons.xst.track.adapter.ViewHolder helper,
		// DJ_ResultHis item) {
		// // TODO 自动生成的方法存根
		// helper.setText(R.id.result, item.getResult_TX());
		// helper.setText(R.id.djresult_listitem_time,
		// item.getCompleteTime_DT());
		// helper.setText(R.id.djresult_listitem_user,
		// item.getAppUserName_TX());
		// if (lastcompletedt.equals(item.getCompleteTime_DT())) {
		// helper.setImageResource(R.id.image, R.drawable.circle_green);
		// } else {
		// helper.setImageResource(R.id.image, R.drawable.circle_gray);
		// }
		// }
		// });
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AppManager.getAppManager().finishActivity(DianJianHisResult.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
						DianJianHisResult.this);
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