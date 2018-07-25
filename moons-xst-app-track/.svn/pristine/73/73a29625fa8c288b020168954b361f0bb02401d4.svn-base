package com.moons.xst.track.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.offmap_searchAdapter;
import com.moons.xst.track.common.UIHelper;

public class Offline_Map extends BaseActivity implements MKOfflineMapListener {

	private ImageButton rebutton;
	private TextView cityview;
	private TextView downcityview;
	private ExpandableListView citylistview;

	private offmap_searchAdapter serchadapter;
	private MKOfflineMap mOffline = null;

	/**
	 * 所有的离线地图信息列表
	 */
	private ArrayList<MKOLSearchRecord> MapList = null;
	/**
	 * 已下载的离线地图信息列表
	 */
	private ArrayList<MKOLUpdateElement> localMapList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_offmap);
		mOffline = new MKOfflineMap();
		mOffline.init(this);
		initView();
		MapList = mOffline.getOfflineCityList();
		// 获取已下过的离线地图信息
		localMapList = mOffline.getAllUpdateInfo();
		if (localMapList == null) {
			localMapList = new ArrayList<MKOLUpdateElement>();
		}
		AppContext.localMapList = localMapList;
		// mOffline.searchCity(arg0)
		if (MapList != null) {
			serchadapter = new offmap_searchAdapter(this, MapList,
					R.layout.listitem_faoffmap, R.layout.listitem_offmap);
			citylistview.setAdapter(serchadapter);
			citylistview.setGroupIndicator(null);
			citylistview.setOnGroupClickListener(new OnGroupClickListener() {
				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					// TODO Auto-generated method stub
					int cityid;
					String a;
					if (MapList.get(groupPosition).childCities == null) {
						cityid = MapList.get(groupPosition).cityID;
						a = MapList.get(groupPosition).cityName;
						UIHelper.ToastMessage(Offline_Map.this, a);
						mOffline.start(cityid);
					}
					return false;
				}
			});
			citylistview.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					int cityid;
					String a;
					if (MapList.get(groupPosition).childCities == null) {
						cityid = MapList.get(groupPosition).cityID;
					} else {
						cityid = MapList.get(groupPosition).childCities
								.get(childPosition).cityID;
						a = MapList.get(groupPosition).childCities
								.get(childPosition).cityName;
						UIHelper.ToastMessage(Offline_Map.this, a);
					}
					mOffline.start(cityid);

					return false;
				}
			});
		}
	}

	private void initView() {
		rebutton = (ImageButton) findViewById(R.id.offmap_head_Rebutton);
		rebutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(Offline_Map.this);
			}
		});
		cityview = (TextView) findViewById(R.id.all_tab);
		downcityview = (TextView) findViewById(R.id.place_tab);
		citylistview = (ExpandableListView) findViewById(R.id.offmap_listview);

	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			AppContext.localMapList = mOffline.getAllUpdateInfo();
			serchadapter.notifyDataSetChanged();
			// 处理下载进度更新提示
			if (update != null) {
				// stateView.setText(String.format("%s : %d%%", update.cityName,
				// update.ratio));
				// updateView();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		default:
			break;
		}
	}
}
