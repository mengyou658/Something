package com.moons.xst.track.widget.PieChart.piechartview;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.track.R;
import com.moons.xst.track.widget.PieChart.custom.view.CountView;

/**
 * 统计页面
 * 
 * @author tz
 * 
 */
public class StatisticsView extends ViewGroup implements OnClickListener {

	private Context context;

	/** 子View */
	private View view;

	private TextView mLast, mCurrent, mNext, mStatisticsTitle;
	private CountView txtCount;

	/** 保存当前显示的上个月、本月和下个月的月份 几当前年份 */
	private int mLastDate, mCurrDate, mNextDate, mYear, mDay;

	private int mMaxMonth, mMaxYear, mMinMonth, mMinYear;

	private String startDate, endDate;

	private OnDateChangedLinstener mDateChangedListener;

	private PieChartView pieChart;
	private String[] colors = {"#8A2BE2","#FFD700", "#7CFC00","#FF6347","#DA70D6" };
	private float[] items;
	private TextView txtView3;
	private TextView tv_item_desc1,tv_item_desc2,tv_item_value1,tv_item_value2;
	private float animSpeed = 8f;
	private int total;
	// 每块扇形代表的类型
	private ConcurrentHashMap<Integer,String[]> type;
	
	private boolean mBaseInfoVisible = true;
	
	private RelativeLayout infoLayout,layoutBaseInfo;
	private LinearLayout layout_title;

	public StatisticsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StatisticsView(Context context, float[] items, int total,
			boolean baseInfoVisible,  
			ConcurrentHashMap<Integer,String[]> type) {
		super(context);
		this.context = context;
		this.items = items;
		this.total = total;
		this.type = type;
		this.mBaseInfoVisible = baseInfoVisible;
		initView();
	}

	private void initView() {

		view = LayoutInflater.from(context).inflate(
				R.layout.statistics_layout, null);
		
		txtCount = (CountView)view.findViewById(R.id.txtCount);
		infoLayout = (RelativeLayout)view.findViewById(R.id.infoLayout);
		layoutBaseInfo = (RelativeLayout)view.findViewById(R.id.layoutBaseInfo);
		txtView3 = (TextView) view.findViewById(R.id.txtView3);
		tv_item_desc1 = (TextView) view.findViewById(R.id.tv_item_desc1);
		tv_item_desc2 = (TextView) view.findViewById(R.id.tv_item_desc2);
		tv_item_value1 = (TextView) view.findViewById(R.id.tv_item_value1);
		tv_item_value2 = (TextView) view.findViewById(R.id.tv_item_value2);
		
		layout_title = (LinearLayout)view.findViewById(R.id.layout_title);

		mLast = (TextView) view.findViewById(R.id.last);
		mCurrent = (TextView) view.findViewById(R.id.curr);
		mNext = (TextView) view.findViewById(R.id.next);
		mLast.setOnClickListener(this);
		mCurrent.setOnClickListener(this);
		mNext.setOnClickListener(this);
		mStatisticsTitle = (TextView) view.findViewById(R.id.statisticstitle);
		
		
		intitPieChart();
		this.addView(view);
		
//		initDate();
	}
	
	public int getBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
            sbar = 50;
        }
        return sbar;
    }
	
	private int getExtraHeight(){
		
		layout_title.measure(0,0);
		int titleHeight = layout_title.getMeasuredHeight();
		int barHeight = getBarHeight();
		
		return titleHeight+barHeight;
	}

	private void intitPieChart() {

		//textInfo = (TextView) view.findViewById(R.id.text_item_info);
		pieChart = (PieChartView) view.findViewById(R.id.parbar_view);

		//pieChart.setInnerCircle(1.6f);
		pieChart.setAnimEnabled(true);// 是否开启动画
		pieChart.setItemsColors(colors);// 设置各个块的颜色
		pieChart.setItemsSizes(items);// 设置各个块的值
		pieChart.setRotateSpeed(animSpeed);// 设置旋转速度
		pieChart.setTotal(total);
		pieChart.setActualTotal(total);
		pieChart.setExtraHeight(getExtraHeight());
		DisplayMetrics dm = getResources().getDisplayMetrics();
		pieChart.setRaduis((int) (dm.widthPixels / 2.5));// 设置饼状图半径
		pieChart.setOnItemSelectedListener(new OnPieChartItemSelectedLinstener() {
			public void onPieChartItemSelected(PieChartView view, int position,
					String colorRgb, float size, float rate,
					boolean isFreePart, float rotateTime) {

				try {
					txtView3.setTextColor(Color.parseColor(colorRgb));
					if (isFreePart) {
						// textInfo.setText("多余的部分" + position +
						// "\r\nitem size: "
						// + size + "\r\nitem color: " + colorRgb
						// + "\r\nitem rate: " + rate + "\r\nrotateTime : "
						// + rotateTime);
					} else {
						float percent = (float) (Math.round(size * 100)) / 100;
						
						String[] res = type.get(position);
						
						txtView3.setText(type.get(position)[0].toString());
												
						if (mBaseInfoVisible) {
							layoutBaseInfo.setVisibility(View.VISIBLE);
							tv_item_desc1.setText(type.get(position)[1].toString());
							tv_item_value1.setText(type.get(position)[2].toString());
							
							tv_item_desc2.setText(type.get(position)[3].toString());
							tv_item_value2.setText(type.get(position)[4].toString());
							
							ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
			                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
			                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
			                scaleAnimation.setDuration(800);
			                scaleAnimation.setFillAfter(true);
			                scaleAnimation.setInterpolator(new BounceInterpolator());
			                layoutBaseInfo.startAnimation(scaleAnimation);
						} else {
							layoutBaseInfo.setVisibility(View.GONE);
						}
					}
					if (total > 0)
						infoLayout.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onTriggerClicked() {
				Toast.makeText(context, "点击了切换按钮!",
						Toast.LENGTH_SHORT).show();
			}

		});
		pieChart.setPieChartSlideLinstener(new OnPieChartSlideLinstener() {
			
			@Override
			public void OnPieChartSlide() {
				// TODO Auto-generated method stub
				infoLayout.setVisibility(View.GONE);
			}
		});
		pieChart.setShowItem(0, true, true);// 设置显示的块

	}

	/**
	 * 初始化日期
	 */
	private void initDate() {
		Calendar c = Calendar.getInstance();
		mMaxYear = mYear = c.get(Calendar.YEAR);
		mMinMonth = mMaxMonth = mCurrDate = c.get(Calendar.MONTH) + 1;
		mLastDate = mCurrDate - 1;
		mNextDate = mCurrDate + 1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mMinYear = mMaxYear - 1;
		freshDate();
		
//		txtCount.setNumber(3870.30f);
//		txtCount.showNumberWithAnimation(3870.30f);
	}

	/**
	 * 设置当前日期
	 * 
	 * @param year
	 * @param month
	 */
	public void setCurrDate(int year, int month) {
		mMaxYear = mYear = year;
		mMinMonth = mMaxMonth = mCurrDate = month;
		mNextDate = mCurrDate + 1;
		mLastDate = mCurrDate - 1;
		mMinYear = mMaxYear - 1;
		freshDate();
	}
	
	/**
	 * 设置自定义内容
	 * @param str
	 */
	public void setCurrText(String str) {
		mCurrent.setText(str);
	}
	
	public void setCurrCount(int count) {
		txtCount.setNumber(count);
		txtCount.showNumberWithAnimation(count);
	}
	
	public void setStatisticsTitle(String str) {
		mStatisticsTitle.setText(str);
	}

	/**
	 * 设置日期范围
	 * 
	 * @param mMaxMonth
	 * @param mMaxYear
	 * @param mMinMonth
	 * @param mMinYear
	 */
	public void setDateRange(int mMaxMonth, int mMaxYear, int mMinMonth,
			int mMinYear) {
		this.mMaxMonth = mMaxMonth;
		this.mMaxYear = mMaxYear;
		this.mMinMonth = mMinMonth;
		this.mMinYear = mMinYear;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child = getChildAt(0);
		child.layout(l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(measureWidth, measureHeigth);
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			int widthSpec = 0;
			int heightSpec = 0;
			LayoutParams params = v.getLayoutParams();
			if (params.width > 0) {
				widthSpec = MeasureSpec.makeMeasureSpec(params.width,
						MeasureSpec.EXACTLY);
			} else if (params.width == -1) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.EXACTLY);
			} else if (params.width == -2) {
				widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
						MeasureSpec.AT_MOST);
			}

			if (params.height > 0) {
				heightSpec = MeasureSpec.makeMeasureSpec(params.height,
						MeasureSpec.EXACTLY);
			} else if (params.height == -1) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.EXACTLY);
			} else if (params.height == -2) {
				heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth,
						MeasureSpec.AT_MOST);
			}
			v.measure(widthSpec, heightSpec);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.last:
			if (mDateChangedListener != null) {
				if (mMinYear >= mYear && mLastDate < mMinMonth) {
					Toast.makeText(context, "只能查询一年内的数据哦!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (mLastDate == 1) {
					mLastDate = 12;
					mCurrDate--;
					mNextDate--;
				} else if (mLastDate == 12) {
					mLastDate--;
					mCurrDate = 12;
					mNextDate--;
					mYear--;
				} else if (mLastDate == 11) {
					mLastDate--;
					mCurrDate--;
					mNextDate = 12;
				} else {
					mLastDate--;
					mCurrDate--;
					mNextDate--;
				}

				freshDate();

				String startDate = mYear + "-" + mCurrDate + "-" + "1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-"
						+ "1 00:00:00";

				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;

		case R.id.next:
			if (mDateChangedListener != null) {

				if (mMaxYear == mYear && mNextDate > mMaxMonth) {
					Toast.makeText(context, "还没有这个月的数据哦!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (mNextDate == 12) {
					mLastDate++;
					mCurrDate++;
					mNextDate = 1;
				} else if (mNextDate == 1) {
					mLastDate++;
					mCurrDate = 1;
					mNextDate++;
					mYear++;
				} else if (mNextDate == 2) {
					mLastDate = 1;
					mCurrDate++;
					mNextDate++;
				} else {
					mLastDate++;
					mCurrDate++;
					mNextDate++;
				}
				freshDate();

				String startDate = mYear + "-" + mCurrDate + "-1 00:00:00";
				String endDate = mYear + "-" + (mCurrDate + 1) + "-1 00:00:00";
				mDateChangedListener.onDateChanged(startDate, endDate);

			}
			break;
		default:
			break;
		}
	}

	public void freshDate() {
		mLast.setText(mLastDate + "月");
		mCurrent.setText(mYear + "年" + mCurrDate + "月");
		mNext.setText(mNextDate + "月");
	}

	public float[] getItems() {
		return items;
	}

	public void setItems(float[] items) {
		this.items = items;
		pieChart.setItemsSizes(items);
	}

	public void freshView() {
		pieChart.setShowItem(0, true, true);// 设置显示的块
		pieChart.invalidate();
		this.invalidate();
	}

	public void relaseTotal() {
		pieChart.relaseTotal(0);
	}

	public OnDateChangedLinstener getDateChangedListener() {
		return mDateChangedListener;
	}

	public void setDateChangedListener(
			OnDateChangedLinstener mDateChangedListener) {
		this.mDateChangedListener = mDateChangedListener;
	}

	public ConcurrentHashMap<Integer,String[]> getType() {
		return type;
	}

	public void setType(ConcurrentHashMap<Integer,String[]> type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
