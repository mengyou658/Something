package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJQuery_hisdataAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_TaskIDPosHis;
import com.moons.xst.track.bean.XJ_TaskIDPosHis;

public class QueryDataIDPosHisResult extends BaseActivity {
	
	List<XJ_TaskIDPosHis> idhislist = new ArrayList<XJ_TaskIDPosHis>();
	private DJQuery_hisdataAdapter hisadapter;
	
	private ImageButton rebutton;
	private TextView tvidposname;
	private ListView idposhisresultlv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_data_idpos_hisresult);
		
//		ButterKnife.bind(this);
		
		String lineid = getIntent().getStringExtra("lineid");
		String idposid = getIntent().getStringExtra("idposid");
		String idposName = getIntent().getStringExtra("idposname");
		
		rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(QueryDataIDPosHisResult.this);
			}
		});
		idposhisresultlv = (ListView) findViewById(R.id.query_data_idpos_hisresult_listview);
		
		idhislist = DJIDPosHelper.GetIntance().loadIDPosHis(
				QueryDataIDPosHisResult.this, Integer.parseInt(lineid),
				idposid);
		
		hisadapter = new DJQuery_hisdataAdapter(this, idhislist,
				R.layout.listitem_hisdata);
		idposhisresultlv.setAdapter(hisadapter);
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
              //1.点击屏幕时，x轴坐标必须大于5小于15,也就是从屏幕最左边开始
              //2.x轴滑动的距离>XDISTANCE_MIN
              //3.y轴滑动的距离在YDISTANCE_MIN范围内
              //4.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
              if(xDown > 5 && xDown < 15 && distanceX > XDISTANCE_MIN &&(distanceY<YDISTANCE_MIN&&distanceY>-YDISTANCE_MIN)&& ySpeed < YSPEED_MIN) {
                    AppManager.getAppManager().finishActivity(QueryDataIDPosHisResult.this);
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