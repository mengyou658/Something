package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moons.xst.buss.DJIDPosHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.DJQuerydataAdapter;
import com.moons.xst.track.bean.DJLine;
import com.moons.xst.track.bean.DJ_IDPos;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.widget.pullToRefresh.PullToRefreshSwipeMenuListView;
import com.moons.xst.track.widget.pullToRefresh.pulltorefresh.interfaces.IXListViewListener;

public class DJQueryData extends BaseActivity implements IXListViewListener {

	private String LTAG = InitCheckPoint.class.getName();

	private TextView titleText;
	private TextView lineText;
	private TextView allModeTV;
	private TextView placeModeTV;
	private TextView unplaceModeTV;

	private ImageButton returnButton;
	private PullToRefreshSwipeMenuListView pointListView;
	private ImageView lineTypeImageView;
	private RelativeLayout linell;

	private int djLineID;
	private String djLineName;
	private DJLine djline;

	private int offset = 0;

	private DJIDPosHelper djidPosHelper;

	private List<DJ_IDPos> idposData = new ArrayList<DJ_IDPos>();
	private DJQuerydataAdapter dataAdapter;
	private LoadingDialog loading;

	private List<DJ_IDPos> initIDposData = new ArrayList<DJ_IDPos>();
	private boolean isLoadMore = false;
	private boolean isFirstLoad = true;
	private ExecutorService singleThreadExecutor;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (loading != null)
				loading.dismiss();
			if (msg.what == 1) {
				if (isFirstLoad) {
					initIDposData = idposData;
					dataAdapter = new DJQuerydataAdapter(DJQueryData.this,
							initIDposData, String.valueOf(djLineID),
							R.layout.listitem_query_data);
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
				UIHelper.ToastMessage(DJQueryData.this, R.string.query_nullmsg);
			} else if (msg.what == -1) {
				UIHelper.ToastMessage(DJQueryData.this,
						((Exception) (msg.obj)).getMessage());
			} else if (msg.what == 2) {
				//数据加载完毕，再次上拉执行的操作 
//				pointListView.stopLoadMore();
				pointListView.setState();

			}
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_data_new);
		init();
		djline = (DJLine) (getIntent().getSerializableExtra("djlineinfo"));
		djLineID = djline.getLineID();
		// djLineID = Integer.parseInt(getIntent().getStringExtra("line_id"));
		AppContext.SetCurrLineID(djLineID);
		djLineName = djline.getLineName();
		// djLineName = getIntent().getStringExtra("line_name");
		lineText.setText(djLineName);
		titleText.setText(R.string.djquery_data_title);
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
		singleThreadExecutor = Executors.newSingleThreadExecutor();

		linell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// UIHelper.enterMyWork(v.getContext(), djline);
			}
		});

		loadIDPostoListView();

		pointListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView aTitle = (TextView) view
						.findViewById(R.id.query_data_listitem_title);
				if (aTitle == null) {
					return;
				}
				final DJ_IDPos idPos = (DJ_IDPos) aTitle.getTag();

				if (idPos == null)
					return;
				UIHelper.showDJQueryPoint(DJQueryData.this, djline,
						idPos.getPlace_TX(), idPos.getIDPos_ID());
				// UIHelper.showDJQueryPoint(DJQueryData.this,String.valueOf(djLineID)
				// ,djLineName,
				// idPos.getPlace_TX(),idPos.getIDPos_ID());
			}
		});

		pointListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				TextView aTitle = (TextView) arg1
						.findViewById(R.id.query_data_listitem_title);
				if (aTitle == null) {
					return false;
				}
				final DJ_IDPos idPos = (DJ_IDPos) aTitle.getTag();

				if (idPos == null)
					return false;

				UIHelper.showQueryDataIDPosHisResult(DJQueryData.this,
						String.valueOf(AppContext.GetCurrLineID()),
						idPos.getIDPos_ID(), idPos.getPlace_TX());
				return true;
			}
		});
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(DJQueryData.this);
			}
		});
	}

	private void loadIDPostoListView() {

		loadIDPosThread();
	}

	private synchronized void loadIDPosThread() {
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
					if (AppContext.DJIDPosStatusDataBuffer.size() >= offset * 10) {

						idposData = djidPosHelper.loadDJPosData(
								DJQueryData.this, isFirstLoad, djLineID,
								offset, 10);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BREAK) {
			AppManager.getAppManager().finishActivity(this);
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		titleText = (TextView) findViewById(R.id.query_data_head_title);
		lineText = (TextView) findViewById(R.id.query_data_line_Value);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		// allModeTV = (TextView) findViewById(R.id.all_tab);
		// placeModeTV = (TextView) findViewById(R.id.place_tab);
		// unplaceModeTV = (TextView) findViewById(R.id.Unplace_tab);
		pointListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.query_data_listview);
		pointListView.setPullLoadEnable(true);
		pointListView.setPullRefreshEnable(false);
		pointListView.setXListViewListener(this);
		lineTypeImageView = (ImageView) findViewById(R.id.djline_type_icon);
		linell = (RelativeLayout) findViewById(R.id.ll_query_data_line);
		djidPosHelper = new DJIDPosHelper();
	}

	@Override
	public void onRefresh() {
		// 下拉刷新
	}

	boolean isLoading = false;

	@Override
	public void onLoadMore() {
		// 上拉加载
		if (isLoadMore) {
			if (pointListView.getLastVisiblePosition() == (pointListView
					.getCount() - 1)) {
				if (!isLoading) {
					isLoading = true;
					pointListView.setSelection(dataAdapter.getCount() - 1);
					loadIDPosThread();
				}
			} else {
				isLoading = false;
				pointListView.stopLoadMore();
			}
		} else {
			pointListView.setState();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (AppContext.DJIDPosStatusDataBuffer.size() > 0) {
			 AppContext.DJIDPosStatusDataBuffer.clear();
			singleThreadExecutor.shutdown();
		}
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
				AppManager.getAppManager().finishActivity(DJQueryData.this);
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
