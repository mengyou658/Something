/**
 * Tool_GPS.java
 * @author LKZ
 * @created 2015-1-8
 */
package com.moons.xst.track.ui;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;

public class Tool_GPS extends BaseActivity {
	private TextView gpsInfoSatellites;
	private TextView gpsInfoJD;
	private TextView gpsInfoWD;
	private TextView gpsInfoDateTime;
	private EditText gpsInfoDes;
	private Button btnSaveGPSInfo;
	private ImageButton returnButton;
	private BDLocation currLocation=null;
	private static final String TAG = "GPS Services";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tool_gps);
		initTextViews();
		initButton();
		registerBoradcastReceiver();
	}

	private void initLocation() {
		currLocation = AppContext.getCurrLocation();
			updateView(currLocation);
	}

	/**
	 * 初始化textview
	 */
	private void initTextViews() {
		gpsInfoSatellites = (TextView) findViewById(R.id.tool_gps_Satellites_Value);
		gpsInfoJD = (TextView) findViewById(R.id.tool_gps_Longitude_Value);
		gpsInfoWD = (TextView) findViewById(R.id.tool_gps_Latitude_Value);
		gpsInfoDateTime = (TextView) findViewById(R.id.tool_gps_DateTime_Value);
		gpsInfoDes = (EditText) findViewById(R.id.tool_gps_TextDesValue);
	}

	/**
	 * 初始化button
	 */
	private void initButton() {
		btnSaveGPSInfo = (Button) findViewById(R.id.btn_saveGPS);
		btnSaveGPSInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveGPSInfo();

			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(Tool_GPS.this);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		updateView(null);
	}

	/**
	 * 保存GPS信息
	 */
	private void saveGPSInfo() {
		if (gpsInfoDes.getText().length() > 250) {
			UIHelper.ToastMessage(Tool_GPS.this, R.string.GPS_import_hint);
			return;
		}
		try {
			String JD = String.valueOf(gpsInfoJD.getText());
			String WD = String.valueOf(gpsInfoWD.getText());
			String SJ = String.valueOf(gpsInfoDateTime.getText());
			;
			String gpsInfoString = "<gpsInfo>";
			if (StringUtils.isEmpty(JD) || StringUtils.isEmpty(WD)) {
				UIHelper.ToastMessage(Tool_GPS.this, R.string.GPS_no_gain_location);
				return;
			}
			gpsInfoString += "<gpsInfoJD>" + JD + "</gpsInfoJD>";
			gpsInfoString += "<gpsInfoWD>" + WD + "</gpsInfoWD>";
			gpsInfoString += "<gpsInfoDateTime>" + SJ + "</gpsInfoDateTime>";
			gpsInfoString += "</gpsInfo>";
			com.moons.xst.track.common.FileUtils.SaveToFile(
					AppConst.XSTBasePath(), AppConst.GpsInfoFile,
					gpsInfoString, true);
			UIHelper.ToastMessage(Tool_GPS.this, R.string.nfc_save_success);
		} catch (IOException e) {
			e.printStackTrace();
			UIHelper.ToastMessage(Tool_GPS.this, R.string.nfc_defeated_hint + e.getMessage());
		}
	}

	/**
	 * 实时更新文本内容
	 * 
	 * @param location
	 */
	private void updateView(BDLocation location) {
		if (location != null) {
			int locType = location.getLocType();
			if (locType == BDLocation.TypeGpsLocation) {
				gpsInfoSatellites.setText(getString(R.string.GPS_satellite_number)
						+ location.getSatelliteNumber());
				gpsInfoJD
						.setText((String.valueOf(location.getLongitude()) + "00000000")
								.substring(0, 9));
				gpsInfoWD
						.setText((String.valueOf(location.getLatitude()) + "00000000")
								.substring(0, 9));
				gpsInfoDateTime.setText(location.getTime());
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				gpsInfoSatellites.setText(R.string.GPS_network_location_hint);
				gpsInfoJD
						.setText((String.valueOf(location.getLongitude()) + "00000000")
								.substring(0, 9));
				gpsInfoWD
						.setText((String.valueOf(location.getLatitude()) + "00000000")
								.substring(0, 9));
				gpsInfoDateTime.setText(location.getTime());
			} else {
				gpsInfoJD.setText("");
				gpsInfoWD.setText("");
				gpsInfoDateTime.setText("");
				gpsInfoSatellites.setText(R.string.GPS_underway_location);
			}
		} else {
			gpsInfoJD.setText("");
			gpsInfoWD.setText("");
			gpsInfoDateTime.setText("");
			gpsInfoSatellites.setText(R.string.GPS_underway_location);
		}
	}

	/**
	 * 返回查询条件
	 * 
	 * @return
	 */

	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"com.xst.track.service.updataCurrentLoction")) {
				initLocation();
			}
		}
	};

	private void registerBoradcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.xst.track.service.updataCurrentLoction");
		registerReceiver(myBroadcastReceiver, intentFilter);
	}
	public void unregisterBoradcastReceiver() {
		try {
			if (myBroadcastReceiver != null)
				this.unregisterReceiver(myBroadcastReceiver);
		} catch (IllegalArgumentException e) {
		}
	}
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.ACTION_DOWN) {
			goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	};

	private void goBack() {
		currLocation=null;
		unregisterBoradcastReceiver();
		AppManager.getAppManager().finishActivity(Tool_GPS.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		goBack();
	}
}
