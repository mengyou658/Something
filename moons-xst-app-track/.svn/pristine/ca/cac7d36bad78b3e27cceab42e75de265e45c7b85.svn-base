package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.QuerydataAdapter;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.pullToRefresh.PullToRefreshSwipeMenuListView;
import com.moons.xst.track.widget.pullToRefresh.pulltorefresh.interfaces.IXListViewListener;

public class QueryData extends BaseActivity implements IXListViewListener {

	private String LTAG = InitCheckPoint.class.getName();

	TextView titleText;
	TextView lineText;
	ImageButton returnButton;
	PullToRefreshSwipeMenuListView pointListView;
	ImageView lineTypeImageView;
	RelativeLayout linell;

	private int djLineID;
	private String djLineName;
	private DJLine djline;
	private int offset = 0;

	private TrackHelper trackHelper;
	private QuerydataAdapter dataAdapter;
	private boolean isLoadMore = false;
	private boolean isFirstLoad = true;
	private LoadingDialog loading;
	boolean isLoading = false;
	private ExecutorService singleThreadExecutor;
	private List<CheckPointInfo> initIDposData = new ArrayList<CheckPointInfo>();
	private List<CheckPointInfo> idposData = new ArrayList<CheckPointInfo>();
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				if (isFirstLoad) {
					initIDposData = idposData;
					dataAdapter = new QuerydataAdapter(QueryData.this,
							initIDposData, R.layout.listitem_query_data);
					pointListView.setAdapter(dataAdapter);
					isFirstLoad = false;
				} else {
					pointListView.stopLoadMore();
					initIDposData.addAll(idposData);
					dataAdapter.notifyDataSetChanged();
					isLoading = false;
				}
				offset++;
			} else if (msg.what == 0) {
				UIHelper.ToastMessage(QueryData.this, R.string.query_nullmsg);
			} else if (msg.what == -1) {
				UIHelper.ToastMessage(QueryData.this,
						((Exception) (msg.obj)).getMessage());
			} else if (msg.what == 2) {
				// 数据加载完毕，再次上拉执行的操作
				pointListView.setState();

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_data_new);
		init();
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
				AppManager.getAppManager().finishActivity(QueryData.this);
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

	/**
	 * 初始化数据
	 */
	private void init() {
		titleText = (TextView) findViewById(R.id.query_data_head_title);
		lineText = (TextView) findViewById(R.id.query_data_line_Value);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		pointListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.query_data_listview);
		lineTypeImageView = (ImageView) findViewById(R.id.djline_type_icon);
		linell = (RelativeLayout) findViewById(R.id.ll_query_data_line);

		pointListView.setPullLoadEnable(true);
		pointListView.setPullRefreshEnable(false);
		pointListView.setXListViewListener(this);
		trackHelper = new TrackHelper();
		djline = (DJLine) (getIntent().getSerializableExtra("djlineinfo"));
		djLineID = djline.getLineID();
		AppContext.SetCurrLineID(djLineID);
		djLineName = djline.getLineName();
		lineText.setText(djLineName);
		titleText.setText(R.string.query_data_title);
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
		singleThreadExecutor = Executors.newSingleThreadExecutor();
		lineTypeImageView.setImageResource(linetypePic);

		loadCheckPionttoListView();

		pointListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				return;
				/**
				 * 巡线点击考核点查询计划暂未处理，后续再加...
				 */
				// TextView aTitle = (TextView) view
				// .findViewById(R.id.query_data_listitem_title);
				// if (aTitle == null) {
				// return;
				// }
				// final CheckPointInfo cpinfo = (CheckPointInfo)
				// aTitle.getTag();
				//
				// if (cpinfo == null)
				// return;
				// UIHelper.showQueryPoint(QueryData.this, djLineName,
				// cpinfo.getDesc_TX(),
				// String.valueOf(cpinfo.getGPSPosition_ID()));
			}
		});

		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(QueryData.this);
			}
		});

	}

	private void loadCheckPionttoListView() {
		loadCheckPointThread();
	}

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onLoadMore() {
		if (isLoadMore) {
			if (pointListView.getLastVisiblePosition() == (pointListView
					.getCount() - 1)) {
				if (!isLoading) {
					isLoading = true;
					pointListView.setSelection(dataAdapter.getCount() - 1);
					loadCheckPointThread();
				}
			} else {
				isLoading = false;
				pointListView.stopLoadMore();
			}
		} else {
			pointListView.setState();
		}
	}

	private synchronized void loadCheckPointThread() {
		if (isFirstLoad) {
			loading = new LoadingDialog(this);
			loading.setCancelable(false);
			loading.show();
		}

		singleThreadExecutor.execute(new Runnable() {
			//
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					// 加载ID位置数据
					if (AppContext.CheckPointIDPosStatusDataBuffer.size() >= offset * 10) {
						idposData = trackHelper.queryCheckPointData(
								QueryData.this, isFirstLoad, djLineID, offset,
								10);
						if (idposData == null) {
							msg.what = 0;
						} else {
							msg.what = 1;
						}
						isLoadMore = true;
					} else {
						msg.what = 2;
						isLoadMore = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		});
	}
}
