/**
 * 
 */
package com.moons.xst.track.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;

/**
 * @author lkz
 * 
 */
public class Tool_VoiceInput extends BaseActivity {
	// 语音输入
	private TextView valueTextView;
	private ImageView voiceImageView;
	private ImageButton returnButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_input_tool);
		registerBoradcastReceiver();
		valueTextView = (TextView) findViewById(R.id.voice_input_value);
		voiceImageView = (ImageView) findViewById(R.id.voice_intput_scan_bar);
		voiceImageView.setBackgroundResource(R.drawable.voice_off);
		voiceImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (voiceImageView.getTag().equals("off")) {
					voiceImageView.setTag("on");
					voiceImageView.setBackgroundResource(R.drawable.voice_on);
					AppContext.voiceConvertService.BeginSpeaking();
				} else if (voiceImageView.getTag().equals("on")) {
					voiceImageView.setTag("off");
					voiceImageView.setBackgroundResource(R.drawable.voice_off);
				}
			}
		});
		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(Tool_VoiceInput.this);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("com.xst.track.service.voiceservice")) {
				String state = intent.getStringExtra("state");
				String voiceMsg = intent.getStringExtra("msg");
				if (voiceImageView != null && state.equals("END")) {
					voiceImageView.setTag("off");
					voiceImageView.setBackgroundResource(R.drawable.voice_off);
				} else if (voiceImageView != null && state.equals("ERR")) {
					voiceImageView.setTag("off");
					voiceImageView.setBackgroundResource(R.drawable.voice_off);
				} else if (voiceImageView != null && state.equals("RESULT")) {
					valueTextView.append(AppContext.voiceConvertService
							.EndSpeaking());
				}
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("com.xst.track.service.voiceservice");
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
}
