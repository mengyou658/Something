package com.moons.xst.track.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;

public class VideoQualitySettingAty extends BaseActivity {
	
	private CheckBox lowqualityChk;
	private CheckBox highqualityChk;
	private ImageButton returnButton;
	
	private AppContext appContext;// 全局Context
	private static String mQuality = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_videoquality_setting);
		
		appContext = (AppContext) getApplication();
		mQuality = AppContext.getVideoQuality();
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				appContext.setConfigVideoQuality(mQuality);
				AppManager.getAppManager().finishActivity(VideoQualitySettingAty.this);
			}
		});
		
		lowqualityChk = (CheckBox) findViewById(R.id.setting_videoquality_setting_lowquality_checked);
		lowqualityChk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				lowqualityChk.setChecked(true);
				highqualityChk.setChecked(false);
				mQuality = "LOW";
			}
		});
		
		highqualityChk = (CheckBox) findViewById(R.id.setting_videoquality_setting_highquality_checked);
		highqualityChk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				lowqualityChk.setChecked(false);
				highqualityChk.setChecked(true);
				mQuality = "HIGH";
			}
		});
		
		if (AppContext.getVideoQuality().equalsIgnoreCase("LOW")) {
			lowqualityChk.setChecked(true);
		}
		else {
			highqualityChk.setChecked(true);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			appContext.setConfigVideoQuality(mQuality);
			AppManager.getAppManager().finishActivity(VideoQualitySettingAty.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}