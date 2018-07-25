package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.FragmentViewPagerAdapter;
import com.moons.xst.track.adapter.SelectConditions_UncheckDetailsAdapter;
import com.moons.xst.track.bean.DateEntity;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.ui.queryhisdata_fragment.HisAbsenceFragment;
import com.moons.xst.track.ui.queryhisdata_fragment.HisMobjectStateFragment;
import com.moons.xst.track.ui.queryhisdata_fragment.HisResultFragment;
import com.moons.xst.track.widget.MyViewPager;

public class QueryHisDataAllAty extends BaseActivity {

	private static final int DATE_FUNCTION = 100;// 日期选择回调

	ImageButton home_head_Rebutton;
	MyViewPager viewpager;
	RadioGroup rg_tabPage_hisdata;
	RadioButton rb_hisresult, rb_hisabsence, rb_hismobjectstate;
	RelativeLayout ll_select;// 筛选按钮
	TextView tv_hisresult;
	public String startTime;// 开始时间
	public String endTime;// 结束时间
	public int lineID;// 路线id
	HisResultFragment hisResultFragment;
	HisAbsenceFragment hisAbsenceFragment;
	HisMobjectStateFragment hisMobjectStateFragment;

	private LayoutInflater lm_idposinflater;
	private View lm_idposlayout;
	private FrameLayout fl_content;
	private PopupWindow idposlistPW;
	EditText search_content;// 条件筛选搜索框
	TextView tv_starttime;// 筛选开始时间
	TextView tv_endtime;// 筛选结束时间
	TextView start_week;// 开始星期
	TextView end_week;// 结束星期
	LinearLayout ll_content, ll_idpos;
	LinearLayout ll_screen_time;

	FragmentViewPagerAdapter adapter;
	SelectConditions_UncheckDetailsAdapter mAdapter;
	// 上次移动的x坐标
	private int fromX = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_hisdata_all);

		startTime = DateTimeHelper.DateToString(
				DateTimeHelper.GetDateTimeNow(), "yyyy-MM-dd") + " 00:00:00";
		/*
		 * endTime = DateTimeHelper.DateToString(
		 * DateTimeHelper.AddDays(DateTimeHelper.GetDateTimeNow(), 1),
		 * "yyyy-MM-dd");
		 */
		endTime = DateTimeHelper.DateToString(DateTimeHelper.GetDateTimeNow(),
				"yyyy-MM-dd") + " 23:59:59";
		lineID = getIntent().getIntExtra("line_id", -1);
		initView();
	}

	private void initView() {
		rg_tabPage_hisdata = (RadioGroup) findViewById(R.id.rg_tabPage_hisdata);
		tv_hisresult = (TextView) findViewById(R.id.tv_hisresult);
		viewpager = (MyViewPager) findViewById(R.id.viewpager_hisdata);

		// tab点击事件
		rb_hisresult = (RadioButton) findViewById(R.id.rb_hisresult);
		rb_hisabsence = (RadioButton) findViewById(R.id.rb_hisabsence);
		rb_hismobjectstate = (RadioButton) findViewById(R.id.rb_hismobjectstate);
		rb_hisresult.setOnClickListener(new MyOnClickListener(0));
		rb_hisabsence.setOnClickListener(new MyOnClickListener(1));
		rb_hismobjectstate.setOnClickListener(new MyOnClickListener(2));

		final ArrayList<Fragment> views = new ArrayList<Fragment>();
		hisResultFragment = new HisResultFragment();
		hisAbsenceFragment = new HisAbsenceFragment();
		hisMobjectStateFragment = new HisMobjectStateFragment();
		views.add(hisResultFragment);
		views.add(hisAbsenceFragment);
		views.add(hisMobjectStateFragment);

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
		viewpager.setOffscreenPageLimit(2);

		ll_select = (RelativeLayout) findViewById(R.id.ll_query_hisdata_select);
		ll_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				ll_select.setEnabled(false);
				initPopupwindow();
			}
		});

		// 返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(
						QueryHisDataAllAty.this);
			}
		});
	}

	LinearLayout ll_idpost;
	TextView tv_idpos;

	// 加载popwindow
	private void initPopupwindow() {
		// 获取LayoutInflater实例
		lm_idposinflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 获取弹出菜单的布局
		lm_idposlayout = lm_idposinflater.inflate(R.layout.select_filter, null);
		fl_content = (FrameLayout) lm_idposlayout.findViewById(R.id.fl_content);
		final View view = LayoutInflater.from(this).inflate(
				R.layout.select_time_filter, null);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		ll_idpos = (LinearLayout) view.findViewById(R.id.ll_idpos);
		// 条件筛选Spinner
		ll_idpost = (LinearLayout) view.findViewById(R.id.ll_idpost);
		tv_idpos = (TextView) view.findViewById(R.id.text_idpos);
		showSelectFilter();
		// 筛选搜索框
		search_content = (EditText) view.findViewById(R.id.search_content);

		if (viewpager.getCurrentItem() != 0) {
			ll_content.setVisibility(View.GONE);
			// search_content.setVisibility(View.GONE);
		} else {
			((LinearLayout) view.findViewById(R.id.ll_type))
					.setVisibility(View.VISIBLE);
		}
		// 筛选开始时间
		tv_starttime = (TextView) view.findViewById(R.id.tv_starttime);
		tv_starttime.setText(getString(R.string.date_month_day,
				DateTimeHelper.getMonth(DateTimeHelper.GetDateTimeNow()),
				DateTimeHelper.getDay(DateTimeHelper.GetDateTimeNow())));
		tv_starttime.setTag(startTime);
		// 开始星期
		start_week = (TextView) view.findViewById(R.id.start_week);
		start_week.setText(DateTimeHelper.getWeek(startTime));
		// 结束星期
		end_week = (TextView) view.findViewById(R.id.end_week);
		end_week.setText(DateTimeHelper.getWeek(endTime));
		// 选择时间的LinearLayout
		((LinearLayout) view.findViewById(R.id.ll_screen_time))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						/*
						 * idposlistPW.setAnimationStyle(TRIM_MEMORY_MODERATE);
						 * idposlistPW.update();
						 */
						Intent intent = new Intent(QueryHisDataAllAty.this,
								DateSelectAty.class);
						startActivityForResult(intent, DATE_FUNCTION);
					}
				});
		// 筛选结束时间
		tv_endtime = (TextView) view.findViewById(R.id.tv_endtime);

		tv_endtime.setText(getString(R.string.date_month_day,
				DateTimeHelper.getMonth(DateTimeHelper.GetDateTimeNow()),
				DateTimeHelper.getDay(DateTimeHelper.GetDateTimeNow())));
		tv_endtime.setTag(endTime);
		fl_content.addView(view);

		// 点击确定
		((Button) lm_idposlayout.findViewById(R.id.btn_pos))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						// 筛选输入内容
						String content = search_content.getText().toString();
						// 筛选开始时间
						String sTime = tv_starttime.getTag().toString();
						// 筛选结束时间
						String etime = tv_endtime.getTag().toString();
						// 筛选选中的listview
						List<String> conditions = new ArrayList<String>();

						if (tv_idpos
								.getText()
								.toString()
								.equalsIgnoreCase(
										getString(R.string.query_select_conditions_all))
								&& mAdapter != null) {
							for (int i = 0; i < mAdapter.getlist().size(); i++) {
								conditions.add(mAdapter.getlist().get(i)[0]);
							}
						} else {
							conditions.add(tv_idpos.getText().toString());
						}

						if (viewpager.getCurrentItem() == 0) {
							String type = null;
							if (((RadioButton) view
									.findViewById(R.id.rb_cezhen)).isChecked()) {
								type = AppConst.DJPLAN_DATACODE_CZ;
							} else if (((RadioButton) view
									.findViewById(R.id.rb_observe)).isChecked()) {
								type = AppConst.DJPLAN_DATACODE_GC;
							} else if (((RadioButton) view
									.findViewById(R.id.rb_record)).isChecked()) {
								type = AppConst.DJPLAN_DATACODE_JL;
							} else if (((RadioButton) view
									.findViewById(R.id.rb_cewen)).isChecked()) {
								type = AppConst.DJPLAN_DATACODE_CW;
							} else if (((RadioButton) view
									.findViewById(R.id.rb_zhuansu)).isChecked()) {
								type = AppConst.DJPLAN_DATACODE_CS;
							}
							// 刷新点检结果
							hisResultFragment.screenRefresh(content, sTime,
									etime, conditions, type);
						} else if (viewpager.getCurrentItem() == 1) {
							// 刷新到位数据
							hisAbsenceFragment.screenRefresh(sTime, etime,
									conditions);
						} else if (viewpager.getCurrentItem() == 2) {
							// 刷新设备状态
							hisMobjectStateFragment.screenRefresh(sTime, etime);
						}
						if (idposlistPW.isShowing()) {
							idposlistPW.dismiss();
						}
					}
				});
		// 点击取消
		((Button) lm_idposlayout.findViewById(R.id.btn_neg))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 自动生成的方法存根
						if (idposlistPW.isShowing()) {
							idposlistPW.dismiss();
						}
					}
				});

		// 设置popupWindow的布局
		idposlistPW = new PopupWindow(lm_idposlayout,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		setBackgroundAlpha(this, 0.5f);// 设置屏幕透明度
		// 实例化一个ColorDrawable颜色为半透明
		idposlistPW.setBackgroundDrawable(new BitmapDrawable());
		// idposlistPW.setAnimationStyle(R.style.mypopwindow_anim_style);
		idposlistPW.setFocusable(true);

		idposlistPW.showAtLocation(lm_idposlayout, Gravity.BOTTOM
				| Gravity.LEFT, 0, 0);

		idposlistPW.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				ll_select.setEnabled(true);
				// popupWindow隐藏时恢复屏幕正常透明度
				if (this != null)
					setBackgroundAlpha(QueryHisDataAllAty.this, 1.0f);
			}
		});
	}

	List<String[]> array;
	com.moons.xst.track.widget.AlertDialog dialog;

	// 筛选条件listview
	private void showSelectFilter() {
		if (viewpager.getCurrentItem() == 0) {
			array = hisResultFragment.ResultArray;
		} else if (viewpager.getCurrentItem() == 1) {
			array = hisAbsenceFragment.AbsenceArray;
		} else {
			array = null;
		}
		if(viewpager.getCurrentItem()==2){
			ll_idpos.setVisibility(View.GONE);
			return;
		}
		//if (array == null || array.size() <= 0) {
		//	ll_content.setVisibility(View.GONE);
		//	ll_idpos.setVisibility(View.GONE);
		//	return;
		//}
		mAdapter = new SelectConditions_UncheckDetailsAdapter(this, array,
				R.layout.listitem_selectfilter_idpos);

		ll_idpost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				LayoutInflater factory = LayoutInflater
						.from(QueryHisDataAllAty.this);
				View view = factory.inflate(R.layout.listview_layout, null);
				ListView listView = (ListView) view.findViewById(R.id.listView);
				// 如果超过4条，重新设置LISTVIEW的高度为屏幕的1/2
				if (array.size() >= 7) {
					LinearLayout.LayoutParams params = (LayoutParams) listView
							.getLayoutParams();
					params.height = getWindowManager().getDefaultDisplay()
							.getHeight() / 2;
					listView.setLayoutParams(params);
				}
				listView.setDivider(getResources().getDrawable(
						R.color.graywhite));
				listView.setDividerHeight(1);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO 自动生成的方法存根
						tv_idpos.setText(array.get(position)[0]);
						dialog.dismiss();
					}
				});
				dialog = new com.moons.xst.track.widget.AlertDialog(
						QueryHisDataAllAty.this)
						.builder()
						.setTitle(
								getString(R.string.query_hisdata_select_idpos))
						.setView(view).setButtonGone();
				dialog.show();
			}
		});
	}

	public void setBackgroundAlpha(Activity activity, float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		if (bgAlpha == 1) {
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
		} else {
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 此行代码主要是解决在华为手机上半透明效果无效的bug
		}
		activity.getWindow().setAttributes(lp);
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

	/**
	 * 页卡切换监听(原作者:D.Winter)
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				rb_hisresult.setEnabled(false);
				rb_hisabsence.setEnabled(true);
				rb_hismobjectstate.setEnabled(true);
				break;
			case 1:
				rb_hisresult.setEnabled(true);
				rb_hisabsence.setEnabled(false);
				rb_hismobjectstate.setEnabled(true);
				break;
			case 2:
				rb_hisresult.setEnabled(true);
				rb_hisabsence.setEnabled(true);
				rb_hismobjectstate.setEnabled(false);
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg2 == 0) {
				return;
			}
			tvMoveTo(arg0, arg1);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// tab下面那条线的移动动画
	private void tvMoveTo(int index, float f) {
		RadioButton button = (RadioButton) rg_tabPage_hisdata.getChildAt(index);
		int location[] = new int[2];
		button.getLocationInWindow(location);
		TranslateAnimation animation = new TranslateAnimation(fromX,
				location[0] + f * button.getWidth(), 0, 0);
		animation.setDuration(50);
		animation.setFillAfter(true);
		fromX = (int) (location[0] + f * button.getWidth());
		// 开启动画
		tv_hisresult.startAnimation(animation);

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO 自动生成的方法存根
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null && arg0 == DATE_FUNCTION) {
			@SuppressWarnings("unchecked")
			List<DateEntity> list = (List<DateEntity>) arg2
					.getSerializableExtra("date");
			DateEntity startEntity = list.get(0);
			DateEntity endEntity = list.get(1);
			tv_starttime.setText(startEntity.getMonth()
					+ getString(R.string.date_month) + startEntity.getDay()
					+ getString(R.string.date_day));
			tv_starttime.setTag(startEntity.getYear() + "-"
					+ startEntity.getMonth() + "-" + startEntity.getDay()
					+ " 00:00:00");
			start_week.setText(startEntity.getWeek());

			tv_endtime.setText(endEntity.getMonth()
					+ getString(R.string.date_month) + endEntity.getDay()
					+ getString(R.string.date_day));
			tv_endtime.setTag(endEntity.getYear() + "-" + endEntity.getMonth()
					+ "-" + endEntity.getDay() + " 23:59:59");
			end_week.setText(endEntity.getWeek());
		}
	}
}