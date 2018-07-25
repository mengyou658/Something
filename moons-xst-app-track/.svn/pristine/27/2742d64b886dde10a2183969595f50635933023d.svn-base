package com.moons.xst.track.ui;

import java.io.Serializable;
import java.util.ArrayList;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.DateEntity;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.calendarview.CalendarView;
import com.moons.xst.track.widget.calendarview.DateBean;
import com.moons.xst.track.widget.calendarview.MonthView;
import com.moons.xst.track.widget.calendarview.listener.OnMonthItemChooseListener;
import com.moons.xst.track.widget.calendarview.listener.OnMonthItemClickListener;
import com.moons.xst.track.widget.calendarview.listener.OnPagerChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DateSelectAty extends BaseActivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7892631651300956006L;

	private CalendarView calendarView;

	RelativeLayout ll_confirm;
	ImageButton home_head_Rebutton;
	TextView title;
	ArrayList<DateEntity> list = new ArrayList<DateEntity>();
	// 加载动画
	LoadingDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_select_layout);
		init();
	}

	private void init() {
		// 返回
		home_head_Rebutton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		home_head_Rebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(DateSelectAty.this);
			}
		});
		// 确定
		ll_confirm = (RelativeLayout) findViewById(R.id.ll_confirm);
		ll_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!MonthView.isClick) {
					UIHelper.ToastMessage(DateSelectAty.this,
							getString(R.string.date_confirm_hint1));
				} else {
					DateEntity entity = new DateEntity();
					entity.setYear(MonthView.clickYear);
					entity.setMonth(transform(MonthView.clickMonth));
					entity.setDay(transform(MonthView.clickDay));
					entity.setWeek(DateTimeHelper.getWeek(MonthView.clickYear
							+ "-" + MonthView.clickMonth + "-"
							+ MonthView.clickDay));
					list.add(entity);

					DateEntity entity1 = new DateEntity();
					if (!MonthView.secondClick) {
						entity1.setYear(MonthView.clickYear);
						entity1.setMonth(transform(MonthView.clickMonth));
						entity1.setDay(transform(MonthView.clickDay));
						entity1.setWeek(DateTimeHelper
								.getWeek(MonthView.clickYear + "-"
										+ MonthView.clickMonth + "-"
										+ MonthView.clickDay));
						list.add(entity1);
					} else {
						entity1.setYear(MonthView.secondYear);
						entity1.setMonth(transform(MonthView.secondMonth));
						entity1.setDay(transform(MonthView.secondDay));
						entity1.setWeek(DateTimeHelper
								.getWeek(MonthView.secondYear + "-"
										+ MonthView.secondMonth + "-"
										+ MonthView.secondDay));
						list.add(entity1);
					}

					Intent intent = new Intent();
					intent.putExtra("date", (Serializable) list);
					setResult(RESULT_OK, intent);
					AppManager.getAppManager().finishActivity(
							DateSelectAty.this);
				}
			}
		});

		title = (TextView) findViewById(R.id.title);
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setCanceledOnTouchOutside(false);
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				Message msg = Message.obtain();
				msg.what = 0;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			calendarView = (CalendarView) findViewById(R.id.calendar);
			calendarView.init();
			DateBean d = calendarView.getDateInit();
			title.setText(getString(R.string.date_year_month_day,
					d.getSolar()[0], d.getSolar()[1], d.getSolar()[2]));

			calendarView
					.setOnMonthItemChooseListener(new OnMonthItemChooseListener() {
						@Override
						public void onMonthItemChoose(View view, DateBean date,
								boolean flag) {
							/*title.setText(getString(
									R.string.date_year_month_day,
									date.getSolar()[0], date.getSolar()[1],
									date.getSolar()[2]));*/
						}
					});
			calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
				@Override
				public void onPagerChanged(int[] date) {
					title.setText(getString(R.string.date_year_month_day,
							date[0], date[1], date[2]));
				}
			});
			calendarView.setOnItemClickListener(new OnMonthItemClickListener() {
				@Override
				public void onMonthItemClick(View view, DateBean date) {
					title.setText(getString(R.string.date_year_month_day,
							date.getSolar()[0], date.getSolar()[1],
							date.getSolar()[2]));
				}
			});
			if (loading != null)
				loading.dismiss();
		}
	};

	private String transform(int sum) {
		return sum < 10 ? "0" + sum : sum + "";
	}

	// 上一月
	public void lastMonth(View view) {
		calendarView.lastMonth();
	}

	// 下一月
	public void nextMonth(View view) {
		calendarView.nextMonth();
	}
}
