package com.moons.xst.track.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;

public class PowerSaveSettingAty extends BaseActivity {
	
	@Bind(R.id.home_head_Rebutton) ImageButton returnButton;
	
	@Bind(R.id.setting_powersave_setting_wifi_state) TextView wifistate;
	@Bind(R.id.setting_powersave_setting_gps_state) TextView gpsstate;
	
	@Bind(R.id.setting_powersave_setting_wifi_checked) CheckBox wifichk;
	@Bind(R.id.setting_powersave_setting_gps_checked) CheckBox gpschk;
	
	private AppContext appContext;// 全局Context
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_powersave_setting);
		
		ButterKnife.bind(this);
		
		appContext = (AppContext) getApplication();
		
		wifichk.setChecked(appContext.isWifiClose());
		int statewifi = appContext.isWifiClose() == true ? R.string.setting_powersave_setting_on :
			R.string.setting_powersave_setting_off;
		wifistate.setText(statewifi);
		
		gpschk.setChecked(appContext.isGPSClose());
		int stategps = appContext.isGPSClose() == true ? R.string.setting_powersave_setting_on :
			R.string.setting_powersave_setting_off;
		gpsstate.setText(stategps);
	}
	
	/**
	 * WIFI是否关闭
	 */
	@OnClick(R.id.setting_powersave_setting_wifi_checked)
	public void wifichkOnChecked() {
		boolean b = appContext.isWifiClose();
		appContext.setWifiClose(!b);
		int statewifi = appContext.isWifiClose() == true ? R.string.setting_powersave_setting_on :
			R.string.setting_powersave_setting_off;
		wifistate.setText(statewifi);
	}
	
	/**
	 * GPS是否关闭
	 */
	@OnClick(R.id.setting_powersave_setting_gps_checked)
	public void gpschkOnChecked() {
		boolean b = appContext.isGPSClose();
		appContext.setGPSClose(!b);
		int stategps = appContext.isGPSClose() == true ? R.string.setting_powersave_setting_on :
			R.string.setting_powersave_setting_off;
		gpsstate.setText(stategps);
	}
	
	@OnClick(R.id.home_head_Rebutton)
	public void rebuttonOnClick() {
		AppManager.getAppManager().finishActivity(PowerSaveSettingAty.this);
	}
}