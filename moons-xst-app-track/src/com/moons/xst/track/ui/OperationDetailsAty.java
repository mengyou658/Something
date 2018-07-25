package com.moons.xst.track.ui;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.FragmentViewPagerAdapter;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.ui.operatebill_fragment.DangerousAnalysisFragment;
import com.moons.xst.track.ui.operatebill_fragment.OperateClauseFragment;
import com.moons.xst.track.ui.operatebill_fragment.OperationDetailsFragment;
import com.moons.xst.track.widget.MyViewPager;

public class OperationDetailsAty extends BaseActivity {

	public String Code;

	ImageButton home_head_Rebutton;
	MyViewPager viewpager;
	TextView tv_opreation_bill_no;
	RadioGroup rg_tabPage_state;
	RadioButton rb_tabPage1,rb_tabPage2,rb_tabPage3;
	TextView tv_tab1;

	FragmentViewPagerAdapter adapter;
	
	//上次移动的x坐标
	private int fromX=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listitem_operation_bill);
		Code = getIntent().getStringExtra("Code");
		initView();
	}

	private void initView() {
		rg_tabPage_state=(RadioGroup) findViewById(R.id.rg_tabPage_state);
		tv_tab1=(TextView) findViewById(R.id.tv_tab1);
		viewpager = (MyViewPager) findViewById(R.id.viewpager);

		// tab点击事件
		rb_tabPage1=(RadioButton) findViewById(R.id.rb_tabPage1);
		rb_tabPage2=(RadioButton) findViewById(R.id.rb_tabPage2);
		rb_tabPage3=(RadioButton) findViewById(R.id.rb_tabPage3);
		rb_tabPage1.setOnClickListener(new MyOnClickListener(0));
		rb_tabPage2.setOnClickListener(new MyOnClickListener(1));
		rb_tabPage3.setOnClickListener(new MyOnClickListener(2));

		final ArrayList<Fragment> views = new ArrayList<Fragment>();
		views.add(new OperationDetailsFragment());
		views.add(DangerousAnalysisFragment.instance());
		views.add(new OperateClauseFragment());

		adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
				viewpager, views);

		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		// ViewPager页面切换动画
		viewpager.setPageTransformer(false, new MyViewPager.PageTransformer() {
		    @Override
		    public void transformPage(View page, float position) {
		    	final float normalizedposition = Math.abs(Math.abs(position) - 1);
		        page.setAlpha(normalizedposition);
	        }
		});
		viewpager.setCurrentItem(0);

		// 返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						OperationDetailsAty.this);
			}
		});

		// 编号
		tv_opreation_bill_no = (TextView) findViewById(R.id.tv_opreation_bill_no);
		tv_opreation_bill_no.setText(getString(R.string.operationDetails_number) + Code);
	}

	/**
	 * ViewPager图标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index);
		}
	};

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1==1){
				DangerousAnalysisFragment.instance().viewPause();
			}
		}
	};
	/**
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			//如果当前页不是第二页就停止录音
			if(arg0!=1){
				new Thread() {
					public void run() {
						try {
							//延时0.5秒，等动画执行完再执行，否则动画会卡住
							sleep(500);
							Message msg = new Message();
							msg.arg1 = 1;
							handler.sendMessage(msg);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}.start();
			}
			//如果当前页面是第二页刷新第二页
			if(arg0==1){
				DangerousAnalysisFragment.instance().recordJudge();
			}
			switch (arg0) {
			case 0:
				rb_tabPage1.setEnabled(false);
				rb_tabPage2.setEnabled(true);
				rb_tabPage3.setEnabled(true);
				break;
			case 1:
				rb_tabPage1.setEnabled(true);
				rb_tabPage2.setEnabled(false);
				rb_tabPage3.setEnabled(true);
				break;
			case 2:
				rb_tabPage1.setEnabled(true);
				rb_tabPage2.setEnabled(true);
				rb_tabPage3.setEnabled(false);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if(arg2==0){
				return;
			}
			tvMoveTo(arg0,arg1);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	//tab下面那条线的移动动画
	private void tvMoveTo(int index, float f) {
		RadioButton button = (RadioButton) rg_tabPage_state.getChildAt(index);
		int location[] = new int[2];
		button.getLocationInWindow(location);
		TranslateAnimation animation = new TranslateAnimation(fromX,
				location[0] + f * button.getWidth(), 0, 0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		fromX = (int) (location[0] + f * button.getWidth());
		// 开启动画
		tv_tab1.startAnimation(animation);
		
	}

}