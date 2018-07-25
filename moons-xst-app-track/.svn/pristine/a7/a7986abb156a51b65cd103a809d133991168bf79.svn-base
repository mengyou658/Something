package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.CommonAdapter;

public class SystemServiceAty extends BaseActivity{
	
	private ImageButton returnButton;
	private ListView lv_service;
	private CommonAdapter mAdapter;
	private List<String> mData = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_service);
			
		getRunningServicesInfo(SystemServiceAty.this);
		initData();
	}
	
	private void getRunningServicesInfo(Context context) {
		
		mData.clear();
        final ActivityManager activityManager = (ActivityManager) context
                        .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> services = activityManager.getRunningServices(100);

        Iterator<RunningServiceInfo> l = services.iterator();
        while (l.hasNext()) {
                RunningServiceInfo si = (RunningServiceInfo) l.next();
                if (si.process.contains("moons")) {
                	int start = si.service.toString().indexOf("/");
                	int end = si.service.toString().indexOf("}");
	                mData.add(si.service.toString().substring(start + 1, end));
                }
        }
	}
	
	private void initData() {
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(SystemServiceAty.this);
			}
		});
		lv_service = (ListView) findViewById(R.id.listview_system_service);
		lv_service.setAdapter(mAdapter = new CommonAdapter<String>(
        		SystemServiceAty.this, mData, R.layout.listitem_system_service){

			@Override
			public void convert(com.moons.xst.track.adapter.ViewHolder helper,
					String serviceName) {
				// TODO 自动生成的方法存根
				helper.setText(R.id.listitem_systemservice_itemdesc, serviceName);
			}  
        });	
	}
}