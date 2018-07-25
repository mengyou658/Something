/**
 * 
 */
package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.moons.xst.buss.GPSPositionHelper;
import com.moons.xst.buss.TrackHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.TrackinitAdapter;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.GPSPositionForInit;
import com.moons.xst.track.common.GPSHelper;
import com.moons.xst.track.common.InputTools;
import com.moons.xst.track.common.LogUtils;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

/**
 * @author lkz
 * 
 */
public class InitCheckPoint extends BaseActivity {
	private String LTAG = InitCheckPoint.class.getName();

	private TextView gpsInfoJD;
	private TextView gpsInfoWD;
	private TextView titleText;
	private TextView pgsState;
	private TextView totaltv, uninittv;
	private ImageButton returnButton;
	private EditText etcontent;
	private ImageView serachButton, ivdelete;

	private ListView init_cpListView;
	private TrackinitAdapter adapter;

	private TrackHelper trackHelper;
	private GPSPositionHelper gpspohelper;

	private RelativeLayout rl_all, rl_uninit, rl_search, rlcancel;

	private List<CheckPointInfo> querylist = new ArrayList<CheckPointInfo>();
	private String MODE = "ALL";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_initcheckpoint);
		initTextViews();
		init();
		// updateView(AppContext.getCurrLocation());
		refreshList();
		init_cpListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				CheckPointInfo cpinfo = null;
				TextView aTitle = (TextView) arg1
						.findViewById(R.id.trackinit_listitem_title);
				cpinfo = (CheckPointInfo) aTitle.getTag();
				AffirmInit(cpinfo);
			}
		});
		LogUtils.WriteLog(LTAG, AppContext.GetCurrLineID());
		registerBoradcastReceiver();
		rl_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				changetabstyle("ALL");
				refreshList();
			}
		});
		rl_uninit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				changetabstyle("UNINIT");
				refreshUinitList();
			}
		});
	}

	/**
	 * 确认初始化信息
	 */
	private void AffirmInit(final CheckPointInfo Point) {
		String infoString = "";
		if (AppContext.getCurrLocation() == null
				|| AppContext.getCurrLocation().getLatitude() <= 0
				|| AppContext.getCurrLocation().getLongitude() <= 0) {
			infoString = InitCheckPoint.this
					.getString(R.string.track_confirm_init_desc4);
		} else {
			infoString = InitCheckPoint.this.getString(
					R.string.track_confirm_init_desc1).replace("%s",
					Point.getDesc_TX());
			// + InitCheckPoint.this.getString(
			// R.string.track_confirm_init_desc2).replace(
			// "%f",
			// (String.valueOf(AppContext.getCurrLocation()
			// .getLongitude()) + "00000000").substring(0,
			// 9))
			// + InitCheckPoint.this.getString(
			// R.string.track_confirm_init_desc3).replace(
			// "%g",
			// (String.valueOf(AppContext.getCurrLocation()
			// .getLatitude()) + "00000000").substring(0,
			// 9));
		}
		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.track_initcp_title))
				.setView(view)
				.setMsg(infoString)
				.setPositiveButton(getString(R.string.sure),
						new OnClickListener() {
							@Override
							public void onClick(View v) {

								try {
									if (AppContext.getCurrLocation() == null
											|| AppContext.getCurrLocation()
													.getLatitude() <= 0
											|| AppContext.getCurrLocation()
													.getLongitude() <= 0) {
										return;
									}
									if (updateCheckPo(Point)
											&& insertGPSInit(Point))
										// refreshList();
										UIHelper.ToastMessage(
												InitCheckPoint.this,
												R.string.initcp_message_savesucceed);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									UIHelper.ToastMessage(
											InitCheckPoint.this,
											R.string.initcp_message_savedefeated
													+ e.getMessage());
								}
								if (MODE.equals("ALL")) {
									changetabstyle("ALL");
									refreshList();
								} else if (MODE.equals("UNINIT")) {
									changetabstyle("UNINIT");
									refreshUinitList();
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

	}

	/**
	 * 更新考核点
	 * 
	 */
	private boolean updateCheckPo(CheckPointInfo Point) {
		int pos = _indexOf(AppContext.trackCheckPointBuffer,
				Point.getGPSPosition_ID());
		String lat = gpsInfoWD.getText().toString();
		String lot = gpsInfoJD.getText().toString();
		if (StringUtils.isEmpty(lat) || StringUtils.isEmpty(lot)) {
			pgsState.setText(R.string.mapmain_message_locationunderway);
			return false;
		}

		LatLng desLatLng = GPSHelper.convertBaiduToGPS(new LatLng(Double
				.parseDouble(lat), Double.parseDouble(lot)));
		AppContext.trackCheckPointBuffer.get(pos).setLatitude(
				String.valueOf(desLatLng.latitude));
		AppContext.trackCheckPointBuffer.get(pos).setLongitude(
				String.valueOf(desLatLng.longitude));
		CheckPointInfo PointInfo = AppContext.trackCheckPointBuffer.get(pos);
		trackHelper.updateCheckPointData(this, PointInfo);
		return true;
	}

	/**
	 * 插入初始化库
	 * 
	 */
	private boolean insertGPSInit(CheckPointInfo Point) {
		int pos = _indexOf(AppContext.trackCheckPointBuffer,
				Point.getGPSPosition_ID());
		GPSPositionForInit gpsinit = new GPSPositionForInit();
		gpsinit.setGPSPosition_ID(Point.getGPSPosition_ID());
		gpsinit.setGPSPosition_CD(Point.getGPSPosition_CD());
		gpsinit.setXJQMark(AppContext.GetxjqCD());
		gpsinit.setLongitude(AppContext.trackCheckPointBuffer.get(pos)
				.getLongitude());
		gpsinit.setLatitude(AppContext.trackCheckPointBuffer.get(pos)
				.getLatitude());
		gpsinit.setUTCDateTime(Point.getUTCDateTime());
		gpsinit.setGPSDesc(Point.getDesc_TX());
		gpsinit.setLineID(String.valueOf(AppContext.GetCurrLineID()));
		if (StringUtils.isEmpty(gpsinit.getLatitude())
				|| StringUtils.isEmpty(gpsinit.getLongitude())) {
			UIHelper.ToastMessage(getApplication(),
					R.string.initcp_message_insertdefeatedhint);
			return false;
		}
		gpspohelper.InsertGPSInit(this, gpsinit);
		return true;
	}

	/**
	 * 刷新列表
	 */
	private void refreshList() {
		querylist.clear();
		for (int i = 0; i < AppContext.trackCheckPointBuffer.size(); i++) {
			querylist.add(AppContext.trackCheckPointBuffer.get(i));
		}
		adapter = new TrackinitAdapter(this, querylist,
				R.layout.listitem_trackinit);
		init_cpListView.setAdapter(adapter);
	}

	/**
	 * 刷新未初始化列表
	 */
	private void refreshUinitList() {
		querylist.clear();
		for (int i = 0; i < AppContext.trackCheckPointBuffer.size(); i++) {
			if (AppContext.trackCheckPointBuffer.get(i).getLatitude()
					.toString() == null
					|| AppContext.trackCheckPointBuffer.get(i).getLatitude()
							.toString().compareTo("0") == 0)
				querylist.add(AppContext.trackCheckPointBuffer.get(i));
		}
		adapter = new TrackinitAdapter(InitCheckPoint.this, querylist,
				R.layout.listitem_trackinit);
		init_cpListView.setAdapter(adapter);
	}

	private int _indexOf(List<CheckPointInfo> arrayList, int ID) {
		int index = -1;
		if (arrayList == null || arrayList.isEmpty()) {
			return index;
		}
		Iterator<CheckPointInfo> iter = arrayList.iterator();
		while (iter.hasNext()) {
			index++;
			if (iter.next().getGPSPosition_ID() == ID) {
				return index;
			}
		}
		index = -1;
		return index;
	}

	/**
	 * 初始化textview
	 */
	private void initTextViews() {
		titleText = (TextView) findViewById(R.id.track_initcheckpoint_head_title);
		titleText.setText(R.string.track_initcp_title);
		gpsInfoJD = (TextView) findViewById(R.id.track_initcheckpoint_Longitude_Value);
		gpsInfoWD = (TextView) findViewById(R.id.track_initcheckpoint_Latitude_Value);
		pgsState = (TextView) findViewById(R.id.track_initcheckpoint_State_Value);
		pgsState.setText(R.string.mapmain_message_locationunderway);
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(InitCheckPoint.this);
			}
		});
		serachButton = (ImageView) findViewById(R.id.iv_search_pic);
		serachButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				rl_search.setVisibility(View.VISIBLE);
				etcontent.requestFocus();
				InputTools.ShowKeyboard(etcontent);
			}
		});
		etcontent = (EditText) findViewById(R.id.search_content);
		etcontent.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputTools.HideKeyboard(etcontent);
				}
				return false;
			}
		});

		etcontent.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivdelete.setVisibility(View.GONE);
				} else {
					ivdelete.setVisibility(View.VISIBLE);
				}
				mHandler.post(eChanged);
			}
		});
		ivdelete = (ImageView) findViewById(R.id.delete);
		ivdelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				etcontent.setText("");
			}
		});
	}

	/**
	 * 初始化
	 */
	private void init() {
		init_cpListView = (ListView) findViewById(R.id.comm_download_listview_status);
		rl_all = (RelativeLayout) findViewById(R.id.rl_total);
		rl_uninit = (RelativeLayout) findViewById(R.id.rl_uninit);
		rl_search = (RelativeLayout) findViewById(R.id.rl_search_initcheckpoint);
		rl_search.setVisibility(View.GONE);
		rlcancel = (RelativeLayout) findViewById(R.id.rl_search_cancel);
		rlcancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				rl_search.setVisibility(View.GONE);
				etcontent.setText("");
				InputTools.HideKeyboard(etcontent);
			}
		});
		totaltv = (TextView) findViewById(R.id.tv_total);
		uninittv = (TextView) findViewById(R.id.tv_uninit);
		trackHelper = new TrackHelper();
		gpspohelper = new GPSPositionHelper();
	}

	/**
	 * 实时更新文本内容
	 * 
	 * @param bdLocation
	 */
	private void updateView(BDLocation bdLocation) {
		if (bdLocation != null) {
			int locType = bdLocation.getLocType();
			if (locType == BDLocation.TypeGpsLocation) {
				gpsInfoJD
						.setText((String.valueOf(bdLocation.getLongitude()) + "00000000")
								.substring(0, 9));
				gpsInfoWD
						.setText((String.valueOf(bdLocation.getLatitude()) + "00000000")
								.substring(0, 9));
				pgsState.setText(getString(R.string.initcp_message_locationsucceedhint)
						+ bdLocation.getSatelliteNumber());
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				gpsInfoJD
						.setText((String.valueOf(bdLocation.getLongitude()) + "00000000")
								.substring(0, 9));
				gpsInfoWD
						.setText((String.valueOf(bdLocation.getLatitude()) + "00000000")
								.substring(0, 9));
				pgsState.setText(R.string.initcp_message_locationsucceed);
			} else {
				gpsInfoJD.setText("");
				gpsInfoWD.setText("");
				pgsState.setText(R.string.mapmain_message_locationunderway);
			}
		} else {
			gpsInfoJD.setText("");
			gpsInfoWD.setText("");
			pgsState.setText(R.string.mapmain_message_locationunderway);
		}
	}

	@Override
	protected void onResume() {
		LogUtils.WriteLog(LTAG, AppContext.GetCurrLineID());
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.xst.track.service.updataCurrentLoction")) {
				updateView(AppContext.getCurrLocation());
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.xst.track.service.gpsInfo");
		myIntentFilter.addAction("com.xst.track.service.updataCurrentLoction");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	// @Override
	// public boolean onQueryTextChange(String newText) {
	// // TODO Auto-generated method stub
	// btnTab.setImageDrawable(getResources().getDrawable(R.drawable.inittab3));
	// MODE = "ALL";
	// updateLayout(searchItem(newText));
	// return false;
	// }
	//
	// @Override
	// public boolean onQueryTextSubmit(String query) {
	// // TODO Auto-generated method stub
	// return false;
	// }

	private void updateLayout(List<CheckPointInfo> list) {
		adapter = new TrackinitAdapter(this, list, R.layout.listitem_trackinit);
		init_cpListView.setAdapter(adapter);
	}

	public List<CheckPointInfo> searchItem(String name) {
		List<CheckPointInfo> querylist = new ArrayList<CheckPointInfo>();
		for (int i = 0; i < AppContext.trackCheckPointBuffer.size(); i++) {
			int index = AppContext.trackCheckPointBuffer.get(i).getDesc_TX()
					.indexOf(name);
			// 存在匹配的数据
			if (index != -1) {
				querylist.add(AppContext.trackCheckPointBuffer.get(i));
			}
		}
		return querylist;
	}

	static Handler mHandler = new Handler();

	Runnable eChanged = new Runnable() {

		@Override
		public void run() {

			// TODO Auto-generated method stub
			String data = etcontent.getText().toString();
			querylist.clear();
			getmDataSub(querylist, data);
			adapter.notifyDataSetChanged();

		}
	};

	/**
	 * 根据输入值筛选数据源
	 * 
	 * @param mDataSubs
	 * @param data
	 */
	private void getmDataSub(List<CheckPointInfo> mDataSubs, String data) {
		for (int i = 0; i < AppContext.trackCheckPointBuffer.size(); ++i) {
			if (MODE.equals("ALL")) {
				if (AppContext.trackCheckPointBuffer.get(i).getDesc_TX()
						.contains(data)) {
					mDataSubs.add(AppContext.trackCheckPointBuffer.get(i));
				}
			} else {
				if (AppContext.trackCheckPointBuffer.get(i).getLatitude()
						.toString() == null
						|| AppContext.trackCheckPointBuffer.get(i)
								.getLatitude().toString().compareTo("0") == 0) {
					if (AppContext.trackCheckPointBuffer.get(i).getDesc_TX()
							.contains(data)) {
						mDataSubs.add(AppContext.trackCheckPointBuffer.get(i));
					}
				}
			}
		}
	}

	private void changetabstyle(String mode) {
		if (mode.equals("ALL")) {
			totaltv.setBackgroundResource(R.color.face_bg);
			uninittv.setBackgroundResource(R.drawable.frame_layout_bg);
			MODE = "ALL";
		} else if (mode.equals("UNINIT")) {
			totaltv.setBackgroundResource(R.drawable.frame_layout_bg);
			uninittv.setBackgroundResource(R.color.face_bg);
			MODE = "UNINIT";
		}
	}

}
