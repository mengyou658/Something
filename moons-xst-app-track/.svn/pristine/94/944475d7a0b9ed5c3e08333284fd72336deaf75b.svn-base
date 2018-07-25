/**
 * 
 */
package com.moons.xst.track.ui;

import java.util.List;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moons.xst.track.R;
import com.moons.xst.track.widget.MoonsBaseDialog;

/**
 * 下载工具
 * 
 * @author LKZ
 * 
 */
public class Tool_Download extends MoonsBaseDialog {

	private Context mcontext;
	private TextView titleTextView, descTextView;
	private EditText urlEditText;
	private ProgressBar progressBar;
	private Button OkButton;

	private List<List<String>> mData;

	public Tool_Download(Context context) {
		super(context);
		this.setCancelable(false);
		setContentView(R.layout.simple_radio_listview);
		mcontext = context;
		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		titleTextView = (TextView) findViewById(R.id.tool_download_title);
		descTextView = (TextView) findViewById(R.id.tool_download_mes);
		urlEditText = (EditText) findViewById(R.id.tool_download_url);
		urlEditText.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO 长按扫描二维码录入
				return false;
			}
		});
		progressBar = (ProgressBar) findViewById(R.id.tool_download_progressBar);
		OkButton = (Button) findViewById(R.id.btn_simple_radio_dialog_ok);
		OkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			}
		});
	}
}
