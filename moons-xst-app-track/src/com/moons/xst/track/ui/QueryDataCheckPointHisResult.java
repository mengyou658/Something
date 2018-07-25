package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.Query_hisdataAdapter;
import com.moons.xst.track.bean.GPSPositionForResultHis;
//import butterknife.Bind;
//import butterknife.ButterKnife;

public class QueryDataCheckPointHisResult extends BaseActivity {
	
	private Query_hisdataAdapter adapter;
	private List<GPSPositionForResultHis> positionForResults = new ArrayList<GPSPositionForResultHis>();
	
	private ImageButton rebutton;
	private ListView idposhisresultlv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_data_idpos_hisresult);
		
		rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(QueryDataCheckPointHisResult.this);
			}
		});
		idposhisresultlv = (ListView) findViewById(R.id.query_data_idpos_hisresult_listview);
		
		String checkpointid = getIntent().getStringExtra("checkpointid");
		
		positionForResults = GPSPositionHelper.GetIntance().QueryGPSTResultHis(
				QueryDataCheckPointHisResult.this, Integer.parseInt(checkpointid));
		adapter = new Query_hisdataAdapter(this, positionForResults,
				R.layout.listitem_hisdata);
		idposhisresultlv.setAdapter(adapter);
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
              yMove= event.getRawY();
              //滑动的距离
              int distanceX = (int) (xMove - xDown);
              int distanceY= (int) (yMove - yDown);
              //获取顺时速度
              int ySpeed = getScrollVelocity();
              //关闭Activity需满足以下条件：
              //1.点击屏幕时，x轴坐标必须小于15,也就是从屏幕最左边开始
              //2.x轴滑动的距离>XDISTANCE_MIN
              //3.y轴滑动的距离在YDISTANCE_MIN范围内
              //4.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
              if(xDown < 15 && distanceX > XDISTANCE_MIN &&(distanceY<YDISTANCE_MIN&&distanceY>-YDISTANCE_MIN)&& ySpeed < YSPEED_MIN) {
                    AppManager.getAppManager().finishActivity(QueryDataCheckPointHisResult.this);
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